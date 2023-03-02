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
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.tablas.CheckListSiembra;
import cl.smapdev.curimapu.clases.tablas.ChecklistDevolucionSemilla;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.temporales.TempFirmas;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.dialogos.DialogFirma;
import es.dmoral.toasty.Toasty;

public class FragmentCheckDevolucionSemilla extends Fragment {


    private MainActivity activity;
    private SharedPreferences prefs;

    //cabecera
    private TextView tv_numero_anexo;
    private TextView tv_variedad;

//    generales
    private EditText et_fecha;
    private EditText et_agronomo_responsable;
    private EditText et_agricultor;
    private EditText et_numero_guia;
    private EditText et_propuesta;
    private EditText et_especie;

    //hembra
    private EditText et_linea_hembra;
    private EditText et_lote_hembra;
    private EditText et_numero_envase_hembra;
    private EditText et_kg_aproximados_hembra;

//    macho
private EditText et_linea_macho;
    private EditText et_lote_macho;
    private EditText et_numero_envase_macho;
    private EditText et_kg_aproximados_macho;

    //    poli
    private EditText et_linea_poli;
    private EditText et_lote_poli;
    private EditText et_numero_envase_poli;
    private EditText et_kg_aproximados_poli;

    //botonera
    private Button btn_guardar_cl_siembra;
    private Button btn_cancelar_cl_siembra;


    private EditText et_nombre_responsable;
    private Button btn_firma_responsable;
    private ImageView check_firma_responsable;
    private EditText et_nombre_revisor;
    private Button btn_firma_revisor;
    private ImageView check_firma_revisor;

    private AnexoCompleto anexoCompleto;
    private Usuario usuario;
    private Config config;

    private ChecklistDevolucionSemilla checkListSiembra;




    public void setCheckListDS(ChecklistDevolucionSemilla checkListDS) {
        this.checkListSiembra = checkListDS;
    }

    public static FragmentCheckDevolucionSemilla newInstance(ChecklistDevolucionSemilla checkListDS ){

        FragmentCheckDevolucionSemilla fs = new FragmentCheckDevolucionSemilla();

        fs.setCheckListDS( checkListDS );

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
        return inflater.inflate(R.layout.fragment_checklist_devolucion_siembra, container, false);
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


        if(checkListSiembra != null){

            levantarDatos();

        }
    }

