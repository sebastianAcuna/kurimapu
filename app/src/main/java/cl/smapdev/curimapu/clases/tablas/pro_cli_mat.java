package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "pro_cli_mat")
public class pro_cli_mat {

    @PrimaryKey
    private int id_prop_mat_cli;

    @SerializedName("id_cli")
    private int id_cli;
    @SerializedName("id_materiales")
    private int id_materiales;
    @SerializedName("id_prop")
    private int id_prop;
    @SerializedName("id_etapa")
    private int id_etapa;
    @SerializedName("id_tempo")
    private int id_tempo;
    @SerializedName("nombre_prop_es")
    private String nombre_es;

    @SerializedName("nombre_prop_en")
    private String nombre_en;

    @SerializedName("nombre_elemento")
    private String nombre_elemento;

    @SerializedName("aplica")
    private String aplica;

    @SerializedName("orden")
    private int orden;

    @SerializedName("tipo_campo")
    private String tipo_cambio;



    public int getId_prop_mat_cli() {
        return id_prop_mat_cli;
    }

    public void setId_prop_mat_cli(int id_prop_mat_cli) {
        this.id_prop_mat_cli = id_prop_mat_cli;
    }

    public int getId_cli() {
        return id_cli;
    }

    public void setId_cli(int id_cli) {
        this.id_cli = id_cli;
    }

    public int getId_materiales() {
        return id_materiales;
    }

    public void setId_materiales(int id_materiales) {
        this.id_materiales = id_materiales;
    }

    public int getId_prop() {
        return id_prop;
    }

    public void setId_prop(int id_prop) {
        this.id_prop = id_prop;
    }

    public int getId_etapa() {
        return id_etapa;
    }

    public void setId_etapa(int id_etapa) {
        this.id_etapa = id_etapa;
    }

    public int getId_tempo() {
        return id_tempo;
    }

    public void setId_tempo(int id_tempo) {
        this.id_tempo = id_tempo;
    }

    public String getNombre_es() {
        return nombre_es;
    }

    public void setNombre_es(String nombre_es) {
        this.nombre_es = nombre_es;
    }

    public String getNombre_en() {
        return nombre_en;
    }

    public void setNombre_en(String nombre_en) {
        this.nombre_en = nombre_en;
    }


    public String getNombre_elemento() {
        return nombre_elemento;
    }

    public void setNombre_elemento(String nombre_elemento) {
        this.nombre_elemento = nombre_elemento;
    }

    public String getAplica() {
        return aplica;
    }

    public void setAplica(String aplica) {
        this.aplica = aplica;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getTipo_cambio() {
        return tipo_cambio;
    }

    public void setTipo_cambio(String tipo_cambio) {
        this.tipo_cambio = tipo_cambio;
    }
}
