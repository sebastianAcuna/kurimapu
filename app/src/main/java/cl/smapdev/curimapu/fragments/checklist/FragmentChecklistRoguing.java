package cl.smapdev.curimapu.fragments.checklist;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cl.smapdev.curimapu.BuildConfig;
import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.CheckListRoguingDetailAdapter;
import cl.smapdev.curimapu.clases.adapters.CheckListRoguingDetailFechaAdapter;
import cl.smapdev.curimapu.clases.adapters.FotosCheckListRoguingCabAdapter;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.relaciones.CheckListRoguingFechaCompleto;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguing;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingDetalleFechas;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingFotoCabecera;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.utilidades.CameraUtils;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.coroutines.ApplicationExecutors;
import cl.smapdev.curimapu.fragments.dialogos.DialogRoguingDetailFechas;
import es.dmoral.toasty.Toasty;

public class FragmentChecklistRoguing extends Fragment {

    private CheckListRoguing checklist;
    private MainActivity activity;
    private SharedPreferences prefs;

    private AnexoCompleto anexoCompleto;
    private Usuario usuario;
    private Config config;

    private LinearLayout container_roguing_anteriores, container_roguing_resumen;

    private CheckListRoguingDetailFechaAdapter fechasAdapter;
    private CheckListRoguingDetailAdapter resumenAdapter;

    private FotosCheckListRoguingCabAdapter adapter_cabecera_hembra;
    private FotosCheckListRoguingCabAdapter adapter_cabecera_macho;


    private String currentPhotoPath;
    private static final int COD_FOTO_CAB_H = 005;
    private static final int COD_FOTO_CAB_M = 006;
    private static final int COD_FOTO_DET = 007;
    private File fileImagen;


    private TextView tv_responsable, tv_agricultor, tv_ha, tv_poblacion_hembra,
            tv_poblacion_macho_uno, tv_poblacion_macho_dos, tv_poblacion_macho_tres, tv_especie,
            tv_variedad, tv_anexo, tv_ratio_m, tv_ratio_f, tv_total_offtype_m, tv_total_offtype_h,
            tv_total_offtype_m_percent, tv_total_offtype_h_percent;

    private RecyclerView rv_fotos_cab_hembra, rv_fotos_cab_macho, rv_roguing_anteriores, rv_roguing_actual, rv_roguing_resumen;

    private Button btn_foto_hembra, btn_foto_macho, btn_guardar, btn_cancelar, btn_nuevo_roguing;


    public void setChecklist(CheckListRoguing checklist) {
        this.checklist = checklist;
    }

    public static FragmentChecklistRoguing newInstance(CheckListRoguing clist) {
        FragmentChecklistRoguing fragment = new FragmentChecklistRoguing();
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
        return inflater.inflate(R.layout.fragment_checklist_roguing, container, false);
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
        listadoImagenesCabecera();
        listadoDetalles();
        listadoDetallesAnteriores();
        listadoDetallesResumen();
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

    private void abrirCamara(int codigo) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            Log.e("ERROR IMAGEN", e.getLocalizedMessage());
            Toasty.error(requireActivity(), "No se pudo crear la imagen 2", Toast.LENGTH_LONG, true).show();
        }

        if (photoFile == null) {
            Toasty.error(requireActivity(), "No se pudo crear la imagen 3", Toast.LENGTH_LONG, true).show();
            return;
        }

        Uri photoUri = FileProvider.getUriForFile(requireActivity(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, codigo);
    }


    private void listadoDetallesAnteriores() {

        if (checklist == null) return;

        LinearLayoutManager lManagerH = null;
        if (activity != null) {
            lManagerH = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        }

        rv_roguing_anteriores.setHasFixedSize(true);
        rv_roguing_anteriores.setLayoutManager(lManagerH);

        String clave = checklist.getClave_unica();


        List<CheckListRoguingDetalleFechas> myImageLis = MainActivity.myAppDB.DaoCLRoguing().obtenerDetalleFechaRoguingPorClaveUnicaPadreFinal(clave);

        if (myImageLis.size() >= 4) {
            btn_nuevo_roguing.setEnabled(false);
        }

        container_roguing_anteriores.setVisibility(myImageLis.isEmpty() ? View.GONE : View.VISIBLE);


        List<CheckListRoguingFechaCompleto> lll = new ArrayList<>();


        for (CheckListRoguingDetalleFechas c : myImageLis) {
            CheckListRoguingFechaCompleto com = new CheckListRoguingFechaCompleto();
            com.setFecha(c);
            List<CheckListRoguingDetalle> d = MainActivity.myAppDB.DaoCLRoguing().obtenerDetallePorPadreFecha(c.getClave_unica_detalle_fecha());
            com.setDetalles(d);
            lll.add(com);
        }
        fechasAdapter = new CheckListRoguingDetailFechaAdapter(lll, activity, (d) -> {
        }, (d) -> {
        });
        rv_roguing_anteriores.setAdapter(fechasAdapter);
    }


