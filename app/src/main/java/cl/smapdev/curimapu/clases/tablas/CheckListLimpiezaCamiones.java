package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "anexo_checklist_limpieza_camiones")
public class CheckListLimpiezaCamiones {

    @SerializedName("id_cl_limpieza_camiones")
    @PrimaryKey(autoGenerate = true)
    @Expose
    private int id_cl_limpieza_camiones;

    @SerializedName("id_ac_cl_limpieza_camiones")
    @Expose
    private int id_ac_cl_limpieza_camiones;

    @SerializedName("apellido_checklist")
    @Expose
    private String apellido_checklist;

    @SerializedName("estado_sincronizacion")
    @Expose
    private int estado_sincronizacion;

    @SerializedName("estado_documento")
    @Expose
    private int estado_documento;

    @SerializedName("clave_unica")
    @Expose
    private String clave_unica;

    @SerializedName("id_usuario")
    @Expose
    private int id_usuario;

    @SerializedName("fecha_hora_tx")
    @Expose
    private String fecha_hora_tx;

    @SerializedName("fecha_hora_mod")
    @Expose
    private String fecha_hora_mod;

    @SerializedName("id_usuario_mod")
    @Expose
    private int id_usuario_mod;


    public int getId_cl_limpieza_camiones() {
        return id_cl_limpieza_camiones;
    }

    public void setId_cl_limpieza_camiones(int id_cl_limpieza_camiones) {
        this.id_cl_limpieza_camiones = id_cl_limpieza_camiones;
    }

    public int getId_ac_cl_limpieza_camiones() {
        return id_ac_cl_limpieza_camiones;
    }

    public void setId_ac_cl_limpieza_camiones(int id_ac_cl_limpieza_camiones) {
        this.id_ac_cl_limpieza_camiones = id_ac_cl_limpieza_camiones;
    }

    public String getApellido_checklist() {
        return apellido_checklist;
    }

    public void setApellido_checklist(String apellido_checklist) {
        this.apellido_checklist = apellido_checklist;
    }

    public int getEstado_sincronizacion() {
        return estado_sincronizacion;
    }

    public void setEstado_sincronizacion(int estado_sincronizacion) {
        this.estado_sincronizacion = estado_sincronizacion;
    }

    public int getEstado_documento() {
        return estado_documento;
    }

    public void setEstado_documento(int estado_documento) {
        this.estado_documento = estado_documento;
    }

    public String getClave_unica() {
        return clave_unica;
    }

    public void setClave_unica(String clave_unica) {
        this.clave_unica = clave_unica;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getFecha_hora_tx() {
        return fecha_hora_tx;
    }

    public void setFecha_hora_tx(String fecha_hora_tx) {
        this.fecha_hora_tx = fecha_hora_tx;
    }

    public String getFecha_hora_mod() {
        return fecha_hora_mod;
    }

    public void setFecha_hora_mod(String fecha_hora_mod) {
        this.fecha_hora_mod = fecha_hora_mod;
    }

    public int getId_usuario_mod() {
        return id_usuario_mod;
    }

    public void setId_usuario_mod(int id_usuario_mod) {
        this.id_usuario_mod = id_usuario_mod;
    }
}
