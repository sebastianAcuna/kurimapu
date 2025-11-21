package cl.smapdev.curimapu.fragments.checklist;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import cl.smapdev.curimapu.clases.adapters.CheckListRevisionFrutosDetailAdapter;
import cl.smapdev.curimapu.clases.adapters.FotosCheckListRevFrutosAdapter;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.tablas.CheckListRevisionFrutos;
import cl.smapdev.curimapu.clases.tablas.CheckListRevisionFrutosDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListRevisionFrutosFotos;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.temporales.TempFirmas;
import cl.smapdev.curimapu.clases.utilidades.CameraUtils;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.coroutines.ApplicationExecutors;
import cl.smapdev.curimapu.fragments.dialogos.DialogFirma;
import cl.smapdev.curimapu.fragments.dialogos.DialogRevisionFrutoDetail;
import es.dmoral.toasty.Toasty;

public class FragmentChecklistRevisionFrutos extends Fragment {

    private CheckListRevisionFrutos checklist;
    private MainActivity activity;
    private SharedPreferences prefs;


    private CheckListRevisionFrutosDetailAdapter detailAdapter;
    private FotosCheckListRevFrutosAdapter fotosAdapter;

    private AnexoCompleto anexoCompleto;
    private Usuario usuario;
    private Config config;

    private TextView
            tv_responsable,
            tv_variedad,
            tv_agricultor,
            tv_potrero,
            tv_ha,
            tv_anexo,
            tv_total_frutos_aptos,
            tv_total_frutos_no_aptos,
            tv_porcentaje_frutos_no_aptos;

    private int total_frutos_aptos = 0, total_frutos_no_aptos = 0;
    private double porcentaje_frutos_no_aptos = 0.0;


    private EditText et_fecha;

    private ImageView check_firma_agricultor;

    private Button
            btn_firma_agricultor,
            btn_guardar,
            btn_nuevo_conteo,
            btn_nueva_foto,
            btn_cancelar;


    private Spinner
            sp_termino_cosecha,
            sp_autorizada_cosecha;

    private RecyclerView rv_detalle_revision_frutos, rv_fotos_frutos;


    private File fileImagen;
    private static final int COD_FOTO = 006;

    private String currentPhotoPath;
    private int tipoFoto;
    private Uri currentPhotoUri;
    private ActivityResultLauncher<Uri> cameraLauncher;

