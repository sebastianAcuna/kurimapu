package cl.smapdev.curimapu.clases.modelo;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.clases.relaciones.CheckListCapCompleto;
import cl.smapdev.curimapu.clases.relaciones.CheckListLimpiezaCamionesCompleto;
import cl.smapdev.curimapu.clases.relaciones.CheckListRequest;
import cl.smapdev.curimapu.clases.relaciones.Respuesta;
import cl.smapdev.curimapu.clases.retrofit.ApiService;
import cl.smapdev.curimapu.clases.retrofit.RetrofitClient;
import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembraDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListCosecha;
import cl.smapdev.curimapu.clases.tablas.CheckListSiembra;
import cl.smapdev.curimapu.clases.tablas.ChecklistDevolucionSemilla;
import cl.smapdev.curimapu.clases.tablas.ChecklistLimpiezaCamionesDetalle;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckListSync {

    public interface ResponseData {
        void onResponseData(boolean state, String message);
    }

    private ResponseData IResponse;
    private CheckListRequest checkListRequest;
    private Context context;


    public CheckListSync(CheckListRequest checkListRequest, Context context, ResponseData IResponse ) {
        this.IResponse = IResponse;
        this.checkListRequest = checkListRequest;
        this.context = context;

        subirCheckList();
    }

    private void subirCheckList() {

        ProgressDialog pd = new ProgressDialog(this.context);
        pd.setTitle("Preparando datos para subir...");
        pd.setCancelable(false);
        pd.show();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Config> configFuture = executor.submit(() -> MainActivity.myAppDB.myDao().getConfig());


        if(checkListRequest.getCheckListCapCompletos() != null &&
                checkListRequest.getCheckListCapCompletos().size() > 0){

            List<CheckListCapCompleto> clCapacitacionSiembraList = new ArrayList<>();

            for (CheckListCapCompleto completo : checkListRequest.getCheckListCapCompletos()){

                for (CheckListCapacitacionSiembraDetalle detalle : completo.getDetalles()){
                    if(detalle.getFirma_cl_cap_siembra_detalle() != null &&
                            !detalle.getFirma_cl_cap_siembra_detalle().isEmpty()){
                        String stringed =  Utilidades.imageToString(detalle.getFirma_cl_cap_siembra_detalle());
                        detalle.setStringed_cl_cap_siembra_detalle( stringed.isEmpty() ? "" : stringed );
                    }
                }
                clCapacitacionSiembraList.add(completo);

            }

            checkListRequest.setCheckListCapCompletos(clCapacitacionSiembraList);

        }

        if (checkListRequest.getCheckListSiembras() != null
                && checkListRequest.getCheckListSiembras().size() > 0){
            List<CheckListSiembra>  chkS = new ArrayList<>();
            for (CheckListSiembra chk : checkListRequest.getCheckListSiembras()){

                if(chk.getFirma_responsable_aso_pre_siembra() != null &&
                        !chk.getFirma_responsable_aso_pre_siembra().isEmpty()){

                    String stringed =  Utilidades.imageToString(chk.getFirma_responsable_aso_pre_siembra());
                    chk.setStringed_responsable_aso_pre_siembra( stringed.isEmpty() ? "" : stringed );
                }

                if(chk.getFirma_revision_limpieza_pre_siembra() != null &&
                        !chk.getFirma_revision_limpieza_pre_siembra().isEmpty()){

                    String stringed =  Utilidades.imageToString(chk.getFirma_revision_limpieza_pre_siembra());
                    chk.setStringed_revision_limpieza_pre_siembra( stringed.isEmpty() ? "" : stringed );

                }

                if(chk.getFirma_responsable_aseo_post_siembra() != null &&
                        !chk.getFirma_responsable_aseo_post_siembra().isEmpty()){

                    String stringed =  Utilidades.imageToString(chk.getFirma_responsable_aseo_post_siembra());
                    chk.setStringed_responsable_aseo_post_siembra( stringed.isEmpty() ? "" : stringed );

                }

                if(chk.getFirma_revision_limpieza_post_siembra() != null &&
                        !chk.getFirma_revision_limpieza_post_siembra().isEmpty()){

                    String stringed =  Utilidades.imageToString(chk.getFirma_revision_limpieza_post_siembra());
                    chk.setStringed_revision_limpieza_post_siembra( stringed.isEmpty() ? "" : stringed );

                }

                if(chk.getFirma_responsable_campo() != null &&
                        !chk.getFirma_responsable_campo().isEmpty()){

                    String stringed =  Utilidades.imageToString(chk.getFirma_responsable_campo());
                    chk.setStringed_responsable_campo( stringed.isEmpty() ? "" : stringed );

                }

                if(chk.getFirma_operario_maquina() != null &&
                        !chk.getFirma_operario_maquina().isEmpty()){

                    String stringed =  Utilidades.imageToString(chk.getFirma_operario_maquina());
                    chk.setStringed_operario_maquina( stringed.isEmpty() ? "" : stringed );

                }

                if(chk.getFirma_responsable_campo_termino() != null &&
                        !chk.getFirma_responsable_campo_termino().isEmpty()){

                    String stringed =  Utilidades.imageToString(chk.getFirma_responsable_campo_termino());
                    chk.setStringed_responsable_campo_termino( stringed.isEmpty() ? "" : stringed );

                }

                if(chk.getFirma_operario_maquina_termino() != null &&
                        !chk.getFirma_operario_maquina_termino().isEmpty()){

                    String stringed =  Utilidades.imageToString(chk.getFirma_operario_maquina_termino());
                    chk.setStringed_operario_maquina_termino( stringed.isEmpty() ? "" : stringed );

                }

                chkS.add(chk);
            }

            checkListRequest.setCheckListSiembras(chkS);
        }

        if (checkListRequest.getCheckListCosechas() != null
                && checkListRequest.getCheckListCosechas().size() > 0){
            List<CheckListCosecha>  chkS = new ArrayList<>();
            for (CheckListCosecha chk : checkListRequest.getCheckListCosechas()){

                if(chk.getFirma_responsable_aseo_ingreso() != null &&
                        !chk.getFirma_responsable_aseo_ingreso().isEmpty()){

                    String stringed =  Utilidades.imageToString(chk.getFirma_responsable_aseo_ingreso());
                    chk.setStringed_responsable_aso_ingreso( stringed.isEmpty() ? "" : stringed );
                }

                if(chk.getFirma_responsable_curimapu_ingreso() != null &&
                        !chk.getFirma_responsable_curimapu_ingreso().isEmpty()){

                    String stringed =  Utilidades.imageToString(chk.getFirma_responsable_curimapu_ingreso());
                    chk.setStringed_responsable_curimapu_ingreso( stringed.isEmpty() ? "" : stringed );

                }

                if(chk.getFirma_responsable_aseo_salida() != null &&
                        !chk.getFirma_responsable_aseo_salida().isEmpty()){

                    String stringed =  Utilidades.imageToString(chk.getFirma_responsable_aseo_salida());
                    chk.setStringed_responsable_aso_salida( stringed.isEmpty() ? "" : stringed );

                }

                if(chk.getFirma_responsable_curimapu_salida() != null &&
                        !chk.getFirma_responsable_curimapu_salida().isEmpty()){

                    String stringed =  Utilidades.imageToString(chk.getFirma_responsable_curimapu_salida());
                    chk.setStringed_responsable_curimapu_salida( stringed.isEmpty() ? "" : stringed );
                }
                chkS.add(chk);
            }

            checkListRequest.setCheckListCosechas(chkS);
        }


        if (checkListRequest.getCheckListDevolucionSemilla() != null
                && checkListRequest.getCheckListDevolucionSemilla().size() > 0){
            List<ChecklistDevolucionSemilla>  chkS = new ArrayList<>();
            for (ChecklistDevolucionSemilla chk : checkListRequest.getCheckListDevolucionSemilla()){

                if(chk.getFirma_responsable() != null &&
                        !chk.getFirma_responsable().isEmpty()){

                    String stringed =  Utilidades.imageToString(chk.getFirma_responsable());
                    chk.setStringed_responsable( stringed.isEmpty() ? "" : stringed );
                }

                if(chk.getFirma_revisor_bodega() != null &&
                        !chk.getFirma_revisor_bodega().isEmpty()){

                    String stringed =  Utilidades.imageToString(chk.getFirma_revisor_bodega());
                    chk.setStringed_revisor_bodega( stringed.isEmpty() ? "" : stringed );

                }
                chkS.add(chk);
            }

            checkListRequest.setCheckListDevolucionSemilla(chkS);
        }

        if(checkListRequest.getCheckListLimpiezaCamionesCompletos() != null && checkListRequest.getCheckListLimpiezaCamionesCompletos().size() > 0){
            List<CheckListLimpiezaCamionesCompleto> clLimpiezaCamionesList = new ArrayList<>();

            for (CheckListLimpiezaCamionesCompleto completo : checkListRequest.getCheckListLimpiezaCamionesCompletos()){

                for (ChecklistLimpiezaCamionesDetalle detalle : completo.getDetalles()){
                    if(detalle.getFirma_cl_limpieza_camiones_detalle() != null &&
                            !detalle.getFirma_cl_limpieza_camiones_detalle().isEmpty()){
                        String stringed =  Utilidades.imageToString(detalle.getFirma_cl_limpieza_camiones_detalle());
                        detalle.setStringed_cl_limpieza_camiones_detalle( stringed.isEmpty() ? "" : stringed );
                    }
                }
                clLimpiezaCamionesList.add(completo);

            }

            checkListRequest.setCheckListLimpiezaCamionesCompletos(clLimpiezaCamionesList);
        }

        try {
            Config config = configFuture.get();
            checkListRequest.setIdDispo(config.getId());
            checkListRequest.setIdUsuario(config.getId_usuario());

            ApiService apiService = RetrofitClient.getClient(config.getServidorSeleccionado()).create(ApiService.class);

            Call<Respuesta> call = apiService.subirCheckList(checkListRequest);

            call.enqueue(new Callback<Respuesta>() {
                @Override
                public void onResponse(@NonNull Call<Respuesta> call, @NonNull Response<Respuesta> response) {

                    if (response.isSuccessful()){

                        Respuesta res = response.body();

                        if(res == null){
                            IResponse.onResponseData(false, "Respuesta nula");
                            pd.dismiss();
                            executor.shutdown();
                            return;
                        }

                        if(res.getCodigoRespuesta() != 0){
                            IResponse.onResponseData(false, res.getMensajeRespuesta());
                            pd.dismiss();
                            executor.shutdown();
                            return;
                        }


                        if(checkListRequest.getCheckListCapCompletos() != null
                                && checkListRequest.getCheckListCapCompletos().size() > 0){
                            for (CheckListCapCompleto chk : checkListRequest.getCheckListCapCompletos()) {
                                try {
                                    //cabecera
                                    chk.getCabecera().setEstado_sincronizacion(1);

                                    executor.submit(() -> MainActivity.myAppDB
                                            .DaoCheckListCapSiembra()
                                            .updateCapacitacionSiembra(chk.getCabecera())).get();

                                    for (CheckListCapacitacionSiembraDetalle ck : chk.getDetalles() ){

                                        ck.setEstado_sincronizacion_detalle(1);
                                        executor.submit(() -> MainActivity.myAppDB
                                                .DaoCheckListCapSiembra()
                                                .updateDetalle(ck)).get();
                                    }


                                } catch (ExecutionException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }


                        if(checkListRequest.getCheckListSiembras() != null
                                && checkListRequest.getCheckListSiembras().size() > 0){
                            for (CheckListSiembra chk : checkListRequest.getCheckListSiembras()) {

                                chk.setEstado_sincronizacion(1);

                                try {
                                    executor.submit(() -> MainActivity.myAppDB
                                            .DaoClSiembra()
                                            .updateClSiembra(chk)).get();
                                } catch (ExecutionException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        if(checkListRequest.getCheckListDevolucionSemilla() != null
                                && checkListRequest.getCheckListDevolucionSemilla().size() > 0){
                            for (ChecklistDevolucionSemilla chk : checkListRequest.getCheckListDevolucionSemilla()) {

                                chk.setEstado_sincronizacion(1);

                                try {
                                    executor.submit(() -> MainActivity.myAppDB
                                            .DaoCheckListDevolucionSemilla()
                                            .updateClDevolucionSemilla(chk)).get();
                                } catch (ExecutionException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        if(checkListRequest.getCheckListCosechas() != null
                                && checkListRequest.getCheckListCosechas().size() > 0){
                            for (CheckListCosecha chk : checkListRequest.getCheckListCosechas()) {

                                chk.setEstado_sincronizacion(1);

                                try {
                                    executor.submit(() -> MainActivity.myAppDB
                                            .DaoCheckListCosecha()
                                            .updateClCosecha(chk)).get();
                                } catch (ExecutionException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        if(checkListRequest.getCheckListLimpiezaCamionesCompletos() != null
                                && checkListRequest.getCheckListLimpiezaCamionesCompletos().size() > 0){
                            for (CheckListLimpiezaCamionesCompleto chk : checkListRequest.getCheckListLimpiezaCamionesCompletos()) {
                                try {
                                    //cabecera
                                    chk.getCabecera().setEstado_sincronizacion(1);

                                    executor.submit(() -> MainActivity.myAppDB
                                            .DaoCheckListLimpiezaCamiones()
                                            .updateLimpiezaCamiones(chk.getCabecera())).get();

                                    for (ChecklistLimpiezaCamionesDetalle ck : chk.getDetalles() ){

                                        ck.setEstado_sincronizacion_detalle(1);
                                        executor.submit(() -> MainActivity.myAppDB
                                                .DaoCheckListLimpiezaCamiones()
                                                .updateDetalle(ck)).get();
                                    }


                                } catch (ExecutionException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }


                        IResponse.onResponseData(true, res.getMensajeRespuesta());
                        pd.dismiss();
                        executor.shutdown();

                    }else{
                        pd.dismiss();
                        IResponse.onResponseData(false, response.message());
                        executor.shutdown();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Respuesta> call, @NonNull Throwable t) {
                    pd.dismiss();
                    IResponse.onResponseData(false, t.getMessage());
                    executor.shutdown();
                }
            });


        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            IResponse.onResponseData(false, e.getMessage());
            executor.shutdown();
            pd.dismiss();
        }


    }


}
