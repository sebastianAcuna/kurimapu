package cl.smapdev.curimapu.clases.relaciones;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class resFecha {


    @SerializedName("codigo_respuesta")
    @Expose
    private int codigoRespuesta;

    @SerializedName("lista_respuestas")
    @Expose
    private List<RespuestaFecha> respuestaFechas;

    @SerializedName("mensaje")
    @Expose
    private String mensaje;

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public void setCodigoRespuesta(int codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }

    public List<RespuestaFecha> getRespuestaFechas() {
        return respuestaFechas;
    }

    public void setRespuestaFechas(List<RespuestaFecha> respuestaFechas) {
        this.respuestaFechas = respuestaFechas;
    }
}
