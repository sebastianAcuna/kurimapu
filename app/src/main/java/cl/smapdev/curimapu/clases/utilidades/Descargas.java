package cl.smapdev.curimapu.clases.utilidades;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.List;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.clases.relaciones.GsonDescargas;
import cl.smapdev.curimapu.clases.retrofit.ApiService;
import cl.smapdev.curimapu.clases.retrofit.RetrofitClient;
import cl.smapdev.curimapu.clases.tablas.Config;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Descargas {

    public static class descargar extends AsyncTask<Void, Integer, Boolean> {

        private WeakReference<Context> activity;

        public descargar(Context reference) {
            this.activity = new WeakReference<>(reference);
        }

        ProgressDialog progressDialog;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity.get());
            progressDialog.setTitle("Espere un momento...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            final boolean[] problema = {false};

            Config cnf = MainActivity.myAppDB.myDao().getConfig();

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<GsonDescargas> call = apiService.descargarDatos(cnf.getId(), cnf.getId_usuario());
            call.enqueue(new Callback<GsonDescargas>() {
                @Override
                public void onResponse(@NonNull Call<GsonDescargas> call, @NonNull Response<GsonDescargas> response) {
                    problema[0] = volqueoDatos(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<GsonDescargas> call, @NonNull Throwable t) {
                    System.out.println(t.getMessage());
                }
            });



            return problema[0];
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (!aBoolean){
                Toast.makeText(activity.get(), "Todo descargado con exito", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(activity.get(), "No se pudo descargar todo", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }
    }

    public static class primeraDescarga extends AsyncTask<Void, Integer, Boolean> {

        private WeakReference<Context> activity;
        private String imei;
        private int id;

        public primeraDescarga(Context reference, String imei, int id) {
            this.activity = new WeakReference<>(reference);
            this.imei = imei;
            this.id = id;
        }

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity.get());
            progressDialog.setTitle("Espere un momento...");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            final boolean[] problema = {false};


            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<GsonDescargas> call = apiService.descargaPrimera(imei, id);
            call.enqueue(new Callback<GsonDescargas>() {
                @Override
                public void onResponse(@NonNull Call<GsonDescargas> call, @NonNull Response<GsonDescargas> response) {

                    GsonDescargas gsonDescargas = response.body();
                    if (gsonDescargas != null) {
                        int id = gsonDescargas.getId_dispo();

                        if (id > 0) {
                            Config config = MainActivity.myAppDB.myDao().getConfig(id);
                            if (config == null) {
                                MainActivity.myAppDB.myDao().setConfig(new Config(id));
                            }
                        }
                        if (gsonDescargas.getUsuarios() != null && gsonDescargas.getUsuarios().size() > 0) {
                            try {
                                MainActivity.myAppDB.myDao().deleteUsuario();
                                List<Long> inserts = MainActivity.myAppDB.myDao().setUsuarios(gsonDescargas.getUsuarios());
                                int c = 1;
                                for (long l : inserts) {
                                    if (l <= 0) {
                                        problema[0] = true;
                                    } else {
                                        publishProgress(c * 100 / inserts.size());
                                        c++;
                                    }
                                }
                            } catch (SQLiteException e) {
                                Log.e("SQLITE", e.getMessage());
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GsonDescargas> call, @NonNull Throwable t) {
                    System.out.println(t.getMessage());
                }
            });
            return problema[0];
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
                progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (!aBoolean){
                Toast.makeText(activity.get(), "Todo descargado con exito", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(activity.get(), "No se pudo descargar todo", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }
    }

    public static boolean volqueoDatos(GsonDescargas gsonDescargas){

        boolean problema  = false;
//        GsonDescargas gsonDescargas  = response.body();
        if (gsonDescargas != null){
            if (gsonDescargas.getPro_cli_matList() != null && gsonDescargas.getPro_cli_matList().size() > 0){
                try {
                    MainActivity.myAppDB.myDao().deleteProCliMat();
                    List<Long> inserts = MainActivity.myAppDB.myDao().insertInterfaz(gsonDescargas.getPro_cli_matList());
                    for (long l : inserts) {
                        if (l <= 0 && inserts.size()  == gsonDescargas.getPro_cli_matList().size()) {
                            problema = true;
                            break;
                        }
                    }
                }catch (SQLiteException e){
                    Log.e("SQLITE",e.getMessage());
                }
            }

            if (gsonDescargas.getTemporadas() != null && gsonDescargas.getTemporadas().size() > 0){
                try {

                    MainActivity.myAppDB.myDao().deleteTemporadas();
                    List<Long> inserts = MainActivity.myAppDB.myDao().insertTemporada(gsonDescargas.getTemporadas());
                    for (long l : inserts) {
                        if (l <= 0 && inserts.size()  == gsonDescargas.getTemporadas().size()) {
                            problema = true;
                            break;
                        }
                    }
                }catch (SQLiteException e) {
                    Log.e("SQLITE", e.getMessage());
                }
            }

            if (gsonDescargas.getCropRotations() != null && gsonDescargas.getCropRotations().size() > 0){
                try {

                    MainActivity.myAppDB.myDao().deleteCrops();
                    List<Long> inserts = MainActivity.myAppDB.myDao().insertCrop(gsonDescargas.getCropRotations());
                    for (long l : inserts) {
                        if (l <= 0 && inserts.size()  == gsonDescargas.getCropRotations().size()) {
                            problema = true;
                            break;
                        }
                    }
                }catch (SQLiteException e) {
                    Log.e("SQLITE", e.getMessage());
                }
            }

            if (gsonDescargas.getDetalle_visita_props() != null && gsonDescargas.getDetalle_visita_props().size() > 0){
                try {

                    MainActivity.myAppDB.myDao().deleteDetalle();
                    List<Long> inserts = MainActivity.myAppDB.myDao().insertDetalle(gsonDescargas.getDetalle_visita_props());
                    for (long l : inserts) {
                        if (l <= 0 && inserts.size()  == gsonDescargas.getDetalle_visita_props().size()) {
                            problema = true;
                            break;
                        }
                    }
                }catch (SQLiteException e) {
                    Log.e("SQLITE", e.getMessage());
                }
            }

            if (gsonDescargas.getVisitasList() != null && gsonDescargas.getVisitasList().size() > 0){
                try {

                    MainActivity.myAppDB.myDao().deleteVisitas();
                    List<Long> inserts = MainActivity.myAppDB.myDao().setVisita(gsonDescargas.getVisitasList());
                    for (long l : inserts) {
                        if (l <= 0 && inserts.size()  == gsonDescargas.getVisitasList().size()) {
                            problema = true;
                            break;
                        }
                    }
                }catch (SQLiteException e) {
                    Log.e("SQLITE", e.getMessage());
                }
            }

            if (gsonDescargas.getAnexoContratoList() != null && gsonDescargas.getAnexoContratoList().size() > 0){
                try {

                    MainActivity.myAppDB.myDao().deleteAnexos();
                    List<Long> inserts = MainActivity.myAppDB.myDao().insertAnexo(gsonDescargas.getAnexoContratoList());
                    for (long l : inserts) {
                        if (l <= 0 && inserts.size()  == gsonDescargas.getAnexoContratoList().size()) {
                            problema = true;
                            break;
                        }
                    }
                }catch (SQLiteException e) {
                    Log.e("SQLITE", e.getMessage());
                }
            }


            if (gsonDescargas.getAgricultorList() != null && gsonDescargas.getAgricultorList().size() > 0){
                try {

                    MainActivity.myAppDB.myDao().deleteAgricultores();

//                                MainActivity.myAppDB.myDao().consultaVoid(new SimpleSQLiteQuery("DELETE FROM sqlite_sequence WHERE name='agricultor'", null));
                    List<Long> inserts = MainActivity.myAppDB.myDao().insertAgricultor(gsonDescargas.getAgricultorList());
                    for (long l : inserts) {
                        if (l <= 0 && inserts.size()  == gsonDescargas.getAgricultorList().size()) {
                            problema = true;
                            break;
                        }
                    }
                }catch (SQLiteException e) {
                    Log.e("SQLITE", e.getMessage());
                }
            }


            if (gsonDescargas.getRegionList() != null && gsonDescargas.getRegionList().size() > 0){
                try {

                    MainActivity.myAppDB.myDao().deleteRegiones();
//                                MainActivity.myAppDB.myDao().consultaVoid(new SimpleSQLiteQuery("DELETE FROM sqlite_sequence WHERE name='region'", null));
                    List<Long> inserts = MainActivity.myAppDB.myDao().insertRegiones(gsonDescargas.getRegionList());
                    for (long l : inserts) {
                        if (l <= 0 && inserts.size()  == gsonDescargas.getRegionList().size()) {
                            problema = true;
                            break;
                        }
                    }
                }catch (SQLiteException e) {
                    Log.e("SQLITE", e.getMessage());
                }
            }


            if (gsonDescargas.getProvinciaList() != null && gsonDescargas.getProvinciaList().size() > 0){
                try {

                    MainActivity.myAppDB.myDao().deleteProvincia();
//                                MainActivity.myAppDB.myDao().consultaVoid(new SimpleSQLiteQuery("DELETE FROM sqlite_sequence WHERE name='provincia'", null));
                    List<Long> inserts = MainActivity.myAppDB.myDao().insertProvincias(gsonDescargas.getProvinciaList());
                    for (long l : inserts) {
                        if (l <= 0 && inserts.size()  == gsonDescargas.getProvinciaList().size()) {
                            problema = true;
                            break;
                        }
                    }
                }catch (SQLiteException e) {
                    Log.e("SQLITE", e.getMessage());
                }
            }


            if (gsonDescargas.getComunaList() != null && gsonDescargas.getComunaList().size() > 0){
                try {

                    MainActivity.myAppDB.myDao().deleteComuna();
//                                MainActivity.myAppDB.myDao().consultaVoid(new SimpleSQLiteQuery("DELETE FROM sqlite_sequence WHERE name='comuna'", null));
                    List<Long> inserts = MainActivity.myAppDB.myDao().insertComunas(gsonDescargas.getComunaList());
                    for (long l : inserts) {
                        if (l <= 0 && inserts.size()  == gsonDescargas.getComunaList().size()) {
                            problema = true;
                            break;
                        }
                    }
                }catch (SQLiteException e) {
                    Log.e("SQLITE", e.getMessage());
                }
            }


            if (gsonDescargas.getEspecieList() != null && gsonDescargas.getEspecieList().size() > 0){
                try {

                    MainActivity.myAppDB.myDao().deleteEspecie();
                    List<Long> inserts = MainActivity.myAppDB.myDao().insertEspecie(gsonDescargas.getEspecieList());
                    for (long l : inserts) {
                        if (l <= 0 && inserts.size()  == gsonDescargas.getEspecieList().size()) {
                            problema = true;
                            break;
                        }
                    }
                }catch (SQLiteException e) {
                    Log.e("SQLITE", e.getMessage());
                }
            }

            if (gsonDescargas.getVariedadList() != null && gsonDescargas.getVariedadList().size() > 0){
                try {

                    MainActivity.myAppDB.myDao().deleteVariedad();
                    List<Long> inserts = MainActivity.myAppDB.myDao().insertVariedad(gsonDescargas.getVariedadList());
                    for (long l : inserts) {
                        if (l <= 0 && inserts.size()  == gsonDescargas.getVariedadList().size()) {
                            problema = true;
                            break;
                        }
                    }
                }catch (SQLiteException e) {
                    Log.e("SQLITE", e.getMessage());
                }
            }
            if (gsonDescargas.getFichasList() != null && gsonDescargas.getFichasList().size() > 0){
                try {

                    MainActivity.myAppDB.myDao().deleteFichas();
                    List<Long> inserts = MainActivity.myAppDB.myDao().insertFicha(gsonDescargas.getFichasList());
                    for (long l : inserts) {
                        if (l <= 0 && inserts.size()  == gsonDescargas.getFichasList().size()) {
                            problema = true;
                            break;
                        }
                    }
                }catch (SQLiteException e) {
                    Log.e("SQLITE", e.getMessage());
                }
            }


            if (gsonDescargas.getUnidadMedidas() != null && gsonDescargas.getUnidadMedidas().size() > 0){
                try {

                    MainActivity.myAppDB.myDao().deleteUM();
                    List<Long> inserts = MainActivity.myAppDB.myDao().insertUM(gsonDescargas.getUnidadMedidas());
                    for (long l : inserts) {
                        if (l <= 0 && inserts.size()  == gsonDescargas.getUnidadMedidas().size()) {
                            problema = true;
                            break;
                        }
                    }
                }catch (SQLiteException e) {
                    Log.e("SQLITE", e.getMessage());
                }
            }

            if (gsonDescargas.getUsuarios() != null && gsonDescargas.getUsuarios().size() > 0){
                try {

                    MainActivity.myAppDB.myDao().deleteUsuario();
                    List<Long> inserts = MainActivity.myAppDB.myDao().setUsuarios(gsonDescargas.getUsuarios());
                    for (long l : inserts) {
                        if (l <= 0 && inserts.size()  == gsonDescargas.getUsuarios().size()) {
                            problema = true;
                            break;
                        }
                    }
                }catch (SQLiteException e) {
                    Log.e("SQLITE", e.getMessage());
                }
            }

            if (gsonDescargas.getPredios() != null && gsonDescargas.getPredios().size() > 0){
                try {

                    MainActivity.myAppDB.myDao().deletePredios();
                    List<Long> inserts = MainActivity.myAppDB.myDao().insertPredios(gsonDescargas.getPredios());
                    for (long l : inserts) {
                        if (l <= 0 && inserts.size()  == gsonDescargas.getPredios().size()) {
                            problema = true;
                            break;
                        }
                    }
                }catch (SQLiteException e) {
                    Log.e("SQLITE", e.getMessage());
                }
            }


            if (gsonDescargas.getLotes() != null && gsonDescargas.getLotes().size() > 0){
                try {

                    MainActivity.myAppDB.myDao().deleteLotes();
                    List<Long> inserts = MainActivity.myAppDB.myDao().insertLotes(gsonDescargas.getLotes());
                    for (long l : inserts) {
                        if (l <= 0 && inserts.size()  == gsonDescargas.getLotes().size()) {
                            problema = true;
                            break;
                        }
                    }
                }catch (SQLiteException e) {
                    Log.e("SQLITE", e.getMessage());
                }
            }

            if (gsonDescargas.getTipoRiegos() != null && gsonDescargas.getTipoRiegos().size() > 0){
                try {

                    MainActivity.myAppDB.myDao().deleteTipoRiego();
                    List<Long> inserts = MainActivity.myAppDB.myDao().insertTipoRiego(gsonDescargas.getTipoRiegos());
                    for (long l : inserts) {
                        if (l <= 0 && inserts.size()  == gsonDescargas.getTipoRiegos().size()) {
                            problema = true;
                            break;
                        }
                    }
                }catch (SQLiteException e) {
                    Log.e("SQLITE", e.getMessage());
                }
            }

            if (gsonDescargas.getTipoSuelos() != null && gsonDescargas.getTipoSuelos().size() > 0){
                try {

                    MainActivity.myAppDB.myDao().deleteTipoSuelo();
                    List<Long> inserts = MainActivity.myAppDB.myDao().insertTipoSuelo(gsonDescargas.getTipoSuelos());
                    for (long l : inserts) {
                        if (l <= 0 && inserts.size()  == gsonDescargas.getTipoSuelos().size()) {
                            problema = true;
                            break;
                        }
                    }
                }catch (SQLiteException e) {
                    Log.e("SQLITE", e.getMessage());
                }
            }


            if (gsonDescargas.getMaquinarias() != null && gsonDescargas.getMaquinarias().size() > 0){
                try {

                    MainActivity.myAppDB.myDao().deleteMaquinaria();
                    List<Long> inserts = MainActivity.myAppDB.myDao().insertMaquinara(gsonDescargas.getMaquinarias());
                    for (long l : inserts) {
                        if (l <= 0 && inserts.size()  == gsonDescargas.getMaquinarias().size()) {
                            problema = true;
                            break;
                        }
                    }
                }catch (SQLiteException e) {
                    Log.e("SQLITE", e.getMessage());
                }
            }

            if (gsonDescargas.getTipoTenenciaMaquinarias() != null && gsonDescargas.getTipoTenenciaMaquinarias().size() > 0){
                try {

                    MainActivity.myAppDB.myDao().deleteTipoTenenciaMaquinaria();
                    List<Long> inserts = MainActivity.myAppDB.myDao().insertTipoTenenciaMaquinaria(gsonDescargas.getTipoTenenciaMaquinarias());
                    for (long l : inserts) {
                        if (l <= 0 && inserts.size()  == gsonDescargas.getTipoTenenciaMaquinarias().size()) {
                            problema = true;
                            break;
                        }
                    }
                }catch (SQLiteException e) {
                    Log.e("SQLITE", e.getMessage());
                }
            }

            if (gsonDescargas.getTipoTenenciaTerrenos() != null && gsonDescargas.getTipoTenenciaTerrenos().size() > 0){
                try {

                    MainActivity.myAppDB.myDao().deleteTipoTenenciaTerreno();
                    List<Long> inserts = MainActivity.myAppDB.myDao().insertTipoTenenciaTerreno(gsonDescargas.getTipoTenenciaTerrenos());
                    for (long l : inserts) {
                        if (l <= 0 && inserts.size()  == gsonDescargas.getTipoTenenciaTerrenos().size()) {
                            problema = true;
                            break;
                        }
                    }
                }catch (SQLiteException e) {
                    Log.e("SQLITE", e.getMessage());
                }
            }

            if (gsonDescargas.getFichaMaquinarias() != null && gsonDescargas.getFichaMaquinarias().size() > 0){
                try {
                    MainActivity.myAppDB.myDao().deleteFichaMaquinaria();
                    List<Long> inserts = MainActivity.myAppDB.myDao().insertFichaMaquinaria(gsonDescargas.getFichaMaquinarias());
                    for (long l : inserts) {
                        if (l <= 0 && inserts.size()  == gsonDescargas.getFichaMaquinarias().size()) {
                            problema = true;
                            break;
                        }
                    }
                }catch (SQLiteException e) {
                    Log.e("SQLITE", e.getMessage());
                }
            }

            if (gsonDescargas.getClientes() != null && gsonDescargas.getClientes().size() > 0){
                try {
                    MainActivity.myAppDB.myDao().deleteClientes();
                    List<Long> inserts = MainActivity.myAppDB.myDao().insertClientes(gsonDescargas.getClientes());
                    for (long l : inserts) {
                        if (l <= 0 && inserts.size()  == gsonDescargas.getClientes().size()) {
                            problema = true;
                            break;
                        }
                    }
                }catch (SQLiteException e) {
                    Log.e("SQLITE", e.getMessage());
                }
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


        }




        return problema;
    }
}
