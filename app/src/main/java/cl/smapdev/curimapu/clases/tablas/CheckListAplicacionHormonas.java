package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

@Entity(tableName = "checklist_aplicacion_hormonas")
public class CheckListAplicacionHormonas {

    @PrimaryKey(autoGenerate = true)
    @Expose
    private int id_cl_ap_hormonas;

    @Expose
    private int id_ac_cl_ap_hormonas;

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
    private String prestador_servicio;
    @Expose
    private String aplicacion;
    @Expose
    private String n_hojas_verdaderas;

    @Expose
    private String n_de_aplicacion;

    @Expose
    private String fecha_inicio;

    @Expose
    private String hora_inicio;

    @Expose
    private String hora_termino;

    @Expose
    private String ppm;

    @Expose
    private String mojamiento_lt_ha;

    @Expose
    private String observacion_texto;

    @Expose
    private String firma_prestador_servicio_ap_hormonas;
    @Expose
    private String firma_responsable_ap_hormonas;


    @Expose
    private String stringer_firma_prestador_servicio_ap_hormonas;
    @Expose
    private String stringed_firma_responsable_ap_hormonas;


    public int getId_cl_ap_hormonas() {
        return id_cl_ap_hormonas;
    }

    public void setId_cl_ap_hormonas(int id_cl_ap_hormonas) {
        this.id_cl_ap_hormonas = id_cl_ap_hormonas;
    }

    public int getId_ac_cl_ap_hormonas() {
        return id_ac_cl_ap_hormonas;
    }

    public void setId_ac_cl_ap_hormonas(int id_ac_cl_ap_hormonas) {
        this.id_ac_cl_ap_hormonas = id_ac_cl_ap_hormonas;
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

    public String getPrestador_servicio() {
        return prestador_servicio;
    }

    public void setPrestador_servicio(String prestador_servicio) {
        this.prestador_servicio = prestador_servicio;
    }

    public String getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(String aplicacion) {
        this.aplicacion = aplicacion;
    }

    public String getN_hojas_verdaderas() {
        return n_hojas_verdaderas;
    }

    public void setN_hojas_verdaderas(String n_hojas_verdaderas) {
        this.n_hojas_verdaderas = n_hojas_verdaderas;
    }

    public String getN_de_aplicacion() {
        return n_de_aplicacion;
    }

    public void setN_de_aplicacion(String n_de_aplicacion) {
        this.n_de_aplicacion = n_de_aplicacion;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public String getHora_termino() {
        return hora_termino;
    }

    public void setHora_termino(String hora_termino) {
        this.hora_termino = hora_termino;
    }

    public String getPpm() {
        return ppm;
    }

    public void setPpm(String ppm) {
        this.ppm = ppm;
    }

    public String getMojamiento_lt_ha() {
        return mojamiento_lt_ha;
    }

    public void setMojamiento_lt_ha(String mojamiento_lt_ha) {
        this.mojamiento_lt_ha = mojamiento_lt_ha;
    }

    public String getObservacion_texto() {
        return observacion_texto;
    }

    public void setObservacion_texto(String observacion_texto) {
        this.observacion_texto = observacion_texto;
    }

    public String getFirma_prestador_servicio_ap_hormonas() {
        return firma_prestador_servicio_ap_hormonas;
    }

    public void setFirma_prestador_servicio_ap_hormonas(String firma_prestador_servicio_ap_hormonas) {
        this.firma_prestador_servicio_ap_hormonas = firma_prestador_servicio_ap_hormonas;
    }

    public String getFirma_responsable_ap_hormonas() {
        return firma_responsable_ap_hormonas;
    }

    public void setFirma_responsable_ap_hormonas(String firma_responsable_ap_hormonas) {
        this.firma_responsable_ap_hormonas = firma_responsable_ap_hormonas;
    }


    public String getStringer_firma_prestador_servicio_ap_hormonas() {
        return stringer_firma_prestador_servicio_ap_hormonas;
    }

    public void setStringer_firma_prestador_servicio_ap_hormonas(String stringer_firma_prestador_servicio_ap_hormonas) {
        this.stringer_firma_prestador_servicio_ap_hormonas = stringer_firma_prestador_servicio_ap_hormonas;
    }

    public String getStringed_firma_responsable_ap_hormonas() {
        return stringed_firma_responsable_ap_hormonas;
    }

    public void setStringed_firma_responsable_ap_hormonas(String stringed_firma_responsable_ap_hormonas) {
        this.stringed_firma_responsable_ap_hormonas = stringed_firma_responsable_ap_hormonas;
    }
}
