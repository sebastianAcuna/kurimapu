package cl.smapdev.curimapu.fragments.contratos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.SpinnerAdapter;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.FragmentVisitas;

public class FragmentFormVisitas extends Fragment implements View.OnClickListener {

    private Spinner sp_fenologico,sp_cosecha,sp_crecimiento,sp_fito,sp_general_cultivo,sp_humedad,sp_malezas;
    private Button btn_guardar, btn_volver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form_visitas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind(view);
        cargarSpinners();
    }




    private void cargarSpinners(){

        sp_fenologico.setAdapter(new SpinnerAdapter(getActivity(),R.layout.spinner_template_view, getResources().getStringArray(R.array.fenologico)));
        sp_cosecha.setAdapter(new SpinnerAdapter(getActivity(),R.layout.spinner_template_view, getResources().getStringArray(R.array.cosecha)));
        sp_crecimiento.setAdapter(new SpinnerAdapter(getActivity(),R.layout.spinner_template_view, getResources().getStringArray(R.array.crecimiento)));
        sp_fito.setAdapter(new SpinnerAdapter(getActivity(),R.layout.spinner_template_view, getResources().getStringArray(R.array.crecimiento)));
        sp_general_cultivo.setAdapter(new SpinnerAdapter(getActivity(),R.layout.spinner_template_view, getResources().getStringArray(R.array.crecimiento)));
        sp_humedad.setAdapter(new SpinnerAdapter(getActivity(),R.layout.spinner_template_view, getResources().getStringArray(R.array.crecimiento)));
        sp_malezas.setAdapter(new SpinnerAdapter(getActivity(),R.layout.spinner_template_view, getResources().getStringArray(R.array.maleza)));


//        sp_fenologico.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.fenologico)));

//        sp_cosecha.setAdapter( new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.cosecha)));
//
//        sp_crecimiento.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.crecimiento)));
//
//        sp_fito.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.crecimiento)));
//
//        sp_general_cultivo.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.crecimiento)));
//
//        sp_humedad.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.crecimiento)));
//
//        sp_malezas.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.maleza)));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_guardar:
                break;

            case R.id.btn_volver:
                MainActivity activity = (MainActivity) getActivity();
                if (activity != null){
                    activity.cambiarFragment(new FragmentVisitas(), Utilidades.FRAGMENT_VISITAS, R.anim.slide_in_right, R.anim.slide_out_right);
                }

                break;
        }
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
        btn_volver = (Button) view.findViewById(R.id.btn_volver);




        btn_volver.setOnClickListener(this);
        btn_guardar.setOnClickListener(this);
    }
}
