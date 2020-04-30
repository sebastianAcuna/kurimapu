package cl.smapdev.curimapu.clases.sincronizacion;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.clases.relaciones.GsonDescargas;
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
import cl.smapdev.curimapu.clases.utilidades.Descargas;
import cl.smapdev.curimapu.clases.utilidades.InternetStateClass;
import cl.smapdev.curimapu.clases.utilidades.Subida;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.clases.utilidades.returnValuesFromAsyntask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceSync extends Service {

    private static int FOREGROUND_ID=1338;
    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Service created!", Toast.LENGTH_LONG).show();

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {

                InternetStateClass mm = new InternetStateClass(context, new returnValuesFromAsyntask() {
                    @Override
                    public void myMethod(boolean result) {
                        if (result){
                            //todo aca debe suceder todo

                            /* SUBIDA */
                            List<Visitas> visitas = MainActivity.myAppDB.myDao().getVisitasPorSubir();
                            List<detalle_visita_prop> detalles = MainActivity.myAppDB.myDao().getDetallesPorSubir();
                            List<Fotos> fotos = MainActivity.myAppDB.myDao().getFotos();
                            List<Errores> errores = MainActivity.myAppDB.myDao().getErroresPorSubir();

                            if (visitas.size() > 0 || detalles.size() > 0 || fotos.size() > 0 || errores.size() > 0){
                                subida();
                            }else{
                                Toast.makeText(context, "Nada por subir, todo sincronizado", Toast.LENGTH_SHORT).show();
                            }

                            /* BAJADA */

                            boolean entroMananero = false;
                            boolean entroNocturno = false;

                            Config cnf = MainActivity.myAppDB.myDao().getConfig();
                            String hora = Utilidades.horaMin();

                            /* MAÑANERO */
                            // si es mayor a las 6:50 de la mañana y menor a las 7:50
                            entroMananero = seDescargo(cnf, "06:50", "07:50");

                            if (!entroMananero){
                                try{
                                    Date horaTime = Utilidades.horaFormat(hora);
                                    Date horaDesde = Utilidades.horaFormat("06:50");
                                    Date horaHasta = Utilidades.horaFormat("07:50");
                                    if (horaTime.getTime() >= horaDesde.getTime()  &&  horaTime.getTime() <= horaHasta.getTime()){
                                        descarga(hora);
                                    }
                                }catch (ParseException e){
                                    Log.e("ERROR TIEMPO", e.getLocalizedMessage());
                                }
                            }

                            /* NOCTURNO */
                            //si es mayor a las 20:00 y menor a las 22:00
                            entroNocturno = seDescargo(cnf, "14:45", "15:30");

                            if (!entroNocturno){
                                try{
                                    Date horaTime = Utilidades.horaFormat(hora);
                                    Date horaDesde = Utilidades.horaFormat("14:60");
                                    Date horaHasta = Utilidades.horaFormat("15:30");
                                    if (horaTime.getTime() >= horaDesde.getTime()  &&  horaTime.getTime() <= horaHasta.getTime()){
                                        descarga(hora);
                                    }
                                }catch (ParseException e){
                                    Log.e("ERROR TIEMPO", e.getLocalizedMessage());
                                }
                            }

                        }else{
                            Toast.makeText(context, "No tienes conexion a internet", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 0);
                mm.execute();
                //Toast.makeText(context, "Service is still running", Toast.LENGTH_LONG).show();
                handler.postDelayed(runnable, 3600000);
            }
        };

        handler.postDelayed(runnable, 3605000);
    }



    public void subida(){
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

                Errores mistakes = new Errores();

                Respuesta rsp = response.body();
                if (rsp != null){
                    switch (rsp.getCodigoRespuesta()){
                        case 0:
                            //salio bien se sigue con el procedimiento
                            segundaRespuesta(rsp.getCabeceraRespuesta());
                            break;
                        case 1:
                            //hay un error en sql
                            mistakes.setCabecera_error(rsp.getCabeceraRespuesta());
                            mistakes.setCodigo_error(rsp.getCodigoRespuesta());
                            mistakes.setEstado_error(0);
                            mistakes.setMensaje_error(rsp.getMensajeRespuesta());
                            MainActivity.myAppDB.myDao().setErrores(mistakes);

                            Toast.makeText(context, "Problemas al agregar datos en el servidor, vuelva a intentarlo o contacte con un administrador", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            //hay un error mostrar mensaje
                            mistakes.setCabecera_error(rsp.getCabeceraRespuesta());
                            mistakes.setCodigo_error(rsp.getCodigoRespuesta());
                            mistakes.setEstado_error(0);
                            mistakes.setMensaje_error(rsp.getMensajeRespuesta());
                            MainActivity.myAppDB.myDao().setErrores(mistakes);

                            Toast.makeText(context, "Problema subiendo los datos, por favor vuelva a intentarlo ", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }


            }
            @Override
            public void onFailure(@NonNull Call<Respuesta> call, @NonNull Throwable t) {

                Errores mistakes = new Errores();
                mistakes.setCabecera_error(0);
                mistakes.setCodigo_error(99);
                mistakes.setEstado_error(0);
                mistakes.setMensaje_error(t.getLocalizedMessage());
                MainActivity.myAppDB.myDao().setErrores(mistakes);
            }
        });
    }

    public void segundaRespuesta(int idCabecera){
        Config config = MainActivity.myAppDB.myDao().getConfig();

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Respuesta> call = apiService.comprobacion(config.getId(), config.getId_usuario(), idCabecera);

        call.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(@NonNull Call<Respuesta> call, @NonNull Response<Respuesta> response) {
                Respuesta re = response.body();
                Errores errores = new Errores();
                boolean problema = false;

                if (re != null) {
                    switch (re.getCodigoRespuesta()) {
                        case 0:
                            //salio bien se sigue con el procedimiento


                            List<Fichas> fichas = MainActivity.myAppDB.myDao().getFichasPorSubir();
                            int ficha = MainActivity.myAppDB.myDao().updateFichasSubidas(re.getCabeceraRespuesta());
                            if (ficha != fichas.size()){
                                problema = true;
                                /*problema[0] = 2;
                                problema[1] = re.getCabeceraRespuesta();*/
                            }

                            List<Visitas> visitas = MainActivity.myAppDB.myDao().getVisitasPorSubir();
                            int visita = MainActivity.myAppDB.myDao().updateVisitasSubidas(re.getCabeceraRespuesta());
                            if (visita != visitas.size()) {
                                problema = true;
                                /*problema[0] = 2;
                                problema[1] = re.getCabeceraRespuesta();*/
                            }

                            List<detalle_visita_prop> detalle_visita_props = MainActivity.myAppDB.myDao().getDetallesPorSubir();
                            int detalles = MainActivity.myAppDB.myDao().updateDetalleVisitaSubidas(re.getCabeceraRespuesta());
                            if (detalles != detalle_visita_props.size()) {
                                problema = true;
                               /* problema[0] = 2;
                                problema[1] = re.getCabeceraRespuesta();*/
                            }

                            List<Fotos> fotosList = MainActivity.myAppDB.myDao().getFotos();
                            int fotos = MainActivity.myAppDB.myDao().updateFotosSubidas(re.getCabeceraRespuesta());
                            if (fotos != fotosList.size()) {
                                problema = true;
                                /*problema[0] = 2;
                                problema[1] = re.getCabeceraRespuesta();*/
                            }

                            List<Errores> erroresList = MainActivity.myAppDB.myDao().getErroresPorSubir();
                            int err = MainActivity.myAppDB.myDao().updateErroresSubidos(re.getCabeceraRespuesta());
                            if (err != erroresList.size()){
                                problema = true;
                                /*problema[0] = 2;
                                problema[1] = re.getCabeceraRespuesta();*/
                            }

                            break;
                        case 1:
                        case 2:
                            //hay un error en sql
                            //hay un error mostrar mensaje
                            problema = true;
                            break;
                    }
                } else {
                    /* respuesta nula */
                    problema = true;
                    //problema[1] = re.getCabeceraRespuesta();
                }

                if (problema) {
                    errores.setCabecera_error(0);
                    errores.setCodigo_error((re != null) ? re.getCodigoRespuesta() : 98);
                    errores.setEstado_error(0);
                    errores.setMensaje_error((re != null) ? re.getMensajeRespuesta() : "RES NULL");
                    MainActivity.myAppDB.myDao().setErrores(errores);

                    if (re != null){
                        MainActivity.myAppDB.myDao().updateDetalleVisitaBack(re.getCabeceraRespuesta());
                        MainActivity.myAppDB.myDao().updateFotosBack(re.getCabeceraRespuesta());
                        MainActivity.myAppDB.myDao().updateVisitasBack(re.getCabeceraRespuesta());
                        MainActivity.myAppDB.myDao().updateErroresBack(re.getCabeceraRespuesta());
                        MainActivity.myAppDB.myDao().updateFichasBack(re.getCabeceraRespuesta());
                    }
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    if (notificationManager != null) {
                        notificationManager.notify("tag", FOREGROUND_ID, buildForegroundNotification( Utilidades.fechaActualConHora() + " Problema subiendo datos, por favor, suba datos manualmente o contacte con administrador", "subiendo datos"));
                    }
                }else{
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    if (notificationManager != null) {
                        notificationManager.notify("tag", FOREGROUND_ID, buildForegroundNotification( Utilidades.fechaActualConHora() + " Datos subidos exitosamente", "subiendo datos"));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Respuesta> call, @NonNull Throwable t) {
                Errores errores = new Errores();
                errores.setCabecera_error(0);
                errores.setCodigo_error(99);
                errores.setEstado_error(0);
                errores.setMensaje_error(t.getLocalizedMessage());
                MainActivity.myAppDB.myDao().setErrores(errores);


                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    notificationManager.notify("tag", FOREGROUND_ID, buildForegroundNotification( Utilidades.fechaActualConHora() + " Problema subiendo datos, por favor, suba datos manualmente o contacte con administrador", "subiendo datos"));
                }
            }
        });
    }


    public void descarga(final String hora){

//        final boolean[] problema = {false};

        Config config = MainActivity.myAppDB.myDao().getConfig();

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<GsonDescargas> call = apiService.descargarDatos(config.getId(), config.getId_usuario());
        call.enqueue(new Callback<GsonDescargas>() {
            @Override
            public void onResponse(@NonNull Call<GsonDescargas> call, @NonNull Response<GsonDescargas> response) {
               /* problema[0] =*/
                if(!Descargas.volqueoDatos(response.body())){
                    Toast.makeText(context, "SE DESCARGO", Toast.LENGTH_SHORT).show();
                        int[] res = GsonDescargas.sizeELements(response.body());
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        if (notificationManager != null) {
                            notificationManager.notify("tag", FOREGROUND_ID, buildForegroundNotification( hora+" : Se han descargado " + res[0] + " datos de " + res[1] + " elementos", "descargando datos"));
                        }
                }
            }
            @Override
            public void onFailure(@NonNull Call<GsonDescargas> call, @NonNull Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    public boolean seDescargo(Config cnf, String horaD, String horaH){
        boolean respon = false;
        if (cnf != null) {
            if (!TextUtils.isEmpty(cnf.getHoraDescarga())){
                try{
                    Date horaTime = Utilidades.horaFormat(cnf.getHoraDescarga());
                    Date horaDesde = Utilidades.horaFormat(horaD);
                    Date horaHasta = Utilidades.horaFormat(horaH);

                    if (horaTime.getTime() >= horaDesde.getTime()  &&  horaTime.getTime() <= horaHasta.getTime()){
                        respon = true;
                    }
                }catch (ParseException e){
                    Log.e("ERROR TIEMPO", e.getLocalizedMessage());
                }
            }
        }

        return respon;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);

    }

    @TargetApi(Build.VERSION_CODES.O)
    private Notification buildForegroundNotification(String filename, String contentText) {

        String channelId;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = createNotificationChannel();
        } else {
            channelId = "";
        }

        NotificationCompat.Builder b = new NotificationCompat.Builder(this,channelId);
        b.setSmallIcon(android.R.drawable.ic_menu_mylocation)
                .setContentTitle(contentText)
                .setContentText(filename)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        return(b.build());
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(){
        String serviceId = "service_notification_id";
        NotificationChannel chan = new NotificationChannel(serviceId, "download data from service notification", NotificationManager.IMPORTANCE_DEFAULT);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        NotificationManager service = (NotificationManager) getSystemService(NotificationManager.class);
        if (service != null) {
            service.createNotificationChannel(chan);
        }

        return serviceId;
    }



}
