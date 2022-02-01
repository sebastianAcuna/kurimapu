package cl.smapdev.curimapu.clases.tablas;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "especie")
public class Especie {

    @PrimaryKey()
    @SerializedName("id_esp")
    @NonNull
    private String id_especie;
    @SerializedName("nombre")
    private String desc_especie;


    public Especie() {
    }

    public String getId_especie() {
        return id_especie;
    }

    public void setId_especie(String id_especie) {
        this.id_especie = id_especie;
    }

    public String getDesc_especie() {
        return desc_especie;
    }

    public void setDesc_especie(String desc_especie) {
        this.desc_especie = desc_especie;
    }
}
