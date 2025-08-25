package cl.smapdev.curimapu.clases.tablas;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "almacigos_fotos_visita")
public class FotosAlmacigos {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id_foto")
    @Expose
    private int id_foto;

    @SerializedName("uid_foto")
    @Expose
    private String uid_foto;


    @SerializedName("uid_visita")
    @Expose
    private String uid_visita;

    @SerializedName("fecha")
    @Expose
    private String fecha;

    @SerializedName("hora")
    @Expose
    private String hora;

    @SerializedName("fecha_hora")
    @Expose
    private String fecha_hora;


    @SerializedName("favorita")
    @Expose
    private int favorita;


    @SerializedName("nombre_foto")
    @Expose
    private String nombre_foto;

    @SerializedName("id_user_tx")
    @Expose
    private String id_user_tx;


    @SerializedName("ruta_foto")
    @Expose
    private String ruta_foto;

    @SerializedName("imagen_base64")
    @Expose
    private String imagen_base64;

    @SerializedName("estado_sincronizacion")
    @Expose
    private String estado_sincronizacion;


    public String getUid_foto() {
        return uid_foto;
    }

    public void setUid_foto(String uid_foto) {
        this.uid_foto = uid_foto;
    }

    public String getImagen_base64() {
        return imagen_base64;
    }

    public void setImagen_base64(String imagen_base64) {
        this.imagen_base64 = imagen_base64;
    }

    public int getId_foto() {
        return id_foto;
    }

    public void setId_foto(int id_foto) {
        this.id_foto = id_foto;
    }

    public String getUid_visita() {
        return uid_visita;
    }

    public void setUid_visita(String uid_visita) {
        this.uid_visita = uid_visita;
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

    public String getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(String fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public int getFavorita() {
        return favorita;
    }

    public void setFavorita(int favorita) {
        this.favorita = favorita;
    }

    public String getNombre_foto() {
        return nombre_foto;
    }

    public void setNombre_foto(String nombre_foto) {
        this.nombre_foto = nombre_foto;
    }

    public String getId_user_tx() {
        return id_user_tx;
    }

    public void setId_user_tx(String id_user_tx) {
        this.id_user_tx = id_user_tx;
    }

    public String getRuta_foto() {
        return ruta_foto;
    }

    public void setRuta_foto(String ruta_foto) {
        this.ruta_foto = ruta_foto;
    }

    public String getEstado_sincronizacion() {
        return estado_sincronizacion;
    }

    public void setEstado_sincronizacion(String estado_sincronizacion) {
        this.estado_sincronizacion = estado_sincronizacion;
    }


}
