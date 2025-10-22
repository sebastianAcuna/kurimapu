package cl.smapdev.curimapu.fragments.contratos;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import cl.smapdev.curimapu.BuildConfig;
import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.FotosListAdapter;
import cl.smapdev.curimapu.clases.adapters.MultipleSelectSpinner;
import cl.smapdev.curimapu.clases.adapters.SpinnerAdapter;
import cl.smapdev.curimapu.clases.modelo.EvaluacionAnterior;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.relaciones.SpinnerItem;
import cl.smapdev.curimapu.clases.relaciones.VisitasCompletas;
import cl.smapdev.curimapu.clases.tablas.AnexoContrato;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Evaluaciones;
import cl.smapdev.curimapu.clases.tablas.Fotos;
import cl.smapdev.curimapu.clases.tablas.Visitas;
import cl.smapdev.curimapu.clases.tablas.detalle_visita_prop;
import cl.smapdev.curimapu.clases.utilidades.CameraUtils;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.FragmentVisitas;
import cl.smapdev.curimapu.fragments.dialogos.DialogBuscaObsRecAnterior;
import cl.smapdev.curimapu.fragments.dialogos.DialogLibroCampo;
import cl.smapdev.curimapu.fragments.dialogos.DialogObservationTodo;
import cl.smapdev.curimapu.infraestructure.utils.coroutines.ApplicationExecutors;
import es.dmoral.toasty.Toasty;

public class FragmentFormVisitas extends Fragment {

    //obligatorio
    private VisitasCompletas visitasCompletas;
    private AnexoCompleto anexoCompleto;
    private String variedad, temporada;
    private MainActivity activity;
    private SharedPreferences prefs;
    private EvaluacionAnterior evaluacionAnterior = null;

    //UI
    private Spinner sp_fenologico, sp_cosecha, sp_crecimiento, sp_fito,
            sp_general_cultivo, sp_humedad, sp_malezas, sp_planta_voluntaria;
    private TextView titulo_raices;
    private Button btn_guardar, btn_volver, btn_buscar_obs;
    private ConstraintLayout contenedor_estados, contenedor_monitoreo;
    private TextInputLayout obs_growth, obs_weed, obs_fito, obs_harvest, obs_overall, obs_humedad;
    private EditText et_obs, et_obs_growth, et_obs_weed, et_obs_fito, et_obs_harvest,
            et_obs_overall, et_obs_humedad, et_percent_humedad, et_fecha_estimada, et_fecha_arranca, et_fecha_estimada_postura_abejas;
    private RecyclerView rwAgronomo, rwCliente, rwRaices;
    private FloatingActionButton material_private, material_public, foto_raices;
    private FotosListAdapter adapterAgronomo, adapterCliente, adapterRaices;


    private String currentPhotoPath, medidaRaices;

    private final ArrayList<String> fenologico = new ArrayList<>(),
            planta_voluntaria = new ArrayList<>(), cosecha = new ArrayList<>(),
            crecimiento = new ArrayList<>(), maleza = new ArrayList<>();

    private ExecutorService executors = Executors.newSingleThreadExecutor();

    /* IMAGENES */
    private static final int COD_FOTO = 005;
    private File fileImagen;
    private ProgressDialog progressBar;

    public void setAnexoCompleto(AnexoCompleto anexoCompleto) {
        this.anexoCompleto = anexoCompleto;
    }

    public void setVisitasCompletas(VisitasCompletas visitasCompletas) {
        this.visitasCompletas = visitasCompletas;
    }

    public void setVariedad(String variedad) {
        this.variedad = variedad;
    }

    public void setTemporada(String temporada) {
        this.temporada = temporada;
    }

    public void setEvaluacionAnterior(EvaluacionAnterior evaluacionAnterior) {
        this.evaluacionAnterior = evaluacionAnterior;
    }

