package com.example.myapplication;

import android.app.DownloadManager;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class BookDetailsActivity extends AppCompatActivity {

    ImageView imgBookDetailsImage;
    TextView txtBookDetailsTitle;
    TextView txtBookDetailsAuthor;
    TextView txtBookDetailsRatingOutOf5;
    RatingBar ratingBarBookDetails;
    TextView txtToatalNumberOfRates;
    ImageView imgBookDetailsLike;
    ImageView imgBookDetailsSave;
    TextView txtBookDetailsDescription;
    TextView txtBookDetailsCategory ;
    ImageView imgBookDetailsDownload ;
    ImageView imgBookDetailsReading ;
    int Id ;
    String Title ;
    Integer Price ;
    String Description ;
    String ImageUrl ;
    String Category ;
    Double Rating ;
    String PublishDate ;
    String Author ;
    String Book_Link ;
boolean isSaved ;
boolean isLiked ;
    WebService webService ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SetUpView();
         webService = new WebService();
        webService.SetupRequextQueue(this );
fillingLibraryBooks();

        try {
            Bundle bundle = getIntent().getExtras();
            Id = bundle.getInt("Id");
             Title = bundle.getString("Title");
             Price = bundle.getInt("Price");
             Description = bundle.getString("Description");
             ImageUrl = bundle.getString("imageUrl");
             Category = bundle.getString("Category");
             Rating = bundle.getDouble("Rating");
             PublishDate = bundle.getString("PublishDate");
             Author = bundle.getString("Author");
             Book_Link = bundle.getString("Book_Link");
             isSaved = bundle.getBoolean("isSaved");
            isLiked = bundle.getBoolean("isLiked");

            txtBookDetailsTitle.setText(Title);
            txtBookDetailsAuthor.setText("by "+Author);
            txtBookDetailsDescription.setText(Description);
            txtBookDetailsRatingOutOf5.setText(Rating+"");
            txtToatalNumberOfRates.setText("100 ratings");
            imgBookDetailsImage.setTransitionName("bookImage");
            txtBookDetailsCategory.setText(Category);
            RequestQueue requestQueue = Volley.newRequestQueue(imgBookDetailsImage.getContext()); ;


            try{
                WebService.GetImage(ImageUrl,imgBookDetailsImage,requestQueue);
            }catch (Exception e){ e.printStackTrace();}

        }catch (Exception e){
            e.printStackTrace();
        }


        isSaved = false ;
        for (int i = 0; i < PersonalAccountFragment.myBooks.size(); i++) {
            if (Id == PersonalAccountFragment.myBooks.get(i).getId()){
                isSaved = true ;
            }
        }
        if (isSaved==true){
            imgBookDetailsSave.setImageResource(R.drawable.baseline_bookmark_24);
        }
        else {
            imgBookDetailsSave.setImageResource(R.drawable.baseline_bookmark_border_24);
        }
        imgBookDetailsSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSaved = !isSaved;
                Animate(imgBookDetailsSave);
                if (isSaved == false){
                    imgBookDetailsSave.setImageResource(R.drawable.baseline_bookmark_border_24);
                Update(webService.deleteSavedBook);
                fillingLibraryBooks();
                }
                else {
                    imgBookDetailsSave.setImageResource(R.drawable.baseline_bookmark_24);
                  Update(webService.insertBookToUserLibrary);
                  fillingLibraryBooks();
                }
                Animate(imgBookDetailsSave);
            }
        });
        final boolean[] like = {false};
        imgBookDetailsLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                like[0] = !like[0];

                if (like[0] == false){
                    imgBookDetailsLike.setImageResource(R.drawable.favorite_24px);
                }
                else {
                    imgBookDetailsLike.setImageResource(R.drawable.favorite_24px_filled);

                }
                Animate(imgBookDetailsLike);
            }
        });

        imgBookDetailsDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( Book_Link == null){
                    Toast.makeText(BookDetailsActivity.this, "book link is empty", Toast.LENGTH_SHORT).show();
                }else {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(Book_Link));
                    request.setTitle("Downloading Book");
                    request.setDescription("Downloading book from server...");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "book.pdf");

                    DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    downloadManager.enqueue(request);
                   // Toast.makeText(BookDetailsActivity.this, "Downloading started...", Toast.LENGTH_SHORT).show();
                    Snackbar.make(findViewById(android.R.id.content), "Download started...", Snackbar.LENGTH_LONG).show();
                   Animate(imgBookDetailsDownload);
                }
            }
        });
    }

    private void SetUpView() {
        imgBookDetailsImage = findViewById(R.id.imgBookDetailsImage);
        txtBookDetailsTitle = findViewById(R.id.txtBookDetailsTitle);
        txtBookDetailsAuthor = findViewById(R.id.txtBookDetailsAuthor);
        txtBookDetailsRatingOutOf5 = findViewById(R.id.txtBookDetailsRatingOutOf5);
        ratingBarBookDetails = findViewById(R.id.ratingBarBookDetails);
        txtToatalNumberOfRates = findViewById(R.id.txtToatalNumberOfRates);
        imgBookDetailsLike = findViewById(R.id.imgBookDetailsLike);
        imgBookDetailsSave = findViewById(R.id.imgBookDetailsSave);
        txtBookDetailsDescription = findViewById(R.id.txtBookDetailsDescription);
        txtBookDetailsCategory = findViewById(R.id.txtBookDetailsCategory);
        imgBookDetailsDownload = findViewById(R.id.imgBookDetailsDownload);
        imgBookDetailsReading = findViewById(R.id.imgBookDetailsReading);

    }

    private void Animate(View view){
        view.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
                .setDuration(150)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        view.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(150)
                                .start();
                    }
                })
                .start();
    }

    private void Update(String url){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("userName", MainActivity.USERNAME);
                    jsonObject.put("bookId", Id);

                    webService.sendByPostMethod(url, jsonObject);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        });
        thread.start();
    }

    private   void fillingLibraryBooks() {
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