package cl.smapdev.curimapu.fragments.checklist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import cl.smapdev.curimapu.clases.modelo.AnexoCorreoFechaSync;
import cl.smapdev.curimapu.clases.modelo.CheckListSync;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.relaciones.CheckListRequest;
import cl.smapdev.curimapu.clases.tablas.AnexoCorreoFechas;
import cl.smapdev.curimapu.clases.tablas.CheckListDetails;
import cl.smapdev.curimapu.clases.tablas.CheckListSiembra;
import cl.smapdev.curimapu.clases.tablas.CheckLists;
import cl.smapdev.curimapu.clases.temporales.TempVisitas;
import cl.smapdev.curimapu.clases.utilidades.InternetStateClass;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.dialogos.DialogObservationTodo;
import cl.smapdev.curimapu.fragments.dialogos.DialogWebViewPDF;
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

        setHasOptionsMenu(true);

        executor.shutdown();
        bind(view);

        cargarLista();
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
                Future<List<CheckListSiembra>> chkF = executorService.submit(()
                        -> MainActivity.myAppDB.DaoClSiembra()
                        .getClSiembraToSync());

                try {
                    List<CheckListSiembra> chk = chkF.get();

                    if(chk.size() <= 0){
                        executorService.shutdown();
                        Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                        return true;
                    }

                    CheckListRequest chkS = new CheckListRequest();

                    chkS.setCheckListSiembras( chk );
                    prepararSubir( chkS );

                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private void cargarLista(){

        ExecutorService ex = Executors.newSingleThreadExecutor();

        List<CheckLists> checkLists = new ArrayList<>();

        CheckLists capacitacionSiembra = new CheckLists();
        capacitacionSiembra.setDescCheckList("CAPACITACION SIEMBRA");
        capacitacionSiembra.setIdAnexo(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));
        capacitacionSiembra.setExpanded(false);
        capacitacionSiembra.setDetails(Collections.emptyList());
        capacitacionSiembra.setTipoCheckList(5);


        CheckLists checkListSiembra = new CheckLists();
        checkListSiembra.setDescCheckList("CHECK LIST SIEMBRA");
        checkListSiembra.setIdAnexo(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));
        checkListSiembra.setExpanded(false);

        List<CheckListSiembra> clSiembras;
        Future<List<CheckListSiembra>> clSiembraFuture = ex.submit(() -> MainActivity.myAppDB.DaoClSiembra().getAllClSiembraByAc(checkListSiembra.getIdAnexo()));

        try {
            clSiembras = clSiembraFuture.get();
            if(clSiembras.size() > 0){
                List<CheckListDetails> nested = new ArrayList<>();
                for (CheckListSiembra clSiembra : clSiembras){
                    CheckListDetails tmp = new CheckListDetails();
                    tmp.setDescription(clSiembra.getApellido_checklist());
                    tmp.setUploaded((clSiembra.getEstado_sincronizacion() > 0));
                    tmp.setId(clSiembra.getId_cl_siembra());
                    tmp.setIdAnexo(clSiembra.getId_ac_cl_siembra());
                    tmp.setEstado(clSiembra.getEstado_documento());
                    tmp.setDescEstado((clSiembra.getEstado_documento() <= 0) ? "SIN ESTADO" : (clSiembra.getEstado_documento() > 1) ? "PENDIENTE" : "ACTIVA" );
                    nested.add(tmp);
                }
                checkListSiembra.setDetails(nested);
            }else{
                checkListSiembra.setDetails(Collections.emptyList());
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            checkListSiembra.setDetails(Collections.emptyList());
        }

        ex.shutdown();

        checkLists.add(capacitacionSiembra);
        checkLists.add(checkListSiembra);


        LinearLayoutManager lManager = null;
        if (activity != null){
            lManager  = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        }
        rv_checklist.setHasFixedSize(true);
        rv_checklist.setLayoutManager(lManager);


        adapter = new CheckListAdapter(
                checkLists,
                nuevoCheckList -> {
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.submit(()
                            -> MainActivity.myAppDB.DaoFirmas()
                            .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA));
                    executorService.shutdown();

                    activity.cambiarFragment(
                            new FragmentCheckListSiembra(),
                            Utilidades.FRAGMENT_CHECKLIST_SIEMBRA,
                            R.anim.slide_in_left,R.anim.slide_out_left
                    );
                },
                (checkListPDF, detailsPDF) -> {


                    String URLPDF = "http://" + Utilidades.IP_PRODUCCION + "/curimapu/docs/pdf/checklistSiembra.php?clave_unica=";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(URLPDF+"272220220810165310"));
                    startActivity(i);

                },
                (checkListEditar, detailsEditar) -> {

                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.submit(()
                            -> MainActivity.myAppDB.DaoFirmas()
                            .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA));
                    executorService.shutdown();

                    ExecutorService exec = Executors.newSingleThreadExecutor();

                    Future<CheckListSiembra> ck = exec.submit(()
                            -> MainActivity.myAppDB.DaoClSiembra()
                            .getClSiembraById(detailsEditar.getId()));

                    try {
                        CheckListSiembra cls = ck.get();
                        FragmentCheckListSiembra  fs = FragmentCheckListSiembra.newInstance(cls);
                        activity.cambiarFragment(
                                fs,
                                Utilidades.FRAGMENT_CHECKLIST_SIEMBRA,
                                R.anim.slide_in_left,R.anim.slide_out_left
                        );


                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                },
                (checkListSubir, detailsSubir) -> {

                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    Future<CheckListSiembra> chkF = executorService.submit(()
                            -> MainActivity.myAppDB.DaoClSiembra()
                            .getClSiembraById(detailsSubir.getId(), 0));

                    try {
                        CheckListSiembra checkListSiembras = chkF.get();

                        if(checkListSiembras == null){
                            executorService.shutdown();
                            Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                            return;
                        }

                        CheckListRequest chk = new CheckListRequest();

                        List<CheckListSiembra> chkList = new ArrayList<>();
                        chkList.add( checkListSiembras );

                        chk.setCheckListSiembras( chkList );
                        prepararSubir( chk );


                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                        executorService.shutdown();
                    }
                }
        );

        rv_checklist.setAdapter(adapter);
    }

    private void prepararSubir(CheckListRequest checkListRequest){

        InternetStateClass mm = new InternetStateClass(activity, result -> {
            if(!result){
                Toasty.error(activity, activity.getResources().getString(R.string.sync_not_internet), Toast.LENGTH_SHORT, true).show();
                return;
            }


            ProgressDialog pd = new ProgressDialog(activity);
            pd.setMessage("conectandose a internet, espere por favor");
            pd.show();


            if(pd.isShowing()){
                pd.dismiss();
            }

            new CheckListSync( checkListRequest, requireActivity(), (state, message) -> {
                if(state){
                    cargarLista();
                    Toasty.success(requireActivity(), message, Toast.LENGTH_LONG, true).show();
                }else{
                    Toasty.error(requireActivity(), message, Toast.LENGTH_LONG, true).show();
                }
            });

        }, 1);
        mm.execute();
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
