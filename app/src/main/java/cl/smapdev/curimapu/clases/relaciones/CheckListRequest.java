package cl.smapdev.curimapu.clases.relaciones;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.CheckListAplicacionHormonas;
import cl.smapdev.curimapu.clases.tablas.CheckListGuiaInterna;
import cl.smapdev.curimapu.clases.tablas.CheckListSiembra;

public class CheckListRequest {
    @SerializedName("idDispo")
    private int idDispo;

    @SerializedName("idUsuario")
    private int idUsuario;


    @SerializedName("codigo")
    private int codigo;

    @SerializedName("mensaje")
    private String mensaje;

    @SerializedName("checkListSiembras")
    private List<CheckListSiembra> checkListSiembras;


    @SerializedName("checkListGuiaInterna")
    private List<CheckListGuiaInterna> checkListGuiaInternas;


    @SerializedName("checkListAplicacionHormonas")
    private List<CheckListAplicacionHormonas> checkListAplicacionHormonas;


    @SerializedName("checkListRoguing")
    private List<CheckListRoguingCompleto> checkListRoguing;


    public List<CheckListGuiaInterna> getCheckListGuiaInternas() {
        return checkListGuiaInternas;
    }

    public void setCheckListGuiaInternas(List<CheckListGuiaInterna> checkListGuiaInternas) {
        this.checkListGuiaInternas = checkListGuiaInternas;
    }

    public int getIdDispo() {
        return idDispo;
    }

    public void setIdDispo(int idDispo) {
        this.idDispo = idDispo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }


    public List<CheckListRoguingCompleto> getCheckListRoguing() {
        return checkListRoguing;
    }

    public void setCheckListRoguing(List<CheckListRoguingCompleto> checkListRoguing) {
        this.checkListRoguing = checkListRoguing;
    }

    public List<CheckListSiembra> getCheckListSiembras() {
        return checkListSiembras;
    }

    public void setCheckListSiembras(List<CheckListSiembra> checkListSiembras) {
        this.checkListSiembras = checkListSiembras;
    }

    public List<CheckListAplicacionHormonas> getCheckListAplicacionHormonas() {
        return checkListAplicacionHormonas;
    }

    public void setCheckListAplicacionHormonas(List<CheckListAplicacionHormonas> checkListAplicacionHormonas) {
        this.checkListAplicacionHormonas = checkListAplicacionHormonas;
    }
}
