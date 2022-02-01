package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "errores")
public class Errores  {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id_error")
    @Expose
    private int id_error;

    @SerializedName("mensaje_error")
    @Expose
    private String mensaje_error;

    @SerializedName("codigo_error")
    @Expose
    private int codigo_error;

    private int estado_error;

    private int cabecera_error;






    public int getCabecera_error() {
        return cabecera_error;
    }

    public void setCabecera_error(int cabecera_error) {
        this.cabecera_error = cabecera_error;
    }

    public int getEstado_error() {
        return estado_error;
    }

    public void setEstado_error(int estado_error) {
        this.estado_error = estado_error;
    }

    public int getId_error() {
        return id_error;
    }

    public void setId_error(int id_error) {
        this.id_error = id_error;
    }

    public String getMensaje_error() {
        return mensaje_error;
    }

    public void setMensaje_error(String mensaje_error) {
        this.mensaje_error = mensaje_error;
    }

    public int getCodigo_error() {
        return codigo_error;
    }

    public void setCodigo_error(int codigo_error) {
        this.codigo_error = codigo_error;
    }
}
