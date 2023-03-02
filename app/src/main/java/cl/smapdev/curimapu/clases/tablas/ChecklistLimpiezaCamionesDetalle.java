package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "anexo_checklist_limpieza_camiones_detalle")
public class ChecklistLimpiezaCamionesDetalle {

    @SerializedName("id_ac_cl_limpieza_camiones_detalle")
    @PrimaryKey(autoGenerate = true)
    @Expose
    private int id_ac_cl_limpieza_camiones_detalle;


    @SerializedName("clave_unica_cl_limpieza_camiones_detalle")
    @Expose
    private String clave_unica_cl_limpieza_camiones_detalle;

    @SerializedName("clave_unica_cl_limpieza_camiones")
    @Expose
    private String clave_unica_cl_limpieza_camiones;

    @SerializedName("nombre_chofer_limpieza_camiones")
    @Expose
    private String nombre_chofer_limpieza_camiones;
    @SerializedName("patente_camion_limpieza_camiones")
    @Expose
    private String patente_camion_limpieza_camiones;
    @SerializedName("patente_carro_limpieza_camiones")
    @Expose
    private String patente_carro_limpieza_camiones;
    @SerializedName("estado_general_recepcion_camion_campo_limpieza_camiones")
    @Expose
    private String estado_general_recepcion_camion_campo_limpieza_camiones;
    @SerializedName("equipo_utilizado_limpieza_camiones")
    @Expose
    private String equipo_utilizado_limpieza_camiones;
    @SerializedName("limpieza_puertas_laterales_limpieza_camiones")
    @Expose
    private int limpieza_puertas_laterales_limpieza_camiones;
    @SerializedName("limpieza_puertas_traseras_limpieza_camiones")
    @Expose
    private int limpieza_puertas_traseras_limpieza_camiones;
    @SerializedName("limpieza_piso_limpieza_camiones")
    @Expose
    private int limpieza_piso_limpieza_camiones;
    @SerializedName("inspeccion_rejillas_mallas_limpieza_camiones")
    @Expose
    private int inspeccion_rejillas_mallas_limpieza_camiones;
    @SerializedName("pisos_costados_batea_sin_orificios_limpieza_camiones")
    @Expose
    private int pisos_costados_batea_sin_orificios_limpieza_camiones;
    @SerializedName("camion_carro_limpio_limpieza_camiones")
    @Expose
    private int camion_carro_limpio_limpieza_camiones;
    @SerializedName("carpa_limpia_limpieza_camiones")
    @Expose
    private int carpa_limpia_limpieza_camiones;
    @SerializedName("sistema_cerrado_puertas_limpieza_camiones")
    @Expose
    private int sistema_cerrado_puertas_limpieza_camiones;
    @SerializedName("nivel_llenado_carga_limpieza_camiones")
    @Expose
    private String nivel_llenado_carga_limpieza_camiones;

    @SerializedName("limpieza_anterior_limpieza_camiones")
    @Expose
    private String limpieza_anterior_limpieza_camiones;
    @SerializedName("sello_color_indica_condicion_limpieza_camiones")
    @Expose
    private int sello_color_indica_condicion_limpieza_camiones;
    @SerializedName("etiqueta_cosecha_adherida_camion_jumbo_limpieza_camiones")
    @Expose
    private int etiqueta_cosecha_adherida_camion_jumbo_limpieza_camiones;
    @SerializedName("sello_verde_curimapu_cierre_camion_limpieza_camiones")
    @Expose
     private String sello_verde_curimapu_cierre_camion_limpieza_camiones;


    @SerializedName("firma_cl_limpieza_camiones_detalle")
    @Expose
    private String firma_cl_limpieza_camiones_detalle;

    @SerializedName("stringed_cl_limpieza_camiones_detalle")
    @Expose
    private String stringed_cl_limpieza_camiones_detalle;

    @SerializedName("estado_sincronizacion_detalle")
    @Expose
    private int estado_sincronizacion_detalle;


    public String getLimpieza_anterior_limpieza_camiones() {
        return limpieza_anterior_limpieza_camiones;
    }

    public void setLimpieza_anterior_limpieza_camiones(String limpieza_anterior_limpieza_camiones) {
        this.limpieza_anterior_limpieza_camiones = limpieza_anterior_limpieza_camiones;
    }

