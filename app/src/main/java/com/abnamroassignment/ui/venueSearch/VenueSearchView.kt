package com.abnamroassignment.ui.venueSearch

import com.abnamroassignment.foreaquare.Venue
import com.abnamroassignment.foreaquare.VenueDetails


interface VenueSearchView {

    fun hideKeyboard()

    fun getLocation(): String

    fun showSnackbar(message: Int)

    fun setAdapterItems(items: List<Venue>)

    fun startVenueDetailsActivity(venueDetails: VenueDetails)
}