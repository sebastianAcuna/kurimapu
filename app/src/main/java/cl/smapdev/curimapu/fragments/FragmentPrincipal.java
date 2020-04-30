package cl.smapdev.curimapu.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.CardViewAdapter;
import cl.smapdev.curimapu.clases.adapters.SpinnerToolbarAdapter;
import cl.smapdev.curimapu.clases.relaciones.GsonDescargas;
import cl.smapdev.curimapu.clases.retrofit.ApiService;
import cl.smapdev.curimapu.clases.retrofit.RetrofitClient;
import cl.smapdev.curimapu.clases.sincronizacion.ServiceSync;
import cl.smapdev.curimapu.clases.tablas.CardViewsResumen;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Errores;
import cl.smapdev.curimapu.clases.tablas.Fichas;
import cl.smapdev.curimapu.clases.tablas.Fotos;
import cl.smapdev.curimapu.clases.tablas.Temporada;
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

import static cl.smapdev.curimapu.clases.utilidades.Descargas.volqueoDatos;


public class FragmentPrincipal extends Fragment {

    private MainActivity activity;

    private PieChart left_chart_view, right_chart_view, chv_3;
    private RecyclerView card_list;


    private ArrayList<String> id_temporadas = new ArrayList<>();
    private ArrayList<String> desc_temporadas = new ArrayList<>();

    private List<Temporada> temporadaList;

    private SharedPreferences prefs;

    private Spinner spinner_toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();

        if (activity != null){
            activity.startService(new Intent(activity, ServiceSync.class));
            prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
        }

        temporadaList = MainActivity.myAppDB.myDao().getTemporada();
        if (temporadaList.size() > 0){
            for (Temporada t : temporadaList){
                id_temporadas.add(t.getId_tempo_tempo());
                desc_temporadas.add(t.getNombre_tempo());
            }
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

        left_chart_view = view.findViewById(R.id.left_chart_view);
        right_chart_view = view.findViewById(R.id.right_chart_view);
        chv_3 = view.findViewById(R.id.chv_3);
        card_list = view.findViewById(R.id.card_list);
        spinner_toolbar = (Spinner) view.findViewById(R.id.spinner_toolbar);




        spinner_toolbar.setAdapter(new SpinnerToolbarAdapter(Objects.requireNonNull(getActivity()),R.layout.spinner_template_toolbar_view, temporadaList));


        recargarYear();
        spinner_toolbar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                prefs.edit().putString(Utilidades.SELECTED_ANO,id_temporadas.get(spinner_toolbar.getSelectedItemPosition())).apply();
                prefs.edit().putInt(Utilidades.SHARED_FILTER_FICHAS_YEAR, i).apply();
                cargarGraficos();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private void cargarGrafico(PieChart chart, Integer[] cantidadVisitas, String titulo, String[] parties){

        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);
        chart.setDragDecelerationFrictionCoef(0.95f);

//        Typeface tf = Typeface.createFromAsset(activity.getAssets(), "OpenSans-Light.ttf");

//        left_chart_view.setCenterTextTypeface(tf);
        chart.setCenterText(generateCenterSpannableText(titulo));

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.TRANSPARENT);

        chart.setTransparentCircleColor(Color.TRANSPARENT);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(60f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
//        left_chart_view.setDrawUnitsInChart(true);

        // add a selection listener
//        left_chart_view.setOnChartValueSelectedListener(this);

        /*seekBarX.setProgress(4);
        seekBarY.setProgress(10);*/

        chart.animateY(1400, Easing.EaseInOutQuad);
         chart.spin(2000, 0, 360, null);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        l.setTextColor(activity.getResources().getColor(R.color.colorOnSurface));

        // entry label styling
        chart.setEntryLabelColor(Color.BLACK);
//        left_chart_view.setEntryLabelTypeface(tfRegular);
        chart.setEntryLabelTextSize(12f);

        setData(chart,cantidadVisitas, parties);
    }

    private void setData(PieChart chart, Integer[] count, String[] parties) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        if (count.length > 0){
            for (int i = 0 ; i < count.length ; i++){
                entries.add(new PieEntry((float) count[i],  parties[i % parties.length],
                        activity.getResources().getDrawable(R.drawable.boton_export)));
            }
        }


        PieDataSet dataSet = new PieDataSet(entries, "Resultados");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
//        data.setValueTypeface(tfLight);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }


