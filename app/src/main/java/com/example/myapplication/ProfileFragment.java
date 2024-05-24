package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private Button logoutButton;
    private ImageView userImage;
    private TextView userName;
    private TextView userEmail;

    private ProgressBar loadingSpinner;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        logoutButton = rootView.findViewById(R.id.logoutBtn);
        userImage = rootView.findViewById(R.id.userImage);
        userName = rootView.findViewById(R.id.userName);
        userEmail = rootView.findViewById(R.id.userEmail);
        loadingSpinner = rootView.findViewById(R.id.loadingSpinner);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        // Fetch user data from the server
        fetchUserData();

        return rootView;
    }

    private void logOut() {
        // Clear the SharedPreferences since the jwt is held there
        SharedPreferences preferences = this.getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        preferences.edit().clear().commit();

        // Redirect to UserLoginActivity
        Intent intent = new Intent(getActivity(), UserLoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void fetchUserData() {
        // Loading state
        loadingSpinner.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        String jwt = sharedPreferences.getString("jwt", "");

        String url = "https://mangareader.liara.run/user/read/";

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            User user = new User();
                            user.setUserID(response.getInt("userID"));
                            user.setUserName(response.getString("userName"));
                            user.setFirstName(response.getString("firstName"));
                            user.setLastName(response.getString("lastName"));
                            user.setEmail(response.getString("email"));
                            user.setImage(response.getString("picture"));

                            userName.setText(user.getUserName());
                            userEmail.setText(user.getEmail());

                            // Load the image from the URL into the ImageView
                            GetImage(user.getImage(), userImage, queue);
                            loadingSpinner.setVisibility(View.GONE);
                            userImage.setVisibility(View.VISIBLE);
                            userName.setVisibility(View.VISIBLE);
                            userEmail.setVisibility(View.VISIBLE);
                            logoutButton.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        loadingSpinner.setVisibility(View.GONE);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + jwt);
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }


    public static void GetImage(String url, ImageView img, RequestQueue requestQueue) {

        ImageRequest request = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        img.setImageBitmap(bitmap);
                    }
                },
                120,
                120,
                ImageView.ScaleType.FIT_XY,
                Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                });
        requestQueue.add(request);
    }
}