    private void levantarDatos() {


        if(checkListSiembra.getFecha() != null && !checkListSiembra.getFecha().isEmpty()){
            et_fecha.setText(checkListSiembra.getFecha());
        }

        if(checkListSiembra.getAgricultor() != null && !checkListSiembra.getAgricultor().isEmpty()){
            et_agricultor.setText(checkListSiembra.getAgricultor());
        }

        if(checkListSiembra.getAgronomo_responsable() != null && !checkListSiembra.getAgronomo_responsable().isEmpty()){
            et_agronomo_responsable.setText(checkListSiembra.getAgronomo_responsable());
        }

        if(checkListSiembra.getNumero_guia() != null && !checkListSiembra.getNumero_guia().isEmpty()){
            et_numero_guia.setText(checkListSiembra.getNumero_guia());
        }

        if(checkListSiembra.getPropuesta() != null && !checkListSiembra.getPropuesta().isEmpty()){
            et_propuesta.setText(checkListSiembra.getPropuesta());
        }

        if(checkListSiembra.getEspecie() != null && !checkListSiembra.getEspecie().isEmpty()){
            et_especie.setText(checkListSiembra.getEspecie());
        }


        if(checkListSiembra.getLinea_hembra() != null && !checkListSiembra.getLinea_hembra().isEmpty()){
            et_linea_hembra.setText(checkListSiembra.getLinea_hembra());
        }

        if(checkListSiembra.getLote_hembra() != null && !checkListSiembra.getLote_hembra().isEmpty()){
            et_lote_hembra.setText(checkListSiembra.getLote_hembra());
        }

        if(checkListSiembra.getNumero_envase_hembra() != null && !checkListSiembra.getNumero_envase_hembra().isEmpty()){
            et_numero_envase_hembra.setText(checkListSiembra.getNumero_envase_hembra());
        }

        if(checkListSiembra.getKg_aproximado_hembra() != null && !checkListSiembra.getKg_aproximado_hembra().isEmpty()){
            et_kg_aproximados_hembra.setText(checkListSiembra.getKg_aproximado_hembra());
        }


        if(checkListSiembra.getLinea_macho() != null && !checkListSiembra.getLinea_macho().isEmpty()){
            et_linea_macho.setText(checkListSiembra.getLinea_macho());
        }

        if(checkListSiembra.getLote_macho() != null && !checkListSiembra.getLote_macho().isEmpty()){
            et_lote_macho.setText(checkListSiembra.getLote_macho());
        }

        if(checkListSiembra.getNumero_envase_macho() != null && !checkListSiembra.getNumero_envase_macho().isEmpty()){
            et_numero_envase_macho.setText(checkListSiembra.getNumero_envase_macho());
        }

        if(checkListSiembra.getKg_aproximado_macho() != null && !checkListSiembra.getKg_aproximado_macho().isEmpty()){
            et_kg_aproximados_macho.setText(checkListSiembra.getKg_aproximado_macho());
        }

        if(checkListSiembra.getLinea_autopolinizacion() != null && !checkListSiembra.getLinea_autopolinizacion().isEmpty()){
            et_linea_poli.setText(checkListSiembra.getLinea_autopolinizacion());
        }

        if(checkListSiembra.getLote_autopolinizacion() != null && !checkListSiembra.getLote_autopolinizacion().isEmpty()){
            et_lote_poli.setText(checkListSiembra.getLote_autopolinizacion());
        }

        if(checkListSiembra.getNumero_envase_autopolinizacion() != null && !checkListSiembra.getNumero_envase_autopolinizacion().isEmpty()){
            et_numero_envase_poli.setText(checkListSiembra.getNumero_envase_autopolinizacion());
        }

        if(checkListSiembra.getKg_aproximado_autopolinizacion() != null && !checkListSiembra.getKg_aproximado_autopolinizacion().isEmpty()){
            et_kg_aproximados_poli.setText(checkListSiembra.getKg_aproximado_autopolinizacion());
        }

        if(checkListSiembra.getNombre_responsable() != null && !checkListSiembra.getNombre_responsable().isEmpty()){
            et_nombre_responsable.setText(checkListSiembra.getNombre_responsable());
        }

        if(checkListSiembra.getNombre_revisor_bodega() != null && !checkListSiembra.getNombre_revisor_bodega().isEmpty()){
            et_nombre_revisor.setText(checkListSiembra.getNombre_revisor_bodega());
        }


        if(checkListSiembra.getFirma_responsable() != null && !checkListSiembra.getFirma_responsable().isEmpty()){
            btn_firma_responsable.setEnabled(false);
            check_firma_responsable.setVisibility(View.VISIBLE);
        }

        if(checkListSiembra.getFirma_revisor_bodega() != null && !checkListSiembra.getFirma_revisor_bodega().isEmpty()){
            btn_firma_revisor.setEnabled(false);
            check_firma_revisor.setVisibility(View.VISIBLE);
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
            activity.updateView(getResources().getString(R.string.app_name), "CHECKLIST SIEMBRA");
        }
    }


    private void cargarDatosPrevios(){
        if(anexoCompleto == null){
            Toasty.error(requireActivity(), "No se pudo obtener informacion del anexo", Toast.LENGTH_LONG, true).show();
        }

        tv_numero_anexo.setText(anexoCompleto.getAnexoContrato().getAnexo_contrato());
        tv_variedad.setText(anexoCompleto.getVariedad().getDesc_variedad());


    }

