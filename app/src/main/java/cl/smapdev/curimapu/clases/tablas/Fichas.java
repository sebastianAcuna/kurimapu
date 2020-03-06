package cl.smapdev.curimapu.clases.tablas;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "fichas")
public class Fichas {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id_ficha")
    private int id_ficha;
    @SerializedName("id_tempo")
    private int anno;

    @SerializedName("id_agric")
    private int id_agricultor_ficha;

    @SerializedName("oferta_de_negocio")
    private String oferta_negocio;

    @SerializedName("id_region")
    private int id_region_ficha;

    @SerializedName("id_comuna")
    private int id_comuna_ficha;

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


    public Fichas() {
    }


    public int getId_region_ficha() {
        return id_region_ficha;
    }

    public void setId_region_ficha(int id_region_ficha) {
        this.id_region_ficha = id_region_ficha;
    }

    public int getId_comuna_ficha() {
        return id_comuna_ficha;
    }

    public void setId_comuna_ficha(int id_comuna_ficha) {
        this.id_comuna_ficha = id_comuna_ficha;
    }

    public int getId_ficha() {
        return id_ficha;
    }

    public void setId_ficha(int id_ficha) {
        this.id_ficha = id_ficha;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }


    public int getId_agricultor_ficha() {
        return id_agricultor_ficha;
    }

    public void setId_agricultor_ficha(int id_agricultor_ficha) {
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
