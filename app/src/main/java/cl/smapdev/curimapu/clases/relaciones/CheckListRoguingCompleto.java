package cl.smapdev.curimapu.clases.relaciones;

import androidx.room.Embedded;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.CheckListRoguing;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingDetalleFechas;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingFotoCabecera;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingFotoDetalle;

public class CheckListRoguingCompleto {

    @Embedded
    CheckListRoguing checkListRoguing;

    @Embedded
    List<CheckListRoguingDetalle> checkListRoguingDetalle;

    @Embedded
    List<CheckListRoguingDetalleFechas> checkListRoguingDetalleFechas;

    @Embedded
    List<CheckListRoguingFotoCabecera> checkListFotoCabecera;

    @Embedded
    List<CheckListRoguingFotoDetalle> checkListFotoDetalle;


    public List<CheckListRoguingDetalleFechas> getCheckListRoguingDetalleFechas() {
        return checkListRoguingDetalleFechas;
    }

    public void setCheckListRoguingDetalleFechas(List<CheckListRoguingDetalleFechas> checkListRoguingDetalleFechas) {
        this.checkListRoguingDetalleFechas = checkListRoguingDetalleFechas;
    }

    public CheckListRoguing getCheckListRoguing() {
        return checkListRoguing;
    }

    public void setCheckListRoguing(CheckListRoguing checkListRoguing) {
        this.checkListRoguing = checkListRoguing;
    }

    public List<CheckListRoguingDetalle> getCheckListRoguingDetalle() {
        return checkListRoguingDetalle;
    }

    public void setCheckListRoguingDetalle(List<CheckListRoguingDetalle> checkListRoguingDetalle) {
        this.checkListRoguingDetalle = checkListRoguingDetalle;
    }

    public List<CheckListRoguingFotoCabecera> getCheckListFotoCabecera() {
        return checkListFotoCabecera;
    }

    public void setCheckListFotoCabecera(List<CheckListRoguingFotoCabecera> checkListFotoCabecera) {
        this.checkListFotoCabecera = checkListFotoCabecera;
    }

    public List<CheckListRoguingFotoDetalle> getCheckListFotoDetalle() {
        return checkListFotoDetalle;
    }

    public void setCheckListFotoDetalle(List<CheckListRoguingFotoDetalle> checkListFotoDetalle) {
        this.checkListFotoDetalle = checkListFotoDetalle;
    }
}
