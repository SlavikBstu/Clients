package com.example.shop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shop.adapter.DrawerAdapter;
import com.example.shop.adapter.ItemModel;
import com.example.shop.database.LocalDB;
import com.example.shop.fragments.AddProductFragment;
import com.example.shop.fragments.ProductFragment;
import com.example.shop.models.ProductInfo;
import com.example.shop.models.Products;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    private String[] mItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private Toolbar mToolbar;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private LocalDB db;
    public static SQLiteDatabase database;

    String URL = "http://192.168.0.113:8080/setproduct/products.wsdl";
    String NAMESPACE = "http://vlad.com/product";
    String SOAP_ACTION = "http://vlad.com/product/getProductRequest";
    String METHOD_NAME = "getProductRequest";
    String PARAMETER_NAME = "requset";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTitle = getTitle();
        mItemTitles = getResources().getStringArray(R.array.drawer_items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerListView = (ListView) findViewById(R.id.left_drawer);
        setupToolbar();

        new CallWebService().execute();

        ItemModel[] dItems = fillDataModel();

        DrawerAdapter adapter = new DrawerAdapter(this, R.layout.item_row, dItems);
        mDrawerListView.setAdapter(adapter);
        mDrawerListView.setOnItemClickListener(new ItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        setupDrawerToggle();
        db = new LocalDB(getApplicationContext(), "Shop", null, 1);
        database = db.getWritableDatabase();


        if (!isOnline()) {
            Toast.makeText(getApplicationContext(), "Offline", Toast.LENGTH_SHORT).show();
        }
        else {
            List<Products> list = new ArrayList<>();
            Cursor cursor = MainActivity.database.rawQuery("SELECT *FROM tempProducts", null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    list.add(new Products(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getDouble(4), cursor.getInt(5)));
                }
            }
            database.execSQL("DELETE FROM tempProducts");
            for(int i =0;i<list.size();i++) {
                new PostProduct().execute(list.get(i));
            }
            database.execSQL("DELETE FROM Products");
            List<Products> listProducts = new ArrayList<>();
            GetProducts getProducts = new GetProducts();
            getProducts.execute();
            try {
                listProducts = getProducts.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < listProducts.size(); i++) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("id", listProducts.get(i).getId());
                contentValues.put("Title", listProducts.get(i).getTitle());
                contentValues.put("Category", listProducts.get(i).getCategory());
                contentValues.put("Amount", listProducts.get(i).getAmount());
                contentValues.put("Price", listProducts.get(i).getPrice());
                contentValues.put("Add_date", listProducts.get(i).getAddDate());

                database.insert("Products", null, contentValues);
            }

            database.execSQL("DELETE FROM ProductInfo");
            List<ProductInfo> listProductInfo = new ArrayList<>();
            GetProductInfo getProductInfo = new GetProductInfo();
            getProductInfo.execute();
            try {
                listProductInfo = getProductInfo.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < listProductInfo.size(); i++) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("id", listProductInfo.get(i).getId());
                contentValues.put("Name", listProductInfo.get(i).getName());
                contentValues.put("Company", listProductInfo.get(i).getCompany());
                contentValues.put("Annotation", listProductInfo.get(i).getAnnotation());
                contentValues.put("Release_date", listProductInfo.get(i).getReleaseDate());

                database.insert("ProductInfo", null, contentValues);
            }
        }
    }

    public boolean isOnline() {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(cs);
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        } else {
            return true;
        }
    }

    // формируем массив с данными для адаптера
    private ItemModel[] fillDataModel() {
        return new ItemModel[]{
                new ItemModel(R.drawable.product, "Продукты"),
                new ItemModel(R.drawable.user, "Добавить продукт")
        };
    }

    // по клику на элемент списка устанавливаем нужный фрагмент в контейнер
    private class ItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Fragment fragment = null;

            // на основании выбранного элемента меню
            // вызываем соответственный ему фрагмент
            switch (position) {
                case 0:
                    fragment = new ProductFragment();
                    break;
                case 1:
                    fragment = new AddProductFragment();
                    break;

                default:
                    break;
            }
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

                mDrawerListView.setItemChecked(position, true);
                mDrawerListView.setSelection(position);
                setTitle(mItemTitles[position]);
                mDrawerLayout.closeDrawer(mDrawerListView);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    void setupDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        // Это необходимо для изменения иконки на основании текущего состояния
        mDrawerToggle.syncState();
    }
    class GetProducts extends AsyncTask<List<Products>, Void, List<Products>> {
        String str = "";

        @Override
        protected List<Products> doInBackground(List<Products>... params) {
            List<Products> list = new ArrayList<>();
            Products products;
            try {
                //String uri = "http://192.168.0.113:8080/products";
                String uri = "http://192.168.1.66:8080/products";


                HttpClient getHttpClient = new HttpClient();
                GetMethod getMethod = new GetMethod(uri);
                getHttpClient.executeMethod(getMethod);
                str = getMethod.getResponseBodyAsString();
                JSONArray array = new JSONArray(str);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    products = new Products(object.getInt("id"), object.getString("title"), object.getString("category"),
                             object.getInt("amount"), object.getDouble("price"), object.getInt("addDate"));
                    list.add(products);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }
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

    class GetProductInfo extends AsyncTask<List<ProductInfo>, Void, List<ProductInfo>> {
        String str = "";

        @Override
        protected List<ProductInfo> doInBackground(List<ProductInfo>... params) {
            List<ProductInfo> list = new ArrayList<>();
            ProductInfo productInfo;
            try {
                //String uri = "http://192.168.0.113:8080/productinfo";
                String uri = "http://192.168.1.66:8080/productinfo";

                HttpClient getHttpClient = new HttpClient();
                GetMethod getMethod = new GetMethod(uri);
                getHttpClient.executeMethod(getMethod);
                str = getMethod.getResponseBodyAsString();
                JSONArray array = new JSONArray(str);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    productInfo = new ProductInfo(object.getInt("id"),object.getString("name"),
                            object.getString("company"), object.getString("annotation"),object.getInt("releaseDate"));
                    list.add(productInfo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }
    }

    class CallWebService extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "fail";

            SoapObject soapObject = new SoapObject(NAMESPACE, METHOD_NAME);

            PropertyInfo propertyInfo = new PropertyInfo();
            propertyInfo.setName(PARAMETER_NAME);
            propertyInfo.setValue("lol");
            propertyInfo.setType(String.class);

            soapObject.addProperty(propertyInfo);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(soapObject);
            envelope.dotNet = true;

            HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

            try {
                httpTransportSE.call(SOAP_ACTION, envelope);
                SoapPrimitive soapPrimitive = (SoapPrimitive)envelope.getResponse();
                result = soapPrimitive.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }
    }
}
