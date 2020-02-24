package cl.smapdev.curimapu.clases.relaciones;

import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;

public class CantidadVisitas {

    private int todos;


    private int etapa_visitas;


    private int estado_visita;


    public int getEstado_visita() {
        return estado_visita;
    }

    public void setEstado_visita(int estado_visita) {
        this.estado_visita = estado_visita;
    }

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
