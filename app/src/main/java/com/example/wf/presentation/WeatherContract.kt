package com.example.wf.presentation

import com.example.wf.model.weatherdata.mainrequestdata.Main

interface WeatherContract {

    interface WeatherViewInterface {
        fun showWeatherData(weatherData: Main)
        fun showFailureToast(s: String)
        fun hideProgressBar()
    }

    interface WeatherPresenterInterface {
        fun onSearchClick(cityName: String)
        fun onAppLaunch(lat: Double, lon: Double)
    }

}