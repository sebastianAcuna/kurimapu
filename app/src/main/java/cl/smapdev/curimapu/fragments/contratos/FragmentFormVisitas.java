package cl.smapdev.curimapu.fragments.contratos;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.FotosListAdapter;
import cl.smapdev.curimapu.clases.adapters.SpinnerAdapter;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.relaciones.VisitasCompletas;
import cl.smapdev.curimapu.clases.tablas.AnexoContrato;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Especie;
import cl.smapdev.curimapu.clases.tablas.Fotos;
import cl.smapdev.curimapu.clases.tablas.Visitas;
import cl.smapdev.curimapu.clases.temporales.TempVisitas;
import cl.smapdev.curimapu.clases.utilidades.CameraUtils;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.FragmentVisitas;
import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;

public class FragmentFormVisitas extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, View.OnFocusChangeListener {

    private Spinner sp_fenologico,sp_cosecha,sp_crecimiento,sp_fito,sp_general_cultivo,sp_humedad,sp_malezas;

    private Button btn_guardar, btn_volver;

    private MainActivity activity;
    private SharedPreferences prefs;

    private TempVisitas temp_visitas;

    private final ArrayList<String> fenologico = new ArrayList<>();
    private final ArrayList<String> cosecha = new ArrayList<>();
    private final ArrayList<String> crecimiento = new ArrayList<>();
    private final ArrayList<String> maleza = new ArrayList<>();

    private TextInputLayout obs_growth, obs_weed, obs_fito,obs_harvest,obs_overall, obs_humedad;
    private EditText et_obs, et_recomendacion, et_obs_growth, et_obs_weed, et_obs_fito, et_obs_harvest, et_obs_overall,et_obs_humedad,et_percent_humedad;

    /* IMAGENES */
    private RecyclerView rwAgronomo, rwCliente;

//    private static final String FIELDBOOKKEY = "fieldbookkey";
    private static final int COD_FOTO = 005;

    private File fileImagen;

    private int fieldbook;

    private FloatingActionButton material_private, material_public;

    private FotosListAdapter adapterAgronomo;
    private FotosListAdapter adapterCliente;

    private ProgressDialog progressBar;

    private final String[] forbiddenWords = new String[]{"á", "é", "í", "ó", "ú", "Á", "É", "Í", "Ó", "Ú"};
    private final String[] forbiddenReplacement = new String[]{"a", "e", "i", "o", "u", "A", "E", "I", "O", "U"};


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity a = (MainActivity) getActivity();
        if ( a != null) activity = a;

        prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);

        fenologico.addAll(Arrays.asList(getResources().getStringArray(R.array.fenologico)));
        cosecha.addAll(Arrays.asList(getResources().getStringArray(R.array.cosecha)));
        crecimiento.addAll(Arrays.asList(getResources().getStringArray(R.array.crecimiento)));
        maleza.addAll(Arrays.asList(getResources().getStringArray(R.array.maleza)));


        progressBar = new ProgressDialog(activity);
        progressBar.setTitle(getResources().getString(R.string.espere));
        progressBar.show();