    public int getId_ac_cl_limpieza_camiones_detalle() {
        return id_ac_cl_limpieza_camiones_detalle;
    }

    public void setId_ac_cl_limpieza_camiones_detalle(int id_ac_cl_limpieza_camiones_detalle) {
        this.id_ac_cl_limpieza_camiones_detalle = id_ac_cl_limpieza_camiones_detalle;
    }

    public String getClave_unica_cl_limpieza_camiones_detalle() {
        return clave_unica_cl_limpieza_camiones_detalle;
    }

    public void setClave_unica_cl_limpieza_camiones_detalle(String clave_unica_cl_limpieza_camiones_detalle) {
        this.clave_unica_cl_limpieza_camiones_detalle = clave_unica_cl_limpieza_camiones_detalle;
    }

    public String getClave_unica_cl_limpieza_camiones() {
        return clave_unica_cl_limpieza_camiones;
    }

    public void setClave_unica_cl_limpieza_camiones(String clave_unica_cl_limpieza_camiones) {
        this.clave_unica_cl_limpieza_camiones = clave_unica_cl_limpieza_camiones;
    }

    public String getNombre_chofer_limpieza_camiones() {
        return nombre_chofer_limpieza_camiones;
    }

    public void setNombre_chofer_limpieza_camiones(String nombre_chofer_limpieza_camiones) {
        this.nombre_chofer_limpieza_camiones = nombre_chofer_limpieza_camiones;
    }

    public String getPatente_camion_limpieza_camiones() {
        return patente_camion_limpieza_camiones;
    }

    public void setPatente_camion_limpieza_camiones(String patente_camion_limpieza_camiones) {
        this.patente_camion_limpieza_camiones = patente_camion_limpieza_camiones;
    }

    public String getPatente_carro_limpieza_camiones() {
        return patente_carro_limpieza_camiones;
    }

    public void setPatente_carro_limpieza_camiones(String patente_carro_limpieza_camiones) {
        this.patente_carro_limpieza_camiones = patente_carro_limpieza_camiones;
    }

    public String getEstado_general_recepcion_camion_campo_limpieza_camiones() {
        return estado_general_recepcion_camion_campo_limpieza_camiones;
    }

    public void setEstado_general_recepcion_camion_campo_limpieza_camiones(String estado_general_recepcion_camion_campo_limpieza_camiones) {
        this.estado_general_recepcion_camion_campo_limpieza_camiones = estado_general_recepcion_camion_campo_limpieza_camiones;
    }

    public String getEquipo_utilizado_limpieza_camiones() {
        return equipo_utilizado_limpieza_camiones;
    }

    public void setEquipo_utilizado_limpieza_camiones(String equipo_utilizado_limpieza_camiones) {
        this.equipo_utilizado_limpieza_camiones = equipo_utilizado_limpieza_camiones;
    }

    public int getLimpieza_puertas_laterales_limpieza_camiones() {
        return limpieza_puertas_laterales_limpieza_camiones;
    }

    public void setLimpieza_puertas_laterales_limpieza_camiones(int limpieza_puertas_laterales_limpieza_camiones) {
        this.limpieza_puertas_laterales_limpieza_camiones = limpieza_puertas_laterales_limpieza_camiones;
    }

    public int getLimpieza_puertas_traseras_limpieza_camiones() {
        return limpieza_puertas_traseras_limpieza_camiones;
    }

    public void setLimpieza_puertas_traseras_limpieza_camiones(int limpieza_puertas_traseras_limpieza_camiones) {
        this.limpieza_puertas_traseras_limpieza_camiones = limpieza_puertas_traseras_limpieza_camiones;
    }

    public int getLimpieza_piso_limpieza_camiones() {
        return limpieza_piso_limpieza_camiones;
    }

    public void setLimpieza_piso_limpieza_camiones(int limpieza_piso_limpieza_camiones) {
        this.limpieza_piso_limpieza_camiones = limpieza_piso_limpieza_camiones;
    }

    public int getInspeccion_rejillas_mallas_limpieza_camiones() {
        return inspeccion_rejillas_mallas_limpieza_camiones;
    }

    public void setInspeccion_rejillas_mallas_limpieza_camiones(int inspeccion_rejillas_mallas_limpieza_camiones) {
        this.inspeccion_rejillas_mallas_limpieza_camiones = inspeccion_rejillas_mallas_limpieza_camiones;
    }