    private void listadoDetallesResumen() {


        LinearLayoutManager lManagerH = null;
        if (activity != null) {
            lManagerH = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        }

        rv_roguing_resumen.setHasFixedSize(true);
        rv_roguing_resumen.setLayoutManager(lManagerH);


        String clave = (checklist == null) ? "" : checklist.getClave_unica();

        List<CheckListRoguingDetalle> myImageLis = MainActivity.myAppDB.DaoCLRoguing().obtenerResumenFueraTipo(clave);

        int totalH = 0, totalM = 0;
        for (CheckListRoguingDetalle d : myImageLis) {
            if (d.getGenero().equals("Hembra")) {
                totalH += d.getCantidad();
            }
            if (d.getGenero().equals("Macho")) {
                totalM += d.getCantidad();
            }

        }

        tv_total_offtype_m.setText(String.valueOf(totalM));
        tv_total_offtype_h.setText(String.valueOf(totalH));

        double macho1 = (anexoCompleto.getAnexoContrato().poblacion_macho_1 != null) ? Double.parseDouble(anexoCompleto.getAnexoContrato().getPoblacion_macho_1()) : 0.0;
        double macho2 = (anexoCompleto.getAnexoContrato().poblacion_macho_2 != null) ? Double.parseDouble(anexoCompleto.getAnexoContrato().getPoblacion_macho_2()) : 0.0;
        double macho3 = (anexoCompleto.getAnexoContrato().poblacion_macho_3 != null) ? Double.parseDouble(anexoCompleto.getAnexoContrato().getPoblacion_macho_3()) : 0.0;
        double hembra = (anexoCompleto.getAnexoContrato().poblacion_hembra != null) ? Double.parseDouble(anexoCompleto.getAnexoContrato().getPoblacion_hembra()) : 0.0;


        double porcentaje_m = (totalM) / ((macho1 + macho2 + macho3) * Double.parseDouble(anexoCompleto.getAnexoContrato().getHas_contrato()));
        double porcentaje_h = (totalH) / ((hembra) * Double.parseDouble(anexoCompleto.getAnexoContrato().getHas_contrato()));


        tv_total_offtype_m_percent.setText(Utilidades.myDecimalFormat(porcentaje_m * 100));
        tv_total_offtype_h_percent.setText(Utilidades.myDecimalFormat(porcentaje_h * 100));


        container_roguing_resumen.setVisibility(myImageLis.isEmpty() ? View.GONE : View.VISIBLE);
        resumenAdapter = new CheckListRoguingDetailAdapter(myImageLis, activity, (d) -> {
        }, (d) -> {
        });
        rv_roguing_resumen.setAdapter(resumenAdapter);
    }


