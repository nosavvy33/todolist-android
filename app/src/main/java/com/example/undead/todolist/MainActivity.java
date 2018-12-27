package com.example.undead.todolist;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.undead.todolist.adpaters.ProductsAdapter;
import com.example.undead.todolist.models.Product;
import com.example.undead.todolist.network.ApiClient;
import com.example.undead.todolist.network.GsonGetRequest;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements ProductsAdapter.ProductsAdapterListener {

    private final String CLASSNAME = this.getClass().getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private Unbinder unbinder;

    private ArrayList<Product> products = new ArrayList<Product>();

    private ProductsAdapter adapter;

    GsonGetRequest<Product> gsonGetRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        gsonGetRequest = new GsonGetRequest<Product>(Product.class,getListSuccess(),getListError());
        ApiClient.getInstance(this.getApplicationContext()).addToRequestQueue(gsonGetRequest);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this,CreateProductActivity.class),1);
            }
        });


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        Log.wtf("PRODUCT",String.valueOf(products.size()));

        adapter = new ProductsAdapter(getApplicationContext(),products, MainActivity.this::onProductSelected);

        recyclerView.setAdapter(adapter);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                ApiClient.getInstance(getApplicationContext()).addToRequestQueue(gsonGetRequest);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private Response.Listener<ArrayList<Product>> getListSuccess() {
        return new Response.Listener<ArrayList<Product>>() {
            @Override
            public void onResponse(ArrayList<Product> response) {
                // Do whatever you want to do with response;
                // Like response.tags.getListing_count(); etc. etc.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    response.forEach((r)->
                            Log.wtf(CLASSNAME, String.valueOf(r.id))
                    );
                }
                products.clear();
                products.addAll(response);
                adapter.notifyDataSetChanged();
                Log.wtf("RESPONSE COMPLETED",String.valueOf(response.size()));
            }
        };
    }

    private Response.ErrorListener getListError() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Do whatever you want to do with error.getMessage();
                Log.wtf("ERROR",error.getMessage());
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unbind the views to claim the memory used when the instance of the view is destroyed
        unbinder.unbind();
    }

    @Override
    public void onProductSelected(Product product) {
        Log.wtf("CLICKED",product.descripcion);
    }
}
