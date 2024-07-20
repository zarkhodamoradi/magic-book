package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class PersonalAccountFragment extends Fragment {
    TextView PersonalAccountText ;
    RecyclerView PersonalAccountRecyclerView ;
   public static  ArrayList<Book> myBooks ;
    WebService webService ;
    Space spaceAboveProgressBarPersonalAccount ;
    Space spaceUnderProgressBarPersonalAccount ;
    ProgressBar PersonalAccountProgressBar ;
   bookArrayAdapter adapter ;


    public PersonalAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webService = new WebService();
        adapter = new bookArrayAdapter(this.getContext(), R.layout.book, myBooks);
        webService.SetupRequextQueue(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_personal_account, container, false);
        SetUpView(rootView);


        PersonalAccountRecyclerView.setItemAnimator(new DefaultItemAnimator());
        PersonalAccountRecyclerView.setLayoutManager(new GridLayoutManager(rootView.getContext(), 3));
        PersonalAccountRecyclerView.setAdapter(adapter);
        getUserLibraryBooks();
        return rootView;
    }

    private void SetUpView(View rootView){
        PersonalAccountRecyclerView = rootView.findViewById(R.id.PersonalAccountRecyclerView);
        PersonalAccountText = rootView.findViewById(R.id.PersonalAccountText);
        spaceAboveProgressBarPersonalAccount = rootView.findViewById(R.id.spaceAboveProgressBarPersonalAccount);
        spaceUnderProgressBarPersonalAccount = rootView.findViewById(R.id.spaceUnderProgressBarPersonalAccount);
        PersonalAccountProgressBar = rootView.findViewById(R.id.PersonalAccountProgressBar);
    }
    private void getUserLibraryBooks() {

        PersonalAccountRecyclerView.setVisibility(View.GONE);
        PersonalAccountProgressBar.setVisibility(View.VISIBLE);
        spaceUnderProgressBarPersonalAccount.setVisibility(View.VISIBLE);
        spaceAboveProgressBarPersonalAccount.setVisibility(View.VISIBLE);

        Handler handler = new Handler();
        new Thread(() -> {
            try {

                String json = webService.GetContentOfUrlConnection(webService.getBooksByUsername+MainActivity.USERNAME);
                if (json != null && !json.isEmpty()) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<Book>>() {
                    }.getType();
                    myBooks = gson.fromJson(json, listType);
                    if (myBooks != null) {
                        getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
                    }
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        PersonalAccountProgressBar.setVisibility(View.GONE);
                        spaceAboveProgressBarPersonalAccount.setVisibility(View.GONE);
                        spaceUnderProgressBarPersonalAccount.setVisibility(View.GONE);
                        PersonalAccountRecyclerView.setVisibility(View.VISIBLE);
                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}