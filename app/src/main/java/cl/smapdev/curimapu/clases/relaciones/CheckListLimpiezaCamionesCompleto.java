package cl.smapdev.curimapu.clases.relaciones;

import androidx.room.Embedded;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembra;
import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembraDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListLimpiezaCamiones;
import cl.smapdev.curimapu.clases.tablas.ChecklistLimpiezaCamionesDetalle;


public class CheckListLimpiezaCamionesCompleto {

    @SerializedName("limpieza_camiones_cabecera")
    @Embedded
    private CheckListLimpiezaCamiones cabecera;

    @SerializedName("limpieza_camiones_detalle")
    @Embedded
    private List<ChecklistLimpiezaCamionesDetalle> detalles;


    public CheckListLimpiezaCamiones getCabecera() {
        return cabecera;
    }

    public void setCabecera(CheckListLimpiezaCamiones cabecera) {
        this.cabecera = cabecera;
    }

    public List<ChecklistLimpiezaCamionesDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<ChecklistLimpiezaCamionesDetalle> detalles) {
        this.detalles = detalles;
    }
}
