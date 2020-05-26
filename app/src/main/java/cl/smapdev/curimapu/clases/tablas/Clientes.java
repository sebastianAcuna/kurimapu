package cl.smapdev.curimapu.clases.tablas;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "cliente")
public class Clientes {




    @PrimaryKey(autoGenerate = true)
    private int primarykeyCliente;

    @SerializedName("id_cli")
    private int id_clientes_tabla;

    @SerializedName("id_materiales")
    private String id_materiales_clientes;

    @SerializedName("razon_social")
    private String razon_social_clientes;


    public int getPrimarykeyCliente() {
        return primarykeyCliente;
    }

    public void setPrimarykeyCliente(int primarykeyCliente) {
        this.primarykeyCliente = primarykeyCliente;
    }

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

    public String getRazon_social_clientes() {
        return razon_social_clientes;
    }

    public void setRazon_social_clientes(String razon_social_clientes) {
        this.razon_social_clientes = razon_social_clientes;
    }

}
