package cl.smapdev.curimapu.fragments.checklist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.tablas.CheckListSiembra;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Evaluaciones;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.dialogos.DialogFirma;
import es.dmoral.toasty.Toasty;

public class FragmentCheckListSiembra extends Fragment {


    private MainActivity activity;
    private SharedPreferences prefs;


    // botones que ocultan contenedores
    private ImageView btn_oculta_cabecera;
    private ImageView btn_oculta_suelo;
    private ImageView btn_oculta_siembra;
    private ImageView btn_oculta_chequeo_envases;
    private ImageView btn_oculta_siembra_anterior;
    private ImageView btn_oculta_regulacion_siembra;
    private ImageView btn_oculta_aseo_maquinaria_pre_siembra;
    private ImageView btn_oculta_aseo_maquinaria_post_siembra;
    private ImageView btn_oculta_general;
    private ImageView btn_oculta_ingreso;
    private ImageView btn_oculta_salida;

    //cabecera
    private TextView tv_numero_anexo;
    private TextView tv_variedad;
    private TextView tv_agricultor;
    private TextView tv_potrero;
    private TextView tv_rch;
    private TextView tv_sag_ogm;
    private TextView tv_sag_idase;
    private TextView tv_condicion_semilla;
    private TextView tv_supervisor_curimapu;

    //suelo
    private RadioGroup grupo_chequeo_aislacion;
    private RadioButton btn_chequeo_si;
    private RadioButton btn_chequeo_no;
    private Spinner sp_cama_semilla;
    private EditText et_cultivo_anterior;
    private Spinner sp_estado_humedad;
    private Spinner sp_compactacion;

    //siembra
    private EditText et_protocolo_siembra;
    private RadioGroup grupo_fotografia_cartel;
    private RadioButton btn_fotografia_si;
    private RadioButton btn_fotografia_no;
    private RadioGroup grupo_indica_fecha_siembra;
    private RadioButton btn_indica_fecha_siembra_si;
    private RadioButton btn_indica_fecha_siembra_no;
    private EditText et_relacion_m;
    private EditText et_relacion_h;

    //chequeo de envases
    private RadioGroup grupo_foto_envase;
    private RadioButton btn_foto_envase_si;
    private RadioButton btn_foto_envase_no;
    private RadioGroup grupo_foto_semilla;
    private RadioButton btn_foto_semilla_si;
    private RadioButton btn_foto_semilla_no;
    private EditText et_mezcla;
    private EditText et_cantidad_fertilizante;
    private EditText et_cantidad_envases_h;
    private EditText et_lote_hembra;
    private EditText et_cantidad_envases_m;
    private EditText et_lote_macho;

    //siembra anterior
    private EditText et_especie;
    private EditText et_variedad;
    private RadioGroup grupo_ogm;
    private RadioButton btn_ogm_si;
    private RadioButton btn_ogm_no;
    private EditText et_anexo_curimapu;

    //regulacion de siembra
    private EditText et_prestador_servicio;
    private Spinner sp_estado_discos;
    private EditText et_sembradora_marca;
    private EditText et_sembradora_modelo;
    private EditText et_trocha;
    private Spinner sp_tipo_sembradora;
    private Spinner sp_chequeo_selector;
    private Spinner sp_estado_maquina;
    private RadioGroup grupo_desterronadores;
    private RadioButton btn_desterronadores_si;
    private RadioButton btn_desterronadores_no;
    private Spinner sp_presion_neumaticos;
    private EditText et_especie_lote;
    private RadioGroup grupo_rueda_angosta;
    private RadioButton btn_rueda_angosta_si;
    private RadioButton btn_rueda_angosta_no;
    private EditText et_largo_guia;
    private EditText et_sistema_fertilizacion;
    private EditText et_distancia_hileras;
    private Spinner  sp_cheque_caidas;
    private EditText et_numero_semillas_mt;
    private EditText et_profundidad_fertilizante;
    private EditText et_profundidad_siembra;
    private EditText et_dist_entre_fert_semilla;

    //aseo maquinaria pre siembra
    private RadioGroup grupo_tarros_semilla;
    private RadioButton btn_tarros_semilla_si;
    private RadioButton btn_tarros_semilla_no;
    private RadioGroup grupo_discos_sembradores;
    private RadioButton btn_discos_sembradores_si;
    private RadioButton btn_discos_sembradores_no;
    private RadioGroup grupo_estructura_maquinaria;
    private RadioButton btn_estructura_maquinaria_si;
    private RadioButton btn_estructura_maquinaria_no;
    private EditText et_lugar_limpieza;
    private EditText et_responsable_aseo;
    private EditText et_rut_responsable_aseo;
    private Button btn_firma_responsable_aseo_ingreso;
    private ImageView check_firma_responsable_aseo_ingreso;
    private EditText et_responsable_revision_limpieza_ingreso;
    private Button btn_firma_responsable_revision_limpieza_ingreso;
    private ImageView check_firma_responsable_revision_limpieza_ingreso;

    //aseo maquinaria post siembra
    private RadioGroup grupo_tarros_semilla_post_siembra;
    private RadioButton btn_tarros_semilla_post_siembra_si;
    private RadioButton btn_tarros_semilla_post_siembra_no;
    private RadioGroup grupo_discos_sembradores_post_siembra;
    private RadioButton btn_discos_sembradores_post_siembra_si;
    private RadioButton btn_discos_sembradores_post_siembra_no;
    private RadioGroup grupo_estructura_maquinaria_post_siembra;
    private RadioButton btn_estructura_maquinaria_post_siembra_si;
    private RadioButton btn_estructura_maquinaria_post_siembra_no;
    private EditText et_lugar_limpieza_post_siembra;
    private EditText et_responsable_aseo_post_siembra;
    private EditText et_rut_responsable_aseo_post_siembra;
    private Button btn_firma_responsable_aseo_ingreso_post_siembra;
    private ImageView check_firma_responsable_aseo_ingreso_post_siembra;
    private EditText et_responsable_revision_limpieza_ingreso_post_siembra;
    private Button btn_firma_responsable_revision_limpieza_ingreso_post_siembra;
    private ImageView check_firma_responsable_revision_limpieza_ingreso_post_siembra;

    //general
    private Spinner sp_desempeno_siembra;
    private EditText et_observaciones_general;

    //ingreso
    private EditText et_fecha_ingreso;
    private EditText et_hora_ingreso;
    private EditText et_nombre_supervisor_ingreso_siembra;
    private EditText et_nombre_responsable_campo_ingreso;
    private Button btn_firma_responsable_campo_ingreso;
    private ImageView check_firma_responsable_campo_ingreso;

