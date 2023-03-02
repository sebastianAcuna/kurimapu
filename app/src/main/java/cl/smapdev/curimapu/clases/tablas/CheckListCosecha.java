package cl.smapdev.curimapu.clases.tablas;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "anexo_checklist_cosecha")
public class CheckListCosecha {

    @PrimaryKey(autoGenerate = true)
    @Expose
    private int id_cl_siembra;
    @Expose
    private int id_ac_cl_siembra;
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
    private String responsable_cosecha;
    @Expose
    private String fecha_inicio_cosecha;

    @Expose
    private String hora_inicio_cosecha;
    @Expose
    private int capacitacion;

    //condicion del potrero
    @Expose
    private String acceso;
    @Expose
    private String control_maleza;
    @Expose
    private int corte_macho;
    @Expose
    private int eliminacion_regueros;

    @Expose
    private String humedad_suelo;

    @Expose
    private String estado_visual;
    @Expose
    private String humedad_cosecha;

    @Expose
    private String comentarios;


    //cosechadora
    @Expose
    private String prestador_servicio;

    @Expose
    private String sembradora_marca;
    @Expose
    private  String sembradora_modelo;

    @Expose
    private String nombre_operario_maquina;

    @Expose
    private String cosecha_anterior;


    //checklist aseo maquinaria -ingreso de campo

    @Expose
    private String fecha_revision_limpieza_ingreso;

    @Expose
    private int cabezal_ingreso;
    @Expose
    private int elevadores_ingreso;

    @Expose
    private int concavos_ingreso;

    @Expose
    private int harneros_ingreso;

    @Expose
    private int tolva_ingreso;

    @Expose
    private int descarga_ingreso;

    @Expose
    private String lugar_limpieza_ingreso;


    @Expose
    private String responsable_aseo_ingreso;

    @Expose
    private String rut_responsable_aseo_ingreso;

    @Expose
    private String firma_responsable_aseo_ingreso;

    @Expose
    private String responsable_curimapu_ingreso;
    @Expose
    private String firma_responsable_curimapu_ingreso;


    //checklist aseo maquinaria -salida de campo

    @Expose
    private String fecha_revision_limpieza_salida;

    @Expose
    private int cabezal_salida;
    @Expose
    private int elevadores_salida;

    @Expose
    private int concavos_salida;

    @Expose
    private int harneros_salida;

    @Expose
    private int tolva_salida;

    @Expose
    private int descarga_salida;

    @Expose
    private String lugar_limpieza_salida;


    @Expose
    private String responsable_aseo_salida;

    @Expose
    private String rut_responsable_aseo_salida;

    @Expose
    private String firma_responsable_aseo_salida;

    @Expose
    private String responsable_curimapu_salida;
    @Expose
    private String firma_responsable_curimapu_salida;



    @SerializedName("stringed_responsable_aso_ingreso")
    private String stringed_responsable_aso_ingreso;
    @SerializedName("stringed_revision_limpieza_ingreso")
    private String stringed_responsable_curimapu_ingreso;
    @SerializedName("stringed_responsable_aseo_salida")
    private String stringed_responsable_aso_salida;
    @SerializedName("stringed_revision_limpieza_salida")
    private String stringed_responsable_curimapu_salida;


    public String getStringed_responsable_aso_ingreso() {
        return stringed_responsable_aso_ingreso;
    }

    public void setStringed_responsable_aso_ingreso(String stringed_responsable_aso_ingreso) {
        this.stringed_responsable_aso_ingreso = stringed_responsable_aso_ingreso;
    }

    public String getStringed_responsable_curimapu_ingreso() {
        return stringed_responsable_curimapu_ingreso;
    }

    public void setStringed_responsable_curimapu_ingreso(String stringed_responsable_curimapu_ingreso) {
        this.stringed_responsable_curimapu_ingreso = stringed_responsable_curimapu_ingreso;
    }

    public String getStringed_responsable_aso_salida() {
        return stringed_responsable_aso_salida;
    }

    public void setStringed_responsable_aso_salida(String stringed_responsable_aso_salida) {
        this.stringed_responsable_aso_salida = stringed_responsable_aso_salida;
    }

    public String getStringed_responsable_curimapu_salida() {
        return stringed_responsable_curimapu_salida;
    }

    public void setStringed_responsable_curimapu_salida(String stringed_responsable_curimapu_salida) {
        this.stringed_responsable_curimapu_salida = stringed_responsable_curimapu_salida;
    }

    public int getId_cl_siembra() {
        return id_cl_siembra;
    }

    public void setId_cl_siembra(int id_cl_siembra) {
        this.id_cl_siembra = id_cl_siembra;
    }

    public int getId_ac_cl_siembra() {
        return id_ac_cl_siembra;
    }

