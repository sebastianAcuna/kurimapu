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
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
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

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
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
import cl.smapdev.curimapu.clases.tablas.pro_cli_mat;
import cl.smapdev.curimapu.clases.temporales.TempSowing;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static androidx.core.content.ContextCompat.checkSelfPermission;


public class FragmentSowing extends Fragment implements View.OnFocusChangeListener {


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

    private View Globalview;

    private ArrayList<ArrayList<Integer>> global = null;
    private ArrayList<Integer> id_importante = null;
    private ArrayList<Integer> id_generica = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity a = (MainActivity) getActivity();
        if (a != null) {
            activity = a;
        }

        prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);

        id_importante = new ArrayList<>();
        global = new ArrayList<>();
        id_generica = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Globalview = inflater.inflate(R.layout.fragment_sowing, container, false);
        return Globalview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind(view);
        global = Utilidades.cargarUI(Globalview,R.id.relative_constraint_sowing, getActivity(), prefs.getInt(Utilidades.SHARED_VISIT_MATERIAL_ID,0), 0);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (activity != null){
                global = Utilidades.cargarUI(Globalview,R.id.relative_constraint_sowing, getActivity(), prefs.getInt(Utilidades.SHARED_VISIT_MATERIAL_ID,0), 0);
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

/*    private void cargarCrop(){

        List<CropRotation> cropList = MainActivity.myAppDB.myDao().getCropRotation(temp_sowing.getId_anexo_temp_sowing());
        CropRotationAdapter cropAdapter = new CropRotationAdapter(cropList, activity);
        recycler_crop.setAdapter(cropAdapter);
    }*/


    private void loadUiData(){
        try{

            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fotos_sowing, FragmentFotos.getInstance(2), Utilidades.FRAGMENT_FOTOS).commit();
        }catch (IllegalArgumentException e){
            Log.e("ILEGAL EXCEPTION SOWING", e.getMessage());
        }

/*
        cargarCrop();

        date_siembra_sag.setText(Utilidades.voltearFechaVista(temp_sowing.getSag_planting_temp_sowing()));
        date_sowing_female_start.setText(Utilidades.voltearFechaVista(temp_sowing.getFemale_sowing_date_start_temp_sowing()));
        date_sowing_female_end.setText(Utilidades.voltearFechaVista(temp_sowing.getFemale_sowing_date_end_temp_sowing()));
        date_date_foliar.setText(Utilidades.voltearFechaVista(temp_sowing.getDate_foliar_temp_sowing()));

        *//* ET TEXTO *//*


        et_entregado.setText(temp_sowing.getDelivered_temp_sowing());
        et_tipo_mezcla.setText(temp_sowing.getType_of_mixture_applied_temp_sowing());
        et_cant_aplicada.setText(temp_sowing.getAmount_applied_temp_sowing());
        et_lines_female.setText(temp_sowing.getFemale_lines_temp_sowing());
        et_showing_female.setText(temp_sowing.getFemale_sowing_lot_temp_sowing());
        et_foliar.setText(temp_sowing.getFoliar_temp_sowing());
        et_dose_foliar.setText(temp_sowing.getDose_foliar_temp_sowing());


        et_meters.setText(String.valueOf(temp_sowing.getMeters_isoliation_temp_sowing()));
        et_real_sowing.setText(String.valueOf(temp_sowing.getReal_sowing_female_temp_sowing()));
        et_sowin_seed_f.setText(String.valueOf(temp_sowing.getSowing_seed_meter_temp_sowing()));
        et_row_distance.setText(String.valueOf(temp_sowing.getRow_distance_temp_sowing()));
        et_f_plant.setText(String.valueOf(temp_sowing.getPlant_m_temp_sowing()));
        et_f_population.setText(String.valueOf(temp_sowing.getPopulation_plants_ha_temp_sowing()));


        if (temp_sowing.getAction_temp_sowing() == 2){
            et_f_population.setEnabled(false);
            et_f_plant.setEnabled(false);
            et_row_distance.setEnabled(false);
            et_sowin_seed_f.setEnabled(false);
            et_real_sowing.setEnabled(false);
            et_meters.setEnabled(false);
            et_dose_foliar.setEnabled(false);
            et_foliar.setEnabled(false);
            et_showing_female.setEnabled(false);
            et_lines_female.setEnabled(false);
            et_cant_aplicada.setEnabled(false);
            et_tipo_mezcla.setEnabled(false);
            et_entregado.setEnabled(false);
            date_siembra_sag.setEnabled(false);
            date_sowing_female_start.setEnabled(false);
            date_sowing_female_end.setEnabled(false);
            date_date_foliar.setEnabled(false);
        }*/

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


/*        *//* EDIT TEXT DE FECHAS *//*
        date_siembra_sag = view.findViewById(R.id.date_siembra_sag);
        date_sowing_female_start = view.findViewById(R.id.date_sowing_female_start);
        date_sowing_female_end = view.findViewById(R.id.date_sowing_female_end);
        date_date_foliar = view.findViewById(R.id.date_date_foliar);


        *//*RECYCLERS*//*
        recycler_crop = view.findViewById(R.id.recycler_crop);

        *//* TextView *//*
        et_norting = view.findViewById(R.id.et_norting);
        et_easting = view.findViewById(R.id.et_easting);


        et_entregado = view.findViewById(R.id.et_entregado);
        et_tipo_mezcla = view.findViewById(R.id.et_tipo_mezcla);
        et_cant_aplicada = view.findViewById(R.id.et_cant_aplicada);
        et_meters = view.findViewById(R.id.et_meters);
        et_lines_female = view.findViewById(R.id.et_lines_female);
        et_showing_female = view.findViewById(R.id.et_showing_female);
        et_real_sowing = view.findViewById(R.id.et_real_sowing);
        et_sowin_seed_f = view.findViewById(R.id.et_sowin_seed_f);
        et_row_distance = view.findViewById(R.id.et_row_distance);
        et_f_plant = view.findViewById(R.id.et_f_plant);
        et_f_population = view.findViewById(R.id.et_f_population);
        et_foliar = view.findViewById(R.id.et_foliar);
        et_dose_foliar = view.findViewById(R.id.et_dose_foliar);


        et_dose_foliar.setSelectAllOnFocus(true);
        et_foliar.setSelectAllOnFocus(true);
        et_f_population.setSelectAllOnFocus(true);
        et_f_plant.setSelectAllOnFocus(true);
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
        et_f_plant.setOnFocusChangeListener(this);
        et_f_population.setOnFocusChangeListener(this);
        et_foliar.setOnFocusChangeListener(this);
        et_dose_foliar.setOnFocusChangeListener(this);*/

    }

    @Override
    public void onFocusChange(View view, boolean b) {
       /* switch (view.getId()){
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
            case R.id.et_f_plant:
                if(!b) temp_sowing.setPlant_m_temp_sowing(Double.parseDouble(et_f_plant.getText().toString()));
                break;
            case R.id.et_f_population:
                if(!b) temp_sowing.setPopulation_plants_ha_temp_sowing(Double.parseDouble(et_f_population.getText().toString()));
                break;
            case R.id.et_foliar:
                if(!b) temp_sowing.setFoliar_temp_sowing(et_foliar.getText().toString());
                break;
            case R.id.et_dose_foliar:
                if(!b) temp_sowing.setDose_foliar_temp_sowing(et_dose_foliar.getText().toString());
                break;

        }
        if (!b){
            MainActivity.myAppDB.myDao().updateTempSowing(temp_sowing);
        }*/
    }
}
