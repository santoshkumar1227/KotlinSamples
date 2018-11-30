package com.launchship.www.mvvm.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.launchship.www.mvvm.db.model.Logging
import com.launchship.www.mvvm.repository.CommonRepository

class HomePageViewModel : ViewModel() {


    private var mAllWords: LiveData<List<Logging>>? = null

    fun getMiniStatement(context: Context) {
        mAllWords = CommonRepository.getCommonRepository().loadMiniStatement(context)
    }

    fun getLoggedUserName(context: Context): String {
        return CommonRepository.getCommonRepository().getLoggedUserName(context)
    }

    fun insertLogging(context: Context, logging: Logging) {
        CommonRepository.getCommonRepository().logTransaction(context, logging)
    }

    fun getStatement(mContext: Context): List<Logging>? {
        return CommonRepository.getCommonRepository().loadStatement(mContext)
    }

    fun getAllWords(): LiveData<List<Logging>>? {
        return mAllWords
    }
}