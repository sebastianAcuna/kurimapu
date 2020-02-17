package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "especie")
public class Especie {

    @PrimaryKey(autoGenerate = true)
    private int id_especie;

    private String desc_especie;


    public Especie() {
    }

    @Ignore
    public Especie(String desc_especie) {

        this.desc_especie = desc_especie;
    }


    public int getId_especie() {
        return id_especie;
    }

    public void setId_especie(int id_especie) {
        this.id_especie = id_especie;
    }

    public String getDesc_especie() {
        return desc_especie;
    }

    public void setDesc_especie(String desc_especie) {
        this.desc_especie = desc_especie;
    }
}
