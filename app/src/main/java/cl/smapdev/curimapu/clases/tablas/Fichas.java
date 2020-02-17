package cl.smapdev.curimapu.clases.tablas;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "fichas")
public class Fichas {

    @PrimaryKey(autoGenerate = true)
    private int id_ficha;
    private int anno;
    private String rut_agricultor_fichas;
    private String oferta_negocio;
    private String localidad;
    private double has_disponible;
    private String observaciones;
    private double norting;
    private double easting;
    private int activa; /* 0 : provisoria, 1: activa, 2: rechazada */
    private boolean subida; /* true : subida, false : no subida */


    public Fichas() {
    }

//    public Fichas(int anno, String rut_agricultor_fichas, String oferta_negocio, String localidad, double has_disponible, String observaciones, double norting, double easting, int activa, boolean subida) {
//        this.anno = anno;
//        this.rut_agricultor_fichas = rut_agricultor_fichas;
//        this.oferta_negocio = oferta_negocio;
//        this.localidad = localidad;
//        this.has_disponible = has_disponible;
//        this.observaciones = observaciones;
//        this.norting = norting;
//        this.easting = easting;
//        this.activa = activa;
//        this.subida = subida;
//    }

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

    public String getRut_agricultor_fichas() {
        return rut_agricultor_fichas;
    }

    public void setRut_agricultor_fichas(String rut_agricultor_fichas) {
        this.rut_agricultor_fichas = rut_agricultor_fichas;
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
