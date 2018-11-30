package com.launchship.www.mvvm.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.launchship.www.mvvm.R
import com.launchship.www.mvvm.db.model.Logging
import com.launchship.www.mvvm.util.Commons
import com.launchship.www.mvvm.view.adapter.GridSingleItemAdapter
import com.launchship.www.mvvm.view.adapter.MiniStatementAdapter
import com.launchship.www.mvvm.view.adapter.StatementAdapter
import com.launchship.www.mvvm.view.interfaces.ClickListener
import com.launchship.www.mvvm.viewmodel.HomePageViewModel
import kotlinx.android.synthetic.main.activity_home_page.*
import java.time.LocalDateTime


class HomePageActivity : AppCompatActivity(), ClickListener {

    var list: MutableList<String>? = null
    lateinit var homePageViewModel: HomePageViewModel
    var adapter: MiniStatementAdapter? = null
    var miniStatement: List<Logging>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
    }

    override fun onStart() {
        super.onStart()
        homePageViewModel = ViewModelProviders.of(this).get(HomePageViewModel::class.java)
        homePageViewModel.getMiniStatement(this)
        setNameTitle()
        setRecyclerView()
        loadMiniStatement()
    }

    private fun loadMiniStatement() {
        miniStatement = ArrayList()
        recyclerViewMiniStatement.setHasFixedSize(true)
        adapter = MiniStatementAdapter(this, miniStatement as ArrayList<Logging>)
        recyclerViewMiniStatement.adapter = adapter
        homePageViewModel.getAllWords()?.observe(this,
            Observer<List<Logging>> { t ->
                if (t?.size!! > 10)
                    adapter?.updateData(t?.subList(0, 10))
                else
                    adapter?.updateData(t)
            })
    }

    private fun setRecyclerView() {
        list = mutableListOf(
            resources.getString(R.string.Withdraw),
            resources.getString(R.string.Deposit),
            resources.getString(R.string.Transfer),
            resources.getString(R.string.Statement)
        )
        recyclerViewHomeMenu.adapter = GridSingleItemAdapter(this@HomePageActivity, list!!)
    }

    private fun setNameTitle() {
        val loggedUser = homePageViewModel.getLoggedUserName(applicationContext)
        supportActionBar?.title = "Welcome $loggedUser"
        tvName.text = "A/c Name :: $loggedUser"
        accNo.text = "A/c No :: ${Commons.acNo}"
        refreshBalance()
    }

    private fun refreshBalance() {
        balance.text = "A/c Balance :: ${Commons.balance}"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.logOut -> {
                finish()
            }
        }
        return true
    }

    override fun itemClick(position: Int) {
        if (list?.get(position) != resources.getString(R.string.Statement)) {
            val intent = Intent(applicationContext, CommonActivity::class.java)
            intent.putExtra("name", list?.get(position))
            startActivityForResult(intent, 100)
        } else {
            startActivity(Intent(applicationContext, StatementActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.let {
            if (requestCode == 100 && resultCode == 101) {
                val type = data.getStringExtra("type")
                val amount = data.getIntExtra("amount", 0)

                when (type) {
                    resources.getString(R.string.Withdraw) -> {
                        Commons.balance = Commons.balance - amount
                    }
                    resources.getString(R.string.Deposit) -> {
                        Commons.balance = Commons.balance + amount
                    }
                    resources.getString(R.string.Transfer) -> {
                        Commons.balance = Commons.balance - amount
                    }
                }
                refreshBalance()

                val logging = Logging()
                logging.setAccNo("123")
                logging.setTransactionAmount(amount)
                logging.setTransactionType(type)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    logging.setTransactionDate(LocalDateTime.now().toString())
                }
                homePageViewModel.insertLogging(applicationContext, logging)
            }
        }
    }
}
