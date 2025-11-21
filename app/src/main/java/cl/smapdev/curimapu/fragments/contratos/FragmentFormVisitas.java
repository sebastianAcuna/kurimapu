package cl.smapdev.curimapu.fragments.contratos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cl.smapdev.curimapu.BuildConfig;
import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.FotosListAdapter;
import cl.smapdev.curimapu.clases.adapters.SpinnerAdapter;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.relaciones.VisitasCompletas;
import cl.smapdev.curimapu.clases.tablas.AnexoContrato;
import cl.smapdev.curimapu.clases.tablas.CheckListRecepcionPlantinera;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Evaluaciones;
import cl.smapdev.curimapu.clases.tablas.Fotos;
import cl.smapdev.curimapu.clases.tablas.Visitas;
import cl.smapdev.curimapu.clases.temporales.TempVisitas;
import cl.smapdev.curimapu.clases.utilidades.CameraUtils;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.FragmentVisitas;
import cl.smapdev.curimapu.fragments.dialogos.DialogObservationTodo;
import es.dmoral.toasty.Toasty;

public class FragmentFormVisitas extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, View.OnFocusChangeListener {

    private Spinner sp_fenologico, sp_cosecha, sp_crecimiento, sp_fito, sp_general_cultivo, sp_humedad, sp_malezas;

    private Button btn_guardar;

    private MainActivity activity;
    private SharedPreferences prefs;

    private TempVisitas temp_visitas;
    private String currentPhotoPath;
    private String medidaRaices;

    private final ArrayList<String> fenologico = new ArrayList<>();
    private final ArrayList<String> cosecha = new ArrayList<>();
    private final ArrayList<String> crecimiento = new ArrayList<>();
    private final ArrayList<String> maleza = new ArrayList<>();

    private TextInputLayout obs_growth, obs_weed, obs_fito, obs_harvest, obs_overall, obs_humedad;
    private EditText et_obs, et_obs_growth, et_obs_weed, et_obs_fito, et_obs_harvest, et_obs_overall, et_obs_humedad, et_percent_humedad;

    /* IMAGENES */
    private RecyclerView rwAgronomo, rwCliente, rwRaices;
    private static final int COD_FOTO = 005;
    private File fileImagen;
    private EditText et_fecha_estimada;
    private EditText et_fecha_arranca;
    private FloatingActionButton material_private, material_public, foto_raices;
    private FotosListAdapter adapterAgronomo;
    private FotosListAdapter adapterCliente;
    private ProgressDialog progressBar;


    //    private String currentPhotoPath;
    private Uri currentPhotoUri;
    private ActivityResultLauncher<Uri> cameraLauncher;


    private final String[] forbiddenWords = new String[]{"á", "é", "í", "ó", "ú", "Á", "É", "Í", "Ó", "Ú"};
    private final String[] forbiddenReplacement = new String[]{"a", "e", "i", "o", "u", "A", "E", "I", "O", "U"};

    private static final String TAG = "FragmentFormVisitas";
    private static final String DIALOG_TAG_EVALUACION = "EVALUACION_RECOMENDACION";

