package cl.smapdev.curimapu.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.SingleValueDataSet;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.CircularGauge;
import com.anychart.charts.Pie;
import com.anychart.core.axes.Circular;
import com.anychart.core.gauge.pointers.Bar;
import com.anychart.enums.Align;
import com.anychart.enums.Anchor;
import com.anychart.enums.LegendLayout;
import com.anychart.graphics.vector.Fill;
import com.anychart.graphics.vector.SolidFill;
import com.anychart.graphics.vector.text.HAlign;
import com.anychart.graphics.vector.text.VAlign;

import java.util.ArrayList;
import java.util.List;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.relaciones.CantidadVisitas;
import cl.smapdev.curimapu.clases.tablas.CropRotation;
import cl.smapdev.curimapu.clases.tablas.Variedad;
import cl.smapdev.curimapu.clases.tablas.Agricultor;
import cl.smapdev.curimapu.clases.tablas.AnexoContrato;
import cl.smapdev.curimapu.clases.tablas.Comuna;
import cl.smapdev.curimapu.clases.tablas.Especie;
import cl.smapdev.curimapu.clases.tablas.Region;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class FragmentPrincipal extends Fragment {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                if(MainActivity.myAppDB.myDao().getComunas().size() <= 0){

                    MainActivity.myAppDB.myDao().insertComuna(new Comuna("Talca", 1));
                    MainActivity.myAppDB.myDao().insertComuna(new Comuna("Constitución", 1));
                    MainActivity.myAppDB.myDao().insertComuna(new Comuna("Concepcion", 2));
                    MainActivity.myAppDB.myDao().insertComuna(new Comuna("Los Ángeles", 2));
                    MainActivity.myAppDB.myDao().insertComuna(new Comuna("Mulchén", 2));
                    MainActivity.myAppDB.myDao().insertComuna(new Comuna("Temuco", 3));
                    MainActivity.myAppDB.myDao().insertComuna(new Comuna("Angól", 3));
                    MainActivity.myAppDB.myDao().insertComuna(new Comuna("Villa Rica", 3));
                    MainActivity.myAppDB.myDao().insertComuna(new Comuna("Valdivia", 4));
                    MainActivity.myAppDB.myDao().insertComuna(new Comuna("Rio Bueno", 4));
                    MainActivity.myAppDB.myDao().insertComuna(new Comuna("Los Lagos", 4));
                }


                if (MainActivity.myAppDB.myDao().getRegiones().size() <= 0){
                    MainActivity.myAppDB.myDao().insertRegion(new Region("Región del Maule"));
                    MainActivity.myAppDB.myDao().insertRegion(new Region("Región del Biobío"));
                    MainActivity.myAppDB.myDao().insertRegion(new Region("Región del la Araucanía"));
                    MainActivity.myAppDB.myDao().insertRegion(new Region("Región del los Rios"));
                }

                if (MainActivity.myAppDB.myDao().getCropRotation().size() <= 0){
                    MainActivity.myAppDB.myDao().insertCrop(new CropRotation(2020,"Arvejas",1));
                    MainActivity.myAppDB.myDao().insertCrop(new CropRotation(2020,"Canola",1));
                    MainActivity.myAppDB.myDao().insertCrop(new CropRotation(2020,"Maiz",1));
                }

                if (MainActivity.myAppDB.myDao().getEspecies().size() <= 0 ){
                    MainActivity.myAppDB.myDao().insertEspecie(new Especie("Maravilla"));
                    MainActivity.myAppDB.myDao().insertEspecie(new Especie("Frejol"));
                    MainActivity.myAppDB.myDao().insertEspecie(new Especie("Canola"));
                    MainActivity.myAppDB.myDao().insertEspecie(new Especie("Arveja"));
                }

                if (MainActivity.myAppDB.myDao().getAgricultores().size() <= 0){
                    MainActivity.myAppDB.myDao().insertAgricultor(new Agricultor("96.611.780-k","agricultor test","948426521","admin test","948426521",1,1,false));
                    MainActivity.myAppDB.myDao().insertAgricultor(new Agricultor("18.804.066-7","Sebastian Acuña","948426521","admin test","948426521",2,4,false));
                    MainActivity.myAppDB.myDao().insertAgricultor(new Agricultor("18.805.186-3","agricultor 2","948426521","admin test","948426521",3,7,false));
                    MainActivity.myAppDB.myDao().insertAgricultor(new Agricultor("19.714.871-3","Josue Parada","948426521","admin test","948426521",4,9,false));
                }

                if (MainActivity.myAppDB.myDao().getVariedades().size() <= 0){
                    MainActivity.myAppDB.myDao().insertVariedad(new Variedad(1,"278A (1627A) FEMALE"));
                    MainActivity.myAppDB.myDao().insertVariedad(new Variedad(1,"FS73020 FEMALE"));
                    MainActivity.myAppDB.myDao().insertVariedad(new Variedad(1,"FR84222 MALE"));
                    MainActivity.myAppDB.myDao().insertVariedad(new Variedad(1,"54Ax18B FEMALE"));


                    MainActivity.myAppDB.myDao().insertVariedad(new Variedad(2,"PV 792"));
                    MainActivity.myAppDB.myDao().insertVariedad(new Variedad(2,"PV766"));
                    MainActivity.myAppDB.myDao().insertVariedad(new Variedad(2,"PV 609"));
                    MainActivity.myAppDB.myDao().insertVariedad(new Variedad(2,"PV 653 ENVASADO"));


                    MainActivity.myAppDB.myDao().insertVariedad(new Variedad(3,"PR4CN-610 MALE"));
                    MainActivity.myAppDB.myDao().insertVariedad(new Variedad(3,"PA1CN-129 FEMALE"));
                    MainActivity.myAppDB.myDao().insertVariedad(new Variedad(3,"SSC661R MALE ENVASADO"));
                    MainActivity.myAppDB.myDao().insertVariedad(new Variedad(3,"WSC556A FEMALE ENVASADO"));

                    MainActivity.myAppDB.myDao().insertVariedad(new Variedad(4,"UTRILLO"));
                    MainActivity.myAppDB.myDao().insertVariedad(new Variedad(4,"RRBF 4575"));
                    MainActivity.myAppDB.myDao().insertVariedad(new Variedad(4,"COBLENTZ"));
                }

                if (MainActivity.myAppDB.myDao().getAnexos().size() <= 0){

                    MainActivity.myAppDB.myDao().insertAnexo(new AnexoContrato("20-AR0011",1,1,"96.611.780-k","Privote",1));
                    MainActivity.myAppDB.myDao().insertAnexo(new AnexoContrato("20-AR0031",2,1,"96.611.780-k","El Roble 1",3));
                    MainActivity.myAppDB.myDao().insertAnexo(new AnexoContrato("20-AR0009",3,1,"18.804.066-7","El Ajial",4));
                    MainActivity.myAppDB.myDao().insertAnexo(new AnexoContrato("20-AR0079",5,2,"18.804.066-7","Privote B",1));
                    MainActivity.myAppDB.myDao().insertAnexo(new AnexoContrato("20-AR0010",6,2,"19.971.871-3","Pivote Chico Sur",3));
                    MainActivity.myAppDB.myDao().insertAnexo(new AnexoContrato("20-AR0032",7,2,"19.971.871-3","CENTRO CORDILLERA",4));
                    MainActivity.myAppDB.myDao().insertAnexo(new AnexoContrato("20-AR0136",9,3,"96.611.780-k","ARVEJA",1));
                    MainActivity.myAppDB.myDao().insertAnexo(new AnexoContrato("20-AR0139",10,3,"96.611.780-k","PAÑO 3",3));
                    MainActivity.myAppDB.myDao().insertAnexo(new AnexoContrato("20-AR0138",11,3,"18.804.066-7","PAÑO 2",4));
                    MainActivity.myAppDB.myDao().insertAnexo(new AnexoContrato("20-AR0137",13,4,"18.804.066-7","PAÑO 1",1));
                    MainActivity.myAppDB.myDao().insertAnexo(new AnexoContrato("20-AR0140",14,4,"19.971.871-3","PAÑO 4",3));
                    MainActivity.myAppDB.myDao().insertAnexo(new AnexoContrato("20-AR0300",15,4,"19.971.871-3","CENTRO CORDILLERA 2",4));

                }
            }
        });


        ConstraintLayout constraintLayout = (ConstraintLayout) view.findViewById(R.id.relative_constraint_principal);
        ConstraintSet constraintSet = new ConstraintSet();

        TextView textview1 = new TextView(getActivity());
        textview1.setId(View.generateViewId());
        textview1.setText("PROBANDO ESTA SHIET");