    private void listadoDetalles() {
        LinearLayoutManager lManagerH = null;
        if (activity != null) {
            lManagerH = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        }

        rv_roguing_actual.setHasFixedSize(true);
        rv_roguing_actual.setLayoutManager(lManagerH);

        String clave = (checklist == null) ? "" : checklist.getClave_unica();


        List<CheckListRoguingDetalleFechas> myImageLis = MainActivity.myAppDB.DaoCLRoguing().obtenerDetalleFechaRoguingPorClaveUnicaPadre(null);
        List<CheckListRoguingDetalleFechas> detallesAnteriores = MainActivity.myAppDB.DaoCLRoguing().obtenerDetalleFechaRoguingPorClaveUnicaPadreFinal(clave);

        if (myImageLis.isEmpty() && detallesAnteriores.size() < 4) {
            btn_nuevo_roguing.setEnabled(true);
            return;
        }
        btn_nuevo_roguing.setEnabled(false);

        List<CheckListRoguingFechaCompleto> lll = new ArrayList<>();


        for (CheckListRoguingDetalleFechas c : myImageLis) {
            CheckListRoguingFechaCompleto com = new CheckListRoguingFechaCompleto();
            com.setFecha(c);
            List<CheckListRoguingDetalle> d = MainActivity.myAppDB.DaoCLRoguing().obtenerDetallePorPadreFecha(c.getClave_unica_detalle_fecha());
            com.setDetalles(d);

            lll.add(com);
        }
        fechasAdapter = new CheckListRoguingDetailFechaAdapter(lll, activity, (d) -> {
            Log.e("CLICK", d.getFecha());
            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentByTag(Utilidades.DIALOG_TAG_ROGUING_DETALLE_FECHA);
            if (prev != null) {
                ft.remove(prev);
            }

            DialogRoguingDetailFechas dialogo = DialogRoguingDetailFechas.newInstance(saved -> {
                listadoDetalles();
                listadoDetallesAnteriores();
                listadoDetallesResumen();
            }, d, usuario, checklist, anexoCompleto);
            dialogo.show(ft, Utilidades.DIALOG_TAG_ROGUING_DETALLE_FECHA);
        }, (d) -> {
            Log.e("LONG CLICK", d.getFecha());
        });
        rv_roguing_actual.setAdapter(fechasAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) return;


        try {
            Bitmap originalBtm = BitmapFactory.decodeFile(currentPhotoPath);
            Bitmap nuevaFoto = CameraUtils.escribirFechaImg(originalBtm, activity);

            currentPhotoPath = currentPhotoPath.replaceAll("Pictures/", "");

            File file = new File(currentPhotoPath);

            FileOutputStream fos = new FileOutputStream(file);
            nuevaFoto.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            if (requestCode == COD_FOTO_CAB_H) guardarBDCabecera("H");
            if (requestCode == COD_FOTO_CAB_M) guardarBDCabecera("M");
//            if (requestCode == COD_FOTO_DET) guardarBD();

        } catch (Exception e) {
            Log.e("FOTOS", e.getLocalizedMessage());
            System.out.println(e);
        }

    }


    private void guardarBDCabecera(String tipo) {


        ApplicationExecutors exec = new ApplicationExecutors();
        exec.getBackground().execute(() -> {
            CheckListRoguingFotoCabecera fotos = new CheckListRoguingFotoCabecera();
            fotos.setFecha_tx(Utilidades.fechaActualConHora());

            Log.e("RUTA 2", currentPhotoPath);
            File file = new File(currentPhotoPath);

            fotos.setClave_unica(UUID.randomUUID().toString());
            fotos.setTipo_foto(tipo);
            fotos.setUser_tx(usuario.getId_usuario());
            fotos.setNombre_foto(file.getName());
            fotos.setRuta(currentPhotoPath);

            MainActivity.myAppDB.DaoCLRoguing().insertFotosRoguing(fotos);
            exec.getMainThread().execute(() -> {
                listadoImagenesCabecera();
            });

        });

        exec.shutDownBackground();

    }


    private void listadoImagenesCabecera() {


        LinearLayoutManager lManagerH = null, lManagerM = null;
        if (activity != null) {
            lManagerH = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
            lManagerM = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        }

        rv_fotos_cab_hembra.setHasFixedSize(true);
        rv_fotos_cab_hembra.setLayoutManager(lManagerH);

        rv_fotos_cab_macho.setHasFixedSize(true);
        rv_fotos_cab_macho.setLayoutManager(lManagerM);

        String clave = (checklist == null) ? "" : checklist.getClave_unica();

        List<CheckListRoguingFotoCabecera> myImageListHembra = MainActivity.myAppDB.DaoCLRoguing().obtenerFotosSinPadreHembra(clave);

        btn_foto_hembra.setEnabled(myImageListHembra.size() < 2);

        adapter_cabecera_hembra = new FotosCheckListRoguingCabAdapter(myImageListHembra, activity,
                fotos -> muestraImagen(fotos.getRuta()),
                fotos -> alertForDeletePhoto("¿Estas seguro?", "vas a eliminar la foto hembra", fotos));
        rv_fotos_cab_hembra.setAdapter(adapter_cabecera_hembra);

        List<CheckListRoguingFotoCabecera> myImageListMacho = MainActivity.myAppDB.DaoCLRoguing().obtenerFotosSinPadreMacho(clave);
        btn_foto_macho.setEnabled(myImageListMacho.size() < 2);
        adapter_cabecera_macho = new FotosCheckListRoguingCabAdapter(myImageListMacho, activity,
                fotos -> muestraImagen(fotos.getRuta()),
                fotos -> alertForDeletePhoto("¿Estas seguro?", "vas a eliminar la foto macho ", fotos));
        rv_fotos_cab_macho.setAdapter(adapter_cabecera_macho);


    }


