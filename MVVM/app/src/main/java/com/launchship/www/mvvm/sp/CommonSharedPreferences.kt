package com.launchship.www.mvvm.sp

import android.content.SharedPreferences
import com.launchship.www.mvvm.model.User

class CommonSharedPreferences {

    fun checkLoginCredentials(user: User, sharedPreferences: SharedPreferences): Boolean {
        if ((sharedPreferences.getString("username", "Santosh") == user.userName) &&
            (sharedPreferences.getString("password", "test") == user.password)
        ) {
            return true
        }
        return false
    }

    fun saveLoginInfo(user: User, editor: SharedPreferences.Editor) {
        editor.putString("username", user.userName)
        editor.putString("password", user.password)
        editor.apply()
    }

    fun getLoggedUserName(sharedPreferences: SharedPreferences): String {
        return sharedPreferences.getString("username", "")
    }

}