package cl.smapdev.curimapu.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.FichasAdapter;
import cl.smapdev.curimapu.clases.adapters.SpinnerToolbarAdapter;
import cl.smapdev.curimapu.clases.relaciones.FichasCompletas;
import cl.smapdev.curimapu.clases.relaciones.Respuesta;
import cl.smapdev.curimapu.clases.relaciones.SubidaDatos;
import cl.smapdev.curimapu.clases.retrofit.ApiService;
import cl.smapdev.curimapu.clases.retrofit.RetrofitClient;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.CropRotation;
import cl.smapdev.curimapu.clases.tablas.Errores;
import cl.smapdev.curimapu.clases.tablas.Fichas;
import cl.smapdev.curimapu.clases.tablas.Fotos;
import cl.smapdev.curimapu.clases.tablas.FotosFichas;
import cl.smapdev.curimapu.clases.tablas.Temporada;
import cl.smapdev.curimapu.clases.tablas.Visitas;
import cl.smapdev.curimapu.clases.tablas.detalle_visita_prop;
import cl.smapdev.curimapu.clases.utilidades.InternetStateClass;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.dialogos.DialogFilterFichas;
import cl.smapdev.curimapu.fragments.fichas.FragmentCreaFicha;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentFichas extends Fragment {

    private LinearLayout contenedorProgreso;
    private TextView textoProgreso;
    private FichasAdapter fichasAdapter;
    private RecyclerView id_lista_fichas;
    private MainActivity activity;
    private FloatingActionButton fb_add_ficha;

    private Spinner spinner_toolbar;

    private SharedPreferences prefs;

    private Button btn_subir;

    private final ArrayList<String> id_temporadas = new ArrayList<>();
    private final ArrayList<String> desc_temporadas = new ArrayList<>();

    private List<Temporada> temporadaList;

    private String marca_especial_temporada;

    private ExecutorService executor;
    private final Handler handler = new Handler(Looper.getMainLooper());


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            activity = (MainActivity) context;
            prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
        } else {
            throw new RuntimeException(context.toString() + " must be MainActivity");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        executor = Executors.newSingleThreadExecutor();

        ejecutarSeguro(() -> {
            List<Temporada> temporadas = MainActivity.myAppDB.myDao().getTemporada();
            handler.post(() -> {
                temporadaList = temporadas;

                if (!temporadaList.isEmpty()) {
                    for (Temporada t : temporadaList) {
                        id_temporadas.add(t.getId_tempo_tempo());
                        desc_temporadas.add(t.getNombre_tempo());
                        if (t.getEspecial_temporada() > 0) {
                            marca_especial_temporada = t.getId_tempo_tempo();
                        }
                    }
                }

                recargarYear();
                spinner_toolbar.setAdapter(new SpinnerToolbarAdapter(requireActivity(), R.layout.spinner_template_toolbar_view, temporadaList));
                if (temporadaList != null && !temporadaList.isEmpty()) {
                    cargarLista(MainActivity.myAppDB.myDao().getFichasByYear(prefs.getString(Utilidades.SELECTED_ANO, temporadaList.get(temporadaList.size() - 1).getId_tempo_tempo())));
                }
            });
        });
    }

    private void ejecutarSeguro(Runnable r) {
        if (executor == null || executor.isShutdown() || executor.isTerminated()) {
            executor = Executors.newSingleThreadExecutor();
        }
        executor.execute(r);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fichas, container, false);
    }


    public void mostrarProgreso(String texto) {
        if (contenedorProgreso != null && textoProgreso != null) {
            contenedorProgreso.setVisibility(View.VISIBLE);
            textoProgreso.setText(texto);
        }
    }

    public void ocultarProgreso() {
        if (contenedorProgreso != null && textoProgreso != null) {
            contenedorProgreso.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        id_lista_fichas = view.findViewById(R.id.id_lista_fichas);
        spinner_toolbar = view.findViewById(R.id.spinner_toolbar);
        fb_add_ficha = view.findViewById(R.id.fb_add_ficha);
        btn_subir = view.findViewById(R.id.btn_subir);
        contenedorProgreso = view.findViewById(R.id.contenedor_progreso);
        textoProgreso = view.findViewById(R.id.texto_progreso);

        btn_subir.setOnClickListener(view1 -> {
            btn_subir.setEnabled(false);
            antiguoMetodo();
        });


        spinner_toolbar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (spinner_toolbar.getTag() != null) {
                    if (Integer.parseInt(spinner_toolbar.getTag().toString()) != i) {

                        prefs.edit().putString(Utilidades.SELECTED_ANO, id_temporadas.get(spinner_toolbar.getSelectedItemPosition())).apply();
                        prefs.edit().putInt(Utilidades.FILTRO_TEMPORADA, i).apply();
                        cargarLista(MainActivity.myAppDB.myDao().getFichasByYear(id_temporadas.get(spinner_toolbar.getSelectedItemPosition())));
                    } else {
                        spinner_toolbar.setTag(null);
                    }
                } else {
                    prefs.edit().putString(Utilidades.SELECTED_ANO, id_temporadas.get(spinner_toolbar.getSelectedItemPosition())).apply();
                    prefs.edit().putInt(Utilidades.FILTRO_TEMPORADA, i).apply();
                    cargarLista(MainActivity.myAppDB.myDao().getFichasByYear(id_temporadas.get(spinner_toolbar.getSelectedItemPosition())));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        fb_add_ficha.setOnClickListener(view2 -> avisoCreaNuevaFicha(getResources().getString(R.string.title_new_ficha), getResources().getString(R.string.text_new_ficha)));


        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.menu_vistas, menu);

            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_vistas_filter) {
                    DialogFilterFichas dialogo = new DialogFilterFichas();
                    dialogo.show(requireActivity().getSupportFragmentManager(), "DIALOGO_FICHAS");
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.CREATED);

        Utilidades.setToolbar(activity, view, getResources().getString(R.string.app_name), getResources().getString(R.string.subtitles_records));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }

    /* SUBIR PROSPECTOS */
    void antiguoMetodo() {

        mostrarProgreso("Preparando datos para subir...");

        btn_subir.setEnabled(true);
        new InternetStateClass(activity, result -> {
            ocultarProgreso();
            if (!result) {
                btn_subir.setEnabled(true);
                Toasty.error(activity, activity.getResources().getString(R.string.sync_not_internet), Toast.LENGTH_SHORT, true).show();
                return;
            }

            ejecutarSeguro(() -> {
                List<Visitas> visitas = MainActivity.myAppDB.myDao().getVisitasPorSubir();
                List<detalle_visita_prop> detalles = MainActivity.myAppDB.myDao().getDetallesPorSubir();
                List<Fotos> fotos = MainActivity.myAppDB.myDao().getFotos();

                List<Fichas> fichas = MainActivity.myAppDB.myDao().getFichasPorSubir();
                List<FotosFichas> fotosFichas = MainActivity.myAppDB.myDao().getFotosFichasPorSubir();
                List<CropRotation> crops = MainActivity.myAppDB.myDao().getCropsPorSubir();

                handler.post(() -> {
                    if (visitas.isEmpty() && detalles.isEmpty() && fotos.isEmpty()) {
                        if (!fichas.isEmpty() || !fotosFichas.isEmpty() || !crops.isEmpty()) {
                            probarTodo();
                        } else {
                            btn_subir.setEnabled(true);
                            Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        btn_subir.setEnabled(true);
                        Utilidades.avisoListo(getActivity(), "ATENCION", "TIENE \n-" + visitas.size() + " VISITAS \n-" + fotos.size() + " FOTOS \nPENDIENTES ,POR FAVOR, SINCRONICE.", "ENTIENDO");
                    }
                });
            });
        }, executor, handler).execute();

    }

    void probarTodo() {

        mostrarProgreso("Preparando datos para subir...");

        ejecutarSeguro(() -> {
            List<Fichas> fichas = MainActivity.myAppDB.myDao().getFichasPorSubir();
            List<FotosFichas> fotosFichas = MainActivity.myAppDB.myDao().getFotosFichasPorSubir();
            List<Errores> errores = MainActivity.myAppDB.myDao().getErroresPorSubir();

            List<FotosFichas> fts_fichas = new ArrayList<>();
            if (!fotosFichas.isEmpty()) {
                for (FotosFichas fs : fotosFichas) {
                    String imagenString = Utilidades.imageToString(fs.getRuta_foto_ficha());
                    if (!imagenString.isEmpty()) {
                        fs.setEncrypted_image(imagenString);
                        fts_fichas.add(fs);
                    }

                }
            }

            int cantidadSuma = 0;


            if (!fotosFichas.isEmpty()) {
                for (FotosFichas v : fotosFichas) {
                    cantidadSuma += v.getId_fotos_fichas();
                }
            }
            cantidadSuma = cantidadSuma + fotosFichas.size();

            if (!fichas.isEmpty()) {
                for (Fichas v : fichas) {
                    cantidadSuma += v.getId_ficha();
                }
            }
            cantidadSuma += fichas.size();
            Config config = MainActivity.myAppDB.myDao().getConfig();
            SubidaDatos list = new SubidaDatos();

            List<CropRotation> crops = MainActivity.myAppDB.myDao().getCropsPorSubir();

            list.setId_dispo(config.getId());
            list.setId_usuario(config.getId_usuario());
            list.setErrores(errores);
            list.setFichas(fichas);
            list.setCantidadSuma(cantidadSuma);
            list.setVersion(Utilidades.APPLICATION_VERSION);
            list.setFotosFichas(fts_fichas);
            list.setCropRotation(crops);

            handler.post(() -> {
                ocultarProgreso();
                btn_subir.setEnabled(true);
                subidaDatos(list);
            });
        });


    }

    public void subidaDatos(SubidaDatos subidaDatos) {


        mostrarProgreso("subiendo datos");

        Config config = MainActivity.myAppDB.myDao().getConfig();
        ApiService apiService = RetrofitClient.getClient(config.getServidorSeleccionado()).create(ApiService.class);
        Call<Respuesta> call = apiService.enviarDatos(subidaDatos);

        call.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(@NonNull Call<Respuesta> call, @NonNull Response<Respuesta> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        Respuesta resSubidaDatos = response.body();
                        if (resSubidaDatos != null) {
                            switch (resSubidaDatos.getCodigoRespuesta()) {
                                case 0:
                                    Toasty.info(activity, "pasando a segunda respuesta", Toast.LENGTH_SHORT, true).show();
                                    segundaRespuesta(resSubidaDatos.getCabeceraRespuesta());
                                    break;
                                case 5:
                                    Utilidades.avisoListo(activity, "ATENCION", "NO POSEES LA ULTIMA VERSION DE LA APLICACION ", "entiendo");
                                    ocultarProgreso();
                                    break;
                                default:
                                    ocultarProgreso();
                                    Utilidades.avisoListo(getActivity(), "ATENCION", "PROBLEMAS SUBIENDO DATOS \nCODIGO:  " + resSubidaDatos.getCodigoRespuesta() + "\nMENSAJE: \n" + resSubidaDatos.getMensajeRespuesta(), "ENTIENDO");
                                    break;
                            }
                        } else {
                            ocultarProgreso();
                            Utilidades.avisoListo(getActivity(), "ATENCION", "CUERPO DE RESPUESTA VACIO \nCODIGO:  " + response.code() + "\nMENSAJE: \n" + response.message(), "ENTIENDO");
                        }
                    } else {
                        ocultarProgreso();
                        Utilidades.avisoListo(getActivity(), "ATENCION", "PROBLEMAS CON SERVIDOR \nCODIGO:  " + response.code() + "\nMENSAJE: \n" + response.message(), "ENTIENDO");
                    }
                } else {
                    ocultarProgreso();
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
                ocultarProgreso();
                Utilidades.avisoListo(getActivity(), "ATENCION", "PROBLEMA EN LA COMUNICACION \nMENSAJE: \n" + t.getMessage(), "ENTIENDO");
            }
        });

    }

    private void segundaRespuesta(int cab) {

        mostrarProgreso("esperando confirmacion...");

        final int[] respuesta = {0, 0};
        Config config = MainActivity.myAppDB.myDao().getConfig();
        ApiService apiService = RetrofitClient.getClient(config.getServidorSeleccionado()).create(ApiService.class);
        Call<Respuesta> callResponse = apiService.comprobacion(config.getId(), config.getId_usuario(), cab);
        callResponse.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(@NonNull Call<Respuesta> callResponse, @NonNull Response<Respuesta> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        Respuesta re = response.body();
                        if (re != null) {
                            if (re.getCodigoRespuesta() == 0) { //salio bien se sigue con el procedimiento
                                List<Fichas> fichas = MainActivity.myAppDB.myDao().getFichasPorSubir(); /* hay 5 fichas */
                                int ficha = MainActivity.myAppDB.myDao().updateFichasSubidas(re.getCabeceraRespuesta()); /* update a las 5 */
                                if (ficha != fichas.size()) { /* 5  !=  5 */
                                    respuesta[0] = 2;
                                    respuesta[1] = re.getCabeceraRespuesta();
                                }
                                List<CropRotation> cropRotations = MainActivity.myAppDB.myDao().getCropsPorSubir();
                                int crop = MainActivity.myAppDB.myDao().updateCropsSubidos(re.getCabeceraRespuesta());
                                if (crop != cropRotations.size()) {
                                    respuesta[0] = 2;
                                    respuesta[1] = re.getCabeceraRespuesta();
                                }

                                List<FotosFichas> fotosFichasList = MainActivity.myAppDB.myDao().getFotosFichasPorSubir();
                                int fotosFichas = MainActivity.myAppDB.myDao().updateFotosFichasSubidas(re.getCabeceraRespuesta());
                                if (fotosFichas != fotosFichasList.size()) {
                                    respuesta[0] = 2;
                                    respuesta[1] = re.getCabeceraRespuesta();
                                }

                                if (respuesta[0] == 2) {
                                    MainActivity.myAppDB.myDao().updateFichasBack(re.getCabeceraRespuesta());
                                    MainActivity.myAppDB.myDao().updateFotosFichasBack(re.getCabeceraRespuesta());
                                    MainActivity.myAppDB.myDao().updateCropsBack(re.getCabeceraRespuesta());


                                    Utilidades.avisoListo(getActivity(), "ATENCION", "PROBLEMAS SUBIENDO DATOS \nCODIGO:  " + re.getCodigoRespuesta() + "\nMENSAJE: \n" + re.getMensajeRespuesta(), "ENTIENDO");

                                    Toasty.success(activity, "Problema subiendo los datos , por favor, vuelva a intentarlo", Toast.LENGTH_SHORT, true).show();
                                    ocultarProgreso();
                                } else {
                                    ocultarProgreso();
                                    Toasty.success(activity, "Se subio Prospectos con exito", Toast.LENGTH_SHORT, true).show();
                                }
                            } else {
                                ocultarProgreso();
                                Utilidades.avisoListo(getActivity(), "ATENCION", "PROBLEMAS SUBIENDO DATOS \nCODIGO:  " + re.getCodigoRespuesta() + "\nMENSAJE: \n" + re.getMensajeRespuesta(), "ENTIENDO");
                            }
                        } else {
                            /* respuesta nula */
                            ocultarProgreso();
                            Toasty.error(activity, "Problema conectandonos al servidor, por favor vuelva a intentarlo ", Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        ocultarProgreso();
                        Utilidades.avisoListo(getActivity(), "ATENCION", "PROBLEMAS CON SERVIDOR \nCODIGO:  " + response.code() + "\nMENSAJE: \n" + response.message(), "ENTIENDO");
                    }
                } else {
                    ocultarProgreso();
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
                ocultarProgreso();
                Utilidades.avisoListo(getActivity(), "ATENCION", "PROBLEMA EN LA COMUNICACION \nMENSAJE: \n" + t.getMessage(), "ENTIENDO");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MyReceiver r = new MyReceiver();
        LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(r, new IntentFilter("TAG_REFRESH"));
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && context != null) {
                List<FichasCompletas> trabajo = (List<FichasCompletas>) intent.getSerializableExtra(DialogFilterFichas.LLAVE_ENVIO_OBJECTO);
                if (trabajo != null) {
                    spinner_toolbar.setTag(prefs.getInt(Utilidades.FILTRO_TEMPORADA, temporadaList.size() - 1));
                    spinner_toolbar.setSelection(prefs.getInt(Utilidades.FILTRO_TEMPORADA, temporadaList.size() - 1));
                    recargarYear();
                    cargarLista(trabajo);

                }
            }

        }
    }

    private void recargarYear() {
        if (!temporadaList.isEmpty()) {
            spinner_toolbar.setSelection(prefs.getInt(Utilidades.FILTRO_TEMPORADA, (marca_especial_temporada.isEmpty()) ? temporadaList.size() - 1 : id_temporadas.indexOf(marca_especial_temporada)));
        }
    }


    private void cargarLista(List<FichasCompletas> fichasCompletas) {

        if (Utilidades.isLandscape(activity)) {
            id_lista_fichas.setLayoutManager(new GridLayoutManager(activity, 2));
        } else {
            id_lista_fichas.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        }


        id_lista_fichas.setHasFixedSize(true);


        fichasAdapter = new FichasAdapter(fichasCompletas, fichas -> {
            activity.cambiarFragment(FragmentCreaFicha.getInstance(fichas), Utilidades.FRAGMENT_CREA_FICHA, R.anim.slide_in_left, R.anim.slide_out_left);
        }, fichas -> {
            if (fichas.getFichas().getActiva() == 1) {
                avisoActivaFicha("Seleccione estado del prospecto", fichas);
            } else {
                Utilidades.avisoListo(activity, "Prospecto ya activado", "No se puede modificar en tableta, solo en web y por un administrador", "entiendo");
            }

        }, activity);


        id_lista_fichas.setAdapter(fichasAdapter);

    }


    private void avisoCreaNuevaFicha(String title, String message) {
        View viewInfalted = LayoutInflater.from(getActivity()).inflate(R.layout.alert_empty, null);

        final AlertDialog builder = new AlertDialog.Builder(requireActivity())
                .setView(viewInfalted)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getResources().getString(R.string.button_create), (dialogInterface, i) -> {
                })
                .setNegativeButton(getResources().getString(R.string.nav_cancel), (dialogInterface, i) -> {
                })
                .create();

        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
            Button c = builder.getButton(AlertDialog.BUTTON_NEGATIVE);
            b.setOnClickListener(v -> {
                if (activity != null) {
                    activity.cambiarFragment(new FragmentCreaFicha(), Utilidades.FRAGMENT_CREA_FICHA, R.anim.slide_in_left, R.anim.slide_out_left);
                }
                builder.dismiss();
            });
            c.setOnClickListener(view -> builder.dismiss());
        });

        builder.setCancelable(false);
        builder.show();
    }


    private void avisoActivaFicha(String title, final FichasCompletas completas) {
        final View viewInfalted = LayoutInflater.from(getActivity()).inflate(R.layout.alert_activa_ficha, null);


        final RadioButton ra = viewInfalted.findViewById(R.id.radio_inactiva);
        final RadioButton rb = viewInfalted.findViewById(R.id.radio_activa);


        switch (completas.getFichas().getActiva()) {
            case 1:
                ra.setChecked(true);
                break;
            case 2:
            case 3:
                rb.setChecked(true);
                break;
        }


        final AlertDialog builder = new AlertDialog.Builder(requireActivity())
                .setView(viewInfalted)
                .setTitle(title)
                .setPositiveButton("Modificar", (dialogInterface, i) -> {
                })
                .setNegativeButton("Cancelar", (dialogInterface, i) -> {
                })
                .create();

        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
            Button c = builder.getButton(AlertDialog.BUTTON_NEGATIVE);


            b.setOnClickListener(v -> {
                Fichas fichas = completas.getFichas();
                if (fichas != null) {

                    if (rb.isChecked() && !TextUtils.isEmpty(fichas.getOferta_negocio()) && !TextUtils.isEmpty(fichas.getLocalidad()) && fichas.getHas_disponible() > 0.0
                            && !TextUtils.isEmpty(fichas.getPredio_ficha()) && !TextUtils.isEmpty(fichas.getPotrero_ficha()) && !TextUtils.isEmpty(fichas.getMaleza())
                            && !TextUtils.isEmpty(fichas.getEstado_general_ficha()) && !TextUtils.isEmpty(fichas.getFecha_limite_siembra_ficha()) && !TextUtils.isEmpty(fichas.getObservaciones())
                            && !TextUtils.isEmpty(fichas.getObservacion_negocio_ficha()) && fichas.getNorting() != 0.0 && fichas.getEasting() != 0.0 && !TextUtils.isEmpty(fichas.getId_tipo_tenencia_maquinaria())
                            && !TextUtils.isEmpty(fichas.getId_tipo_tenencia_terreno()) && !TextUtils.isEmpty(fichas.getExperiencia()) && !TextUtils.isEmpty(fichas.getId_tipo_riego())
                            && !TextUtils.isEmpty(fichas.getId_tipo_suelo()) && !TextUtils.isEmpty(fichas.getEspecie_ficha())) {

                        int estado = (ra.isChecked()) ? 1 : 3;
                        fichas.setActiva(estado);
                        MainActivity.myAppDB.myDao().updateFicha(fichas);

                        Toasty.success(activity, "Prospecto activado con exito", Toast.LENGTH_LONG, true).show();

                        if (fichasAdapter != null) {
                            fichasAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Utilidades.avisoListo(activity, "Falta Algo", "Para activar el prospecto debe completar todos los campos", "entiendo");
                    }
                }
                builder.dismiss();
            });
            c.setOnClickListener(view -> builder.dismiss());
        });

        builder.setCancelable(false);
        builder.show();
    }
}
