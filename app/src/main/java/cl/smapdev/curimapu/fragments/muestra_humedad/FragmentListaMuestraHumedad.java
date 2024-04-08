package cl.smapdev.curimapu.fragments.muestra_humedad;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.MuestraHumedadAdapter;
import cl.smapdev.curimapu.clases.modelo.MuestraHumedadSync;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.relaciones.MuestraHumedadRequest;
import cl.smapdev.curimapu.clases.tablas.MuestraHumedad;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import es.dmoral.toasty.Toasty;

public class FragmentListaMuestraHumedad extends Fragment {

    private RecyclerView rv_est_floracion;
    private Button nueva_estacion;

    private SharedPreferences prefs;
    private MainActivity activity;
    private AnexoCompleto anexoCompleto = null;

    private MuestraHumedadAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity a = (MainActivity) getActivity();
        if(a != null) activity = a;
        prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_muestra_humedad, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<AnexoCompleto> futureVisitas = executor.submit(() ->
                MainActivity
                        .myAppDB
                        .myDao()
                        .getAnexoCompletoById(
                                prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, "")
                        ));
        try {
            anexoCompleto = futureVisitas.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        setHasOptionsMenu(true);

        executor.shutdown();
        bind(view);

        cargarLista();


        nueva_estacion.setOnClickListener((v)->{
            activity.cambiarFragment(
                    new FragmentNuevaMuestraHumedad(),
                    Utilidades.FRAGMENT_NUEVA_MUESTRA,
                    R.anim.slide_in_left,R.anim.slide_out_left
            );
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_sube_fechas, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_upload_files:

                ExecutorService executorService = Executors.newSingleThreadExecutor();
                Future<List<MuestraHumedad>> chkF = executorService.submit(()
                        -> MainActivity.myAppDB.DaoMuestraHumedad()
                        .getMuestrasToSync());
                try {

                    List<MuestraHumedad> muestraHumedads = chkF.get();

                    if(muestraHumedads.size() == 0){
                        executorService.shutdown();
                        Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                        return true;
                    }

                    MuestraHumedadRequest humedadRequest = new MuestraHumedadRequest();

                    humedadRequest.setMuestrasHumedad(muestraHumedads);
                    prepararSubir( humedadRequest );

                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                    executorService.shutdown();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private List<MuestraHumedad> getMuestraHumedad(ExecutorService ex){

        Future<List<MuestraHumedad>> estacionFloracionFuture =
                ex.submit(() -> MainActivity.myAppDB
                        .DaoMuestraHumedad().getMuestrasByAc(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato())));
        try {
         return  estacionFloracionFuture.get();
        }catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    private void cargarLista(){

        ExecutorService ex = Executors.newSingleThreadExecutor();

        List<MuestraHumedad> muestraHumedad = getMuestraHumedad(ex);

        ex.shutdown();

        LinearLayoutManager lManager = null;
        if (activity != null){
            lManager  = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        }
        rv_est_floracion.setHasFixedSize(true);
        rv_est_floracion.setLayoutManager(lManager);


        adapter = new MuestraHumedadAdapter(
                muestraHumedad,
                (v, estacionesEliminar) -> {},
                (v, estacionesEditar) -> {

                    activity.cambiarFragment(
                            FragmentNuevaMuestraHumedad.newInstance(estacionesEditar),
                            Utilidades.FRAGMENT_NUEVA_MUESTRA,
                            R.anim.slide_in_left,R.anim.slide_out_left
                    );
                },
                requireContext()
        );
        rv_est_floracion.setAdapter(adapter);
    }

    private void prepararSubir(MuestraHumedadRequest checkListRequest){
        new MuestraHumedadSync( checkListRequest, requireActivity(), (state, message) -> {
            if(state){
                cargarLista();
                Toasty.success(requireActivity(), message, Toast.LENGTH_LONG, true).show();
            }else{
                Toasty.error(requireActivity(), message, Toast.LENGTH_LONG, true).show();
            }
        });
    }


    private void bind (View view){
        rv_est_floracion = view.findViewById(R.id.rv_estacion_floracion);
        nueva_estacion = view.findViewById(R.id.nueva_estacion);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (activity != null){
            activity.updateView(
                    getResources().getString(R.string.app_name),
                    (anexoCompleto != null)
                            ? " M. Humedad Anexo "+anexoCompleto.getAnexoContrato().getAnexo_contrato()
                            : getResources().getString(R.string.subtitles_visit));
        }
    }
}
