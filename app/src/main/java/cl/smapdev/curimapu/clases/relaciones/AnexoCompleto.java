package cl.smapdev.curimapu.clases.relaciones;

import androidx.room.Embedded;

import java.io.Serializable;

import cl.smapdev.curimapu.Variedad;
import cl.smapdev.curimapu.clases.Agricultor;
import cl.smapdev.curimapu.clases.AnexoContrato;
import cl.smapdev.curimapu.clases.Especie;

public class AnexoCompleto implements Serializable {

    @Embedded
    private AnexoContrato anexoContrato;

    @Embedded
    private Agricultor agricultor;

    @Embedded
    private Especie especie;

    @Embedded
    private Variedad variedad;


    public AnexoContrato getAnexoContrato() {
        return anexoContrato;
    }

    public void setAnexoContrato(AnexoContrato anexoContrato) {
        this.anexoContrato = anexoContrato;
    }

    public Agricultor getAgricultor() {
        return agricultor;
    }

    public void setAgricultor(Agricultor agricultor) {
        this.agricultor = agricultor;
    }

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }

    public Variedad getVariedad() {
        return variedad;
    }

    public void setVariedad(Variedad variedad) {
        this.variedad = variedad;
    }
}
