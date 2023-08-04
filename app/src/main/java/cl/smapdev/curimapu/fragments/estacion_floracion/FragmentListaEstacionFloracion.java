package cl.smapdev.curimapu.fragments.estacion_floracion;

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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.EstacionFloracionAdapter;
import cl.smapdev.curimapu.clases.modelo.CheckListSync;
import cl.smapdev.curimapu.clases.modelo.EstacionFloracionSync;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.relaciones.CheckListCapCompleto;
import cl.smapdev.curimapu.clases.relaciones.CheckListLimpiezaCamionesCompleto;
import cl.smapdev.curimapu.clases.relaciones.CheckListRequest;
import cl.smapdev.curimapu.clases.relaciones.EstacionFloracionCompleto;
import cl.smapdev.curimapu.clases.relaciones.EstacionFloracionRequest;
import cl.smapdev.curimapu.clases.relaciones.EstacionesCompletas;
import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembra;
import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembraDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListCosecha;
import cl.smapdev.curimapu.clases.tablas.CheckListLimpiezaCamiones;
import cl.smapdev.curimapu.clases.tablas.CheckListSiembra;
import cl.smapdev.curimapu.clases.tablas.ChecklistDevolucionSemilla;
import cl.smapdev.curimapu.clases.tablas.ChecklistLimpiezaCamionesDetalle;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracion;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracionDetalle;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracionEstaciones;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import es.dmoral.toasty.Toasty;

public class FragmentListaEstacionFloracion extends Fragment {

    private RecyclerView rv_est_floracion;
    private Button nueva_estacion;

    private SharedPreferences prefs;
    private MainActivity activity;
    private AnexoCompleto anexoCompleto = null;

    private EstacionFloracionAdapter adapter;

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
        return inflater.inflate(R.layout.fragment_estacion_floracion, container, false);
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


            MainActivity.myAppDB.DaoEstacionFloracion().deleteEstacionesSueltas();
            MainActivity.myAppDB.DaoEstacionFloracion().deleteDetalleSuelto();


            activity.cambiarFragment(
                    new FragmentNuevaEstacionFloracion(),
                    Utilidades.FRAGMENT_NUEVA_ESTACION,
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
                Future<List<EstacionFloracion>> chkF = executorService.submit(()
                        -> MainActivity.myAppDB.DaoEstacionFloracion()
                        .getEstacionesToSync());
                try {

                    List<EstacionFloracion> estacionFloracions = chkF.get();

                    if(estacionFloracions.size() == 0){
                        executorService.shutdown();
                        Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                        return true;
                    }

                    EstacionFloracionRequest floracionRequest = new EstacionFloracionRequest();
                    List<EstacionFloracionCompleto> estacionesFloracion = new ArrayList<>();


                    for (EstacionFloracion estacionFloracion : estacionFloracions){
                        List<EstacionesCompletas> estacionesCompletas = new ArrayList<>();

                        List<EstacionFloracionEstaciones> estaciones  =
                                executorService.submit(() -> MainActivity.myAppDB
                                        .DaoEstacionFloracion().getEstacionesByPadre(estacionFloracion.getClave_unica_floracion())).get();

                        for (EstacionFloracionEstaciones estacion : estaciones){

                            List<EstacionFloracionDetalle> detalles  =
                                    executorService.submit(() -> MainActivity.myAppDB
                                            .DaoEstacionFloracion().getDetalleByClaveEstacion(estacion.getClave_unica_floracion_estaciones())).get();

                            EstacionesCompletas estacionesCompleta = new EstacionesCompletas();
                            estacionesCompleta.setEstaciones(estacion);
                            estacionesCompleta.setDetalles(detalles);
                            estacionesCompletas.add(estacionesCompleta);
                        }

                        EstacionFloracionCompleto completo = new EstacionFloracionCompleto();

                        completo.setEstacionFloracion(estacionFloracion);
                        completo.setEstaciones(estacionesCompletas);

                        estacionesFloracion.add(completo);
                    }

                    floracionRequest.setEstacionFloracionCompletos(estacionesFloracion);
                    prepararSubir( floracionRequest );

                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                    executorService.shutdown();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private List<EstacionFloracionCompleto> getEstacionesFloracion(ExecutorService ex){

        List<EstacionFloracionCompleto> estacionesFloracion = new ArrayList<>();

        List<EstacionFloracion> estacionFloracions;
        Future<List<EstacionFloracion>> estacionFloracionFuture =
                ex.submit(() -> MainActivity.myAppDB
                        .DaoEstacionFloracion().getEstacionesByAc(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato())));
        try {

            estacionFloracions = estacionFloracionFuture.get();


            for (EstacionFloracion estacionFloracion : estacionFloracions){
                List<EstacionesCompletas> estacionesCompletas = new ArrayList<>();

                List<EstacionFloracionEstaciones> estaciones  =
                        ex.submit(() -> MainActivity.myAppDB
                                .DaoEstacionFloracion().getEstacionesByPadre(estacionFloracion.getClave_unica_floracion())).get();

                for (EstacionFloracionEstaciones estacion : estaciones){

                    List<EstacionFloracionDetalle> detalles  =
                            ex.submit(() -> MainActivity.myAppDB
                                    .DaoEstacionFloracion().getDetalleByClaveEstacion(estacion.getClave_unica_floracion_estaciones())).get();

                    EstacionesCompletas estacionesCompleta = new EstacionesCompletas();
                    estacionesCompleta.setEstaciones(estacion);
                    estacionesCompleta.setDetalles(detalles);
                    estacionesCompletas.add(estacionesCompleta);
                }

                EstacionFloracionCompleto completo = new EstacionFloracionCompleto();

                completo.setEstacionFloracion(estacionFloracion);
                completo.setEstaciones(estacionesCompletas);

                estacionesFloracion.add(completo);
            }


        }catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return estacionesFloracion;
    }

    private void cargarLista(){

        ExecutorService ex = Executors.newSingleThreadExecutor();

        List<EstacionFloracionCompleto> estacionesFloracion = getEstacionesFloracion(ex);

        ex.shutdown();

        LinearLayoutManager lManager = null;
        if (activity != null){
            lManager  = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        }
        rv_est_floracion.setHasFixedSize(true);
        rv_est_floracion.setLayoutManager(lManager);


        adapter = new EstacionFloracionAdapter(
                estacionesFloracion,
                (v, estacionesEliminar) -> {},
                (v, estacionesEditar) -> {

                    activity.cambiarFragment(
                            FragmentNuevaEstacionFloracion.newInstance(estacionesEditar),
                            Utilidades.FRAGMENT_NUEVA_ESTACION,
                            R.anim.slide_in_left,R.anim.slide_out_left
                    );
                },
                requireContext()
        );

        rv_est_floracion.setAdapter(adapter);
    }

    private void prepararSubir(EstacionFloracionRequest checkListRequest){

        new EstacionFloracionSync( checkListRequest, requireActivity(), (state, message) -> {
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
                            ? " E. Floracion Anexo "+anexoCompleto.getAnexoContrato().getAnexo_contrato()
                            : getResources().getString(R.string.subtitles_visit));
        }
    }
}
