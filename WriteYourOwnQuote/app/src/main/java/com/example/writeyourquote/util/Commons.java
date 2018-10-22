package com.example.writeyourquote.util;

import android.app.Activity;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.writeyourquote.R;
import com.example.writeyourquote.model.Quote;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class Commons {
    public static boolean checkUserEmailExists(String email) {
        CommonSharedPreference commonSharedPreference = CommonSharedPreference.getInstance();
        if (commonSharedPreference.containsKey("users")) {
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(commonSharedPreference.getString("users"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject oneUserJsonObject = new JSONObject((String) jsonArray.get(i));
                    if (oneUserJsonObject.has("email") && oneUserJsonObject.getString("email") == email) {
                        return true;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    public static JSONArray filterQuotesBasedOnLoginEmail() {
        CommonSharedPreference commonSharedPreference = CommonSharedPreference.getInstance();
        JSONArray quoteList = getQuotes();
        JSONArray updatedQuoteList = new JSONArray();
        for (int i = 0; i < quoteList.length(); i++) {
            JSONObject quoteObject = null;
            try {
                quoteObject = new JSONObject(quoteList.getString(i));
                if (commonSharedPreference.getString("LoggedEmail").
                        equals(quoteObject.getString("quoteAddedBy")))
                    updatedQuoteList.put(quoteList.get(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return updatedQuoteList;
    }

    public static JSONArray getQuotes() {
        JSONArray quoteList = new JSONArray();
        CommonSharedPreference commonSharedPreference = CommonSharedPreference.getInstance();
        if (commonSharedPreference.containsKey("quotes")) {
            try {
                quoteList = new JSONArray(commonSharedPreference.getString("quotes"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return quoteList;
    }

    public static boolean checkIsViewEmpty(View view) {
        if (view != null) {
            if (view instanceof AppCompatEditText) {
                return TextUtils.isEmpty(((AppCompatEditText) view).getText());
            }
        }
        return false;
    }

    public static boolean checkIsViewContainsValidEmail(View view) {
        if (view != null) {
            if (view instanceof AppCompatEditText) {
                return !android.util.Patterns.EMAIL_ADDRESS.matcher(((AppCompatEditText) view).getText()).matches();
            }
        }
        return false;
    }

    public static boolean checkIsPasswordLengthNotEnough(View view) {
        if (view != null) {
            if (view instanceof AppCompatEditText) {
                return getTextLengthOfView(view) < 6;
            }
        }
        return false;
    }

    public static int getTextLengthOfView(View view) {
        if (view != null) {
            if (view instanceof AppCompatEditText) {
                return ((AppCompatEditText) view).getText().toString().trim().length();
            }
        }
        return 0;
    }

    public static String readTextFromView(View view) {
        if (view != null) {
            if (view instanceof AppCompatEditText) {
                return ((AppCompatEditText) view).getText().toString().trim();
            }
        }
        return "";
    }

    public static void showSnakbarWithMessage(Activity activity, View view, String message) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_SHORT);
        // Changing message text color
        snackbar.setActionTextColor(ContextCompat.getColor(activity, R.color.primary_dark));
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(activity, R.color.yellow));
        snackbar.show();
    }

    public static String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa", Locale.US);
        String datetime = dateformat.format(c.getTime());
        return datetime;
    }

    public static int getRandomInteger() {
        Random r = new Random();
        return r.nextInt(999999 - 111111) + 1;
    }

    public static String upperCaseFirstLetter(String text) {
        return text.toLowerCase().substring(0, 1).toUpperCase() + "" + (text.substring(1));
    }

    public static void removeQuoteBasedOnId(String quoteId) {
        CommonSharedPreference commonSharedPreference = CommonSharedPreference.getInstance();
        JSONArray quoteList;
        if (commonSharedPreference.containsKey("quotes")) {
            if (commonSharedPreference.containsKey("quotes")) {
                quoteList = filterQuotesBasedOnLoginEmail();
                for (int i = 0; i < quoteList.length(); i++) {
                    JSONObject quoteObject = null;
                    try {
                        quoteObject = new JSONObject(quoteList.getString(i));
                        if (quoteObject.getString("id").matches(quoteId) ) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                quoteList.remove(i);
                            }
                            commonSharedPreference.put("quotes", quoteList.toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static String convertQuoteModelToJSONObject(Quote quote) {
        Gson gson = new Gson();
        String json = gson.toJson(quote);
        Log.e("Params", json);
        return json;
    }

}
