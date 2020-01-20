package cl.smapdev.curimapu.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.Tabla;

public class FragmentVisitas extends Fragment {

    private Tabla tabla;

    private View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view  =  inflater.inflate(R.layout.fragment_visitas, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cargarInforme();
    }


    void cargarInforme(){
        tabla = new Tabla((TableLayout) view.findViewById(R.id.tabla),getActivity());
        tabla.removeViews();

        tabla.agregarCabecera(R.array.cabecera_informe);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.layoutTabla);
        linearLayout.setVisibility(View.VISIBLE);


        String[] dato = new String[5];

        List<String[]> arrDatos=  new ArrayList();

        dato[0] = "VILMORIN";
        dato[1] = "VIÑA ERRAZURIZ DOMINGUEZ S.A.";
        dato[2] = "Pivote";
        dato[3] = "RC 3589";


        arrDatos.add(dato);

        dato[0] = "VILMORIN";
        dato[1] = "SERGIO ANTONIO CORTES OBREQUE";
        dato[2] = "El Roble 1";
        dato[3] = "CONGO";

        arrDatos.add(dato);

        if (arrDatos.size() > 0){
            for (int i = 0; i < arrDatos.size(); i++){
                tabla.agregarFilaTabla(arrDatos.get(i));
            }
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null){
            activity.updateView(getResources().getString(R.string.app_name), "Visitas");
        }
    }
}
