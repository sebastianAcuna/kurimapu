package cl.smapdev.curimapu.clases.tablas;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "materiales")
public class Variedad {

    @PrimaryKey()
    @SerializedName("id_materiales")
    @NonNull
    private String id_variedad;
    @SerializedName("id_esp")
    private String id_especie_variedad;

    @SerializedName("nom_fantasia")
    private String desc_variedad;

    @SerializedName("nom_hibrido")
    private String desc_hibrido_variedad;

    @SerializedName("condition")
    private String condition;

    @SerializedName("certification")
    private String certification;

    @SerializedName("condition_mat")
    private String condition_mat;

    public Variedad() {
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCondition_mat() {
        return condition_mat;
    }

    public void setCondition_mat(String condition_mat) {
        this.condition_mat = condition_mat;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public String getDesc_hibrido_variedad() {
        return desc_hibrido_variedad;
    }

    public void setDesc_hibrido_variedad(String desc_hibrido_variedad) {
        this.desc_hibrido_variedad = desc_hibrido_variedad;
    }


    public String getId_variedad() {
        return id_variedad;
    }

    public void setId_variedad(String id_variedad) {
        this.id_variedad = id_variedad;
    }

    public String getId_especie_variedad() {
        return id_especie_variedad;
    }

    public void setId_especie_variedad(String id_especie_variedad) {
        this.id_especie_variedad = id_especie_variedad;
    }

    public String getDesc_variedad() {
        return desc_variedad;
    }

    public void setDesc_variedad(String desc_variedad) {
        this.desc_variedad = desc_variedad;
    }
}
