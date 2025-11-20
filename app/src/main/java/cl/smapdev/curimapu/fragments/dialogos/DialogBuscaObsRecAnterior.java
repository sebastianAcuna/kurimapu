package cl.smapdev.curimapu.fragments.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.ObsRecAdapter;
import cl.smapdev.curimapu.clases.adapters.SpinnerAdapter;
import cl.smapdev.curimapu.clases.tablas.Evaluaciones;
import cl.smapdev.curimapu.clases.tablas.Visitas;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import es.dmoral.toasty.Toasty;


public class DialogBuscaObsRecAnterior extends DialogFragment {

    public interface IOnSave {
        void onSave(boolean saved, String query);
    }

    ObsRecAdapter adapter;
    private String estadoFenologico;
    private String tipoBusqueda;
    private IOnSave IOnSave;


    private Spinner sp_estado_fenologico;
    private EditText etBuscarOP;
    private Button btn_cancelar_muestra;
    private RecyclerView rv_listado_obs;

    private List<String> elementosAdapter = new ArrayList<>();

    private SharedPreferences prefs;

    private final ArrayList<String> fenologico = new ArrayList<>();

    private ExecutorService executor;

    public void setEstadoFenologico(String estadoFenologico) {
        this.estadoFenologico = estadoFenologico;
    }

    public void setTipoBusqueda(String tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }


    public void setIOnSave(IOnSave IOnSave) {
        this.IOnSave = IOnSave;
    }

    public static DialogBuscaObsRecAnterior newInstance(String estadoFenologico, String tipoBusqueda, IOnSave onSave) {
        DialogBuscaObsRecAnterior dg = new DialogBuscaObsRecAnterior();
        dg.setEstadoFenologico(estadoFenologico);
        dg.setTipoBusqueda(tipoBusqueda);
        dg.setIOnSave(onSave);

        return dg;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        executor = Executors.newSingleThreadExecutor();

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_busqueda_obs_recom, null);
        builder.setView(view);

        fenologico.addAll(Arrays.asList(getResources().getStringArray(R.array.fenologico)));
        prefs = requireActivity().getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);

        bind(view);
        obtenerElementos();

        return builder.create();
    }

    private void ejecutarSeguro(Runnable r) {
        if (executor == null || executor.isShutdown() || executor.isTerminated()) {
            executor = Executors.newSingleThreadExecutor();
        }
        executor.execute(r);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void obtenerElementos() {

        elementosAdapter = new ArrayList<>();

        if (tipoBusqueda.equals("OBSERVACION")) {
            List<Visitas> visitas = filtrarVisitas();
            for (Visitas v : visitas) {
                if (v.getObservation_visita() != null && !v.getObservation_visita().isEmpty()) {
                    elementosAdapter.add(v.getObservation_visita());
                }
            }
        } else {

            List<Evaluaciones> evaluaciones = filtrarRecomendaciones();
            for (Evaluaciones v : evaluaciones) {
                if (v.getDescripcion_recom() != null && !v.getDescripcion_recom().isEmpty()) {
                    elementosAdapter.add(v.getDescripcion_recom());
                }
            }


        }

        setAdapter();
    }


    private List<Visitas> filtrarVisitas() {

        Object[] ob = new Object[]{};
        try {
            String consulta = "SELECT * FROM visita " +
                    "INNER JOIN anexo_contrato ON (visita.id_anexo_visita = anexo_contrato.id_anexo_contrato) " +
                    "WHERE anexo_contrato.temporada_anexo = ? ";
            ob = Utilidades.appendValue(ob, prefs.getString(Utilidades.SELECTED_ANO, "17"));
            if (sp_estado_fenologico.getSelectedItemPosition() > 0) {
                consulta += "AND visita.phenological_state_visita LIKE ?";
                ob = Utilidades.appendValue(ob, sp_estado_fenologico.getSelectedItem().toString());
            }


            consulta += " ORDER BY visita.fecha_visita DESC ";

            return MainActivity.myAppDB.myDao().getVisitasFilter(new SimpleSQLiteQuery(consulta, ob));
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
            Toasty.error(requireActivity(), "No pudimos encontrar lo que buscaste", Toast.LENGTH_SHORT, true).show();
            return new ArrayList<>();
        }
    }


    private List<Evaluaciones> filtrarRecomendaciones() {

        Object[] ob = new Object[]{};
        try {
            String consulta = "SELECT * FROM anexo_recomendaciones " +
                    "INNER JOIN anexo_contrato ON (anexo_recomendaciones.id_ac = anexo_contrato.id_anexo_contrato) " +
                    "LEFT JOIN visita ON (visita.clave_unica_visita = anexo_recomendaciones.clave_unica_visita)" +
                    "WHERE anexo_contrato.temporada_anexo = ? ";
            ob = Utilidades.appendValue(ob, prefs.getString(Utilidades.SELECTED_ANO, "17"));
            if (sp_estado_fenologico.getSelectedItemPosition() > 0) {
                consulta += "AND visita.phenological_state_visita LIKE ?";
                ob = Utilidades.appendValue(ob, sp_estado_fenologico.getSelectedItem().toString());
            }
            consulta += "GROUP BY  anexo_recomendaciones.id_ac_recom  ORDER BY anexo_recomendaciones.fecha_hora_tx DESC ";

            return MainActivity.myAppDB.myDao().getEvaluacionesFilter(new SimpleSQLiteQuery(consulta, ob));
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
            Toasty.error(requireActivity(), "No pudimos encontrar lo que buscaste", Toast.LENGTH_SHORT, true).show();
            return new ArrayList<>();
        }
    }


    public void bind(View view) {
        sp_estado_fenologico = view.findViewById(R.id.sp_estado_fenologico);
        etBuscarOP = view.findViewById(R.id.etBuscarOP);
        rv_listado_obs = view.findViewById(R.id.rv_listado_obs);
        btn_cancelar_muestra = view.findViewById(R.id.btn_cancelar_muestra);


        btn_cancelar_muestra.setOnClickListener(v -> {
            IOnSave.onSave(false, "");
            dismiss();
        });

        sp_estado_fenologico.setAdapter(new SpinnerAdapter(requireActivity(), R.layout.spinner_template_view, fenologico));

        if (estadoFenologico != null && fenologico.contains(estadoFenologico)) {
            sp_estado_fenologico.setSelection(fenologico.indexOf(estadoFenologico));
        }


        sp_estado_fenologico.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                obtenerElementos();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        etBuscarOP.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    adapter.filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
            }
        });
    }


    public void setAdapter() {

        rv_listado_obs.setHasFixedSize(true);
        rv_listado_obs.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        adapter = new ObsRecAdapter(elementosAdapter, seleccion -> {
            if (IOnSave != null) {
                IOnSave.onSave(true, seleccion);
            }
            dismiss();
        });
        rv_listado_obs.setAdapter(adapter);
    }
}
