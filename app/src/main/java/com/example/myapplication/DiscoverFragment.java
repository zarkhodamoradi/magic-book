package com.example.myapplication;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Space;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class DiscoverFragment extends Fragment {

    private RecyclerView recyclerViewDiscover;
    private RecyclerView parentRecyclerViewDiscover;
    private SearchView searchViewDiscover;
    private ArrayList<Book> booksList;
    private bookArrayAdapter adapter;
    private ArrayList<Book> AllOfTheBooks;
    private WebService webService;
    private ProgressBar progressBar;
    private Space spaceAboveProgressBarDiscoverFragment;
    private Space spaceUnderProgressBarDiscoverFragment;

    private majorArrayAdapter majorAdapter;
    private ArrayList<Category> categories;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public DiscoverFragment() {
        // Required empty public constructor
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AllOfTheBooks = new ArrayList<>();
        booksList = new ArrayList<>();
        categories = new ArrayList<Category>();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discover, container, false);
        recyclerViewDiscover = rootView.findViewById(R.id.recyclerViewDiscover);
        searchViewDiscover = rootView.findViewById(R.id.searchViewDiscover);
        progressBar = rootView.findViewById(R.id.progressBarDiscover);
        spaceAboveProgressBarDiscoverFragment = rootView.findViewById(R.id.spaceAboveProgressBarDiscoverFragment);
        spaceUnderProgressBarDiscoverFragment = rootView.findViewById(R.id.spaceUnderProgressBarDiscoverFragment);
        parentRecyclerViewDiscover = rootView.findViewById(R.id.parentRecyclerViewDiscover);


        webService = new WebService();
        webService.SetupRequextQueue(rootView.getContext());




        fetchDataAndFillList();
        getCategories(rootView);
//        majorAdapter = new majorArrayAdapter(rootView.getContext(), R.layout.child_recycler_view, booksList, categories);
//        parentRecyclerViewDiscover.setLayoutManager(new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false));
//        parentRecyclerViewDiscover.setAdapter(majorAdapter);


        adapter = new bookArrayAdapter(rootView.getContext(), R.layout.book, booksList);
        recyclerViewDiscover.setItemAnimator(new DefaultItemAnimator());
        recyclerViewDiscover.setLayoutManager(new GridLayoutManager(rootView.getContext(), 3));
        recyclerViewDiscover.setAdapter(adapter);









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
            parentRecyclerViewDiscover.setVisibility(View.VISIBLE);
            recyclerViewDiscover.setVisibility(View.GONE);
            return false;
        });

        return rootView;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void fetchDataAndFillList() {
        parentRecyclerViewDiscover.setVisibility(View.GONE);
        recyclerViewDiscover.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        spaceAboveProgressBarDiscoverFragment.setVisibility(View.VISIBLE);
        spaceUnderProgressBarDiscoverFragment.setVisibility(View.VISIBLE);

        Handler handler = new Handler();
        new Thread(() -> {
            try {

                String json = webService.GetContentOfUrlConnection(webService.GetByUrl);
                if (json != null && !json.isEmpty()) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<Book>>() {
                    }.getType();
                    AllOfTheBooks = gson.fromJson(json, listType);
                    if (AllOfTheBooks != null) {
                        booksList.clear();
                        booksList.addAll(AllOfTheBooks);
                        getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
                        getActivity().runOnUiThread(() -> majorAdapter.notifyDataSetChanged());
                    }
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        spaceAboveProgressBarDiscoverFragment.setVisibility(View.GONE);
                        spaceUnderProgressBarDiscoverFragment.setVisibility(View.GONE);

                        parentRecyclerViewDiscover.setVisibility(View.VISIBLE);
                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void getCategories(View rootView) {
        Handler handler = new Handler();
        new Thread(() -> {
            try {
                String json = webService.GetContentOfUrlConnection(webService.GetCategories);
                if (json != null && !json.isEmpty()) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<Category>>() {}.getType();
                    categories = gson.fromJson(json, listType);

                    if (categories != null) {
                        getActivity().runOnUiThread(() -> {
                            majorAdapter = new majorArrayAdapter(rootView.getContext(), R.layout.child_recycler_view, booksList, categories);
                            parentRecyclerViewDiscover.setLayoutManager(new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false));
                            parentRecyclerViewDiscover.setAdapter(majorAdapter);
                            majorAdapter.notifyDataSetChanged();
                        });
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void searchBooks(String query) {
        parentRecyclerViewDiscover.setVisibility(View.GONE);
        recyclerViewDiscover.setVisibility(View.VISIBLE);
        new Thread(() -> {
            try {

                String searchBooksJson = webService.GetContentOfUrlConnection(webService.URLSearchByTitle + query);
                if (searchBooksJson != null && !searchBooksJson.isEmpty()) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<Book>>() {
                    }.getType();
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
