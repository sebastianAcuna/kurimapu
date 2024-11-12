package cl.smapdev.curimapu.clases.tablas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;

@Entity(tableName = "anexo_checklist_siembra")
public class CheckListSiembra {

    @PrimaryKey(autoGenerate = true)
    @Expose
    private int id_cl_siembra;
    @Expose
    private int id_ac_cl_siembra;
    @Expose
    private String apellido_checklist;
    @Expose
    private int estado_sincronizacion;
    @Expose
    private int estado_documento;
    @Expose
    private String clave_unica;
    @Expose
    private int id_usuario;

    @Expose
    private String fecha_hora_tx;

    @Expose
    private String fecha_hora_mod;

    @Expose
    private int id_usuario_mod;

    //cabecera
    @Expose
    private String prestador_servicio;
    @Expose
    private String n_maquina;
    @Expose
    private String operador;
    @Expose
    private String fecha_siembra;
    @Expose
    private String hora_inicio;
    @Expose
    private String hora_termino;
    @Expose
    private String linea;
    @Expose
    private String registro_anpros;
    @Expose
    private String superficie_sembrada;
    @Expose
    private String peso_real_stock_seed;
    @Expose
    private String propuesta;
    @Expose
    private String ruta_foto_envase;
    @Expose
    private String ruta_foto_semilla;
    @Expose
    private String stringed_foto_envase;
    @Expose
    private String stringed_foto_semilla;
    @Expose
    private String observaciones;

    //preparacion suelo
    @Expose
    private String estado_cama_raices;
    @Expose
    private String estado_cama_semilla;
    @Expose
    private String humedad_suelo;
    @Expose
    private String micro_nivelacion;
    @Expose
    private String compactacion;

    // limpieza maquina
    @Expose
    private String tarros_sembradores_pre;
    @Expose
    private String tarros_sembradores_post;
    @Expose
    private String disco_pre;
    @Expose
    private String disco_post;
    @Expose
    private String linea_anterior;

    //regulacion maquina

    @Expose
    private String distancia_hileras_t1;
    @Expose
    private String distancia_hileras_t2;
    @Expose
    private String distancia_hileras_t3;
    @Expose
    private String distancia_hileras_t4;
    @Expose
    private String distancia_hileras_t5;
    @Expose
    private String distancia_hileras_t6;


    @Expose
    private String n_semillas_t1;
    @Expose
    private String n_semillas_t2;
    @Expose
    private String n_semillas_t3;
    @Expose
    private String n_semillas_t4;
    @Expose
    private String n_semillas_t5;
    @Expose
    private String n_semillas_t6;


    @Expose
    private String prof_siembra_t1;
    @Expose
    private String prof_siembra_t2;
    @Expose
    private String prof_siembra_t3;
    @Expose
    private String prof_siembra_t4;
    @Expose
    private String prof_siembra_t5;
    @Expose
    private String prof_siembra_t6;


    @Expose
    private String prof_fertilizante_t1;
    @Expose
    private String prof_fertilizante_t2;
    @Expose
    private String prof_fertilizante_t3;
    @Expose
    private String prof_fertilizante_t4;
    @Expose
    private String prof_fertilizante_t5;
    @Expose
    private String prof_fertilizante_t6;


    @Expose
    private String dist_fet_semilla_t1;
    @Expose
    private String dist_fet_semilla_t2;
    @Expose
    private String dist_fet_semilla_t3;
    @Expose
    private String dist_fet_semilla_t4;
    @Expose
    private String dist_fet_semilla_t5;
    @Expose
    private String dist_fet_semilla_t6;


    @Expose
    private String hilera;
    @Expose
    private String kg_mezcla;
    @Expose
    private String relacion_n;
    @Expose
    private String relacion_p;
    @Expose
    private String relacion_k;
    @Expose
    private String relacion_m;
    @Expose
    private String relacion_h;
    @Expose
    private String regulacion_pinones;
    @Expose
    private String sistema_fertilizacion;


    public String getFecha_hora_tx() {
        return fecha_hora_tx;
    }

    public void setFecha_hora_tx(String fecha_hora_tx) {
        this.fecha_hora_tx = fecha_hora_tx;
    }

    public String getFecha_hora_mod() {
        return fecha_hora_mod;
    }

    public void setFecha_hora_mod(String fecha_hora_mod) {
        this.fecha_hora_mod = fecha_hora_mod;
    }

    public int getId_usuario_mod() {
        return id_usuario_mod;
    }

    public void setId_usuario_mod(int id_usuario_mod) {
        this.id_usuario_mod = id_usuario_mod;
    }

    public String getDistancia_hileras_t1() {
        return distancia_hileras_t1;
    }

