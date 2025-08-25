package cl.smapdev.curimapu.fragments.almacigos;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

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
import cl.smapdev.curimapu.clases.adapters.OPAlmacigoAdapter;
import cl.smapdev.curimapu.clases.tablas.OpAlmacigos;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.dialogos.FragmentQRScanner;

public class FragmentListadoVisitasAlmacigos extends Fragment {

    private MainActivity activity;
    private SharedPreferences prefs;

    private RecyclerView rv_listado_op;
    private EditText etBuscarOP;
    private ImageButton btnScanQR;


    private OPAlmacigoAdapter almacigoAdapter;


    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    public static FragmentListadoVisitasAlmacigos newInstance(MainActivity activity) {
        FragmentListadoVisitasAlmacigos fragment = new FragmentListadoVisitasAlmacigos();
        fragment.setActivity(activity);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listado_visita_almacigo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        rv_listado_op = view.findViewById(R.id.rv_listado_op);
        etBuscarOP = view.findViewById(R.id.etBuscarOP);
        btnScanQR = view.findViewById(R.id.btnScanQR);


        cargarListadoOP();


        etBuscarOP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (almacigoAdapter != null) {
                    almacigoAdapter.filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btnScanQR.setOnClickListener(v -> {
            FragmentQRScanner scannerFragment = new FragmentQRScanner();
            scannerFragment.setOnScanResultListener(code -> {
                if (almacigoAdapter != null) {
                    etBuscarOP.setText(code); // actualiza el campo para que dispare el filtro
                }
            });

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_qr, scannerFragment) // asegúrate que este ID es tu contenedor de fragmentos
                    .addToBackStack(null)
                    .commit();
        });


    }


    private void cargarListadoOP() {

        LinearLayoutManager lManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rv_listado_op.setHasFixedSize(true);
        rv_listado_op.setLayoutManager(lManager);

        ExecutorService io = Executors.newSingleThreadExecutor();
        io.execute(() -> {
            List<OpAlmacigos> almacigosList = MainActivity.myAppDB.DaoOPAlmacigos().getOpAlmacigos();

            activity.runOnUiThread(() -> {
                almacigoAdapter = new OPAlmacigoAdapter(almacigosList, (view, op) -> {

                    activity.cambiarFragment(
                            FragmentVisitasAlmacigos.newInstance(activity, op, null),
                            Utilidades.FRAGMENT_VISITA_ALMACIGOS,
                            R.anim.slide_in_left, R.anim.slide_out_left
                    );

                }, (view, op) -> {
                    activity.cambiarFragment(
                            FragmentListadoHistorialVisitasAlmacigos.newInstance(activity, op),
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
                    "Almacigos", "listado de op");
        }
    }

}
