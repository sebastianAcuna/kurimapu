package cl.smapdev.curimapu.clases.relaciones;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RespuestaFecha {

    @SerializedName("codigo_respuesta")
    @Expose
    private int codigo_respuesta;

    @SerializedName("anexo")
    @Expose
    private String anexo;

    @SerializedName("correo_inicio_despano")
    @Expose
    private int correo_inicio_despano;

    @SerializedName("correo_cinco_porciento")
    @Expose
    private int correo_cinco_porciento;

    @SerializedName("correo_inicio_corte_seda")
    @Expose
    private int correo_inicio_corte_seda;

    @SerializedName("correo_inicio_cosecha")
    @Expose
    private int correo_inicio_cosecha;

    @SerializedName("correo_termino_cosecha")
    @Expose
    private int correo_termino_cosecha;

    @SerializedName("correo_termino_labores")
    @Expose
    private int correo_termino_labores;

    @SerializedName("detalle")
    @Expose
    private String detalle;

    @SerializedName("mensaje")
    @Expose
    private String mensaje;


    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getAnexo() {
        return anexo;
    }

    public void setAnexo(String anexo) {
        this.anexo = anexo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getCodigo_respuesta() {
        return codigo_respuesta;
    }

    public void setCodigo_respuesta(int codigo_respuesta) {
        this.codigo_respuesta = codigo_respuesta;
    }

    public int getCorreo_inicio_despano() {
        return correo_inicio_despano;
    }

    public void setCorreo_inicio_despano(int correo_inicio_despano) {
        this.correo_inicio_despano = correo_inicio_despano;
    }

    public int getCorreo_cinco_porciento() {
        return correo_cinco_porciento;
    }

    public void setCorreo_cinco_porciento(int correo_cinco_porciento) {
        this.correo_cinco_porciento = correo_cinco_porciento;
    }

    public int getCorreo_inicio_corte_seda() {
        return correo_inicio_corte_seda;
    }

    public void setCorreo_inicio_corte_seda(int correo_inicio_corte_seda) {
        this.correo_inicio_corte_seda = correo_inicio_corte_seda;
    }

    public int getCorreo_inicio_cosecha() {
        return correo_inicio_cosecha;
    }

    public void setCorreo_inicio_cosecha(int correo_inicio_cosecha) {
        this.correo_inicio_cosecha = correo_inicio_cosecha;
    }

    public int getCorreo_termino_cosecha() {
        return correo_termino_cosecha;
    }

    public void setCorreo_termino_cosecha(int correo_termino_cosecha) {
        this.correo_termino_cosecha = correo_termino_cosecha;
    }

    public int getCorreo_termino_labores() {
        return correo_termino_labores;
    }

    public void setCorreo_termino_labores(int correo_termino_labores) {
        this.correo_termino_labores = correo_termino_labores;
    }
}
