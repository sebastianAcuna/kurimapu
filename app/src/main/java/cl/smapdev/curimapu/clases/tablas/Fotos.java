package cl.smapdev.curimapu.clases.tablas;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//https://github.com/alexvasilkov/GestureViews
@Entity(tableName = "fotos")
public class Fotos {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id_foto")
    @Expose
    private int id_foto;

    @SerializedName("id_anexo")
    @Expose
    private String id_ficha_fotos; /* ES EL ANEXO*/

    @SerializedName("nombre_foto")
    @Expose
    private String nombre_foto;

    @SerializedName("fieldbook")
    @Expose
    private int fieldbook; /*0: visit form, 1: resumen, 2:siembra, 3:floarcion, 4:cosecha */

    @SerializedName("vista")
    @Expose
    private int vista; /*0: cliente, 1: agricultor 2: ambos*/

    @SerializedName("plano")
    @Expose
    private int plano; /* 0: general, 1: detalle*/

    @SerializedName("fecha")
    @Expose
    private String fecha;

    @SerializedName("hora")
    @Expose
    private String hora;

    @SerializedName("favorita")
    @Expose
    private boolean favorita;

    @SerializedName("ruta")
    @Expose
    private String ruta;

    /* id local de la visita */
    @SerializedName("id_visita_foto")
    @Expose
    private int id_visita_foto;

    /* id servidor de visita */
    private int id_visita_servidor_foto;

    /* id_dispo_foto */
    private int id_dispo_foto;

    private int estado_fotos;

    @SerializedName("imagen")
    @Expose
    private String encrypted_image;

    @SerializedName("cabecera_fotos")
    @Expose
    private int cabecera_fotos;


    @SerializedName("tomada_fotos")
    @Expose
    private int tomada_foto;


    @SerializedName("medida_raices")
    @Expose
    private String medida_raices;

    @SerializedName("acepto_regla_raices")
    @Expose
    private int acepto_regla_raices;


    public String getMedida_raices() {
        return medida_raices;
    }

    public void setMedida_raices(String medida_raices) {
        this.medida_raices = medida_raices;
    }

    public int getAcepto_regla_raices() {
        return acepto_regla_raices;
    }

    public void setAcepto_regla_raices(int acepto_regla_raices) {
        this.acepto_regla_raices = acepto_regla_raices;
    }

    public int getTomada_foto() {
        return tomada_foto;
    }

    public void setTomada_foto(int tomada_foto) {
        this.tomada_foto = tomada_foto;
    }

    public int getId_visita_servidor_foto() {
        return id_visita_servidor_foto;
    }

    public void setId_visita_servidor_foto(int id_visita_servidor_foto) {
        this.id_visita_servidor_foto = id_visita_servidor_foto;
    }

    public int getId_dispo_foto() {
        return id_dispo_foto;
    }

    public void setId_dispo_foto(int id_dispo_foto) {
        this.id_dispo_foto = id_dispo_foto;
    }

    public int getCabecera_fotos() {
        return cabecera_fotos;
    }

    public void setCabecera_fotos(int cabecera_fotos) {
        this.cabecera_fotos = cabecera_fotos;
    }

    public String getEncrypted_image() {
        return encrypted_image;
    }
    public void setEncrypted_image(String encrypted_image) {
        this.encrypted_image = encrypted_image;
    }

    public int getEstado_fotos() {
        return estado_fotos;
    }

    public void setEstado_fotos(int estado_fotos) {
        this.estado_fotos = estado_fotos;
    }

    public int getId_visita_foto() {
        return id_visita_foto;
    }

    public void setId_visita_foto(int id_visita_foto) {
        this.id_visita_foto = id_visita_foto;
    }

    public int getId_foto() {
        return id_foto;
    }

    public void setId_foto(int id_foto) {
        this.id_foto = id_foto;
    }

    public String getId_ficha_fotos() {
        return id_ficha_fotos;
    }

    public void setId_ficha_fotos(String id_ficha_fotos) {
        this.id_ficha_fotos = id_ficha_fotos;
    }

    public String getNombre_foto() {
        return nombre_foto;
    }

    public void setNombre_foto(String nombre_foto) {
        this.nombre_foto = nombre_foto;
    }

    public int getFieldbook() {
        return fieldbook;
    }

    public void setFieldbook(int fieldbook) {
        this.fieldbook = fieldbook;
    }

    public int getVista() {
        return vista;
    }

    public void setVista(int vista) {
        this.vista = vista;
    }

    public int getPlano() {
        return plano;
    }

    public void setPlano(int plano) {
        this.plano = plano;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public boolean isFavorita() {
        return favorita;
    }

    public void setFavorita(boolean favorita) {
        this.favorita = favorita;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}
