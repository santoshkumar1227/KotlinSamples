package com.launchship.www.mvvm.view.fragment

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.launchship.www.mvvm.R
import com.launchship.www.mvvm.util.Commons
import com.launchship.www.mvvm.view.interfaces.ReturnToParent
import com.launchship.www.mvvm.viewmodel.CommonViewModel
import kotlinx.android.synthetic.main.fragment_common.*

class DepositFragment : Fragment() {

    var returnToParent: ReturnToParent? = null
    var commonViewModel: CommonViewModel? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        returnToParent = context as ReturnToParent
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_common, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel::class.java)
        btn1.text = resources.getString(R.string.Deposit)
        btn1.setOnClickListener {
            if (!validateAmount())
                returnToParent?.returnToParent(
                    resources.getString(R.string.Deposit),
                    Commons.readFromEditText(et2).toInt()
                )
        }
    }

    private fun validateAmount(): Boolean {
        return Commons.readFromEditText(et2) == ""
    }
}