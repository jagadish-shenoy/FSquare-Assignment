package com.abnamroassignment.foreaquare;

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface VenueService {

    @GET("venues/search")
    fun searchVenues(@Query("near") nearLocation: String,
                     @Query("radius") radius: Int,
                     @Query("limit") limit: Int): Call<VenueSearchResult>

    @GET("venues/{venue_id}")
    fun getVenueDetails(@Path("venue_id") venueId: String): Call<VenueDetailsResult>
}

data class VenueSearchResult(val status:Status, val venues:List<Venue>)

data class Venue(val id:String, val name:String, val address:String)


data class VenueDetailsResult(val status:Status, val venueDetails: VenueDetails?)

@Parcelize
data class VenueDetails(val id:String,
                        val name:String,
                        val description:String,
                        val address:String,
                        val contactPhone:String,
                        val rating:String): Parcelable

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

val InvalidRequestSearchResult = VenueSearchResult(Status.INVALID_REQUEST, emptyList())

val NetworkErrorSearchResult = VenueSearchResult(Status.NETWORK_ERROR, emptyList())

val InvalidRequestVenueDetailsResult = VenueDetailsResult(Status.INVALID_REQUEST, null)

val NetworkErrorVenueDetailsResult = VenueDetailsResult(Status.NETWORK_ERROR, null)