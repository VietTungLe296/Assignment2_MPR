package edu.hanu.mycart.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.hanu.mycart.R;
import edu.hanu.mycart.models.Product;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> implements Filterable {

    private List<Product> products;
    private List<Product> allProducts;

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Product> filteredList = new ArrayList<>();
            if(charSequence.toString().isEmpty()) {
                filteredList.addAll(allProducts);
            } else {
                for(Product product : allProducts) {
                    if(product.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(product);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                products.clear();
                products.addAll((Collection<? extends Product>) filterResults.values);
                notifyDataSetChanged();
        }
    };

    public ProductAdapter(List<Product> products) {
        this.products = products;
        this.allProducts = new ArrayList<>(products);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.item_product, parent, false);

        return new ProductHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product product = products.get(position);
//        String idInString = Integer.toString(product.getId());
//        holder.tvID.setText(idInString);
        holder.tvName.setText(product.getName());
        String priceInString = Integer.toString(product.getUnitPrice());
        holder.tvPrice.setText("₫ " + priceInString);
        holder.imgProduct.setImageBitmap(product.getImage());
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    protected class ProductHolder extends RecyclerView.ViewHolder {
        TextView tvThumbnail, tvName, tvPrice;
        ImageView imgProduct;
        private Context context;

        public ProductHolder(@NonNull View itemView, Context context) {
            super(itemView);
            tvThumbnail = itemView.findViewById(R.id.tvName);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            this.context = context;
        }
    }
}