package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "comuna")
public class Comuna {

    @SerializedName("id_comuna")
    @PrimaryKey()
    private int id_comuna;
    @SerializedName("nombre")
    private String desc_comuna;
    @SerializedName("id_provincia")
    private int id_provincia_comuna;


    public Comuna() {
    }


    public int getId_comuna() {
        return id_comuna;
    }

    public void setId_comuna(int id_comuna) {
        this.id_comuna = id_comuna;
    }

    public String getDesc_comuna() {
        return desc_comuna;
    }

    public void setDesc_comuna(String desc_comuna) {
        this.desc_comuna = desc_comuna;
    }


    public int getId_provincia_comuna() {
        return id_provincia_comuna;
    }

    public void setId_provincia_comuna(int id_provincia_comuna) {
        this.id_provincia_comuna = id_provincia_comuna;
    }
}
