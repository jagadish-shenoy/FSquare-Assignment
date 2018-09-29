package com.abnamroassignment.ui.venueDetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.abnamroassignment.R
import com.abnamroassignment.foreaquare.VenueDetails
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_venue_details.*

private const val EXTRA_VENUE_DETAILS = "venueDetails"

class VenueDetailsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue_details)
        val venueDetails: VenueDetails = intent.extras!!.getParcelable(EXTRA_VENUE_DETAILS)!!

        venueDetails.apply {
            title = name
            if (photoUrl.isEmpty()) {
                venueImage.visibility = View.GONE
            } else {
                Glide.with(this@VenueDetailsActivity).load(photoUrl).into(venueImage)
            }
            venueDescription.text = description
            venueContact.text = contactPhone
            venueAddress.text = address
            venueRating.text = rating
        }
    }

    companion object {
        fun start(venueDetails: VenueDetails, context: Context) {
            val intent = Intent(context, VenueDetailsActivity::class.java)
            intent.putExtra(EXTRA_VENUE_DETAILS, venueDetails)
            context.startActivity(intent)
        }
    }
}
