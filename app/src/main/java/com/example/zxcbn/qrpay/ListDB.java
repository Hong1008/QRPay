package com.example.zxcbn.qrpay;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

public class ListDB extends AsyncTask<String, Void, String>{
    String jsonString;
    int p;
    String oid, name, count, menu, time;
    String cnt = "0";
    private static final String TAG = "result";
    private static final String TAG_OID = "oid";
    private static final String TAG_NAME = "name";
    private static final String TAG_MENU = "menu";
    private static final String TAG_count = "count";
    private static final String TAG_TIME = "time";
    ArrayList<HashMap<String, String>> personList;
    ArrayList<Order> item;
    HashMap<String, String> persons;
    ListAdapter adapter;

    JSONArray peoples = null;
    Context context;
    ListView olv;
    Order order;
    OrderAdapter oadp;

    public ListDB(Context context, ListView olv){
        this.context = context;
        this.olv = olv;
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
        try{
            JSONObject jObj= new JSONObject(jsonString);
                peoples = jObj.getJSONArray(TAG_JSON);
                item = new ArrayList<Order>();
                for (int i = 0; i < peoples.length(); i++) {
                    JSONObject c = peoples.getJSONObject(i);
                    oid = c.getString(TAG_OID);
                    name = c.getString(TAG_NAME);
                    menu = c.getString(TAG_MENU);
                    count = c.getString(TAG_count);
                    order = new Order(oid, name, menu, count);
                    item.add(order);
                }
                oadp = new OrderAdapter(context, item);
                olv.setAdapter(oadp);
                /*adapter = new SimpleAdapter(context,
                        personList, R.layout.custom, new String[]{TAG_cnt, TAG_NAME, TAG_MENU, TAG_count},
                        new int[]{R.id.oid, R.id.user, R.id.menulist, R.id.count});
                olv.setAdapter(adapter);*/
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void setOid(int oid){
        p = oid;
    }

    public String getOid(){
        String id = String.valueOf(oadp.getItem(p));
        return id;
    }

    public void resetAdp(){
        oadp.removeItem(p);
        oadp.notifyDataSetChanged();
    }

}
