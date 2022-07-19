package cl.smapdev.curimapu.fragments.checklist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.CheckListAdapter;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.tablas.CheckList;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class FragmentCheckList extends Fragment {

    private RecyclerView rv_checklist;

    private SharedPreferences prefs;
    private MainActivity activity;
    private AnexoCompleto anexoCompleto = null;

    private CheckListAdapter adapter;

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
        return inflater.inflate(R.layout.fragment_checklist, container, false);
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
        executor.shutdown();
        bind(view);


        List<CheckList> checkLists = new ArrayList<>();

        CheckList chk1 = new CheckList();
        chk1.setEstadoDocumento(0);
        chk1.setDescTipoDocumento("CHECK LIST SIEMBRA");
        chk1.setIdAnexo(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));
        chk1.setEstadoSincronizazion(0);
        chk1.setIdDocumentoPendiente(454);
        chk1.setExpanded(false);

        List<String> nested1 = new ArrayList<>();
        nested1.add("1");
        nested1.add("2");
        nested1.add("3");
        nested1.add("4");
        nested1.add("5");
        chk1.setNestedChecks(nested1);
        checkLists.add(chk1);


        CheckList chk2 = new CheckList();
        chk2.setEstadoDocumento(1);
        chk2.setDescTipoDocumento("CAPACITACION SIEMBRA");
        chk2.setIdAnexo(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));
        chk2.setEstadoSincronizazion(1);
        chk2.setExpanded(false);
        List<String> nested2 = new ArrayList<>();
        nested2.add("1");
        nested2.add("2");
        nested2.add("3");
        nested2.add("4");
        nested2.add("5");
        nested2.add("6");
        chk1.setNestedChecks(nested2);
        checkLists.add(chk2);


        CheckList chk3 = new CheckList();
        chk3.setEstadoDocumento(0);
        chk3.setDescTipoDocumento("CHECK LIST COSECHA");
        chk3.setIdAnexo(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));
        chk3.setEstadoSincronizazion(0);
        chk3.setExpanded(false);
        chk3.setNestedChecks(Collections.emptyList());
        checkLists.add(chk3);

        CheckList chk4 = new CheckList();
        chk4.setEstadoDocumento(0);
        chk4.setDescTipoDocumento("CAPACITACION COSECHA");
        chk4.setIdAnexo(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));
        chk4.setEstadoSincronizazion(0);
        chk4.setExpanded(false);
        chk4.setNestedChecks(Collections.emptyList());
        checkLists.add(chk4);


        LinearLayoutManager lManager = null;
        if (activity != null){
            lManager  = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        }
        rv_checklist.setHasFixedSize(true);
        rv_checklist.setLayoutManager(lManager);


        adapter = new CheckListAdapter(
                checkLists,
                nuevoCheckList -> { },
                editCheckList -> { }
        );

        rv_checklist.setAdapter(adapter);
    }


    private void bind (View view){

        rv_checklist = view.findViewById(R.id.rv_checklist);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (activity != null){
            activity.updateView(
                    getResources().getString(R.string.app_name),
                    (anexoCompleto != null)
                        ? " C. virtual Anexo "+anexoCompleto.getAnexoContrato().getAnexo_contrato()
                        : getResources().getString(R.string.subtitles_visit));
        }
    }
}
