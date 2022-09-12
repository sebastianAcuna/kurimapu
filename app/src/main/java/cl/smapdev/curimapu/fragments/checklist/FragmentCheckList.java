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
import cl.smapdev.curimapu.clases.relaciones.CheckListCapCompleto;
import cl.smapdev.curimapu.clases.relaciones.CheckListRequest;
import cl.smapdev.curimapu.clases.tablas.AnexoCorreoFechas;
import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembra;
import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembraDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListDetails;
import cl.smapdev.curimapu.clases.tablas.CheckListSiembra;
import cl.smapdev.curimapu.clases.tablas.CheckLists;
import cl.smapdev.curimapu.clases.temporales.TempVisitas;
import cl.smapdev.curimapu.clases.utilidades.InternetStateClass;
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

                Future<List<CheckListCapacitacionSiembra>> checkListCapacitacionSiembraFuture
                        = executorService.submit(()
                        -> MainActivity.myAppDB.DaoCheckListCapSiembra()
                        .getClCapSiembraByEstado( 0));

                try {

                    List<CheckListSiembra> chk = chkF.get();

                    List<CheckListCapacitacionSiembra> capSiembraCab
                            = checkListCapacitacionSiembraFuture.get();

                    if(chk.size() <= 0 && capSiembraCab.size() <= 0){
                        executorService.shutdown();
                        Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                        return true;
                    }

                    CheckListRequest chkS = new CheckListRequest();
                    List<CheckListCapCompleto> chkList = new ArrayList<>();

                    if(capSiembraCab.size() > 0){

                        for (CheckListCapacitacionSiembra clc : capSiembraCab) {
                            List<CheckListCapacitacionSiembraDetalle> detalle =
                                    executorService.submit(() -> MainActivity.myAppDB
                                            .DaoCheckListCapSiembra()
                                            .getCapSiembraDetallesByPadre(clc.getClave_unica())
                                    ).get();

                            CheckListCapCompleto completo = new CheckListCapCompleto();
                            completo.setCabecera(clc);
                            completo.setDetalles(detalle);

                            chkList.add(completo);
                        }

                        chkS.setCheckListCapCompletos( chkList );
                    }

                    if(chk.size() > 0){
                        chkS.setCheckListSiembras( chk );
                    }
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
        capacitacionSiembra.setDescCheckList("CHECK LIST CAPACITACION SIEMBRA");
        capacitacionSiembra.setIdAnexo(Integer.parseInt(anexoCompleto
                .getAnexoContrato().getId_anexo_contrato()));
        capacitacionSiembra.setExpanded(false);
        capacitacionSiembra.setTipoCheckList(Utilidades.TIPO_DOCUMENTO_CAPACITACION_SIEMBRA);


        List<CheckListCapacitacionSiembra> clCapSiembras;
        Future<List<CheckListCapacitacionSiembra>> clCapSiembraFuture =
                ex.submit(() -> MainActivity.myAppDB
                        .DaoCheckListCapSiembra().getAllClCapSiembraByAc(capacitacionSiembra.getIdAnexo()));


        try {
            clCapSiembras = clCapSiembraFuture.get();
            if(clCapSiembras.size() > 0){
                List<CheckListDetails> nested = new ArrayList<>();
                for (CheckListCapacitacionSiembra clCapSiembra : clCapSiembras){
                    CheckListDetails tmp = new CheckListDetails();
                    tmp.setDescription(clCapSiembra.getApellido_checklist());
                    tmp.setUploaded((clCapSiembra.getEstado_sincronizacion() > 0));
                    tmp.setId(clCapSiembra.getId_cl_cap_siembra());
                    tmp.setIdAnexo(clCapSiembra.getId_ac_cl_cap_siembra());
                    tmp.setEstado(clCapSiembra.getEstado_documento());
                    tmp.setClave_unica(clCapSiembra.getClave_unica());
                    tmp.setTipo_documento(Utilidades.TIPO_DOCUMENTO_CAPACITACION_SIEMBRA);
                    tmp.setDescEstado((clCapSiembra.getEstado_documento() <= 0) ? "SIN ESTADO" : (clCapSiembra.getEstado_documento() > 1) ? "PENDIENTE" : "ACTIVA" );
                    nested.add(tmp);
                }
                capacitacionSiembra.setDetails(nested);
            }else{
                capacitacionSiembra.setDetails(Collections.emptyList());
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            capacitacionSiembra.setDetails(Collections.emptyList());
        }


        CheckLists checkListSiembra = new CheckLists();
        checkListSiembra.setDescCheckList("CHECK LIST SIEMBRA");
        checkListSiembra.setIdAnexo(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));
        checkListSiembra.setExpanded(false);
        checkListSiembra.setTipoCheckList(Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA);

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
                    tmp.setClave_unica(clSiembra.getClave_unica());
                    tmp.setTipo_documento(Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA);
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

                    switch (nuevoCheckList.getTipoCheckList()){
                        case Utilidades.TIPO_DOCUMENTO_CAPACITACION_SIEMBRA:

                            ExecutorService executorServiceCap = Executors.newSingleThreadExecutor();
                            try {
                                executorServiceCap.submit(()
                                        -> MainActivity.myAppDB.DaoFirmas()
                                        .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CAPACITACION_SIEMBRA)
                                ).get();

                                executorServiceCap.submit(() -> MainActivity.myAppDB
                                        .DaoCheckListCapSiembra()
                                        .deleteDetalles()).get();
                                executorServiceCap.shutdown();

                                activity.cambiarFragment(
                                        new FragmentCheckListCapacitacionSiembra(),
                                        Utilidades.FRAGMENT_CHECKLIST_CAPACITACION_SIEMBRA,
                                        R.anim.slide_in_left,R.anim.slide_out_left
                                );

                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                                executorServiceCap.shutdown();
                            }

                            break;
                        case Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA:
                            ExecutorService executorService = Executors.newSingleThreadExecutor();
                            try {
                                executorService.submit(()
                                        -> MainActivity.myAppDB.DaoFirmas()
                                        .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA)
                                ).get();

                                executorService.shutdown();

                                activity.cambiarFragment(
                                        new FragmentCheckListSiembra(),
                                        Utilidades.FRAGMENT_CHECKLIST_SIEMBRA,
                                        R.anim.slide_in_left,R.anim.slide_out_left
                                );

                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                                executorService.shutdown();
                            }


                            break;
                        default:break;
                    }

                },
                (checkListPDF, detailsPDF) -> {

                    String URLPDF = "";
                    Intent i = new Intent(Intent.ACTION_VIEW);

                    switch (checkListPDF.getTipoCheckList()){

                        case Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA:
                             URLPDF = "http://" + Utilidades.IP_PRODUCCION + "/curimapu/docs/pdf/checklistSiembra.php?clave_unica=";
                            i.setData(Uri.parse(URLPDF+detailsPDF.getClave_unica()));
                        break;
                        case Utilidades.TIPO_DOCUMENTO_CAPACITACION_SIEMBRA:
                            URLPDF = "http://" + Utilidades.IP_PRODUCCION + "/curimapu/docs/pdf/checklistCapacitaSiembra.php?clave_unica=";
                            i.setData(Uri.parse(URLPDF+detailsPDF.getClave_unica()));
                        break;
                    }


                    startActivity(i);

                },
                (checkListEditar, detailsEditar) -> {

                    switch (checkListEditar.getTipoCheckList()){
                        case Utilidades.TIPO_DOCUMENTO_CAPACITACION_SIEMBRA:
                            ExecutorService executorService2 = Executors.newSingleThreadExecutor();
                            executorService2.submit(()
                                    -> MainActivity.myAppDB.DaoFirmas()
                                    .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA));
                            executorService2.shutdown();

                            ExecutorService exec2 = Executors.newSingleThreadExecutor();

                            Future<CheckListCapacitacionSiembra> ck2 = exec2.submit(()
                                    -> MainActivity.myAppDB.DaoCheckListCapSiembra()
                                    .getClCapSiembraByAc(detailsEditar.getId()));

                            try {
                                CheckListCapacitacionSiembra cls = ck2.get();
                                FragmentCheckListCapacitacionSiembra  fs = FragmentCheckListCapacitacionSiembra.newInstance(cls);
                                activity.cambiarFragment(
                                        fs,
                                        Utilidades.FRAGMENT_CHECKLIST_CAPACITACION_SIEMBRA,
                                        R.anim.slide_in_left,R.anim.slide_out_left
                                );


                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        break;

                        case Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA:
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
                            break;
                        default:
                            break;

                    }


                },
                (checkListSubir, detailsSubir) -> {


                    switch (checkListSubir.getTipoCheckList()){

                        case Utilidades.TIPO_DOCUMENTO_CAPACITACION_SIEMBRA:

                            ExecutorService executorCapSiembra = Executors.newSingleThreadExecutor();
                            Future<CheckListCapacitacionSiembra> checkListCapacitacionSiembraFuture
                                    = executorCapSiembra.submit(()
                                    -> MainActivity.myAppDB.DaoCheckListCapSiembra()
                                    .getClCapSiembraByAcAndEstado(detailsSubir.getId(), 0));

                            try {
                                CheckListCapacitacionSiembra capSiembraCab
                                        = checkListCapacitacionSiembraFuture.get();

                                if(capSiembraCab == null){
                                    executorCapSiembra.shutdown();
                                    Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                                    return;
                                }

                                CheckListRequest chk = new CheckListRequest();

                                List<CheckListCapacitacionSiembraDetalle> detalle =
                                        executorCapSiembra.submit(() -> MainActivity.myAppDB
                                                .DaoCheckListCapSiembra()
                                                .getCapSiembraDetallesByPadre(capSiembraCab.getClave_unica())
                                        ).get();

                                CheckListCapCompleto completo = new CheckListCapCompleto();

                                completo.setCabecera(capSiembraCab);
                                completo.setDetalles(detalle);

                                List<CheckListCapCompleto> chkList = new ArrayList<>();
                                chkList.add( completo );

                                chk.setCheckListCapCompletos( chkList );
                                prepararSubir( chk );


                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                                executorCapSiembra.shutdown();
                            }

                        break;
                        case Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA:

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

                            break;

                        default:
                            break;
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