    public void setDistancia_hileras_t1(String distancia_hileras_t1) {
        this.distancia_hileras_t1 = distancia_hileras_t1;
    }

    public String getDistancia_hileras_t2() {
        return distancia_hileras_t2;
    }

    public void setDistancia_hileras_t2(String distancia_hileras_t2) {
        this.distancia_hileras_t2 = distancia_hileras_t2;
    }

    public String getDistancia_hileras_t3() {
        return distancia_hileras_t3;
    }

    public void setDistancia_hileras_t3(String distancia_hileras_t3) {
        this.distancia_hileras_t3 = distancia_hileras_t3;
    }

    public String getDistancia_hileras_t4() {
        return distancia_hileras_t4;
    }

    public void setDistancia_hileras_t4(String distancia_hileras_t4) {
        this.distancia_hileras_t4 = distancia_hileras_t4;
    }

    public String getDistancia_hileras_t5() {
        return distancia_hileras_t5;
    }

    public void setDistancia_hileras_t5(String distancia_hileras_t5) {
        this.distancia_hileras_t5 = distancia_hileras_t5;
    }

    public String getDistancia_hileras_t6() {
        return distancia_hileras_t6;
    }

    public void setDistancia_hileras_t6(String distancia_hileras_t6) {
        this.distancia_hileras_t6 = distancia_hileras_t6;
    }

    public String getN_semillas_t1() {
        return n_semillas_t1;
    }

    public void setN_semillas_t1(String n_semillas_t1) {
        this.n_semillas_t1 = n_semillas_t1;
    }

    public String getN_semillas_t2() {
        return n_semillas_t2;
    }

    public void setN_semillas_t2(String n_semillas_t2) {
        this.n_semillas_t2 = n_semillas_t2;
    }

    public String getN_semillas_t3() {
        return n_semillas_t3;
    }

    public void setN_semillas_t3(String n_semillas_t3) {
        this.n_semillas_t3 = n_semillas_t3;
    }

    public String getN_semillas_t4() {
        return n_semillas_t4;
    }

    public void setN_semillas_t4(String n_semillas_t4) {
        this.n_semillas_t4 = n_semillas_t4;
    }

    public String getN_semillas_t5() {
        return n_semillas_t5;
    }

    public void setN_semillas_t5(String n_semillas_t5) {
        this.n_semillas_t5 = n_semillas_t5;
    }

    public String getN_semillas_t6() {
        return n_semillas_t6;
    }

    public void setN_semillas_t6(String n_semillas_t6) {
        this.n_semillas_t6 = n_semillas_t6;
    }

    public String getProf_siembra_t1() {
        return prof_siembra_t1;
    }

    public void setProf_siembra_t1(String prof_siembra_t1) {
        this.prof_siembra_t1 = prof_siembra_t1;
    }

    public String getProf_siembra_t2() {
        return prof_siembra_t2;
    }

    public void setProf_siembra_t2(String prof_siembra_t2) {
        this.prof_siembra_t2 = prof_siembra_t2;
    }

    public String getProf_siembra_t3() {
        return prof_siembra_t3;
    }

    public void setProf_siembra_t3(String prof_siembra_t3) {
        this.prof_siembra_t3 = prof_siembra_t3;
    }

    public String getProf_siembra_t4() {
        return prof_siembra_t4;
    }

    public void setProf_siembra_t4(String prof_siembra_t4) {
        this.prof_siembra_t4 = prof_siembra_t4;
    }

    public String getProf_siembra_t5() {
        return prof_siembra_t5;
    }

    public void setProf_siembra_t5(String prof_siembra_t5) {
        this.prof_siembra_t5 = prof_siembra_t5;
    }

    public String getProf_siembra_t6() {
        return prof_siembra_t6;
    }

    public void setProf_siembra_t6(String prof_siembra_t6) {
        this.prof_siembra_t6 = prof_siembra_t6;
    }

    public String getProf_fertilizante_t1() {
        return prof_fertilizante_t1;
    }

    public void setProf_fertilizante_t1(String prof_fertilizante_t1) {
        this.prof_fertilizante_t1 = prof_fertilizante_t1;
    }

    public String getProf_fertilizante_t2() {
        return prof_fertilizante_t2;
    }

    public void setProf_fertilizante_t2(String prof_fertilizante_t2) {
        this.prof_fertilizante_t2 = prof_fertilizante_t2;
    }

    public String getProf_fertilizante_t3() {
        return prof_fertilizante_t3;
    }

    public void setProf_fertilizante_t3(String prof_fertilizante_t3) {
        this.prof_fertilizante_t3 = prof_fertilizante_t3;
    }

