package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "crop_rotation")
public class CropRotation {


    @PrimaryKey()
    @SerializedName("id_his_pre")
    private int id_crop_rotation;
    @SerializedName("anno")
    private String temporada_crop_rotation;
    @SerializedName("descripcion")
    private String cultivo_crop_rotation;
    @SerializedName("id_ficha")
    private int id_ficha_crop_rotation;

    private String id_anexo_crop_rotation;

    public CropRotation() {
    }


    public int getId_crop_rotation() {
        return id_crop_rotation;
    }

    public void setId_crop_rotation(int id_crop_rotation) {
        this.id_crop_rotation = id_crop_rotation;
    }

    public String getTemporada_crop_rotation() {
        return temporada_crop_rotation;
    }

    public void setTemporada_crop_rotation(String temporada_crop_rotation) {
        this.temporada_crop_rotation = temporada_crop_rotation;
    }

    public String getId_anexo_crop_rotation() {
        return id_anexo_crop_rotation;
    }

    public void setId_anexo_crop_rotation(String id_anexo_crop_rotation) {
        this.id_anexo_crop_rotation = id_anexo_crop_rotation;
    }

    public String getCultivo_crop_rotation() {
        return cultivo_crop_rotation;
    }

    public void setCultivo_crop_rotation(String cultivo_crop_rotation) {
        this.cultivo_crop_rotation = cultivo_crop_rotation;
    }

    public int getId_ficha_crop_rotation() {
        return id_ficha_crop_rotation;
    }

    public void setId_ficha_crop_rotation(int id_ficha_crop_rotation) {
        this.id_ficha_crop_rotation = id_ficha_crop_rotation;
    }

}