    public void alertForDeletePhoto(String title, String message, CheckListRoguingFotoCabecera foto) {
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
                    executor.submit(() -> MainActivity.myAppDB.DaoCLRoguing().deleteFotoRoguingCabeceraPorId(foto)).get();
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
        tv_especie.setText(anexoCompleto.getEspecie().getDesc_especie());


//
        tv_poblacion_hembra.setText(anexoCompleto.getAnexoContrato().getPoblacion_hembra());
        tv_poblacion_macho_uno.setText(anexoCompleto.getAnexoContrato().getPoblacion_macho_1());
        tv_poblacion_macho_dos.setText(anexoCompleto.getAnexoContrato().getPoblacion_macho_2());
        tv_poblacion_macho_tres.setText(anexoCompleto.getAnexoContrato().getPoblacion_macho_3());
        tv_ratio_m.setText(String.valueOf(anexoCompleto.getAnexoContrato().getRatio_m()));
        tv_ratio_f.setText(String.valueOf(anexoCompleto.getAnexoContrato().getRatio_f()));
//
    }


    private void bind(View view) {

        tv_responsable = view.findViewById(R.id.tv_responsable);
        tv_agricultor = view.findViewById(R.id.tv_agricultor);
        tv_ha = view.findViewById(R.id.tv_ha);
        tv_poblacion_hembra = view.findViewById(R.id.tv_poblacion_hembra);
        tv_poblacion_macho_uno = view.findViewById(R.id.tv_poblacion_macho_uno);
        tv_poblacion_macho_dos = view.findViewById(R.id.tv_poblacion_macho_dos);
        tv_poblacion_macho_tres = view.findViewById(R.id.tv_poblacion_macho_tres);
        tv_especie = view.findViewById(R.id.tv_especie);
        tv_variedad = view.findViewById(R.id.tv_variedad);
        tv_anexo = view.findViewById(R.id.tv_anexo);
        tv_ratio_m = view.findViewById(R.id.tv_ratio_m);
        tv_ratio_f = view.findViewById(R.id.tv_ratio_f);
        rv_fotos_cab_hembra = view.findViewById(R.id.rv_fotos_cab_hembra);
        rv_fotos_cab_macho = view.findViewById(R.id.rv_fotos_cab_macho);
        rv_roguing_anteriores = view.findViewById(R.id.rv_roguing_anteriores);
        rv_roguing_actual = view.findViewById(R.id.rv_roguing_actual);
        btn_foto_hembra = view.findViewById(R.id.btn_foto_hembra);
        btn_foto_macho = view.findViewById(R.id.btn_foto_macho);
        btn_guardar = view.findViewById(R.id.btn_guardar);
        btn_cancelar = view.findViewById(R.id.btn_cancelar);
        btn_nuevo_roguing = view.findViewById(R.id.btn_nuevo_roguing);
        container_roguing_anteriores = view.findViewById(R.id.container_roguing_anteriores);
        container_roguing_resumen = view.findViewById(R.id.container_roguing_resumen);
        rv_roguing_resumen = view.findViewById(R.id.rv_roguing_resumen);


        tv_total_offtype_m = view.findViewById(R.id.tv_total_offtype_m);
        tv_total_offtype_h = view.findViewById(R.id.tv_total_offtype_h);


        tv_total_offtype_m_percent = view.findViewById(R.id.tv_total_offtype_m_percent);
        tv_total_offtype_h_percent = view.findViewById(R.id.tv_total_offtype_h_percent);


        btn_nuevo_roguing.setOnClickListener(view1 -> {

            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentByTag(Utilidades.DIALOG_TAG_ROGUING_DETALLE_FECHA);
            if (prev != null) {
                ft.remove(prev);
            }

            DialogRoguingDetailFechas dialogo = DialogRoguingDetailFechas.newInstance(saved -> {
                listadoDetalles();
                listadoDetallesAnteriores();
                listadoDetallesResumen();
            }, null, usuario, checklist, anexoCompleto);
            dialogo.show(ft, Utilidades.DIALOG_TAG_ROGUING_DETALLE_FECHA);
        });

        btn_foto_hembra.setOnClickListener(view1 -> abrirCamara(COD_FOTO_CAB_H));
        btn_foto_macho.setOnClickListener(view1 -> abrirCamara(COD_FOTO_CAB_M));


        btn_guardar.setOnClickListener(view1 -> onSave());
        btn_cancelar.setOnClickListener(view1 -> cancelar());

    }

