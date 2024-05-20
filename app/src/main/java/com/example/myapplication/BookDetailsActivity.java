package com.example.myapplication;

import android.media.Image;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

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

        try {
            Bundle bundle = getIntent().getExtras();
            String Title = bundle.getString("Title");
            Integer Price = bundle.getInt("Price");
            String Description = bundle.getString("Description");
            String ImageUrl = bundle.getString("imageUrl");
            String Category = bundle.getString("Category");
            Double Rating = bundle.getDouble("Rating");
            String PublishDate = bundle.getString("PublishDate");
            String Author = bundle.getString("Author");
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
        final boolean[] save = {false};
        imgBookDetailsSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save[0] = !save[0];
                if (save[0] == false){
                    imgBookDetailsSave.setImageResource(R.drawable.baseline_bookmark_border_24);
                }
                else  imgBookDetailsSave.setImageResource(R.drawable.baseline_bookmark_24);
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
                else  imgBookDetailsLike.setImageResource(R.drawable.favorite_24px_filled);
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
    }
}