package cl.smapdev.curimapu.clases.modelo;

import android.app.ProgressDialog;
import android.content.Context;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.clases.relaciones.RespuestaFecha;
import cl.smapdev.curimapu.clases.relaciones.SubirFechasRetro;
import cl.smapdev.curimapu.clases.relaciones.resFecha;
import cl.smapdev.curimapu.clases.retrofit.ApiService;
import cl.smapdev.curimapu.clases.retrofit.RetrofitClient;
import cl.smapdev.curimapu.clases.tablas.AnexoCorreoFechas;
import cl.smapdev.curimapu.clases.tablas.Config;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnexoCorreoFechaSync {

    public interface ResponseData {
        void onResponseData(boolean state, String message);
    }

    private ResponseData IResponse;
    private List<AnexoCorreoFechas> fechas;
    private Context context;


    public void setResponse(ResponseData IResponse) {
        this.IResponse = IResponse;
    }

    public void setFechas(List<AnexoCorreoFechas> fechas) {
        this.fechas = fechas;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public AnexoCorreoFechaSync (List<AnexoCorreoFechas> fechas, Context context, ResponseData IResponse ) {
        this.setResponse(IResponse);
        this.setContext(context);
        this.setFechas(fechas);

        subirFechas();
    }



    private void subirFechas(){

        ProgressDialog pd = new ProgressDialog(this.context);
        pd.setTitle("Preparando datos para subir...");
        pd.setCancelable(false);
        pd.show();


        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<Config> configFuture = executor.submit(() -> MainActivity.myAppDB.myDao().getConfig());

        try {
            Config config = configFuture.get();

            SubirFechasRetro sfp = new SubirFechasRetro();
            sfp.setId_dispo(config.getId());
            sfp.setId_usuario(config.getId_usuario());
            sfp.setFechas(this.fechas);


            ApiService apiService = RetrofitClient.getClient(config.getServidorSeleccionado()).create(ApiService.class);
            Call<resFecha> call = apiService.enviarFechas(sfp);
            call.enqueue(new Callback<resFecha>() {
                @Override
                public void onResponse(Call<resFecha> call, Response<resFecha> response) {

                    if(response.isSuccessful()){

                        resFecha res = response.body();
                        if(res == null){
                            IResponse.onResponseData(false, "Respuesta nula");
                            pd.dismiss();
                            executor.shutdown();
                            return;
                        }

                        if(res.getCodigoRespuesta() != 0){
                            IResponse.onResponseData(false, res.getMensaje());
                            pd.dismiss();
                            executor.shutdown();
                            return;
                        }

                        if (res.getRespuestaFechas() == null || res.getRespuestaFechas().size() <= 0){

                            IResponse.onResponseData(true, "respuesta vacia");
                            pd.dismiss();
                            executor.shutdown();
                            return;

                        }

                        for (RespuestaFecha fc : res.getRespuestaFechas()){

                            Future<AnexoCorreoFechas> acfFuture = executor.submit(()
                            -> MainActivity.
                                    myAppDB.
                                    DaoAnexosFechas().
                                    getAnexoCorreoFechasByAnexo(Integer.parseInt(fc.getAnexo())));

                            try {
                                AnexoCorreoFechas  acf = acfFuture.get();
                                if(acf != null){
                                    acf.setCorreo_termino_labores_post_cosechas(fc.getCorreo_termino_labores());
                                    acf.setCorreo_termino_cosecha(fc.getCorreo_termino_cosecha());
                                    acf.setCorreo_inicio_despano(fc.getCorreo_inicio_despano());
                                    acf.setCorreo_inicio_cosecha(fc.getCorreo_inicio_cosecha());
                                    acf.setCorreo_inicio_corte_seda(fc.getCorreo_inicio_corte_seda());
                                    acf.setCorreo_cinco_porciento_floracion(fc.getCorreo_cinco_porciento());
                                    acf.setEstado_sincro_corr_fech(1);
                                    executor.submit(() -> MainActivity.myAppDB.DaoAnexosFechas().UpdateFechasAnexos(acf));
                                }
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();

                                IResponse.onResponseData(false, "Fallo update");
                                pd.dismiss();
                                executor.shutdown();
                                return;
                            }
                        }

                        IResponse.onResponseData(true, "correos enviados con exito");
                        pd.dismiss();
                        executor.shutdown();
                    }
                }

                @Override
                public void onFailure(Call<resFecha> call, Throwable t) {
                    String mensaje = "Resupuesta nula del servidor";
                    pd.dismiss();
                    IResponse.onResponseData(false, mensaje);
                    executor.shutdown();
                }
            });
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            executor.shutdown();
        }

    }

}
