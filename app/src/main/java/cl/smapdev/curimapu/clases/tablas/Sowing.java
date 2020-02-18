package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sowing")
public class Sowing {

    @PrimaryKey(autoGenerate = true)
    private int id_sowing;
    private int id_anexo_sowing;

    private int id_visita_sowing;
    private int estado_server_sowing;



    /* AVISO DE SIEMBRA SAG*/
    private String sag_planting_sowing;

    /* ANALISIS DE SUELO */
    private String delivered_sowing;
    private String type_of_mixture_applied_sowing;
    private String amount_applied_sowing;

    /* ISOLATION */
    private int meters_isoliation_sowing;

    /*TODO NO ESTA EN TODOS LOS FORM VISIT*/
    /*NEAR FIELDS*/
    private String south_sowing;
    private String north_sowing;
    private String east_sowing;
    private String west_sowing;



    /* LINES */
    private String female_lines_sowing;

    /* SOWING LOT */
    private String female_sowing_lot_sowing;
    private double real_sowing_female_sowing;

    /* SOWING DATE START */
    private String female_sowing_date_start_sowing;

    /* SOWING DATE END */
    private String female_sowing_date_end_sowing;


    /* SOWING SEED / METER */
    private double sowing_seed_meter_sowing;

    /* ROW DISTANCE (M) */
    private double row_distance_sowing;


    /* SPREAD UREA (N) */
    private String dose_1_sowing;
    private String date_1_sowing;
    private String dose_2_sowing;
    private String date_2_sowing;

    /* HERBICIDE SPRAYING DATE */

    private String name_1_herb_sowing;
    private String date_1_herb_sowing;
    private String name_2_herb_sowing;
    private String date_2_herb_sowing;
    private String name_3_herb_sowing;
    private String date_3_herb_sowing;

    /* PRE EMERGENCE HERBICIDE */
    private String date_pre_emergence_sowing;
    private String dose_pre_emergence_sowing;
    private double water_pre_emergence_sowing;

    /* PLANT / M*/
    private double plant_m_sowing;

    /* POPULATION (PLANTS / HA) */
    private double population_plants_ha_sowing;


    /*TODO ESTO SERA UNA LISTA O 5 CAMPOS EN UNA TABLA */
    /* BASTA SPRAYING DATE */
    private String basta_spray_1_sowing;
    private String basta_spray_2_sowing;
    private String basta_spray_3_sowing;
    private String basta_spray_4_sowing;
    private String basta_splat_4_ha_sowing;


    /*BRUCHUS PISORUM L. AND SCLEROTINIA PREVENTIVE APPLICATION*/

    private String date_nombre_largo_sowing;
    private String product_nombre_largo_sowing;
    private String dose_nombre_largo_sowing;

    /* FOLIAR APPLICATION */
    private String foliar_sowing;
    private String dose_foliar_sowing;
    private String date_foliar_sowing;


    public int getId_sowing() {
        return id_sowing;
    }

    public void setId_sowing(int id_sowing) {
        this.id_sowing = id_sowing;
    }

    public int getId_anexo_sowing() {
        return id_anexo_sowing;
    }

    public void setId_anexo_sowing(int id_anexo_sowing) {
        this.id_anexo_sowing = id_anexo_sowing;
    }

    public int getId_visita_sowing() {
        return id_visita_sowing;
    }

    public void setId_visita_sowing(int id_visita_sowing) {
        this.id_visita_sowing = id_visita_sowing;
    }

    public int getEstado_server_sowing() {
        return estado_server_sowing;
    }

    public void setEstado_server_sowing(int estado_server_sowing) {
        this.estado_server_sowing = estado_server_sowing;
    }

    public String getSag_planting_sowing() {
        return sag_planting_sowing;
    }

    public void setSag_planting_sowing(String sag_planting_sowing) {
        this.sag_planting_sowing = sag_planting_sowing;
    }

    public String getDelivered_sowing() {
        return delivered_sowing;
    }

    public void setDelivered_sowing(String delivered_sowing) {
        this.delivered_sowing = delivered_sowing;
    }

    public String getType_of_mixture_applied_sowing() {
        return type_of_mixture_applied_sowing;
    }

    public void setType_of_mixture_applied_sowing(String type_of_mixture_applied_sowing) {
        this.type_of_mixture_applied_sowing = type_of_mixture_applied_sowing;
    }

    public String getAmount_applied_sowing() {
        return amount_applied_sowing;
    }

