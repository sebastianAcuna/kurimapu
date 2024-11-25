package cl.smapdev.curimapu.fragments.checklist;


import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
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

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.tablas.CheckListRevisionFrutos;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.temporales.TempFirmas;
import cl.smapdev.curimapu.clases.utilidades.CameraUtils;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import es.dmoral.toasty.Toasty;

public class FragmentChecklistRevisionFrutos extends Fragment {

    private CheckListRevisionFrutos checklist;
    private MainActivity activity;
    private SharedPreferences prefs;


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


    private EditText et_fecha;

    private ImageView check_firma_agricultor;

    private Button
            btn_firma_agricultor,
            btn_guardar,
            btn_cancelar;


    private Spinner sp_estado_fenologico,
            sp_termino_cosecha,
            sp_autorizada_cosecha;

    private RecyclerView rv_detalle_revision_frutos;


    private File fileImagen;
    private static final int COD_FOTO = 006;


    public void setChecklist(CheckListRevisionFrutos checklist) {
        this.checklist = checklist;
    }

    public static FragmentChecklistRevisionFrutos newInstance(CheckListRevisionFrutos clist) {
        FragmentChecklistRevisionFrutos fragment = new FragmentChecklistRevisionFrutos();
        fragment.setChecklist(clist);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity a = (MainActivity) getActivity();
        if (a != null) activity = a;
        prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
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

        if (checklist != null) {
            levantarDatos();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (activity != null) {
            activity.updateView(getResources().getString(R.string.app_name), "CHECKLIST SIEMBRA");
        }
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

        sp_estado_fenologico = view.findViewById(R.id.sp_estado_fenologico);
        sp_termino_cosecha = view.findViewById(R.id.sp_termino_cosecha);
        sp_autorizada_cosecha = view.findViewById(R.id.sp_autorizada_cosecha);

        rv_detalle_revision_frutos = view.findViewById(R.id.rv_detalle_revision_frutos);

        btn_firma_agricultor = view.findViewById(R.id.btn_firma_agricultor);
        btn_guardar = view.findViewById(R.id.btn_guardar);
        btn_cancelar = view.findViewById(R.id.btn_cancelar);

        et_fecha.setOnClickListener(view1 -> Utilidades.levantarFecha(et_fecha, requireContext()));
        et_fecha.setOnFocusChangeListener((view1, b) -> {
            if (b) Utilidades.levantarFecha(et_fecha, requireContext());
        });


//        btn_tomar_foto_envase.setOnClickListener(view1 -> {
//            abrirCamara("ENVASE");
//        });

        btn_guardar.setOnClickListener(view1 -> {
            showAlertForConfirmarGuardar();
        });
        btn_cancelar.setOnClickListener(view1 -> activity.onBackPressed());

    }


    private void abrirCamara() {

        File miFile = new File(Environment.getExternalStoragePublicDirectory("DCIM"), Utilidades.DIRECTORIO_IMAGEN);
        boolean isCreada = miFile.exists();

        if (!isCreada) {
            isCreada = miFile.mkdirs();
        }

        if (isCreada) {

            String uid = UUID.randomUUID().toString();
            String nombre = uid + "_REV_FRUTOS.jpg";
            String path = Environment.getExternalStoragePublicDirectory("DCIM") + File.separator + Utilidades.DIRECTORIO_IMAGEN + File.separator + nombre;
            fileImagen = new File(path);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            startActivityForResult(intent, COD_FOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK || fileImagen == null || requestCode != COD_FOTO) {
            return;
        }

        Bitmap bm = BitmapFactory.decodeFile(fileImagen.getAbsolutePath());
        Integer[] inte = Utilidades.neededRotation(Uri.fromFile(fileImagen));
        int rotation = inte[1];
        int rotationInDegrees = inte[0];

        Matrix m = new Matrix();
        if (rotation != 0) {
            m.preRotate(rotationInDegrees);
        }

        if (bm != null) {
            Bitmap src = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);

            ByteArrayOutputStream bos = null;
            try {
                bos = new ByteArrayOutputStream();
                CameraUtils.escribirFechaImg(src, activity).compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] bitmapdata = bos.toByteArray();

                FileOutputStream fos = new FileOutputStream(fileImagen.getAbsoluteFile());
                fos.write(bitmapdata);
                fos.flush();
                fos.close();

            } catch (IOException e) {
                Log.e("ERROR -- FOTOS", e.getLocalizedMessage());
            }
        }


        // ExecutorService exe = Executors.newSingleThreadExecutor();


//        String lugar = (requestCode == COD_FOTO_ENVASE) ? Utilidades.DIALOG_TAG_FOTO_CHECKLIST_SIEMBRA_ENVASE : Utilidades.DIALOG_TAG_FOTO_CHECKLIST_SIEMBRA_SEMILLA;
//
//        TempFirmas tempFirmas = new TempFirmas();
//        tempFirmas.setTipo_documento(Utilidades.TIPO_DOCUMENTO_CHECKLIST_SIEMBRA);
//        tempFirmas.setPath(fileImagen.getPath());
//        tempFirmas.setLugar_firma(lugar);
//        exe.submit(() -> MainActivity.myAppDB.DaoFirmas().insertFirma(tempFirmas));
//
//        if (requestCode == COD_FOTO_ENVASE) {
//            btn_ver_foto_envase.setEnabled(true);
//        } else {
//            btn_ver_foto_semilla.setEnabled(true);
//        }


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
            return;
        }

