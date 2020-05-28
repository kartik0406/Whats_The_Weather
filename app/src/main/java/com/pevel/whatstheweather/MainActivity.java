package com.pevel.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView resultTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     editText=findViewById(R.id.editText);
     resultTextView=findViewById(R.id.resultTextView);
    }
    public void getWeather(View view)
    {
        DownloadTask task=new DownloadTask();
        task.execute("https://openweathermap.org/data/2.5/weather?q="+editText.getText().toString()+"&appid=439d4b804bc8187953eb36d2a8c26a02");

    }
    public class DownloadTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            HttpURLConnection httpconnection=null;
            try {
                url = new URL(urls[0]);
                httpconnection = (HttpURLConnection) url.openConnection();
                InputStream in = httpconnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherinfo=jsonObject.getString("weather");
                Log.i("Weather content",weatherinfo);
                JSONArray arr=new JSONArray(weatherinfo);
                String message="";
                for(int i=0;i<arr.length();i++)
                {
                    JSONObject jsonpart=arr.getJSONObject(i);
                    String main=jsonpart.getString("main");
                    String discription=jsonpart.getString("description");
                   if(!main.equals("") && !discription.equals(""))
                    {
                        message += main + ":" + discription;
                    }
                }
                if(!message.equals(""))
                {
                    resultTextView.setText(message);
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