    private void bind (View view ){

        //cabecera
        tv_numero_anexo = view.findViewById(R.id.tv_numero_anexo);
        tv_variedad = view.findViewById(R.id.tv_variedad);


        et_fecha = view.findViewById(R.id.et_fecha);
        et_agricultor = view.findViewById(R.id.et_agricultor);
        et_agronomo_responsable = view.findViewById(R.id.et_agronomo_responsable);
        et_numero_guia = view.findViewById(R.id.et_numero_guia);
        et_propuesta = view.findViewById(R.id.et_propuesta);
        et_especie = view.findViewById(R.id.et_especie);
        et_linea_hembra = view.findViewById(R.id.et_linea_hembra);
        et_lote_hembra = view.findViewById(R.id.et_lote_hembra);
        et_numero_envase_hembra = view.findViewById(R.id.et_numero_envase_hembra);
        et_kg_aproximados_hembra = view.findViewById(R.id.et_kg_aproximados_hembra);
        et_linea_macho = view.findViewById(R.id.et_linea_macho);
        et_lote_macho = view.findViewById(R.id.et_lote_macho);
        et_numero_envase_macho = view.findViewById(R.id.et_numero_envase_macho);
        et_kg_aproximados_macho = view.findViewById(R.id.et_kg_aproximados_macho);
        et_linea_poli = view.findViewById(R.id.et_linea_poli);
        et_lote_poli = view.findViewById(R.id.et_lote_poli);
        et_numero_envase_poli = view.findViewById(R.id.et_numero_envase_poli);
        et_kg_aproximados_poli = view.findViewById(R.id.et_kg_aproximados_poli);

        //botonera
        btn_guardar_cl_siembra = view.findViewById(R.id.btn_guardar_cl_siembra);
        btn_cancelar_cl_siembra = view.findViewById(R.id.btn_cancelar_cl_siembra);



        et_nombre_responsable = view.findViewById(R.id.et_nombre_responsable);
        btn_firma_responsable = view.findViewById(R.id.btn_firma_responsable);
        check_firma_responsable = view.findViewById(R.id.check_firma_responsable);
        et_nombre_revisor = view.findViewById(R.id.et_nombre_revisor);
        btn_firma_revisor = view.findViewById(R.id.btn_firma_revisor);
        check_firma_revisor = view.findViewById(R.id.check_firma_revisor);


        //botonera
        btn_guardar_cl_siembra.setOnClickListener(view1 -> showAlertForConfirmarGuardar());
        btn_cancelar_cl_siembra.setOnClickListener(view1 -> cancelar());



        et_fecha.setOnClickListener(view1 -> levantarFecha(et_fecha));
        et_fecha.setOnFocusChangeListener((view1, b) -> {if(b) levantarFecha(et_fecha);} );



//        check_firma_responsable_aseo_ingreso
//                check_firma_responsable_revision_limpieza_ingreso


        btn_firma_responsable.setOnClickListener(view1 -> {
            if(et_nombre_responsable.getText().toString().isEmpty()){
                Toasty.warning(
                        requireActivity(),
                        "Debe ingresar nombre de responsable",
                        Toast.LENGTH_LONG, true).show();
                return;
            }

            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentByTag(Utilidades.DIALOG_TAG_RESPONSABLE_DEVOLUCION_SIEMBRA);
            if (prev != null) {
                ft.remove(prev);
            }

            String etRA = UUID.randomUUID().toString()+".png";

            DialogFirma dialogo = DialogFirma.newInstance(
                    Utilidades.TIPO_DOCUMENTO_CHECKLIST_DEVOLUCION_SEMILLA,
                    etRA,
                    Utilidades.DIALOG_TAG_RESPONSABLE_DEVOLUCION_SIEMBRA,
                    (isSaved, path) -> {
                        if(isSaved){
                            check_firma_responsable.setVisibility(View.VISIBLE);
                        }
                    }
                    );

            dialogo.show(ft, Utilidades.DIALOG_TAG_RESPONSABLE_DEVOLUCION_SIEMBRA);
        });




        btn_firma_revisor.setOnClickListener(view1 -> {

            if(et_nombre_revisor.getText().toString().isEmpty()){
                Toasty.warning(
                        requireActivity(),
                        "Debe ingresar nombre de revisor",
                        Toast.LENGTH_LONG, true).show();
                return;
            }

            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentByTag(Utilidades.DIALOG_TAG_REVISOR_DEVOLUCION_SIEMBRA);
            if (prev != null) {
                ft.remove(prev);
            }

            String etRA = UUID.randomUUID().toString()+".png";

            DialogFirma dialogo = DialogFirma.newInstance(
                    Utilidades.TIPO_DOCUMENTO_CHECKLIST_DEVOLUCION_SEMILLA,
                    etRA,
                    Utilidades.DIALOG_TAG_REVISOR_DEVOLUCION_SIEMBRA,
                    (isSaved, path) -> {
                        if(isSaved){
                            check_firma_revisor
                                    .setVisibility(View.VISIBLE);
                        }
                    }
            );

            dialogo.show(ft, Utilidades.DIALOG_TAG_REVISOR_DEVOLUCION_SIEMBRA);

        });

    }