    public int getPisos_costados_batea_sin_orificios_limpieza_camiones() {
        return pisos_costados_batea_sin_orificios_limpieza_camiones;
    }

    public void setPisos_costados_batea_sin_orificios_limpieza_camiones(int pisos_costados_batea_sin_orificios_limpieza_camiones) {
        this.pisos_costados_batea_sin_orificios_limpieza_camiones = pisos_costados_batea_sin_orificios_limpieza_camiones;
    }

    public int getCamion_carro_limpio_limpieza_camiones() {
        return camion_carro_limpio_limpieza_camiones;
    }

    public void setCamion_carro_limpio_limpieza_camiones(int camion_carro_limpio_limpieza_camiones) {
        this.camion_carro_limpio_limpieza_camiones = camion_carro_limpio_limpieza_camiones;
    }

    public int getCarpa_limpia_limpieza_camiones() {
        return carpa_limpia_limpieza_camiones;
    }

    public void setCarpa_limpia_limpieza_camiones(int carpa_limpia_limpieza_camiones) {
        this.carpa_limpia_limpieza_camiones = carpa_limpia_limpieza_camiones;
    }

    public int getSistema_cerrado_puertas_limpieza_camiones() {
        return sistema_cerrado_puertas_limpieza_camiones;
    }

    public void setSistema_cerrado_puertas_limpieza_camiones(int sistema_cerrado_puertas_limpieza_camiones) {
        this.sistema_cerrado_puertas_limpieza_camiones = sistema_cerrado_puertas_limpieza_camiones;
    }

    public String getNivel_llenado_carga_limpieza_camiones() {
        return nivel_llenado_carga_limpieza_camiones;
    }

    public void setNivel_llenado_carga_limpieza_camiones(String nivel_llenado_carga_limpieza_camiones) {
        this.nivel_llenado_carga_limpieza_camiones = nivel_llenado_carga_limpieza_camiones;
    }

    public int getSello_color_indica_condicion_limpieza_camiones() {
        return sello_color_indica_condicion_limpieza_camiones;
    }

    public void setSello_color_indica_condicion_limpieza_camiones(int sello_color_indica_condicion_limpieza_camiones) {
        this.sello_color_indica_condicion_limpieza_camiones = sello_color_indica_condicion_limpieza_camiones;
    }

    public int getEtiqueta_cosecha_adherida_camion_jumbo_limpieza_camiones() {
        return etiqueta_cosecha_adherida_camion_jumbo_limpieza_camiones;
    }

    public void setEtiqueta_cosecha_adherida_camion_jumbo_limpieza_camiones(int etiqueta_cosecha_adherida_camion_jumbo_limpieza_camiones) {
        this.etiqueta_cosecha_adherida_camion_jumbo_limpieza_camiones = etiqueta_cosecha_adherida_camion_jumbo_limpieza_camiones;
    }

    public String getSello_verde_curimapu_cierre_camion_limpieza_camiones() {
        return sello_verde_curimapu_cierre_camion_limpieza_camiones;
    }

    public void setSello_verde_curimapu_cierre_camion_limpieza_camiones(String sello_verde_curimapu_cierre_camion_limpieza_camiones) {
        this.sello_verde_curimapu_cierre_camion_limpieza_camiones = sello_verde_curimapu_cierre_camion_limpieza_camiones;
    }

    public String getFirma_cl_limpieza_camiones_detalle() {
        return firma_cl_limpieza_camiones_detalle;
    }

    public void setFirma_cl_limpieza_camiones_detalle(String firma_cl_limpieza_camiones_detalle) {
        this.firma_cl_limpieza_camiones_detalle = firma_cl_limpieza_camiones_detalle;
    }

    public String getStringed_cl_limpieza_camiones_detalle() {
        return stringed_cl_limpieza_camiones_detalle;
    }

    public void setStringed_cl_limpieza_camiones_detalle(String stringed_cl_limpieza_camiones_detalle) {
        this.stringed_cl_limpieza_camiones_detalle = stringed_cl_limpieza_camiones_detalle;
    }

    public int getEstado_sincronizacion_detalle() {
        return estado_sincronizacion_detalle;
    }

    public void setEstado_sincronizacion_detalle(int estado_sincronizacion_detalle) {
        this.estado_sincronizacion_detalle = estado_sincronizacion_detalle;
    }
}
