package cl.smapdev.curimapu.clases.modelo;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.clases.relaciones.RecomendacionesRequest;
import cl.smapdev.curimapu.clases.relaciones.Respuesta;
import cl.smapdev.curimapu.clases.retrofit.ApiService;
import cl.smapdev.curimapu.clases.retrofit.RetrofitClient;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Evaluaciones;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecomendacionesSync {

    public interface ResponseData {
        void onResponseData(boolean state, String message);
    }

    private ResponseData IResponse;
    private RecomendacionesRequest recomendacionesRequest;
    private Context context;


    public RecomendacionesSync(RecomendacionesRequest recomendacionesRequest,
                               Context context,
                               ResponseData IResponse ) {
        this.IResponse = IResponse;
        this.recomendacionesRequest = recomendacionesRequest;
        this.context = context;

        subirRecomendaciones();
    }

    private void subirRecomendaciones() {

        ProgressDialog pd = new ProgressDialog(this.context);
        pd.setTitle("Preparando datos para subir...");
        pd.setCancelable(false);
        pd.show();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Config> configFuture = executor.submit(() -> MainActivity.myAppDB.myDao().getConfig());


        if (recomendacionesRequest.getEvaluacionesList().size() > 0){

        }
        try {
            Config config = configFuture.get();
            recomendacionesRequest.setIdDispo(config.getId());
            recomendacionesRequest.setIdUsuario(config.getId_usuario());

            ApiService apiService = RetrofitClient.getClient(config.getServidorSeleccionado()).create(ApiService.class);

            Call<Respuesta> call = apiService.subirRecomendaciones(recomendacionesRequest);

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

                        for (Evaluaciones chk : recomendacionesRequest.getEvaluacionesList()) {

                            chk.setEstado_server(1);

                            try {
                                executor.submit(() -> MainActivity.myAppDB
                                        .DaoEvaluaciones()
                                        .updateEvaluaciones(chk)).get();
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
