package cl.smapdev.curimapu.clases;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "comuna")
public class Comuna {

    @PrimaryKey(autoGenerate = true)
    private int id_comuna;
    private String desc_comuna;
    private int id_region_comuna;


    public Comuna() {
    }


    @Ignore
    public Comuna(String desc_comuna, int id_region_comuna) {
        this.desc_comuna = desc_comuna;
        this.id_region_comuna = id_region_comuna;
    }

    public int getId_comuna() {
        return id_comuna;
    }

    public void setId_comuna(int id_comuna) {
        this.id_comuna = id_comuna;
    }

    public String getDesc_comuna() {
        return desc_comuna;
    }

    public void setDesc_comuna(String desc_comuna) {
        this.desc_comuna = desc_comuna;
    }

    public int getId_region_comuna() {
        return id_region_comuna;
    }

    public void setId_region_comuna(int id_region_comuna) {
        this.id_region_comuna = id_region_comuna;
    }
}
