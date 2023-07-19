package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "anexo_vilab")
public class AnexoVilab {

    @PrimaryKey()
    public int id_vilab;

    public int id_ac;

    public String fecha_imagen_ndvi;

    public String promedio_vilab;

    public String ruta_img_vilab;

    public String nombre_imagen;


    public int getId_vilab() {
        return id_vilab;
    }

    public void setId_vilab(int id_vilab) {
        this.id_vilab = id_vilab;
    }

    public int getId_ac() {
        return id_ac;
    }

    public void setId_ac(int id_ac) {
        this.id_ac = id_ac;
    }

    public String getFecha_imagen_ndvi() {
        return fecha_imagen_ndvi;
    }

    public void setFecha_imagen_ndvi(String fecha_imagen_ndvi) {
        this.fecha_imagen_ndvi = fecha_imagen_ndvi;
    }

    public String getPromedio_vilab() {
        return promedio_vilab;
    }

    public void setPromedio_vilab(String promedio_vilab) {
        this.promedio_vilab = promedio_vilab;
    }

    public String getRuta_img_vilab() {
        return ruta_img_vilab;
    }

    public void setRuta_img_vilab(String ruta_img_vilab) {
        this.ruta_img_vilab = ruta_img_vilab;
    }

    public String getNombre_imagen() {
        return nombre_imagen;
    }

    public void setNombre_imagen(String nombre_imagen) {
        this.nombre_imagen = nombre_imagen;
    }
}
