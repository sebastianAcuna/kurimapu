package cl.smapdev.curimapu.clases.relaciones;

import androidx.room.Embedded;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.AgrPredTemp;
import cl.smapdev.curimapu.clases.tablas.Agricultor;
import cl.smapdev.curimapu.clases.tablas.AnexoContrato;
import cl.smapdev.curimapu.clases.tablas.AnexoCorreoFechas;
import cl.smapdev.curimapu.clases.tablas.CardViewsResumen;
import cl.smapdev.curimapu.clases.tablas.CheckListSiembra;
import cl.smapdev.curimapu.clases.tablas.Clientes;
import cl.smapdev.curimapu.clases.tablas.Comuna;
import cl.smapdev.curimapu.clases.tablas.CropRotation;
import cl.smapdev.curimapu.clases.tablas.Especie;
import cl.smapdev.curimapu.clases.tablas.Evaluaciones;
import cl.smapdev.curimapu.clases.tablas.FichaMaquinaria;
import cl.smapdev.curimapu.clases.tablas.Fichas;
import cl.smapdev.curimapu.clases.tablas.FichasNew;
import cl.smapdev.curimapu.clases.tablas.Lotes;
import cl.smapdev.curimapu.clases.tablas.Maquinaria;
import cl.smapdev.curimapu.clases.tablas.Predios;
import cl.smapdev.curimapu.clases.tablas.Provincia;
import cl.smapdev.curimapu.clases.tablas.Region;
import cl.smapdev.curimapu.clases.tablas.Temporada;
import cl.smapdev.curimapu.clases.tablas.TipoRiego;
import cl.smapdev.curimapu.clases.tablas.TipoSuelo;
import cl.smapdev.curimapu.clases.tablas.TipoTenenciaMaquinaria;
import cl.smapdev.curimapu.clases.tablas.TipoTenenciaTerreno;
import cl.smapdev.curimapu.clases.tablas.UnidadMedida;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.tablas.Variedad;
import cl.smapdev.curimapu.clases.tablas.Visitas;
import cl.smapdev.curimapu.clases.tablas.cli_pcm;
import cl.smapdev.curimapu.clases.tablas.detalle_visita_prop;
import cl.smapdev.curimapu.clases.tablas.pro_cli_mat;
import cl.smapdev.curimapu.clases.tablas.quotation;

public class GsonDescargas {


    @SerializedName("id")
    private int id_dispo;

    @SerializedName("array_detalle_prop")
    @Embedded
    private List<pro_cli_mat> pro_cli_matList;



    @SerializedName("array_check_list_siembra")
    @Embedded
    private List<CheckListSiembra> checkListSiembras;


    @SerializedName("array_check_list_capacitacion_siembra")
    @Embedded
    private List<CheckListCapCompleto> checkListCapCompletos;

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
    private List<FichasNew> fichasList;

    @SerializedName("array_um")
    @Embedded
    private List<UnidadMedida> unidadMedidas;

    @SerializedName("array_usuarios")
    @Embedded
    private List<Usuario> usuarios;

    @SerializedName("problemas")
    @Embedded
    private List<Respuesta> respuestas;

    @SerializedName("array_predios")
    @Embedded
    private List<Predios> predios;

    @SerializedName("evaluaciones")
    @Embedded
    private List<Evaluaciones> evaluaciones;


    @SerializedName("array_lotes")
    @Embedded
    private List<Lotes> lotes;

    @SerializedName("array_tipo_riego")
    @Embedded
    private List<TipoRiego> tipoRiegos;

    @SerializedName("array_tipo_suelo")
    @Embedded
    private List<TipoSuelo> tipoSuelos;

    @SerializedName("array_maquinaria")
    @Embedded
    private List<Maquinaria> maquinarias;

    @SerializedName("array_tipo_maquinaria")
    @Embedded
    private List<TipoTenenciaMaquinaria> tipoTenenciaMaquinarias;

