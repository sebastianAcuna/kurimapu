package cl.smapdev.curimapu.fragments.checklist;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cl.smapdev.curimapu.BuildConfig;
import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.tablas.CheckListSiembra;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.temporales.TempFirmas;
import cl.smapdev.curimapu.clases.utilidades.CameraUtils;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import es.dmoral.toasty.Toasty;

public class FragmentChecklistSiembra extends Fragment {

    private CheckListSiembra checklist;
    private MainActivity activity;
    private SharedPreferences prefs;


    private AnexoCompleto anexoCompleto;
    private Usuario usuario;
    private Config config;

    private TextView
            tv_encargado_siembra,
            tv_agricultor,
            tv_especie,
            tv_variedad,
            tv_lote,
            tv_anexo;


    private EditText et_prestador_servicio,
            et_n_maquina,
            et_operador_maquina,
            et_fecha_siembra,
            et_hora_inicio,
            et_hora_termino,
            et_linea,
            et_registro_anpros,
            et_superficie_sembrada,
            et_peso_real_stock_seed,
            et_linea_anterior,
            et_observaciones,
            et_distancia_hileras_t1,
            et_distancia_hileras_t2,
            et_distancia_hileras_t3,
            et_distancia_hileras_t4,
            et_distancia_hileras_t5,
            et_distancia_hileras_t6,
            et_n_semillas_t1,
            et_n_semillas_t2,
            et_n_semillas_t3,
            et_n_semillas_t4,
            et_n_semillas_t5,
            et_n_semillas_t6,
            et_prof_siembra_t1,
            et_prof_siembra_t2,
            et_prof_siembra_t3,
            et_prof_siembra_t4,
            et_prof_siembra_t5,
            et_prof_siembra_t6,
            et_prof_fert_t1,
            et_prof_fert_t2,
            et_prof_fert_t3,
            et_prof_fert_t4,
            et_prof_fert_t5,
            et_prof_fert_t6,
            et_dist_fert_semilla_t1,
            et_dist_fert_semilla_t2,
            et_dist_fert_semilla_t3,
            et_dist_fert_semilla_t4,
            et_dist_fert_semilla_t5,
            et_dist_fert_semilla_t6,
            et_kg_mezcla,
            et_relacion_n,
            et_relacion_p,
            et_relacion_k,
            et_relacion_m,
            et_relacion_h,
            et_regulacion_pinones,
            et_sistema_fertilizacion;


    private RadioButton rb_propuesta_si,
            rb_propuesta_no;


    private Button btn_ver_foto_envase,
            btn_tomar_foto_envase,
            btn_ver_foto_semilla,
            btn_tomar_foto_semilla,
            btn_guardar_cl_siembra,
            btn_cancelar_cl_siembra;

    private ImageButton btn_expand_ts;

    private ConstraintLayout container_ts;

    private Spinner sp_estado_cama_raices,
            sp_estado_cama_semilla,
            sp_humedad_suelo,
            sp_micro_nivelacion,
            sp_compactacion,
            sp_tarros_prestadores_pre,
            sp_tarros_prestadores_post,
            sp_discos_pre,
            sp_discos_post,
            sp_hilera;

    private File fileImagen;
    private static final int COD_FOTO_ENVASE = 006;
    private static final int COD_FOTO_SEMILLA = 007;

    private String currentPhotoPath;
    private int tipoFoto;
    private Uri currentPhotoUri;
    private ActivityResultLauncher<Uri> cameraLauncher;

