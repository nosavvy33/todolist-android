package com.example.undead.todolist.adpaters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.undead.todolist.MainActivity;
import com.example.undead.todolist.R;
import com.example.undead.todolist.models.Product;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder>  {
    private Context context;
    private List<Product> products;
    private ProductsAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.pid)
        TextView productId;

        @BindView(R.id.pnombre)
        TextView productNombre;

        @BindView(R.id.pcantidad)
        TextView productCantidad;

        @BindView(R.id.pdescripcion)
        TextView productDescripcion;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    Log.e("Click","Item Clicked");
                    listener.onProductSelected(products.get(getAdapterPosition()));
                }
            });

        }
    }

    public ProductsAdapter(Context context, List<Product> products, ProductsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.products = products;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Product product = products.get(position);

        holder.productId.setText(String.valueOf(product.id));

        holder.productNombre.setText(product.nombre);
        holder.productCantidad.setText(String.valueOf(product.cantidad));

        holder.productDescripcion.setText(product.descripcion);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public interface ProductsAdapterListener {
        void onProductSelected(Product product);
    }

}
