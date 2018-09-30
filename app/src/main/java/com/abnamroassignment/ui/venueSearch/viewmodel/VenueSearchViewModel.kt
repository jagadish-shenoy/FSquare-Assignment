package com.abnamroassignment.ui.venueSearch.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.abnamroassignment.foreaquare.FourSquareManager
import com.abnamroassignment.foreaquare.FourSquareManager.ForeSquareManagerCallback
import com.abnamroassignment.foreaquare.VenueDetailsResult
import com.abnamroassignment.foreaquare.VenueSearchResult

class VenueSearchViewModel(context: Application) : AndroidViewModel(context), ForeSquareManagerCallback {

    val foreSquareManager = FourSquareManager(context, this)

    val venueSearchResultLiveData = MutableLiveData<VenueSearchResult>()

    val venueDetailsResultLiveData = MutableLiveData<VenueDetailsResult>()

    override fun onVenueSearchResponse(venueSearchResult: VenueSearchResult) {
        venueSearchResultLiveData.postValue(venueSearchResult)
    }

    override fun onVenueDetailsResponse(venueDetailsResult: VenueDetailsResult) {
        venueDetailsResultLiveData.postValue(venueDetailsResult)
    }
}