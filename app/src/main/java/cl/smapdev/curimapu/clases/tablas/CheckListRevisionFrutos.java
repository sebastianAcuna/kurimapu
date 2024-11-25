package cl.smapdev.curimapu.clases.tablas;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

@Entity(tableName = "checklist_revision_frutos")
public class CheckListRevisionFrutos {

    @PrimaryKey(autoGenerate = true)
    @Expose
    private int id_cl_revision_frutos;

    @Expose
    private int id_ac_cl_revision_frutos;

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

    private String estado_fenologico;
    private String fecha;
    private String termino_cosecha;
    private String autoriza_cosecha;
    private double total_frutos_aptos;
    private double total_frutos_no_aptos;

    private String firma_agricultor;
    private String stringed_firma_agricultor;

    public int getId_cl_revision_frutos() {
        return id_cl_revision_frutos;
    }

    public void setId_cl_revision_frutos(int id_cl_revision_frutos) {
        this.id_cl_revision_frutos = id_cl_revision_frutos;
    }

    public int getId_ac_cl_revision_frutos() {
        return id_ac_cl_revision_frutos;
    }

    public void setId_ac_cl_revision_frutos(int id_ac_cl_revision_frutos) {
        this.id_ac_cl_revision_frutos = id_ac_cl_revision_frutos;
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

    public String getEstado_fenologico() {
        return estado_fenologico;
    }

    public void setEstado_fenologico(String estado_fenologico) {
        this.estado_fenologico = estado_fenologico;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTermino_cosecha() {
        return termino_cosecha;
    }

    public void setTermino_cosecha(String termino_cosecha) {
        this.termino_cosecha = termino_cosecha;
    }

    public String getAutoriza_cosecha() {
        return autoriza_cosecha;
    }

    public void setAutoriza_cosecha(String autoriza_cosecha) {
        this.autoriza_cosecha = autoriza_cosecha;
    }

    public double getTotal_frutos_aptos() {
        return total_frutos_aptos;
    }

    public void setTotal_frutos_aptos(double total_frutos_aptos) {
        this.total_frutos_aptos = total_frutos_aptos;
    }

    public double getTotal_frutos_no_aptos() {
        return total_frutos_no_aptos;
    }

    public void setTotal_frutos_no_aptos(double total_frutos_no_aptos) {
        this.total_frutos_no_aptos = total_frutos_no_aptos;
    }

    public String getFirma_agricultor() {
        return firma_agricultor;
    }

    public void setFirma_agricultor(String firma_agricultor) {
        this.firma_agricultor = firma_agricultor;
    }

    public String getStringed_firma_agricultor() {
        return stringed_firma_agricultor;
    }

    public void setStringed_firma_agricultor(String stringed_firma_agricultor) {
        this.stringed_firma_agricultor = stringed_firma_agricultor;
    }
}
