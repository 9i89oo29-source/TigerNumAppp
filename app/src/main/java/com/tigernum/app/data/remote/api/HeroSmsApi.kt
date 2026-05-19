package com.tigernum.app.data.remote.api

import com.tigernum.app.data.remote.dto.*
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface HeroSmsApi {
    @GET("services")
    suspend fun getServices(
        @Query("api_key") apiKey: String
    ): HeroServicesResponse

    @POST("buy")
    suspend fun buyNumber(
        @Query("api_key") apiKey: String,
        @Query("service") serviceId: String,
        @Query("country") countryCode: String
    ): HeroBuyNumberResponse

    @GET("sms")
    suspend fun getSms(
        @Query("api_key") apiKey: String,
        @Query("activation") activationId: String
    ): HeroSmsResponse
}
