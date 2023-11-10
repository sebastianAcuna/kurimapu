package cl.smapdev.curimapu.clases.tablas;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "temporada")
public class Temporada {

    @SerializedName("id_tempo")
    @PrimaryKey
    @NonNull
    private String id_tempo_tempo;

    @SerializedName("nombre")
    private String nombre_tempo;

    @SerializedName("desde")
    private String desde_tempo;

    @SerializedName("hasta")
    private String hasta_tempo;

    @SerializedName("especial")
    private int especial_temporada;


    @SerializedName("default_season")
    private int default_season;

    public int getDefault_season() {
        return default_season;
    }

    public void setDefault_season(int default_season) {
        this.default_season = default_season;
    }

    public int getEspecial_temporada() {
        return especial_temporada;
    }

    public void setEspecial_temporada(int especial_temporada) {
        this.especial_temporada = especial_temporada;
    }

    public String getId_tempo_tempo() {
        return id_tempo_tempo;
    }

    public void setId_tempo_tempo(String id_tempo_tempo) {
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
