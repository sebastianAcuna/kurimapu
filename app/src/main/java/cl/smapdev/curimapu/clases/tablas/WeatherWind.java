package cl.smapdev.curimapu.clases.tablas;

import com.google.gson.annotations.SerializedName;

public class WeatherWind {

    @SerializedName("speed")
    private String speed;

    @SerializedName("symbol")
    private String speed_image;

    @SerializedName("gusts")
    private String gusts;

    @SerializedName("symbolB")
    private String gusts_image;


    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getSpeed_image() {
        return speed_image;
    }

    public void setSpeed_image(String speed_image) {
        this.speed_image = speed_image;
    }

    public String getGusts() {
        return gusts;
    }

    public void setGusts(String gusts) {
        this.gusts = gusts;
    }

    public String getGusts_image() {
        return gusts_image;
    }

    public void setGusts_image(String gusts_image) {
        this.gusts_image = gusts_image;
    }
}
