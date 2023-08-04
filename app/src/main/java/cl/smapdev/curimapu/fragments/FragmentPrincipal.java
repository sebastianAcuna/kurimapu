package cl.smapdev.curimapu.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.PrimeraPrioridadAdapter;
import cl.smapdev.curimapu.clases.adapters.SitiosNoVisitadosAdapter;
import cl.smapdev.curimapu.clases.adapters.SpinnerToolbarAdapter;
import cl.smapdev.curimapu.clases.modelo.CheckListSync;
import cl.smapdev.curimapu.clases.modelo.EstacionFloracionSync;
import cl.smapdev.curimapu.clases.modelo.RecomendacionesSync;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.relaciones.CheckListCapCompleto;
import cl.smapdev.curimapu.clases.relaciones.CheckListLimpiezaCamionesCompleto;
import cl.smapdev.curimapu.clases.relaciones.CheckListRequest;
import cl.smapdev.curimapu.clases.relaciones.EstacionFloracionCompleto;
import cl.smapdev.curimapu.clases.relaciones.EstacionFloracionRequest;
import cl.smapdev.curimapu.clases.relaciones.EstacionesCompletas;
import cl.smapdev.curimapu.clases.relaciones.GsonDescargas;
import cl.smapdev.curimapu.clases.relaciones.RecomendacionesRequest;
import cl.smapdev.curimapu.clases.relaciones.Respuesta;
import cl.smapdev.curimapu.clases.relaciones.SitiosNoVisitadosAnexos;
import cl.smapdev.curimapu.clases.relaciones.SubidaDatos;
import cl.smapdev.curimapu.clases.relaciones.VisitaDetalle;
import cl.smapdev.curimapu.clases.relaciones.VisitasCompletas;
import cl.smapdev.curimapu.clases.retrofit.ApiService;
import cl.smapdev.curimapu.clases.retrofit.RetrofitClient;
import cl.smapdev.curimapu.clases.tablas.AnexoContrato;
import cl.smapdev.curimapu.clases.tablas.AnexoVilab;
import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembra;
import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembraDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListCosecha;
import cl.smapdev.curimapu.clases.tablas.CheckListLimpiezaCamiones;
import cl.smapdev.curimapu.clases.tablas.CheckListSiembra;
import cl.smapdev.curimapu.clases.tablas.ChecklistLimpiezaCamionesDetalle;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.CropRotation;
import cl.smapdev.curimapu.clases.tablas.Errores;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracion;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracionDetalle;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracionEstaciones;
import cl.smapdev.curimapu.clases.tablas.Evaluaciones;
import cl.smapdev.curimapu.clases.tablas.Fichas;
import cl.smapdev.curimapu.clases.tablas.Fotos;
import cl.smapdev.curimapu.clases.tablas.FotosFichas;
import cl.smapdev.curimapu.clases.tablas.Temporada;
import cl.smapdev.curimapu.clases.tablas.Visitas;
import cl.smapdev.curimapu.clases.tablas.detalle_visita_prop;
import cl.smapdev.curimapu.clases.utilidades.DescargaImagenes;
import cl.smapdev.curimapu.clases.utilidades.InternetStateClass;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.clases.utilidades.returnValuesFromAsyntask;
import cl.smapdev.curimapu.infraestructure.utils.coroutines.ApplicationExecutors;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static cl.smapdev.curimapu.clases.utilidades.Descargas.volqueoDatos;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

public class FragmentPrincipal extends Fragment {


    private MainActivity activity;

    private RecyclerView card_list;
    private RecyclerView lista_sitios_no_visitados;
    private RecyclerView lista_primera_prioridad;

    private SitiosNoVisitadosAdapter adapterNovis;
    private PrimeraPrioridadAdapter adapterPrimera;

    private final ArrayList<String> id_temporadas = new ArrayList<>();
    private final ArrayList<String> desc_temporadas = new ArrayList<>();


    private LinearLayout contenedor_botones;

    private LinearLayout contenedor_alerta_inicio;

    private TextView lbl_muestra_subidas;
    private ImageView  img_muestra_subidas;

    private ConstraintLayout contenedor_botonera_subida;
    private Button btn_subir_check, btn_subir_recomendaciones, btn_subir_estaciones;

    private ProgressDialog vilabProgressDialog;


    private Button btn_descargar;
    private Button btn_preparar;
    private Button btn_sube_marcadas;

    private TextView visitas_titulo,visitas_marca;

    private ImageView imagen_muestra;



    private List<Temporada> temporadaList;

    private SharedPreferences prefs;

    private Spinner spinner_toolbar;

    private String marca_especial_temporada;
    private ProgressDialog progressDialogGeneral;

    private ProgressBar progressBar1, progressBar2;

    int cantidadVisitasSubidas = 0;
    int cantidadVisitasPorSubir = 0;
    int contadorVisita = 0;

    private TextView titulo_sitios_no_visitados;
    private TextView titulo_primera_prioridad;


    ArrayList<Integer> botonesSeleccionados = new ArrayList<>();
    ArrayList<Integer> idVisitasSeleccionadas = new ArrayList<>();

    private View view;

    private Handler handler = new Handler();
    private int retryDelay = 500;
    private Runnable retryRunnable;

    private int currentQueue = 1;
    private int totalQueue;
    private List<Integer> imagenesDescargadas = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();

        if (activity != null){
            prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
            progressDialogGeneral = new ProgressDialog(activity);
        }

        vilabProgressDialog = new ProgressDialog(activity);

