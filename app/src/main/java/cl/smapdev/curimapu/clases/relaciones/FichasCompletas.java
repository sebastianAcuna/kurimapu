package cl.smapdev.curimapu.clases.relaciones;


import androidx.room.Embedded;

import java.io.Serializable;

import cl.smapdev.curimapu.clases.tablas.Agricultor;
import cl.smapdev.curimapu.clases.tablas.Comuna;
import cl.smapdev.curimapu.clases.tablas.Fichas;
import cl.smapdev.curimapu.clases.tablas.Region;

public class FichasCompletas implements Serializable {

    @Embedded
    private Fichas fichas;

    @Embedded
    private Agricultor agricultor;

    @Embedded
    private Region region;

    @Embedded
    private Comuna comuna;


    public Fichas getFichas() {
        return fichas;
    }

    public void setFichas(Fichas fichas) {
        this.fichas = fichas;
    }

    public Agricultor getAgricultor() {
        return agricultor;
    }

    public void setAgricultor(Agricultor agricultor) {
        this.agricultor = agricultor;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Comuna getComuna() {
        return comuna;
    }

    public void setComuna(Comuna comuna) {
        this.comuna = comuna;
    }
}
