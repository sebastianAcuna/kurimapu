package cl.smapdev.curimapu.clases.relaciones;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.Evaluaciones;

public class RecomendacionesRequest {

    @SerializedName("idDispo")
    private int idDispo;

    @SerializedName("idUsuario")
    private int idUsuario;

    @SerializedName("codigo")
    private int codigo;

    @SerializedName("mensaje")
    private String mensaje;

    @SerializedName("recomendaciones")
    private List<Evaluaciones> evaluacionesList;


    public int getIdDispo() {
        return idDispo;
    }

    public void setIdDispo(int idDispo) {
        this.idDispo = idDispo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<Evaluaciones> getEvaluacionesList() {
        return evaluacionesList;
    }

    public void setEvaluacionesList(List<Evaluaciones> evaluacionesList) {
        this.evaluacionesList = evaluacionesList;
    }
}
