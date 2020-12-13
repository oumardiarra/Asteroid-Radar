package com.udacity.asteroidradar.network

import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.StringOrJsonConverterFactory
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface AsteroidService {

    @GET("neo/rest/v1/feed/")  @StringAno
    suspend fun getAsteroidList(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String
    ): String
}

interface AsteroidImageOfDayService {
    @JsonAno
    @GET("planetary/apod")
    suspend fun getAsteroidOfDay(@Query("api_key") apiKey: String): PictureOfDay
}


object Network {
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(StringOrJsonConverterFactory.create())
        .build()

    val retrofitService = retrofit.create(AsteroidService::class.java)
    val imageOfDayService = retrofit.create(AsteroidImageOfDayService::class.java)
}


internal annotation class StringAno

internal annotation class JsonAno

