package cl.smapdev.curimapu.clases.utilidades;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.clases.relaciones.DetalleCampo;
import cl.smapdev.curimapu.clases.relaciones.Respuesta;
import cl.smapdev.curimapu.clases.relaciones.SubidaDatos;
import cl.smapdev.curimapu.clases.retrofit.ApiService;
import cl.smapdev.curimapu.clases.retrofit.RetrofitClient;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Errores;
import cl.smapdev.curimapu.clases.tablas.Fichas;
import cl.smapdev.curimapu.clases.tablas.Fotos;
import cl.smapdev.curimapu.clases.tablas.Visitas;
import cl.smapdev.curimapu.clases.tablas.detalle_visita_prop;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Subida {

    public static  void subida(final Activity activity) {

            final ProgressDialog progressDialog = new ProgressDialog(activity);
            progressDialog.setTitle("Espere un momento...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            List<Visitas> visitas = MainActivity.myAppDB.myDao().getVisitasPorSubir();
            List<detalle_visita_prop> detalles = MainActivity.myAppDB.myDao().getDetallesPorSubir();
            List<Fotos> fotos = MainActivity.myAppDB.myDao().getFotos();
            List<Errores> errores = MainActivity.myAppDB.myDao().getErroresPorSubir();
            List<Fichas> fichas = MainActivity.myAppDB.myDao().getFichasPorSubir();



            List<Fotos> fts = new ArrayList<>();

            if (fotos.size() > 0){
                for (Fotos fs : fotos){
                    fs.setEncrypted_image(Utilidades.imageToString(fs.getRuta()));
                    fts.add(fs);
                }
            }
            Config config = MainActivity.myAppDB.myDao().getConfig();

            int cantidadSuma = 0;

            if (visitas.size() > 0){
                for (Visitas v : visitas){
                    cantidadSuma+= v.getId_visita();
                }
            }
            cantidadSuma+= visitas.size();

            if (detalles.size() > 0){
                for (detalle_visita_prop v : detalles){
                    cantidadSuma+= v.getId_det_vis_prop_detalle();
                }
            }

            cantidadSuma+= detalles.size();

            if (fotos.size() > 0){
                for (Fotos v : fotos){
                    cantidadSuma+=  v.getId_foto();
                }
            }

            cantidadSuma= cantidadSuma + fotos.size();

            if (fichas.size() > 0){
                for (Fichas v : fichas){
                    cantidadSuma+= v.getId_ficha();
                }
            }

            cantidadSuma+= fichas.size();



            SubidaDatos list = new SubidaDatos();

            list.setVisitasList(visitas);
            list.setDetalle_visita_props(detalles);
            list.setFotosList(fts);
            list.setId_dispo(config.getId());
            list.setId_usuario(config.getId_usuario());
            list.setErrores(errores);
            list.setFichas(fichas);
            list.setCantidadSuma(cantidadSuma);



            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<Respuesta> call = apiService.enviarDatos(list);

            call.enqueue(new Callback<Respuesta>() {
                @Override
                public void onResponse(@NonNull Call<Respuesta> call, @NonNull Response<Respuesta> response) {

                    Respuesta problema = response.body();

                    if (problema != null){
                        Errores errores = new Errores();
                        switch (problema.getCodigoRespuesta()){
                            case 0:
                                //salio bien se sigue con el procedimiento
                                progressDialog.dismiss();
                                 segundaRespuesta(activity, problema.getCabeceraRespuesta());
                                break;
                            case 1:
                                //hay un error en sql
                                errores.setCabecera_error(problema.getCabeceraRespuesta());
                                errores.setCodigo_error(problema.getCodigoRespuesta());
                                errores.setEstado_error(0);
                                errores.setMensaje_error(problema.getMensajeRespuesta());
                                MainActivity.myAppDB.myDao().setErrores(errores);
                                progressDialog.dismiss();
                                Toast.makeText(activity, "Problemas al agregar datos en el servidor, vuelva a intentarlo o contacte con un administrador", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                //hay un error mostrar mensaje
                                errores.setCabecera_error(problema.getCabeceraRespuesta());
                                errores.setCodigo_error(problema.getCodigoRespuesta());
                                errores.setEstado_error(0);
                                errores.setMensaje_error(problema.getMensajeRespuesta());
                                MainActivity.myAppDB.myDao().setErrores(errores);
                                progressDialog.dismiss();
                                Toast.makeText(activity, "Problema subiendo los datos, por favor vuelva a intentarlo ", Toast.LENGTH_SHORT).show();
                                break;
                        }


                    }else{
                        progressDialog.dismiss();
                    }
                }
                @Override
                public void onFailure(@NonNull Call<Respuesta> call, @NonNull Throwable t) {
                    Log.e("ERROR SUBIDA", t.getMessage());
                    progressDialog.dismiss();
                }
            });


    }


    private static void segundaRespuesta(final Activity activity, int cab){

        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Espere un momento...");
        progressDialog.setCancelable(false);
        progressDialog.show();

            final Integer[] problema = new Integer[2];

            Config config = MainActivity.myAppDB.myDao().getConfig();

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<Respuesta> call = apiService.comprobacion(config.getId(), config.getId_usuario(), cab);

            call.enqueue(new Callback<Respuesta>() {
                @Override
                public void onResponse(@NonNull Call<Respuesta> call, @NonNull Response<Respuesta> response) {

                    problema[0] = 0;
                    problema[1] = 0;

                    Respuesta re = response.body();
                    Errores errores = new Errores();
                    if (re != null){
                        switch (re.getCodigoRespuesta()){
                            case 0:
                                //salio bien se sigue con el procedimiento

                                List<Fichas> fichas = MainActivity.myAppDB.myDao().getFichasPorSubir();
                                int ficha = MainActivity.myAppDB.myDao().updateFichasSubidas(re.getCabeceraRespuesta());
                                if (ficha != fichas.size()){
                                    problema[0] = 2;
                                    problema[1] = re.getCabeceraRespuesta();
                                }

                                List<Visitas> visitas = MainActivity.myAppDB.myDao().getVisitasPorSubir();
                                int visita = MainActivity.myAppDB.myDao().updateVisitasSubidas(re.getCabeceraRespuesta());
                                if (visita != visitas.size()){
                                    problema[0] = 2;
                                    problema[1] = re.getCabeceraRespuesta();
                                }

                                List<detalle_visita_prop> detalle_visita_props = MainActivity.myAppDB.myDao().getDetallesPorSubir();
                                int detalles = MainActivity.myAppDB.myDao().updateDetalleVisitaSubidas(re.getCabeceraRespuesta());
                                if (detalles != detalle_visita_props.size()){
                                    problema[0] = 2;
                                    problema[1] = re.getCabeceraRespuesta();
                                }

                                List<Fotos> fotosList = MainActivity.myAppDB.myDao().getFotos();
                                int fotos = MainActivity.myAppDB.myDao().updateFotosSubidas(re.getCabeceraRespuesta());
                                if (fotos != fotosList.size()){
                                    problema[0] = 2;
                                    problema[1] = re.getCabeceraRespuesta();
                                }

                                List<Errores> erroresList = MainActivity.myAppDB.myDao().getErroresPorSubir();
                                int err = MainActivity.myAppDB.myDao().updateErroresSubidos(re.getCabeceraRespuesta());
                                if (err != erroresList.size()){
                                    problema[0] = 2;
                                    problema[1] = re.getCabeceraRespuesta();
                                }

                                if (problema[0] == 2){
                                    errores.setCabecera_error(re.getCabeceraRespuesta());
                                    errores.setCodigo_error(re.getCodigoRespuesta());
                                    errores.setEstado_error(0);
                                    errores.setMensaje_error(re.getMensajeRespuesta());
                                    MainActivity.myAppDB.myDao().setErrores(errores);


                                    MainActivity.myAppDB.myDao().updateDetalleVisitaBack(re.getCabeceraRespuesta());
                                    MainActivity.myAppDB.myDao().updateFotosBack(re.getCabeceraRespuesta());
                                    MainActivity.myAppDB.myDao().updateVisitasBack(re.getCabeceraRespuesta());
                                    MainActivity.myAppDB.myDao().updateErroresBack(re.getCabeceraRespuesta());
                                    MainActivity.myAppDB.myDao().updateFichasBack(re.getCabeceraRespuesta());

                                }


                                break;
                            case 1:
                                //hay un error en sql
                                problema[0] = 1;
                                break;
                            case 2:
                                //hay un error mostrar mensaje
                                problema[0] = 4;
                                break;
                        }
                    }else{
                        /* respuesta nula */
                        problema[0] = 3;
                        Toast.makeText(activity, "Respuesta nula del servidor, vuelva a intentarlo", Toast.LENGTH_SHORT).show();
                        //problema[1] = re.getCabeceraRespuesta();
                    }



                    switch (problema[0]) {
                        case 0:
                            /*salio bien se sigue con el procedimiento*/
                            Toast.makeText(activity, "Se subio todo con exito", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            /*hay un error en sql*/
                            Toast.makeText(activity, "algo sucedio, por favor, vuelva a intentarlo", Toast.LENGTH_SHORT).show();
                            break;/* no se modificaron los mismos elementos que se subieron */
                        case 3:
                        case 2:
                        default:
                            Toast.makeText(activity, "algo no anda bien, por favor, vuelva a intentarlo", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
                @Override
                public void onFailure(@NonNull Call<Respuesta> call, @NonNull Throwable t) {
                    problema[0] = 3;
                    Log.e("ERROR SUBIDA", t.getMessage());
                    Toast.makeText(activity, "Respuesta nula del servidor, vuelva a intentarlo", Toast.LENGTH_SHORT).show();
                }
            });

        progressDialog.dismiss();

    }
}
