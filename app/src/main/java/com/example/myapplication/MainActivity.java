package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionInflater;
import android.transition.Visibility;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    public static String USERNAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        SetUpView();

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
        USERNAME = sharedPreferences.getString("userName", "defaultUserName");
        fillingLibraryBooks();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Configure shared element transition
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));
            getWindow().setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));

        }
        bottomNavigationView.setSelectedItemId(R.id.discover);
        replaceFragment(new DiscoverFragment());
        PersonalAccountFragment.myBooks = new ArrayList<>();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int discover = R.id.discover;
                int profile = R.id.profile;
                int library = R.id.library;
                int id = item.getItemId();
                if (id == discover) {
                    replaceFragment(new DiscoverFragment());
                    return true;
                } else if (id == profile) {
                    replaceFragment(new ProfileFragment());
                    return true;
                } else if (id == library) {
                    replaceFragment(new PersonalAccountFragment());
                    return true;
                }

//                    case R.id.navigation_dashboard:
//                        startActivity(new Intent(MainActivity.this, Profile.class));
//                        return true;
//                    case R.id.navigation_notifications:
//                        startActivity(new Intent(MainActivity.this, Library.class));
//                        return true;

                return false;
            }
        });

    }

    private void SetUpView() {
        bottomNavigationView = findViewById(R.id.bottomNavigationview);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.botton_nav_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.discoverFrame, fragment);
        fragmentTransaction.commit();
    }

    private void fillingLibraryBooks() {
        WebService webService = new WebService();
        webService.SetupRequextQueue(this.getApplicationContext());


        Handler handler = new Handler();
        new Thread(() -> {
            try {

                String json = webService.GetContentOfUrlConnection(webService.getBooksByUsername + MainActivity.USERNAME);
                if (json != null && !json.isEmpty()) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<Book>>() {
                    }.getType();
                    PersonalAccountFragment.myBooks = gson.fromJson(json, listType);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}