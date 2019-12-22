package com.jojartbence.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*


// TODO: do not initialize with these dummy values

@Parcelize
data class SiteModel (
    var id: Long = 0,
    var title: String = "",
    var description: String = "",
    var location: Location = Location(),
    var images: Array<String> = arrayOf("", "", "", ""),
    var visited: Boolean = false,
    var visitDate: Date? = null,
    var additionalNotes: String? = null
): Parcelable


@Parcelize
data class Location (
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f
): Parcelable