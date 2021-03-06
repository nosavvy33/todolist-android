package com.example.undead.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.undead.todolist.constants.Constant;
import com.example.undead.todolist.models.Product;
import com.example.undead.todolist.network.ApiClient;
import com.example.undead.todolist.network.GsonPatchRequest;
import com.example.undead.todolist.network.GsonPostRequest;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CreateProductActivity extends AppCompatActivity {

    private final String CLASSNAME = this.getClass().getSimpleName();

    @BindView(R.id.newpnombre)
     EditText producto_nombre;

    @BindView(R.id.newpcantidad)
     EditText producto_cantidad;

    @BindView(R.id.newpdescripcion)
     EditText producto_descripcion;

    @BindView(R.id.btn_crear)
    Button crear_button;

    @BindView(R.id.btn_cancelar)
     Button cancelar_button;

    @BindView(R.id.btn_eliminar)
    Button eliminar_button;

    private Unbinder unbinder;

    private String nombre;
    private String cantidad;
    private String descripcion;
    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);
        unbinder = ButterKnife.bind(this);

        Intent i = getIntent();
        if(i.hasExtra("nombre") && i.hasExtra("cantidad") && i.hasExtra("descripcion") && i.hasExtra("id")){
            producto_nombre.setText(i.getExtras().getString("nombre"));
            producto_cantidad.setText(i.getExtras().getString("cantidad"));
            producto_descripcion.setText(i.getExtras().getString("descripcion"));
            id = i.getExtras().getString("id");
            crear_button.setText("ACTUALIZAR");
            eliminar_button.setVisibility(View.VISIBLE);
            Log.wtf("HAS ID :: ",id);
        }

        eliminar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest = new StringRequest(Request.Method.DELETE,Constant.REST_ROOT+Constant.REST_DELETE+id,getStringSuccess(),getListError());
                ApiClient.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
        });

        crear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(producto_nombre.getText()) || !TextUtils.isEmpty(producto_cantidad.getText()) || !TextUtils.isEmpty(producto_descripcion.getText()) ){
                    if(!TextUtils.isEmpty(id)){
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("nombre",String.valueOf(producto_nombre.getText()));
                        jsonObject.addProperty("cantidad",String.valueOf(producto_cantidad.getText()));
                        jsonObject.addProperty("descripcion",String.valueOf(producto_descripcion.getText()));
                        GsonPatchRequest gsonPatchRequest = new GsonPatchRequest(id,jsonObject.toString(),Product.class,getListSuccess(),getListError());
                        ApiClient.getInstance(getApplicationContext()).addToRequestQueue(gsonPatchRequest);
                    }else{
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("nombre",String.valueOf(producto_nombre.getText()));
                        jsonObject.addProperty("cantidad",String.valueOf(producto_cantidad.getText()));
                        jsonObject.addProperty("descripcion",String.valueOf(producto_descripcion.getText()));

                        GsonPostRequest gsonPostRequest = new GsonPostRequest(jsonObject.toString(),Product.class,getListSuccess(),getListError());
                        ApiClient.getInstance(getApplicationContext()).addToRequestQueue(gsonPostRequest);
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Todos los campos son obligatorios",Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
    }

    private Response.Listener<Product> getListSuccess() {
        return new Response.Listener<Product>() {
            @Override
            public void onResponse(Product response) {
                Log.wtf("RESPONSE",response.toString());
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        };
    }

    private Response.Listener<String> getStringSuccess(){
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        };
    }

    private Response.ErrorListener getListError() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.wtf("ERROR",error.getMessage());
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unbind the views to claim the memory used when the instance of the view is destroyed
        unbinder.unbind();
    }
}
