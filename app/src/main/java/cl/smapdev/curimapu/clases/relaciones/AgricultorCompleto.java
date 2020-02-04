package cl.smapdev.curimapu.clases.relaciones;

import androidx.room.Embedded;

import cl.smapdev.curimapu.clases.Agricultor;
import cl.smapdev.curimapu.clases.Comuna;
import cl.smapdev.curimapu.clases.Region;

public class AgricultorCompleto {

    @Embedded
    private Agricultor agricultor;

    @Embedded
    private Region region;

    @Embedded
    private Comuna comuna;


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
