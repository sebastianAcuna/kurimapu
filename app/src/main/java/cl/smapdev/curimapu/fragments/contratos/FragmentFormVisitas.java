package cl.smapdev.curimapu.fragments.contratos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import cl.smapdev.curimapu.R;

public class FragmentFormVisitas extends Fragment {

    private Spinner sp_fenologico,sp_cosecha,sp_crecimiento,sp_fito,sp_general_cultivo,sp_humedad,sp_malezas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form_visitas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sp_fenologico = (Spinner) view.findViewById(R.id.sp_feno);
        sp_cosecha = (Spinner) view.findViewById(R.id.sp_cosecha);
        sp_crecimiento = (Spinner) view.findViewById(R.id.sp_crecimiento);
        sp_fito = (Spinner) view.findViewById(R.id.sp_fito);
        sp_general_cultivo = (Spinner) view.findViewById(R.id.sp_general_cultivo);
        sp_humedad = (Spinner) view.findViewById(R.id.sp_humedad);
        sp_malezas = (Spinner) view.findViewById(R.id.sp_malezas);




        cargarSpinners();
    }




    private void cargarSpinners(){

        sp_fenologico.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.fenologico)));

        sp_cosecha.setAdapter( new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.cosecha)));

        sp_crecimiento.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.crecimiento)));

        sp_fito.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.crecimiento)));

        sp_general_cultivo.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.crecimiento)));

        sp_humedad.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.crecimiento)));

        sp_malezas.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.maleza)));

    }
}
