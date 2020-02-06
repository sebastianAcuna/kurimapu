package cl.smapdev.curimapu.clases.temporales;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "temp_flowering")
public class TempFlowering {

    @PrimaryKey(autoGenerate = true)
    private int id_temp_flowering;
    private int id_anexo_temp_flowering;

    /* NOTICE SAG */
    private String date_notice_sag_temp_flowering;
    private String flowering_estimation_temp_flowering;

    /*FLOWERING START*/
    private String flowering_start_temp_flowering;

    /*FLOWERING END */
    private String flowering_end_temp_flowering;

    /*FERTILITY */
    private String fertility_1_temp_flowering;
    private String fertility_2_temp_flowering;

    /* FUNGICIDE APPLICATION */
    private String fungicide_name_temp_flowering;
    /*TODO VERIFICAR TIPO DE DATO */
    private String dose_fungicide_temp_flowering;
    private String date_funficide_temp_flowering;


    /*INSECTICIDE APPLICATION */
    private String date_insecticide_temp_flowering;

    /*BEGINNING DEPURATION */
    private String date_beginning_depuration_temp_flowering;

    /* OFF TYPE */
    private String date_off_type_temp_flowering;
    private String plant_number_checked_temp_flowering;
    private String check_temp_flowering;
    private String main_characteristic_temp_flowering;

    /* INSPECTION */
    private String date_inspection_temp_flowering;


}
