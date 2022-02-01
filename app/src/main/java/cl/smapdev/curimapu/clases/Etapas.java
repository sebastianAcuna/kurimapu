package cl.smapdev.curimapu.clases;

public class Etapas {

    private int numeroEtapa;

    private String descEtapa;

    private boolean etapaSelected;



    public Etapas() {
    }

    public Etapas(int numeroEtapa, String descEtapa, boolean etapaSelected) {
        this.numeroEtapa = numeroEtapa;
        this.descEtapa = descEtapa;
        this.etapaSelected = etapaSelected;
    }


    public int getNumeroEtapa() {
        return numeroEtapa;
    }

    public void setNumeroEtapa(int numeroEtapa) {
        this.numeroEtapa = numeroEtapa;
    }

    public String getDescEtapa() {
        return descEtapa;
    }

    public void setDescEtapa(String descEtapa) {
        this.descEtapa = descEtapa;
    }

    public boolean isEtapaSelected() {
        return etapaSelected;
    }

    public void setEtapaSelected(boolean etapaSelected) {
        this.etapaSelected = etapaSelected;
    }
}
