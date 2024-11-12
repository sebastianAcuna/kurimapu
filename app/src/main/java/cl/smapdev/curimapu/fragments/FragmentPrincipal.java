package cl.smapdev.curimapu.fragments;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static cl.smapdev.curimapu.clases.utilidades.Descargas.volqueoDatos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
import cl.smapdev.curimapu.clases.modelo.RecomendacionesSync;
import cl.smapdev.curimapu.clases.relaciones.CheckListRequest;
import cl.smapdev.curimapu.clases.relaciones.GsonDescargas;
import cl.smapdev.curimapu.clases.relaciones.RecomendacionesRequest;
import cl.smapdev.curimapu.clases.relaciones.Respuesta;
import cl.smapdev.curimapu.clases.relaciones.SubidaDatos;
import cl.smapdev.curimapu.clases.retrofit.ApiService;
import cl.smapdev.curimapu.clases.retrofit.RetrofitClient;
import cl.smapdev.curimapu.clases.tablas.AnexoContrato;
import cl.smapdev.curimapu.clases.tablas.CheckListAplicacionHormonas;
import cl.smapdev.curimapu.clases.tablas.CheckListSiembra;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.CropRotation;
import cl.smapdev.curimapu.clases.tablas.Errores;
import cl.smapdev.curimapu.clases.tablas.Evaluaciones;
import cl.smapdev.curimapu.clases.tablas.Fichas;
import cl.smapdev.curimapu.clases.tablas.Fotos;
import cl.smapdev.curimapu.clases.tablas.FotosFichas;
import cl.smapdev.curimapu.clases.tablas.PrimeraPrioridad;
import cl.smapdev.curimapu.clases.tablas.SitiosNoVisitados;
import cl.smapdev.curimapu.clases.tablas.Temporada;
import cl.smapdev.curimapu.clases.tablas.Visitas;
import cl.smapdev.curimapu.clases.tablas.detalle_visita_prop;
import cl.smapdev.curimapu.clases.utilidades.InternetStateClass;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentPrincipal extends Fragment {

    private MainActivity activity;

    private RecyclerView lista_sitios_no_visitados;
    private RecyclerView lista_primera_prioridad;


    private Handler handlerGrafico;

    private ArrayList<String> id_temporadas = new ArrayList<>();
    private ArrayList<String> desc_temporadas = new ArrayList<>();


    private LinearLayout contenedor_botones;


    private Button btn_descargar;
    private Button btn_preparar;
    private Button btn_sube_marcadas;

    private TextView visitas_titulo, visitas_marca;


    private List<Temporada> temporadaList;

    private SharedPreferences prefs;

    private Spinner spinner_toolbar;

    private String marca_especial_temporada;
    private ProgressDialog progressDialogGeneral;

    private ProgressBar progressBar1, progressBar2;

    int contadorVisita = 0;


    private TextView titulo_sitios_no_visitados;
    private TextView titulo_primera_prioridad;

    ArrayList<Integer> botonesSeleccionados = new ArrayList<>();
    ArrayList<Integer> idVisitasSeleccionadas = new ArrayList<>();

    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();

        if (activity != null) {
            prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
            progressDialogGeneral = new ProgressDialog(activity);
        }

        handlerGrafico = new Handler(Looper.getMainLooper());

        temporadaList = MainActivity.myAppDB.myDao().getTemporada();
        if (!temporadaList.isEmpty()) {
            for (Temporada t : temporadaList) {
                id_temporadas.add(t.getId_tempo_tempo());
                desc_temporadas.add(t.getNombre_tempo());

                if (t.getEspecial_temporada() > 0) {
                    marca_especial_temporada = t.getId_tempo_tempo();
                }
            }
            setSpecialSeason(temporadaList);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inicio, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);


        spinner_toolbar = (Spinner) view.findViewById(R.id.spinner_toolbar);

        btn_descargar = (Button) view.findViewById(R.id.btn_descargar);
        btn_preparar = (Button) view.findViewById(R.id.btn_preparar);
        btn_sube_marcadas = (Button) view.findViewById(R.id.btn_sube_marcadas);

        visitas_titulo = (TextView) view.findViewById(R.id.visitas_titulo);
        visitas_marca = (TextView) view.findViewById(R.id.visitas_marca);
        titulo_sitios_no_visitados = (TextView) view.findViewById(R.id.titulo_sitios_no_visitados);
        titulo_primera_prioridad = (TextView) view.findViewById(R.id.titulo_primera_prioridad);

        progressBar1 = (ProgressBar) view.findViewById(R.id.progressBar1);
        progressBar2 = (ProgressBar) view.findViewById(R.id.progressBar2);


        contenedor_botones = (LinearLayout) view.findViewById(R.id.contenedor_botones);


        lista_sitios_no_visitados = (RecyclerView) view.findViewById(R.id.lista_sitios_no_visitados);
        lista_primera_prioridad = (RecyclerView) view.findViewById(R.id.lista_primera_prioridad);

        Button btn_calcula_datos_primera_prio = view.findViewById(R.id.btn_calcula_datos_primera_prio);
        Button btn_calcula_datos_sitio_visita = view.findViewById(R.id.btn_calcula_datos_sitio_visita);
        Button btn_subir_check = view.findViewById(R.id.btn_subir_check);


        btn_subir_check.setOnClickListener(view1 -> preparaSubirChecklist());


        cargarToolbar();

        recargarYear();
        spinner_toolbar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                prefs.edit().putString(Utilidades.SELECTED_ANO, id_temporadas.get(spinner_toolbar.getSelectedItemPosition())).apply();
                prefs.edit().putInt(Utilidades.SHARED_FILTER_FICHAS_YEAR, i).apply();

                sitiosNoVisitados(Integer.parseInt(id_temporadas.get(spinner_toolbar.getSelectedItemPosition())));
                primeraPrioridad(Integer.parseInt(id_temporadas.get(spinner_toolbar.getSelectedItemPosition())));


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btn_calcula_datos_primera_prio.setOnClickListener((view1) -> primeraPrioridad(Integer.parseInt(id_temporadas.get(spinner_toolbar.getSelectedItemPosition()))));
        btn_calcula_datos_sitio_visita.setOnClickListener((view2) -> sitiosNoVisitados(Integer.parseInt(id_temporadas.get(spinner_toolbar.getSelectedItemPosition()))));


        btn_descargar.setOnClickListener(view1 -> {

            btn_descargar.setEnabled(false);
            btn_preparar.setEnabled(false);
            List<Visitas> visitas = MainActivity.myAppDB.myDao().getVisitasPorSubir();
            List<detalle_visita_prop> detalles = MainActivity.myAppDB.myDao().getDetallesPorSubir();
            List<Fotos> fotos = MainActivity.myAppDB.myDao().getFotos();
            List<Fichas> fichas = MainActivity.myAppDB.myDao().getFichasPorSubir();
            List<FotosFichas> fotosFichas = MainActivity.myAppDB.myDao().getFotosFichasPorSubir();
            List<CropRotation> crops = MainActivity.myAppDB.myDao().getCropsPorSubir();

            if (visitas.isEmpty() && detalles.isEmpty() && fotos.isEmpty() && fichas.isEmpty() && fotosFichas.isEmpty() && crops.isEmpty()) {
                InternetStateClass mm = new InternetStateClass(activity, result -> {
                    if (result) {
                        btn_descargar.setEnabled(true);
                        btn_preparar.setEnabled(true);
                        descargando();
                    }
                }, 1);
                mm.execute();
            } else {
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
            if (!botonesSeleccionados.isEmpty()) {
                contadorVisita = 0;
                prepararVisitaAgrupada(contadorVisita);
            }
        });

        btn_preparar.setOnClickListener(view13 -> {
            btn_descargar.setEnabled(false);
            btn_preparar.setEnabled(false);
            prepararVisitas();
        });


    }


    public void preparaSubirChecklist() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<CheckListSiembra>> chkF = executorService.submit(()
                -> MainActivity.myAppDB.DaoClSiembra()
                .getClSiembraToSync());

        Future<List<CheckListAplicacionHormonas>> chkApHor = executorService.submit(() ->
                MainActivity.myAppDB.DaoCLAplicacionHormonas().getClApHormonasToSync());

        try {

            List<CheckListSiembra> chk = chkF.get();
            List<CheckListAplicacionHormonas> chkA = chkApHor.get();


            if (chk.isEmpty() && chkA.isEmpty()) {
                executorService.shutdown();
                Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                return;
            }

            CheckListRequest chkS = new CheckListRequest();

            if (!chkA.isEmpty()) {
                chkS.setCheckListAplicacionHormonas(chkA);
            }

            if (!chk.isEmpty()) {
                chkS.setCheckListSiembras(chk);
            }
            prepararSubir(chkS);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            executorService.shutdown();
        }
    }

    private void prepararSubir(CheckListRequest checkListRequest) {

        InternetStateClass mm = new InternetStateClass(activity, result -> {
            if (!result) {
                Toasty.error(activity, activity.getResources().getString(R.string.sync_not_internet), Toast.LENGTH_SHORT, true).show();
                return;
            }


            ProgressDialog pd = new ProgressDialog(activity);
            pd.setMessage("conectandose a internet, espere por favor");
            pd.show();


            if (pd.isShowing()) {
                pd.dismiss();
            }

            new CheckListSync(checkListRequest, requireActivity(), (state, message) -> {
                if (state) {
                    Toasty.success(requireActivity(), message, Toast.LENGTH_LONG, true).show();
                } else {
                    Toasty.error(requireActivity(), message, Toast.LENGTH_LONG, true).show();
                }
            });

        }, 1);
        mm.execute();
    }


    void prepararVisitaAgrupada(int contadorVisitas) {
        Visitas v = MainActivity.myAppDB.myDao().getVisitas(idVisitasSeleccionadas.get(contadorVisitas));
        subirVisita(v, null);
    }

    void cargarToolbar() {
        spinner_toolbar.setAdapter(new SpinnerToolbarAdapter(activity, R.layout.spinner_template_toolbar_view, temporadaList));
    }


    private void recargarYear() {
        if (temporadaList.size() > 0) {
            spinner_toolbar.setSelection((marca_especial_temporada.isEmpty()) ? prefs.getInt(Utilidades.SHARED_FILTER_FICHAS_YEAR, temporadaList.size() - 1) : id_temporadas.indexOf(marca_especial_temporada));
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_inicio, menu);
    }

    public void preparaSubirRecomendaciones() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<List<Evaluaciones>> chkF = executorService.submit(()
                -> MainActivity.myAppDB.DaoEvaluaciones()
                .getEvaluacionesPendientesSync());

        try {
            List<Evaluaciones> chk = chkF.get();

            if (chk.isEmpty()) {
                executorService.shutdown();
                return;
            }

            RecomendacionesRequest chkS = new RecomendacionesRequest();

            chkS.setEvaluacionesList(chk);
            prepararSubirRecomendaciones(chkS);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void prepararSubirRecomendaciones(RecomendacionesRequest recomendacionesRequest) {
        InternetStateClass mm = new InternetStateClass(activity, result -> {
            if (!result) {
                Toasty.error(activity, activity.getResources().getString(R.string.sync_not_internet), Toast.LENGTH_SHORT, true).show();
                return;
            }


            ProgressDialog pd = new ProgressDialog(activity);
            pd.setMessage("conectandose a internet, espere por favor");
            pd.show();


            if (pd.isShowing()) {
                pd.dismiss();
            }

            new RecomendacionesSync(recomendacionesRequest, requireActivity(), (state, message) -> {
                if (state) {
                    Toasty.success(requireActivity(), message, Toast.LENGTH_LONG, true).show();
                } else {
                    Toasty.error(requireActivity(), message, Toast.LENGTH_LONG, true).show();
                }
            });

        }, 1);
        mm.execute();
    }


    void prepararVisitas() {
        if (contenedor_botones != null) {


            contenedor_botones.removeAllViews();

            btn_descargar.setEnabled(true);
            btn_preparar.setEnabled(true);

            List<Visitas> visitas = MainActivity.myAppDB.myDao().getVisitasPorSubir(); //2
            if (visitas.size() > 0) {

                Utilidades.exportDatabse(Utilidades.NOMBRE_DATABASE, activity.getPackageName());

                botonesSeleccionados = new ArrayList<>();
                idVisitasSeleccionadas = new ArrayList<>();

                visitas_titulo.setVisibility(View.VISIBLE);
                visitas_titulo.setText("Visitas pendientes por subir (ROJAS)");
                visitas_marca.setVisibility(View.VISIBLE);
                visitas_marca.setText("pinche cada visita para marcarla (solo de a 3), para subirla inmeditamente mantenga presionado el botón.");


                ArrayList<Visitas> visitas1 = new ArrayList<>(); //0
                int totalContadas = 0;
                int cantidadAMostrar = 3;
                for (final Visitas v2 : visitas) { //2 vueltas

                    visitas1.add(v2);
//                        2 == 3?
                    if (visitas1.size() == cantidadAMostrar) {
                        LinearLayout linearLayout = new LinearLayout(getActivity());
                        linearLayout.setId(View.generateViewId());
                        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                        for (final Visitas v : visitas1) { //vueltas de a 3

                            AnexoContrato anexoContrato = MainActivity.myAppDB.myDao().getAnexos(v.getId_anexo_visita());
                            List<Fotos> fotos = MainActivity.myAppDB.myDao().getFotosByIdVisita(v.getId_visita());

                            final Button button = new Button(getActivity());
                            button.setId(View.generateViewId());
                            button.setGravity(Gravity.CENTER);
                            button.setTag("VISITASPENDIENTES_" + v.getId_visita());

                            if (v.getEstado_server_visitas() == 0) {
                                button.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.colorRedLight));
                                button.setTextColor(getActivity().getColor(R.color.colorSurface));
                            }

                            long peso = 0;

                            for (Fotos f : fotos) {
                                File file = new File(f.getRuta());
                                if (file.exists()) {
                                    peso += (file.length() / 1024);
                                }
                            }

                            String texto = "";

                            texto += (anexoContrato != null) ? anexoContrato.getAnexo_contrato() : "";
                            texto += "\n" + fotos.size() + " FOTOS ";
                            texto += "CON " + (peso / 1024) + " MB  ";


                            button.setText(texto);

                            linearLayout.addView(button);

                            LinearLayout.LayoutParams propParam = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
                            propParam.setMargins(20, 10, 20, 10);

                            button.setLayoutParams(propParam);


                            button.setOnClickListener(view -> Toasty.info(getActivity(), "Opcion deshabilitada, mantenga presionado para subir", Toast.LENGTH_LONG, true).show());

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
                    } else if ((visitas.size()/* 2 */ - totalContadas /* 1 */) > 0 && (visitas.size() - totalContadas) < cantidadAMostrar) {

                        LinearLayout linearLayout = new LinearLayout(getActivity());
                        linearLayout.setId(View.generateViewId());
                        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                        for (final Visitas v : visitas1) { //vueltas de a 3

                            AnexoContrato anexoContrato = MainActivity.myAppDB.myDao().getAnexos(v.getId_anexo_visita());
                            List<Fotos> fotos = MainActivity.myAppDB.myDao().getFotosByIdVisita(v.getId_visita());

                            final Button button = new Button(getActivity());
                            button.setId(View.generateViewId());
                            button.setGravity(Gravity.CENTER);
                            button.setTag("VISITASPENDIENTES_" + v.getId_visita());

                            if (v.getEstado_server_visitas() == 0) {
                                button.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.colorRedLight));
                                button.setTextColor(getActivity().getColor(R.color.colorSurface));
                            }

                            long peso = 0;

                            for (Fotos f : fotos) {
                                File file = new File(f.getRuta());
                                if (file.exists()) {
                                    peso += (file.length() / 1024);
                                }
                            }

                            String texto = "";

                            texto += (anexoContrato != null) ? anexoContrato.getAnexo_contrato() : "";
                            texto += "\n" + fotos.size() + " FOTOS ";
                            texto += "CON " + (peso / 1024) + " MB  ";


                            button.setText(texto);

                            linearLayout.addView(button);

                            LinearLayout.LayoutParams propParam = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
                            propParam.setMargins(20, 10, 20, 10);


                            button.setLayoutParams(propParam);


                            button.setOnClickListener(view -> {
                                Toasty.info(getActivity(), "Opcion deshabilitada, mantenga presionado para subir", Toast.LENGTH_LONG, true).show();
                            });

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
                        totalContadas++;
                        visitas1.clear();

                    }
                }
            } else {
                visitas_titulo.setVisibility(View.VISIBLE);
                visitas_titulo.setText("no hay visitas pendientes");
                visitas_marca.setVisibility(View.VISIBLE);
                visitas_marca.setText("");
            }
        }


    }

    void subirVisita(final Visitas v, final Button button) {
        if (progressDialogGeneral != null && !progressDialogGeneral.isShowing()) {
            progressDialogGeneral.setTitle("Preparando visitas para subir...");
            progressDialogGeneral.setCancelable(false);
            progressDialogGeneral.show();
        }


        InternetStateClass mm = new InternetStateClass(activity, result -> {
            if (result) {
                if (button != null) button.setEnabled(true);
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


                cantidadSuma += v.getId_visita();
                cantidadSuma += 1;


                List<Fotos> fts = new ArrayList<>();
                if (fotos.size() > 0) {
                    for (Fotos fs : fotos) {
                        fs.setEncrypted_image(Utilidades.imageToString(fs.getRuta()));
                        fts.add(fs);
                    }
                }


                if (detalles.size() > 0) {
                    for (detalle_visita_prop v1 : detalles) {
                        cantidadSuma += v1.getId_det_vis_prop_detalle();
                    }
                }
                cantidadSuma += detalles.size();


                if (fts.size() > 0) {
                    for (Fotos v1 : fts) {
                        cantidadSuma += v1.getId_foto();
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


            } else {
                if (button != null) button.setEnabled(true);
                if (progressDialogGeneral.isShowing()) progressDialogGeneral.dismiss();
                Toasty.error(activity, activity.getResources().getString(R.string.sync_not_internet), Toast.LENGTH_SHORT, true).show();
            }
        }, 1);
        mm.execute();
    }

    public void subidaDatosVisita(SubidaDatos subidaDatos, final int id_visita) {
        if (progressDialogGeneral.isShowing()) progressDialogGeneral.setTitle("subiendo visita");

        Config config = MainActivity.myAppDB.myDao().getConfig();
        ApiService apiService = RetrofitClient.getClient(config.getServidorSeleccionado()).create(ApiService.class);
        Call<Respuesta> call = apiService.enviarDatos(subidaDatos);

        call.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(@NonNull Call<Respuesta> call, @NonNull Response<Respuesta> response) {
                if (response.isSuccessful()) {
                    switch (response.code()) {
                        case 200:
                            Respuesta resSubidaDatos = response.body();
                            if (resSubidaDatos != null) {
                                switch (resSubidaDatos.getCodigoRespuesta()) {
                                    case 0:
                                        Toasty.info(activity, "pasando a segunda respuesta", Toast.LENGTH_SHORT, true).show();
                                        segundaRespuestaVisita(resSubidaDatos.getCabeceraRespuesta(), id_visita);
                                        break;
                                    case 5:
                                        Utilidades.avisoListo(activity, "ATENCION", "NO POSEES LA ULTIMA VERSION DE LA APLICACION ", "entiendo");
                                        if (progressDialogGeneral.isShowing())
                                            progressDialogGeneral.dismiss();
                                        break;
                                    default:
                                        if (progressDialogGeneral.isShowing())
                                            progressDialogGeneral.dismiss();
                                        Utilidades.avisoListo(getActivity(), "ATENCION", "PROBLEMAS SUBIENDO DATOS \nCODIGO:  " + resSubidaDatos.getCodigoRespuesta() + "\nMENSAJE: \n" + resSubidaDatos.getMensajeRespuesta(), "ENTIENDO");
                                        break;
                                }
                            } else {
                                if (progressDialogGeneral.isShowing())
                                    progressDialogGeneral.dismiss();
                                Utilidades.avisoListo(getActivity(), "ATENCION", "CUERPO DE RESPUESTA VACIO \nCODIGO:  " + response.code() + "\nMENSAJE: \n" + response.message(), "ENTIENDO");
                            }
                            break;
                        default:
                            if (progressDialogGeneral.isShowing()) progressDialogGeneral.dismiss();
                            Utilidades.avisoListo(getActivity(), "ATENCION", "PROBLEMAS CON SERVIDOR \nCODIGO:  " + response.code() + "\nMENSAJE: \n" + response.message(), "ENTIENDO");
                            break;
                    }
                } else {
                    if (progressDialogGeneral.isShowing()) progressDialogGeneral.dismiss();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Utilidades.avisoListo(getActivity(), "ATENCION", "COMUNICACION FALLIDA \nCODIGO:  " + response.code() + "\nMENSAJE: \n" + jObjError.getJSONObject("error").getString("message"), "ENTIENDO");

                    } catch (Exception e) {
                        Utilidades.avisoListo(getActivity(), "ATENCION", "COMUNICACION FALLIDA \nCODIGO:  " + response.code() + "\nMENSAJE: \n" + e.getMessage(), "ENTIENDO");
                    }

                }


            }

            @Override
            public void onFailure(@NonNull Call<Respuesta> call, @NonNull Throwable t) {
                if (progressDialogGeneral.isShowing()) progressDialogGeneral.dismiss();
                Utilidades.avisoListo(getActivity(), "ATENCION", "PROBLEMA EN LA COMUNICACION \nMENSAJE: \n" + t.getMessage(), "ENTIENDO");
            }
        });

    }

    private void segundaRespuestaVisita(int cab, final int id_visita) {
        if (progressDialogGeneral.isShowing()) {
            progressDialogGeneral.setTitle("esperando confirmacion de la visita ...");
        }

        final int[] respuesta = {0, 0};
        Config config = MainActivity.myAppDB.myDao().getConfig();
        ApiService apiService = RetrofitClient.getClient(config.getServidorSeleccionado()).create(ApiService.class);
        Call<Respuesta> callResponse = apiService.comprobacion(config.getId(), config.getId_usuario(), cab);
        callResponse.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(@NonNull Call<Respuesta> callResponse, @NonNull Response<Respuesta> response) {
                if (response.isSuccessful()) {
                    switch (response.code()) {
                        case 200:
                            Respuesta re = response.body();
                            if (re != null) {
                                switch (re.getCodigoRespuesta()) {
                                    case 0: //salio bien se sigue con el procedimiento

                                        int visita = MainActivity.myAppDB.myDao().updateVisitasSubidasTomadas(re.getCabeceraRespuesta()); /* las actualizo y las dejo en tomadas = 0 */
                                        if (visita <= 0) {
                                            respuesta[0] = 2;
                                            respuesta[1] = re.getCabeceraRespuesta();
                                        }

                                        MainActivity.myAppDB.myDao().updateDetalleVisitaSubidasTomadas(re.getCabeceraRespuesta());


                                        MainActivity.myAppDB.myDao().updateFotosSubidasTomada(re.getCabeceraRespuesta());

                                        if (respuesta[0] == 2) {

                                            MainActivity.myAppDB.myDao().updateDetalleVisitaBack(re.getCabeceraRespuesta());
                                            MainActivity.myAppDB.myDao().updateFotosBack(re.getCabeceraRespuesta());
                                            MainActivity.myAppDB.myDao().updateVisitasBack(re.getCabeceraRespuesta());

                                            Utilidades.avisoListo(getActivity(), "ATENCION", "PROBLEMAS SUBIENDO DATOS \nCODIGO:  " + re.getCodigoRespuesta() + "\nMENSAJE: \n" + re.getMensajeRespuesta(), "ENTIENDO");

                                            Toasty.success(activity, "Problema subiendo los datos , por favor, vuelva a intentarlo", Toast.LENGTH_SHORT, true).show();
                                            if (progressDialogGeneral.isShowing())
                                                progressDialogGeneral.dismiss();
                                        } else {

                                            Button button = view.findViewWithTag("VISITASPENDIENTES_" + id_visita);
                                            if (button != null) {
                                                button.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.colorGreenLight));
                                                button.setEnabled(false);
                                            }
                                            contadorVisita++;
                                            if (idVisitasSeleccionadas.size() > 0 && contadorVisita < 3) {
                                                prepararVisitaAgrupada(contadorVisita);
                                            } else {
                                                btn_sube_marcadas.setVisibility(View.INVISIBLE);
                                                if (progressDialogGeneral.isShowing())
                                                    progressDialogGeneral.dismiss();
                                            }
                                            preparaSubirRecomendaciones();
                                            Toasty.success(activity, "Se subio La visita con exito", Toast.LENGTH_SHORT, true).show();
                                        }
                                        break;

                                    default:
                                        if (progressDialogGeneral.isShowing())
                                            progressDialogGeneral.dismiss();
                                        Utilidades.avisoListo(getActivity(), "ATENCION", "PROBLEMAS SUBIENDO DATOS \nCODIGO:  " + re.getCodigoRespuesta() + "\nMENSAJE: \n" + re.getMensajeRespuesta(), "ENTIENDO");
                                        break;
                                }
                            } else {
                                /* respuesta nula */
                                if (progressDialogGeneral.isShowing())
                                    progressDialogGeneral.dismiss();
                                Toasty.error(activity, "Problema conectandonos al servidor, por favor vuelva a intentarlo ", Toast.LENGTH_SHORT, true).show();
                            }
                            break;

                        default:
                            if (progressDialogGeneral.isShowing()) progressDialogGeneral.dismiss();
                            Utilidades.avisoListo(getActivity(), "ATENCION", "PROBLEMAS CON SERVIDOR \nCODIGO:  " + response.code() + "\nMENSAJE: \n" + response.message(), "ENTIENDO");
                            break;
                    }
                } else {
                    if (progressDialogGeneral.isShowing()) progressDialogGeneral.dismiss();
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Utilidades.avisoListo(getActivity(), "ATENCION", "COMUNICACION FALLIDA EN COMPROBACION \nCODIGO:  " + response.code() + "\nMENSAJE: \n" + jObjError.getJSONObject("error").getString("message"), "ENTIENDO");

                    } catch (Exception e) {
                        Utilidades.avisoListo(getActivity(), "ATENCION", "COMUNICACION FALLIDA EN COMPROBACION \nCODIGO:  " + response.code() + "\nMENSAJE: \n" + e.getMessage(), "ENTIENDO");
                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<Respuesta> call, @NonNull Throwable t) {
                if (progressDialogGeneral.isShowing()) progressDialogGeneral.dismiss();
                Utilidades.avisoListo(getActivity(), "ATENCION", "PROBLEMA EN LA COMUNICACION \nMENSAJE: \n" + t.getMessage(), "ENTIENDO");
            }
        });
    }



    /* FIN SUBIR PROSPECTOS */

    void descargando() {
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        if (getView() != null) {
            progressDialog.setTitle("Espere un momento...");
            progressDialog.setMessage("descargando datos...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        Config cnf = MainActivity.myAppDB.myDao().getConfig();
        String vv = Utilidades.APPLICATION_VERSION;
        ApiService apiService = RetrofitClient.getClient(cnf.getServidorSeleccionado()).create(ApiService.class);
        Call<GsonDescargas> call = apiService.descargarDatos(cnf.getId(), cnf.getId_usuario_suplandato(), vv);
        call.enqueue(new Callback<GsonDescargas>() {
            @Override
            public void onResponse(@NonNull Call<GsonDescargas> call, @NonNull Response<GsonDescargas> response) {
                GsonDescargas gsonDescargas = response.body();

                if (gsonDescargas == null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Utilidades.avisoListo(activity, "ERROR SINCRONIZACION", "respuesta nula", "aceptar");
                    return;
                }

                if (gsonDescargas.getRespuestas() != null && !gsonDescargas.getRespuestas().isEmpty()) {
                    for (Respuesta rsp : gsonDescargas.getRespuestas()) {
                        switch (rsp.getCodigoRespuesta()) {
                            case 5:
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                activity.cambiarFragment(new FragmentLogin(), Utilidades.FRAGMENT_INICIO, R.anim.slide_in_left, R.anim.slide_out_left);
                                return;
                            case 2:
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                Utilidades.avisoListo(activity, "ERROR SINCRONIZACION", rsp.getMensajeRespuesta()
                                        + " por favor vuelva a intentarlo, si el problema persiste contacte con un administrador", "aceptar");
                                return;
                        }
                    }
                }

                progressDialog.setMessage("guardando datos...");
                ExecutorService ex = Executors.newSingleThreadExecutor();

                ex.execute(() -> {
                    boolean[] problema = volqueoDatos(gsonDescargas);
                    Future<Config> futureConfig = ex.submit(() -> MainActivity.myAppDB.myDao().getConfig());
                    Future<List<Temporada>> futureTempo = ex.submit(() -> MainActivity.myAppDB.myDao().getTemporada());

                    handlerGrafico.post(() -> {
                        try {

                            Config config = futureConfig.get();
                            temporadaList = futureTempo.get();

                            setSpecialSeason(temporadaList);

                            if (!problema[0] && !problema[1]) {
                                cargarToolbar();
                                if (config != null) {
                                    activity.cambiarNombreUser(config.getId_usuario());
                                }
                                progressDialog.dismiss();
                                Toasty.info(activity, "datos descargados con exito", Toast.LENGTH_SHORT, true).show();

                                ex.shutdown();
                            } else {
                                Errores errores = new Errores();
                                errores.setCodigo_error(66);
                                errores.setMensaje_error("positivo en true al descargar home");
                                MainActivity.myAppDB.myDao().setErrores(errores);
                                progressDialog.dismiss();
                                Toasty.error(activity, "No se pudo descargar todo " + response.errorBody(), Toast.LENGTH_SHORT, true).show();
                                ex.shutdown();
                            }
                        } catch (ExecutionException | InterruptedException e) {
                            ex.shutdown();
                            progressDialog.dismiss();
                        }
                    });
                });
            }

            @Override
            public void onFailure(@NonNull Call<GsonDescargas> call, @NonNull Throwable t) {
                Errores errores = new Errores();
                errores.setCodigo_error(66);
                errores.setMensaje_error(t.toString());
                MainActivity.myAppDB.myDao().setErrores(errores);

                Toasty.error(activity, "No se pudo descargar todo front " + t.getLocalizedMessage(), Toast.LENGTH_SHORT, true).show();
                if (getView() != null) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    public void setSpecialSeason(List<Temporada> temporadas) {
        if (!temporadas.isEmpty()) {
            for (Temporada t : temporadas) {
                id_temporadas.add(t.getId_tempo_tempo());
                desc_temporadas.add(t.getNombre_tempo());

                if (t.getEspecial_temporada() > 0) {
                    marca_especial_temporada = t.getId_tempo_tempo();
                }
            }
        }

    }

    public void procesarInfoPrimera(int tempo) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler primerHandler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> {
            List<PrimeraPrioridad> ppList = MainActivity.myAppDB.DaoPrimeraPrioridad().getPPByTemporada(tempo);
            primerHandler.post(() -> mostrarTablaPrimera(ppList));
        });
        executorService.shutdown();
    }

    void primeraPrioridad(int tempo) {
        progressBar1.setVisibility(View.VISIBLE);
        lista_primera_prioridad.setVisibility(View.GONE);
        procesarInfoPrimera(tempo);
    }

    public void mostrarTablaPrimera(List<PrimeraPrioridad> pplist) {

        progressBar1.setVisibility(View.GONE);
        lista_primera_prioridad.setVisibility(View.VISIBLE);
        PrimeraPrioridadAdapter adapterPrimera = new PrimeraPrioridadAdapter(pplist, activity);
        lista_primera_prioridad.setLayoutManager(new LinearLayoutManager(activity));
        lista_primera_prioridad.setHasFixedSize(true);
        lista_primera_prioridad.setAdapter(adapterPrimera);

    }

    public void mostrarTablaNoVis(List<SitiosNoVisitados> visitasCompletas) {

        progressBar2.setVisibility(View.GONE);
        lista_sitios_no_visitados.setVisibility(View.VISIBLE);
        SitiosNoVisitadosAdapter adapterNovis = new SitiosNoVisitadosAdapter(visitasCompletas);
        lista_sitios_no_visitados.setLayoutManager(new LinearLayoutManager(activity));
        lista_sitios_no_visitados.setHasFixedSize(true);
        lista_sitios_no_visitados.setAdapter(adapterNovis);

    }


    void sitiosNoVisitados(int tempo) {
        progressBar2.setVisibility(View.VISIBLE);
        lista_sitios_no_visitados.setVisibility(View.GONE);
        procesarInfoNoVisitados(tempo);
    }

    public void procesarInfoNoVisitados(int tempo) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler sitiosHandler = new Handler(Looper.getMainLooper());

        executorService.execute(() -> {
            List<SitiosNoVisitados> listAnexos = MainActivity.myAppDB.DaoSitiosNoVisitados().getSNVByTemporada(tempo);
            sitiosHandler.post(() -> mostrarTablaNoVis(listAnexos));
        });
        executorService.shutdown();
    }

    @Override
    public void onResume() {
        super.onResume();
        sitiosNoVisitados((!id_temporadas.isEmpty()) ? Integer.parseInt(id_temporadas.get(spinner_toolbar.getSelectedItemPosition())) : 0);
        primeraPrioridad((!id_temporadas.isEmpty()) ? Integer.parseInt(id_temporadas.get(spinner_toolbar.getSelectedItemPosition())) : 0);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.setDrawerEnabled(true);
            activity.updateView(getResources().getString(R.string.app_name), getResources().getString(R.string.subtitles_start));

            Config config = MainActivity.myAppDB.myDao().getConfig();
            activity.cambiarNombreUser(config.getId_usuario());
        }
    }
}
