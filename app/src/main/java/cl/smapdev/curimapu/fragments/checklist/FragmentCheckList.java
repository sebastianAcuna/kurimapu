package cl.smapdev.curimapu.fragments.checklist;

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
import cl.smapdev.curimapu.clases.modelo.CheckListSync;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.relaciones.CheckListRequest;
import cl.smapdev.curimapu.clases.tablas.CheckListAplicacionHormonas;
import cl.smapdev.curimapu.clases.tablas.CheckListDetails;
import cl.smapdev.curimapu.clases.tablas.CheckListSiembra;
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
        if (a != null) activity = a;
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
        switch (item.getItemId()) {
            case R.id.menu_upload_files:

                ExecutorService executorService = Executors.newSingleThreadExecutor();
                Future<List<CheckListSiembra>> chkF = executorService.submit(()
                        -> MainActivity.myAppDB.DaoClSiembra()
                        .getClSiembraToSync());

                Future<List<CheckListAplicacionHormonas>> chkApHor = executorService.submit(() ->
                        MainActivity.myAppDB.DaoCLAplicacionHormonas().getClApHormonasToSync());


                try {

                    List<CheckListSiembra> chk = chkF.get();
                    List<CheckListAplicacionHormonas> chkA = chkApHor.get();


                    if (chk.isEmpty() && chkA.isEmpty()) {
                        executorService.shutdown();
                        Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                        return true;
                    }

                    CheckListRequest chkS = new CheckListRequest();

                    if (!chkA.isEmpty()) {
                        chkS.setCheckListAplicacionHormonas(chkA);
                    }

                    if (!chk.isEmpty()) {
                        chkS.setCheckListSiembras(chk);
                    }
                    prepararSubir(chkS);

                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                    executorService.shutdown();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private CheckLists getCheckListAplicacionHormonas(ExecutorService ex) {

        CheckLists chkAppHormonas = new CheckLists();
        chkAppHormonas.setDescCheckList("CHECK LIST APLICACION HORMONAS");
        chkAppHormonas.setIdAnexo(Integer.parseInt(anexoCompleto
                .getAnexoContrato().getId_anexo_contrato()));
        chkAppHormonas.setExpanded(false);
        chkAppHormonas.setTipoCheckList(Utilidades.TIPO_DOCUMENTO_CHECKLIST_APLICACION_HORMONAS);

        List<CheckListAplicacionHormonas> clAppHormonas;
        Future<List<CheckListAplicacionHormonas>> clCapSiembraFuture =
                ex.submit(() -> MainActivity.myAppDB
                        .DaoCLAplicacionHormonas().getAllClApHormonasByAc(chkAppHormonas.getIdAnexo()));

        try {

            clAppHormonas = clCapSiembraFuture.get();
            if (!clAppHormonas.isEmpty()) {
                List<CheckListDetails> nested = new ArrayList<>();
                for (CheckListAplicacionHormonas clApHorm : clAppHormonas) {
                    CheckListDetails tmp = new CheckListDetails();
                    tmp.setDescription(clApHorm.getApellido_checklist());
                    tmp.setUploaded((clApHorm.getEstado_sincronizacion() > 0));
                    tmp.setId(clApHorm.getId_cl_ap_hormonas());
                    tmp.setIdAnexo(clApHorm.getId_ac_cl_ap_hormonas());
                    tmp.setEstado(clApHorm.getEstado_documento());
                    tmp.setClave_unica(clApHorm.getClave_unica());
                    tmp.setTipo_documento(Utilidades.TIPO_DOCUMENTO_CHECKLIST_APLICACION_HORMONAS);
                    tmp.setDescEstado((clApHorm.getEstado_documento() <= 0) ? "SIN ESTADO" : (clApHorm.getEstado_documento() > 1) ? "PENDIENTE" : "ACTIVA");
                    nested.add(tmp);
                }
                chkAppHormonas.setDetails(nested);
            } else {
                chkAppHormonas.setDetails(Collections.emptyList());
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            chkAppHormonas.setDetails(Collections.emptyList());
        }
        return chkAppHormonas;
    }


    private CheckLists getCheckListSiembra(ExecutorService ex) {

        CheckLists chkSiembra = new CheckLists();
        chkSiembra.setDescCheckList("CHECK LIST SIEMBRA");
        chkSiembra.setIdAnexo(Integer.parseInt(anexoCompleto
                .getAnexoContrato().getId_anexo_contrato()));
        chkSiembra.setExpanded(false);
        chkSiembra.setTipoCheckList(Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA);

        List<CheckListSiembra> clSiembras;
        Future<List<CheckListSiembra>> clCapSiembraFuture =
                ex.submit(() -> MainActivity.myAppDB
                        .DaoClSiembra().getAllClSiembraByAc(chkSiembra.getIdAnexo()));

        try {

            clSiembras = clCapSiembraFuture.get();
            if (!clSiembras.isEmpty()) {
                List<CheckListDetails> nested = new ArrayList<>();
                for (CheckListSiembra clCapSiembra : clSiembras) {
                    CheckListDetails tmp = new CheckListDetails();
                    tmp.setDescription(clCapSiembra.getApellido_checklist());
                    tmp.setUploaded((clCapSiembra.getEstado_sincronizacion() > 0));
                    tmp.setId(clCapSiembra.getId_cl_siembra());
                    tmp.setIdAnexo(clCapSiembra.getId_ac_cl_siembra());
                    tmp.setEstado(clCapSiembra.getEstado_documento());
                    tmp.setClave_unica(clCapSiembra.getClave_unica());
                    tmp.setTipo_documento(Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA);
                    tmp.setDescEstado((clCapSiembra.getEstado_documento() <= 0) ? "SIN ESTADO" : (clCapSiembra.getEstado_documento() > 1) ? "PENDIENTE" : "ACTIVA");
                    nested.add(tmp);
                }
                chkSiembra.setDetails(nested);
            } else {
                chkSiembra.setDetails(Collections.emptyList());
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            chkSiembra.setDetails(Collections.emptyList());
        }
        return chkSiembra;
    }


    private void cargarLista() {

        ExecutorService ex = Executors.newSingleThreadExecutor();

        List<CheckLists> checkLists = new ArrayList<>();


        CheckLists clSiembra = getCheckListSiembra(ex);
        CheckLists clApHormonas = getCheckListAplicacionHormonas(ex);


        ex.shutdown();

        checkLists.add(clSiembra);
        checkLists.add(clApHormonas);

        LinearLayoutManager lManager = null;
        if (activity != null) {
            lManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        }
        rv_checklist.setHasFixedSize(true);
        rv_checklist.setLayoutManager(lManager);


        adapter = new CheckListAdapter(
                checkLists,
                nuevoCheckList -> {

                    ExecutorService executorServiceCap = Executors.newSingleThreadExecutor();

                    switch (nuevoCheckList.getTipoCheckList()) {
                        case Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA:
                            try {
                                executorServiceCap.submit(()
                                        -> MainActivity.myAppDB.DaoFirmas()
                                        .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA)
                                ).get();

                                executorServiceCap.shutdown();

                                activity.cambiarFragment(
                                        new FragmentChecklistSiembra(),
                                        Utilidades.FRAGMENT_CHECKLIST_SIEMBRA,
                                        R.anim.slide_in_left, R.anim.slide_out_left
                                );

                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                                executorServiceCap.shutdown();
                            }
                            break;
                        case Utilidades.TIPO_DOCUMENTO_CHECKLIST_APLICACION_HORMONAS:
                            try {
                                executorServiceCap.submit(()
                                        -> MainActivity.myAppDB.DaoFirmas()
                                        .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_APLICACION_HORMONAS)
                                ).get();

                                executorServiceCap.shutdown();

                                activity.cambiarFragment(
                                        new FragmentChecklistAplicacionHormonas(),
                                        Utilidades.FRAGMENT_CHECKLIST_APLICACION_HORMONAS,
                                        R.anim.slide_in_left, R.anim.slide_out_left
                                );

                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                                executorServiceCap.shutdown();
                            }
                            break;
                    }
                },
                (checkListPDF, detailsPDF) -> {

                    String URLPDF = "";
                    Intent i = new Intent(Intent.ACTION_VIEW);

                    switch (checkListPDF.getTipoCheckList()) {
                        case Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA:
                            URLPDF = Utilidades.URL_SERVER_API + "/docs/pdf/checkListAplicacionHormonas.php?clave_unica=";
                            i.setData(Uri.parse(URLPDF + detailsPDF.getClave_unica()));
                            break;

                    }
                    startActivity(i);

                },
                (checkListEditar, detailsEditar) -> {
                    ExecutorService executorServiceCap = Executors.newSingleThreadExecutor();

                    switch (checkListEditar.getTipoCheckList()) {
                        case Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA:
                            try {
                                executorServiceCap.submit(()
                                        -> MainActivity.myAppDB.DaoFirmas()
                                        .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA)
                                ).get();

                                CheckListSiembra cl = executorServiceCap.submit(() -> MainActivity.myAppDB.DaoClSiembra().getClSiembraById(detailsEditar.getId())).get();

                                executorServiceCap.shutdown();

                                activity.cambiarFragment(
                                        FragmentChecklistSiembra.newInstance(cl),
                                        Utilidades.FRAGMENT_CHECKLIST_SIEMBRA,
                                        R.anim.slide_in_left, R.anim.slide_out_left
                                );

                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                                executorServiceCap.shutdown();
                            }
                            break;
                        case Utilidades.TIPO_DOCUMENTO_CHECKLIST_APLICACION_HORMONAS:
                            try {
                                executorServiceCap.submit(()
                                        -> MainActivity.myAppDB.DaoFirmas()
                                        .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_APLICACION_HORMONAS)
                                ).get();

                                CheckListAplicacionHormonas cl = executorServiceCap.submit(() -> MainActivity.myAppDB.DaoCLAplicacionHormonas().getAllClApHormonasById(detailsEditar.getId())).get();

                                executorServiceCap.shutdown();

                                activity.cambiarFragment(
                                        FragmentChecklistAplicacionHormonas.newInstance(cl),
                                        Utilidades.FRAGMENT_CHECKLIST_APLICACION_HORMONAS,
                                        R.anim.slide_in_left, R.anim.slide_out_left
                                );

                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                                executorServiceCap.shutdown();
                            }
                            break;
                    }
                },
                (checkListSubir, detailsSubir) -> {
                    ExecutorService executorServiceCap = Executors.newSingleThreadExecutor();

                    switch (checkListSubir.getTipoCheckList()) {
                        case Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA:
                            try {
                                CheckListSiembra cl = executorServiceCap.submit(() ->
                                        MainActivity.myAppDB.DaoClSiembra().getClSiembraById(detailsSubir.getId(), 0)).get();

                                if (cl == null) {
                                    executorServiceCap.shutdown();
                                    Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                                    return;
                                }

                                CheckListRequest chk = new CheckListRequest();
                                List<CheckListSiembra> chkList = new ArrayList<>();

                                chkList.add(cl);
                                chk.setCheckListSiembras(chkList);
                                prepararSubir(chk);
                                executorServiceCap.shutdown();
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                                executorServiceCap.shutdown();
                            }
                            break;
                        case Utilidades.TIPO_DOCUMENTO_CHECKLIST_APLICACION_HORMONAS:
                            try {
                                CheckListAplicacionHormonas cl = executorServiceCap.submit(() ->
                                        MainActivity.myAppDB.DaoCLAplicacionHormonas().getAllClApHormonasByIdAndEstado(detailsSubir.getId(), 0)).get();

                                if (cl == null) {
                                    executorServiceCap.shutdown();
                                    Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                                    return;
                                }

                                CheckListRequest chk = new CheckListRequest();
                                List<CheckListAplicacionHormonas> chkList = new ArrayList<>();

                                chkList.add(cl);
                                chk.setCheckListAplicacionHormonas(chkList);
                                prepararSubir(chk);
                                executorServiceCap.shutdown();
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                                executorServiceCap.shutdown();
                            }
                            break;
                    }
                }
        );

        rv_checklist.setAdapter(adapter);
    }

    private void prepararSubir(CheckListRequest checkListRequest) {

        new CheckListSync(checkListRequest, requireActivity(), (state, message) -> {
            if (state) {
                cargarLista();
                Toasty.success(requireActivity(), message, Toast.LENGTH_LONG, true).show();
            } else {
                Toasty.error(requireActivity(), message, Toast.LENGTH_LONG, true).show();
            }
        });
    }


    private void bind(View view) {
        rv_checklist = view.findViewById(R.id.rv_checklist);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (activity != null) {
            activity.updateView(
                    getResources().getString(R.string.app_name),
                    (anexoCompleto != null)
                            ? " C. virtual Anexo " + anexoCompleto.getAnexoContrato().getAnexo_contrato()
                            : getResources().getString(R.string.subtitles_visit));
        }
    }
}
