package com.abnamroassignment.foreaquare.datasource.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.abnamroassignment.foreaquare.Venue

@Dao
interface VenuesDao {

    @Query("select * from venue where address like :location limit :limit")
    fun getVenuesForLocation(location: String, limit: Int): List<Venue>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVenues(vararg venues: Venue)

    @Query("select id from venue")
    fun getAllVenueIds(): List<String>
}