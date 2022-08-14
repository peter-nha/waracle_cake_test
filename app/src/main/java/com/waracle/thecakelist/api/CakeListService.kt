package com.waracle.thecakelist.api

import retrofit2.Call
import retrofit2.http.GET

interface CakeListService {

    @GET("raw/1d4dd9c5a0ec758ff5ae92b7b13fe4d57d34e1dc/waracle_cake-android-client")
    fun getCakeList(): Call<List<CakeDTO>>
}