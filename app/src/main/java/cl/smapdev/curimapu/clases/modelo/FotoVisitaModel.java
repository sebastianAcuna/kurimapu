package cl.smapdev.curimapu.clases.modelo;

import java.io.File;

public class FotoVisitaModel {
    private File archivo;
    private boolean favorita;

    public FotoVisitaModel(File archivo) {
        this.archivo = archivo;
        this.favorita = false;
    }

    public File getArchivo() {
        return archivo;
    }

    public boolean isFavorita() {
        return favorita;
    }

    public void setFavorita(boolean favorita) {
        this.favorita = favorita;
    }
}