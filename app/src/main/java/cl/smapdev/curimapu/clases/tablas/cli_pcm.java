package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cli_pcm")
public class cli_pcm {

    @PrimaryKey
    private int id_cli_pcm;

    private int id_cli;

    private int id_prop_mat_cli;

    private int ver;

    private int registrar;

    public int getId_cli_pcm() {
        return id_cli_pcm;
    }

    public void setId_cli_pcm(int id_cli_pcm) {
        this.id_cli_pcm = id_cli_pcm;
    }

    public int getId_cli() {
        return id_cli;
    }

    public void setId_cli(int id_cli) {
        this.id_cli = id_cli;
    }

    public int getId_prop_mat_cli() {
        return id_prop_mat_cli;
    }

    public void setId_prop_mat_cli(int id_prop_mat_cli) {
        this.id_prop_mat_cli = id_prop_mat_cli;
    }

    public int getVer() {
        return ver;
    }

    public void setVer(int ver) {
        this.ver = ver;
    }

    public int getRegistrar() {
        return registrar;
    }

    public void setRegistrar(int registrar) {
        this.registrar = registrar;
    }
}
