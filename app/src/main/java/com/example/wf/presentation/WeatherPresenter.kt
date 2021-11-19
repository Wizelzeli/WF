package com.example.wf.presentation

import com.example.wf.model.RetrofitUtil
import com.example.wf.model.weatherdata.mainrequestdata.Main
import com.example.wf.model.weatherdata.primaryrequestdata.Primary
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherPresenter(var view: WeatherContract.WeatherViewInterface) :
    WeatherContract.WeatherPresenterInterface {

    override fun onSearchClick(cityName: String) {
        val retrofitUtilPrimary = RetrofitUtil.create().getLatLonByCity(cityName)
        retrofitUtilPrimary.enqueue(object : Callback<Primary> {
            override fun onResponse(call: Call<Primary>?, response: Response<Primary>?) {
                if (response?.body() != null) {
                   onAppLaunch(response.body()!!.coord.lat, response.body()!!.coord.lon)
                } else {
                    if (response != null) {
                        view.hideProgressBar()
                        view.showFailureToast(
                            response.code().toString() + response.errorBody()?.string()
                        )
                    }
                }
            }
            override fun onFailure(call: Call<Primary>?, t: Throwable?) {
                view.hideProgressBar()
                view.showFailureToast(t.toString())
            }
        })
    }

    override fun onAppLaunch(lat: Double, lon: Double) {
        val retrofitUtilMain = RetrofitUtil.create().getWeatherData(lat, lon)
        retrofitUtilMain.enqueue(object : Callback<Main> {
            override fun onResponse(call: Call<Main>?, response: Response<Main>?) {
                if (response?.body() != null) {
                    view.hideProgressBar()
                    view.showWeatherData(response.body()!!)
                } else {
                    if (response != null) {
                        view.hideProgressBar()
                        view.showFailureToast(
                            response.code().toString() + response.errorBody()?.string()
                        )
                    }
                }
            }
            override fun onFailure(call: Call<Main>?, t: Throwable?) {
                view.hideProgressBar()
                view.showFailureToast(t.toString())
            }
        })
    }
}