    // Executor reutilizable para operaciones DB rápidas (evita crear/shutdown por click)
    private final ExecutorService singleDbExecutor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());


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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mostrarDialogoEspere();

        fenologico.addAll(Arrays.asList(getResources().getStringArray(R.array.fenologico)));
        cosecha.addAll(Arrays.asList(getResources().getStringArray(R.array.cosecha)));
        crecimiento.addAll(Arrays.asList(getResources().getStringArray(R.array.crecimiento)));
        maleza.addAll(Arrays.asList(getResources().getStringArray(R.array.maleza)));

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(),
                (Boolean success) -> {
                    if (success) {
                        procesarFotoTomada();
                    } else {
                        Toasty.info(activity, "Captura de foto cancelada", Toast.LENGTH_LONG, true).show();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (singleDbExecutor != null && !singleDbExecutor.isShutdown()) {
            singleDbExecutor.shutdown();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form_visitas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind(view);


        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            if (!Utilidades.checkPermission(requireContext())) {
                Utilidades.requestPermission(requireActivity());
            }
        }

        singleDbExecutor.execute(() -> {
            if (temp_visitas == null) {
                prefs = obtenerSharedPreferences();
                temp_visitas = new TempVisitas();
                if (prefs != null) {
                    temp_visitas.setId_anexo_temp_visita(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
                    temp_visitas.setId_temp_visita(0);

                    String claveUnica = UUID.randomUUID().toString();
                    temp_visitas.setClave_unica_visita(claveUnica);
                }
                MainActivity.myAppDB.myDao().setTempVisitas(temp_visitas);
                temp_visitas = MainActivity.myAppDB.myDao().getTempFichas();
            }


            handler.post(() -> {
                chargeAll();
                ocultarDialogEspere();
            });
        });

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.menu_visitas, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_visitas_recom) {
                    abrirEvaluacionRecomendacion();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        singleDbExecutor.execute(() -> {
            AnexoCompleto anexoCompleto = MainActivity.myAppDB.myDao().getAnexoCompletoById(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));

            if (anexoCompleto != null) {
                handler.post(() -> {
                    Utilidades.setToolbar(activity, view, getResources().getString(R.string.app_name), "Anexo " + anexoCompleto.getAnexoContrato().getAnexo_contrato());
                });
            }
        });
    }

    public SharedPreferences obtenerSharedPreferences() {

        MainActivity m = (activity == null) ? (MainActivity) requireActivity() : activity;

        if (prefs == null) {
            prefs = m.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
        }

        return prefs;
    }

    public void mostrarDialogoEspere() {

        if (progressBar == null) {
            progressBar = new ProgressDialog(activity);
            progressBar.setMessage(getResources().getString(R.string.espere));
            progressBar.setCancelable(false);
        }
        progressBar.show();
    }

    public void ocultarDialogEspere() {
        if (progressBar != null && progressBar.isShowing()) {
            progressBar.dismiss();
        }
    }


    private void abrirEvaluacionRecomendacion() {
        // Validaciones básicas
        if (!isAdded()) {
            Toasty.error(requireContext(), "Fragment no agregado, abortando abrirEvaluacionRecomendacion", Toast.LENGTH_LONG, true).show();
            Log.w(TAG, "Fragment no agregado, abortando abrirEvaluacionRecomendacion");
            return;
        }
        activity = (activity == null) ? (MainActivity) getActivity() : activity;
        prefs = obtenerSharedPreferences();


        final String idAnexo = prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, null);
        if (idAnexo == null || idAnexo.trim().isEmpty()) {
            Log.w(TAG, "Id anexo vacío, no se abre diálogo");
            Toasty.warning(requireContext(), "Sin anexo activo", Toast.LENGTH_SHORT, true).show();
            return;
        }

        // Evitar mostrar dos veces si ya está en pantalla
        Fragment existente = requireActivity().getSupportFragmentManager().findFragmentByTag(DIALOG_TAG_EVALUACION);
        if (existente != null && existente.isAdded()) {
            Log.i(TAG, "Diálogo ya visible, ignorando click");
            return;
        }

        mostrarDialogoEspere();

        singleDbExecutor.execute(() -> {
            VisitasCompletas visitasCompletas = null;
            AnexoContrato ac = null;
            TempVisitas tmp = null;
            try {
                tmp = MainActivity.myAppDB.myDao().getTempFichas();
                if (tmp != null && tmp.getAction_temp_visita() != 2) {
                    visitasCompletas = MainActivity.myAppDB.myDao().getUltimaVisitaByAnexo(tmp.getId_anexo_temp_visita());
                }
                ac = MainActivity.myAppDB.myDao().getAnexos(idAnexo);
            } catch (Exception e) {
                Log.e(TAG, "Error consultando DB", e);
            }

            TempVisitas finalTmp = tmp; // para lambda
            VisitasCompletas finalVisitasCompletas = visitasCompletas;
            AnexoContrato finalAc = ac;

            handler.post(() -> {
                ocultarDialogEspere();
                if (!isAdded()) {
                    Log.w(TAG, "Fragment desacoplado antes de mostrar diálogo");
                    return;
                }
                if (finalAc == null) {
                    Toasty.error(requireContext(), "No se pudo cargar datos", Toast.LENGTH_SHORT, true).show();
                    return;
                }
                // Actualizar campo global para evitar sombra
                if (finalTmp != null) {
                    temp_visitas = finalTmp;
                }
                try {
                    FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                    Fragment prev = requireActivity().getSupportFragmentManager().findFragmentByTag(DIALOG_TAG_EVALUACION);
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    if (getParentFragmentManager().isStateSaved()) {
                        Log.w(TAG, "Estado de fragment manager guardado, posponiendo diálogo");
                        return;
                    }
                    DialogObservationTodo dialogo = DialogObservationTodo.newInstance(finalAc, temp_visitas, finalVisitasCompletas, this::cargarTemp);
                    dialogo.show(ft, DIALOG_TAG_EVALUACION);
                } catch (IllegalStateException ise) {
                    Log.e(TAG, "Error mostrando diálogo", ise);
                }
            });
        });
    }


    private void cargarTemp(TempVisitas tempVisitas) {
        temp_visitas = tempVisitas;
    }

    private void chargeAll() {
        ocultarDialogEspere();

        if (temp_visitas != null && temp_visitas.getAction_temp_visita() != 2 && temp_visitas.getEvaluacion() <= 0.0) {
            mostrarDialogoEspere();
            singleDbExecutor.execute(() -> {
                VisitasCompletas visitasCompletas = MainActivity.myAppDB.myDao().getUltimaVisitaByAnexo(temp_visitas.getId_anexo_temp_visita());
                AnexoContrato anexoContrato;
                if (visitasCompletas != null) {
                    anexoContrato = visitasCompletas.getAnexoCompleto().getAnexoContrato();
                } else {
                    anexoContrato = MainActivity.myAppDB.myDao().getAnexos(temp_visitas.getId_anexo_temp_visita());
                }

                handler.post(() -> {
                    ocultarDialogEspere();
                    if (visitasCompletas == null) {
                        Toasty.info(activity, "No se han encontrado visitas anteriores para este anexo.", Toast.LENGTH_LONG, true).show();
                        return;
                    }

                    if (!isAdded()) {
                        Log.w(TAG, "Fragment desacoplado antes de mostrar diálogo");
                        return;
                    }

                    FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                    Fragment prev = requireActivity().getSupportFragmentManager().findFragmentByTag(DIALOG_TAG_EVALUACION);
                    if (prev != null) {
                        ft.remove(prev);
                    }

                    DialogObservationTodo dialogo = DialogObservationTodo.newInstance(
                            anexoContrato,
                            temp_visitas,
                            visitasCompletas,
                            (TempVisitas tp) -> {
                                if (tp != null) {
                                    temp_visitas = tp;
                                }
                            }
                    );
                    dialogo.show(ft, DIALOG_TAG_EVALUACION);
                });
            });
        }

        cargarSpinners();

        foto_raices.setOnClickListener(v -> {
            preguntarFotoRaices();
        });

        material_private.setOnClickListener(v -> {

            int cantidaAgr = MainActivity.myAppDB.myDao().getCantAgroByFieldViewAndFicha(0, 2, temp_visitas.getId_anexo_temp_visita(), temp_visitas.getId_temp_visita());
            if (temp_visitas.getAction_temp_visita() == 2) {
                Utilidades.avisoListo(activity, getResources().getString(R.string.title_dialog_agron), getResources().getString(R.string.visitas_terminadas), getResources().getString(R.string.entiendo));
            } else {
                Utilidades.hideKeyboard(activity);
                abrirCamara(2, "0");
            }
        });
        material_public.setOnClickListener(v -> {
            if (sp_fenologico.getSelectedItemPosition() > 0) {
                if (temp_visitas.getAction_temp_visita() == 2) {
                    Utilidades.avisoListo(activity, getResources().getString(R.string.title_dialog_agron), getResources().getString(R.string.visitas_terminadas), getResources().getString(R.string.entiendo));
                } else {
                    Utilidades.hideKeyboard(activity);
                    abrirCamara(0, "0");
                }
            } else {
                Utilidades.avisoListo(activity, getResources().getString(R.string.falta_algo), getResources().getString(R.string.pheno_first), getResources().getString(R.string.entiendo));
            }
        });


    }

    /*
     * GENERA LOS ONITEMSELECTEDLISTENER DE LOS SPINNERS
     * */
    private void accionSpinners() {
        if (temp_visitas != null) {

            sp_fenologico.setSelection(fenologico.indexOf(temp_visitas.getPhenological_state_temp_visita()));
            sp_cosecha.setSelection(cosecha.indexOf(temp_visitas.getHarvest_temp_visita()));
            sp_crecimiento.setSelection(crecimiento.indexOf(temp_visitas.getGrowth_status_temp_visita()));
            sp_fito.setSelection(crecimiento.indexOf(temp_visitas.getPhytosanitary_state_temp_visita()));
            sp_general_cultivo.setSelection(crecimiento.indexOf(temp_visitas.getOverall_status_temp_visita()));
            sp_humedad.setSelection(crecimiento.indexOf(temp_visitas.getHumidity_floor_temp_visita()));
            sp_malezas.setSelection(maleza.indexOf(temp_visitas.getWeed_state_temp_visita()));

            et_obs.setText(temp_visitas.getObservation_temp_visita());

            et_obs_harvest.setText(temp_visitas.getObs_cosecha());
            et_obs_overall.setText(temp_visitas.getObs_overall());
            et_obs_humedad.setText(temp_visitas.getObs_humedad());
            et_obs_weed.setText(temp_visitas.getObs_maleza());
            et_obs_fito.setText(temp_visitas.getObs_fito());
            et_obs_growth.setText(temp_visitas.getObs_creci());
            et_percent_humedad.setText(String.valueOf(temp_visitas.getPercent_humedad()));


            et_fecha_arranca.setText(Utilidades.voltearFechaVista(temp_visitas.getFecha_estimada_arranca()));
            et_fecha_estimada.setText(Utilidades.voltearFechaVista(temp_visitas.getFecha_estimada_cosecha()));


            et_percent_humedad.setSelectAllOnFocus(true);
            et_obs_harvest.setSelectAllOnFocus(true);
            et_obs_growth.setSelectAllOnFocus(true);
            et_obs_fito.setSelectAllOnFocus(true);
            et_obs_weed.setSelectAllOnFocus(true);
            et_obs_humedad.setSelectAllOnFocus(true);
            et_obs_overall.setSelectAllOnFocus(true);
            et_obs_fito.setSelectAllOnFocus(true);

            agregarImagenToAgronomos();
            agregarImagenToClientes();


            if (temp_visitas.getAction_temp_visita() == 2) {
                sp_fenologico.setEnabled(false);
                sp_cosecha.setEnabled(false);
                sp_crecimiento.setEnabled(false);
                sp_fito.setEnabled(false);
                sp_general_cultivo.setEnabled(false);
                sp_humedad.setEnabled(false);
                sp_malezas.setEnabled(false);
                et_obs.setEnabled(false);
                btn_guardar.setEnabled(false);

                et_fecha_estimada.setEnabled(false);
                et_fecha_arranca.setEnabled(false);

                et_obs_fito.setEnabled(false);
                et_obs_overall.setEnabled(false);
                et_obs_humedad.setEnabled(false);
                et_obs_weed.setEnabled(false);
                et_obs_fito.setEnabled(false);
                et_obs_growth.setEnabled(false);
                et_obs_harvest.setEnabled(false);

                et_percent_humedad.setEnabled(false);

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activity = (activity == null) ? (MainActivity) getActivity() : activity;
        singleDbExecutor.execute(() -> {
            temp_visitas = MainActivity.myAppDB.myDao().getTempFichas();

            handler.post(() -> {
                agregarImagenToAgronomos();
                agregarImagenToClientes();
            });
        });


    }

    private void cargarSpinners() {

        sp_fenologico.setAdapter(new SpinnerAdapter(activity, R.layout.spinner_template_view, fenologico));
        sp_cosecha.setAdapter(new SpinnerAdapter(activity, R.layout.spinner_template_view, cosecha));
        sp_crecimiento.setAdapter(new SpinnerAdapter(activity, R.layout.spinner_template_view, crecimiento));
        sp_fito.setAdapter(new SpinnerAdapter(activity, R.layout.spinner_template_view, crecimiento));
        sp_general_cultivo.setAdapter(new SpinnerAdapter(activity, R.layout.spinner_template_view, crecimiento));
        sp_humedad.setAdapter(new SpinnerAdapter(activity, R.layout.spinner_template_view, crecimiento));
        sp_malezas.setAdapter(new SpinnerAdapter(activity, R.layout.spinner_template_view, maleza));

        accionSpinners();

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            activity = (activity == null) ? (MainActivity) getActivity() : activity;
            singleDbExecutor.execute(() -> {
                temp_visitas = MainActivity.myAppDB.myDao().getTempFichas();

                handler.post(() -> {
                    accionSpinners();
                    agregarImagenToAgronomos();
                    agregarImagenToClientes();
                });
            });

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_guardar:

                if (activity != null) {
                    showAlertAskForSave123("¿Esta seguro que desea guardar?", "Revise el libro de campo antes de hacerlo, si esta seguro, presione aceptar.");
                }
                break;

            case R.id.btn_volver:
                preguntarSiQuiereVolver("ATENCION", "SI VUELVES NO GUARDARA LOS CAMBIOS, ESTAS SEGURO QUE DESEAS VOLVER ?");

                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void bind(View view) {
        sp_fenologico = view.findViewById(R.id.sp_feno);
        sp_cosecha = view.findViewById(R.id.sp_cosecha);
        sp_crecimiento = view.findViewById(R.id.sp_crecimiento);
        sp_fito = view.findViewById(R.id.sp_fito);
        sp_general_cultivo = view.findViewById(R.id.sp_general_cultivo);
        sp_humedad = view.findViewById(R.id.sp_humedad);
        sp_malezas = view.findViewById(R.id.sp_malezas);
        btn_guardar = view.findViewById(R.id.btn_guardar);

        et_obs = view.findViewById(R.id.et_obs);
        et_obs_fito = view.findViewById(R.id.et_obs_fito);
        et_obs_growth = view.findViewById(R.id.et_obs_growth);
        et_obs_harvest = view.findViewById(R.id.et_obs_harvest);
        et_obs_overall = view.findViewById(R.id.et_obs_overall);
        et_obs_weed = view.findViewById(R.id.et_obs_weed);
        et_obs_humedad = view.findViewById(R.id.et_obs_humedad);
        et_percent_humedad = view.findViewById(R.id.et_percent_humedad);

        obs_growth = view.findViewById(R.id.obs_growth);
        obs_weed = view.findViewById(R.id.obs_weed);
        obs_fito = view.findViewById(R.id.obs_fito);
        obs_harvest = view.findViewById(R.id.obs_harvest);
        obs_overall = view.findViewById(R.id.obs_overall);
        obs_humedad = view.findViewById(R.id.obs_humedad);


        et_fecha_estimada = view.findViewById(R.id.et_fecha_estimada);
        et_fecha_arranca = view.findViewById(R.id.et_fecha_arranca);

        Button btn_volver = view.findViewById(R.id.btn_volver);

        sp_fenologico.setOnItemSelectedListener(this);
        sp_malezas.setOnItemSelectedListener(this);
        sp_humedad.setOnItemSelectedListener(this);
        sp_general_cultivo.setOnItemSelectedListener(this);
        sp_fito.setOnItemSelectedListener(this);
        sp_crecimiento.setOnItemSelectedListener(this);
        sp_cosecha.setOnItemSelectedListener(this);

        et_obs.setOnFocusChangeListener(this);
        et_obs_growth.setOnFocusChangeListener(this);
        et_obs_weed.setOnFocusChangeListener(this);
        et_obs_overall.setOnFocusChangeListener(this);
        et_obs_harvest.setOnFocusChangeListener(this);
        et_obs_fito.setOnFocusChangeListener(this);
        et_obs_humedad.setOnFocusChangeListener(this);
        et_percent_humedad.setOnFocusChangeListener(this);

        et_fecha_estimada.setOnFocusChangeListener(this);
        et_fecha_arranca.setOnFocusChangeListener(this);


        btn_volver.setOnClickListener(this);
        btn_guardar.setOnClickListener(this);


        rwAgronomo = view.findViewById(R.id.fotos_agronomos);
        rwCliente = view.findViewById(R.id.fotos_clientes);
        rwRaices = view.findViewById(R.id.fotos_raices);

        material_private = view.findViewById(R.id.material_private);
        material_public = view.findViewById(R.id.material_public);
        foto_raices = view.findViewById(R.id.foto_raices);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i > 0) {
            switch (adapterView.getId()) {
                case R.id.sp_feno:
                    prefs = obtenerSharedPreferences();
                    temp_visitas.setPhenological_state_temp_visita(fenologico.get(i));
                    temp_visitas.setEtapa_temp_visitas(Utilidades.getPhenoState(sp_fenologico.getSelectedItemPosition()));
                    prefs.edit().putInt(Utilidades.SHARED_ETAPA_SELECTED, Utilidades.getPhenoState(sp_fenologico.getSelectedItemPosition())).apply();

                    if (fenologico.get(i).equals("TRANSPLANTE")) {
                        singleDbExecutor.execute(() -> {
                            Visitas vis = MainActivity.myAppDB.myDao().getVisitasByACAndEstado(temp_visitas.getId_anexo_temp_visita(), "TRANSPLANTE");
                            List<CheckListRecepcionPlantinera> chk = MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().getAllClRPByAc(Integer.parseInt(temp_visitas.getId_anexo_temp_visita()));
                            if (chk.isEmpty() && vis == null) {
                                handler.post(() -> {
                                    showAlertAviso("Atención", "No se encontro Checklist de recepcion de plantinera");
                                });
                            }
                        });
                    }


                    break;
                case R.id.sp_cosecha:
                    obs_harvest.setVisibility(View.VISIBLE);
                    temp_visitas.setHarvest_temp_visita(cosecha.get(i));
                    break;
                case R.id.sp_crecimiento:
                    obs_growth.setVisibility(View.VISIBLE);
                    temp_visitas.setGrowth_status_temp_visita(crecimiento.get(i));
                    break;
                case R.id.sp_fito:
                    obs_fito.setVisibility(View.VISIBLE);
                    temp_visitas.setPhytosanitary_state_temp_visita(crecimiento.get(i));
                    break;
                case R.id.sp_general_cultivo:
                    obs_overall.setVisibility(View.VISIBLE);
                    temp_visitas.setOverall_status_temp_visita(crecimiento.get(i));
                    break;
                case R.id.sp_humedad:
                    obs_humedad.setVisibility(View.VISIBLE);
                    temp_visitas.setHumidity_floor_temp_visita(crecimiento.get(i));
                    break;
                case R.id.sp_malezas:
                    obs_weed.setVisibility(View.VISIBLE);
                    temp_visitas.setWeed_state_temp_visita(maleza.get(i));
                    break;
            }
            singleDbExecutor.execute(() -> {
                MainActivity.myAppDB.myDao().updateTempVisitas(temp_visitas);
            });

        } else {
            switch (adapterView.getId()) {
                case R.id.sp_feno:
                    temp_visitas.setPhenological_state_temp_visita("");
                    temp_visitas.setEtapa_temp_visitas(Utilidades.getPhenoState(sp_fenologico.getSelectedItemPosition()));
                    break;
                case R.id.sp_cosecha:
                    obs_harvest.setVisibility(View.INVISIBLE);
                    temp_visitas.setHarvest_temp_visita("");
                    break;
                case R.id.sp_crecimiento:
                    obs_growth.setVisibility(View.INVISIBLE);
                    temp_visitas.setGrowth_status_temp_visita("");
                    break;
                case R.id.sp_fito:
                    obs_fito.setVisibility(View.INVISIBLE);
                    temp_visitas.setPhytosanitary_state_temp_visita("");
                    break;
                case R.id.sp_general_cultivo:
                    obs_overall.setVisibility(View.INVISIBLE);
                    temp_visitas.setOverall_status_temp_visita("");
                    break;
                case R.id.sp_humedad:
                    obs_humedad.setVisibility(View.INVISIBLE);
                    temp_visitas.setHumidity_floor_temp_visita("");
                    break;
                case R.id.sp_malezas:
                    obs_weed.setVisibility(View.INVISIBLE);
                    temp_visitas.setWeed_state_temp_visita("");
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    String quitarTildes(String texto) {
        String nuevoTexto = texto;
        if (!TextUtils.isEmpty(texto)) {
            for (int i = 0; i < forbiddenWords.length; i++) {
                nuevoTexto = texto.replace(forbiddenWords[i], forbiddenReplacement[i]);
            }
        }
        return nuevoTexto;
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        String text = "";
        if (b) {
            switch (view.getId()) {
                case R.id.et_fecha_estimada:
                    Utilidades.levantarFecha(et_fecha_estimada, view.getContext());
                    break;

                case R.id.et_fecha_arranca:
                    Utilidades.levantarFecha(et_fecha_arranca, view.getContext());
                    break;
            }
        }
        if (!b) {
            switch (view.getId()) {
                case R.id.et_obs:
                    temp_visitas.setObservation_temp_visita(quitarTildes(et_obs.getText().toString().toUpperCase()));
                    break;
                case R.id.et_obs_growth:
                    text = et_obs_growth.getText().toString().toUpperCase();

                    for (int i = 0; i < forbiddenWords.length; i++) {
                        text = text.replace(forbiddenWords[i], forbiddenReplacement[i]);
                    }
                    temp_visitas.setObs_creci(text);
                    break;
                case R.id.et_obs_weed:
                    text = et_obs_weed.getText().toString().toUpperCase();

                    for (int i = 0; i < forbiddenWords.length; i++) {
                        text = text.replace(forbiddenWords[i], forbiddenReplacement[i]);
                    }
                    temp_visitas.setObs_maleza(text);
                    break;
                case R.id.et_obs_fito:
                    text = et_obs_fito.getText().toString().toUpperCase();

                    for (int i = 0; i < forbiddenWords.length; i++) {
                        text = text.replace(forbiddenWords[i], forbiddenReplacement[i]);
                    }
                    temp_visitas.setObs_fito(text);
                    break;
                case R.id.et_obs_harvest:
                    text = et_obs_harvest.getText().toString().toUpperCase();

                    for (int i = 0; i < forbiddenWords.length; i++) {
                        text = text.replace(forbiddenWords[i], forbiddenReplacement[i]);
                    }
                    temp_visitas.setObs_cosecha(text);
                    break;
                case R.id.et_obs_overall:
                    text = et_obs_overall.getText().toString().toUpperCase();

                    for (int i = 0; i < forbiddenWords.length; i++) {
                        text = text.replace(forbiddenWords[i], forbiddenReplacement[i]);
                    }
                    temp_visitas.setObs_overall(text);
                    break;
                case R.id.et_obs_humedad:
                    text = et_obs_humedad.getText().toString().toUpperCase();

                    for (int i = 0; i < forbiddenWords.length; i++) {
                        text = text.replace(forbiddenWords[i], forbiddenReplacement[i]);
                    }
                    temp_visitas.setObs_humedad(text);
                    break;
                case R.id.et_percent_humedad:
                    try {
                        temp_visitas.setPercent_humedad(Double.parseDouble(et_percent_humedad.getText().toString()));
                    } catch (Exception e) {
                        temp_visitas.setPercent_humedad(0);
                        Toasty.warning(activity, "No se pudo guardar el porcentaje, formato incorrecto.", Toast.LENGTH_SHORT, true).show();
                    }
                    break;

                case R.id.et_fecha_estimada:
                    temp_visitas.setFecha_estimada_cosecha(Utilidades.voltearFechaBD(et_fecha_estimada.getText().toString()));
                    break;

                case R.id.et_fecha_arranca:
                    temp_visitas.setFecha_estimada_arranca(Utilidades.voltearFechaBD(et_fecha_arranca.getText().toString()));
                    break;
            }
            singleDbExecutor.execute(() -> {
                MainActivity.myAppDB.myDao().updateTempVisitas(temp_visitas);
            });

        }
    }


    private void abrirCamara(int vista, String medida) {

        File photoFile = null;
        try {
            photoFile = Utilidades.createImageFile(requireActivity(), "visitas");
            currentPhotoPath = photoFile.getAbsolutePath();
        } catch (IOException e) {
            Toasty.error(requireActivity(), "No se pudo crear la imagen " + e.getMessage(), Toast.LENGTH_LONG, true).show();
        }

        if (photoFile == null) {
            Toasty.error(requireActivity(), "No se pudo crear la imagen (nula)", Toast.LENGTH_LONG, true).show();
            return;
        }

        prefs.edit().remove(Utilidades.VISTA_FOTOS).putInt(Utilidades.VISTA_FOTOS, vista).apply();
        medidaRaices = medida;

        currentPhotoUri = FileProvider.getUriForFile(requireActivity(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
        cameraLauncher.launch(currentPhotoUri);
    }


    public void procesarFotoTomada() {
        try {
            Bitmap originalBtm = BitmapFactory.decodeFile(currentPhotoPath);
            if (originalBtm == null) {
                Toasty.error(requireActivity(), "No se pudo decodificar la imagen.", Toast.LENGTH_LONG, true).show();
                return;
            }

            Bitmap nuevaFoto = CameraUtils.escribirFechaImg(originalBtm, activity);

            File file = new File(currentPhotoPath);
            FileOutputStream fos = new FileOutputStream(file);

            nuevaFoto.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            guardarBD();

        } catch (Exception e) {
            Toasty.error(requireActivity(), "Error al procesar la foto: " + e.getMessage(), Toast.LENGTH_LONG, true).show();
        }
    }


    private void guardarBD() {

        singleDbExecutor.execute(() -> {
            Config config = MainActivity.myAppDB.myDao().getConfig();
            prefs = obtenerSharedPreferences();
            Fotos fotos = new Fotos();
            fotos.setFecha(Utilidades.fechaActualConHora());
            Log.e("RUTA 2", currentPhotoPath);

            int vista = prefs.getInt(Utilidades.VISTA_FOTOS, 0);

            File file = new File(currentPhotoPath);
            fotos.setFieldbook((prefs.getInt(Utilidades.VISTA_FOTOS, 0) == 2) ? 0 : Utilidades.getPhenoState(sp_fenologico.getSelectedItemPosition()));
            fotos.setHora(Utilidades.hora());
            fotos.setNombre_foto(file.getName());
            fotos.setFavorita(false);
            fotos.setMedida_raices(!medidaRaices.isEmpty() ? medidaRaices : "0");
            fotos.setAcepto_regla_raices((vista == 3) ? 1 : 0);
            fotos.setPlano(0);
            if (temp_visitas != null) {
                fotos.setId_ficha_fotos(temp_visitas.getId_anexo_temp_visita());
            }
            fotos.setVista(vista);
            fotos.setRuta(currentPhotoPath);


            fotos.setId_visita_foto(prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0));

            if (config != null) {
                fotos.setId_dispo_foto(config.getId());
            }
            MainActivity.myAppDB.myDao().insertFotos(fotos);

            handler.post(() -> {
                agregarImagenToAgronomos();
                agregarImagenToClientes();
                agregarImagenToRaices();
            });
        });
    }


    private void showSavingDialog() {
        if (progressBar == null) {
            progressBar = new ProgressDialog(activity);
            progressBar.setCancelable(false);
            progressBar.setMessage("Guardando visita...");
        }
        progressBar.show();
    }

    public void showAlertAviso(String title, String message) {
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_empty, null);

        final AlertDialog builder = new AlertDialog.Builder(activity)
                .setView(viewInfalted)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton("OK", (dialog, which) -> {
                })
                .create();

        builder.setOnShowListener(dialog -> {
            Button c = builder.getButton(AlertDialog.BUTTON_NEGATIVE);
            c.setOnClickListener(v -> builder.dismiss());
        });
        builder.setCancelable(false);
        builder.show();
    }


    private void setOnSave() {

        if (temp_visitas == null) {
            Utilidades.avisoListo(activity, "Falta algo", "Algo salio mal, por favor revise el fieldbook", "entiendo");
            return;
        }

        int positionFenologico = sp_fenologico.getSelectedItemPosition();
        int positionMaleza = sp_malezas.getSelectedItemPosition();
        int positionFito = sp_fito.getSelectedItemPosition();
        int positionCosecha = sp_cosecha.getSelectedItemPosition();
        int positionGeneral = sp_general_cultivo.getSelectedItemPosition();
        int positionHumedad = sp_humedad.getSelectedItemPosition();


        String obsMaleza = et_obs_weed.getText().toString().toUpperCase();
        String obsFito = et_obs_fito.getText().toString().toUpperCase();
        String obsCosecha = et_obs_harvest.getText().toString().toUpperCase();
        String obsOverall = et_obs_overall.getText().toString().toUpperCase();
        String obsCreci = et_percent_humedad.getText().toString().toUpperCase();
        String obsHumedad = et_percent_humedad.getText().toString().toUpperCase();
        String etobs = et_obs.getText().toString().toUpperCase();
        String porc_humedad = et_percent_humedad.getText().toString();


        if (positionFenologico <= 0 || positionMaleza <= 0 || positionFito <= 0 || positionCosecha <= 0 ||
                positionGeneral <= 0 || positionHumedad <= 0) {
            Utilidades.avisoListo(activity, "Hey", "no puede dejar elementos en '--seleccione--'.", "entiendo");
            return;
        }


        boolean entra = (positionMaleza == 3 && TextUtils.isEmpty(obsMaleza));
        if (!entra) {
            entra = (positionFito == 4 && TextUtils.isEmpty(obsFito));
        }
        if (!entra) {
            entra = ((positionCosecha == 1) && TextUtils.isEmpty(obsCosecha));
        }
        if (!entra) {
            entra = (positionGeneral == 4 && TextUtils.isEmpty(obsOverall));
        }

        if (entra) {
            Utilidades.avisoListo(activity, "Faltan cosas", "Debe completar todas las observaciones en donde su estado sea malo/alto/rechazado", "entiendo");
            return;
        }

        if (TextUtils.isEmpty(porc_humedad) || (Double.parseDouble(porc_humedad) <= 0.0 || Double.parseDouble(porc_humedad) > 200.0)) {
            Utilidades.avisoListo(activity, "Hey", "Debes ingresar un potencial de rendimiento sobre 0 y bajo 200 ", "entiendo");
            return;
        }

        for (int i = 0; i < forbiddenWords.length; i++) {
            obsCosecha = obsCosecha.replace(forbiddenWords[i], forbiddenReplacement[i]);
            obsCreci = obsCreci.replace(forbiddenWords[i], forbiddenReplacement[i]);
            obsFito = obsFito.replace(forbiddenWords[i], forbiddenReplacement[i]);
            obsHumedad = obsHumedad.replace(forbiddenWords[i], forbiddenReplacement[i]);
            obsMaleza = obsMaleza.replace(forbiddenWords[i], forbiddenReplacement[i]);
            obsOverall = obsOverall.replace(forbiddenWords[i], forbiddenReplacement[i]);
            etobs = etobs.replace(forbiddenWords[i], forbiddenReplacement[i]);
        }

        String finalObsOverall = obsOverall;
        String finalObsMaleza = obsMaleza;
        String finalObsHumedad = obsHumedad;
        String finalObsFito = obsFito;
        String finalObsCreci = obsCreci;
        String finalObsCosecha = obsCosecha;
        String finalEtobs = etobs;
        showSavingDialog();
        singleDbExecutor.execute(() -> {
            try {
                VisitasCompletas cc = MainActivity.myAppDB.myDao().getUltimaVisitaByAnexo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
                List<Evaluaciones> evs = MainActivity.myAppDB.DaoEvaluaciones().getEvaluacionesByACObliga(Integer.parseInt(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, "")));

                if (evs.isEmpty()) {
                    handler.post(() -> {
                        ocultarDialogEspere();
                        Utilidades.avisoListo(activity, "Falta algo", "Antes de guardar debes agregar una recomendacion para esta visita. ( estrella de arriba a la derecha )", "entiendo");
                    });
                    return;
                }

                if ((temp_visitas.getEvaluacion() <= 0.0 || temp_visitas.getComentario_evaluacion().isEmpty()) && cc != null && cc.getVisitas() != null) {
                    handler.post(() -> {
                        ocultarDialogEspere();
                        Utilidades.avisoListo(activity, "Falta algo", "Antes de guardar debes realizar la evaluacion de la visita anterior. ( estrella de arriba a la derecha )", "entiendo");
                    });
                    return;
                }

                int fotosClientes = MainActivity.myAppDB.myDao().getCantFotos(temp_visitas.getId_anexo_temp_visita(), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0), 0);
                int fotosAgricultores = MainActivity.myAppDB.myDao().getCantFotos(temp_visitas.getId_anexo_temp_visita(), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0), 2);


                if (fotosClientes <= 0 || fotosAgricultores <= 0) {
                    handler.post(() -> {
                        ocultarDialogEspere();
                        Utilidades.avisoListo(activity, "Falta algo", "Debes tomar al menos una foto cliente y agricultor", "entiendo");
                    });
                    return;
                }

                int favsCliente = MainActivity.myAppDB.myDao().getCantFavoritasByFieldbookAndFicha(temp_visitas.getId_anexo_temp_visita(), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0), 0);
                int favsAgricultores = MainActivity.myAppDB.myDao().getCantFavoritasByFieldbookAndFicha(temp_visitas.getId_anexo_temp_visita(), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0), 2);

                if (favsCliente <= 0 || favsAgricultores <= 0) {
                    handler.post(() -> {
                        ocultarDialogEspere();
                        Utilidades.avisoListo(activity, "Falta algo", "Debes seleccionar como favorita al menos una foto cliente y agricultor (manten presionada la foto para marcar)", "entiendo");
                    });

                    return;
                }

                Visitas visitas = new Visitas();
                visitas.setGrowth_status_visita(temp_visitas.getGrowth_status_temp_visita());
                visitas.setHarvest_visita(temp_visitas.getHarvest_temp_visita());
                visitas.setHumidity_floor_visita(temp_visitas.getHumidity_floor_temp_visita());
                visitas.setId_anexo_visita(temp_visitas.getId_anexo_temp_visita());
                visitas.setOverall_status_visita(temp_visitas.getOverall_status_temp_visita());
                visitas.setPhenological_state_visita(temp_visitas.getPhenological_state_temp_visita());
                visitas.setPhytosanitary_state_visita(temp_visitas.getPhytosanitary_state_temp_visita());
                visitas.setWeed_state_visita(temp_visitas.getWeed_state_temp_visita());
                visitas.setEstado_server_visitas(0);
                visitas.setEstado_visita(2);
                visitas.setEtapa_visitas(temp_visitas.getEtapa_temp_visitas());
                visitas.setEvaluacion(temp_visitas.getEvaluacion());
                visitas.setComentario_evaluacion(temp_visitas.getComentario_evaluacion());
                visitas.setId_evaluacion(temp_visitas.getId_evaluacion());
                visitas.setFecha_estimada_arranca(temp_visitas.getFecha_estimada_arranca());
                visitas.setFecha_estimada_cosecha(temp_visitas.getFecha_estimada_cosecha());

                double percent = 0.0;
                try {
                    percent = (temp_visitas.getPercent_humedad() <= 0.0) ? Double.parseDouble(et_percent_humedad.getText().toString()) : temp_visitas.getPercent_humedad();
                } catch (Exception e) {
                    percent = 0.0;
                }

                visitas.setPercent_humedad(percent);
                visitas.setObservation_visita(finalEtobs);
                visitas.setObs_cosecha(finalObsCosecha);
                visitas.setObs_creci(finalObsCreci);
                visitas.setObs_fito(finalObsFito);
                visitas.setObs_humedad(finalObsHumedad);
                visitas.setObs_maleza(finalObsMaleza);
                visitas.setObs_overall(finalObsOverall);

                AnexoContrato an = MainActivity.myAppDB.myDao().getAnexos(temp_visitas.getId_anexo_temp_visita());
                visitas.setTemporada(an.getTemporada_anexo());

                String fecha = Utilidades.fechaActualConHora();
                String[] fechaHora = fecha.split(" ");

                visitas.setHora_visita(fechaHora[1]);
                visitas.setFecha_visita(fechaHora[0]);

                visitas.setClave_unica_visita(temp_visitas.getClave_unica_visita());

                long idVisita = 0;

                if (prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0) > 0) {

                    idVisita = prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0);
                    visitas.setId_visita((int) idVisita);
                    visitas.setId_visita_local((int) idVisita);
                    MainActivity.myAppDB.myDao().updateVisita(visitas);

                } else {
                    idVisita = MainActivity.myAppDB.myDao().setVisita(visitas);
                }

                MainActivity.myAppDB.DaoEvaluaciones().updateEvaluacionesObligadas(Integer.parseInt(an.getId_anexo_contrato()));
                MainActivity.myAppDB.myDao().updateFotosWithVisita((int) idVisita, temp_visitas.getId_anexo_temp_visita());
                MainActivity.myAppDB.myDao().updateDetallesToVisits((int) idVisita);

                Visitas visitas1 = MainActivity.myAppDB.myDao().getVisitas((int) idVisita);
                visitas1.setId_visita_local((int) idVisita);
                MainActivity.myAppDB.myDao().updateVisita(visitas1);


                handler.post(() -> {
                    ocultarDialogEspere();
                    showAlertForSave("Genial", "Se guardo todo como corresponde");
                });


            } catch (Exception e) {
                Log.w("ERROR", e.getLocalizedMessage());
            }
        });

    }

    private void agregarImagenToAgronomos() {

        if (temp_visitas != null && rwAgronomo != null) {

            LinearLayoutManager lManager = null;
            if (activity != null) {
                lManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
            }

            rwAgronomo.setHasFixedSize(true);
            rwAgronomo.setLayoutManager(lManager);


            singleDbExecutor.execute(() -> {
                int visitaServidor = 0;
                int idDispo = 0;
                Config config = MainActivity.myAppDB.myDao().getConfig();
                if (config != null) {
                    idDispo = config.getId();
                }

                Visitas visitas = MainActivity.myAppDB.myDao().getVisitas(temp_visitas.getId_temp_visita());
                if (visitas != null) {
                    visitaServidor = (visitas.getEstado_server_visitas() == 1) ? prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0) : 0;
                }
                List<Fotos> myImageList = MainActivity.myAppDB.myDao().getFotosByFieldAndViewVisitas(2, temp_visitas.getId_anexo_temp_visita(), temp_visitas.getId_visita_local(), visitaServidor, idDispo);


                handler.post(() -> {

                    adapterAgronomo = new FotosListAdapter(myImageList, activity, this::showAlertForUpdate, fotos -> {

                        if (temp_visitas.getAction_temp_visita() == 2) {
                            Utilidades.avisoListo(activity, getResources().getString(R.string.title_dialog_agron), "Visita en estado terminado, no puedes cambiar el estado de las fotos.", "entiendo");
                        } else {
                            if (!fotos.isFavorita()) {
                                int favoritas = MainActivity.myAppDB.myDao().getCantFavoritasByFieldbookFichaAndVista(0, temp_visitas.getId_anexo_temp_visita(), 2, temp_visitas.getId_temp_visita());

                                if (favoritas < 3) {
                                    cambiarFavorita(fotos);
                                } else {
                                    Utilidades.avisoListo(activity, getResources().getString(R.string.title_dialog_fav), getResources().getString(R.string.message_dialog_fav), getResources().getString(R.string.message_dialog_btn_ok));
                                }
                            } else {
                                cambiarFavorita(fotos);
                            }
                        }


                    });
                    rwAgronomo.setAdapter(adapterAgronomo);
                });


            });


        }
    }

    private void cambiarFavorita(Fotos fotos) {

        if (fotos.isFavorita()) {
            Toasty.info(activity, getResources().getString(R.string.message_fav_remove), Toast.LENGTH_SHORT, true).show();
            fotos.setFavorita(false);
        } else {
            Toasty.info(activity, getResources().getString(R.string.message_fav), Toast.LENGTH_SHORT, true).show();
            fotos.setFavorita(true);
        }


        MainActivity.myAppDB.myDao().updateFavorita(fotos);
        if (adapterAgronomo != null) {
            adapterAgronomo.notifyDataSetChanged();
        }

        if (adapterCliente != null) {
            adapterCliente.notifyDataSetChanged();
        }

    }


    private void preguntarFotoRaices() {
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.foto_raices, null);


        final AlertDialog builder = new AlertDialog.Builder(activity)
                .setView(viewInfalted)
                .create();

        final EditText et = viewInfalted.findViewById(R.id.et_produndidad_raices);
        final CheckBox check = viewInfalted.findViewById(R.id.check_pretunta_regla);
        final Button pic = viewInfalted.findViewById(R.id.btn_tomar_foto_raiz);
        final Button cancelar = viewInfalted.findViewById(R.id.btn_cancelar_modal);


        check.setOnCheckedChangeListener((buttonView, isChecked) -> {
            pic.setEnabled(isChecked);
        });


        pic.setOnClickListener(ev -> {

            Utilidades.hideKeyboard(activity);

            if (et.getText().toString().isEmpty() || !check.isChecked()) {
                medidaRaices = "";
                Utilidades.hideKeyboard(activity);
                Toasty.error(requireActivity(),
                                "Necesitas ingresar la profundidad y afirmar el uso de regla",
                                Toast.LENGTH_LONG, true)
                        .show();
                return;
            }
            abrirCamara(3, et.getText().toString());
            builder.dismiss();
        });

        cancelar.setOnClickListener(ev -> builder.dismiss());

        builder.setCancelable(true);
        builder.show();
    }

    private void agregarImagenToRaices() {

        if (temp_visitas != null && rwRaices != null) {

            LinearLayoutManager lManager = null;
            if (activity != null) {
                lManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);

            }

            int idDispo = 0;
            Config config = MainActivity.myAppDB.myDao().getConfig();
            if (config != null) {
                idDispo = config.getId();
            }

            rwRaices.setHasFixedSize(true);
            rwRaices.setLayoutManager(lManager);

            int visitaServidor = 0;

            Visitas visitas = MainActivity.myAppDB.myDao().getVisitas(temp_visitas.getId_temp_visita());
            if (visitas != null) {
                visitaServidor = (visitas.getEstado_server_visitas() == 1) ? prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0) : 0;
            }

            List<Fotos> myImageList = MainActivity.myAppDB.myDao().getFotosByFieldAndView(3, temp_visitas.getId_anexo_temp_visita(), temp_visitas.getId_visita_local(), visitaServidor, idDispo);

            FotosListAdapter adapterRaices = new FotosListAdapter(myImageList, activity, this::showAlertForUpdate, photo -> {
            });


            rwRaices.setAdapter(adapterRaices);
        }
    }

    private void agregarImagenToClientes() {

        if (temp_visitas != null && rwCliente != null) {

            LinearLayoutManager lManager = null;
            if (activity != null) {
                lManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);

            }

            int idDispo = 0;
            Config config = MainActivity.myAppDB.myDao().getConfig();
            if (config != null) {
                idDispo = config.getId();
            }

            rwCliente.setHasFixedSize(true);
            rwCliente.setLayoutManager(lManager);

            int visitaServidor = 0;

            Visitas visitas = MainActivity.myAppDB.myDao().getVisitas(temp_visitas.getId_temp_visita());
            if (visitas != null) {
                visitaServidor = (visitas.getEstado_server_visitas() == 1) ? prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0) : 0;
            }

            List<Fotos> myImageList = MainActivity.myAppDB.myDao().getFotosByFieldAndView(0, temp_visitas.getId_anexo_temp_visita(), temp_visitas.getId_visita_local(), visitaServidor, idDispo);

            adapterCliente = new FotosListAdapter(myImageList, activity, this::showAlertForUpdate, fotos -> {
                if (temp_visitas.getAction_temp_visita() == 2) {
                    Utilidades.avisoListo(activity, getResources().getString(R.string.title_dialog_agron), "Visita en estado terminado, no puedes cambiar el estado de las fotos.", "entiendo");
                } else {
                    if (!fotos.isFavorita()) {
                        int favoritas = MainActivity.myAppDB.myDao().getCantFavoritasByFieldbookFichaAndVista(0, temp_visitas.getId_anexo_temp_visita(), 0, temp_visitas.getId_temp_visita());

                        if (favoritas < 3) {
                            cambiarFavorita(fotos);
                        } else {
                            Utilidades.avisoListo(activity, getResources().getString(R.string.title_dialog_fav), getResources().getString(R.string.message_dialog_fav), getResources().getString(R.string.message_dialog_btn_ok));
                        }
                    } else {
                        cambiarFavorita(fotos);
                    }
                }
            });


            rwCliente.setAdapter(adapterCliente);
        }
    }


    public void preguntarSiQuiereVolver(String title, String message) {
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_empty, null);

        final AlertDialog builder = new AlertDialog.Builder(activity)
                .setView(viewInfalted)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("aceptar", (dialogInterface, i) -> {
                })
                .setNegativeButton("cancelar", (dialog, which) -> {
                })
                .create();

        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
            Button c = builder.getButton(AlertDialog.BUTTON_NEGATIVE);
            b.setOnClickListener(v -> {
                MainActivity activity = (MainActivity) getActivity();
                if (activity != null) {
                    activity.cambiarFragment(new FragmentVisitas(), Utilidades.FRAGMENT_VISITAS, R.anim.slide_in_right, R.anim.slide_out_right);
                }
                builder.dismiss();
            });
            c.setOnClickListener(v -> builder.dismiss());
        });
        builder.setCancelable(false);
        builder.show();
    }


    public void showAlertAskForSave123(String title, String message) {
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_empty, null);

        final AlertDialog builder = new AlertDialog.Builder(activity)
                .setView(viewInfalted)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("aceptar", (dialogInterface, i) -> {
                })
                .setNegativeButton("cancelar", (dialog, which) -> {
                })
                .create();

        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
            Button c = builder.getButton(AlertDialog.BUTTON_NEGATIVE);
            b.setOnClickListener(v -> {
                setOnSave();
                builder.dismiss();
            });
            c.setOnClickListener(v -> builder.dismiss());
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void showAlertForSave(String title, String message) {
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_empty, null);


        final AlertDialog builder22 = new AlertDialog.Builder(activity)
                .setView(viewInfalted)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar", (dialogInterface, i) -> {
                })
                .create();


        builder22.setOnShowListener(dialog -> {
            Button a = builder22.getButton(AlertDialog.BUTTON_POSITIVE);
            a.setOnClickListener(v -> {
                builder22.dismiss();
                activity.cambiarFragment(new FragmentVisitas(), Utilidades.FRAGMENT_VISITAS, R.anim.slide_in_left, R.anim.slide_out_left);

            });
        });
        builder22.setCancelable(true);
        builder22.show();
    }

    private void showAlertForUpdate(final Fotos foto) {
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_big_img, null);

        final AlertDialog builder;
        builder = new AlertDialog.Builder(requireActivity())
                .setView(viewInfalted)
                .setNeutralButton("eliminar",
                        (foto.getEstado_fotos() <= 0)
                                ? (dialogInterface, i) -> {
                        }
                                : null
                )
                .setPositiveButton("cerrar", (dialogInterface, i) -> {
                }).create();

        final ImageView imageView = viewInfalted.findViewById(R.id.img_alert_foto);

        Picasso.get().load("file:///" + foto.getRuta()).into(imageView);
        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
            Button c = builder.getButton(AlertDialog.BUTTON_NEUTRAL);

            b.setOnClickListener(v -> builder.dismiss());


            c.setOnClickListener(view -> {
                if (foto.getEstado_fotos() > 0) {
                    Toasty.warning(activity, "No puedes eliminar la foto, esta visita ya se subio a servidor", Toast.LENGTH_SHORT, true).show();
                } else {
                    showAlertForEliminarFoto(foto);
                    builder.dismiss();
                }
            });
        });
        builder.setCancelable(false);
        builder.show();
    }

    void showAlertForEliminarFoto(final Fotos foto) {

        final AlertDialog builder = new AlertDialog.Builder(activity)
                .setView(R.layout.alert_empty)
                .setPositiveButton("Eliminar",
                        (foto.getEstado_fotos() <= 0)
                                ? (dialogInterface, i) -> {
                        } : null
                )
                .setNegativeButton("cancelar", (dialogInterface, i) -> {
                })
                .setTitle("Confirmación")
                .setMessage("¿Estas seguro que deseas eliminar esta foto?").create();

        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
            Button c = builder.getButton(AlertDialog.BUTTON_NEGATIVE);

            b.setOnClickListener(view -> {
                try {
                    File file = new File(foto.getRuta());

                    if (!file.exists()) {
                        MainActivity.myAppDB.myDao().deleteFotos(foto);
                        agregarImagenToAgronomos();
                        agregarImagenToClientes();
                        Toasty.success(activity, "Foto eliminada con exito", Toast.LENGTH_SHORT, true).show();
                        builder.dismiss();
                        return;
                    }

                    if (!file.delete()) {
                        Toasty.warning(activity, "Por favor vuelva a intentarlo", Toast.LENGTH_SHORT, true).show();
                        builder.dismiss();
                        return;
                    }

                    MainActivity.myAppDB.myDao().deleteFotos(foto);
                    agregarImagenToAgronomos();
                    agregarImagenToClientes();
                    Toasty.success(activity, "Foto eliminada con exito", Toast.LENGTH_SHORT, true).show();
                    builder.dismiss();

                } catch (Exception e) {
                    MainActivity.myAppDB.myDao().deleteFotos(foto);
                    agregarImagenToAgronomos();
                    agregarImagenToClientes();
                    builder.dismiss();
                }
            });
            c.setOnClickListener(v -> builder.dismiss());
        });

        builder.setCancelable(false);
        builder.show();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (fileImagen != null) {
            outState.putParcelable("file_uri", Uri.fromFile(fileImagen));
        }


    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.getParcelable("file_uri") != null) {
            Uri ui = savedInstanceState.getParcelable("file_uri");
            if (ui != null && ui.getPath() != null) {
                fileImagen = new File(ui.getPath());
            }
        }


    }


}
