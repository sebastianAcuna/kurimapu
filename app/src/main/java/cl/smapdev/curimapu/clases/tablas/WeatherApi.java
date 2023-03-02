package cl.smapdev.curimapu.clases.tablas;

import com.google.gson.annotations.SerializedName;


public class WeatherApi {

    @SerializedName("date")
    public String date;

    @SerializedName("name")
    public String name;

    @SerializedName("symbol_value")
    public int imageWeather;

    @SerializedName("symbol_description")
    public String descriptionWeather;

    @SerializedName("tempmin")
    public String tempMin;

    @SerializedName("tempmax")
    public String tempMax;

    @SerializedName("rain")
    public String rain;

    @SerializedName("humidity")
    public String humidity;
    @SerializedName("pressure")
    public String pressure;
    @SerializedName("snowline")
    public String snowLine;

    @SerializedName("uv_index_max")
    public String uv_index_max;

    @SerializedName("units")
    public WeatherUnits units;


    @SerializedName("wind")

    private WeatherWind wind;


    public WeatherWind getWind() {
        return wind;
    }

    public void setWind(WeatherWind wind) {
        this.wind = wind;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageWeather() {
        return imageWeather;
    }

    public void setImageWeather(int imageWeather) {
        this.imageWeather = imageWeather;
    }

    public String getDescriptionWeather() {
        return descriptionWeather;
    }

    public void setDescriptionWeather(String descriptionWeather) {
        this.descriptionWeather = descriptionWeather;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getRain() {
        return rain;
    }

    public void setRain(String rain) {
        this.rain = rain;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getSnowLine() {
        return snowLine;
    }

    public void setSnowLine(String snowLine) {
        this.snowLine = snowLine;
    }

    public String getUv_index_max() {
        return uv_index_max;
    }

    public void setUv_index_max(String uv_index_max) {
        this.uv_index_max = uv_index_max;
    }

    public WeatherUnits getUnits() {
        return units;
    }

    public void setUnits(WeatherUnits units) {
        this.units = units;
    }
}


