package com.launchship.www.mvvm.db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.launchship.www.mvvm.db.model.Logging

@Dao
interface DAO {
    @Query("SELECT * FROM Logging")
    fun getAll(): List<Logging>

    @Insert
    fun insert(task: Logging)

    @Query("DELETE FROM Logging")
    fun deleteAll()
}