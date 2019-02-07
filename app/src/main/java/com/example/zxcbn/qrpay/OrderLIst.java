package com.example.zxcbn.qrpay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
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
 * Created by zxcbn on 2018-09-22.
 */

public class OrderLIst extends Activity {
    ListView olist;
    TextView curCash;
    String cid;
    ListThread listThread;
    Order order;
    OrderAdapter oadp;

    String sid,spw;
    String dbId,dbPw,dbName;
    String jsonString;

    String js;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_OID = "oid";
    private static final String TAG_NAME = "name";
    private static final String TAG_MENU = "menu";
    private static final String TAG_count = "count";

    JSONArray peoples = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderlist);
        olist = (ListView)findViewById(R.id.orderlist);
        curCash = (TextView)findViewById(R.id.curCash);

        Intent intent = getIntent();
        cid = intent.getStringExtra("ID");

        listThread = new ListThread(olist,this,curCash,cid);
        //getData("http://ec2-18-219-159-66.us-east-2.compute.amazonaws.com/listDB.php");

        /*order = new Order("홍철민","상하이스파이스치킨버거","2","0001");
        oadp = new OrderAdapter(this,order);
        olist.setAdapter(oadp);*/
        olist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                listThread.setOid(position);
                AlertDialog.Builder dlg = new AlertDialog.Builder(OrderLIst.this);
                dlg.setTitle("확인");
                dlg.setMessage("확인하셨습니까?");
                dlg.setPositiveButton("취소",null);
                dlg.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteData task = new DeleteData();
                        task.execute("http://ec2-18-222-167-250.us-east-2.compute.amazonaws.com/list_DBdelete.php", listThread.getOid(), cid);
                        listThread.resetAdp();
                    }
                });
                dlg.show();
            }
        });
    }

    @Override
    public void finish() {
        listThread.setPower();
        super.finish();
    }

    class DeleteData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String TAG = "DeleteData";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(OrderLIst.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String id = (String)params[1];
            String cid = (String)params[2];

            String serverURL = (String)params[0];
            String postParameters =  "&oid=" + id + "&cid=" + cid;


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

                return new String("Error: " + e.getMessage());
            }

        }
    }
}
