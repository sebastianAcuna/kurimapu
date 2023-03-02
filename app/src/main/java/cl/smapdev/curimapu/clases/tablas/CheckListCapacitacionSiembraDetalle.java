package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "anexo_checklist_capacitacion_siembra_detalle")
public class CheckListCapacitacionSiembraDetalle {

    @SerializedName("id_ac_cl_cap_siembra")
    @PrimaryKey(autoGenerate = true)
    @Expose
    private int id_cl_cap_siembra_detalle;

    @SerializedName("clave_unica_cl_cap_siembra_detalle")
    @Expose
    private String clave_unica_cl_cap_siembra_detalle;

    @SerializedName("clave_unica_cl_cap_siembra")
    @Expose
    private String clave_unica_cl_cap_siembra;

    @SerializedName("fecha_cl_cap_siembra_detalle")
    @Expose
    private String fecha_cl_cap_siembra_detalle;

    @SerializedName("area_cl_cap_siembra_detalle")
    @Expose
    private String area_cl_cap_siembra_detalle;

    @SerializedName("nombre_cl_cap_siembra_detalle")
    @Expose
    private String nombre_cl_cap_siembra_detalle;

    @SerializedName("rut_cl_cap_siembra_detalle")
    @Expose
    private String rut_cl_cap_siembra_detalle;

    @SerializedName("firma_cl_cap_siembra_detalle")
    @Expose
    private String firma_cl_cap_siembra_detalle;

    @SerializedName("stringed_cl_cap_siembra_detalle")
    @Expose
    private String stringed_cl_cap_siembra_detalle;

    @SerializedName("estado_sincronizacion_detalle")
    @Expose
    private int estado_sincronizacion_detalle;

    public int getEstado_sincronizacion_detalle() {
        return estado_sincronizacion_detalle;
    }

    public void setEstado_sincronizacion_detalle(int estado_sincronizacion_detalle) {
        this.estado_sincronizacion_detalle = estado_sincronizacion_detalle;
    }

    public int getId_cl_cap_siembra_detalle() {
        return id_cl_cap_siembra_detalle;
    }

    public void setId_cl_cap_siembra_detalle(int id_cl_cap_siembra_detalle) {
        this.id_cl_cap_siembra_detalle = id_cl_cap_siembra_detalle;
    }

    public String getClave_unica_cl_cap_siembra_detalle() {
        return clave_unica_cl_cap_siembra_detalle;
    }

    public void setClave_unica_cl_cap_siembra_detalle(String clave_unica_cl_cap_siembra_detalle) {
        this.clave_unica_cl_cap_siembra_detalle = clave_unica_cl_cap_siembra_detalle;
    }

    public String getClave_unica_cl_cap_siembra() {
        return clave_unica_cl_cap_siembra;
    }

    public void setClave_unica_cl_cap_siembra(String clave_unica_cl_cap_siembra) {
        this.clave_unica_cl_cap_siembra = clave_unica_cl_cap_siembra;
    }

    public String getFecha_cl_cap_siembra_detalle() {
        return fecha_cl_cap_siembra_detalle;
    }

    public void setFecha_cl_cap_siembra_detalle(String fecha_cl_cap_siembra_detalle) {
        this.fecha_cl_cap_siembra_detalle = fecha_cl_cap_siembra_detalle;
    }

    public String getArea_cl_cap_siembra_detalle() {
        return area_cl_cap_siembra_detalle;
    }

    public void setArea_cl_cap_siembra_detalle(String area_cl_cap_siembra_detalle) {
        this.area_cl_cap_siembra_detalle = area_cl_cap_siembra_detalle;
    }

    public String getNombre_cl_cap_siembra_detalle() {
        return nombre_cl_cap_siembra_detalle;
    }

    public void setNombre_cl_cap_siembra_detalle(String nombre_cl_cap_siembra_detalle) {
        this.nombre_cl_cap_siembra_detalle = nombre_cl_cap_siembra_detalle;
    }

    public String getRut_cl_cap_siembra_detalle() {
        return rut_cl_cap_siembra_detalle;
    }

    public void setRut_cl_cap_siembra_detalle(String rut_cl_cap_siembra_detalle) {
        this.rut_cl_cap_siembra_detalle = rut_cl_cap_siembra_detalle;
    }

    public String getFirma_cl_cap_siembra_detalle() {
        return firma_cl_cap_siembra_detalle;
    }

    public void setFirma_cl_cap_siembra_detalle(String firma_cl_cap_siembra_detalle) {
        this.firma_cl_cap_siembra_detalle = firma_cl_cap_siembra_detalle;
    }

    public String getStringed_cl_cap_siembra_detalle() {
        return stringed_cl_cap_siembra_detalle;
    }

    public void setStringed_cl_cap_siembra_detalle(String stringed_cl_cap_siembra_detalle) {
        this.stringed_cl_cap_siembra_detalle = stringed_cl_cap_siembra_detalle;
    }
}