    // Executor reutilizable para operaciones DB rápidas (evita crear/shutdown por click)
    private final ExecutorService singleDbExecutor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());


    public void setChecklist(CheckListRevisionFrutos checklist) {
        this.checklist = checklist;
    }

    public static FragmentChecklistRevisionFrutos newInstance(CheckListRevisionFrutos clist) {
        FragmentChecklistRevisionFrutos fragment = new FragmentChecklistRevisionFrutos();
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
        return inflater.inflate(R.layout.fragment_checklist_revision_frutos, container, false);
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
        obtenerDetalles();
        mostrarResumen();
        listadoImagenesCabecera();

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
        Utilidades.setToolbar(activity, view, getResources().getString(R.string.app_name), "CHECKLIST REVISION FRUTOS");
    }


    @Override
    public void onStart() {
        super.onStart();
        cargarDatosPrevios();
    }

    private void bind(View view) {

        tv_responsable = view.findViewById(R.id.tv_responsable);
        tv_variedad = view.findViewById(R.id.tv_variedad);
        tv_agricultor = view.findViewById(R.id.tv_agricultor);
        tv_potrero = view.findViewById(R.id.tv_potrero);
        tv_ha = view.findViewById(R.id.tv_ha);
        tv_anexo = view.findViewById(R.id.tv_anexo);
        tv_total_frutos_aptos = view.findViewById(R.id.tv_total_frutos_aptos);
        tv_total_frutos_no_aptos = view.findViewById(R.id.tv_total_frutos_no_aptos);
        tv_porcentaje_frutos_no_aptos = view.findViewById(R.id.tv_porcentaje_frutos_no_aptos);

        et_fecha = view.findViewById(R.id.et_fecha);


        check_firma_agricultor = view.findViewById(R.id.check_firma_agricultor);
        rv_fotos_frutos = view.findViewById(R.id.rv_fotos_frutos);

        sp_termino_cosecha = view.findViewById(R.id.sp_termino_cosecha);
        sp_autorizada_cosecha = view.findViewById(R.id.sp_autorizada_cosecha);

        rv_detalle_revision_frutos = view.findViewById(R.id.rv_detalle_revision_frutos);

        btn_firma_agricultor = view.findViewById(R.id.btn_firma_agricultor);
        btn_guardar = view.findViewById(R.id.btn_guardar);
        btn_cancelar = view.findViewById(R.id.btn_cancelar);
        btn_nuevo_conteo = view.findViewById(R.id.btn_nuevo_conteo);
        btn_nueva_foto = view.findViewById(R.id.btn_nueva_foto);

        et_fecha.setKeyListener(null);
        et_fecha.setInputType(InputType.TYPE_NULL);
        et_fecha.setOnClickListener(view1 -> Utilidades.levantarFecha(et_fecha, requireContext()));
        et_fecha.setOnFocusChangeListener((view1, b) -> {
            Utilidades.hideKeyboard(activity);
            if (b) Utilidades.levantarFecha(et_fecha, requireContext());
        });

        btn_nueva_foto.setOnClickListener(v -> abrirCamara());

        btn_nuevo_conteo.setOnClickListener(v -> {

            ExecutorService executor = Executors.newSingleThreadExecutor();
            try {

                String claveUnica = (checklist == null) ? "" : checklist.getClave_unica();

                List<CheckListRevisionFrutosDetalle> detalle = executor.submit(() -> MainActivity.myAppDB.DaoCheckListRevisionFrutos().obtenerDetallesPorClaveUnicaPadreOrNull(claveUnica)).get();

                FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                Fragment prev = requireActivity()
                        .getSupportFragmentManager()
                        .findFragmentByTag(Utilidades.DIALOG_TAG_REVISION_FRUTO_DETALLE);
                if (prev != null) {
                    ft.remove(prev);
                }

                DialogRevisionFrutoDetail dialogo = DialogRevisionFrutoDetail.newInstance(saved -> {
                    obtenerDetalles();
                    mostrarResumen();
                }, null, detalle.size() + 1);
                dialogo.show(ft, Utilidades.DIALOG_TAG_ROGUING_DETALLE_FECHA);

            } catch (ExecutionException | InterruptedException e) {
                Toasty.error(requireActivity(), "No se pudo abrir el dialogo" + e.getMessage(), Toast.LENGTH_LONG, true).show();

            } finally {
                executor.shutdown();
            }


        });

        btn_firma_agricultor.setOnClickListener(view1 -> {

            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentByTag(Utilidades.DIALOG_TAG_FIRMA_AGRICULTOR_REVISION_FRUTOS);
            if (prev != null) {
                ft.remove(prev);
            }

            String etRA = UUID.randomUUID().toString() + ".png";

            DialogFirma dialogo = DialogFirma.newInstance(
                    Utilidades.TIPO_DOCUMENTO_CHECKLIST_REVISION_FRUTOS,
                    etRA,
                    Utilidades.DIALOG_TAG_FIRMA_AGRICULTOR_REVISION_FRUTOS,
                    (isSaved, path) -> {
                        if (isSaved) {
                            check_firma_agricultor.setVisibility(View.VISIBLE);
                        }
                    }
            );

            dialogo.show(ft, Utilidades.DIALOG_TAG_FIRMA_AGRICULTOR_REVISION_FRUTOS);
        });


        btn_guardar.setOnClickListener(view1 -> onSave());
        btn_cancelar.setOnClickListener(view1 -> cancelar());

    }


    private void abrirCamara() {

        File photoFile = null;
        String uid = UUID.randomUUID().toString();
        String nombre = uid + "_";

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


    /* IMAGENES */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = UUID.randomUUID().toString();
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
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


            guardarFotoBd();

        } catch (Exception e) {
            Toasty.error(requireActivity(), "Error al procesar la foto: " + e.getMessage(), Toast.LENGTH_LONG, true).show();
        }
    }


    private void guardarFotoBd() {


        ApplicationExecutors exec = new ApplicationExecutors();
        exec.getBackground().execute(() -> {
            CheckListRevisionFrutosFotos fotos = new CheckListRevisionFrutosFotos();


            File file = new File(currentPhotoPath);

            fotos.setClave_unica_foto(UUID.randomUUID().toString());
            fotos.setNombre_foto(file.getName());
            fotos.setRuta_foto(currentPhotoPath);
            fotos.setEstado_sincronizacion(0);

            MainActivity.myAppDB.DaoCheckListRevisionFrutos().insertFotosRevFrutos(fotos);
            exec.getMainThread().execute(this::listadoImagenesCabecera);

        });

        exec.shutDownBackground();

    }


    private void listadoImagenesCabecera() {


        LinearLayoutManager lManagerH = null;
        if (activity != null) {
            lManagerH = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        }

        rv_fotos_frutos.setHasFixedSize(true);
        rv_fotos_frutos.setLayoutManager(lManagerH);

        String clave = (checklist == null) ? "" : checklist.getClave_unica();

        List<CheckListRevisionFrutosFotos> myImageListHembra = MainActivity.myAppDB.DaoCheckListRevisionFrutos().obtenerFotosPorClaveUnicaPadreOrNull(clave);


        fotosAdapter = new FotosCheckListRevFrutosAdapter(myImageListHembra, activity,
                fotos -> muestraImagen(fotos.getRuta_foto()),
                fotos -> alertForDeletePhoto("¿Estas seguro?", "vas a eliminar la foto", fotos));
        rv_fotos_frutos.setAdapter(fotosAdapter);


    }


    public void alertForDeletePhoto(String title, String message, CheckListRevisionFrutosFotos foto) {
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_empty, null);

        final AlertDialog builder = new AlertDialog.Builder(activity)
                .setView(viewInfalted)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("eliminar", (dialogInterface, i) -> {
                })
                .setNegativeButton("cancelar", (dialog, which) -> {
                })
                .create();

        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
            Button c = builder.getButton(AlertDialog.BUTTON_NEGATIVE);
            b.setOnClickListener(v -> {

                ExecutorService executor = Executors.newSingleThreadExecutor();
                try {
                    executor.submit(() -> MainActivity.myAppDB.DaoCheckListRevisionFrutos().deleteFotosRevisionFrutos(foto)).get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                executor.shutdown();
                listadoImagenesCabecera();
                builder.dismiss();
            });
            c.setOnClickListener(v -> builder.dismiss());
        });
        builder.setCancelable(false);
        builder.show();
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

    private void obtenerDetalles() {
        ExecutorService ex = Executors.newSingleThreadExecutor();
        try {

            String clave = (checklist == null) ? "" : checklist.getClave_unica();

            List<CheckListRevisionFrutosDetalle> det =
                    ex.submit(() -> MainActivity.myAppDB.DaoCheckListRevisionFrutos().obtenerDetallesPorClaveUnicaPadreOrNull(clave)).get();


            LinearLayoutManager lManagerH = null;
            if (activity != null) {
                lManagerH = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            }

            rv_detalle_revision_frutos.setHasFixedSize(true);
            rv_detalle_revision_frutos.setLayoutManager(lManagerH);

            btn_nuevo_conteo.setEnabled(det.size() < 10);


            detailAdapter = new CheckListRevisionFrutosDetailAdapter(det, activity, (d, c) -> {

                Toast.makeText(getContext(), "click", Toast.LENGTH_LONG).show();
                FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                Fragment prev = requireActivity()
                        .getSupportFragmentManager()
                        .findFragmentByTag(Utilidades.DIALOG_TAG_REVISION_FRUTO_DETALLE);
                if (prev != null) {
                    ft.remove(prev);
                }

                DialogRevisionFrutoDetail dialogo = DialogRevisionFrutoDetail.newInstance(saved -> {
                    obtenerDetalles();
                    mostrarResumen();
                }, d, c);
                dialogo.show(ft, Utilidades.DIALOG_TAG_ROGUING_DETALLE_FECHA);


            }, (d) -> {
            });

            rv_detalle_revision_frutos.setAdapter(detailAdapter);


        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            ex.shutdown();
        }
    }


    public void alertForDeletePhoto(String title, String message, CheckListRevisionFrutosDetalle foto) {
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_empty, null);

        final androidx.appcompat.app.AlertDialog builder = new androidx.appcompat.app.AlertDialog.Builder(activity)
                .setView(viewInfalted)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("eliminar", (dialogInterface, i) -> {
                })
                .setNegativeButton("cancelar", (dialog, which) -> {
                })
                .create();

        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
            Button c = builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE);
            b.setOnClickListener(v -> {

                ExecutorService executor = Executors.newSingleThreadExecutor();
                try {
                    executor.submit(() -> MainActivity.myAppDB.DaoCheckListRevisionFrutos().deleteDetalleRevisionFrutos(foto)).get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                executor.shutdown();
                obtenerDetalles();
                builder.dismiss();
            });
            c.setOnClickListener(v -> builder.dismiss());
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void mostrarResumen() {

        total_frutos_aptos = 0;
        total_frutos_no_aptos = 0;
        porcentaje_frutos_no_aptos = 0;

        int conteoAptos = 0;
        int conteoNoAptos = 0;
        ExecutorService ex = Executors.newSingleThreadExecutor();
        try {

            String clave = (checklist == null) ? "" : checklist.getClave_unica();

            List<CheckListRevisionFrutosDetalle> det =
                    ex.submit(() -> MainActivity.myAppDB.DaoCheckListRevisionFrutos().obtenerDetallesPorClaveUnicaPadreOrNull(clave)).get();


            for (CheckListRevisionFrutosDetalle d : det) {
                total_frutos_aptos += d.getFrutos_aptos();
                total_frutos_no_aptos += d.getFrutos_no_aptos();
            }

            porcentaje_frutos_no_aptos = (double) total_frutos_no_aptos / (total_frutos_aptos + total_frutos_no_aptos) * 100;

            tv_total_frutos_aptos.setText(Utilidades.myNumberFormat(total_frutos_aptos));
            tv_total_frutos_no_aptos.setText(Utilidades.myNumberFormat(total_frutos_no_aptos));
            tv_porcentaje_frutos_no_aptos.setText(Utilidades.myDecimalFormat(porcentaje_frutos_no_aptos));

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            ex.shutdown();
        }

    }


    private void cargarDatosPrevios() {
        if (anexoCompleto == null) {
            Toasty.error(requireActivity(), "No se pudo obtener informacion del anexo", Toast.LENGTH_LONG, true).show();
            return;
        }

        tv_anexo.setText(anexoCompleto.getAnexoContrato().getAnexo_contrato());
        tv_variedad.setText(anexoCompleto.getVariedad().getDesc_variedad());
        tv_agricultor.setText(anexoCompleto.getAgricultor().getNombre_agricultor());
        tv_ha.setText(anexoCompleto.getAnexoContrato().getHas_contrato());
        tv_responsable.setText(usuario.getNombre() + " " + usuario.getApellido_p());
        tv_potrero.setText(anexoCompleto.getLotes().getNombre_lote());
    }


    private void levantarDatos() {

        if (checklist.getFecha() != null && !checklist.getFecha().isEmpty()) {
            et_fecha.setText(Utilidades.voltearFechaVista(checklist.getFecha()));
        }

        List<String> sino_option = Arrays.asList(getResources().getStringArray(R.array.desplegable_checklist_1));

        if (checklist.getAutoriza_cosecha() != null && !checklist.getAutoriza_cosecha().isEmpty()) {
            sp_autorizada_cosecha.setSelection(sino_option.indexOf(checklist.getAutoriza_cosecha()));
        }
        if (checklist.getTermino_cosecha() != null && !checklist.getTermino_cosecha().isEmpty()) {
            sp_termino_cosecha.setSelection(sino_option.indexOf(checklist.getTermino_cosecha()));
        }
    }

    private void onSave() {

        try {
            Utilidades.validarFecha(Utilidades.voltearFechaBD(et_fecha.getText().toString()), "fecha");
        } catch (Error e) {
            Toasty.error(requireActivity(), e.getMessage(), Toast.LENGTH_LONG, true).show();
            return;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<List<TempFirmas>> firmasF = executor.submit(()
                -> MainActivity.myAppDB.DaoFirmas()
                .getFirmasByDocum(Utilidades.TIPO_DOCUMENTO_CHECKLIST_REVISION_FRUTOS));

        String clave = (checklist == null) ? "" : checklist.getClave_unica();

        try {

            List<CheckListRevisionFrutosDetalle> detalles = executor.submit(() -> MainActivity.myAppDB.DaoCheckListRevisionFrutos().obtenerDetallesPorClaveUnicaPadreOrNull(clave)).get();
            List<CheckListRevisionFrutosFotos> fotos = executor.submit(() -> MainActivity.myAppDB.DaoCheckListRevisionFrutos().obtenerFotosPorClaveUnicaPadreOrNull(clave)).get();

            if (detalles.isEmpty()) {
                Toasty.error(requireActivity(), "Debe tener al menos un conteo", Toast.LENGTH_LONG, true).show();
                return;
            }

            CheckListRevisionFrutos clActual = new CheckListRevisionFrutos();
            String claveUnica = (checklist == null) ? UUID.randomUUID().toString() : checklist.getClave_unica();
            String apellido = anexoCompleto.getAgricultor().getNombre_agricultor() + " " + Utilidades.fechaActualConHora();

            List<TempFirmas> firmas = firmasF.get();
            for (TempFirmas ff : firmas) {
                if (ff.getLugar_firma().equals(Utilidades.DIALOG_TAG_FIRMA_AGRICULTOR_REVISION_FRUTOS)) {
                    clActual.setFirma_agricultor(ff.getPath());
                }
            }

            clActual.setClave_unica(claveUnica);
            clActual.setEstado_documento(1);
            clActual.setEstado_sincronizacion(0);
            clActual.setApellido_checklist(apellido);
            clActual.setId_ac_cl_revision_frutos(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));
            clActual.setEstado_fenologico("COSECHA");
            clActual.setFecha(Utilidades.voltearFechaBD(et_fecha.getText().toString()));
            clActual.setAutoriza_cosecha(sp_autorizada_cosecha.getSelectedItem().toString());
            clActual.setTermino_cosecha(sp_termino_cosecha.getSelectedItem().toString());
            clActual.setTotal_frutos_aptos(total_frutos_aptos);
            clActual.setTotal_frutos_no_aptos(total_frutos_no_aptos);

            if (checklist != null) {
                clActual.setId_cl_revision_frutos(checklist.getId_cl_revision_frutos());
                clActual.setFecha_hora_tx(checklist.getFecha_hora_tx());
                clActual.setFecha_hora_mod(Utilidades.fechaActualConHora());
                clActual.setId_usuario_mod(usuario.getId_usuario());
                clActual.setId_usuario(checklist.getId_usuario());

                executor.submit(() -> MainActivity.myAppDB.DaoCheckListRevisionFrutos().updateclrevisionFrutos(clActual)).get();

            } else {
                clActual.setFecha_hora_tx(Utilidades.fechaActualConHora());
                clActual.setId_usuario(usuario.getId_usuario());

                executor.submit(() -> MainActivity.myAppDB.DaoCheckListRevisionFrutos().insertclrevisionFrutos(clActual)).get();
            }

            for (CheckListRevisionFrutosDetalle d : detalles) {
                d.setClave_unica_revision_frutos(claveUnica);
                executor.submit(() -> MainActivity.myAppDB.DaoCheckListRevisionFrutos().updateclrevisionFrutosDetalle(d)).get();
            }

            for (CheckListRevisionFrutosFotos d : fotos) {
                d.setClave_unica_revision_frutos(claveUnica);
                executor.submit(() -> MainActivity.myAppDB.DaoCheckListRevisionFrutos().updateclrevisionFrutosFotos(d)).get();
            }


            Toasty.success(requireActivity(), "Guardado con exito", Toast.LENGTH_LONG, true).show();
            cancelar();

        } catch (ExecutionException | InterruptedException e) {
            Toasty.error(requireActivity(), "No se pudo guardar" + e.getMessage(), Toast.LENGTH_LONG, true).show();
        } finally {
            executor.shutdown();
        }
    }

    private void cancelar() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        try {
            executorService.submit(()
                    -> MainActivity.myAppDB.DaoFirmas()
                    .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CHECKLIST_REVISION_FRUTOS)).get();

            executorService.submit(() -> MainActivity.myAppDB.DaoCheckListRevisionFrutos().deleteDetalleRevisionFrutosSinPadre()).get();
        } catch (ExecutionException | InterruptedException ignored) {
        } finally {
            executorService.shutdown();

        }

        activity.onBackPressed();
    }

}

