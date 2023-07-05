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

    @SerializedName("estado_sincro_corr_fech")
    @Expose
    private int estado_sincro_corr_fech;

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



    @SerializedName("correo_destruccion_semillero")
    @Expose
    private int correo_destruccion_semillero;

    @SerializedName("correo_siembra_temprana")
    @Expose
    private int correo_siembra_temprana;

    @SerializedName("inicio_cosecha")
    @Expose
    private String inicio_cosecha;

    @SerializedName("hora_inicio_cosecha")
    @Expose
    private String hora_inicio_cosecha;


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



    @SerializedName("siem_tempra_grami")
    @Expose
    private String siem_tempra_grami;

    @SerializedName("tipo_graminea")
    @Expose
    private String tipo_graminea;

    @SerializedName("fecha_destruccion_semillero")
    @Expose
    private String fecha_destruccion_semillero;

    @SerializedName("hora_destruccion_semillero")
    @Expose
    private String hora_destruccion_semillero;

    @SerializedName("destruc_semill_ensayo")
    @Expose
    private String destruc_semill_ensayo;

    @SerializedName("cantidad_has_destruidas")
    @Expose
    private Double cantidad_has_destruidas;

    @SerializedName("motivo_destruccion")
    @Expose
    private String motivo_destruccion;

    @SerializedName("inicio_siembra")
    @Expose
    private String inicio_siembra;


    @SerializedName("correo_inicio_siembra")
    @Expose
    private int correo_inicio_siembra;


    public String getInicio_siembra() {
        return inicio_siembra;
    }

    public void setInicio_siembra(String inicio_siembra) {
        this.inicio_siembra = inicio_siembra;
    }

    public int getCorreo_inicio_siembra() {
        return correo_inicio_siembra;
    }

    public void setCorreo_inicio_siembra(int correo_inicio_siembra) {
        this.correo_inicio_siembra = correo_inicio_siembra;
    }

    public int getCorreo_destruccion_semillero() {
        return correo_destruccion_semillero;
    }

    public void setCorreo_destruccion_semillero(int correo_destruccion_semillero) {
        this.correo_destruccion_semillero = correo_destruccion_semillero;
    }

    public int getCorreo_siembra_temprana() {
        return correo_siembra_temprana;
    }

    public void setCorreo_siembra_temprana(int correo_siembra_temprana) {
        this.correo_siembra_temprana = correo_siembra_temprana;
    }

    public int getEstado_sincro_corr_fech() {
        return estado_sincro_corr_fech;
    }

    public void setEstado_sincro_corr_fech(int estado_sincro_corr_fech) {
        this.estado_sincro_corr_fech = estado_sincro_corr_fech;
    }

    public String getSiem_tempra_grami() {
        return siem_tempra_grami;
    }

    public void setSiem_tempra_grami(String siem_tempra_grami) {
        this.siem_tempra_grami = siem_tempra_grami;
    }

    public String getTipo_graminea() {
        return tipo_graminea;
    }

    public void setTipo_graminea(String tipo_graminea) {
        this.tipo_graminea = tipo_graminea;
    }

    public String getFecha_destruccion_semillero() {
        return fecha_destruccion_semillero;
    }

    public void setFecha_destruccion_semillero(String fecha_destruccion_semillero) {
        this.fecha_destruccion_semillero = fecha_destruccion_semillero;
    }

    public String getHora_destruccion_semillero() {
        return hora_destruccion_semillero;
    }

    public void setHora_destruccion_semillero(String hora_destruccion_semillero) {
        this.hora_destruccion_semillero = hora_destruccion_semillero;
    }

    public String getDestruc_semill_ensayo() {
        return destruc_semill_ensayo;
    }

    public void setDestruc_semill_ensayo(String destruc_semill_ensayo) {
        this.destruc_semill_ensayo = destruc_semill_ensayo;
    }

    public Double getCantidad_has_destruidas() {
        return cantidad_has_destruidas;
    }

    public void setCantidad_has_destruidas(Double cantidad_has_destruidas) {
        this.cantidad_has_destruidas = cantidad_has_destruidas;
    }

    public String getMotivo_destruccion() {
        return motivo_destruccion;
    }

    public void setMotivo_destruccion(String motivo_destruccion) {
        this.motivo_destruccion = motivo_destruccion;
    }

    public String getHora_inicio_cosecha() {
        return hora_inicio_cosecha;
    }

    public void setHora_inicio_cosecha(String hora_inicio_cosecha) {
        this.hora_inicio_cosecha = hora_inicio_cosecha;
    }

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
