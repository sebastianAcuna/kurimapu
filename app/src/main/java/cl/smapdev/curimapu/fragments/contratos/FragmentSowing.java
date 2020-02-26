package cl.smapdev.curimapu.fragments.contratos;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.CropRotationAdapter;
import cl.smapdev.curimapu.clases.tablas.CropRotation;
import cl.smapdev.curimapu.clases.temporales.TempSowing;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

import static androidx.core.content.ContextCompat.checkSelfPermission;


public class FragmentSowing extends Fragment implements View.OnFocusChangeListener, LocationListener {


    private MainActivity activity = null;


    private String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private LocationManager locationManager;


    private EditText date_siembra_sag, date_sowing_female_start, date_sowing_female_end, date_date_foliar;

    /*private ImageButton btn_fecha_siembra_sag, btn_sowing_female_start, btn_sowing_female_end,btn_date_one,btn_date_two,btn_herbicide_date_one,btn_herbicide_date_two,btn_herbicide_date_tres,
            btn_emergence_date,btn_spray_uno,btn_spray_dos,btn_spray_tres,btn_spray_cuatro,btn_spray_cuatro_ha,btn_bruchus,btn_date_foliar;*/


    private EditText  et_2015, et_2016, et_2017, et_2018, et_entregado,
            et_tipo_mezcla, et_cant_aplicada, et_meters, et_lines_female, et_showing_female, et_real_sowing, et_sowin_seed_f, et_row_distance, et_f_plant, et_f_population,  et_foliar, et_dose_foliar;


    private RecyclerView recycler_crop;


    private TextView et_norting, et_easting;

    private DatePickerDialog datePickerDialog;


    private SharedPreferences prefs;

    private TempSowing temp_sowing;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity a = (MainActivity) getActivity();
        if (a != null) {
            activity = a;
        }

        prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sowing, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind(view);


        //if (view.findViewById(R.id.container_fotos_sowing) != null){
            cargarTemp();
        //}



//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        if (checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//                            requestPermission();
//                            return;
//                        }
//                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, FragmentSowing.this);
//                    }
//                });
//
//            }
//        });


    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (activity != null){
                    cargarTemp();
            }
        }
    }

    private void cargarTemp(){
        temp_sowing  = (temp_sowing != null) ? temp_sowing : MainActivity.myAppDB.myDao().getTempSowing(prefs.getInt(Utilidades.SHARED_VISIT_ANEXO_ID, 0));


        if (temp_sowing == null) {
            temp_sowing = new TempSowing();
            if (prefs != null) {
                temp_sowing.setId_anexo_temp_sowing(prefs.getInt(Utilidades.SHARED_VISIT_ANEXO_ID, 0));

            }
            MainActivity.myAppDB.myDao().setTempSowing(temp_sowing);
            temp_sowing = MainActivity.myAppDB.myDao().getTempSowing(temp_sowing.getId_anexo_temp_sowing());

            if (temp_sowing != null){
                loadUiData();
            }
        }else{
            loadUiData();
        }

    }

//    private boolean checkPermission() {
//
//
//        if (activity != null){
//            int result = checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
//            int result1 = checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
//            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
//        }else{
//            return false;
//        }
//
//
//    }
//
//
//    private void requestPermission() {
//
//        if (activity != null){
//            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
//                Toast.makeText(activity, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
//            } else {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
//                }
//            }
//        }
//
//    }


    private void cargarCrop(){

        List<CropRotation> cropList = MainActivity.myAppDB.myDao().getCropRotation(temp_sowing.getId_anexo_temp_sowing());
        CropRotationAdapter cropAdapter = new CropRotationAdapter(cropList, activity);
        recycler_crop.setAdapter(cropAdapter);
    }


    private void loadUiData(){
        try{

            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fotos_sowing, FragmentFotos.getInstance(2), Utilidades.FRAGMENT_FOTOS).commit();
        }catch (IllegalArgumentException e){
            Log.e("ILEGAL EXCEPTION SOWING", e.getMessage());
        }


        cargarCrop();

        date_siembra_sag.setText(Utilidades.voltearFechaVista(temp_sowing.getSag_planting_temp_sowing()));
        date_sowing_female_start.setText(Utilidades.voltearFechaVista(temp_sowing.getFemale_sowing_date_start_temp_sowing()));
        date_sowing_female_end.setText(Utilidades.voltearFechaVista(temp_sowing.getFemale_sowing_date_end_temp_sowing()));
