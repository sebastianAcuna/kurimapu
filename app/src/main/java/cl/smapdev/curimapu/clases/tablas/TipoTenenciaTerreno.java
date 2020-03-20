package cl.smapdev.curimapu.clases.tablas;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tipo_tenencia_terreno")
public class TipoTenenciaTerreno {

    @PrimaryKey
    @SerializedName("id_tipo_tenencia_terreno")
    @NonNull
    private String id_tipo_tenencia_terreno;

    @SerializedName("descripcion")
    private String descripcion;

    public String getId_tipo_tenencia_terreno() {
        return id_tipo_tenencia_terreno;
    }

    public void setId_tipo_tenencia_terreno(String id_tipo_tenencia_terreno) {
        this.id_tipo_tenencia_terreno = id_tipo_tenencia_terreno;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
