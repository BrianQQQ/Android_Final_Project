package com.example.final_project;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeatherDataAdapter extends RecyclerView.Adapter<WeatherDataViewHolder> {

    private List<WeatherData> weatherDataList = new ArrayList<>();

    @NonNull
    @Override
    public WeatherDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_weather, parent, false);
        return new WeatherDataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherDataViewHolder holder, int position) {
        WeatherData weatherData = weatherDataList.get(position);
        holder.cityTextView.setText(weatherData.getCityName());
        holder.temperatureTextView.setText(String.format(Locale.getDefault(), "%.1f °C", weatherData.getTemperature()));
        holder.weatherConditionTextView.setText(weatherData.getWeatherCondition());
        String iconUrl = weatherData.getWeatherIconUrl();
        if (iconUrl != null && !iconUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(iconUrl)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.weatherIconImageView);
        }
    }

    @Override
    public int getItemCount() {
        return weatherDataList.size();
    }

    public void setWeatherDataList(List<WeatherData> weatherDataList) {
        this.weatherDataList = weatherDataList;
        notifyDataSetChanged();
    }
    public void updateData(List<WeatherData> newData) {
        weatherDataList.clear();
        weatherDataList.addAll(newData);
        notifyDataSetChanged();
    }

}
