package cl.smapdev.curimapu.clases.utilidades;

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
import cl.smapdev.curimapu.clases.relaciones.Respuesta;
import cl.smapdev.curimapu.clases.relaciones.SubidaDatos;
import cl.smapdev.curimapu.clases.retrofit.ApiService;
import cl.smapdev.curimapu.clases.retrofit.RetrofitClient;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Errores;
import cl.smapdev.curimapu.clases.tablas.Fotos;
import cl.smapdev.curimapu.clases.tablas.Visitas;
import cl.smapdev.curimapu.clases.tablas.detalle_visita_prop;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Subida {

    public static class subida extends AsyncTask<Void, Integer, Respuesta> {

        ProgressDialog progressDialog;

        private WeakReference<Context> activity;
        private int lugar;

        public subida(Context reference, int lugar) {
            this.activity = new WeakReference<>(reference);
            this.lugar = lugar;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (lugar == 1){
                progressDialog = new ProgressDialog(activity.get());
                progressDialog.setTitle("Espere un momento...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        }

        @Override
        protected Respuesta doInBackground(Void... voids) {
            final Respuesta[] problema = {null};

            List<Visitas> visitas = MainActivity.myAppDB.myDao().getVisitasPorSubir();
            List<detalle_visita_prop> detalles = MainActivity.myAppDB.myDao().getDetallesPorSubir();
            List<Fotos> fotos = MainActivity.myAppDB.myDao().getFotos();
            List<Errores> errores = MainActivity.myAppDB.myDao().getErroresPorSubir();

            List<Fotos> fts = new ArrayList<>();

            if (fotos.size() > 0){
                for (Fotos fs : fotos){
                    fs.setEncrypted_image(Utilidades.imageToString(fs.getRuta()));
                    fts.add(fs);
                }
            }
            Config config = MainActivity.myAppDB.myDao().getConfig();


            SubidaDatos list = new SubidaDatos();

            list.setVisitasList(visitas);
            list.setDetalle_visita_props(detalles);
            list.setFotosList(fts);
            list.setId_dispo(config.getId());
            list.setId_usuario(config.getId_usuario());
            list.setErrores(errores);


            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<Respuesta> call = apiService.enviarDatos(list);

            call.enqueue(new Callback<Respuesta>() {
                @Override
                public void onResponse(@NonNull Call<Respuesta> call, @NonNull Response<Respuesta> response) {
                    problema[0] = response.body();
                }
                @Override
                public void onFailure(@NonNull Call<Respuesta> call, @NonNull Throwable t) {
                    problema[0] = null;
                    Log.e("ERROR SUBIDA", t.getMessage());
                }
            });


            return problema[0];
        }


        @Override
        protected void onPostExecute(Respuesta aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean != null){
                Errores errores = new Errores();
                switch (aBoolean.getCodigoRespuesta()){
                    case 0:
                        //salio bien se sigue con el procedimiento
                        new segundaRespuesta(activity.get(), lugar).execute();
                        break;
                    case 1:
                        //hay un error en sql
                        errores.setCabecera_error(aBoolean.getCabeceraRespuesta());
                        errores.setCodigo_error(aBoolean.getCodigoRespuesta());
                        errores.setEstado_error(0);
                        errores.setMensaje_error(aBoolean.getMensajeRespuesta());
                        MainActivity.myAppDB.myDao().setErrores(errores);

                        Toast.makeText(activity.get(), "Problemas al agregar datos en el servidor, vuelva a intentarlo o contacte con un administrador", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        //hay un error mostrar mensaje
                        errores.setCabecera_error(aBoolean.getCabeceraRespuesta());
                        errores.setCodigo_error(aBoolean.getCodigoRespuesta());
                        errores.setEstado_error(0);
                        errores.setMensaje_error(aBoolean.getMensajeRespuesta());
                        MainActivity.myAppDB.myDao().setErrores(errores);

                        Toast.makeText(activity.get(), "Problema subiendo los datos, por favor vuelva a intentarlo ", Toast.LENGTH_SHORT).show();
                        break;
                }

                //Toast.makeText(activity.get(), "SUBIDO", Toast.LENGTH_SHORT).show();
            }
            if (lugar == 1) {
                progressDialog.dismiss();
            }
        }
    }


    public static class segundaRespuesta extends  AsyncTask<Respuesta, Integer, Integer[]>{

        private ProgressDialog progressDialog;
        private int lugar;
        private WeakReference<Context> activity;

        segundaRespuesta(Context reference, int lugar) {
            this.activity = new WeakReference<>(reference);
            this.lugar = lugar;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (lugar == 1) {
                progressDialog = new ProgressDialog(activity.get());
                progressDialog.setTitle("Espere un momento...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        }

        @Override
        protected Integer[] doInBackground(Respuesta... voids) {

            final Integer[] problema = new Integer[2];

            Config config = MainActivity.myAppDB.myDao().getConfig();

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<Respuesta> call = apiService.comprobacion(config.getId(), config.getId_usuario(), voids[0].getCabeceraRespuesta());

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
                        //problema[1] = re.getCabeceraRespuesta();
                    }
                }
                @Override
                public void onFailure(@NonNull Call<Respuesta> call, @NonNull Throwable t) {
                    problema[0] = 3;
                    Log.e("ERROR SUBIDA", t.getMessage());
                }
            });

            return problema;
        }

        @Override
        protected void onPostExecute(Integer[] aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean != null){
                switch (aBoolean[0]) {
                    case 0:
                        /*salio bien se sigue con el procedimiento*/
                        Toast.makeText(activity.get(), "Se subio todo con exito", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        /*hay un error en sql*/
                        Toast.makeText(activity.get(), "algo sucedio, por favor, vuelva a intentarlo", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        /* no se modificaron los mismos elementos que se subieron */
                        Toast.makeText(activity.get(), "algo no anda bien, por favor, vuelva a intentarlo", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(activity.get(), "Respuesta nula del servidor, vuelva a intentarlo", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            if (lugar == 1) {
                progressDialog.dismiss();
            }
        }
    }
}
