package com.waracle.thecakelist.api

import com.google.gson.annotations.SerializedName

data class CakeDTO(

    @SerializedName("title")
    val title: String,

    @SerializedName("desc")
    val desc: String,

    @SerializedName("image")
    val image: String,
)