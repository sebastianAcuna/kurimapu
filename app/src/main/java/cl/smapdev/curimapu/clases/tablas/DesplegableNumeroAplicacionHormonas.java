package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "desplegable_numero_aplicacion_hormonas")
public class DesplegableNumeroAplicacionHormonas {


    @PrimaryKey(autoGenerate = true)
    private int id;

    private String numeroAplicacion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumeroAplicacion() {
        return numeroAplicacion;
    }


    public void setNumeroAplicacion(String numeroAplicacion) {
        this.numeroAplicacion = numeroAplicacion;
    }
}
