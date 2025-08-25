package cl.smapdev.curimapu.clases.relaciones;

import java.util.List;

import cl.smapdev.curimapu.clases.tablas.FotosAlmacigos;
import cl.smapdev.curimapu.clases.tablas.VisitasAlmacigos;

public class SubirAlmacigos {

    private int idDispo;
    private String version;
    private int id_usuario;
    public List<VisitasAlmacigos> visitas;
    public List<FotosAlmacigos> fotos;


    public int getIdDispo() {
        return idDispo;
    }

    public void setIdDispo(int idDispo) {
        this.idDispo = idDispo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public List<VisitasAlmacigos> getVisitas() {
        return visitas;
    }

    public void setVisitas(List<VisitasAlmacigos> visitas) {
        this.visitas = visitas;
    }

    public List<FotosAlmacigos> getFotos() {
        return fotos;
    }

    public void setFotos(List<FotosAlmacigos> fotos) {
        this.fotos = fotos;
    }
}