    public String getProf_fertilizante_t4() {
        return prof_fertilizante_t4;
    }

    public void setProf_fertilizante_t4(String prof_fertilizante_t4) {
        this.prof_fertilizante_t4 = prof_fertilizante_t4;
    }

    public String getProf_fertilizante_t5() {
        return prof_fertilizante_t5;
    }

    public void setProf_fertilizante_t5(String prof_fertilizante_t5) {
        this.prof_fertilizante_t5 = prof_fertilizante_t5;
    }

    public String getProf_fertilizante_t6() {
        return prof_fertilizante_t6;
    }

    public void setProf_fertilizante_t6(String prof_fertilizante_t6) {
        this.prof_fertilizante_t6 = prof_fertilizante_t6;
    }

    public String getDist_fet_semilla_t1() {
        return dist_fet_semilla_t1;
    }

    public void setDist_fet_semilla_t1(String dist_fet_semilla_t1) {
        this.dist_fet_semilla_t1 = dist_fet_semilla_t1;
    }

    public String getDist_fet_semilla_t2() {
        return dist_fet_semilla_t2;
    }

    public void setDist_fet_semilla_t2(String dist_fet_semilla_t2) {
        this.dist_fet_semilla_t2 = dist_fet_semilla_t2;
    }

    public String getDist_fet_semilla_t3() {
        return dist_fet_semilla_t3;
    }

    public void setDist_fet_semilla_t3(String dist_fet_semilla_t3) {
        this.dist_fet_semilla_t3 = dist_fet_semilla_t3;
    }

    public String getDist_fet_semilla_t4() {
        return dist_fet_semilla_t4;
    }

    public void setDist_fet_semilla_t4(String dist_fet_semilla_t4) {
        this.dist_fet_semilla_t4 = dist_fet_semilla_t4;
    }

    public String getDist_fet_semilla_t5() {
        return dist_fet_semilla_t5;
    }

    public void setDist_fet_semilla_t5(String dist_fet_semilla_t5) {
        this.dist_fet_semilla_t5 = dist_fet_semilla_t5;
    }

    public String getDist_fet_semilla_t6() {
        return dist_fet_semilla_t6;
    }

    public void setDist_fet_semilla_t6(String dist_fet_semilla_t6) {
        this.dist_fet_semilla_t6 = dist_fet_semilla_t6;
    }

    public int getId_cl_siembra() {
        return id_cl_siembra;
    }

    public void setId_cl_siembra(int id_cl_siembra) {
        this.id_cl_siembra = id_cl_siembra;
    }

    public int getId_ac_cl_siembra() {
        return id_ac_cl_siembra;
    }

    public void setId_ac_cl_siembra(int id_ac_cl_siembra) {
        this.id_ac_cl_siembra = id_ac_cl_siembra;
    }

    public String getApellido_checklist() {
        return apellido_checklist;
    }

    public void setApellido_checklist(String apellido_checklist) {
        this.apellido_checklist = apellido_checklist;
    }

    public int getEstado_sincronizacion() {
        return estado_sincronizacion;
    }

    public void setEstado_sincronizacion(int estado_sincronizacion) {
        this.estado_sincronizacion = estado_sincronizacion;
    }

    public int getEstado_documento() {
        return estado_documento;
    }

    public void setEstado_documento(int estado_documento) {
        this.estado_documento = estado_documento;
    }

    public String getClave_unica() {
        return clave_unica;
    }

