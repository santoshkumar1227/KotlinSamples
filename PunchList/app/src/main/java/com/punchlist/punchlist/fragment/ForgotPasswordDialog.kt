package com.punchlist.punchlist.fragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.punchlist.punchlist.R


class ForgotPasswordDialog : DialogFragment(), View.OnClickListener {
    override fun onClick(v: View?) {
        v?.let {
            when (v.id) {
                R.id.btnSubmit -> {

                }

                R.id.btnCancel -> {
                    dismiss()
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragement_forgotpassword, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.btnCancel).setOnClickListener(this)
        view.findViewById<TextView>(R.id.btnSubmit).setOnClickListener(this)
    }
}