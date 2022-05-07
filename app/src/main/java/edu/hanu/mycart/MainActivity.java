package edu.hanu.mycart;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import edu.hanu.mycart.adapters.ProductAdapter;
import edu.hanu.mycart.models.Product;

public class MainActivity extends AppCompatActivity {
    private static String JSON_URL = "https://mpr-cart-api.herokuapp.com/products";
    private Toolbar toolbar;
    private List<Product> products;
    private RecyclerView rvProducts;
    private ProductAdapter adapter;
    private DownloadImage downloadImage;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        products = new ArrayList<>();
        downloadImage = new DownloadImage();
        rvProducts = findViewById(R.id.rvProducts);

        DownloadData downloadData = new DownloadData();
        downloadData.execute();

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(adapter != null) {
                    adapter.getFilter().filter(newText);
                }
                return false;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tbCart:
                 checkOut();
                 break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkOut() {
       intent = new Intent(this,CheckoutActivity.class);
       startActivity(intent);
    }

    private void parseJSONToRecycleView(List<Product> products) {

        int orientation = this.getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT) {
            rvProducts.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            rvProducts.setLayoutManager(new GridLayoutManager(this, 3));
        }
        adapter = new ProductAdapter(products);
        rvProducts.setAdapter(adapter);
    }

    public Bitmap loadImage(String url) {
        downloadImage = new DownloadImage();
        Bitmap bitmap = null;
        try {
            bitmap = downloadImage.execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public class DownloadData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            StringBuilder response = new StringBuilder();
            try {
                url = new URL(JSON_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(30000);
                conn.setConnectTimeout(60000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                int responseCode = conn.getResponseCode();
                Log.e("HTTP Response Code", Integer.toString(responseCode));
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }
                } else {
                    response = new StringBuilder();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);

//                    Integer id = jsonObject.getInt("id");
//                    String thumbnail = jsonObject.getString("thumbnail");
//                    String name = jsonObject.getString("name");
//                    Integer price = jsonObject.getInt("unitPrice");
//                    Product product = new Product(id, thumbnail, name, price);
//                    products.add(product);
                    Product product = new Product();
                    product.setId(jsonObject.getInt("id"));
                    product.setThumbnail(jsonObject.getString("thumbnail"));
                    product.setName(jsonObject.getString("name"));
                    product.setUnitPrice(jsonObject.getInt("unitPrice"));

//                    Fetch link to download image
                    String link = jsonObject.getString("thumbnail");
                    Bitmap bitmap = loadImage(link);
                    product.setImage(bitmap);

                    products.add(product);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            parseJSONToRecycleView(products);

        }
    }

    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            URL url;
            HttpURLConnection connection;
            InputStream inputStream;

            try {
                url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                inputStream = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }

}
