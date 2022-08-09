package cl.smapdev.curimapu.clases.tablas;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "anexo_contrato")
public class AnexoContrato {

    @PrimaryKey()
    @SerializedName("id_ac")
    @NonNull
    private String id_anexo_contrato;

    @SerializedName("num_anexo")
    @ColumnInfo(name = "num_anexo")
    private String anexo_contrato;

    @SerializedName("id_materiales")
    private String id_variedad_anexo;

    @SerializedName("id_tempo")
    private String temporada_anexo;

    @SerializedName("id_esp")
    private String id_especie_anexo;

    @SerializedName("id_agric")
    private String  id_agricultor_anexo;

    @SerializedName("nombre_potrero")
    private String protero;

    @SerializedName("id_ficha")
    private int id_ficha_contrato;

    @SerializedName("id_potrero")
    private int id_potrero;

    @SerializedName("superficie")
    private int superficie;


    /* CAMBIOS 10-08 13:25 */

    @SerializedName("ready_batch")
    private String ready_batch;


    @SerializedName("sectorial_office")
    private String sectorial_office;

    @SerializedName("date_sending_notice_sowing")
    private String field_sag;

    @SerializedName("rch")
    private String rch;

    @SerializedName("sag_register_number")
    private String sag_register_number;


    @SerializedName("has_contrato")
    private String has_contrato;

    @SerializedName("has_gps")
    private String has_gps;

    @SerializedName("orden_multiplicacion")
    private String orden_multiplicacion;

    @SerializedName("sowing_date")
    private String sowing_date;

    @SerializedName("lines_female")
    private String lines_female;

    @SerializedName("lines_male")
    private String lines_male;

    @SerializedName("sl_female")
    private String sl_female;

    @SerializedName("sl_real_sowing_female")
    private String sl_real_sowing_female;

    @SerializedName("sl_male")
    private String sl_male;

    @SerializedName("sl_real_sowing_male")
    private String sl_real_sowing_male;

    @SerializedName("sl_line_increase")
    private String sl_line_increase;

    @SerializedName("sl_real_sowing_increase_line")
    private String sl_real_sowing_increase_line;

    @SerializedName("results_raw_kgs")
    private String results_raw_kgs;

    @SerializedName("results_clean_kgs")
    private String results_clean_kgs;

    @SerializedName("yield_kg_ha")
    private String yield_kg_ha;



    /* CAMBIOS 13-08 10:43 */
    @SerializedName("linea_incremento")
    private String linea_incremento;


    // 26-07-2021

    @SerializedName("raw_kgs_lot_uno")
    private String raw_kgs_lot_uno;

    @SerializedName("raw_kgs_lot_dos")
    private String raw_kgs_lot_dos;

    @SerializedName("raw_kgs_lot_tres")
    private String raw_kgs_lot_tres;

    @SerializedName("raw_kgs_lot_cuatro")
    private String raw_kgs_lot_cuatro;

    @SerializedName("raw_kgs_lot_cinco")
    private String raw_kgs_lot_cinco;

    @SerializedName("total_kgs_raw")
    private String total_kgs_raw;

    @SerializedName("clean_kgs_lot_uno")
    private String clean_kgs_lot_uno;

    @SerializedName("clean_kgs_lot_dos")
    private String clean_kgs_lot_dos;

    @SerializedName("clean_kgs_lot_tres")
    private String clean_kgs_lot_tres;

    @SerializedName("clean_kgs_lot_cuatro")
    private String clean_kgs_lot_cuatro;

    @SerializedName("clean_kgs_lot_cinco")
    private String clean_kgs_lot_cinco;

    @SerializedName("total_clean_kgs")
    private String total_clean_kgs;

    @SerializedName("germination_lot_uno")
    private String germination_lot_uno;

    @SerializedName("germination_lot_dos")
    private String germination_lot_dos;

    @SerializedName("germination_lot_tres")
    private String germination_lot_tres;

    @SerializedName("germination_lot_cuatro")
    private String germination_lot_cuatro;

    @SerializedName("germination_lot_cinco")
    private String germination_lot_cinco;

    @SerializedName("humidity_lot_uno")
    private String humidity_lot_uno;

    @SerializedName("humidity_lot_dos")
    private String humidity_lot_dos;

    @SerializedName("humidity_lot_tres")
    private String humidity_lot_tres;

    @SerializedName("humidity_lot_cuatro")
    private String humidity_lot_cuatro;

    @SerializedName("humidity_lot_cinco")
    private String humidity_lot_cinco;

