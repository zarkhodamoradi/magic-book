package com.example.myapplication;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class majorArrayAdapter extends RecyclerView.Adapter<majorArrayAdapter.ViewHolder> {

     Context context;
    int resource;
    static ArrayList<Book> booksArrayList;
    ArrayList<Category> categories;



    public majorArrayAdapter(Context context, int resource, ArrayList<Book> booksArrayList, ArrayList<Category> categories) {
        this.context = context;
        this.resource = resource;
        this.booksArrayList = booksArrayList;
        this.categories = categories;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(resource, parent, false);
        ViewHolder holder = new ViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.FillItem(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {


        TextView txtTitleCategory;
        RecyclerView recyclerView;
        bookArrayAdapter adapter;
        ArrayList<Book> tempList;
        WebService webService ;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            txtTitleCategory = itemView.findViewById(R.id.titleChildRecyclerView);
            recyclerView = itemView.findViewById(R.id.childRecyclerView);
            tempList = new ArrayList<Book>();
            webService = new WebService();
            webService.SetupRequextQueue(itemView.getContext());

        }

        public void FillItem( Category category) {


            tempList.clear();
//            for (int i = 0; i < booksArrayList.size(); i++) {
//                try {
//
//                    if (booksArrayList.get(i).getCategory().toLowerCase().indexOf(category.getCategoryName().toLowerCase()) != -1) {
//
//                        tempList.add(booksArrayList.get(i));
//                    }
//                } catch (Exception e) {
//                }
//
//
//            }
        tempList = getBooksByCategory(category.getCategoryName());


            category.setBooks(tempList);
            txtTitleCategory.setText(category.getCategoryName());
            adapter = new bookArrayAdapter(itemView.getContext(), R.layout.book, category.getBooks());
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }

        private ArrayList<Book> getBooksByCategory(String query) {
           ArrayList<Book> books = new ArrayList<Book>();
            new Thread(() -> {
                try {

                    String searchBooksJson = webService.GetContentOfUrlConnection(webService.getBooksByCategory + query);
                    if (searchBooksJson != null && !searchBooksJson.isEmpty()) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<ArrayList<Book>>() {
                        }.getType();
                        ArrayList<Book> searchResults = gson.fromJson(searchBooksJson, listType);
                        if (searchResults != null) {

                            ((Activity)context).runOnUiThread(() -> {
                                books.addAll(searchResults);
                                adapter.notifyDataSetChanged();

                            });
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            return books;
        }
    }


}
