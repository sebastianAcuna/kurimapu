package cl.smapdev.curimapu.fragments.checklist;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.tablas.CheckListCosecha;
import cl.smapdev.curimapu.clases.tablas.CheckListSiembra;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.temporales.TempFirmas;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.dialogos.DialogFirma;
import es.dmoral.toasty.Toasty;

public class FragmentCheckListCosecha extends Fragment {


    private MainActivity activity;
    private SharedPreferences prefs;


    //general

    private EditText responsable_cosecha;
    private EditText fecha_inicio_cosecha;
    private EditText hora_inicio_cosecha;
    private RadioGroup grupo_capacitacion;
    private RadioButton btn_capacitacion_si;
    private RadioButton btn_capacitacion_no;


    //condiciones del potrero

    private Spinner sp_acceso;
    private Spinner sp_control_maleza;
    private RadioGroup grupo_corte_macho;
    private RadioButton btn_cortemacho_si;
    private RadioButton btn_cortemacho_no;
    private RadioGroup grupo_eliminacion_regueros;
    private RadioButton btn_eliminacion_regueros_si;
    private RadioButton btn_eliminacion_regueros_no;
    private Spinner sp_humedad_suelo;
    private Spinner sp_estado_visual;
    private Spinner sp_humedad_cosecha;
    private EditText et_comentario;


    //cosechadora
    private EditText et_prestador_servicio;
    private EditText et_operador_maquina;
    private EditText et_marca_maquina;
    private EditText et_modelo_maquina;
    private EditText et_cosecha_anterior;


    // aseo ingreso

    private EditText et_fecha_ingreso;
    private RadioGroup grupo_cabezal_ingreso;
    private RadioButton btn_cabezal_ingreso_si;
    private RadioButton btn_cabezal_ingreso_no;
    private RadioGroup grupo_elevadores_ingreso;
    private RadioButton btn_elevadores_ingreso_si;
    private RadioButton btn_elevadores_ingreso_no;
    private RadioGroup grupo_concavos_ingreso;
    private RadioButton btn_concavos_ingreso_si;
    private RadioButton btn_concavos_ingreso_no;
    private RadioGroup grupo_harneros_ingreso;
    private RadioButton btn_harneros_ingreso_si;
    private RadioButton btn_harneros_ingreso_no;
    private RadioGroup grupo_tolva_ingreso;
    private RadioButton btn_tolva_ingreso_si;
    private RadioButton btn_tolva_ingreso_no;
    private RadioGroup grupo_descarga_ingreso;
    private RadioButton btn_descarga_ingreso_si;
    private RadioButton btn_descarga_ingreso_no;
    private EditText et_lugar_limpieza_ingreso;
    private EditText et_responsable_aseo_ingreso;
    private EditText et_rut_responsable_aseo_ingreso;
    private Button btn_firma_responsable_aseo_ingreso;
    private ImageView check_firma_responsable_aseo_ingreso;
    private EditText et_responsable_revision_limpieza_ingreso;
    private Button btn_firma_responsable_revision_limpieza_ingreso;
    private ImageView check_firma_responsable_revision_limpieza_ingreso;


    //aseo salida

    private EditText et_fecha_salida;
    private RadioGroup grupo_cabezal_salida;
    private RadioButton btn_cabezal_salida_si;
    private RadioButton btn_cabezal_salida_no;
    private RadioGroup grupo_elevadores_salida;
    private RadioButton btn_elevadores_salida_si;
    private RadioButton btn_elevadores_salida_no;
    private RadioGroup grupo_concavos_salida;
    private RadioButton btn_concavos_salida_si;
    private RadioButton btn_concavos_salida_no;
    private RadioGroup grupo_harneros_salida;
    private RadioButton btn_harneros_salida_si;
    private RadioButton btn_harneros_salida_no;
    private RadioGroup grupo_tolva_salida;
    private RadioButton btn_tolva_salida_si;
    private RadioButton btn_tolva_salida_no;
    private RadioGroup grupo_descarga_salida;
    private RadioButton btn_descarga_salida_si;
    private RadioButton btn_descarga_salida_no;
    private EditText et_lugar_limpieza_salida;
    private EditText et_responsable_aseo_salida;
    private EditText et_rut_responsable_aseo_salida;
    private Button btn_firma_responsable_aseo_salida;
    private ImageView check_firma_responsable_aseo_salida;
    private EditText et_responsable_revision_limpieza_salida;
    private Button btn_firma_responsable_revision_limpieza_salida;
    private ImageView check_firma_responsable_revision_limpieza_salida;




    // botones que ocultan contenedores
    private ImageView btn_oculta_condicion_potrero;
    private ImageView btn_oculta_cabecera;
    private ImageView btn_oculta_cosechadora;
    private ImageView btn_oculta_aseo_ingreso;
    private ImageView btn_oculta_aseo_salida;

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


    //botonera
    private Button btn_guardar_cl_siembra;
    private Button btn_cancelar_cl_siembra;


    private ConstraintLayout contenedor_vista;
    private ConstraintLayout cont_condicion_potrero;
    private ConstraintLayout cont_cosechadora;
    private ConstraintLayout cont_aseo_ingreso;
    private ConstraintLayout cont_aseo_salida;

    private AnexoCompleto anexoCompleto;
    private Usuario usuario;
    private Config config;

    private CheckListCosecha checkListSiembra;

    private final ArrayList<String> chk_1 = new ArrayList<>();
    private final ArrayList<String> chk_2 = new ArrayList<>();
    private final ArrayList<String> chk_3 = new ArrayList<>();



    public void setCheckListSiembra(CheckListCosecha checkListSiembra) {
        this.checkListSiembra = checkListSiembra;
    }

    public static FragmentCheckListCosecha newInstance(CheckListCosecha checkListSiembra ){

        FragmentCheckListCosecha fs = new FragmentCheckListCosecha();

        fs.setCheckListSiembra( checkListSiembra );

        return fs;
    }

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
        return inflater.inflate(R.layout.fragment_checklist_cosecha, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bind(view);


        chk_1.addAll(Arrays.asList(getResources().getStringArray(R.array.desplegable_checklist_1)));
        chk_2.addAll(Arrays.asList(getResources().getStringArray(R.array.desplegable_checklist_2)));
        chk_3.addAll(Arrays.asList(getResources().getStringArray(R.array.desplegable_checklist_3)));

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


        if(checkListSiembra != null){

            levantarDatos();

        }
    }