    private void recargarYear(){
        if (temporadaList.size() > 0){
            spinner_toolbar.setSelection(prefs.getInt(Utilidades.SHARED_FILTER_FICHAS_YEAR, temporadaList.size() - 1));
        }
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
                        List<Fichas> fichas = MainActivity.myAppDB.myDao().getFichasPorSubir();

                        if (visitas.size() > 0 || detalles.size() > 0 || fotos.size() > 0 || errores.size() > 0 || fichas.size() > 0) {
                             Subida.subida(activity);
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

        InternetStateClass mm = new InternetStateClass(activity, new returnValuesFromAsyntask() {
            @Override
            public void myMethod(boolean result) {
                if (result) {
                    final ProgressDialog progressDialog = new ProgressDialog(activity);
                    progressDialog.setTitle("Espere un momento...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    Config cnf = MainActivity.myAppDB.myDao().getConfig();

                    ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
                    Call<GsonDescargas> call = apiService.descargarDatos(cnf.getId(), cnf.getId_usuario());
                    call.enqueue(new Callback<GsonDescargas>() {
                        @Override
                        public void onResponse(@NonNull Call<GsonDescargas> call, @NonNull Response<GsonDescargas> response) {
                            boolean problema  = volqueoDatos(response.body());
                            if (!problema){
                                cargarGraficos();
                                temporadaList = MainActivity.myAppDB.myDao().getTemporada();
                                if (temporadaList.size() > 0){
                                    for (Temporada t : temporadaList){
                                        id_temporadas.add(t.getId_tempo_tempo());
                                        desc_temporadas.add(t.getNombre_tempo());
                                    }
                                }
                                Toast.makeText(activity, "Todo descargado con exito", Toast.LENGTH_SHORT).show();
                            }else{
                                Errores errores = new Errores();
                                errores.setCodigo_error(66);
                                errores.setMensaje_error("positivo en true al descargar home");
                                MainActivity.myAppDB.myDao().setErrores(errores);
                                Toast.makeText(activity, "No se pudo descargar todo", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(@NonNull Call<GsonDescargas> call, @NonNull Throwable t) {
                            //System.out.println(t.getMessage());
                            Errores errores = new Errores();
                            errores.setCodigo_error(66);
                            errores.setMensaje_error(t.toString());
                            MainActivity.myAppDB.myDao().setErrores(errores);

                            Toast.makeText(activity, "No se pudo descargar todo front", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(activity, activity.getResources().getString(R.string.sync_not_internet), Toast.LENGTH_SHORT).show();
                }
            }
        }, 1);
        mm.execute();

    }

    private void cargarGraficos(){
        List<CardViewsResumen> cardViewsResumen = MainActivity.myAppDB.myDao().getResumen(prefs.getString(Utilidades.SELECTED_ANO, "1"));
        CardViewAdapter cardViewAdapter = new CardViewAdapter(cardViewsResumen);
        card_list.setAdapter(cardViewAdapter);


        List<CardViewsResumen> lists = MainActivity.myAppDB.myDao().getCantidadVisitasByEtapa(prefs.getString(Utilidades.SELECTED_ANO, "1"));
        try {
            Integer[] visitasDatos = new Integer[lists.size()];
            String[] visitasNombres = new String[lists.size()];
            if (lists.size() > 0){
                int cont = 0;
                for (CardViewsResumen f : lists){
                    visitasDatos[cont] = Integer.parseInt(f.getTotal());
                    visitasNombres[cont] = Utilidades.getStateString(Utilidades.getPhenoState(Integer.parseInt(f.getNombre())));
                    cont++;
                }
            }
            cargarGrafico(left_chart_view,visitasDatos, "VISITAS / ETAPAS", visitasNombres);
        }catch (NumberFormatException e){
            Log.e("PARSE INTEGER", e.getMessage());
        }



        List<CardViewsResumen> listaV = MainActivity.myAppDB.myDao().getCantidadVariedadesByVisita(prefs.getString(Utilidades.SELECTED_ANO, "1"));
        try {
            Integer[] visitasDatos = new Integer[listaV.size()];
            String[] visitasNombres = new String[listaV.size()];
            if (listaV.size() > 0){
                int cont = 0;
                for (CardViewsResumen f : listaV){
                    visitasDatos[cont] = Integer.parseInt(f.getTotal());
                    visitasNombres[cont] = f.getNombre();
                    cont++;
                }
            }
            cargarGrafico(chv_3, visitasDatos, "VISITAS / VARIEDAD", visitasNombres);

        }catch (NumberFormatException e){
            Log.e("PARSE INTEGER", e.getMessage());
        }



        List<CardViewsResumen> listaE = MainActivity.myAppDB.myDao().getCantidadEspeciesByVisita(prefs.getString(Utilidades.SELECTED_ANO, "1"));
        try {
            Integer[] visitasDatos = new Integer[listaE.size()];
            String[] visitasNombres = new String[listaE.size()];
            if (listaE.size() > 0){
                int cont = 0;
                for (CardViewsResumen f : listaE){
                    visitasDatos[cont] = Integer.parseInt(f.getTotal());
                    visitasNombres[cont] = f.getNombre();
                    cont++;
                }
            }
            cargarGrafico(right_chart_view,visitasDatos, "VISITAS / ESPECIE", visitasNombres);
        }catch (NumberFormatException e){
            Log.e("PARSE INTEGER", e.getMessage());
        }
    }



    private SpannableString generateCenterSpannableText(String string) {

        SpannableString s = new SpannableString(string);
        s.setSpan(new RelativeSizeSpan(1.7f), 0, string.length(), 0);
        s.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.colorSecondary)), 0, s.length(), 0);
/*        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);*/
        return s;
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
