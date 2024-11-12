package cl.smapdev.curimapu.clases.relaciones;

import androidx.room.Embedded;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.DesplegableAplicacionHormonas;
import cl.smapdev.curimapu.clases.tablas.DesplegableNumeroAplicacionHormonas;

public class DesplegablesAplicacionHormonaCompleto {


    @Embedded
    List<DesplegableAplicacionHormonas> desplegableAplicacionHormonas;

    @Embedded
    List<DesplegableNumeroAplicacionHormonas> desplegableNumeroAplicacionHormonas;


    public List<DesplegableAplicacionHormonas> getDesplegableAplicacionHormonas() {
        return desplegableAplicacionHormonas;
    }

    public void setDesplegableAplicacionHormonas(List<DesplegableAplicacionHormonas> desplegableAplicacionHormonas) {
        this.desplegableAplicacionHormonas = desplegableAplicacionHormonas;
    }

    public List<DesplegableNumeroAplicacionHormonas> getDesplegableNumeroAplicacionHormonas() {
        return desplegableNumeroAplicacionHormonas;
    }

    public void setDesplegableNumeroAplicacionHormonas(List<DesplegableNumeroAplicacionHormonas> desplegableNumeroAplicacionHormonas) {
        this.desplegableNumeroAplicacionHormonas = desplegableNumeroAplicacionHormonas;
    }
}
