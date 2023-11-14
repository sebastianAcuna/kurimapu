package cl.smapdev.curimapu.clases.utilidades;

public interface DescargaImagenesI {
    void imageCompleted(int porcentaje);
    void erroredImage(String error);
}
