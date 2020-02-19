package cl.smapdev.curimapu.clases.relaciones;

import com.google.gson.annotations.SerializedName;

public class CantidadVisitas {

    private int todos;


    private int etapa_visitas;

    public int getTodos() {
        return todos;
    }

    public void setTodos(int todos) {
        this.todos = todos;
    }

    public int getEtapa_visitas() {
        return etapa_visitas;
    }

    public void setEtapa_visitas(int etapa_visitas) {
        this.etapa_visitas = etapa_visitas;
    }
}
