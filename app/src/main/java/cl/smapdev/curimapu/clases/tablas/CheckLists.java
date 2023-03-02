package cl.smapdev.curimapu.clases.tablas;

import java.util.List;

public class CheckLists {

    private int tipoCheckList;
    private String descCheckList;
    private boolean isExpanded = false;
    private int idAnexo;
    private List<CheckListDetails> details;


    public int getIdAnexo() {
        return idAnexo;
    }

    public void setIdAnexo(int idAnexo) {
        this.idAnexo = idAnexo;
    }

    public int getTipoCheckList() {
        return tipoCheckList;
    }

    public void setTipoCheckList(int tipoCheckList) {
        this.tipoCheckList = tipoCheckList;
    }

    public String getDescCheckList() {
        return descCheckList;
    }

    public void setDescCheckList(String descCheckList) {
        this.descCheckList = descCheckList;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public List<CheckListDetails> getDetails() {
        return details;
    }

    public void setDetails(List<CheckListDetails> details) {
        this.details = details;
    }
}


