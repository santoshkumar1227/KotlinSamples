package com.launchship.www.mvvm.viewmodel

import android.arch.lifecycle.ViewModel
import android.content.Context
import com.launchship.www.mvvm.db.model.Logging
import com.launchship.www.mvvm.repository.CommonRepository

class HomePageViewModel : ViewModel() {
    fun getLoggedUserName(context: Context): String {
        return CommonRepository.getCommonRepository().getLoggedUserName(context)
    }

    fun insertLogging(context: Context, logging: Logging) {
        CommonRepository.getCommonRepository().logTransaction(context, logging)
    }

    fun getStatement(context: Context): List<Logging>? {
        return CommonRepository.getCommonRepository().loadStatement(context)
    }
}