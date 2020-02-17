package cl.smapdev.curimapu.clases.temporales;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "temp_sowing")
public class TempSowing {

    @PrimaryKey(autoGenerate = true)
    private int id_temp_sowing;
    private int id_anexo_temp_sowing;

    /* AVISO DE SIEMBRA SAG*/
    private String sag_planting_temp_sowing;

    /* ANALISIS DE SUELO */
    private String delivered_temp_sowing;
    private String type_of_mixture_applied_temp_sowing;
    private String amount_applied_temp_sowing;

    /* ISOLATION */
    private int meters_isoliation_temp_sowing;

    /*TODO NO ESTA EN TODOS LOS FORM VISIT*/
    /*NEAR FIELDS*/
    private String south_temp_sowing;
    private String north_temp_sowing;
    private String east_temp_sowing;
    private String west_temp_sowing;



    /* LINES */
    private String female_lines_temp_sowing;

    /* SOWING LOT */
    private String female_sowing_lot_temp_sowing;
    private double real_sowing_female_temp_sowing;

    /* SOWING DATE START */
    private String female_sowing_date_start_temp_sowing;

    /* SOWING DATE END */
    private String female_sowing_date_end_temp_sowing;


    /* SOWING SEED / METER */
    private double sowing_seed_meter_temp_sowing;

    /* ROW DISTANCE (M) */
    private double row_distance_temp_sowing;


    /* SPREAD UREA (N) */
    private String dose_1_temp_sowing;
    private String date_1_temp_sowing;
    private String dose_2_temp_sowing;
    private String date_2_temp_sowing;

    /* HERBICIDE SPRAYING DATE */

    private String name_1_herb_temp_sowing;
    private String date_1_herb_temp_sowing;
    private String name_2_herb_temp_sowing;
    private String date_2_herb_temp_sowing;
    private String name_3_herb_temp_sowing;
    private String date_3_herb_temp_sowing;

    /* PRE EMERGENCE HERBICIDE */
    private String date_pre_emergence_temp_sowing;
    private String dose_pre_emergence_temp_sowing;
    private double water_pre_emergence_temp_sowing;

    /* PLANT / M*/
    private double plant_m_temp_sowing;

    /* POPULATION (PLANTS / HA) */
    private double population_plants_ha_temp_sowing;


    /*TODO ESTO SERA UNA LISTA O 5 CAMPOS EN UNA TABLA */
    /* BASTA SPRAYING DATE */
    private String basta_spray_1_temp_sowing;
    private String basta_spray_2_temp_sowing;
    private String basta_spray_3_temp_sowing;
    private String basta_spray_4_temp_sowing;
    private String basta_splat_4_ha_temp_sowing;


    /*BRUCHUS PISORUM L. AND SCLEROTINIA PREVENTIVE APPLICATION*/

    private String date_nombre_largo_temp_sowing;
    private String product_nombre_largo_temp_sowing;
    private String dose_nombre_largo_temp_sowing;

    /* FOLIAR APPLICATION */
    private String foliar_temp_sowing;
    private String dose_foliar_temp_sowing;
    private String date_foliar_temp_sowing;


    public int getId_temp_sowing() {
        return id_temp_sowing;
    }

    public void setId_temp_sowing(int id_temp_sowing) {
        this.id_temp_sowing = id_temp_sowing;
    }

    public int getId_anexo_temp_sowing() {
        return id_anexo_temp_sowing;
    }

    public void setId_anexo_temp_sowing(int id_anexo_temp_sowing) {
        this.id_anexo_temp_sowing = id_anexo_temp_sowing;
    }

    public String getSag_planting_temp_sowing() {
        return sag_planting_temp_sowing;
    }

    public void setSag_planting_temp_sowing(String sag_planting_temp_sowing) {
        this.sag_planting_temp_sowing = sag_planting_temp_sowing;
    }

    public String getDelivered_temp_sowing() {
        return delivered_temp_sowing;
    }

    public void setDelivered_temp_sowing(String delivered_temp_sowing) {
        this.delivered_temp_sowing = delivered_temp_sowing;
    }

    public String getType_of_mixture_applied_temp_sowing() {
        return type_of_mixture_applied_temp_sowing;
    }

    public void setType_of_mixture_applied_temp_sowing(String type_of_mixture_applied_temp_sowing) {
        this.type_of_mixture_applied_temp_sowing = type_of_mixture_applied_temp_sowing;
    }

    public String getAmount_applied_temp_sowing() {
        return amount_applied_temp_sowing;
    }

    public void setAmount_applied_temp_sowing(String amount_applied_temp_sowing) {
        this.amount_applied_temp_sowing = amount_applied_temp_sowing;
    }

    public int getMeters_isoliation_temp_sowing() {
        return meters_isoliation_temp_sowing;
    }

    public void setMeters_isoliation_temp_sowing(int meters_isoliation_temp_sowing) {
        this.meters_isoliation_temp_sowing = meters_isoliation_temp_sowing;
    }

    public String getSouth_temp_sowing() {
        return south_temp_sowing;
    }

