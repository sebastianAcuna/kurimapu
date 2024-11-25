package cl.smapdev.curimapu.clases.tablas;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

@Entity(tableName = "checklist_revision_frutos_detalle")
public class CheckListRevisionFrutosDetalle {

    @PrimaryKey(autoGenerate = true)
    @Expose
    private int id_cl_revision_frutos_detalle;

    @Expose
    private String clave_unica_revision_frutos;

    private String clave_unica_detalle;

    private int numero_conteo;
    private int frutos_aptos;
    private int frutos_no_aptos;

    private int estado_sincronizacion;
    private String comentario;


    public int getId_cl_revision_frutos_detalle() {
        return id_cl_revision_frutos_detalle;
    }

    public void setId_cl_revision_frutos_detalle(int id_cl_revision_frutos_detalle) {
        this.id_cl_revision_frutos_detalle = id_cl_revision_frutos_detalle;
    }

    public String getClave_unica_revision_frutos() {
        return clave_unica_revision_frutos;
    }

    public void setClave_unica_revision_frutos(String clave_unica_revision_frutos) {
        this.clave_unica_revision_frutos = clave_unica_revision_frutos;
    }

    public String getClave_unica_detalle() {
        return clave_unica_detalle;
    }

    public void setClave_unica_detalle(String clave_unica_detalle) {
        this.clave_unica_detalle = clave_unica_detalle;
    }

    public int getNumero_conteo() {
        return numero_conteo;
    }

    public void setNumero_conteo(int numero_conteo) {
        this.numero_conteo = numero_conteo;
    }

    public int getFrutos_aptos() {
        return frutos_aptos;
    }

    public void setFrutos_aptos(int frutos_aptos) {
        this.frutos_aptos = frutos_aptos;
    }

    public int getFrutos_no_aptos() {
        return frutos_no_aptos;
    }

    public void setFrutos_no_aptos(int frutos_no_aptos) {
        this.frutos_no_aptos = frutos_no_aptos;
    }

    public int getEstado_sincronizacion() {
        return estado_sincronizacion;
    }

    public void setEstado_sincronizacion(int estado_sincronizacion) {
        this.estado_sincronizacion = estado_sincronizacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
