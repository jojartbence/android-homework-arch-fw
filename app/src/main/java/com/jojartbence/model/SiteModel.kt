package com.jojartbence.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
data class SiteModel (
    var id: Long? = null,
    var firebaseId : String = "",
    var title: String? = null,
    var description: String? = null,
    var location: Location = Location(),
    var images: Array<String> = arrayOf("", "", "", ""),
    var visited: Boolean = false,
    var dateVisited: Date? = null,
    var additionalNotes: String? = null,
    var isFavourite: Boolean = false,
    var rating: Float = 5.0f
): Parcelable {

    // TODO: use Calendar instead of deprecated Date
    companion object {
        val defaultDateInCaseOfError: Date = Date(100, 1, 1)
        const val defaultDateInCaseOfErrorAsString: String = "2000.01.01"
    }

    fun copy(): SiteModel {
        return SiteModel(id, firebaseId, title, description, location.copy(), images.clone(), visited, dateVisited, additionalNotes, isFavourite, rating)
    }

    fun toEmailText(): String {
        var text: String = "Details of the site: \n\n"

        text += "Title: $title \n"
        if (description != null) {
            text += "Description: $description \n"
        }
        text += "Location: Lat: %.6f - Lng: %.6f \n".format(location.lat, location.lng)
        if (additionalNotes != null) {
            text += "Additional notes: $additionalNotes \n"
        }
        text += "Rating: $rating \n"

        return text
    }
}

@Parcelize
data class Location (
    var lat: Double = 46.9590448,
    var lng: Double = 18.9325799,
    var zoom: Float = 13f
): Parcelable