package cl.smapdev.curimapu.clases.tablas;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "ficha_maquinaria")
public class FichaMaquinaria {

    @PrimaryKey
    @SerializedName("id_ficha_maquinaria")
    @NonNull
    private String id_ficha_maquinaria;

    @SerializedName("id_maquinaria")
    private String id_maquinaria;

    @SerializedName("id_ficha")
    private int id_ficha;

    public String getId_ficha_maquinaria() {
        return id_ficha_maquinaria;
    }

    public void setId_ficha_maquinaria(String id_ficha_maquinaria) {
        this.id_ficha_maquinaria = id_ficha_maquinaria;
    }

    public String getId_maquinaria() {
        return id_maquinaria;
    }

    public void setId_maquinaria(String id_maquinaria) {
        this.id_maquinaria = id_maquinaria;
    }

    public int getId_ficha() {
        return id_ficha;
    }

    public void setId_ficha(int id_ficha) {
        this.id_ficha = id_ficha;
    }
}
