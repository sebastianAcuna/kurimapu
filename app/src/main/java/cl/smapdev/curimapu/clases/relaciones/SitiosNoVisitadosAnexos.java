package cl.smapdev.curimapu.clases.relaciones;

import androidx.room.Embedded;

import java.io.Serializable;

import cl.smapdev.curimapu.clases.tablas.Agricultor;
import cl.smapdev.curimapu.clases.tablas.AnexoContrato;
import cl.smapdev.curimapu.clases.tablas.Especie;
import cl.smapdev.curimapu.clases.tablas.FichasNew;
import cl.smapdev.curimapu.clases.tablas.Lotes;
import cl.smapdev.curimapu.clases.tablas.Predios;
import cl.smapdev.curimapu.clases.tablas.Temporada;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.tablas.Variedad;

public class SitiosNoVisitadosAnexos implements Serializable {
    @Embedded
    AnexoContrato anexoContrato;

    @Embedded(prefix = "vari_")
    Variedad variedad;

    @Embedded
    Especie especie;

    @Embedded
    Lotes lotes;

    @Embedded(prefix = "predi_")
    Predios predios;

    @Embedded(prefix = "fichas_")
    FichasNew fichasNew;

    @Embedded(prefix = "usu_")
    Usuario usuario;

    @Embedded(prefix = "agri_")
    Agricultor agricultor;

    @Embedded(prefix = "tempo_")
    Temporada temporada;


    public AnexoContrato getAnexoContrato() {
        return anexoContrato;
    }

    public void setAnexoContrato(AnexoContrato anexoContrato) {
        this.anexoContrato = anexoContrato;
    }

    public Variedad getVariedad() {
        return variedad;
    }

    public void setVariedad(Variedad variedad) {
        this.variedad = variedad;
    }

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }

    public Lotes getLotes() {
        return lotes;
    }

    public void setLotes(Lotes lotes) {
        this.lotes = lotes;
    }

    public Predios getPredios() {
        return predios;
    }

    public void setPredios(Predios predios) {
        this.predios = predios;
    }

    public FichasNew getFichasNew() {
        return fichasNew;
    }

    public void setFichasNew(FichasNew fichasNew) {
        this.fichasNew = fichasNew;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Agricultor getAgricultor() {
        return agricultor;
    }

    public void setAgricultor(Agricultor agricultor) {
        this.agricultor = agricultor;
    }

    public Temporada getTemporada() {
        return temporada;
    }

    public void setTemporada(Temporada temporada) {
        this.temporada = temporada;
    }
}
