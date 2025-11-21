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
import cl.smapdev.curimapu.clases.relaciones.CheckListRecepcionPlantineraCompleto;
import cl.smapdev.curimapu.clases.relaciones.CheckListRequest;
import cl.smapdev.curimapu.clases.relaciones.CheckListRevisionFrutosCompleto;
import cl.smapdev.curimapu.clases.relaciones.CheckListRoguingCompleto;
import cl.smapdev.curimapu.clases.tablas.CheckListAplicacionHormonas;
import cl.smapdev.curimapu.clases.tablas.CheckListDetails;
import cl.smapdev.curimapu.clases.tablas.CheckListGuiaInterna;
import cl.smapdev.curimapu.clases.tablas.CheckListRecepcionPlantinera;
import cl.smapdev.curimapu.clases.tablas.CheckListRevisionFrutos;
import cl.smapdev.curimapu.clases.tablas.CheckListRevisionFrutosDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListRevisionFrutosFotos;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguing;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingDetalleFechas;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingFotoCabecera;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingFotoDetalle;
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

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

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

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.menu_sube_fechas, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_upload_files) {
                    ProgressDialog pd = ProgressDialog.show(
                            activity,
                            null,
                            "Preparando datos para subir...",
                            true,
                            false);
                    ExecutorService io = Executors.newSingleThreadExecutor();
                    io.execute(() -> {

                        CheckListRequest chkS = new CheckListRequest();

                        List<CheckListSiembra> chk = MainActivity.myAppDB.DaoClSiembra()
                                .getClSiembraToSync();

                        List<CheckListRecepcionPlantinera> RP =
                                MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().getClRPToSync();


                        List<CheckListAplicacionHormonas> chkA = MainActivity.myAppDB.DaoCLAplicacionHormonas().getClApHormonasToSync();
                        List<CheckListRoguing> chkR = MainActivity.myAppDB.DaoCLRoguing().getClroguingToSync();
                        List<CheckListGuiaInterna> chkG = MainActivity.myAppDB.DaoCLGuiaInterna().getClGuiaInternaToSync();
                        List<CheckListRevisionFrutos> chRF = MainActivity.myAppDB.DaoCheckListRevisionFrutos().getClrevisionFrutosToSync();

                        if (chk.isEmpty() && chkA.isEmpty() && chkR.isEmpty() && chkG.isEmpty() && chRF.isEmpty() && RP.isEmpty()) {
                            activity.runOnUiThread(() -> {
                                Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                                pd.dismiss();
                            });
                            return;
                        }

                        if (!RP.isEmpty()) {
                            List<CheckListRecepcionPlantineraCompleto> lista = new ArrayList<>();
                            for (CheckListRecepcionPlantinera cl : RP) {
                                CheckListRecepcionPlantineraCompleto completo = new CheckListRecepcionPlantineraCompleto();

                                completo.setClCabecera(cl);
                                completo.setClDetalle(MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().obtenerRPDetallePorClaveCabeceraToSynk(cl.getClave_unica()));
                                completo.setClDetalleFoto(MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().obtenerRPDetalleFotoPorClaveCabeceraToSynk(cl.getClave_unica()));
                                lista.add(completo);
                            }

                            chkS.setCheckListRecepcionPlantineraCompletos(lista);

                        }

                        if (!chkA.isEmpty()) {
                            chkS.setCheckListAplicacionHormonas(chkA);
                        }

                        if (!chk.isEmpty()) {
                            chkS.setCheckListSiembras(chk);
                        }

                        if (!chkG.isEmpty()) {
                            chkS.setCheckListGuiaInternas(chkG);
                        }

                        if (!chkR.isEmpty()) {
                            List<CheckListRoguingCompleto> clCompletoList = new ArrayList<>();
                            for (CheckListRoguing clr : chkR) {
                                CheckListRoguingCompleto clCompleto = new CheckListRoguingCompleto();

                                List<CheckListRoguingDetalle> clrd = MainActivity.myAppDB.DaoCLRoguing().obtenerDetalleRoguingPorClaveUnicaPadreToSynk(clr.getClave_unica());
                                List<CheckListRoguingFotoCabecera> clRc = MainActivity.myAppDB.DaoCLRoguing().obtenerDetalleRoguingFotoCabPorClaveUnicaPadreToSynk(clr.getClave_unica());
                                List<CheckListRoguingFotoDetalle> clRd = MainActivity.myAppDB.DaoCLRoguing().obtenerDetalleRoguingFotoDetPorClaveUnicaPadreToSynk(clr.getClave_unica());
                                List<CheckListRoguingDetalleFechas> clRF = MainActivity.myAppDB.DaoCLRoguing().obtenerDetalleFechaRoguingPorClaveUnicaPadreFinalToSynk(clr.getClave_unica());

                                clCompleto.setCheckListRoguing(clr);
                                clCompleto.setCheckListRoguingDetalle(clrd);
                                clCompleto.setCheckListFotoCabecera(clRc);
                                clCompleto.setCheckListFotoDetalle(clRd);
                                clCompleto.setCheckListRoguingDetalleFechas(clRF);

                                clCompletoList.add(clCompleto);
                            }
                            chkS.setCheckListRoguing(clCompletoList);
                        }
                        if (!chRF.isEmpty()) {
                            List<CheckListRevisionFrutosCompleto> chRFCompletoList = new ArrayList<>();

                            for (CheckListRevisionFrutos rf : chRF) {
                                CheckListRevisionFrutosCompleto rfCompleto = new CheckListRevisionFrutosCompleto();
                                List<CheckListRevisionFrutosDetalle> clrd = MainActivity.myAppDB.DaoCheckListRevisionFrutos().obtenerDetallesPorClaveUnicaPadreToSynk(rf.getClave_unica());
                                List<CheckListRevisionFrutosFotos> clff = MainActivity.myAppDB.DaoCheckListRevisionFrutos().obtenerFotosPorClaveUnicaPadreToSynk(rf.getClave_unica());
                                rfCompleto.setCheckListRevisionFrutos(rf);
                                rfCompleto.setCheckListRevisionFrutosDetalle(clrd);
                                rfCompleto.setCheckListRevisionFrutosFotos(clff);
                                chRFCompletoList.add(rfCompleto);
                            }
                            chkS.setCheckListRevisionFrutos(chRFCompletoList);
                        }
                        activity.runOnUiThread(() -> {
                            prepararSubir(chkS);
                            pd.dismiss();
                        });
                    });

                    return true;

                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        
        String subt = (anexoCompleto != null)
                ? " C. virtual Anexo " + anexoCompleto.getAnexoContrato().getAnexo_contrato()
                : getResources().getString(R.string.subtitles_visit);
        Utilidades.setToolbar(activity, view, getResources().getString(R.string.app_name), subt);
    }


    private CheckLists getChecklistRecepcionPlantinera() {
        CheckLists chkRecepcionPlantineras = new CheckLists();
        chkRecepcionPlantineras.setDescCheckList("CHECK LIST RECEPCION PLANTINERA");
        chkRecepcionPlantineras.setIdAnexo(Integer.parseInt(anexoCompleto
                .getAnexoContrato().getId_anexo_contrato()));
        chkRecepcionPlantineras.setExpanded(false);
        chkRecepcionPlantineras.setTipoCheckList(Utilidades.TIPO_DOCUMENTO_CHECKLIST_RECEPCION_PLANTINERA);

        List<CheckListDetails> nested = new ArrayList<>();

        try {

            List<CheckListRecepcionPlantinera> RP = MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().getAllClRPByAc(chkRecepcionPlantineras.getIdAnexo());
            if (!RP.isEmpty()) {
                for (CheckListRecepcionPlantinera pl : RP) {
                    CheckListDetails tmp = new CheckListDetails();
                    tmp.setDescription(pl.getApellido_checklist());
                    tmp.setUploaded((pl.getEstado_sincronizacion() > 0));
                    tmp.setId(pl.getId_cl_recepcion_plantinera());
                    tmp.setIdAnexo(pl.getId_ac_recepcion_plantinera());
                    tmp.setEstado(pl.getEstado_documento());
                    tmp.setClave_unica(pl.getClave_unica());
                    tmp.setTipo_documento(Utilidades.TIPO_DOCUMENTO_CHECKLIST_RECEPCION_PLANTINERA);
                    tmp.setDescEstado((pl.getEstado_documento() <= 0) ? "SIN ESTADO" : (pl.getEstado_documento() > 1) ? "PENDIENTE" : "ACTIVA");
                    nested.add(tmp);
                }
            }
        } finally {
            chkRecepcionPlantineras.setDetails(nested);
        }


        return chkRecepcionPlantineras;
    }

    private CheckLists getCheckListAplicacionHormonas() {

        CheckLists chkAppHormonas = new CheckLists();
        chkAppHormonas.setDescCheckList("CHECK LIST APLICACION HORMONAS");
        chkAppHormonas.setIdAnexo(Integer.parseInt(anexoCompleto
                .getAnexoContrato().getId_anexo_contrato()));
        chkAppHormonas.setExpanded(false);
        chkAppHormonas.setTipoCheckList(Utilidades.TIPO_DOCUMENTO_CHECKLIST_APLICACION_HORMONAS);


        try {
            List<CheckListAplicacionHormonas> clAppHormonas = MainActivity.myAppDB
                    .DaoCLAplicacionHormonas().getAllClApHormonasByAc(chkAppHormonas.getIdAnexo());
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

        } catch (Exception e) {
            chkAppHormonas.setDetails(Collections.emptyList());
        }
        return chkAppHormonas;
    }

    private CheckLists getChecklistRevisionFrutos() {

        CheckLists chkAppHormonas = new CheckLists();
        chkAppHormonas.setDescCheckList("CHECK LIST REVISION FRUTOS");
        chkAppHormonas.setIdAnexo(Integer.parseInt(anexoCompleto
                .getAnexoContrato().getId_anexo_contrato()));
        chkAppHormonas.setExpanded(false);
        chkAppHormonas.setTipoCheckList(Utilidades.TIPO_DOCUMENTO_CHECKLIST_REVISION_FRUTOS);

        try {
            List<CheckListRevisionFrutos> cl = MainActivity.myAppDB
                    .DaoCheckListRevisionFrutos().getAllClrevisionFrutosByAc(chkAppHormonas.getIdAnexo());
            if (!cl.isEmpty()) {
                List<CheckListDetails> nested = new ArrayList<>();
                for (CheckListRevisionFrutos clApHorm : cl) {
                    CheckListDetails tmp = new CheckListDetails();
                    tmp.setDescription(clApHorm.getApellido_checklist());
                    tmp.setUploaded((clApHorm.getEstado_sincronizacion() > 0));
                    tmp.setId(clApHorm.getId_cl_revision_frutos());
                    tmp.setIdAnexo(clApHorm.getId_ac_cl_revision_frutos());
                    tmp.setEstado(clApHorm.getEstado_documento());
                    tmp.setClave_unica(clApHorm.getClave_unica());
                    tmp.setTipo_documento(Utilidades.TIPO_DOCUMENTO_CHECKLIST_REVISION_FRUTOS);
                    tmp.setDescEstado((clApHorm.getEstado_documento() <= 0) ? "SIN ESTADO" : (clApHorm.getEstado_documento() > 1) ? "PENDIENTE" : "ACTIVA");
                    nested.add(tmp);
                }
                chkAppHormonas.setDetails(nested);
            } else {
                chkAppHormonas.setDetails(Collections.emptyList());
            }

        } catch (Exception e) {
            chkAppHormonas.setDetails(Collections.emptyList());
        }
        return chkAppHormonas;
    }

    private CheckLists getCheckListRoguing() {
        CheckLists chk = new CheckLists();
        chk.setDescCheckList("CHECK LIST ROGUING");
        chk.setIdAnexo(Integer.parseInt(anexoCompleto
                .getAnexoContrato().getId_anexo_contrato()));
        chk.setExpanded(false);
        chk.setTipoCheckList(Utilidades.TIPO_DOCUMENTO_CHECKLIST_ROGUING);

        try {

            List<CheckListRoguing> cl = MainActivity.myAppDB
                    .DaoCLRoguing().getAllClroguingByAc(chk.getIdAnexo());
            if (!cl.isEmpty()) {
                List<CheckListDetails> nested = new ArrayList<>();
                for (CheckListRoguing cli : cl) {
                    CheckListDetails tmp = new CheckListDetails();
                    tmp.setDescription(cli.getApellido_checklist());
                    tmp.setUploaded((cli.getEstado_sincronizacion() > 0));
                    tmp.setId(cli.getId_cl_roguing());
                    tmp.setIdAnexo(cli.getId_ac_cl_roguing());
                    tmp.setEstado(cli.getEstado_documento());
                    tmp.setClave_unica(cli.getClave_unica());
                    tmp.setTipo_documento(Utilidades.TIPO_DOCUMENTO_CHECKLIST_ROGUING);
                    tmp.setDescEstado((cli.getEstado_documento() <= 0) ? "SIN ESTADO" : (cli.getEstado_documento() > 1) ? "PENDIENTE" : "ACTIVA");
                    nested.add(tmp);
                }
                chk.setDetails(nested);
                chk.setNewDisabled(true);
            } else {
                chk.setDetails(Collections.emptyList());
            }

        } catch (Exception e) {
            chk.setDetails(Collections.emptyList());
        }

        return chk;
    }

    private CheckLists getCheckListSiembra() {

        CheckLists chkSiembra = new CheckLists();
        chkSiembra.setDescCheckList("CHECK LIST SIEMBRA");
        chkSiembra.setIdAnexo(Integer.parseInt(anexoCompleto
                .getAnexoContrato().getId_anexo_contrato()));
        chkSiembra.setExpanded(false);
        chkSiembra.setTipoCheckList(Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA);

        List<CheckListSiembra> clSiembras = MainActivity.myAppDB
                .DaoClSiembra().getAllClSiembraByAc(chkSiembra.getIdAnexo());
        try {

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

        } catch (Exception e) {
            chkSiembra.setDetails(Collections.emptyList());
        }
        return chkSiembra;
    }

    private CheckLists getCheckListGuiaInterna() {

        CheckLists chkSiembra = new CheckLists();
        chkSiembra.setDescCheckList("CHECK LIST GUIA INTERNA");
        chkSiembra.setIdAnexo(Integer.parseInt(anexoCompleto
                .getAnexoContrato().getId_anexo_contrato()));
        chkSiembra.setExpanded(false);
        chkSiembra.setTipoCheckList(Utilidades.TIPO_DOCUMENTO_CHECKLIST_GUIA_INTERNA);

        try {
            List<CheckListGuiaInterna> cl = MainActivity.myAppDB
                    .DaoCLGuiaInterna().getAllClGuiaInternaByAc(chkSiembra.getIdAnexo());
            if (!cl.isEmpty()) {
                List<CheckListDetails> nested = new ArrayList<>();
                for (CheckListGuiaInterna cli : cl) {
                    CheckListDetails tmp = new CheckListDetails();
                    tmp.setDescription(cli.getApellido_checklist());
                    tmp.setUploaded((cli.getEstado_sincronizacion() > 0));
                    tmp.setId(cli.getId_cl_guia_interna());
                    tmp.setIdAnexo(cli.getId_ac_cl_guia_interna());
                    tmp.setEstado(cli.getEstado_documento());
                    tmp.setClave_unica(cli.getClave_unica());
                    tmp.setTipo_documento(Utilidades.TIPO_DOCUMENTO_CHECKLIST_GUIA_INTERNA);
                    tmp.setDescEstado((cli.getEstado_documento() <= 0) ? "SIN ESTADO" : (cli.getEstado_documento() > 1) ? "PENDIENTE" : "ACTIVA");
                    nested.add(tmp);
                }
                chkSiembra.setDetails(nested);
            } else {
                chkSiembra.setDetails(Collections.emptyList());
            }

        } catch (Exception e) {
            chkSiembra.setDetails(Collections.emptyList());
        }
        return chkSiembra;

    }


    private void cargarLista() {


        LinearLayoutManager lManager = null;
        if (activity != null) {
            lManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        }
        rv_checklist.setHasFixedSize(true);
        rv_checklist.setLayoutManager(lManager);

        List<CheckLists> checkLists = new ArrayList<>();

        executorService.execute(() -> {

            checkLists.add(getCheckListSiembra());
            checkLists.add(getCheckListAplicacionHormonas());
            checkLists.add(getCheckListRoguing());
            checkLists.add(getCheckListGuiaInterna());
            checkLists.add(getChecklistRevisionFrutos());
            checkLists.add(getChecklistRecepcionPlantinera());

            requireActivity().runOnUiThread(() -> {
                adapter = new CheckListAdapter(
                        checkLists,
                        this::nuevoChecklist,
                        this::levantarPDFChecklist,
                        this::editarChecklist,
                        this::sincronizarCheckListIndividual
                );

                rv_checklist.setAdapter(adapter);
            });
        });

    }


    private void editarChecklist(CheckLists checkListEditar, CheckListDetails detailsEditar) {


        switch (checkListEditar.getTipoCheckList()) {

            case Utilidades.TIPO_DOCUMENTO_CHECKLIST_RECEPCION_PLANTINERA:
                executorService.execute(() -> {
                    MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().eliminarFotosSinClaveUnica();
                    MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().eliminarDetalleSinClaveUnica();
                    MainActivity.myAppDB.DaoFirmas()
                            .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_RECEPCION_PLANTINERA);

                    AnexoCompleto anexo = MainActivity.myAppDB.myDao().getAnexoCompletoById(String.valueOf(detailsEditar.getIdAnexo()));

                    CheckListRecepcionPlantinera cl = MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().getclRPgById(detailsEditar.getId());

                    activity.runOnUiThread(() -> {
                        activity.cambiarFragment(
                                FragmentChecklistRecepcionPlantinera.newInstance(anexo, cl),
                                Utilidades.FRAGMENT_CHECKLIST_RECEPCION_PLANTINERA,
                                R.anim.slide_in_left,
                                R.anim.slide_out_left
                        );
                    });

                });

                break;

            case Utilidades.TIPO_DOCUMENTO_CHECKLIST_ROGUING:
                executorService.execute(() -> {
                    MainActivity.myAppDB.DaoCLRoguing().deleteFotosRoguingSinPadre();
                    MainActivity.myAppDB.DaoCLRoguing().deleteRoguingDetalleSinPadre();
                    MainActivity.myAppDB.DaoCLRoguing().deleteFotosRoguingDetalleSinPadre();
                    MainActivity.myAppDB.DaoCLRoguing().deleteDetalleFechaSinPadreFinal();

                    CheckListRoguing cl = MainActivity.myAppDB.DaoCLRoguing().getclroguingById(detailsEditar.getId());

                    activity.runOnUiThread(() -> {
                        activity.cambiarFragment(
                                FragmentChecklistRoguing.newInstance(cl),
                                Utilidades.FRAGMENT_CHECKLIST_ROGUING,
                                R.anim.slide_in_left,
                                R.anim.slide_out_left
                        );
                    });
                });
                break;

            case Utilidades.TIPO_DOCUMENTO_CHECKLIST_REVISION_FRUTOS:

                executorService.execute(() -> {
                    MainActivity.myAppDB.DaoFirmas()
                            .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_REVISION_FRUTOS);
                    CheckListRevisionFrutos cl = MainActivity.myAppDB.DaoCheckListRevisionFrutos().getclrevisionFrutosById(detailsEditar.getId());

                    activity.runOnUiThread(() -> {
                        activity.cambiarFragment(
                                FragmentChecklistRevisionFrutos.newInstance(cl),
                                Utilidades.FRAGMENT_CHECKLIST_REVISION_FRUTOS,
                                R.anim.slide_in_left, R.anim.slide_out_left
                        );
                    });

                });
                break;

            case Utilidades.TIPO_DOCUMENTO_CHECKLIST_GUIA_INTERNA:
                executorService.execute(() -> {
                    MainActivity.myAppDB.DaoFirmas()
                            .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_GUIA_INTERNA);
                    CheckListGuiaInterna cl = MainActivity.myAppDB.DaoCLGuiaInterna().getClGuiaInternaById(detailsEditar.getId());

                    activity.runOnUiThread(() -> {
                        activity.cambiarFragment(
                                FragmentChecklistGuiaInterna.newInstance(cl),
                                Utilidades.FRAGMENT_CHECKLIST_GUIA_INTERNA,
                                R.anim.slide_in_left, R.anim.slide_out_left
                        );
                    });
                });

                break;


            case Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA:

                executorService.execute(() -> {
                    MainActivity.myAppDB.DaoFirmas()
                            .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA);
                    CheckListSiembra cl = MainActivity.myAppDB.DaoClSiembra().getClSiembraById(detailsEditar.getId());

                    activity.runOnUiThread(() -> {
                        activity.cambiarFragment(
                                FragmentChecklistSiembra.newInstance(cl),
                                Utilidades.FRAGMENT_CHECKLIST_SIEMBRA,
                                R.anim.slide_in_left, R.anim.slide_out_left
                        );
                    });
                });

                break;
            case Utilidades.TIPO_DOCUMENTO_CHECKLIST_APLICACION_HORMONAS:

                executorService.execute(() -> {
                    MainActivity.myAppDB.DaoFirmas()
                            .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_APLICACION_HORMONAS);
                    CheckListAplicacionHormonas cl = MainActivity.myAppDB.DaoCLAplicacionHormonas().getAllClApHormonasById(detailsEditar.getId());

                    activity.runOnUiThread(() -> {
                        activity.cambiarFragment(
                                FragmentChecklistAplicacionHormonas.newInstance(cl),
                                Utilidades.FRAGMENT_CHECKLIST_APLICACION_HORMONAS,
                                R.anim.slide_in_left, R.anim.slide_out_left
                        );
                    });
                });

                break;
        }
    }

    private void nuevoChecklist(CheckLists nuevoCheckList) {

        switch (nuevoCheckList.getTipoCheckList()) {
            case Utilidades.TIPO_DOCUMENTO_CHECKLIST_REVISION_FRUTOS:
                executorService.execute(() -> {
                    MainActivity.myAppDB.DaoFirmas()
                            .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_REVISION_FRUTOS);

                    activity.runOnUiThread(() -> {
                        activity.cambiarFragment(
                                new FragmentChecklistRevisionFrutos(),
                                Utilidades.FRAGMENT_CHECKLIST_REVISION_FRUTOS,
                                R.anim.slide_in_left, R.anim.slide_out_left
                        );
                    });
                });
                break;

            case Utilidades.TIPO_DOCUMENTO_CHECKLIST_GUIA_INTERNA:
                executorService.execute(() -> {
                    MainActivity.myAppDB.DaoFirmas()
                            .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_GUIA_INTERNA);

                    activity.runOnUiThread(() -> {
                        activity.cambiarFragment(
                                new FragmentChecklistGuiaInterna(),
                                Utilidades.FRAGMENT_CHECKLIST_GUIA_INTERNA,
                                R.anim.slide_in_left, R.anim.slide_out_left
                        );
                    });
                });
                break;
            case Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA:

                executorService.execute(() -> {
                    MainActivity.myAppDB.DaoFirmas()
                            .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA);
                    activity.runOnUiThread(() -> {
                        activity.cambiarFragment(
                                new FragmentChecklistSiembra(),
                                Utilidades.FRAGMENT_CHECKLIST_SIEMBRA,
                                R.anim.slide_in_left, R.anim.slide_out_left
                        );
                    });
                });

                break;
            case Utilidades.TIPO_DOCUMENTO_CHECKLIST_APLICACION_HORMONAS:
                executorService.execute(() -> {
                    MainActivity.myAppDB.DaoFirmas()
                            .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_APLICACION_HORMONAS);

                    activity.runOnUiThread(() -> {
                        activity.cambiarFragment(
                                new FragmentChecklistAplicacionHormonas(),
                                Utilidades.FRAGMENT_CHECKLIST_APLICACION_HORMONAS,
                                R.anim.slide_in_left, R.anim.slide_out_left
                        );
                    });
                });

                break;

            case Utilidades.TIPO_DOCUMENTO_CHECKLIST_ROGUING:
                executorService.execute(() -> {
                    MainActivity.myAppDB.DaoCLRoguing().deleteFotosRoguingSinPadres();
                    MainActivity.myAppDB.DaoCLRoguing().deleteRoguingDetalleSinPadreFinal();
                    MainActivity.myAppDB.DaoCLRoguing().deleteFotosRoguingDetalleSinPadreFinal();
                    MainActivity.myAppDB.DaoCLRoguing().deleteDetalleFechaSinPadreFinal();

                    activity.runOnUiThread(() -> {
                        activity.cambiarFragment(
                                new FragmentChecklistRoguing(),
                                Utilidades.FRAGMENT_CHECKLIST_ROGUING,
                                R.anim.slide_in_left,
                                R.anim.slide_out_left
                        );
                    });
                });
                break;

            case Utilidades.TIPO_DOCUMENTO_CHECKLIST_RECEPCION_PLANTINERA:

                executorService.execute(() -> {
                    MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().eliminarDetalleSinClaveUnica();
                    MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().eliminarFotosSinClaveUnica();
                    MainActivity.myAppDB.DaoFirmas()
                            .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_RECEPCION_PLANTINERA);

                    AnexoCompleto anexoCompleto = MainActivity.myAppDB.myDao().getAnexoCompletoById(String.valueOf(nuevoCheckList.getIdAnexo()));

                    activity.runOnUiThread(() -> {
                        activity.cambiarFragment(
                                FragmentChecklistRecepcionPlantinera.newInstance(anexoCompleto, null),
                                Utilidades.FRAGMENT_CHECKLIST_RECEPCION_PLANTINERA,
                                R.anim.slide_in_left,
                                R.anim.slide_out_left
                        );
                    });
                });


                break;
        }
    }

    private void levantarPDFChecklist(CheckLists checkListPDF, CheckListDetails detailsPDF) {
        String URLPDF = "";
        Intent i = new Intent(Intent.ACTION_VIEW);

        switch (checkListPDF.getTipoCheckList()) {
            case Utilidades.TIPO_DOCUMENTO_CHECKLIST_GUIA_INTERNA:
                URLPDF = Utilidades.URL_SERVER_API + "/docs/pdf/checklistGuiaInterna.php?clave_unica=";
                i.setData(Uri.parse(URLPDF + detailsPDF.getClave_unica()));
                break;
            case Utilidades.TIPO_DOCUMENTO_CHECKLIST_REVISION_FRUTOS:
                URLPDF = Utilidades.URL_SERVER_API + "/docs/pdf/checklistRevisionFrutos.php?clave_unica=";
                i.setData(Uri.parse(URLPDF + detailsPDF.getClave_unica()));
                break;
            case Utilidades.TIPO_DOCUMENTO_CHECKLIST_APLICACION_HORMONAS:
                URLPDF = Utilidades.URL_SERVER_API + "/docs/pdf/checklistAplicaHormona.php?clave_unica=";
                i.setData(Uri.parse(URLPDF + detailsPDF.getClave_unica()));
                break;
            case Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA:
                URLPDF = Utilidades.URL_SERVER_API + "/docs/pdf/checklistSiembra.php?clave_unica=";
                i.setData(Uri.parse(URLPDF + detailsPDF.getClave_unica()));
                break;
            case Utilidades.TIPO_DOCUMENTO_CHECKLIST_ROGUING:
                URLPDF = Utilidades.URL_SERVER_API + "/docs/pdf/checklistRoguing.php?clave_unica=";
                i.setData(Uri.parse(URLPDF + detailsPDF.getClave_unica()));
                break;
            case Utilidades.TIPO_DOCUMENTO_CHECKLIST_RECEPCION_PLANTINERA:
                URLPDF = Utilidades.URL_SERVER_API + "/docs/pdf/checklistRecepcionPlantines.php?clave_unica=";
                i.setData(Uri.parse(URLPDF + detailsPDF.getClave_unica()));
                break;

        }
        startActivity(i);
    }

    private void sincronizarCheckListIndividual(CheckLists checkListSubir, CheckListDetails detailsSubir) {

        ProgressDialog pd = ProgressDialog.show(
                activity,
                null,
                "Preparando datos para subir...",
                true,
                false);

        switch (checkListSubir.getTipoCheckList()) {

            case Utilidades.TIPO_DOCUMENTO_CHECKLIST_REVISION_FRUTOS:
                executorService.execute(() -> {

                    try {
                        CheckListRevisionFrutos cl =
                                MainActivity.myAppDB.DaoCheckListRevisionFrutos().getClrevisionFrutosByIdAndEstado(detailsSubir.getId(), 0);
                        if (cl == null) {
                            activity.runOnUiThread(() -> {
                                if (pd.isShowing()) pd.dismiss();
                                Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                            });
                            return;
                        }

                        List<CheckListRevisionFrutosCompleto> chRFCompletoList = new ArrayList<>();
                        CheckListRevisionFrutosCompleto rfCompleto = new CheckListRevisionFrutosCompleto();

                        List<CheckListRevisionFrutosDetalle> clrd = MainActivity.myAppDB.DaoCheckListRevisionFrutos().obtenerDetallesPorClaveUnicaPadreToSynk(cl.getClave_unica());
                        List<CheckListRevisionFrutosFotos> clff = MainActivity.myAppDB.DaoCheckListRevisionFrutos().obtenerFotosPorClaveUnicaPadreToSynk(cl.getClave_unica());

                        rfCompleto.setCheckListRevisionFrutos(cl);
                        rfCompleto.setCheckListRevisionFrutosDetalle(clrd);
                        rfCompleto.setCheckListRevisionFrutosFotos(clff);

                        chRFCompletoList.add(rfCompleto);
                        CheckListRequest chk = new CheckListRequest();
                        chk.setCheckListRevisionFrutos(chRFCompletoList);

                        activity.runOnUiThread(() -> {
                            prepararSubir(chk);
                            if (pd.isShowing()) pd.dismiss();
                        });

                    } finally {
                        activity.runOnUiThread(() -> {
                            if (pd.isShowing()) pd.dismiss();
                        });
                    }

                });
                break;

            case Utilidades.TIPO_DOCUMENTO_CHECKLIST_RECEPCION_PLANTINERA:
                executorService.execute(() -> {
                    try {
                        CheckListRequest chk = new CheckListRequest();
                        List<CheckListRecepcionPlantinera> RP =
                                MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().getClRPToSync();
                        if (RP.isEmpty()) {

                            activity.runOnUiThread(() -> {
                                if (pd.isShowing()) pd.dismiss();
                                Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                            });
                            return;
                        }

                        List<CheckListRecepcionPlantineraCompleto> lista = new ArrayList<>();
                        for (CheckListRecepcionPlantinera cl : RP) {
                            CheckListRecepcionPlantineraCompleto completo = new CheckListRecepcionPlantineraCompleto();

                            completo.setClCabecera(cl);
                            completo.setClDetalle(MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().obtenerRPDetallePorClaveCabeceraToSynk(cl.getClave_unica()));
                            completo.setClDetalleFoto(MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().obtenerRPDetalleFotoPorClaveCabeceraToSynk(cl.getClave_unica()));
                            lista.add(completo);
                        }

                        activity.runOnUiThread(() -> {
                            chk.setCheckListRecepcionPlantineraCompletos(lista);
                            prepararSubir(chk);
                            if (pd.isShowing()) pd.dismiss();
                        });
                    } finally {
                        activity.runOnUiThread(() -> {
                            if (pd.isShowing()) pd.dismiss();
                        });
                    }
                });
                break;
            case Utilidades.TIPO_DOCUMENTO_CHECKLIST_ROGUING:

                executorService.execute(() -> {
                    try {
                        CheckListRequest chk = new CheckListRequest();

                        List<CheckListRoguing> chkR =
                                MainActivity.myAppDB.DaoCLRoguing().getClroguingToSync();

                        if (!chkR.isEmpty()) {
                            List<CheckListRoguingCompleto> lista = new ArrayList<>();
                            for (CheckListRoguing clr : chkR) {
                                CheckListRoguingCompleto completo = new CheckListRoguingCompleto();
                                completo.setCheckListRoguing(clr);
                                completo.setCheckListRoguingDetalle(
                                        MainActivity.myAppDB.DaoCLRoguing()
                                                .obtenerDetalleRoguingPorClaveUnicaPadreToSynk(clr.getClave_unica()));
                                completo.setCheckListFotoCabecera(
                                        MainActivity.myAppDB.DaoCLRoguing()
                                                .obtenerDetalleRoguingFotoCabPorClaveUnicaPadreToSynk(clr.getClave_unica()));
                                completo.setCheckListFotoDetalle(
                                        MainActivity.myAppDB.DaoCLRoguing()
                                                .obtenerDetalleRoguingFotoDetPorClaveUnicaPadreToSynk(clr.getClave_unica()));
                                completo.setCheckListRoguingDetalleFechas(
                                        MainActivity.myAppDB.DaoCLRoguing()
                                                .obtenerDetalleFechaRoguingPorClaveUnicaPadreFinalToSynk(clr.getClave_unica()));
                                lista.add(completo);
                            }
                            chk.setCheckListRoguing(lista);
                        }
                        activity.runOnUiThread(() -> {
                            prepararSubir(chk);   // continúas tu flujo normal
                            if (pd.isShowing()) pd.dismiss();    // cierra el diálogo
                        });
                    } finally {
                        activity.runOnUiThread(() -> {
                            if (pd.isShowing()) pd.dismiss();
                        });
                    }
                });
                break;

            case Utilidades.TIPO_DOCUMENTO_CHECKLIST_GUIA_INTERNA:

                executorService.execute(() -> {
                    try {
                        CheckListGuiaInterna cl =
                                MainActivity.myAppDB.DaoCLGuiaInterna().getClGuiaInternaById(detailsSubir.getId(), 0);
                        if (cl == null) {

                            activity.runOnUiThread(() -> {
                                if (pd.isShowing()) pd.dismiss();
                                Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                            });
                            return;
                        }
                        CheckListRequest chk = new CheckListRequest();
                        List<CheckListGuiaInterna> chkList = new ArrayList<>();

                        chkList.add(cl);
                        chk.setCheckListGuiaInternas(chkList);


                        activity.runOnUiThread(() -> {
                            prepararSubir(chk);
                            if (pd.isShowing()) pd.dismiss();
                        });
                    } finally {
                        activity.runOnUiThread(() -> {
                            if (pd.isShowing()) pd.dismiss();
                        });
                    }
                });
                break;

            case Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA:
                executorService.execute(() -> {
                    try {
                        CheckListSiembra cl = MainActivity.myAppDB.DaoClSiembra().getClSiembraById(detailsSubir.getId(), 0);

                        if (cl == null) {
                            activity.runOnUiThread(() -> {
                                if (pd.isShowing()) pd.dismiss();
                                Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                            });
                            return;
                        }

                        CheckListRequest chk = new CheckListRequest();
                        List<CheckListSiembra> chkList = new ArrayList<>();

                        chkList.add(cl);
                        chk.setCheckListSiembras(chkList);
                        activity.runOnUiThread(() -> {
                            prepararSubir(chk);
                            if (pd.isShowing()) pd.dismiss();
                        });
                    } finally {
                        activity.runOnUiThread(() -> {
                            if (pd.isShowing()) pd.dismiss();
                        });
                    }
                });
                break;
            case Utilidades.TIPO_DOCUMENTO_CHECKLIST_APLICACION_HORMONAS:
                executorService.execute(() -> {
                    try {
                        CheckListAplicacionHormonas cl = MainActivity.myAppDB.DaoCLAplicacionHormonas().getAllClApHormonasByIdAndEstado(detailsSubir.getId(), 0);

                        if (cl == null) {
                            activity.runOnUiThread(() -> {
                                if (pd.isShowing()) pd.dismiss();
                                Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                            });
                            return;
                        }

                        CheckListRequest chk = new CheckListRequest();
                        List<CheckListAplicacionHormonas> chkList = new ArrayList<>();

                        chkList.add(cl);
                        chk.setCheckListAplicacionHormonas(chkList);

                        activity.runOnUiThread(() -> {
                            prepararSubir(chk);
                            if (pd.isShowing()) pd.dismiss();
                        });
                    } finally {
                        activity.runOnUiThread(() -> {
                            if (pd.isShowing()) pd.dismiss();
                        });
                    }
                });
                break;
        }
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
