package cl.smapdev.curimapu.clases.temporales;


import androidx.room.Entity;

@Entity(tableName = "temp_sowing")
public class TempSowing {

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











}