/*        date_date_one.setText(Utilidades.voltearFechaVista(temp_sowing.getDate_1_temp_sowing()));
        date_date_two.setText(Utilidades.voltearFechaVista(temp_sowing.getDate_2_temp_sowing()));
        date_herbicide_date_one.setText(Utilidades.voltearFechaVista(temp_sowing.getDate_1_herb_temp_sowing()));
        date_herbicide_date_two.setText(Utilidades.voltearFechaVista(temp_sowing.getDate_2_herb_temp_sowing()));
        date_herbicide_date_tres.setText(Utilidades.voltearFechaVista(temp_sowing.getDate_3_herb_temp_sowing()));
        date_emergence_date.setText(Utilidades.voltearFechaVista(temp_sowing.getDate_pre_emergence_temp_sowing()));
        date_spray_uno.setText(Utilidades.voltearFechaVista(temp_sowing.getBasta_spray_1_temp_sowing()));
        date_spray_dos.setText(Utilidades.voltearFechaVista(temp_sowing.getBasta_spray_2_temp_sowing()));
        date_spray_tres.setText(Utilidades.voltearFechaVista(temp_sowing.getBasta_spray_3_temp_sowing()));
        date_spray_cuatro.setText(Utilidades.voltearFechaVista(temp_sowing.getBasta_spray_4_temp_sowing()));
        date_spray_cuatro_ha.setText(Utilidades.voltearFechaVista(temp_sowing.getBasta_splat_4_ha_temp_sowing()));
        date_bruchus.setText(Utilidades.voltearFechaVista(temp_sowing.getDate_nombre_largo_temp_sowing()));*/
        date_date_foliar.setText(Utilidades.voltearFechaVista(temp_sowing.getDate_foliar_temp_sowing()));





        /* ET TEXTO */


        et_entregado.setText(temp_sowing.getDelivered_temp_sowing());
        et_tipo_mezcla.setText(temp_sowing.getType_of_mixture_applied_temp_sowing());
        et_cant_aplicada.setText(temp_sowing.getAmount_applied_temp_sowing());
        et_lines_female.setText(temp_sowing.getFemale_lines_temp_sowing());
        et_showing_female.setText(temp_sowing.getFemale_sowing_lot_temp_sowing());
/*        et_dose_one.setText(temp_sowing.getDose_1_temp_sowing());
        et_dose_two.setText(temp_sowing.getDose_2_temp_sowing());
        et_name_one.setText(temp_sowing.getName_1_herb_temp_sowing());
        et_name_two.setText(temp_sowing.getName_2_herb_temp_sowing());
        et_name_tres.setText(temp_sowing.getName_3_herb_temp_sowing());
        et_emergence_dose.setText(temp_sowing.getDose_pre_emergence_temp_sowing());
        et_producto_bruchus.setText(temp_sowing.getProduct_nombre_largo_temp_sowing());
        et_dose_lt_ha.setText(temp_sowing.getDose_nombre_largo_temp_sowing());*/
        et_foliar.setText(temp_sowing.getFoliar_temp_sowing());
        et_dose_foliar.setText(temp_sowing.getDose_foliar_temp_sowing());


        et_meters.setText(String.valueOf(temp_sowing.getMeters_isoliation_temp_sowing()));
        et_real_sowing.setText(String.valueOf(temp_sowing.getReal_sowing_female_temp_sowing()));
        et_sowin_seed_f.setText(String.valueOf(temp_sowing.getSowing_seed_meter_temp_sowing()));
        et_row_distance.setText(String.valueOf(temp_sowing.getRow_distance_temp_sowing()));
