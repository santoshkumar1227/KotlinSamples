package com.example.writeyourquote.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.writeyourquote.util.CommonSharedPreference;
import com.example.writeyourquote.util.Commons;
import com.example.writeyourquote.R;
import com.example.writeyourquote.adapter.QuoteListAdapter;
import com.example.writeyourquote.model.Quote;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListQuotesActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList listOfQuotes = new ArrayList<Quote>();
    private Toolbar toolbarList;
    private FloatingActionButton fabCreateQuote;
    private RecyclerView recyclerQuotes;
    private CommonSharedPreference commonSharedPreference = CommonSharedPreference.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_quotes);

        toolbarList = findViewById(R.id.toolbarList);
        fabCreateQuote = findViewById(R.id.fabCreateQuote);
        recyclerQuotes = findViewById(R.id.recyclerQuotes);

        setSupportActionBar(toolbarList);
        fabCreateQuote.setOnClickListener(this);
        showListOfQuotes();
    }

    public void showListOfQuotes() {
        listOfQuotes.clear();
        if (commonSharedPreference.containsKey("quotes")) {
            JSONArray quotesArray = Commons.filterQuotesBasedOnLoginEmail();
            for (int i = 0; i < quotesArray.length(); i++) {
                String quoteString = null;
                try {
                    quoteString = quotesArray.getString(i);
                    JSONObject quoteJSONObject = new JSONObject(quoteString);
                    if (quoteJSONObject.has("id") &&
                            quoteJSONObject.has("quotation") &&
                            quoteJSONObject.has("authorName") &&
                            quoteJSONObject.has("quotationDate")) {
                        Quote quote = new Quote(quoteJSONObject.getInt("id"),
                                quoteJSONObject.getString("quotation"),
                                quoteJSONObject.getString("authorName"),
                                quoteJSONObject.getString("quotationDate"),
                                quoteJSONObject.getString("quoteAddedBy"));
                        listOfQuotes.add(quote);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        //Add a LayoutManager
        recyclerQuotes.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        recyclerQuotes.setAdapter(new QuoteListAdapter(this, listOfQuotes));

        if (listOfQuotes.size() == 0) {
            // CommonUtil.showToast(this, getResources().getString(R.string.noQuotesNoted))
            Toast.makeText(this, getResources().getString(R.string.noQuotesNoted), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fabCreateQuote) {
            startActivity(new Intent(this, WriteQuoteActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //Uncomment the below code to Set the message and title from the strings.xml file
            //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);

            //Setting message manually and performing action on button click
            builder.setMessage("Do you want to Logout ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(ListQuotesActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            commonSharedPreference.put("LoggedEmail","");
                            commonSharedPreference.put("LoggedIn",false);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            dialog.cancel();
                        }
                    });

            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("Logout");
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }

    //
//@Override
//public boolean onOptionsItemSelected(MenuItem item) {
//switch (item.getItemId()) {
//case R.id.logout: {
//CommonUtil.Validations.getAlertDialogIncludesInterfaceListenerToCaller(
//this,
//getResources().getString(R.string.doYouwantLogout),
//getResources().getString(R.string.logout),
//true,
//getResources().getString(R.string.cancel),
//getResources().getString(R.string.ok),
//false,
//new BooleanStatusCallback() {
//@Override
//public void callBack(boolean call) {
//if (call) {
//MySharedPref mySharedPref = new MySharedPref();
//mySharedPref.remove("LoggedIn");
//mySharedPref.remove("LoggedInUser");
//Intent loginCall = new Intent(ListQuotesActivity.this, LoginActivity.class);
//loginCall.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//loginCall.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//startActivity(loginCall);
//finish();
//}
//
//}
//});
//}
//
//
//R.id.changePwd ->{
//navigate<ChangePasswordActivity> (needsFinish = false)
//}
//R.id.viewProfile ->{
//navigate<ProfileActivity> (needsFinish = false)
//}
//else ->return super.onOptionsItemSelected(item)
//}
//return super.
//
//onOptionsItemSelected(item);
//}
}
