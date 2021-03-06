package cl.smapdev.curimapu.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.Tabla;
import cl.smapdev.curimapu.clases.adapters.SpinnerToolbarAdapter;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.tablas.Temporada;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.dialogos.DialogFilterTables;

public class FragmentVisitas extends Fragment {

    private View view;

    private Tabla tabla;
    private MainActivity activity;
    private SharedPreferences prefs;
    private Spinner spinner_toolbar;


    private ArrayList<String> id_temporadas = new ArrayList<>();
    private ArrayList<String> desc_temporadas = new ArrayList<>();

    private List<Temporada> temporadaList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        activity = (MainActivity) getActivity();
        if (activity != null){
            prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view  =  inflater.inflate(R.layout.fragment_visitas, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        spinner_toolbar = (Spinner) view.findViewById(R.id.spinner_toolbar);
        Toolbar toolbar = view.findViewById(R.id.toolbar);


        temporadaList = MainActivity.myAppDB.myDao().getTemporada();
        if (temporadaList.size() > 0){
            for (Temporada t : temporadaList){
                id_temporadas.add(t.getId_tempo_tempo());
                desc_temporadas.add(t.getNombre_tempo());
            }
        }

        setHasOptionsMenu(true);

        spinner_toolbar.setAdapter(new SpinnerToolbarAdapter(activity,R.layout.spinner_template_toolbar_view, temporadaList));

        if (activity != null){
            tabla = new Tabla((TableLayout) view.findViewById(R.id.tabla),activity);
        }

        recargarYear();
        spinner_toolbar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (spinner_toolbar.getTag() != null ){
                    if (Integer.parseInt(spinner_toolbar.getTag().toString()) != i){

                        prefs.edit().putString(Utilidades.SELECTED_ANO,id_temporadas.get(spinner_toolbar.getSelectedItemPosition())).apply();
                        prefs.edit().putInt(Utilidades.SHARED_FILTER_VISITAS_YEAR, i).apply();

                        cargarInforme(MainActivity.myAppDB.myDao().getAnexosByYear(id_temporadas.get(spinner_toolbar.getSelectedItemPosition())));

                    }else{
                        spinner_toolbar.setTag(null);
                    }
                }else{
                    prefs.edit().putString(Utilidades.SELECTED_ANO, id_temporadas.get(spinner_toolbar.getSelectedItemPosition())).apply();
                    prefs.edit().putInt(Utilidades.SHARED_FILTER_VISITAS_YEAR, i).apply();

                    cargarInforme(MainActivity.myAppDB.myDao().getAnexosByYear(id_temporadas.get(spinner_toolbar.getSelectedItemPosition())));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (temporadaList.size() > 0){
            cargarInforme( MainActivity.myAppDB.myDao().getAnexosByYear(prefs.getString(Utilidades.SELECTED_ANO, temporadaList.get(temporadaList.size() - 1).getId_tempo_tempo())));
        }
    }

    private void recargarYear(){
        if (temporadaList.size() > 0){
            spinner_toolbar.setSelection(prefs.getInt(Utilidades.SHARED_FILTER_VISITAS_YEAR, temporadaList.size() - 1));
        }
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


    @Override
    public void onResume() {
        super.onResume();
        MyReceiver r = new MyReceiver();
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getActivity())).registerReceiver(r, new IntentFilter("TAG_REFRESH"));
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && context != null){
                List<AnexoCompleto> trabajo = (List<AnexoCompleto>) intent.getSerializableExtra(DialogFilterTables.LLAVE_FILTER_TABLAS);
                if (trabajo != null ){
                    spinner_toolbar.setTag(prefs.getInt(Utilidades.SHARED_FILTER_VISITAS_YEAR, temporadaList.size() - 1));
                    spinner_toolbar.setSelection(prefs.getInt(Utilidades.SHARED_FILTER_VISITAS_YEAR, temporadaList.size() - 1));
                    cargarInforme(trabajo);
                }
            }

        }
    }

    private void cargarInforme(List<AnexoCompleto> anexoCompletos){
        if (tabla != null){
            tabla.removeViews();
            tabla.agregarCabecera(R.array.cabecera_informe);

            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.layoutTabla);
            linearLayout.setVisibility(View.VISIBLE);


            if (anexoCompletos.size() > 0){
                for (int i = 0; i < anexoCompletos.size(); i++){
                    tabla.agregarFilaTabla(anexoCompletos.get(i));
                }
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null){
            activity.updateView(getResources().getString(R.string.app_name), getResources().getString(R.string.subtitles_visit));
        }
    }
}
