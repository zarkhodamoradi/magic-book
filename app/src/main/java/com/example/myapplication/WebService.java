package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebService {

    private  static RequestQueue requestQueue ;
    public static final String orgURL = "http://192.168.138.92/magicBooks/";
    public static  final  String URLSearchByTitle  =  orgURL+"showBooksByTitle.php?SearchedTitle=";
    public static final String GetByUrl = orgURL +"getBooks.php"; //"getBooks.php" ;
    public static final String GetFiction = orgURL +"getBooksByCategory.php"; //"getBooks.php" ;

    public static String InputStreamToString(InputStream stream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error!!!";


    }

    public static void SetupRequextQueue(Context context){
        requestQueue =Volley.newRequestQueue(context);
    }
    public static String GetContentOfUrlConnection(String uri) throws IOException {
        URL url = new URL(uri);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = httpURLConnection.getInputStream();
        String result = InputStreamToString(inputStream);
        return result;
    }


    public void sendRequestByGetMethod(String url) {
        final StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {


                    }
                });
        requestQueue.add(request);

    }

    public static void GetImage(String url, ImageView img, RequestQueue requestQueue) {

        ImageRequest request = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        img.setImageBitmap(bitmap);
                    }
                },
                120,
                120,
                ImageView.ScaleType.FIT_XY,
                Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                });
        requestQueue.add(request);
    }
}
