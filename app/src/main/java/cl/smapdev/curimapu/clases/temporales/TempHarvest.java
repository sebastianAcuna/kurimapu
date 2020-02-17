package cl.smapdev.curimapu.clases.temporales;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "temp_harvest")
public class TempHarvest {

    @PrimaryKey(autoGenerate = true)
    private int id_temp_harvest;
    private int id_anexo_temp_harvest;



    /* DESSICANT APPLICATION */
    private String estimated_date_temp_harvest;
    private String real_date_temp_harvest;
    private String observation_dessicant_temp_harvest;

    /*SWATHING DATE*/
    private String swathing_date_temp_harvest;

    /*YIELD ESTIMATION PRE CLEAN*/
    private String kg_ha_yield_temp_harvest;
    private String observation_yield_temp_harvest;

    /* HARVEST ESTIMATION */
    private String date_harvest_estimation_temp_harvest;

    /* REAL HARVEST DATE */
    private String beginning_date_temp_harvest;
    private String end_date_temp_harvest;

    /* HARVEST MACHINE */
    private String owner_machine_temp_harvest;
    private String model_machine_temp_harvest;

    /* SEED HARVEST MOISTURE */
    private double porcent_temp_harvest;


    public int getId_temp_harvest() {
        return id_temp_harvest;
    }

    public void setId_temp_harvest(int id_temp_harvest) {
        this.id_temp_harvest = id_temp_harvest;
    }

    public int getId_anexo_temp_harvest() {
        return id_anexo_temp_harvest;
    }

    public void setId_anexo_temp_harvest(int id_anexo_temp_harvest) {
        this.id_anexo_temp_harvest = id_anexo_temp_harvest;
    }

    public String getEstimated_date_temp_harvest() {
        return estimated_date_temp_harvest;
    }

    public void setEstimated_date_temp_harvest(String estimated_date_temp_harvest) {
        this.estimated_date_temp_harvest = estimated_date_temp_harvest;
    }

    public String getReal_date_temp_harvest() {
        return real_date_temp_harvest;
    }

    public void setReal_date_temp_harvest(String real_date_temp_harvest) {
        this.real_date_temp_harvest = real_date_temp_harvest;
    }

    public String getObservation_dessicant_temp_harvest() {
        return observation_dessicant_temp_harvest;
    }

    public void setObservation_dessicant_temp_harvest(String observation_dessicant_temp_harvest) {
        this.observation_dessicant_temp_harvest = observation_dessicant_temp_harvest;
    }

    public String getSwathing_date_temp_harvest() {
        return swathing_date_temp_harvest;
    }

    public void setSwathing_date_temp_harvest(String swathing_date_temp_harvest) {
        this.swathing_date_temp_harvest = swathing_date_temp_harvest;
    }

    public String getKg_ha_yield_temp_harvest() {
        return kg_ha_yield_temp_harvest;
    }

    public void setKg_ha_yield_temp_harvest(String kg_ha_yield_temp_harvest) {
        this.kg_ha_yield_temp_harvest = kg_ha_yield_temp_harvest;
    }

    public String getObservation_yield_temp_harvest() {
        return observation_yield_temp_harvest;
    }

    public void setObservation_yield_temp_harvest(String observation_yield_temp_harvest) {
        this.observation_yield_temp_harvest = observation_yield_temp_harvest;
    }

    public String getDate_harvest_estimation_temp_harvest() {
        return date_harvest_estimation_temp_harvest;
    }

    public void setDate_harvest_estimation_temp_harvest(String date_harvest_estimation_temp_harvest) {
        this.date_harvest_estimation_temp_harvest = date_harvest_estimation_temp_harvest;
    }

    public String getBeginning_date_temp_harvest() {
        return beginning_date_temp_harvest;
    }

    public void setBeginning_date_temp_harvest(String beginning_date_temp_harvest) {
        this.beginning_date_temp_harvest = beginning_date_temp_harvest;
    }

    public String getEnd_date_temp_harvest() {
        return end_date_temp_harvest;
    }

    public void setEnd_date_temp_harvest(String end_date_temp_harvest) {
        this.end_date_temp_harvest = end_date_temp_harvest;
    }

    public String getOwner_machine_temp_harvest() {
        return owner_machine_temp_harvest;
    }

    public void setOwner_machine_temp_harvest(String owner_machine_temp_harvest) {
        this.owner_machine_temp_harvest = owner_machine_temp_harvest;
    }

    public String getModel_machine_temp_harvest() {
        return model_machine_temp_harvest;
    }

    public void setModel_machine_temp_harvest(String model_machine_temp_harvest) {
        this.model_machine_temp_harvest = model_machine_temp_harvest;
    }

    public double getPorcent_temp_harvest() {
        return porcent_temp_harvest;
    }

    public void setPorcent_temp_harvest(double porcent_temp_harvest) {
        this.porcent_temp_harvest = porcent_temp_harvest;
    }
}
