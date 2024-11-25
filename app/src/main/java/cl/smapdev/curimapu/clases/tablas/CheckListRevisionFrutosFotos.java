package cl.smapdev.curimapu.clases.tablas;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

@Entity(tableName = "checklist_revision_frutos_fotos")
public class CheckListRevisionFrutosFotos {

    @PrimaryKey(autoGenerate = true)
    @Expose
    private int id_cl_revision_frutos_fotos;

    @Expose
    private String clave_unica_revision_frutos;

    private String clave_unica_foto;


    private String nombre_foto;
    private String ruta_foto;
    private String striged_foto;

    private int estado_sincronizacion;


    public String getClave_unica_foto() {
        return clave_unica_foto;
    }

    public void setClave_unica_foto(String clave_unica_foto) {
        this.clave_unica_foto = clave_unica_foto;
    }

    public int getId_cl_revision_frutos_fotos() {
        return id_cl_revision_frutos_fotos;
    }

    public void setId_cl_revision_frutos_fotos(int id_cl_revision_frutos_fotos) {
        this.id_cl_revision_frutos_fotos = id_cl_revision_frutos_fotos;
    }

    public String getClave_unica_revision_frutos() {
        return clave_unica_revision_frutos;
    }

    public void setClave_unica_revision_frutos(String clave_unica_revision_frutos) {
        this.clave_unica_revision_frutos = clave_unica_revision_frutos;
    }

    public String getNombre_foto() {
        return nombre_foto;
    }

    public void setNombre_foto(String nombre_foto) {
        this.nombre_foto = nombre_foto;
    }

    public String getRuta_foto() {
        return ruta_foto;
    }

    public void setRuta_foto(String ruta_foto) {
        this.ruta_foto = ruta_foto;
    }

    public String getStriged_foto() {
        return striged_foto;
    }

    public void setStriged_foto(String striged_foto) {
        this.striged_foto = striged_foto;
    }

    public int getEstado_sincronizacion() {
        return estado_sincronizacion;
    }

    public void setEstado_sincronizacion(int estado_sincronizacion) {
        this.estado_sincronizacion = estado_sincronizacion;
    }
}
