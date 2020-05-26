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
