package cl.smapdev.curimapu.clases.tablas;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "comuna")
public class Comuna implements Serializable {

    @SerializedName("id_comuna")
    @PrimaryKey()
    @NonNull
    private String id_comuna;
    @SerializedName("nombre")
    private String desc_comuna;
    @SerializedName("id_provincia")
    private String id_provincia_comuna;


    public Comuna() {
    }

    public String getDesc_comuna() {
        return desc_comuna;
    }

    public void setDesc_comuna(String desc_comuna) {
        this.desc_comuna = desc_comuna;
    }

    public String getId_comuna() {
        return id_comuna;
    }

    public void setId_comuna(String id_comuna) {
        this.id_comuna = id_comuna;
    }

    public String getId_provincia_comuna() {
        return id_provincia_comuna;
    }

    public void setId_provincia_comuna(String id_provincia_comuna) {
        this.id_provincia_comuna = id_provincia_comuna;
    }
}