    public void setClave_unica(String clave_unica) {
        this.clave_unica = clave_unica;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getPrestador_servicio() {
        return prestador_servicio;
    }

    public void setPrestador_servicio(String prestador_servicio) {
        this.prestador_servicio = prestador_servicio;
    }

    public String getN_maquina() {
        return n_maquina;
    }

    public void setN_maquina(String n_maquina) {
        this.n_maquina = n_maquina;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public String getFecha_siembra() {
        return fecha_siembra;
    }

    public void setFecha_siembra(String fecha_siembra) {
        this.fecha_siembra = fecha_siembra;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public String getHora_termino() {
        return hora_termino;
    }

    public void setHora_termino(String hora_termino) {
        this.hora_termino = hora_termino;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getRegistro_anpros() {
        return registro_anpros;
    }

    public void setRegistro_anpros(String registro_anpros) {
        this.registro_anpros = registro_anpros;
    }

    public String getSuperficie_sembrada() {
        return superficie_sembrada;
    }

    public void setSuperficie_sembrada(String superficie_sembrada) {
        this.superficie_sembrada = superficie_sembrada;
    }

    public String getPeso_real_stock_seed() {
        return peso_real_stock_seed;
    }

    public void setPeso_real_stock_seed(String peso_real_stock_seed) {
        this.peso_real_stock_seed = peso_real_stock_seed;
    }

    public String getPropuesta() {
        return propuesta;
    }

    public void setPropuesta(String propuesta) {
        this.propuesta = propuesta;
    }

    public String getRuta_foto_envase() {
        return ruta_foto_envase;
    }

    public void setRuta_foto_envase(String ruta_foto_envase) {
        this.ruta_foto_envase = ruta_foto_envase;
    }

    public String getRuta_foto_semilla() {
        return ruta_foto_semilla;
    }

    public void setRuta_foto_semilla(String ruta_foto_semilla) {
        this.ruta_foto_semilla = ruta_foto_semilla;
    }

    public String getStringed_foto_envase() {
        return stringed_foto_envase;
    }

    public void setStringed_foto_envase(String stringed_foto_envase) {
        this.stringed_foto_envase = stringed_foto_envase;
    }

    public String getStringed_foto_semilla() {
        return stringed_foto_semilla;
    }

    public void setStringed_foto_semilla(String stringed_foto_semilla) {
        this.stringed_foto_semilla = stringed_foto_semilla;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEstado_cama_raices() {
        return estado_cama_raices;
    }

    public void setEstado_cama_raices(String estado_cama_raices) {
        this.estado_cama_raices = estado_cama_raices;
    }

    public String getEstado_cama_semilla() {
        return estado_cama_semilla;
    }

    public void setEstado_cama_semilla(String estado_cama_semilla) {
        this.estado_cama_semilla = estado_cama_semilla;
    }

    public String getHumedad_suelo() {
        return humedad_suelo;
    }

    public void setHumedad_suelo(String humedad_suelo) {
        this.humedad_suelo = humedad_suelo;
    }

    public String getMicro_nivelacion() {
        return micro_nivelacion;
    }

    public void setMicro_nivelacion(String micro_nivelacion) {
        this.micro_nivelacion = micro_nivelacion;
    }

    public String getCompactacion() {
        return compactacion;
    }

    public void setCompactacion(String compactacion) {
        this.compactacion = compactacion;
    }

    public String getTarros_sembradores_pre() {
        return tarros_sembradores_pre;
    }

    public void setTarros_sembradores_pre(String tarros_sembradores_pre) {
        this.tarros_sembradores_pre = tarros_sembradores_pre;
    }

    public String getTarros_sembradores_post() {
        return tarros_sembradores_post;
    }

    public void setTarros_sembradores_post(String tarros_sembradores_post) {
        this.tarros_sembradores_post = tarros_sembradores_post;
    }

    public String getDisco_pre() {
        return disco_pre;
    }

    public void setDisco_pre(String disco_pre) {
        this.disco_pre = disco_pre;
    }

    public String getDisco_post() {
        return disco_post;
    }

    public void setDisco_post(String disco_post) {
        this.disco_post = disco_post;
    }

    public String getLinea_anterior() {
        return linea_anterior;
    }

    public void setLinea_anterior(String linea_anterior) {
        this.linea_anterior = linea_anterior;
    }

    public String getHilera() {
        return hilera;
    }

    public void setHilera(String hilera) {
        this.hilera = hilera;
    }

    public String getKg_mezcla() {
        return kg_mezcla;
    }

    public void setKg_mezcla(String kg_mezcla) {
        this.kg_mezcla = kg_mezcla;
    }

    public String getRelacion_n() {
        return relacion_n;
    }

    public void setRelacion_n(String relacion_n) {
        this.relacion_n = relacion_n;
    }

    public String getRelacion_p() {
        return relacion_p;
    }

    public void setRelacion_p(String relacion_p) {
        this.relacion_p = relacion_p;
    }

    public String getRelacion_k() {
        return relacion_k;
    }

    public void setRelacion_k(String relacion_k) {
        this.relacion_k = relacion_k;
    }

    public String getRelacion_m() {
        return relacion_m;
    }

    public void setRelacion_m(String relacion_m) {
        this.relacion_m = relacion_m;
    }

    public String getRelacion_h() {
        return relacion_h;
    }

    public void setRelacion_h(String relacion_h) {
        this.relacion_h = relacion_h;
    }

    public String getRegulacion_pinones() {
        return regulacion_pinones;
    }

    public void setRegulacion_pinones(String regulacion_pinones) {
        this.regulacion_pinones = regulacion_pinones;
    }

    public String getSistema_fertilizacion() {
        return sistema_fertilizacion;
    }

    public void setSistema_fertilizacion(String sistema_fertilizacion) {
        this.sistema_fertilizacion = sistema_fertilizacion;
    }
}
