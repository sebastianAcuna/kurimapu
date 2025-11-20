package cl.smapdev.curimapu.fragments.estacion_floracion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.EstacionFloracionAdapter;
import cl.smapdev.curimapu.clases.modelo.EstacionFloracionSync;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.relaciones.EstacionFloracionCompleto;
import cl.smapdev.curimapu.clases.relaciones.EstacionFloracionRequest;
import cl.smapdev.curimapu.clases.relaciones.EstacionesCompletas;
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

    private ExecutorService executors;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            activity = (MainActivity) context;
            prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
        } else {
            throw new RuntimeException(context + " must be MainActivity");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        executors = Executors.newSingleThreadExecutor();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executors != null && !executors.isShutdown()) {
            executors.shutdown();
        }
    }

    private void ejecutarSeguro(Runnable r) {
        if (executors == null || executors.isShutdown() || executors.isTerminated()) {
            executors = Executors.newSingleThreadExecutor();
        }
        executors.execute(r);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_estacion_floracion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ejecutarSeguro(() -> {
            anexoCompleto = MainActivity
                    .myAppDB
                    .myDao()
                    .getAnexoCompletoById(
                            prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, "")
                    );

            handler.post(this::cargarLista);
        });

        bind(view);

        nueva_estacion.setOnClickListener((v) -> {
            ejecutarSeguro(() -> {
                MainActivity.myAppDB.DaoEstacionFloracion().deleteEstacionesSueltas();
                MainActivity.myAppDB.DaoEstacionFloracion().deleteDetalleSuelto();
                handler.post(() -> {
                    activity.cambiarFragment(
                            new FragmentNuevaEstacionFloracion(),
                            Utilidades.FRAGMENT_NUEVA_ESTACION,
                            R.anim.slide_in_left, R.anim.slide_out_left
                    );
                });
            });
        });

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.menu_sube_fechas, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.menu_upload_files) {

                    ejecutarSeguro(() -> {

                        List<EstacionFloracion> estacionFloracions = MainActivity.myAppDB.DaoEstacionFloracion()
                                .getEstacionesToSync();

                        if (estacionFloracions.isEmpty()) {
                            handler.post(() -> Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show());
                            return;
                        }

                        EstacionFloracionRequest floracionRequest = new EstacionFloracionRequest();
                        List<EstacionFloracionCompleto> estacionesFloracion = new ArrayList<>();

                        for (EstacionFloracion estacionFloracion : estacionFloracions) {
                            List<EstacionesCompletas> estacionesCompletas = new ArrayList<>();

                            List<EstacionFloracionEstaciones> estaciones = MainActivity.myAppDB
                                    .DaoEstacionFloracion().getEstacionesByPadre(estacionFloracion.getClave_unica_floracion());

                            for (EstacionFloracionEstaciones estacion : estaciones) {

                                List<EstacionFloracionDetalle> detalles = MainActivity.myAppDB
                                        .DaoEstacionFloracion().getDetalleByClaveEstacion(estacion.getClave_unica_floracion_estaciones());

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

                        handler.post(() -> prepararSubir(floracionRequest));
                    });
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.CREATED);

        Utilidades.setToolbar(activity, view, getResources().getString(R.string.app_name), (anexoCompleto != null)
                ? " E. Floracion Anexo " + anexoCompleto.getAnexoContrato().getAnexo_contrato()
                : getResources().getString(R.string.subtitles_visit));

    }

    private List<EstacionFloracionCompleto> getEstacionesFloracion() {

        List<EstacionFloracionCompleto> estacionesFloracion = new ArrayList<>();

        List<EstacionFloracion> estacionFloracions = MainActivity.myAppDB
                .DaoEstacionFloracion().getEstacionesByAc(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));
        for (EstacionFloracion estacionFloracion : estacionFloracions) {
            List<EstacionesCompletas> estacionesCompletas = new ArrayList<>();

            List<EstacionFloracionEstaciones> estaciones = MainActivity.myAppDB
                    .DaoEstacionFloracion().getEstacionesByPadre(estacionFloracion.getClave_unica_floracion());

            for (EstacionFloracionEstaciones estacion : estaciones) {

                List<EstacionFloracionDetalle> detalles = MainActivity.myAppDB
                        .DaoEstacionFloracion().getDetalleByClaveEstacion(estacion.getClave_unica_floracion_estaciones());

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

        return estacionesFloracion;
    }

    private void cargarLista() {
        LinearLayoutManager lManager = null;
        if (activity != null) {
            lManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        }
        rv_est_floracion.setHasFixedSize(true);
        rv_est_floracion.setLayoutManager(lManager);

        ejecutarSeguro(() -> {

            List<EstacionFloracionCompleto> estacionesFloracion = getEstacionesFloracion();

            handler.post(() -> {
                adapter = new EstacionFloracionAdapter(
                        estacionesFloracion,
                        (v, estacionesEliminar) -> {
                        },
                        (v, estacionesEditar) -> activity.cambiarFragment(
                                FragmentNuevaEstacionFloracion.newInstance(estacionesEditar),
                                Utilidades.FRAGMENT_NUEVA_ESTACION,
                                R.anim.slide_in_left, R.anim.slide_out_left
                        ),
                        requireContext()
                );

                rv_est_floracion.setAdapter(adapter);
            });

        });


    }

    private void prepararSubir(EstacionFloracionRequest checkListRequest) {

        new EstacionFloracionSync(checkListRequest, requireActivity(), (state, message) -> {
            if (state) {
                cargarLista();
                Toasty.success(requireActivity(), message, Toast.LENGTH_LONG, true).show();
            } else {
                Toasty.error(requireActivity(), message, Toast.LENGTH_LONG, true).show();
            }
        });
    }


    private void bind(View view) {
        rv_est_floracion = view.findViewById(R.id.rv_estacion_floracion);
        nueva_estacion = view.findViewById(R.id.nueva_estacion);
    }

}
