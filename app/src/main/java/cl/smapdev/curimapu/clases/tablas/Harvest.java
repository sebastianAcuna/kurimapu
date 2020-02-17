package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "harvest")
public class Harvest {

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


}
