package cl.smapdev.curimapu.fragments.anexoFechas;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.AnexosFechasAdapter;
import cl.smapdev.curimapu.clases.adapters.SpinnerToolbarAdapter;
import cl.smapdev.curimapu.clases.modelo.AnexoCorreoFechaSync;
import cl.smapdev.curimapu.clases.relaciones.AnexoWithDates;
import cl.smapdev.curimapu.clases.relaciones.RespuestaFecha;
import cl.smapdev.curimapu.clases.relaciones.SubirFechasRetro;
import cl.smapdev.curimapu.clases.relaciones.resFecha;
import cl.smapdev.curimapu.clases.retrofit.ApiService;
import cl.smapdev.curimapu.clases.retrofit.RetrofitClient;
import cl.smapdev.curimapu.clases.tablas.AnexoCorreoFechas;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Temporada;
import cl.smapdev.curimapu.clases.utilidades.InternetStateClass;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.dialogos.DialogAnexoFecha;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAnexoFechas extends Fragment {

    private View view;

    private MainActivity activity;
    private SharedPreferences prefs;
    private Spinner spinner_toolbar;

    private List<Temporada> temporadaList;
    private final ArrayList<String> id_temporadas = new ArrayList<>();
    private final ArrayList<String> desc_temporadas = new ArrayList<>();

    private String marca_especial_temporada;

    private RecyclerView rv_anexo_fecha;
    private AnexosFechasAdapter anexosFechasAdapter;

    private SearchView search_anexo_fecha;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (MainActivity) getActivity();
        if (activity != null) {
            prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_anexo_fecha, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinner_toolbar = view.findViewById(R.id.spinner_toolbar);
        search_anexo_fecha = view.findViewById(R.id.search_anexo_fecha);

        rv_anexo_fecha = view.findViewById(R.id.rv_anexo_fecha);
        LinearLayoutManager lManager = null;
        if (activity != null) {
            lManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        }
        rv_anexo_fecha.setHasFixedSize(true);
        rv_anexo_fecha.setLayoutManager(lManager);

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

        spinner_toolbar.setAdapter(new SpinnerToolbarAdapter(activity, R.layout.spinner_template_toolbar_view, temporadaList));


        if (marca_especial_temporada != null && !marca_especial_temporada.isEmpty() && spinner_toolbar != null) {
            spinner_toolbar.setSelection(id_temporadas.indexOf(marca_especial_temporada));
        }

        spinner_toolbar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (spinner_toolbar.getTag() == null) {
                    prefs.edit().putString(Utilidades.SELECTED_ANO, id_temporadas.get(spinner_toolbar.getSelectedItemPosition())).apply();
                    prefs.edit().putInt(Utilidades.SHARED_FILTER_VISITAS_YEAR, i).apply();

                    cargarLista(id_temporadas.get(spinner_toolbar.getSelectedItemPosition()), search_anexo_fecha.getQuery().toString());

                    return;
                }

                if (Integer.parseInt(spinner_toolbar.getTag().toString()) == i) {
                    spinner_toolbar.setTag(null);
                    return;
                }


                prefs.edit().putString(Utilidades.SELECTED_ANO, id_temporadas.get(spinner_toolbar.getSelectedItemPosition())).apply();
                prefs.edit().putInt(Utilidades.SHARED_FILTER_VISITAS_YEAR, i).apply();

                cargarLista(id_temporadas.get(spinner_toolbar.getSelectedItemPosition()), search_anexo_fecha.getQuery().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if (temporadaList.size() > 0) {
            cargarLista(prefs.getString(Utilidades.SELECTED_ANO, temporadaList.get(temporadaList.size() - 1).getId_tempo_tempo()), search_anexo_fecha.getQuery().toString());
        }


        search_anexo_fecha.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                cargarLista(id_temporadas.get(spinner_toolbar.getSelectedItemPosition()), query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                cargarLista(id_temporadas.get(spinner_toolbar.getSelectedItemPosition()), newText);
                return false;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        menu.clear();
        inflater.inflate(R.menu.menu_sube_fechas, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_upload_files:
                prepararSubirFechas();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public void cargarLista(String fecha, String query) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<AnexoWithDates>> anexoCompleto;

        if (query.isEmpty()) {
            anexoCompleto = executor.submit(() -> MainActivity.myAppDB.DaoAnexosFechas().getFechasSag(fecha));
        } else {
            anexoCompleto = executor.submit(() -> MainActivity.myAppDB.DaoAnexosFechas().getFechasSag(fecha, "%" + query + "%"));
        }
        try {
            crearAdaptador(anexoCompleto.get());
            executor.shutdown();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void crearAdaptador(List<AnexoWithDates> anexo) {
        anexosFechasAdapter = new AnexosFechasAdapter(anexo,
                (view1, anexos) -> {
                    FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                    Fragment prev = requireActivity().getSupportFragmentManager().findFragmentByTag("EDICION_ANEXO_FECHA");
                    if (prev != null) {
                        ft.remove(prev);
                    }

                    DialogAnexoFecha dialogo = DialogAnexoFecha.newInstance(anexos, (saved, query) -> {
                        search_anexo_fecha.setQuery(query, true);
                    }, search_anexo_fecha.getQuery().toString());
                    dialogo.show(ft, "EDICION_ANEXO_FECHA");
                },
                getContext()
        );

        rv_anexo_fecha.setAdapter(anexosFechasAdapter);
    }


    private void prepararSubirFechas() {

        InternetStateClass mm = new InternetStateClass(activity, result -> {
            if (!result) {
                Toasty.error(activity, activity.getResources().getString(R.string.sync_not_internet), Toast.LENGTH_SHORT, true).show();
                return;
            }


            ProgressDialog pd = new ProgressDialog(activity);
            pd.setMessage("conectandose a internet, espere por favor");
            pd.show();

            List<AnexoCorreoFechas> fechas = MainActivity.myAppDB.DaoAnexosFechas().getAnexoCorreosFechasSincro();

            if (fechas == null || fechas.size() <= 0) {
                if (pd.isShowing()) pd.dismiss();
                Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                return;
            }

            if (pd.isShowing()) {
                pd.dismiss();
            }


            new AnexoCorreoFechaSync(fechas, requireActivity(), (state, message) -> {
                if (state) {
                    Toasty.success(requireActivity(), message, Toast.LENGTH_LONG, true).show();
                    cargarLista(id_temporadas.get(spinner_toolbar.getSelectedItemPosition()), search_anexo_fecha.getQuery().toString());
                } else {
                    Toasty.error(requireActivity(), message, Toast.LENGTH_LONG, true).show();
                }
            });

//            subirFechas(fechas);

        }, 1);
        mm.execute();
    }


    public void subirFechas(List<AnexoCorreoFechas> fechas) {

        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Preparando datos para subir...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Config config = MainActivity.myAppDB.myDao().getConfig();

        SubirFechasRetro sfp = new SubirFechasRetro();
        sfp.setId_dispo(config.getId());
        sfp.setId_usuario(config.getId_usuario());
        sfp.setFechas(fechas);

        ApiService apiService = RetrofitClient.getClient(config.getServidorSeleccionado()).create(ApiService.class);
        Call<resFecha> call = apiService.enviarFechas(sfp);
        call.enqueue(new Callback<resFecha>() {
            @Override
            public void onResponse(Call<resFecha> call, Response<resFecha> response) {

                resFecha res = response.body();

                if (res != null) {

                    switch (res.getCodigoRespuesta()) {
                        case 0:
                            if (res.getRespuestaFechas() != null && res.getRespuestaFechas().size() > 0) {

                                boolean problema = false;
                                StringBuilder msg = new StringBuilder();

                                for (RespuestaFecha fc : res.getRespuestaFechas()) {

                                    AnexoCorreoFechas acf = MainActivity.myAppDB.DaoAnexosFechas().getAnexoCorreoFechasByAnexo(Integer.parseInt(fc.getAnexo()));
                                    if (acf != null) {
                                        acf.setCorreo_termino_labores_post_cosechas(fc.getCorreo_termino_labores());
                                        acf.setCorreo_termino_cosecha(fc.getCorreo_termino_cosecha());
                                        acf.setCorreo_inicio_despano(fc.getCorreo_inicio_despano());
                                        acf.setCorreo_inicio_cosecha(fc.getCorreo_inicio_cosecha());
                                        acf.setCorreo_inicio_corte_seda(fc.getCorreo_inicio_corte_seda());
                                        acf.setCorreo_cinco_porciento_floracion(fc.getCorreo_cinco_porciento());
                                        acf.setDetalle_labores(fc.getDetalle());

                                        try {
                                            MainActivity.myAppDB.DaoAnexosFechas().UpdateFechasAnexos(acf);
                                        } catch (Exception e) {
                                            problema = true;
                                            msg.append(e.getLocalizedMessage());
                                        }
                                    }
                                }


                                if (problema) {
                                    Toasty.error(activity, msg, Toast.LENGTH_LONG, true).show();
                                } else {
                                    Toasty.success(activity, "correos enviados con exito", Toast.LENGTH_LONG, true).show();
                                }

                            }
//                            cargarInforme(0);
                            progressDialog.dismiss();
                            break;
                        default:
                            Toasty.error(activity, res.getMensaje(), Toast.LENGTH_SHORT, true).show();
                            System.out.println(res.getMensaje());
                            progressDialog.dismiss();
                            break;
                    }

                } else {
                    Toasty.error(activity, "Resupuesta nula del servidor", Toast.LENGTH_SHORT, true).show();
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<resFecha> call, Throwable t) {
                Toasty.error(activity, (t.getLocalizedMessage() != null) ? t.getLocalizedMessage() : "Problemas con el servicio", Toast.LENGTH_LONG, true).show();
                progressDialog.dismiss();
            }
        });


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.updateView(getResources().getString(R.string.app_name), "Anexos Fechas");
        }
    }
}