//        et_water_lts.setText(String.valueOf(temp_sowing.getWater_pre_emergence_temp_sowing()));
        et_f_plant.setText(String.valueOf(temp_sowing.getPlant_m_temp_sowing()));
        et_f_population.setText(String.valueOf(temp_sowing.getPopulation_plants_ha_temp_sowing()));


        if (temp_sowing.getAction_temp_sowing() == 2){
            et_f_population.setEnabled(false);
            et_f_plant.setEnabled(false);
//            et_water_lts.setEnabled(false);
            et_row_distance.setEnabled(false);
            et_sowin_seed_f.setEnabled(false);
            et_real_sowing.setEnabled(false);
            et_meters.setEnabled(false);
            et_dose_foliar.setEnabled(false);
            et_foliar.setEnabled(false);
/*            et_dose_lt_ha.setEnabled(false);
            et_producto_bruchus.setEnabled(false);
            et_emergence_dose.setEnabled(false);
            et_name_tres.setEnabled(false);
            et_name_two.setEnabled(false);
            et_name_one.setEnabled(false);
            et_dose_two.setEnabled(false);
            et_dose_one.setEnabled(false);*/
            et_showing_female.setEnabled(false);
            et_lines_female.setEnabled(false);
            et_cant_aplicada.setEnabled(false);
            et_tipo_mezcla.setEnabled(false);
            et_entregado.setEnabled(false);
            date_siembra_sag.setEnabled(false);
            date_sowing_female_start.setEnabled(false);
            date_sowing_female_end.setEnabled(false);
/*            date_date_one.setEnabled(false);
            date_date_two.setEnabled(false);
            date_herbicide_date_one.setEnabled(false);
            date_herbicide_date_two.setEnabled(false);
            date_herbicide_date_tres.setEnabled(false);
            date_emergence_date.setEnabled(false);
            date_spray_uno.setEnabled(false);
            date_spray_dos.setEnabled(false);
            date_spray_tres.setEnabled(false);
            date_spray_cuatro.setEnabled(false);
            date_spray_cuatro_ha.setEnabled(false);
            date_bruchus.setEnabled(false);*/
            date_date_foliar.setEnabled(false);
        }

    }

    private void levantarFecha(final EditText edit){
        String fecha = Utilidades.fechaActualSinHora();
        String[] fechaRota = fecha.split("-");
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                month = month + 1;

                String mes = "", dia;

                if (month < 10) {
                    mes = "0" + month;
                } else {
                    mes = String.valueOf(month);
                }
                if (dayOfMonth < 10) dia = "0" + dayOfMonth;
                else dia = String.valueOf(dayOfMonth);

                String finalDate = dia + "-" + mes + "-" + year;
                edit.setText(finalDate);
            }
        }, Integer.parseInt(fechaRota[0]), (Integer.parseInt(fechaRota[1]) - 1), Integer.parseInt(fechaRota[2]));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }


    private void bind(View view){


        /* EDIT TEXT DE FECHAS */
        date_siembra_sag = view.findViewById(R.id.date_siembra_sag);
        date_sowing_female_start = view.findViewById(R.id.date_sowing_female_start);
        date_sowing_female_end = view.findViewById(R.id.date_sowing_female_end);
/*        date_date_one = view.findViewById(R.id.date_date_one);
        date_date_two = view.findViewById(R.id.date_date_two);
        date_herbicide_date_one = view.findViewById(R.id.date_herbicide_date_one);
        date_herbicide_date_two = view.findViewById(R.id.date_herbicide_date_two);
        date_herbicide_date_tres = view.findViewById(R.id.date_herbicide_date_tres);
        date_emergence_date = view.findViewById(R.id.date_emergence_date);
        date_spray_uno = view.findViewById(R.id.date_spray_uno);
        date_spray_dos = view.findViewById(R.id.date_spray_dos);
        date_spray_tres = view.findViewById(R.id.date_spray_tres);
        date_spray_cuatro = view.findViewById(R.id.date_spray_cuatro);
        date_spray_cuatro_ha = view.findViewById(R.id.date_spray_cuatro_ha);
        date_bruchus = view.findViewById(R.id.date_bruchus);*/
        date_date_foliar = view.findViewById(R.id.date_date_foliar);


        /*RECYCLERS*/
        recycler_crop = view.findViewById(R.id.recycler_crop);


        /* BOTON PARA LEVANTAR CALENDARIO*/
/*        btn_fecha_siembra_sag = view.findViewById(R.id.btn_fecha_siembra_sag);
        btn_sowing_female_start = view.findViewById(R.id.btn_sowing_female_start);
        btn_sowing_female_end = view.findViewById(R.id.btn_sowing_female_end);
        btn_date_one = view.findViewById(R.id.btn_date_one);
        btn_date_two = view.findViewById(R.id.btn_date_two);
        btn_herbicide_date_one = view.findViewById(R.id.btn_herbicide_date_one);
        btn_herbicide_date_two = view.findViewById(R.id.btn_herbicide_date_two);
        btn_herbicide_date_tres = view.findViewById(R.id.btn_herbicide_date_tres);
        btn_emergence_date = view.findViewById(R.id.btn_emergence_date);
        btn_spray_uno = view.findViewById(R.id.btn_spray_uno);
        btn_spray_dos = view.findViewById(R.id.btn_spray_dos);
        btn_spray_tres = view.findViewById(R.id.btn_spray_tres);
        btn_spray_cuatro = view.findViewById(R.id.btn_spray_cuatro);
        btn_spray_cuatro_ha = view.findViewById(R.id.btn_spray_cuatro_ha);
        btn_bruchus = view.findViewById(R.id.btn_bruchus);
        btn_date_foliar = view.findViewById(R.id.btn_date_foliar);*/


        /* TextView */
        et_norting = view.findViewById(R.id.et_norting);
        et_easting = view.findViewById(R.id.et_easting);



//        et_2015 = view.findViewById(R.id.et_2015);
//        et_2016 = view.findViewById(R.id.et_2016);
//        et_2017 = view.findViewById(R.id.et_2017);
//        et_2018 = view.findViewById(R.id.et_2018);
        et_entregado = view.findViewById(R.id.et_entregado);
        et_tipo_mezcla = view.findViewById(R.id.et_tipo_mezcla);
        et_cant_aplicada = view.findViewById(R.id.et_cant_aplicada);
        et_meters = view.findViewById(R.id.et_meters);
        et_lines_female = view.findViewById(R.id.et_lines_female);
        et_showing_female = view.findViewById(R.id.et_showing_female);
        et_real_sowing = view.findViewById(R.id.et_real_sowing);
        et_sowin_seed_f = view.findViewById(R.id.et_sowin_seed_f);
        et_row_distance = view.findViewById(R.id.et_row_distance);
/*        et_dose_one = view.findViewById(R.id.et_dose_one);
        et_dose_two = view.findViewById(R.id.et_dose_two);
        et_name_one = view.findViewById(R.id.et_name_one);
        et_name_two = view.findViewById(R.id.et_name_two);

        et_name_tres = view.findViewById(R.id.et_name_tres);
        et_emergence_dose = view.findViewById(R.id.et_emergence_dose);
        et_water_lts = view.findViewById(R.id.et_water_lts);*/
        et_f_plant = view.findViewById(R.id.et_f_plant);
        et_f_population = view.findViewById(R.id.et_f_population);
/*        et_producto_bruchus = view.findViewById(R.id.et_producto_bruchus);
        et_dose_lt_ha = view.findViewById(R.id.et_dose_lt_ha);*/
        et_foliar = view.findViewById(R.id.et_foliar);
        et_dose_foliar = view.findViewById(R.id.et_dose_foliar);


        et_dose_foliar.setSelectAllOnFocus(true);
        et_foliar.setSelectAllOnFocus(true);
/*        et_dose_lt_ha.setSelectAllOnFocus(true);
        et_producto_bruchus.setSelectAllOnFocus(true);*/
        et_f_population.setSelectAllOnFocus(true);
        et_f_plant.setSelectAllOnFocus(true);
/*        et_water_lts.setSelectAllOnFocus(true);
        et_emergence_dose.setSelectAllOnFocus(true);
        et_name_tres.setSelectAllOnFocus(true);
        et_name_two.setSelectAllOnFocus(true);
        et_name_one.setSelectAllOnFocus(true);
        et_dose_two.setSelectAllOnFocus(true);
        et_dose_one.setSelectAllOnFocus(true);*/
        et_row_distance.setSelectAllOnFocus(true);
        et_sowin_seed_f.setSelectAllOnFocus(true);
        et_real_sowing.setSelectAllOnFocus(true);
        et_showing_female.setSelectAllOnFocus(true);
        et_lines_female.setSelectAllOnFocus(true);
        et_meters.setSelectAllOnFocus(true);
        et_cant_aplicada.setSelectAllOnFocus(true);
        et_tipo_mezcla.setSelectAllOnFocus(true);
        et_entregado.setSelectAllOnFocus(true);
        et_easting.setSelectAllOnFocus(true);
        et_norting.setSelectAllOnFocus(true);



        date_siembra_sag.setOnFocusChangeListener(this);
        date_sowing_female_start.setOnFocusChangeListener(this);
        date_sowing_female_end.setOnFocusChangeListener(this);
/*        date_date_one.setOnFocusChangeListener(this);
        date_date_two.setOnFocusChangeListener(this);
        date_herbicide_date_one.setOnFocusChangeListener(this);
        date_herbicide_date_two.setOnFocusChangeListener(this);
        date_herbicide_date_tres.setOnFocusChangeListener(this);
        date_emergence_date.setOnFocusChangeListener(this);
        date_spray_uno.setOnFocusChangeListener(this);
        date_spray_dos.setOnFocusChangeListener(this);
        date_spray_tres.setOnFocusChangeListener(this);
        date_spray_cuatro.setOnFocusChangeListener(this);
        date_spray_cuatro_ha.setOnFocusChangeListener(this);
        date_bruchus.setOnFocusChangeListener(this);*/
        date_date_foliar.setOnFocusChangeListener(this);


        et_entregado.setOnFocusChangeListener(this);
        et_tipo_mezcla.setOnFocusChangeListener(this);
        et_cant_aplicada.setOnFocusChangeListener(this);
        et_meters.setOnFocusChangeListener(this);
        et_lines_female.setOnFocusChangeListener(this);
        et_showing_female.setOnFocusChangeListener(this);
        et_real_sowing.setOnFocusChangeListener(this);
        et_sowin_seed_f.setOnFocusChangeListener(this);
        et_row_distance.setOnFocusChangeListener(this);
/*        et_dose_one.setOnFocusChangeListener(this);
        et_dose_two.setOnFocusChangeListener(this);
        et_name_one.setOnFocusChangeListener(this);
        et_name_two.setOnFocusChangeListener(this);
        et_name_tres.setOnFocusChangeListener(this);
        et_emergence_dose.setOnFocusChangeListener(this);
        et_water_lts.setOnFocusChangeListener(this);*/
        et_f_plant.setOnFocusChangeListener(this);
        et_f_population.setOnFocusChangeListener(this);
/*        et_producto_bruchus.setOnFocusChangeListener(this);
        et_dose_lt_ha.setOnFocusChangeListener(this);*/
        et_foliar.setOnFocusChangeListener(this);
        et_dose_foliar.setOnFocusChangeListener(this);


    }

    @Override
    public void onLocationChanged(Location location) {
        et_norting.setText(String.valueOf(location.getLatitude()));
        et_easting.setText(String.valueOf(location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()){
            case R.id.date_siembra_sag:
                if(b) levantarFecha(date_siembra_sag);
                else temp_sowing.setSag_planting_temp_sowing(Utilidades.voltearFechaBD(date_siembra_sag.getText().toString()));
                break;
            case R.id.date_sowing_female_start:
                if(b) levantarFecha(date_sowing_female_start);
                else temp_sowing.setFemale_sowing_date_start_temp_sowing(Utilidades.voltearFechaBD(date_sowing_female_start.getText().toString()));
                break;
            case R.id.date_sowing_female_end:
                if(b) levantarFecha(date_sowing_female_end);
                else temp_sowing.setFemale_sowing_date_end_temp_sowing(Utilidades.voltearFechaBD(date_sowing_female_end.getText().toString()));
                break;
/*            case R.id.date_date_one:
                if(b) levantarFecha(date_date_one);
                else temp_sowing.setDose_1_temp_sowing(Utilidades.voltearFechaBD(date_date_one.getText().toString()));
                break;
            case R.id.date_date_two:
                if(b) levantarFecha(date_date_two);
                else temp_sowing.setDose_2_temp_sowing(Utilidades.voltearFechaBD(date_date_two.getText().toString()));
                break;
            case R.id.date_herbicide_date_one:
                if(b) levantarFecha(date_herbicide_date_one);
                else temp_sowing.setDate_1_herb_temp_sowing(Utilidades.voltearFechaBD(date_herbicide_date_one.getText().toString()));
                break;
            case R.id.date_herbicide_date_two:
                if(b) levantarFecha(date_herbicide_date_two);
                else temp_sowing.setDate_2_herb_temp_sowing(Utilidades.voltearFechaBD(date_herbicide_date_two.getText().toString()));
                break;
            case R.id.date_herbicide_date_tres:
                if(b) levantarFecha(date_herbicide_date_tres);
                else temp_sowing.setDate_3_herb_temp_sowing(Utilidades.voltearFechaBD(date_herbicide_date_tres.getText().toString()));
                break;
            case R.id.date_emergence_date:
                if(b) levantarFecha(date_emergence_date);
                else temp_sowing.setDate_pre_emergence_temp_sowing(Utilidades.voltearFechaBD(date_emergence_date.getText().toString()));
                break;
            case R.id.date_spray_uno:
                if(b) levantarFecha(date_spray_uno);
                else temp_sowing.setBasta_spray_1_temp_sowing(Utilidades.voltearFechaBD(date_spray_uno.getText().toString()));
                break;
            case R.id.date_spray_dos:
                if(b) levantarFecha(date_spray_dos);
                else temp_sowing.setBasta_spray_2_temp_sowing(Utilidades.voltearFechaBD(date_spray_dos.getText().toString()));
                break;
            case R.id.date_spray_tres:
                if(b) levantarFecha(date_spray_tres);
                else temp_sowing.setBasta_spray_3_temp_sowing(Utilidades.voltearFechaBD(date_spray_tres.getText().toString()));
                break;
            case R.id.date_spray_cuatro:
                if(b) levantarFecha(date_spray_cuatro);
                else temp_sowing.setBasta_spray_4_temp_sowing(Utilidades.voltearFechaBD(date_spray_cuatro.getText().toString()));
                break;
            case R.id.date_spray_cuatro_ha:
                if(b) levantarFecha(date_spray_cuatro_ha);
                else temp_sowing.setBasta_splat_4_ha_temp_sowing(Utilidades.voltearFechaBD(date_spray_cuatro_ha.getText().toString()));
                break;
            case R.id.date_bruchus:
                if(b) levantarFecha(date_bruchus);
                else temp_sowing.setDate_nombre_largo_temp_sowing(Utilidades.voltearFechaBD(date_bruchus.getText().toString()));
                break;*/
            case R.id.date_date_foliar:
                if(b) levantarFecha(date_date_foliar);
                else temp_sowing.setDate_foliar_temp_sowing(Utilidades.voltearFechaBD(date_date_foliar.getText().toString()));
                break;
            case R.id.et_entregado:
                if(!b) temp_sowing.setDelivered_temp_sowing(et_entregado.getText().toString());
                break;
            case R.id.et_tipo_mezcla:
                if(!b) temp_sowing.setType_of_mixture_applied_temp_sowing(et_tipo_mezcla.getText().toString());
                break;
            case R.id.et_cant_aplicada:
                if(!b) temp_sowing.setAmount_applied_temp_sowing(et_cant_aplicada.getText().toString());
                break;
            case R.id.et_meters:
                if(!b) temp_sowing.setMeters_isoliation_temp_sowing(Integer.parseInt(et_meters.getText().toString()));
                break;
            case R.id.et_lines_female:
                if(!b) temp_sowing.setFemale_lines_temp_sowing(et_lines_female.getText().toString());
                break;
            case R.id.et_showing_female:
                if(!b) temp_sowing.setDate_foliar_temp_sowing(et_showing_female.getText().toString());
                break;
            case R.id.et_real_sowing:
                if(!b) temp_sowing.setReal_sowing_female_temp_sowing(Double.parseDouble(et_real_sowing.getText().toString()));
                break;
            case R.id.et_sowin_seed_f:
                if(!b) temp_sowing.setReal_sowing_female_temp_sowing(Double.parseDouble(et_sowin_seed_f.getText().toString()));
                break;
            case R.id.et_row_distance:
                if(!b) temp_sowing.setRow_distance_temp_sowing(Double.parseDouble(et_row_distance.getText().toString()));
                break;
/*            case R.id.et_dose_one:
                if(!b) temp_sowing.setDose_1_temp_sowing(et_dose_one.getText().toString());
                break;
            case R.id.et_dose_two:
                if(!b) temp_sowing.setDose_2_temp_sowing(et_dose_two.getText().toString());
                break;
            case R.id.et_name_one:
                if(!b) temp_sowing.setName_1_herb_temp_sowing(et_name_one.getText().toString());
                break;*/
            /*case R.id.et_name_two:
                if(!b) temp_sowing.setName_2_herb_temp_sowing(et_name_two.getText().toString());
                break;*/
            /*case R.id.et_name_tres:
                if(!b) temp_sowing.setName_3_herb_temp_sowing(et_name_tres.getText().toString());
                break;*/
            /*case R.id.et_emergence_dose:
                if(!b) temp_sowing.setDose_pre_emergence_temp_sowing(et_emergence_dose.getText().toString());
                break;*/
            /*case R.id.et_water_lts:
                if(!b) temp_sowing.setWater_pre_emergence_temp_sowing(Double.parseDouble(et_water_lts.getText().toString()));
                break;*/
            case R.id.et_f_plant:
                if(!b) temp_sowing.setPlant_m_temp_sowing(Double.parseDouble(et_f_plant.getText().toString()));
                break;
            case R.id.et_f_population:
                if(!b) temp_sowing.setPopulation_plants_ha_temp_sowing(Double.parseDouble(et_f_population.getText().toString()));
                break;
            /*case R.id.et_producto_bruchus:
                if(!b) temp_sowing.setProduct_nombre_largo_temp_sowing(et_producto_bruchus.getText().toString());
                break;*/
            /*case R.id.et_dose_lt_ha:
                if(!b) temp_sowing.setDose_nombre_largo_temp_sowing(et_dose_lt_ha.getText().toString());
                break;*/
            case R.id.et_foliar:
                if(!b) temp_sowing.setFoliar_temp_sowing(et_foliar.getText().toString());
                break;
            case R.id.et_dose_foliar:
                if(!b) temp_sowing.setDose_foliar_temp_sowing(et_dose_foliar.getText().toString());
                break;

        }
        if (!b){
            MainActivity.myAppDB.myDao().updateTempSowing(temp_sowing);
        }
    }
}
