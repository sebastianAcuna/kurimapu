package cl.smapdev.curimapu.clases.relaciones;

import androidx.room.Embedded;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.Temporada;
import cl.smapdev.curimapu.clases.tablas.pro_cli_mat;

public class GsonDescargas {


    @SerializedName("array_detalle_prop")
    @Embedded
    private List<pro_cli_mat> pro_cli_matList;


    @SerializedName("array_temporada")
    @Embedded
    private List<Temporada> temporadas;


    public List<pro_cli_mat> getPro_cli_matList() {
        return pro_cli_matList;
    }

    public void setPro_cli_matList(List<pro_cli_mat> pro_cli_matList) {
        this.pro_cli_matList = pro_cli_matList;
    }

    public List<Temporada> getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(List<Temporada> temporadas) {
        this.temporadas = temporadas;
    }
}
