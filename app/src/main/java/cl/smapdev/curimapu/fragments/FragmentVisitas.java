package cl.smapdev.curimapu.fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.AnexosAdapter;
import cl.smapdev.curimapu.clases.adapters.SpinnerToolbarAdapter;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.tablas.Fotos;
import cl.smapdev.curimapu.clases.tablas.Temporada;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.contratos.FragmentListVisits;
import cl.smapdev.curimapu.fragments.dialogos.DialogFilterTables;
import cl.smapdev.curimapu.infraestructure.utils.coroutines.ApplicationExecutors;

public class FragmentVisitas extends Fragment {

    private View view;

    private MainActivity activity;
    private SharedPreferences prefs;
    private Spinner spinner_toolbar;


    private final ArrayList<String> id_temporadas = new ArrayList<>();
    private final ArrayList<String> desc_temporadas = new ArrayList<>();

    private RecyclerView lista_anexos;
    private List<Temporada> temporadaList;

    private String marca_especial_temporada;
    private AnexosAdapter anexosAdapter;


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

        lista_anexos = view.findViewById(R.id.lista_anexos);

        LinearLayoutManager lManager = null;
        if (activity != null){
            lManager  = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        }
        lista_anexos.setHasFixedSize(true);
        lista_anexos.setLayoutManager(lManager);


        spinner_toolbar = view.findViewById(R.id.spinner_toolbar);
//        spinner_toolbar.setEnabled(false);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<Temporada>> futuretemporadas = executor.submit( () -> MainActivity.myAppDB.myDao().getTemporada());
        try {
            temporadaList = futuretemporadas.get();
            for (Temporada t : temporadaList){
                id_temporadas.add(t.getId_tempo_tempo());
                desc_temporadas.add(t.getNombre_tempo());
                if(t.getEspecial_temporada() > 0){
                    marca_especial_temporada = t.getId_tempo_tempo();
                }
            }

            executor.shutdown();
        } catch (ExecutionException | InterruptedException e) {
            temporadaList = Collections.emptyList();
            e.printStackTrace();
            executor.shutdown();
        }

        setHasOptionsMenu(true);

        spinner_toolbar.setAdapter(new SpinnerToolbarAdapter(activity,R.layout.spinner_template_toolbar_view, temporadaList));