    private EditText et_operador_maquina_ingreso;
    private Button btn_firma_operario_maquina_ingreso;
    private ImageView check_firma_operario_maquina_ingreso;



    //salida
    private EditText et_fecha_termino;
    private EditText et_hora_termino;
    private EditText et_nombre_supervisor_termino_siembra;
    private EditText et_nombre_responsable_campo_termino;
    private Button btn_firma_responsable_campo_termino;
    private ImageView check_firma_responsable_campo_termino;
    private EditText et_operador_maquina_termino;
    private Button btn_firma_operario_maquina_termino;
    private ImageView check_firma_operario_maquina_termino;


    //botonera
    private Button btn_guardar_cl_siembra;
    private Button btn_cancelar_cl_siembra;


    private ConstraintLayout contenedor_vista;
    private ConstraintLayout cont_suelo;
    private ConstraintLayout cont_siembra;
    private ConstraintLayout cont_chequeo_envases;
    private ConstraintLayout cont_siembra_anterior;
    private ConstraintLayout cont_regulacion_siembra;
    private ConstraintLayout cont_aseo_maquinaria_pre_siembra;
    private ConstraintLayout cont_aseo_maquinaria_post_siembra;
    private ConstraintLayout cont_general;
    private ConstraintLayout cont_ingreso;
    private ConstraintLayout cont_termino;


