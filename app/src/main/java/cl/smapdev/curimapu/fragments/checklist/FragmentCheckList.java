package cl.smapdev.curimapu.fragments.checklist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
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
import cl.smapdev.curimapu.clases.relaciones.CheckListCapCompleto;
import cl.smapdev.curimapu.clases.relaciones.CheckListLimpiezaCamionesCompleto;
import cl.smapdev.curimapu.clases.relaciones.CheckListRequest;
import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembra;
import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembraDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListCosecha;
import cl.smapdev.curimapu.clases.tablas.CheckListDetails;
import cl.smapdev.curimapu.clases.tablas.CheckListLimpiezaCamiones;
import cl.smapdev.curimapu.clases.tablas.CheckListSiembra;
import cl.smapdev.curimapu.clases.tablas.CheckLists;
import cl.smapdev.curimapu.clases.tablas.ChecklistDevolucionSemilla;
import cl.smapdev.curimapu.clases.tablas.ChecklistLimpiezaCamionesDetalle;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import es.dmoral.toasty.Toasty;

public class FragmentCheckList extends Fragment {

    private RecyclerView rv_checklist;

    private SharedPreferences prefs;
    private MainActivity activity;
    private AnexoCompleto anexoCompleto = null;

    private CheckListAdapter adapter;

