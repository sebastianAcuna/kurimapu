package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "visitas")
public class Visitas {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id_visita")
    @Expose
    private int id_visita;
    @SerializedName("id_ac")
    @Expose
    private String id_anexo_visita;

    @SerializedName("estado_fen")
    @Expose
    private String phenological_state_visita;
    @SerializedName("estado_crec")
    @Expose
    private String growth_status_visita;
    @SerializedName("estado_male")
    @Expose
    private String weed_state_visita;
    @SerializedName("estado_fito")
    @Expose
    private String phytosanitary_state_visita;
    @SerializedName("cosecha")
    @Expose
    private String harvest_visita;
    @SerializedName("estado_fen_culti")
    @Expose
    private String overall_status_visita;
    @SerializedName("hum_del_suelo")
    @Expose
    private String humidity_floor_visita;
    @SerializedName("obs")
    @Expose
    private String observation_visita;
    @SerializedName("recome")
    @Expose
    private String recomendation_visita;

    @SerializedName("estado_server_visitas")
    @Expose
    private int estado_server_visitas;

    @SerializedName("id_est_vis")
    @Expose
    private int estado_visita; /* 1 : EFECTUADA; 2: NO EFECTUADA ; 3:POR EFECTUAR */

    @SerializedName("etapa_visitas")
    @Expose
    private int etapa_visitas;

    @SerializedName("fecha_r")
    @Expose
    private String fecha_visita;

    @SerializedName("hora_r")
    @Expose
    private String hora_visita;
    @SerializedName("temporada")
    @Expose
    private String temporada;

    @SerializedName("obs_fito")
    @Expose
    private String obs_fito;
    @SerializedName("obs_creci")
    @Expose
    private String obs_creci;
    @SerializedName("obs_maleza")
    @Expose
    private String obs_maleza;
    @SerializedName("obs_cosecha")
    @Expose
    private String obs_cosecha;
    @SerializedName("obs_overall")
    @Expose
    private String obs_overall;
    @SerializedName("obs_humedad")
    @Expose
    private String obs_humedad;

    @SerializedName("id_usuario")
    private int id_user_visita;

    @SerializedName("cabecera_visita")
    @Expose
    private int cabecera_visita;


    public int getId_user_visita() {
        return id_user_visita;
    }

    public void setId_user_visita(int id_user_visita) {
        this.id_user_visita = id_user_visita;
    }

    public int getCabecera_visita() {
        return cabecera_visita;
    }

    public void setCabecera_visita(int cabecera_visita) {
        this.cabecera_visita = cabecera_visita;
    }

    public String getObs_fito() {
        return obs_fito;
    }

    public void setObs_fito(String obs_fito) {
        this.obs_fito = obs_fito;
    }

    public String getObs_creci() {
        return obs_creci;
    }

    public void setObs_creci(String obs_creci) {
        this.obs_creci = obs_creci;
    }

    public String getObs_maleza() {
        return obs_maleza;
    }

    public void setObs_maleza(String obs_maleza) {
        this.obs_maleza = obs_maleza;
    }

    public String getObs_cosecha() {
        return obs_cosecha;
    }

    public void setObs_cosecha(String obs_cosecha) {
        this.obs_cosecha = obs_cosecha;
    }

    public String getObs_overall() {
        return obs_overall;
    }

    public void setObs_overall(String obs_overall) {
        this.obs_overall = obs_overall;
    }

    public String getObs_humedad() {
        return obs_humedad;
    }

    public void setObs_humedad(String obs_humedad) {
        this.obs_humedad = obs_humedad;
    }

    public String getTemporada() {
        return temporada;
    }

    public void setTemporada(String temporada) {
        this.temporada = temporada;
    }

    public String getFecha_visita() {
        return fecha_visita;
    }

    public void setFecha_visita(String fecha_visita) {
        this.fecha_visita = fecha_visita;
    }

    public String getHora_visita() {
        return hora_visita;
    }

    public void setHora_visita(String hora_visita) {
        this.hora_visita = hora_visita;
    }

    public int getEtapa_visitas() {
        return etapa_visitas;
    }

    public void setEtapa_visitas(int etapa_visitas) {
        this.etapa_visitas = etapa_visitas;
    }

    public int getEstado_visita() {
        return estado_visita;
    }

    public void setEstado_visita(int estado_visita) {
        this.estado_visita = estado_visita;
    }

    public int getEstado_server_visitas() {
        return estado_server_visitas;
    }

    public void setEstado_server_visitas(int estado_server_visitas) {
        this.estado_server_visitas = estado_server_visitas;
    }

    public int getId_visita() {
        return id_visita;
    }

    public void setId_visita(int id_visita) {
        this.id_visita = id_visita;
    }

    public String getId_anexo_visita() {
        return id_anexo_visita;
    }

    public void setId_anexo_visita(String id_anexo_visita) {
        this.id_anexo_visita = id_anexo_visita;
    }

    public String getPhenological_state_visita() {
        return phenological_state_visita;
    }

    public void setPhenological_state_visita(String phenological_state_visita) {
        this.phenological_state_visita = phenological_state_visita;
    }

    public String getGrowth_status_visita() {
        return growth_status_visita;
    }

    public void setGrowth_status_visita(String growth_status_visita) {
        this.growth_status_visita = growth_status_visita;
    }

    public String getWeed_state_visita() {
        return weed_state_visita;
    }

    public void setWeed_state_visita(String weed_state_visita) {
        this.weed_state_visita = weed_state_visita;
    }

    public String getPhytosanitary_state_visita() {
        return phytosanitary_state_visita;
    }

    public void setPhytosanitary_state_visita(String phytosanitary_state_visita) {
        this.phytosanitary_state_visita = phytosanitary_state_visita;
    }

    public String getHarvest_visita() {
        return harvest_visita;
    }

    public void setHarvest_visita(String harvest_visita) {
        this.harvest_visita = harvest_visita;
    }

    public String getOverall_status_visita() {
        return overall_status_visita;
    }

    public void setOverall_status_visita(String overall_status_visita) {
        this.overall_status_visita = overall_status_visita;
    }

    public String getHumidity_floor_visita() {
        return humidity_floor_visita;
    }

    public void setHumidity_floor_visita(String humidity_floor_visita) {
        this.humidity_floor_visita = humidity_floor_visita;
    }

    public String getObservation_visita() {
        return observation_visita;
    }

    public void setObservation_visita(String observation_visita) {
        this.observation_visita = observation_visita;
    }

    public String getRecomendation_visita() {
        return recomendation_visita;
    }

    public void setRecomendation_visita(String recomendation_visita) {
        this.recomendation_visita = recomendation_visita;
    }
}