//        textview1.setWidth(WRAP_CONTENT);
//        textview1.setHeight(WRAP_CONTENT);

        constraintLayout.addView(textview1,0);
        ViewGroup.LayoutParams params = textview1.getLayoutParams();
// Changes the height an
// d width to the specified *pixels*
        params.height = WRAP_CONTENT;
        params.width = 0;
        textview1.setLayoutParams(params);

        EditText textview2 = new EditText(getActivity());


        textview2.setId(View.generateViewId());
        textview2.setText("PROBANDO OTRA  SHIET");
//        textview2.setBackgroundColor(getResources().getColor(R.color.colorGold));
        constraintLayout.addView(textview2,1);


        ViewGroup.LayoutParams params2 = textview2.getLayoutParams();
// Changes the height an
// d width to the specified *pixels*
        params2.height = WRAP_CONTENT;
        params2.width = 0;
        textview2.setLayoutParams(params2);

        constraintSet.clone(constraintLayout);
        constraintSet.connect(textview1.getId(), ConstraintSet.TOP,constraintLayout.getId(),ConstraintSet.TOP,60);
        constraintSet.connect(textview1.getId(), ConstraintSet.START,constraintLayout.getId(),ConstraintSet.START,60);
        constraintSet.connect(textview1.getId(), ConstraintSet.END,textview2.getId(),ConstraintSet.START,60);


        constraintSet.connect(textview2.getId(), ConstraintSet.START,textview1.getId(),ConstraintSet.END,60);
        constraintSet.connect(textview2.getId(), ConstraintSet.TOP,constraintLayout.getId(),ConstraintSet.TOP,60);
        constraintSet.connect(textview2.getId(), ConstraintSet.END,constraintLayout.getId(),ConstraintSet.END,60);
        constraintSet.applyTo(constraintLayout);





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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        if (activity != null){
            activity.setDrawerEnabled(true);
            activity.updateView(getResources().getString(R.string.app_name), getResources().getString(R.string.subtitles_start));
        }
    }
}
