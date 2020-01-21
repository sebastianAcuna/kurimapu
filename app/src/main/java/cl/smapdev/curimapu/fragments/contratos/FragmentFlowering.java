package cl.smapdev.curimapu.fragments.contratos;

import android.app.DatePickerDialog;
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

public class FragmentFlowering extends Fragment implements View.OnClickListener/*, DatePickerDialog.OnDateSetListener*/{


    private EditText date_date,date_flowering,date_f_start,date_f_end,date_fert_one,date_fert_two,
            date_fungicide,date_insecticide,date_depuration,date_off_type,date_inspection;

    private ImageButton btn_fecha_date,btn_fecha_flowering,btn_fecha_start,btn_fecha_end,btn_fecha_fert_one,btn_fecha_fert_two,
            btn_fecha_fungicide,btn_fecha_insecticide,btn_fecha_depuration,btn_fecha_off_type,btn_fecha_inspection;


    private EditText et_fungicide,et_doce,et_plant_number_checked,et_check,et_main_char;


    private DatePickerDialog datePickerDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flowering, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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
        btn_fecha_date = view.findViewById(R.id.btn_fecha_date);
        btn_fecha_flowering = view.findViewById(R.id.btn_fecha_flowering);
        btn_fecha_start = view.findViewById(R.id.btn_fecha_start);
        btn_fecha_end = view.findViewById(R.id.btn_fecha_end);
        btn_fecha_fert_one = view.findViewById(R.id.btn_fecha_fert_one);
        btn_fecha_fert_two = view.findViewById(R.id.btn_fecha_fert_two);
        btn_fecha_fungicide = view.findViewById(R.id.btn_fecha_fungicide);
        btn_fecha_insecticide = view.findViewById(R.id.btn_fecha_insecticide);
        btn_fecha_depuration = view.findViewById(R.id.btn_fecha_depuration);
        btn_fecha_off_type = view.findViewById(R.id.btn_fecha_off_type);
        btn_fecha_inspection = view.findViewById(R.id.btn_fecha_inspection);


        date_date.setOnClickListener(this);
        date_flowering.setOnClickListener(this);
        date_f_start.setOnClickListener(this);
        date_f_end.setOnClickListener(this);
        date_fert_one.setOnClickListener(this);
        date_fert_two.setOnClickListener(this);
        date_fungicide.setOnClickListener(this);
        date_insecticide.setOnClickListener(this);
        date_depuration.setOnClickListener(this);
        date_off_type.setOnClickListener(this);
        date_inspection.setOnClickListener(this);
        btn_fecha_date.setOnClickListener(this);
        btn_fecha_flowering.setOnClickListener(this);
        btn_fecha_start.setOnClickListener(this);
        btn_fecha_end.setOnClickListener(this);
        btn_fecha_fert_one.setOnClickListener(this);
        btn_fecha_fert_two.setOnClickListener(this);
        btn_fecha_fungicide.setOnClickListener(this);
        btn_fecha_insecticide.setOnClickListener(this);
        btn_fecha_depuration.setOnClickListener(this);
        btn_fecha_off_type.setOnClickListener(this);
        btn_fecha_inspection.setOnClickListener(this);


    }



    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.date_date:
            case R.id.date_flowering:
            case R.id.date_f_start:
            case R.id.date_f_end:
            case R.id.date_fert_one:
            case R.id.date_fert_two:
            case R.id.date_fungicide:
            case R.id.date_insecticide:
            case R.id.date_depuration:
            case R.id.date_off_type:
            case R.id.date_inspection:
                final EditText edit  = (EditText) view.findViewById(view.getId());
                levantarFecha(edit);
                break;
            case R.id.btn_fecha_date:
                levantarFecha(date_date);
                break;
            case R.id.btn_fecha_flowering:
                levantarFecha(date_flowering);
                break;
            case R.id.btn_fecha_start:
                levantarFecha(date_f_start);
                break;
            case R.id.btn_fecha_end:
                levantarFecha(date_f_end);
                break;
            case R.id.btn_fecha_fert_one:
                levantarFecha(date_fert_one);
                break;
            case R.id.btn_fecha_fert_two:
                levantarFecha(date_fert_two);
                break;
            case R.id.btn_fecha_fungicide:
                levantarFecha(date_fungicide);
                break;
            case R.id.btn_fecha_insecticide:
                levantarFecha(date_insecticide);
                break;
            case R.id.btn_fecha_depuration:
                levantarFecha(date_depuration);
                break;
            case R.id.btn_fecha_off_type:
                levantarFecha(date_off_type);
                break;
            case R.id.btn_fecha_inspection:
                levantarFecha(date_inspection);
                break;
        }




    }



    void levantarFecha(final EditText edit){
        String fecha = Utilidades.fechaActualSinHora();
        String[] fechaRota = fecha.split("-");
        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                month = month +1;

                String mes="", dia;

                if (month<10){
                    mes = "0"+month;
                }else{
                    mes = String.valueOf(month);
                }
                if (dayOfMonth < 10) dia = "0"+dayOfMonth; else dia = String.valueOf(dayOfMonth);

                String finalDate = dia + "-"+mes+"-"+year;
                edit.setText(finalDate);
            }
        }, Integer.parseInt(fechaRota[0]), (Integer.parseInt(fechaRota[1]) - 1), Integer.parseInt(fechaRota[2]));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
}
