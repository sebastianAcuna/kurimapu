package cl.smapdev.curimapu.fragments.contratos;

import android.app.DatePickerDialog;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class FragmentHarvest extends Fragment implements View.OnFocusChangeListener {

    private EditText date_estimated_date,date_real_date,date_swatching_date,date_date_harvest,date_begining_date,date_end_date;

//    private ImageButton btn_estimated_date,btn_real_date,btn_swatching_date,btn_date_harvest,btn_begining_date,btn_end_date;

    private EditText et_observation_harvest,et_kg_ha,et_observation_yield,et_owner,et_model,et_percent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_harvest, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bind(view);

        date_estimated_date.setOnFocusChangeListener(this);
        date_real_date.setOnFocusChangeListener(this);
        date_swatching_date.setOnFocusChangeListener(this);
        date_date_harvest.setOnFocusChangeListener(this);
        date_begining_date.setOnFocusChangeListener(this);
        date_end_date.setOnFocusChangeListener(this);
//        date_end_date.setKeyListener(null);


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

        /* ET TEXTO */
        et_observation_harvest  = (EditText) view.findViewById(R.id.et_observation_harvest);
        et_kg_ha  = (EditText) view.findViewById(R.id.et_kg_ha);
        et_observation_yield  = (EditText) view.findViewById(R.id.et_observation_yield);
        et_owner  = (EditText) view.findViewById(R.id.et_owner);
        et_model  = (EditText) view.findViewById(R.id.et_model);
        et_percent  = (EditText) view.findViewById(R.id.et_percent);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()){

            case R.id.date_estimated_date:
                if(b)levantarFecha(date_estimated_date);
                break;
            case R.id.date_real_date:
                if(b)levantarFecha(date_real_date);
                break;
            case R.id.date_swatching_date:
                if(b)levantarFecha(date_swatching_date);
                break;
            case R.id.date_date_harvest:
                if(b)levantarFecha(date_date_harvest);
                break;
            case R.id.date_begining_date:
                if(b)levantarFecha(date_begining_date);
                break;
            case R.id.date_end_date:
                if(b)levantarFecha(date_end_date);
                break;
        }
    }
}
