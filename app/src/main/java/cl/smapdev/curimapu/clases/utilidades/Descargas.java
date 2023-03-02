package cl.smapdev.curimapu.clases.utilidades;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.clases.relaciones.CheckListCapCompleto;
import cl.smapdev.curimapu.clases.relaciones.CheckListLimpiezaCamionesCompleto;
import cl.smapdev.curimapu.clases.relaciones.GsonDescargas;
import cl.smapdev.curimapu.clases.relaciones.Respuesta;
import cl.smapdev.curimapu.clases.retrofit.ApiService;
import cl.smapdev.curimapu.clases.retrofit.RetrofitClient;
import cl.smapdev.curimapu.clases.tablas.AnexoCorreoFechas;
import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembra;
import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembraDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListCosecha;
import cl.smapdev.curimapu.clases.tablas.CheckListLimpiezaCamiones;
import cl.smapdev.curimapu.clases.tablas.CheckListSiembra;
import cl.smapdev.curimapu.clases.tablas.ChecklistDevolucionSemilla;
import cl.smapdev.curimapu.clases.tablas.ChecklistLimpiezaCamionesDetalle;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Evaluaciones;
import cl.smapdev.curimapu.clases.tablas.Fichas;
import cl.smapdev.curimapu.clases.tablas.FichasNew;
import cl.smapdev.curimapu.clases.tablas.Visitas;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Descargas {

    public static void primeraDescarga(final MainActivity activity, String imei, int id, String version) {

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Espere un momento...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.show();

            Config config = MainActivity.myAppDB.myDao().getConfig();

            ApiService apiService = RetrofitClient.getClient(config.getServidorSeleccionado()).create(ApiService.class);
            Call<GsonDescargas> call = apiService.descargaPrimera(imei, id, version);
            call.enqueue(new Callback<GsonDescargas>() {
                @Override
                public void onResponse(@NonNull Call<GsonDescargas> call, @NonNull Response<GsonDescargas> response) {
                    int problema = 0;
                    GsonDescargas gsonDescargas = response.body();
                    if (gsonDescargas != null) {
                        int id = gsonDescargas.getId_dispo();

                        int codigoRespuesta = 1;
                        if (gsonDescargas.getRespuestas() != null && gsonDescargas.getRespuestas().size() > 0){

                            for(Respuesta rsp : gsonDescargas.getRespuestas()){
                                if (rsp.getCodigoRespuesta() == 5){
                                    codigoRespuesta = 5;
                                    break;
                                }
                            }
                        }

                        if (codigoRespuesta > 1){

                            Utilidades.avisoListo(activity,"ATENCION", "NO TIENES LA ULTIMA VERSION DE LA APLICACION, FAVOR ACTUALIZAR", "ENTIENDO");
                            problema = 1;
                        }else{
                            if (id > 0) {
                                Config config = MainActivity.myAppDB.myDao().getConfig();
                                if (config == null) {
                                    Config config1 = new Config();
                                    config1.setId(id);
                                    config1.setServidorSeleccionado(Utilidades.URL_SERVER_API);
                                    MainActivity.myAppDB.myDao().setConfig(config1);
                                }else{
                                    MainActivity.myAppDB.myDao().updateConfig(id);
                                }
                            }
                            if (gsonDescargas.getUsuarios() != null && gsonDescargas.getUsuarios().size() > 0) {
                                try {
                                    MainActivity.myAppDB.myDao().deleteUsuario();
                                    List<Long> inserts = MainActivity.myAppDB.myDao().setUsuarios(gsonDescargas.getUsuarios());
                                    int c = 1;
                                    for (long l : inserts) {
                                        if (l <= 0) {
                                            problema = 1;
                                        } else {
                                            progressDialog.setProgress(c * 100 / inserts.size());
                                            c++;
                                        }
                                    }
                                } catch (SQLiteException e) {
                                    problema = 1;
                                    Log.e("SQLITE", e.getMessage());
                                }
                            }else{
                                problema = 1;
                            }
                        }
                    }else{
                        problema = 1;
                    }

                    if (problema > 0){
                        Toasty.error(activity, "No se pudo descargar todo", Toast.LENGTH_SHORT, true).show();
                        progressDialog.dismiss();
                    }else{
                        Toasty.success(activity, "Todo descargado con exito", Toast.LENGTH_SHORT, true).show();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GsonDescargas> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    System.out.println(t.getMessage());
                    Toasty.error(activity, "No se pudo descargar todo", Toast.LENGTH_SHORT, true).show();
                    progressDialog.dismiss();
                }
            });

        }


    public static boolean[] volqueoDatos(GsonDescargas gsonDescargas, Activity activity){

        boolean[] problema = {false, false};
        if( gsonDescargas == null) {
            Utilidades.avisoListo(activity, "ERROR SINCRONIZACION", "respuesta nula", "aceptar");
            return problema;
        }


        if(gsonDescargas.getRespuestas() != null && gsonDescargas.getRespuestas().size() > 0){
            for(Respuesta rsp : gsonDescargas.getRespuestas()){
                switch(rsp.getCodigoRespuesta()){
                    case 5:
                        problema[1] = true;
                        break;
                    case 2:
                        Toasty.error(activity, rsp.getMensajeRespuesta(), Toast.LENGTH_LONG, true).show();
                        Utilidades.avisoListo(activity, "ERROR SINCRONIZACION", rsp.getMensajeRespuesta()
                                +" por favor vuelva a intentarlo, si el problema persiste contacte con un administrador", "aceptar");
                        problema[0] = true;
                        break;
                }
            }
        }

        if(problema[1] && problema[0]){
            return problema;
        }

        Toasty.error(activity, "revisando datos", Toast.LENGTH_SHORT, true).show();

        if(gsonDescargas.getArray_fechas_anexos() != null && gsonDescargas.getArray_fechas_anexos().size() > 0){
            try{
                for (AnexoCorreoFechas fch : gsonDescargas.getArray_fechas_anexos()){
                    AnexoCorreoFechas f = MainActivity.myAppDB.DaoAnexosFechas().getAnexoCorreoFechasByAnexo(fch.getId_ac_corr_fech());
                    if(f != null){
                        f.setCorreo_cinco_porciento_floracion(fch.getCorreo_cinco_porciento_floracion());
                        f.setCorreo_inicio_corte_seda(fch.getCorreo_inicio_corte_seda());
                        f.setCorreo_inicio_cosecha(fch.getCorreo_inicio_cosecha());
                        f.setCorreo_inicio_despano(fch.getCorreo_inicio_despano());
                        f.setCorreo_termino_cosecha(fch.getCorreo_termino_cosecha());
                        f.setCorreo_termino_labores_post_cosechas(fch.getCorreo_termino_labores_post_cosechas());
                        f.setCorreo_destruccion_semillero(fch.getCorreo_destruccion_semillero());
                        f.setCorreo_siembra_temprana(fch.getCorreo_siembra_temprana());

                        f.setCinco_porciento_floracion(fch.getCinco_porciento_floracion());
                        f.setInicio_corte_seda(fch.getInicio_corte_seda());
                        f.setInicio_cosecha(fch.getInicio_cosecha());
                        f.setInicio_despano(fch.getInicio_despano());
                        f.setTermino_cosecha(fch.getTermino_cosecha());
                        f.setTermino_labores_post_cosechas(fch.getTermino_labores_post_cosechas());

                        f.setCantidad_has_destruidas(fch.getCantidad_has_destruidas());
                        f.setDestruc_semill_ensayo(fch.getDestruc_semill_ensayo());
                        f.setMotivo_destruccion(fch.getMotivo_destruccion());
                        f.setTipo_graminea(fch.getTipo_graminea());
                        f.setSiem_tempra_grami(fch.getSiem_tempra_grami());
                        f.setId_fieldman(fch.getId_fieldman());
                        f.setHora_destruccion_semillero(fch.getHora_destruccion_semillero());
                        f.setFecha_destruccion_semillero(fch.getFecha_destruccion_semillero());
                        f.setHora_inicio_cosecha(fch.getHora_inicio_cosecha());
                        f.setDetalle_labores(fch.getDetalle_labores());

                        MainActivity.myAppDB.DaoAnexosFechas().UpdateFechasAnexos(f);
                    }else{
                        MainActivity.myAppDB.DaoAnexosFechas().insertFechasAnexos(fch);
                    }
                }

            }catch (SQLiteException e){
                Log.e("SQLITE",e.getMessage());
                Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                problema[0] = true;
            }
        }

        if (gsonDescargas.getPro_cli_matList() != null && gsonDescargas.getPro_cli_matList().size() > 0){
            try {
                MainActivity.myAppDB.myDao().deleteProCliMat();
                List<Long> inserts = MainActivity.myAppDB.myDao().insertInterfaz(gsonDescargas.getPro_cli_matList());
            }catch (SQLiteException e){
                Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                Log.e("SQLITE",e.getMessage());
                problema[0] = true;
            }
        }else{
            MainActivity.myAppDB.myDao().deleteProCliMat();
        }


        if(gsonDescargas.getChecklistDevolucionSemillas() != null && gsonDescargas.getChecklistDevolucionSemillas().size() > 0 ){
            ExecutorService ex = Executors.newSingleThreadExecutor();
            for (ChecklistDevolucionSemilla ck : gsonDescargas.getChecklistDevolucionSemillas()){
                Future<ChecklistDevolucionSemilla> chkF = ex.submit(() -> MainActivity.myAppDB.DaoCheckListDevolucionSemilla().getCLDevolucionSemillaByClaveUnica(ck.getClave_unica()));
                try {
                    ChecklistDevolucionSemilla chk  = chkF.get();
                    //update
                    if(chk != null){
                        ck.setId_cl_devolucion_semilla(chk.getId_cl_devolucion_semilla());
                        ex.submit(() -> MainActivity.myAppDB.DaoCheckListDevolucionSemilla().updateClDevolucionSemilla(ck)).get();
                    }
                    else{
                        //insert
                        ex.submit(() -> MainActivity.myAppDB.DaoCheckListDevolucionSemilla().insertClDevolucionSemilla(ck));
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ex.shutdown();
        }

        if(gsonDescargas.getCheckListSiembras() != null && gsonDescargas.getCheckListSiembras().size() > 0 ){
            ExecutorService ex = Executors.newSingleThreadExecutor();
            for (CheckListSiembra ck : gsonDescargas.getCheckListSiembras()){
                Future<CheckListSiembra> chkF = ex.submit(() -> MainActivity.myAppDB.DaoClSiembra().getCLSiembraByClaveUnica(ck.getClave_unica()));
                try {
                    CheckListSiembra chk  = chkF.get();
                    //update
                    if(chk != null){
                        ck.setId_cl_siembra(chk.getId_cl_siembra());
                        ex.submit(() -> MainActivity.myAppDB.DaoClSiembra().updateClSiembra(ck)).get();
                    }
                    else{
                        //insert
                        ex.submit(() -> MainActivity.myAppDB.DaoClSiembra().insertClSiembra(ck));
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ex.shutdown();
        }

        if(gsonDescargas.getCheckListCosecha() != null && gsonDescargas.getCheckListCosecha().size() > 0 ){
            ExecutorService ex = Executors.newSingleThreadExecutor();
            for (CheckListCosecha ck : gsonDescargas.getCheckListCosecha()){
                Future<CheckListCosecha> chkF = ex.submit(() -> MainActivity.myAppDB.DaoCheckListCosecha().getCLCosechaByClaveUnica(ck.getClave_unica()));
                try {
                    CheckListCosecha chk  = chkF.get();
                    //update
                    if(chk != null){
                        ck.setId_cl_siembra(chk.getId_cl_siembra());
                        ex.submit(() -> MainActivity.myAppDB.DaoCheckListCosecha().updateClCosecha(ck)).get();
                    }
                    else{
                        //insert
                        ex.submit(() -> MainActivity.myAppDB.DaoCheckListCosecha().insertClCosecha(ck));
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ex.shutdown();
        }


        if(gsonDescargas.getCheckListLimpiezaCamionesCompletos() != null && gsonDescargas.getCheckListLimpiezaCamionesCompletos().size() > 0 ){
            ExecutorService ex = Executors.newSingleThreadExecutor();
            for (CheckListLimpiezaCamionesCompleto ck : gsonDescargas.getCheckListLimpiezaCamionesCompletos()){

                Future<CheckListLimpiezaCamiones> chkF = ex.submit(()
                        -> MainActivity.myAppDB
                        .DaoCheckListLimpiezaCamiones()
                        .getClLimpiezaCamionesByClaveUnica(ck.getCabecera().getClave_unica()));
                try {
                    CheckListLimpiezaCamiones chk  = chkF.get();
                    //update
                    if(chk != null){
                        ck.getCabecera().setId_cl_limpieza_camiones(chk.getId_cl_limpieza_camiones());
                        ex.submit(()
                                -> MainActivity.myAppDB.DaoCheckListLimpiezaCamiones()
                                .updateLimpiezaCamiones(ck.getCabecera())
                        ).get();
                    }
                    else{
                        //insert
                        ex.submit(() -> MainActivity.myAppDB.DaoCheckListLimpiezaCamiones()
                                .insertLimpiezaCamiones(ck.getCabecera()));
                    }


                    for (ChecklistLimpiezaCamionesDetalle detalle : ck.getDetalles()) {

                        ChecklistLimpiezaCamionesDetalle chkDF = ex.submit(()
                                -> MainActivity.myAppDB
                                .DaoCheckListLimpiezaCamiones()
                                .getLimpiezaCamionesDetallesByClaveUnica(
                                        detalle.getClave_unica_cl_limpieza_camiones_detalle())).get();


                        if(chkDF != null){
                            detalle.setId_ac_cl_limpieza_camiones_detalle(chkDF.getId_ac_cl_limpieza_camiones_detalle());
                            ex.submit(()
                                    -> MainActivity.myAppDB.DaoCheckListLimpiezaCamiones()
                                    .updateDetalle(detalle)
                            ).get();
                        }
                        else{
                            //insert
                            ex.submit(() -> MainActivity.myAppDB.DaoCheckListLimpiezaCamiones()
                                    .insertLimpiezaCamionesDetalle(detalle)).get();
                        }

                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ex.shutdown();
        }


        if(gsonDescargas.getCheckListCapCompletos() != null && gsonDescargas.getCheckListCapCompletos().size() > 0 ){
            ExecutorService ex = Executors.newSingleThreadExecutor();
            for (CheckListCapCompleto ck : gsonDescargas.getCheckListCapCompletos()){

                Future<CheckListCapacitacionSiembra> chkF = ex.submit(()
                        -> MainActivity.myAppDB
                        .DaoCheckListCapSiembra()
                        .getClCapSiembraByClaveUnica(ck.getCabecera().getClave_unica()));
                try {
                    CheckListCapacitacionSiembra chk  = chkF.get();
                    //update
                    if(chk != null){
                        ck.getCabecera().setId_cl_cap_siembra(chk.getId_cl_cap_siembra());
                        ex.submit(()
                                -> MainActivity.myAppDB.DaoCheckListCapSiembra()
                                .updateCapacitacionSiembra(ck.getCabecera())
                        ).get();
                    }
                    else{
                        //insert
                        ex.submit(() -> MainActivity.myAppDB.DaoCheckListCapSiembra()
                                .insertCapacitacionSiembra(ck.getCabecera()));
                    }


                    for (CheckListCapacitacionSiembraDetalle detalle : ck.getDetalles()) {

                        CheckListCapacitacionSiembraDetalle chkDF = ex.submit(()
                                -> MainActivity.myAppDB
                                .DaoCheckListCapSiembra()
                                .getCapSiembraDetallesByClaveUnica(
                                        detalle.getClave_unica_cl_cap_siembra_detalle())).get();


                        if(chkDF != null){
                            detalle.setId_cl_cap_siembra_detalle(chkDF.getId_cl_cap_siembra_detalle());
                            ex.submit(()
                                    -> MainActivity.myAppDB.DaoCheckListCapSiembra()
                                    .updateDetalle(detalle)
                            ).get();
                        }
                        else{
                            //insert
                            ex.submit(() -> MainActivity.myAppDB.DaoCheckListCapSiembra()
                                    .insertCapacitacionSiembraDetalle(detalle)).get();
                        }

                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ex.shutdown();
        }

                if (gsonDescargas.getTemporadas() != null && gsonDescargas.getTemporadas().size() > 0){
                    try {

                        MainActivity.myAppDB.myDao().deleteTemporadas();
                        List<Long> inserts = MainActivity.myAppDB.myDao().insertTemporada(gsonDescargas.getTemporadas());

                    }catch (SQLiteException e) {
                        Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                        Log.e("SQLITE", e.getMessage());
                        problema[0] = true;
                    }
                }else{
                    MainActivity.myAppDB.myDao().deleteTemporadas();
                }




                if (gsonDescargas.getCropRotations() != null && gsonDescargas.getCropRotations().size() > 0){
                    try {

                        MainActivity.myAppDB.myDao().deleteCrops();
                        List<Long> inserts = MainActivity.myAppDB.myDao().insertCrop(gsonDescargas.getCropRotations());

                    }catch (SQLiteException e) {
                        Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                        Log.e("SQLITE", e.getMessage());
                        problema[0] = true;
                    }
                }else{
                    MainActivity.myAppDB.myDao().deleteCrops();
                }

                if (gsonDescargas.getEvaluaciones() != null && gsonDescargas.getEvaluaciones().size() > 0){
                    ExecutorService ex = Executors.newSingleThreadExecutor();
                    for (Evaluaciones ck : gsonDescargas.getEvaluaciones()){

                        Future<Evaluaciones> chkF = ex.submit(() -> MainActivity.myAppDB.DaoEvaluaciones().getEvaluacionesByClaveUnica(ck.getClave_unica_recomendacion()));


                        try {
                            Evaluaciones chk  = chkF.get();

                            //update
                            if(chk != null){
                                ck.setId_ac_recom(chk.getId_ac_recom());
                                ex.submit(() -> MainActivity.myAppDB.DaoEvaluaciones().updateEvaluaciones(ck)).get();
                            }
                            else{
                                //insert
                                ex.submit(() -> MainActivity.myAppDB.DaoEvaluaciones().insertEvaluaciones(ck));
                            }


                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                    ex.shutdown();
                }else{
                    MainActivity.myAppDB.myDao().deleteCrops();
                }


                if (gsonDescargas.getDetalle_visita_props() != null && gsonDescargas.getDetalle_visita_props().size() > 0){
                    try {

                        MainActivity.myAppDB.myDao().deleteDetalle();
                        List<Long> inserts = MainActivity.myAppDB.myDao().insertDetalle(gsonDescargas.getDetalle_visita_props());

                    }catch (SQLiteException e) {
                        Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                        Log.e("SQLITE", e.getMessage());
                        problema[0] = true;
                    }
                }else{
                    MainActivity.myAppDB.myDao().deleteDetalle();
                }


                if (gsonDescargas.getPred_agr_temp() != null && gsonDescargas.getPred_agr_temp().size() > 0){
                    try {

                        MainActivity.myAppDB.myDao().deleteAgriPredTemp();
                        List<Long> inserts = MainActivity.myAppDB.myDao().insertAgriPredTemp(gsonDescargas.getPred_agr_temp());

                    }catch (SQLiteException e) {
                        Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                        Log.e("SQLITE", e.getMessage());
                        problema[0] = true;
                    }
                }else{
                    MainActivity.myAppDB.myDao().deleteAgriPredTemp();
                }

                if (gsonDescargas.getVisitasList() != null && gsonDescargas.getVisitasList().size() > 0){
                    try {

                        MainActivity.myAppDB.myDao().deleteVisitas();
                        List<Long> inserts = MainActivity.myAppDB.myDao().setVisita(gsonDescargas.getVisitasList());
                        for(Visitas ln : gsonDescargas.getVisitasList()){
                            MainActivity.myAppDB.myDao().updateFotos(ln.getId_visita(), ln.getId_visita_local(), ln.getId_dispo());
                        }
                    }catch (SQLiteException e) {
                        Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                        Log.e("SQLITE", e.getMessage());
                        problema[0] = true;
                    }
                }else{
                    MainActivity.myAppDB.myDao().deleteVisitas();
                }

                if (gsonDescargas.getAnexoContratoList() != null && gsonDescargas.getAnexoContratoList().size() > 0){
                    try {

                        MainActivity.myAppDB.myDao().deleteAnexos();
                        List<Long> inserts = MainActivity.myAppDB.myDao().insertAnexo(gsonDescargas.getAnexoContratoList());
                    }catch (SQLiteException e) {
                        Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                        Log.e("SQLITE", e.getMessage());
                        problema[0] = true;
                    }
                }else{
                    MainActivity.myAppDB.myDao().deleteAnexos();
                }




                if (gsonDescargas.getAgricultorList() != null && gsonDescargas.getAgricultorList().size() > 0){
                    try {

                        MainActivity.myAppDB.myDao().deleteAgricultores();
                        List<Long> inserts = MainActivity.myAppDB.myDao().insertAgricultor(gsonDescargas.getAgricultorList());
                    }catch (SQLiteException e) {
                        Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                        Log.e("SQLITE", e.getMessage());
                        problema[0] = true;
                    }
                }else{
                    MainActivity.myAppDB.myDao().deleteAgricultores();
                }


                if (gsonDescargas.getRegionList() != null && gsonDescargas.getRegionList().size() > 0){
                    try {

                        MainActivity.myAppDB.myDao().deleteRegiones();
                        List<Long> inserts = MainActivity.myAppDB.myDao().insertRegiones(gsonDescargas.getRegionList());

                    }catch (SQLiteException e) {
                        Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                        problema[0] = true;
                        Log.e("SQLITE", e.getMessage());
                    }
                }else{
                    MainActivity.myAppDB.myDao().deleteRegiones();
                }



                if (gsonDescargas.getQuotations() != null && gsonDescargas.getQuotations().size() > 0){
                    try {

                        MainActivity.myAppDB.myDao().deleteQuotation();
                        List<Long> inserts = MainActivity.myAppDB.myDao().insertQuotation(gsonDescargas.getQuotations());
                    }catch (SQLiteException e) {
                        Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                        problema[0] = true;
                        Log.e("SQLITE", e.getMessage());
                    }
                }else{
                    MainActivity.myAppDB.myDao().deleteQuotation();
                }


                if (gsonDescargas.getProvinciaList() != null && gsonDescargas.getProvinciaList().size() > 0){
                    try {

                        MainActivity.myAppDB.myDao().deleteProvincia();
                        List<Long> inserts = MainActivity.myAppDB.myDao().insertProvincias(gsonDescargas.getProvinciaList());
                    }catch (SQLiteException e) {
                        Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                        problema[0] = true;
                        Log.e("SQLITE", e.getMessage());
                    }
                }else{
                    MainActivity.myAppDB.myDao().deleteProvincia();
                }



                if (gsonDescargas.getCli_pcms() != null && gsonDescargas.getCli_pcms().size() > 0){
                    try {

                        MainActivity.myAppDB.myDao().deleteCliPCM();
                        List<Long> inserts = MainActivity.myAppDB.myDao().insertPCM(gsonDescargas.getCli_pcms());
                    }catch (SQLiteException e) {
                        Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                        Log.e("SQLITE", e.getMessage());
                        problema[0] = true;
                    }
                }else{
                    MainActivity.myAppDB.myDao().deleteCliPCM();
                }


                if (gsonDescargas.getComunaList() != null && gsonDescargas.getComunaList().size() > 0){
                    try {

                        MainActivity.myAppDB.myDao().deleteComuna();
                        List<Long> inserts = MainActivity.myAppDB.myDao().insertComunas(gsonDescargas.getComunaList());
                    }catch (SQLiteException e) {
                        Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                        problema[0] = true;
                        Log.e("SQLITE", e.getMessage());
                    }
                }else{
                    MainActivity.myAppDB.myDao().deleteComuna();
                }



                if (gsonDescargas.getEspecieList() != null && gsonDescargas.getEspecieList().size() > 0){
                    try {

                        MainActivity.myAppDB.myDao().deleteEspecie();
                        List<Long> inserts = MainActivity.myAppDB.myDao().insertEspecie(gsonDescargas.getEspecieList());
                    }catch (SQLiteException e) {
                        Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                        problema[0] = true;
                        Log.e("SQLITE", e.getMessage());
                    }
                }else{
                    MainActivity.myAppDB.myDao().deleteEspecie();
                }

                if (gsonDescargas.getVariedadList() != null && gsonDescargas.getVariedadList().size() > 0){
                    try {

                        MainActivity.myAppDB.myDao().deleteVariedad();
                        List<Long> inserts = MainActivity.myAppDB.myDao().insertVariedad(gsonDescargas.getVariedadList());
                    }catch (SQLiteException e) {
                        Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                        problema[0] = true;
                        Log.e("SQLITE", e.getMessage());
                    }
                }else{
                    MainActivity.myAppDB.myDao().deleteVariedad();
                }


                if (gsonDescargas.getFichasList() != null && gsonDescargas.getFichasList().size() > 0){
                    try {

                        MainActivity.myAppDB.myDao().deleteFichas();
                        List<Long> inserts = MainActivity.myAppDB.myDao().insertFicha(gsonDescargas.getFichasList());
                        Config config = MainActivity.myAppDB.myDao().getConfig();
                        if (config != null){
                            for(FichasNew ln : gsonDescargas.getFichasList()){
                                MainActivity.myAppDB.myDao().updateFotosFichas(ln.getId_ficha(), ln.getId_ficha_local_ficha(), config.getId());
                            }
                        }

                    }catch (SQLiteException e) {
                        Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                        problema[0] = true;
                        Log.e("SQLITE", e.getMessage());
                    }
                }else{
                    MainActivity.myAppDB.myDao().deleteFichas();
                }


                if (gsonDescargas.getUnidadMedidas() != null && gsonDescargas.getUnidadMedidas().size() > 0){
                    try {

                        MainActivity.myAppDB.myDao().deleteUM();
                        List<Long> inserts = MainActivity.myAppDB.myDao().insertUM(gsonDescargas.getUnidadMedidas());
                    }catch (SQLiteException e) {
                        Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                        problema[0] = true;
                        Log.e("SQLITE", e.getMessage());
                    }
                }else{
                    MainActivity.myAppDB.myDao().deleteUM();
                }


                if (gsonDescargas.getUsuarios() != null && gsonDescargas.getUsuarios().size() > 0){
                    try {

                        MainActivity.myAppDB.myDao().deleteUsuario();
                        List<Long> inserts = MainActivity.myAppDB.myDao().setUsuarios(gsonDescargas.getUsuarios());
                    }catch (SQLiteException e) {
                        Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                        problema[0] = true;
                        Log.e("SQLITE", e.getMessage());
                    }
                }else{
                    MainActivity.myAppDB.myDao().deleteUsuario();
                }

                if (gsonDescargas.getPredios() != null && gsonDescargas.getPredios().size() > 0){
                    try {

                        MainActivity.myAppDB.myDao().deletePredios();
                        List<Long> inserts = MainActivity.myAppDB.myDao().insertPredios(gsonDescargas.getPredios());
                    }catch (SQLiteException e) {
                        Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                        Log.e("SQLITE", e.getMessage());
                        problema[0] = true;
                    }
                }else{
                    MainActivity.myAppDB.myDao().deletePredios();
                }

                if (gsonDescargas.getLotes() != null && gsonDescargas.getLotes().size() > 0){
                    try {

                        MainActivity.myAppDB.myDao().deleteLotes();
                        List<Long> inserts = MainActivity.myAppDB.myDao().insertLotes(gsonDescargas.getLotes());
                    }catch (SQLiteException e) {
                        Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                        problema[0] = true;
                        Log.e("SQLITE", e.getMessage());
                    }
                }else{
                    MainActivity.myAppDB.myDao().deleteLotes();
                }

                if (gsonDescargas.getTipoRiegos() != null && gsonDescargas.getTipoRiegos().size() > 0){
                    try {

                        MainActivity.myAppDB.myDao().deleteTipoRiego();
                        List<Long> inserts = MainActivity.myAppDB.myDao().insertTipoRiego(gsonDescargas.getTipoRiegos());
                    }catch (SQLiteException e) {
                        Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                        problema[0] = true;
                        Log.e("SQLITE", e.getMessage());
                    }
                }else{
                    MainActivity.myAppDB.myDao().deleteTipoRiego();
                }


                if (gsonDescargas.getTipoSuelos() != null && gsonDescargas.getTipoSuelos().size() > 0){
                    try {

                        MainActivity.myAppDB.myDao().deleteTipoSuelo();
                        List<Long> inserts = MainActivity.myAppDB.myDao().insertTipoSuelo(gsonDescargas.getTipoSuelos());
                    }catch (SQLiteException e) {
                        Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                        problema[0] = true;
                        Log.e("SQLITE", e.getMessage());
                    }
                }else{
                    MainActivity.myAppDB.myDao().deleteTipoSuelo();
                }


                if (gsonDescargas.getMaquinarias() != null && gsonDescargas.getMaquinarias().size() > 0){
                    try {

                        MainActivity.myAppDB.myDao().deleteMaquinaria();
                        List<Long> inserts = MainActivity.myAppDB.myDao().insertMaquinara(gsonDescargas.getMaquinarias());
                    }catch (SQLiteException e) {
                        Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                        problema[0] = true;
                        Log.e("SQLITE", e.getMessage());
                    }
                }else{
                    MainActivity.myAppDB.myDao().deleteMaquinaria();
                }

                if (gsonDescargas.getTipoTenenciaMaquinarias() != null && gsonDescargas.getTipoTenenciaMaquinarias().size() > 0){
                    try {

                        MainActivity.myAppDB.myDao().deleteTipoTenenciaMaquinaria();
                        List<Long> inserts = MainActivity.myAppDB.myDao().insertTipoTenenciaMaquinaria(gsonDescargas.getTipoTenenciaMaquinarias());
                    }catch (SQLiteException e) {
                        Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                        problema[0] = true;
                        Log.e("SQLITE", e.getMessage());
                    }
                }else{
                    MainActivity.myAppDB.myDao().deleteTipoTenenciaMaquinaria();
                }



                if (gsonDescargas.getTipoTenenciaTerrenos() != null && gsonDescargas.getTipoTenenciaTerrenos().size() > 0){
                    try {

                        MainActivity.myAppDB.myDao().deleteTipoTenenciaTerreno();
                        List<Long> inserts = MainActivity.myAppDB.myDao().insertTipoTenenciaTerreno(gsonDescargas.getTipoTenenciaTerrenos());
                    }catch (SQLiteException e) {
                        Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                        problema[0] = true;
                        Log.e("SQLITE", e.getMessage());
                    }
                }else{
                    MainActivity.myAppDB.myDao().deleteTipoTenenciaTerreno();
                }

                if (gsonDescargas.getFichaMaquinarias() != null && gsonDescargas.getFichaMaquinarias().size() > 0){
                    try {
                        MainActivity.myAppDB.myDao().deleteFichaMaquinaria();
                        List<Long> inserts = MainActivity.myAppDB.myDao().insertFichaMaquinaria(gsonDescargas.getFichaMaquinarias());
                    }catch (SQLiteException e) {
                        Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                        problema[0] = true;
                        Log.e("SQLITE", e.getMessage());
                    }
                }else{
                    MainActivity.myAppDB.myDao().deleteFichaMaquinaria();
                }

                if (gsonDescargas.getClientes() != null && gsonDescargas.getClientes().size() > 0){
                    try {
                        MainActivity.myAppDB.myDao().deleteClientes();
                        List<Long> inserts = MainActivity.myAppDB.myDao().insertClientes(gsonDescargas.getClientes());
                    }catch (SQLiteException e) {
                        Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                        problema[0] = true;
                        Log.e("SQLITE", e.getMessage());
                    }
                }else{
                    MainActivity.myAppDB.myDao().deleteClientes();
                }


                if (gsonDescargas.getCardViewsResumen() != null && gsonDescargas.getCardViewsResumen().size() > 0){
                    try {
                        MainActivity.myAppDB.myDao().deleteResumenes();
                        List<Long> inserts = MainActivity.myAppDB.myDao().insertResumenes(gsonDescargas.getCardViewsResumen());
                    }catch (SQLiteException e) {
                        Toasty.error(activity, e.getMessage(), Toast.LENGTH_LONG, true).show();
                        problema[0] = true;
                        Log.e("SQLITE", e.getMessage());
                    }
                }else{
                    MainActivity.myAppDB.myDao().deleteResumenes();
                }


                if(gsonDescargas.getAgricultorList().size() > 0 || gsonDescargas.getUsuarios().size()  > 0 || gsonDescargas.getPro_cli_matList().size() > 0 || gsonDescargas.getUnidadMedidas().size() > 0 || gsonDescargas.getFichasList().size() > 0 || gsonDescargas.getVariedadList().size()  > 0|| gsonDescargas.getEspecieList().size() > 0
                        || gsonDescargas.getAnexoContratoList().size() > 0 || gsonDescargas.getComunaList().size() > 0 || gsonDescargas.getProvinciaList().size()  > 0|| gsonDescargas.getRegionList().size() > 0 || gsonDescargas.getVisitasList().size() > 0 || gsonDescargas.getDetalle_visita_props().size() > 0 || gsonDescargas.getCropRotations().size() > 0
                        || gsonDescargas.getTemporadas().size() > 0 ){

                    Config config = MainActivity.myAppDB.myDao().getConfig();

                    if (config != null){
                        config.setHoraDescarga(Utilidades.hora());
                        MainActivity.myAppDB.myDao().updateConfig(config);
                    }
                }


        return problema;
    }
}
