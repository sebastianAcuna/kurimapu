package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "lote")
public class Lotes  implements Serializable {

    @PrimaryKey
    @SerializedName("id_lote")
    private int lote;

    @SerializedName("id_pred")
    private int id_pred_lote;

    @SerializedName("id_comuna")
    private String id_comuna_lote;

    @SerializedName("id_region")
    private String id_region_lote;

    @SerializedName("nombre")
    private String nombre_lote;

    @SerializedName("coo_utm_ampros")
    private String coo_utm_ampros;

    @SerializedName("nombre_ac")
    private String nombre_ac;

    @SerializedName("telefono_ac")
    private String telefono_ac;


    public String getNombre_ac() {
        return nombre_ac;
    }

    public void setNombre_ac(String nombre_ac) {
        this.nombre_ac = nombre_ac;
    }

    public int getLote() {
        return lote;
    }

    public void setLote(int lote) {
        this.lote = lote;
    }

    public int getId_pred_lote() {
        return id_pred_lote;
    }

    public void setId_pred_lote(int id_pred_lote) {
        this.id_pred_lote = id_pred_lote;
    }

    public String getId_comuna_lote() {
        return id_comuna_lote;
    }

    public void setId_comuna_lote(String id_comuna_lote) {
        this.id_comuna_lote = id_comuna_lote;
    }

    public String getId_region_lote() {
        return id_region_lote;
    }

    public void setId_region_lote(String id_region_lote) {
        this.id_region_lote = id_region_lote;
    }

    public String getNombre_lote() {
        return nombre_lote;
    }

    public void setNombre_lote(String nombre_lote) {
        this.nombre_lote = nombre_lote;
    }

    public String getCoo_utm_ampros() {
        return coo_utm_ampros;
    }

    public void setCoo_utm_ampros(String coo_utm_ampros) {
        this.coo_utm_ampros = coo_utm_ampros;
    }

    public String getTelefono_ac() {
        return telefono_ac;
    }

    public void setTelefono_ac(String telefono_ac) {
        this.telefono_ac = telefono_ac;
    }
}
