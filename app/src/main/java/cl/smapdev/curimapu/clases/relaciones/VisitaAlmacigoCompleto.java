package cl.smapdev.curimapu.clases.relaciones;

import androidx.room.Embedded;

import java.io.Serializable;

import cl.smapdev.curimapu.clases.tablas.OpAlmacigos;
import cl.smapdev.curimapu.clases.tablas.VisitasAlmacigos;


public class VisitaAlmacigoCompleto implements Serializable {

    @Embedded
    private VisitasAlmacigos visitasAlmacigos;

    @Embedded
    private OpAlmacigos opAlmacigos;

    public VisitasAlmacigos getVisitasAlmacigos() {
        return visitasAlmacigos;
    }

    public void setVisitasAlmacigos(VisitasAlmacigos visitasAlmacigos) {
        this.visitasAlmacigos = visitasAlmacigos;
    }

    public OpAlmacigos getOpAlmacigos() {
        return opAlmacigos;
    }

    public void setOpAlmacigos(OpAlmacigos opAlmacigos) {
        this.opAlmacigos = opAlmacigos;
    }
}
