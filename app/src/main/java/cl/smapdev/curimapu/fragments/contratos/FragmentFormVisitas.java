package cl.smapdev.curimapu.fragments.contratos;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cl.smapdev.curimapu.BuildConfig;
import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.FotosListAdapter;
import cl.smapdev.curimapu.clases.adapters.SpinnerAdapter;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.relaciones.VisitasCompletas;
import cl.smapdev.curimapu.clases.tablas.AnexoContrato;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Evaluaciones;
import cl.smapdev.curimapu.clases.tablas.Fotos;
import cl.smapdev.curimapu.clases.tablas.Visitas;
import cl.smapdev.curimapu.clases.temporales.TempVisitas;
import cl.smapdev.curimapu.clases.utilidades.CameraUtils;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.FragmentVisitas;
import cl.smapdev.curimapu.fragments.dialogos.DialogLibroCampo;
import cl.smapdev.curimapu.fragments.dialogos.DialogObservationTodo;
import cl.smapdev.curimapu.infraestructure.utils.coroutines.ApplicationExecutors;
import es.dmoral.toasty.Toasty;

public class FragmentFormVisitas extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, View.OnFocusChangeListener {

    private Spinner sp_fenologico, sp_cosecha, sp_crecimiento, sp_fito, sp_general_cultivo, sp_humedad, sp_malezas;
    private Spinner sp_planta_voluntaria;

    private TextView titulo_raices;

    private TextInputLayout ti_percent_humedad;
    private Button btn_guardar, btn_volver;


    private ConstraintLayout contenedor_estados, contenedor_imagenes, contenedor_observaciones, contenedor_monitoreo;


    private MainActivity activity;
    private SharedPreferences prefs;

    private TempVisitas temp_visitas;
    private String currentPhotoPath;
    private String medidaRaices;

    private final ArrayList<String> fenologico = new ArrayList<>();
    private final ArrayList<String> planta_voluntaria = new ArrayList<>();
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

    private FotosListAdapter adapterRaices;


    private ProgressDialog progressBar;

    private final String[] forbiddenWords = new String[]{"á", "é", "í", "ó", "ú", "Á", "É", "Í", "Ó", "Ú"};
    private final String[] forbiddenReplacement = new String[]{"a", "e", "i", "o", "u", "A", "E", "I", "O", "U"};


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity a = (MainActivity) getActivity();
        if (a != null) activity = a;


        prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);

        progressBar = new ProgressDialog(activity);
        progressBar.setTitle(getResources().getString(R.string.espere));
        progressBar.show();

        planta_voluntaria.addAll(Arrays.asList(getResources().getStringArray(R.array.plantas_voluntarias)));
        fenologico.addAll(Arrays.asList(getResources().getStringArray(R.array.fenologico)));
        cosecha.addAll(Arrays.asList(getResources().getStringArray(R.array.cosecha)));
        crecimiento.addAll(Arrays.asList(getResources().getStringArray(R.array.crecimiento)));
        maleza.addAll(Arrays.asList(getResources().getStringArray(R.array.maleza)));

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contrato, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind(view);
        setHasOptionsMenu(true);


        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (!checkPermission()) {
                requestPermission();
            }
        }

        ApplicationExecutors exec = new ApplicationExecutors();
        exec.getBackground().execute(() -> {
            if (temp_visitas == null) {
                temp_visitas = new TempVisitas();
                if (prefs != null) {
                    temp_visitas.setId_anexo_temp_visita(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
                }
                temp_visitas.setId_temp_visita(0);
                String claveUnica = UUID.randomUUID().toString();
                temp_visitas.setClave_unica_visita(claveUnica);
                MainActivity.myAppDB.myDao().setTempVisitas(temp_visitas);
//                temp_visitas = MainActivity.myAppDB.myDao().getTempFichas();
            }
            exec.getMainThread().execute(() -> {
                chargeAll();
                if (progressBar != null && progressBar.isShowing()) {
                    progressBar.dismiss();
                }

            });
        });

        exec.shutDownBackground();


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_visitas, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String variedad = prefs.getString(Utilidades.SHARED_VISIT_MATERIAL_ID, "");
        String temporada = prefs.getString(Utilidades.SHARED_VISIT_TEMPORADA, "1");
        if (item.getItemId() == R.id.menu_visitas_recom) {
            ExecutorService executor = Executors.newSingleThreadExecutor();

            VisitasCompletas visitasCompletas = null;
            try {
                TempVisitas temp_visitas = executor.submit(() -> MainActivity.myAppDB.myDao().getTempFichas()).get();

                Future<VisitasCompletas> visitasCompletasFuture = executor.submit(() -> MainActivity.myAppDB.myDao().getUltimaVisitaByAnexo(temp_visitas.getId_anexo_temp_visita()));

                if (temp_visitas != null && temp_visitas.getAction_temp_visita() != 2) {
                    visitasCompletas = visitasCompletasFuture.get();
                }

                AnexoContrato ac = executor.submit(() -> MainActivity.myAppDB.myDao().getAnexos(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""))).get();

                FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                Fragment prev = requireActivity().getSupportFragmentManager().findFragmentByTag("EVALUACION_RECOMENDACION");
                if (prev != null) {
                    ft.remove(prev);
                }

                DialogObservationTodo dialogo = DialogObservationTodo.newInstance(ac, temp_visitas, visitasCompletas, this::cargarTemp);
                dialogo.show(ft, "EVALUACION_RECOMENDACION");
            } catch (ExecutionException | InterruptedException e) {
                Toasty.error(activity, "Error levantando dialogo " + e.getMessage(), Toast.LENGTH_LONG, true).show();
            } finally {
                executor.shutdown();
            }
            return true;
        } else if (item.getItemId() == R.id.menu_visitas_lc_resumen) {

            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = requireActivity().getSupportFragmentManager().findFragmentByTag("LIBRO_CAMPO_RESUMEN");
            if (prev != null) {
                ft.remove(prev);
            }

            DialogLibroCampo dialogo = DialogLibroCampo.newInstance(temporada, variedad, 1, (boolean saved) -> {
                if (saved) {
                    Toasty.success(activity, "No olvides que se guardará al guardar la visita", Toast.LENGTH_LONG, true).show();
                }
            });
            dialogo.show(ft, "LIBRO_CAMPO_RESUMEN");

        } else if (item.getItemId() == R.id.menu_visitas_lc_sowing) {
            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = requireActivity().getSupportFragmentManager().findFragmentByTag("LIBRO_CAMPO_SOWING");
            if (prev != null) {
                ft.remove(prev);
            }

            DialogLibroCampo dialogo = DialogLibroCampo.newInstance(temporada, variedad, 2, (boolean saved) -> {
                if (saved) {
                    Toasty.success(activity, "No olvides que se guardará al guardar la visita", Toast.LENGTH_LONG, true).show();
                }
            });
            dialogo.show(ft, "LIBRO_CAMPO_SOWING");

        } else if (item.getItemId() == R.id.menu_visitas_lc_flowering) {

            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = requireActivity().getSupportFragmentManager().findFragmentByTag("LIBRO_CAMPO_FLOWERING");
            if (prev != null) {
                ft.remove(prev);
            }

            DialogLibroCampo dialogo = DialogLibroCampo.newInstance(temporada, variedad, 3, (boolean saved) -> {
                if (saved) {
                    Toasty.success(activity, "No olvides que se guardará al guardar la visita", Toast.LENGTH_LONG, true).show();
                }
            });
            dialogo.show(ft, "LIBRO_CAMPO_FLOWERING");

        } else if (item.getItemId() == R.id.menu_visitas_lc_harvest) {
            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = requireActivity().getSupportFragmentManager().findFragmentByTag("LIBRO_CAMPO_HARVEST");
            if (prev != null) {
                ft.remove(prev);
            }

            DialogLibroCampo dialogo = DialogLibroCampo.newInstance(temporada, variedad, 4, (boolean saved) -> {
                if (saved) {
                    Toasty.success(activity, "No olvides que se guardará al guardar la visita", Toast.LENGTH_LONG, true).show();
                }
            });
            dialogo.show(ft, "LIBRO_CAMPO_HARVEST");
        }


        return super.onOptionsItemSelected(item);
    }


    private void cargarTemp(TempVisitas tempVisitas) {
        temp_visitas = tempVisitas;
    }

    private void chargeAll() {
        if (progressBar != null && progressBar.isShowing()) {
            progressBar.dismiss();
        }
        if (temp_visitas != null && temp_visitas.getAction_temp_visita() != 2 && temp_visitas.getEvaluacion() <= 0.0) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            try {
                VisitasCompletas visitasCompletas = executor.submit(() -> MainActivity.myAppDB.myDao().getUltimaVisitaByAnexo(temp_visitas.getId_anexo_temp_visita())).get();

                AnexoContrato anexoContrato;

                if (visitasCompletas != null) {
                    anexoContrato = visitasCompletas.getAnexoCompleto().getAnexoContrato();
                } else {
                    anexoContrato = executor.submit(() -> MainActivity.myAppDB.myDao().getAnexos(temp_visitas.getId_anexo_temp_visita())).get();
                }

                try {
                    FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                    Fragment prev = requireActivity().getSupportFragmentManager().findFragmentByTag("EVALUACION_RECOMENDACION");
                    if (prev != null) {
                        ft.remove(prev).commit();
                    }
                    if (visitasCompletas != null) {
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
                        dialogo.show(ft, "EVALUACION_RECOMENDACION");
                    }
                } catch (IllegalStateException e) {
                    Log.e("ERROR_CONTEXT", e.getLocalizedMessage());
                    Toast.makeText(activity, "Sin contexto para dialogo", Toast.LENGTH_SHORT).show();
                }

            } catch (ExecutionException | InterruptedException e) {
                Toasty.error(activity, "No se pudo obtener informacion de la visita " + e.getMessage(), Toast.LENGTH_LONG, true).show();
            } finally {
                executor.shutdown();
            }
        }

        cargarSpinners();
        foto_raices.setOnClickListener(v -> preguntarFotoRaices());

        material_private.setOnClickListener(v -> {

            int cantidaAgr = MainActivity.myAppDB.myDao().getCantAgroByFieldViewAndFicha(0, 2, temp_visitas.getId_anexo_temp_visita(), temp_visitas.getId_temp_visita());
            if (cantidaAgr > 2) {
                Utilidades.avisoListo(activity, getResources().getString(R.string.title_dialog_agron), getResources().getString(R.string.message_dialog_agron), getResources().getString(R.string.message_dialog_btn_ok));
                return;
            }

            if (temp_visitas.getAction_temp_visita() == 2) {
                Utilidades.avisoListo(activity, getResources().getString(R.string.title_dialog_agron), getResources().getString(R.string.visitas_terminadas), getResources().getString(R.string.entiendo));
                return;
            }

            Utilidades.hideKeyboard(activity);
            abrirCamara(2, "0");

        });
        material_public.setOnClickListener(v -> {

            if (sp_fenologico.getSelectedItemPosition() == 0) {
                Utilidades.avisoListo(activity, getResources().getString(R.string.falta_algo), getResources().getString(R.string.pheno_first), getResources().getString(R.string.entiendo));
                return;
            }
            if (temp_visitas.getAction_temp_visita() == 2) {
                Utilidades.avisoListo(activity, getResources().getString(R.string.title_dialog_agron), getResources().getString(R.string.visitas_terminadas), getResources().getString(R.string.entiendo));
                return;
            }

            Utilidades.hideKeyboard(activity);
            abrirCamara(0, "0");

        });


    }

    private void accionSpinners() {

        if (temp_visitas == null) return;

        sp_planta_voluntaria.setSelection(planta_voluntaria.indexOf(temp_visitas.getPlanta_voluntaria()));


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
        agregarImagenToRaices();


        if (temp_visitas.getAction_temp_visita() == 2) {
            sp_fenologico.setEnabled(false);
            sp_cosecha.setEnabled(false);
            sp_planta_voluntaria.setEnabled(false);
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

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toasty.normal(requireActivity(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 100);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (activity == null) return;

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        try {
            temp_visitas = executorService.submit(() -> MainActivity.myAppDB.myDao().getTempFichas()).get();
            agregarImagenToAgronomos();
            agregarImagenToClientes();
            agregarImagenToRaices();

        } catch (ExecutionException | InterruptedException e) {
            Toasty.error(activity, "Error cargando datos " + e.getMessage(), Toast.LENGTH_LONG, true).show();
        } finally {
            executorService.shutdown();
        }


    }

    private void cargarSpinners() {

        sp_fenologico.setAdapter(new SpinnerAdapter(activity, R.layout.spinner_template_view, fenologico));
        sp_cosecha.setAdapter(new SpinnerAdapter(activity, R.layout.spinner_template_view, cosecha));
        sp_planta_voluntaria.setAdapter(new SpinnerAdapter(activity, R.layout.spinner_template_view, planta_voluntaria));
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
        if (!isVisibleToUser || activity == null) return;

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        try {
            temp_visitas = executorService.submit(() -> MainActivity.myAppDB.myDao().getTempFichas()).get();
            accionSpinners();
            agregarImagenToAgronomos();
            agregarImagenToClientes();
            agregarImagenToRaices();

        } catch (ExecutionException | InterruptedException e) {
            Toasty.error(activity, "Error cargando datos " + e.getMessage(), Toast.LENGTH_LONG, true).show();
        } finally {
            executorService.shutdown();
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_guardar:

                if (activity == null) {
                    Toasty.error(requireActivity(), "No encontramos algo importante , por favor, vuelva a realizar la visita", Toast.LENGTH_LONG, true).show();
                    return;
                }

                String message = !sp_fenologico.getSelectedItem().toString().equals("MONITOREO")
                        ? "Revise el libro de campo antes de hacerlo, si esta seguro, presione aceptar."
                        : "";

                showAlertAskForSave123("¿Esta seguro que desea guardar?", message);
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
        contenedor_estados = view.findViewById(R.id.contenedor_estados);
        contenedor_monitoreo = view.findViewById(R.id.contenedor_monitoreo);
        contenedor_imagenes = view.findViewById(R.id.contenedor_imagenes);
        contenedor_observaciones = view.findViewById(R.id.contenedor_observaciones);
        titulo_raices = view.findViewById(R.id.titulo_raices);
        ti_percent_humedad = view.findViewById(R.id.ti_percent_humedad);
        sp_fenologico = view.findViewById(R.id.sp_feno);
        sp_cosecha = view.findViewById(R.id.sp_cosecha);
        sp_planta_voluntaria = view.findViewById(R.id.sp_planta_voluntaria);
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

        btn_volver = view.findViewById(R.id.btn_volver);

        sp_fenologico.setOnItemSelectedListener(this);
        sp_malezas.setOnItemSelectedListener(this);
        sp_humedad.setOnItemSelectedListener(this);
        sp_general_cultivo.setOnItemSelectedListener(this);
        sp_fito.setOnItemSelectedListener(this);
        sp_crecimiento.setOnItemSelectedListener(this);
        sp_cosecha.setOnItemSelectedListener(this);
        sp_planta_voluntaria.setOnItemSelectedListener(this);

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

    public void modificarVista(boolean esMonitoreo) {
        contenedor_estados.setVisibility((esMonitoreo) ? View.GONE : View.VISIBLE);
        titulo_raices.setVisibility((esMonitoreo) ? View.GONE : View.VISIBLE);
        rwRaices.setVisibility((esMonitoreo) ? View.GONE : View.VISIBLE);
        contenedor_monitoreo.setVisibility((!esMonitoreo) ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i > 0) {
            switch (adapterView.getId()) {
                case R.id.sp_feno:
                    temp_visitas.setPhenological_state_temp_visita(fenologico.get(i));
                    temp_visitas.setEtapa_temp_visitas(Utilidades.getPhenoState(sp_fenologico.getSelectedItemPosition()));
                    prefs.edit().putInt(Utilidades.SHARED_ETAPA_SELECTED, Utilidades.getPhenoState(sp_fenologico.getSelectedItemPosition())).apply();

                    modificarVista(sp_fenologico.getSelectedItem().toString().equals("MONITOREO"));

                    break;
                case R.id.sp_cosecha:
                    obs_harvest.setVisibility(View.VISIBLE);
                    temp_visitas.setHarvest_temp_visita(cosecha.get(i));
                    break;
                case R.id.sp_planta_voluntaria:
                    temp_visitas.setPlanta_voluntaria(planta_voluntaria.get(i));
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
            ExecutorService ex = Executors.newSingleThreadExecutor();
            try {
                ex.submit(() -> MainActivity.myAppDB.myDao().updateTempVisitas(temp_visitas)).get();
            } catch (ExecutionException | InterruptedException e) {
                Toasty.error(activity, "Error guardando temporal" + e.getMessage(), Toast.LENGTH_LONG, true).show();
            } finally {
                ex.shutdown();
            }
        } else {
            switch (adapterView.getId()) {
                case R.id.sp_feno:
                    temp_visitas.setPhenological_state_temp_visita("");
                    temp_visitas.setEtapa_temp_visitas(Utilidades.getPhenoState(sp_fenologico.getSelectedItemPosition()));
                    break;
                case R.id.sp_cosecha:
                    obs_harvest.setVisibility(View.GONE);
                    temp_visitas.setHarvest_temp_visita("");
                    break;
                case R.id.sp_planta_voluntaria:
                    temp_visitas.setPlanta_voluntaria("");
                    break;
                case R.id.sp_crecimiento:
                    obs_growth.setVisibility(View.GONE);
                    temp_visitas.setGrowth_status_temp_visita("");
                    break;
                case R.id.sp_fito:
                    obs_fito.setVisibility(View.GONE);
                    temp_visitas.setPhytosanitary_state_temp_visita("");
                    break;
                case R.id.sp_general_cultivo:
                    obs_overall.setVisibility(View.GONE);
                    temp_visitas.setOverall_status_temp_visita("");
                    break;
                case R.id.sp_humedad:
                    obs_humedad.setVisibility(View.GONE);
                    temp_visitas.setHumidity_floor_temp_visita("");
                    break;
                case R.id.sp_malezas:
                    obs_weed.setVisibility(View.GONE);
                    temp_visitas.setWeed_state_temp_visita("");
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }


    @Override
    public void onFocusChange(View view, boolean b) {
        String text = "";
        if (b) {
            switch (view.getId()) {
                case R.id.et_fecha_estimada:
                    et_fecha_estimada.setKeyListener(null);
                    et_fecha_estimada.setInputType(InputType.TYPE_NULL);

                    Utilidades.levantarFecha(et_fecha_estimada, view.getContext());
                    break;

                case R.id.et_fecha_arranca:
                    et_fecha_arranca.setKeyListener(null);
                    et_fecha_arranca.setInputType(InputType.TYPE_NULL);
                    Utilidades.levantarFecha(et_fecha_arranca, view.getContext());
                    break;
            }
        }
        if (!b) {
            switch (view.getId()) {
                case R.id.et_obs:
                    temp_visitas.setObservation_temp_visita(et_obs.getText().toString().toUpperCase());
                    break;
                case R.id.et_obs_growth:
                    text = et_obs_growth.getText().toString().toUpperCase();
                    temp_visitas.setObs_creci(text);
                    break;
                case R.id.et_obs_weed:
                    text = et_obs_weed.getText().toString().toUpperCase();
                    temp_visitas.setObs_maleza(text);
                    break;
                case R.id.et_obs_fito:
                    text = et_obs_fito.getText().toString().toUpperCase();
                    temp_visitas.setObs_fito(text);
                    break;
                case R.id.et_obs_harvest:
                    text = et_obs_harvest.getText().toString().toUpperCase();
                    temp_visitas.setObs_cosecha(text);
                    break;
                case R.id.et_obs_overall:
                    text = et_obs_overall.getText().toString().toUpperCase();
                    temp_visitas.setObs_overall(text);
                    break;
                case R.id.et_obs_humedad:
                    text = et_obs_humedad.getText().toString().toUpperCase();
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

            ExecutorService ex = Executors.newSingleThreadExecutor();
            try {
                ex.submit(() -> MainActivity.myAppDB.myDao().updateTempVisitas(temp_visitas)).get();
            } catch (ExecutionException | InterruptedException e) {
                Toasty.error(activity, "Error guardando temporal" + e.getMessage(), Toast.LENGTH_LONG, true).show();
            } finally {
                ex.shutdown();
            }
        }
    }

    private void abrirCamara(int vista, String medida) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;
        try {
            photoFile = Utilidades.createImageFile(requireActivity());
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

        Uri photoUri = FileProvider.getUriForFile(requireActivity(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, COD_FOTO);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == COD_FOTO && resultCode == RESULT_OK) {

            try {
                Bitmap originalBtm = BitmapFactory.decodeFile(currentPhotoPath);
                Bitmap nuevaFoto = CameraUtils.escribirFechaImg(originalBtm, activity);


                currentPhotoPath = currentPhotoPath.replaceAll("Pictures/", "");
                File file = new File(currentPhotoPath);
                FileOutputStream fos = new FileOutputStream(file);
                nuevaFoto.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                guardarBD();

            } catch (Exception e) {
                Toasty.error(requireActivity(), "Error al sacar foto " + e.getMessage(), Toast.LENGTH_LONG, true).show();
            }
        }

    }


    private void guardarBD() {


        ApplicationExecutors exec = new ApplicationExecutors();
        exec.getBackground().execute(() -> {
            Fotos fotos = new Fotos();
            fotos.setFecha(Utilidades.fechaActualConHora());

            int vista = prefs.getInt(Utilidades.VISTA_FOTOS, 0);

            Log.e("RUTA 2", currentPhotoPath);

            File file = new File(currentPhotoPath);

            fotos.setFieldbook((prefs.getInt(Utilidades.VISTA_FOTOS, 0) == 2) ? 0 : Utilidades.getPhenoState(sp_fenologico.getSelectedItemPosition()));
            fotos.setHora(Utilidades.hora());
            fotos.setNombre_foto(file.getName());
            fotos.setFavorita(false);
            fotos.setPlano(0);
            fotos.setMedida_raices(!medidaRaices.isEmpty() ? medidaRaices : "0");
            fotos.setAcepto_regla_raices((vista == 3) ? 1 : 0);


            if (temp_visitas != null) {
                fotos.setId_ficha_fotos(temp_visitas.getId_anexo_temp_visita());
            }
            fotos.setVista(vista);
            fotos.setRuta(currentPhotoPath);


            fotos.setId_visita_foto(prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0));


            Config config = MainActivity.myAppDB.myDao().getConfig();
            if (config != null) {
                fotos.setId_dispo_foto(config.getId());
            }

            MainActivity.myAppDB.myDao().insertFotos(fotos);

            exec.getMainThread().execute(() -> {
                agregarImagenToAgronomos();
                agregarImagenToClientes();
                agregarImagenToRaices();
            });

        });

        exec.shutDownBackground();

    }


    private void saveVisitaNormal() {

        ExecutorService exec = Executors.newSingleThreadExecutor();

        try {
            VisitasCompletas cc = exec.submit(() -> MainActivity.myAppDB.myDao().getUltimaVisitaByAnexo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""))).get();

            if ((temp_visitas.getEvaluacion() == 0.0 || temp_visitas.getComentario_evaluacion().isEmpty()) && cc != null && cc.getVisitas() != null) {
                Utilidades.avisoListo(activity, "Falta algo", "Antes de guardar debes realizar la evaluacion de la visita anterior. ( estrella de arriba a la derecha )", "entiendo");
                return;
            }

            int fotosClientes = exec.submit(() -> MainActivity.myAppDB.myDao().getCantFotos(temp_visitas.getId_anexo_temp_visita(), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0), 0)).get();
            int fotosAgricultores = exec.submit(() -> MainActivity.myAppDB.myDao().getCantFotos(temp_visitas.getId_anexo_temp_visita(), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0), 2)).get();


            if (fotosClientes <= 0 || fotosAgricultores <= 0) {
                Utilidades.avisoListo(activity, "Falta algo", "Debes tomar al menos una foto cliente y agricultor", "entiendo");
                return;
            }


            int favsCliente = exec.submit(() -> MainActivity.myAppDB.myDao().getCantFavoritasByFieldbookAndFicha(temp_visitas.getId_anexo_temp_visita(), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0), 0)).get();
            int favsAgricultores = exec.submit(() -> MainActivity.myAppDB.myDao().getCantFavoritasByFieldbookAndFicha(temp_visitas.getId_anexo_temp_visita(), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0), 2)).get();

            if (favsCliente <= 0 || favsAgricultores <= 0) {
                Utilidades.avisoListo(activity, "Falta algo", "Debes seleccionar como favorita al menos una foto cliente y agricultor (manten presionada la foto para marcar)", "entiendo");
                return;
            }

            boolean entra = (sp_malezas.getSelectedItemPosition() == 3 && TextUtils.isEmpty(et_obs_weed.getText()));
            if (!entra) {
                entra = (sp_fito.getSelectedItemPosition() == 4 && TextUtils.isEmpty(et_obs_fito.getText()));
            }
            if (!entra) {
                entra = ((sp_cosecha.getSelectedItemPosition() == 1) && TextUtils.isEmpty(et_obs_harvest.getText()));
            }
            if (!entra) {
                entra = (sp_general_cultivo.getSelectedItemPosition() == 4 && TextUtils.isEmpty(et_obs_overall.getText()));
            }
            if (entra) {
                Utilidades.avisoListo(activity, "Faltan cosas", "Debe completar todas las observaciones en donde su estado sea malo/alto/rechazado", "entiendo");
                return;
            }

            if (sp_fenologico.getSelectedItemPosition() <= 0 || sp_malezas.getSelectedItemPosition() <= 0 ||
                    sp_fito.getSelectedItemPosition() <= 0 || sp_cosecha.getSelectedItemPosition() <= 0 ||
                    sp_general_cultivo.getSelectedItemPosition() <= 0 || sp_humedad.getSelectedItemPosition() <= 0) {
                Utilidades.avisoListo(activity, "Hey", "no puede dejar elementos en '--seleccione--'.", "entiendo");
                return;
            }

            if (TextUtils.isEmpty(et_percent_humedad.getText().toString()) || (Double.parseDouble(et_percent_humedad.getText().toString()) <= 0.0 || Double.parseDouble(et_percent_humedad.getText().toString()) > 200.0)) {
                Utilidades.avisoListo(activity, "Hey", "Debes ingresar un potencial de rendimiento sobre 0 y bajo 200 ", "entiendo");
                return;
            }


            Visitas visitas = new Visitas();
            visitas.setGrowth_status_visita(sp_crecimiento.getSelectedItem().toString());
            visitas.setHarvest_visita(sp_cosecha.getSelectedItem().toString());
            visitas.setEstado_server_visitas(0);
            visitas.setEstado_visita(2);
            visitas.setTipo_visita("NORMAL");
            visitas.setFecha_estimada_arranca(Utilidades.voltearFechaBD(et_fecha_arranca.getText().toString()));
            visitas.setFecha_estimada_cosecha(Utilidades.voltearFechaBD(et_fecha_estimada.getText().toString()));
            visitas.setWeed_state_visita(sp_malezas.getSelectedItem().toString());
            visitas.setPhytosanitary_state_visita(sp_fito.getSelectedItem().toString());
            visitas.setPhenological_state_visita(sp_fenologico.getSelectedItem().toString());
            visitas.setHumidity_floor_visita(sp_humedad.getSelectedItem().toString());
            visitas.setOverall_status_visita(sp_general_cultivo.getSelectedItem().toString());

            visitas.setId_anexo_visita(temp_visitas.getId_anexo_temp_visita());
            visitas.setEtapa_visitas(temp_visitas.getEtapa_temp_visitas());
            visitas.setEvaluacion(temp_visitas.getEvaluacion());
            visitas.setComentario_evaluacion(temp_visitas.getComentario_evaluacion());
            visitas.setId_evaluacion(temp_visitas.getId_evaluacion());


            double percent;
            try {
                percent = Double.parseDouble(et_percent_humedad.getText().toString());
            } catch (Exception e) {
                percent = 0.0;
            }

            visitas.setPercent_humedad(percent);

            String alfaNumerico = getResources().getString(R.string.alfanumericos_con_signos);
            et_obs.setText(Utilidades.sanitizarString(et_obs.getText().toString(), alfaNumerico));
            et_obs_overall.setText(Utilidades.sanitizarString(et_obs_overall.getText().toString(), alfaNumerico));
            et_obs_weed.setText(Utilidades.sanitizarString(et_obs_weed.getText().toString(), alfaNumerico));
            et_obs_humedad.setText(Utilidades.sanitizarString(et_obs_humedad.getText().toString(), alfaNumerico));
            et_obs_fito.setText(Utilidades.sanitizarString(et_obs_fito.getText().toString(), alfaNumerico));
            et_obs_growth.setText(Utilidades.sanitizarString(et_obs_growth.getText().toString(), alfaNumerico));
            et_obs_harvest.setText(Utilidades.sanitizarString(et_obs_harvest.getText().toString(), alfaNumerico));


            String obsCosecha = et_obs_harvest.getText().toString().toUpperCase();
            String obsCreci = et_obs_growth.getText().toString().toUpperCase();
            String obsFito = et_obs_fito.getText().toString().toUpperCase();
            String obsHumedad = et_obs_humedad.getText().toString().toUpperCase();
            String obsMaleza = et_obs_weed.getText().toString().toUpperCase();
            String obsOverall = et_obs_overall.getText().toString().toUpperCase();
            String etobs = et_obs.getText().toString().toUpperCase();


            visitas.setObservation_visita(etobs);
            visitas.setObs_cosecha(obsCosecha);
            visitas.setObs_creci(obsCreci);
            visitas.setObs_fito(obsFito);
            visitas.setObs_humedad(obsHumedad);
            visitas.setObs_maleza(obsMaleza);
            visitas.setObs_overall(obsOverall);

            AnexoContrato an = exec.submit(() -> MainActivity.myAppDB.myDao().getAnexos(temp_visitas.getId_anexo_temp_visita())).get();
            visitas.setTemporada(an.getTemporada_anexo());
            String fecha = Utilidades.fechaActualSinHora();
            String hora = Utilidades.hora();

            visitas.setHora_visita(fecha);
            visitas.setFecha_visita(hora);

            visitas.setClave_unica_visita(temp_visitas.getClave_unica_visita());

            long idVisita;

            if (prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0) > 0) {

                idVisita = prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0);
                visitas.setId_visita((int) idVisita);
                visitas.setId_visita_local((int) idVisita);
                exec.submit(() -> MainActivity.myAppDB.myDao().updateVisita(visitas)).get();

            } else {
                idVisita = exec.submit(() -> MainActivity.myAppDB.myDao().setVisita(visitas)).get();
            }

            long finalIdVisita = idVisita;

            exec.submit(() -> MainActivity.myAppDB.DaoEvaluaciones().updateEvaluacionesObligadas(Integer.parseInt(an.getId_anexo_contrato()))).get();
            exec.submit(() -> MainActivity.myAppDB.myDao().updateFotosWithVisita((int) finalIdVisita, temp_visitas.getId_anexo_temp_visita())).get();
            exec.submit(() -> MainActivity.myAppDB.myDao().updateDetallesToVisits((int) finalIdVisita)).get();
            Visitas visitas1 = exec.submit(() -> MainActivity.myAppDB.myDao().getVisitas((int) finalIdVisita)).get();
            visitas1.setId_visita_local((int) idVisita);
            exec.submit(() -> MainActivity.myAppDB.myDao().updateVisita(visitas1)).get();

            showAlertForSave("Genial", "Se guardo todo como corresponde");

        } catch (ExecutionException | InterruptedException e) {
            Utilidades.avisoListo(activity, "Problemas", "No se pudo guardar la visita " + e.getMessage(), "entiendo");

        } finally {
            exec.shutdown();
        }
    }


    private void saveVisitaMonitoreo() {

        if (sp_fenologico.getSelectedItemPosition() <= 0 || sp_planta_voluntaria.getSelectedItemPosition() <= 0) {
            Utilidades.avisoListo(activity, "Hey", "no puede dejar elementos en '--seleccione--'.", "entiendo");
            return;
        }

        Visitas visitas = new Visitas();

        visitas.setPhenological_state_visita(sp_fenologico.getSelectedItem().toString());
        visitas.setPlanta_voluntaria(sp_planta_voluntaria.getSelectedItem().toString());

        String alfaNumerico = getResources().getString(R.string.alfanumericos_con_signos);
        et_obs.setText(Utilidades.sanitizarString(et_obs.getText().toString(), alfaNumerico));
        String etobs = et_obs.getText().toString().toUpperCase();
        visitas.setObservation_visita(etobs);

        ExecutorService exec = Executors.newSingleThreadExecutor();

        try {
            AnexoContrato an = exec.submit(() -> MainActivity.myAppDB.myDao().getAnexos(temp_visitas.getId_anexo_temp_visita())).get();
            visitas.setTemporada(an.getTemporada_anexo());

            String fecha = Utilidades.fechaActualConHora();
            String[] fechaHora = fecha.split(" ");

            visitas.setId_anexo_visita(temp_visitas.getId_anexo_temp_visita());
            visitas.setHora_visita(fechaHora[1]);
            visitas.setFecha_visita(fechaHora[0]);
            visitas.setEtapa_visitas(temp_visitas.getEtapa_temp_visitas());
            visitas.setEvaluacion(temp_visitas.getEvaluacion());
            visitas.setComentario_evaluacion(temp_visitas.getComentario_evaluacion());
            visitas.setId_evaluacion(temp_visitas.getId_evaluacion());
            visitas.setEstado_server_visitas(0);
            visitas.setEstado_visita(2);

            visitas.setClave_unica_visita(temp_visitas.getClave_unica_visita());
            visitas.setTipo_visita("MONITOREO");

            long idVisita;

            if (prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0) > 0) {
                idVisita = prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0);
                visitas.setId_visita((int) idVisita);
                visitas.setId_visita_local((int) idVisita);
                exec.submit(() -> MainActivity.myAppDB.myDao().updateVisita(visitas)).get();
            } else {
                idVisita = MainActivity.myAppDB.myDao().setVisita(visitas);
            }

            long finalIdVisita = idVisita;

            exec.submit(() -> MainActivity.myAppDB.DaoEvaluaciones().updateEvaluacionesObligadas(Integer.parseInt(an.getId_anexo_contrato()))).get();
            exec.submit(() -> MainActivity.myAppDB.myDao().updateFotosWithVisita((int) finalIdVisita, temp_visitas.getId_anexo_temp_visita())).get();


            Visitas visitas1 = exec.submit(() -> MainActivity.myAppDB.myDao().getVisitas((int) finalIdVisita)).get();
            visitas1.setId_visita_local((int) idVisita);
            MainActivity.myAppDB.myDao().updateVisita(visitas1);

            showAlertForSave("Genial", "Se guardo todo como corresponde");

        } catch (ExecutionException | InterruptedException e) {
            Utilidades.avisoListo(activity, "Falta algo", "No se pudo guardar " + e.getMessage(), "entiendo");
        } finally {
            exec.shutdown();
        }
    }

    private void setOnSave() {

        if (temp_visitas == null) {
            Utilidades.avisoListo(activity, "Falta algo", "Algo salio mal, por favor revise la visita", "entiendo");
            return;
        }

        ExecutorService exec = Executors.newSingleThreadExecutor();
        Future<List<Evaluaciones>> evas = exec.submit(() -> MainActivity.myAppDB.DaoEvaluaciones().getEvaluacionesByACObliga(Integer.parseInt(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""))));
        try {
            List<Evaluaciones> evs = evas.get();

            if (evs.isEmpty()) {
                Utilidades.avisoListo(activity, "Falta algo", "Antes de guardar debes agregar una recomendacion para esta visita. ( estrella de arriba a la derecha )", "entiendo");
                return;
            }

            if (sp_fenologico.getSelectedItem().toString().equals("MONITOREO")) {
                saveVisitaMonitoreo();
            } else {
                saveVisitaNormal();
            }

        } catch (ExecutionException | InterruptedException e) {
            Utilidades.avisoListo(activity, "Problema", "No se pudo obtener datos de la evaluacion, por favor, vuelva a abrir y nuevamente la evaluacion." + e.getMessage(), "entiendo");
        } finally {
            exec.shutdown();
        }
    }

    private void agregarImagenToAgronomos() {

        if (temp_visitas == null || rwAgronomo == null || activity == null) {
            return;
        }


        LinearLayoutManager lManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        rwAgronomo.setHasFixedSize(true);
        rwAgronomo.setLayoutManager(lManager);

        ExecutorService ex = Executors.newSingleThreadExecutor();
        try {

            Config config = ex.submit(() -> MainActivity.myAppDB.myDao().getConfig()).get();
            int idDispo = (config == null) ? 0 : config.getId();


            Visitas visitas = ex.submit(() -> MainActivity.myAppDB.myDao().getVisitas(temp_visitas.getId_temp_visita())).get();

            int visitaServidor = (visitas == null) ? 0 : (visitas.getEstado_server_visitas() == 1) ? prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0) : 0;


            List<Fotos> myImageList = ex.submit(() -> MainActivity.myAppDB.myDao().getFotosByFieldAndViewVisitas(2, temp_visitas.getId_anexo_temp_visita(), temp_visitas.getId_visita_local(), visitaServidor, idDispo)).get();


            adapterAgronomo = new FotosListAdapter(myImageList, activity, this::showAlertForUpdate, fotos -> {

                if (temp_visitas.getAction_temp_visita() == 2) {
                    Utilidades.avisoListo(activity, getResources().getString(R.string.title_dialog_agron), "Visita en estado terminado, no puedes cambiar el estado de las fotos.", "entiendo");
                } else {
                    if (!fotos.isFavorita()) {
                        ExecutorService dd = Executors.newSingleThreadExecutor();
                        try {
                            int favoritas = dd.submit(() -> MainActivity.myAppDB.myDao().getCantFavoritasByFieldbookFichaAndVista(0, temp_visitas.getId_anexo_temp_visita(), 2, temp_visitas.getId_temp_visita())).get();
                            if (favoritas < 3) {
                                cambiarFavorita(fotos);
                            } else {
                                Utilidades.avisoListo(activity, getResources().getString(R.string.title_dialog_fav), getResources().getString(R.string.message_dialog_fav), getResources().getString(R.string.message_dialog_btn_ok));
                            }
                        } catch (ExecutionException | InterruptedException e) {
                            throw new RuntimeException(e);
                        } finally {
                            dd.shutdown();
                        }
                    } else {
                        cambiarFavorita(fotos);
                    }
                }
            });
            rwAgronomo.setAdapter(adapterAgronomo);

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            ex.shutdown();
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

            adapterRaices = new FotosListAdapter(myImageList, activity, this::showAlertForUpdate, photo -> {
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


    private void preguntarFotoRaices() {

        if (sp_fenologico.getSelectedItem().toString().equals("MONITOREO")) {
            Toasty.error(requireActivity(), "Foto Raiz deshabilitada para monitoreo", Toast.LENGTH_LONG, true).show();
            return;
        }

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
                        agregarImagenToRaices();
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
                    agregarImagenToRaices();
                    Toasty.success(activity, "Foto eliminada con exito", Toast.LENGTH_SHORT, true).show();
                    builder.dismiss();

                } catch (Exception e) {
                    MainActivity.myAppDB.myDao().deleteFotos(foto);
                    agregarImagenToAgronomos();
                    agregarImagenToClientes();
                    agregarImagenToRaices();
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

    void cambiarSubtitulo() {

        AnexoCompleto anexoCompleto = MainActivity.myAppDB.myDao().getAnexoCompletoById(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));

        if (activity != null && anexoCompleto != null) {
            activity.updateView(getResources().getString(R.string.app_name), "Anexo " + anexoCompleto.getAnexoContrato().getAnexo_contrato());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        activity.updateView(getResources().getString(R.string.app_name), "Anexo ");
        cambiarSubtitulo();

    }
}