    private AnexoCompleto anexoCompleto;
    private Usuario usuario;
    private Config config;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity a = (MainActivity) getActivity();
        if(a != null) activity = a;
        prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_checklist_siembra, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bind(view);


        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<AnexoCompleto> futureVisitas = executor.submit(() ->
                MainActivity
                        .myAppDB
                        .myDao()
                        .getAnexoCompletoById(
                                prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, "")
                        )
        );

        Future<Config> futureConfig = executor.submit(() ->
                MainActivity.myAppDB.myDao().getConfig());



        try {
            anexoCompleto = futureVisitas.get();
            config = futureConfig.get();

            Future<Usuario> usuarioFuture = executor.submit(() ->
                    MainActivity.myAppDB.myDao().getUsuarioById(config.getId_usuario()));

            usuario = usuarioFuture.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }


    @Override
    public void onStart() {
        super.onStart();

        cargarDatosPrevios();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (activity != null){
            activity.updateView(getResources().getString(R.string.app_name), "CHECKLIST SIEMBRA");
        }
    }


    private void cargarDatosPrevios(){
        if(anexoCompleto == null){
            Toasty.error(requireActivity(), "No se pudo obtener informacion del anexo", Toast.LENGTH_LONG, true).show();
        }

        tv_numero_anexo.setText(anexoCompleto.getAnexoContrato().getAnexo_contrato());
        tv_variedad.setText(anexoCompleto.getVariedad().getDesc_variedad());
        tv_agricultor.setText(anexoCompleto.getAgricultor().getNombre_agricultor());

        tv_potrero.setText(anexoCompleto.getLotes().getNombre_lote());
        tv_rch.setText(anexoCompleto.getAnexoContrato().getRch());

//        tv_sag_ogm.setText(anexoCompleto.getAnexoContrato().getAnexo_contrato());
//        tv_sag_idase.setText(anexoCompleto.getAnexoContrato().getAnexo_contrato());
//        tv_condicion_semilla.setText(anexoCompleto.getAnexoContrato().getAnexo_contrato());
        tv_supervisor_curimapu.setText(usuario.getNombre()+ " " +usuario.getApellido_p());


    }

    private void bind (View view ){
        //imageviews
        btn_oculta_cabecera = view.findViewById(R.id.btn_oculta_cabecera);
        btn_oculta_suelo = view.findViewById(R.id.btn_oculta_suelo);
        btn_oculta_siembra = view.findViewById(R.id.btn_oculta_siembra);
        btn_oculta_chequeo_envases = view.findViewById(R.id.btn_oculta_chequeo_envases);
        btn_oculta_siembra_anterior = view.findViewById(R.id.btn_oculta_siembra_anterior);
        btn_oculta_regulacion_siembra = view.findViewById(R.id.btn_oculta_regulacion_siembra);
        btn_oculta_aseo_maquinaria_pre_siembra = view.findViewById(R.id.btn_oculta_aseo_maquinaria_pre_siembra);
        btn_oculta_aseo_maquinaria_post_siembra = view.findViewById(R.id.btn_oculta_aseo_maquinaria_post_siembra);
        btn_oculta_general = view.findViewById(R.id.btn_oculta_general);
        btn_oculta_ingreso = view.findViewById(R.id.btn_oculta_ingreso);
        btn_oculta_salida = view.findViewById(R.id.btn_oculta_salida);

        //cabecera
        tv_numero_anexo = view.findViewById(R.id.tv_numero_anexo);
        tv_variedad = view.findViewById(R.id.tv_variedad);
        tv_agricultor = view.findViewById(R.id.tv_agricultor);
        tv_potrero = view.findViewById(R.id.tv_potrero);
        tv_rch = view.findViewById(R.id.tv_rch);
        tv_sag_ogm = view.findViewById(R.id.tv_sag_ogm);
        tv_sag_idase = view.findViewById(R.id.tv_sag_idase);
        tv_condicion_semilla = view.findViewById(R.id.tv_condicion_semilla);
        tv_supervisor_curimapu = view.findViewById(R.id.tv_supervisor_curimapu);


        //suelo
        grupo_chequeo_aislacion = view.findViewById(R.id.grupo_chequeo_aislacion);
        btn_chequeo_si = view.findViewById(R.id.btn_chequeo_si);
        btn_chequeo_no = view.findViewById(R.id.btn_chequeo_no);
        sp_cama_semilla = view.findViewById(R.id.sp_cama_semilla);
        et_cultivo_anterior = view.findViewById(R.id.et_cultivo_anterior);
        sp_estado_humedad = view.findViewById(R.id.sp_estado_humedad);
        sp_compactacion = view.findViewById(R.id.sp_compactacion);

        //siembra
         et_protocolo_siembra = view.findViewById(R.id.et_protocolo_siembra);
         grupo_fotografia_cartel = view.findViewById(R.id.grupo_fotografia_cartel);
         btn_fotografia_si = view.findViewById(R.id.btn_fotografia_si);
         btn_fotografia_no = view.findViewById(R.id.btn_fotografia_no);
         grupo_indica_fecha_siembra = view.findViewById(R.id.grupo_indica_fecha_siembra);
         btn_indica_fecha_siembra_si = view.findViewById(R.id.btn_indica_fecha_siembra_si);
         btn_indica_fecha_siembra_no = view.findViewById(R.id.btn_indica_fecha_siembra_no);
         et_relacion_m = view.findViewById(R.id.et_relacion_m);
         et_relacion_h = view.findViewById(R.id.et_relacion_h);

         //chequeo de envases
         grupo_foto_envase = view.findViewById(R.id.grupo_foto_envase);
         btn_foto_envase_si = view.findViewById(R.id.btn_foto_envase_si);
         btn_foto_envase_no = view.findViewById(R.id.btn_foto_envase_no);
         grupo_foto_semilla = view.findViewById(R.id.grupo_foto_semilla);
         btn_foto_semilla_si = view.findViewById(R.id.btn_foto_semilla_si);
         btn_foto_semilla_no = view.findViewById(R.id.btn_foto_semilla_no);
         et_mezcla = view.findViewById(R.id.et_mezcla);
         et_cantidad_fertilizante = view.findViewById(R.id.et_cantidad_fertilizante);
         et_cantidad_envases_h = view.findViewById(R.id.et_cantidad_envases_h);
         et_lote_hembra = view.findViewById(R.id.et_lote_hembra);
         et_cantidad_envases_m = view.findViewById(R.id.et_cantidad_envases_m);
         et_lote_macho = view.findViewById(R.id.et_lote_macho);

         //siembra anterior
         et_especie = view.findViewById(R.id.et_especie);
         et_variedad = view.findViewById(R.id.et_variedad);
         grupo_ogm = view.findViewById(R.id.grupo_ogm);
         btn_ogm_si = view.findViewById(R.id.btn_ogm_si);
         btn_ogm_no = view.findViewById(R.id.btn_ogm_no);
         et_anexo_curimapu = view.findViewById(R.id.et_anexo_curimapu);

         //regulacion de siembra
         et_prestador_servicio = view.findViewById(R.id.et_prestador_servicio);
         sp_estado_discos = view.findViewById(R.id.sp_estado_discos);
         et_sembradora_marca = view.findViewById(R.id.et_sembradora_marca);
         et_sembradora_modelo = view.findViewById(R.id.et_sembradora_modelo);
         et_trocha = view.findViewById(R.id.et_trocha);
         sp_tipo_sembradora = view.findViewById(R.id.sp_tipo_sembradora);
         sp_chequeo_selector = view.findViewById(R.id.sp_chequeo_selector);
         sp_estado_maquina = view.findViewById(R.id.sp_estado_maquina);
         grupo_desterronadores = view.findViewById(R.id.grupo_desterronadores);
         btn_desterronadores_si = view.findViewById(R.id.btn_desterronadores_si);
         btn_desterronadores_no = view.findViewById(R.id.btn_desterronadores_no);
         sp_presion_neumaticos = view.findViewById(R.id.sp_presion_neumaticos);
         et_especie_lote = view.findViewById(R.id.et_especie_lote);
         grupo_rueda_angosta = view.findViewById(R.id.grupo_rueda_angosta);
         btn_rueda_angosta_si = view.findViewById(R.id.btn_rueda_angosta_si);
         btn_rueda_angosta_no = view.findViewById(R.id.btn_rueda_angosta_no);
         et_largo_guia = view.findViewById(R.id.et_largo_guia);
         et_sistema_fertilizacion = view.findViewById(R.id.et_sistema_fertilizacion);
         et_distancia_hileras = view.findViewById(R.id.et_distancia_hileras);
         sp_cheque_caidas = view.findViewById(R.id.sp_cheque_caidas);
         et_numero_semillas_mt = view.findViewById(R.id.et_numero_semillas_mt);
         et_profundidad_fertilizante = view.findViewById(R.id.et_profundidad_fertilizante);
         et_profundidad_siembra = view.findViewById(R.id.et_profundidad_siembra);
         et_dist_entre_fert_semilla = view.findViewById(R.id.et_dist_entre_fert_semilla);

        //aseo maquinaria pre siembra
         grupo_tarros_semilla = view.findViewById(R.id.grupo_tarros_semilla);
         btn_tarros_semilla_si = view.findViewById(R.id.btn_tarros_semilla_si);
         btn_tarros_semilla_no = view.findViewById(R.id.btn_tarros_semilla_no);
         grupo_discos_sembradores = view.findViewById(R.id.grupo_discos_sembradores);
         btn_discos_sembradores_si = view.findViewById(R.id.btn_discos_sembradores_si);
         btn_discos_sembradores_no = view.findViewById(R.id.btn_discos_sembradores_no);
         grupo_estructura_maquinaria = view.findViewById(R.id.grupo_estructura_maquinaria);
         btn_estructura_maquinaria_si = view.findViewById(R.id.btn_estructura_maquinaria_si);
         btn_estructura_maquinaria_no = view.findViewById(R.id.btn_estructura_maquinaria_no);
         et_lugar_limpieza = view.findViewById(R.id.et_lugar_limpieza);
         et_responsable_aseo = view.findViewById(R.id.et_responsable_aseo);
         et_rut_responsable_aseo = view.findViewById(R.id.et_rut_responsable_aseo);
         btn_firma_responsable_aseo_ingreso = view.findViewById(R.id.btn_firma_responsable_aseo_ingreso);
         check_firma_responsable_aseo_ingreso = view.findViewById(R.id.check_firma_responsable_aseo_ingreso);
         et_responsable_revision_limpieza_ingreso = view.findViewById(R.id.et_responsable_revision_limpieza_ingreso);
         btn_firma_responsable_revision_limpieza_ingreso = view.findViewById(R.id.btn_firma_responsable_revision_limpieza_ingreso);
         check_firma_responsable_revision_limpieza_ingreso = view.findViewById(R.id.check_firma_responsable_revision_limpieza_ingreso);

        //aseo maquinaria post siembra
         grupo_tarros_semilla_post_siembra = view.findViewById(R.id.grupo_tarros_semilla_post_siembra);
         btn_tarros_semilla_post_siembra_si = view.findViewById(R.id.btn_tarros_semilla_post_siembra_si);
         btn_tarros_semilla_post_siembra_no = view.findViewById(R.id.btn_tarros_semilla_post_siembra_no);
         grupo_discos_sembradores_post_siembra = view.findViewById(R.id.grupo_discos_sembradores_post_siembra);
         btn_discos_sembradores_post_siembra_si = view.findViewById(R.id.btn_discos_sembradores_post_siembra_si);
         btn_discos_sembradores_post_siembra_no = view.findViewById(R.id.btn_discos_sembradores_post_siembra_no);
         grupo_estructura_maquinaria_post_siembra = view.findViewById(R.id.grupo_estructura_maquinaria_post_siembra);
         btn_estructura_maquinaria_post_siembra_si = view.findViewById(R.id.btn_estructura_maquinaria_post_siembra_si);
         btn_estructura_maquinaria_post_siembra_no = view.findViewById(R.id.btn_estructura_maquinaria_post_siembra_no);
         et_lugar_limpieza_post_siembra = view.findViewById(R.id.et_lugar_limpieza_post_siembra);
         et_responsable_aseo_post_siembra = view.findViewById(R.id.et_responsable_aseo_post_siembra);
         et_rut_responsable_aseo_post_siembra = view.findViewById(R.id.et_rut_responsable_aseo_post_siembra);
         btn_firma_responsable_aseo_ingreso_post_siembra = view.findViewById(R.id.btn_firma_responsable_aseo_ingreso_post_siembra);
         check_firma_responsable_aseo_ingreso_post_siembra = view.findViewById(R.id.check_firma_responsable_aseo_ingreso_post_siembra);
         et_responsable_revision_limpieza_ingreso_post_siembra = view.findViewById(R.id.et_responsable_revision_limpieza_ingreso_post_siembra);
         btn_firma_responsable_revision_limpieza_ingreso_post_siembra = view.findViewById(R.id.btn_firma_responsable_revision_limpieza_ingreso_post_siembra);
         check_firma_responsable_revision_limpieza_ingreso_post_siembra = view.findViewById(R.id.check_firma_responsable_revision_limpieza_ingreso_post_siembra);


         //general
        sp_desempeno_siembra = view.findViewById(R.id.sp_desempeno_siembra);
        et_observaciones_general = view.findViewById(R.id.et_observaciones_general);

        //ingreso
         et_fecha_ingreso = view.findViewById(R.id.et_fecha_ingreso);
         et_hora_ingreso = view.findViewById(R.id.et_hora_ingreso);
         et_nombre_supervisor_ingreso_siembra = view.findViewById(R.id.et_nombre_supervisor_ingreso_siembra);
         et_nombre_responsable_campo_ingreso = view.findViewById(R.id.et_nombre_responsable_campo_ingreso);
         btn_firma_responsable_campo_ingreso = view.findViewById(R.id.btn_firma_responsable_campo_ingreso);
         check_firma_responsable_campo_ingreso = view.findViewById(R.id.check_firma_responsable_campo_ingreso);
         et_operador_maquina_ingreso = view.findViewById(R.id.et_operador_maquina_ingreso);
         btn_firma_operario_maquina_ingreso = view.findViewById(R.id.btn_firma_operario_maquina_ingreso);
         check_firma_operario_maquina_ingreso = view.findViewById(R.id.check_firma_operario_maquina_ingreso);

        //salida
         et_fecha_termino = view.findViewById(R.id.et_fecha_termino);
         et_hora_termino = view.findViewById(R.id.et_hora_termino);
         et_nombre_supervisor_termino_siembra = view.findViewById(R.id.et_nombre_supervisor_termino_siembra);
         et_nombre_responsable_campo_termino = view.findViewById(R.id.et_nombre_responsable_campo_termino);
         btn_firma_responsable_campo_termino = view.findViewById(R.id.btn_firma_responsable_campo_termino);
         check_firma_responsable_campo_termino = view.findViewById(R.id.check_firma_responsable_campo_termino);
         et_operador_maquina_termino = view.findViewById(R.id.et_operador_maquina_termino);
         btn_firma_operario_maquina_termino = view.findViewById(R.id.btn_firma_operario_maquina_termino);
         check_firma_operario_maquina_termino = view.findViewById(R.id.check_firma_operario_maquina_termino);

        //constraint layout
        contenedor_vista = view.findViewById(R.id.contenedor_vista);
        cont_suelo = view.findViewById(R.id.cont_suelo);
        cont_siembra = view.findViewById(R.id.cont_siembra);
        cont_chequeo_envases = view.findViewById(R.id.cont_chequeo_envases);
        cont_siembra_anterior = view.findViewById(R.id.cont_siembra_anterior);
        cont_regulacion_siembra = view.findViewById(R.id.cont_regulacion_siembra);
        cont_aseo_maquinaria_pre_siembra = view.findViewById(R.id.cont_aseo_maquinaria_pre_siembra);
        cont_aseo_maquinaria_post_siembra = view.findViewById(R.id.cont_aseo_maquinaria_post_siembra);
        cont_general = view.findViewById(R.id.cont_general);
        cont_ingreso = view.findViewById(R.id.cont_ingreso);
        cont_termino = view.findViewById(R.id.cont_termino);

        //botonera
        btn_guardar_cl_siembra = view.findViewById(R.id.btn_guardar_cl_siembra);
        btn_cancelar_cl_siembra = view.findViewById(R.id.btn_cancelar_cl_siembra);



        //ocultadores
        btn_oculta_cabecera.setOnClickListener(view1 -> {
            contenedor_vista.setVisibility((contenedor_vista.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
            btn_oculta_cabecera.setImageDrawable((contenedor_vista.getVisibility() == View.VISIBLE) ? getResources().getDrawable(R.drawable.ic_expand_up) : getResources().getDrawable(R.drawable.ic_expand_down));
        });
        btn_oculta_suelo.setOnClickListener(view1 -> {
            cont_suelo.setVisibility((cont_suelo.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
            btn_oculta_suelo.setImageDrawable((cont_suelo.getVisibility() == View.VISIBLE) ? getResources().getDrawable(R.drawable.ic_expand_up) : getResources().getDrawable(R.drawable.ic_expand_down));
        });
        btn_oculta_siembra.setOnClickListener(view1 -> {
            cont_siembra.setVisibility((cont_siembra.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
            btn_oculta_siembra.setImageDrawable((cont_siembra.getVisibility() == View.VISIBLE) ? getResources().getDrawable(R.drawable.ic_expand_up) : getResources().getDrawable(R.drawable.ic_expand_down));
        });
        btn_oculta_chequeo_envases.setOnClickListener(view1 -> {
            cont_chequeo_envases.setVisibility((cont_chequeo_envases.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
            btn_oculta_chequeo_envases.setImageDrawable((cont_chequeo_envases.getVisibility() == View.VISIBLE) ? getResources().getDrawable(R.drawable.ic_expand_up) : getResources().getDrawable(R.drawable.ic_expand_down));
        });
        btn_oculta_siembra_anterior.setOnClickListener(view1 -> {
            cont_siembra_anterior.setVisibility((cont_siembra_anterior.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
            btn_oculta_siembra_anterior.setImageDrawable((cont_siembra_anterior.getVisibility() == View.VISIBLE) ? getResources().getDrawable(R.drawable.ic_expand_up) : getResources().getDrawable(R.drawable.ic_expand_down));
        });
        btn_oculta_regulacion_siembra.setOnClickListener(view1 -> {
            cont_regulacion_siembra.setVisibility((cont_regulacion_siembra.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
            btn_oculta_regulacion_siembra.setImageDrawable((cont_regulacion_siembra.getVisibility() == View.VISIBLE) ? getResources().getDrawable(R.drawable.ic_expand_up) : getResources().getDrawable(R.drawable.ic_expand_down));
        });
        btn_oculta_aseo_maquinaria_pre_siembra.setOnClickListener(view1 -> {
            cont_aseo_maquinaria_pre_siembra.setVisibility((cont_aseo_maquinaria_pre_siembra.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
            btn_oculta_aseo_maquinaria_pre_siembra.setImageDrawable((cont_aseo_maquinaria_pre_siembra.getVisibility() == View.VISIBLE) ? getResources().getDrawable(R.drawable.ic_expand_up) : getResources().getDrawable(R.drawable.ic_expand_down));
        });
        btn_oculta_aseo_maquinaria_post_siembra.setOnClickListener(view1 -> {
            cont_aseo_maquinaria_post_siembra.setVisibility((cont_aseo_maquinaria_post_siembra.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
            btn_oculta_aseo_maquinaria_post_siembra.setImageDrawable((cont_aseo_maquinaria_post_siembra.getVisibility() == View.VISIBLE) ? getResources().getDrawable(R.drawable.ic_expand_up) : getResources().getDrawable(R.drawable.ic_expand_down));
        });
        btn_oculta_general.setOnClickListener(view1 -> {
            cont_general.setVisibility((cont_general.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
            btn_oculta_general.setImageDrawable((cont_general.getVisibility() == View.VISIBLE) ? getResources().getDrawable(R.drawable.ic_expand_up) : getResources().getDrawable(R.drawable.ic_expand_down));
        });
        btn_oculta_ingreso.setOnClickListener(view1 -> {
            cont_ingreso.setVisibility((cont_ingreso.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
            btn_oculta_ingreso.setImageDrawable((cont_ingreso.getVisibility() == View.VISIBLE) ? getResources().getDrawable(R.drawable.ic_expand_up) : getResources().getDrawable(R.drawable.ic_expand_down));
        });
        btn_oculta_salida.setOnClickListener(view1 -> {
            cont_termino.setVisibility((cont_termino.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
            btn_oculta_salida.setImageDrawable((cont_termino.getVisibility() == View.VISIBLE) ? getResources().getDrawable(R.drawable.ic_expand_up) : getResources().getDrawable(R.drawable.ic_expand_down));
        });



        //botonera
        btn_guardar_cl_siembra.setOnClickListener(view1 -> showAlertForConfirmarGuardar());
        btn_cancelar_cl_siembra.setOnClickListener(view1 -> cancelar());



        et_fecha_ingreso.setOnClickListener(view1 -> levantarFecha(et_fecha_ingreso));
        et_fecha_ingreso.setOnFocusChangeListener((view1, b) -> {if(b) levantarFecha(et_fecha_ingreso);} );
        et_fecha_termino.setOnFocusChangeListener((view1, b) -> {if(b) levantarFecha(et_fecha_termino);} );
        et_fecha_termino.setOnClickListener(view1 -> levantarFecha(et_fecha_ingreso));

        et_hora_ingreso.setOnFocusChangeListener((view1, b) -> {if(b) levantarHora(et_hora_ingreso);} );
        et_hora_termino.setOnFocusChangeListener((view1, b) -> {if(b) levantarHora(et_hora_termino);} );
        et_hora_ingreso.setOnClickListener(view1 -> levantarHora(et_hora_ingreso));
        et_hora_termino.setOnClickListener(view1 -> levantarHora(et_hora_termino));




        btn_firma_responsable_aseo_ingreso.setOnClickListener(view1 -> levantarDialogo("RESPONSABLE_ASEO_INGRESO"));
        btn_firma_responsable_revision_limpieza_ingreso.setOnClickListener(view1 -> levantarDialogo("REVISOR_LIMPIEZA_INGRESO"));
        btn_firma_responsable_aseo_ingreso_post_siembra.setOnClickListener(view1 -> levantarDialogo("RESPONSABLE_ASEO_SALIDA"));
        btn_firma_responsable_revision_limpieza_ingreso_post_siembra.setOnClickListener(view1 -> levantarDialogo("REVISOR_LIMPIEZA_SALIDA"));
        btn_firma_responsable_campo_ingreso.setOnClickListener(view1 -> levantarDialogo("RESPONSABLE_CAMPO_INGRESO"));
        btn_firma_responsable_campo_termino.setOnClickListener(view1 -> levantarDialogo("RESPONSABLE_CAMPO_TERMINO"));

    }

    private boolean guardar(int state, String description) {

        String comparaSpinner = "--Seleccione--";
        //crear clase y guardar en bd

        //levantar modal para preguntar si quiere guardar y activar o solo guardar.
        CheckListSiembra siembra  = new CheckListSiembra();

        siembra.setEstado_documento(state);
        siembra.setApellido_checklist( description);
        siembra.setId_ac_cl_siembra(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));


        String claveUnica = config.getId()+""+config.getId_usuario()+""+Utilidades.fechaActualConHora();

        //suelo
        if(btn_chequeo_si.isChecked() || btn_chequeo_no.isChecked()){
            int chequeoAislacion = (btn_chequeo_si.isSelected()) ? 1 : 0;
            siembra.setChequeo_aislacion(chequeoAislacion);
        }

        if(!sp_cama_semilla.getSelectedItem().toString().equals(comparaSpinner)){
            String cama_semilla = sp_cama_semilla.getSelectedItem().toString();
            siembra.setCama_semilla(cama_semilla);
        }

        if(!et_cultivo_anterior.getText().toString().isEmpty()){
            String cultivo_anterior = et_cultivo_anterior.getText().toString();
            siembra.setCultivo_anterior(cultivo_anterior);
        }

        if(!sp_estado_humedad.getSelectedItem().toString().equals(comparaSpinner)){
            String estadoHumedad = sp_estado_humedad.getSelectedItem().toString();
            siembra.setEstado_humedad(estadoHumedad);
        }

        if(!sp_compactacion.getSelectedItem().toString().equals(comparaSpinner)){
            String compactacion = sp_compactacion.getSelectedItem().toString();
            siembra.setCompactacion(compactacion);
        }

        //siembra
        if(!et_protocolo_siembra.getText().toString().isEmpty()){
            String protocoloSiembra = et_protocolo_siembra.getText().toString();
            siembra.setProtocolo_siembra(Integer.parseInt(protocoloSiembra));
        }

        if(btn_fotografia_si.isChecked() || btn_fotografia_no.isChecked()){
            int fotografiaCartel = (btn_fotografia_si.isChecked()) ? 1 : 0;
            siembra.setFotografia_cartel_identificacion(fotografiaCartel);
        }

        if(btn_indica_fecha_siembra_si.isChecked() || btn_indica_fecha_siembra_no.isChecked()){
            int indicaFechaSiembra = (btn_indica_fecha_siembra_si.isChecked()) ? 1 : 0;
            siembra.setSe_indica_fecha_siembra_lc(indicaFechaSiembra);
        }

        if(!et_relacion_m.getText().toString().isEmpty()){
            String relacionM = et_relacion_m.getText().toString();
            siembra.setRelacion_m(Double.parseDouble(relacionM));
        }

        if(!et_relacion_h.getText().toString().isEmpty()){
            String relacionH = et_relacion_h.getText().toString();
            siembra.setRelacion_h(Double.parseDouble(relacionH));
        }

        //chequeo envases

        if(btn_foto_envase_si.isChecked() || btn_foto_envase_no.isChecked()){
            int fotoEnvase = (btn_foto_envase_si.isChecked()) ? 1 : 0;
            siembra.setFoto_envase(fotoEnvase);
        }

        if(btn_foto_semilla_si.isChecked() || btn_foto_semilla_no.isChecked()){
            int fotoSemilla = (btn_foto_semilla_si.isChecked()) ? 1 : 0;
            siembra.setFoto_semilla(fotoSemilla);
        }

        if(!et_mezcla.getText().toString().isEmpty()){
            String mezcla = et_mezcla.getText().toString();
            siembra.setMezcla(mezcla);
        }

        if(!et_cantidad_fertilizante.getText().toString().isEmpty()){
            String cantidadFertilizante = et_cantidad_fertilizante.getText().toString();
            siembra.setCantidad_aplicada(Double.parseDouble(cantidadFertilizante));
        }


        if(!et_cantidad_envases_h.getText().toString().isEmpty()){
            String cantidadEnvasesH = et_cantidad_envases_h.getText().toString();
            siembra.setCantidad_envase_h(Double.parseDouble(cantidadEnvasesH));
        }

        if(!et_lote_hembra.getText().toString().isEmpty()){
            String loteHembra = et_lote_hembra.getText().toString();
            siembra.setLote_hembra(loteHembra);
        }

        if(!et_cantidad_envases_m.getText().toString().isEmpty()){
            String cantidadEnvasesM = et_cantidad_envases_m.getText().toString();
            siembra.setCantidad_envase_m(Double.parseDouble(cantidadEnvasesM));
        }

        if(!et_lote_macho.getText().toString().isEmpty()){
            String loteMacho = et_lote_macho.getText().toString();
            siembra.setLote_macho(loteMacho);
        }


        //siembra anterior

        if(!et_especie.getText().toString().isEmpty()){
            String especie = et_especie.getText().toString();
            siembra.setEspecie(especie);
        }

        if(!et_variedad.getText().toString().isEmpty()){
            String variedad = et_variedad.getText().toString();
            siembra.setVariedad(variedad);
        }

        if(btn_ogm_si.isChecked() || btn_ogm_no.isChecked()){
            int ogm = (btn_ogm_si.isChecked()) ? 1 : 0;
            siembra.setOgm(ogm);
        }

        if(!et_anexo_curimapu.getText().toString().isEmpty()){
            String anexoCurimapu = et_anexo_curimapu.getText().toString();
            siembra.setAnexo_curimapu(anexoCurimapu);
        }

        //regulacion de siembra
        if(!et_prestador_servicio.getText().toString().isEmpty()){
            String prestadorServicio = et_prestador_servicio.getText().toString();
            siembra.setPrestador_servicio(prestadorServicio);
        }

        if(!sp_estado_discos.getSelectedItem().toString().equals(comparaSpinner)){
            String estadoDiscos = sp_estado_discos.getSelectedItem().toString();
            siembra.setEstado_discos(estadoDiscos);
        }

        if(!et_sembradora_marca.getText().toString().isEmpty()){
            String sembradoraMarca = et_sembradora_marca.getText().toString();
            siembra.setSembradora_marca(sembradoraMarca);
        }
        if(!et_sembradora_modelo.getText().toString().isEmpty()){
            String sembradoraModelo = et_sembradora_modelo.getText().toString();
            siembra.setSembradora_modelo(sembradoraModelo);
        }
        if(!et_trocha.getText().toString().isEmpty()){
            String trocha = et_trocha.getText().toString();
            siembra.setTrocha(Double.parseDouble(trocha));
        }
        if(!sp_tipo_sembradora.getSelectedItem().toString().equals(comparaSpinner)){
            String tipoSembradora = sp_tipo_sembradora.getSelectedItem().toString();
            siembra.setTipo_sembradora(tipoSembradora);
        }
        if(!sp_chequeo_selector.getSelectedItem().toString().equals(comparaSpinner)){
            String chequeoSelector = sp_chequeo_selector.getSelectedItem().toString();
            siembra.setChequeo_selector(chequeoSelector);
        }
        if(!sp_estado_maquina.getSelectedItem().toString().equals(comparaSpinner)){
            String estadoMaquina = sp_estado_maquina.getSelectedItem().toString();
            siembra.setEstado_maquina(estadoMaquina);
        }

        if(btn_desterronadores_si.isChecked() || btn_desterronadores_no.isChecked()){
            int desterronadores = (btn_desterronadores_si.isChecked()) ? 1 : 0;
            siembra.setDesterronadores(desterronadores);
        }

        if(!sp_presion_neumaticos.getSelectedItem().toString().equals(comparaSpinner)){
            String presionNeumatico = sp_presion_neumaticos.getSelectedItem().toString();
            siembra.setPresion_neumaticos(presionNeumatico);
        }

        if(!et_especie_lote.getText().toString().isEmpty()){
            String especieLote = et_especie_lote.getText().toString();
            siembra.setEspecie_lote_anterior(especieLote);
        }
        if(btn_rueda_angosta_si.isChecked() || btn_rueda_angosta_no.isChecked()){
            int ruedaAngosta = (btn_rueda_angosta_si.isChecked()) ? 1 : 0;
            siembra.setRueda_angosta(ruedaAngosta);
        }
        if(!et_largo_guia.getText().toString().isEmpty()){
            String largoGuia = et_largo_guia.getText().toString();
            siembra.setLargo_guia(Double.parseDouble(largoGuia));
        }
        if(!et_sistema_fertilizacion.getText().toString().isEmpty()){
            String sistemaFertilizacion = et_sistema_fertilizacion.getText().toString();
            siembra.setSistema_fertilizacion(sistemaFertilizacion);
        }
        if(!et_distancia_hileras.getText().toString().isEmpty()){
            String distanciaHileras = et_distancia_hileras.getText().toString();
            siembra.setDistancia_hileras(Double.parseDouble(distanciaHileras));
        }

        if(btn_chequeo_si.isChecked() || btn_chequeo_no.isChecked()){
            int chequeo = (btn_chequeo_si.isChecked()) ? 1 : 0;
            siembra.setRueda_angosta(chequeo);
        }
        if(!et_numero_semillas_mt.getText().toString().isEmpty()){
            String numeroSemillas = et_numero_semillas_mt.getText().toString();
            siembra.setNumero_semillas(Double.parseDouble(numeroSemillas));
        }
        if(!et_profundidad_fertilizante.getText().toString().isEmpty()){
            String profFertilizante = et_profundidad_fertilizante.getText().toString();
            siembra.setProfundidad_fertilizante(Double.parseDouble(profFertilizante));
        }
        if(!et_profundidad_siembra.getText().toString().isEmpty()){
            String profSiembra = et_profundidad_siembra.getText().toString();
            siembra.setProfundidad_siembra(Double.parseDouble(profSiembra));
        }

        if(!et_dist_entre_fert_semilla.getText().toString().isEmpty()){
            String distanciaFertSemilla = et_dist_entre_fert_semilla.getText().toString();
            siembra.setDistancia_fertilizante_semilla(Double.parseDouble(distanciaFertSemilla));
        }


        //aseo maquinaria pre siembra
        if(btn_tarros_semilla_si.isChecked() || btn_tarros_semilla_no.isChecked()){
            int tarrosSemillas = (btn_tarros_semilla_si.isChecked()) ? 1 : 0;
            siembra.setTarros_semilla_pre_siembra(tarrosSemillas);
        }
        if(btn_discos_sembradores_si.isChecked() || btn_discos_sembradores_no.isChecked()){
            int discosSembradores = (btn_discos_sembradores_si.isChecked()) ? 1 : 0;
            siembra.setDiscos_sembradores_pre_siembra(discosSembradores);
        }
        if(btn_estructura_maquinaria_si.isChecked() || btn_estructura_maquinaria_no.isChecked()){
            int estructuraMaquinaria = (btn_estructura_maquinaria_si.isChecked()) ? 1 : 0;
            siembra.setEstructura_maquinaria_pre_siembra(estructuraMaquinaria);
        }

        if(!et_lugar_limpieza.getText().toString().isEmpty()){
            String lugarLimpieza = et_lugar_limpieza.getText().toString();
            siembra.setLugar_limpieza_pre_siembra(lugarLimpieza);
        }

        if(!et_responsable_aseo.getText().toString().isEmpty()){
            String responsableAseo = et_responsable_aseo.getText().toString();
            siembra.setResponsable_aseo_pre_siembra(responsableAseo);
        }
        if(!et_rut_responsable_aseo.getText().toString().isEmpty()){
            String rutResponsableAseo = et_rut_responsable_aseo.getText().toString();
            siembra.setRut_responsable_aseo_pre_siembra(rutResponsableAseo);
        }
        //firma

        if(!et_responsable_revision_limpieza_ingreso.getText().toString().isEmpty()){
            String responsableRevisionLimpieza = et_responsable_revision_limpieza_ingreso.getText().toString();
            siembra.setEncargado_revision_limpieza_post_siembra(responsableRevisionLimpieza);
        }
        //firma


        //aseo maquinaria post siembra
        if(btn_tarros_semilla_post_siembra_si.isChecked() || btn_tarros_semilla_post_siembra_no.isChecked()){
            int tarroSemillaPostSiembra = (btn_tarros_semilla_post_siembra_si.isChecked()) ? 1 : 0;
            siembra.setTarros_semilla_post_siembra(tarroSemillaPostSiembra);
        }
        if(btn_discos_sembradores_post_siembra_si.isChecked() || btn_discos_sembradores_post_siembra_no.isChecked()){
            int discosSembradoresPostSiembra = (btn_discos_sembradores_post_siembra_si.isChecked()) ? 1 : 0;
            siembra.setDiscos_sembradores_post_siembra(discosSembradoresPostSiembra);
        }

        if(btn_estructura_maquinaria_post_siembra_si.isChecked() || btn_estructura_maquinaria_post_siembra_no.isChecked()){
            int estrucutraMaquinariaPostSiembra = (btn_estructura_maquinaria_post_siembra_si.isChecked()) ? 1 : 0;
            siembra.setEstructura_maquinaria_post_cosecha(estrucutraMaquinariaPostSiembra);
        }

        if(!et_lugar_limpieza_post_siembra.getText().toString().isEmpty()){
            String lugarLimpiezaPS = et_lugar_limpieza_post_siembra.getText().toString();
            siembra.setLugar_limpieza_post_siembra(lugarLimpiezaPS);
        }
        if(!et_responsable_aseo_post_siembra.getText().toString().isEmpty()){
            String responsableAseoPS = et_responsable_aseo_post_siembra.getText().toString();
            siembra.setResponsable_aseo_post_siembra(responsableAseoPS);
        }

        if(!et_rut_responsable_aseo_post_siembra.getText().toString().isEmpty()){
            String rutResponsableAseoPS = et_rut_responsable_aseo_post_siembra.getText().toString();
            siembra.setRut_responsable_aseo_post_siembra(rutResponsableAseoPS);
        }
        //firma


        if(!et_responsable_revision_limpieza_ingreso_post_siembra.getText().toString().isEmpty()){
            String rutResponsableRevisionAseoPS = et_responsable_revision_limpieza_ingreso_post_siembra.getText().toString();
            siembra.setEncargado_revision_limpieza_post_siembra(rutResponsableRevisionAseoPS);
        }
        //firma



        //general
        if(!sp_desempeno_siembra.getSelectedItem().toString().equals(comparaSpinner)){
            String desempenoSiembra = sp_desempeno_siembra.getSelectedItem().toString();
            siembra.setDesempeno_siembra(desempenoSiembra);
        }

        if(!et_observaciones_general.getText().toString().isEmpty()){
            String observaciones = et_observaciones_general.getText().toString();
            siembra.setObservacion_general(observaciones);
        }


        //ingreso
        if(!et_fecha_ingreso.getText().toString().isEmpty()){
            String fechaIngreso = et_fecha_ingreso.getText().toString();
            siembra.setFecha_ingreso(fechaIngreso);
        }

        if(!et_hora_ingreso.getText().toString().isEmpty()){
            String horaIngreso = et_hora_ingreso.getText().toString();
            siembra.setHora_ingreso(horaIngreso);
        }

        if(!et_nombre_supervisor_ingreso_siembra.getText().toString().isEmpty()){
            String nombreSupervisor = et_nombre_supervisor_ingreso_siembra.getText().toString();
            siembra.setNombre_supervisor_siembra(nombreSupervisor);
        }
        //firma

        if(!et_nombre_responsable_campo_ingreso.getText().toString().isEmpty()){
            String resposableCampo = et_nombre_responsable_campo_ingreso.getText().toString();
            siembra.setNombre_responsable_campo(resposableCampo);
        }
        //firma

        if(!et_operador_maquina_ingreso.getText().toString().isEmpty()){
            String operarioMaquina = et_operador_maquina_ingreso.getText().toString();
            siembra.setNombre_operario_maquina(operarioMaquina);
        }
        //firma


        //salida
        if(!et_fecha_termino.getText().toString().isEmpty()){
            String fechaIngreso = et_fecha_termino.getText().toString();
            siembra.setFecha_termino(fechaIngreso);
        }

        if(!et_hora_termino.getText().toString().isEmpty()){
            String horaIngreso = et_hora_termino.getText().toString();
            siembra.setHora_termino(horaIngreso);
        }

        if(!et_nombre_supervisor_termino_siembra.getText().toString().isEmpty()){
            String nombreSupervisor = et_nombre_supervisor_termino_siembra.getText().toString();
            siembra.setNombre_supervisor_siembra_termino(nombreSupervisor);
        }
        //firma

        if(!et_nombre_responsable_campo_termino.getText().toString().isEmpty()){
            String resposableCampo = et_nombre_responsable_campo_termino.getText().toString();
            siembra.setNombre_responsable_campo_termino(resposableCampo);
        }
        //firma

        if(!et_operador_maquina_termino.getText().toString().isEmpty()){
            String operarioMaquina = et_operador_maquina_termino.getText().toString();
            siembra.setNombre_operario_maquina_termino(operarioMaquina);
        }
        //firma


        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<Long> newIdFuture =  executor.submit(() ->
            MainActivity.myAppDB.DaoClSiembra().insertClSiembra(siembra));

        try {
            long newId = newIdFuture.get();

            if(newId > 0) {
                Toasty.success(requireActivity(), "Guardado con exito", Toast.LENGTH_LONG, true).show();
            }else{
                Toasty.error(requireActivity(), "No se pudo guardar con exito", Toast.LENGTH_LONG, true).show();
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Toasty.warning(requireActivity(), "Error al guardar ->"+ e.getMessage(), Toast.LENGTH_LONG, true).show();
        }

        return true;
    }


    private void cancelar() {
        //eliminar firmas si es que las hay

        //devolver a checklists
    }


    private void levantarDialogo(String tipoFirma){



        switch (tipoFirma){
            case "RESPONSABLE_ASEO_INGRESO":break;
            case "REVISOR_LIMPIEZA_INGRESO":break;
            case "RESPONSABLE_ASEO_SALIDA":break;
            case "REVISOR_LIMPIEZA_SALIDA":break;
            case "RESPONSABLE_CAMPO_INGRESO":break;
            case "RESPONSABLE_CAMPO_TERMINO":break;
        }

            DialogFirma df = new DialogFirma();
            df.show(requireActivity().getSupportFragmentManager(), "FIRMA_USUARIOSDIALOG");
    }

    private void showAlertForConfirmarGuardar(){
        View viewInfalted = LayoutInflater.from(requireActivity()).inflate(R.layout.alert_guardar_checklist,null);

        RadioGroup grupo_radios_estado = viewInfalted.findViewById(R.id.grupo_radios_estado);
        RadioButton rbtn_activo = viewInfalted.findViewById(R.id.rbtn_activo);
        RadioButton rbtn_pendiente = viewInfalted.findViewById(R.id.rbtn_pendiente);
        EditText et_apellido = viewInfalted.findViewById(R.id.et_apellido);


        final androidx.appcompat.app.AlertDialog builder = new androidx.appcompat.app.AlertDialog.Builder(requireActivity())
                .setView(viewInfalted)
                .setPositiveButton(getResources().getString(R.string.guardar), (dialogInterface, i) -> { })
                .setNegativeButton(getResources().getString(R.string.nav_cancel), (dialogInterface, i) -> { })
                .create();


        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
            Button c = builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE);
            b.setOnClickListener(view -> {

                if((!rbtn_activo.isChecked() && !rbtn_pendiente.isChecked()) || et_apellido.getText().toString().isEmpty() ){
                    Toasty.error(requireActivity(), "Debes seleccionar un estado e ingresar una descripcion", Toast.LENGTH_LONG, true).show();
                    return;
                }
                int state = (rbtn_activo.isChecked()) ? 1 : 0;
                boolean isSaved = guardar(state, et_apellido.getText().toString());
                if(isSaved) builder.dismiss();

            });
            c.setOnClickListener(view -> builder.dismiss());
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void levantarHora(final EditText edit ){
        String hora = Utilidades.hora();

        String[] horaRota;

        if(!TextUtils.isEmpty(edit.getText())){
            horaRota = edit.getText().toString().split(":");
        }else{
            horaRota = hora.split(":");
        }

        TimePickerDialog timePickerDialog =  new TimePickerDialog(getContext(), (timePicker, i, i1) -> {
            String horaFinal = i+":"+i1+":00";
            edit.setText(horaFinal);
        },
                Integer.parseInt(horaRota[0]),
                Integer.parseInt(horaRota[1]),
                true
        );
        timePickerDialog.show();
    }

    private void levantarFecha(final EditText edit){

        String fecha = Utilidades.fechaActualSinHora();
        String[] fechaRota;

        if (!TextUtils.isEmpty(edit.getText())){
            try{ fechaRota = Utilidades.voltearFechaBD(edit.getText().toString()).split("-"); }
            catch (Exception e){  fechaRota = fecha.split("-");  }
        }else{  fechaRota = fecha.split("-"); }
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, year, month, dayOfMonth) -> {

            month = month + 1;
            String mes = "", dia;

            if (month < 10) { mes = "0" + month; }
            else { mes = String.valueOf(month); }

            if (dayOfMonth < 10) dia = "0" + dayOfMonth;
            else dia = String.valueOf(dayOfMonth);

            String finalDate = dia + "-" + mes + "-" + year;
            edit.setText(finalDate);
        }, Integer.parseInt(fechaRota[0]), (Integer.parseInt(fechaRota[1]) - 1), Integer.parseInt(fechaRota[2]));
        datePickerDialog.show();

    }

}
