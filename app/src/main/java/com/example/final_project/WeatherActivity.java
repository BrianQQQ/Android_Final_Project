package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.view.View;

import com.bumptech.glide.Glide;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import android.widget.ImageView;
import com.example.final_project.databinding.ActivityWeatherBinding;
import com.example.final_project.R;
public class WeatherActivity extends AppCompatActivity implements WeatherDataListener {
    private EditText searchEditText;
    private TextView cityTextView;

    private TextView temperatureTextView;
    private TextView weatherConditionTextView;
    private ImageView weatherIconImageView;
    private WeatherAPI weatherAPI;
    private TextView searchResultsTextView;
    private RecyclerView weatherRecyclerView;

    private List<WeatherData> weatherDataList = new ArrayList<>();
private ActivityWeatherBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherRecyclerView = findViewById(R.id.result_list);
        weatherRecyclerView.setLayoutManager(new LinearLayoutManager(this));







        weatherIconImageView = findViewById(R.id.weatherIconImageView);



       searchEditText = findViewById(R.id.search_edit_text);
        cityTextView = findViewById(R.id.city_text_view);
        temperatureTextView = findViewById(R.id.temperature_text_view);
        weatherConditionTextView = findViewById(R.id.weather_condition_text_view);
        searchResultsTextView = findViewById(R.id.search_results_text_view);


        weatherAPI = new WeatherAPI(this);

  /*      searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String cityName = searchEditText.getText().toString();
                    weatherAPI.getWeatherDataForCity(cityName, (WeatherDataListener) WeatherActivity.this);
                    return true;
                }
                return false;
            }

        });*/


        WeatherData savedWeatherData = weatherAPI.getSavedWeatherData();
        if (savedWeatherData != null) {
            updateWeatherData(savedWeatherData);
        }
        Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                String cityName = searchEditText.getText().toString();
                weatherAPI.getWeatherDataForCity(cityName, (WeatherDataListener) WeatherActivity.this);
            }
        });
        Button saveButton = findViewById(R.id.Savebutton);
        saveButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                String cityName = searchEditText.getText().toString();
                weatherAPI.getWeatherDataForCity(cityName, (WeatherDataListener) WeatherActivity.this);

            }
        });
        WeatherDataAdapter adapter = new WeatherDataAdapter();
        weatherRecyclerView.setAdapter(new WeatherDataAdapter());




    }




    @Override
    public void onDataReceived(WeatherData weatherData) {
        weatherDataList.add(weatherData);
        updateWeatherData(weatherData);
        weatherRecyclerView.getAdapter().notifyDataSetChanged();
        ((WeatherDataAdapter) weatherRecyclerView.getAdapter()).updateData(weatherDataList);
        //updateWeatherData(weatherData);
    }

    @Override
    public void onError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void updateWeatherData(WeatherData weatherData) {
        cityTextView.setText(weatherData.getCityName());
        temperatureTextView.setText(String.format(Locale.getDefault(), "%.1f °C", weatherData.getTemperature()));
        weatherConditionTextView.setText(weatherData.getWeatherCondition());

        String searchResultsText = String.format(Locale.getDefault(), "Search results for \"%s\":\n%s", searchEditText.getText().toString(), weatherData.getWeatherCondition());
        searchResultsTextView.setText(searchResultsText);

        // Display the weather icon
        String iconUrl = weatherData.getWeatherIconUrl();
        if (iconUrl != null && !iconUrl.isEmpty()) {
            Glide.with(this).load(iconUrl).into(weatherIconImageView);
        }
    }








}
