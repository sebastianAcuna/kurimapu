package cl.smapdev.curimapu.clases;

import androidx.room.Entity;

@Entity(tableName = "sowing")
public class Sowing {

    private int id_sowing;
    private int id_anexo_sowing;

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
}
