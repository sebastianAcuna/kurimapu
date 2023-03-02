package cl.smapdev.curimapu.clases.relaciones;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.CheckListCosecha;
import cl.smapdev.curimapu.clases.tablas.CheckListSiembra;
import cl.smapdev.curimapu.clases.tablas.ChecklistDevolucionSemilla;

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

    @SerializedName("checkListCosechas")
    private List<CheckListCosecha> checkListCosechas;

    @SerializedName("checkListCapacitacionSiembras")
    private List<CheckListCapCompleto> checkListCapCompletos;

    @SerializedName("checkListLimpiezaCamiones")
    private List<CheckListLimpiezaCamionesCompleto> checkListLimpiezaCamionesCompletos;


    @SerializedName("checkListDevolucionSemilla")
    private List<ChecklistDevolucionSemilla> checkListDevolucionSemilla;


    public List<ChecklistDevolucionSemilla> getCheckListDevolucionSemilla() {
        return checkListDevolucionSemilla;
    }

    public void setCheckListDevolucionSemilla(List<ChecklistDevolucionSemilla> checkListDevolucionSemilla) {
        this.checkListDevolucionSemilla = checkListDevolucionSemilla;
    }

    public List<CheckListLimpiezaCamionesCompleto> getCheckListLimpiezaCamionesCompletos() {
        return checkListLimpiezaCamionesCompletos;
    }

    public void setCheckListLimpiezaCamionesCompletos(List<CheckListLimpiezaCamionesCompleto> checkListLimpiezaCamionesCompletos) {
        this.checkListLimpiezaCamionesCompletos = checkListLimpiezaCamionesCompletos;
    }

    public List<CheckListCosecha> getCheckListCosechas() {
        return checkListCosechas;
    }

    public void setCheckListCosechas(List<CheckListCosecha> checkListCosechas) {
        this.checkListCosechas = checkListCosechas;
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

    public List<CheckListSiembra> getCheckListSiembras() {
        return checkListSiembras;
    }

    public void setCheckListSiembras(List<CheckListSiembra> checkListSiembras) {
        this.checkListSiembras = checkListSiembras;
    }

    public List<CheckListCapCompleto> getCheckListCapCompletos() {
        return checkListCapCompletos;
    }

    public void setCheckListCapCompletos(List<CheckListCapCompleto> checkListCapCompletos) {
        this.checkListCapCompletos = checkListCapCompletos;
    }
}
