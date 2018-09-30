package com.abnamroassignment.ui.venueSearch

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.abnamroassignment.R
import com.abnamroassignment.foreaquare.Venue
import com.abnamroassignment.foreaquare.VenueDetails
import com.abnamroassignment.ui.venueDetails.VenueDetailsActivity
import com.abnamroassignment.ui.venueSearch.viewmodel.VenueSearchViewModel
import kotlinx.android.synthetic.main.activity_venue_search.*

class VenueSearchActivity : AppCompatActivity(), VenueSearchView {

    private lateinit var adapter: VenueSearchResultAdapter

    private lateinit var presenter: VenueSearchPresenter

    private lateinit var imm: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue_search)

        val viewModel = ViewModelProviders.of(this).get(VenueSearchViewModel::class.java)
        presenter = VenueSearchPresenter(this, viewModel, this)

        initView()
    }

    override fun hideKeyboard() {
        imm.hideSoftInputFromWindow(searchResultContainer.windowToken, 0)
    }

    override fun getLocation() = searchLocation.text.toString()

    override fun setAdapterItems(items: List<Venue>) {
        adapter.venues = items
    }

    override fun startVenueDetailsActivity(venueDetails: VenueDetails) {
        VenueDetailsActivity.start(venueDetails, this@VenueSearchActivity)
    }

    override fun showSnackbar(message: Int) {
        Snackbar.make(searchResultContainer, message, Snackbar.LENGTH_LONG).show()
    }

    private fun initView() {
        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        venueSearchResultsView.layoutManager = LinearLayoutManager(this)
        adapter = VenueSearchResultAdapter(presenter)
        venueSearchResultsView.adapter = adapter

        searchLocation.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                presenter.onSearchClicked()
                true
            } else {
                false
            }
        }
        searchButton.setOnClickListener { presenter.onSearchClicked() }
    }

}