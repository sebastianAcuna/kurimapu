package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "clientes")
public class Clientes {

    @PrimaryKey(autoGenerate = true)
    private int id_clientes_tabla;

    @SerializedName("id_materiales")
    private String id_materiales_clientes;

    @SerializedName("id_cli")
    private String id_cli_clientes;

    @SerializedName("razon_social")
    private String razon_social_clientes;

    @SerializedName("id_ac")
    private String id_ac_clientes;

    @SerializedName("id_quotation")
    private String id_quotation_clientes;

    @SerializedName("id_de_quo")
    private String id_de_quo_clientes;


    public int getId_clientes_tabla() {
        return id_clientes_tabla;
    }

    public void setId_clientes_tabla(int id_clientes_tabla) {
        this.id_clientes_tabla = id_clientes_tabla;
    }

    public String getId_materiales_clientes() {
        return id_materiales_clientes;
    }

    public void setId_materiales_clientes(String id_materiales_clientes) {
        this.id_materiales_clientes = id_materiales_clientes;
    }

    public String getId_cli_clientes() {
        return id_cli_clientes;
    }

    public void setId_cli_clientes(String id_cli_clientes) {
        this.id_cli_clientes = id_cli_clientes;
    }

    public String getRazon_social_clientes() {
        return razon_social_clientes;
    }

    public void setRazon_social_clientes(String razon_social_clientes) {
        this.razon_social_clientes = razon_social_clientes;
    }

    public String getId_ac_clientes() {
        return id_ac_clientes;
    }

    public void setId_ac_clientes(String id_ac_clientes) {
        this.id_ac_clientes = id_ac_clientes;
    }

    public String getId_quotation_clientes() {
        return id_quotation_clientes;
    }

    public void setId_quotation_clientes(String id_quotation_clientes) {
        this.id_quotation_clientes = id_quotation_clientes;
    }

    public String getId_de_quo_clientes() {
        return id_de_quo_clientes;
    }

    public void setId_de_quo_clientes(String id_de_quo_clientes) {
        this.id_de_quo_clientes = id_de_quo_clientes;
    }
}
