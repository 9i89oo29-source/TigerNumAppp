package com.tigernum.app

import android.app.Application
import com.tigernum.app.util.security.EmulatorDetector
import com.tigernum.app.util.security.RootDetector
import com.tigernum.app.util.security.TamperDetector

class TigerNumApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        // يمكن تفعيل فحوصات الأمان هنا إذا أردت منع تشغيل التطبيق في بيئات غير آمنة
        if (isSecurityCheckEnabled()) {
            performSecurityChecks()
        }
    }

    /**
     * تنفيذ فحوصات الأمان الأساسية (الجذر، المحاكي، التلاعب).
     * إذا فشل أي فحص يمكن إنهاء التطبيق.
     */
    private fun performSecurityChecks() {
        val isRooted = RootDetector.isDeviceRooted()
        val isEmulator = EmulatorDetector.isEmulator()
        val isTampered = TamperDetector.isAppTampered(this)

        if (isRooted || isEmulator || isTampered) {
            // يمكن عرض حوار تحذيري أو إنهاء التطبيق (تطبيق تجريبي - يكتفي بتسجيل التحذير)
            // في الإصدار النهائي: يمكن استدعاء finish() أو عرض رسالة للمستخدم.
        }
    }

    private fun isSecurityCheckEnabled(): Boolean {
        // يمكن ربطها بـ BuildConfig.DEBUG أو متغير مفضل
        return true
    }

    companion object {
        lateinit var instance: TigerNumApp
            private set
    }
}
