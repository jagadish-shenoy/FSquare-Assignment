package com.abnamroassignment.foreaquare

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Entity
data class Venue(@PrimaryKey val id: String, val name: String, val address: String)

@Entity
@Parcelize
data class VenueDetails(@PrimaryKey val id: String,
                        val name: String,
                        val description: String,
                        val photoUrl: String,
                        val address: String,
                        val contactPhone: String,
                        val rating: String) : Parcelable

data class VenueSearchResult(val status: Status, val venues: List<Venue>)

data class VenueDetailsResult(val status: Status, val venueDetails: VenueDetails?)

enum class Status {

    /**
     * If everything is fine!
     */
    SUCCESS,

    /**
     * If request could not be fulfilled - server reported error
     */
    INVALID_REQUEST,

    /**
     * Failed due to network error
     */
    NETWORK_ERROR
}