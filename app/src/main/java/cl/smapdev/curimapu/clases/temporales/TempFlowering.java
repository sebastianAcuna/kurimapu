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


    private int action_temp_flowering;


    public int getAction_temp_flowering() {
        return action_temp_flowering;
    }

    public void setAction_temp_flowering(int action_temp_flowering) {
        this.action_temp_flowering = action_temp_flowering;
    }

    public int getId_temp_flowering() {
        return id_temp_flowering;
    }

    public void setId_temp_flowering(int id_temp_flowering) {
        this.id_temp_flowering = id_temp_flowering;
    }

    public int getId_anexo_temp_flowering() {
        return id_anexo_temp_flowering;
    }

    public void setId_anexo_temp_flowering(int id_anexo_temp_flowering) {
        this.id_anexo_temp_flowering = id_anexo_temp_flowering;
    }

    public String getDate_notice_sag_temp_flowering() {
        return date_notice_sag_temp_flowering;
    }

    public void setDate_notice_sag_temp_flowering(String date_notice_sag_temp_flowering) {
        this.date_notice_sag_temp_flowering = date_notice_sag_temp_flowering;
    }

    public String getFlowering_estimation_temp_flowering() {
        return flowering_estimation_temp_flowering;
    }

    public void setFlowering_estimation_temp_flowering(String flowering_estimation_temp_flowering) {
        this.flowering_estimation_temp_flowering = flowering_estimation_temp_flowering;
    }

    public String getFlowering_start_temp_flowering() {
        return flowering_start_temp_flowering;
    }

    public void setFlowering_start_temp_flowering(String flowering_start_temp_flowering) {
        this.flowering_start_temp_flowering = flowering_start_temp_flowering;
    }

    public String getFlowering_end_temp_flowering() {
        return flowering_end_temp_flowering;
    }

    public void setFlowering_end_temp_flowering(String flowering_end_temp_flowering) {
        this.flowering_end_temp_flowering = flowering_end_temp_flowering;
    }

    public String getFertility_1_temp_flowering() {
        return fertility_1_temp_flowering;
    }

    public void setFertility_1_temp_flowering(String fertility_1_temp_flowering) {
        this.fertility_1_temp_flowering = fertility_1_temp_flowering;
    }

    public String getFertility_2_temp_flowering() {
        return fertility_2_temp_flowering;
    }

    public void setFertility_2_temp_flowering(String fertility_2_temp_flowering) {
        this.fertility_2_temp_flowering = fertility_2_temp_flowering;
    }

    public String getFungicide_name_temp_flowering() {
        return fungicide_name_temp_flowering;
    }

    public void setFungicide_name_temp_flowering(String fungicide_name_temp_flowering) {
        this.fungicide_name_temp_flowering = fungicide_name_temp_flowering;
    }

    public String getDose_fungicide_temp_flowering() {
        return dose_fungicide_temp_flowering;
    }

    public void setDose_fungicide_temp_flowering(String dose_fungicide_temp_flowering) {
        this.dose_fungicide_temp_flowering = dose_fungicide_temp_flowering;
    }

    public String getDate_funficide_temp_flowering() {
        return date_funficide_temp_flowering;
    }

    public void setDate_funficide_temp_flowering(String date_funficide_temp_flowering) {
        this.date_funficide_temp_flowering = date_funficide_temp_flowering;
    }

    public String getDate_insecticide_temp_flowering() {
        return date_insecticide_temp_flowering;
    }

    public void setDate_insecticide_temp_flowering(String date_insecticide_temp_flowering) {
        this.date_insecticide_temp_flowering = date_insecticide_temp_flowering;
    }

    public String getDate_beginning_depuration_temp_flowering() {
        return date_beginning_depuration_temp_flowering;
    }

    public void setDate_beginning_depuration_temp_flowering(String date_beginning_depuration_temp_flowering) {
        this.date_beginning_depuration_temp_flowering = date_beginning_depuration_temp_flowering;
    }

    public String getDate_off_type_temp_flowering() {
        return date_off_type_temp_flowering;
    }

    public void setDate_off_type_temp_flowering(String date_off_type_temp_flowering) {
        this.date_off_type_temp_flowering = date_off_type_temp_flowering;
    }

    public String getPlant_number_checked_temp_flowering() {
        return plant_number_checked_temp_flowering;
    }

    public void setPlant_number_checked_temp_flowering(String plant_number_checked_temp_flowering) {
        this.plant_number_checked_temp_flowering = plant_number_checked_temp_flowering;
    }

    public String getCheck_temp_flowering() {
        return check_temp_flowering;
    }

    public void setCheck_temp_flowering(String check_temp_flowering) {
        this.check_temp_flowering = check_temp_flowering;
    }

    public String getMain_characteristic_temp_flowering() {
        return main_characteristic_temp_flowering;
    }

    public void setMain_characteristic_temp_flowering(String main_characteristic_temp_flowering) {
        this.main_characteristic_temp_flowering = main_characteristic_temp_flowering;
    }

    public String getDate_inspection_temp_flowering() {
        return date_inspection_temp_flowering;
    }

    public void setDate_inspection_temp_flowering(String date_inspection_temp_flowering) {
        this.date_inspection_temp_flowering = date_inspection_temp_flowering;
    }
}
