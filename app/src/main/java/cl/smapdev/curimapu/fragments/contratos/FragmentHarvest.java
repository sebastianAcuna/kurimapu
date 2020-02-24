package cl.smapdev.curimapu.fragments.contratos;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.temporales.TempHarvest;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class FragmentHarvest extends Fragment implements View.OnFocusChangeListener {

    private EditText date_estimated_date,date_real_date,date_swatching_date,date_date_harvest,date_begining_date,date_end_date;

//    private ImageButton btn_estimated_date,btn_real_date,btn_swatching_date,btn_date_harvest,btn_begining_date,btn_end_date;

    private EditText et_observation_harvest,et_kg_ha,et_observation_yield,et_owner,et_model,et_percent;
    private MainActivity activity = null;

    private SharedPreferences prefs;

    private TempHarvest temp_harvest;

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
        return inflater.inflate(R.layout.fragment_harvest, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind(view);
        cargarTemp();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            MainActivity a = (MainActivity) getActivity();
            if (a != null) activity = a;

            Log.e("ON VISIBLE", "VISIBLE");
            if (activity != null) { cargarTemp(); }
        }
    }


    private void cargarTemp(){
        temp_harvest  = (temp_harvest != null) ? temp_harvest : MainActivity.myAppDB.myDao().getTempHarvest(prefs.getInt(Utilidades.SHARED_VISIT_ANEXO_ID, 0));


        if (temp_harvest == null) {
            temp_harvest = new TempHarvest();
            if (prefs != null) {
                temp_harvest.setId_anexo_temp_harvest(prefs.getInt(Utilidades.SHARED_VISIT_ANEXO_ID, 0));
            }
            MainActivity.myAppDB.myDao().setTempHarvest(temp_harvest);
            temp_harvest = MainActivity.myAppDB.myDao().getTempHarvest(temp_harvest.getId_anexo_temp_harvest());

            if (temp_harvest != null){
                loadUiData();
            }
        }else{
            loadUiData();
        }


    }


    private void loadUiData(){

        activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fotos_harvest, FragmentFotos.getInstance(4), Utilidades.FRAGMENT_FOTOS).commit();

        date_estimated_date.setText(Utilidades.voltearFechaVista(temp_harvest.getEstimated_date_temp_harvest()));
        date_real_date.setText(Utilidades.voltearFechaVista(temp_harvest.getReal_date_temp_harvest()));
        date_swatching_date.setText(Utilidades.voltearFechaVista(temp_harvest.getSwathing_date_temp_harvest()));
        date_date_harvest.setText(Utilidades.voltearFechaVista(temp_harvest.getDate_harvest_estimation_temp_harvest()));
        date_begining_date.setText(Utilidades.voltearFechaVista(temp_harvest.getBeginning_date_temp_harvest()));
        date_end_date.setText(Utilidades.voltearFechaVista(temp_harvest.getEnd_date_temp_harvest()));

        /* ET TEXTO */
        et_observation_harvest.setText(temp_harvest.getObservation_dessicant_temp_harvest());
        et_kg_ha.setText(temp_harvest.getKg_ha_yield_temp_harvest());
        et_observation_yield.setText(temp_harvest.getObservation_yield_temp_harvest());
        et_owner.setText(temp_harvest.getOwner_machine_temp_harvest());
        et_model.setText(temp_harvest.getModel_machine_temp_harvest());
        et_percent.setText(String.valueOf(temp_harvest.getPorcent_temp_harvest()));



        if(temp_harvest.getAction_temp_harvest() == 2){
            date_estimated_date.setEnabled(false);
            date_real_date.setEnabled(false);
            date_swatching_date.setEnabled(false);
            date_date_harvest.setEnabled(false);
            date_begining_date.setEnabled(false);
            date_end_date.setEnabled(false);
            et_observation_harvest.setEnabled(false);
            et_kg_ha.setEnabled(false);
            et_observation_yield.setEnabled(false);
            et_owner.setEnabled(false);
            et_model.setEnabled(false);
            et_percent.setEnabled(false);
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
        /*EDIT TEXT FECHAS */
        date_estimated_date  = (EditText) view.findViewById(R.id.date_estimated_date);
        date_real_date  = (EditText) view.findViewById(R.id.date_real_date);
        date_swatching_date  = (EditText) view.findViewById(R.id.date_swatching_date);
        date_date_harvest  = (EditText) view.findViewById(R.id.date_date_harvest);
        date_begining_date  = (EditText) view.findViewById(R.id.date_begining_date);
        date_end_date  = (EditText) view.findViewById(R.id.date_end_date);

        /*  BOTON PARA MOSTRAR CALENDARIO */
/*        btn_estimated_date  = (ImageButton) view.findViewById(R.id.btn_estimated_date);
        btn_real_date  = (ImageButton) view.findViewById(R.id.btn_real_date);
        btn_swatching_date  = (ImageButton) view.findViewById(R.id.btn_swatching_date);
        btn_date_harvest  = (ImageButton) view.findViewById(R.id.btn_date_harvest);
        btn_begining_date  = (ImageButton) view.findViewById(R.id.btn_begining_date);*/
//        btn_end_date  = (ImageButton) view.findViewById(R.id.btn_end_date);


        /* ET CONTAINER*/



        /* ET TEXTO */
        et_observation_harvest  = (EditText) view.findViewById(R.id.et_observation_harvest);
        et_kg_ha  = (EditText) view.findViewById(R.id.et_kg_ha);
        et_observation_yield  = (EditText) view.findViewById(R.id.et_observation_yield);
        et_owner  = (EditText) view.findViewById(R.id.et_owner);
        et_model  = (EditText) view.findViewById(R.id.et_model);
        et_percent  = (EditText) view.findViewById(R.id.et_percent);


        et_observation_harvest.setSelectAllOnFocus(true);
        et_kg_ha.setSelectAllOnFocus(true);
        et_observation_yield.setSelectAllOnFocus(true);
        et_owner.setSelectAllOnFocus(true);
        et_model.setSelectAllOnFocus(true);
        et_percent.setSelectAllOnFocus(true);





        date_estimated_date.setOnFocusChangeListener(this);
        date_real_date.setOnFocusChangeListener(this);
        date_swatching_date.setOnFocusChangeListener(this);
        date_date_harvest.setOnFocusChangeListener(this);
        date_begining_date.setOnFocusChangeListener(this);
        date_end_date.setOnFocusChangeListener(this);


        et_observation_harvest.setOnFocusChangeListener(this);
        et_kg_ha.setOnFocusChangeListener(this);
        et_observation_yield.setOnFocusChangeListener(this);
        et_owner.setOnFocusChangeListener(this);
        et_model.setOnFocusChangeListener(this);
        et_percent.setOnFocusChangeListener(this);

    }

    @Override
    public void onFocusChange(View view, boolean b) {

            switch (view.getId()){

                case R.id.date_estimated_date:
                    if(b)levantarFecha(date_estimated_date);
                    else  temp_harvest.setEstimated_date_temp_harvest(Utilidades.voltearFechaBD(date_estimated_date.getText().toString()));
                    break;
                case R.id.date_real_date:
                    if(b)levantarFecha(date_real_date);
                    else  temp_harvest.setReal_date_temp_harvest(Utilidades.voltearFechaBD(date_real_date.getText().toString()));
                    break;
                case R.id.date_swatching_date:
                    if(b)levantarFecha(date_swatching_date);
                    else  temp_harvest.setSwathing_date_temp_harvest(Utilidades.voltearFechaBD(date_swatching_date.getText().toString()));
                    break;
                case R.id.date_date_harvest:
                    if(b)levantarFecha(date_date_harvest);
                    else  temp_harvest.setDate_harvest_estimation_temp_harvest(Utilidades.voltearFechaBD(date_date_harvest.getText().toString()));
                    break;
                case R.id.date_begining_date:
                    if(b)levantarFecha(date_begining_date);
                    else  temp_harvest.setBeginning_date_temp_harvest(Utilidades.voltearFechaBD(date_begining_date.getText().toString()));
                    break;
                case R.id.date_end_date:
                    if(b)levantarFecha(date_end_date);
                    else temp_harvest.setEnd_date_temp_harvest(Utilidades.voltearFechaBD(date_end_date.getText().toString()));
                    break;
                case R.id.et_observation_harvest:
                    if(!b ) temp_harvest.setObservation_dessicant_temp_harvest(et_observation_harvest.getText().toString());
                    break;
                case R.id.et_kg_ha:
                    if(!b) temp_harvest.setKg_ha_yield_temp_harvest(et_kg_ha.getText().toString());
                    break;
                case R.id.et_observation_yield:
                    if(!b  ) temp_harvest.setObservation_yield_temp_harvest(et_observation_yield.getText().toString());
                    break;
                case R.id.et_owner:
                    if(!b ) temp_harvest.setOwner_machine_temp_harvest(et_owner.getText().toString());
                    break;
                case R.id.et_model:
                    if(!b ) temp_harvest.setModel_machine_temp_harvest(et_model.getText().toString());
                    break;
                case R.id.et_percent:
                    if(!b ) temp_harvest.setPorcent_temp_harvest(Double.parseDouble(et_percent.getText().toString()));
                    break;
            }

            if (!b){
                MainActivity.myAppDB.myDao().updateTempHarvest(temp_harvest);
            }

    }
}
