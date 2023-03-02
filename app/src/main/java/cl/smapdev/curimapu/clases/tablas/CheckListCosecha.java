package cl.smapdev.curimapu.clases.tablas;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

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
    private String prestador_servicio;


    @Expose
    private String sembradora_marca;
    @Expose
    private  String sembradora_modelo;


    @Expose
    private String nombre_operario_maquina;




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
}
