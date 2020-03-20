package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "config")
public class Config {

    @PrimaryKey
    private int id;

    private int id_usuario;

    private String horaDescarga;

    private String horaSubida;




    public Config() {
    }

    @Ignore
    public Config(int id) {
        this.id = id;
    }


    public String getHoraDescarga() {
        return horaDescarga;
    }

    public void setHoraDescarga(String horaDescarga) {
        this.horaDescarga = horaDescarga;
    }

    public String getHoraSubida() {
        return horaSubida;
    }

    public void setHoraSubida(String horaSubida) {
        this.horaSubida = horaSubida;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }
}
