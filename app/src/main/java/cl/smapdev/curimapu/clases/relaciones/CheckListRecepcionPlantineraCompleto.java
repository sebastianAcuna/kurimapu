package cl.smapdev.curimapu.clases.relaciones;

import androidx.room.Embedded;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.CheckListRecepcionPlantinera;
import cl.smapdev.curimapu.clases.tablas.CheckListRecepcionPlantineraDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListRecepcionPlantineraDetalleFotos;

public class CheckListRecepcionPlantineraCompleto {

    @Embedded
    CheckListRecepcionPlantinera clCabecera;

    @Embedded
    List<CheckListRecepcionPlantineraDetalle> clDetalle;

    @Embedded
    List<CheckListRecepcionPlantineraDetalleFotos> clDetalleFoto;


    public CheckListRecepcionPlantinera getClCabecera() {
        return clCabecera;
    }

    public void setClCabecera(CheckListRecepcionPlantinera clCabecera) {
        this.clCabecera = clCabecera;
    }

    public List<CheckListRecepcionPlantineraDetalle> getClDetalle() {
        return clDetalle;
    }

    public void setClDetalle(List<CheckListRecepcionPlantineraDetalle> clDetalle) {
        this.clDetalle = clDetalle;
    }

    public List<CheckListRecepcionPlantineraDetalleFotos> getClDetalleFoto() {
        return clDetalleFoto;
    }

    public void setClDetalleFoto(List<CheckListRecepcionPlantineraDetalleFotos> clDetalleFoto) {
        this.clDetalleFoto = clDetalleFoto;
    }
}
