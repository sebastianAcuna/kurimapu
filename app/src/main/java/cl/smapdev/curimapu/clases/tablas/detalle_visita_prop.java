package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "detalle_visita_prop")
public class detalle_visita_prop {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id_det_vis_prop_detalle")
    @Expose
    private int id_det_vis_prop_detalle;

    @SerializedName("id_visita")
    @Expose
    private int id_visita_detalle;

    @SerializedName("id_prop_mat_cli")
    @Expose
    private int id_prop_mat_cli_detalle;

    @SerializedName("valor")
    @Expose
    private String valor_detalle;

    @SerializedName("estado_detalle")
    @Expose
    private int estado_detalle;

    @SerializedName("cabecera_detalle")
    @Expose
    private int cabecera_detalle;


    @SerializedName("tomada_detalle")
    @Expose
    private int tomada_detalle;


    public int getTomada_detalle() {
        return tomada_detalle;
    }

    public void setTomada_detalle(int tomada_detalle) {
        this.tomada_detalle = tomada_detalle;
    }

    public int getCabecera_detalle() {
        return cabecera_detalle;
    }

    public void setCabecera_detalle(int cabecera_detalle) {
        this.cabecera_detalle = cabecera_detalle;
    }

    public int getEstado_detalle() {
        return estado_detalle;
    }

    public void setEstado_detalle(int estado_detalle) {
        this.estado_detalle = estado_detalle;
    }

    public int getId_det_vis_prop_detalle() {
        return id_det_vis_prop_detalle;
    }

    public void setId_det_vis_prop_detalle(int id_det_vis_prop_detalle) {
        this.id_det_vis_prop_detalle = id_det_vis_prop_detalle;
    }

    public int getId_visita_detalle() {
        return id_visita_detalle;
    }

    public void setId_visita_detalle(int id_visita_detalle) {
        this.id_visita_detalle = id_visita_detalle;
    }

    public int getId_prop_mat_cli_detalle() {
        return id_prop_mat_cli_detalle;
    }

    public void setId_prop_mat_cli_detalle(int id_prop_mat_cli_detalle) {
        this.id_prop_mat_cli_detalle = id_prop_mat_cli_detalle;
    }

    public String getValor_detalle() {
        return valor_detalle;
    }

    public void setValor_detalle(String valor_detalle) {
        this.valor_detalle = valor_detalle;
    }
}
