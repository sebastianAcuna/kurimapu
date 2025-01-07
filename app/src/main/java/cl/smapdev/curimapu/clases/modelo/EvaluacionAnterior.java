package cl.smapdev.curimapu.clases.modelo;

public class EvaluacionAnterior {

    public int id_evaluacion;
    public float evaluacion;
    public String comentario;

    public EvaluacionAnterior(int id_evaluacion, float evaluacion, String comentario) {
        this.id_evaluacion = id_evaluacion;
        this.evaluacion = evaluacion;
        this.comentario = comentario;
    }

    public void clean() {
        this.id_evaluacion = 0;
        this.evaluacion = 0;
        this.comentario = "";
    }

    public EvaluacionAnterior getInstance() {
        return this;
    }


}
