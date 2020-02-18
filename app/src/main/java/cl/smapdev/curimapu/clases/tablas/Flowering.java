package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "flowering")
public class Flowering {

    @PrimaryKey(autoGenerate = true)
    private int id_flowering;
    private int id_anexo_flowering;

    private int id_visita_flowering;
    private int estado_server_flowering;

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


    public int getId_flowering() {
        return id_flowering;
    }

    public void setId_flowering(int id_flowering) {
        this.id_flowering = id_flowering;
    }

    public int getId_anexo_flowering() {
        return id_anexo_flowering;
    }

    public void setId_anexo_flowering(int id_anexo_flowering) {
        this.id_anexo_flowering = id_anexo_flowering;
    }

    public int getId_visita_flowering() {
        return id_visita_flowering;
    }

    public void setId_visita_flowering(int id_visita_flowering) {
        this.id_visita_flowering = id_visita_flowering;
    }

    public int getEstado_server_flowering() {
        return estado_server_flowering;
    }

    public void setEstado_server_flowering(int estado_server_flowering) {
        this.estado_server_flowering = estado_server_flowering;
    }

    public String getDate_notice_sag_flowering() {
        return date_notice_sag_flowering;
    }

    public void setDate_notice_sag_flowering(String date_notice_sag_flowering) {
        this.date_notice_sag_flowering = date_notice_sag_flowering;
    }

    public String getFlowering_estimation_flowering() {
        return flowering_estimation_flowering;
    }

    public void setFlowering_estimation_flowering(String flowering_estimation_flowering) {
        this.flowering_estimation_flowering = flowering_estimation_flowering;
    }

    public String getFlowering_start_flowering() {
        return flowering_start_flowering;
    }

    public void setFlowering_start_flowering(String flowering_start_flowering) {
        this.flowering_start_flowering = flowering_start_flowering;
    }

    public String getFlowering_end_flowering() {
        return flowering_end_flowering;
    }

    public void setFlowering_end_flowering(String flowering_end_flowering) {
        this.flowering_end_flowering = flowering_end_flowering;
    }

    public String getFertility_1_flowering() {
        return fertility_1_flowering;
    }

    public void setFertility_1_flowering(String fertility_1_flowering) {
        this.fertility_1_flowering = fertility_1_flowering;
    }

    public String getFertility_2_flowering() {
        return fertility_2_flowering;
    }

    public void setFertility_2_flowering(String fertility_2_flowering) {
        this.fertility_2_flowering = fertility_2_flowering;
    }

    public String getFungicide_name_flowering() {
        return fungicide_name_flowering;
    }

    public void setFungicide_name_flowering(String fungicide_name_flowering) {
        this.fungicide_name_flowering = fungicide_name_flowering;
    }

    public String getDose_fungicide_flowering() {
        return dose_fungicide_flowering;
    }

    public void setDose_fungicide_flowering(String dose_fungicide_flowering) {
        this.dose_fungicide_flowering = dose_fungicide_flowering;
    }

    public String getDate_funficide_flowering() {
        return date_funficide_flowering;
    }

    public void setDate_funficide_flowering(String date_funficide_flowering) {
        this.date_funficide_flowering = date_funficide_flowering;
    }

    public String getDate_insecticide_flowering() {
        return date_insecticide_flowering;
    }

    public void setDate_insecticide_flowering(String date_insecticide_flowering) {
        this.date_insecticide_flowering = date_insecticide_flowering;
    }

    public String getDate_beginning_depuration_flowering() {
        return date_beginning_depuration_flowering;
    }

    public void setDate_beginning_depuration_flowering(String date_beginning_depuration_flowering) {
        this.date_beginning_depuration_flowering = date_beginning_depuration_flowering;
    }

    public String getDate_off_type_flowering() {
        return date_off_type_flowering;
    }

    public void setDate_off_type_flowering(String date_off_type_flowering) {
        this.date_off_type_flowering = date_off_type_flowering;
    }

    public String getPlant_number_checked_flowering() {
        return plant_number_checked_flowering;
    }

    public void setPlant_number_checked_flowering(String plant_number_checked_flowering) {
        this.plant_number_checked_flowering = plant_number_checked_flowering;
    }

    public String getCheck_flowering() {
        return check_flowering;
    }

    public void setCheck_flowering(String check_flowering) {
        this.check_flowering = check_flowering;
    }

    public String getMain_characteristic_flowering() {
        return main_characteristic_flowering;
    }

    public void setMain_characteristic_flowering(String main_characteristic_flowering) {
        this.main_characteristic_flowering = main_characteristic_flowering;
    }

    public String getDate_inspection_flowering() {
        return date_inspection_flowering;
    }

    public void setDate_inspection_flowering(String date_inspection_flowering) {
        this.date_inspection_flowering = date_inspection_flowering;
    }
}
