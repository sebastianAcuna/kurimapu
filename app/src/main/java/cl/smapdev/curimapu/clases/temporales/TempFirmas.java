package cl.smapdev.curimapu.clases.temporales;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "temp_firmas")
public class TempFirmas {

    @PrimaryKey(autoGenerate = true)
    private int id_firmas;

    private String path;

    private int tipo_documento;

    private String lugar_firma;


    public int getId_firmas() {
        return id_firmas;
    }
    public void setId_firmas(int id_firmas) {
        this.id_firmas = id_firmas;
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getTipo_documento() {
        return tipo_documento;
    }

    public void setTipo_documento(int tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public String getLugar_firma() {
        return lugar_firma;
    }

    public void setLugar_firma(String lugar_firma) {
        this.lugar_firma = lugar_firma;
    }
}