    private boolean guardar(int state, String description) {

        String comparaSpinner = "--Seleccione--";
        //crear clase y guardar en bd

        //levantar modal para preguntar si quiere guardar y activar o solo guardar.
        ChecklistDevolucionSemilla siembra  = new ChecklistDevolucionSemilla();

        siembra.setEstado_documento(state);
        siembra.setApellido_checklist( description);
        siembra.setId_ac_cl_devolucion_semilla(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));


        if(checkListSiembra == null){
            String claveUnica = UUID.randomUUID().toString();
            siembra.setClave_unica( claveUnica );
        }else{
            siembra.setClave_unica( checkListSiembra.getClave_unica() );
        }


        siembra.setFecha(et_fecha.getText().toString());
        siembra.setAgricultor(et_agricultor.getText().toString());
        siembra.setAgronomo_responsable(et_agronomo_responsable.getText().toString());
        siembra.setNumero_guia(et_numero_guia.getText().toString());
        siembra.setPropuesta(et_propuesta.getText().toString());
        siembra.setEspecie(et_especie.getText().toString());
        siembra.setLinea_hembra(et_linea_hembra.getText().toString());
        siembra.setLote_hembra(et_lote_hembra.getText().toString());
        siembra.setNumero_envase_hembra(et_numero_envase_hembra.getText().toString());
        siembra.setKg_aproximado_hembra(et_kg_aproximados_hembra.getText().toString());

        siembra.setLinea_macho(et_linea_macho.getText().toString());
        siembra.setLote_macho(et_lote_macho.getText().toString());
        siembra.setNumero_envase_macho(et_numero_envase_macho.getText().toString());
        siembra.setKg_aproximado_macho(et_kg_aproximados_macho.getText().toString());


        siembra.setLinea_autopolinizacion(et_linea_poli.getText().toString());
        siembra.setLote_autopolinizacion(et_lote_poli.getText().toString());
        siembra.setNumero_envase_autopolinizacion(et_numero_envase_poli.getText().toString());
        siembra.setKg_aproximado_autopolinizacion(et_kg_aproximados_poli.getText().toString());



        siembra.setNombre_responsable(et_nombre_responsable.getText().toString());
        siembra.setNombre_revisor_bodega(et_nombre_revisor.getText().toString());


        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<List<TempFirmas>> firmasF = executor.submit(()
                -> MainActivity.myAppDB.DaoFirmas()
                .getFirmasByDocum(Utilidades.TIPO_DOCUMENTO_CHECKLIST_DEVOLUCION_SEMILLA));

        Future<Config> configFuture = executor.submit(() -> MainActivity.myAppDB.myDao().getConfig());


        if(checkListSiembra != null){
            siembra.setFirma_responsable(checkListSiembra.getFirma_responsable());
            siembra.setFirma_revisor_bodega(checkListSiembra.getFirma_revisor_bodega());
        }

        try {
            List<TempFirmas> firmas = firmasF.get();
            Config config = configFuture.get();
            siembra.setId_usuario(config.getId_usuario());

            for (TempFirmas ff : firmas ){

                switch (ff.getLugar_firma()){
                    case Utilidades.DIALOG_TAG_RESPONSABLE_DEVOLUCION_SIEMBRA:
                        siembra.setFirma_responsable(ff.getPath());
                        break;
                    case Utilidades.DIALOG_TAG_REVISOR_DEVOLUCION_SIEMBRA:
                        siembra.setFirma_revisor_bodega(ff.getPath());
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
                    MainActivity.myAppDB.DaoCheckListDevolucionSemilla().insertClDevolucionSemilla(siembra));
        }else{

            siembra.setId_cl_devolucion_semilla(checkListSiembra.getId_cl_devolucion_semilla());
            UpdIdFuture =  executor.submit(() ->
                    MainActivity.myAppDB.DaoCheckListDevolucionSemilla().updateClDevolucionSemilla(siembra));
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
                .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_DEVOLUCION_SEMILLA));
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
