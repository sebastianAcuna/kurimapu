package cl.smapdev.curimapu.clases.modelo;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.clases.relaciones.EstacionFloracionCompleto;
import cl.smapdev.curimapu.clases.relaciones.EstacionFloracionRequest;
import cl.smapdev.curimapu.clases.relaciones.MuestraHumedadRequest;
import cl.smapdev.curimapu.clases.relaciones.Respuesta;
import cl.smapdev.curimapu.clases.retrofit.ApiService;
import cl.smapdev.curimapu.clases.retrofit.RetrofitClient;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.MuestraHumedad;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MuestraHumedadSync {

    public interface ResponseData {
        void onResponseData(boolean state, String message);
    }

    private ResponseData IResponse;
    private MuestraHumedadRequest muestraHumedadRequest;
    private Context context;


    public MuestraHumedadSync(MuestraHumedadRequest muestraHumedadRequest, Context context, ResponseData IResponse ) {
        this.IResponse = IResponse;
        this.muestraHumedadRequest = muestraHumedadRequest;
        this.context = context;

        subirEstaciones();
    }

    private void subirEstaciones() {

        ProgressDialog pd = new ProgressDialog(this.context);
        pd.setTitle("Preparando datos para subir...");
        pd.setCancelable(false);
        pd.show();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Config> configFuture = executor.submit(() -> MainActivity.myAppDB.myDao().getConfig());


        try {
            Config config = configFuture.get();
            muestraHumedadRequest.setIdDispo(config.getId());
            muestraHumedadRequest.setIdUsuario(config.getId_usuario());
            ApiService apiService = RetrofitClient.getClient(config.getServidorSeleccionado()).create(ApiService.class);
            Call<Respuesta> call = apiService.subirMuestras(muestraHumedadRequest);

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


                        if(muestraHumedadRequest.getMuestrasHumedad() != null && muestraHumedadRequest.getMuestrasHumedad().size() > 0){
                            for (MuestraHumedad chk : muestraHumedadRequest.getMuestrasHumedad()) {
                                try {
                                    chk.setEstado_sincronizacion_muestrao(1);

                                    executor.submit(() -> MainActivity.myAppDB
                                            .DaoMuestraHumedad()
                                            .updateMuestraHumedad(chk)).get();
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
