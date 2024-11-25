package cl.smapdev.curimapu.clases.relaciones;

import androidx.room.Embedded;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.CheckListRoguingDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingDetalleFechas;

public class CheckListRoguingFechaCompleto {

    @Embedded
    CheckListRoguingDetalleFechas fecha;

    @Embedded
    List<CheckListRoguingDetalle> detalles;


    public CheckListRoguingDetalleFechas getFecha() {
        return fecha;
    }

    public void setFecha(CheckListRoguingDetalleFechas fecha) {
        this.fecha = fecha;
    }

    public List<CheckListRoguingDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<CheckListRoguingDetalle> detalles) {
        this.detalles = detalles;
    }
}