    @SerializedName("tsw_lot_uno")
    private String tsw_lot_uno;

    @SerializedName("tsw_lot_dos")
    private String tsw_lot_dos;

    @SerializedName("tsw_lot_tres")
    private String tsw_lot_tres;

    @SerializedName("tsw_lot_cuatro")
    private String tsw_lot_cuatro;

    @SerializedName("tsw_lot_cinco")
    private String tsw_lot_cinco;

    @SerializedName("id_de_quo")
    private String id_de_quo;

    /* CAMBIOS 07-07-2022 */
    @SerializedName("ha_destruidas")
    private String ha_destruidas;

    /* CAMBIOS 07-07-2022 16:56 */
    @SerializedName("destruido")
    private String destruido;

    /* CAMBIOS 03-08-2022 */
    @SerializedName("condicion")
    private String condicion;


    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public String getDestruido() {
        return destruido;
    }

    public void setDestruido(String destruido) {
        this.destruido = destruido;
    }

    public String getHa_destruidas() {
        return ha_destruidas;
    }

    public void setHa_destruidas(String ha_destruidas) {
        this.ha_destruidas = ha_destruidas;
    }

    public String getId_de_quo() {
        return id_de_quo;
    }

    public void setId_de_quo(String id_de_quo) {
        this.id_de_quo = id_de_quo;
    }

    public String getRaw_kgs_lot_uno() {
        return raw_kgs_lot_uno;
    }

    public void setRaw_kgs_lot_uno(String raw_kgs_lot_uno) {
        this.raw_kgs_lot_uno = raw_kgs_lot_uno;
    }

    public String getRaw_kgs_lot_dos() {
        return raw_kgs_lot_dos;
    }

    public void setRaw_kgs_lot_dos(String raw_kgs_lot_dos) {
        this.raw_kgs_lot_dos = raw_kgs_lot_dos;
    }

    public String getRaw_kgs_lot_tres() {
        return raw_kgs_lot_tres;
    }

    public void setRaw_kgs_lot_tres(String raw_kgs_lot_tres) {
        this.raw_kgs_lot_tres = raw_kgs_lot_tres;
    }

    public String getRaw_kgs_lot_cuatro() {
        return raw_kgs_lot_cuatro;
    }

    public void setRaw_kgs_lot_cuatro(String raw_kgs_lot_cuatro) {
        this.raw_kgs_lot_cuatro = raw_kgs_lot_cuatro;
    }

    public String getRaw_kgs_lot_cinco() {
        return raw_kgs_lot_cinco;
    }

    public void setRaw_kgs_lot_cinco(String raw_kgs_lot_cinco) {
        this.raw_kgs_lot_cinco = raw_kgs_lot_cinco;
    }

    public String getTotal_kgs_raw() {
        return total_kgs_raw;
    }

    public void setTotal_kgs_raw(String total_kgs_raw) {
        this.total_kgs_raw = total_kgs_raw;
    }

    public String getClean_kgs_lot_uno() {
        return clean_kgs_lot_uno;
    }

    public void setClean_kgs_lot_uno(String clean_kgs_lot_uno) {
        this.clean_kgs_lot_uno = clean_kgs_lot_uno;
    }

    public String getClean_kgs_lot_dos() {
        return clean_kgs_lot_dos;
    }

    public void setClean_kgs_lot_dos(String clean_kgs_lot_dos) {
        this.clean_kgs_lot_dos = clean_kgs_lot_dos;
    }

    public String getClean_kgs_lot_tres() {
        return clean_kgs_lot_tres;
    }

    public void setClean_kgs_lot_tres(String clean_kgs_lot_tres) {
        this.clean_kgs_lot_tres = clean_kgs_lot_tres;
    }

    public String getClean_kgs_lot_cuatro() {
        return clean_kgs_lot_cuatro;
    }

    public void setClean_kgs_lot_cuatro(String clean_kgs_lot_cuatro) {
        this.clean_kgs_lot_cuatro = clean_kgs_lot_cuatro;
    }

    public String getClean_kgs_lot_cinco() {
        return clean_kgs_lot_cinco;
    }

    public void setClean_kgs_lot_cinco(String clean_kgs_lot_cinco) {
        this.clean_kgs_lot_cinco = clean_kgs_lot_cinco;
    }

    public String getTotal_clean_kgs() {
        return total_clean_kgs;
    }

    public void setTotal_clean_kgs(String total_clean_kgs) {
        this.total_clean_kgs = total_clean_kgs;
    }

