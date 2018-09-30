package com.abnamroassignment.foreaquare.datasource.remote;

import com.abnamroassignment.foreaquare.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface VenueService {

    @GET("venues/search")
    fun searchVenues(@Query("near") nearLocation: String,
                     @Query("radius") radius: Int,
                     @Query("limit") limit: Int): Call<Venues>

    @GET("venues/{venue_id}")
    fun getVenueDetails(@Path("venue_id") venueId: String): Call<VenueDetails>
}

fun createInvalidRequestSearchResult(searchLocation: String) = VenueSearchResult(searchLocation, Status.INVALID_REQUEST, emptyList())

fun createNetworkErrorSearchResult(searchLocation: String) = VenueSearchResult(searchLocation, Status.NETWORK_ERROR, emptyList())

fun createInvalidRequestVenueDetailsResult(venueId: String) = VenueDetailsResult(venueId, null, Status.INVALID_REQUEST)

fun createNetworkErrorVenueDetailsResult(venueId: String) = VenueDetailsResult(venueId, null, Status.NETWORK_ERROR)