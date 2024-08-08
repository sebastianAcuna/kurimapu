package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sitios_no_visitados")
public class SitiosNoVisitados {

    @PrimaryKey(autoGenerate = true)
    private int id;


    private int id_ac;
    private int id_temporada;
    private String numAnexo;


    private String nombreEspecie;
    private String nombreAgricultor;
    private String fechaUltimaVisita;
    private String nombreUsuario;
    private String nombreLote;

    private String dias;


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

    public int getId_temporada() {
        return id_temporada;
    }

    public void setId_temporada(int id_temporada) {
        this.id_temporada = id_temporada;
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

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreLote() {
        return nombreLote;
    }

    public void setNombreLote(String nombreLote) {
        this.nombreLote = nombreLote;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }
}
