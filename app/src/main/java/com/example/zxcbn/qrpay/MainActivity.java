package com.example.zxcbn.qrpay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText id,pw;
    Button join,login;
    String sid,spw;
    String dbId,dbPw,dbName;
    String jsonString;
    //String test;
    //URLConnector task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        id = (EditText)findViewById(R.id.id);
        pw = (EditText)findViewById(R.id.pw);
        join = (Button)findViewById(R.id.join);
        login = (Button)findViewById(R.id.login);

        /*test = "http://localhost/";
        task = new URLConnector(test);

        task.start();

        try{
            task.join();
            System.out.println("waiting for result");
        }catch (InterruptedException e){}

        String result = task.getResult();
        System.out.println(result);*/

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Join.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sid = id.getText().toString();
                spw = pw.getText().toString();
                InsertData task = new InsertData();
                task.execute("http://ec2-18-222-167-250.us-east-2.compute.amazonaws.com/com_login.php", sid);
            }
        });
    }
    class InsertData extends AsyncTask<String, Void, String> {
        String TAG = "login";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "POST response  - " + result);
            if(result==null){
                Toast.makeText(MainActivity.this, "에러", Toast.LENGTH_SHORT).show();
            }else{
                jsonString = result;
                showResult();
                if (spw.equals(dbPw)){
                    Intent intent = new Intent(getApplicationContext(),OrderLIst.class);
                    intent.putExtra("ID",id.getText().toString());
                    startActivity(intent);
                    id.setText("");
                    pw.setText("");
                }else{
                    Toast.makeText(MainActivity.this, "다시 로그인해주세요", Toast.LENGTH_SHORT).show();
                }
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
    }

    private void showResult(){
        String TAG_JSON="root";
        String TAG_ID = "comId";
        String TAG_PW = "comPw";
        String TAG_NAME ="comName";


        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                dbId = item.getString(TAG_ID);
                dbName = item.getString(TAG_NAME);
                dbPw = item.getString(TAG_PW);
            }



        } catch (JSONException e) {

            Log.d("showresult", "showResult : ", e);
        }
    }
}