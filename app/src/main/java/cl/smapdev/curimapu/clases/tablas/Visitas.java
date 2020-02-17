package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "visitas")
public class Visitas {

    @PrimaryKey(autoGenerate = true)
    private int id_visita;
    private int id_anexo_visita;


    private int phenological_state_visita;
    private int growth_status_visita;
    private int weed_state_visita;
    private int phytosanitary_state_visita;
    private int harvest_visita;
    private int overall_status_visita;
    private int humidity_floor_visita;
    private String observation_visita;
    private String recomendation_visita;


    public int getId_visita() {
        return id_visita;
    }

    public void setId_visita(int id_visita) {
        this.id_visita = id_visita;
    }

    public int getId_anexo_visita() {
        return id_anexo_visita;
    }

    public void setId_anexo_visita(int id_anexo_visita) {
        this.id_anexo_visita = id_anexo_visita;
    }

    public int getPhenological_state_visita() {
        return phenological_state_visita;
    }

    public void setPhenological_state_visita(int phenological_state_visita) {
        this.phenological_state_visita = phenological_state_visita;
    }

    public int getGrowth_status_visita() {
        return growth_status_visita;
    }

    public void setGrowth_status_visita(int growth_status_visita) {
        this.growth_status_visita = growth_status_visita;
    }

    public int getWeed_state_visita() {
        return weed_state_visita;
    }

    public void setWeed_state_visita(int weed_state_visita) {
        this.weed_state_visita = weed_state_visita;
    }

    public int getPhytosanitary_state_visita() {
        return phytosanitary_state_visita;
    }

    public void setPhytosanitary_state_visita(int phytosanitary_state_visita) {
        this.phytosanitary_state_visita = phytosanitary_state_visita;
    }

    public int getHarvest_visita() {
        return harvest_visita;
    }

    public void setHarvest_visita(int harvest_visita) {
        this.harvest_visita = harvest_visita;
    }

    public int getOverall_status_visita() {
        return overall_status_visita;
    }

    public void setOverall_status_visita(int overall_status_visita) {
        this.overall_status_visita = overall_status_visita;
    }

    public int getHumidity_floor_visita() {
        return humidity_floor_visita;
    }

    public void setHumidity_floor_visita(int humidity_floor_visita) {
        this.humidity_floor_visita = humidity_floor_visita;
    }

    public String getObservation_visita() {
        return observation_visita;
    }

    public void setObservation_visita(String observation_visita) {
        this.observation_visita = observation_visita;
    }

    public String getRecomendation_visita() {
        return recomendation_visita;
    }

    public void setRecomendation_visita(String recomendation_visita) {
        this.recomendation_visita = recomendation_visita;
    }
}
