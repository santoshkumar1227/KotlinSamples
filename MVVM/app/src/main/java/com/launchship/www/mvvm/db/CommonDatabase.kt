package com.launchship.www.mvvm.db

import android.arch.persistence.room.Database
import com.launchship.www.mvvm.db.model.Logging
import android.arch.persistence.room.RoomDatabase
import com.launchship.www.mvvm.db.dao.DAO


@Database(entities = [Logging::class], version = 1)
abstract class CommonDatabase : RoomDatabase() {
    abstract fun getDao(): DAO
}