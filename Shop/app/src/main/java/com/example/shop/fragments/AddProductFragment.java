package com.example.shop.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shop.R;
import com.example.shop.models.Products;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.util.Date;

import static com.example.shop.MainActivity.database;

/**
 * Created by Владислав on 22.05.2017.
 */

public class AddProductFragment extends Fragment {

    Button add;
    EditText title, category, price, amount;

    public AddProductFragment() {}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productadd, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return view;
    }
    public boolean isOnline() {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager)
                getActivity().getSystemService(cs);
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        } else {
            return true;
        }
    }
    @Override
    public void onStart() {
        super.onStart();

        add = (Button)this.getView().findViewById(R.id.addbutton);

        title = (EditText)this.getView().findViewById(R.id.edtitle);
        category = (EditText)this.getView().findViewById(R.id.edcategory);
        price = (EditText)this.getView().findViewById(R.id.edprice);
        amount = (EditText)this.getView().findViewById(R.id.edamount);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.getText().length() > 0 && category.getText().length() > 0
                        && price.getText().length() > 0 && amount.getText().length() > 0)
                {
                    if(isOnline()) {
                        Products products = new Products(title.getText().toString(), category.getText().toString(),
                                Double.valueOf(price.getText().toString()), Integer.valueOf(amount.getText().toString()), new Date());
                        new PostProduct().execute(products);
                        try {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("Title", title.getText().toString());
                            contentValues.put("Category", category.getText().toString());
                            contentValues.put("Amount", Integer.valueOf(amount.getText().toString()));
                            contentValues.put("Price", Double.valueOf(price.getText().toString()));
                            contentValues.put("Add_date", String.valueOf(new Date()));
                            long rowid = database.insert("Products", null, contentValues);
                            if(rowid>0)
                            {
                                Toast.makeText(getActivity().getApplicationContext(), "Added global", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (SQLiteException e)
                        {
                            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        try {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("Title", title.getText().toString());
                            contentValues.put("Category", category.getText().toString());
                            contentValues.put("Amount", Integer.valueOf(amount.getText().toString()));
                            contentValues.put("Price", Double.valueOf(price.getText().toString()));
                            contentValues.put("Add_date", String.valueOf(new Date()));
                            database.insert("Products", null, contentValues);
                            ContentValues content = new ContentValues();
                            content.put("Title", title.getText().toString());
                            content.put("Category", category.getText().toString());
                            content.put("Amount", Integer.valueOf(amount.getText().toString()));
                            content.put("Price", Double.valueOf(price.getText().toString()));
                            content.put("Add_date", String.valueOf(new Date()));
                            long rowid = database.insert("tempProducts", null, content);
                            if (rowid > 0) {
                                Toast.makeText(getActivity().getApplicationContext(), "Added to Local", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (SQLiteException e)
                        {
                            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                else {
                    Toast.makeText(getActivity().getApplicationContext(), "поля не заполнены", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class PostProduct extends AsyncTask<Products, Void, Products> {
        String result = "";

        @Override
        protected Products doInBackground(Products... params) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            Products products = new Products(params[0].getTitle(), params[0].getCategory(),params[0].getPrice(),params[0].getAmount(), new Date());
            String s = gson.toJson(products);

            HttpClient httpClient = new HttpClient();
            //PostMethod postMethod = new PostMethod("http://192.168.0.113:8080/products");
            PostMethod postMethod = new PostMethod("http://192.168.1.66:8080/products");
            postMethod.addRequestHeader("Content-Type", "application/json;charset=UTF-8");
            postMethod.setRequestBody(s);
            try {
                httpClient.executeMethod(postMethod);
                result = postMethod.getResponseBodyAsString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return products;
        }

    }

}
