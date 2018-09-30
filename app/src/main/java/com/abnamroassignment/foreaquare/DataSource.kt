package com.abnamroassignment.foreaquare

import android.content.Context

/**
 * Represents a source for fetching the Venue data.
 */
abstract class DataSource(protected val context: Context) {

    abstract fun searchVenues(location: String, limit: Int): VenueSearchResult

    abstract fun fetchVenueDetails(venueId: String): VenueDetailsResult
}

/**
 * Represents a source for fetching & persisting Venue data
 */
abstract class StorageDataSource(context: Context) : DataSource(context) {

    abstract fun saveSearchResult(venues: List<Venue>)

    abstract fun saveVenueDetails(venueDetails: VenueDetails)

    abstract fun getAllVenueIds(): List<String>
}