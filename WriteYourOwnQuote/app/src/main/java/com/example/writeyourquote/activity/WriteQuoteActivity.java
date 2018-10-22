package com.example.writeyourquote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.writeyourquote.util.CommonSharedPreference;
import com.example.writeyourquote.util.Commons;
import com.example.writeyourquote.R;
import com.example.writeyourquote.model.Quote;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WriteQuoteActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbarListWriteQuote;
    private Button btnSaveQuote;
    private String quoteId = "";
    private JSONArray quoteList = Commons.getQuotes();
    private EditText editTextQuoteDesc, editTextQuoteAuthor;
    private CoordinatorLayout coordinatorLayoutWriteQuote;
    CommonSharedPreference commonSharedPreference = CommonSharedPreference.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_quote);

        toolbarListWriteQuote = findViewById(R.id.toolbarListWriteQuote);
        btnSaveQuote = findViewById(R.id.btnSaveQuote);
        editTextQuoteDesc = findViewById(R.id.editTextQuoteDesc);
        editTextQuoteAuthor = findViewById(R.id.editTextQuoteAuthor);
        coordinatorLayoutWriteQuote = findViewById(R.id.coordinatorLayoutWriteQuote);

        setSupportActionBar(toolbarListWriteQuote);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnSaveQuote.setOnClickListener(this);
        if (getIntent().hasExtra("quoteId")) {
            quoteId = getIntent().getStringExtra("quoteId");
            updateDateUponField();
        }
    }

    private void updateDateUponField() {
        for (int i = 0; i < quoteList.length(); i++) {
            JSONObject quoteJSONObject = null;
            try {
                quoteJSONObject = new JSONObject(quoteList.getString(i));
                if (quoteId.matches(quoteJSONObject.getString("id"))) {
                    editTextQuoteDesc.setText(quoteJSONObject.getString("quotation"));
                    editTextQuoteAuthor.setText(quoteJSONObject.getString("authorName"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkValidationsWriteQuote() {
        String validationMessage = "";
        if (Commons.checkIsViewEmpty(editTextQuoteDesc)) {
            validationMessage = getResources().getString(R.string.enterwriteQuote);
        } else if (Commons.checkIsViewEmpty(editTextQuoteAuthor)) {
            validationMessage = getResources().getString(R.string.enterquoteAuthor);
        }

        if (!validationMessage.matches("")) {
            Commons.showSnakbarWithMessage(this, coordinatorLayoutWriteQuote, validationMessage);
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSaveQuote) {
            if (checkValidationsWriteQuote()) {
                // JSONObject userObject = null;
                if (commonSharedPreference.containsKey("LoggedEmail")) {
                    //userObject = new JSONObject(commonSharedPreference.getString("LoggedInUser"));
                    String email = "";
                    email = commonSharedPreference.getString("LoggedEmail");
                    Quote quote = new Quote(Commons.getRandomInteger(),
                            Commons.upperCaseFirstLetter((Commons.readTextFromView(editTextQuoteAuthor))),
                            Commons.readTextFromView(editTextQuoteDesc),
                            Commons.getCurrentDateAndTime(),
                            email);

                    Intent previous = new Intent(WriteQuoteActivity.this, ListQuotesActivity.class);
                    previous.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    previous.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    if (!quoteId.matches("")) {
                        Commons.removeQuoteBasedOnId(quoteId);
                        Toast.makeText(this, getResources().getString(R.string.editedQuotation), Toast.LENGTH_SHORT).show();
                        previous.putExtra("needsRefresh", true);
                        quoteList = Commons.filterQuotesBasedOnLoginEmail();
                    } else {
                        previous.putExtra("needsRefresh", false);
                        Toast.makeText(this, getResources().getString(R.string.addedQuotation), Toast.LENGTH_SHORT).show();
                    }
                    String quoteString = Commons.convertQuoteModelToJSONObject(quote);
                    quoteList.put(quoteString);
                    commonSharedPreference.put("quotes", quoteList.toString());
                    previous.putExtra("quote", quoteString);
                    startActivity(previous);
                    finish();
                }

            }
        }
    }
}