    // Executor reutilizable para operaciones DB rápidas (evita crear/shutdown por click)
    private final ExecutorService singleDbExecutor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());


    public void setChecklist(CheckListSiembra checklist) {
        this.checklist = checklist;
    }

    public static FragmentChecklistSiembra newInstance(CheckListSiembra clist) {
        FragmentChecklistSiembra fragment = new FragmentChecklistSiembra();
        fragment.setChecklist(clist);
        return fragment;
    }

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
        if (singleDbExecutor != null && !singleDbExecutor.isShutdown()) {
            singleDbExecutor.shutdown();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(),
                (Boolean success) -> {
                    if (success) {
                        procesarFotoTomada();
                    } else {
                        Toasty.info(activity, "Captura de foto cancelada", Toast.LENGTH_LONG, true).show();
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_checklist_siembra, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<AnexoCompleto> futureVisitas = executor.submit(() ->
                MainActivity
                        .myAppDB
                        .myDao()
                        .getAnexoCompletoById(
                                prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, "")
                        )
        );


        Future<Config> futureConfig = executor.submit(() ->
                MainActivity.myAppDB.myDao().getConfig());

        try {
            anexoCompleto = futureVisitas.get();
            config = futureConfig.get();

            Future<Usuario> usuarioFuture = executor.submit(() ->
                    MainActivity.myAppDB.myDao().getUsuarioById(config.getId_usuario()));

            usuario = usuarioFuture.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();

        bind(view);

        if (checklist != null) {
            levantarDatos();
        }

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        Utilidades.setToolbar(activity, view, getResources().getString(R.string.app_name), "CHECKLIST SIEMBRA");
    }


    @Override
    public void onStart() {
        super.onStart();
        cargarDatosPrevios();
    }

    private void bind(View view) {


        tv_encargado_siembra = view.findViewById(R.id.tv_encargado_siembra);
        tv_agricultor = view.findViewById(R.id.tv_agricultor);
        tv_especie = view.findViewById(R.id.tv_especie);
        tv_variedad = view.findViewById(R.id.tv_variedad);
        tv_lote = view.findViewById(R.id.tv_lote);
        tv_anexo = view.findViewById(R.id.tv_anexo);


        et_prestador_servicio = view.findViewById(R.id.et_prestador_servicio);
        et_n_maquina = view.findViewById(R.id.et_n_maquina);
        et_operador_maquina = view.findViewById(R.id.et_operador_maquina);
        et_fecha_siembra = view.findViewById(R.id.et_fecha_siembra);
        et_hora_inicio = view.findViewById(R.id.et_hora_inicio);
        et_hora_termino = view.findViewById(R.id.et_hora_termino);
        et_linea = view.findViewById(R.id.et_linea);
        et_registro_anpros = view.findViewById(R.id.et_registro_anpros);
        et_superficie_sembrada = view.findViewById(R.id.et_superficie_sembrada);
        et_peso_real_stock_seed = view.findViewById(R.id.et_peso_real_stock_seed);
        et_linea_anterior = view.findViewById(R.id.et_linea_anterior);
        et_observaciones = view.findViewById(R.id.et_observaciones);
        et_distancia_hileras_t1 = view.findViewById(R.id.et_distancia_hileras_t1);
        et_distancia_hileras_t2 = view.findViewById(R.id.et_distancia_hileras_t2);
        et_distancia_hileras_t3 = view.findViewById(R.id.et_distancia_hileras_t3);
        et_distancia_hileras_t4 = view.findViewById(R.id.et_distancia_hileras_t4);
        et_distancia_hileras_t5 = view.findViewById(R.id.et_distancia_hileras_t5);
        et_distancia_hileras_t6 = view.findViewById(R.id.et_distancia_hileras_t6);
        et_n_semillas_t1 = view.findViewById(R.id.et_n_semillas_t1);
        et_n_semillas_t2 = view.findViewById(R.id.et_n_semillas_t2);
        et_n_semillas_t3 = view.findViewById(R.id.et_n_semillas_t3);
        et_n_semillas_t4 = view.findViewById(R.id.et_n_semillas_t4);
        et_n_semillas_t5 = view.findViewById(R.id.et_n_semillas_t5);
        et_n_semillas_t6 = view.findViewById(R.id.et_n_semillas_t6);
        et_prof_siembra_t1 = view.findViewById(R.id.et_prof_siembra_t1);
        et_prof_siembra_t2 = view.findViewById(R.id.et_prof_siembra_t2);
        et_prof_siembra_t3 = view.findViewById(R.id.et_prof_siembra_t3);
        et_prof_siembra_t4 = view.findViewById(R.id.et_prof_siembra_t4);
        et_prof_siembra_t5 = view.findViewById(R.id.et_prof_siembra_t5);
        et_prof_siembra_t6 = view.findViewById(R.id.et_prof_siembra_t6);
        et_prof_fert_t1 = view.findViewById(R.id.et_prof_fert_t1);
        et_prof_fert_t2 = view.findViewById(R.id.et_prof_fert_t2);
        et_prof_fert_t3 = view.findViewById(R.id.et_prof_fert_t3);
        et_prof_fert_t4 = view.findViewById(R.id.et_prof_fert_t4);
        et_prof_fert_t5 = view.findViewById(R.id.et_prof_fert_t5);
        et_prof_fert_t6 = view.findViewById(R.id.et_prof_fert_t6);
        et_dist_fert_semilla_t1 = view.findViewById(R.id.et_dist_fert_semilla_t1);
        et_dist_fert_semilla_t2 = view.findViewById(R.id.et_dist_fert_semilla_t2);
        et_dist_fert_semilla_t3 = view.findViewById(R.id.et_dist_fert_semilla_t3);
        et_dist_fert_semilla_t4 = view.findViewById(R.id.et_dist_fert_semilla_t4);
        et_dist_fert_semilla_t5 = view.findViewById(R.id.et_dist_fert_semilla_t5);
        et_dist_fert_semilla_t6 = view.findViewById(R.id.et_dist_fert_semilla_t6);
        et_kg_mezcla = view.findViewById(R.id.et_kg_mezcla);
        et_relacion_n = view.findViewById(R.id.et_relacion_n);
        et_relacion_p = view.findViewById(R.id.et_relacion_p);
        et_relacion_k = view.findViewById(R.id.et_relacion_k);
        et_relacion_m = view.findViewById(R.id.et_relacion_m);
        et_relacion_h = view.findViewById(R.id.et_relacion_h);
        et_regulacion_pinones = view.findViewById(R.id.et_regulacion_pinones);
        et_sistema_fertilizacion = view.findViewById(R.id.et_sistema_fertilizacion);


        rb_propuesta_si = view.findViewById(R.id.rb_propuesta_si);
        rb_propuesta_no = view.findViewById(R.id.rb_propuesta_no);


        btn_ver_foto_envase = view.findViewById(R.id.btn_ver_foto_envase);
        btn_tomar_foto_envase = view.findViewById(R.id.btn_tomar_foto_envase);
        btn_ver_foto_semilla = view.findViewById(R.id.btn_ver_foto_semilla);
        btn_tomar_foto_semilla = view.findViewById(R.id.btn_tomar_foto_semilla);
        btn_guardar_cl_siembra = view.findViewById(R.id.btn_guardar_cl_siembra);
        btn_cancelar_cl_siembra = view.findViewById(R.id.btn_cancelar_cl_siembra);


        sp_estado_cama_raices = view.findViewById(R.id.sp_estado_cama_raices);
        sp_estado_cama_semilla = view.findViewById(R.id.sp_estado_cama_semilla);
        sp_humedad_suelo = view.findViewById(R.id.sp_humedad_suelo);
        sp_micro_nivelacion = view.findViewById(R.id.sp_micro_nivelacion);
        sp_compactacion = view.findViewById(R.id.sp_compactacion);
        sp_tarros_prestadores_pre = view.findViewById(R.id.sp_tarros_prestadores_pre);
        sp_tarros_prestadores_post = view.findViewById(R.id.sp_tarros_prestadores_post);
        sp_discos_pre = view.findViewById(R.id.sp_discos_pre);
        sp_discos_post = view.findViewById(R.id.sp_discos_post);
        sp_hilera = view.findViewById(R.id.sp_hilera);


        btn_expand_ts = view.findViewById(R.id.btn_expand_ts);
        container_ts = view.findViewById(R.id.container_ts);


        btn_expand_ts.setOnClickListener(v -> {
            if (container_ts.getVisibility() == View.VISIBLE) {
                btn_expand_ts.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_expand_down, null));

                container_ts.setVisibility(View.GONE);
            } else {
                btn_expand_ts.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_expand_up, null));
                container_ts.setVisibility(View.VISIBLE);
            }
        });


        et_fecha_siembra.setKeyListener(null);
        et_fecha_siembra.setInputType(InputType.TYPE_NULL);
        et_fecha_siembra.setOnClickListener(view1 -> Utilidades.levantarFecha(et_fecha_siembra, requireContext()));
        et_fecha_siembra.setOnFocusChangeListener((view1, b) -> {
            Utilidades.hideKeyboard(activity);
            if (b) Utilidades.levantarFecha(et_fecha_siembra, requireContext());
        });


        et_hora_inicio.setKeyListener(null);
        et_hora_inicio.setInputType(InputType.TYPE_NULL);
        et_hora_inicio.setOnClickListener(view1 -> Utilidades.levantarHora(et_hora_inicio, requireActivity()));
        et_hora_inicio.setOnFocusChangeListener((view1, b) -> {
            Utilidades.hideKeyboard(activity);
            if (b) Utilidades.levantarHora(et_hora_inicio, requireActivity());
        });

        et_hora_termino.setKeyListener(null);
        et_hora_termino.setInputType(InputType.TYPE_NULL);
        et_hora_termino.setOnClickListener(view1 -> Utilidades.levantarHora(et_hora_termino, requireActivity()));
        et_hora_termino.setOnFocusChangeListener((view1, b) -> {
            Utilidades.hideKeyboard(activity);
            if (b) Utilidades.levantarHora(et_hora_termino, requireActivity());
        });


        btn_ver_foto_envase.setEnabled(false);
        btn_ver_foto_semilla.setEnabled(false);

        btn_ver_foto_semilla.setOnClickListener(view1 -> {


            ExecutorService executor = Executors.newSingleThreadExecutor();

            Future<TempFirmas> firmaF = executor.submit(()
                    -> MainActivity.myAppDB.DaoFirmas()
                    .getFirmaByDocumAndLugar(Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA, Utilidades.DIALOG_TAG_FOTO_CHECKLIST_SIEMBRA_SEMILLA));

            try {
                TempFirmas firmas = firmaF.get();
                muestraImagen(firmas.getPath());

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                executor.shutdown();
            }


        });
        btn_ver_foto_envase.setOnClickListener(view1 -> {
            ExecutorService executor = Executors.newSingleThreadExecutor();

            Future<TempFirmas> firmaF = executor.submit(()
                    -> MainActivity.myAppDB.DaoFirmas()
                    .getFirmaByDocumAndLugar(Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA, Utilidades.DIALOG_TAG_FOTO_CHECKLIST_SIEMBRA_ENVASE));

            try {
                TempFirmas firmas = firmaF.get();
                muestraImagen(firmas.getPath());

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                executor.shutdown();
            }
        });

        btn_tomar_foto_envase.setOnClickListener(view1 -> {
            abrirCamara("ENVASE");
        });
        btn_tomar_foto_semilla.setOnClickListener(view1 -> {
            abrirCamara("SEMILLA");
        });

        String alfaNumerico = getResources().getString(R.string.alfanumericos_con_signos);
        et_observaciones.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                et_observaciones.setText(Utilidades.sanitizarString(et_observaciones.getText().toString(), alfaNumerico));
            }
        });


        btn_guardar_cl_siembra.setOnClickListener(view1 -> {
            onSave(1, anexoCompleto.getAgricultor().getNombre_agricultor() + " " + Utilidades.fechaActualConHora());
        });
        btn_cancelar_cl_siembra.setOnClickListener(view1 -> activity.onBackPressed());

    }


    private void abrirCamara(String tipo) {

        File photoFile = null;
        String uid = UUID.randomUUID().toString();
        tipoFoto = (tipo.equals("ENVASE")) ? COD_FOTO_ENVASE : COD_FOTO_SEMILLA;
        String nombre = uid + "_" + tipo;

        try {
            photoFile = Utilidades.createImageFile(requireActivity(), nombre);
            currentPhotoPath = photoFile.getAbsolutePath();
        } catch (IOException e) {
            Toasty.error(requireActivity(), "No se pudo crear la imagen " + e.getMessage(), Toast.LENGTH_LONG, true).show();
        }

        if (photoFile == null) {
            Toasty.error(requireActivity(), "No se pudo crear la imagen (nula)", Toast.LENGTH_LONG, true).show();
            return;
        }

        currentPhotoUri = FileProvider.getUriForFile(requireActivity(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
        cameraLauncher.launch(currentPhotoUri);
    }

    public void procesarFotoTomada() {
        try {
            Bitmap originalBtm = BitmapFactory.decodeFile(currentPhotoPath);
            if (originalBtm == null) {
                Toasty.error(requireActivity(), "No se pudo decodificar la imagen.", Toast.LENGTH_LONG, true).show();
                return;
            }

            Bitmap nuevaFoto = CameraUtils.escribirFechaImg(originalBtm, activity);

            File file = new File(currentPhotoPath);
            FileOutputStream fos = new FileOutputStream(file);

            nuevaFoto.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();


            singleDbExecutor.execute(() -> {
                String lugar = (tipoFoto == COD_FOTO_ENVASE) ? Utilidades.DIALOG_TAG_FOTO_CHECKLIST_SIEMBRA_ENVASE : Utilidades.DIALOG_TAG_FOTO_CHECKLIST_SIEMBRA_SEMILLA;
                TempFirmas tempFirmas = new TempFirmas();
                tempFirmas.setTipo_documento(Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA);
                tempFirmas.setPath(currentPhotoPath);
                tempFirmas.setLugar_firma(lugar);

                MainActivity.myAppDB.DaoFirmas().insertFirma(tempFirmas);
                handler.post(() -> {
                    if (tipoFoto == COD_FOTO_ENVASE) {
                        btn_ver_foto_envase.setEnabled(true);
                    } else {
                        btn_ver_foto_semilla.setEnabled(true);
                    }
                });

            });

        } catch (Exception e) {
            Toasty.error(requireActivity(), "Error al procesar la foto: " + e.getMessage(), Toast.LENGTH_LONG, true).show();
        }
    }


    private void muestraImagen(final String ruta) {
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_big_img, null);

        final AlertDialog builder;
        builder = new AlertDialog.Builder(requireActivity())
                .setView(viewInfalted)
                .setPositiveButton("cerrar", (dialogInterface, i) -> {
                }).create();

        final ImageView imageView = viewInfalted.findViewById(R.id.img_alert_foto);

        Picasso.get().load("file:///" + ruta).into(imageView);
        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
            b.setOnClickListener(v -> builder.dismiss());

        });
        builder.setCancelable(false);
        builder.show();
    }


    private void cargarDatosPrevios() {
        if (anexoCompleto == null) {
            Toasty.error(requireActivity(), "No se pudo obtener informacion del anexo", Toast.LENGTH_LONG, true).show();
        }


        tv_anexo.setText(anexoCompleto.getAnexoContrato().getAnexo_contrato());
        tv_variedad.setText(anexoCompleto.getVariedad().getDesc_variedad());
        tv_especie.setText(anexoCompleto.getEspecie().getDesc_especie());
        tv_agricultor.setText(anexoCompleto.getAgricultor().getNombre_agricultor());


        tv_encargado_siembra.setText(usuario.getNombre() + " " + usuario.getApellido_p());
        tv_lote.setText(anexoCompleto.getLotes().getNombre_lote());
    }

    private void levantarDatos() {


        if (checklist.getPrestador_servicio() != null && !checklist.getPrestador_servicio().isEmpty()) {
            et_prestador_servicio.setText(checklist.getPrestador_servicio());
        }

        if (checklist.getN_maquina() != null && !checklist.getN_maquina().isEmpty()) {
            et_n_maquina.setText(checklist.getN_maquina());
        }

        if (checklist.getOperador() != null && !checklist.getOperador().isEmpty()) {
            et_operador_maquina.setText(checklist.getOperador());
        }

        if (checklist.getFecha_siembra() != null && !checklist.getFecha_siembra().isEmpty()) {
            et_fecha_siembra.setText(checklist.getFecha_siembra());
        }

        if (checklist.getHora_inicio() != null && !checklist.getHora_inicio().isEmpty()) {
            et_hora_inicio.setText(checklist.getHora_inicio());
        }

        if (checklist.getHora_termino() != null && !checklist.getHora_termino().isEmpty()) {
            et_hora_termino.setText(checklist.getHora_termino());
        }

        if (checklist.getLinea() != null && !checklist.getLinea().isEmpty()) {
            et_linea.setText(checklist.getLinea());
        }

        if (checklist.getRegistro_anpros() != null && !checklist.getRegistro_anpros().isEmpty()) {
            et_registro_anpros.setText(checklist.getRegistro_anpros());
        }

        if (checklist.getSuperficie_sembrada() != null && !checklist.getSuperficie_sembrada().isEmpty()) {
            et_superficie_sembrada.setText(checklist.getSuperficie_sembrada());
        }

        if (checklist.getPeso_real_stock_seed() != null && !checklist.getPeso_real_stock_seed().isEmpty()) {
            et_peso_real_stock_seed.setText(checklist.getPeso_real_stock_seed());
        }
        if (checklist.getPropuesta() != null && !checklist.getPropuesta().isEmpty()) {
            rb_propuesta_si.setChecked(checklist.getPropuesta().equals("SI"));
            rb_propuesta_no.setChecked(checklist.getPropuesta().equals("NO"));
        }
        if (checklist.getObservaciones() != null && !checklist.getObservaciones().isEmpty()) {
            et_observaciones.setText(checklist.getObservaciones());
        }

        List<String> brm_option = Arrays.asList(getResources().getStringArray(R.array.desplegable_checklist_3));
        List<String> sino_option = Arrays.asList(getResources().getStringArray(R.array.desplegable_checklist_1));
        List<String> simpleDoble_option = Arrays.asList(getResources().getStringArray(R.array.desplegable_checklist_2));

        if (checklist.getEstado_cama_raices() != null && !checklist.getEstado_cama_raices().isEmpty()) {
            sp_estado_cama_raices.setSelection(brm_option.indexOf(checklist.getEstado_cama_raices()));
        }

        if (checklist.getEstado_cama_semilla() != null && !checklist.getEstado_cama_semilla().isEmpty()) {
            sp_estado_cama_semilla.setSelection(brm_option.indexOf(checklist.getEstado_cama_semilla()));
        }

        if (checklist.getHumedad_suelo() != null && !checklist.getHumedad_suelo().isEmpty()) {
            sp_humedad_suelo.setSelection(brm_option.indexOf(checklist.getHumedad_suelo()));
        }

        if (checklist.getMicro_nivelacion() != null && !checklist.getMicro_nivelacion().isEmpty()) {
            sp_micro_nivelacion.setSelection(brm_option.indexOf(checklist.getMicro_nivelacion()));
        }
        if (checklist.getCompactacion() != null && !checklist.getCompactacion().isEmpty()) {
            sp_compactacion.setSelection(brm_option.indexOf(checklist.getCompactacion()));
        }

        if (checklist.getTarros_sembradores_pre() != null && !checklist.getTarros_sembradores_pre().isEmpty()) {
            sp_tarros_prestadores_pre.setSelection(sino_option.indexOf(checklist.getTarros_sembradores_pre()));
        }
        if (checklist.getTarros_sembradores_post() != null && !checklist.getTarros_sembradores_post().isEmpty()) {
            sp_tarros_prestadores_post.setSelection(sino_option.indexOf(checklist.getTarros_sembradores_post()));
        }
        if (checklist.getDisco_pre() != null && !checklist.getDisco_pre().isEmpty()) {
            sp_discos_pre.setSelection(sino_option.indexOf(checklist.getDisco_pre()));
        }

        if (checklist.getDisco_post() != null && !checklist.getDisco_post().isEmpty()) {
            sp_discos_post.setSelection(sino_option.indexOf(checklist.getDisco_post()));
        }

        if (checklist.getLinea_anterior() != null && !checklist.getLinea_anterior().isEmpty()) {
            et_linea_anterior.setText(checklist.getLinea_anterior());
        }

        if (checklist.getDistancia_hileras_t1() != null && !checklist.getDistancia_hileras_t1().isEmpty()) {
            et_distancia_hileras_t1.setText(checklist.getDistancia_hileras_t1());
        }
        if (checklist.getDistancia_hileras_t2() != null && !checklist.getDistancia_hileras_t2().isEmpty()) {
            et_distancia_hileras_t2.setText(checklist.getDistancia_hileras_t2());
        }
        if (checklist.getDistancia_hileras_t3() != null && !checklist.getDistancia_hileras_t3().isEmpty()) {
            et_distancia_hileras_t3.setText(checklist.getDistancia_hileras_t3());
        }
        if (checklist.getDistancia_hileras_t4() != null && !checklist.getDistancia_hileras_t4().isEmpty()) {
            et_distancia_hileras_t4.setText(checklist.getDistancia_hileras_t4());
        }
        if (checklist.getDistancia_hileras_t5() != null && !checklist.getDistancia_hileras_t5().isEmpty()) {
            et_distancia_hileras_t5.setText(checklist.getDistancia_hileras_t5());
        }
        if (checklist.getDistancia_hileras_t6() != null && !checklist.getDistancia_hileras_t6().isEmpty()) {
            et_distancia_hileras_t6.setText(checklist.getDistancia_hileras_t6());
        }

        if (checklist.getN_semillas_t1() != null && !checklist.getN_semillas_t1().isEmpty()) {
            et_n_semillas_t1.setText(checklist.getN_semillas_t1());
        }
        if (checklist.getN_semillas_t2() != null && !checklist.getN_semillas_t2().isEmpty()) {
            et_n_semillas_t2.setText(checklist.getN_semillas_t2());
        }
        if (checklist.getN_semillas_t3() != null && !checklist.getN_semillas_t3().isEmpty()) {
            et_n_semillas_t3.setText(checklist.getN_semillas_t3());
        }
        if (checklist.getN_semillas_t4() != null && !checklist.getN_semillas_t4().isEmpty()) {
            et_n_semillas_t4.setText(checklist.getN_semillas_t4());
        }
        if (checklist.getN_semillas_t5() != null && !checklist.getN_semillas_t5().isEmpty()) {
            et_n_semillas_t5.setText(checklist.getN_semillas_t5());
        }
        if (checklist.getN_semillas_t6() != null && !checklist.getN_semillas_t6().isEmpty()) {
            et_n_semillas_t6.setText(checklist.getN_semillas_t6());
        }

        if (checklist.getProf_siembra_t1() != null && !checklist.getProf_siembra_t1().isEmpty()) {
            et_prof_siembra_t1.setText(checklist.getProf_siembra_t1());
        }
        if (checklist.getProf_siembra_t2() != null && !checklist.getProf_siembra_t2().isEmpty()) {
            et_prof_siembra_t2.setText(checklist.getProf_siembra_t2());
        }
        if (checklist.getProf_siembra_t3() != null && !checklist.getProf_siembra_t3().isEmpty()) {
            et_prof_siembra_t3.setText(checklist.getProf_siembra_t3());
        }
        if (checklist.getProf_siembra_t4() != null && !checklist.getProf_siembra_t4().isEmpty()) {
            et_prof_siembra_t4.setText(checklist.getProf_siembra_t4());
        }
        if (checklist.getProf_siembra_t5() != null && !checklist.getProf_siembra_t5().isEmpty()) {
            et_prof_siembra_t5.setText(checklist.getProf_siembra_t5());
        }
        if (checklist.getProf_siembra_t6() != null && !checklist.getProf_siembra_t6().isEmpty()) {
            et_prof_siembra_t6.setText(checklist.getProf_siembra_t6());
        }

        if (checklist.getProf_fertilizante_t1() != null && !checklist.getProf_fertilizante_t1().isEmpty()) {
            et_prof_fert_t1.setText(checklist.getProf_fertilizante_t1());
        }
        if (checklist.getProf_fertilizante_t2() != null && !checklist.getProf_fertilizante_t2().isEmpty()) {
            et_prof_fert_t2.setText(checklist.getProf_fertilizante_t2());
        }
        if (checklist.getProf_fertilizante_t3() != null && !checklist.getProf_fertilizante_t3().isEmpty()) {
            et_prof_fert_t3.setText(checklist.getProf_fertilizante_t3());
        }
        if (checklist.getProf_fertilizante_t4() != null && !checklist.getProf_fertilizante_t4().isEmpty()) {
            et_prof_fert_t4.setText(checklist.getProf_fertilizante_t4());
        }
        if (checklist.getProf_fertilizante_t5() != null && !checklist.getProf_fertilizante_t5().isEmpty()) {
            et_prof_fert_t5.setText(checklist.getProf_fertilizante_t5());
        }
        if (checklist.getProf_fertilizante_t6() != null && !checklist.getProf_fertilizante_t6().isEmpty()) {
            et_prof_fert_t6.setText(checklist.getProf_fertilizante_t6());
        }


        if (checklist.getDist_fet_semilla_t1() != null && !checklist.getDist_fet_semilla_t1().isEmpty()) {
            et_dist_fert_semilla_t1.setText(checklist.getDist_fet_semilla_t1());
        }

        if (checklist.getDist_fet_semilla_t2() != null && !checklist.getDist_fet_semilla_t2().isEmpty()) {
            et_dist_fert_semilla_t2.setText(checklist.getDist_fet_semilla_t2());
        }
        if (checklist.getDist_fet_semilla_t3() != null && !checklist.getDist_fet_semilla_t3().isEmpty()) {
            et_dist_fert_semilla_t3.setText(checklist.getDist_fet_semilla_t3());
        }
        if (checklist.getDist_fet_semilla_t4() != null && !checklist.getDist_fet_semilla_t4().isEmpty()) {
            et_dist_fert_semilla_t4.setText(checklist.getDist_fet_semilla_t4());
        }
        if (checklist.getDist_fet_semilla_t5() != null && !checklist.getDist_fet_semilla_t5().isEmpty()) {
            et_dist_fert_semilla_t5.setText(checklist.getDist_fet_semilla_t5());
        }
        if (checklist.getDist_fet_semilla_t6() != null && !checklist.getDist_fet_semilla_t6().isEmpty()) {
            et_dist_fert_semilla_t6.setText(checklist.getDist_fet_semilla_t6());
        }

        if (checklist.getHilera() != null && !checklist.getHilera().isEmpty()) {
            sp_hilera.setSelection(simpleDoble_option.indexOf(checklist.getHilera()));
        }

        if (checklist.getKg_mezcla() != null && !checklist.getKg_mezcla().isEmpty()) {
            et_kg_mezcla.setText(checklist.getKg_mezcla());
        }

        if (checklist.getRelacion_n() != null && !checklist.getRelacion_n().isEmpty()) {
            et_relacion_n.setText(checklist.getRelacion_n());
        }
        if (checklist.getRelacion_p() != null && !checklist.getRelacion_p().isEmpty()) {
            et_relacion_p.setText(checklist.getRelacion_p());
        }
        if (checklist.getRelacion_k() != null && !checklist.getRelacion_k().isEmpty()) {
            et_relacion_k.setText(checklist.getRelacion_k());
        }
        if (checklist.getRelacion_m() != null && !checklist.getRelacion_m().isEmpty()) {
            et_relacion_m.setText(checklist.getRelacion_m());
        }
        if (checklist.getRelacion_h() != null && !checklist.getRelacion_h().isEmpty()) {
            et_relacion_h.setText(checklist.getRelacion_h());
        }
        if (checklist.getRegulacion_pinones() != null && !checklist.getRegulacion_pinones().isEmpty()) {
            et_regulacion_pinones.setText(checklist.getRegulacion_pinones());
        }
        if (checklist.getSistema_fertilizacion() != null && !checklist.getSistema_fertilizacion().isEmpty()) {
            et_sistema_fertilizacion.setText(checklist.getSistema_fertilizacion());
        }

//                ruta_foto_envase
//        ruta_foto_semilla
//                stringed_foto_envase
//        stringed_foto_semilla

    }

    private boolean onSave(int state, String apellido) {

        String alfaNumerico = getResources().getString(R.string.alfanumericos_con_signos);

        et_observaciones.setText(Utilidades.sanitizarString(et_observaciones.getText().toString(), alfaNumerico));


        ExecutorService executor = Executors.newSingleThreadExecutor();

        CheckListSiembra clActual = new CheckListSiembra();
        String claveUnica = UUID.randomUUID().toString();
        if (checklist != null) {
            claveUnica = checklist.getClave_unica();
            clActual.setId_cl_siembra(checklist.getId_cl_siembra());
            clActual.setFecha_hora_tx(checklist.getFecha_hora_tx());
            clActual.setFecha_hora_mod(Utilidades.fechaActualConHora());
            clActual.setId_usuario_mod(usuario.getId_usuario());
            clActual.setId_usuario(checklist.getId_usuario());

//            clHormonas.setFirma_prestador_servicio_ap_hormonas(checklist.getFirma_prestador_servicio_ap_hormonas());
//            clHormonas.setFirma_responsable_ap_hormonas(checklist.getFirma_responsable_ap_hormonas());

        } else {
            clActual.setFecha_hora_tx(Utilidades.fechaActualConHora());
            clActual.setId_usuario(usuario.getId_usuario());
        }

        Future<List<TempFirmas>> firmasF = executor.submit(()
                -> MainActivity.myAppDB.DaoFirmas()
                .getFirmasByDocum(Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA));


        try {
            List<TempFirmas> firmas = firmasF.get();

            for (TempFirmas ff : firmas) {

                switch (ff.getLugar_firma()) {
                    case Utilidades.DIALOG_TAG_FOTO_CHECKLIST_SIEMBRA_ENVASE:
                        clActual.setRuta_foto_envase(ff.getPath());
                        break;
                    case Utilidades.DIALOG_TAG_FOTO_CHECKLIST_SIEMBRA_SEMILLA:
                        clActual.setRuta_foto_semilla(ff.getPath());
                        break;

                }
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }


        if (!et_relacion_n.getText().toString().isEmpty()) {
            int n = Integer.parseInt(et_relacion_n.getText().toString());
            et_relacion_n.setText((n < 10) ? "0" + n : String.valueOf(n));
        }

        if (!et_relacion_p.getText().toString().isEmpty()) {
            int n = Integer.parseInt(et_relacion_p.getText().toString());
            et_relacion_p.setText((n < 10) ? "0" + n : String.valueOf(n));
        }

        if (!et_relacion_k.getText().toString().isEmpty()) {
            int n = Integer.parseInt(et_relacion_k.getText().toString());
            et_relacion_k.setText((n < 10) ? "0" + n : String.valueOf(n));
        }

        //ultimo cuadro
        clActual.setSistema_fertilizacion(et_sistema_fertilizacion.getText().toString());
        clActual.setRegulacion_pinones(et_regulacion_pinones.getText().toString());
        clActual.setRelacion_h(et_relacion_h.getText().toString());
        clActual.setRelacion_m(et_relacion_m.getText().toString());
        clActual.setRelacion_k(et_relacion_k.getText().toString());
        clActual.setRelacion_p(et_relacion_p.getText().toString());
        clActual.setRelacion_n(et_relacion_n.getText().toString());
        clActual.setKg_mezcla(et_kg_mezcla.getText().toString());
        clActual.setHilera(sp_hilera.getSelectedItem().toString());

        //dist fert semilla
        clActual.setDist_fet_semilla_t6(et_dist_fert_semilla_t6.getText().toString());
        clActual.setDist_fet_semilla_t5(et_dist_fert_semilla_t5.getText().toString());
        clActual.setDist_fet_semilla_t4(et_dist_fert_semilla_t4.getText().toString());
        clActual.setDist_fet_semilla_t3(et_dist_fert_semilla_t3.getText().toString());
        clActual.setDist_fet_semilla_t2(et_dist_fert_semilla_t2.getText().toString());
        clActual.setDist_fet_semilla_t1(et_dist_fert_semilla_t1.getText().toString());
        // prof fertilizante
        clActual.setProf_fertilizante_t6(et_prof_fert_t6.getText().toString());
        clActual.setProf_fertilizante_t5(et_prof_fert_t5.getText().toString());
        clActual.setProf_fertilizante_t4(et_prof_fert_t4.getText().toString());
        clActual.setProf_fertilizante_t3(et_prof_fert_t3.getText().toString());
        clActual.setProf_fertilizante_t2(et_prof_fert_t2.getText().toString());
        clActual.setProf_fertilizante_t1(et_prof_fert_t1.getText().toString());
        //prof siembra
        clActual.setProf_siembra_t6(et_prof_siembra_t6.getText().toString());
        clActual.setProf_siembra_t5(et_prof_siembra_t5.getText().toString());
        clActual.setProf_siembra_t4(et_prof_siembra_t4.getText().toString());
        clActual.setProf_siembra_t3(et_prof_siembra_t3.getText().toString());
        clActual.setProf_siembra_t2(et_prof_siembra_t2.getText().toString());
        clActual.setProf_siembra_t1(et_prof_siembra_t1.getText().toString());
        //n semillas
        clActual.setN_semillas_t6(et_n_semillas_t6.getText().toString());
        clActual.setN_semillas_t5(et_n_semillas_t5.getText().toString());
        clActual.setN_semillas_t4(et_n_semillas_t4.getText().toString());
        clActual.setN_semillas_t3(et_n_semillas_t3.getText().toString());
        clActual.setN_semillas_t2(et_n_semillas_t2.getText().toString());
        clActual.setN_semillas_t1(et_n_semillas_t1.getText().toString());
        //dist hileras
        clActual.setDistancia_hileras_t6(et_distancia_hileras_t6.getText().toString());
        clActual.setDistancia_hileras_t5(et_distancia_hileras_t5.getText().toString());
        clActual.setDistancia_hileras_t4(et_distancia_hileras_t4.getText().toString());
        clActual.setDistancia_hileras_t3(et_distancia_hileras_t3.getText().toString());
        clActual.setDistancia_hileras_t2(et_distancia_hileras_t2.getText().toString());
        clActual.setDistancia_hileras_t1(et_distancia_hileras_t1.getText().toString());


        //general
        clActual.setId_ac_cl_siembra(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));
        clActual.setApellido_checklist(apellido);
        clActual.setClave_unica(claveUnica);
        clActual.setEstado_sincronizacion(0);
        clActual.setEstado_documento(state);

        clActual.setObservaciones(et_observaciones.getText().toString());
        clActual.setPeso_real_stock_seed(et_peso_real_stock_seed.getText().toString());
        clActual.setSuperficie_sembrada(et_superficie_sembrada.getText().toString());
        clActual.setRegistro_anpros(et_registro_anpros.getText().toString());

        String prop = "";
        if (rb_propuesta_si.isChecked()) {
            prop = "SI";
        } else if (rb_propuesta_no.isChecked()) {
            prop = "NO";
        }
        clActual.setPropuesta(prop);
        clActual.setLinea(et_linea.getText().toString());
        clActual.setHora_termino(et_hora_termino.getText().toString());
        clActual.setHora_inicio(et_hora_inicio.getText().toString());
        clActual.setFecha_siembra(Utilidades.voltearFechaBD(et_fecha_siembra.getText().toString()));
        clActual.setOperador(et_operador_maquina.getText().toString());
        clActual.setN_maquina(et_n_maquina.getText().toString());
        clActual.setPrestador_servicio(et_prestador_servicio.getText().toString());


        //limpieza maquina
        clActual.setLinea_anterior(et_linea_anterior.getText().toString());
        clActual.setDisco_post(sp_discos_post.getSelectedItem().toString());
        clActual.setDisco_pre(sp_discos_pre.getSelectedItem().toString());
        clActual.setTarros_sembradores_post(sp_tarros_prestadores_post.getSelectedItem().toString());
        clActual.setTarros_sembradores_pre(sp_tarros_prestadores_pre.getSelectedItem().toString());

        //preparacion suelo
        clActual.setCompactacion(sp_compactacion.getSelectedItem().toString());
        clActual.setMicro_nivelacion(sp_micro_nivelacion.getSelectedItem().toString());
        clActual.setHumedad_suelo(sp_humedad_suelo.getSelectedItem().toString());
        clActual.setEstado_cama_semilla(sp_estado_cama_semilla.getSelectedItem().toString());
        clActual.setEstado_cama_raices(sp_estado_cama_raices.getSelectedItem().toString());


        if (checklist != null) {
            Future<Integer> id = executor.submit(() -> MainActivity.myAppDB.DaoClSiembra().updateClSiembra(clActual));
            try {
                long newId = id.get();
                if (newId > 0) {
                    Toasty.success(requireActivity(), "Guardado con exito", Toast.LENGTH_LONG, true).show();
                    cancelar();
                } else {
                    Toasty.error(requireActivity(), "No se pudo guardar con exito", Toast.LENGTH_LONG, true).show();
                }


            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                Toasty.warning(requireActivity(), "Error al guardar ->" + e.getMessage(), Toast.LENGTH_LONG, true).show();
            }


        } else {
            Future<Long> id = executor.submit(() -> MainActivity.myAppDB.DaoClSiembra().insertClSiembra(clActual));


            try {
                long newId = id.get();
                if (newId > 0) {
                    Toasty.success(requireActivity(), "Guardado con exito", Toast.LENGTH_LONG, true).show();
                    cancelar();
                } else {
                    Toasty.error(requireActivity(), "No se pudo guardar con exito", Toast.LENGTH_LONG, true).show();
                }


            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                Toasty.warning(requireActivity(), "Error al guardar ->" + e.getMessage(), Toast.LENGTH_LONG, true).show();
            }

        }

        return true;
    }

    private void cancelar() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.submit(()
                -> MainActivity.myAppDB.DaoFirmas()
                .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA));
        executorService.shutdown();
        activity.onBackPressed();
    }

}

