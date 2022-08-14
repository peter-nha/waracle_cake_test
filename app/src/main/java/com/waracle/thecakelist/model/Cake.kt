package com.waracle.thecakelist.model

import com.waracle.thecakelist.api.CakeDTO

data class Cake(
    val title: String,
    val desc: String,
    val image: String,
)

fun CakeDTO.toCake(): Cake =
    Cake(
        title = title,
        desc = desc,
        image = image,
    )