package cl.smapdev.curimapu.clases.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.viewHolders.WeatherViewHolder;
import cl.smapdev.curimapu.clases.tablas.WeatherApi;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherViewHolder> {

    private List<WeatherApi> days;


    public WeatherAdapter(List<WeatherApi> days) {
        this.days = days;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_api_list, parent,false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        holder.bind( days.get(position) );
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
