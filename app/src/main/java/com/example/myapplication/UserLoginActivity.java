package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserLoginActivity extends AppCompatActivity {
    private TextInputLayout usernameInputLayout, passwordInputLayout;
    private TextInputEditText usernameEditText, passwordEditText;
    private MaterialButton loginButton;
    private TextView adminLoginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
        String jwt = sharedPreferences.getString("jwt", "");
        String userType = sharedPreferences.getString("userType", "");
        if (!jwt.equals("")) {
            if (userType.equals("Admin")) {
                // change this to Admin Login Acitivty later
                Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
        initializeViews();

        loginButton.setOnClickListener(view -> handleLogin());
        adminLoginButton.setOnClickListener(view -> handleAdminLogin());
    }

    private void handleLogin() {
        String enteredUsername = Objects.requireNonNull(usernameEditText.getText()).toString().trim();
        String enteredPassword = Objects.requireNonNull(passwordEditText.getText()).toString().trim();
        String endpoint = "https://mangareader.liara.run/auth/login/user/";
        RequestQueue queue = Volley.newRequestQueue(this);

        // Create a JSON object with the username and password
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", enteredUsername);
            jsonBody.put("password", enteredPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, endpoint, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String jwt = response.getString("jwt");
                            SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
                            SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
                            sharedPrefEditor.putString("jwt", jwt);
                            sharedPrefEditor.putString("userType", "User");
                            sharedPrefEditor.apply();
                            Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UserLoginActivity.this, "Invalid Username Or Password", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonObjectRequest);
    }

    private void handleAdminLogin() {
        Intent intent = new Intent(UserLoginActivity.this, AdminLoginActivity.class);
        startActivity(intent);
        finish();
    }


    private void initializeViews() {
        usernameInputLayout = findViewById(R.id.userNameInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        usernameEditText = findViewById(R.id.userNameInputEditText);
        passwordEditText = findViewById(R.id.passwordInputEditText);
        loginButton = findViewById(R.id.loginButton);
        adminLoginButton = findViewById(R.id.loginAdminButton);
    }
}