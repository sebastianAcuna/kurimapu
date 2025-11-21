package cl.smapdev.curimapu.clases.tablas;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "anexo_checklist_recepcion_plantineras")
public class CheckListRecepcionPlantinera {

    @PrimaryKey(autoGenerate = true)
    private int id_cl_recepcion_plantinera;

    private int id_ac_recepcion_plantinera;

    private String apellido_checklist;

    private int estado_sincronizacion;

    private int estado_documento;

    private String clave_unica;

    private int id_usuario;

    private String fecha_hora_tx;

    private int id_usuario_mod;

    private String fecha_hora_mod;

    private String ruta_firma_agricultor;

    private String ruta_firma_responsable;


    public String getFecha_hora_mod() {
        return fecha_hora_mod;
    }

    public void setFecha_hora_mod(String fecha_hora_mod) {
        this.fecha_hora_mod = fecha_hora_mod;
    }

    public int getId_cl_recepcion_plantinera() {
        return id_cl_recepcion_plantinera;
    }

    public void setId_cl_recepcion_plantinera(int id_cl_recepcion_plantinera) {
        this.id_cl_recepcion_plantinera = id_cl_recepcion_plantinera;
    }

    public int getId_ac_recepcion_plantinera() {
        return id_ac_recepcion_plantinera;
    }

    public void setId_ac_recepcion_plantinera(int id_ac_recepcion_plantinera) {
        this.id_ac_recepcion_plantinera = id_ac_recepcion_plantinera;
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

    public int getId_usuario_mod() {
        return id_usuario_mod;
    }

    public void setId_usuario_mod(int id_usuario_mod) {
        this.id_usuario_mod = id_usuario_mod;
    }

    public String getRuta_firma_agricultor() {
        return ruta_firma_agricultor;
    }

    public void setRuta_firma_agricultor(String ruta_firma_agricultor) {
        this.ruta_firma_agricultor = ruta_firma_agricultor;
    }

    public String getRuta_firma_responsable() {
        return ruta_firma_responsable;
    }

    public void setRuta_firma_responsable(String ruta_firma_responsable) {
        this.ruta_firma_responsable = ruta_firma_responsable;
    }
}
