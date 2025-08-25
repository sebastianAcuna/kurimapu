package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "op_almacigos")
public class OpAlmacigos {

    @PrimaryKey
    @SerializedName("id_v_post_siembra")
    private int id_v_post_siembra;

    @SerializedName("id_tipo_lc")
    private int id_tipo_lc;

    @SerializedName("id_especie")
    private int id_especie;

    @SerializedName("nombre_especie")
    private String nombre_especie;

    @SerializedName("id_cliente")
    private int id_cliente;
    @SerializedName("nombre_cliente")
    private String nombre_cliente;

    @SerializedName("id_variedad")
    private int id_variedad;
    @SerializedName("nombre_variedad")
    private String nombre_variedad;

    @SerializedName("nombre_parental")
    private String nombre_parental;

    @SerializedName("op")
    private String op;

    @SerializedName("fecha_siembra")
    private String fecha_siembra;

    @SerializedName("fecha_estimada_despacho")
    private String fecha_estimada_despacho;

    @SerializedName("descripcion_tipo_raiz")
    private String descripcion_tipo_raiz;

    @SerializedName("nombre_fantasia")
    private String nombre_fantasia;


    public String getDescripcion_tipo_raiz() {
        return descripcion_tipo_raiz;
    }

    public void setDescripcion_tipo_raiz(String descripcion_tipo_raiz) {
        this.descripcion_tipo_raiz = descripcion_tipo_raiz;
    }

    public String getNombre_fantasia() {
        return nombre_fantasia;
    }

    public void setNombre_fantasia(String nombre_fantasia) {
        this.nombre_fantasia = nombre_fantasia;
    }

    public int getId_v_post_siembra() {
        return id_v_post_siembra;
    }

    public void setId_v_post_siembra(int id_v_post_siembra) {
        this.id_v_post_siembra = id_v_post_siembra;
    }

    public int getId_tipo_lc() {
        return id_tipo_lc;
    }

    public void setId_tipo_lc(int id_tipo_lc) {
        this.id_tipo_lc = id_tipo_lc;
    }

    public int getId_especie() {
        return id_especie;
    }

    public void setId_especie(int id_especie) {
        this.id_especie = id_especie;
    }

    public String getNombre_especie() {
        return nombre_especie;
    }

    public void setNombre_especie(String nombre_especie) {
        this.nombre_especie = nombre_especie;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getNombre_cliente() {
        return nombre_cliente;
    }

    public void setNombre_cliente(String nombre_cliente) {
        this.nombre_cliente = nombre_cliente;
    }

    public int getId_variedad() {
        return id_variedad;
    }

    public void setId_variedad(int id_variedad) {
        this.id_variedad = id_variedad;
    }

    public String getNombre_variedad() {
        return nombre_variedad;
    }

    public void setNombre_variedad(String nombre_variedad) {
        this.nombre_variedad = nombre_variedad;
    }

    public String getNombre_parental() {
        return nombre_parental;
    }

    public void setNombre_parental(String nombre_parental) {
        this.nombre_parental = nombre_parental;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getFecha_siembra() {
        return fecha_siembra;
    }

    public void setFecha_siembra(String fecha_siembra) {
        this.fecha_siembra = fecha_siembra;
    }

    public String getFecha_estimada_despacho() {
        return fecha_estimada_despacho;
    }

    public void setFecha_estimada_despacho(String fecha_estimada_despacho) {
        this.fecha_estimada_despacho = fecha_estimada_despacho;
    }
}