    public String getGermination_lot_uno() {
        return germination_lot_uno;
    }

    public void setGermination_lot_uno(String germination_lot_uno) {
        this.germination_lot_uno = germination_lot_uno;
    }

    public String getGermination_lot_dos() {
        return germination_lot_dos;
    }

    public void setGermination_lot_dos(String germination_lot_dos) {
        this.germination_lot_dos = germination_lot_dos;
    }

    public String getGermination_lot_tres() {
        return germination_lot_tres;
    }

    public void setGermination_lot_tres(String germination_lot_tres) {
        this.germination_lot_tres = germination_lot_tres;
    }

    public String getGermination_lot_cuatro() {
        return germination_lot_cuatro;
    }

    public void setGermination_lot_cuatro(String germination_lot_cuatro) {
        this.germination_lot_cuatro = germination_lot_cuatro;
    }

    public String getGermination_lot_cinco() {
        return germination_lot_cinco;
    }

    public void setGermination_lot_cinco(String germination_lot_cinco) {
        this.germination_lot_cinco = germination_lot_cinco;
    }

    public String getHumidity_lot_uno() {
        return humidity_lot_uno;
    }

    public void setHumidity_lot_uno(String humidity_lot_uno) {
        this.humidity_lot_uno = humidity_lot_uno;
    }

    public String getHumidity_lot_dos() {
        return humidity_lot_dos;
    }

    public void setHumidity_lot_dos(String humidity_lot_dos) {
        this.humidity_lot_dos = humidity_lot_dos;
    }

    public String getHumidity_lot_tres() {
        return humidity_lot_tres;
    }

    public void setHumidity_lot_tres(String humidity_lot_tres) {
        this.humidity_lot_tres = humidity_lot_tres;
    }

    public String getHumidity_lot_cuatro() {
        return humidity_lot_cuatro;
    }

    public void setHumidity_lot_cuatro(String humidity_lot_cuatro) {
        this.humidity_lot_cuatro = humidity_lot_cuatro;
    }

    public String getHumidity_lot_cinco() {
        return humidity_lot_cinco;
    }

    public void setHumidity_lot_cinco(String humidity_lot_cinco) {
        this.humidity_lot_cinco = humidity_lot_cinco;
    }

    public String getTsw_lot_uno() {
        return tsw_lot_uno;
    }

    public void setTsw_lot_uno(String tsw_lot_uno) {
        this.tsw_lot_uno = tsw_lot_uno;
    }

    public String getTsw_lot_dos() {
        return tsw_lot_dos;
    }

    public void setTsw_lot_dos(String tsw_lot_dos) {
        this.tsw_lot_dos = tsw_lot_dos;
    }

    public String getTsw_lot_tres() {
        return tsw_lot_tres;
    }

    public void setTsw_lot_tres(String tsw_lot_tres) {
        this.tsw_lot_tres = tsw_lot_tres;
    }

    public String getTsw_lot_cuatro() {
        return tsw_lot_cuatro;
    }

    public void setTsw_lot_cuatro(String tsw_lot_cuatro) {
        this.tsw_lot_cuatro = tsw_lot_cuatro;
    }

    public String getTsw_lot_cinco() {
        return tsw_lot_cinco;
    }

    public void setTsw_lot_cinco(String tsw_lot_cinco) {
        this.tsw_lot_cinco = tsw_lot_cinco;
    }

    public String getLinea_incremento() {
        return linea_incremento;
    }

    public void setLinea_incremento(String linea_incremento) {
        this.linea_incremento = linea_incremento;
    }

    public int getSuperficie() {
        return superficie;
    }

    public void setSuperficie(int superficie) {
        this.superficie = superficie;
    }

    public int getId_potrero() {
        return id_potrero;
    }

    public void setId_potrero(int id_potrero) {
        this.id_potrero = id_potrero;
    }

    public AnexoContrato() {
    }

    public String getReady_batch() {
        return ready_batch;
    }

    public void setReady_batch(String ready_batch) {
        this.ready_batch = ready_batch;
    }

    public String getSectorial_office() {
        return sectorial_office;
    }

    public void setSectorial_office(String sectorial_office) {
        this.sectorial_office = sectorial_office;
    }

    public String getField_sag() {
        return field_sag;
    }

    public void setField_sag(String field_sag) {
        this.field_sag = field_sag;
    }

    public String getRch() {
        return rch;
    }

    public void setRch(String rch) {
        this.rch = rch;
    }

    public String getSag_register_number() {
        return sag_register_number;
    }

