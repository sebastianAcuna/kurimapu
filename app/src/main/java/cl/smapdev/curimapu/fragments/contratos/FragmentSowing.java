package cl.smapdev.curimapu.fragments.contratos;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;


public class FragmentSowing extends Fragment implements View.OnClickListener, LocationListener {



    private MainActivity activity = null;


    private String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private LocationManager locationManager;



    private EditText date_siembra_sag, date_sowing_female_start, date_sowing_female_end,date_date_one,date_date_two,date_herbicide_date_one,date_herbicide_date_two,date_herbicide_date_tres,
            date_emergence_date,date_spray_uno,date_spray_dos,date_spray_tres,date_spray_cuatro,date_spray_cuatro_ha,date_bruchus,date_date_foliar;

    private ImageButton btn_fecha_siembra_sag, btn_sowing_female_start, btn_sowing_female_end,btn_date_one,btn_date_two,btn_herbicide_date_one,btn_herbicide_date_two,btn_herbicide_date_tres,
            btn_emergence_date,btn_spray_uno,btn_spray_dos,btn_spray_tres,btn_spray_cuatro,btn_spray_cuatro_ha,btn_bruchus,btn_date_foliar;


    private EditText et_norting ,et_easting, et_2015, et_2016, et_2017, et_2018, et_entregado,
    et_tipo_mezcla, et_cant_aplicada, et_meters, et_lines_female, et_showing_female,et_real_sowing,et_sowin_seed_f,et_row_distance,et_dose_one,et_dose_two,et_name_one,et_name_two,
    et_name_tres,et_emergence_dose,et_water_lts,et_f_plant,et_f_population,et_producto_bruchus,et_dose_lt_ha,et_foliar,et_dose_foliar;


    private DatePickerDialog datePickerDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity a = (MainActivity) getActivity();
        if (a != null) {
            activity = a;
        }
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








        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
                            if (checkPermission()) {
                                requestPermission();
                            }
                        }
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,FragmentSowing.this);
                    }
                });

            }
        });







    }

    private boolean checkPermission() {


        if (activity != null){
            int result = ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
            int result1 = ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }else{
            return false;
        }


    }


    private void requestPermission() {

        if (activity != null){
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(activity, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                }
            }
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

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_fecha_siembra_sag:
                levantarFecha(date_siembra_sag);
                break;
            case R.id.btn_sowing_female_start:
                levantarFecha(date_sowing_female_start);
                break;
            case R.id.btn_sowing_female_end:
                levantarFecha(date_sowing_female_end);
                break;
            case R.id.btn_date_one:
                levantarFecha(date_date_one);
                break;
            case R.id.btn_date_two:
                levantarFecha(date_date_two);
                break;
            case R.id.btn_herbicide_date_one:
                levantarFecha(date_herbicide_date_one);
                break;
            case R.id.btn_herbicide_date_two:
                levantarFecha(date_herbicide_date_two);
                break;
            case R.id.btn_herbicide_date_tres:
                levantarFecha(date_herbicide_date_tres);
                break;
            case R.id.btn_emergence_date:
                levantarFecha(date_emergence_date);
                break;
            case R.id.btn_spray_uno:
                levantarFecha(date_spray_uno);
                break;
            case R.id.btn_spray_dos:
                levantarFecha(date_spray_dos);
                break;
            case R.id.btn_spray_tres:
                levantarFecha(date_spray_tres);
                break;
            case R.id.btn_spray_cuatro:
                levantarFecha(date_spray_cuatro);
                break;
            case R.id.btn_spray_cuatro_ha:
                levantarFecha(date_spray_cuatro_ha);
                break;
            case R.id.btn_bruchus:
                levantarFecha(date_bruchus);
                break;
            case R.id.btn_date_foliar:
                levantarFecha(date_date_foliar);
                break;
        }

    }


    private void bind(View view){


        /* EDIT TEXT DE FECHAS */
        date_siembra_sag = view.findViewById(R.id.date_siembra_sag);
        date_sowing_female_start = view.findViewById(R.id.date_sowing_female_start);
        date_sowing_female_end = view.findViewById(R.id.date_sowing_female_end);
        date_date_one = view.findViewById(R.id.date_date_one);
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
        date_bruchus = view.findViewById(R.id.date_bruchus);
        date_date_foliar = view.findViewById(R.id.date_date_foliar);



        /* BOTON PARA LEVANTAR CALENDARIO*/
        btn_fecha_siembra_sag = view.findViewById(R.id.btn_fecha_siembra_sag);
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
        btn_date_foliar = view.findViewById(R.id.btn_date_foliar);



        et_norting = view.findViewById(R.id.et_norting);
        et_easting = view.findViewById(R.id.et_easting);
        et_2015 = view.findViewById(R.id.et_2015);
        et_2016 = view.findViewById(R.id.et_2016);
        et_2017 = view.findViewById(R.id.et_2017);
        et_2018 = view.findViewById(R.id.et_2018);
        et_entregado = view.findViewById(R.id.et_entregado);
        et_tipo_mezcla = view.findViewById(R.id.et_tipo_mezcla);
        et_cant_aplicada = view.findViewById(R.id.et_cant_aplicada);
        et_meters = view.findViewById(R.id.et_meters);
        et_lines_female = view.findViewById(R.id.et_lines_female);
        et_showing_female = view.findViewById(R.id.et_showing_female);
        et_real_sowing = view.findViewById(R.id.et_real_sowing);
        et_sowin_seed_f = view.findViewById(R.id.et_sowin_seed_f);
        et_row_distance = view.findViewById(R.id.et_row_distance);
        et_dose_one = view.findViewById(R.id.et_dose_one);
        et_dose_two = view.findViewById(R.id.et_dose_two);
        et_name_one = view.findViewById(R.id.et_name_one);
        et_name_two = view.findViewById(R.id.et_name_two);
        et_name_tres = view.findViewById(R.id.et_name_tres);
        et_emergence_dose = view.findViewById(R.id.et_emergence_dose);
        et_water_lts = view.findViewById(R.id.et_water_lts);
        et_f_plant = view.findViewById(R.id.et_f_plant);
        et_f_population = view.findViewById(R.id.et_f_population);
        et_producto_bruchus = view.findViewById(R.id.et_producto_bruchus);
        et_dose_lt_ha = view.findViewById(R.id.et_dose_lt_ha);
        et_foliar = view.findViewById(R.id.et_foliar);
        et_dose_foliar = view.findViewById(R.id.et_dose_foliar);




        btn_fecha_siembra_sag.setOnClickListener(this);
        btn_sowing_female_start.setOnClickListener(this);
        btn_sowing_female_end.setOnClickListener(this);
        btn_date_one.setOnClickListener(this);
        btn_date_two.setOnClickListener(this);
        btn_herbicide_date_one.setOnClickListener(this);
        btn_herbicide_date_two.setOnClickListener(this);
        btn_herbicide_date_tres.setOnClickListener(this);
        btn_emergence_date.setOnClickListener(this);
        btn_spray_uno.setOnClickListener(this);
        btn_spray_dos.setOnClickListener(this);
        btn_spray_tres.setOnClickListener(this);
        btn_spray_cuatro.setOnClickListener(this);
        btn_spray_cuatro_ha.setOnClickListener(this);
        btn_bruchus.setOnClickListener(this);
        btn_date_foliar.setOnClickListener(this);
        btn_date_foliar.setOnClickListener(this);

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
}
