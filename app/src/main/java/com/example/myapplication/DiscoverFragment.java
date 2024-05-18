package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class DiscoverFragment extends Fragment {

    private RecyclerView recyclerViewDiscover;
    private SearchView searchViewDiscover;
    private ArrayList<Book> booksList;
    private bookArrayAdapter adapter;
    private ArrayList<Book> AllOfTheBooks;
    private WebService webService;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AllOfTheBooks = new ArrayList<>(); // Initialize here to avoid null reference
        booksList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discover, container, false);
        recyclerViewDiscover = rootView.findViewById(R.id.recyclerViewDiscover);
        searchViewDiscover = rootView.findViewById(R.id.searchViewDiscover);
        webService = new WebService();
        webService.SetupRequextQueue(rootView.getContext());

        // Set up RecyclerView
        adapter = new bookArrayAdapter(rootView.getContext(), R.layout.book, booksList);
        recyclerViewDiscover.setItemAnimator(new DefaultItemAnimator());
        recyclerViewDiscover.setLayoutManager(new GridLayoutManager(rootView.getContext(), 3));
        recyclerViewDiscover.setAdapter(adapter);

        // Fetch data and update UI
        fetchDataAndFillList();

        // Set up SearchView
        searchViewDiscover.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchBooks(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchBooks(newText);
                return false;
            }
        });
        searchViewDiscover.setOnCloseListener(() -> {
            booksList.clear();
            booksList.addAll(AllOfTheBooks);
            adapter.notifyDataSetChanged();
            return false;
        });

        return rootView;
    }

    private void fetchDataAndFillList() {
        new Thread(() -> {
            try {
                String json = webService.GetContentOfUrlConnection(webService.GetByUrl);
                if (json != null && !json.isEmpty()) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<Book>>() {}.getType();
                    AllOfTheBooks = gson.fromJson(json, listType);
                    if (AllOfTheBooks != null) {
                        booksList.clear();
                        booksList.addAll(AllOfTheBooks);
                        getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void searchBooks(String query) {
        new Thread(() -> {
            try {
                String searchBooksJson = webService.GetContentOfUrlConnection(webService.URLSearchByTitle + query);
                if (searchBooksJson != null && !searchBooksJson.isEmpty()) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<Book>>() {}.getType();
                    ArrayList<Book> searchResults = gson.fromJson(searchBooksJson, listType);
                    if (searchResults != null) {
                        getActivity().runOnUiThread(() -> {
                            booksList.clear();
                            booksList.addAll(searchResults);
                            adapter.notifyDataSetChanged();
                        });
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
