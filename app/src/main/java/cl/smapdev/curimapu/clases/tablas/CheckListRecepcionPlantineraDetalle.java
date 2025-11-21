package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "anexo_checklist_recepcion_plantineras_detalle")
public class CheckListRecepcionPlantineraDetalle {

    @PrimaryKey(autoGenerate = true)
    private int id_cl_rp_detalle;

    private String clave_unica_rp_cabecera;
    private String clave_unica_rp_detalle;
    private String variedad;
    private int n_orden;
    private String fecha_recepcion;
    private String fecha_actual;
    private String linea;
    private int cantidad_bandeja;
    private double condicion_plantas;
    private String bandejas_etiquetadas;
    private String lugar_acopio_adecuado;
    private String separacion_entre_lineas;
    private String observaciones;
    private int id_usuario_tx;
    private String fecha_hora_tx;
    private int id_usuario_mod;
    private String fecha_hora_mod;
    private int estado_sincronizacion;

    public int getId_cl_rp_detalle() {
        return id_cl_rp_detalle;
    }

    public void setId_cl_rp_detalle(int id_cl_rp_detalle) {
        this.id_cl_rp_detalle = id_cl_rp_detalle;
    }

    public String getClave_unica_rp_cabecera() {
        return clave_unica_rp_cabecera;
    }

    public void setClave_unica_rp_cabecera(String clave_unica_rp_cabecera) {
        this.clave_unica_rp_cabecera = clave_unica_rp_cabecera;
    }

    public String getClave_unica_rp_detalle() {
        return clave_unica_rp_detalle;
    }

    public void setClave_unica_rp_detalle(String clave_unica_rp_detalle) {
        this.clave_unica_rp_detalle = clave_unica_rp_detalle;
    }

    public String getVariedad() {
        return variedad;
    }

    public void setVariedad(String variedad) {
        this.variedad = variedad;
    }

    public int getN_orden() {
        return n_orden;
    }

    public void setN_orden(int n_orden) {
        this.n_orden = n_orden;
    }

    public String getFecha_recepcion() {
        return fecha_recepcion;
    }

    public void setFecha_recepcion(String fecha_recepcion) {
        this.fecha_recepcion = fecha_recepcion;
    }

    public String getFecha_actual() {
        return fecha_actual;
    }

    public void setFecha_actual(String fecha_actual) {
        this.fecha_actual = fecha_actual;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public int getCantidad_bandeja() {
        return cantidad_bandeja;
    }

    public void setCantidad_bandeja(int cantidad_bandeja) {
        this.cantidad_bandeja = cantidad_bandeja;
    }

    public double getCondicion_plantas() {
        return condicion_plantas;
    }

    public void setCondicion_plantas(double condicion_plantas) {
        this.condicion_plantas = condicion_plantas;
    }

    public String getBandejas_etiquetadas() {
        return bandejas_etiquetadas;
    }

    public void setBandejas_etiquetadas(String bandejas_etiquetadas) {
        this.bandejas_etiquetadas = bandejas_etiquetadas;
    }

    public String getLugar_acopio_adecuado() {
        return lugar_acopio_adecuado;
    }

    public void setLugar_acopio_adecuado(String lugar_acopio_adecuado) {
        this.lugar_acopio_adecuado = lugar_acopio_adecuado;
    }

    public String getSeparacion_entre_lineas() {
        return separacion_entre_lineas;
    }

    public void setSeparacion_entre_lineas(String separacion_entre_lineas) {
        this.separacion_entre_lineas = separacion_entre_lineas;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getId_usuario_tx() {
        return id_usuario_tx;
    }

    public void setId_usuario_tx(int id_usuario_tx) {
        this.id_usuario_tx = id_usuario_tx;
    }

    public String getFecha_hora_tx() {
        return fecha_hora_tx;
    }

    public void setFecha_hora_tx(String fecha_hora_tx) {
        this.fecha_hora_tx = fecha_hora_tx;
    }

    public int getId_usuario_mod() {
        return id_usuario_mod;
    }

    public void setId_usuario_mod(int id_usuario_mod) {
        this.id_usuario_mod = id_usuario_mod;
    }

    public String getFecha_hora_mod() {
        return fecha_hora_mod;
    }

    public void setFecha_hora_mod(String fecha_hora_mod) {
        this.fecha_hora_mod = fecha_hora_mod;
    }

    public int getEstado_sincronizacion() {
        return estado_sincronizacion;
    }

    public void setEstado_sincronizacion(int estado_sincronizacion) {
        this.estado_sincronizacion = estado_sincronizacion;
    }
}
