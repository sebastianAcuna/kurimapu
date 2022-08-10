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
import cl.smapdev.curimapu.clases.relaciones.CheckListRequest;
import cl.smapdev.curimapu.clases.relaciones.Respuesta;
import cl.smapdev.curimapu.clases.retrofit.ApiService;
import cl.smapdev.curimapu.clases.retrofit.RetrofitClient;
import cl.smapdev.curimapu.clases.tablas.CheckListSiembra;
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


        if (checkListRequest.getCheckListSiembras().size() > 0){
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

                        IResponse.onResponseData(true, res.getMensajeRespuesta());
                        pd.dismiss();
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
