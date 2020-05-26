package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "quotation")
public class quotation {

    @PrimaryKey
    private int id_de_quo;


    private int id_quotation;


    private int id_materiales;

    @SerializedName("id_cli")
    private int cliente;

    private String superficie_contr;


    public String getSuperficie_contr() {
        return superficie_contr;
    }

    public void setSuperficie_contr(String superficie_contr) {
        this.superficie_contr = superficie_contr;
    }

    public int getId_de_quo() {
        return id_de_quo;
    }

    public void setId_de_quo(int id_de_quo) {
        this.id_de_quo = id_de_quo;
    }

    public int getId_quotation() {
        return id_quotation;
    }

    public void setId_quotation(int id_quotation) {
        this.id_quotation = id_quotation;
    }

    public int getId_materiales() {
        return id_materiales;
    }

    public void setId_materiales(int id_materiales) {
        this.id_materiales = id_materiales;
    }

    public int getCliente() {
        return cliente;
    }

    public void setCliente(int cliente) {
        this.cliente = cliente;
    }
}
