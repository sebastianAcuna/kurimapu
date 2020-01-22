package cl.smapdev.curimapu.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.Tabla;
import cl.smapdev.curimapu.clases.adapters.SpinnerAdapter;
import cl.smapdev.curimapu.clases.adapters.SpinnerToolbarAdapter;
import cl.smapdev.curimapu.fragments.dialogos.DialogFilterTables;

public class FragmentVisitas extends Fragment {

    private View view;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view  =  inflater.inflate(R.layout.fragment_visitas, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Spinner spinner_toolbar = (Spinner) view.findViewById(R.id.spinner_toolbar);
        toolbar = view.findViewById(R.id.toolbar);


        cargarInforme();



        spinner_toolbar.setAdapter(new SpinnerToolbarAdapter(Objects.requireNonNull(getActivity()),R.layout.spinner_template_toolbar_view, getResources().getStringArray(R.array.anos_toolbar)));


        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_vistas, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_vistas_filter:

                DialogFilterTables dialogo = new DialogFilterTables();
                dialogo.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "DIALOGO");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }



    void cargarInforme(){
        Tabla tabla = new Tabla((TableLayout) view.findViewById(R.id.tabla), getActivity());
        tabla.removeViews();

        tabla.agregarCabecera(R.array.cabecera_informe);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.layoutTabla);
        linearLayout.setVisibility(View.VISIBLE);


        String[] dato = new String[5];

        List<String[]> arrDatos=  new ArrayList();

        dato[0] = "20-AR0011";
        dato[1] = "RC 3589";
        dato[2] = "xxxxxxx";
        dato[3] = "VIÑA ERRAZURIZ DOMINGUEZ S.A.";
        dato[4] = "Pivote";



        arrDatos.add(dato);

        String[] dato1 = new String[5];
        dato1[0] = "20-AR0011";
        dato1[1] = "RC 3589";
        dato1[2] = "xxxxxxx";
        dato1[3] = "VIÑA ERRAZURIZ DOMINGUEZ S.A.";
        dato1[4] = "Pivote";

        arrDatos.add(dato1);

        String[] dato2 = new String[5];
        dato2[0] = "20-AR0031";
        dato2[1] = "CONGO";
        dato2[2] = "xxxxxxx";
        dato2[3] = "SERGUIO ANTONIO CORES OBREQUE";
        dato2[4] = "EL ROBLE 1";

        arrDatos.add(dato2);


        String[] dato3 = new String[5];
        dato3[0] = "20-AR0021";
        dato3[1] = "RF 2087";
        dato3[2] = "xxxxxxx";
        dato3[3] = "SOC AGRICOLA SAN LUIS DE PAL-PAL LIMITADA.";
        dato3[4] = "EL AJIAL";

        arrDatos.add(dato3);


        String[] dato4 = new String[5];
        dato4[0] = "20-AR0011";
        dato4[1] = "RC 3589";
        dato4[2] = "xxxxxxx";
        dato4[3] = "VIÑA ERRAZURIZ DOMINGUEZ S.A.";
        dato4[4] = "Pivote";

        arrDatos.add(dato4);


        String[] dato5 = new String[5];
        dato5[0] = "20-AR0091";
        dato5[1] = "LC 2006";
        dato5[2] = "xxxxxxx";
        dato5[3] = "SOC HECTOR ESPINOZA E HIJOS LTDA";
        dato5[4] = "Paño 3";

        arrDatos.add(dato4);



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
