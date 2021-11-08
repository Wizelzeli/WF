package com.example.wf.presentation


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.wf.databinding.ActivityWeatherBinding

class WeatherActivity : AppCompatActivity(), WeatherContract.WeatherViewInterface {

    var presenter: WeatherContract.WeatherPresenterInterface = WeatherPresenter(this)
    lateinit var binding: ActivityWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Погода"
    }

}