package cl.smapdev.curimapu.fragments;

import android.app.ProgressDialog;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import java.util.ArrayList;
import java.util.List;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.relaciones.GsonDescargas;
import cl.smapdev.curimapu.clases.retrofit.ApiService;
import cl.smapdev.curimapu.clases.retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentPrincipal extends Fragment {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

/*
        List<CantidadVisitas>cantidadVisitas = MainActivity.myAppDB.myDao().getCantidadVisitasByEstado(2020);




        AnyChartView anyChartView = (AnyChartView) view.findViewById(R.id.left_chart_view);
        APIlib.getInstance().setActiveAnyChartView(anyChartView);
        anyChartView.setProgressBar(view.findViewById(R.id.progress_bar_left));


        Pie pie = AnyChart.pie();

        List<DataEntry> data = new ArrayList<>();
        String[] estados = new String[]{"Abierto", "Editable", "Cerrado"};

        if (cantidadVisitas.size() > 0){
            for (CantidadVisitas vs : cantidadVisitas){

                data.add(new ValueDataEntry(estados[vs.getEstado_visita()], vs.getTodos()));
            }
        }


        pie.data(data);
        pie.title("Estado de visitas");*/
//        pie.background().fill("white 0.3");

//        pie.background().fill("aquastyle");

//        pie.labels().position("inside");

//        pie.legend().title().enabled(true);
//        pie.legend().title()
//                .text("Retail channels")
//                .padding(0d, 0d, 10d, 0d);

//        pie.legend()
//                .position("center-bottom")
//                .itemsLayout(LegendLayout.VERTICAL)
//                .align(Align.BOTTOM);

//        anyChartView.setBackgroundColor("black");
//        anyChartView.setChart(pie);


        /*AnyChartView anyChartViewRight = (AnyChartView) view.findViewById(R.id.right_chart_view);
        APIlib.getInstance().setActiveAnyChartView(anyChartViewRight);
        anyChartViewRight.setProgressBar(view.findViewById(R.id.progress_bar_right));





        CircularGauge circularGauge = AnyChart.circular();
        circularGauge.data(new SingleValueDataSet(new String[] { "23", "34", "67", "93", "56", "100"}));
        circularGauge.fill("#fff")
                .stroke(null)
                .padding(0d, 0d, 0d, 0d)
                .margin(100d, 100d, 100d, 100d);
        circularGauge.startAngle(0d);
        circularGauge.sweepAngle(320d);

        Circular xAxis = circularGauge.axis(0)
                .radius(100d)
                .width(1d)
                .fill((Fill) null);
        xAxis.scale()
                .minimum(0d)
                .maximum(100d);
        xAxis.ticks("{ interval: 1 }")
                .minorTicks("{ interval: 1 }");
        xAxis.labels().enabled(false);
        xAxis.ticks().enabled(false);
        xAxis.minorTicks().enabled(false);

        circularGauge.label(0d)
                .text("Temazepam, <span style=\"\">32%</span>")
                .useHtml(true)
                .hAlign(HAlign.CENTER)
                .vAlign(VAlign.MIDDLE);
        circularGauge.label(0d)
                .anchor(Anchor.RIGHT_CENTER)
                .padding(0d, 10d, 0d, 0d)
                .height(17d / 2d + "%")
                .offsetY(100d + "%")
                .offsetX(0d);
        Bar bar0 = circularGauge.bar(0d);
        bar0.dataIndex(0d);
        bar0.radius(100d);
        bar0.width(17d);
        bar0.fill(new SolidFill("#64b5f6", 1d));
        bar0.stroke(null);
        bar0.zIndex(5d);
        Bar bar100 = circularGauge.bar(100d);
        bar100.dataIndex(5d);
        bar100.radius(100d);
        bar100.width(17d);
        bar100.fill(new SolidFill("#F5F4F4", 1d));
        bar100.stroke("1 #e5e4e4");
        bar100.zIndex(4d);



        circularGauge.margin(50d, 50d, 50d, 50d);
        circularGauge.title()
                .text("Medicine manufacturing progress' +\n" +
                        "    '<br/><span style=\"color:#929292; font-size: 12px;\">(ACME CORPORATION)</span>")
                .useHtml(true);
        circularGauge.title().enabled(true);
        circularGauge.title().hAlign(HAlign.CENTER);
        circularGauge.title()
                .padding(0d, 0d, 0d, 0d)
                .margin(0d, 0d, 20d, 0d);

        anyChartViewRight.setChart(circularGauge);
*/



//

//
//
//        MainActivity.myAppDB.myDao().insertAgricultor(new Agricultor("96.611.780-k","COMERCIAL LT LTDA.","966764273","NN","NN",2,4,false));
//
//        MainActivity.myAppDB.myDao().insertFicha(new Fichas(2019, "96.611.780-k","Poroto","Los Tilos",30.0,"Pivote 30ha suelo trumao categoría A",-36.872088173419165, -72.28010347444638,1,false));
//

    }


    @Override
    public void onResume() {
        super.onResume();
        new descargar().execute();
    }

    public class descargar extends AsyncTask<Void, Integer, Boolean>{

        ProgressDialog progressDialog = new ProgressDialog(getActivity());


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Espere un momento...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            final boolean[] problema = {false};

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<GsonDescargas> call = apiService.descargarDatos();
            call.enqueue(new Callback<GsonDescargas>() {
                @Override
                public void onResponse(@NonNull Call<GsonDescargas> call, @NonNull Response<GsonDescargas> response) {

                    GsonDescargas gsonDescargas  = response.body();
                    if (gsonDescargas != null){

                        if (gsonDescargas.getPro_cli_matList() != null && gsonDescargas.getPro_cli_matList().size() > 0){

                            try {


                                MainActivity.myAppDB.myDao().deleteProCliMat();
                                List<Long> inserts = MainActivity.myAppDB.myDao().insertInterfaz(gsonDescargas.getPro_cli_matList());
                                for (long l : inserts) {
                                    if (l <= 0) {
                                        problema[0] = true;
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
                                    if (l <= 0) {
                                        problema[0] = true;
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
                                    if (l <= 0) {
                                        problema[0] = true;
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
                                    if (l <= 0) {
                                        problema[0] = true;
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
                                    if (l <= 0) {
                                        problema[0] = true;
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
                                    if (l <= 0) {
                                        problema[0] = true;
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
                                    if (l <= 0) {
                                        problema[0] = true;
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
                                    if (l <= 0) {
                                        problema[0] = true;
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
                                    if (l <= 0) {
                                        problema[0] = true;
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
                                    if (l <= 0) {
                                        problema[0] = true;
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
                                    if (l <= 0) {
                                        problema[0] = true;
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
                                    if (l <= 0) {
                                        problema[0] = true;
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
                                    if (l <= 0) {
                                        problema[0] = true;
                                        break;
                                    }
                                }
                            }catch (SQLiteException e) {
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
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (!aBoolean){
                Toast.makeText(getActivity(), "Todo descargado con exito", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), "No se pudo descargar todo", Toast.LENGTH_SHORT).show();
            }


            progressDialog.dismiss();
        }
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        if (activity != null){
            activity.setDrawerEnabled(true);
            activity.updateView(getResources().getString(R.string.app_name), getResources().getString(R.string.subtitles_start));
        }
    }
}
