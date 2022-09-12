package cl.smapdev.curimapu.clases.relaciones;

import androidx.room.Embedded;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembra;
import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembraDetalle;


public class CheckListCapCompleto {

    @SerializedName("capacitacion_siembra_cabecera")
    @Embedded
    private CheckListCapacitacionSiembra cabecera;

    @SerializedName("capacitacion_siembra_detalle")
    @Embedded
    private List<CheckListCapacitacionSiembraDetalle> detalles;


    public CheckListCapacitacionSiembra getCabecera() {
        return cabecera;
    }

    public void setCabecera(CheckListCapacitacionSiembra cabecera) {
        this.cabecera = cabecera;
    }

    public List<CheckListCapacitacionSiembraDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<CheckListCapacitacionSiembraDetalle> detalles) {
        this.detalles = detalles;
    }
}