    @SerializedName("array_tipo_tenencia_terreno")
    @Embedded
    private List<TipoTenenciaTerreno> tipoTenenciaTerrenos;

    @SerializedName("array_ficha_maquinaria")
    @Embedded
    private List<FichaMaquinaria> fichaMaquinarias;

    @SerializedName("array_clientes")
    @Embedded
    private List<Clientes> clientes;


    @SerializedName("array_quotation")
    @Embedded
    private List<quotation> quotations;


    @SerializedName("array_card_views")
    @Embedded
    private List<CardViewsResumen> cardViewsResumen;

    @SerializedName("array_pcm")
    @Embedded
    private List<cli_pcm> cli_pcms;


    @SerializedName("array_pred_agr_temp")
    @Embedded
    private List<AgrPredTemp> pred_agr_temp;


    @SerializedName("array_fechas_anexos")
    @Embedded
    private List<AnexoCorreoFechas> array_fechas_anexos;


    public List<CheckListCapCompleto> getCheckListCapCompletos() {
        return checkListCapCompletos;
    }

    public void setCheckListCapCompletos(List<CheckListCapCompleto> checkListCapCompletos) {
        this.checkListCapCompletos = checkListCapCompletos;
    }

    public List<CheckListSiembra> getCheckListSiembras() {
        return checkListSiembras;
    }

    public void setCheckListSiembras(List<CheckListSiembra> checkListSiembras) {
        this.checkListSiembras = checkListSiembras;
    }

    public List<Evaluaciones> getEvaluaciones() {
        return evaluaciones;
    }

    public void setEvaluaciones(List<Evaluaciones> evaluaciones) {
        this.evaluaciones = evaluaciones;
    }

    public List<AnexoCorreoFechas> getArray_fechas_anexos() {
        return array_fechas_anexos;
    }

    public void setArray_fechas_anexos(List<AnexoCorreoFechas> array_fechas_anexos) {
        this.array_fechas_anexos = array_fechas_anexos;
    }

    public List<AgrPredTemp> getPred_agr_temp() {
        return pred_agr_temp;
    }

    public void setPred_agr_temp(List<AgrPredTemp> pred_agr_temp) {
        this.pred_agr_temp = pred_agr_temp;
    }

    public List<quotation> getQuotations() {
        return quotations;
    }

    public void setQuotations(List<quotation> quotations) {
        this.quotations = quotations;
    }

    public List<cli_pcm> getCli_pcms() {
        return cli_pcms;
    }

    public void setCli_pcms(List<cli_pcm> cli_pcms) {
        this.cli_pcms = cli_pcms;
    }

    public List<CardViewsResumen> getCardViewsResumen() {
        return cardViewsResumen;
    }

    public void setCardViewsResumen(List<CardViewsResumen> cardViewsResumen) {
        this.cardViewsResumen = cardViewsResumen;
    }

    public List<Clientes> getClientes() {
        return clientes;
    }

    public void setClientes(List<Clientes> clientes) {
        this.clientes = clientes;
    }

    public List<Predios> getPredios() {
        return predios;
    }

    public void setPredios(List<Predios> predios) {
        this.predios = predios;
    }

    public List<Lotes> getLotes() {
        return lotes;
    }

    public void setLotes(List<Lotes> lotes) {
        this.lotes = lotes;
    }

    public List<TipoRiego> getTipoRiegos() {
        return tipoRiegos;
    }

    public void setTipoRiegos(List<TipoRiego> tipoRiegos) {
        this.tipoRiegos = tipoRiegos;
    }

    public List<TipoSuelo> getTipoSuelos() {
        return tipoSuelos;
    }

    public void setTipoSuelos(List<TipoSuelo> tipoSuelos) {
        this.tipoSuelos = tipoSuelos;
    }

    public List<Maquinaria> getMaquinarias() {
        return maquinarias;
    }

    public void setMaquinarias(List<Maquinaria> maquinarias) {
        this.maquinarias = maquinarias;
    }