    public void setAmount_applied_sowing(String amount_applied_sowing) {
        this.amount_applied_sowing = amount_applied_sowing;
    }

    public int getMeters_isoliation_sowing() {
        return meters_isoliation_sowing;
    }

    public void setMeters_isoliation_sowing(int meters_isoliation_sowing) {
        this.meters_isoliation_sowing = meters_isoliation_sowing;
    }

    public String getSouth_sowing() {
        return south_sowing;
    }

    public void setSouth_sowing(String south_sowing) {
        this.south_sowing = south_sowing;
    }

    public String getNorth_sowing() {
        return north_sowing;
    }

    public void setNorth_sowing(String north_sowing) {
        this.north_sowing = north_sowing;
    }

    public String getEast_sowing() {
        return east_sowing;
    }

    public void setEast_sowing(String east_sowing) {
        this.east_sowing = east_sowing;
    }

    public String getWest_sowing() {
        return west_sowing;
    }

    public void setWest_sowing(String west_sowing) {
        this.west_sowing = west_sowing;
    }

    public String getFemale_lines_sowing() {
        return female_lines_sowing;
    }

    public void setFemale_lines_sowing(String female_lines_sowing) {
        this.female_lines_sowing = female_lines_sowing;
    }

    public String getFemale_sowing_lot_sowing() {
        return female_sowing_lot_sowing;
    }

    public void setFemale_sowing_lot_sowing(String female_sowing_lot_sowing) {
        this.female_sowing_lot_sowing = female_sowing_lot_sowing;
    }

    public double getReal_sowing_female_sowing() {
        return real_sowing_female_sowing;
    }

    public void setReal_sowing_female_sowing(double real_sowing_female_sowing) {
        this.real_sowing_female_sowing = real_sowing_female_sowing;
    }

    public String getFemale_sowing_date_start_sowing() {
        return female_sowing_date_start_sowing;
    }

    public void setFemale_sowing_date_start_sowing(String female_sowing_date_start_sowing) {
        this.female_sowing_date_start_sowing = female_sowing_date_start_sowing;
    }

    public String getFemale_sowing_date_end_sowing() {
        return female_sowing_date_end_sowing;
    }

    public void setFemale_sowing_date_end_sowing(String female_sowing_date_end_sowing) {
        this.female_sowing_date_end_sowing = female_sowing_date_end_sowing;
    }

    public double getSowing_seed_meter_sowing() {
        return sowing_seed_meter_sowing;
    }

    public void setSowing_seed_meter_sowing(double sowing_seed_meter_sowing) {
        this.sowing_seed_meter_sowing = sowing_seed_meter_sowing;
    }

    public double getRow_distance_sowing() {
        return row_distance_sowing;
    }

    public void setRow_distance_sowing(double row_distance_sowing) {
        this.row_distance_sowing = row_distance_sowing;
    }

    public String getDose_1_sowing() {
        return dose_1_sowing;
    }

    public void setDose_1_sowing(String dose_1_sowing) {
        this.dose_1_sowing = dose_1_sowing;
    }

    public String getDate_1_sowing() {
        return date_1_sowing;
    }

    public void setDate_1_sowing(String date_1_sowing) {
        this.date_1_sowing = date_1_sowing;
    }

    public String getDose_2_sowing() {
        return dose_2_sowing;
    }

    public void setDose_2_sowing(String dose_2_sowing) {
        this.dose_2_sowing = dose_2_sowing;
    }

    public String getDate_2_sowing() {
        return date_2_sowing;
    }

    public void setDate_2_sowing(String date_2_sowing) {
        this.date_2_sowing = date_2_sowing;
    }

    public String getName_1_herb_sowing() {
        return name_1_herb_sowing;
    }

    public void setName_1_herb_sowing(String name_1_herb_sowing) {
        this.name_1_herb_sowing = name_1_herb_sowing;
    }

    public String getDate_1_herb_sowing() {
        return date_1_herb_sowing;
    }

    public void setDate_1_herb_sowing(String date_1_herb_sowing) {
        this.date_1_herb_sowing = date_1_herb_sowing;
    }

    public String getName_2_herb_sowing() {
        return name_2_herb_sowing;
    }

    public void setName_2_herb_sowing(String name_2_herb_sowing) {
        this.name_2_herb_sowing = name_2_herb_sowing;
    }

    public String getDate_2_herb_sowing() {
        return date_2_herb_sowing;
    }

    public void setDate_2_herb_sowing(String date_2_herb_sowing) {
        this.date_2_herb_sowing = date_2_herb_sowing;
    }