    public void setSouth_temp_sowing(String south_temp_sowing) {
        this.south_temp_sowing = south_temp_sowing;
    }

    public String getNorth_temp_sowing() {
        return north_temp_sowing;
    }

    public void setNorth_temp_sowing(String north_temp_sowing) {
        this.north_temp_sowing = north_temp_sowing;
    }

    public String getEast_temp_sowing() {
        return east_temp_sowing;
    }

    public void setEast_temp_sowing(String east_temp_sowing) {
        this.east_temp_sowing = east_temp_sowing;
    }

    public String getWest_temp_sowing() {
        return west_temp_sowing;
    }

    public void setWest_temp_sowing(String west_temp_sowing) {
        this.west_temp_sowing = west_temp_sowing;
    }

    public String getFemale_lines_temp_sowing() {
        return female_lines_temp_sowing;
    }

    public void setFemale_lines_temp_sowing(String female_lines_temp_sowing) {
        this.female_lines_temp_sowing = female_lines_temp_sowing;
    }

    public String getFemale_sowing_lot_temp_sowing() {
        return female_sowing_lot_temp_sowing;
    }

    public void setFemale_sowing_lot_temp_sowing(String female_sowing_lot_temp_sowing) {
        this.female_sowing_lot_temp_sowing = female_sowing_lot_temp_sowing;
    }

    public double getReal_sowing_female_temp_sowing() {
        return real_sowing_female_temp_sowing;
    }

    public void setReal_sowing_female_temp_sowing(double real_sowing_female_temp_sowing) {
        this.real_sowing_female_temp_sowing = real_sowing_female_temp_sowing;
    }

    public String getFemale_sowing_date_start_temp_sowing() {
        return female_sowing_date_start_temp_sowing;
    }

    public void setFemale_sowing_date_start_temp_sowing(String female_sowing_date_start_temp_sowing) {
        this.female_sowing_date_start_temp_sowing = female_sowing_date_start_temp_sowing;
    }

    public String getFemale_sowing_date_end_temp_sowing() {
        return female_sowing_date_end_temp_sowing;
    }

    public void setFemale_sowing_date_end_temp_sowing(String female_sowing_date_end_temp_sowing) {
        this.female_sowing_date_end_temp_sowing = female_sowing_date_end_temp_sowing;
    }

    public double getSowing_seed_meter_temp_sowing() {
        return sowing_seed_meter_temp_sowing;
    }

    public void setSowing_seed_meter_temp_sowing(double sowing_seed_meter_temp_sowing) {
        this.sowing_seed_meter_temp_sowing = sowing_seed_meter_temp_sowing;
    }

    public double getRow_distance_temp_sowing() {
        return row_distance_temp_sowing;
    }

    public void setRow_distance_temp_sowing(double row_distance_temp_sowing) {
        this.row_distance_temp_sowing = row_distance_temp_sowing;
    }

    public String getDose_1_temp_sowing() {
        return dose_1_temp_sowing;
    }

    public void setDose_1_temp_sowing(String dose_1_temp_sowing) {
        this.dose_1_temp_sowing = dose_1_temp_sowing;
    }

    public String getDate_1_temp_sowing() {
        return date_1_temp_sowing;
    }

    public void setDate_1_temp_sowing(String date_1_temp_sowing) {
        this.date_1_temp_sowing = date_1_temp_sowing;
    }

    public String getDose_2_temp_sowing() {
        return dose_2_temp_sowing;
    }

    public void setDose_2_temp_sowing(String dose_2_temp_sowing) {
        this.dose_2_temp_sowing = dose_2_temp_sowing;
    }

    public String getDate_2_temp_sowing() {
        return date_2_temp_sowing;
    }

    public void setDate_2_temp_sowing(String date_2_temp_sowing) {
        this.date_2_temp_sowing = date_2_temp_sowing;
    }

    public String getName_1_herb_temp_sowing() {
        return name_1_herb_temp_sowing;
    }

    public void setName_1_herb_temp_sowing(String name_1_herb_temp_sowing) {
        this.name_1_herb_temp_sowing = name_1_herb_temp_sowing;
    }

    public String getDate_1_herb_temp_sowing() {
        return date_1_herb_temp_sowing;
    }

    public void setDate_1_herb_temp_sowing(String date_1_herb_temp_sowing) {
        this.date_1_herb_temp_sowing = date_1_herb_temp_sowing;
    }

    public String getName_2_herb_temp_sowing() {
        return name_2_herb_temp_sowing;
    }

    public void setName_2_herb_temp_sowing(String name_2_herb_temp_sowing) {
        this.name_2_herb_temp_sowing = name_2_herb_temp_sowing;
    }

    public String getDate_2_herb_temp_sowing() {
        return date_2_herb_temp_sowing;
    }

    public void setDate_2_herb_temp_sowing(String date_2_herb_temp_sowing) {
        this.date_2_herb_temp_sowing = date_2_herb_temp_sowing;
    }

    public String getName_3_herb_temp_sowing() {
        return name_3_herb_temp_sowing;
    }

    public void setName_3_herb_temp_sowing(String name_3_herb_temp_sowing) {
        this.name_3_herb_temp_sowing = name_3_herb_temp_sowing;
    }

