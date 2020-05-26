package cl.smapdev.curimapu.clases.tablas;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "card_view_resumen")
public class CardViewsResumen {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @SerializedName("nombre")
    private String nombre;

    @SerializedName("total")
    private String total;

    @SerializedName("tempo")
    @Nullable
    private String id_tempo_cardiview;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getId_tempo_cardiview() {
        return id_tempo_cardiview;
    }

    public void setId_tempo_cardiview(String id_tempo_cardiview) {
        this.id_tempo_cardiview = id_tempo_cardiview;
    }
}
