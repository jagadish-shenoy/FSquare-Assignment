package com.abnamroassignment.ui.venueSearch

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.abnamroassignment.foreaquare.Venue
import com.abnamroassignment.ui.DetailsCard

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
        val searchResultView = DetailsCard(parent.context)
        return SearchResultViewHolder(searchResultView)
    }

    override fun getItemCount() = venues.size

    override fun onBindViewHolder(viewHolder: SearchResultViewHolder, position: Int) {
        venues[position].apply {
            viewHolder.detailsCard.setTitle(name)
            viewHolder.detailsCard.setDescription(address)
            viewHolder.detailsCard.setOnClickListener { callback.onVenueItemClicked(this) }
        }
    }
}

class SearchResultViewHolder(val detailsCard: DetailsCard) : RecyclerView.ViewHolder(detailsCard)