package com.example.wf.presentation


import android.Manifest
import android.annotation.SuppressLint
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.wf.databinding.ActivityWeatherBinding
import java.text.SimpleDateFormat
import java.util.*
import com.example.wf.model.weatherdata.mainrequestdata.Main
import android.view.inputmethod.InputMethodManager
import com.example.wf.R
import android.app.Activity
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import java.lang.Exception


class WeatherActivity : AppCompatActivity(), WeatherContract.WeatherViewInterface {

    private var presenter: WeatherContract.WeatherPresenterInterface = WeatherPresenter(this)
    lateinit var binding: ActivityWeatherBinding
    lateinit var weatherData: Main
    private var currentDetailsIndex: Int = 0
    private var isFirstLaunch: Boolean = true
    private val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    private val sdfMain = SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault())
    private val sdfSun = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Погода"

        if (savedInstanceState != null) {
            isFirstLaunch = savedInstanceState.getBoolean("isFirst")
            if (savedInstanceState.getSerializable("data") != null) {
                weatherData = savedInstanceState.getSerializable("data") as Main
                currentDetailsIndex = savedInstanceState.getInt("selectedIndex")
                showWeatherData(weatherData)
                if (currentDetailsIndex != 7) showDetailsForecast(currentDetailsIndex)
            }
        }

        if (isFirstLaunch) {
            val locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
            val isNetworkEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isNetworkEnabled) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        (this as Activity?)!!,
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ),
                        101
                    )
                }
                try {
                    val location =
                        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        binding.progressBar.visibility = View.VISIBLE
                        presenter.onAppLaunch(location.latitude, location.longitude)
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this,
                        "Разрешение на доступ к местоположению не предоставлено. Данные о погоде в текущем городе не загружены.",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } else Toast.makeText(
                this,
                "Отключена функция доступа к местоположению. Данные о погоде в текущем городе не загружены.",
                Toast.LENGTH_LONG
            ).show()
            isFirstLaunch = false
        }

        binding.searchButton.setOnClickListener {
            hideKeyboard()
            binding.dayZero.visibility = View.GONE
            binding.details.visibility = View.GONE
            binding.horizontalScrollView.visibility = View.GONE
            binding.mainScrollView.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            presenter.onSearchClick(binding.searchField.text.toString())
        }
        binding.dayZero.setOnClickListener {
            showDetailsCurrent()
            currentDetailsIndex = 7
        }
        binding.dayZeroMini.setOnClickListener {
            showDetailsForecast(0)
            currentDetailsIndex = 0
        }
        binding.dayFirst.setOnClickListener {
            showDetailsForecast(1)
            currentDetailsIndex = 1
        }
        binding.daySecond.setOnClickListener {
            showDetailsForecast(2)
            currentDetailsIndex = 2
        }
        binding.dayThird.setOnClickListener {
            showDetailsForecast(3)
            currentDetailsIndex = 3
        }
        binding.dayFourth.setOnClickListener {
            showDetailsForecast(4)
            currentDetailsIndex = 4
        }
        binding.dayFifth.setOnClickListener {
            showDetailsForecast(5)
            currentDetailsIndex = 5
        }
        binding.daySixth.setOnClickListener {
            showDetailsForecast(6)
            currentDetailsIndex = 6
        }
    }

    override fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    override fun showFailureToast(s: String) {
        Toast.makeText(
            this,
            "Произошла ошибка: $s",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (this::weatherData.isInitialized) {
            outState.putSerializable("data", weatherData)
            outState.putInt("selectedIndex", currentDetailsIndex)
        }
        outState.putBoolean("isFirst", isFirstLaunch)
        super.onSaveInstanceState(outState)
    }

    @SuppressLint("SetTextI18n")
    override fun showWeatherData(weatherData: Main) {
        this.weatherData = weatherData

        binding.dateDayZero.text =
            sdfMain.format(Date((weatherData.current.dt.toLong()) * 1000))
        binding.tempDayZero.text = weatherData.current.temp.toString() + " ℃"
        binding.descriptionDayZero.text = weatherData.current.weather[0].description
        binding.feelsLikeDayZero.text =
            getString(R.string.feels_like) + weatherData.current.feels_like.toString() + " ℃"
        val uriImgZero: Uri =
            Uri.parse("https://openweathermap.org/img/wn/" + weatherData.current.weather[0].icon + "@2x.png")
        Glide.with(this).load(uriImgZero).into(binding.imageDayZero)

        showDetailsCurrent()

        binding.dateDayZeroMini.text = sdf.format(Date(weatherData.daily[0].dt.toLong() * 1000))
        binding.minmaxDayZeroMini.text =
            weatherData.daily[0].temp.min.toString() + " / " + weatherData.daily[0].temp.max.toString()
        binding.tempDayZeroMini.text = weatherData.daily[0].temp.day.toString() + " ℃"
        val uriImgZeroMini: Uri =
            Uri.parse("https://openweathermap.org/img/wn/" + weatherData.daily[0].weather[0].icon + "@2x.png")
        Glide.with(this).load(uriImgZeroMini).into(binding.imageDayZeroMini)

        binding.dateDayFirst.text = sdf.format(Date(weatherData.daily[1].dt.toLong() * 1000))
        binding.minmaxDayFirst.text =
            weatherData.daily[1].temp.min.toString() + " / " + weatherData.daily[1].temp.max.toString()
        binding.tempDayFirst.text = weatherData.daily[1].temp.day.toString() + " ℃"
        val uriImgFirst: Uri =
            Uri.parse("https://openweathermap.org/img/wn/" + weatherData.daily[1].weather[0].icon + "@2x.png")
        Glide.with(this).load(uriImgFirst).into(binding.imageDayFirst)

        binding.dateDaySecond.text = sdf.format(Date(weatherData.daily[2].dt.toLong() * 1000))
        binding.minmaxDaySecond.text =
            weatherData.daily[2].temp.min.toString() + " / " + weatherData.daily[2].temp.max.toString()
        binding.tempDaySecond.text = weatherData.daily[2].temp.day.toString() + " ℃"
        val uriImgSecond: Uri =
            Uri.parse("https://openweathermap.org/img/wn/" + weatherData.daily[2].weather[0].icon + "@2x.png")
        Glide.with(this).load(uriImgSecond).into(binding.imageDaySecond)

        binding.dateDayThird.text = sdf.format(Date(weatherData.daily[3].dt.toLong() * 1000))
        binding.minmaxDayThird.text =
            weatherData.daily[3].temp.min.toString() + " / " + weatherData.daily[3].temp.max.toString()
        binding.tempDayThird.text = weatherData.daily[3].temp.day.toString() + " ℃"
        val uriImgThird: Uri =
            Uri.parse("https://openweathermap.org/img/wn/" + weatherData.daily[3].weather[0].icon + "@2x.png")
        Glide.with(this).load(uriImgThird).into(binding.imageDayThird)

        binding.dateDayFourth.text = sdf.format(Date(weatherData.daily[4].dt.toLong() * 1000))
        binding.minmaxDayFourth.text =
            weatherData.daily[4].temp.min.toString() + " / " + weatherData.daily[4].temp.max.toString()
        binding.tempDayFourth.text = weatherData.daily[4].temp.day.toString() + " ℃"
        val uriImgFourth: Uri =
            Uri.parse("https://openweathermap.org/img/wn/" + weatherData.daily[4].weather[0].icon + "@2x.png")
        Glide.with(this).load(uriImgFourth).into(binding.imageDayFourth)

        binding.dateDayFifth.text = sdf.format(Date(weatherData.daily[5].dt.toLong() * 1000))
        binding.minmaxDayFifth.text =
            weatherData.daily[5].temp.min.toString() + " / " + weatherData.daily[5].temp.max.toString()
        binding.tempDayFifth.text = weatherData.daily[5].temp.day.toString() + " ℃"
        val uriImgFifth: Uri =
            Uri.parse("https://openweathermap.org/img/wn/" + weatherData.daily[5].weather[0].icon + "@2x.png")
        Glide.with(this).load(uriImgFifth).into(binding.imageDayFifth)

        binding.dateDaySixth.text = sdf.format(Date(weatherData.daily[6].dt.toLong() * 1000))
        binding.minmaxDaySixth.text =
            weatherData.daily[6].temp.min.toString() + " / " + weatherData.daily[6].temp.max.toString()
        binding.tempDaySixth.text = weatherData.daily[6].temp.day.toString() + " ℃"
        val uriImgSixth: Uri =
            Uri.parse("https://openweathermap.org/img/wn/" + weatherData.daily[6].weather[0].icon + "@2x.png")
        Glide.with(this).load(uriImgSixth).into(binding.imageDaySixth)

        binding.dayZero.visibility = View.VISIBLE
        binding.details.visibility = View.VISIBLE
        binding.horizontalScrollView.visibility = View.VISIBLE
        binding.mainScrollView.visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    fun showDetailsForecast(index: Int) {
        binding.moreTime.text =
            getString(R.string.forecast_weather_time) + sdf.format(Date(weatherData.daily[index].dt.toLong() * 1000))
        binding.moreClouds.text =
            getString(R.string.clouds) + weatherData.daily[index].clouds.toString() + " %"
        binding.moreHumidity.text =
            getString(R.string.humidity) + weatherData.daily[index].humidity.toString() + " %"
        binding.morePressure.text =
            getString(R.string.pressure) + weatherData.daily[index].pressure.toString() + " hPa"
        binding.moreSunrise.text =
            getString(R.string.sunrise_time) + sdfSun.format(Date(weatherData.daily[index].sunrise.toLong() * 1000))
        binding.moreSunset.text =
            getString(R.string.sunset_time) + sdfSun.format(Date(weatherData.daily[index].sunset.toLong() * 1000))
        binding.moreUvi.text = getString(R.string.uvi) + weatherData.daily[index].uvi.toString()
        binding.moreWindSpeed.text =
            getString(R.string.wind_speed) + weatherData.daily[index].wind_speed.toString() + " м/с"
    }

    @SuppressLint("SetTextI18n")
    fun showDetailsCurrent() {
        binding.moreTime.text = getString(R.string.current_weather_time)
        binding.moreClouds.text =
            getString(R.string.clouds) + weatherData.current.clouds.toString() + " %"
        binding.moreHumidity.text =
            getString(R.string.humidity) + weatherData.current.humidity.toString() + " %"
        binding.morePressure.text =
            getString(R.string.pressure) + weatherData.current.pressure.toString() + " hPa"
        binding.moreSunrise.text =
            getString(R.string.sunrise_time) + sdfSun.format(Date(weatherData.current.sunrise.toLong() * 1000))
        binding.moreSunset.text =
            getString(R.string.sunset_time) + sdfSun.format(Date(weatherData.current.sunset.toLong() * 1000))
        binding.moreUvi.text = getString(R.string.uvi) + weatherData.current.uvi.toString()
        binding.moreWindSpeed.text =
            getString(R.string.wind_speed) + weatherData.current.wind_speed.toString() + " м/с"
    }
}