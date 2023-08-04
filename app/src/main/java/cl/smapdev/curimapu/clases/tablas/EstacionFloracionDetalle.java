package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "estacion_floracion_detalle")
public class EstacionFloracionDetalle {

    @PrimaryKey(autoGenerate = true)
    public int id_estacion_floracion_detalle;


    public String clave_unica_floracion;
    public String clave_unica_floracion_estacion;
    public String clave_unica_floracion_detalle;

    public String tituloDato;
    public String tipo_dato;
    public String valor_dato;


    public String getClave_unica_floracion_estacion() {
        return clave_unica_floracion_estacion;
    }

    public void setClave_unica_floracion_estacion(String clave_unica_floracion_estacion) {
        this.clave_unica_floracion_estacion = clave_unica_floracion_estacion;
    }

    public String getTituloDato() {
        return tituloDato;
    }

    public void setTituloDato(String tituloDato) {
        this.tituloDato = tituloDato;
    }

    public int getId_estacion_floracion_detalle() {
        return id_estacion_floracion_detalle;
    }

    public void setId_estacion_floracion_detalle(int id_estacion_floracion_detalle) {
        this.id_estacion_floracion_detalle = id_estacion_floracion_detalle;
    }

    public String getClave_unica_floracion() {
        return clave_unica_floracion;
    }

    public void setClave_unica_floracion(String clave_unica_floracion) {
        this.clave_unica_floracion = clave_unica_floracion;
    }

    public String getClave_unica_floracion_detalle() {
        return clave_unica_floracion_detalle;
    }

    public void setClave_unica_floracion_detalle(String clave_unica_floracion_detalle) {
        this.clave_unica_floracion_detalle = clave_unica_floracion_detalle;
    }

    public String getTipo_dato() {
        return tipo_dato;
    }

    public void setTipo_dato(String tipo_dato) {
        this.tipo_dato = tipo_dato;
    }

    public String getValor_dato() {
        return valor_dato;
    }

    public void setValor_dato(String valor_dato) {
        this.valor_dato = valor_dato;
    }
}
