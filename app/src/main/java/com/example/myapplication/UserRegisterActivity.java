package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;
public class UserRegisterActivity extends AppCompatActivity {
    EditText firstNameInputEditText, lastNameInputEditText, userNameInputEditText, emailInputEditText, userPasswordInputEditText;
    MaterialButton registerBtn;
    TextView userLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        firstNameInputEditText = findViewById(R.id.firstNameInputEditText);
        lastNameInputEditText = findViewById(R.id.lastNameInputEditText);
        userNameInputEditText = findViewById(R.id.userNameInputEditText);
        emailInputEditText = findViewById(R.id.emailInputEditText);
        userPasswordInputEditText = findViewById(R.id.userPasswordInputEditText);
        registerBtn = findViewById(R.id.registerButton);
        userLoginBtn = findViewById(R.id.userLoginButton);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        userLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserRegisterActivity.this, UserLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser() {
        String url = "https://magicbooks.liara.run/magicbooks/user/create/";
        RequestQueue queue = Volley.newRequestQueue(this);

        // Create a JSON object with the user details
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("firstName", firstNameInputEditText.getText().toString());
            jsonBody.put("lastName", lastNameInputEditText.getText().toString());
            jsonBody.put("userName", userNameInputEditText.getText().toString());
            jsonBody.put("email", emailInputEditText.getText().toString());
            jsonBody.put("userPassword", userPasswordInputEditText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            Toast.makeText(UserRegisterActivity.this, message, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(UserRegisterActivity.this, UserLoginActivity.class);
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
                        Toast.makeText(UserRegisterActivity.this, "Invalid Data! Make Sure You Fill All The Fields", Toast.LENGTH_LONG).show();
                    }
                });

        queue.add(jsonObjectRequest);
    }

}
