package cl.smapdev.curimapu.clases.tablas;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "region")
public class Region implements Serializable {

    @SerializedName("id_region")
    @PrimaryKey()
    @NonNull
    private String id_region;
    @SerializedName("nombre")
    private String desc_region;
    @SerializedName("id_pais")
    private String id_pais;

    public Region() {
    }


    public String getId_region() {
        return id_region;
    }

    public void setId_region(String id_region) {
        this.id_region = id_region;
    }

    public String getId_pais() {
        return id_pais;
    }

    public void setId_pais(String id_pais) {
        this.id_pais = id_pais;
    }

    public String getDesc_region() {
        return desc_region;
    }

    public void setDesc_region(String desc_region) {
        this.desc_region = desc_region;
    }
}
