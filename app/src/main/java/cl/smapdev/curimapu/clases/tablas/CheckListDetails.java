package cl.smapdev.curimapu.clases.tablas;

public class CheckListDetails {

    private int id;
    private String  description;
    private String  idAnexo;
    private boolean isUploaded;
    private String  estado;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdAnexo() {
        return idAnexo;
    }

    public void setIdAnexo(String idAnexo) {
        this.idAnexo = idAnexo;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