        recargarYear();
        spinner_toolbar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinner_toolbar.getTag() != null ){
                    if (Integer.parseInt(spinner_toolbar.getTag().toString()) != i){
                        prefs.edit().putString(Utilidades.SELECTED_ANO,id_temporadas.get(spinner_toolbar.getSelectedItemPosition())).apply();
                        prefs.edit().putInt(Utilidades.SHARED_FILTER_VISITAS_YEAR, i).apply();
                         cargarLista(id_temporadas.get(spinner_toolbar.getSelectedItemPosition()));
                    }else{
                        spinner_toolbar.setTag(null);
                    }
                }else{
                    prefs.edit().putString(Utilidades.SELECTED_ANO, id_temporadas.get(spinner_toolbar.getSelectedItemPosition())).apply();
                    prefs.edit().putInt(Utilidades.SHARED_FILTER_VISITAS_YEAR, i).apply();
                    cargarLista(id_temporadas.get(spinner_toolbar.getSelectedItemPosition()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (temporadaList.size() > 0){
            cargarLista( prefs.getString(Utilidades.SELECTED_ANO, temporadaList.get(temporadaList.size() - 1).getId_tempo_tempo()));
        }
    }

    private void recargarYear(){
        if (temporadaList.size() > 0){
            spinner_toolbar.setSelection((marca_especial_temporada.isEmpty()) ? prefs.getInt(Utilidades.SHARED_FILTER_VISITAS_YEAR, temporadaList.size() - 1) : id_temporadas.indexOf(marca_especial_temporada));
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
                dialogo.show(requireActivity().getSupportFragmentManager(), "DIALOGO");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        MyReceiver r = new MyReceiver();
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(r, new IntentFilter("TAG_REFRESH"));
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && context != null){
                ApplicationExecutors exec = new ApplicationExecutors();

                exec.getBackground().execute(()->{
                    List<AnexoCompleto> trabajo = (List<AnexoCompleto>) intent.getSerializableExtra(DialogFilterTables.LLAVE_FILTER_TABLAS);

                    exec.getMainThread().execute(()-> crearAdaptador(trabajo));
                });

                exec.shutDownBackground();
            }
        }
    }


    public void cargarLista( String fecha ){

        ApplicationExecutors exec = new ApplicationExecutors();

        exec.getBackground().execute(()->{
            List<AnexoCompleto> anexoCompleto = MainActivity.myAppDB.myDao().getAnexosByYear( fecha );

            exec.getMainThread().execute(()-> crearAdaptador(anexoCompleto));
        });

        exec.shutDownBackground();

    }

    public void crearAdaptador( List<AnexoCompleto> anexo ) {
        anexosAdapter =  new AnexosAdapter( anexo,
                (view1, anexos) -> nuevaVisita(anexos),
                (view1, anexos) -> mostrarMenu(anexos),
                getContext()
        );

        lista_anexos.setAdapter(anexosAdapter);
    }

    public void nuevaVisita (AnexoCompleto anexo) {

        ProgressDialog  progressBar = new ProgressDialog(activity);
        progressBar.setProgress(0);
        progressBar.setTitle("preparando nueva visita");
        progressBar.show();
        ApplicationExecutors exec = new ApplicationExecutors();
        exec.getBackground().execute(()->{
            MainActivity.myAppDB.myDao().deleteTempVisitas();
            MainActivity.myAppDB.myDao().deleteDetalleVacios();

            List<Fotos> fotos = MainActivity.myAppDB.myDao().getFotosByIdVisita(0);
            if (fotos.size() > 0){
                for (Fotos fts : fotos){
                    try{
                        File file = new File(fts.getRuta());
                        if (file.exists()) {
                            boolean eliminado = file.delete();
                            if (eliminado){
                                MainActivity.myAppDB.myDao().deleteFotos(fts);
                            }
                        }
                    }catch (Exception e){
                        Log.e("ERROR DELETING", Objects.requireNonNull(e.getMessage()));
                    }
                }
            }

            if (prefs != null){
                prefs.edit().putInt(Utilidades.SHARED_VISIT_FICHA_ID, anexo.getAnexoContrato().getId_ficha_contrato()).apply();
                prefs.edit().putString(Utilidades.SHARED_VISIT_MATERIAL_ID, anexo.getAnexoContrato().getId_especie_anexo()).apply();
                prefs.edit().putString(Utilidades.SHARED_VISIT_ANEXO_ID, anexo.getAnexoContrato().getId_anexo_contrato()).apply();
                prefs.edit().putString(Utilidades.SHARED_VISIT_TEMPORADA, anexo.getAnexoContrato().getTemporada_anexo()).apply();
                prefs.edit().putInt(Utilidades.SHARED_VISIT_VISITA_ID, 0).apply();
            }

            exec.getMainThread().execute(()-> {
                activity.cambiarFragment(new FragmentContratos(), Utilidades.FRAGMENT_CONTRATOS, R.anim.slide_in_left, R.anim.slide_out_left);
                if(progressBar.isShowing()){
                    progressBar.setProgress(100);
                    progressBar.dismiss();
                }
            });
        });
        exec.shutDownBackground();
    }

    public void mostrarMenu (AnexoCompleto anexo) {
        if (prefs != null) {
            prefs.edit().putInt(Utilidades.SHARED_VISIT_FICHA_ID, anexo.getAnexoContrato().getId_ficha_contrato()).apply();
            prefs.edit().putString(Utilidades.SHARED_VISIT_ANEXO_ID, anexo.getAnexoContrato().getId_anexo_contrato()).apply();
            prefs.edit().putString(Utilidades.SHARED_VISIT_MATERIAL_ID, anexo.getAnexoContrato().getId_especie_anexo()).apply();
            prefs.edit().putString(Utilidades.SHARED_VISIT_TEMPORADA, anexo.getAnexoContrato().getTemporada_anexo()).apply();
            prefs.edit().putInt(Utilidades.SHARED_VISIT_VISITA_ID, 0).apply();
        }

        activity.cambiarFragment(new FragmentListVisits(), Utilidades.FRAGMENT_LIST_VISITS, R.anim.slide_in_left, R.anim.slide_out_left);
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
