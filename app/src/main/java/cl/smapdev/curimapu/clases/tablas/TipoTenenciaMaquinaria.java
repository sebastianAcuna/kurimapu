package cl.smapdev.curimapu.clases.tablas;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tipo_tenencia_maquinaria")
public class TipoTenenciaMaquinaria {


    @PrimaryKey
    @SerializedName("id_tipo_tenencia_maquinaria")
    @NonNull
    private String id_tipo_tenencia_maquinaria;

    @SerializedName("descripcion")
    private String descripcion;

    public String getId_tipo_tenencia_maquinaria() {
        return id_tipo_tenencia_maquinaria;
    }

    public void setId_tipo_tenencia_maquinaria(String id_tipo_tenencia_maquinaria) {
        this.id_tipo_tenencia_maquinaria = id_tipo_tenencia_maquinaria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
