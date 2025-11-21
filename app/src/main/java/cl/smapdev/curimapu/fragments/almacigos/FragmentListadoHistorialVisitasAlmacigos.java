package cl.smapdev.curimapu.fragments.almacigos;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.VisitasAlmacigoAdapter;
import cl.smapdev.curimapu.clases.relaciones.VisitaAlmacigoCompleto;
import cl.smapdev.curimapu.clases.tablas.OpAlmacigos;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class FragmentListadoHistorialVisitasAlmacigos extends Fragment {

    private MainActivity activity;

    private RecyclerView rv_listado_op;
    private OpAlmacigos almacigos;
    private VisitasAlmacigoAdapter almacigoAdapter;

    // Executor reutilizable para operaciones DB rápidas (evita crear/shutdown por click)
    private final ExecutorService singleDbExecutor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    public void setAlmacigos(OpAlmacigos almacigos) {
        this.almacigos = almacigos;
    }

    public static FragmentListadoHistorialVisitasAlmacigos newInstance(MainActivity activity, OpAlmacigos almacigos) {
        FragmentListadoHistorialVisitasAlmacigos fragment = new FragmentListadoHistorialVisitasAlmacigos();
        fragment.setActivity(activity);
        fragment.setAlmacigos(almacigos);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            activity = (MainActivity) context;
        } else {
            throw new RuntimeException(context + " must be MainActivity");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (singleDbExecutor != null && !singleDbExecutor.isShutdown()) {
            singleDbExecutor.shutdown();
        }
    }


    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listado_historial_visita_almacigo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        rv_listado_op = view.findViewById(R.id.rv_listado_op);
        cargarListadoOP();

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        Utilidades.setToolbar(activity, view, "Almacigos", "listado de visitas realizadas");
    }


    private void cargarListadoOP() {

        LinearLayoutManager lManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rv_listado_op.setHasFixedSize(true);
        rv_listado_op.setLayoutManager(lManager);


        singleDbExecutor.execute(() -> {
            List<VisitaAlmacigoCompleto> almacigosList = MainActivity.myAppDB.VisitasFotosAlmacigos().getVisitasAlmacigosPoridOP(almacigos.getId_v_post_siembra());

            handler.post(() -> {
                almacigoAdapter = new VisitasAlmacigoAdapter(almacigosList, (view, op) -> {

                    activity.cambiarFragment(
                            FragmentVisitasAlmacigos.newInstance(activity, almacigos, op.getVisitasAlmacigos()),
                            Utilidades.FRAGMENT_VISITA_ALMACIGOS,
                            R.anim.slide_in_left, R.anim.slide_out_left
                    );

                }, activity);

                rv_listado_op.setAdapter(almacigoAdapter);
            });
        });


    }


}
