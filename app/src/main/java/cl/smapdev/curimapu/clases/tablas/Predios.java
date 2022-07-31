package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "predio")
public class Predios implements Serializable  {

    @PrimaryKey
    @SerializedName("id_pred")
    private int id_pred;

//    @SerializedName("id_agric")
//    private String id_agric;
//
//    @SerializedName("id_tempo")
//    private String id_tempo;

    @SerializedName("id_comuna")
    private String id_comuna;

    @SerializedName("id_region")
    private String id_region;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("coo_utm_ref")
    private String coo_utm_ref;


    public int getId_pred() {
        return id_pred;
    }

    public void setId_pred(int id_pred) {
        this.id_pred = id_pred;
    }





//    public String getId_agric() {
//        return id_agric;
//    }
//
//    public void setId_agric(String id_agric) {
//        this.id_agric = id_agric;
//    }
//
//    public String getId_tempo() {
//        return id_tempo;
//    }
//
//    public void setId_tempo(String id_tempo) {
//        this.id_tempo = id_tempo;
//    }





    public String getId_comuna() {
        return id_comuna;
    }

    public void setId_comuna(String id_comuna) {
        this.id_comuna = id_comuna;
    }

    public String getId_region() {
        return id_region;
    }

    public void setId_region(String id_region) {
        this.id_region = id_region;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCoo_utm_ref() {
        return coo_utm_ref;
    }

    public void setCoo_utm_ref(String coo_utm_ref) {
        this.coo_utm_ref = coo_utm_ref;
    }
}
