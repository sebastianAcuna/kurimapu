package cl.smapdev.curimapu.clases.tablas;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "maquinaria")
public class Maquinaria {

    @PrimaryKey
    @SerializedName("id_maquinaria")
    @NonNull
    private String id_maquinaria;

    @SerializedName("descripcion")
    private String descripcion;


    public String getId_maquinaria() {
        return id_maquinaria;
    }

    public void setId_maquinaria(String id_maquinaria) {
        this.id_maquinaria = id_maquinaria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
