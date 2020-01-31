package cl.smapdev.curimapu.fragments.contratos;

import android.app.DatePickerDialog;
import android.os.Bundle;
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
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class FragmentFlowering extends Fragment implements View.OnFocusChangeListener/*, DatePickerDialog.OnDateSetListener*/{


    private EditText date_date,date_flowering,date_f_start,date_f_end,date_fert_one,date_fert_two,
            date_fungicide,date_insecticide,date_depuration,date_off_type,date_inspection;

/*    private ImageButton btn_fecha_date,btn_fecha_flowering,btn_fecha_start,btn_fecha_end,btn_fecha_fert_one,btn_fecha_fert_two,
            btn_fecha_fungicide,btn_fecha_insecticide,btn_fecha_depuration,btn_fecha_off_type,btn_fecha_inspection;*/


    private EditText et_fungicide,et_doce,et_plant_number_checked,et_check,et_main_char;

    private MainActivity activity = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         MainActivity a = (MainActivity) getActivity();
         if(a != null){
             activity = a;
         }
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


    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (activity != null){
                Toast.makeText(activity, "Visible flowering", Toast.LENGTH_SHORT).show();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fotos_flowering, FragmentFotos.getInstance(3), Utilidades.FRAGMENT_FOTOS).commit();
            }
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
    }

    @Override
    public void onFocusChange(View view, boolean b) {

        switch (view.getId()){
            case R.id.date_date:
                if(b) levantarFecha(date_date);
                break;
            case R.id.date_flowering:
                if(b) levantarFecha(date_flowering);
                break;
            case R.id.date_f_start:
                if(b) levantarFecha(date_f_start);
                break;
            case R.id.date_f_end:
                if(b) levantarFecha(date_f_end);
                break;
            case R.id.date_fert_one:
                if(b) levantarFecha(date_fert_one);
                break;
            case R.id.date_fert_two:
                if(b) levantarFecha(date_fert_two);
                break;
            case R.id.date_fungicide:
                if(b) levantarFecha(date_fungicide);
                break;
            case R.id.date_insecticide:
                if(b) levantarFecha(date_insecticide);
                break;
            case R.id.date_depuration:
                if(b) levantarFecha(date_depuration);
                break;
            case R.id.date_off_type:
                if(b) levantarFecha(date_off_type);
                break;
            case R.id.date_inspection:
                if(b) levantarFecha(date_inspection);
                break;
        }

    }
}
