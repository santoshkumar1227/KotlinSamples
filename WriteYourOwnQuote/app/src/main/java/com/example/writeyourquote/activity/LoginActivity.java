package com.example.writeyourquote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.writeyourquote.BuildConfig;
import com.example.writeyourquote.util.CommonSharedPreference;
import com.example.writeyourquote.util.Commons;
import com.example.writeyourquote.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnLogin, btnNoAccountYet;
    private TextView textViewVersion;
    private EditText editTextEmailLogin, editTextPasswordLogin;
    private CoordinatorLayout coordinatorLayoutLogin;
    private static String[] emails = {"test@test.com", "test1@test.com"};
    CommonSharedPreference commonSharedPreference = CommonSharedPreference.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btnLogin);
        btnNoAccountYet = findViewById(R.id.btnNoAccountYet);
        btnLogin = findViewById(R.id.btnLogin);
        textViewVersion = findViewById(R.id.textViewVersion);
        editTextEmailLogin = findViewById(R.id.editTextEmailLogin);
        editTextPasswordLogin = findViewById(R.id.editTextPasswordLogin);
        coordinatorLayoutLogin = findViewById(R.id.coordinatorLayoutLogin);
        btnLogin.setOnClickListener(this);
        btnNoAccountYet.setOnClickListener(this);
        textViewVersion.setText("Version :: " + BuildConfig.VERSION_CODE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnLogin) {
            if (checkLoginValidation()) {
                if (checkUserLoginExists()) {
                    Toast.makeText(this, getResources().getString(R.string.successLogin).toString(), Toast.LENGTH_SHORT);
                    startActivity(new Intent(this, ListQuotesActivity.class));
                    finish();
                } else {
                    Commons.showSnakbarWithMessage(this, coordinatorLayoutLogin, getResources().getString(R.string.userNotExists));
                }
            }
        }
    }

    private boolean checkLoginValidation() {
        String validationMessage = "";
        if (Commons.checkIsViewEmpty(editTextEmailLogin)) {
            validationMessage = getResources().getString(R.string.enterEmail);
        } else if (Commons.checkIsViewEmpty(editTextPasswordLogin)) {
            validationMessage = getResources().getString(R.string.enterPassword);
        } else if (Commons.checkIsViewContainsValidEmail(editTextEmailLogin)) {
            validationMessage = getResources().getString(R.string.enterValidEmail);
        } else if (Commons.checkIsPasswordLengthNotEnough(editTextPasswordLogin)) {
            validationMessage = getResources().getString(R.string.minPassword);
        }

        if (!validationMessage.matches("")) {
            Commons.showSnakbarWithMessage(this, coordinatorLayoutLogin, validationMessage);
            return false;
        } else
            return true;
    }

    private boolean checkUserLoginExists() {
        /*if (commonSharedPreference.containsKey("users")) {
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(commonSharedPreference.getString("users"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject oneUserJsonObject = new JSONObject((String) jsonArray.get(i));
                    if (oneUserJsonObject.has("email") &&
                            oneUserJsonObject.getString("email") == CommonUtil.Validations.readTextFromView(editTextEmailLogin) &&
                            oneUserJsonObject.has("password") &&
                            oneUserJsonObject.getString("password") == CommonUtil.Validations.readTextFromView(editTextPasswordLogin)) {
                        commonSharedPreference.put("LoggedIn", true);
                        commonSharedPreference.put("LoggedInUser", oneUserJsonObject.toString());
                        commonSharedPreference.put("LoggedEmail", oneUserJsonObject.getString("email"));
                        return true;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }*/
        for (int i = 0; i < emails.length; i++) {
            if (Commons.readTextFromView(editTextEmailLogin).equalsIgnoreCase(emails[i])) {
                commonSharedPreference.put("LoggedIn", true);
                commonSharedPreference.put("LoggedEmail", Commons.readTextFromView(editTextEmailLogin));
                return true;
            }
        }
        return false;
    }

}
