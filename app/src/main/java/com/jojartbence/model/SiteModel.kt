package com.jojartbence.model

import java.util.*

data class SiteModel (
    var title: String = "",
    var description: String = "",
    var location: Location = Location(),
    var images: Array<String> = emptyArray(),
    var visited: Boolean = false,
    var visitDate: Date? = null,
    var additionalNotes: String? = null
)



data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f)