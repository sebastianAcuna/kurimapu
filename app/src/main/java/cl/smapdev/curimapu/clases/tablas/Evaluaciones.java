package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity( tableName = "anexo_recomendaciones")
public class Evaluaciones {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id_ac_recom")
    @Expose
    private int id_ac_recom;
    @SerializedName("id_ac")
    @Expose
    private int id_ac;
    @SerializedName("descripcion_recom")
    @Expose
    private String descripcion_recom;
    @SerializedName("estado")
    @Expose
    private String estado;
    @SerializedName("observacion_recom")
    @Expose
    private String observacion_recom;
    @SerializedName("fecha_plazo")
    @Expose
    private String fecha_plazo;
    @SerializedName("user_tx")
    @Expose
    private String user_tx;
    @SerializedName("nombre_crea")
    @Expose
    private String nombre_crea;
    @SerializedName("fecha_hora_tx")
    @Expose
    private String fecha_hora_tx;
    @SerializedName("user_mod")
    @Expose
    private String user_mod;
    @SerializedName("nombre_mod")
    @Expose
    private String nombre_mod;
    @SerializedName("fecha_hora_mod")
    @Expose
    private String fecha_hora_mod;

    @SerializedName("estado_server")
    @Expose
    private int estado_server;

    @SerializedName("cabecera_server")
    @Expose
    private int cabecera_server;

    @SerializedName("marca_evaluacion_server")
    @Expose
    private int marca_evaluacion_server;


    public int getMarca_evaluacion_server() {
        return marca_evaluacion_server;
    }

    public void setMarca_evaluacion_server(int marca_evaluacion_server) {
        this.marca_evaluacion_server = marca_evaluacion_server;
    }

    public int getEstado_server() {
        return estado_server;
    }

    public void setEstado_server(int estado_server) {
        this.estado_server = estado_server;
    }

    public int getCabecera_server() {
        return cabecera_server;
    }

    public void setCabecera_server(int cabecera_server) {
        this.cabecera_server = cabecera_server;
    }

    public String getNombre_crea() {
        return nombre_crea;
    }

    public void setNombre_crea(String nombre_crea) {
        this.nombre_crea = nombre_crea;
    }

    public String getNombre_mod() {
        return nombre_mod;
    }

    public void setNombre_mod(String nombre_mod) {
        this.nombre_mod = nombre_mod;
    }

    public int getId_ac_recom() {
        return id_ac_recom;
    }

    public void setId_ac_recom(int id_ac_recom) {
        this.id_ac_recom = id_ac_recom;
    }

    public int getId_ac() {
        return id_ac;
    }

    public void setId_ac(int id_ac) {
        this.id_ac = id_ac;
    }

    public String getDescripcion_recom() {
        return descripcion_recom;
    }

    public void setDescripcion_recom(String descripcion_recom) {
        this.descripcion_recom = descripcion_recom;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacion_recom() {
        return observacion_recom;
    }

    public void setObservacion_recom(String observacion_recom) {
        this.observacion_recom = observacion_recom;
    }

    public String getFecha_plazo() {
        return fecha_plazo;
    }

    public void setFecha_plazo(String fecha_plazo) {
        this.fecha_plazo = fecha_plazo;
    }

    public String getUser_tx() {
        return user_tx;
    }

    public void setUser_tx(String user_tx) {
        this.user_tx = user_tx;
    }

    public String getFecha_hora_tx() {
        return fecha_hora_tx;
    }

    public void setFecha_hora_tx(String fecha_hora_tx) {
        this.fecha_hora_tx = fecha_hora_tx;
    }

    public String getUser_mod() {
        return user_mod;
    }

    public void setUser_mod(String user_mod) {
        this.user_mod = user_mod;
    }

    public String getFecha_hora_mod() {
        return fecha_hora_mod;
    }

    public void setFecha_hora_mod(String fecha_hora_mod) {
        this.fecha_hora_mod = fecha_hora_mod;
    }
}
