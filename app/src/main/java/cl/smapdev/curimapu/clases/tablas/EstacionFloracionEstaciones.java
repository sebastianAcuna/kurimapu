package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "estacion_floracion_estaciones")
public class EstacionFloracionEstaciones {

    @PrimaryKey(autoGenerate = true)
    public int id_estacion_floracion_estaciones;

    public String clave_unica_floracion_cabecera;
    public String clave_unica_floracion_estaciones;
    public int numero_estacion;


    public int getId_estacion_floracion_estaciones() {
        return id_estacion_floracion_estaciones;
    }

    public void setId_estacion_floracion_estaciones(int id_estacion_floracion_estaciones) {
        this.id_estacion_floracion_estaciones = id_estacion_floracion_estaciones;
    }

    public String getClave_unica_floracion_cabecera() {
        return clave_unica_floracion_cabecera;
    }

    public void setClave_unica_floracion_cabecera(String clave_unica_floracion_cabecera) {
        this.clave_unica_floracion_cabecera = clave_unica_floracion_cabecera;
    }

    public String getClave_unica_floracion_estaciones() {
        return clave_unica_floracion_estaciones;
    }

    public void setClave_unica_floracion_estaciones(String clave_unica_floracion_estaciones) {
        this.clave_unica_floracion_estaciones = clave_unica_floracion_estaciones;
    }

    public int getNumero_estacion() {
        return numero_estacion;
    }

    public void setNumero_estacion(int numero_estacion) {
        this.numero_estacion = numero_estacion;
    }
}
