package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "temporada")
public class Temporada {

    @SerializedName("id_tempo")
    @PrimaryKey
    private int id_tempo_tempo;

    @SerializedName("nombre")
    private String nombre_tempo;

    @SerializedName("desde")
    private String desde_tempo;

    @SerializedName("hasta")
    private String hasta_tempo;


    public int getId_tempo_tempo() {
        return id_tempo_tempo;
    }

    public void setId_tempo_tempo(int id_tempo_tempo) {
        this.id_tempo_tempo = id_tempo_tempo;
    }

    public String getNombre_tempo() {
        return nombre_tempo;
    }

    public void setNombre_tempo(String nombre_tempo) {
        this.nombre_tempo = nombre_tempo;
    }

    public String getDesde_tempo() {
        return desde_tempo;
    }

    public void setDesde_tempo(String desde_tempo) {
        this.desde_tempo = desde_tempo;
    }

    public String getHasta_tempo() {
        return hasta_tempo;
    }

    public void setHasta_tempo(String hasta_tempo) {
        this.hasta_tempo = hasta_tempo;
    }
}
