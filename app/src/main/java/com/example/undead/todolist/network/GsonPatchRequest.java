package com.example.undead.todolist.network;

import android.app.DownloadManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.example.undead.todolist.constants.Constant;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class GsonPatchRequest<T> extends JsonRequest<T> {

    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Response.Listener<T> listener;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     *
     *
     * @param listener is the listener for the right answer
     * @param errorListener  is the listener for the wrong answer
     */
    public GsonPatchRequest
    (String id, String body, Class<T> clazz,
     Response.Listener<T> listener, Response.ErrorListener errorListener)
    {
        super(Method.PATCH, Constant.REST_ROOT+Constant.REST_PATCH+id, body, listener, errorListener);
        this.clazz = clazz;
        this.listener = listener;
    }

    @Override
    protected void deliverResponse(T response)
    {
        listener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        return headers;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response)
    {
        try
        {

            Log.wtf("RESPONSE FROM PATCH :: ",response.toString());
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            return (Response<T>) Response.success
                    (
                            gson.fromJson(json, clazz),
                            HttpHeaderParser.parseCacheHeaders(response)
                    );
        }
        catch (UnsupportedEncodingException e)
        {
            return Response.error(new ParseError(e));
        }
        catch (JsonSyntaxException e)
        {
            return Response.error(new ParseError(e));
        }
    }
}
