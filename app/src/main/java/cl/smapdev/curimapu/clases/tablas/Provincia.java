package cl.smapdev.curimapu.clases.tablas;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "provincia")
public class Provincia  {

    @PrimaryKey
    @SerializedName("id_provincia")
    @NonNull
    private String id_provincia;

    @SerializedName("nombre")
    private String nombre_provincia;

     @SerializedName("id_region")
    private String id_region_provincia;


    public String getId_provincia() {
        return id_provincia;
    }

    public void setId_provincia(String id_provincia) {
        this.id_provincia = id_provincia;
    }

    public String getId_region_provincia() {
        return id_region_provincia;
    }

    public void setId_region_provincia(String id_region_provincia) {
        this.id_region_provincia = id_region_provincia;
    }

    public String getNombre_provincia() {
        return nombre_provincia;
    }

    public void setNombre_provincia(String nombre_provincia) {
        this.nombre_provincia = nombre_provincia;
    }


}
