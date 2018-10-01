package com.abnamroassignment.foreaquare

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Represents a Venue for the Search result
 */

data class Venue(val id: String, val name: String, val address: String)

@Entity
data class VenueEntity(@PrimaryKey val id: String, val name: String, val address: String, val searchKey: String) {

    companion object {
        fun fromVenue(searchKey: String, venue: Venue) = VenueEntity(venue.id, venue.name, venue.address, searchKey)
    }

    fun toVenue() = Venue(id, name, address)
}

/**
 * Wrapper class for List<Venue> - to simplify JSON parsing
 */
data class Venues(val list: List<Venue>)

/**
 * Wrapper class for Venue with more details.
 */
@Entity
@Parcelize
data class VenueDetails(@PrimaryKey val id: String,
                        val name: String,
                        val description: String,
                        val photoUrl: String,
                        val address: String,
                        val contactPhone: String,
                        val rating: String) : Parcelable

/**
 * Represents the status of the API operation
 */
interface Result {

    val status: Status

    val isSuccess: Boolean
        get() = status == Status.SUCCESS

    val networkError: Boolean
        get() = status == Status.NETWORK_ERROR

    val invalidRequest: Boolean
        get() = status == Status.INVALID_REQUEST

    val isCacheMiss: Boolean
        get() = status == Status.CACHE_MISS
}


/**
 * Wrapper for API result carries status + data for Venue search
 */
data class VenueSearchResult(val searchLocation: String, override val status: Status,
                             val venues: List<Venue>) : Result

/**
 * Wrapper for API result carries status + data for Venue Details
 */
data class VenueDetailsResult(val venueId: String,
                              val venueDetails: VenueDetails?,
                              override val status: Status) : Result

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
    NETWORK_ERROR,

    /**
     * Data not available in cache
     */
    CACHE_MISS
}