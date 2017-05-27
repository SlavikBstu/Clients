package com.example.shop.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class Products {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("addDate")
    @Expose
    private Integer addDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getAddDate() {
        return addDate;
    }

    public void setAddDate(Integer addDate) {
        this.addDate = addDate;
    }

    public Date getDate(){
        return new java.util.Date(addDate);
    }

    public void setDate(Date date) {
        addDate = (int) date.getTime();
    }

    public Products(Integer id,  String title, String category, Integer amount, Double price, Integer addDate) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.price = price;
        this.amount = amount;
        this.addDate = addDate;
    }

    public Products(String title, String category, Double price, Integer amount, Date date)
    {
        this.category = category;
        this.title = title;
        this.price = price;
        this.amount = amount;
        this.addDate = (int) date.getTime();
    }
}