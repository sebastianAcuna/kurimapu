package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "anexo_correo_fechas")
public class AnexoCorreoFechas {

    @SerializedName("id_ac_cor_fech")
    @Expose
    @PrimaryKey(autoGenerate = true)
    private int id_ac_cor_fech;

    @SerializedName("id_ac_corr_fech")
    @Expose
    private int id_ac_corr_fech;

    @SerializedName("id_fieldman")
    @Expose
    private int id_fieldman;

    @SerializedName("inicio_despano")
    @Expose
    private String inicio_despano;

    @SerializedName("correo_inicio_despano")
    @Expose
    private int correo_inicio_despano;

    @SerializedName("cinco_porciento_floracion")
    @Expose
    private String cinco_porciento_floracion;

    @SerializedName("correo_cinco_porciento_floracion")
    @Expose
    private int correo_cinco_porciento_floracion;


    @SerializedName("inicio_corte_seda")
    @Expose
    private String inicio_corte_seda;


    @SerializedName("correo_inicio_corte_seda")
    @Expose
    private int correo_inicio_corte_seda;

    @SerializedName("inicio_cosecha")
    @Expose
    private String inicio_cosecha;

    @SerializedName("correo_inicio_cosecha")
    @Expose
    private int correo_inicio_cosecha;

    @SerializedName("termino_cosecha")
    @Expose
    private String termino_cosecha;

    @SerializedName("correo_termino_cosecha")
    @Expose
    private int correo_termino_cosecha;

    @SerializedName("termino_labores_post_cosechas")
    @Expose
    private String termino_labores_post_cosechas;

    @SerializedName("correo_termino_labores_post_cosechas")
    @Expose
    private int correo_termino_labores_post_cosechas;

    @SerializedName("detalle_labores")
    @Expose
    private String detalle_labores;

    @SerializedName("id_asistente")
    @Expose
    private int id_asistente;


    public int getId_ac_cor_fech() {
        return id_ac_cor_fech;
    }

    public void setId_ac_cor_fech(int id_ac_cor_fech) {
        this.id_ac_cor_fech = id_ac_cor_fech;
    }

    public int getId_ac_corr_fech() {
        return id_ac_corr_fech;
    }

    public void setId_ac_corr_fech(int id_ac_corr_fech) {
        this.id_ac_corr_fech = id_ac_corr_fech;
    }

    public int getId_fieldman() {
        return id_fieldman;
    }

    public void setId_fieldman(int id_fieldman) {
        this.id_fieldman = id_fieldman;
    }

    public String getInicio_despano() {
        return inicio_despano;
    }

    public void setInicio_despano(String inicio_despano) {
        this.inicio_despano = inicio_despano;
    }

    public int getCorreo_inicio_despano() {
        return correo_inicio_despano;
    }

    public void setCorreo_inicio_despano(int correo_inicio_despano) {
        this.correo_inicio_despano = correo_inicio_despano;
    }

    public String getCinco_porciento_floracion() {
        return cinco_porciento_floracion;
    }

    public void setCinco_porciento_floracion(String cinco_porciento_floracion) {
        this.cinco_porciento_floracion = cinco_porciento_floracion;
    }

    public int getCorreo_cinco_porciento_floracion() {
        return correo_cinco_porciento_floracion;
    }

    public void setCorreo_cinco_porciento_floracion(int correo_cinco_porciento_floracion) {
        this.correo_cinco_porciento_floracion = correo_cinco_porciento_floracion;
    }

    public String getInicio_corte_seda() {
        return inicio_corte_seda;
    }

    public void setInicio_corte_seda(String inicio_corte_seda) {
        this.inicio_corte_seda = inicio_corte_seda;
    }

    public int getCorreo_inicio_corte_seda() {
        return correo_inicio_corte_seda;
    }

    public void setCorreo_inicio_corte_seda(int correo_inicio_corte_seda) {
        this.correo_inicio_corte_seda = correo_inicio_corte_seda;
    }

    public String getInicio_cosecha() {
        return inicio_cosecha;
    }

    public void setInicio_cosecha(String inicio_cosecha) {
        this.inicio_cosecha = inicio_cosecha;
    }

    public int getCorreo_inicio_cosecha() {
        return correo_inicio_cosecha;
    }

    public void setCorreo_inicio_cosecha(int correo_inicio_cosecha) {
        this.correo_inicio_cosecha = correo_inicio_cosecha;
    }

    public String getTermino_cosecha() {
        return termino_cosecha;
    }

    public void setTermino_cosecha(String termino_cosecha) {
        this.termino_cosecha = termino_cosecha;
    }

    public int getCorreo_termino_cosecha() {
        return correo_termino_cosecha;
    }

    public void setCorreo_termino_cosecha(int correo_termino_cosecha) {
        this.correo_termino_cosecha = correo_termino_cosecha;
    }

    public String getTermino_labores_post_cosechas() {
        return termino_labores_post_cosechas;
    }

    public void setTermino_labores_post_cosechas(String termino_labores_post_cosechas) {
        this.termino_labores_post_cosechas = termino_labores_post_cosechas;
    }

    public int getCorreo_termino_labores_post_cosechas() {
        return correo_termino_labores_post_cosechas;
    }

    public void setCorreo_termino_labores_post_cosechas(int correo_termino_labores_post_cosechas) {
        this.correo_termino_labores_post_cosechas = correo_termino_labores_post_cosechas;
    }

    public String getDetalle_labores() {
        return detalle_labores;
    }

    public void setDetalle_labores(String detalle_labores) {
        this.detalle_labores = detalle_labores;
    }

    public int getId_asistente() {
        return id_asistente;
    }

    public void setId_asistente(int id_asistente) {
        this.id_asistente = id_asistente;
    }
}
