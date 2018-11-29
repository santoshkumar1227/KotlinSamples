package com.launchship.www.mvvm.viewmodel

import android.arch.lifecycle.ViewModel
import android.content.Context
import com.launchship.www.mvvm.model.User
import com.launchship.www.mvvm.repository.CommonRepository

class LoginViewModel : ViewModel() {

    fun checkLoginCredentials(user: User, context: Context): Boolean? {
        if (user.userName == "" || user.password == "") {
            return false
        }
        return CommonRepository.getCommonRepository().checkLoginCredentials(user, context)
    }

    fun saveLoginUsername(user: User, context: Context) {
        return CommonRepository.getCommonRepository().saveLoginInfo(user, context)
    }

}