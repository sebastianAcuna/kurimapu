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

    private String servidorSeleccionado;

    private int id_usuario_suplandato;


    private int multi_temporada;


    public int getMulti_temporada() {
        return multi_temporada;
    }

    public void setMulti_temporada(int multi_temporada) {
        this.multi_temporada = multi_temporada;
    }

    public String getServidorSeleccionado() {
        return servidorSeleccionado;
    }

    public void setServidorSeleccionado(String servidorSeleccionado) {
        this.servidorSeleccionado = servidorSeleccionado;
    }

    public Config() {
    }


    public int getId_usuario_suplandato() {
        return id_usuario_suplandato;
    }

    public void setId_usuario_suplandato(int id_usuario_suplandato) {
        this.id_usuario_suplandato = id_usuario_suplandato;
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
