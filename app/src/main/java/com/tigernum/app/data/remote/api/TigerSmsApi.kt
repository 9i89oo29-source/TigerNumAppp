package com.tigernum.app.data.remote.api

import com.tigernum.app.data.remote.dto.*
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TigerSmsApi {
    @GET("services")
    suspend fun getServices(
        @Query("api_key") apiKey: String
    ): TigerServicesResponse

    @POST("buy")
    suspend fun buyNumber(
        @Query("api_key") apiKey: String,
        @Query("service") serviceId: Int,
        @Query("country") countryCode: String
    ): TigerBuyNumberResponse

    @GET("sms")
    suspend fun getSms(
        @Query("api_key") apiKey: String,
        @Query("id") activationId: Int
    ): TigerSmsResponse
}
