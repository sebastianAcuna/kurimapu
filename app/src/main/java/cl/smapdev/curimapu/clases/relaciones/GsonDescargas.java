package cl.smapdev.curimapu.clases.relaciones;

import androidx.room.Embedded;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.Agricultor;
import cl.smapdev.curimapu.clases.tablas.AnexoContrato;
import cl.smapdev.curimapu.clases.tablas.Comuna;
import cl.smapdev.curimapu.clases.tablas.CropRotation;
import cl.smapdev.curimapu.clases.tablas.Especie;
import cl.smapdev.curimapu.clases.tablas.Fichas;
import cl.smapdev.curimapu.clases.tablas.Provincia;
import cl.smapdev.curimapu.clases.tablas.Region;
import cl.smapdev.curimapu.clases.tablas.Temporada;
import cl.smapdev.curimapu.clases.tablas.Variedad;
import cl.smapdev.curimapu.clases.tablas.Visitas;
import cl.smapdev.curimapu.clases.tablas.detalle_visita_prop;
import cl.smapdev.curimapu.clases.tablas.pro_cli_mat;

public class GsonDescargas {


    @SerializedName("array_detalle_prop")
    @Embedded
    private List<pro_cli_mat> pro_cli_matList;


    @SerializedName("array_temporada")
    @Embedded
    private List<Temporada> temporadas;

    @SerializedName("array_rotation_crop")
    @Embedded
    private List<CropRotation> cropRotations;

    @SerializedName("array_valor_prop")
    @Embedded
    private List<detalle_visita_prop> detalle_visita_props;

    @SerializedName("array_visitas")
    @Embedded
    private List<Visitas> visitasList;

    @SerializedName("array_anexos")
    @Embedded
    private List<AnexoContrato> anexoContratoList;

    @SerializedName("array_agricultores")
    @Embedded
    private List<Agricultor> agricultorList;

    @SerializedName("array_region")
    @Embedded
    private List<Region> regionList;

    @SerializedName("array_provincias")
    @Embedded
    private List<Provincia> provinciaList;

    @SerializedName("array_comuna")
    @Embedded
    private List<Comuna> comunaList;

    @SerializedName("array_especie")
    @Embedded
    private List<Especie> especieList;

    @SerializedName("array_materiales")
    @Embedded
    private List<Variedad> variedadList;

    @SerializedName("array_fichas")
    @Embedded
    private List<Fichas> fichasList;


    public List<Fichas> getFichasList() {
        return fichasList;
    }

    public void setFichasList(List<Fichas> fichasList) {
        this.fichasList = fichasList;
    }

    public List<Especie> getEspecieList() {
        return especieList;
    }

    public void setEspecieList(List<Especie> especieList) {
        this.especieList = especieList;
    }

    public List<Variedad> getVariedadList() {
        return variedadList;
    }

    public void setVariedadList(List<Variedad> variedadList) {
        this.variedadList = variedadList;
    }

    public List<Region> getRegionList() {
        return regionList;
    }

    public void setRegionList(List<Region> regionList) {
        this.regionList = regionList;
    }

    public List<Comuna> getComunaList() {
        return comunaList;
    }

    public void setComunaList(List<Comuna> comunaList) {
        this.comunaList = comunaList;
    }

    public List<Provincia> getProvinciaList() {
        return provinciaList;
    }

    public void setProvinciaList(List<Provincia> provinciaList) {
        this.provinciaList = provinciaList;
    }

    public List<AnexoContrato> getAnexoContratoList() {
        return anexoContratoList;
    }

    public void setAnexoContratoList(List<AnexoContrato> anexoContratoList) {
        this.anexoContratoList = anexoContratoList;
    }

    public List<Agricultor> getAgricultorList() {
        return agricultorList;
    }

    public void setAgricultorList(List<Agricultor> agricultorList) {
        this.agricultorList = agricultorList;
    }

    public List<Visitas> getVisitasList() {
        return visitasList;
    }

    public void setVisitasList(List<Visitas> visitasList) {
        this.visitasList = visitasList;
    }

    public List<detalle_visita_prop> getDetalle_visita_props() {
        return detalle_visita_props;
    }

    public void setDetalle_visita_props(List<detalle_visita_prop> detalle_visita_props) {
        this.detalle_visita_props = detalle_visita_props;
    }

    public List<CropRotation> getCropRotations() {
        return cropRotations;
    }

    public void setCropRotations(List<CropRotation> cropRotations) {
        this.cropRotations = cropRotations;
    }

    public List<pro_cli_mat> getPro_cli_matList() {
        return pro_cli_matList;
    }

    public void setPro_cli_matList(List<pro_cli_mat> pro_cli_matList) {
        this.pro_cli_matList = pro_cli_matList;
    }

    public List<Temporada> getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(List<Temporada> temporadas) {
        this.temporadas = temporadas;
    }
}
