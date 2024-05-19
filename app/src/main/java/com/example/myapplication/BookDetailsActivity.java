package com.example.myapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
            txtBookDetailsAuthor.setText(Author);
            txtBookDetailsDescription.setText(Description);
            txtBookDetailsRatingOutOf5.setText(Rating/5 + "");
            txtToatalNumberOfRates.setText("0");
        }catch (Exception e){
            e.printStackTrace();
        }


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
    }
}