    private void levantarDatos() {


        if(checkListSiembra.getCapacitacion() > 0){
            btn_capacitacion_si.setChecked((checkListSiembra.getCapacitacion() == 1));
            btn_capacitacion_no.setChecked((checkListSiembra.getCapacitacion() == 2));
        }

        if(checkListSiembra.getCorte_macho() > 0){
            btn_cortemacho_si.setChecked((checkListSiembra.getCorte_macho() == 1));
            btn_cortemacho_no.setChecked((checkListSiembra.getCorte_macho() == 2));
        }

        if(checkListSiembra.getEliminacion_regueros() > 0){
            btn_eliminacion_regueros_si.setChecked((checkListSiembra.getEliminacion_regueros() == 1));
            btn_eliminacion_regueros_no.setChecked((checkListSiembra.getEliminacion_regueros() == 2));
        }

        if(checkListSiembra.getCabezal_ingreso() > 0){
            btn_cabezal_ingreso_si.setChecked((checkListSiembra.getCabezal_ingreso() == 1));
            btn_cabezal_ingreso_no.setChecked((checkListSiembra.getCabezal_ingreso() == 2));
        }

        if(checkListSiembra.getElevadores_ingreso() > 0){
            btn_elevadores_ingreso_si.setChecked((checkListSiembra.getElevadores_ingreso() == 1));
            btn_elevadores_ingreso_no.setChecked((checkListSiembra.getElevadores_ingreso() == 2));
        }

        if(checkListSiembra.getConcavos_ingreso() > 0){
            btn_concavos_ingreso_si.setChecked((checkListSiembra.getConcavos_ingreso() == 1));
            btn_concavos_ingreso_no.setChecked((checkListSiembra.getConcavos_ingreso() == 2));
        }

        if(checkListSiembra.getHarneros_ingreso() > 0){
            btn_harneros_ingreso_si.setChecked((checkListSiembra.getHarneros_ingreso() == 1));
            btn_harneros_ingreso_no.setChecked((checkListSiembra.getHarneros_ingreso() == 2));
        }

        if(checkListSiembra.getTolva_ingreso() > 0){
            btn_tolva_ingreso_si.setChecked((checkListSiembra.getTolva_ingreso() == 1));
            btn_tolva_ingreso_no.setChecked((checkListSiembra.getTolva_ingreso() == 2));
        }

        if(checkListSiembra.getDescarga_ingreso() > 0){
            btn_descarga_ingreso_si.setChecked((checkListSiembra.getDescarga_ingreso() == 1));
            btn_descarga_ingreso_no.setChecked((checkListSiembra.getDescarga_ingreso() == 2));
        }

        if(checkListSiembra.getCabezal_salida() > 0){
            btn_cabezal_salida_si.setChecked((checkListSiembra.getCabezal_salida() == 1));
            btn_cabezal_salida_no.setChecked((checkListSiembra.getCabezal_salida() == 2));
        }

        if(checkListSiembra.getElevadores_salida() > 0){
            btn_elevadores_salida_si.setChecked((checkListSiembra.getElevadores_salida() == 1));
            btn_elevadores_salida_no.setChecked((checkListSiembra.getElevadores_salida() == 2));
        }

        if(checkListSiembra.getConcavos_salida() > 0){
            btn_concavos_salida_si.setChecked((checkListSiembra.getConcavos_salida() == 1));
            btn_concavos_salida_no.setChecked((checkListSiembra.getConcavos_salida() == 2));
        }

        if(checkListSiembra.getHarneros_salida() > 0){
            btn_harneros_salida_si.setChecked((checkListSiembra.getHarneros_salida() == 1));
            btn_harneros_salida_no.setChecked((checkListSiembra.getHarneros_salida() == 2));
        }

        if(checkListSiembra.getTolva_salida() > 0){
            btn_tolva_salida_si.setChecked((checkListSiembra.getTolva_salida() == 1));
            btn_tolva_salida_no.setChecked((checkListSiembra.getTolva_salida() == 2));
        }

        if(checkListSiembra.getDescarga_salida() > 0){
            btn_descarga_salida_si.setChecked((checkListSiembra.getDescarga_salida() == 1));
            btn_descarga_salida_no.setChecked((checkListSiembra.getDescarga_salida() == 2));
        }





        if(checkListSiembra.getAcceso() != null && !checkListSiembra.getAcceso().isEmpty()){
              int d = chk_1.indexOf(checkListSiembra.getAcceso());
              sp_acceso.setSelection(d);
        }

        if(checkListSiembra.getControl_maleza() != null && !checkListSiembra.getControl_maleza().isEmpty()){
            int d = chk_1.indexOf(checkListSiembra.getControl_maleza());
            sp_control_maleza.setSelection(d);
        }

        if(checkListSiembra.getHumedad_suelo() != null && !checkListSiembra.getHumedad_suelo().isEmpty()){
            int d = chk_1.indexOf(checkListSiembra.getHumedad_suelo());
            sp_humedad_suelo.setSelection(d);
        }

        if(checkListSiembra.getEstado_visual() != null && !checkListSiembra.getEstado_visual().isEmpty()){
            int d = chk_1.indexOf(checkListSiembra.getEstado_visual());
            sp_estado_visual.setSelection(d);
        }

        if(checkListSiembra.getHumedad_cosecha() != null && !checkListSiembra.getHumedad_cosecha().isEmpty()){
            int d = chk_1.indexOf(checkListSiembra.getHumedad_cosecha());
            sp_humedad_cosecha.setSelection(d);
        }

        if(checkListSiembra.getResponsable_cosecha() != null && !checkListSiembra.getResponsable_cosecha().isEmpty()){
            responsable_cosecha.setText(checkListSiembra.getResponsable_cosecha());
        }

        if(checkListSiembra.getFecha_inicio_cosecha() != null && !checkListSiembra.getFecha_inicio_cosecha().isEmpty()){
            fecha_inicio_cosecha.setText(checkListSiembra.getFecha_inicio_cosecha());
        }

        if(checkListSiembra.getHora_inicio_cosecha() != null && !checkListSiembra.getHora_inicio_cosecha().isEmpty()){
            hora_inicio_cosecha.setText(checkListSiembra.getHora_inicio_cosecha());
        }

        if(checkListSiembra.getComentarios() != null && !checkListSiembra.getComentarios().isEmpty()){
            et_comentario.setText(checkListSiembra.getComentarios());
        }

        if(checkListSiembra.getPrestador_servicio() != null && !checkListSiembra.getPrestador_servicio().isEmpty()){
            et_prestador_servicio.setText(checkListSiembra.getPrestador_servicio());
        }
        if(checkListSiembra.getNombre_operario_maquina() != null && !checkListSiembra.getNombre_operario_maquina().isEmpty()){
            et_operador_maquina.setText(checkListSiembra.getNombre_operario_maquina());
        }

        if(checkListSiembra.getSembradora_marca() != null && !checkListSiembra.getSembradora_marca().isEmpty()){
            et_marca_maquina.setText(checkListSiembra.getSembradora_marca());
        }

        if(checkListSiembra.getSembradora_modelo() != null && !checkListSiembra.getSembradora_modelo().isEmpty()){
            et_modelo_maquina.setText(checkListSiembra.getSembradora_modelo());
        }
        if(checkListSiembra.getCosecha_anterior() != null && !checkListSiembra.getCosecha_anterior().isEmpty()){
            et_cosecha_anterior.setText(checkListSiembra.getCosecha_anterior());
        }

        if(checkListSiembra.getFecha_revision_limpieza_ingreso() != null && !checkListSiembra.getFecha_revision_limpieza_ingreso().isEmpty()){
            et_fecha_ingreso.setText(checkListSiembra.getFecha_revision_limpieza_ingreso());
        }

        if(checkListSiembra.getLugar_limpieza_ingreso() != null && !checkListSiembra.getLugar_limpieza_ingreso().isEmpty()){
            et_lugar_limpieza_ingreso.setText(checkListSiembra.getLugar_limpieza_ingreso());
        }
        if(checkListSiembra.getResponsable_aseo_ingreso() != null && !checkListSiembra.getResponsable_aseo_ingreso().isEmpty()){
            et_responsable_aseo_ingreso.setText(checkListSiembra.getResponsable_aseo_ingreso());
        }

        if(checkListSiembra.getRut_responsable_aseo_ingreso() != null && !checkListSiembra.getRut_responsable_aseo_ingreso().isEmpty()){
            et_rut_responsable_aseo_ingreso.setText(checkListSiembra.getRut_responsable_aseo_ingreso());
        }

        //firma


        if(checkListSiembra.getFecha_revision_limpieza_ingreso() != null && !checkListSiembra.getFecha_revision_limpieza_ingreso().isEmpty()){
            et_responsable_revision_limpieza_ingreso.setText(checkListSiembra.getFecha_revision_limpieza_ingreso());
        }

        //firma


        if(checkListSiembra.getFecha_revision_limpieza_salida() != null && !checkListSiembra.getFecha_revision_limpieza_salida().isEmpty()){
            et_fecha_salida.setText(checkListSiembra.getFecha_revision_limpieza_salida());
        }

        if(checkListSiembra.getLugar_limpieza_salida() != null && !checkListSiembra.getLugar_limpieza_salida().isEmpty()){
            et_lugar_limpieza_salida.setText(checkListSiembra.getLugar_limpieza_salida());
        }
        if(checkListSiembra.getResponsable_aseo_salida() != null && !checkListSiembra.getResponsable_aseo_salida().isEmpty()){
            et_responsable_aseo_salida.setText(checkListSiembra.getResponsable_aseo_salida());
        }

        if(checkListSiembra.getRut_responsable_aseo_salida() != null && !checkListSiembra.getRut_responsable_aseo_salida().isEmpty()){
            et_rut_responsable_aseo_salida.setText(checkListSiembra.getRut_responsable_aseo_salida());
        }

        //firma


        if(checkListSiembra.getFecha_revision_limpieza_salida() != null && !checkListSiembra.getFecha_revision_limpieza_salida().isEmpty()){
            et_responsable_revision_limpieza_salida.setText(checkListSiembra.getResponsable_curimapu_salida());
        }

        //firma



        if(checkListSiembra.getFirma_responsable_aseo_ingreso() != null && !checkListSiembra.getFirma_responsable_aseo_ingreso().isEmpty()){
            btn_firma_responsable_aseo_ingreso.setEnabled(false);
            check_firma_responsable_aseo_ingreso.setVisibility(View.VISIBLE);
        }

        if(checkListSiembra.getFirma_responsable_curimapu_ingreso() != null && !checkListSiembra.getFirma_responsable_curimapu_ingreso().isEmpty()){
            btn_firma_responsable_revision_limpieza_ingreso.setEnabled(false);
            check_firma_responsable_revision_limpieza_ingreso.setVisibility(View.VISIBLE);
        }

        if(checkListSiembra.getFirma_responsable_aseo_salida() != null && !checkListSiembra.getFirma_responsable_aseo_salida().isEmpty()){
            btn_firma_responsable_aseo_salida.setEnabled(false);
            check_firma_responsable_aseo_salida.setVisibility(View.VISIBLE);
        }

        if(checkListSiembra.getFirma_responsable_curimapu_salida() != null && !checkListSiembra.getFirma_responsable_curimapu_salida().isEmpty()) {
            btn_firma_responsable_revision_limpieza_salida.setEnabled(false);
            check_firma_responsable_revision_limpieza_salida.setVisibility(View.VISIBLE);
        }

        btn_guardar_cl_siembra.setText("EDITAR");
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
            activity.updateView(getResources().getString(R.string.app_name), "CHECKLIST COSECHA");
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

        tv_sag_ogm.setText(anexoCompleto.getAnexoContrato().getSag_register_number());
        tv_sag_idase.setText(anexoCompleto.getAnexoContrato().getSag_register_idase());
        tv_condicion_semilla.setText(anexoCompleto.getAnexoContrato().getCondicion());
        tv_supervisor_curimapu.setText(usuario.getNombre()+ " " +usuario.getApellido_p());


    }

