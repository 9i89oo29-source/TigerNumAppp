package com.tigernum.app.data.local

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.provider.Settings
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Manages device identification and secure storage.
 *
 * - Uses ANDROID_ID as a unique, resettable device identifier (compliant with Google Play policies).
 * - Stores sensitive data (like API keys, tokens) using EncryptedSharedPreferences with AES-256 encryption.
 * - Provides reset functionality to allow users to clear all locally stored data.
 * - Avoids hardware identifiers (IMEI, serial number) in compliance with privacy best practices.
 */
class DeviceManager(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "tiger_num_secure_prefs"
        private const val KEY_DEVICE_ID = "device_id"
        private const val KEY_API_KEY_HERO = "api_key_hero"
        private const val KEY_API_KEY_TIGER = "api_key_tiger"
        private const val KEY_LAST_BUY_TIME = "last_buy_time"
        private const val KEY_DAILY_COUNT = "daily_count"
        private const val KEY_DAY_STAMP = "day_stamp"
    }

    // Master key for AES-256 encryption, generated once and securely stored in the Android Keystore.
    private val masterKey: MasterKey by lazy {
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    // EncryptedSharedPreferences instance – reads/writes are automatically encrypted/decrypted.
    private val securePrefs: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    /**
     * Returns a pseudo-unique device ID (ANDROID_ID).
     * This ID is a 64-bit hex string that is unique to each app-signing key, user, and device.
     * It can be reset by a factory reset, and it's Google Play compliant.
     * No IMEI or hardware identifier is used.
     */
    @SuppressLint("HardwareIds")
    fun getDeviceId(): String {
        // Check if we already cached the device ID
        val cached = securePrefs.getString(KEY_DEVICE_ID, null)
        if (!cached.isNullOrBlank()) return cached

        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            ?: "unknown_device"

        // Cache it in encrypted storage for faster retrieval
        securePrefs.edit().putString(KEY_DEVICE_ID, androidId).apply()
        return androidId
    }

    // ---- Secure API Key Storage ----

    fun saveHeroApiKey(apiKey: String) {
        securePrefs.edit().putString(KEY_API_KEY_HERO, apiKey).apply()
    }

    fun getHeroApiKey(): String? {
        return securePrefs.getString(KEY_API_KEY_HERO, null)
    }

    fun saveTigerApiKey(apiKey: String) {
        securePrefs.edit().putString(KEY_API_KEY_TIGER, apiKey).apply()
    }

    fun getTigerApiKey(): String? {
        return securePrefs.getString(KEY_API_KEY_TIGER, null)
    }

    // ---- Reset Support ----

    fun resetAllData() {
        securePrefs.edit().clear().apply()
    }

    fun deleteKey(key: String) {
        securePrefs.edit().remove(key).apply()
    }

    // ---- Utility methods for AbusePrevention (Rate Limiting) ----

    fun saveLong(key: String, value: Long) {
        securePrefs.edit().putLong(key, value).apply()
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return securePrefs.getLong(key, defaultValue)
    }

    fun saveString(key: String, value: String) {
        securePrefs.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String?): String? {
        return securePrefs.getString(key, defaultValue)
    }
}
