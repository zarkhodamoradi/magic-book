package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;




    public class bookArrayAdapter extends RecyclerView.Adapter<bookArrayAdapter.ViewHolder> {

        Context context;
        int resource;
        static ArrayList<Book> booksArrayList;

        public bookArrayAdapter(Context context, int resource, ArrayList<Book> booksArrayList) {
            this.context = context;
            this.resource = resource;
            this.booksArrayList = booksArrayList;
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
            holder.FillItem(booksArrayList.get(position));
        }

        @Override
        public int getItemCount() {
            return booksArrayList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            TextView txtTitle;
            TextView txtPrice;
            ImageView btnPurchase;
            ImageView imgBook ;


            public ViewHolder(@NonNull View itemView) {

                super(itemView);
                txtTitle = itemView.findViewById(R.id.txtTitle);
                txtPrice = itemView.findViewById(R.id.txtPrice);
                btnPurchase = itemView.findViewById(R.id.btnPurchase);
                imgBook = itemView.findViewById(R.id.imgBook);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Book book = booksArrayList.get(position);

                            // Create an intent to start the BookDetailActivity
                            Intent intent = new Intent(itemView.getContext(), BookDetailsActivity.class);
                            intent.putExtra("Title", book.getTitle());
                            intent.putExtra("Price", book.getPrice());
                            intent.putExtra("Description", book.getDescription());
                            intent.putExtra("imageUrl", book.getImage());
                            intent.putExtra("Category",book.getCategory());
                            intent.putExtra("Rating",book.getRating());
                            intent.putExtra("PublishDate",book.getPublishDate());
                            intent.putExtra("Author",book.getAuthor());

                            // Start the activity
                            itemView.getContext().startActivity(intent);
                        }
                    }
                });
            }

            public void FillItem(final Book book) {
                RequestQueue requestQueue = Volley.newRequestQueue(imgBook.getContext()); ;
                txtTitle.setText(book.getTitle() );
                if (book.getTitle().length()>12){
                    txtTitle.setText(book.getTitle().substring(0,12)+" ..." );
                }
                txtPrice.setText( "$" + book.getPrice() );
                try{
                    WebService.GetImage(book.getImage(),imgBook,requestQueue);
                }catch (Exception e){ e.printStackTrace();}


            }
        }


    }

