package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "pro_cli_mat")
public class pro_cli_mat {

    @PrimaryKey
    @SerializedName("id_prop_mat_cli")
    private int id_prop_mat_cli;

    @SerializedName("id_cli")
    private int id_cli;
    @SerializedName("id_esp")
    private String id_materiales;
    @SerializedName("id_prop")
    private int id_prop;
    @SerializedName("id_etapa")
    private int id_etapa;
    @SerializedName("id_tempo")
    private String id_tempo;
    @SerializedName("nombre_prop_es")
    private String nombre_es;

    @SerializedName("nombre_prop_en")
    private String nombre_en;

    @SerializedName("nombre_elemento_en")
    private String nombre_elemento_en;

    @SerializedName("nombre_elemento_es")
    private String nombre_elemento_es;

    @SerializedName("aplica")
    private String aplica;

    @SerializedName("orden")
    private int orden;

    @SerializedName("tipo_campo")
    private String tipo_cambio;

    @SerializedName("es_lista")
    private String es_lista;

    /* 04 - 05 - 2020 */
    @SerializedName("foraneo")
    private String foraneo;

    @SerializedName("tabla")
    private String tabla;


    @SerializedName("campo")
    private String campo;


    @SerializedName("marca_sitios_no_visitados")
    private int marca_sitios_no_visitados;

    @SerializedName("id_sub_propiedad_pcm")
    private int id_sub_propiedad_pcm;


    public int getMarca_sitios_no_visitados() {
        return marca_sitios_no_visitados;
    }

    public void setMarca_sitios_no_visitados(int marca_sitios_no_visitados) {
        this.marca_sitios_no_visitados = marca_sitios_no_visitados;
    }

    public int getId_sub_propiedad_pcm() {
        return id_sub_propiedad_pcm;
    }

    public void setId_sub_propiedad_pcm(int id_sub_propiedad_pcm) {
        this.id_sub_propiedad_pcm = id_sub_propiedad_pcm;
    }

    public String getForaneo() {
        return foraneo;
    }

    public void setForaneo(String foraneo) {
        this.foraneo = foraneo;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getEs_lista() {
        return es_lista;
    }

    public void setEs_lista(String es_lista) {
        this.es_lista = es_lista;
    }

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

    public String getId_materiales() {
        return id_materiales;
    }

    public void setId_materiales(String id_materiales) {
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

    public String getId_tempo() {
        return id_tempo;
    }

    public void setId_tempo(String id_tempo) {
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


    public String getNombre_elemento_en() {
        return nombre_elemento_en;
    }

    public void setNombre_elemento_en(String nombre_elemento_en) {
        this.nombre_elemento_en = nombre_elemento_en;
    }

    public String getNombre_elemento_es() {
        return nombre_elemento_es;
    }

    public void setNombre_elemento_es(String nombre_elemento_es) {
        this.nombre_elemento_es = nombre_elemento_es;
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
