package cl.smapdev.curimapu.clases.tablas;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName =  "tipo_suelo")
public class TipoSuelo {

    @PrimaryKey
    @NonNull
    @SerializedName("id_tipo_suelo")
    private String id_tipo_suelo;


    @SerializedName("descripcion")
    private String descripcion;

    public String getId_tipo_suelo() {
        return id_tipo_suelo;
    }

    public void setId_tipo_suelo(String id_tipo_suelo) {
        this.id_tipo_suelo = id_tipo_suelo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
