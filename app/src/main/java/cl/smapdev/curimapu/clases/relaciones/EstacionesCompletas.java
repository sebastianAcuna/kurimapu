package cl.smapdev.curimapu.clases.relaciones;

import androidx.room.Embedded;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import cl.smapdev.curimapu.clases.tablas.EstacionFloracion;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracionDetalle;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracionEstaciones;


public class EstacionesCompletas implements Serializable {

    @SerializedName("estacion_floracion_estaciones_cabecera")
    @Embedded
    private EstacionFloracionEstaciones estaciones;

    @SerializedName("estacion_floracion_estaciones_detalle")
    @Embedded
    private List<EstacionFloracionDetalle> detalles;

    public EstacionFloracionEstaciones getEstaciones() {
        return estaciones;
    }

    public void setEstaciones(EstacionFloracionEstaciones estaciones) {
        this.estaciones = estaciones;
    }

    public List<EstacionFloracionDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<EstacionFloracionDetalle> detalles) {
        this.detalles = detalles;
    }
}
