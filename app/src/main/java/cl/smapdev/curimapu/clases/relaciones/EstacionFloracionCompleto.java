package cl.smapdev.curimapu.clases.relaciones;

import androidx.room.Embedded;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import cl.smapdev.curimapu.clases.tablas.EstacionFloracion;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracionDetalle;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracionEstaciones;


public class EstacionFloracionCompleto implements Serializable {

    @SerializedName("estacion_floracion_cabecera")
    @Embedded
    private EstacionFloracion estacionFloracion;


    @SerializedName("estacion_floracion_estaciones")
    @Embedded
    private List<EstacionesCompletas> estaciones;


    public EstacionFloracion getEstacionFloracion() {
        return estacionFloracion;
    }

    public void setEstacionFloracion(EstacionFloracion estacionFloracion) {
        this.estacionFloracion = estacionFloracion;
    }

    public List<EstacionesCompletas> getEstaciones() {
        return estaciones;
    }

    public void setEstaciones(List<EstacionesCompletas> estaciones) {
        this.estaciones = estaciones;
    }
}
