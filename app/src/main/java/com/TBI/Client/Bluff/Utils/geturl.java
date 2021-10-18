package com.TBI.Client.Bluff.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class geturl {

    public static String BASE_URL = "http://ec2-54-164-254-255.compute-1.amazonaws.com:3001/api/";

    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();


    public String makeHttpRequestpost(String url, ArrayList<param> a1, String token) {


        String json = "";
        try {


            for (int i = 0; i < a1.size(); i++) {

                a1.get(i).setValue(a1.get(i).getValue().replace("\"", "\\\"").replace("'", "\\'"));
            }
            json = new geturl().run(url, a1, token);
            //  Log.d("json", json);

        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
            return "error";
        }


        return json;

    }

    public String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        /* if (Build.VERSION.SDK_INT >= 11*//*HONEYCOMB*//*) {
            Response response = client.newCall(request).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {*/
        Response response = client.newCall(request).execute();
        /*}*/

        return response.body().string();
    }


    public String run(String url, ArrayList<param> a1, String token) throws IOException {

        FormBody.Builder formBody = new FormBody.Builder();

        for (int i = 0; i < a1.size(); i++) {
            formBody.add(a1.get(i).name, a1.get(i).value);
        }
        FormBody f1 = formBody.build();
        Log.d("jsonURL", url);
        Log.d("jsonURL PARAM", a1.toString());
        Request request = new Request.Builder()
                .addHeader("Authorization", token)
                .url(url)
                .post(f1)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public void getdata(Context context, MyAsyncTaskCallback callback, ArrayList<param> parameters, String url) {
        if (Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
            new GETDATA(context, callback, parameters, url).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new GETDATA(context, callback, parameters, url).execute();
        }
    }

    private class GETDATA extends AsyncTask<Void, Void, String> {

        ArrayList<param> parameters;
        Object dataresponse;

        String url;
        Context context;
        MyAsyncTaskCallback callback;


        public GETDATA(Context context, MyAsyncTaskCallback callback, ArrayList<param> parameters, String url) {
            this.context = context;
            this.callback = callback;
            this.parameters = parameters;
            this.url = url;
        }


        @Override
        protected String doInBackground(Void... voids) {

            try {
                String result = "";

                Log.d("url", BASE_URL + url);
                if (parameters.size() > 0) {
                    result = makeHttpRequestpost(BASE_URL + url, parameters, sharedpreference.getUserToken(context));
                    Log.e("LLLLL_toekn: ", sharedpreference.getUserToken(context));
                } else {
                    result = run(BASE_URL + url);
                }
                Log.d("AAA", result);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }

        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            callback.onAsyncTaskComplete(res);
        }
    }
}