    public void setSag_register_number(String sag_register_number) {
        this.sag_register_number = sag_register_number;
    }

    public String getHas_contrato() {
        return has_contrato;
    }

    public void setHas_contrato(String has_contrato) {
        this.has_contrato = has_contrato;
    }

    public String getHas_gps() {
        return has_gps;
    }

    public void setHas_gps(String has_gps) {
        this.has_gps = has_gps;
    }

    public String getOrden_multiplicacion() {
        return orden_multiplicacion;
    }

    public void setOrden_multiplicacion(String orden_multiplicacion) {
        this.orden_multiplicacion = orden_multiplicacion;
    }

    public String getSowing_date() {
        return sowing_date;
    }

    public void setSowing_date(String sowing_date) {
        this.sowing_date = sowing_date;
    }

    public String getLines_female() {
        return lines_female;
    }

    public void setLines_female(String lines_female) {
        this.lines_female = lines_female;
    }

    public String getLines_male() {
        return lines_male;
    }

    public void setLines_male(String lines_male) {
        this.lines_male = lines_male;
    }

    public String getSl_female() {
        return sl_female;
    }

    public void setSl_female(String sl_female) {
        this.sl_female = sl_female;
    }

    public String getSl_real_sowing_female() {
        return sl_real_sowing_female;
    }

    public void setSl_real_sowing_female(String sl_real_sowing_female) {
        this.sl_real_sowing_female = sl_real_sowing_female;
    }

    public String getSl_male() {
        return sl_male;
    }

    public void setSl_male(String sl_male) {
        this.sl_male = sl_male;
    }

    public String getSl_real_sowing_male() {
        return sl_real_sowing_male;
    }

    public void setSl_real_sowing_male(String sl_real_sowing_male) {
        this.sl_real_sowing_male = sl_real_sowing_male;
    }

    public String getSl_line_increase() {
        return sl_line_increase;
    }

    public void setSl_line_increase(String sl_line_increase) {
        this.sl_line_increase = sl_line_increase;
    }

    public String getSl_real_sowing_increase_line() {
        return sl_real_sowing_increase_line;
    }

    public void setSl_real_sowing_increase_line(String sl_real_sowing_increase_line) {
        this.sl_real_sowing_increase_line = sl_real_sowing_increase_line;
    }

    public String getResults_raw_kgs() {
        return results_raw_kgs;
    }

    public void setResults_raw_kgs(String results_raw_kgs) {
        this.results_raw_kgs = results_raw_kgs;
    }

    public String getResults_clean_kgs() {
        return results_clean_kgs;
    }

    public void setResults_clean_kgs(String results_clean_kgs) {
        this.results_clean_kgs = results_clean_kgs;
    }

    public String getYield_kg_ha() {
        return yield_kg_ha;
    }

    public void setYield_kg_ha(String yield_kg_ha) {
        this.yield_kg_ha = yield_kg_ha;
    }

    public String getTemporada_anexo() {
        return temporada_anexo;
    }

    public void setTemporada_anexo(String temporada_anexo) {
        this.temporada_anexo = temporada_anexo;
    }

    public int getId_ficha_contrato() {
        return id_ficha_contrato;
    }

    public void setId_ficha_contrato(int id_ficha_contrato) {
        this.id_ficha_contrato = id_ficha_contrato;
    }


    public String getAnexo_contrato() {
        return anexo_contrato;
    }

    public void setAnexo_contrato(String anexo_contrato) {
        this.anexo_contrato = anexo_contrato;
    }


    public String getId_especie_anexo() {
        return id_especie_anexo;
    }

    public void setId_especie_anexo(String id_especie_anexo) {
        this.id_especie_anexo = id_especie_anexo;
    }

    public String getProtero() {
        return protero;
    }

    public void setProtero(String protero) {
        this.protero = protero;
    }

    public String getId_anexo_contrato() {
        return id_anexo_contrato;
    }

    public void setId_anexo_contrato(String id_anexo_contrato) {
        this.id_anexo_contrato = id_anexo_contrato;
    }

    public String getId_variedad_anexo() {
        return id_variedad_anexo;
    }

    public void setId_variedad_anexo(String id_variedad_anexo) {
        this.id_variedad_anexo = id_variedad_anexo;
    }

    public String getId_agricultor_anexo() {
        return id_agricultor_anexo;
    }

    public void setId_agricultor_anexo(String id_agricultor_anexo) {
        this.id_agricultor_anexo = id_agricultor_anexo;
    }
}
