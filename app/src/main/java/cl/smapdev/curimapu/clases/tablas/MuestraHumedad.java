package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "muestra_humedad")
public class MuestraHumedad {

    @PrimaryKey(autoGenerate = true)
    public int id_muestra_humedad;

    @SerializedName("uid_muestra")
    public String clave_unica_muestra;

    @SerializedName("id_ac_muestra")
    public  int id_ac_muestra_humedad;

    @SerializedName("valor_muestra")
    public String  muestra;

    @SerializedName("fecha_muestra")
    public String fecha_muestra;

    @SerializedName("estado_sincronizacion_muestra")
    public int estado_sincronizacion_muestrao;

    public int id_user_crea;
    @SerializedName("fecha_hora_crea")
    public String fecha_crea;
    public int id_user_mod;
    @SerializedName("fecha_hora_mod")
    public String fecha_mod;

    public int estado_documento_muestra;

    public int getId_muestra_humedad() {
        return id_muestra_humedad;
    }

    public void setId_muestra_humedad(int id_muestra_humedad) {
        this.id_muestra_humedad = id_muestra_humedad;
    }

    public String getClave_unica_muestra() {
        return clave_unica_muestra;
    }

    public void setClave_unica_muestra(String clave_unica_muestra) {
        this.clave_unica_muestra = clave_unica_muestra;
    }

    public int getId_ac_muestra_humedad() {
        return id_ac_muestra_humedad;
    }

    public void setId_ac_muestra_humedad(int id_ac_muestra_humedad) {
        this.id_ac_muestra_humedad = id_ac_muestra_humedad;
    }

    public String getMuestra() {
        return muestra;
    }

    public void setMuestra(String muestra) {
        this.muestra = muestra;
    }

    public String getFecha_muestra() {
        return fecha_muestra;
    }

    public void setFecha_muestra(String fecha_muestra) {
        this.fecha_muestra = fecha_muestra;
    }

    public int getEstado_sincronizacion_muestrao() {
        return estado_sincronizacion_muestrao;
    }

    public void setEstado_sincronizacion_muestrao(int estado_sincronizacion_muestrao) {
        this.estado_sincronizacion_muestrao = estado_sincronizacion_muestrao;
    }

    public int getId_user_crea() {
        return id_user_crea;
    }

    public void setId_user_crea(int id_user_crea) {
        this.id_user_crea = id_user_crea;
    }

    public String getFecha_crea() {
        return fecha_crea;
    }

    public void setFecha_crea(String fecha_crea) {
        this.fecha_crea = fecha_crea;
    }

    public int getId_user_mod() {
        return id_user_mod;
    }

    public void setId_user_mod(int id_user_mod) {
        this.id_user_mod = id_user_mod;
    }

    public String getFecha_mod() {
        return fecha_mod;
    }

    public void setFecha_mod(String fecha_mod) {
        this.fecha_mod = fecha_mod;
    }

    public int getEstado_documento_muestra() {
        return estado_documento_muestra;
    }

    public void setEstado_documento_muestra(int estado_documento_muestra) {
        this.estado_documento_muestra = estado_documento_muestra;
    }
}
