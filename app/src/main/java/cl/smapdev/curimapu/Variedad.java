package cl.smapdev.curimapu;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "variedad")
public class Variedad {

    @PrimaryKey(autoGenerate = true)
    private int id_variedad;


    private int id_especie_variedad;


    private String desc_variedad;


    public Variedad() {
    }

    @Ignore
    public Variedad(int id_especie_variedad, String desc_variedad) {
        this.id_especie_variedad = id_especie_variedad;
        this.desc_variedad = desc_variedad;
    }


    public int getId_variedad() {
        return id_variedad;
    }

    public void setId_variedad(int id_variedad) {
        this.id_variedad = id_variedad;
    }

    public int getId_especie_variedad() {
        return id_especie_variedad;
    }

    public void setId_especie_variedad(int id_especie_variedad) {
        this.id_especie_variedad = id_especie_variedad;
    }

    public String getDesc_variedad() {
        return desc_variedad;
    }

    public void setDesc_variedad(String desc_variedad) {
        this.desc_variedad = desc_variedad;
    }
}