    public List<TipoTenenciaMaquinaria> getTipoTenenciaMaquinarias() {
        return tipoTenenciaMaquinarias;
    }

    public void setTipoTenenciaMaquinarias(List<TipoTenenciaMaquinaria> tipoTenenciaMaquinarias) {
        this.tipoTenenciaMaquinarias = tipoTenenciaMaquinarias;
    }

    public List<TipoTenenciaTerreno> getTipoTenenciaTerrenos() {
        return tipoTenenciaTerrenos;
    }

    public void setTipoTenenciaTerrenos(List<TipoTenenciaTerreno> tipoTenenciaTerrenos) {
        this.tipoTenenciaTerrenos = tipoTenenciaTerrenos;
    }

    public List<FichaMaquinaria> getFichaMaquinarias() {
        return fichaMaquinarias;
    }

    public void setFichaMaquinarias(List<FichaMaquinaria> fichaMaquinarias) {
        this.fichaMaquinarias = fichaMaquinarias;
    }

    public int getId_dispo() {
        return id_dispo;
    }

    public void setId_dispo(int id_dispo) {
        this.id_dispo = id_dispo;
    }

    public List<Respuesta> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<UnidadMedida> getUnidadMedidas() {
        return unidadMedidas;
    }

    public void setUnidadMedidas(List<UnidadMedida> unidadMedidas) {
        this.unidadMedidas = unidadMedidas;
    }

    public List<FichasNew> getFichasList() {
        return fichasList;
    }

    public void setFichasList(List<FichasNew> fichasList) {
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


    public static int[] sizeELements(GsonDescargas gs){

        int[] res = {0,0};

        if (gs != null) {

            if (gs.getAgricultorList() != null && gs.getAgricultorList().size() > 0) res[0]+=gs.getAgricultorList().size(); res[1]++;
            if (gs.getUsuarios() != null && gs.getUsuarios().size() > 0) res[0]+=gs.getUsuarios().size(); res[1]++;
            if (gs.getPro_cli_matList() != null && gs.getPro_cli_matList().size() > 0) res[0]+=gs.getPro_cli_matList().size(); res[1]++;
            if (gs.getUnidadMedidas() != null && gs.getUnidadMedidas().size() > 0) res[0]+=gs.getUnidadMedidas().size(); res[1]++;
            if (gs.getFichasList() != null && gs.getFichasList().size() > 0) res[0]+=gs.getFichasList().size(); res[1]++;
            if (gs.getVariedadList() != null && gs.getVariedadList().size() > 0) res[0]+=gs.getVariedadList().size(); res[1]++;
            if (gs.getEspecieList() != null && gs.getEspecieList().size() > 0) res[0]+=gs.getEspecieList().size(); res[1]++;
            if (gs.getAnexoContratoList() != null && gs.getAnexoContratoList().size() > 0) res[0]+=gs.getAnexoContratoList().size(); res[1]++;
            if (gs.getComunaList() != null && gs.getComunaList().size() > 0) res[0]+=gs.getComunaList().size(); res[1]++;
            if (gs.getProvinciaList() != null && gs.getProvinciaList().size() > 0) res[0]+=gs.getProvinciaList().size(); res[1]++;
            if (gs.getRegionList() != null && gs.getRegionList().size() > 0) res[0]+=gs.getRegionList().size(); res[1]++;
            if (gs.getVisitasList() != null && gs.getVisitasList().size() > 0) res[0]+=gs.getVisitasList().size(); res[1]++;
            if (gs.getDetalle_visita_props() != null && gs.getDetalle_visita_props().size() > 0) res[0]+=gs.getDetalle_visita_props().size(); res[1]++;
            if (gs.getCropRotations() != null && gs.getCropRotations().size() > 0) res[0]+=gs.getCropRotations().size(); res[1]++;
            if (gs.getTemporadas() != null && gs.getTemporadas().size() > 0) res[0]+=gs.getTemporadas().size(); res[1]++;

        }
        return res;

    }
}
