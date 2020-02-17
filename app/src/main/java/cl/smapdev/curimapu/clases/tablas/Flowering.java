package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "flowering")
public class Flowering {

    @PrimaryKey(autoGenerate = true)
    private int id_flowering;
    private int id_anexo_flowering;

    /* NOTICE SAG */
    private String date_notice_sag_flowering;
    private String flowering_estimation_flowering;

    /*FLOWERING START*/
    private String flowering_start_flowering;

    /*FLOWERING END */
    private String flowering_end_flowering;

    /*FERTILITY */
    private String fertility_1_flowering;
    private String fertility_2_flowering;

    /* FUNGICIDE APPLICATION */
    private String fungicide_name_flowering;
    /*TODO VERIFICAR TIPO DE DATO */
    private String dose_fungicide_flowering;
    private String date_funficide_flowering;


    /*INSECTICIDE APPLICATION */
    private String date_insecticide_flowering;

    /*BEGINNING DEPURATION */
    private String date_beginning_depuration_flowering;

    /* OFF TYPE */
    private String date_off_type_flowering;
    private String plant_number_checked_flowering;
    private String check_flowering;
    private String main_characteristic_flowering;

    /* INSPECTION */
    private String date_inspection_flowering;

}
