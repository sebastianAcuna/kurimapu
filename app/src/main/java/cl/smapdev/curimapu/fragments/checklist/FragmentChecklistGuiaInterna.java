package cl.smapdev.curimapu.fragments.checklist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.tablas.CheckListGuiaInterna;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.temporales.TempFirmas;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.dialogos.DialogFirma;
import es.dmoral.toasty.Toasty;

public class FragmentChecklistGuiaInterna extends Fragment {

    private CheckListGuiaInterna checklist;
    private MainActivity activity;
    private SharedPreferences prefs;

    private AnexoCompleto anexoCompleto;
    private Usuario usuario;
    private Config config;


    private Button btn_firma_transportista, btn_firma_supervisor, btn_guardar_cl, btn_cancelar_cl;


    private ImageView check_firma_transportista, check_firma_supervisor;


    private EditText et_of,
            et_n_guia_despacho,
            et_parcialidad_lote,
            et_fin_lote,
            et_fin_cosecha,
            et_lote_campo,
            et_cantidad_sacos,
            et_color_sacos,
            et_cantidad_jumbo,
            et_color_jumbo,
            et_cantidad_bins,
            et_color_bins,
            et_kilos_estimados,
            et_fecha_cosecha,
            et_n_maquina,
            et_fecha_trilla,
            et_vb_sup_limpieza,
            et_humedad,
            et_temperatura,
            et_malezas,
            et_resto_vegetales,
            et_nombre_transportista,
            et_contacto_transportista,
            et_patente_transportista,
            et_nombre_supervisor,
            et_contacto_supervisor,
            et_observaciones;


    private TextView tv_rs_agricultor,
            tv_rut_agricultor,
            tv_variedad,
            tv_anexo,
            tv_correlativo,
            tv_fecha;

    // Executor reutilizable para operaciones DB rápidas (evita crear/shutdown por click)
    private final ExecutorService singleDbExecutor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public void setChecklist(CheckListGuiaInterna checklist) {
        this.checklist = checklist;
    }