    public void setId_ac_cl_siembra(int id_ac_cl_siembra) {
        this.id_ac_cl_siembra = id_ac_cl_siembra;
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

    public String getPrestador_servicio() {
        return prestador_servicio;
    }

    public void setPrestador_servicio(String prestador_servicio) {
        this.prestador_servicio = prestador_servicio;
    }

    public String getSembradora_marca() {
        return sembradora_marca;
    }

    public void setSembradora_marca(String sembradora_marca) {
        this.sembradora_marca = sembradora_marca;
    }

    public String getSembradora_modelo() {
        return sembradora_modelo;
    }

    public void setSembradora_modelo(String sembradora_modelo) {
        this.sembradora_modelo = sembradora_modelo;
    }

    public String getNombre_operario_maquina() {
        return nombre_operario_maquina;
    }

    public void setNombre_operario_maquina(String nombre_operario_maquina) {
        this.nombre_operario_maquina = nombre_operario_maquina;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getResponsable_cosecha() {
        return responsable_cosecha;
    }

    public void setResponsable_cosecha(String responsable_cosecha) {
        this.responsable_cosecha = responsable_cosecha;
    }

    public String getFecha_inicio_cosecha() {
        return fecha_inicio_cosecha;
    }

    public void setFecha_inicio_cosecha(String fecha_inicio_cosecha) {
        this.fecha_inicio_cosecha = fecha_inicio_cosecha;
    }

    public String getHora_inicio_cosecha() {
        return hora_inicio_cosecha;
    }

    public void setHora_inicio_cosecha(String hora_inicio_cosecha) {
        this.hora_inicio_cosecha = hora_inicio_cosecha;
    }

    public int getCapacitacion() {
        return capacitacion;
    }

    public void setCapacitacion(int capacitacion) {
        this.capacitacion = capacitacion;
    }

    public String getAcceso() {
        return acceso;
    }

    public void setAcceso(String acceso) {
        this.acceso = acceso;
    }

    public String getControl_maleza() {
        return control_maleza;
    }

    public void setControl_maleza(String control_maleza) {
        this.control_maleza = control_maleza;
    }

    public int getCorte_macho() {
        return corte_macho;
    }

    public void setCorte_macho(int corte_macho) {
        this.corte_macho = corte_macho;
    }

    public int getEliminacion_regueros() {
        return eliminacion_regueros;
    }

    public void setEliminacion_regueros(int eliminacion_regueros) {
        this.eliminacion_regueros = eliminacion_regueros;
    }

    public String getHumedad_suelo() {
        return humedad_suelo;
    }

    public void setHumedad_suelo(String humedad_suelo) {
        this.humedad_suelo = humedad_suelo;
    }

    public String getEstado_visual() {
        return estado_visual;
    }

    public void setEstado_visual(String estado_visual) {
        this.estado_visual = estado_visual;
    }

    public String getHumedad_cosecha() {
        return humedad_cosecha;
    }

    public void setHumedad_cosecha(String humedad_cosecha) {
        this.humedad_cosecha = humedad_cosecha;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getCosecha_anterior() {
        return cosecha_anterior;
    }

    public void setCosecha_anterior(String cosecha_anterior) {
        this.cosecha_anterior = cosecha_anterior;
    }

    public String getFecha_revision_limpieza_ingreso() {
        return fecha_revision_limpieza_ingreso;
    }

    public void setFecha_revision_limpieza_ingreso(String fecha_revision_limpieza_ingreso) {
        this.fecha_revision_limpieza_ingreso = fecha_revision_limpieza_ingreso;
    }

    public int getCabezal_ingreso() {
        return cabezal_ingreso;
    }

    public void setCabezal_ingreso(int cabezal_ingreso) {
        this.cabezal_ingreso = cabezal_ingreso;
    }

    public int getElevadores_ingreso() {
        return elevadores_ingreso;
    }

    public void setElevadores_ingreso(int elevadores_ingreso) {
        this.elevadores_ingreso = elevadores_ingreso;
    }

    public int getConcavos_ingreso() {
        return concavos_ingreso;
    }

    public void setConcavos_ingreso(int concavos_ingreso) {
        this.concavos_ingreso = concavos_ingreso;
    }

    public int getHarneros_ingreso() {
        return harneros_ingreso;
    }

    public void setHarneros_ingreso(int harneros_ingreso) {
        this.harneros_ingreso = harneros_ingreso;
    }

    public int getTolva_ingreso() {
        return tolva_ingreso;
    }

    public void setTolva_ingreso(int tolva_ingreso) {
        this.tolva_ingreso = tolva_ingreso;
    }

    public int getDescarga_ingreso() {
        return descarga_ingreso;
    }

    public void setDescarga_ingreso(int descarga_ingreso) {
        this.descarga_ingreso = descarga_ingreso;
    }

    public String getLugar_limpieza_ingreso() {
        return lugar_limpieza_ingreso;
    }

    public void setLugar_limpieza_ingreso(String lugar_limpieza_ingreso) {
        this.lugar_limpieza_ingreso = lugar_limpieza_ingreso;
    }

    public String getResponsable_aseo_ingreso() {
        return responsable_aseo_ingreso;
    }

    public void setResponsable_aseo_ingreso(String responsable_aseo_ingreso) {
        this.responsable_aseo_ingreso = responsable_aseo_ingreso;
    }

    public String getRut_responsable_aseo_ingreso() {
        return rut_responsable_aseo_ingreso;
    }

    public void setRut_responsable_aseo_ingreso(String rut_responsable_aseo_ingreso) {
        this.rut_responsable_aseo_ingreso = rut_responsable_aseo_ingreso;
    }

    public String getFirma_responsable_aseo_ingreso() {
        return firma_responsable_aseo_ingreso;
    }

    public void setFirma_responsable_aseo_ingreso(String firma_responsable_aseo_ingreso) {
        this.firma_responsable_aseo_ingreso = firma_responsable_aseo_ingreso;
    }

    public String getResponsable_curimapu_ingreso() {
        return responsable_curimapu_ingreso;
    }

    public void setResponsable_curimapu_ingreso(String responsable_curimapu_ingreso) {
        this.responsable_curimapu_ingreso = responsable_curimapu_ingreso;
    }

    public String getFirma_responsable_curimapu_ingreso() {
        return firma_responsable_curimapu_ingreso;
    }

    public void setFirma_responsable_curimapu_ingreso(String firma_responsable_curimapu_ingreso) {
        this.firma_responsable_curimapu_ingreso = firma_responsable_curimapu_ingreso;
    }

    public String getFecha_revision_limpieza_salida() {
        return fecha_revision_limpieza_salida;
    }

    public void setFecha_revision_limpieza_salida(String fecha_revision_limpieza_salida) {
        this.fecha_revision_limpieza_salida = fecha_revision_limpieza_salida;
    }

    public int getCabezal_salida() {
        return cabezal_salida;
    }

    public void setCabezal_salida(int cabezal_salida) {
        this.cabezal_salida = cabezal_salida;
    }

    public int getElevadores_salida() {
        return elevadores_salida;
    }

    public void setElevadores_salida(int elevadores_salida) {
        this.elevadores_salida = elevadores_salida;
    }

    public int getConcavos_salida() {
        return concavos_salida;
    }

    public void setConcavos_salida(int concavos_salida) {
        this.concavos_salida = concavos_salida;
    }

    public int getHarneros_salida() {
        return harneros_salida;
    }

    public void setHarneros_salida(int harneros_salida) {
        this.harneros_salida = harneros_salida;
    }

    public int getTolva_salida() {
        return tolva_salida;
    }

    public void setTolva_salida(int tolva_salida) {
        this.tolva_salida = tolva_salida;
    }

    public int getDescarga_salida() {
        return descarga_salida;
    }

    public void setDescarga_salida(int descarga_salida) {
        this.descarga_salida = descarga_salida;
    }

    public String getLugar_limpieza_salida() {
        return lugar_limpieza_salida;
    }

    public void setLugar_limpieza_salida(String lugar_limpieza_salida) {
        this.lugar_limpieza_salida = lugar_limpieza_salida;
    }

    public String getResponsable_aseo_salida() {
        return responsable_aseo_salida;
    }

    public void setResponsable_aseo_salida(String responsable_aseo_salida) {
        this.responsable_aseo_salida = responsable_aseo_salida;
    }

    public String getRut_responsable_aseo_salida() {
        return rut_responsable_aseo_salida;
    }

    public void setRut_responsable_aseo_salida(String rut_responsable_aseo_salida) {
        this.rut_responsable_aseo_salida = rut_responsable_aseo_salida;
    }

    public String getFirma_responsable_aseo_salida() {
        return firma_responsable_aseo_salida;
    }

    public void setFirma_responsable_aseo_salida(String firma_responsable_aseo_salida) {
        this.firma_responsable_aseo_salida = firma_responsable_aseo_salida;
    }

    public String getResponsable_curimapu_salida() {
        return responsable_curimapu_salida;
    }

    public void setResponsable_curimapu_salida(String responsable_curimapu_salida) {
        this.responsable_curimapu_salida = responsable_curimapu_salida;
    }

    public String getFirma_responsable_curimapu_salida() {
        return firma_responsable_curimapu_salida;
    }

    public void setFirma_responsable_curimapu_salida(String firma_responsable_curimapu_salida) {
        this.firma_responsable_curimapu_salida = firma_responsable_curimapu_salida;
    }
}
