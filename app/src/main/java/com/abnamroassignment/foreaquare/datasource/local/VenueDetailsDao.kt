package com.abnamroassignment.foreaquare.datasource.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.abnamroassignment.foreaquare.VenueDetails

@Dao
interface VenueDetailsDao {

    @Insert
    fun insertVenueDetails(venueDetails: VenueDetails)

    @Query("Select * from venuedetails where id = :id")
    fun getVenueDetails(id:String):VenueDetails
}