    public static FragmentChecklistGuiaInterna newInstance(CheckListGuiaInterna clist) {
        FragmentChecklistGuiaInterna fragment = new FragmentChecklistGuiaInterna();
        fragment.setChecklist(clist);
        return fragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            activity = (MainActivity) context;
            prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
        } else {
            throw new RuntimeException(context + " must be MainActivity");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (singleDbExecutor != null && !singleDbExecutor.isShutdown()) {
            singleDbExecutor.shutdown();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_checklist_guia_interna, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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


        bind(view);
        if (checklist != null) {
            levantarDatos();
        } else {
            tv_fecha.setText(Utilidades.voltearFechaVista(Utilidades.fechaActualSinHora()));
        }

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        Utilidades.setToolbar(activity, view, getResources().getString(R.string.app_name), "CHECKLIST GUIA INTERNA");
    }


    private void bind(View view) {

        btn_firma_transportista = view.findViewById(R.id.btn_firma_transportista);
        btn_firma_supervisor = view.findViewById(R.id.btn_firma_supervisor);
        btn_guardar_cl = view.findViewById(R.id.btn_guardar_cl);
        btn_cancelar_cl = view.findViewById(R.id.btn_cancelar_cl);


        check_firma_transportista = view.findViewById(R.id.check_firma_transportista);
        check_firma_supervisor = view.findViewById(R.id.check_firma_supervisor);
        et_of = view.findViewById(R.id.et_of);
        et_n_guia_despacho = view.findViewById(R.id.et_n_guia_despacho);
        et_parcialidad_lote = view.findViewById(R.id.et_parcialidad_lote);
        et_fin_lote = view.findViewById(R.id.et_fin_lote);
        et_fin_cosecha = view.findViewById(R.id.et_fin_cosecha);
        et_lote_campo = view.findViewById(R.id.et_lote_campo);
        et_cantidad_sacos = view.findViewById(R.id.et_cantidad_sacos);
        et_color_sacos = view.findViewById(R.id.et_color_sacos);
        et_cantidad_jumbo = view.findViewById(R.id.et_cantidad_jumbo);
        et_color_jumbo = view.findViewById(R.id.et_color_jumbo);
        et_cantidad_bins = view.findViewById(R.id.et_cantidad_bins);
        et_color_bins = view.findViewById(R.id.et_color_bins);
        et_kilos_estimados = view.findViewById(R.id.et_kilos_estimados);
        et_fecha_cosecha = view.findViewById(R.id.et_fecha_cosecha);
        et_n_maquina = view.findViewById(R.id.et_n_maquina);
        et_fecha_trilla = view.findViewById(R.id.et_fecha_trilla);
        et_vb_sup_limpieza = view.findViewById(R.id.et_vb_sup_limpieza);
        et_humedad = view.findViewById(R.id.et_humedad);
        et_temperatura = view.findViewById(R.id.et_temperatura);
        et_malezas = view.findViewById(R.id.et_malezas);
        et_resto_vegetales = view.findViewById(R.id.et_resto_vegetales);
        et_nombre_transportista = view.findViewById(R.id.et_nombre_transportista);
        et_contacto_transportista = view.findViewById(R.id.et_contacto_transportista);
        et_patente_transportista = view.findViewById(R.id.et_patente_transportista);
        et_nombre_supervisor = view.findViewById(R.id.et_nombre_supervisor);
        et_contacto_supervisor = view.findViewById(R.id.et_contacto_supervisor);
        et_observaciones = view.findViewById(R.id.et_observaciones);

        tv_rs_agricultor = view.findViewById(R.id.tv_rs_agricultor);
        tv_rut_agricultor = view.findViewById(R.id.tv_rut_agricultor);
        tv_variedad = view.findViewById(R.id.tv_variedad);
        tv_anexo = view.findViewById(R.id.tv_anexo);
        tv_correlativo = view.findViewById(R.id.tv_correlativo);
        tv_fecha = view.findViewById(R.id.tv_fecha);


        btn_firma_supervisor.setOnClickListener(view1 -> {

            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentByTag(Utilidades.DIALOG_TAG_GUIA_INTERNA_SUPERVISOR);
            if (prev != null) {
                ft.remove(prev);
            }

            String etRA = UUID.randomUUID().toString() + ".png";

            DialogFirma dialogo = DialogFirma.newInstance(
                    Utilidades.TIPO_DOCUMENTO_CHECKLIST_GUIA_INTERNA,
                    etRA,
                    Utilidades.DIALOG_TAG_GUIA_INTERNA_SUPERVISOR,
                    (isSaved, path) -> {
                        if (isSaved) {
                            check_firma_supervisor.setVisibility(View.VISIBLE);
                        }
                    }
            );

            dialogo.show(ft, Utilidades.DIALOG_TAG_GUIA_INTERNA_SUPERVISOR);
        });


        btn_firma_transportista.setOnClickListener(view1 -> {

            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentByTag(Utilidades.DIALOG_TAG_GUIA_INTERNA_TRANSPORTISTA);
            if (prev != null) {
                ft.remove(prev);
            }

            String etRA = UUID.randomUUID().toString() + ".png";

            DialogFirma dialogo = DialogFirma.newInstance(
                    Utilidades.TIPO_DOCUMENTO_CHECKLIST_GUIA_INTERNA,
                    etRA,
                    Utilidades.DIALOG_TAG_GUIA_INTERNA_TRANSPORTISTA,
                    (isSaved, path) -> {
                        if (isSaved) {
                            check_firma_transportista.setVisibility(View.VISIBLE);
                        }
                    }
            );

            dialogo.show(ft, Utilidades.DIALOG_TAG_GUIA_INTERNA_TRANSPORTISTA);
        });


        et_fin_lote.setKeyListener(null);
        et_fin_lote.setInputType(InputType.TYPE_NULL);
        et_fin_lote.setOnClickListener(view1 -> Utilidades.levantarFecha(et_fin_lote, requireActivity()));
        et_fin_lote.setOnFocusChangeListener((view1, b) -> {
            Utilidades.hideKeyboard(activity);
            if (b) Utilidades.levantarFecha(et_fin_lote, requireActivity());
        });


        et_fin_cosecha.setKeyListener(null);
        et_fin_cosecha.setInputType(InputType.TYPE_NULL);
        et_fin_cosecha.setOnClickListener(view1 -> Utilidades.levantarFecha(et_fin_cosecha, requireActivity()));
        et_fin_cosecha.setOnFocusChangeListener((view1, b) -> {
            Utilidades.hideKeyboard(activity);
            if (b) Utilidades.levantarFecha(et_fin_cosecha, requireActivity());
        });


        et_fecha_cosecha.setKeyListener(null);
        et_fecha_cosecha.setInputType(InputType.TYPE_NULL);
        et_fecha_cosecha.setOnClickListener(view1 -> Utilidades.levantarFecha(et_fecha_cosecha, requireActivity()));
        et_fecha_cosecha.setOnFocusChangeListener((view1, b) -> {
            Utilidades.hideKeyboard(activity);
            if (b) Utilidades.levantarFecha(et_fecha_cosecha, requireActivity());
        });

        et_fecha_trilla.setKeyListener(null);
        et_fecha_trilla.setInputType(InputType.TYPE_NULL);
        et_fecha_trilla.setOnClickListener(view1 -> Utilidades.levantarFecha(et_fecha_trilla, requireActivity()));
        et_fecha_trilla.setOnFocusChangeListener((view1, b) -> {
            Utilidades.hideKeyboard(activity);
            if (b) Utilidades.levantarFecha(et_fecha_trilla, requireActivity());
        });


        btn_cancelar_cl.setOnClickListener(v -> cancelar());
        btn_guardar_cl.setOnClickListener(v -> onSave());

    }


    private void levantarDatos() {


        tv_correlativo.setText(String.valueOf(checklist.getCorrelativo()));
        if (checklist.getFecha() != null) {
            tv_fecha.setText(Utilidades.voltearFechaVista(checklist.getFecha()));
        }
        if (checklist.getOf() != null) {
            et_of.setText(checklist.getOf());
        }
        if (checklist.getN_guia_despacho() != null) {
            et_n_guia_despacho.setText(checklist.getN_guia_despacho());
        }
        if (checklist.getParcialidad_lote() != null) {
            et_parcialidad_lote.setText(checklist.getParcialidad_lote());
        }
        if (checklist.getFin_lote() != null) {
            et_fin_lote.setText(Utilidades.voltearFechaVista(checklist.getFin_lote()));
        }
        if (checklist.getFin_cosecha() != null) {
            et_fin_cosecha.setText(Utilidades.voltearFechaVista(checklist.getFin_cosecha()));
        }
        if (checklist.getLote_campo() != null) {
            et_lote_campo.setText(checklist.getLote_campo());
        }
        if (checklist.getCantidad_sacos() != null) {
            et_cantidad_sacos.setText(checklist.getCantidad_sacos());
        }
        if (checklist.getColor_sacos() != null) {
            et_color_sacos.setText(checklist.getColor_sacos());
        }
        if (checklist.getCantidad_jumbo() != null) {
            et_cantidad_jumbo.setText(checklist.getCantidad_jumbo());
        }
        if (checklist.getColor_jumbo() != null) {
            et_color_jumbo.setText(checklist.getColor_jumbo());
        }
        if (checklist.getCantidad_bins() != null) {
            et_cantidad_bins.setText(checklist.getCantidad_bins());
        }
        if (checklist.getColor_bins() != null) {
            et_color_bins.setText(checklist.getColor_bins());
        }
        if (checklist.getKilos_estimados() != null) {
            et_kilos_estimados.setText(checklist.getKilos_estimados());
        }
        if (checklist.getFecha_cosecha() != null) {
            et_fecha_cosecha.setText(Utilidades.voltearFechaVista(checklist.getFecha_cosecha()));
        }
        if (checklist.getN_maquina() != null) {
            et_n_maquina.setText(checklist.getN_maquina());
        }
        if (checklist.getFecha_trilla() != null) {
            et_fecha_trilla.setText(Utilidades.voltearFechaVista(checklist.getFecha_trilla()));
        }
        if (checklist.getVb_supervisor_limpieza() != null) {
            et_vb_sup_limpieza.setText(checklist.getVb_supervisor_limpieza());
        }
        if (checklist.getHumedad() != null) {
            et_humedad.setText(checklist.getHumedad());
        }
        if (checklist.getTemperatura() != null) {
            et_temperatura.setText(checklist.getTemperatura());
        }
        if (checklist.getMalezas() != null) {
            et_malezas.setText(checklist.getMalezas());
        }
        if (checklist.getRestos_vegetales() != null) {
            et_resto_vegetales.setText(checklist.getRestos_vegetales());
        }
        if (checklist.getNombre_transportista() != null) {
            et_nombre_transportista.setText(checklist.getNombre_transportista());
        }
        if (checklist.getContacto_transportista() != null) {
            et_contacto_transportista.setText(checklist.getContacto_transportista());
        }
        if (checklist.getPatente_transportista() != null) {
            et_patente_transportista.setText(checklist.getPatente_transportista());
        }
        if (checklist.getNombre_supervisor() != null) {
            et_nombre_supervisor.setText(checklist.getNombre_supervisor());
        }
        if (checklist.getContacto_supervisor() != null) {
            et_contacto_supervisor.setText(checklist.getContacto_supervisor());
        }
        if (checklist.getObservaciones() != null) {
            et_observaciones.setText(checklist.getObservaciones());
        }


    }


    private void cargarDatosPrevios() {
        if (anexoCompleto == null) {
            Toasty.error(requireActivity(), "No se pudo obtener informacion del anexo", Toast.LENGTH_LONG, true).show();
            return;
        }

        tv_rs_agricultor.setText(anexoCompleto.getAgricultor().getNombre_agricultor());
        tv_rut_agricultor.setText(anexoCompleto.getAgricultor().getRut_agricultor());
        tv_variedad.setText(anexoCompleto.getVariedad().getDesc_variedad());
        tv_anexo.setText(anexoCompleto.getAnexoContrato().getAnexo_contrato());


        tv_fecha.setText((checklist == null) ? Utilidades.voltearFechaVista(Utilidades.fechaActualSinHora()) : "");
        tv_correlativo.setText((checklist == null) ? "" : "");

    }


    @Override
    public void onStart() {
        super.onStart();

        cargarDatosPrevios();
    }


    private void onSave() {


        try {
            Utilidades.validarFecha(Utilidades.voltearFechaBD(et_fin_lote.getText().toString()), "fin de lote");
            Utilidades.validarFecha(Utilidades.voltearFechaBD(et_fin_cosecha.getText().toString()), "fin de cosecha");
            Utilidades.validarFecha(Utilidades.voltearFechaBD(et_fecha_cosecha.getText().toString()), "fecha cosecha");
            Utilidades.validarFecha(Utilidades.voltearFechaBD(et_fecha_trilla.getText().toString()), "fecha trilla");
        } catch (Error e) {
            Toasty.error(requireActivity(), e.getMessage(), Toast.LENGTH_LONG, true).show();
            return;
        }


        String alfaNumerico = getResources().getString(R.string.alfanumericos_con_signos);

        et_observaciones.setText(Utilidades.sanitizarString(et_observaciones.getText().toString(), alfaNumerico));


        String apellido = anexoCompleto.getAgricultor().getNombre_agricultor().toLowerCase();
        String claveUnica = (checklist == null) ? UUID.randomUUID().toString() : checklist.getClave_unica();


        CheckListGuiaInterna chk = new CheckListGuiaInterna();

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<List<TempFirmas>> firmasF = executor.submit(()
                -> MainActivity.myAppDB.DaoFirmas()
                .getFirmasByDocum(Utilidades.TIPO_DOCUMENTO_CHECKLIST_GUIA_INTERNA));


        try {
            List<TempFirmas> firmas = firmasF.get();

            for (TempFirmas ff : firmas) {

                switch (ff.getLugar_firma()) {
                    case Utilidades.DIALOG_TAG_GUIA_INTERNA_TRANSPORTISTA:
                        chk.setFirma_transportista(ff.getPath());
                        break;
                    case Utilidades.DIALOG_TAG_GUIA_INTERNA_SUPERVISOR:
                        chk.setFirma_supervisor(ff.getPath());
                        break;
                }
            }


            chk.setId_ac_cl_guia_interna(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));
            chk.setApellido_checklist(apellido);
            chk.setEstado_sincronizacion(0);
            chk.setEstado_documento(1);
            chk.setClave_unica(claveUnica);

            chk.setFecha_trilla(Utilidades.voltearFechaBD(et_fecha_trilla.getText().toString()));
            chk.setFecha_cosecha(Utilidades.voltearFechaBD(et_fecha_cosecha.getText().toString()));
            chk.setFin_lote(Utilidades.voltearFechaBD(et_fin_lote.getText().toString()));
            chk.setFin_cosecha(Utilidades.voltearFechaBD(et_fin_cosecha.getText().toString()));

            chk.setOf(et_of.getText().toString());
            chk.setN_guia_despacho(et_n_guia_despacho.getText().toString());
            chk.setParcialidad_lote(et_parcialidad_lote.getText().toString());
            chk.setLote_campo(et_lote_campo.getText().toString());
            chk.setCantidad_sacos(et_cantidad_sacos.getText().toString());
            chk.setColor_sacos(et_color_sacos.getText().toString());
            chk.setCantidad_jumbo(et_cantidad_jumbo.getText().toString());
            chk.setColor_jumbo(et_color_jumbo.getText().toString());
            chk.setCantidad_bins(et_cantidad_bins.getText().toString());
            chk.setColor_bins(et_color_bins.getText().toString());
            chk.setKilos_estimados(et_kilos_estimados.getText().toString());
            chk.setN_maquina(et_n_maquina.getText().toString());
            chk.setVb_supervisor_limpieza(et_vb_sup_limpieza.getText().toString());
            chk.setHumedad(et_humedad.getText().toString());
            chk.setTemperatura(et_temperatura.getText().toString());
            chk.setMalezas(et_malezas.getText().toString());
            chk.setRestos_vegetales(et_resto_vegetales.getText().toString());
            chk.setNombre_transportista(et_nombre_transportista.getText().toString());
            chk.setContacto_transportista(et_contacto_transportista.getText().toString());
            chk.setPatente_transportista(et_patente_transportista.getText().toString());
            chk.setNombre_supervisor(et_nombre_supervisor.getText().toString());
            chk.setContacto_supervisor(et_contacto_supervisor.getText().toString());
            chk.setObservaciones(et_observaciones.getText().toString());

            if (checklist != null) {
                chk.setId_cl_guia_interna(checklist.getId_cl_guia_interna());
                chk.setId_usuario(checklist.getId_usuario());
                chk.setFecha_hora_tx(checklist.getFecha_hora_tx());
                chk.setFecha_hora_mod(Utilidades.fechaActualConHora());
                chk.setId_usuario_mod(usuario.getId_usuario());
                chk.setCorrelativo(checklist.getCorrelativo());
                chk.setFecha(checklist.getFecha());

                executor.submit(() -> MainActivity.myAppDB.DaoCLGuiaInterna().updateClGuiaInterna(chk)).get();
            } else {
                chk.setId_usuario(usuario.getId_usuario());
                chk.setFecha_hora_tx(Utilidades.fechaActualConHora());
                chk.setFecha(Utilidades.fechaActualSinHora());

                executor.submit(() -> MainActivity.myAppDB.DaoCLGuiaInterna().insertClGuiaInterna(chk)).get();
            }

            Toasty.success(requireActivity(), "Guardado con exito", Toast.LENGTH_LONG, true).show();
            cancelar();

        } catch (ExecutionException | InterruptedException e) {
            Toasty.error(requireActivity(), e.getMessage(), Toast.LENGTH_LONG, true).show();
        } finally {
            executor.shutdown();
        }
    }

    private void cancelar() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.submit(()
                -> MainActivity.myAppDB.DaoFirmas()
                .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_GUIA_INTERNA));
        executorService.shutdown();
        activity.onBackPressed();
    }

}
