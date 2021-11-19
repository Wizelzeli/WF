package com.example.wf.model

import com.example.wf.model.weatherdata.mainrequestdata.Main
import com.example.wf.model.weatherdata.primaryrequestdata.Primary
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherAPI {

    @GET("weather?&appid=3f1534db16ce77a99dd177e1c5dca4b9&lang=ru")
    fun getLatLonByCity(@Query("q") city: String): Call<Primary>

    @GET("onecall?&exclude=minutely,hourly&appid=3f1534db16ce77a99dd177e1c5dca4b9&units=metric&lang=ru")
    fun getWeatherData(@Query("lat") lat: Double, @Query("lon") lon: Double): Call<Main>
}