package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "checklist_roguing_foto_cabecera")
public class CheckListRoguingFotoCabecera {

    @PrimaryKey(autoGenerate = true)
    private int id_cl_roguing_foto_cabecera;

    private String clave_unica_roguing;
    private String clave_unica;

    private String nombre_foto;
    private String ruta;

    private String stringed_foto;

    private String tipo_foto;

    private String fecha_tx;
    private int user_tx;
    private int estado_sincronizacion;


    public int getEstado_sincronizacion() {
        return estado_sincronizacion;
    }

    public void setEstado_sincronizacion(int estado_sincronizacion) {
        this.estado_sincronizacion = estado_sincronizacion;
    }

    public String getStringed_foto() {
        return stringed_foto;
    }

    public void setStringed_foto(String stringed_foto) {
        this.stringed_foto = stringed_foto;
    }

    public String getTipo_foto() {
        return tipo_foto;
    }

    public void setTipo_foto(String tipo_foto) {
        this.tipo_foto = tipo_foto;
    }

    public String getFecha_tx() {
        return fecha_tx;
    }

    public void setFecha_tx(String fecha_tx) {
        this.fecha_tx = fecha_tx;
    }

    public int getUser_tx() {
        return user_tx;
    }

    public void setUser_tx(int user_tx) {
        this.user_tx = user_tx;
    }

    public int getId_cl_roguing_foto_cabecera() {
        return id_cl_roguing_foto_cabecera;
    }

    public void setId_cl_roguing_foto_cabecera(int id_cl_roguing_foto_cabecera) {
        this.id_cl_roguing_foto_cabecera = id_cl_roguing_foto_cabecera;
    }

    public String getClave_unica_roguing() {
        return clave_unica_roguing;
    }

    public void setClave_unica_roguing(String clave_unica_roguing) {
        this.clave_unica_roguing = clave_unica_roguing;
    }

    public String getClave_unica() {
        return clave_unica;
    }

    public void setClave_unica(String clave_unica) {
        this.clave_unica = clave_unica;
    }

    public String getNombre_foto() {
        return nombre_foto;
    }

    public void setNombre_foto(String nombre_foto) {
        this.nombre_foto = nombre_foto;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}
