package com.example.myapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SetUpView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Configure shared element transition
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));
            getWindow().setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));
        }
        bottomNavigationView.setSelectedItemId(R.id.discover);
        replaceFragment(new DiscoverFragment());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int discover = R.id.discover;
                int profile = R.id.profile ;
                int library = R.id.library ;
                int id = item.getItemId();
                if (id == discover) {
                   replaceFragment(new DiscoverFragment());
                    return true;
                }
                else if(id == profile){
                    replaceFragment(new DiscoverFragment());
                    return true;
                }
                else if (id == library){
                    replaceFragment(new DiscoverFragment());
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

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.discoverFrame , fragment);
        fragmentTransaction.commit();
    }


}