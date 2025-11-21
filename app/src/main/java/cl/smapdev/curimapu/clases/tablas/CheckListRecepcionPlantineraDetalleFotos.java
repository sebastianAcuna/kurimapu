package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "anexo_checklist_recepcion_plantineras_detalle_fotos")
public class CheckListRecepcionPlantineraDetalleFotos {


    @PrimaryKey(autoGenerate = true)
    private int id_cl_rp_detalle_foto;

    private String clave_unica_rp_cabecera;
    private String clave_unica_rp_detalle;
    private String clave_unica_foto;
    private String nombre_foto;
    private String ruta_foto;
    private String fecha_tx;
    private int user_tx;
    private int estado_sincronizacion;

    public int getEstado_sincronizacion() {
        return estado_sincronizacion;
    }

    public void setEstado_sincronizacion(int estado_sincronizacion) {
        this.estado_sincronizacion = estado_sincronizacion;
    }

    public int getId_cl_rp_detalle_foto() {
        return id_cl_rp_detalle_foto;
    }

    public void setId_cl_rp_detalle_foto(int id_cl_rp_detalle_foto) {
        this.id_cl_rp_detalle_foto = id_cl_rp_detalle_foto;
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

    public String getClave_unica_foto() {
        return clave_unica_foto;
    }

    public void setClave_unica_foto(String clave_unica_foto) {
        this.clave_unica_foto = clave_unica_foto;
    }

    public String getNombre_foto() {
        return nombre_foto;
    }

    public void setNombre_foto(String nombre_foto) {
        this.nombre_foto = nombre_foto;
    }

    public String getRuta_foto() {
        return ruta_foto;
    }

    public void setRuta_foto(String ruta_foto) {
        this.ruta_foto = ruta_foto;
    }

    public String getFecha_tx() {
        return fecha_tx;
    }

    public void setFecha_tx(String fecha_tx) {
        this.fecha_tx = fecha_tx;
    }

    public int getUser_tx() {
        return user_tx;
    }

    public void setUser_tx(int user_tx) {
        this.user_tx = user_tx;
    }
}
