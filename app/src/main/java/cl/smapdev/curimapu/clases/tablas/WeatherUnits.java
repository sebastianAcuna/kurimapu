package cl.smapdev.curimapu.clases.tablas;

import com.google.gson.annotations.SerializedName;

public class WeatherUnits {

    @SerializedName("temp")
    public String temp;
    @SerializedName("wind")
    public String wind;
    @SerializedName("rain")
    public String rain;
    @SerializedName("pressure")
    public String pressure;
    @SerializedName("snowline")
    public String snow;


    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getRain() {
        return rain;
    }

    public void setRain(String rain) {
        this.rain = rain;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getSnow() {
        return snow;
    }

    public void setSnow(String snow) {
        this.snow = snow;
    }
}
