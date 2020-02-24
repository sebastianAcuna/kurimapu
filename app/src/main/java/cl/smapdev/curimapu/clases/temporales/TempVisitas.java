package cl.smapdev.curimapu.clases.temporales;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "temp_visitas")
public class TempVisitas {

    @PrimaryKey(autoGenerate = true)
    private int id_temp_visita;
    private int id_anexo_temp_visita;


    private int phenological_state_temp_visita;
    private int growth_status_temp_visita;
    private int weed_state_temp_visita;
    private int phytosanitary_state_temp_visita;
    private int harvest_temp_visita;
    private int overall_status_temp_visita;
    private int humidity_floor_temp_visita;
    private String observation_temp_visita;
    private String recomendation_temp_visita;


    private int action_temp_visita; /* 0 insert ; 1 update; 2 finished */


    public int getAction_temp_visita() {
        return action_temp_visita;
    }

    public void setAction_temp_visita(int action_temp_visita) {
        this.action_temp_visita = action_temp_visita;
    }

    private int etapa_temp_visitas;


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

    public int getId_anexo_temp_visita() {
        return id_anexo_temp_visita;
    }

    public void setId_anexo_temp_visita(int id_anexo_temp_visita) {
        this.id_anexo_temp_visita = id_anexo_temp_visita;
    }

    public int getPhenological_state_temp_visita() {
        return phenological_state_temp_visita;
    }

    public void setPhenological_state_temp_visita(int phenological_state_temp_visita) {
        this.phenological_state_temp_visita = phenological_state_temp_visita;
    }

    public int getGrowth_status_temp_visita() {
        return growth_status_temp_visita;
    }

    public void setGrowth_status_temp_visita(int growth_status_temp_visita) {
        this.growth_status_temp_visita = growth_status_temp_visita;
    }

    public int getWeed_state_temp_visita() {
        return weed_state_temp_visita;
    }

    public void setWeed_state_temp_visita(int weed_state_temp_visita) {
        this.weed_state_temp_visita = weed_state_temp_visita;
    }

    public int getPhytosanitary_state_temp_visita() {
        return phytosanitary_state_temp_visita;
    }

    public void setPhytosanitary_state_temp_visita(int phytosanitary_state_temp_visita) {
        this.phytosanitary_state_temp_visita = phytosanitary_state_temp_visita;
    }

    public int getHarvest_temp_visita() {
        return harvest_temp_visita;
    }

    public void setHarvest_temp_visita(int harvest_temp_visita) {
        this.harvest_temp_visita = harvest_temp_visita;
    }

    public int getOverall_status_temp_visita() {
        return overall_status_temp_visita;
    }

    public void setOverall_status_temp_visita(int overall_status_temp_visita) {
        this.overall_status_temp_visita = overall_status_temp_visita;
    }

    public int getHumidity_floor_temp_visita() {
        return humidity_floor_temp_visita;
    }

    public void setHumidity_floor_temp_visita(int humidity_floor_temp_visita) {
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
