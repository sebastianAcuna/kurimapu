package cl.smapdev.curimapu.clases;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "agricultor")
public class Agricultor {

    @PrimaryKey
    @NonNull
    private String rut_agricultor;
    private String nombre_agricultor;
    private String telefono_agricultor;
    private String administrador_agricultor;
    private String telefono_admin_agricultor;
    private int region_agricultor;
    private int comuna_agricultor;
    private boolean agricultor_subido; /* true : subido, false : no subido*/

    public Agricultor() {
    }

    @Ignore
    public Agricultor(@NonNull String rut_agricultor, String nombre_agricultor, String telefono_agricultor, String administrador_agricultor, String telefono_admin_agricultor, int region_agricultor, int comuna_agricultor, boolean agricultor_subido) {
        this.rut_agricultor = rut_agricultor;
        this.nombre_agricultor = nombre_agricultor;
        this.telefono_agricultor = telefono_agricultor;
        this.administrador_agricultor = administrador_agricultor;
        this.telefono_admin_agricultor = telefono_admin_agricultor;
        this.region_agricultor = region_agricultor;
        this.comuna_agricultor = comuna_agricultor;
        this.agricultor_subido = agricultor_subido;
    }

    public String getRut_agricultor() {
        return rut_agricultor;
    }

    public void setRut_agricultor(String rut_agricultor) {
        this.rut_agricultor = rut_agricultor;
    }

    public String getNombre_agricultor() {
        return nombre_agricultor;
    }

    public void setNombre_agricultor(String nombre_agricultor) {
        this.nombre_agricultor = nombre_agricultor;
    }

    public String getTelefono_agricultor() {
        return telefono_agricultor;
    }

    public void setTelefono_agricultor(String telefono_agricultor) {
        this.telefono_agricultor = telefono_agricultor;
    }

    public String getAdministrador_agricultor() {
        return administrador_agricultor;
    }

    public void setAdministrador_agricultor(String administrador_agricultor) {
        this.administrador_agricultor = administrador_agricultor;
    }

    public String getTelefono_admin_agricultor() {
        return telefono_admin_agricultor;
    }

    public void setTelefono_admin_agricultor(String telefono_admin_agricultor) {
        this.telefono_admin_agricultor = telefono_admin_agricultor;
    }

    public int getRegion_agricultor() {
        return region_agricultor;
    }

    public void setRegion_agricultor(int region_agricultor) {
        this.region_agricultor = region_agricultor;
    }

    public int getComuna_agricultor() {
        return comuna_agricultor;
    }

    public void setComuna_agricultor(int comuna_agricultor) {
        this.comuna_agricultor = comuna_agricultor;
    }

    public boolean isAgricultor_subido() {
        return agricultor_subido;
    }

    public void setAgricultor_subido(boolean agricultor_subido) {
        this.agricultor_subido = agricultor_subido;
    }
}
