package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "estacion_floracion")
public class EstacionFloracion {

    @PrimaryKey(autoGenerate = true)
    public int id_estacion_floracion;

    public String clave_unica_floracion;

    public  int id_ac_floracion;


    public int cantidad_machos;

    public String fecha;

    public int estado_sincronizacion;

    public int id_user_crea;
    public String fecha_crea;
    public int id_user_mod;
    public String fecha_mod;


    public int getId_ac_floracion() {
        return id_ac_floracion;
    }

    public void setId_ac_floracion(int id_ac_floracion) {
        this.id_ac_floracion = id_ac_floracion;
    }

    public int getId_estacion_floracion() {
        return id_estacion_floracion;
    }

    public void setId_estacion_floracion(int id_estacion_floracion) {
        this.id_estacion_floracion = id_estacion_floracion;
    }

    public String getClave_unica_floracion() {
        return clave_unica_floracion;
    }

    public void setClave_unica_floracion(String clave_unica_floracion) {
        this.clave_unica_floracion = clave_unica_floracion;
    }

    public int getCantidad_machos() {
        return cantidad_machos;
    }

    public void setCantidad_machos(int cantidad_machos) {
        this.cantidad_machos = cantidad_machos;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getEstado_sincronizacion() {
        return estado_sincronizacion;
    }

    public void setEstado_sincronizacion(int estado_sincronizacion) {
        this.estado_sincronizacion = estado_sincronizacion;
    }

    public int getId_user_crea() {
        return id_user_crea;
    }

    public void setId_user_crea(int id_user_crea) {
        this.id_user_crea = id_user_crea;
    }

    public String getFecha_crea() {
        return fecha_crea;
    }

    public void setFecha_crea(String fecha_crea) {
        this.fecha_crea = fecha_crea;
    }

    public int getId_user_mod() {
        return id_user_mod;
    }

    public void setId_user_mod(int id_user_mod) {
        this.id_user_mod = id_user_mod;
    }

    public String getFecha_mod() {
        return fecha_mod;
    }

    public void setFecha_mod(String fecha_mod) {
        this.fecha_mod = fecha_mod;
    }
}
