package com.launchship.www.mvvm.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.launchship.www.mvvm.R
import com.launchship.www.mvvm.view.fragment.DepositFragment
import com.launchship.www.mvvm.view.fragment.TransferFragment
import com.launchship.www.mvvm.view.fragment.WithdrawFragment
import com.launchship.www.mvvm.view.interfaces.ReturnToParent

class CommonActivity : AppCompatActivity(), ReturnToParent {

    var type: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common)
        supportActionBar?.setHomeButtonEnabled(true)

        if (intent.hasExtra("name")) {
            val name = intent.getStringExtra("name")
            supportActionBar?.title = name
            when (name) {
                resources.getString(R.string.Withdraw) -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, WithdrawFragment()).commit()
                }
                resources.getString(R.string.Deposit) -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, DepositFragment()).commit()
                }
                resources.getString(R.string.Transfer) -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, TransferFragment()).commit()
                }
            }

        }
    }

    override fun returnToParent(transactionType: String, transactionAmount: Int) {
        val intent: Intent = Intent()
        intent.putExtra("type", transactionType)
        intent.putExtra("amount", transactionAmount)
        setResult(101, intent)
        finish()
    }
}
