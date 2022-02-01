package cl.smapdev.curimapu.clases.tablas;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "unidad_medida")
public class UnidadMedida {

    @PrimaryKey
    @SerializedName("id_um")
    @NonNull
    private String id_um;


    @SerializedName("nombre")
    private String nombre_um;


    public String getId_um() {
        return id_um;
    }

    public void setId_um(String id_um) {
        this.id_um = id_um;
    }

    public String getNombre_um() {
        return nombre_um;
    }

    public void setNombre_um(String nombre_um) {
        this.nombre_um = nombre_um;
    }
}
