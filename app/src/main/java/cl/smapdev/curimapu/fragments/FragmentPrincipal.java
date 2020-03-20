package cl.smapdev.curimapu.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.relaciones.Respuesta;
import cl.smapdev.curimapu.clases.relaciones.SubidaDatos;
import cl.smapdev.curimapu.clases.retrofit.ApiService;
import cl.smapdev.curimapu.clases.retrofit.RetrofitClient;
import cl.smapdev.curimapu.clases.sincronizacion.ServiceSync;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Errores;
import cl.smapdev.curimapu.clases.tablas.Fotos;
import cl.smapdev.curimapu.clases.tablas.Visitas;
import cl.smapdev.curimapu.clases.tablas.detalle_visita_prop;
import cl.smapdev.curimapu.clases.utilidades.Descargas;
import cl.smapdev.curimapu.clases.utilidades.InternetStateClass;
import cl.smapdev.curimapu.clases.utilidades.Subida;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.clases.utilidades.returnValuesFromAsyntask;
import cl.smapdev.curimapu.fragments.dialogos.DialogFilterFichas;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentPrincipal extends Fragment {

    private MainActivity activity;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();

        if (activity != null){
            activity.startService(new Intent(activity, ServiceSync.class));
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        setHasOptionsMenu(true);
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_inicio, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_upload_files) {
            InternetStateClass mm = new InternetStateClass(activity, new returnValuesFromAsyntask() {
                @Override
                public void myMethod(boolean result) {
                    if (result) {
                        List<Visitas> visitas = MainActivity.myAppDB.myDao().getVisitasPorSubir();
                        List<detalle_visita_prop> detalles = MainActivity.myAppDB.myDao().getDetallesPorSubir();
                        List<Fotos> fotos = MainActivity.myAppDB.myDao().getFotos();
                        List<Errores> errores = MainActivity.myAppDB.myDao().getErroresPorSubir();

                        if (visitas.size() > 0 || detalles.size() > 0 || fotos.size() > 0 || errores.size() > 0) {
                            new Subida.subida(activity, 1).execute();
                        } else {
                            Toast.makeText(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(activity, activity.getResources().getString(R.string.sync_not_internet), Toast.LENGTH_SHORT).show();
                    }
                }
            }, 1);
            mm.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onResume() {
        super.onResume();
        new Descargas.descargar(activity).execute();
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
