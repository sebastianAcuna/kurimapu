package cl.smapdev.curimapu.clases.tablas;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "region")
public class Region {

    @SerializedName("id_region")
    @PrimaryKey()
    private int id_region;
    @SerializedName("nombre")
    private String desc_region;
    @SerializedName("id_pais")
    private int id_pais;

    public Region() {
    }

    public int getId_pais() {
        return id_pais;
    }

    public void setId_pais(int id_pais) {
        this.id_pais = id_pais;
    }

    public int getId_region() {
        return id_region;
    }

    public void setId_region(int id_region) {
        this.id_region = id_region;
    }

    public String getDesc_region() {
        return desc_region;
    }

    public void setDesc_region(String desc_region) {
        this.desc_region = desc_region;
    }
}
