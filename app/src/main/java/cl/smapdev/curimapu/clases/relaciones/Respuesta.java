package cl.smapdev.curimapu.clases.relaciones;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Respuesta {



    @SerializedName("codigo_respuesta")
    @Expose
    private int codigoRespuesta;

    @SerializedName("mensaje_respuesta")
    @Expose
    private String mensajeRespuesta;


    @SerializedName("cabecera_respuesta")
    @Expose
    private int cabeceraRespuesta;


    public int getCabeceraRespuesta() {
        return cabeceraRespuesta;
    }

    public void setCabeceraRespuesta(int cabeceraRespuesta) {
        this.cabeceraRespuesta = cabeceraRespuesta;
    }

    public int getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public void setCodigoRespuesta(int codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }

    public String getMensajeRespuesta() {
        return mensajeRespuesta;
    }

    public void setMensajeRespuesta(String mensajeRespuesta) {
        this.mensajeRespuesta = mensajeRespuesta;
    }
}
