package com.example.myapplication;

import java.util.Date;

public class Book {
    private int Id ;
    private String Title ;
    private int Price ;
    private String Discription ;
    private String Category ;
    private String Image ;
    private Double rating ;
    private String publishDate ;

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }
//    public Book(String title, int price) {
//        Title = title;
//        this.price = price;
//    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public Book() {
    }

//    public Book(int id, String title, int price) {
//        this.id = id;
//        Title = title;
//        this.price = price;
//    }
//
//
//    public Book(int id, String title, int price, String category) {
//        this.id = id;
//        Title = title;
//        this.price = price;
//        this.category = category;
//    }

//    public Book(int id, String title, int price, String discription, String category) {
//        this.id = id;
//        Title = title;
//        this.price = price;
//        this.discription = discription;
//        this.category = category;
//    }
//    public Book( String title, int price,  String category) {
//        Title = title;
//        this.price = price;
//        this.category = category;
//    }

    public Book(int id, String title, int price, String discription, String category, String image, Double rating, String publishDate) {
        Id = id;
        Title = title;
        Price = price;
        Discription = discription;
        Category = category;
        Image = image;
        this.rating = rating;
        this.publishDate = publishDate;
    }

    public Book(String title, int price, String discription, String category, String Image) {

        Title = title;
        this.Price = price;
        this.Discription = discription;
        this.Category = category;
        this.Image = Image ;
    }
    public Book( String title, int price, String discription, String category) {

        Title = title;
        this.Price = price;
        this.Discription = discription;
        this.Category = category;

    }
    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        this.Category = category;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        this.Price = price;
    }

    public String getDiscription() {
        return Discription;
    }

    public void setDiscription(String discription) {
        this.Discription = discription;
    }
}
