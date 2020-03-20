package cl.smapdev.curimapu.clases.relaciones;

import androidx.room.Embedded;


import cl.smapdev.curimapu.clases.tablas.Clientes;
import cl.smapdev.curimapu.clases.tablas.Visitas;

public class VisitasCompletas {

    @Embedded
    private Visitas visitas;

    @Embedded
    private AnexoCompleto  anexoCompleto;

    @Embedded
    private Clientes clientes;


    public Clientes getClientes() {
        return clientes;
    }

    public void setClientes(Clientes clientes) {
        this.clientes = clientes;
    }

    public Visitas getVisitas() {
        return visitas;
    }

    public void setVisitas(Visitas visitas) {
        this.visitas = visitas;
    }

    public AnexoCompleto getAnexoCompleto() {
        return anexoCompleto;
    }

    public void setAnexoCompleto(AnexoCompleto anexoCompleto) {
        this.anexoCompleto = anexoCompleto;
    }

}