    private void onSave() {

        String apellido = anexoCompleto.getAgricultor().getNombre_agricultor().toLowerCase();
        String claveUnica = (checklist == null) ? UUID.randomUUID().toString() : checklist.getClave_unica();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            List<CheckListRoguingDetalleFechas> cFechas = executor.submit(() -> MainActivity.myAppDB.DaoCLRoguing().obtenerDetalleFechaRoguingPorClaveUnicaPadre(claveUnica)).get();

            if (cFechas.isEmpty()) {
                Toasty.error(requireActivity(), "Debes tener al menos una fecha de roguing", Toast.LENGTH_LONG, true).show();
                return;
            }

            CheckListRoguing chk = new CheckListRoguing();
            chk.setEstado_sincronizacion(0);
            chk.setEstado_documento(1);
            chk.setId_ac_cl_roguing(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));
            chk.setApellido_checklist(apellido);
            chk.setClave_unica(claveUnica);
            chk.setN_total_plantas_hembra("0");
            chk.setN_total_plantas_macho("0");

            if (checklist != null) {
                chk.setId_usuario_mod(usuario.getId_usuario());
                chk.setId_cl_roguing(checklist.getId_cl_roguing());
                chk.setFecha_hora_mod(Utilidades.fechaActualConHora());
                chk.setId_usuario(checklist.getId_usuario());
                executor.submit(() -> MainActivity.myAppDB.DaoCLRoguing().updateclroguing(chk)).get();

            } else {
                chk.setId_usuario(usuario.getId_usuario());
                chk.setFecha_hora_tx(Utilidades.fechaActualConHora());
                executor.submit(() -> MainActivity.myAppDB.DaoCLRoguing().insertclroguing(chk)).get();
            }

            executor.submit(() -> MainActivity.myAppDB.DaoCLRoguing().updateDetalleRoguingClaveUnicaPadreFinal(claveUnica)).get();
            executor.submit(() -> MainActivity.myAppDB.DaoCLRoguing().updateDetalleFechaRoguingClaveUnicaPadreFinal(claveUnica)).get();
            executor.submit(() -> MainActivity.myAppDB.DaoCLRoguing().updateFotoCabRoguingClaveUnicaPadreFinal(claveUnica)).get();
            executor.submit(() -> MainActivity.myAppDB.DaoCLRoguing().updateFotoDetRoguingClaveUnicaPadreFinal(claveUnica)).get();

            Toasty.success(requireActivity(), "Guardado con exito", Toast.LENGTH_LONG, true).show();
            cancelar();

        } catch (ExecutionException | InterruptedException e) {
            Toasty.error(requireActivity(), "No se pudo guardar el roguing" + e.getMessage(), Toast.LENGTH_LONG, true).show();
        } finally {
            executor.shutdown();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        cargarDatosPrevios();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (activity != null) {
            activity.updateView(getResources().getString(R.string.app_name), "CHECKLIST ROGUING");
        }
    }

    private void cancelar() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        try {
            executorService.submit(() -> MainActivity.myAppDB.DaoCLRoguing().deleteFotosRoguingSinPadres()).get();
            executorService.submit(() -> MainActivity.myAppDB.DaoCLRoguing().deleteRoguingDetalleSinPadreFinal()).get();
            executorService.submit(() -> MainActivity.myAppDB.DaoCLRoguing().deleteFotosRoguingDetalleSinPadreFinal()).get();
            executorService.submit(() -> MainActivity.myAppDB.DaoCLRoguing().deleteDetalleFechaSinPadreFinal()).get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
        activity.onBackPressed();
    }


}
