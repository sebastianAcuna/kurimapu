package cl.smapdev.curimapu.clases.tablas;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "anexo_checklist_devolucion_semilla")
public class ChecklistDevolucionSemilla {

    @PrimaryKey(autoGenerate = true)
    @Expose
    private int id_cl_devolucion_semilla;
    @Expose
    private int id_ac_cl_devolucion_semilla;
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
    private String fecha;
    @Expose
    private String agronomo_responsable;

    @Expose
    private String agricultor;

    @Expose
    private String numero_guia;
    @Expose
    private String propuesta;

    @Expose
    private String especie;

//    detalle
//    hembra
    @Expose
    private String linea_hembra;
    @Expose
    private String lote_hembra;
    @Expose
    private String numero_envase_hembra;
    @Expose
    private String kg_aproximado_hembra;

//    macho
    @Expose
    private String linea_macho;
    @Expose
    private String lote_macho;
    @Expose
    private String numero_envase_macho;
    @Expose
    private String kg_aproximado_macho;

//autopolinizacion
    @Expose
    private String linea_autopolinizacion;
    @Expose
    private  String lote_autopolinizacion;

    @Expose
    private String numero_envase_autopolinizacion;

    @Expose
    private String kg_aproximado_autopolinizacion;


    //firmas

    @Expose
    private String nombre_responsable;
    @Expose
    private String firma_responsable;

    @Expose
    private String nombre_revisor_bodega;
    @Expose
    private String firma_revisor_bodega;


    @SerializedName("stringed_responsable")
    private String stringed_responsable;
    @SerializedName("stringed_revisor_bodega")
    private String stringed_revisor_bodega;



    public int getId_cl_devolucion_semilla() {
        return id_cl_devolucion_semilla;
    }

    public void setId_cl_devolucion_semilla(int id_cl_devolucion_semilla) {
        this.id_cl_devolucion_semilla = id_cl_devolucion_semilla;
    }

    public int getId_ac_cl_devolucion_semilla() {
        return id_ac_cl_devolucion_semilla;
    }

    public void setId_ac_cl_devolucion_semilla(int id_ac_cl_devolucion_semilla) {
        this.id_ac_cl_devolucion_semilla = id_ac_cl_devolucion_semilla;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getAgronomo_responsable() {
        return agronomo_responsable;
    }

    public void setAgronomo_responsable(String agronomo_responsable) {
        this.agronomo_responsable = agronomo_responsable;
    }

    public String getAgricultor() {
        return agricultor;
    }

    public void setAgricultor(String agricultor) {
        this.agricultor = agricultor;
    }

    public String getNumero_guia() {
        return numero_guia;
    }

    public void setNumero_guia(String numero_guia) {
        this.numero_guia = numero_guia;
    }

    public String getPropuesta() {
        return propuesta;
    }

    public void setPropuesta(String propuesta) {
        this.propuesta = propuesta;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getLinea_hembra() {
        return linea_hembra;
    }

    public void setLinea_hembra(String linea_hembra) {
        this.linea_hembra = linea_hembra;
    }

    public String getLote_hembra() {
        return lote_hembra;
    }

    public void setLote_hembra(String lote_hembra) {
        this.lote_hembra = lote_hembra;
    }

    public String getNumero_envase_hembra() {
        return numero_envase_hembra;
    }

    public void setNumero_envase_hembra(String numero_envase_hembra) {
        this.numero_envase_hembra = numero_envase_hembra;
    }

    public String getKg_aproximado_hembra() {
        return kg_aproximado_hembra;
    }

    public void setKg_aproximado_hembra(String kg_aproximado_hembra) {
        this.kg_aproximado_hembra = kg_aproximado_hembra;
    }

    public String getLinea_macho() {
        return linea_macho;
    }

    public void setLinea_macho(String linea_macho) {
        this.linea_macho = linea_macho;
    }

    public String getLote_macho() {
        return lote_macho;
    }

    public void setLote_macho(String lote_macho) {
        this.lote_macho = lote_macho;
    }

    public String getNumero_envase_macho() {
        return numero_envase_macho;
    }

    public void setNumero_envase_macho(String numero_envase_macho) {
        this.numero_envase_macho = numero_envase_macho;
    }

    public String getKg_aproximado_macho() {
        return kg_aproximado_macho;
    }

    public void setKg_aproximado_macho(String kg_aproximado_macho) {
        this.kg_aproximado_macho = kg_aproximado_macho;
    }

    public String getLinea_autopolinizacion() {
        return linea_autopolinizacion;
    }

    public void setLinea_autopolinizacion(String linea_autopolinizacion) {
        this.linea_autopolinizacion = linea_autopolinizacion;
    }

    public String getLote_autopolinizacion() {
        return lote_autopolinizacion;
    }

    public void setLote_autopolinizacion(String lote_autopolinizacion) {
        this.lote_autopolinizacion = lote_autopolinizacion;
    }

    public String getNumero_envase_autopolinizacion() {
        return numero_envase_autopolinizacion;
    }

    public void setNumero_envase_autopolinizacion(String numero_envase_autopolinizacion) {
        this.numero_envase_autopolinizacion = numero_envase_autopolinizacion;
    }

    public String getKg_aproximado_autopolinizacion() {
        return kg_aproximado_autopolinizacion;
    }

    public void setKg_aproximado_autopolinizacion(String kg_aproximado_autopolinizacion) {
        this.kg_aproximado_autopolinizacion = kg_aproximado_autopolinizacion;
    }

    public String getNombre_responsable() {
        return nombre_responsable;
    }

    public void setNombre_responsable(String nombre_responsable) {
        this.nombre_responsable = nombre_responsable;
    }

    public String getFirma_responsable() {
        return firma_responsable;
    }

    public void setFirma_responsable(String firma_responsable) {
        this.firma_responsable = firma_responsable;
    }

    public String getNombre_revisor_bodega() {
        return nombre_revisor_bodega;
    }

    public void setNombre_revisor_bodega(String nombre_revisor_bodega) {
        this.nombre_revisor_bodega = nombre_revisor_bodega;
    }

    public String getFirma_revisor_bodega() {
        return firma_revisor_bodega;
    }

    public void setFirma_revisor_bodega(String firma_revisor_bodega) {
        this.firma_revisor_bodega = firma_revisor_bodega;
    }

    public String getStringed_responsable() {
        return stringed_responsable;
    }

    public void setStringed_responsable(String stringed_responsable) {
        this.stringed_responsable = stringed_responsable;
    }

    public String getStringed_revisor_bodega() {
        return stringed_revisor_bodega;
    }

    public void setStringed_revisor_bodega(String stringed_revisor_bodega) {
        this.stringed_revisor_bodega = stringed_revisor_bodega;
    }
}