    public String getName_3_herb_sowing() {
        return name_3_herb_sowing;
    }

    public void setName_3_herb_sowing(String name_3_herb_sowing) {
        this.name_3_herb_sowing = name_3_herb_sowing;
    }

    public String getDate_3_herb_sowing() {
        return date_3_herb_sowing;
    }

    public void setDate_3_herb_sowing(String date_3_herb_sowing) {
        this.date_3_herb_sowing = date_3_herb_sowing;
    }

    public String getDate_pre_emergence_sowing() {
        return date_pre_emergence_sowing;
    }

    public void setDate_pre_emergence_sowing(String date_pre_emergence_sowing) {
        this.date_pre_emergence_sowing = date_pre_emergence_sowing;
    }

    public String getDose_pre_emergence_sowing() {
        return dose_pre_emergence_sowing;
    }

    public void setDose_pre_emergence_sowing(String dose_pre_emergence_sowing) {
        this.dose_pre_emergence_sowing = dose_pre_emergence_sowing;
    }

    public double getWater_pre_emergence_sowing() {
        return water_pre_emergence_sowing;
    }

    public void setWater_pre_emergence_sowing(double water_pre_emergence_sowing) {
        this.water_pre_emergence_sowing = water_pre_emergence_sowing;
    }

    public double getPlant_m_sowing() {
        return plant_m_sowing;
    }

    public void setPlant_m_sowing(double plant_m_sowing) {
        this.plant_m_sowing = plant_m_sowing;
    }

    public double getPopulation_plants_ha_sowing() {
        return population_plants_ha_sowing;
    }

    public void setPopulation_plants_ha_sowing(double population_plants_ha_sowing) {
        this.population_plants_ha_sowing = population_plants_ha_sowing;
    }

    public String getBasta_spray_1_sowing() {
        return basta_spray_1_sowing;
    }

    public void setBasta_spray_1_sowing(String basta_spray_1_sowing) {
        this.basta_spray_1_sowing = basta_spray_1_sowing;
    }

    public String getBasta_spray_2_sowing() {
        return basta_spray_2_sowing;
    }

    public void setBasta_spray_2_sowing(String basta_spray_2_sowing) {
        this.basta_spray_2_sowing = basta_spray_2_sowing;
    }

    public String getBasta_spray_3_sowing() {
        return basta_spray_3_sowing;
    }

    public void setBasta_spray_3_sowing(String basta_spray_3_sowing) {
        this.basta_spray_3_sowing = basta_spray_3_sowing;
    }

    public String getBasta_spray_4_sowing() {
        return basta_spray_4_sowing;
    }

    public void setBasta_spray_4_sowing(String basta_spray_4_sowing) {
        this.basta_spray_4_sowing = basta_spray_4_sowing;
    }

    public String getBasta_splat_4_ha_sowing() {
        return basta_splat_4_ha_sowing;
    }

    public void setBasta_splat_4_ha_sowing(String basta_splat_4_ha_sowing) {
        this.basta_splat_4_ha_sowing = basta_splat_4_ha_sowing;
    }

    public String getDate_nombre_largo_sowing() {
        return date_nombre_largo_sowing;
    }

    public void setDate_nombre_largo_sowing(String date_nombre_largo_sowing) {
        this.date_nombre_largo_sowing = date_nombre_largo_sowing;
    }

    public String getProduct_nombre_largo_sowing() {
        return product_nombre_largo_sowing;
    }

    public void setProduct_nombre_largo_sowing(String product_nombre_largo_sowing) {
        this.product_nombre_largo_sowing = product_nombre_largo_sowing;
    }

    public String getDose_nombre_largo_sowing() {
        return dose_nombre_largo_sowing;
    }

    public void setDose_nombre_largo_sowing(String dose_nombre_largo_sowing) {
        this.dose_nombre_largo_sowing = dose_nombre_largo_sowing;
    }

    public String getFoliar_sowing() {
        return foliar_sowing;
    }

    public void setFoliar_sowing(String foliar_sowing) {
        this.foliar_sowing = foliar_sowing;
    }

    public String getDose_foliar_sowing() {
        return dose_foliar_sowing;
    }

    public void setDose_foliar_sowing(String dose_foliar_sowing) {
        this.dose_foliar_sowing = dose_foliar_sowing;
    }

    public String getDate_foliar_sowing() {
        return date_foliar_sowing;
    }

    public void setDate_foliar_sowing(String date_foliar_sowing) {
        this.date_foliar_sowing = date_foliar_sowing;
    }
}
