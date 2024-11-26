package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

@Entity(tableName = "anexo_checklist_guia_interna")
public class CheckListGuiaInterna {

    @PrimaryKey(autoGenerate = true)
    @Expose
    private int id_cl_guia_interna;

    @Expose
    private int id_ac_cl_guia_interna;

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
    private int correlativo;

    @Expose
    private String fecha;

    @Expose
    private String of;

    @Expose
    private String n_guia_despacho;

    @Expose
    private String parcialidad_lote;

    @Expose
    private String fin_lote;

    @Expose
    private String fin_cosecha;

    @Expose
    private String lote_campo;

    @Expose
    private String cantidad_sacos;

    @Expose
    private String color_sacos;

    @Expose
    private String cantidad_jumbo;

    @Expose
    private String color_jumbo;

    @Expose
    private String cantidad_bins;
    @Expose
    private String color_bins;

    @Expose
    private String kilos_estimados;


    @Expose
    private String fecha_cosecha;

    @Expose
    private String n_maquina;

    @Expose
    private String fecha_trilla;

    @Expose
    private String vb_supervisor_limpieza;

    @Expose
    private String humedad;

    @Expose
    private String temperatura;

    @Expose
    private String malezas;

    @Expose
    private String restos_vegetales;


    @Expose
    private String nombre_transportista;

    @Expose
    private String contacto_transportista;

    @Expose
    private String patente_transportista;

    @Expose
    private String firma_transportista;

    @Expose
    private String stringed_firma_transportista;

    @Expose
    private String nombre_supervisor;
    @Expose
    private String contacto_supervisor;

    @Expose
    private String firma_supervisor;
    @Expose
    private String stringed_firma_supervisor;

    @Expose
    private String observaciones;


    public int getCorrelativo() {
        return correlativo;
    }

    public void setCorrelativo(int correlativo) {
        this.correlativo = correlativo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getOf() {
        return of;
    }

    public void setOf(String of) {
        this.of = of;
    }

    public String getN_guia_despacho() {
        return n_guia_despacho;
    }

    public void setN_guia_despacho(String n_guia_despacho) {
        this.n_guia_despacho = n_guia_despacho;
    }

    public String getParcialidad_lote() {
        return parcialidad_lote;
    }

    public void setParcialidad_lote(String parcialidad_lote) {
        this.parcialidad_lote = parcialidad_lote;
    }

    public String getFin_lote() {
        return fin_lote;
    }

    public void setFin_lote(String fin_lote) {
        this.fin_lote = fin_lote;
    }

    public String getFin_cosecha() {
        return fin_cosecha;
    }

    public void setFin_cosecha(String fin_cosecha) {
        this.fin_cosecha = fin_cosecha;
    }

    public String getLote_campo() {
        return lote_campo;
    }

    public void setLote_campo(String lote_campo) {
        this.lote_campo = lote_campo;
    }

    public String getCantidad_sacos() {
        return cantidad_sacos;
    }

    public void setCantidad_sacos(String cantidad_sacos) {
        this.cantidad_sacos = cantidad_sacos;
    }

    public String getColor_sacos() {
        return color_sacos;
    }

    public void setColor_sacos(String color_sacos) {
        this.color_sacos = color_sacos;
    }

    public String getCantidad_jumbo() {
        return cantidad_jumbo;
    }

    public void setCantidad_jumbo(String cantidad_jumbo) {
        this.cantidad_jumbo = cantidad_jumbo;
    }

    public String getColor_jumbo() {
        return color_jumbo;
    }

    public void setColor_jumbo(String color_jumbo) {
        this.color_jumbo = color_jumbo;
    }

    public String getCantidad_bins() {
        return cantidad_bins;
    }

    public void setCantidad_bins(String cantidad_bins) {
        this.cantidad_bins = cantidad_bins;
    }

    public String getColor_bins() {
        return color_bins;
    }

    public void setColor_bins(String color_bins) {
        this.color_bins = color_bins;
    }

    public String getKilos_estimados() {
        return kilos_estimados;
    }

    public void setKilos_estimados(String kilos_estimados) {
        this.kilos_estimados = kilos_estimados;
    }

    public String getFecha_cosecha() {
        return fecha_cosecha;
    }

    public void setFecha_cosecha(String fecha_cosecha) {
        this.fecha_cosecha = fecha_cosecha;
    }

    public String getN_maquina() {
        return n_maquina;
    }

    public void setN_maquina(String n_maquina) {
        this.n_maquina = n_maquina;
    }

    public String getFecha_trilla() {
        return fecha_trilla;
    }

    public void setFecha_trilla(String fecha_trilla) {
        this.fecha_trilla = fecha_trilla;
    }

    public String getVb_supervisor_limpieza() {
        return vb_supervisor_limpieza;
    }

    public void setVb_supervisor_limpieza(String vb_supervisor_limpieza) {
        this.vb_supervisor_limpieza = vb_supervisor_limpieza;
    }

    public String getHumedad() {
        return humedad;
    }

    public void setHumedad(String humedad) {
        this.humedad = humedad;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public String getMalezas() {
        return malezas;
    }

    public void setMalezas(String malezas) {
        this.malezas = malezas;
    }

    public String getRestos_vegetales() {
        return restos_vegetales;
    }

    public void setRestos_vegetales(String restos_vegetales) {
        this.restos_vegetales = restos_vegetales;
    }

    public String getNombre_transportista() {
        return nombre_transportista;
    }

    public void setNombre_transportista(String nombre_transportista) {
        this.nombre_transportista = nombre_transportista;
    }

    public String getContacto_transportista() {
        return contacto_transportista;
    }

    public void setContacto_transportista(String contacto_transportista) {
        this.contacto_transportista = contacto_transportista;
    }

    public String getPatente_transportista() {
        return patente_transportista;
    }

    public void setPatente_transportista(String patente_transportista) {
        this.patente_transportista = patente_transportista;
    }

    public String getFirma_transportista() {
        return firma_transportista;
    }

    public void setFirma_transportista(String firma_transportista) {
        this.firma_transportista = firma_transportista;
    }

    public String getStringed_firma_transportista() {
        return stringed_firma_transportista;
    }

    public void setStringed_firma_transportista(String stringed_firma_transportista) {
        this.stringed_firma_transportista = stringed_firma_transportista;
    }

    public String getNombre_supervisor() {
        return nombre_supervisor;
    }

    public void setNombre_supervisor(String nombre_supervisor) {
        this.nombre_supervisor = nombre_supervisor;
    }

    public String getContacto_supervisor() {
        return contacto_supervisor;
    }

    public void setContacto_supervisor(String contacto_supervisor) {
        this.contacto_supervisor = contacto_supervisor;
    }

    public String getFirma_supervisor() {
        return firma_supervisor;
    }

    public void setFirma_supervisor(String firma_supervisor) {
        this.firma_supervisor = firma_supervisor;
    }

    public String getStringed_firma_supervisor() {
        return stringed_firma_supervisor;
    }

    public void setStringed_firma_supervisor(String stringed_firma_supervisor) {
        this.stringed_firma_supervisor = stringed_firma_supervisor;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getId_cl_guia_interna() {
        return id_cl_guia_interna;
    }

    public void setId_cl_guia_interna(int id_cl_guia_interna) {
        this.id_cl_guia_interna = id_cl_guia_interna;
    }

    public int getId_ac_cl_guia_interna() {
        return id_ac_cl_guia_interna;
    }

    public void setId_ac_cl_guia_interna(int id_ac_cl_guia_interna) {
        this.id_ac_cl_guia_interna = id_ac_cl_guia_interna;
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
