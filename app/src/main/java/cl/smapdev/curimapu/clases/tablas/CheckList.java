package cl.smapdev.curimapu.clases.tablas;

import java.util.List;

public class CheckList {


    private int tipoDocumento;
    private String descTipoDocumento;

    private int estadoDocumento;
    private int estadoSincronizazion;

    private int idAnexo;
    private int idDocumentoPendiente;

    private boolean isExpanded;

    private List<String> nestedChecks;


    public List<String> getNestedChecks() {
        return nestedChecks;
    }

    public void setNestedChecks(List<String> nestedChecks) {
        this.nestedChecks = nestedChecks;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public int getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(int tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDescTipoDocumento() {
        return descTipoDocumento;
    }

    public void setDescTipoDocumento(String descTipoDocumento) {
        this.descTipoDocumento = descTipoDocumento;
    }

    public int getEstadoDocumento() {
        return estadoDocumento;
    }

    public void setEstadoDocumento(int estadoDocumento) {
        this.estadoDocumento = estadoDocumento;
    }

    public int getEstadoSincronizazion() {
        return estadoSincronizazion;
    }

    public void setEstadoSincronizazion(int estadoSincronizazion) {
        this.estadoSincronizazion = estadoSincronizazion;
    }

    public int getIdAnexo() {
        return idAnexo;
    }

    public void setIdAnexo(int idAnexo) {
        this.idAnexo = idAnexo;
    }

    public int getIdDocumentoPendiente() {
        return idDocumentoPendiente;
    }

    public void setIdDocumentoPendiente(int idDocumentoPendiente) {
        this.idDocumentoPendiente = idDocumentoPendiente;
    }
}