    private ExecutorService executors;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            activity = (MainActivity) context;
            prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
        } else {
            throw new RuntimeException(context.toString() + " must be MainActivity");
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executors != null && !executors.isShutdown()) {
            executors.shutdown();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        executors = Executors.newSingleThreadExecutor();
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

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.menu_sube_fechas, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_upload_files) {

                    ejecutarSeguro(() -> {

                        List<CheckListSiembra> chk = MainActivity.myAppDB.DaoClSiembra()
                                .getClSiembraToSync();


                        List<CheckListCosecha> chkC = MainActivity.myAppDB.DaoCheckListCosecha()
                                .getClCosechaToSync();

                        List<CheckListCapacitacionSiembra> capSiembraCab
                                = MainActivity.myAppDB.DaoCheckListCapSiembra()
                                .getClCapSiembraByEstado(0, Utilidades.TIPO_DOCUMENTO_CAPACITACION_SIEMBRA);

                        List<CheckListCapacitacionSiembra> capCosechaCab
                                = MainActivity.myAppDB.DaoCheckListCapSiembra()
                                .getClCapSiembraByEstado(0, Utilidades.TIPO_DOCUMENTO_CAPACITACION_COSECHA);

                        List<CheckListLimpiezaCamiones> capLimpiezaCamionesCab
                                = MainActivity.myAppDB.DaoCheckListLimpiezaCamiones()
                                .getClLimpiezaCamionesByEstado(0);

                        List<ChecklistDevolucionSemilla> chkDS
                                = MainActivity.myAppDB.DaoCheckListDevolucionSemilla()
                                .getClDevolucionSemillaToSync();

                        if (chk.isEmpty() && chkC.isEmpty() && capSiembraCab.isEmpty() && capCosechaCab.isEmpty() && capLimpiezaCamionesCab.isEmpty() && chkDS.isEmpty()) {
                            handler.post(() -> {
                                Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                            });

                            return;
                        }
                        CheckListRequest chkS = new CheckListRequest();
                        List<CheckListCapCompleto> chkList = new ArrayList<>();
                        List<CheckListLimpiezaCamionesCompleto> chkListLimpiezaCamiones = new ArrayList<>();

                        if (!capSiembraCab.isEmpty()) {

                            for (CheckListCapacitacionSiembra clc : capSiembraCab) {
                                List<CheckListCapacitacionSiembraDetalle> detalle = MainActivity.myAppDB
                                        .DaoCheckListCapSiembra()
                                        .getCapSiembraDetallesByPadre(clc.getClave_unica(), Utilidades.TIPO_DOCUMENTO_CAPACITACION_SIEMBRA);

                                CheckListCapCompleto completo = new CheckListCapCompleto();
                                completo.setCabecera(clc);
                                completo.setDetalles(detalle);

                                chkList.add(completo);
                            }

                            chkS.setCheckListCapCompletos(chkList);
                        }

                        if (!capCosechaCab.isEmpty()) {

                            for (CheckListCapacitacionSiembra clc : capCosechaCab) {
                                List<CheckListCapacitacionSiembraDetalle> detalle = MainActivity.myAppDB
                                        .DaoCheckListCapSiembra()
                                        .getCapSiembraDetallesByPadre(clc.getClave_unica(), Utilidades.TIPO_DOCUMENTO_CAPACITACION_COSECHA);

                                CheckListCapCompleto completo = new CheckListCapCompleto();
                                completo.setCabecera(clc);
                                completo.setDetalles(detalle);

                                chkList.add(completo);
                            }

                            chkS.setCheckListCapCompletos(chkList);
                        }

                        if (!capLimpiezaCamionesCab.isEmpty()) {

                            for (CheckListLimpiezaCamiones clc : capLimpiezaCamionesCab) {
                                List<ChecklistLimpiezaCamionesDetalle> detalle = MainActivity.myAppDB
                                        .DaoCheckListLimpiezaCamiones()
                                        .getLimpiezaCamionesDetallesByPadre(clc.getClave_unica());

                                CheckListLimpiezaCamionesCompleto completo = new CheckListLimpiezaCamionesCompleto();
                                completo.setCabecera(clc);
                                completo.setDetalles(detalle);

                                chkListLimpiezaCamiones.add(completo);
                            }
                            chkS.setCheckListLimpiezaCamionesCompletos(chkListLimpiezaCamiones);
                        }

                        if (!chkC.isEmpty()) {
                            chkS.setCheckListCosechas(chkC);
                        }
                        if (!chkDS.isEmpty()) {
                            chkS.setCheckListDevolucionSemilla(chkDS);
                        }

                        if (!chk.isEmpty()) {
                            chkS.setCheckListSiembras(chk);
                        }

                        handler.post(() -> prepararSubir(chkS));
                    });
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.CREATED);

        Utilidades.setToolbar(activity, view, getResources().getString(R.string.app_name), (anexoCompleto != null)
                ? " C. virtual Anexo " + anexoCompleto.getAnexoContrato().getAnexo_contrato()
                : getResources().getString(R.string.subtitles_visit));

    }


    private CheckLists getCheckListLimpiezaCamiones(ExecutorService ex) {
        CheckLists limpiezaCamiones = new CheckLists();

        limpiezaCamiones.setDescCheckList("CHECK LIST LIMPIEZA CAMIONES");
        limpiezaCamiones.setIdAnexo(Integer.parseInt(anexoCompleto
                .getAnexoContrato().getId_anexo_contrato()));
        limpiezaCamiones.setExpanded(false);
        limpiezaCamiones.setTipoCheckList(Utilidades.TIPO_DOCUMENTO_CHECKLIST_LIMPIEZA_CAMIONES);

        List<CheckListLimpiezaCamiones> clLimpiezaCamiones;
        Future<List<CheckListLimpiezaCamiones>> clCapSiembraFuture =
                ex.submit(() -> MainActivity.myAppDB
                        .DaoCheckListLimpiezaCamiones().getAllClLimpiezaCamionesByAc(limpiezaCamiones.getIdAnexo()));


        try {

            clLimpiezaCamiones = clCapSiembraFuture.get();
            if (!clLimpiezaCamiones.isEmpty()) {
                List<CheckListDetails> nested = new ArrayList<>();
                for (CheckListLimpiezaCamiones clLimpCamiones : clLimpiezaCamiones) {
                    CheckListDetails tmp = new CheckListDetails();
                    tmp.setDescription(clLimpCamiones.getApellido_checklist());
                    tmp.setUploaded((clLimpCamiones.getEstado_sincronizacion() > 0));
                    tmp.setId(clLimpCamiones.getId_cl_limpieza_camiones());
                    tmp.setIdAnexo(clLimpCamiones.getId_ac_cl_limpieza_camiones());
                    tmp.setEstado(clLimpCamiones.getEstado_documento());
                    tmp.setClave_unica(clLimpCamiones.getClave_unica());
                    tmp.setTipo_documento(Utilidades.TIPO_DOCUMENTO_CHECKLIST_LIMPIEZA_CAMIONES);
                    tmp.setDescEstado((clLimpCamiones.getEstado_documento() <= 0) ? "SIN ESTADO" : (clLimpCamiones.getEstado_documento() > 1) ? "PENDIENTE" : "ACTIVA");
                    nested.add(tmp);
                }
                limpiezaCamiones.setDetails(nested);
            } else {
                limpiezaCamiones.setDetails(Collections.emptyList());
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            limpiezaCamiones.setDetails(Collections.emptyList());
        }
        return limpiezaCamiones;
    }

    private CheckLists getCheckListCapacitacionSiembra(ExecutorService ex) {

        CheckLists capacitacionSiembra = new CheckLists();

        capacitacionSiembra.setDescCheckList("CHECK LIST CAPACITACION SIEMBRA");
        capacitacionSiembra.setIdAnexo(Integer.parseInt(anexoCompleto
                .getAnexoContrato().getId_anexo_contrato()));
        capacitacionSiembra.setExpanded(false);
        capacitacionSiembra.setTipoCheckList(Utilidades.TIPO_DOCUMENTO_CAPACITACION_SIEMBRA);

        List<CheckListCapacitacionSiembra> clCapSiembras;
        Future<List<CheckListCapacitacionSiembra>> clCapSiembraFuture =
                ex.submit(() -> MainActivity.myAppDB
                        .DaoCheckListCapSiembra().getAllClCapSiembraByAc(capacitacionSiembra.getIdAnexo(), Utilidades.TIPO_DOCUMENTO_CAPACITACION_SIEMBRA));


        try {

            clCapSiembras = clCapSiembraFuture.get();
            if (clCapSiembras.size() > 0) {
                List<CheckListDetails> nested = new ArrayList<>();
                for (CheckListCapacitacionSiembra clCapSiembra : clCapSiembras) {
                    CheckListDetails tmp = new CheckListDetails();
                    tmp.setDescription(clCapSiembra.getApellido_checklist());
                    tmp.setUploaded((clCapSiembra.getEstado_sincronizacion() > 0));
                    tmp.setId(clCapSiembra.getId_cl_cap_siembra());
                    tmp.setIdAnexo(clCapSiembra.getId_ac_cl_cap_siembra());
                    tmp.setEstado(clCapSiembra.getEstado_documento());
                    tmp.setClave_unica(clCapSiembra.getClave_unica());
                    tmp.setTipo_documento(Utilidades.TIPO_DOCUMENTO_CAPACITACION_SIEMBRA);
                    tmp.setDescEstado((clCapSiembra.getEstado_documento() <= 0) ? "SIN ESTADO" : (clCapSiembra.getEstado_documento() > 1) ? "PENDIENTE" : "ACTIVA");
                    nested.add(tmp);
                }
                capacitacionSiembra.setDetails(nested);
            } else {
                capacitacionSiembra.setDetails(Collections.emptyList());
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            capacitacionSiembra.setDetails(Collections.emptyList());
        }
        return capacitacionSiembra;
    }

    private CheckLists getCheckListCapacitacionCosecha(ExecutorService ex) {

        CheckLists capacitacionSiembra = new CheckLists();

        capacitacionSiembra.setDescCheckList("CHECK LIST CAPACITACION COSECHA");
        capacitacionSiembra.setIdAnexo(Integer.parseInt(anexoCompleto
                .getAnexoContrato().getId_anexo_contrato()));
        capacitacionSiembra.setExpanded(false);
        capacitacionSiembra.setTipoCheckList(Utilidades.TIPO_DOCUMENTO_CAPACITACION_COSECHA);

        List<CheckListCapacitacionSiembra> clCapSiembras;
        Future<List<CheckListCapacitacionSiembra>> clCapSiembraFuture =
                ex.submit(() -> MainActivity.myAppDB
                        .DaoCheckListCapSiembra().getAllClCapSiembraByAc(capacitacionSiembra.getIdAnexo(), Utilidades.TIPO_DOCUMENTO_CAPACITACION_COSECHA));


        try {

            clCapSiembras = clCapSiembraFuture.get();
            if (clCapSiembras.size() > 0) {
                List<CheckListDetails> nested = new ArrayList<>();
                for (CheckListCapacitacionSiembra clCapSiembra : clCapSiembras) {
                    CheckListDetails tmp = new CheckListDetails();
                    tmp.setDescription(clCapSiembra.getApellido_checklist());
                    tmp.setUploaded((clCapSiembra.getEstado_sincronizacion() > 0));
                    tmp.setId(clCapSiembra.getId_cl_cap_siembra());
                    tmp.setIdAnexo(clCapSiembra.getId_ac_cl_cap_siembra());
                    tmp.setEstado(clCapSiembra.getEstado_documento());
                    tmp.setClave_unica(clCapSiembra.getClave_unica());
                    tmp.setTipo_documento(Utilidades.TIPO_DOCUMENTO_CAPACITACION_COSECHA);
                    tmp.setDescEstado((clCapSiembra.getEstado_documento() <= 0) ? "SIN ESTADO" : (clCapSiembra.getEstado_documento() > 1) ? "PENDIENTE" : "ACTIVA");
                    nested.add(tmp);
                }
                capacitacionSiembra.setDetails(nested);
            } else {
                capacitacionSiembra.setDetails(Collections.emptyList());
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            capacitacionSiembra.setDetails(Collections.emptyList());
        }
        return capacitacionSiembra;
    }

    private CheckLists getCheckListSiembra(ExecutorService ex) {

        CheckLists checkListSiembra = new CheckLists();
        checkListSiembra.setDescCheckList("CHECK LIST SIEMBRA");
        checkListSiembra.setIdAnexo(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));
        checkListSiembra.setExpanded(false);
        checkListSiembra.setTipoCheckList(Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA);

        List<CheckListSiembra> clSiembras;
        Future<List<CheckListSiembra>> clSiembraFuture = ex.submit(() -> MainActivity.myAppDB.DaoClSiembra().getAllClSiembraByAc(checkListSiembra.getIdAnexo()));

        try {
            clSiembras = clSiembraFuture.get();
            if (clSiembras.size() > 0) {
                List<CheckListDetails> nested = new ArrayList<>();
                for (CheckListSiembra clSiembra : clSiembras) {
                    CheckListDetails tmp = new CheckListDetails();
                    tmp.setDescription(clSiembra.getApellido_checklist());
                    tmp.setUploaded((clSiembra.getEstado_sincronizacion() > 0));
                    tmp.setId(clSiembra.getId_cl_siembra());
                    tmp.setIdAnexo(clSiembra.getId_ac_cl_siembra());
                    tmp.setEstado(clSiembra.getEstado_documento());
                    tmp.setClave_unica(clSiembra.getClave_unica());
                    tmp.setTipo_documento(Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA);
                    tmp.setDescEstado((clSiembra.getEstado_documento() <= 0) ? "SIN ESTADO" : (clSiembra.getEstado_documento() > 1) ? "PENDIENTE" : "ACTIVA");
                    nested.add(tmp);
                }
                checkListSiembra.setDetails(nested);
            } else {
                checkListSiembra.setDetails(Collections.emptyList());
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            checkListSiembra.setDetails(Collections.emptyList());
        }
        return checkListSiembra;
    }

    private CheckLists getCheckListCosecha(ExecutorService ex) {

        CheckLists checkListCosecha = new CheckLists();
        checkListCosecha.setDescCheckList("CHECK LIST COSECHA");
        checkListCosecha.setIdAnexo(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));
        checkListCosecha.setExpanded(false);
        checkListCosecha.setTipoCheckList(Utilidades.TIPO_DOCUMENTO_CHECKLIST_COSECHA);

        List<CheckListCosecha> clSiembras;
        Future<List<CheckListCosecha>> clSiembraFuture = ex.submit(() -> MainActivity.myAppDB.DaoCheckListCosecha().getAllClCosechaByAc(checkListCosecha.getIdAnexo()));

        try {
            clSiembras = clSiembraFuture.get();
            if (clSiembras.size() > 0) {
                List<CheckListDetails> nested = new ArrayList<>();
                for (CheckListCosecha clSiembra : clSiembras) {
                    CheckListDetails tmp = new CheckListDetails();
                    tmp.setDescription(clSiembra.getApellido_checklist());
                    tmp.setUploaded((clSiembra.getEstado_sincronizacion() > 0));
                    tmp.setId(clSiembra.getId_cl_siembra());
                    tmp.setIdAnexo(clSiembra.getId_ac_cl_siembra());
                    tmp.setEstado(clSiembra.getEstado_documento());
                    tmp.setClave_unica(clSiembra.getClave_unica());
                    tmp.setTipo_documento(Utilidades.TIPO_DOCUMENTO_CHECKLIST_COSECHA);
                    tmp.setDescEstado((clSiembra.getEstado_documento() <= 0) ? "SIN ESTADO" : (clSiembra.getEstado_documento() > 1) ? "PENDIENTE" : "ACTIVA");
                    nested.add(tmp);
                }
                checkListCosecha.setDetails(nested);
            } else {
                checkListCosecha.setDetails(Collections.emptyList());
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            checkListCosecha.setDetails(Collections.emptyList());
        }
        return checkListCosecha;
    }

    private CheckLists getCheckListDevolucionSemilla(ExecutorService ex) {

        CheckLists checkListDevolucionSemilla = new CheckLists();
        checkListDevolucionSemilla.setDescCheckList("CHECK LIST DEVOLUCION SEMILLA");
        checkListDevolucionSemilla.setIdAnexo(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));
        checkListDevolucionSemilla.setExpanded(false);
        checkListDevolucionSemilla.setTipoCheckList(Utilidades.TIPO_DOCUMENTO_CHECKLIST_DEVOLUCION_SEMILLA);

        List<ChecklistDevolucionSemilla> clDevSemilla;
        Future<List<ChecklistDevolucionSemilla>> clSiembraFuture = ex.submit(() -> MainActivity.myAppDB.DaoCheckListDevolucionSemilla().getAllClDevolucionSemillaByAc(checkListDevolucionSemilla.getIdAnexo()));

        try {
            clDevSemilla = clSiembraFuture.get();
            if (clDevSemilla.size() > 0) {
                List<CheckListDetails> nested = new ArrayList<>();
                for (ChecklistDevolucionSemilla clSiembra : clDevSemilla) {
                    CheckListDetails tmp = new CheckListDetails();
                    tmp.setDescription(clSiembra.getApellido_checklist());
                    tmp.setUploaded((clSiembra.getEstado_sincronizacion() > 0));
                    tmp.setId(clSiembra.getId_cl_devolucion_semilla());
                    tmp.setIdAnexo(clSiembra.getId_ac_cl_devolucion_semilla());
                    tmp.setEstado(clSiembra.getEstado_documento());
                    tmp.setClave_unica(clSiembra.getClave_unica());
                    tmp.setTipo_documento(Utilidades.TIPO_DOCUMENTO_CHECKLIST_DEVOLUCION_SEMILLA);
                    tmp.setDescEstado((clSiembra.getEstado_documento() <= 0) ? "SIN ESTADO" : (clSiembra.getEstado_documento() > 1) ? "PENDIENTE" : "ACTIVA");
                    nested.add(tmp);
                }
                checkListDevolucionSemilla.setDetails(nested);
            } else {
                checkListDevolucionSemilla.setDetails(Collections.emptyList());
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            checkListDevolucionSemilla.setDetails(Collections.emptyList());
        }
        return checkListDevolucionSemilla;
    }


    private void cargarLista() {

        ExecutorService ex = Executors.newSingleThreadExecutor();

        List<CheckLists> checkLists = new ArrayList<>();

        CheckLists capacitacionSiembra = getCheckListCapacitacionSiembra(ex);
        CheckLists checkListSiembra = getCheckListSiembra(ex);

        CheckLists capacitacionCosecha = getCheckListCapacitacionCosecha(ex);
        CheckLists checkListCosecha = getCheckListCosecha(ex);

        CheckLists checkListDevolucionSemilla = getCheckListDevolucionSemilla(ex);


        CheckLists limpiezaCamiones = getCheckListLimpiezaCamiones(ex);

        ex.shutdown();

        checkLists.add(capacitacionSiembra);
        checkLists.add(checkListSiembra);
        checkLists.add(capacitacionCosecha);
        checkLists.add(checkListCosecha);
        checkLists.add(limpiezaCamiones);
        checkLists.add(checkListDevolucionSemilla);

        LinearLayoutManager lManager = null;
        if (activity != null) {
            lManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        }
        rv_checklist.setHasFixedSize(true);
        rv_checklist.setLayoutManager(lManager);


        adapter = new CheckListAdapter(
                checkLists,
                nuevoCheckList -> {

                    switch (nuevoCheckList.getTipoCheckList()) {
                        case Utilidades.TIPO_DOCUMENTO_CAPACITACION_SIEMBRA:

                            ExecutorService executorServiceCap = Executors.newSingleThreadExecutor();
                            try {
                                executorServiceCap.submit(()
                                        -> MainActivity.myAppDB.DaoFirmas()
                                        .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CAPACITACION_SIEMBRA)
                                ).get();

                                executorServiceCap.submit(() -> MainActivity.myAppDB
                                        .DaoCheckListCapSiembra()
                                        .deleteDetalles(Utilidades.TIPO_DOCUMENTO_CAPACITACION_SIEMBRA)).get();
                                executorServiceCap.shutdown();

                                activity.cambiarFragment(
                                        new FragmentCheckListCapacitacionSiembra(),
                                        Utilidades.FRAGMENT_CHECKLIST_CAPACITACION_SIEMBRA,
                                        R.anim.slide_in_left, R.anim.slide_out_left
                                );

                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                                executorServiceCap.shutdown();
                            }
                            break;
                        case Utilidades.TIPO_DOCUMENTO_CHECKLIST_DEVOLUCION_SEMILLA:

                            ExecutorService executorServiceDS = Executors.newSingleThreadExecutor();
                            try {
                                executorServiceDS.submit(()
                                        -> MainActivity.myAppDB.DaoFirmas()
                                        .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_DEVOLUCION_SEMILLA)
                                ).get();

                                activity.cambiarFragment(
                                        new FragmentCheckDevolucionSemilla(),
                                        Utilidades.FRAGMENT_CHECKLIST_DEVOLUCION_SEMILLA,
                                        R.anim.slide_in_left, R.anim.slide_out_left
                                );

                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                                executorServiceDS.shutdown();
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
                                        R.anim.slide_in_left, R.anim.slide_out_left
                                );

                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                                executorService.shutdown();
                            }


                            break;
                        case Utilidades.TIPO_DOCUMENTO_CAPACITACION_COSECHA:
                            ExecutorService executorServiceCos = Executors.newSingleThreadExecutor();
                            try {
                                executorServiceCos.submit(()
                                        -> MainActivity.myAppDB.DaoFirmas()
                                        .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CAPACITACION_COSECHA)
                                ).get();

                                executorServiceCos.submit(() -> MainActivity.myAppDB
                                        .DaoCheckListCapSiembra()
                                        .deleteDetalles(Utilidades.TIPO_DOCUMENTO_CAPACITACION_COSECHA)).get();
                                executorServiceCos.shutdown();

                                activity.cambiarFragment(
                                        new FragmentCheckListCapacitacionCosecha(),
                                        Utilidades.FRAGMENT_CHECKLIST_CAPACITACION_COSECHA,
                                        R.anim.slide_in_left, R.anim.slide_out_left
                                );

                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                                executorServiceCos.shutdown();
                            }
                            break;
                        case Utilidades.TIPO_DOCUMENTO_CHECKLIST_COSECHA:

                            ExecutorService executorServiceC = Executors.newSingleThreadExecutor();
                            try {
                                executorServiceC.submit(()
                                        -> MainActivity.myAppDB.DaoFirmas()
                                        .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_COSECHA)
                                ).get();

                                executorServiceC.shutdown();

                                activity.cambiarFragment(
                                        new FragmentCheckListCosecha(),
                                        Utilidades.FRAGMENT_CHECKLIST_COSECHA,
                                        R.anim.slide_in_left, R.anim.slide_out_left
                                );

                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                                executorServiceC.shutdown();
                            }
                            break;

                        case Utilidades.TIPO_DOCUMENTO_CHECKLIST_LIMPIEZA_CAMIONES:
                            ExecutorService executorLimpiezaCamiones = Executors.newSingleThreadExecutor();
                            try {
                                executorLimpiezaCamiones.submit(()
                                        -> MainActivity.myAppDB.DaoFirmas()
                                        .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_LIMPIEZA_CAMIONES)
                                ).get();

                                executorLimpiezaCamiones.submit(() -> MainActivity.myAppDB
                                        .DaoCheckListLimpiezaCamiones()
                                        .deleteDetalles()).get();

                                executorLimpiezaCamiones.shutdown();

                                activity.cambiarFragment(
                                        new FragmentChecklistLimpiezaCamiones(),
                                        Utilidades.FRAGMENT_CHECKLIST_LIMPIEZA_CAMIONES,
                                        R.anim.slide_in_left, R.anim.slide_out_left
                                );

                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                                executorLimpiezaCamiones.shutdown();
                            }
                            break;
                        default:
                            break;
                    }

                },
                (checkListPDF, detailsPDF) -> {

                    String URLPDF = "";
                    Intent i = new Intent(Intent.ACTION_VIEW);

                    switch (checkListPDF.getTipoCheckList()) {

                        case Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA:

                            URLPDF = Utilidades.URL_SERVER_API + "/docs/pdf/checklistSiembra.php?clave_unica=";
                            i.setData(Uri.parse(URLPDF + detailsPDF.getClave_unica()));
                            break;
                        case Utilidades.TIPO_DOCUMENTO_CAPACITACION_SIEMBRA:
                            URLPDF = Utilidades.URL_SERVER_API + "/docs/pdf/checklistCapacitaSiembra.php?clave_unica=";
                            i.setData(Uri.parse(URLPDF + detailsPDF.getClave_unica()));
                            break;

                        case Utilidades.TIPO_DOCUMENTO_CAPACITACION_COSECHA:
                            URLPDF = Utilidades.URL_SERVER_API + "/docs/pdf/checklistCapacitaCosecha.php?clave_unica=";
                            i.setData(Uri.parse(URLPDF + detailsPDF.getClave_unica()));
                            break;

                        case Utilidades.TIPO_DOCUMENTO_CHECKLIST_DEVOLUCION_SEMILLA:
                            URLPDF = Utilidades.URL_SERVER_API + "/docs/pdf/checklistDevolucionSemillas.php?clave_unica=";
                            i.setData(Uri.parse(URLPDF + detailsPDF.getClave_unica()));
                            break;

                        case Utilidades.TIPO_DOCUMENTO_CHECKLIST_COSECHA:
                            URLPDF = Utilidades.URL_SERVER_API + "/docs/pdf/checklistCosecha.php?clave_unica=";
                            i.setData(Uri.parse(URLPDF + detailsPDF.getClave_unica()));
                            break;

                        case Utilidades.TIPO_DOCUMENTO_CHECKLIST_LIMPIEZA_CAMIONES:
                            URLPDF = Utilidades.URL_SERVER_API + "/docs/pdf/checklistLimpiezaCamiones.php?clave_unica=";
                            i.setData(Uri.parse(URLPDF + detailsPDF.getClave_unica()));
                            break;

                    }
                    startActivity(i);

                },
                (checkListEditar, detailsEditar) -> {

                    switch (checkListEditar.getTipoCheckList()) {
                        case Utilidades.TIPO_DOCUMENTO_CAPACITACION_SIEMBRA:
                            ExecutorService executorService2 = Executors.newSingleThreadExecutor();
                            executorService2.submit(()
                                    -> MainActivity.myAppDB.DaoFirmas()
                                    .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CAPACITACION_SIEMBRA));
                            executorService2.shutdown();

                            ExecutorService exec2 = Executors.newSingleThreadExecutor();

                            Future<CheckListCapacitacionSiembra> ck2 = exec2.submit(()
                                    -> MainActivity.myAppDB.DaoCheckListCapSiembra()
                                    .getClCapSiembraByAc(detailsEditar.getId(), Utilidades.TIPO_DOCUMENTO_CAPACITACION_SIEMBRA));

                            try {
                                CheckListCapacitacionSiembra cls = ck2.get();
                                FragmentCheckListCapacitacionSiembra fs = FragmentCheckListCapacitacionSiembra.newInstance(cls);
                                activity.cambiarFragment(
                                        fs,
                                        Utilidades.FRAGMENT_CHECKLIST_CAPACITACION_SIEMBRA,
                                        R.anim.slide_in_left, R.anim.slide_out_left
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
                                FragmentCheckListSiembra fs = FragmentCheckListSiembra.newInstance(cls);
                                activity.cambiarFragment(
                                        fs,
                                        Utilidades.FRAGMENT_CHECKLIST_SIEMBRA,
                                        R.anim.slide_in_left, R.anim.slide_out_left
                                );


                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;


                        case Utilidades.TIPO_DOCUMENTO_CHECKLIST_DEVOLUCION_SEMILLA:
                            ExecutorService executorServiceDS = Executors.newSingleThreadExecutor();
                            executorServiceDS.submit(()
                                    -> MainActivity.myAppDB.DaoFirmas()
                                    .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_DEVOLUCION_SEMILLA));
                            executorServiceDS.shutdown();

                            ExecutorService execDS = Executors.newSingleThreadExecutor();

                            Future<ChecklistDevolucionSemilla> ckDS = execDS.submit(()
                                    -> MainActivity.myAppDB.DaoCheckListDevolucionSemilla()
                                    .getClDevolucionSemillaById(detailsEditar.getId()));

                            try {
                                ChecklistDevolucionSemilla cls = ckDS.get();
                                FragmentCheckDevolucionSemilla fs = FragmentCheckDevolucionSemilla.newInstance(cls);
                                activity.cambiarFragment(
                                        fs,
                                        Utilidades.FRAGMENT_CHECKLIST_DEVOLUCION_SEMILLA,
                                        R.anim.slide_in_left, R.anim.slide_out_left
                                );


                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                        case Utilidades.TIPO_DOCUMENTO_CAPACITACION_COSECHA:
                            ExecutorService executorService3 = Executors.newSingleThreadExecutor();
                            executorService3.submit(()
                                    -> MainActivity.myAppDB.DaoFirmas()
                                    .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CAPACITACION_COSECHA));
                            executorService3.shutdown();

                            ExecutorService exec3 = Executors.newSingleThreadExecutor();

                            Future<CheckListCapacitacionSiembra> ck3 = exec3.submit(()
                                    -> MainActivity.myAppDB.DaoCheckListCapSiembra()
                                    .getClCapSiembraByAc(detailsEditar.getId(), Utilidades.TIPO_DOCUMENTO_CAPACITACION_COSECHA));

                            try {
                                CheckListCapacitacionSiembra cls = ck3.get();
                                FragmentCheckListCapacitacionCosecha fs = FragmentCheckListCapacitacionCosecha.newInstance(cls);
                                activity.cambiarFragment(
                                        fs,
                                        Utilidades.FRAGMENT_CHECKLIST_CAPACITACION_COSECHA,
                                        R.anim.slide_in_left, R.anim.slide_out_left
                                );


                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;
                        case Utilidades.TIPO_DOCUMENTO_CHECKLIST_COSECHA:
                            ExecutorService executorCosecha = Executors.newSingleThreadExecutor();
                            executorCosecha.submit(()
                                    -> MainActivity.myAppDB.DaoFirmas()
                                    .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_COSECHA));
                            executorCosecha.shutdown();

                            ExecutorService execCosecha = Executors.newSingleThreadExecutor();

                            Future<CheckListCosecha> ckC = execCosecha.submit(()
                                    -> MainActivity.myAppDB.DaoCheckListCosecha()
                                    .getClCosechaById(detailsEditar.getId()));

                            try {
                                CheckListCosecha cls = ckC.get();
                                FragmentCheckListCosecha fs = FragmentCheckListCosecha.newInstance(cls);
                                activity.cambiarFragment(
                                        fs,
                                        Utilidades.FRAGMENT_CHECKLIST_COSECHA,
                                        R.anim.slide_in_left, R.anim.slide_out_left
                                );
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;

                        case Utilidades.TIPO_DOCUMENTO_CHECKLIST_LIMPIEZA_CAMIONES:
                            ExecutorService executorLimpiezaCamiones = Executors.newSingleThreadExecutor();
                            executorLimpiezaCamiones.submit(()
                                    -> MainActivity.myAppDB.DaoFirmas()
                                    .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_LIMPIEZA_CAMIONES));
                            executorLimpiezaCamiones.shutdown();

                            ExecutorService execLimpiezaCamiones = Executors.newSingleThreadExecutor();

                            Future<CheckListLimpiezaCamiones> ckLimpiezaCamiones = execLimpiezaCamiones.submit(()
                                    -> MainActivity.myAppDB.DaoCheckListLimpiezaCamiones()
                                    .getClLimpiezaCamionesByAc(detailsEditar.getId()));

                            try {
                                CheckListLimpiezaCamiones cls = ckLimpiezaCamiones.get();
                                FragmentChecklistLimpiezaCamiones fs = FragmentChecklistLimpiezaCamiones.newInstance(cls);
                                activity.cambiarFragment(
                                        fs,
                                        Utilidades.FRAGMENT_CHECKLIST_LIMPIEZA_CAMIONES,
                                        R.anim.slide_in_left, R.anim.slide_out_left
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


                    switch (checkListSubir.getTipoCheckList()) {

                        case Utilidades.TIPO_DOCUMENTO_CAPACITACION_SIEMBRA:

                            ExecutorService executorCapSiembra = Executors.newSingleThreadExecutor();
                            Future<CheckListCapacitacionSiembra> checkListCapacitacionSiembraFuture
                                    = executorCapSiembra.submit(()
                                    -> MainActivity.myAppDB.DaoCheckListCapSiembra()
                                    .getClCapSiembraByAcAndEstado(detailsSubir.getId(), 0, Utilidades.TIPO_DOCUMENTO_CAPACITACION_SIEMBRA));

                            try {
                                CheckListCapacitacionSiembra capSiembraCab
                                        = checkListCapacitacionSiembraFuture.get();

                                if (capSiembraCab == null) {
                                    executorCapSiembra.shutdown();
                                    Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                                    return;
                                }

                                CheckListRequest chk = new CheckListRequest();

                                List<CheckListCapacitacionSiembraDetalle> detalle =
                                        executorCapSiembra.submit(() -> MainActivity.myAppDB
                                                .DaoCheckListCapSiembra()
                                                .getCapSiembraDetallesByPadre(capSiembraCab.getClave_unica(), Utilidades.TIPO_DOCUMENTO_CAPACITACION_SIEMBRA)
                                        ).get();

                                CheckListCapCompleto completo = new CheckListCapCompleto();

                                completo.setCabecera(capSiembraCab);
                                completo.setDetalles(detalle);

                                List<CheckListCapCompleto> chkList = new ArrayList<>();
                                chkList.add(completo);

                                chk.setCheckListCapCompletos(chkList);
                                prepararSubir(chk);


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

                                if (checkListSiembras == null) {
                                    executorService.shutdown();
                                    Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                                    return;
                                }

                                CheckListRequest chk = new CheckListRequest();

                                List<CheckListSiembra> chkList = new ArrayList<>();
                                chkList.add(checkListSiembras);

                                chk.setCheckListSiembras(chkList);
                                prepararSubir(chk);


                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                                executorService.shutdown();
                            }

                            break;

                        case Utilidades.TIPO_DOCUMENTO_CHECKLIST_DEVOLUCION_SEMILLA:

                            ExecutorService executorServiceDS = Executors.newSingleThreadExecutor();
                            Future<ChecklistDevolucionSemilla> chkFDS = executorServiceDS.submit(()
                                    -> MainActivity.myAppDB.DaoCheckListDevolucionSemilla()
                                    .getClDevolucionSemillaById(detailsSubir.getId(), 0));

                            try {
                                ChecklistDevolucionSemilla checkListDS = chkFDS.get();

                                if (checkListDS == null) {
                                    executorServiceDS.shutdown();
                                    Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                                    return;
                                }

                                CheckListRequest chk = new CheckListRequest();

                                List<ChecklistDevolucionSemilla> chkList = new ArrayList<>();
                                chkList.add(checkListDS);

                                chk.setCheckListDevolucionSemilla(chkList);
                                prepararSubir(chk);


                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                                executorServiceDS.shutdown();
                            }

                            break;

                        case Utilidades.TIPO_DOCUMENTO_CAPACITACION_COSECHA:

                            ExecutorService executorCapCosecha = Executors.newSingleThreadExecutor();
                            Future<CheckListCapacitacionSiembra> checkListCapacitacionCosechaFuture
                                    = executorCapCosecha.submit(()
                                    -> MainActivity.myAppDB.DaoCheckListCapSiembra()
                                    .getClCapSiembraByAcAndEstado(detailsSubir.getId(), 0, Utilidades.TIPO_DOCUMENTO_CAPACITACION_COSECHA));

                            try {
                                CheckListCapacitacionSiembra capSiembraCab
                                        = checkListCapacitacionCosechaFuture.get();

                                if (capSiembraCab == null) {
                                    executorCapCosecha.shutdown();
                                    Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                                    return;
                                }

                                CheckListRequest chk = new CheckListRequest();

                                List<CheckListCapacitacionSiembraDetalle> detalle =
                                        executorCapCosecha.submit(() -> MainActivity.myAppDB
                                                .DaoCheckListCapSiembra()
                                                .getCapSiembraDetallesByPadre(capSiembraCab.getClave_unica(), Utilidades.TIPO_DOCUMENTO_CAPACITACION_COSECHA)
                                        ).get();

                                CheckListCapCompleto completo = new CheckListCapCompleto();

                                completo.setCabecera(capSiembraCab);
                                completo.setDetalles(detalle);

                                List<CheckListCapCompleto> chkList = new ArrayList<>();
                                chkList.add(completo);

                                chk.setCheckListCapCompletos(chkList);
                                prepararSubir(chk);


                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                                executorCapCosecha.shutdown();
                            }

                            break;

                        case Utilidades.TIPO_DOCUMENTO_CHECKLIST_COSECHA:

                            ExecutorService executorServiceC = Executors.newSingleThreadExecutor();
                            Future<CheckListCosecha> chkFC = executorServiceC.submit(()
                                    -> MainActivity.myAppDB.DaoCheckListCosecha()
                                    .getClCosechaById(detailsSubir.getId(), 0));

                            try {
                                CheckListCosecha checkLisCosecha = chkFC.get();

                                if (checkLisCosecha == null) {
                                    executorServiceC.shutdown();
                                    Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                                    return;
                                }

                                CheckListRequest chk = new CheckListRequest();

                                List<CheckListCosecha> chkList = new ArrayList<>();
                                chkList.add(checkLisCosecha);

                                chk.setCheckListCosechas(chkList);
                                prepararSubir(chk);


                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                                executorServiceC.shutdown();
                            }

                            break;

                        case Utilidades.TIPO_DOCUMENTO_CHECKLIST_LIMPIEZA_CAMIONES:

                            ExecutorService executorLimpiezaCamiones = Executors.newSingleThreadExecutor();
                            Future<CheckListLimpiezaCamiones> checkListLimpiezaCamionesFuture
                                    = executorLimpiezaCamiones.submit(()
                                    -> MainActivity.myAppDB.DaoCheckListLimpiezaCamiones()
                                    .getClLimpiezaCamionesByAcAndEstado(detailsSubir.getId(), 0));

                            try {
                                CheckListLimpiezaCamiones capSiembraCab
                                        = checkListLimpiezaCamionesFuture.get();

                                if (capSiembraCab == null) {
                                    executorLimpiezaCamiones.shutdown();
                                    Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                                    return;
                                }

                                CheckListRequest chk = new CheckListRequest();

                                List<ChecklistLimpiezaCamionesDetalle> detalle =
                                        executorLimpiezaCamiones.submit(() -> MainActivity.myAppDB
                                                .DaoCheckListLimpiezaCamiones()
                                                .getLimpiezaCamionesDetallesByPadre(capSiembraCab.getClave_unica())
                                        ).get();

                                CheckListLimpiezaCamionesCompleto completo = new CheckListLimpiezaCamionesCompleto();

                                completo.setCabecera(capSiembraCab);
                                completo.setDetalles(detalle);

                                List<CheckListLimpiezaCamionesCompleto> chkList = new ArrayList<>();
                                chkList.add(completo);

                                chk.setCheckListLimpiezaCamionesCompletos(chkList);
                                prepararSubir(chk);


                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                                executorLimpiezaCamiones.shutdown();
                            }

                            break;

                        default:
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
}
