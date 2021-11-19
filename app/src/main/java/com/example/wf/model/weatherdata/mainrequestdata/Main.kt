package com.example.wf.model.weatherdata.mainrequestdata

import java.io.Serializable

data class Main (
	val current : Current,
	val daily : List<Daily>,
) : Serializable



