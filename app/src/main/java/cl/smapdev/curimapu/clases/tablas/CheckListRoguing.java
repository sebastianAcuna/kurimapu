package cl.smapdev.curimapu.clases.tablas;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

@Entity(tableName = "checklist_roguing")
public class CheckListRoguing {


    @PrimaryKey(autoGenerate = true)
    @Expose
    private int id_cl_roguing;

    @Expose
    private int id_ac_cl_roguing;

    @Expose
    private String apellido_checklist;

    @Expose
    private int estado_sincronizacion;

    @Expose
    private int estado_documento;

    @Expose
    private String clave_unica;

    @Expose
    private int id_usuario;

    @Expose
    private String fecha_hora_tx;

    @Expose
    private String fecha_hora_mod;

    @Expose
    private int id_usuario_mod;

    @Expose
    private String n_total_plantas_hembra;
    private String n_total_plantas_macho;


    public String getN_total_plantas_hembra() {
        return n_total_plantas_hembra;
    }

    public void setN_total_plantas_hembra(String n_total_plantas_hembra) {
        this.n_total_plantas_hembra = n_total_plantas_hembra;
    }

    public String getN_total_plantas_macho() {
        return n_total_plantas_macho;
    }

    public void setN_total_plantas_macho(String n_total_plantas_macho) {
        this.n_total_plantas_macho = n_total_plantas_macho;
    }

    public int getId_cl_roguing() {
        return id_cl_roguing;
    }

    public void setId_cl_roguing(int id_cl_roguing) {
        this.id_cl_roguing = id_cl_roguing;
    }

    public int getId_ac_cl_roguing() {
        return id_ac_cl_roguing;
    }

    public void setId_ac_cl_roguing(int id_ac_cl_roguing) {
        this.id_ac_cl_roguing = id_ac_cl_roguing;
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