    private void bind (View view ){


        responsable_cosecha = view.findViewById(R.id.responsable_cosecha);
        fecha_inicio_cosecha = view.findViewById(R.id.fecha_inicio_cosecha);
        hora_inicio_cosecha = view.findViewById(R.id.hora_inicio_cosecha);
        grupo_capacitacion = view.findViewById(R.id.grupo_capacitacion);
        btn_capacitacion_si = view.findViewById(R.id.btn_capacitacion_si);
        btn_capacitacion_no = view.findViewById(R.id.btn_capacitacion_no);
        sp_acceso = view.findViewById(R.id.sp_acceso);
        sp_control_maleza = view.findViewById(R.id.sp_control_maleza);
        grupo_corte_macho = view.findViewById(R.id.grupo_corte_macho);
        btn_cortemacho_si = view.findViewById(R.id.btn_cortemacho_si);
        btn_cortemacho_no = view.findViewById(R.id.btn_cortemacho_no);
        grupo_eliminacion_regueros = view.findViewById(R.id.grupo_eliminacion_regueros);
        btn_eliminacion_regueros_si = view.findViewById(R.id.btn_eliminacion_regueros_si);
        btn_eliminacion_regueros_no = view.findViewById(R.id.btn_eliminacion_regueros_no);
        sp_humedad_suelo = view.findViewById(R.id.sp_humedad_suelo);
        sp_estado_visual = view.findViewById(R.id.sp_estado_visual);
        sp_humedad_cosecha = view.findViewById(R.id.sp_humedad_cosecha);
        et_comentario = view.findViewById(R.id.et_comentario);
        et_prestador_servicio = view.findViewById(R.id.et_prestador_servicio);
        et_operador_maquina = view.findViewById(R.id.et_operador_maquina);
        et_marca_maquina = view.findViewById(R.id.et_marca_maquina);
        et_modelo_maquina = view.findViewById(R.id.et_modelo_maquina);
        et_cosecha_anterior = view.findViewById(R.id.et_cosecha_anterior);
        et_fecha_ingreso = view.findViewById(R.id.et_fecha_ingreso);
        grupo_cabezal_ingreso = view.findViewById(R.id.grupo_cabezal_ingreso);
        btn_cabezal_ingreso_si = view.findViewById(R.id.btn_cabezal_ingreso_si);
        btn_cabezal_ingreso_no = view.findViewById(R.id.btn_cabezal_ingreso_no);
        grupo_elevadores_ingreso = view.findViewById(R.id.grupo_elevadores_ingreso);
        btn_elevadores_ingreso_si = view.findViewById(R.id.btn_elevadores_ingreso_si);
        btn_elevadores_ingreso_no = view.findViewById(R.id.btn_elevadores_ingreso_no);
        grupo_concavos_ingreso = view.findViewById(R.id.grupo_concavos_ingreso);
        btn_concavos_ingreso_si = view.findViewById(R.id.btn_concavos_ingreso_si);
        btn_concavos_ingreso_no = view.findViewById(R.id.btn_concavos_ingreso_no);
        grupo_harneros_ingreso = view.findViewById(R.id.grupo_harneros_ingreso);
        btn_harneros_ingreso_si = view.findViewById(R.id.btn_harneros_ingreso_si);
        btn_harneros_ingreso_no = view.findViewById(R.id.btn_harneros_ingreso_no);
        grupo_tolva_ingreso = view.findViewById(R.id.grupo_tolva_ingreso);
        btn_tolva_ingreso_si = view.findViewById(R.id.btn_tolva_ingreso_si);
        btn_tolva_ingreso_no = view.findViewById(R.id.btn_tolva_ingreso_no);
        grupo_descarga_ingreso = view.findViewById(R.id.grupo_descarga_ingreso);
        btn_descarga_ingreso_si = view.findViewById(R.id.btn_descarga_ingreso_si);
        btn_descarga_ingreso_no = view.findViewById(R.id.btn_descarga_ingreso_no);
        et_lugar_limpieza_ingreso = view.findViewById(R.id.et_lugar_limpieza_ingreso);
        et_responsable_aseo_ingreso = view.findViewById(R.id.et_responsable_aseo_ingreso);
        et_rut_responsable_aseo_ingreso = view.findViewById(R.id.et_rut_responsable_aseo_ingreso);
        btn_firma_responsable_aseo_ingreso = view.findViewById(R.id.btn_firma_responsable_aseo_ingreso);
        check_firma_responsable_aseo_ingreso = view.findViewById(R.id.check_firma_responsable_aseo_ingreso);
        et_responsable_revision_limpieza_ingreso = view.findViewById(R.id.et_responsable_revision_limpieza_ingreso);
        btn_firma_responsable_revision_limpieza_ingreso = view.findViewById(R.id.btn_firma_responsable_revision_limpieza_ingreso);
        check_firma_responsable_revision_limpieza_ingreso = view.findViewById(R.id.check_firma_responsable_revision_limpieza_ingreso);
        et_fecha_salida = view.findViewById(R.id.et_fecha_salida);
        grupo_cabezal_salida = view.findViewById(R.id.grupo_cabezal_salida);
        btn_cabezal_salida_si = view.findViewById(R.id.btn_cabezal_salida_si);
        btn_cabezal_salida_no = view.findViewById(R.id.btn_cabezal_salida_no);
        grupo_elevadores_salida = view.findViewById(R.id.grupo_elevadores_salida);
        btn_elevadores_salida_si = view.findViewById(R.id.btn_elevadores_salida_si);
        btn_elevadores_salida_no = view.findViewById(R.id.btn_elevadores_salida_no);
        grupo_concavos_salida = view.findViewById(R.id.grupo_concavos_salida);
        btn_concavos_salida_si = view.findViewById(R.id.btn_concavos_salida_si);
        btn_concavos_salida_no = view.findViewById(R.id.btn_concavos_salida_no);
        grupo_harneros_salida = view.findViewById(R.id.grupo_harneros_salida);
        btn_harneros_salida_si = view.findViewById(R.id.btn_harneros_salida_si);
        btn_harneros_salida_no = view.findViewById(R.id.btn_harneros_salida_no);
        grupo_tolva_salida = view.findViewById(R.id.grupo_tolva_salida);
        btn_tolva_salida_si = view.findViewById(R.id.btn_tolva_salida_si);
        btn_tolva_salida_no = view.findViewById(R.id.btn_tolva_salida_no);
        grupo_descarga_salida = view.findViewById(R.id.grupo_descarga_salida);
        btn_descarga_salida_si = view.findViewById(R.id.btn_descarga_salida_si);
        btn_descarga_salida_no = view.findViewById(R.id.btn_descarga_salida_no);
        et_lugar_limpieza_salida = view.findViewById(R.id.et_lugar_limpieza_salida);
        et_responsable_aseo_salida = view.findViewById(R.id.et_responsable_aseo_salida);
        et_rut_responsable_aseo_salida = view.findViewById(R.id.et_rut_responsable_aseo_salida);
        btn_firma_responsable_aseo_salida = view.findViewById(R.id.btn_firma_responsable_aseo_salida);
        check_firma_responsable_aseo_salida = view.findViewById(R.id.check_firma_responsable_aseo_salida);
        et_responsable_revision_limpieza_salida = view.findViewById(R.id.et_responsable_revision_limpieza_salida);
        btn_firma_responsable_revision_limpieza_salida = view.findViewById(R.id.btn_firma_responsable_revision_limpieza_salida);
        check_firma_responsable_revision_limpieza_salida = view.findViewById(R.id.check_firma_responsable_revision_limpieza_salida);
        btn_oculta_condicion_potrero = view.findViewById(R.id.btn_oculta_condicion_potrero);
        btn_oculta_cabecera = view.findViewById(R.id.btn_oculta_cabecera);
        btn_oculta_cosechadora = view.findViewById(R.id.btn_oculta_cosechadora);
        btn_oculta_aseo_ingreso = view.findViewById(R.id.btn_oculta_aseo_ingreso);
        btn_oculta_aseo_salida = view.findViewById(R.id.btn_oculta_aseo_salida);
        tv_numero_anexo = view.findViewById(R.id.tv_numero_anexo);
        tv_variedad = view.findViewById(R.id.tv_variedad);
        tv_agricultor = view.findViewById(R.id.tv_agricultor);
        tv_potrero = view.findViewById(R.id.tv_potrero);
        tv_rch = view.findViewById(R.id.tv_rch);
        tv_sag_ogm = view.findViewById(R.id.tv_sag_ogm);
        tv_sag_idase = view.findViewById(R.id.tv_sag_idase);
        tv_condicion_semilla = view.findViewById(R.id.tv_condicion_semilla);
        tv_supervisor_curimapu = view.findViewById(R.id.tv_supervisor_curimapu);
        contenedor_vista = view.findViewById(R.id.contenedor_vista);
        cont_condicion_potrero = view.findViewById(R.id.cont_condicion_potrero);
        cont_cosechadora = view.findViewById(R.id.cont_cosechadora);
        cont_aseo_ingreso = view.findViewById(R.id.cont_aseo_ingreso);
        cont_aseo_salida = view.findViewById(R.id.cont_aseo_salida);

        btn_guardar_cl_siembra = view.findViewById(R.id.btn_guardar_cl_siembra);
        btn_cancelar_cl_siembra = view.findViewById(R.id.btn_cancelar_cl_siembra);



        //ocultadores
        btn_oculta_cabecera.setOnClickListener(view1 -> {
            contenedor_vista.setVisibility((contenedor_vista.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
            btn_oculta_cabecera.setImageDrawable((contenedor_vista.getVisibility() == View.VISIBLE) ? getResources().getDrawable(R.drawable.ic_expand_up) : getResources().getDrawable(R.drawable.ic_expand_down));
        });


        btn_oculta_condicion_potrero.setOnClickListener(view1 ->{
            cont_condicion_potrero.setVisibility((cont_condicion_potrero.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
            btn_oculta_condicion_potrero.setImageDrawable((cont_condicion_potrero.getVisibility() == View.VISIBLE) ? getResources().getDrawable(R.drawable.ic_expand_up) : getResources().getDrawable(R.drawable.ic_expand_down));
        });
        btn_oculta_cosechadora.setOnClickListener(view1 ->{
            cont_cosechadora.setVisibility((cont_cosechadora.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
            btn_oculta_condicion_potrero.setImageDrawable((cont_cosechadora.getVisibility() == View.VISIBLE) ? getResources().getDrawable(R.drawable.ic_expand_up) : getResources().getDrawable(R.drawable.ic_expand_down));
        });
        btn_oculta_aseo_ingreso.setOnClickListener(view1 ->{
            cont_aseo_ingreso.setVisibility((cont_aseo_ingreso.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
            btn_oculta_condicion_potrero.setImageDrawable((cont_aseo_ingreso.getVisibility() == View.VISIBLE) ? getResources().getDrawable(R.drawable.ic_expand_up) : getResources().getDrawable(R.drawable.ic_expand_down));
        });
        btn_oculta_aseo_salida.setOnClickListener(view1 ->{
            cont_aseo_salida.setVisibility((cont_aseo_salida.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
            btn_oculta_condicion_potrero.setImageDrawable((cont_aseo_salida.getVisibility() == View.VISIBLE) ? getResources().getDrawable(R.drawable.ic_expand_up) : getResources().getDrawable(R.drawable.ic_expand_down));
        });

        //botonera
        btn_guardar_cl_siembra.setOnClickListener(view1 -> showAlertForConfirmarGuardar());
        btn_cancelar_cl_siembra.setOnClickListener(view1 -> cancelar());



        et_fecha_ingreso.setOnClickListener(view1 -> levantarFecha(et_fecha_ingreso));
        et_fecha_ingreso.setOnFocusChangeListener((view1, b) -> {if(b) levantarFecha(et_fecha_ingreso);} );

        et_fecha_salida.setOnClickListener(view1 -> levantarFecha(et_fecha_salida));
        et_fecha_salida.setOnFocusChangeListener((view1, b) -> {if(b) levantarFecha(et_fecha_salida);} );

        fecha_inicio_cosecha.setOnClickListener(view1 -> levantarFecha(fecha_inicio_cosecha));
        fecha_inicio_cosecha.setOnFocusChangeListener((view1, b) -> {if(b) levantarFecha(fecha_inicio_cosecha);} );

        hora_inicio_cosecha.setOnClickListener(view1 -> Utilidades.levantarHora(hora_inicio_cosecha, requireActivity()));
        hora_inicio_cosecha.setOnFocusChangeListener((view1, b) -> {if(b) Utilidades.levantarHora(hora_inicio_cosecha, requireActivity());} );



        btn_firma_responsable_aseo_ingreso.setOnClickListener(view1 -> {
            if(et_responsable_aseo_ingreso.getText().toString().isEmpty() ||
                    et_rut_responsable_aseo_ingreso.getText().toString().isEmpty()){
                Toasty.warning(
                        requireActivity(),
                        "Debe ingresar nombre y rut de responsable",
                        Toast.LENGTH_LONG, true).show();
                return;
            }

            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentByTag(Utilidades.DIALOG_TAG_RESPONSABLE_ASEO_INGRESO);
            if (prev != null) {
                ft.remove(prev);
            }

            String etRA = et_responsable_aseo_ingreso.getText().toString()
                    .trim()
                    .toLowerCase(Locale.ROOT)
                    .replaceAll(" ", "_")
                    .replaceAll("ñ", "n")
                    .replaceAll("á", "a")
                    .replaceAll("é", "e")
                    .replaceAll("í", "i")
                    .replaceAll("ó", "o")
                    .replaceAll("ú", "u")
                    +"_"+
                    Utilidades.fechaActualConHora()
                        .replaceAll(" " ,"")
                        .replaceAll("-" ,"")
                        .replaceAll(":", "_")+".png";

            DialogFirma dialogo = DialogFirma.newInstance(
                    Utilidades.TIPO_DOCUMENTO_CHECKLIST_COSECHA,
                    etRA,
                    Utilidades.DIALOG_TAG_RESPONSABLE_ASEO_INGRESO,
                    (isSaved, path) -> {
                        if(isSaved){
                            check_firma_responsable_aseo_ingreso.setVisibility(View.VISIBLE);
                        }
                    }
                    );

            dialogo.show(ft, Utilidades.DIALOG_TAG_RESPONSABLE_ASEO_INGRESO);
        });

        btn_firma_responsable_revision_limpieza_ingreso.setOnClickListener(view1 -> {

            if(et_responsable_revision_limpieza_ingreso.getText().toString().isEmpty()){
                Toasty.warning(
                        requireActivity(),
                        "Debe ingresar nombre de responsable",
                        Toast.LENGTH_LONG, true).show();
                return;
            }

            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentByTag(Utilidades.DIALOG_TAG_REVISOR_LIMPIEZA_INGRESO);
            if (prev != null) {
                ft.remove(prev);
            }

            String etRA = et_responsable_revision_limpieza_ingreso.getText().toString()
                    .trim()
                    .toLowerCase(Locale.ROOT)
                    .replaceAll(" ", "_")
                    .replaceAll("ñ", "n")
                    .replaceAll("á", "a")
                    .replaceAll("é", "e")
                    .replaceAll("í", "i")
                    .replaceAll("ó", "o")
                    .replaceAll("ú", "u")
                    +"_"+
                    Utilidades.fechaActualConHora()
                            .replaceAll(" " ,"")
                            .replaceAll("-" ,"")
                            .replaceAll(":", "_")+".png";

            DialogFirma dialogo = DialogFirma.newInstance(
                    Utilidades.TIPO_DOCUMENTO_CHECKLIST_COSECHA,
                    etRA,
                    Utilidades.DIALOG_TAG_REVISOR_LIMPIEZA_INGRESO,
                    (isSaved, path) -> {
                        if(isSaved){
                            check_firma_responsable_revision_limpieza_ingreso
                                    .setVisibility(View.VISIBLE);
                        }
                    }
            );

            dialogo.show(ft, Utilidades.DIALOG_TAG_REVISOR_LIMPIEZA_INGRESO);

        });

        btn_firma_responsable_aseo_salida.setOnClickListener(view1 -> {

            if(et_responsable_aseo_salida.getText().toString().isEmpty() ||
                    et_rut_responsable_aseo_salida.getText().toString().isEmpty() ){
                Toasty.warning(
                        requireActivity(),
                        "Debe ingresar nombre y rut de responsable",
                        Toast.LENGTH_LONG, true).show();
                return;
            }

            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentByTag(Utilidades.DIALOG_TAG_RESPONSABLE_ASEO_SALIDA);
            if (prev != null) {
                ft.remove(prev);
            }

            String etRA = et_responsable_aseo_salida.getText().toString()
                    .trim()
                    .toLowerCase(Locale.ROOT)
                    .replaceAll(" ", "_")
                    .replaceAll("ñ", "n")
                    .replaceAll("á", "a")
                    .replaceAll("é", "e")
                    .replaceAll("í", "i")
                    .replaceAll("ó", "o")
                    .replaceAll("ú", "u")
                    +"_"+
                    Utilidades.fechaActualConHora()
                            .replaceAll(" " ,"")
                            .replaceAll("-" ,"")
                            .replaceAll(":", "_")+".png";

            DialogFirma dialogo = DialogFirma.newInstance(
                    Utilidades.TIPO_DOCUMENTO_CHECKLIST_COSECHA,
                    etRA,
                    Utilidades.DIALOG_TAG_RESPONSABLE_ASEO_SALIDA,
                    (isSaved, path) -> {
                        if(isSaved){
                            check_firma_responsable_aseo_salida
                                    .setVisibility(View.VISIBLE);
                        }
                    }
            );

            dialogo.show(ft, Utilidades.DIALOG_TAG_RESPONSABLE_ASEO_SALIDA);
        });

        btn_firma_responsable_revision_limpieza_salida.setOnClickListener(view1 -> {

            if(et_responsable_revision_limpieza_salida.getText().toString().isEmpty()  ){
                Toasty.warning(
                        requireActivity(),
                        "Debe ingresar nombre de responsable",
                        Toast.LENGTH_LONG, true).show();
                return;
            }

            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentByTag(Utilidades.DIALOG_TAG_REVISOR_LIMPIEZA_SALIDA);
            if (prev != null) {
                ft.remove(prev);
            }

            String etRA = et_responsable_revision_limpieza_salida.getText().toString()
                    .trim()
                    .toLowerCase(Locale.ROOT)
                    .replaceAll(" ", "_")
                    .replaceAll("ñ", "n")
                    .replaceAll("á", "a")
                    .replaceAll("é", "e")
                    .replaceAll("í", "i")
                    .replaceAll("ó", "o")
                    .replaceAll("ú", "u")
                    +"_"+
                    Utilidades.fechaActualConHora()
                            .replaceAll(" " ,"")
                            .replaceAll("-" ,"")
                            .replaceAll(":", "_")+".png";

            DialogFirma dialogo = DialogFirma.newInstance(
                    Utilidades.TIPO_DOCUMENTO_CHECKLIST_COSECHA,
                    etRA,
                    Utilidades.DIALOG_TAG_REVISOR_LIMPIEZA_SALIDA,
                    (isSaved, path) -> {
                        if(isSaved){
                            check_firma_responsable_revision_limpieza_salida
                                    .setVisibility(View.VISIBLE);
                        }
                    }
            );

            dialogo.show(ft, Utilidades.DIALOG_TAG_REVISOR_LIMPIEZA_SALIDA);
        });


    }

    private boolean guardar(int state, String description) {

        String comparaSpinner = "--Seleccione--";
        //crear clase y guardar en bd

        //levantar modal para preguntar si quiere guardar y activar o solo guardar.
        CheckListCosecha siembra  = new CheckListCosecha();

        siembra.setEstado_documento(state);
        siembra.setApellido_checklist(description);
        siembra.setId_ac_cl_siembra(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));


        if(checkListSiembra == null){
            String claveUnica = config.getId()
                    +""+config.getId_usuario()
                    +""+Utilidades.fechaActualConHora()
                    .replaceAll(" ", "")
                    .replaceAll("-", "")
                    .replaceAll(":", "");

            siembra.setClave_unica( claveUnica );
        }else{
            siembra.setClave_unica( checkListSiembra.getClave_unica() );
        }


        //suelo
        if(btn_capacitacion_si.isChecked() || btn_capacitacion_no.isChecked()){
            int chequeoAislacion = (btn_capacitacion_si.isChecked()) ? 1 : 2;
            siembra.setCapacitacion(chequeoAislacion);
        }

        if(btn_cortemacho_si.isChecked() || btn_cortemacho_no.isChecked()){
            int chequeoAislacion = (btn_cortemacho_si.isChecked()) ? 1 : 2;
            siembra.setCorte_macho(chequeoAislacion);
        }

        if(btn_eliminacion_regueros_si.isChecked() || btn_eliminacion_regueros_no.isChecked()){
            int chequeoAislacion = (btn_eliminacion_regueros_si.isChecked()) ? 1 : 2;
            siembra.setEliminacion_regueros(chequeoAislacion);
        }

        if(btn_cabezal_ingreso_si.isChecked() || btn_cabezal_ingreso_no.isChecked()){
            int chequeoAislacion = (btn_cabezal_ingreso_si.isChecked()) ? 1 : 2;
            siembra.setCabezal_ingreso(chequeoAislacion);
        }

        if(btn_elevadores_ingreso_si.isChecked() || btn_elevadores_ingreso_no.isChecked()){
            int chequeoAislacion = (btn_elevadores_ingreso_si.isChecked()) ? 1 : 2;
            siembra.setElevadores_ingreso(chequeoAislacion);
        }

        if(btn_concavos_ingreso_si.isChecked() || btn_concavos_ingreso_no.isChecked()){
            int chequeoAislacion = (btn_concavos_ingreso_si.isChecked()) ? 1 : 2;
            siembra.setConcavos_ingreso(chequeoAislacion);
        }

        if(btn_harneros_ingreso_si.isChecked() || btn_harneros_ingreso_no.isChecked()){
            int chequeoAislacion = (btn_harneros_ingreso_si.isChecked()) ? 1 : 2;
            siembra.setHarneros_ingreso(chequeoAislacion);
        }

        if(btn_tolva_ingreso_si.isChecked() || btn_tolva_ingreso_no.isChecked()){
            int chequeoAislacion = (btn_tolva_ingreso_si.isChecked()) ? 1 : 2;
            siembra.setTolva_ingreso(chequeoAislacion);
        }
        if(btn_descarga_ingreso_si.isChecked() || btn_descarga_ingreso_no.isChecked()){
            int chequeoAislacion = (btn_descarga_ingreso_si.isChecked()) ? 1 : 2;
            siembra.setDescarga_ingreso(chequeoAislacion);
        }


        if(btn_cabezal_salida_si.isChecked() || btn_cabezal_salida_no.isChecked()){
            int chequeoAislacion = (btn_cabezal_salida_si.isChecked()) ? 1 : 2;
            siembra.setCabezal_salida(chequeoAislacion);
        }

        if(btn_elevadores_salida_si.isChecked() || btn_elevadores_salida_no.isChecked()){
            int chequeoAislacion = (btn_elevadores_salida_si.isChecked()) ? 1 : 2;
            siembra.setElevadores_salida(chequeoAislacion);
        }

        if(btn_concavos_salida_si.isChecked() || btn_concavos_salida_no.isChecked()){
            int chequeoAislacion = (btn_concavos_salida_si.isChecked()) ? 1 : 2;
            siembra.setConcavos_salida(chequeoAislacion);
        }

        if(btn_harneros_salida_si.isChecked() || btn_harneros_salida_no.isChecked()){
            int chequeoAislacion = (btn_harneros_salida_si.isChecked()) ? 1 : 2;
            siembra.setHarneros_salida(chequeoAislacion);
        }

        if(btn_tolva_salida_si.isChecked() || btn_tolva_salida_no.isChecked()){
            int chequeoAislacion = (btn_tolva_salida_si.isChecked()) ? 1 : 2;
            siembra.setTolva_salida(chequeoAislacion);
        }
        if(btn_descarga_salida_si.isChecked() || btn_descarga_salida_no.isChecked()){
            int chequeoAislacion = (btn_descarga_salida_si.isChecked()) ? 1 : 2;
            siembra.setDescarga_salida(chequeoAislacion);
        }


        if(!sp_acceso.getSelectedItem().toString().equals(comparaSpinner)){
            String cama_semilla = sp_acceso.getSelectedItem().toString();
            siembra.setAcceso(cama_semilla);
        }

        if(!sp_control_maleza.getSelectedItem().toString().equals(comparaSpinner)){
            String cama_semilla = sp_control_maleza.getSelectedItem().toString();
            siembra.setControl_maleza(cama_semilla);
        }
        if(!sp_humedad_suelo.getSelectedItem().toString().equals(comparaSpinner)){
            String cama_semilla = sp_humedad_suelo.getSelectedItem().toString();
            siembra.setHumedad_suelo(cama_semilla);
        }
        if(!sp_estado_visual.getSelectedItem().toString().equals(comparaSpinner)){
            String cama_semilla = sp_estado_visual.getSelectedItem().toString();
            siembra.setEstado_visual(cama_semilla);
        }

        if(!sp_humedad_cosecha.getSelectedItem().toString().equals(comparaSpinner)){
            String cama_semilla = sp_humedad_cosecha.getSelectedItem().toString();
            siembra.setHumedad_cosecha(cama_semilla);
        }


        //siembra anterior

        if(!responsable_cosecha.getText().toString().isEmpty()){
            String especie = responsable_cosecha.getText().toString();
            siembra.setResponsable_cosecha(especie);
        }

        if(!fecha_inicio_cosecha.getText().toString().isEmpty()){
            String especie = fecha_inicio_cosecha.getText().toString();
            siembra.setFecha_inicio_cosecha(especie);
        }

        if(!hora_inicio_cosecha.getText().toString().isEmpty()){
            String especie = hora_inicio_cosecha.getText().toString();
            siembra.setHora_inicio_cosecha(especie);
        }

        if(!et_comentario.getText().toString().isEmpty()){
            String especie = et_comentario.getText().toString();
            siembra.setComentarios(especie);
        }

        if(!et_prestador_servicio.getText().toString().isEmpty()){
            String especie = et_prestador_servicio.getText().toString();
            siembra.setPrestador_servicio(especie);
        }

        if(!et_operador_maquina.getText().toString().isEmpty()){
            String especie = et_operador_maquina.getText().toString();
            siembra.setNombre_operario_maquina(especie);
        }

        if(!et_marca_maquina.getText().toString().isEmpty()){
            String especie = et_marca_maquina.getText().toString();
            siembra.setSembradora_marca(especie);
        }

        if(!et_modelo_maquina.getText().toString().isEmpty()){
            String especie = et_modelo_maquina.getText().toString();
            siembra.setSembradora_modelo(especie);
        }

        if(!et_cosecha_anterior.getText().toString().isEmpty()){
            String especie = et_cosecha_anterior.getText().toString();
            siembra.setCosecha_anterior(especie);
        }


        if(!et_fecha_ingreso.getText().toString().isEmpty()){
            String especie = et_fecha_ingreso.getText().toString();
            siembra.setFecha_revision_limpieza_ingreso(especie);
        }

        if(!et_lugar_limpieza_ingreso.getText().toString().isEmpty()){
            String especie = et_lugar_limpieza_ingreso.getText().toString();
            siembra.setLugar_limpieza_ingreso(especie);
        }

        if(!et_responsable_aseo_ingreso.getText().toString().isEmpty()){
            String especie = et_responsable_aseo_ingreso.getText().toString();
            siembra.setResponsable_aseo_ingreso(especie);
        }
        if(!et_rut_responsable_aseo_ingreso.getText().toString().isEmpty()){
            String especie = et_rut_responsable_aseo_ingreso.getText().toString();
            siembra.setRut_responsable_aseo_ingreso(especie);
        }

        if(!et_responsable_revision_limpieza_ingreso.getText().toString().isEmpty()){
            String especie = et_responsable_revision_limpieza_ingreso.getText().toString();
            siembra.setResponsable_curimapu_ingreso(especie);
        }

        if(!et_fecha_salida.getText().toString().isEmpty()){
            String especie = et_fecha_salida.getText().toString();
            siembra.setFecha_revision_limpieza_salida(especie);
        }

        if(!et_lugar_limpieza_salida.getText().toString().isEmpty()){
            String especie = et_lugar_limpieza_salida.getText().toString();
            siembra.setLugar_limpieza_salida(especie);
        }

        if(!et_responsable_aseo_salida.getText().toString().isEmpty()){
            String especie = et_responsable_aseo_salida.getText().toString();
            siembra.setResponsable_aseo_salida(especie);
        }
        if(!et_rut_responsable_aseo_salida.getText().toString().isEmpty()){
            String especie = et_rut_responsable_aseo_salida.getText().toString();
            siembra.setRut_responsable_aseo_salida(especie);
        }

        if(!et_responsable_revision_limpieza_salida.getText().toString().isEmpty()){
            String especie = et_responsable_revision_limpieza_salida.getText().toString();
            siembra.setResponsable_curimapu_salida(especie);
        }


        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<List<TempFirmas>> firmasF = executor.submit(()
                -> MainActivity.myAppDB.DaoFirmas()
                .getFirmasByDocum(Utilidades.TIPO_DOCUMENTO_CHECKLIST_COSECHA));

        Future<Config> configFuture = executor.submit(() -> MainActivity.myAppDB.myDao().getConfig());


        if(checkListSiembra != null){
            siembra.setFirma_responsable_aseo_ingreso(checkListSiembra.getFirma_responsable_aseo_ingreso());
            siembra.setFirma_responsable_curimapu_ingreso(checkListSiembra.getFirma_responsable_curimapu_ingreso());
            siembra.setFirma_responsable_aseo_salida(checkListSiembra.getFirma_responsable_aseo_salida());
            siembra.setFirma_responsable_curimapu_salida(checkListSiembra.getFirma_responsable_curimapu_salida());
        }

        try {
            List<TempFirmas> firmas = firmasF.get();
            Config config = configFuture.get();
            siembra.setId_usuario(config.getId_usuario());

            for (TempFirmas ff : firmas ){

                switch (ff.getLugar_firma()){
                    case Utilidades.DIALOG_TAG_RESPONSABLE_ASEO_INGRESO:
                        siembra.setFirma_responsable_aseo_ingreso(ff.getPath());
                        break;
                    case Utilidades.DIALOG_TAG_REVISOR_LIMPIEZA_INGRESO:
                        siembra.setFirma_responsable_curimapu_ingreso(ff.getPath());
                        break;
                    case Utilidades.DIALOG_TAG_RESPONSABLE_ASEO_SALIDA:
                        siembra.setFirma_responsable_aseo_salida(ff.getPath());
                        break;
                    case Utilidades.DIALOG_TAG_REVISOR_LIMPIEZA_SALIDA:
                        siembra.setFirma_responsable_curimapu_salida(ff.getPath());
                        break;
                }
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        Future<Long> newIdFuture = null;
        Future<Integer> UpdIdFuture = null;

        if(checkListSiembra == null){
             newIdFuture =  executor.submit(() ->
                    MainActivity.myAppDB.DaoCheckListCosecha().insertClCosecha(siembra));
        }else{

            siembra.setId_cl_siembra(checkListSiembra.getId_cl_siembra());
            UpdIdFuture =  executor.submit(() ->
                    MainActivity.myAppDB.DaoCheckListCosecha().updateClCosecha(siembra));
        }



        try {

            if(checkListSiembra == null && newIdFuture != null){
                long newId = newIdFuture.get();
                if(newId > 0) {
                    Toasty.success(requireActivity(), "Guardado con exito", Toast.LENGTH_LONG, true).show();
                    cancelar();
                }else{
                    Toasty.error(requireActivity(), "No se pudo guardar con exito", Toast.LENGTH_LONG, true).show();
                }
            }else if(checkListSiembra != null && UpdIdFuture != null){
                long newId = UpdIdFuture.get();
                if(newId > 0) {
                    Toasty.success(requireActivity(), "Editado con exito", Toast.LENGTH_LONG, true).show();
                }else{
                    Toasty.error(requireActivity(), "No se pudo editar con exito", Toast.LENGTH_LONG, true).show();
                }
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Toasty.warning(requireActivity(), "Error al guardar ->"+ e.getMessage(), Toast.LENGTH_LONG, true).show();
        }

        return true;
    }


    private void cancelar() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(()
                -> MainActivity.myAppDB.DaoFirmas()
                .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_COSECHA));
        executorService.shutdown();
        activity.onBackPressed();
    }

    private void showAlertForConfirmarGuardar(){
        View viewInfalted = LayoutInflater.from(requireActivity()).inflate(R.layout.alert_guardar_checklist,null);

        RadioGroup grupo_radios_estado = viewInfalted.findViewById(R.id.grupo_radios_estado);
        RadioButton rbtn_activo = viewInfalted.findViewById(R.id.rbtn_activo);
        RadioButton rbtn_pendiente = viewInfalted.findViewById(R.id.rbtn_pendiente);
        EditText et_apellido = viewInfalted.findViewById(R.id.et_apellido);


        if(checkListSiembra != null){

            et_apellido.setText(checkListSiembra.getApellido_checklist());

            if(checkListSiembra.getEstado_documento() > 0){
                rbtn_activo.setChecked(checkListSiembra.getEstado_documento() == 1);
                rbtn_pendiente.setChecked(checkListSiembra.getEstado_documento() == 2);
            }

        }

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
                int state = (rbtn_activo.isChecked()) ? 1 : 2;
                boolean isSaved = guardar(state, et_apellido.getText().toString());
                if(isSaved) builder.dismiss();

            });
            c.setOnClickListener(view -> builder.dismiss());
        });
        builder.setCancelable(false);
        builder.show();
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
