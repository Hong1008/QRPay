package com.example.zxcbn.qrpay;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zxcbn on 2018-11-02.
 */

public class ComDBCash extends AsyncTask<String, Void, String>{
    String TAG = "Cash";
    String jsonString;
    String dbcash;
    Context context;
    TextView textView;

    public ComDBCash(Context context, TextView textView) {
        this.context = context;
        this.textView = textView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d(TAG, "POST response  - " + result);
        if(result==null){
            Toast.makeText(context, "에러", Toast.LENGTH_SHORT).show();
        }else{
            jsonString = result;
            showResult();
            textView.setText("현재잔고: "+dbcash);
        }
    }


    @Override
    protected String doInBackground(String... params) {
        String serverURL = (String)params[0];
        String postParameters =  "id=" + params[1];


        try {

            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.connect();


            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(postParameters.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();


            int responseStatusCode = httpURLConnection.getResponseCode();
            Log.d(TAG, "POST response code - " + responseStatusCode);

            InputStream inputStream;
            if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            }
            else{
                inputStream = httpURLConnection.getErrorStream();
            }


            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }


            bufferedReader.close();
            Log.d(TAG, "String= "+sb.toString());

            return sb.toString();


        } catch (Exception e) {

            Log.d(TAG, "InsertData: Error ", e);

            return null;
        }

    }

    private void showResult(){
        String TAG_JSON="root";
        String TAG_CASH="comCash";


        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);
                dbcash = item.getString(TAG_CASH);
            }



        } catch (JSONException e) {

            Log.d("showresult", "showResult : ", e);
        }
    }
}
