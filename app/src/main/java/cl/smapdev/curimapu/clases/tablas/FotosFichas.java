package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "fotos_fichas")
public class FotosFichas {


    @SerializedName("id_fotos_fichas")
    @Expose
    @PrimaryKey(autoGenerate = true)
    private int id_fotos_fichas;


    @SerializedName("nombre_foto_ficha")
    @Expose
    private String nombre_foto_ficha;


    @SerializedName("ruta_foto_ficha")
    @Expose
    private String ruta_foto_ficha;


    @SerializedName("id_ficha_fotos_local")
    @Expose
    private int id_ficha_fotos_local;

    @SerializedName("id_ficha_fotos_servidor")
    @Expose
    private int id_ficha_fotos_servidor;


    @SerializedName("fecha_hora_captura")
    @Expose
    private String fecha_hora_captura;


    @SerializedName("id_usuario_captura")
    @Expose
    private int id_usuario_captura;


    @SerializedName("id_dispo_captura")
    @Expose
    private int id_dispo_captura;


    @SerializedName("estado_subida_foto")
    @Expose
    private int estado_subida_foto;

    @SerializedName("imagen_foto")
    @Expose
    private String encrypted_image;

    @SerializedName("cabecera_subida")
    @Expose
    private int cabecera_subida;

    public int getCabecera_subida() {
        return cabecera_subida;
    }

    public void setCabecera_subida(int cabecera_subida) {
        this.cabecera_subida = cabecera_subida;
    }

    public String getEncrypted_image() {
        return encrypted_image;
    }

    public void setEncrypted_image(String encrypted_image) {
        this.encrypted_image = encrypted_image;
    }

    public int getId_fotos_fichas() {
        return id_fotos_fichas;
    }

    public void setId_fotos_fichas(int id_fotos_fichas) {
        this.id_fotos_fichas = id_fotos_fichas;
    }

    public String getNombre_foto_ficha() {
        return nombre_foto_ficha;
    }

    public void setNombre_foto_ficha(String nombre_foto_ficha) {
        this.nombre_foto_ficha = nombre_foto_ficha;
    }

    public String getRuta_foto_ficha() {
        return ruta_foto_ficha;
    }

    public void setRuta_foto_ficha(String ruta_foto_ficha) {
        this.ruta_foto_ficha = ruta_foto_ficha;
    }

    public int getId_ficha_fotos_local() {
        return id_ficha_fotos_local;
    }

    public void setId_ficha_fotos_local(int id_ficha_fotos_local) {
        this.id_ficha_fotos_local = id_ficha_fotos_local;
    }

    public int getId_ficha_fotos_servidor() {
        return id_ficha_fotos_servidor;
    }

    public void setId_ficha_fotos_servidor(int id_ficha_fotos_servidor) {
        this.id_ficha_fotos_servidor = id_ficha_fotos_servidor;
    }

    public String getFecha_hora_captura() {
        return fecha_hora_captura;
    }

    public void setFecha_hora_captura(String fecha_hora_captura) {
        this.fecha_hora_captura = fecha_hora_captura;
    }

    public int getId_usuario_captura() {
        return id_usuario_captura;
    }

    public void setId_usuario_captura(int id_usuario_captura) {
        this.id_usuario_captura = id_usuario_captura;
    }

    public int getId_dispo_captura() {
        return id_dispo_captura;
    }

    public void setId_dispo_captura(int id_dispo_captura) {
        this.id_dispo_captura = id_dispo_captura;
    }

    public int getEstado_subida_foto() {
        return estado_subida_foto;
    }

    public void setEstado_subida_foto(int estado_subida_foto) {
        this.estado_subida_foto = estado_subida_foto;
    }
}
