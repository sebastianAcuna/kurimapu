package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "primera_prioridad")
public class PrimeraPrioridad {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int id_ac;

    private int id_temporada;
    private String numAnexo;

    private String nombreEspecie;
    private String nombreAgricultor;
    private String fechaUltimaVisita;


    private String colorCrecimiento;
    private String valorCrecimiento;
    private String colorFitosanitario;
    private String valorFitosanitario;
    private String colorGeneral;
    private String valorGeneral;
    private String colorNdvi;
    private String valorNdvi;
    private String colorMi;
    private String valorMi;
    private String colormaleza;
    private String valormaleza;
    private String colorCosecha;
    private String valorCosecha;


    public int getId_temporada() {
        return id_temporada;
    }

    public void setId_temporada(int id_temporada) {
        this.id_temporada = id_temporada;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_ac() {
        return id_ac;
    }

    public void setId_ac(int id_ac) {
        this.id_ac = id_ac;
    }

    public String getNumAnexo() {
        return numAnexo;
    }

    public void setNumAnexo(String numAnexo) {
        this.numAnexo = numAnexo;
    }

    public String getNombreEspecie() {
        return nombreEspecie;
    }

    public void setNombreEspecie(String nombreEspecie) {
        this.nombreEspecie = nombreEspecie;
    }

    public String getNombreAgricultor() {
        return nombreAgricultor;
    }

    public void setNombreAgricultor(String nombreAgricultor) {
        this.nombreAgricultor = nombreAgricultor;
    }

    public String getFechaUltimaVisita() {
        return fechaUltimaVisita;
    }

    public void setFechaUltimaVisita(String fechaUltimaVisita) {
        this.fechaUltimaVisita = fechaUltimaVisita;
    }

    public String getColorCrecimiento() {
        return colorCrecimiento;
    }

    public void setColorCrecimiento(String colorCrecimiento) {
        this.colorCrecimiento = colorCrecimiento;
    }

    public String getValorCrecimiento() {
        return valorCrecimiento;
    }

    public void setValorCrecimiento(String valorCrecimiento) {
        this.valorCrecimiento = valorCrecimiento;
    }

    public String getColorFitosanitario() {
        return colorFitosanitario;
    }

    public void setColorFitosanitario(String colorFitosanitario) {
        this.colorFitosanitario = colorFitosanitario;
    }

    public String getValorFitosanitario() {
        return valorFitosanitario;
    }

    public void setValorFitosanitario(String valorFitosanitario) {
        this.valorFitosanitario = valorFitosanitario;
    }

    public String getColorGeneral() {
        return colorGeneral;
    }

    public void setColorGeneral(String colorGeneral) {
        this.colorGeneral = colorGeneral;
    }

    public String getValorGeneral() {
        return valorGeneral;
    }

    public void setValorGeneral(String valorGeneral) {
        this.valorGeneral = valorGeneral;
    }

    public String getColorNdvi() {
        return colorNdvi;
    }

    public void setColorNdvi(String colorNdvi) {
        this.colorNdvi = colorNdvi;
    }

    public String getValorNdvi() {
        return valorNdvi;
    }

    public void setValorNdvi(String valorNdvi) {
        this.valorNdvi = valorNdvi;
    }

    public String getColorMi() {
        return colorMi;
    }

    public void setColorMi(String colorMi) {
        this.colorMi = colorMi;
    }

    public String getValorMi() {
        return valorMi;
    }

    public void setValorMi(String valorMi) {
        this.valorMi = valorMi;
    }

    public String getColormaleza() {
        return colormaleza;
    }

    public void setColormaleza(String colormaleza) {
        this.colormaleza = colormaleza;
    }

    public String getValormaleza() {
        return valormaleza;
    }

    public void setValormaleza(String valormaleza) {
        this.valormaleza = valormaleza;
    }

    public String getColorCosecha() {
        return colorCosecha;
    }

    public void setColorCosecha(String colorCosecha) {
        this.colorCosecha = colorCosecha;
    }

    public String getValorCosecha() {
        return valorCosecha;
    }

    public void setValorCosecha(String valorCosecha) {
        this.valorCosecha = valorCosecha;
    }
}
