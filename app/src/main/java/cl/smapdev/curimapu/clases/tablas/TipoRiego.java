package cl.smapdev.curimapu.clases.tablas;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tipo_riego")
public class TipoRiego {

    @PrimaryKey
    @NonNull
    @SerializedName("id_tipo_riego")
    private String id_tipo_riego;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("vigencia")
    private String vigencia;


    public String getId_tipo_riego() {
        return id_tipo_riego;
    }

    public void setId_tipo_riego(String id_tipo_riego) {
        this.id_tipo_riego = id_tipo_riego;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public String getVigencia() {
        return vigencia;
    }

    public void setVigencia(String vigencia) {
        this.vigencia = vigencia;
    }
}
