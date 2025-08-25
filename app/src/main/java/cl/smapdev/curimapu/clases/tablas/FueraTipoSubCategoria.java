package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "fuera_tipo_sub_categoria")
public class FueraTipoSubCategoria {

    @PrimaryKey()
    @SerializedName("id_fuera_tipo_sub_cat")
    @Expose
    private int id_fuera_tipo_sub_cat;


    @SerializedName("id_fuera_tipo_cat")
    @Expose
    private int id_fuera_tipo_cat;


    @SerializedName("descripcion_sub_cat")
    @Expose
    private String descripcion_sub_cat;

    public int getId_fuera_tipo_sub_cat() {
        return id_fuera_tipo_sub_cat;
    }

    public void setId_fuera_tipo_sub_cat(int id_fuera_tipo_sub_cat) {
        this.id_fuera_tipo_sub_cat = id_fuera_tipo_sub_cat;
    }

    public int getId_fuera_tipo_cat() {
        return id_fuera_tipo_cat;
    }

    public void setId_fuera_tipo_cat(int id_fuera_tipo_cat) {
        this.id_fuera_tipo_cat = id_fuera_tipo_cat;
    }

    public String getDescripcion_sub_cat() {
        return descripcion_sub_cat;
    }

    public void setDescripcion_sub_cat(String descripcion_sub_cat) {
        this.descripcion_sub_cat = descripcion_sub_cat;
    }
}
