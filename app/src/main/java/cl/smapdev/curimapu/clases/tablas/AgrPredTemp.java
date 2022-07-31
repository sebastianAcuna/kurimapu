package cl.smapdev.curimapu.clases.tablas;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "agri_pred_temp")
public class AgrPredTemp {

    @PrimaryKey
    @SerializedName("id_agri_pred_temp")
    private int id_agri_pred_temp;

    @SerializedName("id_agric")
    @Expose
    private int id_agric;

    @SerializedName("id_pred")
    @Expose
    private int id_pred;

    @SerializedName("id_tempo")
    @Expose
    private int id_tempo;


    @SerializedName("norting")
    @Expose
    private String norting;


    @SerializedName("easting")
    @Expose
    private String easting;


    public int getId_agri_pred_temp() {
        return id_agri_pred_temp;
    }

    public void setId_agri_pred_temp(int id_agri_pred_temp) {
        this.id_agri_pred_temp = id_agri_pred_temp;
    }

    public int getId_agric() {
        return id_agric;
    }

    public void setId_agric(int id_agric) {
        this.id_agric = id_agric;
    }

    public int getId_pred() {
        return id_pred;
    }

    public void setId_pred(int id_pred) {
        this.id_pred = id_pred;
    }

    public int getId_tempo() {
        return id_tempo;
    }

    public void setId_tempo(int id_tempo) {
        this.id_tempo = id_tempo;
    }

    public String getNorting() {
        return norting;
    }

    public void setNorting(String norting) {
        this.norting = norting;
    }

    public String getEasting() {
        return easting;
    }

    public void setEasting(String easting) {
        this.easting = easting;
    }
}
