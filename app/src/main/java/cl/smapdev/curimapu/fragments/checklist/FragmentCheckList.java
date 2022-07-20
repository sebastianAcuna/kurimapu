package cl.smapdev.curimapu.fragments.checklist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import cl.smapdev.curimapu.clases.tablas.CheckListDetails;
import cl.smapdev.curimapu.clases.tablas.CheckLists;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import es.dmoral.toasty.Toasty;

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


        List<CheckLists> checkLists = new ArrayList<>();

        CheckLists chk1 = new CheckLists();
        CheckLists chk2 = new CheckLists();
        chk1.setDescCheckList("CHECK LIST SIEMBRA");
        chk1.setIdAnexo(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));
        chk1.setExpanded(false);

        chk2.setDescCheckList("CHECK LIST COSECHA");
        chk2.setIdAnexo(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));
        chk2.setExpanded(false);

        List<CheckListDetails> nested1 = new ArrayList<>();
        List<CheckListDetails> nested2 = new ArrayList<>();
        CheckListDetails nd1 = new CheckListDetails();
        nd1.setDescription("HOLA 1 ");
        CheckListDetails nd2 = new CheckListDetails();
        nd2.setDescription("HOLA 1 ");
        CheckListDetails nd3 = new CheckListDetails();
        nd3.setDescription("HOLA 3 ");
        CheckListDetails nd4 = new CheckListDetails();
        nd4.setDescription("HOLA 4 ");
        CheckListDetails nd5 = new CheckListDetails();
        nd5.setDescription("HOLA 5 ");
        nested1.add(nd1);
        nested1.add(nd2);
        nested1.add(nd3);
        nested1.add(nd4);
        nested1.add(nd5);

        nested2.add(nd1);
        nested2.add(nd2);
        nested2.add(nd3);
        nested2.add(nd4);
        nested2.add(nd5);
        chk1.setDetails(nested1);
        chk2.setDetails(nested2);
        checkLists.add(chk1);
        checkLists.add(chk2);


        LinearLayoutManager lManager = null;
        if (activity != null){
            lManager  = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        }
        rv_checklist.setHasFixedSize(true);
        rv_checklist.setLayoutManager(lManager);


        adapter = new CheckListAdapter(
                checkLists,
                nuevoCheckList -> {
                    activity.cambiarFragment(
                            new FragmentCheckListSiembra(),
                            Utilidades.FRAGMENT_CHECKLIST_SIEMBRA,
                            R.anim.slide_in_left,R.anim.slide_out_left
                    );
                },
                (checkListPDF, detailsPDF) -> {
                    Toasty.success(requireContext(), "PDF", Toast.LENGTH_SHORT, true).show();
                },
                (checkListEditar, detailsEditar) -> {
                    Toasty.success(requireContext(), "EDITAR", Toast.LENGTH_SHORT, true).show();
                },
                (checkListSubir, detailsSubir) -> {
                    Toasty.success(requireContext(), "SUBIR", Toast.LENGTH_SHORT, true).show();
                }
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