//        crecimiento = getResources().getStringArray(R.array.crecimiento);
//        maleza = getResources().getStringArray(R.array.maleza);

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

       new LazyLoad().execute();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (!checkPermission()) {
                requestPermission();
            }
        }

    }


    private void chargeAll(){


        cargarSpinners();

        material_private.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int cantidaAgr = MainActivity.myAppDB.myDao().getCantAgroByFieldViewAndFicha(0, 2, temp_visitas.getId_anexo_temp_visita(), temp_visitas.getId_temp_visita());
                if (cantidaAgr > 2){
                    Utilidades.avisoListo(activity,getResources().getString(R.string.title_dialog_agron),getResources().getString(R.string.message_dialog_agron),getResources().getString(R.string.message_dialog_btn_ok));
                }else{
                    if (temp_visitas.getAction_temp_visita() == 2){
                        Utilidades.avisoListo(activity,getResources().getString(R.string.title_dialog_agron),getResources().getString(R.string.visitas_terminadas),getResources().getString(R.string.entiendo));
                    }else{
                        Utilidades.hideKeyboard(activity);
//                        activity.cambiarFragmentFoto(FragmentTakePicture.getInstance(0, 2), Utilidades.FRAGMENT_TAKE_PHOTO, R.anim.slide_in_left,R.anim.slide_out_left);
                        abrirCamara(2);
                    }
                }
            }
        });
        material_public.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (sp_fenologico.getSelectedItemPosition() > 0){
                    if (temp_visitas.getAction_temp_visita() == 2){
                        Utilidades.avisoListo(activity,getResources().getString(R.string.title_dialog_agron),getResources().getString(R.string.visitas_terminadas),getResources().getString(R.string.entiendo));
                    }else{
                        Utilidades.hideKeyboard(activity);
//                        activity.cambiarFragmentFoto(FragmentTakePicture.getInstance((prefs.getInt(Utilidades.VISTA_FOTOS,0) == 2) ? 0 : Utilidades.getPhenoState(sp_fenologico.getSelectedItemPosition()), 0), Utilidades.FRAGMENT_TAKE_PHOTO, R.anim.slide_in_left,R.anim.slide_out_left);
                        abrirCamara(0);
                    }

                }else{
                    Utilidades.avisoListo(activity, getResources().getString(R.string.falta_algo), getResources().getString(R.string.pheno_first),getResources().getString(R.string.entiendo));
                }

            }
        });
    }

    /*
    * GENERA LOS ONITEMSELECTEDLISTENER DE LOS SPINNERS
    * */
    private void accionSpinners(){
        if (temp_visitas != null){

            sp_fenologico.setSelection(fenologico.indexOf(temp_visitas.getPhenological_state_temp_visita()));
            sp_cosecha.setSelection(cosecha.indexOf(temp_visitas.getHarvest_temp_visita()));
            sp_crecimiento.setSelection(crecimiento.indexOf(temp_visitas.getGrowth_status_temp_visita()));
            sp_fito.setSelection(crecimiento.indexOf(temp_visitas.getPhytosanitary_state_temp_visita()));
            sp_general_cultivo.setSelection(crecimiento.indexOf(temp_visitas.getOverall_status_temp_visita()));
            sp_humedad.setSelection(crecimiento.indexOf(temp_visitas.getHumidity_floor_temp_visita()));
            sp_malezas.setSelection(maleza.indexOf(temp_visitas.getWeed_state_temp_visita()));

            et_obs.setText(temp_visitas.getObservation_temp_visita());
            et_recomendacion.setText(temp_visitas.getRecomendation_temp_visita());

            et_obs_harvest.setText(temp_visitas.getObs_cosecha());
            et_obs_overall.setText(temp_visitas.getObs_overall());
            et_obs_humedad.setText(temp_visitas.getObs_humedad());
            et_obs_weed.setText(temp_visitas.getObs_maleza());
            et_obs_fito.setText(temp_visitas.getObs_fito());
            et_obs_growth.setText(temp_visitas.getObs_creci());
            et_percent_humedad.setText(String.valueOf(temp_visitas.getPercent_humedad()));


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


            if (temp_visitas.getAction_temp_visita() == 2){
                sp_fenologico.setEnabled(false);
                sp_cosecha.setEnabled(false);
                sp_crecimiento.setEnabled(false);
                sp_fito.setEnabled(false);
                sp_general_cultivo.setEnabled(false);
                sp_humedad.setEnabled(false);
                sp_malezas.setEnabled(false);
                et_obs.setEnabled(false);
                et_recomendacion.setEnabled(false);
                btn_guardar.setEnabled(false);

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
        int result = ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED ;
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toasty.normal(getActivity(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 100);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (activity != null){
            //activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fotos_resumen, FragmentFotosResumen.getInstance(0), Utilidades.FRAGMENT_FOTOS_RESUMEN).commit();

            agregarImagenToAgronomos();
            agregarImagenToClientes();



            if (temp_visitas != null && temp_visitas.getAction_temp_visita() != 2 &&  temp_visitas.getEvaluacion() <= 0.0){

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        final VisitasCompletas visitaAnterior = MainActivity.myAppDB.myDao().getUltimaVisitaByAnexo(temp_visitas.getId_anexo_temp_visita());

                        if (visitaAnterior != null && visitaAnterior.getVisitas() != null){
                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showAlertForRankear(visitaAnterior.getVisitas(), temp_visitas);
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    private void cargarSpinners(){

        sp_fenologico.setAdapter(new SpinnerAdapter(activity,R.layout.spinner_template_view, fenologico));
        sp_cosecha.setAdapter(new SpinnerAdapter(activity,R.layout.spinner_template_view, cosecha));
        sp_crecimiento.setAdapter(new SpinnerAdapter(activity,R.layout.spinner_template_view, crecimiento));
        sp_fito.setAdapter(new SpinnerAdapter(activity,R.layout.spinner_template_view, crecimiento));
        sp_general_cultivo.setAdapter(new SpinnerAdapter(activity,R.layout.spinner_template_view, crecimiento));
        sp_humedad.setAdapter(new SpinnerAdapter(activity,R.layout.spinner_template_view, crecimiento));
        sp_malezas.setAdapter(new SpinnerAdapter(activity,R.layout.spinner_template_view, maleza));

        accionSpinners();

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (activity != null){
                //activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fotos_resumen, FragmentFotosResumen.getInstance(0), Utilidades.FRAGMENT_FOTOS_RESUMEN).commit();
                temp_visitas  = (temp_visitas != null) ? temp_visitas : MainActivity.myAppDB.myDao().getTempFichas();
                accionSpinners();
                agregarImagenToAgronomos();
                agregarImagenToClientes();

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_guardar:

                if (activity != null){
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

    private void bind(View view){
        sp_fenologico = view.findViewById(R.id.sp_feno);
        sp_cosecha = view.findViewById(R.id.sp_cosecha);
        sp_crecimiento = view.findViewById(R.id.sp_crecimiento);
        sp_fito = view.findViewById(R.id.sp_fito);
        sp_general_cultivo = view.findViewById(R.id.sp_general_cultivo);
        sp_humedad = view.findViewById(R.id.sp_humedad);
        sp_malezas = view.findViewById(R.id.sp_malezas);
        btn_guardar = view.findViewById(R.id.btn_guardar);

        et_obs = view.findViewById(R.id.et_obs);
        et_recomendacion = view.findViewById(R.id.et_recomendacion);
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

        btn_volver = view.findViewById(R.id.btn_volver);

        sp_fenologico.setOnItemSelectedListener(this);
        sp_malezas.setOnItemSelectedListener(this);
        sp_humedad.setOnItemSelectedListener(this);
        sp_general_cultivo.setOnItemSelectedListener(this);
        sp_fito.setOnItemSelectedListener(this);
        sp_crecimiento.setOnItemSelectedListener(this);
        sp_cosecha.setOnItemSelectedListener(this);

        et_recomendacion.setOnFocusChangeListener(this);
        et_obs.setOnFocusChangeListener(this);
        et_obs_growth.setOnFocusChangeListener(this);
        et_obs_weed.setOnFocusChangeListener(this);
        et_obs_overall.setOnFocusChangeListener(this);
        et_obs_harvest.setOnFocusChangeListener(this);
        et_obs_fito.setOnFocusChangeListener(this);
        et_obs_humedad.setOnFocusChangeListener(this);
        et_percent_humedad.setOnFocusChangeListener(this);


        btn_volver.setOnClickListener(this);
        btn_guardar.setOnClickListener(this);


        rwAgronomo = view.findViewById(R.id.fotos_agronomos);
        rwCliente = view.findViewById(R.id.fotos_clientes);

        material_private = view.findViewById(R.id.material_private);
        material_public = view.findViewById(R.id.material_public);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i > 0) {
            switch (adapterView.getId()) {
                case R.id.sp_feno:
                    temp_visitas.setPhenological_state_temp_visita(fenologico.get(i));
                    temp_visitas.setEtapa_temp_visitas(Utilidades.getPhenoState(sp_fenologico.getSelectedItemPosition()));

                    prefs.edit().putInt(Utilidades.SHARED_ETAPA_SELECTED, Utilidades.getPhenoState(sp_fenologico.getSelectedItemPosition())).apply();

//                    cambiarSubtitulo("Estado fenologico : "+sp_fenologico.getSelectedItem());
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
        }else{
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

    String  quitarTildes(String texto){
        String nuevoTexto = texto;
        if (!TextUtils.isEmpty(texto)){
            for (int i = 0; i < forbiddenWords.length; i++){
                nuevoTexto = texto.replace(forbiddenWords[i],forbiddenReplacement[i]);
            }
        }

        return nuevoTexto;
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        String text = "";
        if (!b){
            switch (view.getId()){
                case R.id.et_obs:
                    temp_visitas.setObservation_temp_visita(quitarTildes(et_obs.getText().toString().toUpperCase()));
                    break;
                case R.id.et_recomendacion:
                    text = et_recomendacion.getText().toString().toUpperCase();
                    for (int i = 0; i < forbiddenWords.length; i++){
                        text = text.replace(forbiddenWords[i],forbiddenReplacement[i]);
                    }
                    temp_visitas.setRecomendation_temp_visita(text);
                    break;
                case R.id.et_obs_growth:
                    text = et_obs_growth.getText().toString().toUpperCase();

                    for (int i = 0; i < forbiddenWords.length; i++){
                        text = text.replace(forbiddenWords[i],forbiddenReplacement[i]);
                    }
                    temp_visitas.setObs_creci(text);
                    break;
                case R.id.et_obs_weed:
                    text = et_obs_weed.getText().toString().toUpperCase();

                    for (int i = 0; i < forbiddenWords.length; i++){
                        text = text.replace(forbiddenWords[i],forbiddenReplacement[i]);
                    }
                    temp_visitas.setObs_maleza(text);
                    break;
                case R.id.et_obs_fito:
                    text = et_obs_fito.getText().toString().toUpperCase();

                    for (int i = 0; i < forbiddenWords.length; i++){
                        text = text.replace(forbiddenWords[i],forbiddenReplacement[i]);
                    }
                    temp_visitas.setObs_fito(text);
                    break;
                case R.id.et_obs_harvest:
                    text = et_obs_harvest.getText().toString().toUpperCase();

                    for (int i = 0; i < forbiddenWords.length; i++){
                        text = text.replace(forbiddenWords[i],forbiddenReplacement[i]);
                    }
                    temp_visitas.setObs_cosecha(text);
                    break;
                case R.id.et_obs_overall:
                    text = et_obs_overall.getText().toString().toUpperCase();

                    for (int i = 0; i < forbiddenWords.length; i++){
                        text = text.replace(forbiddenWords[i],forbiddenReplacement[i]);
                    }
                    temp_visitas.setObs_overall(text);
                    break;
                case R.id.et_obs_humedad:
                    text = et_obs_humedad.getText().toString().toUpperCase();

                    for (int i = 0; i < forbiddenWords.length; i++){
                        text = text.replace(forbiddenWords[i],forbiddenReplacement[i]);
                    }
                    temp_visitas.setObs_humedad(text);
                    break;
                case R.id.et_percent_humedad:
                    try{
                        temp_visitas.setPercent_humedad(Double.parseDouble(et_percent_humedad.getText().toString()));
                    }catch (Exception e){
                        temp_visitas.setPercent_humedad(0);
                        Toasty.warning(activity, "No se pudo guardar el porcentaje, formato incorrecto.", Toast.LENGTH_SHORT, true).show();
                    }
                    break;
            }

            MainActivity.myAppDB.myDao().updateTempVisitas(temp_visitas);
        }
    }


    /* IMAGENES */

    private void abrirCamara(int vista){

        prefs.edit().remove(Utilidades.VISTA_FOTOS).apply();

        File miFile = new File(Environment.getExternalStoragePublicDirectory("DCIM"), Utilidades.DIRECTORIO_IMAGEN);
        boolean isCreada = miFile.exists();

        if (!isCreada){
            isCreada=miFile.mkdirs();
        }

        if(isCreada){

            long consecutivo = System.currentTimeMillis()/1000;
            String nombre = consecutivo+"_"+vista+"_0.jpg";
            String path = Environment.getExternalStoragePublicDirectory("DCIM") + File.separator + Utilidades.DIRECTORIO_IMAGEN + File.separator + nombre;

            fileImagen=new File(path);

            prefs.edit().putInt(Utilidades.VISTA_FOTOS, vista).apply();

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            startActivityForResult(intent, COD_FOTO);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == COD_FOTO && resultCode == RESULT_OK) {

            if (fileImagen != null) {

                Bitmap bm = BitmapFactory.decodeFile(fileImagen.getAbsolutePath());
                Integer[] inte = Utilidades.neededRotation(Uri.fromFile(fileImagen));

                int rotation = inte[1];
                int rotationInDegrees = inte[0];

                Matrix m = new Matrix();
                if (rotation != 0) {
                    m.preRotate(rotationInDegrees);
                }


            if (bm != null){
                Bitmap src = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);

                ByteArrayOutputStream bos = null;
                try {
                    bos = new ByteArrayOutputStream();
                    CameraUtils.escribirFechaImg(src, activity).compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    byte[] bitmapdata = bos.toByteArray();

                    FileOutputStream fos = new FileOutputStream(fileImagen.getAbsoluteFile());
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();

                } catch (IOException e) {
                    Log.e("ERROR -- FOTOS", e.getLocalizedMessage());
                }
            }
                guardarBD(fileImagen);
            }

        }
    }


    private void guardarBD(File path){

        Fotos fotos = new Fotos();
        fotos.setFecha(Utilidades.fechaActualConHora());


        fotos.setFieldbook((prefs.getInt(Utilidades.VISTA_FOTOS,0) == 2) ? 0 : Utilidades.getPhenoState(sp_fenologico.getSelectedItemPosition()));
        fotos.setHora(Utilidades.hora());
        fotos.setNombre_foto(path.getName());
        fotos.setFavorita(false);
        fotos.setPlano(0);

        if(temp_visitas != null){
            fotos.setId_ficha_fotos(temp_visitas.getId_anexo_temp_visita());
        }
        fotos.setVista(prefs.getInt(Utilidades.VISTA_FOTOS, 0));
        fotos.setRuta(path.getAbsolutePath());


        fotos.setId_visita_foto(prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0));


        Config config = MainActivity.myAppDB.myDao().getConfig();
        if (config != null){
            fotos.setId_dispo_foto(config.getId());
        }

        MainActivity.myAppDB.myDao().insertFotos(fotos);

        if (adapterAgronomo != null){
            adapterAgronomo.notifyDataSetChanged();
        }

        if (adapterCliente != null){
            adapterCliente.notifyDataSetChanged();
        }

    }


    private void setOnSave(){
        if (temp_visitas != null) {
            int fotosClientes = MainActivity.myAppDB.myDao().getCantFotos(temp_visitas.getId_anexo_temp_visita(), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0) ,0);
            int fotosAgricultores = MainActivity.myAppDB.myDao().getCantFotos(temp_visitas.getId_anexo_temp_visita(), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0) ,2);
            if (fotosClientes <= 0 || fotosAgricultores <= 0) {
                Utilidades.avisoListo(activity, "Falta algo", "Debes tomar al menos una foto cliente y agricultor", "entiendo");
            } else {
                int favsCliente = MainActivity.myAppDB.myDao().getCantFavoritasByFieldbookAndFicha(temp_visitas.getId_anexo_temp_visita(), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0), 0);
                int favsAgricultores = MainActivity.myAppDB.myDao().getCantFavoritasByFieldbookAndFicha(temp_visitas.getId_anexo_temp_visita(), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0), 2);
                if (favsCliente <= 0 || favsAgricultores <= 0) {
                    Utilidades.avisoListo(activity, "Falta algo", "Debes seleccionar como favorita al menos una foto cliente y agricultor (manten presionada la foto para marcar)", "entiendo");
                } else {


                        boolean entra;


//                       entra = (sp_crecimiento.getSelectedItemPosition() > 3 && TextUtils.isEmpty(et_obs_growth.getText()));
                         entra = (sp_malezas.getSelectedItemPosition() == 3 && TextUtils.isEmpty(et_obs_weed.getText()));
                        if(!entra)  entra = (sp_fito.getSelectedItemPosition() == 4 && TextUtils.isEmpty(et_obs_fito.getText()));
                        if(!entra)  entra = ((sp_cosecha.getSelectedItemPosition() == 1 ) && TextUtils.isEmpty(et_obs_harvest.getText()));
                        if(!entra)  entra = (sp_general_cultivo.getSelectedItemPosition() == 4 && TextUtils.isEmpty(et_obs_overall.getText()));


                         if(!entra){

                             if(sp_fenologico.getSelectedItemPosition() <= 0 || sp_malezas.getSelectedItemPosition() <= 0  || sp_fito.getSelectedItemPosition() <= 0
                                     || sp_cosecha.getSelectedItemPosition() <= 0 || sp_general_cultivo.getSelectedItemPosition() <= 0 || sp_humedad.getSelectedItemPosition() <= 0){
                                 Utilidades.avisoListo(activity, "Hey", "no puede dejar elementos en '--seleccione--'.", "entiendo");
                             }else{

                                 if(TextUtils.isEmpty(et_percent_humedad.getText().toString()) || (Double.parseDouble(et_percent_humedad.getText().toString()) <= 0.0 || Double.parseDouble(et_percent_humedad.getText().toString()) > 200.0)){
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



                                 double percent =  0.0;
                                 try{
                                     percent = (temp_visitas.getPercent_humedad() <= 0.0 ) ? Double.parseDouble(et_percent_humedad.getText().toString()) : temp_visitas.getPercent_humedad();
                                 }catch(Exception e){
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
                                 String etrecom = et_recomendacion.getText().toString().toUpperCase();


                                 for (int i = 0; i < forbiddenWords.length; i++){
                                     obsCosecha = obsCosecha.replace(forbiddenWords[i],forbiddenReplacement[i]);
                                     obsCreci = obsCreci.replace(forbiddenWords[i],forbiddenReplacement[i]);
                                     obsFito = obsFito.replace(forbiddenWords[i],forbiddenReplacement[i]);
                                     obsHumedad = obsHumedad.replace(forbiddenWords[i],forbiddenReplacement[i]);
                                     obsMaleza = obsMaleza.replace(forbiddenWords[i],forbiddenReplacement[i]);
                                     obsOverall = obsOverall.replace(forbiddenWords[i],forbiddenReplacement[i]);
                                     etobs = etobs.replace(forbiddenWords[i],forbiddenReplacement[i]);
                                     etrecom = etrecom.replace(forbiddenWords[i],forbiddenReplacement[i]);

                                 }

                                 visitas.setObservation_visita((TextUtils.isEmpty(temp_visitas.getObservation_temp_visita()) ? etobs : temp_visitas.getObservation_temp_visita().toUpperCase()));
                                 visitas.setRecomendation_visita((TextUtils.isEmpty(temp_visitas.getRecomendation_temp_visita()) ? etrecom : temp_visitas.getRecomendation_temp_visita().toUpperCase()));


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


                                 long idVisita = 0;

                                 if (prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0)  > 0 ){

                                     idVisita = prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0);
                                     visitas.setId_visita((int) idVisita);
                                     visitas.setId_visita_local((int) idVisita);
                                     MainActivity.myAppDB.myDao().updateVisita(visitas);

                                 }else{
                                     idVisita = MainActivity.myAppDB.myDao().setVisita(visitas);
                                 }

                                 MainActivity.myAppDB.myDao().updateFotosWithVisita((int) idVisita, temp_visitas.getId_anexo_temp_visita());
                                 MainActivity.myAppDB.myDao().updateDetallesToVisits((int) idVisita);

                                 Visitas visitas1 = MainActivity.myAppDB.myDao().getVisitas((int) idVisita);
                                 visitas1.setId_visita_local((int) idVisita);
                                 MainActivity.myAppDB.myDao().updateVisita(visitas1);


                                 showAlertForSave("Genial", "Se guardo todo como corresponde");
                             }

                         }else {
                             Utilidades.avisoListo(activity, "Faltan cosas", "Debe completar todas las observaciones en donde su estado sea malo/alto/rechazado", "entiendo");
                         }

                    }
                }
        }else{
            Utilidades.avisoListo(activity, "Falta algo", "Algo salio mal, por favor revise el fieldbook", "entiendo");
        }
    }




    private class LazyLoad extends AsyncTask<Void, Void, Void>{


        @Override
        protected void onPreExecute() {


            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if (temp_visitas == null){
                temp_visitas = new TempVisitas();
                if (prefs != null){
                    temp_visitas.setId_anexo_temp_visita(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
                    temp_visitas.setId_temp_visita(0);
                }
                MainActivity.myAppDB.myDao().setTempVisitas(temp_visitas);
                temp_visitas = MainActivity.myAppDB.myDao().getTempFichas();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            chargeAll();

            if (progressBar != null && progressBar.isShowing()){
                progressBar.dismiss();
            }
        }
    }


    private void agregarImagenToAgronomos(){

        if (temp_visitas != null && rwAgronomo != null){

            LinearLayoutManager lManager = null;
            if (activity != null){
                lManager  = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
            }

            rwAgronomo.setHasFixedSize(true);
            rwAgronomo.setLayoutManager(lManager);

            int idDispo = 0;
            Config config = MainActivity.myAppDB.myDao().getConfig();
            if (config != null){
                idDispo = config.getId();
            }


            int visitaServidor = 0;

            Visitas visitas = MainActivity.myAppDB.myDao().getVisitas(temp_visitas.getId_temp_visita());
            if(visitas != null){
                visitaServidor = (visitas.getEstado_server_visitas() == 1) ? prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0) : 0;
            }

            List<Fotos> myImageList = MainActivity.myAppDB.myDao().getFotosByFieldAndViewVisitas( 2, temp_visitas.getId_anexo_temp_visita(), temp_visitas.getId_visita_local(), visitaServidor, idDispo);
            adapterAgronomo = new FotosListAdapter(myImageList,activity, new FotosListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Fotos fotos) {
                    showAlertForUpdate(fotos);
                }
            }, new FotosListAdapter.OnItemLongClickListener() {
                @Override
                public void onItemLongClick(Fotos fotos) {

                    if (temp_visitas.getAction_temp_visita() == 2){
                        Utilidades.avisoListo(activity,getResources().getString(R.string.title_dialog_agron),"Visita en estado terminado, no puedes cambiar el estado de las fotos.","entiendo");
                    }else{
                        if (!fotos.isFavorita()){
                            int favoritas = MainActivity.myAppDB.myDao().getCantFavoritasByFieldbookFichaAndVista(0, temp_visitas.getId_anexo_temp_visita(),2, temp_visitas.getId_temp_visita());

                            if (favoritas < 3){
                                cambiarFavorita(fotos);
                            }else{
                                Utilidades.avisoListo(activity,getResources().getString(R.string.title_dialog_fav),getResources().getString(R.string.message_dialog_fav),getResources().getString(R.string.message_dialog_btn_ok));
                            }
                        }else{
                            cambiarFavorita(fotos);
                        }
                    }


                }
            });
            rwAgronomo.setAdapter(adapterAgronomo);
        }
    }



    private void cambiarFavorita(Fotos fotos){

        if (fotos.isFavorita()){
            Toasty.info(activity, getResources().getString(R.string.message_fav_remove), Toast.LENGTH_SHORT, true).show();
            fotos.setFavorita(false);
        }else{
            Toasty.info(activity, getResources().getString(R.string.message_fav), Toast.LENGTH_SHORT, true).show();
            fotos.setFavorita(true);
        }


        MainActivity.myAppDB.myDao().updateFavorita(fotos);
        if (adapterAgronomo != null){
            adapterAgronomo.notifyDataSetChanged();
        }

        if (adapterCliente != null){
            adapterCliente.notifyDataSetChanged();
        }

    }



    private void agregarImagenToClientes(){

        if (temp_visitas != null && rwCliente != null) {

            LinearLayoutManager lManager = null;
            if (activity != null) {
//            lManager = new GridLayoutManager(activity, 1);
                lManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);

            }

            int idDispo = 0;
            Config config = MainActivity.myAppDB.myDao().getConfig();
            if (config != null){
                idDispo = config.getId();
            }

            rwCliente.setHasFixedSize(true);
            rwCliente.setLayoutManager(lManager);

            int visitaServidor = 0;

            Visitas visitas = MainActivity.myAppDB.myDao().getVisitas(temp_visitas.getId_temp_visita());
            if(visitas != null){
                visitaServidor = (visitas.getEstado_server_visitas() == 1) ? prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0) : 0;
            }

            List<Fotos> myImageList = MainActivity.myAppDB.myDao().getFotosByFieldAndView(0, temp_visitas.getId_anexo_temp_visita(),temp_visitas.getId_visita_local() , visitaServidor,idDispo );
//        int[] myImageList = new int[]{R.drawable.f1, R.drawable.f2,R.drawable.f3, R.drawable.f4,R.drawable.f5 };

//            if (myImageList.size() > 0) sp_fenologico.setEnabled(false);
//            else{sp_fenologico.setEnabled(false);}


            adapterCliente = new FotosListAdapter(myImageList, activity, new FotosListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Fotos fotos) {
                    showAlertForUpdate(fotos);
                }
            }, new FotosListAdapter.OnItemLongClickListener() {
                @Override
                public void onItemLongClick(Fotos fotos) {
                    if (temp_visitas.getAction_temp_visita() == 2) {
                        Utilidades.avisoListo(activity, getResources().getString(R.string.title_dialog_agron), "Visita en estado terminado, no puedes cambiar el estado de las fotos.", "entiendo");
                    } else {
                        if (!fotos.isFavorita()) {
                            int favoritas = MainActivity.myAppDB.myDao().getCantFavoritasByFieldbookFichaAndVista(fieldbook, temp_visitas.getId_anexo_temp_visita(), 0, temp_visitas.getId_temp_visita());

                            if (favoritas < 3) {
                                cambiarFavorita(fotos);
                            } else {
                                Utilidades.avisoListo(activity, getResources().getString(R.string.title_dialog_fav), getResources().getString(R.string.message_dialog_fav), getResources().getString(R.string.message_dialog_btn_ok));
                            }
                        } else {
                            cambiarFavorita(fotos);
                        }
                    }
                }
            });


            rwCliente.setAdapter(adapterCliente);
        }
    }


    public void  preguntarSiQuiereVolver ( String title, String message) {
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_empty, null);

        final AlertDialog builder = new AlertDialog.Builder(activity)
                .setView(viewInfalted)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("aceptar", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();

        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                Button c = builder.getButton(AlertDialog.BUTTON_NEGATIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MainActivity activity = (MainActivity) getActivity();
                        if (activity != null){
                            activity.cambiarFragment(new FragmentVisitas(), Utilidades.FRAGMENT_VISITAS, R.anim.slide_in_right, R.anim.slide_out_right);
                        }
                        builder.dismiss();
                    }
                });
                c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
            }
        });
        builder.setCancelable(false);
        builder.show();
    }


    public void showAlertAskForSave123( String title, String message) {
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_empty, null);

        final AlertDialog builder = new AlertDialog.Builder(activity)
                .setView(viewInfalted)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("aceptar", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();

        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                Button c = builder.getButton(AlertDialog.BUTTON_NEGATIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setOnSave();
                        builder.dismiss();
                    }
                });
                c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
            }
        });
        builder.setCancelable(false);
        builder.show();
    }




    private void showAlertForSave(String title, String message){
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_empty,null);


        final AlertDialog builder22 = new AlertDialog.Builder(activity)
                .setView(viewInfalted)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })/*.setNegativeButton("cancelar",null)*/.create();


        builder22.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button a = builder22.getButton(AlertDialog.BUTTON_POSITIVE);
                a.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder22.dismiss();
                        activity.cambiarFragment(new FragmentVisitas(), Utilidades.FRAGMENT_VISITAS, R.anim.slide_in_left, R.anim.slide_out_left);

                    }
                });
            }
        });
        builder22.setCancelable(true);
        builder22.show();
    }


    private void showAlertForRankear(final Visitas visitaAnterior, final TempVisitas temp_visitas){
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_rankear,null);

        final AlertDialog builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setView(viewInfalted)
                .setCancelable(false)
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();


        final TextView fecha_utima_visita = viewInfalted.findViewById(R.id.tv_fecha_rankear);
        final TextView recomendacion_ultima = viewInfalted.findViewById(R.id.tv_recom_rankear);
        final RatingBar rating = viewInfalted.findViewById(R.id.ratingBar);
        final EditText comentario_nuevo = viewInfalted.findViewById(R.id.et_comentario);


        String fecha_last = "Fecha: " + visitaAnterior.getFecha_visita() + " " + visitaAnterior.getHora_visita();


        fecha_utima_visita.setText(fecha_last);
        recomendacion_ultima.setText(visitaAnterior.getRecomendation_visita());

        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(rating.getRating() > 0.0 && !TextUtils.isEmpty(comentario_nuevo.getText().toString())){

                            temp_visitas.setEvaluacion(rating.getRating());
                            temp_visitas.setComentario_evaluacion(comentario_nuevo.getText().toString());
                            temp_visitas.setId_evaluacion(visitaAnterior.getId_visita());
                            builder.dismiss();

                        }else{
                            Snackbar.make(view, "Debe evaluar y comentar para guardar ", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        builder.setCancelable(false);
        builder.show();
    }


    private void showAlertForUpdate(final Fotos foto){
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_big_img,null);

//        String titulo = "Editando " + fotos.getNombreFoto() + " de PAQUETE " + fotos.getEtiquetaPaquete();
        final AlertDialog builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setView(viewInfalted)
                .setNeutralButton("eliminar", (foto.getEstado_fotos() <= 0) ? new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                } : null)
                .setPositiveButton("cerrar", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })/*.setNegativeButton("cancelar",null)*/.create();

