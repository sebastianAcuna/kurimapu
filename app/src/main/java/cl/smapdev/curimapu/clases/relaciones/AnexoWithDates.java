package cl.smapdev.curimapu.clases.relaciones;

import androidx.room.Embedded;

import java.io.Serializable;

import cl.smapdev.curimapu.clases.tablas.AnexoCorreoFechas;
import cl.smapdev.curimapu.clases.tablas.Comuna;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.tablas.Visitas;

public class AnexoWithDates implements Serializable {


    @Embedded
    AnexoCompleto AnexoCompleto;


    @Embedded
    AnexoCorreoFechas AnexoCorreoFichas;


    @Embedded(prefix = "foo_")
    Comuna comuna;


    @Embedded(prefix = "usu_")
    Usuario usuario;

    @Embedded
    Visitas visitas;


    public Visitas getVisitas() {
        return visitas;
    }

    public void setVisitas(Visitas visitas) {
        this.visitas = visitas;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Comuna getComuna() {
        return comuna;
    }

    public void setComuna(Comuna comuna) {
        this.comuna = comuna;
    }

    public cl.smapdev.curimapu.clases.relaciones.AnexoCompleto getAnexoCompleto() {
        return AnexoCompleto;
    }

    public void setAnexoCompleto(cl.smapdev.curimapu.clases.relaciones.AnexoCompleto anexoCompleto) {
        AnexoCompleto = anexoCompleto;
    }

    public AnexoCorreoFechas getAnexoCorreoFichas() {
        return AnexoCorreoFichas;
    }

    public void setAnexoCorreoFichas(AnexoCorreoFechas anexoCorreoFichas) {
        AnexoCorreoFichas = anexoCorreoFichas;
    }
}
