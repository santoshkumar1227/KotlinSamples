package com.launchship.www.mvvm.repository

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Room
import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import com.launchship.www.mvvm.db.CommonDatabase
import com.launchship.www.mvvm.db.dao.DAO
import com.launchship.www.mvvm.db.model.Logging
import com.launchship.www.mvvm.model.User
import com.launchship.www.mvvm.sp.CommonSharedPreferences

class CommonRepository {

    fun checkLoginCredentials(user: User, context: Context): Boolean? {
        return getPreferences(context)?.let { getCommonSharedPreferences(context)?.checkLoginCredentials(user, it) }
    }

    fun saveLoginInfo(user: User, context: Context) {
        getPreferencesEditor(context)?.let { getCommonSharedPreferences(context)?.saveLoginInfo(user, it) }
    }

    fun getLoggedUserName(context: Context): String {
        return getPreferences(context)?.let { getCommonSharedPreferences(context)?.getLoggedUserName(it) }.toString()
    }

    fun logTransaction(context: Context, logging: Logging) {
        getDao(context)
        LogTransaction().execute(logging)
    }

    fun loadStatement(context: Context): List<Logging>? {
        getDao(context)
        return StatementLogs().execute(dao).get()
    }

    class StatementLogs : AsyncTask<DAO, List<Logging>, List<Logging>>() {
        override fun doInBackground(vararg params: DAO?): List<Logging>? {
            return params[0]?.getAll()
        }
    }

    class LogTransaction : AsyncTask<Logging, String, String>() {
        override fun doInBackground(vararg params: Logging?): String? {
            params[0]?.let { dao?.insert(it) }
            return null
        }
    }

    companion object Commons {
        private var sharedPreferences: SharedPreferences? = null
        private var editor: SharedPreferences.Editor? = null
        private var commonSharedPreferences: CommonSharedPreferences? = null
        private var commonRepository: CommonRepository? = null
        private var INSTANCE: CommonDatabase? = null
        private var dao: DAO? = null

        fun getPreferences(context: Context): SharedPreferences? {
            if (sharedPreferences == null) {
                sharedPreferences = context.getSharedPreferences("test", Context.MODE_PRIVATE)
            }
            return sharedPreferences
        }

        private fun getPreferencesEditor(context: Context): SharedPreferences.Editor? {
            if (sharedPreferences == null) {
                getPreferences(context)
                editor = sharedPreferences?.edit()
            } else if (editor == null) {
                editor = sharedPreferences?.edit()
            }
            return editor
        }

        fun getCommonSharedPreferences(context: Context): CommonSharedPreferences? {
            if (commonSharedPreferences == null) {
                if (sharedPreferences == null) {
                    getPreferencesEditor(context)
                }
                commonSharedPreferences = CommonSharedPreferences()
            }
            return commonSharedPreferences
        }

        fun getCommonRepository(): CommonRepository {
            if (commonRepository == null)
                commonRepository = CommonRepository()
            return commonRepository as CommonRepository
        }

        fun getDatabase(context: Context): CommonDatabase? {
            if (INSTANCE == null) {
                /*synchronized(CommonDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder<CommonDatabase>(
                            context.applicationContext,
                            CommonDatabase::class.java!!, "word_database"
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }*/
                INSTANCE = Room.databaseBuilder<CommonDatabase>(
                    context.applicationContext,
                    CommonDatabase::class.java!!, "word_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE
        }

        fun getDao(context: Context): DAO? {
            if (INSTANCE == null)
                getDatabase(context)
            dao = INSTANCE?.getDao()
            return dao
        }
    }

}