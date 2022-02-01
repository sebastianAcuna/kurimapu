package cl.smapdev.curimapu.clases.relaciones;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.AnexoCorreoFechas;

public class SubirFechasRetro {

    @SerializedName("id_dispo")
    @Expose
    private int id_dispo;


    @SerializedName("id_usuario")
    @Expose
    private int id_usuario;


    @SerializedName("fechas_subida")
    @Expose
    private List<AnexoCorreoFechas> fechas;


    public int getId_dispo() {
        return id_dispo;
    }

    public void setId_dispo(int id_dispo) {
        this.id_dispo = id_dispo;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public List<AnexoCorreoFechas> getFechas() {
        return fechas;
    }

    public void setFechas(List<AnexoCorreoFechas> fechas) {
        this.fechas = fechas;
    }
}
