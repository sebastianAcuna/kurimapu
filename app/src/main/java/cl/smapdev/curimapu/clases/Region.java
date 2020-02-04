package cl.smapdev.curimapu.clases;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "region")
public class Region {

    public Region() {
    }

//    public Region(String desc_region) {
//        this.desc_region = desc_region;
//    }

    @PrimaryKey(autoGenerate = true)
    private int id_region;
    private String desc_region;


    public int getId_region() {
        return id_region;
    }

    public void setId_region(int id_region) {
        this.id_region = id_region;
    }

    public String getDesc_region() {
        return desc_region;
    }

    public void setDesc_region(String desc_region) {
        this.desc_region = desc_region;
    }
}
