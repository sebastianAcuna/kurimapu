package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "fuera_tipo_categoria")
public class FueraTipoCategoria {

    @PrimaryKey()
    @SerializedName("id_fuera_tipo_cat")
    @Expose
    private int id_fuera_tipo_cat;


    @SerializedName("descripcion")
    @Expose
    private String descripcion;


    public int getId_fuera_tipo_cat() {
        return id_fuera_tipo_cat;
    }

    public void setId_fuera_tipo_cat(int id_fuera_tipo_cat) {
        this.id_fuera_tipo_cat = id_fuera_tipo_cat;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
