package com.example.santoshb.milo.util

import android.content.Context
import android.content.SharedPreferences
import android.content.Context.MODE_PRIVATE
import com.example.santoshb.milo.R


class CustomSharedPreferences private constructor() {
    companion object {
        private var sharedPreferences: SharedPreferences? = null
        private var sharedPreferencesEditor: SharedPreferences.Editor? = null
        private fun getInstance(context: Context): SharedPreferences? {
            if (sharedPreferences == null)
                sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), MODE_PRIVATE)
            else
                sharedPreferences

            return sharedPreferences
        }

        private fun getInstanceEditor(context: Context): SharedPreferences.Editor? {

            if (sharedPreferencesEditor == null)
                sharedPreferencesEditor = getInstance(context)?.edit()
            else

                sharedPreferencesEditor

            return sharedPreferencesEditor
        }

        fun putString(context: Context, key: String, value: String = "") {
            getInstanceEditor(context)?.putString(key, value)?.apply()
        }

        fun getString(context: Context, key: String): String? {
            return getInstance(context)?.getString(key, "")
        }
    }
}