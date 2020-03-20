package cl.smapdev.curimapu.clases.tablas;


import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "fichas")
public class Fichas  implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id_ficha")
    private int id_ficha;
    @SerializedName("id_tempo")
    private String anno;

    @SerializedName("id_agric")
    private String id_agricultor_ficha;

    @SerializedName("oferta_de_negocio")
    private String oferta_negocio;

    @SerializedName("id_region")
    private String id_region_ficha;

    @SerializedName("id_comuna")
    private String id_comuna_ficha;

    @SerializedName("localidad")
    private String localidad;

    @SerializedName("ha_disponibles")
    private double has_disponible;

    @SerializedName("obs")
    private String observaciones;

//    @SerializedName("")
    private double norting;

//    @SerializedName("")
    private double easting;

    @SerializedName("id_est_fic")
    private int activa; /* 1 : confeccion, 2: activa, 3: rechazada */

    @SerializedName("subida")
    private boolean subida; /* true : subida, false : no subida */


    @SerializedName("id_pred")
    @Expose
    private int id_pred_ficha;


    @SerializedName("id_lote")
    @Expose
    private int id_lote_ficha;


    @SerializedName("coo_utm_ref")
    @Expose
    private String coo_utm_ref_ficha;


    @SerializedName("coo_utm_ampros")
    @Expose
    private String coo_utm_ampros_ficha;


    @SerializedName("id_tipo_suelo")
    @Expose
    private String id_tipo_suelo;


    @SerializedName("id_tipo_riego")
    @Expose
    private String id_tipo_riego;


    @SerializedName("experiencia")
    @Expose
    private String experiencia;

    @SerializedName("id_usuario")
    @Expose
    private int id_usuario;


    @SerializedName("id_tipo_tenencia_maquinaria")
    @Expose
    private String id_tipo_tenencia_maquinaria;


    @SerializedName("id_tipo_tenencia_terreno")
    @Expose
    private String id_tipo_tenencia_terreno;

    @SerializedName("maleza")
    @Expose
    private String maleza;


    public Fichas() {
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_pred_ficha() {
        return id_pred_ficha;
    }

    public void setId_pred_ficha(int id_pred_ficha) {
        this.id_pred_ficha = id_pred_ficha;
    }

    public int getId_lote_ficha() {
        return id_lote_ficha;
    }

    public void setId_lote_ficha(int id_lote_ficha) {
        this.id_lote_ficha = id_lote_ficha;
    }

    public String getCoo_utm_ref_ficha() {
        return coo_utm_ref_ficha;
    }

    public void setCoo_utm_ref_ficha(String coo_utm_ref_ficha) {
        this.coo_utm_ref_ficha = coo_utm_ref_ficha;
    }

    public String getCoo_utm_ampros_ficha() {
        return coo_utm_ampros_ficha;
    }

    public void setCoo_utm_ampros_ficha(String coo_utm_ampros_ficha) {
        this.coo_utm_ampros_ficha = coo_utm_ampros_ficha;
    }

    public String getId_tipo_suelo() {
        return id_tipo_suelo;
    }

    public void setId_tipo_suelo(String id_tipo_suelo) {
        this.id_tipo_suelo = id_tipo_suelo;
    }

    public String getId_tipo_riego() {
        return id_tipo_riego;
    }

    public void setId_tipo_riego(String id_tipo_riego) {
        this.id_tipo_riego = id_tipo_riego;
    }

    public String getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(String experiencia) {
        this.experiencia = experiencia;
    }

    public String getId_tipo_tenencia_maquinaria() {
        return id_tipo_tenencia_maquinaria;
    }

    public void setId_tipo_tenencia_maquinaria(String id_tipo_tenencia_maquinaria) {
        this.id_tipo_tenencia_maquinaria = id_tipo_tenencia_maquinaria;
    }

    public String getId_tipo_tenencia_terreno() {
        return id_tipo_tenencia_terreno;
    }

    public void setId_tipo_tenencia_terreno(String id_tipo_tenencia_terreno) {
        this.id_tipo_tenencia_terreno = id_tipo_tenencia_terreno;
    }

    public String getMaleza() {
        return maleza;
    }

    public void setMaleza(String maleza) {
        this.maleza = maleza;
    }

    public String getAnno() {
        return anno;
    }

    public void setAnno(String anno) {
        this.anno = anno;
    }

    public String getId_region_ficha() {
        return id_region_ficha;
    }

    public void setId_region_ficha(String id_region_ficha) {
        this.id_region_ficha = id_region_ficha;
    }

    public String getId_comuna_ficha() {
        return id_comuna_ficha;
    }

    public void setId_comuna_ficha(String id_comuna_ficha) {
        this.id_comuna_ficha = id_comuna_ficha;
    }

    public int getId_ficha() {
        return id_ficha;
    }

    public void setId_ficha(int id_ficha) {
        this.id_ficha = id_ficha;
    }




    public String getId_agricultor_ficha() {
        return id_agricultor_ficha;
    }

    public void setId_agricultor_ficha(String id_agricultor_ficha) {
        this.id_agricultor_ficha = id_agricultor_ficha;
    }

    public String getOferta_negocio() {
        return oferta_negocio;
    }

    public void setOferta_negocio(String oferta_negocio) {
        this.oferta_negocio = oferta_negocio;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public double getHas_disponible() {
        return has_disponible;
    }

    public void setHas_disponible(double has_disponible) {
        this.has_disponible = has_disponible;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public double getNorting() {
        return norting;
    }

    public void setNorting(double norting) {
        this.norting = norting;
    }

    public double getEasting() {
        return easting;
    }

    public void setEasting(double easting) {
        this.easting = easting;
    }

    public int getActiva() {
        return activa;
    }

    public void setActiva(int activa) {
        this.activa = activa;
    }

    public boolean isSubida() {
        return subida;
    }

    public void setSubida(boolean subida) {
        this.subida = subida;
    }
}
