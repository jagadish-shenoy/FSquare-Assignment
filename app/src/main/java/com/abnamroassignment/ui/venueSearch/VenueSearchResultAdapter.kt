package com.abnamroassignment.ui.venueSearch

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.abnamroassignment.R
import com.abnamroassignment.foreaquare.Venue
import kotlinx.android.synthetic.main.layout_search_result.view.*

internal class VenueSearchResultAdapter(private val callback:VenueItemClickCallback): RecyclerView.Adapter<SearchResultViewHolder>() {

    interface VenueItemClickCallback {
        fun onVenueItemClicked(venue: Venue)
    }

    var venues = emptyList<Venue>()

    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): SearchResultViewHolder {
        val searchResultView = LayoutInflater.from(parent.context).inflate(R.layout.layout_search_result, parent, false)
        return SearchResultViewHolder(searchResultView)
    }

    override fun getItemCount() = venues.size

    override fun onBindViewHolder(viewHolder: SearchResultViewHolder, position: Int) {
        venues[position].apply {
            viewHolder.venueName.text = name
            viewHolder.venueAddress.text = address
            viewHolder.searchResultContainer.setOnClickListener { callback.onVenueItemClicked(this) }
        }
    }
}

class SearchResultViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val searchResultContainer:View = view.searchResultContainer

    val venueName:TextView = view.venueName

    val venueAddress:TextView = view.venueAddress

}