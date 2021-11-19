package com.example.wf.model

import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Retrofit

class RetrofitUtil {
    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

        fun create(): OpenWeatherAPI {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(OpenWeatherAPI::class.java)
        }
    }
}