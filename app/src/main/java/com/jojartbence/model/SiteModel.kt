package com.jojartbence.model

import java.util.*

data class SiteModel (
    var id: Long = 0,
    var title: String = "",
    var description: String = "",
    var location: Location = Location(),
    var image: String = "",
    var visited: Boolean = false,
    var visitDate: Date? = null,
    var additionalNotes: String? = null
)



data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f)