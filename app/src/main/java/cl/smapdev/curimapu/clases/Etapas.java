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

    public String getDescEtapa() {
        return descEtapa;
    }

    public boolean isEtapaSelected() {
        return etapaSelected;
    }

    public void setEtapaSelected(boolean etapaSelected) {
        this.etapaSelected = etapaSelected;
    }
}
