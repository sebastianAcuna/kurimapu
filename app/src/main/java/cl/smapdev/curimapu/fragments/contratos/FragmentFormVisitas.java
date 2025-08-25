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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import cl.smapdev.curimapu.coroutines.ApplicationExecutors;
import cl.smapdev.curimapu.fragments.FragmentVisitas;
import cl.smapdev.curimapu.fragments.dialogos.DialogObservationTodo;
import es.dmoral.toasty.Toasty;

public class FragmentFormVisitas extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, View.OnFocusChangeListener {

    private Spinner sp_fenologico, sp_cosecha, sp_crecimiento, sp_fito, sp_general_cultivo, sp_humedad, sp_malezas;

    private Button btn_guardar, btn_volver;

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
    ;

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

        fenologico.addAll(Arrays.asList(getResources().getStringArray(R.array.fenologico)));
        cosecha.addAll(Arrays.asList(getResources().getStringArray(R.array.cosecha)));
        crecimiento.addAll(Arrays.asList(getResources().getStringArray(R.array.crecimiento)));
        maleza.addAll(Arrays.asList(getResources().getStringArray(R.array.maleza)));

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
        setHasOptionsMenu(true);


        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            if (!checkPermission()) {
                requestPermission();
            }
        }

        ApplicationExecutors exec = new ApplicationExecutors();

        //hilo principal


        exec.getBackground().execute(() -> {
            if (temp_visitas == null) {
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
        if (item.getItemId() == R.id.menu_visitas_recom) {
            ExecutorService executor = Executors.newSingleThreadExecutor();

            TempVisitas tmp = null;
            VisitasCompletas visitasCompletas = null;
            AnexoContrato ac = null;
            try {
                Future<TempVisitas> temp_visitasF = executor.submit(() -> MainActivity.myAppDB.myDao().getTempFichas());

                TempVisitas temp_visitas = temp_visitasF.get();
                Future<VisitasCompletas> visitasCompletasFuture = executor.submit(() -> MainActivity.myAppDB.myDao().getUltimaVisitaByAnexo(temp_visitas.getId_anexo_temp_visita()));

                if (temp_visitas != null && temp_visitas.getAction_temp_visita() != 2) {
                    tmp = temp_visitas;
                    visitasCompletas = visitasCompletasFuture.get();
                }

                Future<AnexoContrato> anexo = executor.submit(() -> MainActivity.myAppDB.myDao().getAnexos(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, "")));
                ac = anexo.get();

                FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                Fragment prev = requireActivity().getSupportFragmentManager().findFragmentByTag("EVALUACION_RECOMENDACION");
                if (prev != null) {
                    ft.remove(prev);
                }

                DialogObservationTodo dialogo = DialogObservationTodo.newInstance(ac, tmp, visitasCompletas, this::cargarTemp);
                dialogo.show(ft, "EVALUACION_RECOMENDACION");
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            executor.shutdown();
            return true;
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
                Future<VisitasCompletas> visitasCompletasFuture = executor.submit(() -> MainActivity.myAppDB.myDao().getUltimaVisitaByAnexo(temp_visitas.getId_anexo_temp_visita()));
                VisitasCompletas visitasCompletas = null;
                AnexoContrato anexoContrato = null;
                visitasCompletas = visitasCompletasFuture.get();

                if (visitasCompletas != null) {
                    anexoContrato = visitasCompletas.getAnexoCompleto().getAnexoContrato();
                } else {
                    Future<AnexoContrato> anexoContratoFuture = executor.submit(() -> MainActivity.myAppDB.myDao().getAnexos(temp_visitas.getId_anexo_temp_visita()));
                    anexoContrato = anexoContratoFuture.get();
                }

                FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                Fragment prev = requireActivity().getSupportFragmentManager().findFragmentByTag("EVALUACION_RECOMENDACION");
                if (prev != null) {
                    ft.remove(prev);
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
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            executor.shutdown();
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

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toasty.normal(requireActivity(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 100);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (activity != null) {
            temp_visitas = MainActivity.myAppDB.myDao().getTempFichas();
            agregarImagenToAgronomos();
            agregarImagenToClientes();
        }
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
            if (activity != null) {
                temp_visitas = MainActivity.myAppDB.myDao().getTempFichas();
                accionSpinners();
                agregarImagenToAgronomos();
                agregarImagenToClientes();

            }
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

        btn_volver = view.findViewById(R.id.btn_volver);

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
                    temp_visitas.setPhenological_state_temp_visita(fenologico.get(i));
                    temp_visitas.setEtapa_temp_visitas(Utilidades.getPhenoState(sp_fenologico.getSelectedItemPosition()));
                    prefs.edit().putInt(Utilidades.SHARED_ETAPA_SELECTED, Utilidades.getPhenoState(sp_fenologico.getSelectedItemPosition())).apply();
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
            MainActivity.myAppDB.myDao().updateTempVisitas(temp_visitas);
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

            MainActivity.myAppDB.myDao().updateTempVisitas(temp_visitas);
        }
    }


    /* IMAGENES */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void abrirCamara(int vista, String medida) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//        if(intent.resolveActivity(requireActivity().getPackageManager()) == null) {
//            Toasty.error(requireActivity(), "No se pudo crear la imagen 1", Toast.LENGTH_LONG, true).show();
//            return;
//        };

        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            Log.e("ERROR IMAGEN", e.getLocalizedMessage());
            Toasty.error(requireActivity(), "No se pudo crear la imagen 2", Toast.LENGTH_LONG, true).show();
        }

        if (photoFile == null) {
            Toasty.error(requireActivity(), "No se pudo crear la imagen 3", Toast.LENGTH_LONG, true).show();
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

                Log.e("RUTA", currentPhotoPath);
                currentPhotoPath = currentPhotoPath.replaceAll("Pictures/", "");
                Log.e("RUTA", currentPhotoPath);

                File file = new File(currentPhotoPath);

                FileOutputStream fos = new FileOutputStream(file);
                nuevaFoto.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                guardarBD();

            } catch (Exception e) {
                Log.e("FOTOS", e.getLocalizedMessage());
                System.out.println(e);
            }
        }

    }


    private void guardarBD() {


        ApplicationExecutors exec = new ApplicationExecutors();
        exec.getBackground().execute(() -> {
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


    private void setOnSave() {

        if (temp_visitas == null) {
            Utilidades.avisoListo(activity, "Falta algo", "Algo salio mal, por favor revise el fieldbook", "entiendo");
            return;
        }


        ExecutorService exec = Executors.newSingleThreadExecutor();

        Future<VisitasCompletas> visitasCompletasFuture = exec.submit(() -> MainActivity.myAppDB.myDao().getUltimaVisitaByAnexo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, "")));
        Future<List<Evaluaciones>> evas = exec.submit(() -> MainActivity.myAppDB.DaoEvaluaciones().getEvaluacionesByACObliga(Integer.parseInt(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""))));
        try {
            VisitasCompletas cc = visitasCompletasFuture.get();
            List<Evaluaciones> evs = evas.get();

            if (evs.size() <= 0) {
                Utilidades.avisoListo(activity, "Falta algo", "Antes de guardar debes agregar una recomendacion para esta visita. ( estrella de arriba a la derecha )", "entiendo");
                return;
            }

            if ((temp_visitas.getEvaluacion() <= 0.0 || temp_visitas.getComentario_evaluacion().isEmpty()) && cc != null && cc.getVisitas() != null) {
                Utilidades.avisoListo(activity, "Falta algo", "Antes de guardar debes realizar la evaluacion de la visita anterior. ( estrella de arriba a la derecha )", "entiendo");
                return;
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }


        int fotosClientes = MainActivity.myAppDB.myDao().getCantFotos(temp_visitas.getId_anexo_temp_visita(), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0), 0);
        int fotosAgricultores = MainActivity.myAppDB.myDao().getCantFotos(temp_visitas.getId_anexo_temp_visita(), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0), 2);

        if (fotosClientes <= 0 || fotosAgricultores <= 0) {
            Utilidades.avisoListo(activity, "Falta algo", "Debes tomar al menos una foto cliente y agricultor", "entiendo");
            return;
        }

        int favsCliente = MainActivity.myAppDB.myDao().getCantFavoritasByFieldbookAndFicha(temp_visitas.getId_anexo_temp_visita(), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0), 0);
        int favsAgricultores = MainActivity.myAppDB.myDao().getCantFavoritasByFieldbookAndFicha(temp_visitas.getId_anexo_temp_visita(), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0), 2);

        if (favsCliente <= 0 || favsAgricultores <= 0) {
            Utilidades.avisoListo(activity, "Falta algo", "Debes seleccionar como favorita al menos una foto cliente y agricultor (manten presionada la foto para marcar)", "entiendo");
            return;
        }

        boolean entra = (sp_malezas.getSelectedItemPosition() == 3 && TextUtils.isEmpty(et_obs_weed.getText()));
        if (!entra)
            entra = (sp_fito.getSelectedItemPosition() == 4 && TextUtils.isEmpty(et_obs_fito.getText()));
        if (!entra)
            entra = ((sp_cosecha.getSelectedItemPosition() == 1) && TextUtils.isEmpty(et_obs_harvest.getText()));
        if (!entra)
            entra = (sp_general_cultivo.getSelectedItemPosition() == 4 && TextUtils.isEmpty(et_obs_overall.getText()));

        if (entra) {
            Utilidades.avisoListo(activity, "Faltan cosas", "Debe completar todas las observaciones en donde su estado sea malo/alto/rechazado", "entiendo");
            return;
        }

        if (sp_fenologico.getSelectedItemPosition() <= 0 ||
                sp_malezas.getSelectedItemPosition() <= 0 ||
                sp_fito.getSelectedItemPosition() <= 0 ||
                sp_cosecha.getSelectedItemPosition() <= 0 ||
                sp_general_cultivo.getSelectedItemPosition() <= 0 ||
                sp_humedad.getSelectedItemPosition() <= 0) {
            Utilidades.avisoListo(activity, "Hey", "no puede dejar elementos en '--seleccione--'.", "entiendo");
            return;
        }

        if (TextUtils.isEmpty(et_percent_humedad.getText().toString()) || (Double.parseDouble(et_percent_humedad.getText().toString()) <= 0.0 || Double.parseDouble(et_percent_humedad.getText().toString()) > 200.0)) {
            Utilidades.avisoListo(activity, "Hey", "Debes ingresar un potencial de rendimiento sobre 0 y bajo 200 ", "entiendo");
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

        String obsCosecha = et_obs_harvest.getText().toString().toUpperCase();
        String obsCreci = et_obs_growth.getText().toString().toUpperCase();
        String obsFito = et_obs_fito.getText().toString().toUpperCase();
        String obsHumedad = et_obs_humedad.getText().toString().toUpperCase();
        String obsMaleza = et_obs_weed.getText().toString().toUpperCase();
        String obsOverall = et_obs_overall.getText().toString().toUpperCase();
        String etobs = et_obs.getText().toString().toUpperCase();


        for (int i = 0; i < forbiddenWords.length; i++) {
            obsCosecha = obsCosecha.replace(forbiddenWords[i], forbiddenReplacement[i]);
            obsCreci = obsCreci.replace(forbiddenWords[i], forbiddenReplacement[i]);
            obsFito = obsFito.replace(forbiddenWords[i], forbiddenReplacement[i]);
            obsHumedad = obsHumedad.replace(forbiddenWords[i], forbiddenReplacement[i]);
            obsMaleza = obsMaleza.replace(forbiddenWords[i], forbiddenReplacement[i]);
            obsOverall = obsOverall.replace(forbiddenWords[i], forbiddenReplacement[i]);
            etobs = etobs.replace(forbiddenWords[i], forbiddenReplacement[i]);

        }

        visitas.setObservation_visita((TextUtils.isEmpty(temp_visitas.getObservation_temp_visita()) ? etobs : temp_visitas.getObservation_temp_visita().toUpperCase()));


        visitas.setObs_cosecha((TextUtils.isEmpty(temp_visitas.getObs_cosecha())) ? obsCosecha : temp_visitas.getObs_cosecha().toUpperCase());
        visitas.setObs_creci((TextUtils.isEmpty(temp_visitas.getObs_creci())) ? obsCreci : temp_visitas.getObs_creci().toUpperCase());
        visitas.setObs_fito((TextUtils.isEmpty(temp_visitas.getObs_fito())) ? obsFito : temp_visitas.getObs_fito().toUpperCase());
        visitas.setObs_humedad((TextUtils.isEmpty(temp_visitas.getObs_humedad())) ? obsHumedad : temp_visitas.getObs_humedad().toUpperCase());
        visitas.setObs_maleza((TextUtils.isEmpty(temp_visitas.getObs_maleza())) ? obsMaleza : temp_visitas.getObs_maleza().toUpperCase());
        visitas.setObs_overall((TextUtils.isEmpty(temp_visitas.getObs_overall())) ? obsOverall : temp_visitas.getObs_overall().toUpperCase());

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

        showAlertForSave("Genial", "Se guardo todo como corresponde");
    }

    private void agregarImagenToAgronomos() {

        if (temp_visitas != null && rwAgronomo != null) {

            LinearLayoutManager lManager = null;
            if (activity != null) {
                lManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
            }

            rwAgronomo.setHasFixedSize(true);
            rwAgronomo.setLayoutManager(lManager);

            int idDispo = 0;
            Config config = MainActivity.myAppDB.myDao().getConfig();
            if (config != null) {
                idDispo = config.getId();
            }


            int visitaServidor = 0;

            Visitas visitas = MainActivity.myAppDB.myDao().getVisitas(temp_visitas.getId_temp_visita());
            if (visitas != null) {
                visitaServidor = (visitas.getEstado_server_visitas() == 1) ? prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0) : 0;
            }

            List<Fotos> myImageList = MainActivity.myAppDB.myDao().getFotosByFieldAndViewVisitas(2, temp_visitas.getId_anexo_temp_visita(), temp_visitas.getId_visita_local(), visitaServidor, idDispo);
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

    void cambiarSubtitulo() {

        AnexoCompleto anexoCompleto = MainActivity.myAppDB.myDao().getAnexoCompletoById(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));

        if (activity != null && anexoCompleto != null) {
            activity.updateView(getResources().getString(R.string.app_name), "Anexo " + anexoCompleto.getAnexoContrato().getAnexo_contrato());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cambiarSubtitulo();

    }
}
