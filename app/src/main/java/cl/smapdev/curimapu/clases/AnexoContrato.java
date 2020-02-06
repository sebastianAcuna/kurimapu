package cl.smapdev.curimapu.clases;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "anexo_contrato")
public class AnexoContrato {

    @PrimaryKey(autoGenerate = true)
    private int id_anexo_contrato;

    private String anexo_contrato;

    private int id_variedad_anexo;

    private int id_especie_anexo;

    private String rut_agricultor_anexo;

    private String protero;

    private int id_ficha_contrato;


    public AnexoContrato() {
    }


    @Ignore
    public AnexoContrato(String anexo_contrato, int id_variedad_anexo, int id_especie_anexo, String rut_agricultor_anexo, String protero, int id_ficha_contrato) {
        this.anexo_contrato = anexo_contrato;
        this.id_variedad_anexo = id_variedad_anexo;
        this.id_especie_anexo = id_especie_anexo;
        this.rut_agricultor_anexo = rut_agricultor_anexo;
        this.protero = protero;
        this.id_ficha_contrato = id_ficha_contrato;
    }


    public int getId_ficha_contrato() {
        return id_ficha_contrato;
    }

    public void setId_ficha_contrato(int id_ficha_contrato) {
        this.id_ficha_contrato = id_ficha_contrato;
    }

    public int getId_anexo_contrato() {
        return id_anexo_contrato;
    }

    public void setId_anexo_contrato(int id_anexo_contrato) {
        this.id_anexo_contrato = id_anexo_contrato;
    }

    public String getAnexo_contrato() {
        return anexo_contrato;
    }

    public void setAnexo_contrato(String anexo_contrato) {
        this.anexo_contrato = anexo_contrato;
    }

    public int getId_variedad_anexo() {
        return id_variedad_anexo;
    }

    public void setId_variedad_anexo(int id_variedad_anexo) {
        this.id_variedad_anexo = id_variedad_anexo;
    }

    public int getId_especie_anexo() {
        return id_especie_anexo;
    }

    public void setId_especie_anexo(int id_especie_anexo) {
        this.id_especie_anexo = id_especie_anexo;
    }

    public String getRut_agricultor_anexo() {
        return rut_agricultor_anexo;
    }

    public void setRut_agricultor_anexo(String rut_agricultor_anexo) {
        this.rut_agricultor_anexo = rut_agricultor_anexo;
    }

    public String getProtero() {
        return protero;
    }

    public void setProtero(String protero) {
        this.protero = protero;
    }
}