//        final TextView txt = viewInfalted.findViewById(R.id.et_cambia_nombre_foto);
        final ImageView imageView = viewInfalted.findViewById(R.id.img_alert_foto);

//        txt.setText(medidaAMostrar);
        Picasso.get().load("file:///"+foto.getRuta()).into(imageView);
        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                Button c = builder.getButton(AlertDialog.BUTTON_NEUTRAL);

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();

                    }
                });


                c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (foto.getEstado_fotos() > 0){
                            Toasty.warning(activity, "No puedes eliminar la foto, esta visita ya se subio a servidor", Toast.LENGTH_SHORT, true).show();
                        }else{
                            showAlertForEliminarFoto(foto);
                            builder.dismiss();
                        }


                    }
                });
            }
        });
        builder.setCancelable(false);
        builder.show();
    }


    void showAlertForEliminarFoto(final Fotos foto){

        final AlertDialog builder = new AlertDialog.Builder(activity)
                .setView(R.layout.alert_empty)
                .setPositiveButton("Eliminar", (foto.getEstado_fotos() <= 0) ? new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }: null )
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                })
                .setTitle("Confirmación")
                .setMessage("¿Estas seguro que deseas eliminar esta foto?").create();



                builder.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                        Button c = builder.getButton(AlertDialog.BUTTON_NEGATIVE);

                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try{
                                    File file = new File(foto.getRuta());
                                    if (file.exists()) {
//                                            boolean eliminado = ;
                                        if (file.delete()){
                                            MainActivity.myAppDB.myDao().deleteFotos(foto);
                                            agregarImagenToAgronomos();
                                            agregarImagenToClientes();
                                            Toasty.success(activity, "Foto eliminada con exito", Toast.LENGTH_SHORT, true).show();
                                        }else{
                                            Toasty.warning(activity, "Por favor vuelva a intentarlo", Toast.LENGTH_SHORT, true).show();
                                        }
                                        builder.dismiss();
                                    }else{
                                        MainActivity.myAppDB.myDao().deleteFotos(foto);
                                        agregarImagenToAgronomos();
                                        agregarImagenToClientes();
                                        Toasty.success(activity, "Foto eliminada con exito", Toast.LENGTH_SHORT, true).show();
                                    }
                                }catch (Exception e){
                                    MainActivity.myAppDB.myDao().deleteFotos(foto);
                                    agregarImagenToAgronomos();
                                    agregarImagenToClientes();
                                    builder.dismiss();
                                }
                            }
                        });

                        c.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                builder.dismiss();
                            }
                        });


                    }
                });

                builder.setCancelable(false);
                builder.show();

    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if(fileImagen != null){
            outState.putParcelable("file_uri", Uri.fromFile(fileImagen));
        }


    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        // get the file url
        if(savedInstanceState != null && savedInstanceState.getParcelable("file_uri") != null){
            Uri ui = savedInstanceState.getParcelable("file_uri");
            if (ui != null && ui.getPath() != null){
                fileImagen= new File(ui.getPath());
            }
        }


    }

    void cambiarSubtitulo(String feno){

        AnexoCompleto anexoCompleto = MainActivity.myAppDB.myDao().getAnexoCompletoById(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));


        if (activity != null && anexoCompleto != null){
            String especie =  anexoCompleto.getEspecie().getDesc_especie();
            String Agricultor =  anexoCompleto.getAgricultor().getNombre_agricultor();
            String Potrero = anexoCompleto.getLotes().getNombre_lote();
            String Predio = anexoCompleto.getPredios().getNombre();

            String espe = (!especie.isEmpty()) ? " Es.(" +especie+ ") " : "";
            String agri = (!Agricultor.isEmpty()) ? " Agr.(" +Agricultor.substring(0, 15)+ ") " : "";
            espe += agri;
            String potrero = (!Potrero.isEmpty()) ? " Pr.(" +Potrero+ ") " : "";
            espe += potrero;
            String predio = (!Predio.isEmpty()) ? " Pr.(" +Predio+ ") " : "";
            espe += predio;

            activity.updateView(getResources().getString(R.string.app_name), "An.(" + anexoCompleto.getAnexoContrato().getAnexo_contrato() +" )"+ espe +" "+ feno);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cambiarSubtitulo("");

    }
}
