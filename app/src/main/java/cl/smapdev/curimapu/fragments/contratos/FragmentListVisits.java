package cl.smapdev.curimapu.fragments.contratos;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.VisitasListAdapter;
import cl.smapdev.curimapu.clases.adapters.WeatherAdapter;
import cl.smapdev.curimapu.clases.modelo.EvaluacionAnterior;
import cl.smapdev.curimapu.clases.modelo.WeatherApiRequest;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.relaciones.VisitasCompletas;
import cl.smapdev.curimapu.clases.tablas.AnexoContrato;
import cl.smapdev.curimapu.clases.tablas.AnexoVilab;
import cl.smapdev.curimapu.clases.tablas.Fotos;
import cl.smapdev.curimapu.clases.tablas.WeatherApi;
import cl.smapdev.curimapu.clases.tablas.WeatherApiStatus;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.checklist.FragmentCheckList;
import cl.smapdev.curimapu.fragments.estacion_floracion.FragmentListaEstacionFloracion;
import cl.smapdev.curimapu.fragments.muestra_humedad.FragmentListaMuestraHumedad;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentListVisits extends Fragment {

    private SharedPreferences prefs;
    private MainActivity activity;
    private RecyclerView lista_visitas;
    private ImageView ic_collapse, img_vilab;
    private AnexoContrato anexoContrato = null;
    private Button btn_nueva_visita, btn_carpeta_virtual, btn_estacion_floracion, btn_muestra_humedad;
    private ConstraintLayout contenedor_vilab;
    private TextView fecha_ndvi, indicador_ndvi;
    private TextView lbl_titulo_comuna;

    private TextView fecha_grafico;
    private ImageView img_grafico;

    private RecyclerView weather_list;

    private WeatherAdapter weatherAdapter;

    private List<VisitasCompletas> visitasCompletas = Collections.emptyList();

    private ExecutorService executors;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            activity = (MainActivity) context;
            prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
        } else {
            throw new RuntimeException(context + " must be MainActivity");
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executors != null && !executors.isShutdown()) {
            executors.shutdown();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        executors = Executors.newSingleThreadExecutor();

        ejecutarSeguro(() -> {
            visitasCompletas = MainActivity
                    .myAppDB
                    .myDao()
                    .getVisitasCompletasWithFotos(
                            prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, "")
                    );

            anexoContrato = MainActivity.myAppDB.myDao().getAnexos(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));

            handler.post(this::cargarListaGrande);
        });

    }

    private void ejecutarSeguro(Runnable r) {
        if (executors == null || executors.isShutdown() || executors.isTerminated()) {
            executors = Executors.newSingleThreadExecutor();
        }
        executors.execute(r);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista_visitas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lista_visitas = view.findViewById(R.id.lista_visitas);
        ic_collapse = view.findViewById(R.id.ic_collapse);
        btn_nueva_visita = view.findViewById(R.id.btn_nueva_visita);
        btn_carpeta_virtual = view.findViewById(R.id.btn_carpeta_virtual);
        btn_estacion_floracion = view.findViewById(R.id.btn_estacion_floracion);
        btn_muestra_humedad = view.findViewById(R.id.btn_muestra_humedad);

        weather_list = view.findViewById(R.id.weather_list);
        lbl_titulo_comuna = view.findViewById(R.id.lbl_titulo_comuna);
        img_vilab = view.findViewById(R.id.img_vilab);
        fecha_ndvi = view.findViewById(R.id.fecha_ndvi);
        img_grafico = view.findViewById(R.id.img_grafico);
        indicador_ndvi = view.findViewById(R.id.indicador_ndvi);
        contenedor_vilab = view.findViewById(R.id.contenedor_vilab);


        TextView txt_titulo_selected = view.findViewById(R.id.txt_titulo_selected);

        txt_titulo_selected.setText(R.string.visitas_anteriores);
        txt_titulo_selected.setOnClickListener(view1 -> {
            ic_collapse.setImageDrawable((lista_visitas.getVisibility() == View.VISIBLE)
                    ? ContextCompat.getDrawable(requireContext(), R.drawable.ic_expand_down)
                    : ContextCompat.getDrawable(requireContext(), R.drawable.ic_expand_up)
            );
            lista_visitas.setVisibility((lista_visitas.getVisibility() == View.VISIBLE)
                    ? View.GONE
                    : View.VISIBLE);
        });

        ic_collapse.setOnClickListener(view1 -> {
            ic_collapse.setImageDrawable((lista_visitas.getVisibility() == View.VISIBLE)
                    ? ContextCompat.getDrawable(requireContext(), R.drawable.ic_expand_down)
                    : ContextCompat.getDrawable(requireContext(), R.drawable.ic_expand_up)
            );
            lista_visitas.setVisibility((lista_visitas.getVisibility() == View.VISIBLE)
                    ? View.GONE
                    : View.VISIBLE);
        });

        btn_carpeta_virtual.setOnClickListener(view1 -> activity.cambiarFragment(
                new FragmentCheckList(),
                Utilidades.FRAGMENT_CHECKLIST,
                R.anim.slide_in_left, R.anim.slide_out_left
        ));

        btn_estacion_floracion.setOnClickListener(view1 -> activity.cambiarFragment(
                new FragmentListaEstacionFloracion(),
                Utilidades.FRAGMENT_ESTACION_FLORACION,
                R.anim.slide_in_left, R.anim.slide_out_left
        ));


        btn_muestra_humedad.setOnClickListener(view1 -> activity.cambiarFragment(
                new FragmentListaMuestraHumedad(),
                Utilidades.FRAGMENT_MUESTRA_HUMEDAD,
                R.anim.slide_in_left, R.anim.slide_out_left
        ));


        LinearLayoutManager lManagerVisitas = null;
        if (activity != null) {
            lManagerVisitas = new LinearLayoutManager(
                    activity,
                    LinearLayoutManager.HORIZONTAL,
                    false
            );

            btn_nueva_visita.setOnClickListener(view1 -> nuevaVisita(anexoContrato));
        }
        weather_list.setHasFixedSize(true);
        weather_list.setLayoutManager(lManagerVisitas);


        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem item) {
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.CREATED);


        Utilidades.setToolbar(activity, view, getResources().getString(R.string.app_name), (anexoContrato != null) ? " Resumen Anexo " + anexoContrato.getAnexo_contrato() : getResources().getString(R.string.subtitles_visit));


        ejecutarSeguro(() -> {
            AnexoCompleto anexoCompleto = MainActivity
                    .myAppDB
                    .myDao()
                    .getAnexoCompletoById(anexoContrato.getId_anexo_contrato());

            if (anexoCompleto == null) {
                return;
            }

            handler.post(() -> {
                if (anexoCompleto.getComuna() != null && anexoCompleto.getComuna().getId_api() != null) {

                    lbl_titulo_comuna.setText(anexoCompleto.getComuna().getDesc_comuna());

                    WeatherApiRequest weatherApiRequest = new WeatherApiRequest(anexoCompleto.getComuna().getId_api());
                    Call<WeatherApiStatus> call = weatherApiRequest.obtenerData();
                    call.enqueue(new Callback<WeatherApiStatus>() {
                        @Override
                        public void onResponse(@NonNull Call<WeatherApiStatus> call, @NonNull Response<WeatherApiStatus> response) {

                            if (response.errorBody() != null) {
                                Toasty.error(requireActivity(),
                                        "No se pudo obtener clima", Toast.LENGTH_LONG, true).show();
                                return;
                            }

                            if (response.code() == 200 && response.isSuccessful()) {

                                WeatherApiStatus status = response.body();

                                if (status == null) {
                                    Toasty.error(requireActivity(),
                                            "Respuesta nula", Toast.LENGTH_LONG, true).show();
                                    return;
                                }

                                if (status.getStatus() != 0) {
                                    Toasty.error(requireActivity(),
                                            "No se pudo obtener clima", Toast.LENGTH_LONG, true).show();
                                    return;
                                }

                                List<WeatherApi> weatherApiList = new ArrayList<>();
                                weatherApiList.add(status.getDay().getDayOne());
                                weatherApiList.add(status.getDay().getDayTwo());
                                weatherApiList.add(status.getDay().getDayThree());
                                weatherApiList.add(status.getDay().getDayFour());
                                weatherApiList.add(status.getDay().getDayFive());
                                weatherAdapter = new WeatherAdapter(weatherApiList);
                                weather_list.setAdapter(weatherAdapter);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<WeatherApiStatus> call, @NonNull Throwable t) {
                            Toasty.error(requireActivity(),
                                    "No se pudo obtener clima", Toast.LENGTH_LONG, true).show();
                        }
                    });
                }

                AnexoVilab vilab = MainActivity.myAppDB.DaoVilab().getVilabByAc(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));
                if (vilab != null) {
                    String ndvi = (vilab.promedio_vilab == null) ? "n/a" : vilab.promedio_vilab;
                    contenedor_vilab.setVisibility(View.VISIBLE);
                    Picasso.get().load("file://" + requireActivity().getFilesDir() + "/imagenes_vilab/" + vilab.nombre_imagen).memoryPolicy(MemoryPolicy.NO_CACHE).into(img_vilab);
                    indicador_ndvi.setText("NDVI: " + ndvi);
                    fecha_ndvi.setText("Fecha: " + vilab.fecha_imagen_ndvi);
                }

                Picasso.get().load("file://" + requireActivity().getFilesDir() + "/imagenes_grafico/" + anexoCompleto.getAnexoContrato().getAnexo_contrato() + ".jpg").memoryPolicy(MemoryPolicy.NO_CACHE).into(img_grafico);
            });
        });
    }


    private void cargarListaGrande() {
        LinearLayoutManager lManagerVisitas = null;
        if (activity != null) {
            lManagerVisitas = new LinearLayoutManager(
                    activity,
                    LinearLayoutManager.VERTICAL,
                    false
            );
        }
        lista_visitas.setHasFixedSize(true);
        lista_visitas.setLayoutManager(lManagerVisitas);


        VisitasListAdapter visitasListAdapter = new VisitasListAdapter(
                visitasCompletas,
                (view, fichas) ->
                        showAlertForEdit(fichas), (view, fichas) ->
                avisoActivaFicha(
                        "Esta a punto de eliminar esta visita para el anexo " +
                                fichas.getAnexoCompleto()
                                        .getAnexoContrato()
                                        .getAnexo_contrato(),
                        "esta visita realizada el dia " +
                                fichas.getVisitas().getFecha_visita() +
                                " se eliminara completamente de la tableta, no se subira a servidor tampoco",
                        fichas
                ),
                activity
        );
        lista_visitas.setAdapter(visitasListAdapter);
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        if (activity != null) {
//            activity.updateView(getResources().getString(R.string.app_name), (anexoContrato != null) ? " Resumen Anexo " + anexoContrato.getAnexo_contrato() : getResources().getString(R.string.subtitles_visit));
//        }
//    }

    public void nuevaVisita(AnexoContrato anexo) {

        if (prefs != null) {
            prefs.edit().putInt(Utilidades.SHARED_VISIT_FICHA_ID, anexo.getId_ficha_contrato()).apply();
            prefs.edit().putString(Utilidades.SHARED_VISIT_MATERIAL_ID, anexo.getId_especie_anexo()).apply();
            prefs.edit().putString(Utilidades.SHARED_VISIT_ANEXO_ID, anexo.getId_anexo_contrato()).apply();
            prefs.edit().putString(Utilidades.SHARED_VISIT_TEMPORADA, anexo.getTemporada_anexo()).apply();
            prefs.edit().putInt(Utilidades.SHARED_VISIT_VISITA_ID, 0).apply();
        }

        ejecutarSeguro(() -> {
            AnexoCompleto acCompleto = MainActivity.myAppDB.myDao().getAnexoCompletoById(anexo.getId_anexo_contrato());
            MainActivity.myAppDB.myDao().deleteTempVisitas();
            MainActivity.myAppDB.myDao().deleteDetalleVacios();

            List<Fotos> fotos = MainActivity.myAppDB.myDao().getFotosByIdVisita(0);
            if (!fotos.isEmpty()) {
                for (Fotos fts : fotos) {
                    try {
                        File file = new File(fts.getRuta());
                        if (!file.exists()) continue;
                        boolean eliminado = file.delete();
                        if (eliminado) {
                            MainActivity.myAppDB.myDao().deleteFotos(fts);
                        }
                    } catch (Exception e) {
                        Log.e("ERROR DELETING", Objects.requireNonNull(e.getMessage()));
                    }
                }
            }

            handler.post(() -> {
                activity.cambiarFragment(FragmentFormVisitas.newInstance(
                        acCompleto,
                        null,
                        new EvaluacionAnterior(0, 0.0f, ""),
                        anexo.getId_especie_anexo(),
                        anexo.getTemporada_anexo()
                ), Utilidades.FRAGMENT_CONTRATOS, R.anim.slide_in_left, R.anim.slide_out_left);
            });
        });
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    private void showAlertForEdit(final VisitasCompletas visitasCompletas) {
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_empty, null);


        final AlertDialog builder = new AlertDialog.Builder(requireActivity())
                .setView(viewInfalted)
                .setTitle(getResources().getString(R.string.atencion))
                .setMessage(getResources().getString(R.string.mensaje_alerta_editar_visita))
                .setPositiveButton(getResources().getString(R.string.entiendo), (dialogInterface, i) -> {
                })
                .setNegativeButton(getResources().getString(R.string.nav_cancel), (dialogInterface, i) -> {
                })
                .create();


        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
            Button c = builder.getButton(AlertDialog.BUTTON_NEGATIVE);
            b.setOnClickListener(v -> {
                ejecutarSeguro(() -> {
                    try {
                        MainActivity.myAppDB.myDao().deleteTempVisitas();
                        MainActivity.myAppDB.myDao().deleteDetalleVacios();
                        List<Fotos> fotos = MainActivity.myAppDB.myDao().getFotosByIdVisita(0);
                        if (!fotos.isEmpty()) {
                            for (Fotos fts : fotos) {
                                try {
                                    File file = new File(fts.getRuta());
                                    if (file.exists()) {
                                        boolean eliminado = file.delete();
                                        if (eliminado) {
                                            MainActivity.myAppDB.myDao().deleteFotos(fts);
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.e("ERROR DELETING", Objects.requireNonNull(e.getMessage()));
                                }
                            }
                        }

                        handler.post(() -> {
                            if (prefs != null) {
                                prefs.edit().putInt(Utilidades.SHARED_VISIT_FICHA_ID, visitasCompletas.getAnexoCompleto().getAnexoContrato().getId_ficha_contrato()).apply();
                                prefs.edit().putString(Utilidades.SHARED_VISIT_ANEXO_ID, visitasCompletas.getVisitas().getId_anexo_visita()).apply();
                                prefs.edit().putInt(Utilidades.SHARED_VISIT_VISITA_ID, visitasCompletas.getVisitas().getId_visita()).apply();
                            }

                            activity.cambiarFragment(FragmentFormVisitas.newInstance(
                                    visitasCompletas.getAnexoCompleto(),
                                    visitasCompletas,
                                    new EvaluacionAnterior(0, 0.0f, ""),
                                    visitasCompletas.getAnexoCompleto().getAnexoContrato().getId_especie_anexo(),
                                    visitasCompletas.getAnexoCompleto().getAnexoContrato().getTemporada_anexo()
                            ), Utilidades.FRAGMENT_CONTRATOS, R.anim.slide_in_left, R.anim.slide_out_left);
                            builder.dismiss();
                        });

                    } catch (SQLiteException e) {
                        handler.post(() -> Toasty.error(requireActivity(), "No se pudo realizar la acción", Toast.LENGTH_LONG, true).show());
                    }
                });
            });
            c.setOnClickListener(view -> builder.dismiss());
        });
        builder.setCancelable(false);
        builder.show();
    }


    private void avisoActivaFicha(String title, String message, final VisitasCompletas completas) {
        final View viewInfalted = LayoutInflater.from(getActivity()).inflate(R.layout.alert_empty, null);


        final AlertDialog builder = new AlertDialog.Builder(requireActivity())
                .setView(viewInfalted)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Eliminar", (dialogInterface, i) -> {
                })
                .setNegativeButton(getResources().getString(R.string.nav_cancel), (dialogInterface, i) -> {
                })
                .create();

        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
            Button c = builder.getButton(AlertDialog.BUTTON_NEGATIVE);

            b.setOnClickListener(v -> {

                ejecutarSeguro(() -> {
                    MainActivity.myAppDB.myDao().deleteDetallesByVisita(completas.getVisitas().getId_visita());
                    MainActivity.myAppDB.myDao().deleteFotosByVisita(completas.getVisitas().getId_visita());
                    MainActivity.myAppDB.myDao().deleteVisita(completas.getVisitas().getId_visita());
                    visitasCompletas = MainActivity.myAppDB.myDao().getVisitasCompletasWithFotos(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
                    handler.post(() -> {
                        cargarListaGrande();
                        builder.dismiss();
                    });

                });
            });
            c.setOnClickListener(view -> builder.dismiss());
        });

        builder.setCancelable(false);
        builder.show();
    }

}
