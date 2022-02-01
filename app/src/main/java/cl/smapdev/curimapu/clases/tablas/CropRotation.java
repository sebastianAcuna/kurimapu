package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "crop_rotation")
public class CropRotation {


    @PrimaryKey(autoGenerate = true)
    @SerializedName("id_his_pre")
    private int id_crop_rotation;
    @SerializedName("anno")
    private String temporada_crop_rotation;
    @SerializedName("descripcion")
    private String cultivo_crop_rotation;

    @SerializedName("id_ficha")
    private int id_ficha_crop_rotation;

    @SerializedName("id_ficha_local")
    private int id_ficha_local_cp;

    @SerializedName("estado_subida")
    private int estado_subida_crop_rotation;

    @SerializedName("cabecera_crop")
    private int cabecera_crop;

    @SerializedName("tipo_crop")
    private String tipo_crop;

    public String getTipo_crop() {
        return tipo_crop;
    }

    public void setTipo_crop(String tipo_crop) {
        this.tipo_crop = tipo_crop;
    }

    private String id_anexo_crop_rotation;


    public int getCabecera_crop() {
        return cabecera_crop;
    }

    public void setCabecera_crop(int cabecera_crop) {
        this.cabecera_crop = cabecera_crop;
    }

    @Ignore
    public CropRotation(String temporada_crop_rotation, String cultivo_crop_rotation, int id_ficha_crop_rotation, int id_ficha_local_cp, int estado_subida_crop_rotation) {
        this.temporada_crop_rotation = temporada_crop_rotation;
        this.cultivo_crop_rotation = cultivo_crop_rotation;
        this.id_ficha_crop_rotation = id_ficha_crop_rotation;
        this.id_ficha_local_cp = id_ficha_local_cp;
        this.estado_subida_crop_rotation = estado_subida_crop_rotation;
    }

    public int getId_ficha_local_cp() {
        return id_ficha_local_cp;
    }

    public void setId_ficha_local_cp(int id_ficha_local_cp) {
        this.id_ficha_local_cp = id_ficha_local_cp;
    }

    public int getEstado_subida_crop_rotation() {
        return estado_subida_crop_rotation;
    }

    public void setEstado_subida_crop_rotation(int estado_subida_crop_rotation) {
        this.estado_subida_crop_rotation = estado_subida_crop_rotation;
    }

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
