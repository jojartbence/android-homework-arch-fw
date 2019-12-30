package com.jojartbence.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
data class SiteModel (
    var id: Long? = null,
    var title: String? = null,
    var description: String? = null,
    var location: Location = Location(),
    var images: Array<String> = arrayOf("", "", "", ""),
    var visited: Boolean = false,
    var dateVisited: Date? = null,
    var additionalNotes: String? = null,
    var isFavourite: Boolean = false
): Parcelable {

    // TODO: use Calendar instead of deprecated Date
    companion object {
        val defaultDateInCaseOfError: Date = Date(100, 1, 1)
        const val defaultDateInCaseOfErrorAsString: String = "2000.01.01"
    }
}

@Parcelize
data class Location (
    var lat: Double = 46.9590448,
    var lng: Double = 18.9325799,
    var zoom: Float = 13f
): Parcelable