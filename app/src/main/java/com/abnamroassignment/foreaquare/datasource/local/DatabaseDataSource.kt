package com.abnamroassignment.foreaquare.datasource.local

import android.content.Context
import com.abnamroassignment.foreaquare.*

/**
 * Datasource which fetches the data from local database + helps cache the results.
 */
class DatabaseDataSource(context: Context) : StorageDataSource(context) {

    private val venueDatabase = VenueDatabase.getInMemoryDatabase(context)

    override fun saveSearchResult(searchKey: String, venues: List<Venue>) {
        venueDatabase.venuesDao().insertVenues(*venues.map { VenueEntity.fromVenue(searchKey, it) }.toTypedArray())
    }

    override fun saveVenueDetails(venueDetails: VenueDetails) {
        venueDatabase.venueDetailsDao().insertVenueDetails(venueDetails)
    }

    override fun searchVenues(location: String, limit: Int): VenueSearchResult {
        val venues = venueDatabase.venuesDao().getVenuesForLocation("%$location%", limit)
        return if (venues.isEmpty()) {
            VenueSearchResult(location, Status.NETWORK_ERROR, emptyList())
        } else {
            VenueSearchResult(location, Status.SUCCESS, venues.map { it.toVenue() })
        }
    }

    override fun fetchVenueDetails(venueId: String): VenueDetailsResult {
        val venueDetails = venueDatabase.venueDetailsDao().getVenueDetails(venueId)
        val status = if (venueDetails == null) Status.CACHE_MISS else Status.SUCCESS
        return VenueDetailsResult(venueId, venueDetails, status)
    }

    override fun getAllVenueIds() = venueDatabase.venuesDao().getAllVenueIds()
}