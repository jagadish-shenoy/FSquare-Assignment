package com.abnamroassignment.foreaquare.datasource.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.abnamroassignment.foreaquare.VenueEntity

@Dao
interface VenuesDao {

    @Query("select * from venueentity where searchKey like :location limit :limit")
    fun getVenuesForLocation(location: String, limit: Int): List<VenueEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVenues(vararg venues: VenueEntity)
}