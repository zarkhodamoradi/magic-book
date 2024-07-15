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

import org.w3c.dom.Text;

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

    public static class ViewHolder extends RecyclerView.ViewHolder {


        TextView txtTitleCategory;
        RecyclerView recyclerView;
        bookArrayAdapter adapter;
        ArrayList<Book> booksList;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            txtTitleCategory = itemView.findViewById(R.id.titleChildRecyclerView);
            recyclerView = itemView.findViewById(R.id.childRecyclerView);


        }

        public void FillItem(final Category category) {

            booksList = new ArrayList<>();
            for (int i = 0; i < booksArrayList.size(); i++) {
                try {

                    if (booksArrayList.get(i).getCategory().toLowerCase().indexOf(category.getCategoryName().toLowerCase()) != -1) {

                        booksList.add(booksArrayList.get(i));
                    }
                } catch (Exception e) {
                }


            }
            category.setBooks(booksList);
            txtTitleCategory.setText(category.getCategoryName());
            adapter = new bookArrayAdapter(itemView.getContext(), R.layout.book, category.getBooks());
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(adapter);


        }
    }


}
