package com.example.undead.todolist.network;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.undead.todolist.constants.Constant;
import com.example.undead.todolist.models.Product;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GsonGetRequest<T> extends Request<ArrayList<T>>{
        private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Response.Listener<ArrayList<T>> listener;

        /**
         * Make a GET request and return a parsed object from JSON.
         *
         *
         * @param listener
         *
         */
        public GsonGetRequest
        (Class<T> clazz,
         Response.Listener<ArrayList<T>> listener, Response.ErrorListener errorListener)
        {
            super(Method.GET, Constant.REST_ROOT+Constant.REST_GET, errorListener);
            this.clazz = clazz;
            this.listener = listener;
        }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        return headers;
    }


   /* @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }*/

   /* @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }*/

    @Override
    protected void deliverResponse(ArrayList<T> response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<ArrayList<T>> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            Type listType = com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null, ArrayList.class, clazz);
            ArrayList<T> tList = gson.fromJson(json, listType);
            return Response.success(
                    tList,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

   /* public static GsonGetRequest<ArrayList<Product>> getDummyObjectArray
            (
                    Response.Listener<ArrayList<Product>> listener,
                    Response.ErrorListener errorListener
            )
    {

        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(Product.class, new DummyObjectDeserializer())
                .create();

        return new GsonGetRequest<>
                (

                        new TypeToken<ArrayList<DummyObject>>() {}.getType(),
                        gson,
                        listener,
                        errorListener
                );
    }*/

}
