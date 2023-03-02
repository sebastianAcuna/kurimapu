package cl.smapdev.curimapu.clases.relaciones;

import androidx.room.Embedded;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import cl.smapdev.curimapu.clases.tablas.Comuna;
import cl.smapdev.curimapu.clases.tablas.Fichas;
import cl.smapdev.curimapu.clases.tablas.FichasNew;
import cl.smapdev.curimapu.clases.tablas.Lotes;
import cl.smapdev.curimapu.clases.tablas.Predios;
import cl.smapdev.curimapu.clases.tablas.Variedad;
import cl.smapdev.curimapu.clases.tablas.Agricultor;
import cl.smapdev.curimapu.clases.tablas.AnexoContrato;
import cl.smapdev.curimapu.clases.tablas.Especie;


public class AnexoCompleto implements Serializable {

    @Embedded
    private AnexoContrato anexoContrato;

    @Embedded
    private Agricultor agricultor;

    @Embedded
    private Especie especie;

    @Embedded
    private Variedad variedad;

    @Embedded
    private FichasNew fichas;
    @Embedded
    private Predios predios;
    @Embedded
    private Lotes lotes;

    @Embedded(prefix = "c_")
    private Comuna comuna;


    public Comuna getComuna() {
        return comuna;
    }

    public void setComuna(Comuna comuna) {
        this.comuna = comuna;
    }

    public FichasNew getFichas() {
        return fichas;
    }

    public void setFichas(FichasNew fichas) {
        this.fichas = fichas;
    }

    public Predios getPredios() {
        return predios;
    }

    public void setPredios(Predios predios) {
        this.predios = predios;
    }

    public Lotes getLotes() {
        return lotes;
    }

    public void setLotes(Lotes lotes) {
        this.lotes = lotes;
    }

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
