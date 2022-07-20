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

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.tablas.Config;
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

    //salida
    private EditText et_fecha_termino;
    private EditText et_hora_termino;
    private EditText et_nombre_supervisor_termino_siembra;
    private EditText et_nombre_responsable_campo_termino;
    private Button btn_firma_responsable_campo_termino;
    private ImageView check_firma_responsable_campo_termino;


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


        //ingreso
         et_fecha_ingreso = view.findViewById(R.id.et_fecha_ingreso);
         et_hora_ingreso = view.findViewById(R.id.et_hora_ingreso);
         et_nombre_supervisor_ingreso_siembra = view.findViewById(R.id.et_nombre_supervisor_ingreso_siembra);
         et_nombre_responsable_campo_ingreso = view.findViewById(R.id.et_nombre_responsable_campo_ingreso);
         btn_firma_responsable_campo_ingreso = view.findViewById(R.id.btn_firma_responsable_campo_ingreso);
         check_firma_responsable_campo_ingreso = view.findViewById(R.id.check_firma_responsable_campo_ingreso);

        //salida
         et_fecha_termino = view.findViewById(R.id.et_fecha_termino);
         et_hora_termino = view.findViewById(R.id.et_hora_termino);
         et_nombre_supervisor_termino_siembra = view.findViewById(R.id.et_nombre_supervisor_termino_siembra);
         et_nombre_responsable_campo_termino = view.findViewById(R.id.et_nombre_responsable_campo_termino);
         btn_firma_responsable_campo_termino = view.findViewById(R.id.btn_firma_responsable_campo_termino);
         check_firma_responsable_campo_termino = view.findViewById(R.id.check_firma_responsable_campo_termino);

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
