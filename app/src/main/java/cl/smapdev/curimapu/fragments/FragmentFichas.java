package cl.smapdev.curimapu.fragments;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
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
import java.util.Objects;

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
import cl.smapdev.curimapu.clases.utilidades.returnValuesFromAsyntask;
import cl.smapdev.curimapu.fragments.dialogos.DialogFilterFichas;
import cl.smapdev.curimapu.fragments.fichas.FragmentCreaFicha;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentFichas extends Fragment {


    private FichasAdapter fichasAdapter;
    private RecyclerView id_lista_fichas;
    private MainActivity activity;
    private FloatingActionButton fb_add_ficha;

    private Spinner spinner_toolbar;

    private SharedPreferences prefs;

    private Button btn_subir;

    private ArrayList<String> id_temporadas = new ArrayList<>();
    private ArrayList<String> desc_temporadas = new ArrayList<>();

    private List<Temporada> temporadaList;

    private String marca_especial_temporada;

    private ProgressDialog progressDialogGeneral;


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


        progressDialogGeneral = new ProgressDialog(activity);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fichas, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        id_lista_fichas = (RecyclerView) view.findViewById(R.id.id_lista_fichas);
        spinner_toolbar = (Spinner) view.findViewById(R.id.spinner_toolbar);
        fb_add_ficha = (FloatingActionButton) view.findViewById(R.id.fb_add_ficha);

        btn_subir = (Button) view.findViewById(R.id.btn_subir);

        temporadaList = MainActivity.myAppDB.myDao().getTemporada();
        if (temporadaList.size() > 0) {
            for (Temporada t : temporadaList) {
                id_temporadas.add(t.getId_tempo_tempo());
                desc_temporadas.add(t.getNombre_tempo());
                if (t.getEspecial_temporada() > 0) {
                    marca_especial_temporada = t.getId_tempo_tempo();
                }
            }
        }

        setHasOptionsMenu(true);


        btn_subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_subir.setEnabled(false);
                antiguoMetodo();
            }
        });

        spinner_toolbar.setAdapter(new SpinnerToolbarAdapter(Objects.requireNonNull(getActivity()), R.layout.spinner_template_toolbar_view, temporadaList));


        //spinner_toolbar.setSelection(getResources().getStringArray(R.array.anos_toolbar).length - 1);
        recargarYear();
        spinner_toolbar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (spinner_toolbar.getTag() != null) {
                    if (Integer.parseInt(spinner_toolbar.getTag().toString()) != i) {


                        prefs.edit().putString(Utilidades.SELECTED_ANO, id_temporadas.get(spinner_toolbar.getSelectedItemPosition())).apply();
                        prefs.edit().putInt(Utilidades.SHARED_FILTER_FICHAS_YEAR, i).apply();

                        cargarLista(MainActivity.myAppDB.myDao().getFichasByYear(id_temporadas.get(spinner_toolbar.getSelectedItemPosition())));
                    } else {
                        spinner_toolbar.setTag(null);
                    }
                } else {

                    prefs.edit().putString(Utilidades.SELECTED_ANO, id_temporadas.get(spinner_toolbar.getSelectedItemPosition())).apply();
                    prefs.edit().putInt(Utilidades.SHARED_FILTER_FICHAS_YEAR, i).apply();

                    cargarLista(MainActivity.myAppDB.myDao().getFichasByYear(id_temporadas.get(spinner_toolbar.getSelectedItemPosition())));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        fb_add_ficha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avisoCreaNuevaFicha(getResources().getString(R.string.title_new_ficha), getResources().getString(R.string.text_new_ficha));
            }
        });

        if (temporadaList.size() > 0) {
            cargarLista(MainActivity.myAppDB.myDao().getFichasByYear(prefs.getString(Utilidades.SELECTED_ANO, temporadaList.get(temporadaList.size() - 1).getId_tempo_tempo())));
        }


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
                    dialogo.show(activity.getSupportFragmentManager(), "DIALOGO_FICHAS");
                    return true;
                }

                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.CREATED);

        Utilidades.setToolbar(activity, view, getResources().getString(R.string.app_name), getResources().getString(R.string.subtitles_records));

    }

    /* SUBIR PROSPECTOS */
    void antiguoMetodo() {

        if (progressDialogGeneral != null && !progressDialogGeneral.isShowing()) {
            progressDialogGeneral.setTitle("Preparando datos para subir...");
            progressDialogGeneral.setCancelable(false);
            progressDialogGeneral.show();
        }

        btn_subir.setEnabled(true);
        InternetStateClass mm = new InternetStateClass(activity, new returnValuesFromAsyntask() {
            @Override
            public void myMethod(boolean result) {
                if (result) {
                    List<Visitas> visitas = MainActivity.myAppDB.myDao().getVisitasPorSubir();
                    List<detalle_visita_prop> detalles = MainActivity.myAppDB.myDao().getDetallesPorSubir();
                    List<Fotos> fotos = MainActivity.myAppDB.myDao().getFotos();
                    List<Fichas> fichas = MainActivity.myAppDB.myDao().getFichasPorSubir();
                    List<FotosFichas> fotosFichas = MainActivity.myAppDB.myDao().getFotosFichasPorSubir();
                    List<CropRotation> crops = MainActivity.myAppDB.myDao().getCropsPorSubir();


                    if (visitas.size() <= 0 && detalles.size() <= 0 && fotos.size() <= 0) {
                        if (fichas.size() > 0 || fotosFichas.size() > 0 || crops.size() > 0) {

                            if (Utilidades.exportDatabse(Utilidades.NOMBRE_DATABASE, activity.getPackageName())) {
                                probarTodo();
                            } else {
                                if (progressDialogGeneral.isShowing())
                                    progressDialogGeneral.dismiss();
                                Utilidades.avisoListo(getActivity(), "Hey", "NO PUDIMOS GENERAR EL RESPALDO A LA BASE DE DATOS, VUELVE A INTENTARLO, SI EL PROBLEMA PERSISTE CONTACTE CON UN ADMINISTRADOR.", "ENTIENDO");
                            }
                            btn_subir.setEnabled(true);
                        } else {
                            btn_subir.setEnabled(true);
                            if (progressDialogGeneral.isShowing()) progressDialogGeneral.dismiss();
                            Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                        }
                    } else {
                        btn_subir.setEnabled(true);
                        if (progressDialogGeneral.isShowing()) progressDialogGeneral.dismiss();
                        Utilidades.avisoListo(getActivity(), "ATENCION", "TIENE \n-" + visitas.size() + " VISITAS \n-" + fotos.size() + " FOTOS \nPENDIENTES ,POR FAVOR, SINCRONICE.", "ENTIENDO");
                    }

                } else {
                    btn_subir.setEnabled(true);
                    if (progressDialogGeneral.isShowing()) progressDialogGeneral.dismiss();
                    Toasty.error(activity, activity.getResources().getString(R.string.sync_not_internet), Toast.LENGTH_SHORT, true).show();
                }
            }
        }, 1);
        mm.execute();

    }

    void probarTodo() {

        if (progressDialogGeneral.isShowing()) {
            progressDialogGeneral.setTitle("Preparando datos para subir...");
        }
        /* esto se mantendra igual */
        List<Fichas> fichas = MainActivity.myAppDB.myDao().getFichasPorSubir();
        List<FotosFichas> fotosFichas = MainActivity.myAppDB.myDao().getFotosFichasPorSubir();
        List<Errores> errores = MainActivity.myAppDB.myDao().getErroresPorSubir();

        List<FotosFichas> fts_fichas = new ArrayList<>();
        if (fotosFichas.size() > 0) {
            for (FotosFichas fs : fotosFichas) {
                fs.setEncrypted_image(Utilidades.imageToString(fs.getRuta_foto_ficha()));
                fts_fichas.add(fs);
            }
        }

        int cantidadSuma = 0;


        if (fotosFichas.size() > 0) {
            for (FotosFichas v : fotosFichas) {
                cantidadSuma += v.getId_fotos_fichas();
            }
        }
        cantidadSuma = cantidadSuma + fotosFichas.size();

        if (fichas.size() > 0) {
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

        subidaDatos(list);
    }

    public void subidaDatos(SubidaDatos subidaDatos) {


        if (progressDialogGeneral.isShowing()) progressDialogGeneral.setTitle("subiendo datos");

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
                                        segundaRespuesta(resSubidaDatos.getCabeceraRespuesta());
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

    private void segundaRespuesta(int cab) {

        if (progressDialogGeneral.isShowing()) {
            progressDialogGeneral.setTitle("esperando confirmacion...");
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
                                            if (progressDialogGeneral.isShowing())
                                                progressDialogGeneral.dismiss();
                                        } else {
                                            if (progressDialogGeneral.isShowing())
                                                progressDialogGeneral.dismiss();
                                            Toasty.success(activity, "Se subio Prospectos con exito", Toast.LENGTH_SHORT, true).show();
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


    @Override
    public void onResume() {
        super.onResume();
        MyReceiver r = new MyReceiver();
        LocalBroadcastManager.getInstance(activity).registerReceiver(r, new IntentFilter("TAG_REFRESH"));
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && context != null) {
                List<FichasCompletas> trabajo = (List<FichasCompletas>) intent.getSerializableExtra(DialogFilterFichas.LLAVE_ENVIO_OBJECTO);
                if (trabajo != null) {
                    spinner_toolbar.setTag(prefs.getInt(Utilidades.SHARED_FILTER_FICHAS_YEAR, temporadaList.size() - 1));
                    spinner_toolbar.setSelection(prefs.getInt(Utilidades.SHARED_FILTER_FICHAS_YEAR, temporadaList.size() - 1));
                    cargarLista(trabajo);
                }
            }

        }
    }

    private void recargarYear() {
        if (temporadaList.size() > 0) {
            spinner_toolbar.setSelection((marca_especial_temporada.isEmpty()) ? prefs.getInt(Utilidades.SHARED_FILTER_FICHAS_YEAR, temporadaList.size() - 1) : id_temporadas.indexOf(marca_especial_temporada));
        }
    }


    private void cargarLista(List<FichasCompletas> fichasCompletas) {

        if (activity != null) {
            if (Utilidades.isLandscape(activity)) {
                id_lista_fichas.setLayoutManager(new GridLayoutManager(activity, 2));
            } else {
                id_lista_fichas.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            }
        }

        id_lista_fichas.setHasFixedSize(true);


        fichasAdapter = new FichasAdapter(fichasCompletas, new FichasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FichasCompletas fichas) {
                activity.cambiarFragment(FragmentCreaFicha.getInstance(fichas), Utilidades.FRAGMENT_CREA_FICHA, R.anim.slide_in_left, R.anim.slide_out_left);

            }
        }, new FichasAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(FichasCompletas fichas) {
                if (fichas.getFichas().getActiva() == 1) {
                    avisoActivaFicha("Seleccione estado del prospecto", fichas);
                } else {
                    Utilidades.avisoListo(activity, "Prospecto ya activado", "No se puede modificar en tableta, solo en web y por un administrador", "entiendo");
                }

            }
        }, activity);


        id_lista_fichas.setAdapter(fichasAdapter);

    }


    private void avisoCreaNuevaFicha(String title, String message) {
        View viewInfalted = LayoutInflater.from(getActivity()).inflate(R.layout.alert_empty, null);

        final AlertDialog builder = new AlertDialog.Builder(activity)
                .setView(viewInfalted)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getResources().getString(R.string.button_create), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setNegativeButton(getResources().getString(R.string.nav_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create();

        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                Button c = builder.getButton(AlertDialog.BUTTON_NEGATIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (activity != null) {
                            activity.cambiarFragment(new FragmentCreaFicha(), Utilidades.FRAGMENT_CREA_FICHA, R.anim.slide_in_left, R.anim.slide_out_left);
                        }
                        builder.dismiss();
                    }
                });
                c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder.dismiss();
                    }
                });
            }
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


        final AlertDialog builder = new AlertDialog.Builder(activity)
                .setView(viewInfalted)
                .setTitle(title)
                .setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create();

        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                Button c = builder.getButton(AlertDialog.BUTTON_NEGATIVE);


                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fichas fichas = completas.getFichas();
                        if (fichas != null) {

                            if (rb.isChecked() && !TextUtils.isEmpty(fichas.getOferta_negocio()) && !TextUtils.isEmpty(fichas.getLocalidad()) && fichas.getHas_disponible() > 0.0
                                    && !TextUtils.isEmpty(fichas.getPredio_ficha()) && !TextUtils.isEmpty(fichas.getPotrero_ficha()) && !TextUtils.isEmpty(fichas.getMaleza())
                                    && !TextUtils.isEmpty(fichas.getEstado_general_ficha()) && !TextUtils.isEmpty(fichas.getFecha_limite_siembra_ficha()) && !TextUtils.isEmpty(fichas.getObservaciones())
                                    && !TextUtils.isEmpty(fichas.getObservacion_negocio_ficha()) && fichas.getNorting() != 0.0 && fichas.getEasting() != 0.0 && !TextUtils.isEmpty(fichas.getId_tipo_tenencia_maquinaria())
                                    && !TextUtils.isEmpty(fichas.getId_tipo_tenencia_terreno()) && !TextUtils.isEmpty(fichas.getExperiencia()) && !TextUtils.isEmpty(fichas.getId_tipo_riego())
                                    && !TextUtils.isEmpty(fichas.getId_tipo_suelo()) && !TextUtils.isEmpty(fichas.getEspecie_ficha())) {

                                int estado = (ra.isChecked()) ? 1 : (rb.isChecked()) ? 3 : 3;
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
                    }
                });
                c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder.dismiss();
                    }
                });
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

}
