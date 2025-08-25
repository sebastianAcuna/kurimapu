package cl.smapdev.curimapu.fragments.almacigos;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
    }


    private void cargarListadoOP() {

        LinearLayoutManager lManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rv_listado_op.setHasFixedSize(true);
        rv_listado_op.setLayoutManager(lManager);

        ExecutorService io = Executors.newSingleThreadExecutor();
        io.execute(() -> {
            List<VisitaAlmacigoCompleto> almacigosList = MainActivity.myAppDB.VisitasFotosAlmacigos().getVisitasAlmacigosPoridOP(almacigos.getId_v_post_siembra());

            activity.runOnUiThread(() -> {
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (activity != null) {
            activity.updateView(
                    "Almacigos", "listado de visitas realizadas");
        }
    }

}
