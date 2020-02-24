package cl.smapdev.curimapu.fragments.contratos;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.FotosListAdapter;
import cl.smapdev.curimapu.clases.adapters.SpinnerAdapter;
import cl.smapdev.curimapu.clases.tablas.Flowering;
import cl.smapdev.curimapu.clases.tablas.Fotos;
import cl.smapdev.curimapu.clases.tablas.Harvest;
import cl.smapdev.curimapu.clases.tablas.Sowing;
import cl.smapdev.curimapu.clases.tablas.Visitas;
import cl.smapdev.curimapu.clases.temporales.TempFlowering;
import cl.smapdev.curimapu.clases.temporales.TempHarvest;
import cl.smapdev.curimapu.clases.temporales.TempSowing;
import cl.smapdev.curimapu.clases.temporales.TempVisitas;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.FragmentVisitas;
import okhttp3.internal.Util;

import static android.app.Activity.RESULT_OK;

public class FragmentFormVisitas extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, View.OnFocusChangeListener {

    private Spinner sp_fenologico,sp_cosecha,sp_crecimiento,sp_fito,sp_general_cultivo,sp_humedad,sp_malezas;

    private Button btn_guardar, btn_volver;

    private MainActivity activity;
    private SharedPreferences prefs;

    private TempVisitas temp_visitas;

    private String[] fenologico, cosecha, crecimiento, maleza;

    private EditText et_obs, et_recomendacion;



    /* IMAGENES */
    private RecyclerView rwAgronomo, rwCliente;

//    private static final String FIELDBOOKKEY = "fieldbookkey";
    private static final int COD_FOTO = 005;

    private File fileImagen;

    private int fieldbook;

    private FloatingActionButton material_private, material_public;

    private FotosListAdapter adapterAgronomo;
    private FotosListAdapter adapterCliente;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity a = (MainActivity) getActivity();
        if ( a != null) activity = a;

        prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);


        fenologico = getResources().getStringArray(R.array.fenologico);
        cosecha = getResources().getStringArray(R.array.cosecha);
        crecimiento = getResources().getStringArray(R.array.crecimiento);
        maleza = getResources().getStringArray(R.array.maleza);




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


   /*     if (prefs != null && prefs.getInt(Utilidades.SHARED_VISIT_FICHA_ID, 0) > 0){
            temp_visitas = MainActivity.myAppDB.myDao().getTempFichas();
        }*/

        if (temp_visitas == null){
            temp_visitas = new TempVisitas();
            if (prefs != null){
                temp_visitas.setId_anexo_temp_visita(prefs.getInt(Utilidades.SHARED_VISIT_ANEXO_ID, 0));
                temp_visitas.setId_temp_visita(0);
            }
            MainActivity.myAppDB.myDao().setTempVisitas(temp_visitas);
            temp_visitas = MainActivity.myAppDB.myDao().getTempFichas();
        }



        cargarSpinners();



        material_private.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int cantidaAgr = MainActivity.myAppDB.myDao().getCantAgroByFieldViewAndFicha(0, 2, temp_visitas.getId_anexo_temp_visita(), temp_visitas.getId_temp_visita());
                if (cantidaAgr > 2){
                    Utilidades.avisoListo(activity,getResources().getString(R.string.title_dialog_agron),getResources().getString(R.string.message_dialog_agron),getResources().getString(R.string.message_dialog_btn_ok));
                }else{
                    if (temp_visitas.getAction_temp_visita() == 2){
                        Utilidades.avisoListo(activity,getResources().getString(R.string.title_dialog_agron),"Visita en estado terminado, no puedes agregar mas fotos.","entiendo");
                    }else{
                        abrirCamara(2);
                    }

                }


            }
        });
        material_public.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (sp_fenologico.getSelectedItemPosition() > 0){
                    if (temp_visitas.getAction_temp_visita() == 2){
                        Utilidades.avisoListo(activity,getResources().getString(R.string.title_dialog_agron),"Visita en estado terminado, no puedes agregar mas fotos.","entiendo");
                    }else{
                        abrirCamara(0);
                    }

                }else{
                    Utilidades.avisoListo(activity, "Falta algo!", "Debes seleccionar un estado fenologico primero","entiendo");
                }

            }
        });






        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (!checkPermission()) {
                requestPermission();
            }
        }


    }

    /*
    * GENERA LOS ONITEMSELECTEDLISTENER DE LOS SPINNERS
    * */
    private void accionSpinners(){
        if (temp_visitas != null){


            sp_fenologico.setSelection(temp_visitas.getPhenological_state_temp_visita());
            sp_cosecha.setSelection(temp_visitas.getHarvest_temp_visita());
            sp_crecimiento.setSelection(temp_visitas.getGrowth_status_temp_visita());
            sp_fito.setSelection(temp_visitas.getPhytosanitary_state_temp_visita());
            sp_general_cultivo.setSelection(temp_visitas.getOverall_status_temp_visita());
            sp_humedad.setSelection(temp_visitas.getHumidity_floor_temp_visita());
            sp_malezas.setSelection(temp_visitas.getWeed_state_temp_visita());

            et_obs.setText(temp_visitas.getObservation_temp_visita());
            et_recomendacion.setText(temp_visitas.getRecomendation_temp_visita());

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
            Toast.makeText(getActivity(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
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
                    setOnSave();
                break;

            case R.id.btn_volver:
                MainActivity activity = (MainActivity) getActivity();
                if (activity != null){
                    activity.cambiarFragment(new FragmentVisitas(), Utilidades.FRAGMENT_VISITAS, R.anim.slide_in_right, R.anim.slide_out_right);
                }

                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void bind(View view){
        sp_fenologico = (Spinner) view.findViewById(R.id.sp_feno);
        sp_cosecha = (Spinner) view.findViewById(R.id.sp_cosecha);
        sp_crecimiento = (Spinner) view.findViewById(R.id.sp_crecimiento);
        sp_fito = (Spinner) view.findViewById(R.id.sp_fito);
        sp_general_cultivo = (Spinner) view.findViewById(R.id.sp_general_cultivo);
        sp_humedad = (Spinner) view.findViewById(R.id.sp_humedad);
        sp_malezas = (Spinner) view.findViewById(R.id.sp_malezas);
        btn_guardar = (Button) view.findViewById(R.id.btn_guardar);


        et_obs = (EditText) view.findViewById(R.id.et_obs);
        et_recomendacion = (EditText) view.findViewById(R.id.et_recomendacion);



        btn_volver = (Button) view.findViewById(R.id.btn_volver);



        sp_fenologico.setOnItemSelectedListener(this);
        sp_malezas.setOnItemSelectedListener(this);
        sp_humedad.setOnItemSelectedListener(this);
        sp_general_cultivo.setOnItemSelectedListener(this);
        sp_fito.setOnItemSelectedListener(this);
        sp_crecimiento.setOnItemSelectedListener(this);
        sp_cosecha.setOnItemSelectedListener(this);


        et_recomendacion.setOnFocusChangeListener(this);
        et_obs.setOnFocusChangeListener(this);


        btn_volver.setOnClickListener(this);
        btn_guardar.setOnClickListener(this);



        rwAgronomo = (RecyclerView) view.findViewById(R.id.fotos_agronomos);
        rwCliente = (RecyclerView) view.findViewById(R.id.fotos_clientes);

        material_private = (FloatingActionButton) view.findViewById(R.id.material_private);
        material_public = (FloatingActionButton) view.findViewById(R.id.material_public);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i > 0) {

            switch (adapterView.getId()) {
                case R.id.sp_feno:
                    temp_visitas.setPhenological_state_temp_visita(i);
                    temp_visitas.setEtapa_temp_visitas(Utilidades.getPhenoState(sp_fenologico.getSelectedItemPosition()));
                    break;
                case R.id.sp_cosecha:
                    temp_visitas.setHarvest_temp_visita(i);
                    break;
                case R.id.sp_crecimiento:
                    temp_visitas.setGrowth_status_temp_visita(i);
                    break;
                case R.id.sp_fito:
                    temp_visitas.setPhytosanitary_state_temp_visita(i);
                    break;
                case R.id.sp_general_cultivo:
                    temp_visitas.setOverall_status_temp_visita(i);
                    break;
                case R.id.sp_humedad:
                    temp_visitas.setHumidity_floor_temp_visita(i);
                    break;
                case R.id.sp_malezas:
                    temp_visitas.setWeed_state_temp_visita(i);
                    break;
            }
            MainActivity.myAppDB.myDao().updateTempVisitas(temp_visitas);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onFocusChange(View view, boolean b) {

        if (!b){
            switch (view.getId()){
                case R.id.et_obs:
                    temp_visitas.setObservation_temp_visita(et_obs.getText().toString());
                    break;
                case R.id.et_recomendacion:
                    temp_visitas.setRecomendation_temp_visita(et_recomendacion.getText().toString());
                    break;
            }

            MainActivity.myAppDB.myDao().updateTempVisitas(temp_visitas);
        }
    }





    /* IMAGENES */

    private void abrirCamara(int vista){

        prefs.edit().remove(Utilidades.VISTA_FOTOS).apply();

        File miFile = new File(Environment.getExternalStorageDirectory(), Utilidades.DIRECTORIO_IMAGEN);
        boolean isCreada = miFile.exists();

        if (!isCreada){
            isCreada=miFile.mkdirs();
        }

        if(isCreada){

            long consecutivo = System.currentTimeMillis()/1000;
            String nombre = consecutivo+"_"+vista+"_0.jpg";
            String path = Environment.getExternalStorageDirectory() + File.separator + Utilidades.DIRECTORIO_IMAGEN + File.separator + nombre;

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


                Bitmap src = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);

                ByteArrayOutputStream bos = null;
                try {
                    bos = new ByteArrayOutputStream();
                    escribirFechaImg(src).compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    byte[] bitmapdata = bos.toByteArray();

                    FileOutputStream fos = new FileOutputStream(fileImagen.getAbsoluteFile());
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();

                } catch (IOException e) {
                    e.printStackTrace();
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
        fotos.setId_ficha(temp_visitas.getId_anexo_temp_visita());
        fotos.setVista(prefs.getInt(Utilidades.VISTA_FOTOS, 0));
        fotos.setRuta(path.getAbsolutePath());
        fotos.setId_visita_foto(prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0));


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
            int fotos = MainActivity.myAppDB.myDao().getCantFotos(temp_visitas.getId_anexo_temp_visita(), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0));
            if (fotos <= 0) {
                Utilidades.avisoListo(activity, "Falta algo", "Debes tomar al menos una foto", "entiendo");
            } else {
                int favs = MainActivity.myAppDB.myDao().getCantFavoritasByFieldbookAndFicha(temp_visitas.getId_anexo_temp_visita(), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0));
                if (favs <= 0) {
                    Utilidades.avisoListo(activity, "Falta algo", "Debes seleccionar como favorita al menos una foto (manten precionada la foto para marcar)", "entiendo");
                } else {
                    /*int sow = MainActivity.myAppDB.myDao().getCantTempSowing(temp_visitas.getId_anexo_temp_visita());
                    int flow = MainActivity.myAppDB.myDao().getCantTempFlowering(temp_visitas.getId_anexo_temp_visita());
                    int har = MainActivity.myAppDB.myDao().getCantTempHarvest(temp_visitas.getId_anexo_temp_visita());

                    if (sow <= 0 && flow <= 0 && har <= 0) {
                        Utilidades.avisoListo(activity, "Falta algo", "Debes seleccionar como favorita al menos una foto (manten precionada la foto para marcar)", "entiendo");
                    } else {*/
//                    todo do save


                        boolean problema = false;

                        Visitas visitas = new Visitas();
                        visitas.setGrowth_status_visita(temp_visitas.getGrowth_status_temp_visita());
                        visitas.setHarvest_visita(temp_visitas.getHarvest_temp_visita());
                        visitas.setHumidity_floor_visita(temp_visitas.getHumidity_floor_temp_visita());
                        visitas.setId_anexo_visita(temp_visitas.getId_anexo_temp_visita());
                        visitas.setObservation_visita(temp_visitas.getObservation_temp_visita());
                        visitas.setOverall_status_visita(temp_visitas.getOverall_status_temp_visita());
                        visitas.setPhenological_state_visita(temp_visitas.getPhenological_state_temp_visita());
                        visitas.setPhytosanitary_state_visita(temp_visitas.getPhytosanitary_state_temp_visita());
                        visitas.setRecomendation_visita(temp_visitas.getRecomendation_temp_visita());
                        visitas.setWeed_state_visita(temp_visitas.getWeed_state_temp_visita());
                        visitas.setEstado_server_visitas(0);
                        visitas.setEstado_visita(0);
                        visitas.setEtapa_visitas(temp_visitas.getEtapa_temp_visitas());


                        String fecha = Utilidades.fechaActualConHora();

                        String[] fechaHora = fecha.split(" ");

                        String[] temporada = fechaHora[0].split("-");

                        visitas.setHora_visita(fechaHora[1]);
                        visitas.setFecha_visita(fechaHora[0]);
                        visitas.setTemporada(Integer.parseInt(temporada[0]));

                        long idVisita = 0;

                        if (prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0)  > 0 ){

                            idVisita = prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0);
                            visitas.setId_visita((int) idVisita);
                            MainActivity.myAppDB.myDao().updateVisita(visitas);

                        }else{
                            idVisita = MainActivity.myAppDB.myDao().setVisita(visitas);
                        }


                        MainActivity.myAppDB.myDao().updateFotosWithVisita((int) idVisita, temp_visitas.getId_anexo_temp_visita());

                        try{
                            TempSowing tempSowing =  MainActivity.myAppDB.myDao().getTempSowing(temp_visitas.getId_anexo_temp_visita());
                            if (tempSowing != null){
                                Sowing sowing = new Sowing();
                                sowing.setAmount_applied_sowing(tempSowing.getAmount_applied_temp_sowing());
                                sowing.setBasta_splat_4_ha_sowing(tempSowing.getBasta_splat_4_ha_temp_sowing());
                                sowing.setBasta_spray_1_sowing(tempSowing.getBasta_spray_1_temp_sowing());
                                sowing.setBasta_spray_2_sowing(tempSowing.getBasta_spray_2_temp_sowing());
                                sowing.setBasta_spray_3_sowing(tempSowing.getBasta_spray_3_temp_sowing());
                                sowing.setBasta_spray_4_sowing(tempSowing.getBasta_spray_4_temp_sowing());
                                sowing.setDate_1_herb_sowing(tempSowing.getDate_1_herb_temp_sowing());
                                sowing.setDate_1_sowing(tempSowing.getDate_1_temp_sowing());
                                sowing.setDate_2_herb_sowing(tempSowing.getDate_2_herb_temp_sowing());
                                sowing.setDate_2_sowing(tempSowing.getDate_2_temp_sowing());
                                sowing.setDate_3_herb_sowing(tempSowing.getDate_3_herb_temp_sowing());
                                sowing.setDate_foliar_sowing(tempSowing.getDate_foliar_temp_sowing());
                                sowing.setDate_nombre_largo_sowing(tempSowing.getDate_nombre_largo_temp_sowing());
                                sowing.setDate_pre_emergence_sowing(tempSowing.getDate_pre_emergence_temp_sowing());
                                sowing.setDelivered_sowing(tempSowing.getDelivered_temp_sowing());
                                sowing.setDose_1_sowing(tempSowing.getDose_1_temp_sowing());
                                sowing.setDose_2_sowing(tempSowing.getDose_2_temp_sowing());
                                sowing.setDose_foliar_sowing(tempSowing.getDose_foliar_temp_sowing());
                                sowing.setDose_nombre_largo_sowing(tempSowing.getDose_nombre_largo_temp_sowing());
                                sowing.setDose_pre_emergence_sowing(tempSowing.getDose_pre_emergence_temp_sowing());
                                sowing.setEast_sowing(tempSowing.getEast_temp_sowing());
                                sowing.setEstado_server_sowing(0);
                                sowing.setFemale_lines_sowing(tempSowing.getFemale_lines_temp_sowing());
                                sowing.setFemale_sowing_date_end_sowing(tempSowing.getFemale_sowing_date_end_temp_sowing());
                                sowing.setFemale_sowing_date_start_sowing(tempSowing.getFemale_sowing_date_start_temp_sowing());
                                sowing.setFemale_sowing_lot_sowing(tempSowing.getFemale_sowing_lot_temp_sowing());
                                sowing.setFoliar_sowing(tempSowing.getFoliar_temp_sowing());
                                sowing.setId_anexo_sowing(tempSowing.getId_anexo_temp_sowing());
                                sowing.setId_visita_sowing((int) idVisita);
                                sowing.setMeters_isoliation_sowing(tempSowing.getMeters_isoliation_temp_sowing());
                                sowing.setName_1_herb_sowing(tempSowing.getName_1_herb_temp_sowing());
                                sowing.setName_2_herb_sowing(tempSowing.getName_2_herb_temp_sowing());
                                sowing.setName_3_herb_sowing(tempSowing.getName_3_herb_temp_sowing());
                                sowing.setNorth_sowing(tempSowing.getNorth_temp_sowing());
                                sowing.setPlant_m_sowing(tempSowing.getPlant_m_temp_sowing());
                                sowing.setPopulation_plants_ha_sowing(tempSowing.getPopulation_plants_ha_temp_sowing());
                                sowing.setProduct_nombre_largo_sowing(tempSowing.getProduct_nombre_largo_temp_sowing());
                                sowing.setReal_sowing_female_sowing(tempSowing.getReal_sowing_female_temp_sowing());
                                sowing.setRow_distance_sowing(tempSowing.getRow_distance_temp_sowing());
                                sowing.setSag_planting_sowing(tempSowing.getSag_planting_temp_sowing());
                                sowing.setSouth_sowing(tempSowing.getSouth_temp_sowing());
                                sowing.setSowing_seed_meter_sowing(tempSowing.getSowing_seed_meter_temp_sowing());
                                sowing.setType_of_mixture_applied_sowing(tempSowing.getType_of_mixture_applied_temp_sowing());
                                sowing.setWater_pre_emergence_sowing(tempSowing.getWater_pre_emergence_temp_sowing());
                                sowing.setWest_sowing(tempSowing.getWest_temp_sowing());




                                if (temp_visitas.getId_temp_visita()  > 0 ) {

                                    sowing.setId_visita_sowing((int)idVisita);
                                    MainActivity.myAppDB.myDao().updateSowing(sowing);
                                }else{
                                    long idSowing = MainActivity.myAppDB.myDao().setSowing(sowing);
                                }


                            }
                        }catch (SQLiteException e){
                            problema = true;
                        }


                        try{
                            TempFlowering tempFlowering = MainActivity.myAppDB.myDao().getTempFlowering(temp_visitas.getId_anexo_temp_visita());
                            if (tempFlowering != null){
                                Flowering flowering = new Flowering();

                                flowering.setCheck_flowering(tempFlowering.getCheck_temp_flowering());
                                flowering.setDate_beginning_depuration_flowering(tempFlowering.getDate_beginning_depuration_temp_flowering());
                                flowering.setDate_funficide_flowering(tempFlowering.getDate_funficide_temp_flowering());
                                flowering.setDate_insecticide_flowering(tempFlowering.getDate_insecticide_temp_flowering());
                                flowering.setDate_inspection_flowering(tempFlowering.getDate_inspection_temp_flowering());
                                flowering.setDate_notice_sag_flowering(tempFlowering.getDate_notice_sag_temp_flowering());
                                flowering.setDate_off_type_flowering(tempFlowering.getDate_off_type_temp_flowering());
                                flowering.setDose_fungicide_flowering(tempFlowering.getDose_fungicide_temp_flowering());
                                flowering.setEstado_server_flowering(0);
                                flowering.setFertility_1_flowering(tempFlowering.getFertility_1_temp_flowering());
                                flowering.setFertility_2_flowering(tempFlowering.getFertility_2_temp_flowering());
                                flowering.setFlowering_end_flowering(tempFlowering.getFlowering_end_temp_flowering());
                                flowering.setFlowering_estimation_flowering(tempFlowering.getFlowering_estimation_temp_flowering());
                                flowering.setFlowering_start_flowering(tempFlowering.getFlowering_start_temp_flowering());
                                flowering.setFungicide_name_flowering(tempFlowering.getFungicide_name_temp_flowering());
                                flowering.setId_anexo_flowering(tempFlowering.getId_anexo_temp_flowering());
                                flowering.setId_visita_flowering((int) idVisita);
                                flowering.setMain_characteristic_flowering(tempFlowering.getMain_characteristic_temp_flowering());
                                flowering.setPlant_number_checked_flowering(tempFlowering.getPlant_number_checked_temp_flowering());


                                if (temp_visitas.getId_temp_visita()  > 0 ) {

                                    flowering.setId_visita_flowering((int) idVisita);
                                    MainActivity.myAppDB.myDao().updateFlowering(flowering);
                                }else{
                                    long idFlowing = MainActivity.myAppDB.myDao().setFlowering(flowering);
                                }

                            }
                        }catch (SQLiteException e){
                            problema = true;
                        }


                        try {
                            TempHarvest tempHarvest = MainActivity.myAppDB.myDao().getTempHarvest(temp_visitas.getId_anexo_temp_visita());
                            if (tempHarvest != null){
                                Harvest harvest = new Harvest();

                                harvest.setBeginning_date_temp_harvest(tempHarvest.getBeginning_date_temp_harvest());
                                harvest.setDate_harvest_estimation_temp_harvest(tempHarvest.getDate_harvest_estimation_temp_harvest());
                                harvest.setEnd_date_temp_harvest(tempHarvest.getEnd_date_temp_harvest());
                                harvest.setEstado_server_harvest(0);
                                harvest.setEstimated_date_temp_harvest(tempHarvest.getEstimated_date_temp_harvest());
                                harvest.setId_anexo_temp_harvest(tempHarvest.getId_anexo_temp_harvest());
                                harvest.setId_visita_harvest((int) idVisita);
                                harvest.setKg_ha_yield_temp_harvest(tempHarvest.getKg_ha_yield_temp_harvest());
                                harvest.setModel_machine_temp_harvest(tempHarvest.getModel_machine_temp_harvest());
                                harvest.setObservation_dessicant_temp_harvest(tempHarvest.getObservation_dessicant_temp_harvest());
                                harvest.setObservation_yield_temp_harvest(tempHarvest.getObservation_yield_temp_harvest());
                                harvest.setOwner_machine_temp_harvest(tempHarvest.getOwner_machine_temp_harvest());
                                harvest.setPorcent_temp_harvest(tempHarvest.getPorcent_temp_harvest());
                                harvest.setReal_date_temp_harvest(tempHarvest.getReal_date_temp_harvest());
                                harvest.setSwathing_date_temp_harvest(tempHarvest.getSwathing_date_temp_harvest());

                                if (temp_visitas.getId_temp_visita()  > 0 ) {

                                    harvest.setId_visita_harvest((int) idVisita);
                                    MainActivity.myAppDB.myDao().updateHarvest(harvest);
                                }else {
                                    long idHarvest = MainActivity.myAppDB.myDao().setHarvest(harvest);
                                }
                            }
                        }catch (SQLiteException e){
                            problema = true;
                        }

                        if(!problema){
                            showAlertForSave("Genial", "Se guardo todo como corresponde");
                        }else{
                            Utilidades.avisoListo(activity, "Algo pasó!", "Hubo un problema al guardar, vuelva a intentarlo", "entiendo");
                        }
                    }
                }
//            }
        }else{
            Utilidades.avisoListo(activity, "Falta algo", "Algo salio mal, por favor revise el fieldbook", "entiendo");
        }
    }




    //    todo agregar imagen de sello de agua ;)
    private Bitmap escribirFechaImg(Bitmap bm){

        Bitmap dest = Bitmap.createBitmap(bm,0, 0,bm.getWidth(), bm.getHeight()).copy(Bitmap.Config.ARGB_8888, true);

        String fecha = Utilidades.fechaActualInvSinHora();

        Canvas cs = new Canvas(dest);


        Paint myPaint = new Paint();
        myPaint.setColor(getResources().getColor(R.color.transparentBlack));
        myPaint.setStrokeWidth(10);
        cs.drawRect(0, dest.getHeight() - 90, 700, dest.getHeight() - 10, myPaint);


        Paint tPaint = new Paint();
        tPaint.setStyle(Paint.Style.FILL);
        tPaint.setColor(getResources().getColor(android.R.color.white));
        tPaint.setTextSize(80);
        tPaint.setStrokeWidth(10);



        // text shadow
        tPaint.setShadowLayer(1f, 0f, 1f, getResources().getColor(android.R.color.black));

        Rect bounds = new Rect();
        tPaint.getTextBounds(fecha, 0, fecha.length(), bounds);

        cs.drawText(fecha, 90, dest.getHeight() - 25 , tPaint);





        return dest;
    }




    private void agregarImagenToAgronomos(){



        LinearLayoutManager lManager = null;
        if (activity != null){
//            lManager = new GridLayoutManager(activity, 1);
            lManager  = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);

        }

        rwAgronomo.setHasFixedSize(true);
        rwAgronomo.setLayoutManager(lManager);


