package com.example.dmitriy.yandex;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView resultJson;
    EditText newText;
    Button translateButton;
    Editable getText = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //найдем View-элементы
        resultJson = (TextView) findViewById(R.id.result_json);
        newText = (EditText) findViewById(R.id.translate_box);
        translateButton = (Button) findViewById(R.id.translate_button);

        translateButton.setOnClickListener(this); //обработчик кнопки
    }

    //button "перевести"
    @Override
    public void onClick(View v) {
        getText = newText.getText();
        try {
            new ParseTask().execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

            //JSON request
    class ParseTask extends AsyncTask<Void, Void, String> {

        String apiKey = "trnsl.1.1.20170416T125446Z.ea7102906a1e24fd.547dd8260b51419b081b85120f68bd67ea324b8a";
        String requestUrl = "https://translate.yandex.net/api/v1.5/tr.json/translate?key="
                + apiKey + "&lang=" + "en-ru" + "&text=" + URLEncoder.encode(String.valueOf(getText), "UTF-8");;
        String res = "";
        BufferedReader buf = null;

        ParseTask() throws UnsupportedEncodingException {
        }

        //
        @Override
        protected String doInBackground(Void... params) {

            try {
                URL url = new URL(requestUrl);
                HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.connect();

                buf = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                StringBuilder sb = new StringBuilder("");
                String l = "";
                String n1 = System.getProperty("text");
                while ((l = buf.readLine()) != null) {
                    sb.append(l + n1);
                }                                           //while close
                buf.close();
                res = sb.toString();
                return res;
            } catch (Exception e) {     //Catch exception
                e.printStackTrace();
            }
            return res;
        }

        //parsing JSON
        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);


            try {
                JSONObject jsonObject = new JSONObject(res);
                String response = jsonObject.getString("text");
                resultJson.setText(response);

            } catch (JSONException e) {

                e.printStackTrace();
            }

        }

    }


}

