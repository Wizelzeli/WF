package com.example.wf.model.weatherdata.mainrequestdata

data class Daily (
	val dt : Int,
	val sunrise : Int,
	val sunset : Int,
	val temp : Temp,
	val pressure : Int,
	val humidity : Int,
	val wind_speed : Double,
	val weather : List<Weather>,
	val clouds : Int,
	val uvi : Double
)