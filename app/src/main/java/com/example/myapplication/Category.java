package com.example.myapplication;

import java.util.ArrayList;

public class Category {

    private int categoryId;
    private String categoryName;
    private ArrayList<Book> books;
    private int bookCount ;

    public Category(String categoryName) {

        this.categoryName = categoryName;

    }
    public Category(int categoryId, String categoryName, ArrayList<Book> books, int bookCount) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.books = books;
        this.bookCount = bookCount;
    }

    public Category(int categoryId, String categoryName, ArrayList<Book> books) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.books = books;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    public int getBookCount() {
        return bookCount;
    }

    public void setBookCount(int bookCount) {
        this.bookCount = bookCount;
    }
}
