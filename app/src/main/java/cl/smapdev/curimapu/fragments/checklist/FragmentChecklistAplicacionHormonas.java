package cl.smapdev.curimapu.fragments.checklist;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.SpinnerAdapter;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.tablas.CheckListAplicacionHormonas;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.DesplegableAplicacionHormonas;
import cl.smapdev.curimapu.clases.tablas.DesplegableNumeroAplicacionHormonas;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.temporales.TempFirmas;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.dialogos.DialogFirma;
import es.dmoral.toasty.Toasty;

public class FragmentChecklistAplicacionHormonas extends Fragment {

    private CheckListAplicacionHormonas checklist;
    private MainActivity activity;
    private SharedPreferences prefs;


    private AnexoCompleto anexoCompleto;
    private Usuario usuario;
    private Config config;


    private TextView tv_responsable, tv_variedad, tv_anexo, tv_agricultor, tv_potrero, tv_ha, tv_lote_cvs;
    private EditText et_prestador_servicio, et_n_hojas_verdaderas, et_fecha, et_hora_inicio, et_hora_termino, et_ppm, et_mojamiento, et_observacion;
    private Spinner sp_aplicacion, sp_n_aplicacion;
    private Button btn_firma_prestador_servicio, btn_firma_responsable, btn_guardar_cl_ap_hormona, btn_cancelar_cl_ap_hormona;
    private ImageView check_firma_prestador_servicio, check_firma_responsable;


    private final ArrayList<String> chk_aplicacion = new ArrayList<>();
    private final ArrayList<String> chk_n_aplicacion = new ArrayList<>();


    public void setChecklist(CheckListAplicacionHormonas checklist) {
        this.checklist = checklist;
    }

    public static FragmentChecklistAplicacionHormonas newInstance(CheckListAplicacionHormonas clApHormonas) {
        FragmentChecklistAplicacionHormonas fragment = new FragmentChecklistAplicacionHormonas();
        fragment.setChecklist(clApHormonas);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity a = (MainActivity) getActivity();
        if (a != null) activity = a;
        prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_checklist_aplicacion_hormonas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //chk_aplicacion.addAll(Arrays.asList(getResources().getStringArray(R.array.desplegable_checklist_1)));

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<AnexoCompleto> futureVisitas = executor.submit(() ->
                MainActivity
                        .myAppDB
                        .myDao()
                        .getAnexoCompletoById(
                                prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, "")
                        )
        );

        Future<List<DesplegableAplicacionHormonas>> appHorm = executor.submit(() ->
                MainActivity
                        .myAppDB
                        .DaoDesplegables()
                        .getDesplegableAplicacionHormonas()
        );
        Future<List<DesplegableNumeroAplicacionHormonas>> appHormNum = executor.submit(() ->
                MainActivity
                        .myAppDB
                        .DaoDesplegables()
                        .getDesplegableNumeroAplicacionHormonas()
        );

        Future<Config> futureConfig = executor.submit(() ->
                MainActivity.myAppDB.myDao().getConfig());

