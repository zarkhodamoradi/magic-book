package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONException;
import org.json.JSONObject;

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

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateDialog("profile picture", "Enter new profile picture URL", "picture");
            }
        });

        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateDialog("username", "Enter new username", "userName");
            }
        });

        userEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateDialog("email", "Enter new email", "email");
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

        String url = "https://magicbooks.liara.run/magicbooks/user/read/";

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

                            // Check for null email and set a default value if necessary
                            String email = response.isNull("email") ? "No email provided" : response.getString("email");
                            user.setEmail(email);

                            // Check for null picture URL
                            String imageUrl = response.isNull("picture") ? "" : response.getString("picture");
                            user.setImage(imageUrl);

                            userName.setText(user.getUserName());
                            userEmail.setText(user.getEmail());

                            // Load the image from the URL into the ImageView if the URL is not null or empty
                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                GetImage(imageUrl, userImage, queue);
                            }

                            loadingSpinner.setVisibility(View.GONE);
                            userImage.setVisibility(View.VISIBLE);
                            userName.setVisibility(View.VISIBLE);
                            userEmail.setVisibility(View.VISIBLE);
                            logoutButton.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingSpinner.setVisibility(View.GONE);
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

    private void showUpdateDialog(String title, String hint, String field) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());

        View viewInflated = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_update_profile, (ViewGroup) getView(), false);
        final EditText input = viewInflated.findViewById(R.id.editTextValue);
        input.setHint(hint);
        final TextView dialogTitle = viewInflated.findViewById(R.id.updateTitle);
        dialogTitle.setText("Update " + title);

        Button updateButton = viewInflated.findViewById(R.id.buttonUpdate);
        Button cancelButton = viewInflated.findViewById(R.id.buttonCancel);

        builder.setView(viewInflated);

        final androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newValue = input.getText().toString().trim();
                        if (newValue.isEmpty()) {
                            input.setError("This field cannot be empty");
                        } else {
                            dialog.dismiss();
                            updateUserProfile(field, newValue);
                        }
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.7f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }

    private void updateUserProfile(String field, String newValue) {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        String jwt = sharedPreferences.getString("jwt", "");

        String url = "https://magicbooks.liara.run/magicbooks/user/update/";

        Map<String, String> params = new HashMap<>();
        params.put("field", field);
        params.put("newValue", newValue);

        JSONObject jsonRequest = new JSONObject(params);

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonRequest, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                                // Handle the response and update the UI
                                if (field.equals("userName")) {
                                    userName.setText(response.getString("userName"));
                                    // Get the new JWT and save it in shared preferences
                                    String jwt = response.getString("jwt");
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("jwt", jwt);
                                    editor.apply();
                                } else if (field.equals("email")) {
                                    userEmail.setText(response.getString("email"));
                                } else if (field.equals("picture")) {
                                    String imageUrl = response.getString("picture");
                                    GetImage(imageUrl, userImage, queue);
                                }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + jwt);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public static void GetImage(String url, ImageView img, RequestQueue requestQueue) {
        if (url == null || url.equals("NULL") || url.isEmpty()) {
            return;
        }

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
                        System.err.println("Image request failed: " + volleyError.getMessage());
                    }
                });

        requestQueue.add(request);
    }
}
