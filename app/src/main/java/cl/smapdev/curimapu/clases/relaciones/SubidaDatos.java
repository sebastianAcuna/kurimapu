package cl.smapdev.curimapu.clases.relaciones;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.CropRotation;
import cl.smapdev.curimapu.clases.tablas.Errores;
import cl.smapdev.curimapu.clases.tablas.Fichas;
import cl.smapdev.curimapu.clases.tablas.Fotos;
import cl.smapdev.curimapu.clases.tablas.FotosFichas;
import cl.smapdev.curimapu.clases.tablas.Visitas;
import cl.smapdev.curimapu.clases.tablas.detalle_visita_prop;

public class SubidaDatos {

    @Expose
    @SerializedName("id_dispo")
    private int id_dispo;

    @Expose
    @SerializedName("id_usuario")
    private int id_usuario;

    @Expose
    @SerializedName("visitas")
    private List<Visitas> visitasList;

    @Expose
    @SerializedName("detalles")
    private List<detalle_visita_prop> detalle_visita_props;

    @Expose
    @SerializedName("fotos")
    private List<Fotos> fotosList;

    @Expose
    @SerializedName("fotos_fichas")
    private List<FotosFichas> fotosFichas;

    @Expose
    @SerializedName("errores")
    private List<Errores> errores;

    @Expose
    @SerializedName("fichas")
    private List<Fichas> fichas;

    @Expose
    @SerializedName("crop_rotation")
    private List<CropRotation> cropRotation;

    @Expose
    @SerializedName("suma_datos")
    private int cantidadSuma;

    @Expose
    @SerializedName("version")
    private String version;

    public List<CropRotation> getCropRotation() {
        return cropRotation;
    }

    public void setCropRotation(List<CropRotation> cropRotation) {
        this.cropRotation = cropRotation;
    }

    public List<FotosFichas> getFotosFichas() {
        return fotosFichas;
    }

    public void setFotosFichas(List<FotosFichas> fotosFichas) {
        this.fotosFichas = fotosFichas;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getCantidadSuma() {
        return cantidadSuma;
    }

    public void setCantidadSuma(int cantidadSuma) {
        this.cantidadSuma = cantidadSuma;
    }

    public List<Fichas> getFichas() {
        return fichas;
    }

    public void setFichas(List<Fichas> fichas) {
        this.fichas = fichas;
    }

    public List<Errores> getErrores() {
        return errores;
    }

    public void setErrores(List<Errores> errores) {
        this.errores = errores;
    }

    public int getId_dispo() {
        return id_dispo;
    }

    public void setId_dispo(int id_dispo) {
        this.id_dispo = id_dispo;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public List<Fotos> getFotosList() {
        return fotosList;
    }

    public void setFotosList(List<Fotos> fotosList) {
        this.fotosList = fotosList;
    }

    public List<Visitas> getVisitasList() {
        return visitasList;
    }

    public void setVisitasList(List<Visitas> visitasList) {
        this.visitasList = visitasList;
    }

    public List<detalle_visita_prop> getDetalle_visita_props() {
        return detalle_visita_props;
    }

    public void setDetalle_visita_props(List<detalle_visita_prop> detalle_visita_props) {
        this.detalle_visita_props = detalle_visita_props;
    }
}