        try {
            anexoCompleto = futureVisitas.get();
            config = futureConfig.get();

            List<DesplegableAplicacionHormonas> desplegableAplicacionHormonas = appHorm.get();
            List<DesplegableNumeroAplicacionHormonas> desplegableAplicacionHormonasNumero = appHormNum.get();

            for (DesplegableAplicacionHormonas d : desplegableAplicacionHormonas) {
                chk_aplicacion.add(d.getAplicacion());
            }

            for (DesplegableNumeroAplicacionHormonas d : desplegableAplicacionHormonasNumero) {
                chk_n_aplicacion.add(d.getNumeroAplicacion());
            }

            Future<Usuario> usuarioFuture = executor.submit(() ->
                    MainActivity.myAppDB.myDao().getUsuarioById(config.getId_usuario()));

            usuario = usuarioFuture.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();

        bind(view);

        if (checklist != null) {
            levantarDatos();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (activity != null) {
            activity.updateView(getResources().getString(R.string.app_name), "CHECKLIST APLICACION HORMONAS");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        cargarDatosPrevios();
    }

    private void bind(View view) {


        tv_responsable = view.findViewById(R.id.tv_responsable);
        tv_variedad = view.findViewById(R.id.tv_variedad);
        tv_anexo = view.findViewById(R.id.tv_anexo);
        tv_agricultor = view.findViewById(R.id.tv_agricultor);
        tv_potrero = view.findViewById(R.id.tv_potrero);
        tv_lote_cvs = view.findViewById(R.id.tv_lote_cvs);
        tv_ha = view.findViewById(R.id.tv_ha);
        et_prestador_servicio = view.findViewById(R.id.et_prestador_servicio);
        et_n_hojas_verdaderas = view.findViewById(R.id.et_n_hojas_verdaderas);
        et_hora_inicio = view.findViewById(R.id.et_hora_inicio);
        et_fecha = view.findViewById(R.id.et_fecha);
        et_hora_termino = view.findViewById(R.id.et_hora_termino);
        et_ppm = view.findViewById(R.id.et_ppm);
        et_mojamiento = view.findViewById(R.id.et_mojamiento);
        et_observacion = view.findViewById(R.id.et_observacion);
        sp_aplicacion = view.findViewById(R.id.sp_aplicacion);
        sp_n_aplicacion = view.findViewById(R.id.sp_n_aplicacion);
        btn_firma_prestador_servicio = view.findViewById(R.id.btn_firma_prestador_servicio);
        btn_firma_responsable = view.findViewById(R.id.btn_firma_responsable);
        btn_guardar_cl_ap_hormona = view.findViewById(R.id.btn_guardar_cl_ap_hormona);
        btn_cancelar_cl_ap_hormona = view.findViewById(R.id.btn_cancelar_cl_ap_hormona);
        check_firma_prestador_servicio = view.findViewById(R.id.check_firma_prestador_servicio);
        check_firma_responsable = view.findViewById(R.id.check_firma_responsable);


        sp_aplicacion.setAdapter(new SpinnerAdapter(requireActivity(), R.layout.spinner_template_view, chk_aplicacion));
        sp_n_aplicacion.setAdapter(new SpinnerAdapter(requireActivity(), R.layout.spinner_template_view, chk_n_aplicacion));


        et_fecha.setOnClickListener(view1 -> Utilidades.levantarFecha(et_fecha, requireContext()));
        et_fecha.setOnFocusChangeListener((view1, b) -> {
            if (b) Utilidades.levantarFecha(et_fecha, requireContext());
        });

        et_hora_inicio.setOnClickListener(view1 -> Utilidades.levantarHora(et_hora_inicio, requireActivity()));
        et_hora_inicio.setOnFocusChangeListener((view1, b) -> {
            if (b) Utilidades.levantarHora(et_hora_inicio, requireActivity());
        });


        et_hora_termino.setOnClickListener(view1 -> Utilidades.levantarHora(et_hora_termino, requireActivity()));
        et_hora_termino.setOnFocusChangeListener((view1, b) -> {
            if (b) Utilidades.levantarHora(et_hora_termino, requireActivity());
        });


        btn_firma_responsable.setOnClickListener(view1 -> {

            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentByTag(Utilidades.DIALOG_TAG_FIRMA_RESPONSABLE_AP_HORMONA);
            if (prev != null) {
                ft.remove(prev);
            }

            String etRA = UUID.randomUUID().toString() + ".png";

            DialogFirma dialogo = DialogFirma.newInstance(
                    Utilidades.TIPO_DOCUMENTO_CHECKLIST_APLICACION_HORMONAS,
                    etRA,
                    Utilidades.DIALOG_TAG_FIRMA_RESPONSABLE_AP_HORMONA,
                    (isSaved, path) -> {
                        if (isSaved) {
                            check_firma_responsable.setVisibility(View.VISIBLE);
                        }
                    }
            );

            dialogo.show(ft, Utilidades.DIALOG_TAG_FIRMA_RESPONSABLE_AP_HORMONA);
        });


        btn_firma_prestador_servicio.setOnClickListener(view1 -> {

            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentByTag(Utilidades.DIALOG_TAG_FIRMA_PRESTADOR_SERVICIO_AP_HORMONA);
            if (prev != null) {
                ft.remove(prev);
            }

            String etRA = UUID.randomUUID().toString() + ".png";

            DialogFirma dialogo = DialogFirma.newInstance(
                    Utilidades.TIPO_DOCUMENTO_CHECKLIST_APLICACION_HORMONAS,
                    etRA,
                    Utilidades.DIALOG_TAG_FIRMA_PRESTADOR_SERVICIO_AP_HORMONA,
                    (isSaved, path) -> {
                        if (isSaved) {
                            check_firma_prestador_servicio.setVisibility(View.VISIBLE);
                        }
                    }
            );

            dialogo.show(ft, Utilidades.DIALOG_TAG_FIRMA_PRESTADOR_SERVICIO_AP_HORMONA);
        });


        btn_guardar_cl_ap_hormona.setOnClickListener(view1 -> {


            if (et_prestador_servicio.getText().toString().isEmpty()) {
                Toasty.warning(requireActivity(), "Debes agregar quien es el prestador de servicios",
                        Toast.LENGTH_LONG, true).show();
                return;
            }

            if (sp_aplicacion.getSelectedItem().toString().equals("SELECCIONE")) {
                Toasty.warning(requireActivity(), "Debes seleccionar una aplicación",
                        Toast.LENGTH_LONG, true).show();
                return;
            }


            showAlertForConfirmarGuardar();


        });
        btn_cancelar_cl_ap_hormona.setOnClickListener(view1 -> activity.onBackPressed());

    }


    private void cargarDatosPrevios() {
        if (anexoCompleto == null) {
            Toasty.error(requireActivity(), "No se pudo obtener informacion del anexo", Toast.LENGTH_LONG, true).show();
        }


        tv_anexo.setText(anexoCompleto.getAnexoContrato().getAnexo_contrato());
        tv_variedad.setText(anexoCompleto.getVariedad().getDesc_variedad());
        tv_agricultor.setText(anexoCompleto.getAgricultor().getNombre_agricultor());

        tv_potrero.setText(anexoCompleto.getLotes().getNombre_lote());
        tv_ha.setText(anexoCompleto.getAnexoContrato().getHas_contrato());

        tv_responsable.setText(usuario.getNombre() + " " + usuario.getApellido_p());
        tv_lote_cvs.setText(anexoCompleto.getLotes().getNombre_lote());
    }


    private void showAlertForConfirmarGuardar() {
        View viewInfalted = LayoutInflater.from(requireActivity()).inflate(R.layout.alert_guardar_checklist, null);

        RadioButton rbtn_activo = viewInfalted.findViewById(R.id.rbtn_activo);
        RadioButton rbtn_pendiente = viewInfalted.findViewById(R.id.rbtn_pendiente);
        EditText et_apellido = viewInfalted.findViewById(R.id.et_apellido);

        rbtn_activo.setChecked(true);

        rbtn_activo.setVisibility(View.GONE);
        rbtn_pendiente.setVisibility(View.GONE);

        if (checklist != null) {
            et_apellido.setText(checklist.getApellido_checklist());
        }


//        if (checklist != null) {
//
//            et_apellido.setText(checklist.getApellido_checklist());
//
//            if (checklist.getEstado_documento() > 0) {
//                rbtn_activo.setChecked(checklist.getEstado_documento() == 1);
//                rbtn_pendiente.setChecked(checklist.getEstado_documento() == 2);
//            }
//
//        }

        final androidx.appcompat.app.AlertDialog builder = new androidx.appcompat.app.AlertDialog.Builder(requireActivity())
                .setView(viewInfalted)
                .setPositiveButton(getResources().getString(R.string.guardar), (dialogInterface, i) -> {
                })
                .setNegativeButton(getResources().getString(R.string.nav_cancel), (dialogInterface, i) -> {
                })
                .create();

        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
            Button c = builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE);
            b.setOnClickListener(view -> {

                if ((!rbtn_activo.isChecked() && !rbtn_pendiente.isChecked()) || et_apellido.getText().toString().isEmpty()) {
                    Toasty.error(requireActivity(),
                            "Debes seleccionar un estado e ingresar una descripcion",
                            Toast.LENGTH_LONG, true).show();
                    return;
                }
                int state = (rbtn_activo.isChecked()) ? 1 : 2;
                boolean isSaved = onSave(state, et_apellido.getText().toString());
                if (isSaved) {
                    builder.dismiss();
                    activity.onBackPressed();
                }


            });
            c.setOnClickListener(view -> builder.dismiss());
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void levantarDatos() {


        if (checklist.getPrestador_servicio() != null && !checklist.getPrestador_servicio().isEmpty()) {
            et_prestador_servicio.setText(checklist.getPrestador_servicio());
        }
        if (checklist.getN_hojas_verdaderas() != null && !checklist.getN_hojas_verdaderas().isEmpty()) {
            et_n_hojas_verdaderas.setText(checklist.getN_hojas_verdaderas());
        }
        if (checklist.getHora_inicio() != null && !checklist.getHora_inicio().isEmpty()) {
            et_hora_inicio.setText(checklist.getHora_inicio());
        }

        if (checklist.getFecha_inicio() != null && !checklist.getFecha_inicio().isEmpty()) {
            et_fecha.setText(Utilidades.voltearFechaVista(checklist.getFecha_inicio()));
        }

        if (checklist.getHora_termino() != null && !checklist.getHora_termino().isEmpty()) {
            et_hora_termino.setText(checklist.getHora_termino());
        }

        if (checklist.getPpm() != null && !checklist.getPpm().isEmpty()) {
            et_ppm.setText(checklist.getPpm());
        }

        if (checklist.getMojamiento_lt_ha() != null && !checklist.getMojamiento_lt_ha().isEmpty()) {
            et_mojamiento.setText(checklist.getMojamiento_lt_ha());
        }

        if (checklist.getObservacion_texto() != null && !checklist.getObservacion_texto().isEmpty()) {
            et_observacion.setText(checklist.getObservacion_texto());
        }

        if (checklist.getAplicacion() != null) {
            sp_aplicacion.setSelection(chk_aplicacion.indexOf(checklist.getAplicacion()));
        }
        if (checklist.getN_de_aplicacion() != null) {
            sp_n_aplicacion.setSelection(chk_n_aplicacion.indexOf(checklist.getN_de_aplicacion()));
        }

        if (checklist.getFirma_responsable_ap_hormonas() != null && !checklist.getFirma_responsable_ap_hormonas().isEmpty()) {
            btn_firma_responsable.setEnabled(false);
            check_firma_responsable.setVisibility(View.VISIBLE);

        }

        if (checklist.getFirma_prestador_servicio_ap_hormonas() != null && !checklist.getFirma_prestador_servicio_ap_hormonas().isEmpty()) {
            btn_firma_prestador_servicio.setEnabled(false);
            check_firma_prestador_servicio.setVisibility(View.VISIBLE);
        }
    }

    private boolean onSave(int state, String apellido) {


        ExecutorService executor = Executors.newSingleThreadExecutor();

        CheckListAplicacionHormonas clHormonas = new CheckListAplicacionHormonas();
        String claveUnica = UUID.randomUUID().toString();
        if (checklist != null) {
            claveUnica = checklist.getClave_unica();
            clHormonas.setId_cl_ap_hormonas(checklist.getId_cl_ap_hormonas());
            clHormonas.setFecha_hora_tx(checklist.getFecha_hora_tx());
            clHormonas.setFecha_hora_mod(Utilidades.fechaActualConHora());
            clHormonas.setId_usuario_mod(usuario.getId_usuario());
            clHormonas.setId_usuario(checklist.getId_usuario());

            clHormonas.setFirma_prestador_servicio_ap_hormonas(checklist.getFirma_prestador_servicio_ap_hormonas());
            clHormonas.setFirma_responsable_ap_hormonas(checklist.getFirma_responsable_ap_hormonas());

        } else {
            clHormonas.setFecha_hora_tx(Utilidades.fechaActualConHora());
            clHormonas.setId_usuario(usuario.getId_usuario());
        }

        Future<List<TempFirmas>> firmasF = executor.submit(()
                -> MainActivity.myAppDB.DaoFirmas()
                .getFirmasByDocum(Utilidades.TIPO_DOCUMENTO_CHECKLIST_APLICACION_HORMONAS));


        try {
            List<TempFirmas> firmas = firmasF.get();

            for (TempFirmas ff : firmas) {

                switch (ff.getLugar_firma()) {
                    case Utilidades.DIALOG_TAG_FIRMA_RESPONSABLE_AP_HORMONA:
                        clHormonas.setFirma_responsable_ap_hormonas(ff.getPath());
                        break;
                    case Utilidades.DIALOG_TAG_FIRMA_PRESTADOR_SERVICIO_AP_HORMONA:
                        clHormonas.setFirma_prestador_servicio_ap_hormonas(ff.getPath());
                        break;

                }
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        clHormonas.setId_ac_cl_ap_hormonas(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));
        clHormonas.setApellido_checklist(apellido);
        clHormonas.setClave_unica(claveUnica);
        clHormonas.setEstado_sincronizacion(0);
        clHormonas.setEstado_documento(state);
        clHormonas.setPrestador_servicio(et_prestador_servicio.getText().toString());
        clHormonas.setAplicacion(sp_aplicacion.getSelectedItem().toString());
        clHormonas.setN_hojas_verdaderas(et_n_hojas_verdaderas.getText().toString());
        clHormonas.setN_de_aplicacion(sp_n_aplicacion.getSelectedItem().toString());
        clHormonas.setFecha_inicio(Utilidades.voltearFechaBD(et_fecha.getText().toString()));
        clHormonas.setHora_inicio(et_hora_inicio.getText().toString());
        clHormonas.setHora_termino(et_hora_termino.getText().toString());
        clHormonas.setPpm(et_ppm.getText().toString());
        clHormonas.setMojamiento_lt_ha(et_mojamiento.getText().toString());
        clHormonas.setObservacion_texto(et_observacion.getText().toString());


        if (checklist != null) {
            Future<Integer> id = executor.submit(() -> MainActivity.myAppDB.DaoCLAplicacionHormonas().updateCLApHormonas(clHormonas));
            try {
                long newId = id.get();
                if (newId > 0) {
                    Toasty.success(requireActivity(), "Guardado con exito", Toast.LENGTH_LONG, true).show();
                    cancelar();
                } else {
                    Toasty.error(requireActivity(), "No se pudo guardar con exito", Toast.LENGTH_LONG, true).show();
                }


            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                Toasty.warning(requireActivity(), "Error al guardar ->" + e.getMessage(), Toast.LENGTH_LONG, true).show();
            }


        } else {
            Future<Long> id = executor.submit(() -> MainActivity.myAppDB.DaoCLAplicacionHormonas().insertCLApHormonas(clHormonas));


            try {
                long newId = id.get();
                if (newId > 0) {
                    Toasty.success(requireActivity(), "Guardado con exito", Toast.LENGTH_LONG, true).show();
                    cancelar();
                } else {
                    Toasty.error(requireActivity(), "No se pudo guardar con exito", Toast.LENGTH_LONG, true).show();
                }


            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                Toasty.warning(requireActivity(), "Error al guardar ->" + e.getMessage(), Toast.LENGTH_LONG, true).show();
            }

        }

        return true;
    }

    private void cancelar() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.submit(()
                -> MainActivity.myAppDB.DaoFirmas()
                .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_APLICACION_HORMONAS));
        executorService.shutdown();
        activity.onBackPressed();
    }

}