    public static FragmentFormVisitas newInstance(
            AnexoCompleto anexoCompleto,
            VisitasCompletas visitasCompletas,
            EvaluacionAnterior evaluacionAnterior,
            String variedad,
            String temporada
    ) {
        FragmentFormVisitas fg = new FragmentFormVisitas();
        fg.setVisitasCompletas(visitasCompletas);
        fg.setTemporada(temporada);
        fg.setVariedad(variedad);
        fg.setAnexoCompleto(anexoCompleto);
        if (evaluacionAnterior != null) {
            fg.setEvaluacionAnterior(evaluacionAnterior);
        }
        return fg;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity a = (MainActivity) getActivity();
        if (a != null) activity = a;

        progressBar = new ProgressDialog(activity);
        progressBar.setTitle(getResources().getString(R.string.espere));
        progressBar.show();

        ApplicationExecutors exec = new ApplicationExecutors();
        exec.getBackground().execute(() -> {
            prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
            planta_voluntaria.addAll(Arrays.asList(getResources().getStringArray(R.array.plantas_voluntarias)));
            fenologico.addAll(Arrays.asList(getResources().getStringArray(R.array.fenologico)));
            cosecha.addAll(Arrays.asList(getResources().getStringArray(R.array.cosecha)));
            crecimiento.addAll(Arrays.asList(getResources().getStringArray(R.array.crecimiento)));
            maleza.addAll(Arrays.asList(getResources().getStringArray(R.array.maleza)));
        });
        exec.shutDownBackground();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contrato, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind(view);
        setHasOptionsMenu(true);

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (!checkPermission()) {
                requestPermission();
            }
        }
        ApplicationExecutors exec = new ApplicationExecutors();
        exec.getBackground().execute(() -> exec.getMainThread().execute(this::chargeAll));
        exec.shutDownBackground();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_visitas, menu);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (activity == null) return;
        agregarImagenToAgronomos();
        agregarImagenToClientes();
        agregarImagenToRaices();
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toasty.normal(requireActivity(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 100);
        }
    }

    private void cargarSpinners() {

        sp_fenologico.setAdapter(new SpinnerAdapter(activity, R.layout.spinner_template_view, fenologico));
        sp_cosecha.setAdapter(new SpinnerAdapter(activity, R.layout.spinner_template_view, cosecha));
        sp_planta_voluntaria.setAdapter(new SpinnerAdapter(activity, R.layout.spinner_template_view, planta_voluntaria));
        sp_crecimiento.setAdapter(new SpinnerAdapter(activity, R.layout.spinner_template_view, crecimiento));
        sp_fito.setAdapter(new SpinnerAdapter(activity, R.layout.spinner_template_view, crecimiento));
        sp_general_cultivo.setAdapter(new SpinnerAdapter(activity, R.layout.spinner_template_view, crecimiento));
        sp_humedad.setAdapter(new SpinnerAdapter(activity, R.layout.spinner_template_view, crecimiento));
        sp_malezas.setAdapter(new SpinnerAdapter(activity, R.layout.spinner_template_view, maleza));

        accionSpinners();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void modificarVista(boolean esMonitoreo) {
        contenedor_estados.setVisibility((esMonitoreo) ? View.GONE : View.VISIBLE);
        titulo_raices.setVisibility((esMonitoreo) ? View.GONE : View.VISIBLE);
        rwRaices.setVisibility((esMonitoreo) ? View.GONE : View.VISIBLE);
        contenedor_monitoreo.setVisibility((!esMonitoreo) ? View.GONE : View.VISIBLE);
    }

    private void abrirCamara(int vista, String medida) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;
        try {
            photoFile = Utilidades.createImageFile(requireActivity());
            currentPhotoPath = photoFile.getAbsolutePath();
        } catch (IOException e) {
            Toasty.error(requireActivity(), "No se pudo crear la imagen " + e.getMessage(), Toast.LENGTH_LONG, true).show();
        }

        if (photoFile == null) {
            Toasty.error(requireActivity(), "No se pudo crear la imagen (nula)", Toast.LENGTH_LONG, true).show();
            return;
        }

        prefs.edit().remove(Utilidades.VISTA_FOTOS).putInt(Utilidades.VISTA_FOTOS, vista).apply();
        medidaRaices = medida;

        Uri photoUri = FileProvider.getUriForFile(requireActivity(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, COD_FOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != COD_FOTO || resultCode != RESULT_OK) return;

        try {
            Bitmap originalBtm = BitmapFactory.decodeFile(currentPhotoPath);
            Bitmap nuevaFoto = CameraUtils.escribirFechaImg(originalBtm, activity);

            currentPhotoPath = currentPhotoPath.replaceAll("Pictures/", "");
            File file = new File(currentPhotoPath);
            FileOutputStream fos = new FileOutputStream(file);
            nuevaFoto.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            guardarBD();

        } catch (Exception e) {
            Toasty.error(requireActivity(), "Error al sacar foto " + e.getMessage(), Toast.LENGTH_LONG, true).show();
        }
    }

    private void guardarBD() {

        ApplicationExecutors exec = new ApplicationExecutors();
        try {
            exec.getBackground().execute(() -> {
                Fotos fotos = new Fotos();
                fotos.setFecha(Utilidades.fechaActualConHora());

                int vista = prefs.getInt(Utilidades.VISTA_FOTOS, 0);
                File file = new File(currentPhotoPath);

                fotos.setFieldbook(Utilidades.getPhenoState(sp_fenologico.getSelectedItemPosition()));
                fotos.setHora(Utilidades.hora());
                fotos.setNombre_foto(file.getName());
                fotos.setFavorita(false);
                fotos.setPlano(0);
                fotos.setMedida_raices(!medidaRaices.isEmpty() ? medidaRaices : "0");
                fotos.setAcepto_regla_raices((vista == 3) ? 1 : 0);

                if (anexoCompleto != null) {
                    fotos.setId_ficha_fotos(anexoCompleto.getAnexoContrato().getId_anexo_contrato());
                }
                fotos.setVista(vista);
                fotos.setRuta(currentPhotoPath);

                if (visitasCompletas != null) {
                    fotos.setId_visita_foto(visitasCompletas.getVisitas().getId_visita());
                }


                Config config = MainActivity.myAppDB.myDao().getConfig();
                if (config != null) {
                    fotos.setId_dispo_foto(config.getId());
                }

                MainActivity.myAppDB.myDao().insertFotos(fotos);

                exec.getMainThread().execute(() -> {
                    agregarImagenToAgronomos();
                    agregarImagenToClientes();
                    agregarImagenToRaices();
                });

            });
        } catch (Exception e) {
            Toasty.error(activity, "Error guardando foto" + e.getMessage(), Toast.LENGTH_LONG, true).show();
        } finally {
            exec.shutDownBackground();
        }
    }

    private void setOnSave() {

        executors.execute(() -> {
            try {
                List<Evaluaciones> evs = MainActivity.myAppDB.DaoEvaluaciones().getEvaluacionesByACObliga(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));
                if (evs.isEmpty()) {
                    requireActivity().runOnUiThread(() -> {
                        Utilidades.avisoListo(activity, "Falta algo", "Antes de guardar debes agregar una recomendacion para esta visita. ( estrella de arriba a la derecha )", "entiendo");
                    });
                    return;
                }
                List<AnexoCompleto> anexosSimilares = new ArrayList<>();
                if (sp_fenologico.getSelectedItem().toString().equals("MONITOREO")) {
                    anexosSimilares.addAll(MainActivity.myAppDB.myDao().getAnexoParaDuplicarVisita(
                            anexoCompleto.getAgricultor().getId_agricultor(),
                            anexoCompleto.getEspecie().getId_especie(),
                            anexoCompleto.getVariedad().getId_variedad(),
                            anexoCompleto.getAnexoContrato().getTemporada_anexo(),
                            anexoCompleto.getAnexoContrato().getId_anexo_contrato()
                    ));


                } else if (anexoCompleto.getEspecie().getId_especie().equals("13") || anexoCompleto.getEspecie().getId_especie().equals("14")) {
                    anexosSimilares.addAll(MainActivity.myAppDB.myDao().getAnexoParaDuplicarVisita(
                            anexoCompleto.getAgricultor().getId_agricultor(),
                            anexoCompleto.getEspecie().getId_especie(),
                            anexoCompleto.getAnexoContrato().getTemporada_anexo(),
                            anexoCompleto.getAnexoContrato().getId_anexo_contrato()));
                }


                if (anexosSimilares.isEmpty()) {
                    requireActivity().runOnUiThread(() -> {
                        if (sp_fenologico.getSelectedItem().toString().equals("MONITOREO")) {
                            saveVisitaMonitoreo(new ArrayList<String>());
                        } else {
                            saveVisitaNormal(new ArrayList<String>());
                        }
                    });
                } else {
                    requireActivity().runOnUiThread(() -> {
                        showAlertSeleccionAnexos(anexosSimilares, anexoCompleto);
                    });
                }
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    Utilidades.avisoListo(activity, "Problema", "No se pudo obtener datos de la evaluacion, por favor, vuelva a abrir y nuevamente la evaluacion." + e.getMessage(), "entiendo");
                });
            }
        });

    }


    private void showAlertSeleccionAnexos(List<AnexoCompleto> anexos, AnexoCompleto anexoActual) {
        View viewInfalted = LayoutInflater.from(getActivity()).inflate(R.layout.alerta_seleccion_anexos, null);

        final AlertDialog builder = new AlertDialog.Builder(requireActivity())
                .setView(viewInfalted)
                .setTitle("SELECCIONA")
                .setPositiveButton("Continuar", (dialogInterface, i) -> {
                })
                .setNegativeButton(getResources().getString(R.string.nav_cancel), (dialogInterface, i) -> {
                })
                .create();


        TextView texto_descripcion = viewInfalted.findViewById(R.id.texto_descripcion);
        MultipleSelectSpinner spinner_anexos = viewInfalted.findViewById(R.id.spinner_anexos);

        SpinnerItem defaultItem = new SpinnerItem(0, "Ninguno");

        List<SpinnerItem> spinnerItems = anexos.stream().map((AnexoCompleto temp) -> new SpinnerItem(Integer.parseInt(temp.getAnexoContrato().getId_anexo_contrato()), temp.getAnexoContrato().getAnexo_contrato() + " (L:" + temp.getLotes().getNombre_lote() + ");(HA GPS:" + temp.getAnexoContrato().getHas_gps() + ");")).collect(Collectors.toList());
        if (!spinnerItems.isEmpty()) {
            spinner_anexos.setItems(spinnerItems);
            spinner_anexos.setDefaultText(defaultItem);
            spinner_anexos.setSelection(0);
        }
        texto_descripcion.setText("Para el agricultor \n(" + anexoActual.getAgricultor().getNombre_agricultor()
                + ")\nSe encontraron (" + anexos.size() + ") anexos aparte del anexo " + anexoActual.getAnexoContrato().getAnexo_contrato() + "\nLote:" + anexoActual.getLotes().getNombre_lote() + "\nHAS GPS: " + anexoActual.getAnexoContrato().getHas_gps() + ".\nseleccione los anexos que replicarán la visita.");

        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
            Button c = builder.getButton(AlertDialog.BUTTON_NEGATIVE);
            b.setOnClickListener(v -> {
                if (activity != null) {
                    List<String> anexosSeleccionados = spinner_anexos.getSelectedIds().stream().map(String::valueOf).collect(Collectors.toList());
                    if (sp_fenologico.getSelectedItem().toString().equals("MONITOREO")) {
                        saveVisitaMonitoreo(anexosSeleccionados);
                    } else {
                        saveVisitaNormal(anexosSeleccionados);
                    }
//                    descargando(spinnerMulti.getSelectedItems(), spinner_multi_esp.getSelectedItems());
                }
                builder.dismiss();
            });
            c.setOnClickListener(view -> builder.dismiss());
        });

        builder.setCancelable(false);
        builder.show();
    }

    private void cambiarFavorita(Fotos fotos) {

        if (fotos.isFavorita()) {
            Toasty.info(activity, getResources().getString(R.string.message_fav_remove), Toast.LENGTH_SHORT, true).show();
            fotos.setFavorita(false);
        } else {
            Toasty.info(activity, getResources().getString(R.string.message_fav), Toast.LENGTH_SHORT, true).show();
            fotos.setFavorita(true);
        }


        MainActivity.myAppDB.myDao().updateFavorita(fotos);
        if (adapterAgronomo != null) {
            adapterAgronomo.notifyDataSetChanged();
        }

        if (adapterCliente != null) {
            adapterCliente.notifyDataSetChanged();
        }

    }

    void cambiarSubtitulo() {
        if (activity != null && anexoCompleto != null) {
            activity.updateView(getResources().getString(R.string.app_name), "Anexo " + anexoCompleto.getAnexoContrato().getAnexo_contrato());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cambiarSubtitulo();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fileImagen != null) {
            outState.putParcelable("file_uri", Uri.fromFile(fileImagen));
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.getParcelable("file_uri") != null) {
            Uri ui = savedInstanceState.getParcelable("file_uri");
            if (ui != null && ui.getPath() != null) {
                fileImagen = new File(ui.getPath());
            }
        }


    }

    void showAlertForEliminarFoto(final Fotos foto) {

        final AlertDialog builder = new AlertDialog.Builder(activity)
                .setView(R.layout.alert_empty)
                .setPositiveButton("Eliminar",
                        (foto.getEstado_fotos() <= 0)
                                ? (dialogInterface, i) -> {
                        } : null
                )
                .setNegativeButton("cancelar", (dialogInterface, i) -> {
                })
                .setTitle("Confirmación")
                .setMessage("¿Estas seguro que deseas eliminar esta foto?").create();

        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
            Button c = builder.getButton(AlertDialog.BUTTON_NEGATIVE);

            b.setOnClickListener(view -> {
                try {
                    File file = new File(foto.getRuta());

                    if (!file.exists()) {
                        MainActivity.myAppDB.myDao().deleteFotos(foto);
                        agregarImagenToAgronomos();
                        agregarImagenToClientes();
                        agregarImagenToRaices();
                        Toasty.success(activity, "Foto eliminada con exito", Toast.LENGTH_SHORT, true).show();
                        builder.dismiss();
                        return;
                    }

                    if (!file.delete()) {
                        Toasty.warning(activity, "Por favor vuelva a intentarlo", Toast.LENGTH_SHORT, true).show();
                        builder.dismiss();
                        return;
                    }

                    MainActivity.myAppDB.myDao().deleteFotos(foto);
                    agregarImagenToAgronomos();
                    agregarImagenToClientes();
                    agregarImagenToRaices();
                    Toasty.success(activity, "Foto eliminada con exito", Toast.LENGTH_SHORT, true).show();
                    builder.dismiss();

                } catch (Exception e) {
                    MainActivity.myAppDB.myDao().deleteFotos(foto);
                    agregarImagenToAgronomos();
                    agregarImagenToClientes();
                    agregarImagenToRaices();
                    builder.dismiss();
                }
            });
            c.setOnClickListener(v -> builder.dismiss());
        });

        builder.setCancelable(false);
        builder.show();

    }

    private void showAlertForUpdate(final Fotos foto) {
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_big_img, null);

        final AlertDialog builder;
        builder = new AlertDialog.Builder(requireActivity())
                .setView(viewInfalted)
                .setNeutralButton("eliminar",
                        (foto.getEstado_fotos() <= 0)
                                ? (dialogInterface, i) -> {
                        }
                                : null
                )
                .setPositiveButton("cerrar", (dialogInterface, i) -> {
                }).create();

        final ImageView imageView = viewInfalted.findViewById(R.id.img_alert_foto);

        Picasso.get().load("file:///" + foto.getRuta()).into(imageView);
        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
            Button c = builder.getButton(AlertDialog.BUTTON_NEUTRAL);

            b.setOnClickListener(v -> builder.dismiss());


            c.setOnClickListener(view -> {
                if (foto.getEstado_fotos() > 0) {
                    Toasty.warning(activity, "No puedes eliminar la foto, esta visita ya se subio a servidor", Toast.LENGTH_SHORT, true).show();
                } else {
                    showAlertForEliminarFoto(foto);
                    builder.dismiss();
                }
            });
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void preguntarFotoRaices() {

        if (sp_fenologico.getSelectedItem().toString().equals("MONITOREO")) {
            Toasty.error(requireActivity(), "Foto Raiz deshabilitada para monitoreo", Toast.LENGTH_LONG, true).show();
            return;
        }

        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.foto_raices, null);


        final AlertDialog builder = new AlertDialog.Builder(activity)
                .setView(viewInfalted)
                .create();

        final EditText et = viewInfalted.findViewById(R.id.et_produndidad_raices);
        final CheckBox check = viewInfalted.findViewById(R.id.check_pretunta_regla);
        final Button pic = viewInfalted.findViewById(R.id.btn_tomar_foto_raiz);
        final Button cancelar = viewInfalted.findViewById(R.id.btn_cancelar_modal);

        check.setOnCheckedChangeListener((buttonView, isChecked) -> pic.setEnabled(isChecked));


        pic.setOnClickListener(ev -> {

            Utilidades.hideKeyboard(activity);

            if (et.getText().toString().isEmpty() || !check.isChecked()) {
                medidaRaices = "";
                Utilidades.hideKeyboard(activity);
                Toasty.error(requireActivity(),
                                "Necesitas ingresar la profundidad y afirmar el uso de regla",
                                Toast.LENGTH_LONG, true)
                        .show();
                return;
            }
            abrirCamara(3, et.getText().toString());
            builder.dismiss();
        });

        cancelar.setOnClickListener(ev -> builder.dismiss());

        builder.setCancelable(true);
        builder.show();
    }

    private void showAlertForSave(String title, String message) {
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_empty, null);


        final AlertDialog builder22 = new AlertDialog.Builder(activity)
                .setView(viewInfalted)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar", (dialogInterface, i) -> {
                })
                .create();


        builder22.setOnShowListener(dialog -> {
            Button a = builder22.getButton(AlertDialog.BUTTON_POSITIVE);
            a.setOnClickListener(v -> {
                builder22.dismiss();
                activity.cambiarFragment(new FragmentVisitas(), Utilidades.FRAGMENT_VISITAS, R.anim.slide_in_left, R.anim.slide_out_left);

            });
        });
        builder22.setCancelable(true);
        builder22.show();
    }

    public void showAlertAskForSave123(String title, String message) {
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_empty, null);

        final AlertDialog builder = new AlertDialog.Builder(activity)
                .setView(viewInfalted)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("aceptar", (dialogInterface, i) -> {
                })
                .setNegativeButton("cancelar", (dialog, which) -> {
                })
                .create();

        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
            Button c = builder.getButton(AlertDialog.BUTTON_NEGATIVE);
            b.setOnClickListener(v -> {
                setOnSave();
                builder.dismiss();
            });
            c.setOnClickListener(v -> builder.dismiss());
        });
        builder.setCancelable(false);
        builder.show();
    }

    public void preguntarSiQuiereVolver(String title, String message) {
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_empty, null);

        final AlertDialog builder = new AlertDialog.Builder(activity)
                .setView(viewInfalted)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("aceptar", (dialogInterface, i) -> {
                })
                .setNegativeButton("cancelar", (dialog, which) -> {
                })
                .create();

        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
            Button c = builder.getButton(AlertDialog.BUTTON_NEGATIVE);
            b.setOnClickListener(v -> {
                MainActivity activity = (MainActivity) getActivity();
                if (activity != null) {
                    activity.cambiarFragment(new FragmentVisitas(), Utilidades.FRAGMENT_VISITAS, R.anim.slide_in_right, R.anim.slide_out_right);
                }
                builder.dismiss();
            });
            c.setOnClickListener(v -> builder.dismiss());
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void agregarImagenToAgronomos() {

        if (rwAgronomo == null || activity == null) {
            return;
        }


        LinearLayoutManager lManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        rwAgronomo.setHasFixedSize(true);
        rwAgronomo.setLayoutManager(lManager);

        int idVisita = (visitasCompletas != null) ? visitasCompletas.getVisitas().getId_visita() : 0;
        int idVisitaLocal = (visitasCompletas != null) ? visitasCompletas.getVisitas().getId_visita_local() : 0;


        try {

            Config config = MainActivity.myAppDB.myDao().getConfig();
            int idDispo = (config == null) ? 0 : config.getId();

            List<Fotos> myImageList = MainActivity.myAppDB.myDao().getFotosByFieldAndViewVisitas(
                    2,
                    anexoCompleto.getAnexoContrato().getId_anexo_contrato(),
                    idVisitaLocal,
                    idVisita,
                    idDispo);

            adapterAgronomo = new FotosListAdapter(myImageList, activity, this::showAlertForUpdate, fotos -> {

                if (visitasCompletas != null) {
                    Utilidades.avisoListo(activity, getResources().getString(R.string.title_dialog_agron), "Visita en estado terminado, no puedes cambiar el estado de las fotos.", "entiendo");
                    return;
                }
                cambiarFavorita(fotos);
            });
            rwAgronomo.setAdapter(adapterAgronomo);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    private void agregarImagenToRaices() {

        if (rwRaices != null) {

            LinearLayoutManager lManager = null;
            if (activity != null) {
                lManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);

            }

            int idDispo = 0;
            Config config = MainActivity.myAppDB.myDao().getConfig();
            if (config != null) {
                idDispo = config.getId();
            }

            rwRaices.setHasFixedSize(true);
            rwRaices.setLayoutManager(lManager);

            int visitaServidor = (visitasCompletas != null) ? visitasCompletas.getVisitas().getId_visita() : 0;
            int visitaLocal = (visitasCompletas != null) ? visitasCompletas.getVisitas().getId_visita_local() : 0;

            List<Fotos> myImageList = MainActivity.myAppDB.myDao().getFotosByFieldAndView(
                    3,
                    anexoCompleto.getAnexoContrato().getId_anexo_contrato(),
                    visitaLocal,
                    visitaServidor,
                    idDispo
            );

            adapterRaices = new FotosListAdapter(myImageList, activity, this::showAlertForUpdate, photo -> {
            });
            rwRaices.setAdapter(adapterRaices);
        }
    }

    private void agregarImagenToClientes() {

        if (rwCliente != null) {

            LinearLayoutManager lManager = null;
            if (activity != null) {
                lManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
            }

            int idDispo = 0;
            Config config = MainActivity.myAppDB.myDao().getConfig();
            if (config != null) {
                idDispo = config.getId();
            }

            rwCliente.setHasFixedSize(true);
            rwCliente.setLayoutManager(lManager);

            int visitaServidor = (visitasCompletas != null) ? visitasCompletas.getVisitas().getId_visita() : 0;
            int visitaLocal = (visitasCompletas != null) ? visitasCompletas.getVisitas().getId_visita_local() : 0;

            List<Fotos> myImageList = MainActivity.myAppDB.myDao().getFotosByFieldAndView(
                    0,
                    anexoCompleto.getAnexoContrato().getId_anexo_contrato(),
                    visitaLocal,
                    visitaServidor,
                    idDispo
            );
            adapterCliente = new FotosListAdapter(myImageList, activity, this::showAlertForUpdate, fotos -> {
                if (visitasCompletas != null) {
                    Utilidades.avisoListo(activity, getResources().getString(R.string.title_dialog_agron), "Visita en estado terminado, no puedes cambiar el estado de las fotos.", "entiendo");
                    return;
                }
                cambiarFavorita(fotos);
            });
            rwCliente.setAdapter(adapterCliente);
        }
    }

    private void levantarDialogoObservationTodo() {
        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = requireActivity().getSupportFragmentManager().findFragmentByTag("EVALUACION_RECOMENDACION");
        if (prev != null) {
            ft.remove(prev);
        }

        try {
            VisitasCompletas ultimaVisita = MainActivity.myAppDB.myDao().getUltimaVisitaByAnexo(anexoCompleto.getAnexoContrato().getId_anexo_contrato());

            if (ultimaVisita != null) {
                evaluacionAnterior.id_evaluacion = ultimaVisita.getVisitas().getId_visita();
            }

            if (visitasCompletas != null) {
                evaluacionAnterior.comentario = visitasCompletas.getVisitas().getComentario_evaluacion();
                evaluacionAnterior.evaluacion = visitasCompletas.getVisitas().getEvaluacion();
            }

            DialogObservationTodo dialogo = DialogObservationTodo.newInstance(
                    anexoCompleto.getAnexoContrato(),
                    evaluacionAnterior,
                    ultimaVisita,
                    visitasCompletas,
                    (sp_fenologico.getSelectedItem() != null) ? sp_fenologico.getSelectedItem().toString() : "",
                    (EvaluacionAnterior tp) -> evaluacionAnterior = tp);
            dialogo.show(ft, "EVALUACION_RECOMENDACION");
        } catch (Exception e) {
            Toasty.error(activity, "Error levantando dialogo " + e.getMessage(), Toast.LENGTH_LONG, true).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();

        switch (item.getItemId()) {
            //ABRE DIALOGO DE RECOMENDACIONES
            case R.id.menu_visitas_recom:
                levantarDialogoObservationTodo();
                return true;
            //ABRE DIALOGO DE LC RESUMEN
            case R.id.menu_visitas_lc_resumen:

                Fragment prev = requireActivity().getSupportFragmentManager().findFragmentByTag("LIBRO_CAMPO_RESUMEN");
                if (prev != null) {
                    ft.remove(prev);
                }
                DialogLibroCampo dialogo = DialogLibroCampo.newInstance(temporada, variedad, 1, (boolean saved) -> {
                    if (saved) {
                        Toasty.success(activity, "No olvides que se guardará al guardar la visita", Toast.LENGTH_LONG, true).show();
                    }
                });
                dialogo.show(ft, "LIBRO_CAMPO_RESUMEN");
                return true;
            //ABRE DIALOGO DE LC SOWING
            case R.id.menu_visitas_lc_sowing:
                Fragment prevSowing = requireActivity().getSupportFragmentManager().findFragmentByTag("LIBRO_CAMPO_SOWING");
                if (prevSowing != null) {
                    ft.remove(prevSowing);
                }
                DialogLibroCampo dialogSowing = DialogLibroCampo.newInstance(temporada, variedad, 2, (boolean saved) -> {
                    if (saved) {
                        Toasty.success(activity, "No olvides que se guardará al guardar la visita", Toast.LENGTH_LONG, true).show();
                    }
                });
                dialogSowing.show(ft, "LIBRO_CAMPO_SOWING");
                return true;
            //ABRE DIALOGO DE LC FLOWERING
            case R.id.menu_visitas_lc_flowering:
                Fragment prevFlowering = requireActivity().getSupportFragmentManager().findFragmentByTag("LIBRO_CAMPO_FLOWERING");
                if (prevFlowering != null) {
                    ft.remove(prevFlowering);
                }

                DialogLibroCampo dialogFlowering = DialogLibroCampo.newInstance(temporada, variedad, 3, (boolean saved) -> {
                    if (saved) {
                        Toasty.success(activity, "No olvides que se guardará al guardar la visita", Toast.LENGTH_LONG, true).show();
                    }
                });
                dialogFlowering.show(ft, "LIBRO_CAMPO_FLOWERING");
                return true;
            //ABRE DIALOGO DE LC HARVEST
            case R.id.menu_visitas_lc_harvest:
                Fragment prevHarvest = requireActivity().getSupportFragmentManager().findFragmentByTag("LIBRO_CAMPO_HARVEST");
                if (prevHarvest != null) {
                    ft.remove(prevHarvest);
                }

                DialogLibroCampo dialogHarvest = DialogLibroCampo.newInstance(temporada, variedad, 4, (boolean saved) -> {
                    if (saved) {
                        Toasty.success(activity, "No olvides que se guardará al guardar la visita", Toast.LENGTH_LONG, true).show();
                    }
                });
                dialogHarvest.show(ft, "LIBRO_CAMPO_HARVEST");
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void chargeAll() {
        if (progressBar != null && progressBar.isShowing()) {
            progressBar.dismiss();
        }

        if (visitasCompletas == null && anexoCompleto != null) {
            levantarDialogoObservationTodo();
        }

        cargarSpinners();
        foto_raices.setOnClickListener(v -> preguntarFotoRaices());

        material_private.setOnClickListener(v -> {

            if (visitasCompletas != null) {
                Utilidades.avisoListo(activity, getResources().getString(R.string.title_dialog_agron), getResources().getString(R.string.visitas_terminadas), getResources().getString(R.string.entiendo));
                return;
            }

            Utilidades.hideKeyboard(activity);
            abrirCamara(2, "0");

        });
        material_public.setOnClickListener(v -> {

            if (sp_fenologico.getSelectedItemPosition() == 0) {
                Utilidades.avisoListo(activity, getResources().getString(R.string.falta_algo), getResources().getString(R.string.pheno_first), getResources().getString(R.string.entiendo));
                return;
            }
            if (visitasCompletas != null) {
                Utilidades.avisoListo(activity, getResources().getString(R.string.title_dialog_agron), getResources().getString(R.string.visitas_terminadas), getResources().getString(R.string.entiendo));
                return;
            }

            Utilidades.hideKeyboard(activity);
            abrirCamara(0, "0");

        });


    }

    private void accionSpinners() {

        if (visitasCompletas == null) return;

        Visitas visitas = visitasCompletas.getVisitas();

        sp_planta_voluntaria.setSelection(planta_voluntaria.indexOf(visitas.getPlanta_voluntaria()));


        sp_fenologico.setSelection(fenologico.indexOf(visitas.getPhenological_state_visita()));
        sp_cosecha.setSelection(cosecha.indexOf(visitas.getHarvest_visita()));
        sp_crecimiento.setSelection(crecimiento.indexOf(visitas.getGrowth_status_visita()));
        sp_fito.setSelection(crecimiento.indexOf(visitas.getPhytosanitary_state_visita()));
        sp_general_cultivo.setSelection(crecimiento.indexOf(visitas.getOverall_status_visita()));
        sp_humedad.setSelection(crecimiento.indexOf(visitas.getHumidity_floor_visita()));
        sp_malezas.setSelection(maleza.indexOf(visitas.getWeed_state_visita()));

        et_obs.setText(visitas.getObservation_visita());

        et_obs_harvest.setText(visitas.getObs_cosecha());
        et_obs_overall.setText(visitas.getObs_overall());
        et_obs_humedad.setText(visitas.getObs_humedad());
        et_obs_weed.setText(visitas.getObs_maleza());
        et_obs_fito.setText(visitas.getObs_fito());
        et_obs_growth.setText(visitas.getObs_creci());
        et_percent_humedad.setText(String.valueOf(visitas.getPercent_humedad()));

        et_fecha_estimada_postura_abejas.setText(Utilidades.voltearFechaVista(visitas.getFecha_estimada_postura_abejas()));
        et_fecha_arranca.setText(Utilidades.voltearFechaVista(visitas.getFecha_estimada_arranca()));
        et_fecha_estimada.setText(Utilidades.voltearFechaVista(visitas.getFecha_estimada_cosecha()));


        et_percent_humedad.setSelectAllOnFocus(true);
        et_obs_harvest.setSelectAllOnFocus(true);
        et_obs_growth.setSelectAllOnFocus(true);
        et_obs_fito.setSelectAllOnFocus(true);
        et_obs_weed.setSelectAllOnFocus(true);
        et_obs_humedad.setSelectAllOnFocus(true);
        et_obs_overall.setSelectAllOnFocus(true);
        et_obs_fito.setSelectAllOnFocus(true);

        agregarImagenToAgronomos();
        agregarImagenToClientes();
        agregarImagenToRaices();

        sp_fenologico.setEnabled(false);
        sp_cosecha.setEnabled(false);
        sp_planta_voluntaria.setEnabled(false);
        sp_crecimiento.setEnabled(false);
        sp_fito.setEnabled(false);
        sp_general_cultivo.setEnabled(false);
        sp_humedad.setEnabled(false);
        sp_malezas.setEnabled(false);
        et_obs.setEnabled(false);
        btn_guardar.setEnabled(false);

        et_fecha_estimada.setEnabled(false);
        et_fecha_arranca.setEnabled(false);
        et_fecha_estimada_postura_abejas.setEnabled(false);

        et_obs_fito.setEnabled(false);
        et_obs_overall.setEnabled(false);
        et_obs_humedad.setEnabled(false);
        et_obs_weed.setEnabled(false);
        et_obs_fito.setEnabled(false);
        et_obs_growth.setEnabled(false);
        et_obs_harvest.setEnabled(false);

        et_percent_humedad.setEnabled(false);
    }

    private void bind(View view) {
        contenedor_estados = view.findViewById(R.id.contenedor_estados);
        contenedor_monitoreo = view.findViewById(R.id.contenedor_monitoreo);
        titulo_raices = view.findViewById(R.id.titulo_raices);
        sp_fenologico = view.findViewById(R.id.sp_feno);
        sp_cosecha = view.findViewById(R.id.sp_cosecha);
        sp_planta_voluntaria = view.findViewById(R.id.sp_planta_voluntaria);
        sp_crecimiento = view.findViewById(R.id.sp_crecimiento);
        sp_fito = view.findViewById(R.id.sp_fito);
        sp_general_cultivo = view.findViewById(R.id.sp_general_cultivo);
        sp_humedad = view.findViewById(R.id.sp_humedad);
        sp_malezas = view.findViewById(R.id.sp_malezas);
        btn_guardar = view.findViewById(R.id.btn_guardar);
        btn_buscar_obs = view.findViewById(R.id.btn_buscar_obs);

        et_obs = view.findViewById(R.id.et_obs);
        et_obs_fito = view.findViewById(R.id.et_obs_fito);
        et_obs_growth = view.findViewById(R.id.et_obs_growth);
        et_obs_harvest = view.findViewById(R.id.et_obs_harvest);
        et_obs_overall = view.findViewById(R.id.et_obs_overall);
        et_obs_weed = view.findViewById(R.id.et_obs_weed);
        et_obs_humedad = view.findViewById(R.id.et_obs_humedad);
        et_percent_humedad = view.findViewById(R.id.et_percent_humedad);

        obs_growth = view.findViewById(R.id.obs_growth);
        obs_weed = view.findViewById(R.id.obs_weed);
        obs_fito = view.findViewById(R.id.obs_fito);
        obs_harvest = view.findViewById(R.id.obs_harvest);
        obs_overall = view.findViewById(R.id.obs_overall);
        obs_humedad = view.findViewById(R.id.obs_humedad);


        et_fecha_estimada = view.findViewById(R.id.et_fecha_estimada);
        et_fecha_arranca = view.findViewById(R.id.et_fecha_arranca);
        et_fecha_estimada_postura_abejas = view.findViewById(R.id.et_fecha_estimada_postura_abejas);

        btn_volver = view.findViewById(R.id.btn_volver);


        btn_buscar_obs.setOnClickListener(v -> {
            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();

            Fragment obsDialog = requireActivity().getSupportFragmentManager().findFragmentByTag("DIALOG_OBS_RECOM");
            if (obsDialog != null) {
                ft.remove(obsDialog);
            }
            DialogBuscaObsRecAnterior dialogHarvest = DialogBuscaObsRecAnterior.newInstance(sp_fenologico.getSelectedItem().toString(), "OBSERVACION", (boolean saved, String contenido) -> {
                if (saved) {
                    et_obs.setText(contenido);
                }
            });
            dialogHarvest.show(ft, "DIALOG_OBS_RECOM");

        });

        sp_fenologico.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modificarVista(sp_fenologico.getSelectedItem().toString().equals("MONITOREO"));
                prefs.edit().putInt(Utilidades.SHARED_ETAPA_SELECTED, Utilidades.getPhenoState(sp_fenologico.getSelectedItemPosition())).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        sp_malezas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                obs_weed.setVisibility((position == 0) ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_humedad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                obs_humedad.setVisibility((position == 0) ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_general_cultivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                obs_overall.setVisibility((position == 0) ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_fito.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                obs_fito.setVisibility((position == 0) ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_crecimiento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                obs_growth.setVisibility((position == 0) ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_cosecha.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                obs_harvest.setVisibility((position == 0) ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        et_fecha_estimada.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                et_fecha_estimada.setKeyListener(null);
                et_fecha_estimada.setInputType(InputType.TYPE_NULL);

                Utilidades.levantarFecha(et_fecha_estimada, view.getContext());
            }

        });
        et_fecha_arranca.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                et_fecha_arranca.setKeyListener(null);
                et_fecha_arranca.setInputType(InputType.TYPE_NULL);
                Utilidades.levantarFecha(et_fecha_arranca, view.getContext());
            }
        });

        et_fecha_estimada_postura_abejas.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                et_fecha_estimada_postura_abejas.setKeyListener(null);
                et_fecha_estimada_postura_abejas.setInputType(InputType.TYPE_NULL);
                Utilidades.levantarFecha(et_fecha_estimada_postura_abejas, view.getContext());
            }
        });


        btn_volver.setOnClickListener(v -> preguntarSiQuiereVolver("ATENCION", "SI VUELVES NO GUARDARA LOS CAMBIOS, ESTAS SEGURO QUE DESEAS VOLVER ?"));
        btn_guardar.setOnClickListener(v -> {
            if (activity == null) {
                Toasty.error(requireActivity(), "No encontramos algo importante , por favor, vuelva a realizar la visita", Toast.LENGTH_LONG, true).show();
                return;
            }

            String message = !sp_fenologico.getSelectedItem().toString().equals("MONITOREO")
                    ? "Revise el libro de campo antes de hacerlo, si esta seguro, presione aceptar."
                    : "";

            showAlertAskForSave123("¿Esta seguro que desea guardar?", message);
        });


        rwAgronomo = view.findViewById(R.id.fotos_agronomos);
        rwCliente = view.findViewById(R.id.fotos_clientes);
        rwRaices = view.findViewById(R.id.fotos_raices);

        material_private = view.findViewById(R.id.material_private);
        material_public = view.findViewById(R.id.material_public);
        foto_raices = view.findViewById(R.id.foto_raices);
    }


    private void replicarVisita(Visitas visitas, List<String> idAcList) {
        String claveUnicaOriginal = visitas.getClave_unica_visita();
        int idVisitaOriginal = visitas.getId_visita();
        for (String idAc : idAcList) {

            String claveUnicaClon = UUID.randomUUID().toString();

            Visitas visitaNueva = new Visitas();

            visitaNueva.setClave_unica_visita(claveUnicaClon);
            visitaNueva.setEstado_server_visitas(0);
            visitaNueva.setId_anexo_visita(idAc);
            visitaNueva.setFecha_estimada_postura_abejas(visitas.getFecha_estimada_postura_abejas());
            visitaNueva.setTipo_visita(visitas.getTipo_visita());
            visitaNueva.setPlanta_voluntaria(visitas.getPlanta_voluntaria());
            visitaNueva.setFecha_estimada_cosecha(visitas.getFecha_estimada_cosecha());
            visitaNueva.setFecha_estimada_arranca(visitas.getFecha_estimada_arranca());
            visitaNueva.setId_evaluacion(visitas.getId_evaluacion());
            visitaNueva.setEvaluacion(visitas.getEvaluacion());
            visitaNueva.setComentario_evaluacion(visitas.getComentario_evaluacion());
            visitaNueva.setTomadas(visitas.getTomadas());
            visitaNueva.setId_dispo(visitas.getId_dispo());
            visitaNueva.setPercent_humedad(visitas.getPercent_humedad());
            visitaNueva.setId_user_visita(visitas.getId_user_visita());
            visitaNueva.setCabecera_visita(visitas.getCabecera_visita());
            visitaNueva.setObs_fito(visitas.getObs_fito());
            visitaNueva.setObs_creci(visitas.getObs_creci());
            visitaNueva.setObs_maleza(visitas.getObs_maleza());
            visitaNueva.setObs_cosecha(visitas.getObs_cosecha());
            visitaNueva.setObs_overall(visitas.getObs_overall());
            visitaNueva.setObs_humedad(visitas.getObs_humedad());
            visitaNueva.setTemporada(visitas.getTemporada());
            visitaNueva.setFecha_visita(visitas.getFecha_visita());
            visitaNueva.setHora_visita(visitas.getHora_visita());
            visitaNueva.setEtapa_visitas(visitas.getEtapa_visitas());
            visitaNueva.setEstado_visita(visitas.getEstado_visita());
            visitaNueva.setPhenological_state_visita(visitas.getPhenological_state_visita());
            visitaNueva.setGrowth_status_visita(visitas.getGrowth_status_visita());
            visitaNueva.setWeed_state_visita(visitas.getWeed_state_visita());
            visitaNueva.setPhytosanitary_state_visita(visitas.getPhytosanitary_state_visita());
            visitaNueva.setHarvest_visita(visitas.getHarvest_visita());
            visitaNueva.setOverall_status_visita(visitas.getOverall_status_visita());
            visitaNueva.setHumidity_floor_visita(visitas.getHumidity_floor_visita());
            visitaNueva.setObservation_visita(visitas.getObservation_visita());
            visitaNueva.setRecomendation_visita(visitas.getRecomendation_visita());

            long idVisitaNueva = MainActivity.myAppDB.myDao().setVisita(visitaNueva);

            visitaNueva.setId_visita((int) idVisitaNueva);
            visitaNueva.setId_visita_local((int) idVisitaNueva);
            MainActivity.myAppDB.myDao().updateVisita(visitaNueva);

//            fotos
            List<Fotos> fts = MainActivity.myAppDB.myDao().getFotosByIdVisita(idVisitaOriginal);
            for (Fotos ft : fts) {
                if (ft.getRuta() != null && !ft.getRuta().isEmpty()) {
                    File originalFile = new File(ft.getRuta());
                    if (originalFile.exists()) {
                        String nuevoNombre = UUID.randomUUID().toString() + ".jpg";
                        File destino = new File(originalFile.getParent(), nuevoNombre);
                        try {
                            java.nio.file.Files.copy(originalFile.toPath(), destino.toPath());
                            Fotos fotoClonada = new Fotos();

//                            setId_foto
                            fotoClonada.setId_visita_foto(visitaNueva.getId_visita());
                            fotoClonada.setId_ficha_fotos(idAc);
                            fotoClonada.setEstado_fotos(0);
                            fotoClonada.setNombre_foto(nuevoNombre);
                            fotoClonada.setRuta(destino.getAbsolutePath());
                            fotoClonada.setAcepto_regla_raices(ft.getAcepto_regla_raices());
                            fotoClonada.setMedida_raices(ft.getMedida_raices());
                            fotoClonada.setTomada_foto(ft.getTomada_foto());
                            fotoClonada.setId_dispo_foto(ft.getId_dispo_foto());
                            fotoClonada.setCabecera_fotos(ft.getCabecera_fotos());
                            fotoClonada.setFieldbook(ft.getFieldbook());
                            fotoClonada.setVista(ft.getVista());
                            fotoClonada.setPlano(ft.getPlano());
                            fotoClonada.setFecha(ft.getFecha());
                            fotoClonada.setHora(ft.getHora());
                            fotoClonada.setFavorita(ft.isFavorita());
                            MainActivity.myAppDB.myDao().insertFotos(fotoClonada);
                        } catch (IOException ignore) {
                        }
                    }
                }
            }

//          recomendaciones
            List<Evaluaciones> evs = MainActivity.myAppDB.DaoEvaluaciones().getEvaluacionesClon(claveUnicaOriginal);
            for (Evaluaciones ev : evs) {
                Evaluaciones evNueva = new Evaluaciones();
//                evNueva.setCabecera_server
//                evNueva.setId_ac_recom();
                evNueva.setClave_unica_visita(claveUnicaClon);
                evNueva.setClave_unica_recomendacion(UUID.randomUUID().toString());
                evNueva.setEstado_server(0);
                evNueva.setId_ac(Integer.parseInt(idAc));
                evNueva.setObliga_visita(ev.getObliga_visita());
                evNueva.setMarca_evaluacion_server(ev.getMarca_evaluacion_server());
                evNueva.setNombre_crea(ev.getNombre_crea());
                evNueva.setNombre_mod(ev.getNombre_mod());
                evNueva.setDescripcion_recom(ev.getDescripcion_recom());
                evNueva.setEstado(ev.getEstado());
                evNueva.setObservacion_recom(ev.getObservacion_recom());
                evNueva.setFecha_plazo(ev.getFecha_plazo());
                evNueva.setUser_tx(ev.getUser_tx());
                evNueva.setFecha_hora_tx(ev.getFecha_hora_tx());
                evNueva.setUser_mod(ev.getUser_mod());
                evNueva.setFecha_hora_mod(ev.getFecha_hora_mod());
                MainActivity.myAppDB.DaoEvaluaciones().insertEvaluaciones(evNueva);
            }

//            libro de campo
            List<detalle_visita_prop> dts = MainActivity.myAppDB.myDao().detallesToClon(idVisitaOriginal);
            for (detalle_visita_prop dt : dts) {
                detalle_visita_prop dtNueva = new detalle_visita_prop();

//                dtNueva.setCabecera_detalle(dt.getCabecera_detalle());
//                dtNueva.setId_det_vis_prop_detalle(dt.getId_det_vis_prop_detalle());
                dtNueva.setTomada_detalle(dt.getTomada_detalle());
                dtNueva.setEstado_detalle(0);
                dtNueva.setId_visita_detalle(visitaNueva.getId_visita());
                dtNueva.setId_prop_mat_cli_detalle(dt.getId_prop_mat_cli_detalle());
                dtNueva.setValor_detalle(dt.getValor_detalle());
                MainActivity.myAppDB.myDao().insertDatoDetalle(dtNueva);
            }
        }

    }


    private void saveVisitaMonitoreo(List<String> idAcs) {

        if (sp_fenologico.getSelectedItemPosition() <= 0 || sp_planta_voluntaria.getSelectedItemPosition() <= 0) {
            Utilidades.avisoListo(activity, "Hey", "no puede dejar elementos en '--seleccione--'.", "entiendo");
            return;
        }
        String alfaNumerico = getResources().getString(R.string.alfanumericos_con_signos);
        String phenoStateVisita = sp_fenologico.getSelectedItem().toString();
        String plantaVoluntaria = sp_planta_voluntaria.getSelectedItem().toString();
        et_obs.setText(Utilidades.sanitizarString(et_obs.getText().toString(), alfaNumerico));
        String etobs = et_obs.getText().toString().toUpperCase();

        executors.execute(() -> {
            Visitas visitas = new Visitas();

            visitas.setPhenological_state_visita(phenoStateVisita);
            visitas.setPlanta_voluntaria(plantaVoluntaria);

            visitas.setObservation_visita(etobs);

            try {

                String claveUnica = (visitasCompletas != null) ? visitasCompletas.getVisitas().getClave_unica_visita() : UUID.randomUUID().toString();
                AnexoContrato an = anexoCompleto.getAnexoContrato();
                visitas.setTemporada(an.getTemporada_anexo());

                String fecha = Utilidades.fechaActualConHora();
                String[] fechaHora = fecha.split(" ");

                visitas.setId_anexo_visita(an.getId_anexo_contrato());
                visitas.setHora_visita(fechaHora[1]);
                visitas.setFecha_visita(fechaHora[0]);
                visitas.setEtapa_visitas(Utilidades.getPhenoState(sp_fenologico.getSelectedItemPosition()));
                visitas.setEvaluacion(evaluacionAnterior.evaluacion);
                visitas.setComentario_evaluacion(evaluacionAnterior.comentario);
                visitas.setId_evaluacion(evaluacionAnterior.id_evaluacion);

                visitas.setEstado_server_visitas(0);
                visitas.setEstado_visita(2);
                visitas.setClave_unica_visita(claveUnica);

                visitas.setTipo_visita("MONITOREO");

                long idVisita;
                if (visitasCompletas != null) {
                    idVisita = visitasCompletas.getVisitas().getId_visita();
                    visitas.setId_visita((int) idVisita);
                    visitas.setId_visita_local(visitasCompletas.getVisitas().getId_visita_local());
                    MainActivity.myAppDB.myDao().updateVisita(visitas);
                } else {
                    idVisita = MainActivity.myAppDB.myDao().setVisita(visitas);
                }


                long finalIdVisita = idVisita;

                MainActivity.myAppDB.DaoEvaluaciones().updateEvaluacionesObligadas(Integer.parseInt(an.getId_anexo_contrato()), claveUnica);
                MainActivity.myAppDB.myDao().updateFotosWithVisita((int) finalIdVisita, anexoCompleto.getAnexoContrato().getId_anexo_contrato());
                MainActivity.myAppDB.myDao().updateDetallesToVisits((int) finalIdVisita);
                Visitas visitas1 = MainActivity.myAppDB.myDao().getVisitas((int) finalIdVisita);
                visitas1.setId_visita_local((int) idVisita);
                MainActivity.myAppDB.myDao().updateVisita(visitas1);
                evaluacionAnterior.clean();


                if (!idAcs.isEmpty()) {
                    replicarVisita(visitas1, idAcs);
                }

                requireActivity().runOnUiThread(() -> {
                    showAlertForSave("Genial", "Se guardo todo como corresponde");
                });
                return;
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    Utilidades.avisoListo(activity, "Falta algo", "No se pudo guardar " + e.getMessage(), "entiendo");
                });
                return;

            }
        });


    }

    private void saveVisitaNormal(List<String> idAcs) {
        try {
            VisitasCompletas cc = MainActivity.myAppDB.myDao().getUltimaVisitaByAnexo(anexoCompleto.getAnexoContrato().getId_anexo_contrato());

            if ((evaluacionAnterior.evaluacion == 0.0 || evaluacionAnterior.comentario.isEmpty()) && cc != null && cc.getVisitas() != null) {
                Utilidades.avisoListo(activity, "Falta algo", "Antes de guardar debes realizar la evaluacion de la visita anterior. ( estrella de arriba a la derecha )", "entiendo");
                return;
            }

            String idAc = anexoCompleto.getAnexoContrato().getId_anexo_contrato();
            int idVisita = (visitasCompletas != null) ? visitasCompletas.getVisitas().getId_visita() : 0;

            int fotosClientes = MainActivity.myAppDB.myDao().getCantFotos(idAc, idVisita, 0);
            int fotosAgricultores = MainActivity.myAppDB.myDao().getCantFotos(idAc, idVisita, 2);


            if (fotosClientes <= 0 || fotosAgricultores <= 0) {
                Utilidades.avisoListo(activity, "Falta algo", "Debes tomar al menos una foto cliente y agricultor", "entiendo");
                return;
            }


            int favsCliente = MainActivity.myAppDB.myDao().getCantFavoritasByFieldbookAndFicha(idAc, idVisita, 0);
            int favsAgricultores = MainActivity.myAppDB.myDao().getCantFavoritasByFieldbookAndFicha(idAc, idVisita, 2);

            if (favsCliente <= 0 || favsAgricultores <= 0) {
                Utilidades.avisoListo(activity, "Falta algo", "Debes seleccionar como favorita al menos una foto cliente y agricultor (manten presionada la foto para marcar)", "entiendo");
                return;
            }

            boolean entra = (sp_malezas.getSelectedItemPosition() == 3 && TextUtils.isEmpty(et_obs_weed.getText()));
            if (!entra) {
                entra = (sp_fito.getSelectedItemPosition() == 4 && TextUtils.isEmpty(et_obs_fito.getText()));
            }
            if (!entra) {
                entra = ((sp_cosecha.getSelectedItemPosition() == 1) && TextUtils.isEmpty(et_obs_harvest.getText()));
            }
            if (!entra) {
                entra = (sp_general_cultivo.getSelectedItemPosition() == 4 && TextUtils.isEmpty(et_obs_overall.getText()));
            }
            if (entra) {
                Utilidades.avisoListo(activity, "Faltan cosas", "Debe completar todas las observaciones en donde su estado sea malo/alto/rechazado", "entiendo");
                return;
            }

            if (sp_fenologico.getSelectedItemPosition() <= 0 || sp_malezas.getSelectedItemPosition() <= 0 ||
                    sp_fito.getSelectedItemPosition() <= 0 || sp_cosecha.getSelectedItemPosition() <= 0 ||
                    sp_general_cultivo.getSelectedItemPosition() <= 0 || sp_humedad.getSelectedItemPosition() <= 0) {
                Utilidades.avisoListo(activity, "Hey", "no puede dejar elementos en '--seleccione--'.", "entiendo");
                return;
            }

            if (TextUtils.isEmpty(et_percent_humedad.getText().toString()) || (Double.parseDouble(et_percent_humedad.getText().toString()) <= 0.0 || Double.parseDouble(et_percent_humedad.getText().toString()) > 200.0)) {
                Utilidades.avisoListo(activity, "Hey", "Debes ingresar un potencial de rendimiento sobre 0 y bajo 200 ", "entiendo");
                return;
            }


            Visitas visitas = new Visitas();
            visitas.setGrowth_status_visita(sp_crecimiento.getSelectedItem().toString());
            visitas.setHarvest_visita(sp_cosecha.getSelectedItem().toString());
            visitas.setEstado_server_visitas(0);
            visitas.setEstado_visita(2);
            visitas.setTipo_visita("NORMAL");
            visitas.setFecha_estimada_arranca(Utilidades.voltearFechaBD(et_fecha_arranca.getText().toString()));
            visitas.setFecha_estimada_cosecha(Utilidades.voltearFechaBD(et_fecha_estimada.getText().toString()));
            visitas.setFecha_estimada_postura_abejas(Utilidades.voltearFechaBD(et_fecha_estimada_postura_abejas.getText().toString()));
            visitas.setWeed_state_visita(sp_malezas.getSelectedItem().toString());
            visitas.setPhytosanitary_state_visita(sp_fito.getSelectedItem().toString());
            visitas.setPhenological_state_visita(sp_fenologico.getSelectedItem().toString());
            visitas.setHumidity_floor_visita(sp_humedad.getSelectedItem().toString());
            visitas.setOverall_status_visita(sp_general_cultivo.getSelectedItem().toString());
            visitas.setId_anexo_visita(anexoCompleto.getAnexoContrato().getId_anexo_contrato());
            visitas.setEtapa_visitas(Utilidades.getPhenoState(sp_fenologico.getSelectedItemPosition()));


            visitas.setComentario_evaluacion(evaluacionAnterior.comentario);
            visitas.setId_evaluacion(evaluacionAnterior.id_evaluacion);
            visitas.setEvaluacion(evaluacionAnterior.evaluacion);


            double percent;
            try {
                percent = Double.parseDouble(et_percent_humedad.getText().toString());
            } catch (Exception e) {
                percent = 0.0;
            }

            visitas.setPercent_humedad(percent);

            String alfaNumerico = getResources().getString(R.string.alfanumericos_con_signos);
            et_obs.setText(Utilidades.sanitizarString(et_obs.getText().toString(), alfaNumerico));
            et_obs_overall.setText(Utilidades.sanitizarString(et_obs_overall.getText().toString(), alfaNumerico));
            et_obs_weed.setText(Utilidades.sanitizarString(et_obs_weed.getText().toString(), alfaNumerico));
            et_obs_humedad.setText(Utilidades.sanitizarString(et_obs_humedad.getText().toString(), alfaNumerico));
            et_obs_fito.setText(Utilidades.sanitizarString(et_obs_fito.getText().toString(), alfaNumerico));
            et_obs_growth.setText(Utilidades.sanitizarString(et_obs_growth.getText().toString(), alfaNumerico));
            et_obs_harvest.setText(Utilidades.sanitizarString(et_obs_harvest.getText().toString(), alfaNumerico));


            String obsCosecha = et_obs_harvest.getText().toString().toUpperCase();
            String obsCreci = et_obs_growth.getText().toString().toUpperCase();
            String obsFito = et_obs_fito.getText().toString().toUpperCase();
            String obsHumedad = et_obs_humedad.getText().toString().toUpperCase();
            String obsMaleza = et_obs_weed.getText().toString().toUpperCase();
            String obsOverall = et_obs_overall.getText().toString().toUpperCase();
            String etobs = et_obs.getText().toString().toUpperCase();


            visitas.setObservation_visita(etobs);
            visitas.setObs_cosecha(obsCosecha);
            visitas.setObs_creci(obsCreci);
            visitas.setObs_fito(obsFito);
            visitas.setObs_humedad(obsHumedad);
            visitas.setObs_maleza(obsMaleza);
            visitas.setObs_overall(obsOverall);

            AnexoContrato an = anexoCompleto.getAnexoContrato();
            visitas.setTemporada(an.getTemporada_anexo());
            String fecha = Utilidades.fechaActualSinHora();
            String hora = Utilidades.hora();

            String claveUnica = (visitasCompletas != null) ? visitasCompletas.getVisitas().getClave_unica_visita() : UUID.randomUUID().toString();

            visitas.setHora_visita(hora);
            visitas.setFecha_visita(fecha);

            visitas.setClave_unica_visita(claveUnica);

            long idVis;

            if (visitasCompletas != null) {
                idVis = visitasCompletas.getVisitas().getId_visita();
                visitas.setId_visita(visitasCompletas.getVisitas().getId_visita());
                visitas.setId_visita_local(visitasCompletas.getVisitas().getId_visita());
                MainActivity.myAppDB.myDao().updateVisita(visitas);

            } else {
                idVis = MainActivity.myAppDB.myDao().setVisita(visitas);
            }

            long finalIdVisita = idVis;


            MainActivity.myAppDB.DaoEvaluaciones().updateEvaluacionesGuardar(Integer.parseInt(an.getId_anexo_contrato()), claveUnica);
            MainActivity.myAppDB.myDao().updateFotosWithVisita((int) finalIdVisita, anexoCompleto.getAnexoContrato().getId_anexo_contrato());
            MainActivity.myAppDB.myDao().updateDetallesToVisits((int) finalIdVisita);
            Visitas visitas1 = MainActivity.myAppDB.myDao().getVisitas((int) finalIdVisita);
            visitas1.setId_visita_local(idVisita);
            MainActivity.myAppDB.myDao().updateVisita(visitas1);

            if (!idAcs.isEmpty()) {
                replicarVisita(visitas1, idAcs);
            }

            showAlertForSave("Genial", "Se guardo todo como corresponde");

        } catch (Exception e) {
            Utilidades.avisoListo(activity, "Problemas", "No se pudo guardar la visita " + e.getMessage(), "entiendo");
        }
    }
}