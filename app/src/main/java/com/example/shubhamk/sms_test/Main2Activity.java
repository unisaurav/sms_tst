package com.example.shubhamk.sms_test;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

public class Main2Activity extends Activity {
    String Json_string;
    Button gh;
    Button gk;
    String JSON_STRING;


    void ckl() {
        Intent intent = new Intent(this, Main5Activity.class);
        intent.putExtra("json_data", JSON_STRING);
        startActivity(intent);
    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        TextView textView=(TextView)findViewById(R.id.date);
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        textView.setText(currentDateTimeString);
        new BackgroundTask().execute();
        //gk = findViewById(R.id.parse);
//        gk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (JSON_STRING == null) {
//                    Toast.makeText(getApplicationContext(), "First_fetch data", Toast.LENGTH_LONG).show();
//                } else {
//                    ckl();
//                }
//
//            }
//        });

    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String json_url;

        @Override
        protected void onPreExecute() {
            json_url = "https://unribbed-headers.000webhostapp.com/extended_josn.php";

        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(json_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((Json_string = bufferedReader.readLine()) != null) {
                    stringBuilder.append(Json_string + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            JSON_STRING = result;
            clk();


        }

    }

    private void clk() {
        Intent intent = new Intent(this, Main5Activity.class);
        intent.putExtra("json_data", JSON_STRING);
        startActivity(intent);
    }
}