        tv_anexo.setText(anexoCompleto.getAnexoContrato().getAnexo_contrato());
        tv_variedad.setText(anexoCompleto.getVariedad().getDesc_variedad());
        tv_agricultor.setText(anexoCompleto.getAgricultor().getNombre_agricultor());
        tv_ha.setText(anexoCompleto.getAnexoContrato().getHas_contrato());
        tv_responsable.setText(usuario.getNombre() + " " + usuario.getApellido_p());
        tv_potrero.setText(anexoCompleto.getLotes().getNombre_lote());
    }


    private void showAlertForConfirmarGuardar() {
        View viewInfalted = LayoutInflater.from(requireActivity()).inflate(R.layout.alert_guardar_checklist, null);

        RadioButton rbtn_activo = viewInfalted.findViewById(R.id.rbtn_activo);
        RadioButton rbtn_pendiente = viewInfalted.findViewById(R.id.rbtn_pendiente);
        EditText et_apellido = viewInfalted.findViewById(R.id.et_apellido);

        rbtn_activo.setChecked(true);

        rbtn_activo.setVisibility(View.GONE);
        rbtn_pendiente.setVisibility(View.GONE);

        if (checklist != null) {
            et_apellido.setText(checklist.getApellido_checklist());
        }


//        if (checklist != null) {
//
//            et_apellido.setText(checklist.getApellido_checklist());
//
//            if (checklist.getEstado_documento() > 0) {
//                rbtn_activo.setChecked(checklist.getEstado_documento() == 1);
//                rbtn_pendiente.setChecked(checklist.getEstado_documento() == 2);
//            }
//
//        }

        final AlertDialog builder = new AlertDialog.Builder(requireActivity())
                .setView(viewInfalted)
                .setPositiveButton(getResources().getString(R.string.guardar), (dialogInterface, i) -> {
                })
                .setNegativeButton(getResources().getString(R.string.nav_cancel), (dialogInterface, i) -> {
                })
                .create();

        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
            Button c = builder.getButton(AlertDialog.BUTTON_NEGATIVE);
            b.setOnClickListener(view -> {

                if ((!rbtn_activo.isChecked() && !rbtn_pendiente.isChecked()) || et_apellido.getText().toString().isEmpty()) {
                    Toasty.error(requireActivity(),
                            "Debes seleccionar un estado e ingresar una descripcion",
                            Toast.LENGTH_LONG, true).show();
                    return;
                }
                int state = (rbtn_activo.isChecked()) ? 1 : 2;
                boolean isSaved = onSave(state, et_apellido.getText().toString());
                if (isSaved) {
                    builder.dismiss();
                    activity.onBackPressed();
                }


            });
            c.setOnClickListener(view -> builder.dismiss());
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void levantarDatos() {

        if (checklist.getFecha() != null && !checklist.getFecha().isEmpty()) {
            et_fecha.setText(checklist.getFecha());
        }

        List<String> sino_option = Arrays.asList(getResources().getStringArray(R.array.desplegable_checklist_1));
        List<String> est_feno = Arrays.asList(getResources().getStringArray(R.array.fenologico));

        if (checklist.getAutoriza_cosecha() != null && !checklist.getAutoriza_cosecha().isEmpty()) {
            sp_autorizada_cosecha.setSelection(sino_option.indexOf(checklist.getAutoriza_cosecha()));
        }
        if (checklist.getTermino_cosecha() != null && !checklist.getTermino_cosecha().isEmpty()) {
            sp_termino_cosecha.setSelection(sino_option.indexOf(checklist.getTermino_cosecha()));
        }
        if (checklist.getEstado_fenologico() != null && !checklist.getEstado_fenologico().isEmpty()) {
            sp_estado_fenologico.setSelection(sino_option.indexOf(checklist.getEstado_fenologico()));
        }


//                check_firma_agricultor
//                rv_detalle_revision_frutos
//                btn_firma_agricultor


    }

    private boolean onSave(int state, String apellido) {


//        ruta_foto_envase
//        ruta_foto_semilla
//        stringed_foto_envase
//        stringed_foto_semilla


        ExecutorService executor = Executors.newSingleThreadExecutor();

        CheckListRevisionFrutos clActual = new CheckListRevisionFrutos();
        String claveUnica = UUID.randomUUID().toString();
        if (checklist != null) {
            claveUnica = checklist.getClave_unica();
            clActual.setId_cl_revision_frutos(checklist.getId_cl_revision_frutos());
            clActual.setFecha_hora_tx(checklist.getFecha_hora_tx());
            clActual.setFecha_hora_mod(Utilidades.fechaActualConHora());
            clActual.setId_usuario_mod(usuario.getId_usuario());
            clActual.setId_usuario(checklist.getId_usuario());

        } else {
            clActual.setFecha_hora_tx(Utilidades.fechaActualConHora());
            clActual.setId_usuario(usuario.getId_usuario());
        }

        Future<List<TempFirmas>> firmasF = executor.submit(()
                -> MainActivity.myAppDB.DaoFirmas()
                .getFirmasByDocum(Utilidades.TIPO_DOCUMENTO_CHECKLIST_REVISION_FRUTOS));


        try {
            List<TempFirmas> firmas = firmasF.get();
            for (TempFirmas ff : firmas) {
                if (ff.getLugar_firma().equals(Utilidades.DIALOG_TAG_FIRMA_AGRICULTOR_REVISION_FRUTOS)) {
                    clActual.setFirma_agricultor(ff.getPath());
                }
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        //general
        clActual.setId_ac_cl_revision_frutos(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));
        clActual.setApellido_checklist(apellido);
        clActual.setClave_unica(claveUnica);
        clActual.setEstado_sincronizacion(0);
        clActual.setEstado_documento(state);
        clActual.setFecha(Utilidades.voltearFechaBD(et_fecha.getText().toString()));


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

