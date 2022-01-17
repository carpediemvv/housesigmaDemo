package com.example.housesigmademo

data class MapBean(
    val address: String,
    val house_prop: HouseProp,
    val id: String,
    val location: Location,
    val photo: String,
    val price: String,
    val status: String,
    val up_date: String
)

data class HouseProp(
    val bathroom: Int,
    val bedroom: Int,
    val sqft: Int
)

data class Location(
    val lat: Double,
    val lng: Double
)