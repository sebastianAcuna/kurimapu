package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "checklist_roguing_detalle_fechas")
public class CheckListRoguingDetalleFechas {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id_cl_roguing_detalle_fecha")
    private int id_cl_roguing_detalle;
    private String clave_unica_roguing;
    private String clave_unica_detalle_fecha;

    private String fecha;
    private String estado_fenologico;

    @SerializedName("estado_fecha")
    private int estadoFecha;

    private int estado_sincronizacion;


    public int getEstadoFecha() {
        return estadoFecha;
    }

    public void setEstadoFecha(int estadoFecha) {
        this.estadoFecha = estadoFecha;
    }

    public int getEstado_sincronizacion() {
        return estado_sincronizacion;
    }

    public void setEstado_sincronizacion(int estado_sincronizacion) {
        this.estado_sincronizacion = estado_sincronizacion;
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


    public String getClave_unica_detalle_fecha() {
        return clave_unica_detalle_fecha;
    }

    public void setClave_unica_detalle_fecha(String clave_unica_detalle_fecha) {
        this.clave_unica_detalle_fecha = clave_unica_detalle_fecha;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado_fenologico() {
        return estado_fenologico;
    }

    public void setEstado_fenologico(String estado_fenologico) {
        this.estado_fenologico = estado_fenologico;
    }
}
