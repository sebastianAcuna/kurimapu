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
import androidx.constraintlayout.solver.widgets.Helper;
import androidx.fragment.app.Fragment;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class FragmentSowing extends Fragment implements View.OnClickListener/*, DatePickerDialog.OnDateSetListener*/{


    private EditText date_date,date_flowering,date_f_start,date_f_end,date_fert_one,date_fert_two,
            date_fungicide,date_insecticide,date_depuration,date_off_type,date_inspection;

    private ImageButton btn_fecha_date,btn_fecha_flowering,btn_fecha_start,btn_fecha_end,btn_fecha_fert_one,btn_fecha_fert_two,
            btn_fecha_fungicide,btn_fecha_insecticide,btn_fecha_depuration,btn_fecha_off_type,btn_fecha_inspection;


    private EditText et_fungicide,et_doce,et_plant_number_checked,et_check,et_main_char;


    private DatePickerDialog datePickerDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sowing, container, false);
    }

    @Override
    public void onClick(View view) {

    }

}
