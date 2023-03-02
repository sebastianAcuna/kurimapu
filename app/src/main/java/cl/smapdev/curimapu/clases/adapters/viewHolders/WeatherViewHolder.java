package cl.smapdev.curimapu.clases.adapters.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.tablas.WeatherApi;

public class WeatherViewHolder extends RecyclerView.ViewHolder {


    private ImageView weather_image;
    private TextView weather_day_desc;
    private TextView weather_date;
    private TextView weather_uv;
    private TextView weather_humidity;
    private TextView weather_min_temp;
    private TextView weather_max_temp;
    private TextView weather_description;
    private TextView weather_speed;
    private ImageView weather_image_speed;
    private TextView weather_gusts;
    private ImageView weather_image_gusts;

    public WeatherViewHolder(@NonNull View itemView) {
        super(itemView);

        weather_image = itemView.findViewById(R.id.weather_image);
        weather_day_desc = itemView.findViewById(R.id.weather_day_desc);
        weather_date = itemView.findViewById(R.id.weather_date);
        weather_uv = itemView.findViewById(R.id.weather_uv);
        weather_humidity = itemView.findViewById(R.id.weather_humidity);
        weather_min_temp = itemView.findViewById(R.id.weather_min_temp);
        weather_max_temp = itemView.findViewById(R.id.weather_max_temp);
        weather_description = itemView.findViewById(R.id.weather_description);
        weather_speed = itemView.findViewById(R.id.weather_speed);
        weather_image_speed = itemView.findViewById(R.id.weather_image_speed);
        weather_gusts = itemView.findViewById(R.id.weather_gusts);
        weather_image_gusts = itemView.findViewById(R.id.weather_image_gusts);

    }

    public void bind(WeatherApi weatherApi){


        String year = "";
        String month = "";
        String day = "";
        String fecha = "";

        if(!weatherApi.getDate().isEmpty()){

            year = weatherApi.getDate().substring(0, 4);
            month = weatherApi.getDate().substring(4, 6);
            day = weatherApi.getDate().substring(6, 8);


//           String newMonth =  month;
//           String newDay = (day < 10) ? "0" + day : day+"";

            fecha = day + "/" + month + "/" + year;
        }




        String maxTemp = (weatherApi.getTempMax() != null) ? weatherApi.getTempMax()+"°" : "";
        String minTemp = (weatherApi.getTempMin() != null) ? weatherApi.getTempMin()+"°" : "";
        String humidity = (weatherApi.getHumidity() != null) ? weatherApi.getHumidity()+"%" : "";
        String uv =  (weatherApi.getUv_index_max() != null) ? weatherApi.getUv_index_max() : "";

        String gusts = (weatherApi.getWind() != null && weatherApi.getWind().getGusts() != null)
                ? weatherApi.getWind().getGusts()+" "+weatherApi.getUnits().getWind()
                : "";

        String speed = (weatherApi.getWind() != null && weatherApi.getWind().getSpeed() != null)
                ? weatherApi.getWind().getSpeed()+" "+weatherApi.getUnits().getWind()
                : "";





        weather_day_desc.setText(weatherApi.getName());
        weather_date.setText(fecha);
        weather_description.setText(weatherApi.getDescriptionWeather());

        weather_humidity.setText( humidity );
        weather_min_temp.setText(minTemp);
        weather_max_temp.setText( maxTemp );

        weather_gusts.setText( gusts );
        weather_speed.setText( speed );
        weather_uv.setText( uv );


        String imageWeather = "ic_" + weatherApi.getImageWeather();
        int idWeather = itemView.getContext().getResources().getIdentifier(imageWeather, "drawable", itemView.getContext().getPackageName());
        weather_image.setImageResource(idWeather);

        if(weatherApi.getWind() != null){
            String imageSpeed = "wind_" + weatherApi.getWind().getSpeed_image();

            int idSpeed = itemView.getContext().getResources().getIdentifier(imageSpeed, "drawable", itemView.getContext().getPackageName());
            weather_image_speed.setImageResource(idSpeed);

            String imageGust = "wind_" + weatherApi.getWind().getGusts_image();
            int idGust = itemView.getContext().getResources().getIdentifier(imageGust, "drawable", itemView.getContext().getPackageName());
            weather_image_gusts.setImageResource(idGust);
        }





    }
}
