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

    //suelo
    @Expose
    private int chequeo_aislacion; //si, no
    @Expose
    private String cama_semilla_cultivo_anterior; //bueno, regular, malo
    @Expose
    private String cultivo_anterior;
    @Expose
    private String estado_humedad;
    @Expose
    private String compactacion;

    //siembra
    @Expose
    private int protocolo_siembra;
    @Expose
    private int fotografia_cartel_identificacion;
    @Expose
    private int se_indica_fecha_siembra_lc;
    @Expose
    private int relacion_m;
    @Expose
    private int relacion_h;

    //chequeo emvases
    @Expose
    private int foto_envase;
    @Expose
    private int foto_semilla;
    @Expose
    private String mezcla;
    @Expose
    private int cantidad_aplicada;
    @Expose
    private int cantidad_envase_h;
    @Expose
    private String lote_hembra;
    @Expose
    private int cantidad_envase_m;
    @Expose
    private String lote_macho;

//    siembra anterior
    @Expose
    private String especie;
    @Expose
    private String variedad;
    @Expose
    private int ogm;
    @Expose
    private String anexo_curimapu;

    // regulacion de siembra
    @Expose
    private String prestador_servicio;
    @Expose
    private String estado_discos;
    @Expose
    private String sembradora_marca;
    @Expose
    private  String sembradora_modelo;
    @Expose
    private double trocha;
    @Expose
    private String tipo_sembradora;
    @Expose
    private String chequeo_selector;
    @Expose
    private String estado_maquina;
    @Expose
    private int desterronadores;
    @Expose
    private String presion_neumaticos;
    @Expose
    private String especie_lote_anterior;
    @Expose
    private int rueda_angosta;
    @Expose
    private double largo_guia;
    @Expose
    private String sistema_fertilizacion;
    @Expose
    private double distancia_hileras;
    @Expose
    private int cheque_caidas;
    @Expose
    private double numero_semillas;
    @Expose
    private double profundidad_fertilizante;
    @Expose
    private double profundidad_siembra;
    @Expose
    private double distancia_fertilizante_semilla;

    //aseo maquinaria pre siembra
    @Expose
    private int tarros_semilla_pre_siembra;
    @Expose
    private int discos_sembradores_pre_siembra;
    @Expose
    private int estructura_maquinaria_pre_siembra;
    @Expose
    private String lugar_limpieza_pre_siembra;
    @Expose
    private String responsable_aseo_pre_siembra;
    @Expose
    private String rut_responsable_aseo_pre_siembra;
    @Expose
    private String firma_responsable_aso_pre_siembra;
    @Expose
    private String rut_revision_limpieza_pre_siembra;
    @Expose
    private String firma_revision_limpieza_pre_siembra;


    //aseo maquinaria post siembra
    @Expose
    private int tarros_semilla_post_siembra;
    @Expose
    private int discos_sembradores_post_siembra;
    @Expose
    private int estructura_maquinaria_post_cosecha;
    @Expose
    private String lugar_limpieza_post_siembra;
    @Expose
    private String responsable_aseo_post_siembra;
    @Expose
    private String rut_responsable_aseo_post_siembra;
    @Expose
    private String firma_responsable_aseo_post_siembra;
    @Expose
    private String rut_revision_limpieza_post_siembra;
    @Expose
    private String firma_revision_limpieza_post_siembra;


    //general
    @Expose
    private String desempeno_siembra;
    @Expose
    private String observacion_general;

    //ingreso
    @Expose
    private String fecha_ingreso;
    @Expose
    private String hora_ingreso;
    @Expose
    private String nombre_supervisor_siembra;
    @Expose
    private String nombre_responsable_campo;
    @Expose
    private String firma_responsable_campo;
    @Expose
    private String nombre_operario_maquina;
    @Expose
    private String firma_operario_maquina;

    //salida
    @Expose
    private String fecha_termino;
    @Expose
    private String hora_termino;
    @Expose
    private String nombre_supervisor_siembra_termino;
    @Expose
    private String nombre_responsable_campo_termino;
    @Expose
    private String firma_responsable_campo_termino;
    @Expose
    private String nombre_operario_maquina_termino;
    @Expose
    private String firma_operario_maquina_termino;


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

    public int getChequeo_aislacion() {
        return chequeo_aislacion;
    }

    public void setChequeo_aislacion(int chequeo_aislacion) {
        this.chequeo_aislacion = chequeo_aislacion;
    }

    public String getCultivo_anterior() {
        return cultivo_anterior;
    }

    public void setCultivo_anterior(String cultivo_anterior) {
        this.cultivo_anterior = cultivo_anterior;
    }

    public String getCama_semilla_cultivo_anterior() {
        return cama_semilla_cultivo_anterior;
    }

    public void setCama_semilla_cultivo_anterior(String cama_semilla_cultivo_anterior) {
        this.cama_semilla_cultivo_anterior = cama_semilla_cultivo_anterior;
    }

    public String getEstado_humedad() {
        return estado_humedad;
    }

    public void setEstado_humedad(String estado_humedad) {
        this.estado_humedad = estado_humedad;
    }

    public String getCompactacion() {
        return compactacion;
    }

    public void setCompactacion(String compactacion) {
        this.compactacion = compactacion;
    }

    public int getProtocolo_siembra() {
        return protocolo_siembra;
    }

    public void setProtocolo_siembra(int protocolo_siembra) {
        this.protocolo_siembra = protocolo_siembra;
    }

    public int getFotografia_cartel_identificacion() {
        return fotografia_cartel_identificacion;
    }

    public void setFotografia_cartel_identificacion(int fotografia_cartel_identificacion) {
        this.fotografia_cartel_identificacion = fotografia_cartel_identificacion;
    }

    public int getSe_indica_fecha_siembra_lc() {
        return se_indica_fecha_siembra_lc;
    }

    public void setSe_indica_fecha_siembra_lc(int se_indica_fecha_siembra_lc) {
        this.se_indica_fecha_siembra_lc = se_indica_fecha_siembra_lc;
    }

    public int getRelacion_m() {
        return relacion_m;
    }

    public void setRelacion_m(int relacion_m) {
        this.relacion_m = relacion_m;
    }

    public int getRelacion_h() {
        return relacion_h;
    }

    public void setRelacion_h(int relacion_h) {
        this.relacion_h = relacion_h;
    }

    public int getFoto_envase() {
        return foto_envase;
    }

    public void setFoto_envase(int foto_envase) {
        this.foto_envase = foto_envase;
    }

    public int getFoto_semilla() {
        return foto_semilla;
    }

    public void setFoto_semilla(int foto_semilla) {
        this.foto_semilla = foto_semilla;
    }

    public String getMezcla() {
        return mezcla;
    }

    public void setMezcla(String mezcla) {
        this.mezcla = mezcla;
    }

    public int getCantidad_aplicada() {
        return cantidad_aplicada;
    }

    public void setCantidad_aplicada(int cantidad_aplicada) {
        this.cantidad_aplicada = cantidad_aplicada;
    }

    public int getCantidad_envase_h() {
        return cantidad_envase_h;
    }

    public void setCantidad_envase_h(int cantidad_envase_h) {
        this.cantidad_envase_h = cantidad_envase_h;
    }

    public String getLote_hembra() {
        return lote_hembra;
    }

    public void setLote_hembra(String lote_hembra) {
        this.lote_hembra = lote_hembra;
    }

    public int getCantidad_envase_m() {
        return cantidad_envase_m;
    }

    public void setCantidad_envase_m(int cantidad_envase_m) {
        this.cantidad_envase_m = cantidad_envase_m;
    }

    public String getLote_macho() {
        return lote_macho;
    }

    public void setLote_macho(String lote_macho) {
        this.lote_macho = lote_macho;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getVariedad() {
        return variedad;
    }

    public void setVariedad(String variedad) {
        this.variedad = variedad;
    }

    public int getOgm() {
        return ogm;
    }

    public void setOgm(int ogm) {
        this.ogm = ogm;
    }

    public String getAnexo_curimapu() {
        return anexo_curimapu;
    }

    public void setAnexo_curimapu(String anexo_curimapu) {
        this.anexo_curimapu = anexo_curimapu;
    }

    public String getPrestador_servicio() {
        return prestador_servicio;
    }

    public void setPrestador_servicio(String prestador_servicio) {
        this.prestador_servicio = prestador_servicio;
    }

    public String getEstado_discos() {
        return estado_discos;
    }

    public void setEstado_discos(String estado_discos) {
        this.estado_discos = estado_discos;
    }

    public String getSembradora_marca() {
        return sembradora_marca;
    }

    public void setSembradora_marca(String sembradora_marca) {
        this.sembradora_marca = sembradora_marca;
    }

    public String getSembradora_modelo() {
        return sembradora_modelo;
    }

    public void setSembradora_modelo(String sembradora_modelo) {
        this.sembradora_modelo = sembradora_modelo;
    }

    public double getTrocha() {
        return trocha;
    }

    public void setTrocha(double trocha) {
        this.trocha = trocha;
    }

    public String getTipo_sembradora() {
        return tipo_sembradora;
    }

    public void setTipo_sembradora(String tipo_sembradora) {
        this.tipo_sembradora = tipo_sembradora;
    }

    public String getChequeo_selector() {
        return chequeo_selector;
    }

    public void setChequeo_selector(String chequeo_selector) {
        this.chequeo_selector = chequeo_selector;
    }

    public String getEstado_maquina() {
        return estado_maquina;
    }

    public void setEstado_maquina(String estado_maquina) {
        this.estado_maquina = estado_maquina;
    }

    public int getDesterronadores() {
        return desterronadores;
    }

    public void setDesterronadores(int desterronadores) {
        this.desterronadores = desterronadores;
    }

    public String getPresion_neumaticos() {
        return presion_neumaticos;
    }

    public void setPresion_neumaticos(String presion_neumaticos) {
        this.presion_neumaticos = presion_neumaticos;
    }

    public String getEspecie_lote_anterior() {
        return especie_lote_anterior;
    }

    public void setEspecie_lote_anterior(String especie_lote_anterior) {
        this.especie_lote_anterior = especie_lote_anterior;
    }

    public int getRueda_angosta() {
        return rueda_angosta;
    }

    public void setRueda_angosta(int rueda_angosta) {
        this.rueda_angosta = rueda_angosta;
    }

    public double getLargo_guia() {
        return largo_guia;
    }

    public void setLargo_guia(double largo_guia) {
        this.largo_guia = largo_guia;
    }

    public String getSistema_fertilizacion() {
        return sistema_fertilizacion;
    }

    public void setSistema_fertilizacion(String sistema_fertilizacion) {
        this.sistema_fertilizacion = sistema_fertilizacion;
    }

    public double getDistancia_hileras() {
        return distancia_hileras;
    }

    public void setDistancia_hileras(double distancia_hileras) {
        this.distancia_hileras = distancia_hileras;
    }

    public int getCheque_caidas() {
        return cheque_caidas;
    }

    public void setCheque_caidas(int cheque_caidas) {
        this.cheque_caidas = cheque_caidas;
    }

    public double getNumero_semillas() {
        return numero_semillas;
    }

    public void setNumero_semillas(double numero_semillas) {
        this.numero_semillas = numero_semillas;
    }

    public double getProfundidad_fertilizante() {
        return profundidad_fertilizante;
    }

    public void setProfundidad_fertilizante(double profundidad_fertilizante) {
        this.profundidad_fertilizante = profundidad_fertilizante;
    }

    public double getProfundidad_siembra() {
        return profundidad_siembra;
    }

    public void setProfundidad_siembra(double profundidad_siembra) {
        this.profundidad_siembra = profundidad_siembra;
    }

    public double getDistancia_fertilizante_semilla() {
        return distancia_fertilizante_semilla;
    }

    public void setDistancia_fertilizante_semilla(double distancia_fertilizante_semilla) {
        this.distancia_fertilizante_semilla = distancia_fertilizante_semilla;
    }

    public int getTarros_semilla_pre_siembra() {
        return tarros_semilla_pre_siembra;
    }

    public void setTarros_semilla_pre_siembra(int tarros_semilla_pre_siembra) {
        this.tarros_semilla_pre_siembra = tarros_semilla_pre_siembra;
    }

    public int getDiscos_sembradores_pre_siembra() {
        return discos_sembradores_pre_siembra;
    }

    public void setDiscos_sembradores_pre_siembra(int discos_sembradores_pre_siembra) {
        this.discos_sembradores_pre_siembra = discos_sembradores_pre_siembra;
    }

    public int getEstructura_maquinaria_pre_siembra() {
        return estructura_maquinaria_pre_siembra;
    }

    public void setEstructura_maquinaria_pre_siembra(int estructura_maquinaria_pre_siembra) {
        this.estructura_maquinaria_pre_siembra = estructura_maquinaria_pre_siembra;
    }

    public String getLugar_limpieza_pre_siembra() {
        return lugar_limpieza_pre_siembra;
    }

    public void setLugar_limpieza_pre_siembra(String lugar_limpieza_pre_siembra) {
        this.lugar_limpieza_pre_siembra = lugar_limpieza_pre_siembra;
    }

    public String getResponsable_aseo_pre_siembra() {
        return responsable_aseo_pre_siembra;
    }

    public void setResponsable_aseo_pre_siembra(String responsable_aseo_pre_siembra) {
        this.responsable_aseo_pre_siembra = responsable_aseo_pre_siembra;
    }

    public String getRut_responsable_aseo_pre_siembra() {
        return rut_responsable_aseo_pre_siembra;
    }

    public void setRut_responsable_aseo_pre_siembra(String rut_responsable_aseo_pre_siembra) {
        this.rut_responsable_aseo_pre_siembra = rut_responsable_aseo_pre_siembra;
    }

    public String getFirma_responsable_aso_pre_siembra() {
        return firma_responsable_aso_pre_siembra;
    }

    public void setFirma_responsable_aso_pre_siembra(String firma_responsable_aso_pre_siembra) {
        this.firma_responsable_aso_pre_siembra = firma_responsable_aso_pre_siembra;
    }

    public String getRut_revision_limpieza_pre_siembra() {
        return rut_revision_limpieza_pre_siembra;
    }

    public void setRut_revision_limpieza_pre_siembra(String rut_revision_limpieza_pre_siembra) {
        this.rut_revision_limpieza_pre_siembra = rut_revision_limpieza_pre_siembra;
    }

    public String getFirma_revision_limpieza_pre_siembra() {
        return firma_revision_limpieza_pre_siembra;
    }

    public void setFirma_revision_limpieza_pre_siembra(String firma_revision_limpieza_pre_siembra) {
        this.firma_revision_limpieza_pre_siembra = firma_revision_limpieza_pre_siembra;
    }

    public int getTarros_semilla_post_siembra() {
        return tarros_semilla_post_siembra;
    }

    public void setTarros_semilla_post_siembra(int tarros_semilla_post_siembra) {
        this.tarros_semilla_post_siembra = tarros_semilla_post_siembra;
    }

    public int getDiscos_sembradores_post_siembra() {
        return discos_sembradores_post_siembra;
    }

    public void setDiscos_sembradores_post_siembra(int discos_sembradores_post_siembra) {
        this.discos_sembradores_post_siembra = discos_sembradores_post_siembra;
    }

    public int getEstructura_maquinaria_post_cosecha() {
        return estructura_maquinaria_post_cosecha;
    }

    public void setEstructura_maquinaria_post_cosecha(int estructura_maquinaria_post_cosecha) {
        this.estructura_maquinaria_post_cosecha = estructura_maquinaria_post_cosecha;
    }

    public String getLugar_limpieza_post_siembra() {
        return lugar_limpieza_post_siembra;
    }

    public void setLugar_limpieza_post_siembra(String lugar_limpieza_post_siembra) {
        this.lugar_limpieza_post_siembra = lugar_limpieza_post_siembra;
    }

    public String getResponsable_aseo_post_siembra() {
        return responsable_aseo_post_siembra;
    }

    public void setResponsable_aseo_post_siembra(String responsable_aseo_post_siembra) {
        this.responsable_aseo_post_siembra = responsable_aseo_post_siembra;
    }

    public String getRut_responsable_aseo_post_siembra() {
        return rut_responsable_aseo_post_siembra;
    }

    public void setRut_responsable_aseo_post_siembra(String rut_responsable_aseo_post_siembra) {
        this.rut_responsable_aseo_post_siembra = rut_responsable_aseo_post_siembra;
    }

    public String getFirma_responsable_aseo_post_siembra() {
        return firma_responsable_aseo_post_siembra;
    }

    public void setFirma_responsable_aseo_post_siembra(String firma_responsable_aseo_post_siembra) {
        this.firma_responsable_aseo_post_siembra = firma_responsable_aseo_post_siembra;
    }

    public String getRut_revision_limpieza_post_siembra() {
        return rut_revision_limpieza_post_siembra;
    }

    public void setRut_revision_limpieza_post_siembra(String rut_revision_limpieza_post_siembra) {
        this.rut_revision_limpieza_post_siembra = rut_revision_limpieza_post_siembra;
    }

    public String getFirma_revision_limpieza_post_siembra() {
        return firma_revision_limpieza_post_siembra;
    }

    public void setFirma_revision_limpieza_post_siembra(String firma_revision_limpieza_post_siembra) {
        this.firma_revision_limpieza_post_siembra = firma_revision_limpieza_post_siembra;
    }

    public String getDesempeno_siembra() {
        return desempeno_siembra;
    }

    public void setDesempeno_siembra(String desempeno_siembra) {
        this.desempeno_siembra = desempeno_siembra;
    }

    public String getObservacion_general() {
        return observacion_general;
    }

    public void setObservacion_general(String observacion_general) {
        this.observacion_general = observacion_general;
    }

    public String getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(String fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }

    public String getHora_ingreso() {
        return hora_ingreso;
    }

    public void setHora_ingreso(String hora_ingreso) {
        this.hora_ingreso = hora_ingreso;
    }

    public String getNombre_supervisor_siembra() {
        return nombre_supervisor_siembra;
    }

    public void setNombre_supervisor_siembra(String nombre_supervisor_siembra) {
        this.nombre_supervisor_siembra = nombre_supervisor_siembra;
    }

    public String getNombre_responsable_campo() {
        return nombre_responsable_campo;
    }

    public void setNombre_responsable_campo(String nombre_responsable_campo) {
        this.nombre_responsable_campo = nombre_responsable_campo;
    }

    public String getFirma_responsable_campo() {
        return firma_responsable_campo;
    }

    public void setFirma_responsable_campo(String firma_responsable_campo) {
        this.firma_responsable_campo = firma_responsable_campo;
    }

    public String getNombre_operario_maquina() {
        return nombre_operario_maquina;
    }

    public void setNombre_operario_maquina(String nombre_operario_maquina) {
        this.nombre_operario_maquina = nombre_operario_maquina;
    }

    public String getFirma_operario_maquina() {
        return firma_operario_maquina;
    }

    public void setFirma_operario_maquina(String firma_operario_maquina) {
        this.firma_operario_maquina = firma_operario_maquina;
    }

    public String getFecha_termino() {
        return fecha_termino;
    }

    public void setFecha_termino(String fecha_termino) {
        this.fecha_termino = fecha_termino;
    }

    public String getHora_termino() {
        return hora_termino;
    }

    public void setHora_termino(String hora_termino) {
        this.hora_termino = hora_termino;
    }

    public String getNombre_supervisor_siembra_termino() {
        return nombre_supervisor_siembra_termino;
    }

    public void setNombre_supervisor_siembra_termino(String nombre_supervisor_siembra_termino) {
        this.nombre_supervisor_siembra_termino = nombre_supervisor_siembra_termino;
    }

    public String getNombre_responsable_campo_termino() {
        return nombre_responsable_campo_termino;
    }

    public void setNombre_responsable_campo_termino(String nombre_responsable_campo_termino) {
        this.nombre_responsable_campo_termino = nombre_responsable_campo_termino;
    }

    public String getFirma_responsable_campo_termino() {
        return firma_responsable_campo_termino;
    }

    public void setFirma_responsable_campo_termino(String firma_responsable_campo_termino) {
        this.firma_responsable_campo_termino = firma_responsable_campo_termino;
    }

    public String getNombre_operario_maquina_termino() {
        return nombre_operario_maquina_termino;
    }

    public void setNombre_operario_maquina_termino(String nombre_operario_maquina_termino) {
        this.nombre_operario_maquina_termino = nombre_operario_maquina_termino;
    }

    public String getFirma_operario_maquina_termino() {
        return firma_operario_maquina_termino;
    }

    public void setFirma_operario_maquina_termino(String firma_operario_maquina_termino) {
        this.firma_operario_maquina_termino = firma_operario_maquina_termino;
    }
}
