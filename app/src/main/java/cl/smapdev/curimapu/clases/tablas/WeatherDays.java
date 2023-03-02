package cl.smapdev.curimapu.clases.tablas;

import com.google.gson.annotations.SerializedName;

public class WeatherDays {

    @SerializedName("1")
    private WeatherApi dayOne;
    @SerializedName("2")
    private WeatherApi dayTwo;
    @SerializedName("3")
    private WeatherApi dayThree;
    @SerializedName("4")
    private WeatherApi dayFour;
    @SerializedName("5")
    private WeatherApi dayFive;

    public WeatherApi getDayOne() {
        return dayOne;
    }

    public void setDayOne(WeatherApi dayOne) {
        this.dayOne = dayOne;
    }

    public WeatherApi getDayTwo() {
        return dayTwo;
    }

    public void setDayTwo(WeatherApi dayTwo) {
        this.dayTwo = dayTwo;
    }

    public WeatherApi getDayThree() {
        return dayThree;
    }

    public void setDayThree(WeatherApi dayThree) {
        this.dayThree = dayThree;
    }

    public WeatherApi getDayFour() {
        return dayFour;
    }

    public void setDayFour(WeatherApi dayFour) {
        this.dayFour = dayFour;
    }

    public WeatherApi getDayFive() {
        return dayFive;
    }

    public void setDayFive(WeatherApi dayFive) {
        this.dayFive = dayFive;
    }
}