//        int[] myImageList = new int[]{R.drawable.f1, R.drawable.f2 };
        List<Fotos> myImageList = MainActivity.myAppDB.myDao().getFotosByFieldAndView(0, 2, temp_visitas.getId_anexo_temp_visita(), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0));


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



    private void cambiarFavorita(Fotos fotos){

        if (fotos.isFavorita()){
            Toast.makeText(activity, getResources().getString(R.string.message_fav_remove), Toast.LENGTH_SHORT).show();
            fotos.setFavorita(false);
        }else{
            Toast.makeText(activity, getResources().getString(R.string.message_fav), Toast.LENGTH_SHORT).show();
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

        LinearLayoutManager lManager = null;
        if (activity != null){
//            lManager = new GridLayoutManager(activity, 1);
            lManager  = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);

        }

        rwCliente.setHasFixedSize(true);
        rwCliente.setLayoutManager(lManager);


        List<Fotos> myImageList = MainActivity.myAppDB.myDao().getFotosByFieldAndView(0, temp_visitas.getId_anexo_temp_visita(), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0));
//        int[] myImageList = new int[]{R.drawable.f1, R.drawable.f2,R.drawable.f3, R.drawable.f4,R.drawable.f5 };

        if (myImageList.size() > 0) sp_fenologico.setEnabled(false);

        adapterCliente = new FotosListAdapter(myImageList, activity,new FotosListAdapter.OnItemClickListener() {
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
                        int favoritas = MainActivity.myAppDB.myDao().getCantFavoritasByFieldbookFichaAndVista(0, temp_visitas.getId_anexo_temp_visita(), 0, temp_visitas.getId_temp_visita());


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


        rwCliente.setAdapter(adapterCliente);
    }



    private void showAlertForSave(String title, String message){
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_empty,null);


        final AlertDialog builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setView(viewInfalted)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })/*.setNegativeButton("cancelar",null)*/.create();


        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.cambiarFragment(new FragmentVisitas(), Utilidades.FRAGMENT_VISITAS, R.anim.slide_in_left, R.anim.slide_out_left);
                        builder.dismiss();

                    }
                });
            }
        });
        builder.setCancelable(false);
        builder.show();
    }


    private void showAlertForUpdate(Fotos foto){
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_big_img,null);

//        String titulo = "Editando " + fotos.getNombreFoto() + " de PAQUETE " + fotos.getEtiquetaPaquete();
        final AlertDialog builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setView(viewInfalted)
                .setPositiveButton("cerrar", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })/*.setNegativeButton("cancelar",null)*/.create();

        final TextView txt = viewInfalted.findViewById(R.id.et_cambia_nombre_foto);
        final ImageView imageView = viewInfalted.findViewById(R.id.img_alert_foto);
        String medidaAMostrar = "Nombre prueba";
        txt.setText(medidaAMostrar);
        Picasso.get().load("file:///"+foto.getRuta()).into(imageView);
        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
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

}
