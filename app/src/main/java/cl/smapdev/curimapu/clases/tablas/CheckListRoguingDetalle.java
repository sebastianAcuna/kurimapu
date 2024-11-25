package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "checklist_roguing_detalle")
public class CheckListRoguingDetalle {

    @PrimaryKey(autoGenerate = true)
    private int id_cl_roguing_detalle;
    private String clave_unica_roguing;
    private String clave_unica_detalle;
    private String clave_unica_detalle_fecha;

    private String genero; //H y M
    private String descripcion_fuera_tipo;


    private String fecha_tx;
    private int id_user_tx;

    private int estado_sincronizacion;


    private int cantidad;


    public String getFecha_tx() {
        return fecha_tx;
    }

    public void setFecha_tx(String fecha_tx) {
        this.fecha_tx = fecha_tx;
    }

    public int getId_user_tx() {
        return id_user_tx;
    }

    public void setId_user_tx(int id_user_tx) {
        this.id_user_tx = id_user_tx;
    }

    public int getEstado_sincronizacion() {
        return estado_sincronizacion;
    }

    public void setEstado_sincronizacion(int estado_sincronizacion) {
        this.estado_sincronizacion = estado_sincronizacion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion_fuera_tipo() {
        return descripcion_fuera_tipo;
    }

    public void setDescripcion_fuera_tipo(String descripcion_fuera_tipo) {
        this.descripcion_fuera_tipo = descripcion_fuera_tipo;
    }

    public String getClave_unica_detalle_fecha() {
        return clave_unica_detalle_fecha;
    }

    public void setClave_unica_detalle_fecha(String clave_unica_detalle_fecha) {
        this.clave_unica_detalle_fecha = clave_unica_detalle_fecha;
    }


    public int getId_cl_roguing_detalle() {
        return id_cl_roguing_detalle;
    }

    public void setId_cl_roguing_detalle(int id_cl_roguing_detalle) {
        this.id_cl_roguing_detalle = id_cl_roguing_detalle;
    }

    public String getClave_unica_roguing() {
        return clave_unica_roguing;
    }

    public void setClave_unica_roguing(String clave_unica_roguing) {
        this.clave_unica_roguing = clave_unica_roguing;
    }

    public String getClave_unica_detalle() {
        return clave_unica_detalle;
    }

    public void setClave_unica_detalle(String clave_unica_detalle) {
        this.clave_unica_detalle = clave_unica_detalle;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

}
