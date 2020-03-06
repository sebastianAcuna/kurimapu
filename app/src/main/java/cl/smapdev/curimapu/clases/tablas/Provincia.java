package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "provincia")
public class Provincia  {

    @PrimaryKey
    @SerializedName("id_provincia")
    private int id_provincia;

    @SerializedName("nombre")
    private String nombre_provincia;

     @SerializedName("id_region")
    private int id_region_provincia;


    public int getId_provincia() {
        return id_provincia;
    }

    public void setId_provincia(int id_provincia) {
        this.id_provincia = id_provincia;
    }

    public String getNombre_provincia() {
        return nombre_provincia;
    }

    public void setNombre_provincia(String nombre_provincia) {
        this.nombre_provincia = nombre_provincia;
    }

    public int getId_region_provincia() {
        return id_region_provincia;
    }

    public void setId_region_provincia(int id_region_provincia) {
        this.id_region_provincia = id_region_provincia;
    }
}
