package cl.smapdev.curimapu.clases.temporales;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "temp_visitas")
public class TempVisitas {

    @PrimaryKey(autoGenerate = true)
    private int id_temp_visita;
    private String id_anexo_temp_visita;


    private String phenological_state_temp_visita;
    private String growth_status_temp_visita;
    private String weed_state_temp_visita;
    private String phytosanitary_state_temp_visita;
    private String harvest_temp_visita;
    private String overall_status_temp_visita;
    private String humidity_floor_temp_visita;
    private String observation_temp_visita;
    private String recomendation_temp_visita;

    private String obs_fito;
    private String obs_creci;
    private String obs_maleza;
    private String obs_cosecha;
    private String obs_overall;
    private String obs_humedad;

    private double percent_humedad;


    private int action_temp_visita; /* 0 insert ; 1 update; 2 finished */
    private int etapa_temp_visitas;




    // 05-06-2020 se agregó 2 columnas nuevas a visita y visita temp para el tema de las fotos
    private int id_visita_local;
    private int id_dispo;


    private float evaluacion;
    private String comentario_evaluacion;

    private  int id_evaluacion;

    @SerializedName("fecha_estimada_cosecha")
    @Expose
    private String fecha_estimada_cosecha;



    @SerializedName("fecha_estimada_arranca")
    @Expose
    private String fecha_estimada_arranca;


    public String getFecha_estimada_cosecha() {
        return fecha_estimada_cosecha;
    }

    public void setFecha_estimada_cosecha(String fecha_estimada_cosecha) {
        this.fecha_estimada_cosecha = fecha_estimada_cosecha;
    }

    public String getFecha_estimada_arranca() {
        return fecha_estimada_arranca;
    }

    public void setFecha_estimada_arranca(String fecha_estimada_arranca) {
        this.fecha_estimada_arranca = fecha_estimada_arranca;
    }

    public int getId_evaluacion() {
        return id_evaluacion;
    }

    public void setId_evaluacion(int id_evaluacion) {
        this.id_evaluacion = id_evaluacion;
    }


    public float getEvaluacion() {
        return evaluacion;
    }
    public void setEvaluacion(float evaluacion) {
        this.evaluacion = evaluacion;
    }

    public String getComentario_evaluacion() {
        return comentario_evaluacion;
    }

    public void setComentario_evaluacion(String comentario_evaluacion) {
        this.comentario_evaluacion = comentario_evaluacion;
    }

    public int getId_visita_local() {
        return id_visita_local;
    }
    public void setId_visita_local(int id_visita_local) {
        this.id_visita_local = id_visita_local;
    }

    public int getId_dispo() {
        return id_dispo;
    }

    public void setId_dispo(int id_dispo) {
        this.id_dispo = id_dispo;
    }

    public double getPercent_humedad() {
        return percent_humedad;
    }

    public void setPercent_humedad(double percent_humedad) {
        this.percent_humedad = percent_humedad;
    }

    public String getObs_fito() {
        return obs_fito;
    }

    public void setObs_fito(String obs_fito) {
        this.obs_fito = obs_fito;
    }

    public String getObs_creci() {
        return obs_creci;
    }

    public void setObs_creci(String obs_creci) {
        this.obs_creci = obs_creci;
    }

    public String getObs_maleza() {
        return obs_maleza;
    }

    public void setObs_maleza(String obs_maleza) {
        this.obs_maleza = obs_maleza;
    }

    public String getObs_cosecha() {
        return obs_cosecha;
    }

    public void setObs_cosecha(String obs_cosecha) {
        this.obs_cosecha = obs_cosecha;
    }

    public String getObs_overall() {
        return obs_overall;
    }

    public void setObs_overall(String obs_overall) {
        this.obs_overall = obs_overall;
    }

    public String getObs_humedad() {
        return obs_humedad;
    }

    public void setObs_humedad(String obs_humedad) {
        this.obs_humedad = obs_humedad;
    }

    public int getAction_temp_visita() {
        return action_temp_visita;
    }

    public void setAction_temp_visita(int action_temp_visita) {
        this.action_temp_visita = action_temp_visita;
    }




    public int getEtapa_temp_visitas() {
        return etapa_temp_visitas;
    }

    public void setEtapa_temp_visitas(int etapa_temp_visitas) {
        this.etapa_temp_visitas = etapa_temp_visitas;
    }

    public int getId_temp_visita() {
        return id_temp_visita;
    }

    public void setId_temp_visita(int id_temp_visita) {
        this.id_temp_visita = id_temp_visita;
    }

    public String getId_anexo_temp_visita() {
        return id_anexo_temp_visita;
    }

    public void setId_anexo_temp_visita(String id_anexo_temp_visita) {
        this.id_anexo_temp_visita = id_anexo_temp_visita;
    }

    public String getPhenological_state_temp_visita() {
        return phenological_state_temp_visita;
    }

    public void setPhenological_state_temp_visita(String phenological_state_temp_visita) {
        this.phenological_state_temp_visita = phenological_state_temp_visita;
    }

    public String getGrowth_status_temp_visita() {
        return growth_status_temp_visita;
    }

    public void setGrowth_status_temp_visita(String growth_status_temp_visita) {
        this.growth_status_temp_visita = growth_status_temp_visita;
    }

    public String getWeed_state_temp_visita() {
        return weed_state_temp_visita;
    }

    public void setWeed_state_temp_visita(String weed_state_temp_visita) {
        this.weed_state_temp_visita = weed_state_temp_visita;
    }

    public String getPhytosanitary_state_temp_visita() {
        return phytosanitary_state_temp_visita;
    }

    public void setPhytosanitary_state_temp_visita(String phytosanitary_state_temp_visita) {
        this.phytosanitary_state_temp_visita = phytosanitary_state_temp_visita;
    }

    public String getHarvest_temp_visita() {
        return harvest_temp_visita;
    }

    public void setHarvest_temp_visita(String harvest_temp_visita) {
        this.harvest_temp_visita = harvest_temp_visita;
    }

    public String getOverall_status_temp_visita() {
        return overall_status_temp_visita;
    }

    public void setOverall_status_temp_visita(String overall_status_temp_visita) {
        this.overall_status_temp_visita = overall_status_temp_visita;
    }

    public String getHumidity_floor_temp_visita() {
        return humidity_floor_temp_visita;
    }

    public void setHumidity_floor_temp_visita(String humidity_floor_temp_visita) {
        this.humidity_floor_temp_visita = humidity_floor_temp_visita;
    }

    public String getObservation_temp_visita() {
        return observation_temp_visita;
    }

    public void setObservation_temp_visita(String observation_temp_visita) {
        this.observation_temp_visita = observation_temp_visita;
    }

    public String getRecomendation_temp_visita() {
        return recomendation_temp_visita;
    }

    public void setRecomendation_temp_visita(String recomendation_temp_visita) {
        this.recomendation_temp_visita = recomendation_temp_visita;
    }
}
