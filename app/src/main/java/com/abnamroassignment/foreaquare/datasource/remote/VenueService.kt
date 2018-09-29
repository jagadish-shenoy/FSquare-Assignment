package com.abnamroassignment.foreaquare.datasource.remote;

import com.abnamroassignment.foreaquare.Status
import com.abnamroassignment.foreaquare.VenueDetailsResult
import com.abnamroassignment.foreaquare.VenueSearchResult
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

val InvalidRequestSearchResult = VenueSearchResult(Status.INVALID_REQUEST, emptyList())

val NetworkErrorSearchResult = VenueSearchResult(Status.NETWORK_ERROR, emptyList())

val InvalidRequestVenueDetailsResult = VenueDetailsResult(Status.INVALID_REQUEST, null)

val NetworkErrorVenueDetailsResult = VenueDetailsResult(Status.NETWORK_ERROR, null)