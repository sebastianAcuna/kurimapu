package cl.smapdev.curimapu.clases.adapters;

public class SpinnerItemImp implements SpinnerItem {
    private int id;
    private String descripcion;

    public SpinnerItemImp(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