        temporadaList = MainActivity.myAppDB.myDao().getTemporada();
        setSpecialSeason(temporadaList);

    }

    public void setSpecialSeason(List<Temporada> temporadas){
        if (temporadas.size() > 0){
            for (Temporada t : temporadas){
                id_temporadas.add(t.getId_tempo_tempo());
                desc_temporadas.add(t.getNombre_tempo());

                if(t.getEspecial_temporada() > 0){
                    marca_especial_temporada = t.getId_tempo_tempo();
                }
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view  =  inflater.inflate(R.layout.fragment_inicio, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);


        spinner_toolbar = view.findViewById(R.id.spinner_toolbar);

        btn_descargar = view.findViewById(R.id.btn_descargar);
        btn_preparar = view.findViewById(R.id.btn_preparar);
        btn_sube_marcadas = view.findViewById(R.id.btn_sube_marcadas);

        visitas_titulo = view.findViewById(R.id.visitas_titulo);
        visitas_marca = view.findViewById(R.id.visitas_marca);

        contenedor_botones = view.findViewById(R.id.contenedor_botones);
        contenedor_alerta_inicio = view.findViewById(R.id.contenedor_alerta_inicio);

        titulo_sitios_no_visitados = view.findViewById(R.id.titulo_sitios_no_visitados);
        titulo_primera_prioridad = view.findViewById(R.id.titulo_primera_prioridad);

        progressBar1 = view.findViewById(R.id.progressBar1);
        progressBar2 = view.findViewById(R.id.progressBar2);

        lista_sitios_no_visitados = view.findViewById(R.id.lista_sitios_no_visitados);
        lista_primera_prioridad = view.findViewById(R.id.lista_primera_prioridad);

        lbl_muestra_subidas = view.findViewById(R.id.lbl_muestra_subidas);
        img_muestra_subidas = view.findViewById(R.id.img_muestra_subidas);
        contenedor_botonera_subida = view.findViewById(R.id.contenedor_botonera_subida);
        btn_subir_check = view.findViewById(R.id.btn_subir_check);
        btn_subir_estaciones = view.findViewById(R.id.btn_subir_estaciones);
        btn_subir_recomendaciones = view.findViewById(R.id.btn_subir_recomendaciones);

        imagen_muestra = view.findViewById(R.id.imagen_muestra);



        lbl_muestra_subidas.setOnClickListener(view1 -> ocultarBotoneraSubida());
        img_muestra_subidas.setOnClickListener(view1 -> ocultarBotoneraSubida());


        btn_subir_recomendaciones.setOnClickListener(view1 -> preparaSubirRecomendaciones());
        btn_subir_check.setOnClickListener(view1 -> preparaSubirChecklist());
        btn_subir_estaciones.setOnClickListener(view1 -> preparaSubirEstaciones());

        cargarToolbar();

        recargarYear();
        spinner_toolbar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                prefs.edit().putString(Utilidades.SELECTED_ANO,id_temporadas.get(spinner_toolbar.getSelectedItemPosition())).apply();
                prefs.edit().putInt(Utilidades.SHARED_FILTER_FICHAS_YEAR, i).apply();

                sitiosNoVisitados(Integer.parseInt(id_temporadas.get(spinner_toolbar.getSelectedItemPosition())));
                primeraPrioridad(Integer.parseInt(id_temporadas.get(spinner_toolbar.getSelectedItemPosition())));
                revisarAnexosPendienteFecha();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        btn_descargar.setOnClickListener(view1 -> {

            btn_descargar.setEnabled(false);
            btn_preparar.setEnabled(false);
            List<Visitas> visitas = MainActivity.myAppDB.myDao().getVisitasPorSubir();
            List<detalle_visita_prop> detalles = MainActivity.myAppDB.myDao().getDetallesPorSubir();
            List<Fotos> fotos = MainActivity.myAppDB.myDao().getFotos();
            List<Fichas> fichas = MainActivity.myAppDB.myDao().getFichasPorSubir();
            List<FotosFichas> fotosFichas = MainActivity.myAppDB.myDao().getFotosFichasPorSubir();
            List<CropRotation> crops = MainActivity.myAppDB.myDao().getCropsPorSubir();

            if(visitas.size() <= 0 && detalles.size() <= 0 && fotos.size() <= 0 && fichas.size() <= 0 && fotosFichas.size() <= 0 && crops.size() <= 0 ) {
                InternetStateClass mm = new InternetStateClass(activity, result -> {
                    if (result) {
                        btn_descargar.setEnabled(true);
                        btn_preparar.setEnabled(true);
                        descargando();
                    }
                }, 1);
                mm.execute();
            }else{
                btn_descargar.setEnabled(true);
                btn_preparar.setEnabled(true);
                Utilidades.avisoListo(getActivity(), "ATENCION", "TIENE " +
                        "\n-" + visitas.size() + " VISITAS " +
                        "\n-" + fotos.size() + " FOTOS " +
                        "\n-" + detalles.size() + " DETALLE VISITA (LIBRO DE CAMPO) " +
                        "\n-" + fichas.size() + " PROSPECTOS " +
                        "\n-" + fotosFichas.size() + " FOTOS EN PROSPECTOS " +
                        "\n-" + crops.size() + " ROTACIONES EN PROSPECTOS " +
                        "\nPENDIENTES, POR FAVOR, PRIMERO SINCRONICE ", "ENTIENDO");
            }

        });


        btn_sube_marcadas.setOnClickListener(view12 -> {
            if(botonesSeleccionados.size() > 0){
                contadorVisita = 0;
                prepararVisitaAgrupada(contadorVisita);
            }
        });

        btn_preparar.setOnClickListener(view13 -> {
            btn_descargar.setEnabled(false);
            btn_preparar.setEnabled(false);
            prepararVisitas();
        });

        revisarAnexosPendienteFecha();
    }


    void revisarAnexosPendienteFecha(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<AnexoContrato>> anexosF = executorService.submit(()->MainActivity.myAppDB.myDao().getAnexosSinFechaSiembra(id_temporadas.get(spinner_toolbar.getSelectedItemPosition())));

        try {
            List<AnexoContrato> anexos = anexosF.get();

            contenedor_alerta_inicio.setVisibility((anexos.size() > 0) ? View.VISIBLE : View.GONE);
            executorService.shutdown();

        } catch (ExecutionException | InterruptedException e) {
            executorService.shutdown();
            Log.e("ERROR_ALERTA", "ERROR"+e.getMessage());
        }

    }

    void ocultarBotoneraSubida(){
        contenedor_botonera_subida.setVisibility((contenedor_botonera_subida.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
        img_muestra_subidas.setImageDrawable((contenedor_botonera_subida.getVisibility() == View.VISIBLE) ?  getResources().getDrawable(R.drawable.ic_expand_down) : getResources().getDrawable(R.drawable.ic_expand_up));
    }

    void prepararVisitaAgrupada(int contadorVisitas){
        Visitas v= MainActivity.myAppDB.myDao().getVisitas(idVisitasSeleccionadas.get(contadorVisitas));
        subirVisita(v, null);
    }

    void cargarToolbar(){
        spinner_toolbar.setAdapter(new SpinnerToolbarAdapter(activity,R.layout.spinner_template_toolbar_view, temporadaList));
        recargarYear();
    }


    private void recargarYear(){
        if (temporadaList.size() > 0 && spinner_toolbar != null &&  spinner_toolbar.getAdapter() != null){
            spinner_toolbar.setSelection((marca_especial_temporada.isEmpty()) ? prefs.getInt(Utilidades.SHARED_FILTER_FICHAS_YEAR, temporadaList.size() - 1) : id_temporadas.indexOf(marca_especial_temporada));
        }
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_inicio, menu);
    }


    public void preparaSubirRecomendaciones(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Evaluaciones>> chkF = executorService.submit(()
                -> MainActivity.myAppDB.DaoEvaluaciones()
                .getEvaluacionesPendientesSync());

        try {
            List<Evaluaciones> chk = chkF.get();

            if(chk.size() <= 0){
                executorService.shutdown();
                return;
            }

            RecomendacionesRequest chkS = new RecomendacionesRequest();

            chkS.setEvaluacionesList(chk);
            prepararSubirRecomendaciones( chkS );

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void prepararSubirRecomendaciones( RecomendacionesRequest recomendacionesRequest){
        InternetStateClass mm = new InternetStateClass(activity, result -> {
            if(!result){
                Toasty.error(activity, activity.getResources().getString(R.string.sync_not_internet), Toast.LENGTH_SHORT, true).show();
                return;
            }


            ProgressDialog pd = new ProgressDialog(activity);
            pd.setMessage("conectandose a internet, espere por favor");
            pd.show();


            if(pd.isShowing()){
                pd.dismiss();
            }

            new RecomendacionesSync( recomendacionesRequest, requireActivity(), (state, message) -> {
                if(state){
                    Toasty.success(requireActivity(), message, Toast.LENGTH_LONG, true).show();
                }else{
                    Toasty.error(requireActivity(), message, Toast.LENGTH_LONG, true).show();
                }
            });

        }, 1);
        mm.execute();
    }

    private void prepararSubirEst(EstacionFloracionRequest checkListRequest){

        new EstacionFloracionSync( checkListRequest, requireActivity(), (state, message) -> {
            if(state){
                Toasty.success(requireActivity(), message, Toast.LENGTH_LONG, true).show();
            }else{
                Toasty.error(requireActivity(), message, Toast.LENGTH_LONG, true).show();
            }
        });
    }

    public void preparaSubirEstaciones(){

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<EstacionFloracion>> chkF = executorService.submit(()
                -> MainActivity.myAppDB.DaoEstacionFloracion()
                .getEstacionesToSync());
        try {

            List<EstacionFloracion> estacionFloracions = chkF.get();

            if(estacionFloracions.size() == 0){
                executorService.shutdown();
                Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                return;
            }

            EstacionFloracionRequest floracionRequest = new EstacionFloracionRequest();
            List<EstacionFloracionCompleto> estacionesFloracion = new ArrayList<>();


            for (EstacionFloracion estacionFloracion : estacionFloracions){
                List<EstacionesCompletas> estacionesCompletas = new ArrayList<>();

                List<EstacionFloracionEstaciones> estaciones  =
                        executorService.submit(() -> MainActivity.myAppDB
                                .DaoEstacionFloracion().getEstacionesByPadre(estacionFloracion.getClave_unica_floracion())).get();

                for (EstacionFloracionEstaciones estacion : estaciones){

                    List<EstacionFloracionDetalle> detalles  =
                            executorService.submit(() -> MainActivity.myAppDB
                                    .DaoEstacionFloracion().getDetalleByClaveEstacion(estacion.getClave_unica_floracion_estaciones())).get();

                    EstacionesCompletas estacionesCompleta = new EstacionesCompletas();
                    estacionesCompleta.setEstaciones(estacion);
                    estacionesCompleta.setDetalles(detalles);
                    estacionesCompletas.add(estacionesCompleta);
                }

                EstacionFloracionCompleto completo = new EstacionFloracionCompleto();

                completo.setEstacionFloracion(estacionFloracion);
                completo.setEstaciones(estacionesCompletas);

                estacionesFloracion.add(completo);
            }

            floracionRequest.setEstacionFloracionCompletos(estacionesFloracion);
            prepararSubirEst( floracionRequest );

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            executorService.shutdown();
        }
    }

    public void preparaSubirChecklist(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<CheckListSiembra>> chkF = executorService.submit(()
                -> MainActivity.myAppDB.DaoClSiembra()
                .getClSiembraToSync());


        Future<List<CheckListCosecha>> chkFC = executorService.submit(()
                -> MainActivity.myAppDB.DaoCheckListCosecha()
                .getClCosechaToSync());

        Future<List<CheckListCapacitacionSiembra>> checkListCapacitacionSiembraFuture
                = executorService.submit(()
                -> MainActivity.myAppDB.DaoCheckListCapSiembra()
                .getClCapSiembraByEstado( 0, Utilidades.TIPO_DOCUMENTO_CAPACITACION_SIEMBRA));

        Future<List<CheckListCapacitacionSiembra>> checkListCapacitacionCosechaFuture
                = executorService.submit(()
                -> MainActivity.myAppDB.DaoCheckListCapSiembra()
                .getClCapSiembraByEstado( 0, Utilidades.TIPO_DOCUMENTO_CAPACITACION_COSECHA));

        Future<List<CheckListLimpiezaCamiones>> checkLisLimpiezaCamionesFuture
                = executorService.submit(()
                -> MainActivity.myAppDB.DaoCheckListLimpiezaCamiones()
                .getClLimpiezaCamionesByEstado( 0));

        try {

            List<CheckListSiembra> chk = chkF.get();

            List<CheckListCosecha> chkC = chkFC.get();

            List<CheckListCapacitacionSiembra> capSiembraCab
                    = checkListCapacitacionSiembraFuture.get();

            List<CheckListCapacitacionSiembra> capCosechaCab
                    = checkListCapacitacionCosechaFuture.get();

            List<CheckListLimpiezaCamiones> capLimpiezaCamionesCab
                    = checkLisLimpiezaCamionesFuture.get();

            if(chk.size() <= 0 && chkC.size() <= 0 && capSiembraCab.size() <= 0 && capCosechaCab.size() <=0 && capLimpiezaCamionesCab.size() <= 0){
                executorService.shutdown();
                Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                return;
            }

            CheckListRequest chkS = new CheckListRequest();
            List<CheckListCapCompleto> chkList = new ArrayList<>();
            List<CheckListLimpiezaCamionesCompleto> chkListLimpiezaCamiones = new ArrayList<>();

            if(capSiembraCab.size() > 0){

                for (CheckListCapacitacionSiembra clc : capSiembraCab) {
                    List<CheckListCapacitacionSiembraDetalle> detalle =
                            executorService.submit(() -> MainActivity.myAppDB
                                    .DaoCheckListCapSiembra()
                                    .getCapSiembraDetallesByPadre(clc.getClave_unica(), Utilidades.TIPO_DOCUMENTO_CAPACITACION_SIEMBRA)
                            ).get();

                    CheckListCapCompleto completo = new CheckListCapCompleto();
                    completo.setCabecera(clc);
                    completo.setDetalles(detalle);

                    chkList.add(completo);
                }

                chkS.setCheckListCapCompletos( chkList );
            }

            if(capCosechaCab.size() > 0){

                for (CheckListCapacitacionSiembra clc : capCosechaCab) {
                    List<CheckListCapacitacionSiembraDetalle> detalle =
                            executorService.submit(() -> MainActivity.myAppDB
                                    .DaoCheckListCapSiembra()
                                    .getCapSiembraDetallesByPadre(clc.getClave_unica(), Utilidades.TIPO_DOCUMENTO_CAPACITACION_COSECHA)
                            ).get();

                    CheckListCapCompleto completo = new CheckListCapCompleto();
                    completo.setCabecera(clc);
                    completo.setDetalles(detalle);

                    chkList.add(completo);
                }

                chkS.setCheckListCapCompletos( chkList );
            }

            if(capLimpiezaCamionesCab.size() > 0){

                for (CheckListLimpiezaCamiones clc : capLimpiezaCamionesCab) {
                    List<ChecklistLimpiezaCamionesDetalle> detalle =
                            executorService.submit(() -> MainActivity.myAppDB
                                    .DaoCheckListLimpiezaCamiones()
                                    .getLimpiezaCamionesDetallesByPadre(clc.getClave_unica())
                            ).get();

                    CheckListLimpiezaCamionesCompleto completo = new CheckListLimpiezaCamionesCompleto();
                    completo.setCabecera(clc);
                    completo.setDetalles(detalle);

                    chkListLimpiezaCamiones.add(completo);
                }
                chkS.setCheckListLimpiezaCamionesCompletos( chkListLimpiezaCamiones );
            }

            if(chkC.size() > 0){
                chkS.setCheckListCosechas(chkC);
            }

            if(chk.size() > 0){
                chkS.setCheckListSiembras( chk );
            }
            prepararSubir( chkS );

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            executorService.shutdown();
        }
    }

    private void prepararSubir(CheckListRequest checkListRequest){

        InternetStateClass mm = new InternetStateClass(activity, result -> {
            if(!result){
                Toasty.error(activity, activity.getResources().getString(R.string.sync_not_internet), Toast.LENGTH_SHORT, true).show();
                return;
            }


            ProgressDialog pd = new ProgressDialog(activity);
            pd.setMessage("conectandose a internet, espere por favor");
            pd.show();


            if(pd.isShowing()){
                pd.dismiss();
            }

            new CheckListSync( checkListRequest, requireActivity(), (state, message) -> {
                if(state){
                    Toasty.success(requireActivity(), message, Toast.LENGTH_LONG, true).show();
                }else{
                    Toasty.error(requireActivity(), message, Toast.LENGTH_LONG, true).show();
                }
            });

        }, 1);
        mm.execute();
    }

    void prepararVisitas(){
        if(contenedor_botones == null){ return; }

        contenedor_botones.removeAllViews();

        btn_descargar.setEnabled(true);
        btn_preparar.setEnabled(true);

        List<Visitas> visitas = MainActivity.myAppDB.myDao().getVisitasPorSubir(); //2
        if(visitas.size() <= 0) {
            visitas_titulo.setVisibility(View.VISIBLE);
            visitas_titulo.setText("no hay visitas pendientes");
            visitas_marca.setVisibility(View.VISIBLE);
            visitas_marca.setText("");
            return;
        }

        Utilidades.exportDatabse(Utilidades.NOMBRE_DATABASE, activity.getPackageName());


        botonesSeleccionados = new ArrayList<>();
        idVisitasSeleccionadas = new ArrayList<>();

        visitas_titulo.setVisibility(View.VISIBLE);
        visitas_titulo.setText("Visitas pendientes por subir (ROJAS)");
        visitas_marca.setVisibility(View.VISIBLE);
        visitas_marca.setText("para subirla mantenga presionado el botón de cada visita.");


        ArrayList<Visitas> visitas1 = new ArrayList<>();
        int totalContadas = 0;
        int cantidadAMostrar = 2;
        for (final Visitas v2 : visitas) {
            visitas1.add(v2);
            if (visitas1.size() == cantidadAMostrar){
                LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setId(View.generateViewId());
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                for (final Visitas v : visitas1){

                    AnexoContrato anexoContrato = MainActivity.myAppDB.myDao().getAnexos(v.getId_anexo_visita());
                    List<Fotos> fotos = MainActivity.myAppDB.myDao().getFotosByIdVisita(v.getId_visita());

                    final Button button = new Button(getActivity());
                    button.setId(View.generateViewId());
                    button.setGravity(Gravity.CENTER);
                    button.setTag("VISITASPENDIENTES_"+v.getId_visita());

                    if(v.getEstado_server_visitas() == 0){
                        button.setBackgroundTintList(requireActivity().getResources().getColorStateList(R.color.colorRedLight));
                        button.setTextColor(requireActivity().getColor(R.color.colorSurface));
                    }

                    long peso = 0;

                    for(Fotos f : fotos){
                        File file = new File(f.getRuta());
                        if (file.exists()){
                            peso +=  (file.length() / 1024);
                        }
                    }

                    String texto = "";

                    texto += (anexoContrato != null) ? anexoContrato.getAnexo_contrato() : "";
                    texto += "\n" + fotos.size() + " FOTOS ";
                    texto += "CON " + (peso / 1024) + " MB  ";


                    button.setText(texto);

                    linearLayout.addView(button);

                    LinearLayout.LayoutParams propParam = new LinearLayout.LayoutParams( WRAP_CONTENT, WRAP_CONTENT );
                    propParam.setMargins(20, 10, 20,10);

                    button.setLayoutParams(propParam);


                    button.setOnClickListener(view -> Toasty.info(requireActivity(), "Funcionalidad deshabilitada, pmantenga presionado para subir", Toast.LENGTH_LONG, true).show());

                    button.setOnLongClickListener(view -> {

                        botonesSeleccionados = new ArrayList<>();
                        idVisitasSeleccionadas = new ArrayList<>();
                        button.setEnabled(false);
                        subirVisita(v, button);

                        return false;
                    });
                }
                contenedor_botones.addView(linearLayout);

                ViewGroup.LayoutParams propParam = linearLayout.getLayoutParams();
                propParam.height = WRAP_CONTENT;
                propParam.width = WRAP_CONTENT;
                linearLayout.setLayoutParams(propParam);
                totalContadas += cantidadAMostrar;
                visitas1.clear();
            }else if((visitas.size() - totalContadas) > 0 && (visitas.size() - totalContadas) < cantidadAMostrar){

                LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setId(View.generateViewId());
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                for (final Visitas v : visitas1){

                    AnexoContrato anexoContrato = MainActivity.myAppDB.myDao().getAnexos(v.getId_anexo_visita());
                    List<Fotos> fotos = MainActivity.myAppDB.myDao().getFotosByIdVisita(v.getId_visita());

                    final Button button = new Button(getActivity());
                    button.setId(View.generateViewId());
                    button.setGravity(Gravity.CENTER);
                    button.setTag("VISITASPENDIENTES_"+v.getId_visita());

                    if(v.getEstado_server_visitas() == 0){
                        button.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.colorRedLight));
                        button.setTextColor(getActivity().getColor(R.color.colorSurface));
                    }

                    long peso = 0;

                    for(Fotos f : fotos){
                        File file = new File(f.getRuta());
                        if (file.exists()){
                            peso +=  (file.length() / 1024);
                        }
                    }

                    String texto = "";

                    texto += (anexoContrato != null) ? anexoContrato.getAnexo_contrato() : "";
                    texto += "\n" + fotos.size() + " FOTOS ";
                    texto += "CON " + (peso / 1024) + " MB  ";


                    button.setText(texto);

                    linearLayout.addView(button);

                    LinearLayout.LayoutParams propParam = new LinearLayout.LayoutParams( WRAP_CONTENT, WRAP_CONTENT );
                    propParam.setMargins(20, 10, 20,10);


                    button.setLayoutParams(propParam);


                    button.setOnClickListener(view -> Toasty.info(requireActivity(), "Funcionalidad deshabilitada, mantenga presionado para subir", Toast.LENGTH_LONG, true).show());

                    button.setOnLongClickListener(view -> {
                        button.setEnabled(false);
                        subirVisita(v, button);
                        botonesSeleccionados = new ArrayList<>();
                        idVisitasSeleccionadas = new ArrayList<>();
                        return false;
                    });
                }
                contenedor_botones.addView(linearLayout);
                ViewGroup.LayoutParams propParam = linearLayout.getLayoutParams();
                propParam.height = WRAP_CONTENT;
                propParam.width = WRAP_CONTENT;
                linearLayout.setLayoutParams(propParam);
                totalContadas ++;
                visitas1.clear();
            }
        }
    }
    void subirVisita(final Visitas v, final Button button){
        if (progressDialogGeneral != null && !progressDialogGeneral.isShowing()){
            progressDialogGeneral.setTitle("Preparando visitas para subir...");
            progressDialogGeneral.setCancelable(false);
            progressDialogGeneral.show();
        }



        InternetStateClass mm = new InternetStateClass(activity, result -> {

            if(!result){
                if(button != null) button.setEnabled(true);
                if (progressDialogGeneral.isShowing()) progressDialogGeneral.dismiss();
                Toasty.error(activity, activity.getResources().getString(R.string.sync_not_internet), Toast.LENGTH_SHORT, true).show();
                return;
            }

            if(button != null) button.setEnabled(true);
            MainActivity.myAppDB.myDao().updateVisitasSubidasTomadasBack();
            MainActivity.myAppDB.myDao().updateDetalleSubidasTomadasBack();
            MainActivity.myAppDB.myDao().updateFotosSubidasTomadasBack();


            int cantidadSuma = 0;
            ArrayList<Visitas> visitas = new ArrayList<>();

            List<detalle_visita_prop> detalles = MainActivity.myAppDB.myDao().getDetallesPorSubirLimit(v.getId_visita());
            List<Fotos> fotos = MainActivity.myAppDB.myDao().getFotosLimit(v.getId_visita());


            visitas.add(v);
            MainActivity.myAppDB.myDao().marcarVisitas(v.getId_visita());
            MainActivity.myAppDB.myDao().marcarDetalle(v.getId_visita());
            MainActivity.myAppDB.myDao().marcarFotos(v.getId_visita());
            MainActivity.myAppDB.DaoEvaluaciones().marcarEvaluaciones(Integer.parseInt(v.getId_anexo_visita()));

            cantidadSuma+= v.getId_visita();
            cantidadSuma+= 1;


            List<Fotos> fts = new ArrayList<>();
            if (fotos.size() > 0){
                for (Fotos fs : fotos){
                    String imageString  = Utilidades.imageToString(fs.getRuta());
                    if(imageString.length() > 0){
                        fs.setEncrypted_image(imageString);
                        fts.add(fs);
                    }
                }
            }


            if (detalles.size() > 0){
                for (detalle_visita_prop v1 : detalles){
                    cantidadSuma+= v1.getId_det_vis_prop_detalle();
                }
            }
            cantidadSuma+= detalles.size();


            if (fts.size() > 0){
                for (Fotos v1 : fts){
                    cantidadSuma+=  v1.getId_foto();
                }
            }
            cantidadSuma = cantidadSuma + fts.size();


            Config config = MainActivity.myAppDB.myDao().getConfig();
            SubidaDatos list = new SubidaDatos();

            list.setVisitasList(visitas);
            list.setDetalle_visita_props(detalles);
            list.setFotosList(fts);
            list.setId_dispo(config.getId());
            list.setId_usuario(config.getId_usuario());
            list.setCantidadSuma(cantidadSuma);
            list.setVersion(Utilidades.APPLICATION_VERSION);

            subidaDatosVisita(list, v.getId_visita());

        }, 1);
        mm.execute();
    }
    public void subidaDatosVisita(SubidaDatos subidaDatos, final int id_visita){
        if(progressDialogGeneral.isShowing()) progressDialogGeneral.setTitle("subiendo visita");

        Config config = MainActivity.myAppDB.myDao().getConfig();
        ApiService apiService = RetrofitClient.getClient(config.getServidorSeleccionado()).create(ApiService.class);
        Call<Respuesta> call = apiService.enviarDatos(subidaDatos);

        call.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(@NonNull Call<Respuesta> call, @NonNull Response<Respuesta> response) {
                if (response.isSuccessful()){
                    switch (response.code()){
                        case 200:
                            Respuesta resSubidaDatos = response.body();
                            if(resSubidaDatos == null){
                                if (progressDialogGeneral.isShowing()) progressDialogGeneral.dismiss();
                                Utilidades.avisoListo(getActivity(),"ATENCION", "CUERPO DE RESPUESTA VACIO \nCODIGO:  "+response.code()+"\nMENSAJE: \n"+response.message(), "ENTIENDO");
                                break;
                            }
                            switch (resSubidaDatos.getCodigoRespuesta()){
                                case 0:
                                    Toasty.info(activity, "pasando a segunda respuesta", Toast.LENGTH_SHORT, true).show();
                                    segundaRespuestaVisita(resSubidaDatos.getCabeceraRespuesta(), id_visita);
                                    break;
                                case 5:
                                    Utilidades.avisoListo(activity, "ATENCION", "NO POSEES LA ULTIMA VERSION DE LA APLICACION ", "entiendo");
                                    if(progressDialogGeneral.isShowing()) progressDialogGeneral.dismiss();
                                    break;
                                default:
                                    if (progressDialogGeneral.isShowing()) progressDialogGeneral.dismiss();
                                    Utilidades.avisoListo(getActivity(),"ATENCION", "PROBLEMAS SUBIENDO DATOS \nCODIGO:  "+resSubidaDatos.getCodigoRespuesta()+"\nMENSAJE: \n"+resSubidaDatos.getMensajeRespuesta(), "ENTIENDO");
                                    break;
                            }
                            break;
                        default:
                            if (progressDialogGeneral.isShowing()) progressDialogGeneral.dismiss();
                            Utilidades.avisoListo(getActivity(),"ATENCION", "PROBLEMAS CON SERVIDOR \nCODIGO:  "+response.code()+"\nMENSAJE: \n"+response.message(), "ENTIENDO");
                            break;
                    }
                }else{
                    if (progressDialogGeneral.isShowing()) progressDialogGeneral.dismiss();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Utilidades.avisoListo(getActivity(),"ATENCION", "COMUNICACION FALLIDA \nCODIGO:  "+response.code()+"\nMENSAJE: \n"+jObjError.getJSONObject("error").getString("message"), "ENTIENDO");

                    } catch (Exception e) {
                        Utilidades.avisoListo(getActivity(),"ATENCION", "COMUNICACION FALLIDA \nCODIGO:  "+response.code()+"\nMENSAJE: \n"+e.getMessage(), "ENTIENDO");
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<Respuesta> call, @NonNull Throwable t) {
                if (progressDialogGeneral.isShowing()) progressDialogGeneral.dismiss();
                Utilidades.avisoListo(getActivity(),"ATENCION", "PROBLEMA EN LA COMUNICACION \nMENSAJE: \n"+t.getMessage(), "ENTIENDO");
            }
        });

    }
    private void segundaRespuestaVisita(int cab, final int id_visita){
        if(progressDialogGeneral.isShowing()){
            progressDialogGeneral.setTitle("esperando confirmacion de la visita ...");
        }

        final int[] respuesta = {0,0};
        Config config = MainActivity.myAppDB.myDao().getConfig();
        ApiService apiService = RetrofitClient.getClient(config.getServidorSeleccionado()).create(ApiService.class);
        Call<Respuesta> callResponse = apiService.comprobacion(config.getId(), config.getId_usuario(), cab);
        callResponse.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(@NonNull Call<Respuesta> callResponse, @NonNull Response<Respuesta> response) {
                if (response.isSuccessful()){
                    switch (response.code()){
                        case 200:
                            Respuesta re = response.body();
                            if( re == null ){
                                /* respuesta nula */
                                if(progressDialogGeneral.isShowing()) progressDialogGeneral.dismiss();
                                Toasty.error(activity, "Problema conectandonos al servidor, por favor vuelva a intentarlo ", Toast.LENGTH_SHORT, true).show();
                                break;
                            }
                            switch (re.getCodigoRespuesta()) {
                                case 0: //salio bien se sigue con el procedimiento

                                    int visita = MainActivity.myAppDB.myDao().updateVisitasSubidasTomadas(re.getCabeceraRespuesta()); /* las actualizo y las dejo en tomadas = 0 */
                                    if (visita <= 0) {
                                        respuesta[0] = 2;
                                        respuesta[1] = re.getCabeceraRespuesta();
                                    }
                                    int detalles = MainActivity.myAppDB.myDao().updateDetalleVisitaSubidasTomadas(re.getCabeceraRespuesta());
                                    int fotos = MainActivity.myAppDB.myDao().updateFotosSubidasTomada(re.getCabeceraRespuesta());

                                    if (respuesta[0] == 2) {

                                        MainActivity.myAppDB.myDao().updateDetalleVisitaBack(re.getCabeceraRespuesta());
                                        MainActivity.myAppDB.myDao().updateFotosBack(re.getCabeceraRespuesta());
                                        MainActivity.myAppDB.myDao().updateVisitasBack(re.getCabeceraRespuesta());

                                        Utilidades.avisoListo(getActivity(),"ATENCION", "PROBLEMAS SUBIENDO DATOS \nCODIGO:  "+re.getCodigoRespuesta()+"\nMENSAJE: \n"+re.getMensajeRespuesta(), "ENTIENDO");

                                        Toasty.success(activity, "Problema subiendo los datos , por favor, vuelva a intentarlo", Toast.LENGTH_SHORT, true).show();
                                        if(progressDialogGeneral.isShowing()) progressDialogGeneral.dismiss();
                                    }else {

                                        Button button = view.findViewWithTag("VISITASPENDIENTES_"+id_visita);
                                        if (button != null){
                                            button.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.colorGreenLight));
                                            button.setEnabled(false);
                                        }
                                        contadorVisita++;
                                        if(idVisitasSeleccionadas.size() > 0 && contadorVisita < 3){
                                            prepararVisitaAgrupada(contadorVisita);
                                        }else{
                                                btn_sube_marcadas.setVisibility(View.INVISIBLE);
                                            if (progressDialogGeneral.isShowing()) progressDialogGeneral.dismiss();
                                        }

                                        preparaSubirRecomendaciones();
                                        Toasty.success(activity, "Se subio La visita con exito", Toast.LENGTH_SHORT, true).show();
                                    }
                                    break;

                                default:
                                    if(progressDialogGeneral.isShowing()) progressDialogGeneral.dismiss();
                                    Utilidades.avisoListo(getActivity(),"ATENCION", "PROBLEMAS SUBIENDO DATOS \nCODIGO:  "+re.getCodigoRespuesta()+"\nMENSAJE: \n"+re.getMensajeRespuesta(), "ENTIENDO");
                                    break;
                            }
                            break;
                        default:
                            if (progressDialogGeneral.isShowing()) progressDialogGeneral.dismiss();
                            Utilidades.avisoListo(getActivity(),"ATENCION", "PROBLEMAS CON SERVIDOR \nCODIGO:  "+response.code()+"\nMENSAJE: \n"+response.message(), "ENTIENDO");
                            break;
                    }
                }else{
                    if (progressDialogGeneral.isShowing()) progressDialogGeneral.dismiss();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Utilidades.avisoListo(getActivity(),"ATENCION", "COMUNICACION FALLIDA EN COMPROBACION \nCODIGO:  "+response.code()+"\nMENSAJE: \n"+jObjError.getJSONObject("error").getString("message"), "ENTIENDO");

                    } catch (Exception e) {
                        Utilidades.avisoListo(getActivity(),"ATENCION", "COMUNICACION FALLIDA EN COMPROBACION \nCODIGO:  "+response.code()+"\nMENSAJE: \n"+e.getMessage(), "ENTIENDO");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Respuesta> call, @NonNull Throwable t) {
                if (progressDialogGeneral.isShowing()) progressDialogGeneral.dismiss();
                Utilidades.avisoListo(getActivity(),"ATENCION", "PROBLEMA EN LA COMUNICACION \nMENSAJE: \n"+t.getMessage(), "ENTIENDO");
            }
        });
    }



    /* FIN SUBIR PROSPECTOS */


    void descargando(){
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        if (getView() != null){
            progressDialog.setTitle("Espere un momento...");
            progressDialog.setMessage("descargando datos...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        Config cnf = MainActivity.myAppDB.myDao().getConfig();
        String vv = Utilidades.APPLICATION_VERSION;
        ApiService apiService = RetrofitClient.getClient(cnf.getServidorSeleccionado()).create(ApiService.class);
        Call<GsonDescargas> call = apiService.descargarDatos(cnf.getId(), cnf.getId_usuario_suplandato(), vv, id_temporadas.get(spinner_toolbar.getSelectedItemPosition()));
        call.enqueue(new Callback<GsonDescargas>() {
            @Override
            public void onResponse(@NonNull Call<GsonDescargas> call, @NonNull Response<GsonDescargas> response) {
                GsonDescargas gsonDescargas = response.body();

                if( gsonDescargas == null) {
                    Utilidades.avisoListo(activity, "ERROR SINCRONIZACION", "respuesta nula", "aceptar");
                    return;
                }

                if(gsonDescargas.getRespuestas() != null && gsonDescargas.getRespuestas().size() > 0){
                    for(Respuesta rsp : gsonDescargas.getRespuestas()){
                        switch(rsp.getCodigoRespuesta()){
                            case 5:
                                    activity.cambiarFragment(new FragmentLogin(), Utilidades.FRAGMENT_INICIO, R.anim.slide_in_left, R.anim.slide_out_left);
                                    return;
                            case 2:
                                Utilidades.avisoListo(activity, "ERROR SINCRONIZACION", rsp.getMensajeRespuesta()
                                        +" por favor vuelva a intentarlo, si el problema persiste contacte con un administrador", "aceptar");
                                return;
                        }
                    }
                }

                progressDialog.setMessage("insertando datos...");
                ExecutorService ex = Executors.newSingleThreadExecutor();
                ex.execute(()->{
                    Future<boolean[]> futureProblem = ex.submit(() -> volqueoDatos(gsonDescargas, getActivity()));
                    Future<Config> futureConfig = ex.submit(() -> MainActivity.myAppDB.myDao().getConfig());
                        new Thread(() -> {
                            try {
                                final boolean[] problema = futureProblem.get();
                                Future<List<Temporada>> futureTempo = ex.submit(()->MainActivity.myAppDB.myDao().getTemporada());

                                Config config = futureConfig.get();
                                temporadaList = futureTempo.get();
                                setSpecialSeason(temporadaList);

                                List<AnexoVilab> vilabList = ex.submit(() -> MainActivity.myAppDB.DaoVilab().getVilab()).get();
                                activity.runOnUiThread(() -> {
                                        DescargaImagenes descargaImagenes  =  DescargaImagenes.getInstance(activity);
                                        ImageLoader imageLoader = new ImageLoader(descargaImagenes.getRequestQueue(), new ImageLoader.ImageCache() {
                                            @Nullable
                                            @Override
                                            public Bitmap getBitmap(String url) {
                                                return null;
                                            }

                                            @Override
                                            public void putBitmap(String url, Bitmap bitmap) { }
                                        });

                                        totalQueue = vilabList.size();
                                        imagenesDescargadas = new ArrayList<>();
                                        int current = 1;
                                        for (AnexoVilab avilab : vilabList){
                                            downloadImage(avilab, imageLoader, descargaImagenes, 0, current);
                                            current++;
                                        }


                                    if (!problema[0] && !problema[1]){
                                        cargarToolbar();
                                        revisarAnexosPendienteFecha();
                                        if (config != null){
                                            activity.cambiarNombreUser(config.getId_usuario());
                                        }
                                        progressDialog.dismiss();
                                        Toasty.info(activity, "Todo descargado con exito", Toast.LENGTH_SHORT, true).show();
                                        ex.shutdown();
                                    }else{
                                        Errores errores = new Errores();
                                        errores.setCodigo_error(66);
                                        errores.setMensaje_error("positivo en true al descargar home");
                                        MainActivity.myAppDB.myDao().setErrores(errores);
                                        progressDialog.dismiss();
                                        Toasty.error(activity, "No se pudo descargar todo "+response.errorBody(), Toast.LENGTH_SHORT, true).show();
                                        ex.shutdown();
                                    }
                                });
                            }  catch (ExecutionException | InterruptedException e) {
                                ex.shutdown();
                                progressDialog.dismiss();
                            }
                        }).start();

                });


            }

            @Override
            public void onFailure(@NonNull Call<GsonDescargas> call, @NonNull Throwable t) {
                Errores errores = new Errores();
                errores.setCodigo_error(66);
                errores.setMensaje_error(t.toString());
                MainActivity.myAppDB.myDao().setErrores(errores);

                Toasty.error(activity, "No se pudo descargar todo front "+t.getLocalizedMessage(), Toast.LENGTH_SHORT, true).show();
                if (getView() != null) {
                    progressDialog.dismiss();
                }
            }
        });
    }


    private void downloadImage(AnexoVilab avilab, ImageLoader imageLoader, DescargaImagenes descargaImagenes, int intento, int current){

            String imgUrl = Utilidades.URL_SERVER_API+"/../curimapu_docum/image_geos/"+avilab.nombre_imagen;

            if(vilabProgressDialog != null && !vilabProgressDialog.isShowing() && current < totalQueue){
                vilabProgressDialog.setTitle("Falta poco");
                vilabProgressDialog.setMessage("descargando imagenes de vilab");
                vilabProgressDialog.setMax(totalQueue);
                vilabProgressDialog.show();
            }

            imageLoader.get(imgUrl, new ImageLoader.ImageListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("DESCARGA", currentQueue+" == "+totalQueue);
                    if(currentQueue >= totalQueue || current == totalQueue && vilabProgressDialog.isShowing()){
                        vilabProgressDialog.dismiss();
                    }
                    if(imagenesDescargadas.contains(avilab.id_vilab) || intento > 3) return;
                    startRetryDelay(avilab, imageLoader, descargaImagenes, intento + 1, current);
                }

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    Bitmap bitmap = response.getBitmap();

                    Log.i("DESCARGA QUEUE", currentQueue+" == "+totalQueue);
                    if(currentQueue >= totalQueue && vilabProgressDialog.isShowing()){
                        vilabProgressDialog.dismiss();
                    }

                    if(imagenesDescargadas.contains(avilab.id_vilab)) return;
                    if(bitmap == null) {
                        if(intento > 3) return;
                        startRetryDelay(avilab, imageLoader, descargaImagenes, intento + 1, current);
                        return;
                    }


                    vilabProgressDialog.setProgress(currentQueue);
                    descargaImagenes.saveImageToDisk(bitmap, avilab.nombre_imagen);
                    imagenesDescargadas.add(avilab.id_vilab);

                    currentQueue++;

                    Log.i("DESCARGA CURRENT", current+" == "+totalQueue);
                    if(current >= totalQueue && vilabProgressDialog.isShowing()){
                        vilabProgressDialog.dismiss();
                    }
                    cancelRetry();
                }
            });
    }

    private void startRetryDelay(AnexoVilab avilab, ImageLoader imageLoader, DescargaImagenes descargaImagenes, int intento, int current) {
        // Si ya existe un retardo de reintentar, no es necesario crear uno nuevo
        if (retryRunnable != null) {
            return;
        }
        retryRunnable = () -> downloadImage(avilab, imageLoader, descargaImagenes, intento, current);

        handler.postDelayed(retryRunnable, retryDelay);
    }

    private void cancelRetry() {
        // Cancelar el retardo de reintentar si existe
        if (retryRunnable != null) {
            handler.removeCallbacks(retryRunnable);
            retryRunnable = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        cancelRetry();
        DescargaImagenes descargaImagenes  =  DescargaImagenes.getInstance(activity);
        descargaImagenes.cancelQueue();

    }

    @Override
    public void onResume() {
        super.onResume();
        sitiosNoVisitados((id_temporadas.size() > 0) ? Integer.parseInt(id_temporadas.get(spinner_toolbar.getSelectedItemPosition())) : 0);
        primeraPrioridad((id_temporadas.size() > 0) ? Integer.parseInt(id_temporadas.get(spinner_toolbar.getSelectedItemPosition())) : 0);
    }


    public void procesarInfoNoVisitados (int tempo) {

        ApplicationExecutors exec = new ApplicationExecutors();
        exec.getBackground().execute(()->{
            ArrayList<VisitasCompletas> listas = new ArrayList<>();

            List<SitiosNoVisitadosAnexos> listAnexos = MainActivity.myAppDB.myDao().getSitiosNoVisitados(tempo);
            if(listAnexos.size() > 0 ){
                for(SitiosNoVisitadosAnexos si : listAnexos){
                    ArrayList<Integer> anexoPasando = new ArrayList<>();
                    ArrayList<String> fechaAnexo = new ArrayList<>();

                    ArrayList<Integer> anexoParaFechaHarvest = new ArrayList<>();
                    ArrayList<String> fechaHarvest = new ArrayList<>();

                    boolean anexo = false;
                    boolean anexo1 = false;
                    int  idAc = Integer.parseInt(si.getAnexoContrato().getId_anexo_contrato());
                    List<VisitaDetalle> getVisitaDetalle = MainActivity.myAppDB.myDao().getVisitaDetalle(idAc);
                    if (getVisitaDetalle.size() > 0){
                        anexo = true;
                        for (VisitaDetalle vd : getVisitaDetalle){
                            if (
                                    (vd.getPro_cli_mat() != null && vd.getPro_cli_mat().getMarca_sitios_no_visitados() == 1) &&
                                            (vd.getDetalle_visita_prop() != null && vd.getDetalle_visita_prop().getValor_detalle().equals(""))
                            ) {
                                anexoParaFechaHarvest.add(Integer.parseInt(vd.visitas.getId_anexo_visita()));
                                fechaHarvest.add(vd.getDetalle_visita_prop().getValor_detalle());
                            }
                        }
                    }
                    if(anexo){
                        List<Visitas> laVisita = MainActivity.myAppDB.myDao().traeVisitaPorAnexo(idAc);

                        if (laVisita.size() > 0 ){

                            for (Visitas nel : laVisita){
                                anexo1 = true;
                                anexoPasando.add(Integer.parseInt(nel.getId_anexo_visita()));
                                if(fechaHarvest.size() >= 0 ){
                                    fechaAnexo.add(nel.getFecha_visita());
                                }
                            }
                        }

                        if(anexoPasando.size() > 0 && fechaAnexo.size() > 0){

                            if((anexo1 && Utilidades.compararFechas(fechaAnexo.get(anexoPasando.indexOf(idAc))) >= 7)){
                                VisitasCompletas vis1 = new VisitasCompletas();

                                AnexoCompleto ac = new AnexoCompleto();
                                ac.setAnexoContrato(si.getAnexoContrato());
                                ac.setEspecie(si.getEspecie());
                                ac.setLotes(si.getLotes());


                                vis1.setAnexoCompleto(ac);

                                Visitas v = new Visitas();
                                v.setFecha_visita(String.valueOf(Utilidades.compararFechas(fechaAnexo.get(anexoPasando.indexOf(idAc)))));

                                vis1.setVisitas(v);

                                listas.add(vis1);

                            }
                        }
                    }
                }
            }

            exec.getMainThread().execute(()-> mostrarTablaNoVis(listas));
        });

        exec.shutDownBackground();


    }

    public void procesarInfoPrimera(int tempo){
        ApplicationExecutors exec = new ApplicationExecutors();

        exec.getBackground().execute(()->{
            ArrayList<VisitasCompletas> listas = new ArrayList<>();

            List<VisitasCompletas> listAnexos = MainActivity.myAppDB.myDao().getPrimeraPrioridad(tempo);
            if(listAnexos.size() > 0 ){

                ArrayList<Integer> anexoPasando = new ArrayList<>();
                for(VisitasCompletas si : listAnexos){


                    int  idAc = Integer.parseInt(si.getAnexoCompleto().getAnexoContrato().getId_anexo_contrato());

                    Visitas vv =    si.getVisitas();

                    if(vv.getHarvest_visita() == null ||
                            vv.getGrowth_status_visita() == null ||
                            vv.getWeed_state_visita() == null ||
                            vv.getPhytosanitary_state_visita() == null ||
                            vv.getOverall_status_visita() == null ||
                            vv.getHumidity_floor_visita() == null
                    ){
                        continue;
                    }

                    if(!anexoPasando.contains(idAc)){
                        if(
                                (
                                        (vv.getHarvest_visita().equals("Rechazada") || vv.getHarvest_visita().equals("REJECTED")) ||
                                                (vv.getGrowth_status_visita().equals("MALA") || vv.getGrowth_status_visita().equals("BAD")) ||
                                                (vv.getWeed_state_visita().equals("ALTA") || vv.getWeed_state_visita().equals("HIGH")) ||
                                                (vv.getPhytosanitary_state_visita().equals("MALA") || vv.getPhytosanitary_state_visita().equals("BAD")) ||
                                                (vv.getOverall_status_visita().equals("MALA") || vv.getOverall_status_visita().equals("BAD")) ||
                                                (vv.getHumidity_floor_visita().equals("MALA") || vv.getHumidity_floor_visita().equals("BAD"))
                                )
                                        ||
                                        (

                                                (
                                                        vv.getGrowth_status_visita().equals("REGULAR") && vv.getWeed_state_visita().equals("REGULAR") ||
                                                                vv.getGrowth_status_visita().equals("REGULAR") && vv.getPhytosanitary_state_visita().equals("REGULAR") ||
                                                                vv.getGrowth_status_visita().equals("REGULAR") && vv.getOverall_status_visita().equals("REGULAR") ||
                                                                vv.getGrowth_status_visita().equals("REGULAR") && vv.getHumidity_floor_visita().equals("REGULAR")

                                                )
                                                        ||
                                                        (
                                                                vv.getWeed_state_visita().equals("REGULAR") && vv.getPhytosanitary_state_visita().equals("REGULAR") ||
                                                                        vv.getWeed_state_visita().equals("REGULAR") && vv.getOverall_status_visita().equals("REGULAR") ||
                                                                        vv.getWeed_state_visita().equals("REGULAR") && vv.getHumidity_floor_visita().equals("REGULAR")

                                                        )
                                                        ||
                                                        (
                                                                vv.getPhytosanitary_state_visita().equals("REGULAR") && vv.getOverall_status_visita().equals("REGULAR") ||
                                                                        vv.getPhytosanitary_state_visita().equals("REGULAR") && vv.getHumidity_floor_visita().equals("REGULAR")
                                                        )
                                                        ||
                                                        (

                                                                vv.getOverall_status_visita().equals("REGULAR") && vv.getHumidity_floor_visita().equals("REGULAR")
                                                        )


                                        )

                        ){

                            AnexoCompleto ac = si.getAnexoCompleto();

                            Visitas v = si.getVisitas();

                            VisitasCompletas comp = new VisitasCompletas();

                            comp.setVisitas(v);
                            comp.setAnexoCompleto(ac);

                            listas.add(comp);

                        }

                        anexoPasando.add(idAc);
                    }

                }
            }

            exec.getMainThread().execute(()-> mostrarTablaPrimera(listas));
        });
        exec.shutDownBackground();
    }


    void primeraPrioridad(int tempo){
        progressBar1.setVisibility(View.VISIBLE);
        procesarInfoPrimera(tempo);
    }


    public void mostrarTablaPrimera(ArrayList<VisitasCompletas> visitasCompletas) {


        titulo_primera_prioridad.setText("Primera prioridad");
        progressBar1.setVisibility(View.GONE);

        adapterPrimera = new PrimeraPrioridadAdapter(visitasCompletas, activity);

        lista_primera_prioridad.setLayoutManager(new GridLayoutManager(activity, 2));
        lista_primera_prioridad.setHasFixedSize(true);
        lista_primera_prioridad.setAdapter(adapterPrimera);

    }


    public void mostrarTablaNoVis(ArrayList<VisitasCompletas> visitasCompletas) {


        titulo_sitios_no_visitados.setText("Sitios no visitados por mas de 7 dias");


        progressBar2.setVisibility(View.GONE);

        adapterNovis = new SitiosNoVisitadosAdapter(visitasCompletas);

        lista_sitios_no_visitados.setLayoutManager(new GridLayoutManager(activity, 2));
        lista_sitios_no_visitados.setHasFixedSize(true);

        lista_sitios_no_visitados.setAdapter(adapterNovis);

    }


    void sitiosNoVisitados(int tempo){

        progressBar2.setVisibility(View.VISIBLE);
        procesarInfoNoVisitados(tempo);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        if (activity != null){
            activity.setDrawerEnabled(true);
            activity.updateView(getResources().getString(R.string.app_name), getResources().getString(R.string.subtitles_start));

            Config config = MainActivity.myAppDB.myDao().getConfig();
            activity.cambiarNombreUser(config.getId_usuario());
        }
    }
}
