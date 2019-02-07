package com.example.zxcbn.qrpay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zxcbn on 2018-09-22.
 */

public class Join extends Activity{
    private static String TAG = "phptest";
    Button joinBtn, joinCC;
    EditText ON,joinId,joinPw,checkPw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);
        joinBtn = (Button)findViewById(R.id.joinBtn);
        joinCC = (Button)findViewById(R.id.joinCC);
        ON = (EditText)findViewById(R.id.ON);
        joinId = (EditText)findViewById(R.id.joinId);
        joinPw = (EditText)findViewById(R.id.joinPw);
        checkPw = (EditText)findViewById(R.id.checkPw);

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ON.getText().toString();
                String id = joinId.getText().toString();
                String pw = joinPw.getText().toString();
                String cpw = checkPw.getText().toString();

                if(!pw.equals(cpw)){
                    Toast.makeText(Join.this, "비밀번호와 비밀번호확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }else {
                    InsertData task = new InsertData();
                    task.execute("http://ec2-18-222-167-250.us-east-2.compute.amazonaws.com/com_DBinsert.php", id, pw, name);
                    Toast.makeText(Join.this, "가입완료되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        joinCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Join.this, "취소되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Join.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Toast.makeText(Join.this, result, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String id = (String)params[1];
            String pw = (String)params[2];
            String name = (String)params[3];

            String serverURL = (String)params[0];
            String postParameters =  "&id=" + id + "&pw=" + pw + "&name=" + name;


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
