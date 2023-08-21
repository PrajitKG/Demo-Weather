package com.example.demoweather;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {
    ListView lvWeather;
    AsyncHttpClient client;
    ArrayAdapter adapter; // Declare the adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvWeather = findViewById(R.id.lvWeather);
        ArrayList alWeather = new ArrayList<>();
        client = new AsyncHttpClient();
        adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, alWeather); // Initialize the adapter
        lvWeather.setAdapter(adapter); // Set the adapter to the ListView

    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<Weather> alWeather = new ArrayList<Weather>();
        client.get("https://api.data.gov.sg/v1/environment/2-hour-weather-forecast", new JsonHttpResponseHandler() {
            String area;
            String forecast;

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray jsonArrItems = response.getJSONArray("items");
                    JSONObject firstObj = jsonArrItems.getJSONObject(0);
                    JSONArray jsonArrForecasts = firstObj.getJSONArray("forecasts");
                    for(int i = 0; i < jsonArrForecasts.length(); i++) {
                        JSONObject jsonObjForecast = jsonArrForecasts.getJSONObject(i);
                        area = jsonObjForecast.getString("area");
                        forecast = jsonObjForecast.getString("forecast");
                        Weather weather = new Weather(area, forecast);
                        alWeather.add(weather);
                    }
                    // Update the adapter with new data
                    adapter.clear();
                    adapter.addAll(alWeather); // Add new data
                    adapter.notifyDataSetChanged(); // Notify the adapter of data change
                } catch(JSONException e){

                }
            }
        });
    }
}
