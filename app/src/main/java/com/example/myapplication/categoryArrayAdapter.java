package com.example.myapplication;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class categoryArrayAdapter extends RecyclerView.Adapter<categoryArrayAdapter.ViewHolder> {

    Context context;
    int resource;
    static ArrayList<Category> categoriesArrayList;

    public categoryArrayAdapter(Context context, int resource, ArrayList<Category> categoriesArrayList) {
        this.context = context;
        this.resource = resource;
        this.categoriesArrayList = categoriesArrayList;
    }


    @NonNull
    @Override
    public categoryArrayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(resource, parent, false);
        categoryArrayAdapter.ViewHolder holder = new categoryArrayAdapter.ViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull categoryArrayAdapter.ViewHolder holder, int position) {
        holder.FillItem(categoriesArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return categoriesArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtCategoryTitle;
        RecyclerView booksRecyclerView;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            txtCategoryTitle = itemView.findViewById(R.id.txtCategoryTitle);
            booksRecyclerView = itemView.findViewById(R.id.recyclerViewCategory);

        }

        public void FillItem(final Category category) {


            txtCategoryTitle.setText( category.getCategoryName());



            // Initialize the BookAdapter with the book list of the category
            bookArrayAdapter bookArrayAdapter = new bookArrayAdapter(itemView.getContext(),R.layout.book,category.getBooks());
            booksRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            booksRecyclerView.setAdapter(bookArrayAdapter);

        }
    }


}