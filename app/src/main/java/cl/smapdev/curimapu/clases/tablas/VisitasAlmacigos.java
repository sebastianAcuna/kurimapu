package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "almacigos_visita")
public class VisitasAlmacigos {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id_visita_local")
    @Expose
    private int id_visita_local;


    @SerializedName("id_visita")
    @Expose
    private int id_visita;


    @SerializedName("uid_visita")
    @Expose
    private String uid_visita;


    @SerializedName("id_valor_post_siembra")
    @Expose
    private int id_valor_post_siembra;


    @SerializedName("dias_cultivo_a_visita")
    @Expose
    private int dias_cultivo_a_visita;


    @SerializedName("estado_crecimiento")
    @Expose
    private String estado_crecimiento;


    @SerializedName("estado_maleza")
    @Expose
    private String estado_maleza;


    @SerializedName("estado_fito")
    @Expose
    private String estado_fito;

    @SerializedName("humedad_suelo")
    @Expose
    private String humedad_suelo;


    @SerializedName("n_hoja")
    @Expose
    private int n_hoja;

    @SerializedName("altura")
    @Expose
    private double altura;


    @SerializedName("cobertura_raices")
    @Expose
    private double cobertura_raices;


    @SerializedName("dureza")
    @Expose
    private String dureza;

    @SerializedName("uniformidad")
    @Expose
    private double uniformidad;


    @SerializedName("estado_general")
    @Expose
    private String estado_general;

    @SerializedName("diametro")
    @Expose
    private double diametro;

    @SerializedName("emergencia_preliminar")
    @Expose
    private String emergencia_preliminar;

    @SerializedName("comentario")
    @Expose
    private String comentario;

    @SerializedName("id_user_crea")
    @Expose
    private int id_user_crea;

    @SerializedName("fecha_hora_crea")
    @Expose
    private String fecha_hora_crea;

    @SerializedName("estado_sincronizacion")
    @Expose
    private int estado_sincronizacion;

    @SerializedName("obs_crecimiento")
    @Expose
    private String obs_crecimiento;
    @SerializedName("obs_maleza")
    @Expose
    private String obs_maleza;
    @SerializedName("obs_fito")
    @Expose
    private String obs_fito;
    @SerializedName("obs_humedad")
    @Expose
    private String obs_humedad;
    @SerializedName("obs_dureza")
    @Expose
    private String obs_dureza;
    @SerializedName("obs_estado_general")
    @Expose
    private String obs_estado_general;


    public String getObs_crecimiento() {
        return obs_crecimiento;
    }

    public void setObs_crecimiento(String obs_crecimiento) {
        this.obs_crecimiento = obs_crecimiento;
    }

    public String getObs_maleza() {
        return obs_maleza;
    }

    public void setObs_maleza(String obs_maleza) {
        this.obs_maleza = obs_maleza;
    }

    public String getObs_fito() {
        return obs_fito;
    }

    public void setObs_fito(String obs_fito) {
        this.obs_fito = obs_fito;
    }

    public String getObs_humedad() {
        return obs_humedad;
    }

    public void setObs_humedad(String obs_humedad) {
        this.obs_humedad = obs_humedad;
    }

    public String getObs_dureza() {
        return obs_dureza;
    }

    public void setObs_dureza(String obs_dureza) {
        this.obs_dureza = obs_dureza;
    }

    public String getObs_estado_general() {
        return obs_estado_general;
    }

    public void setObs_estado_general(String obs_estado_general) {
        this.obs_estado_general = obs_estado_general;
    }

    public int getId_visita_local() {
        return id_visita_local;
    }

    public void setId_visita_local(int id_visita_local) {
        this.id_visita_local = id_visita_local;
    }

    public int getId_visita() {
        return id_visita;
    }

    public void setId_visita(int id_visita) {
        this.id_visita = id_visita;
    }

    public String getUid_visita() {
        return uid_visita;
    }

    public void setUid_visita(String uid_visita) {
        this.uid_visita = uid_visita;
    }

    public int getId_valor_post_siembra() {
        return id_valor_post_siembra;
    }

    public void setId_valor_post_siembra(int id_valor_post_siembra) {
        this.id_valor_post_siembra = id_valor_post_siembra;
    }

    public int getDias_cultivo_a_visita() {
        return dias_cultivo_a_visita;
    }

    public void setDias_cultivo_a_visita(int dias_cultivo_a_visita) {
        this.dias_cultivo_a_visita = dias_cultivo_a_visita;
    }

    public String getEstado_crecimiento() {
        return estado_crecimiento;
    }

    public void setEstado_crecimiento(String estado_crecimiento) {
        this.estado_crecimiento = estado_crecimiento;
    }

    public String getEstado_maleza() {
        return estado_maleza;
    }

    public void setEstado_maleza(String estado_maleza) {
        this.estado_maleza = estado_maleza;
    }

    public String getEstado_fito() {
        return estado_fito;
    }

    public void setEstado_fito(String estado_fito) {
        this.estado_fito = estado_fito;
    }

    public String getHumedad_suelo() {
        return humedad_suelo;
    }

    public void setHumedad_suelo(String humedad_suelo) {
        this.humedad_suelo = humedad_suelo;
    }

    public int getN_hoja() {
        return n_hoja;
    }

    public void setN_hoja(int n_hoja) {
        this.n_hoja = n_hoja;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getCobertura_raices() {
        return cobertura_raices;
    }

    public void setCobertura_raices(double cobertura_raices) {
        this.cobertura_raices = cobertura_raices;
    }

    public String getDureza() {
        return dureza;
    }

    public void setDureza(String dureza) {
        this.dureza = dureza;
    }

    public double getUniformidad() {
        return uniformidad;
    }

    public void setUniformidad(double uniformidad) {
        this.uniformidad = uniformidad;
    }

    public String getEstado_general() {
        return estado_general;
    }

    public void setEstado_general(String estado_general) {
        this.estado_general = estado_general;
    }

    public double getDiametro() {
        return diametro;
    }

    public void setDiametro(double diametro) {
        this.diametro = diametro;
    }

    public String getEmergencia_preliminar() {
        return emergencia_preliminar;
    }

    public void setEmergencia_preliminar(String emergencia_preliminar) {
        this.emergencia_preliminar = emergencia_preliminar;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getId_user_crea() {
        return id_user_crea;
    }

    public void setId_user_crea(int id_user_crea) {
        this.id_user_crea = id_user_crea;
    }

    public String getFecha_hora_crea() {
        return fecha_hora_crea;
    }

    public void setFecha_hora_crea(String fecha_hora_crea) {
        this.fecha_hora_crea = fecha_hora_crea;
    }

    public int getEstado_sincronizacion() {
        return estado_sincronizacion;
    }

    public void setEstado_sincronizacion(int estado_sincronizacion) {
        this.estado_sincronizacion = estado_sincronizacion;
    }
}
