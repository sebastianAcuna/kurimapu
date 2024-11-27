package cl.smapdev.curimapu.clases.relaciones;

import androidx.room.Embedded;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.CheckListRevisionFrutos;
import cl.smapdev.curimapu.clases.tablas.CheckListRevisionFrutosDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListRevisionFrutosFotos;

public class CheckListRevisionFrutosCompleto {

    @Embedded
    CheckListRevisionFrutos checkListRevisionFrutos;

    @Embedded
    List<CheckListRevisionFrutosDetalle> checkListRevisionFrutosDetalle;

    @Embedded
    List<CheckListRevisionFrutosFotos> checkListRevisionFrutosFotos;


    public CheckListRevisionFrutos getCheckListRevisionFrutos() {
        return checkListRevisionFrutos;
    }

    public void setCheckListRevisionFrutos(CheckListRevisionFrutos checkListRevisionFrutos) {
        this.checkListRevisionFrutos = checkListRevisionFrutos;
    }

    public List<CheckListRevisionFrutosDetalle> getCheckListRevisionFrutosDetalle() {
        return checkListRevisionFrutosDetalle;
    }

    public void setCheckListRevisionFrutosDetalle(List<CheckListRevisionFrutosDetalle> checkListRevisionFrutosDetalle) {
        this.checkListRevisionFrutosDetalle = checkListRevisionFrutosDetalle;
    }

    public List<CheckListRevisionFrutosFotos> getCheckListRevisionFrutosFotos() {
        return checkListRevisionFrutosFotos;
    }

    public void setCheckListRevisionFrutosFotos(List<CheckListRevisionFrutosFotos> checkListRevisionFrutosFotos) {
        this.checkListRevisionFrutosFotos = checkListRevisionFrutosFotos;
    }
}
