package com.example.dmitriy.yandex;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView resultJson;
    String langJson1;
    String langJson2;
    EditText newText;
    Button translateButton;
    Editable getText = null;
    Spinner spinner;
    Spinner spinner1;
    String lang[] = {"Русский", "Английский", "Азербайджанский", "Албанский", "Амхарский", "Арабский", "Армянский", "Белорусский", "Болгарский", "Китайский", "Корейский"};
    ArrayList<String> res = new ArrayList<String>();
    String js;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //найдем View-элементы
        resultJson = (TextView) findViewById(R.id.result_json); // поле вывода результата
       // resultJson = (ListView) findViewById(R.id.result_json);

        newText = (EditText) findViewById(R.id.translate_box); //поле ввода
       // translateButton = (Button) findViewById(R.id.translate_button); // кнопка перевода
        spinner = (Spinner) findViewById(R.id.lang0); //кнопка(1) выбора языка
        spinner1 = (Spinner) findViewById(R.id.lang1);//кнопка(2) выбор языка

        //ArrayAdapter<String> listadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, res);


        //Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lang);
        //  Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //resultJson.setAdapter(listadapter);
        spinner1.setAdapter(adapter);
        spinner.setAdapter(adapter);
        //translateButton.setOnClickListener(this); //обработчик кнопки
        spinner.setOnItemSelectedListener(itemSelectedListener1);
        spinner1.setOnItemSelectedListener(itemSelectedListener);


        //обработчик введенных символов в TextEdit
        newText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                getText = newText.getText();
                try {
                    new ParseTask().execute();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    //button "перевести"
   // @Override
  //  public void onClick(View v) {
    //    getText = newText.getText();
   //     try {
  //          new ParseTask().execute();
   //    } catch (UnsupportedEncodingException e) {
  //          e.printStackTrace();
    //    }
  //  }

    //перевод на...
    AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // Получаем выбранный объект
            String item = (String) parent.getItemAtPosition(position);
            // Выбираем язык
            if (item.equals("Русский")) {
                langJson1 = "ru";
            }else if (item.equals("Английский")) {
                langJson1 = "en";
            }else if (item.equals("Азербайджанский")) {
                langJson1 = "az";
            }else if (item.equals("Албанский")) {
                langJson1 = "sq";
            }else if (item.equals("Амхарский")) {
                langJson1 = "am";
            }else if (item.equals("Арабский")) {
                langJson1 = "ar";
            }else if (item.equals("Армянский")) {
                langJson1 = "hy";
            }else if (item.equals("Белорусский")) {
                langJson1 = "be";
            }else if (item.equals("Болгарский")) {
                langJson1 = "bg";
            }else if (item.equals("Китайский")) {
                langJson1 = "zh";
            }else {
                langJson1 = "ko";
            }

        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    // перевод с...
    AdapterView.OnItemSelectedListener itemSelectedListener1 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // Получаем выбранный объект
            String item = (String) parent.getItemAtPosition(position);
            // Выбираем язык
            if (item.equals("Русский")) {
                langJson2 = "ru";
            }else if (item.equals("Английский")) {
                langJson2 = "en";
            }else if (item.equals("Азербайджанский")) {
                langJson2 = "az";
            }else if (item.equals("Албанский")) {
                langJson2 = "sq";
            }else if (item.equals("Амхарский")) {
                langJson2 = "am";
            }else if (item.equals("Арабский")) {
                langJson2 = "ar";
            }else if (item.equals("Армянский")) {
                langJson2 = "hy";
            }else if (item.equals("Белорусский")) {
                langJson2 = "be";
            }else if (item.equals("Болгарский")) {
                langJson2 = "bg";
            }else if (item.equals("Китайский")) {
                langJson2 = "zh";
            }else {
                langJson2 = "ko";
            }

        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };


            //JSON request
    class ParseTask extends AsyncTask<Void, Void, String> {

        String apiKey = "trnsl.1.1.20170416T125446Z.ea7102906a1e24fd.547dd8260b51419b081b85120f68bd67ea324b8a";
        String requestUrl = "https://translate.yandex.net/api/v1.5/tr.json/translate?key="
                + apiKey + "&lang=" + (langJson2 + "-" + langJson1) + "&text=" + URLEncoder.encode(String.valueOf(getText), "UTF-8");;
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
                }                                           //"while" close
                buf.close();
                res = sb.toString();
                return res;
            } catch (Exception e) {
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
                response = response.substring(2, response.length()-2);
                resultJson.setText(response);

            } catch (JSONException e) {

                e.printStackTrace();
            }
        }
    }

}

