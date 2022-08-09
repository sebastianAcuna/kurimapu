package cl.smapdev.curimapu.clases.tablas;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherApiStatus {

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("day")
    @Expose
    private WeatherDays day;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public WeatherDays getDay() {
        return day;
    }

    public void setDay(WeatherDays day) {
        this.day = day;
    }
}



