package edu.hanu.mycart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

import edu.hanu.mycart.adapters.ProductAdapter;
import edu.hanu.mycart.models.Product;

public class CheckoutActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private List<Product> selectedProducts;
    private ProductAdapter adapter;
    private RecyclerView rvSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        selectedProducts = new ArrayList<>();
        selectedProducts.add(new Product(1,"dcm","hellio",3));
        selectedProducts.add(new Product(1,"dcm","hellio",3));
        rvSelected = findViewById(R.id.rvSelected);
        rvSelected.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(selectedProducts);
        rvSelected.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}