package cl.smapdev.curimapu.fragments.contratos;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.temporales.TempFlowering;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class FragmentFlowering extends Fragment implements View.OnFocusChangeListener/*, DatePickerDialog.OnDateSetListener*/{


    private EditText date_date,date_flowering,date_f_start,date_f_end,date_fert_one,date_fert_two,
            date_fungicide,date_insecticide,date_depuration,date_off_type,date_inspection;

/*    private ImageButton btn_fecha_date,btn_fecha_flowering,btn_fecha_start,btn_fecha_end,btn_fecha_fert_one,btn_fecha_fert_two,
            btn_fecha_fungicide,btn_fecha_insecticide,btn_fecha_depuration,btn_fecha_off_type,btn_fecha_inspection;*/


    private EditText et_fungicide,et_doce,et_plant_number_checked,et_check,et_main_char;

    private MainActivity activity = null;

    private SharedPreferences prefs;

    private TempFlowering temp_flowering;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         MainActivity a = (MainActivity) getActivity();
         if(a != null){
             activity = a;

         }

         prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flowering, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        bind(view);
        setOnclick();

        cargarTemp();

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
        temp_flowering  = (temp_flowering != null) ? temp_flowering : MainActivity.myAppDB.myDao().getTempFlowering(prefs.getInt(Utilidades.SHARED_VISIT_ANEXO_ID, 0));


        if (temp_flowering == null) {
            temp_flowering = new TempFlowering();
            if (prefs != null) {
                temp_flowering.setId_anexo_temp_flowering(prefs.getInt(Utilidades.SHARED_VISIT_ANEXO_ID, 0));
            }
            MainActivity.myAppDB.myDao().setTempFlowering(temp_flowering);
            temp_flowering = MainActivity.myAppDB.myDao().getTempFlowering(temp_flowering.getId_anexo_temp_flowering());
            if (temp_flowering != null){
                loadUiData();
            }
        }else{
            loadUiData();
        }




    }


    private void loadUiData(){

        try{
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fotos_flowering, FragmentFotos.getInstance(3), Utilidades.FRAGMENT_FOTOS).commit();
        }catch (IllegalArgumentException e){
            Log.e("ILEGAL EXCEPTION SOWING", e.getMessage());
        }


        date_date.setText(Utilidades.voltearFechaVista(temp_flowering.getDate_notice_sag_temp_flowering()));
        date_flowering.setText(Utilidades.voltearFechaVista(temp_flowering.getFlowering_estimation_temp_flowering()));
        date_f_start.setText(Utilidades.voltearFechaVista(temp_flowering.getFlowering_start_temp_flowering()));
        date_f_end.setText(Utilidades.voltearFechaVista(temp_flowering.getFlowering_end_temp_flowering()));
        date_fert_one.setText(Utilidades.voltearFechaVista(temp_flowering.getFertility_1_temp_flowering()));
        date_fert_two.setText(Utilidades.voltearFechaVista(temp_flowering.getFertility_2_temp_flowering()));
        date_fungicide.setText(Utilidades.voltearFechaVista(temp_flowering.getDate_funficide_temp_flowering()));
        date_insecticide.setText(Utilidades.voltearFechaVista(temp_flowering.getDate_insecticide_temp_flowering()));
        date_depuration.setText(Utilidades.voltearFechaVista(temp_flowering.getDate_beginning_depuration_temp_flowering()));
        date_off_type.setText(Utilidades.voltearFechaVista(temp_flowering.getDate_off_type_temp_flowering()));
        date_inspection.setText(Utilidades.voltearFechaVista(temp_flowering.getDate_inspection_temp_flowering()));


        /* ET TEXTO */

        et_fungicide.setText(temp_flowering.getFungicide_name_temp_flowering());
        et_doce.setText(temp_flowering.getDose_fungicide_temp_flowering());
        et_plant_number_checked.setText(temp_flowering.getPlant_number_checked_temp_flowering());
        et_check.setText(temp_flowering.getCheck_temp_flowering());
        et_main_char.setText(temp_flowering.getMain_characteristic_temp_flowering());

        if (temp_flowering.getAction_temp_flowering() == 2){
            date_date.setEnabled(false);
            date_flowering.setEnabled(false);
            date_f_start.setEnabled(false);
            date_f_end.setEnabled(false);
            date_fert_one.setEnabled(false);
            date_fert_two.setEnabled(false);
            date_fungicide.setEnabled(false);
            date_insecticide.setEnabled(false);
            date_depuration.setEnabled(false);
            date_off_type.setEnabled(false);
            date_inspection.setEnabled(false);
            et_fungicide.setEnabled(false);
            et_doce.setEnabled(false);
            et_plant_number_checked.setEnabled(false);
            et_check.setEnabled(false);
            et_main_char.setEnabled(false);
        }
    }

    private void levantarFecha(final EditText edit){
        String fecha = Utilidades.fechaActualSinHora();
        String[] fechaRota = fecha.split("-");

        if (activity != null){
            DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
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

    }

    private void setOnclick(){
        date_date.setOnFocusChangeListener(this);
        date_flowering.setOnFocusChangeListener(this);
        date_f_start.setOnFocusChangeListener(this);
        date_f_end.setOnFocusChangeListener(this);
        date_fert_one.setOnFocusChangeListener(this);
        date_fert_two.setOnFocusChangeListener(this);
        date_fungicide.setOnFocusChangeListener(this);
        date_insecticide.setOnFocusChangeListener(this);
        date_depuration.setOnFocusChangeListener(this);
        date_off_type.setOnFocusChangeListener(this);
        date_inspection.setOnFocusChangeListener(this);


        et_fungicide.setOnFocusChangeListener(this);
        et_doce.setOnFocusChangeListener(this);
        et_plant_number_checked.setOnFocusChangeListener(this);
        et_check.setOnFocusChangeListener(this);
        et_main_char.setOnFocusChangeListener(this);



    }

    private void bind(View view){

        /* EDIT TEXT PARA FECHAS */
        date_date = view.findViewById(R.id.date_date);
        date_flowering = view.findViewById(R.id.date_flowering);
        date_f_start = view.findViewById(R.id.date_f_start);
        date_f_end = view.findViewById(R.id.date_f_end);
        date_fert_one = view.findViewById(R.id.date_fert_one);
        date_fert_two = view.findViewById(R.id.date_fert_two);
        date_fungicide = view.findViewById(R.id.date_fungicide);
        date_insecticide = view.findViewById(R.id.date_insecticide);
        date_depuration = view.findViewById(R.id.date_depuration);
        date_off_type = view.findViewById(R.id.date_off_type);
        date_inspection = view.findViewById(R.id.date_inspection);

        /* BOTON PARA MOSTRAR CALENDARIO */
      /*  btn_fecha_date = view.findViewById(R.id.btn_fecha_date);
        btn_fecha_flowering = view.findViewById(R.id.btn_fecha_flowering);
        btn_fecha_start = view.findViewById(R.id.btn_fecha_start);
        btn_fecha_end = view.findViewById(R.id.btn_fecha_end);
        btn_fecha_fert_one = view.findViewById(R.id.btn_fecha_fert_one);
        btn_fecha_fert_two = view.findViewById(R.id.btn_fecha_fert_two);
        btn_fecha_fungicide = view.findViewById(R.id.btn_fecha_fungicide);
        btn_fecha_insecticide = view.findViewById(R.id.btn_fecha_insecticide);
        btn_fecha_depuration = view.findViewById(R.id.btn_fecha_depuration);
        btn_fecha_off_type = view.findViewById(R.id.btn_fecha_off_type);
        btn_fecha_inspection = view.findViewById(R.id.btn_fecha_inspection);*/

      /*EDIT TEXT */
        et_fungicide = (EditText) view.findViewById(R.id.et_fungicide);
        et_doce = (EditText) view.findViewById(R.id.et_doce);
        et_plant_number_checked = (EditText) view.findViewById(R.id.et_plant_number_checked);
        et_check = (EditText) view.findViewById(R.id.et_check);
        et_main_char = (EditText) view.findViewById(R.id.et_main_char);


        et_fungicide.setSelectAllOnFocus(true);
        et_doce.setSelectAllOnFocus(true);
        et_plant_number_checked.setSelectAllOnFocus(true);
        et_check.setSelectAllOnFocus(true);
        et_main_char.setSelectAllOnFocus(true);


    }

    @Override
    public void onFocusChange(View view, boolean b) {

        switch (view.getId()){
            case R.id.date_date:
                if(b) levantarFecha(date_date);
                else temp_flowering.setDate_notice_sag_temp_flowering(Utilidades.voltearFechaBD(date_date.getText().toString()));
                break;
            case R.id.date_flowering:
                if(b) levantarFecha(date_flowering);
                else temp_flowering.setFlowering_estimation_temp_flowering(Utilidades.voltearFechaBD(date_flowering.getText().toString()));
                break;
            case R.id.date_f_start:
                if(b) levantarFecha(date_f_start);
                else temp_flowering.setFlowering_start_temp_flowering(Utilidades.voltearFechaBD(date_f_start.getText().toString()));
                break;
            case R.id.date_f_end:
                if(b) levantarFecha(date_f_end);
                else temp_flowering.setFlowering_end_temp_flowering(Utilidades.voltearFechaBD(date_f_end.getText().toString()));
                break;
            case R.id.date_fert_one:
                if(b) levantarFecha(date_fert_one);
                else temp_flowering.setFertility_1_temp_flowering(Utilidades.voltearFechaBD(date_fert_one.getText().toString()));
                break;
            case R.id.date_fert_two:
                if(b) levantarFecha(date_fert_two);
                else temp_flowering.setFertility_2_temp_flowering(Utilidades.voltearFechaBD(date_fert_two.getText().toString()));
                break;
            case R.id.date_fungicide:
                if(b) levantarFecha(date_fungicide);
                else temp_flowering.setDate_funficide_temp_flowering(Utilidades.voltearFechaBD(date_fungicide.getText().toString()));
                break;
            case R.id.date_insecticide:
                if(b) levantarFecha(date_insecticide);
                else temp_flowering.setDate_insecticide_temp_flowering(Utilidades.voltearFechaBD(date_insecticide.getText().toString()));
                break;
            case R.id.date_depuration:
                if(b) levantarFecha(date_depuration);
                else temp_flowering.setDate_beginning_depuration_temp_flowering(Utilidades.voltearFechaBD(date_depuration.getText().toString()));
                break;
            case R.id.date_off_type:
                if(b) levantarFecha(date_off_type);
                else temp_flowering.setDate_off_type_temp_flowering(Utilidades.voltearFechaBD(date_off_type.getText().toString()));
                break;
            case R.id.date_inspection:
                if(b) levantarFecha(date_inspection);
                else temp_flowering.setDate_inspection_temp_flowering(Utilidades.voltearFechaBD(date_inspection.getText().toString()));
                break;

            case R.id.et_fungicide:
                if (!b) temp_flowering.setFungicide_name_temp_flowering(et_fungicide.getText().toString());
                break;
            case R.id.et_doce:
                if (!b) temp_flowering.setDose_fungicide_temp_flowering(et_doce.getText().toString());
                break;
            case R.id.et_plant_number_checked:
                if (!b) temp_flowering.setPlant_number_checked_temp_flowering(et_plant_number_checked.getText().toString());
                break;
            case R.id.et_check:
                if (!b) temp_flowering.setCheck_temp_flowering(et_check.getText().toString());
                break;
            case R.id.et_main_char:
                if (!b) temp_flowering.setMain_characteristic_temp_flowering(et_main_char.getText().toString());
                break;
        }

        if (!b){
            MainActivity.myAppDB.myDao().updateTempFlowering(temp_flowering);
        }

    }
}
