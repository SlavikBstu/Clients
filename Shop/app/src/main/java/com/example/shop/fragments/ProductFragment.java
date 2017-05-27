package com.example.shop.fragments;

/**
 * Created by Владислав on 17.05.2017.
 */


import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.shop.MainActivity;
import com.example.shop.R;
import com.example.shop.models.ProductInfo;
import com.example.shop.models.Products;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductFragment extends Fragment {

    ListView items;

    ArrayList<HashMap<String, String>> myArrList = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> map;
    SimpleAdapter adapter;

    public ProductFragment() {}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return view;
    }


    @Override
    public void onStart(){
        super.onStart();

        items = (ListView) this.getView().findViewById(R.id.prodView);

        List<Products> list = new ArrayList<>();
        Cursor cursor = MainActivity.database.rawQuery("SELECT *FROM Products",null);
        if(cursor.getCount()>0)
        {
            while (cursor.moveToNext())
            {
                list.add(new Products(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3),cursor.getDouble(4), cursor.getInt(5)));
            }
        }
        else
        {
            Toast.makeText(getActivity().getApplicationContext(),"записей не найдено",Toast.LENGTH_SHORT).show();
        }
        for(int i = 0; i < list.size();i++)
        {
            map = new HashMap<>();
            map.put("Id",String.valueOf(list.get(i).getId()));
            map.put("Title",list.get(i).getTitle());
            map.put("Category", "Категория: " + list.get(i).getCategory());
            map.put("Price","Цена: " + list.get(i).getPrice().toString());
            myArrList.add(map);
        }
        adapter = new SimpleAdapter(getActivity().getApplicationContext(), myArrList,
                R.layout.listing, new String[] {"Title", "Category",
                "Price","Id" }, new int[] {R.id.prtitle,
                 R.id.prcategory, R.id.prprice });
        items.setAdapter(adapter);
        items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int in, long l) {
                try {
                    HashMap<String, String> item = (HashMap<String, String>) adapterView.getAdapter().getItem(in);
                    String title = item.get("Title");
                    ArrayList<HashMap<String, String>> myArrList = new ArrayList<HashMap<String, String>>();
                    HashMap<String, String> map;
                    List<ProductInfo> list = new ArrayList<>();
                    Cursor cursor = MainActivity.database.rawQuery("SELECT *FROM ProductInfo where Name = ?", new String[]{title});
                    if (cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            list.add(new ProductInfo(cursor.getString(1), cursor.getString(2), cursor.getString(3)));
                        }
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Записей не найдено", Toast.LENGTH_SHORT).show();
                    }
                    for (int i = 0; i < list.size(); i++) {
                        map = new HashMap<>();
                        map.put("Name", "Название игры: " + list.get(i).getName());
                        map.put("Company", "Компания: " + list.get(i).getCompany());
                        map.put("Annotation", "Описание: " + list.get(i).getAnnotation());
                        myArrList.add(map);
                    }
                    adapter = new SimpleAdapter(getActivity().getApplicationContext(), myArrList,
                            R.layout.listing, new String[]{"Name", "Company",
                            "Annotation"}, new int[]{R.id.prtitle,
                            R.id.prcategory, R.id.prprice});
                    items.setAdapter(adapter);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

    }

}

     /*class PostProduct extends AsyncTask<Products, Void, Products> {
        String result = "";

        @Override
        protected Products doInBackground(Products... params) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            Products products = new Products(params[0].getCategory(),params[0].getTitle(),params[0].getPrice(),params[0].getAmount(),params[0].getPicture(),new Date());
            String s = gson.toJson(products);

            HttpClient httpClient = new HttpClient();
            PostMethod postMethod = new PostMethod("http://gameshopx.herokuapp.com/products");
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
    }*/