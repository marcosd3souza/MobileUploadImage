package android.mso.com.uploadimagesapp;

import android.app.Application;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.ParseError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.JsonRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by msoliveira2 on 09/12/2016.
 */
public class Service {

    private static RequestQueue mRequestQueue;
    private static String URL_MULTIPART = "http://10.36.10.118:8080/FileUploadExample/experiment/upload/multipart";
    private static String URL_BASE64 = "http://10.36.10.118:8080/FileUploadExample/experiment/upload/encoded";

    private static Service mInstance;
    private Context mApplicationContext;
    private static int count = 0;

    // Prevent instantiation
    private Service() {
        super();
    }

    // Singleton
    public static Service getInstance() {
        if(mInstance == null) {
            mInstance = new Service();
        }
        return mInstance;
    }

    public void init(Application application) {
        if(mApplicationContext == null) {
            mApplicationContext = application.getApplicationContext();
            mRequestQueue = Volley.newRequestQueue(mApplicationContext);
        }
    }

    private static SimpleMultiPartRequest request;
    public static void sendImageByMultipart(File file) {

        request = new SimpleMultiPartRequest(
                Request.Method.POST,
                URL_MULTIPART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ENVIO multipart", "sucesso multipart");
                        canQueue = true;
                        addRequestToQueue();
                        count++;
                        if (count == 30){
                            Log.d("FIM multipart", new Date().toString());
                            count = 0;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ENVIO multipart", "Falha  multipart!");
                    }
                }
        );

        request.addFile("file", file.getPath());

        requests.add(request);

        addRequestToQueue();
    }

    private static JSONObject json;
    private static boolean canQueue = true;

    private static List<Request> requests = new ArrayList<>();

    public static void sendImageByBase64(File file) {

        json = new JSONObject();
        try {
            json.put("name", "teste");
            json.put("content", getFileInBase64(file));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requests.add(new JsonObjectRequest(
                Request.Method.POST,
                URL_BASE64,
                json,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        Log.d("ENVIO", "Sucesso base64!");
                        canQueue = true;
                        addRequestToQueue();
                        count++;
                        if (count == 5){
                            Log.d("FIM base64", new Date().toString());
                            count = 0;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ENVIO", "Falha  base64!");
                    }
                }
        ){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {

                    JSONObject result = new JSONObject();
                    result.put("code", response.statusCode);

                    return Response.success(result,
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }
        });

        addRequestToQueue();
    }

    private static void addRequestToQueue() {
        if(canQueue){
            Log.d("ENVIO", "saiu!");
            canQueue = false;
            mRequestQueue.add(requests.get(0));
            requests.remove(0);
        }
    }


    private static FileInputStream fileInputStream;
    private static byte[] fileInBytes;

    private static String getFileInBase64(File file) {

        fileInBytes = new byte[(int)file.length()];
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(fileInBytes);

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return Base64.encodeToString(fileInBytes, Base64.DEFAULT);
    }
}