    public String getDate_3_herb_temp_sowing() {
        return date_3_herb_temp_sowing;
    }

    public void setDate_3_herb_temp_sowing(String date_3_herb_temp_sowing) {
        this.date_3_herb_temp_sowing = date_3_herb_temp_sowing;
    }

    public String getDate_pre_emergence_temp_sowing() {
        return date_pre_emergence_temp_sowing;
    }

    public void setDate_pre_emergence_temp_sowing(String date_pre_emergence_temp_sowing) {
        this.date_pre_emergence_temp_sowing = date_pre_emergence_temp_sowing;
    }

    public String getDose_pre_emergence_temp_sowing() {
        return dose_pre_emergence_temp_sowing;
    }

    public void setDose_pre_emergence_temp_sowing(String dose_pre_emergence_temp_sowing) {
        this.dose_pre_emergence_temp_sowing = dose_pre_emergence_temp_sowing;
    }

    public double getWater_pre_emergence_temp_sowing() {
        return water_pre_emergence_temp_sowing;
    }

    public void setWater_pre_emergence_temp_sowing(double water_pre_emergence_temp_sowing) {
        this.water_pre_emergence_temp_sowing = water_pre_emergence_temp_sowing;
    }

    public double getPlant_m_temp_sowing() {
        return plant_m_temp_sowing;
    }

    public void setPlant_m_temp_sowing(double plant_m_temp_sowing) {
        this.plant_m_temp_sowing = plant_m_temp_sowing;
    }

    public double getPopulation_plants_ha_temp_sowing() {
        return population_plants_ha_temp_sowing;
    }

    public void setPopulation_plants_ha_temp_sowing(double population_plants_ha_temp_sowing) {
        this.population_plants_ha_temp_sowing = population_plants_ha_temp_sowing;
    }

    public String getBasta_spray_1_temp_sowing() {
        return basta_spray_1_temp_sowing;
    }

    public void setBasta_spray_1_temp_sowing(String basta_spray_1_temp_sowing) {
        this.basta_spray_1_temp_sowing = basta_spray_1_temp_sowing;
    }

    public String getBasta_spray_2_temp_sowing() {
        return basta_spray_2_temp_sowing;
    }

    public void setBasta_spray_2_temp_sowing(String basta_spray_2_temp_sowing) {
        this.basta_spray_2_temp_sowing = basta_spray_2_temp_sowing;
    }

    public String getBasta_spray_3_temp_sowing() {
        return basta_spray_3_temp_sowing;
    }

    public void setBasta_spray_3_temp_sowing(String basta_spray_3_temp_sowing) {
        this.basta_spray_3_temp_sowing = basta_spray_3_temp_sowing;
    }

    public String getBasta_spray_4_temp_sowing() {
        return basta_spray_4_temp_sowing;
    }

    public void setBasta_spray_4_temp_sowing(String basta_spray_4_temp_sowing) {
        this.basta_spray_4_temp_sowing = basta_spray_4_temp_sowing;
    }

    public String getBasta_splat_4_ha_temp_sowing() {
        return basta_splat_4_ha_temp_sowing;
    }

    public void setBasta_splat_4_ha_temp_sowing(String basta_splat_4_ha_temp_sowing) {
        this.basta_splat_4_ha_temp_sowing = basta_splat_4_ha_temp_sowing;
    }

    public String getDate_nombre_largo_temp_sowing() {
        return date_nombre_largo_temp_sowing;
    }

    public void setDate_nombre_largo_temp_sowing(String date_nombre_largo_temp_sowing) {
        this.date_nombre_largo_temp_sowing = date_nombre_largo_temp_sowing;
    }

    public String getProduct_nombre_largo_temp_sowing() {
        return product_nombre_largo_temp_sowing;
    }

    public void setProduct_nombre_largo_temp_sowing(String product_nombre_largo_temp_sowing) {
        this.product_nombre_largo_temp_sowing = product_nombre_largo_temp_sowing;
    }

    public String getDose_nombre_largo_temp_sowing() {
        return dose_nombre_largo_temp_sowing;
    }

    public void setDose_nombre_largo_temp_sowing(String dose_nombre_largo_temp_sowing) {
        this.dose_nombre_largo_temp_sowing = dose_nombre_largo_temp_sowing;
    }

    public String getFoliar_temp_sowing() {
        return foliar_temp_sowing;
    }

    public void setFoliar_temp_sowing(String foliar_temp_sowing) {
        this.foliar_temp_sowing = foliar_temp_sowing;
    }

    public String getDose_foliar_temp_sowing() {
        return dose_foliar_temp_sowing;
    }

    public void setDose_foliar_temp_sowing(String dose_foliar_temp_sowing) {
        this.dose_foliar_temp_sowing = dose_foliar_temp_sowing;
    }

    public String getDate_foliar_temp_sowing() {
        return date_foliar_temp_sowing;
    }

    public void setDate_foliar_temp_sowing(String date_foliar_temp_sowing) {
        this.date_foliar_temp_sowing = date_foliar_temp_sowing;
    }
}
