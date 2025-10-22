package cl.smapdev.curimapu.clases.relaciones;

import androidx.annotation.NonNull;

public class SpinnerItem {

    private int id;
    private String nombre;

    public SpinnerItem(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @NonNull
    @Override
    public String toString() {
        return nombre; // 👈 importante: así se mostrará el texto correcto
    }
}
