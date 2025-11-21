package cl.smapdev.curimapu.fragments.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cl.smapdev.curimapu.BuildConfig;
import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.FotosCheckListRecepcionPlantineraFotoDetalleAdapter;
import cl.smapdev.curimapu.clases.tablas.CheckListRecepcionPlantinera;
import cl.smapdev.curimapu.clases.tablas.CheckListRecepcionPlantineraDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListRecepcionPlantineraDetalleFotos;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.utilidades.CameraUtils;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import es.dmoral.toasty.Toasty;

public class DialogPlantinDetail extends DialogFragment {

    public interface IOnSave {
        void onSave(boolean saved);
    }

    final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private CheckListRecepcionPlantinera clPlantin;
    private CheckListRecepcionPlantineraDetalle detallePlantin;
    private IOnSave IOnSave;
    private Usuario usuario;

    private FotosCheckListRecepcionPlantineraFotoDetalleAdapter adapter_fotos;
    private MainActivity activity;

    private EditText et_variedad, et_n_orden, et_fecha_recepcion, et_fecha_actual, et_cantidad, et_observacion, et_condicion_planta, et_linea_otro;
    private Spinner sp_linea, sp_bandeja_etiquetada, sp_acopio_adecuado, sp_separacion_entre_lineas;
    private Button btn_foto_detalle, btn_guardar_plantin, btn_cancelar_plantin;
    private RecyclerView listado_fotos;

    private LinearLayout contenedor_otro_linea;


    private static final int COD_FOTO = 005;


    private String currentPhotoPath;
    private int tipoFoto;
    private Uri currentPhotoUri;
    private ActivityResultLauncher<Uri> cameraLauncher;

    public void setDetallePlantin(CheckListRecepcionPlantineraDetalle detallePlantin) {
        this.detallePlantin = detallePlantin;
    }

    public void setIOnSave(IOnSave IOnSave) {
        this.IOnSave = IOnSave;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setClPlantin(CheckListRecepcionPlantinera clPlantin) {
        this.clPlantin = clPlantin;
    }

    public static DialogPlantinDetail newInstance(IOnSave onSave, CheckListRecepcionPlantinera clCabecera, CheckListRecepcionPlantineraDetalle clDetalle, Usuario usuario) {
        DialogPlantinDetail dg = new DialogPlantinDetail();
        dg.setIOnSave(onSave);
        dg.setDetallePlantin(clDetalle);
        dg.setUsuario(usuario);
        dg.setClPlantin(clCabecera);
        return dg;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_recepcion_plantin_detail, null);
        builder.setView(view);

        builder.setTitle("Detalle Plantín");

        MainActivity a = (MainActivity) getActivity();
        if (a != null) activity = a;

        bind(view);

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(),
                (Boolean success) -> {
                    if (success) {
                        procesarFotoTomada();
                    } else {
                        Toasty.info(activity, "Captura de foto cancelada", Toast.LENGTH_LONG, true).show();
                    }
                });
        return builder.create();
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            int with = ViewGroup.LayoutParams.WRAP_CONTENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(with, height);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        listadoImagenesCabecera();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        executorService.shutdown();
    }


    public void onSave() {

        String variedad = et_variedad.getText().toString();
        String nOrden = et_n_orden.getText().toString();
        String fechaRecepcion = Utilidades.voltearFechaBD(et_fecha_recepcion.getText().toString());
        String fechaActual = Utilidades.voltearFechaBD(et_fecha_actual.getText().toString());
        String linea = sp_linea.getSelectedItem().toString();
        String cantidadBandejas = et_cantidad.getText().toString();
        String condicionPlanta = et_condicion_planta.getText().toString();
        String bandejaEtiquetada = sp_bandeja_etiquetada.getSelectedItem().toString();
        String acopioAdecuado = sp_acopio_adecuado.getSelectedItem().toString();
        String separacionLineas = sp_separacion_entre_lineas.getSelectedItem().toString();
        String observacion = et_observacion.getText().toString();


        if (variedad.isEmpty() || nOrden.isEmpty() || fechaRecepcion.isEmpty() ||
                fechaActual.isEmpty() || linea.isEmpty() || cantidadBandejas.isEmpty() ||
                condicionPlanta.isEmpty() || bandejaEtiquetada.isEmpty() ||
                acopioAdecuado.isEmpty() || separacionLineas.isEmpty() || observacion.isEmpty()) {
            Toasty.error(requireActivity(), "Debe ingresar todos los campos", Toast.LENGTH_LONG, true).show();
            return;
        }

        if (sp_linea.getSelectedItem().toString().equals("OTRO")) {
            if (et_linea_otro.getText().toString().isEmpty()) {
                Toasty.error(requireActivity(), "Debe ingresar el campo otro en línea", Toast.LENGTH_LONG, true).show();
                return;
            }
            linea = et_linea_otro.getText().toString();
        }

        String claveUnicaCab = (clPlantin == null) ? null : clPlantin.getClave_unica();
        String claveUnicaDet = (detallePlantin == null) ? null : detallePlantin.getClave_unica_rp_detalle();

        String finalLinea = linea;
        executorService.execute(() -> {

            List<CheckListRecepcionPlantineraDetalleFotos> fotos = MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().contarFotosPorClaveCabeceraOVacias(claveUnicaDet);

            if (fotos.isEmpty()) {
                activity.runOnUiThread(() -> Toasty.error(requireActivity(), "Debe ingresar al menos una foto", Toast.LENGTH_LONG, true).show());
                return;
            }

            double cplanta = 0.0;
            int cBandejas = 0;
            int nOrd = 0;
            try {
                cplanta = Double.parseDouble(condicionPlanta);
                cBandejas = Integer.parseInt(cantidadBandejas);
                nOrd = Integer.parseInt(nOrden);
            } catch (NumberFormatException e) {
                cplanta = 0.0;
            }

            if (cplanta <= 0 || cplanta > 5) {
                activity.runOnUiThread(() -> Toasty.error(requireActivity(), "Debe ingresar una condicion de planta entre 1 y 5", Toast.LENGTH_LONG, true).show());
                return;
            }

            try {
                CheckListRecepcionPlantineraDetalle detalle = new CheckListRecepcionPlantineraDetalle();

                detalle.setBandejas_etiquetadas(bandejaEtiquetada);
                detalle.setCantidad_bandeja(cBandejas);
                detalle.setClave_unica_rp_detalle(claveUnicaDet == null ? UUID.randomUUID().toString() : claveUnicaDet);
                detalle.setCondicion_plantas(cplanta);
                detalle.setClave_unica_rp_cabecera(claveUnicaCab);
                detalle.setFecha_actual(fechaActual);
                detalle.setFecha_recepcion(fechaRecepcion);
                detalle.setLinea(finalLinea);
                detalle.setLugar_acopio_adecuado(acopioAdecuado);
                detalle.setN_orden(nOrd);
                detalle.setObservaciones(observacion);
                detalle.setSeparacion_entre_lineas(separacionLineas);
                detalle.setVariedad(variedad);
                detalle.setEstado_sincronizacion(0);

                if (detallePlantin == null) {
                    detalle.setId_usuario_tx(usuario.getId_usuario());
                    detalle.setFecha_hora_tx(Utilidades.fechaActualConHora());
                    MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().insertDetalle(detalle);
                } else {
                    detalle.setId_usuario_tx(detallePlantin.getId_usuario_tx());
                    detalle.setFecha_hora_tx(detallePlantin.getFecha_hora_tx());
                    detalle.setId_usuario_mod(usuario.getId_usuario());
                    detalle.setFecha_hora_mod(Utilidades.fechaActualConHora());
                    detalle.setId_cl_rp_detalle(detallePlantin.getId_cl_rp_detalle());
                    MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().updateDetalle(detalle);
                }

                for (CheckListRecepcionPlantineraDetalleFotos f : fotos) {
                    f.setClave_unica_rp_detalle(detalle.getClave_unica_rp_detalle());
                    f.setClave_unica_rp_cabecera(claveUnicaCab);
                    MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().updateFoto(f);
                }


                activity.runOnUiThread(() -> {
                    Toasty.success(requireActivity(), "Detalle guardado", Toast.LENGTH_LONG, true).show();
                    if (IOnSave != null) IOnSave.onSave(true);
                    Dialog dialog = getDialog();
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                });
            } catch (Exception e) {
                activity.runOnUiThread(() -> Toasty.error(requireActivity(), "Error al guardar detalle: " + e.getMessage(), Toast.LENGTH_LONG, true).show());
            }
        });

    }

    public void bind(View view) {

        et_variedad = view.findViewById(R.id.et_variedad);
        et_n_orden = view.findViewById(R.id.et_n_orden);
        et_fecha_recepcion = view.findViewById(R.id.et_fecha_recepcion);
        et_fecha_actual = view.findViewById(R.id.et_fecha_actual);
        et_cantidad = view.findViewById(R.id.et_cantidad);
        et_observacion = view.findViewById(R.id.et_observacion);
        sp_linea = view.findViewById(R.id.sp_linea);
        sp_bandeja_etiquetada = view.findViewById(R.id.sp_bandeja_etiquetada);
        sp_acopio_adecuado = view.findViewById(R.id.sp_acopio_adecuado);
        sp_separacion_entre_lineas = view.findViewById(R.id.sp_separacion_entre_lineas);
        btn_foto_detalle = view.findViewById(R.id.btn_foto_detalle);
        btn_guardar_plantin = view.findViewById(R.id.btn_guardar_plantin);
        btn_cancelar_plantin = view.findViewById(R.id.btn_cancelar_plantin);
        listado_fotos = view.findViewById(R.id.listado_fotos);
        et_condicion_planta = view.findViewById(R.id.et_condicion_planta);
        et_linea_otro = view.findViewById(R.id.et_linea_otro);
        contenedor_otro_linea = view.findViewById(R.id.contenedor_otro_linea);


        btn_foto_detalle.setOnClickListener(view1 -> {
            executorService.execute(() -> {

                String claveUnicaLinea = (detallePlantin == null) ? null : detallePlantin.getClave_unica_rp_detalle();

                List<CheckListRecepcionPlantineraDetalleFotos> cantidadFotos = MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().contarFotosPorClaveCabeceraOVacias(claveUnicaLinea);
                if (cantidadFotos.size() >= 4) {
                    activity.runOnUiThread(() -> Toasty.error(requireActivity(), "Solo se permiten 4 fotos por línea", Toast.LENGTH_LONG, true).show());
                    return;
                }

                activity.runOnUiThread(this::abrirCamara);
            });

        });

        btn_cancelar_plantin.setOnClickListener(view1 -> preguntarCancelar());
        btn_guardar_plantin.setOnClickListener(view1 -> onSave());


        et_fecha_actual.setText(Utilidades.fechaActualInvSinHora());

        et_fecha_recepcion.setKeyListener(null);
        et_fecha_recepcion.setInputType(InputType.TYPE_NULL);
        et_fecha_recepcion.setOnClickListener(view1 -> Utilidades.levantarFecha(et_fecha_recepcion, requireContext()));
        et_fecha_recepcion.setOnFocusChangeListener((view1, b) -> {
            Utilidades.hideKeyboard(activity);
            if (b) Utilidades.levantarFecha(et_fecha_recepcion, requireContext());
        });


        sp_linea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String seleccionado = parent.getItemAtPosition(position).toString();
                if (seleccionado.equals("OTRO")) {
                    contenedor_otro_linea.setVisibility(View.VISIBLE);
                } else {
                    contenedor_otro_linea.setVisibility(View.GONE);
                    et_linea_otro.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (detallePlantin != null) {

            et_variedad.setText(detallePlantin.getVariedad());
            et_n_orden.setText(String.valueOf(detallePlantin.getN_orden()));
            et_fecha_recepcion.setText(Utilidades.voltearFechaVista(detallePlantin.getFecha_recepcion()));
            et_fecha_actual.setText(Utilidades.voltearFechaVista(detallePlantin.getFecha_actual()));
            et_cantidad.setText(String.valueOf(detallePlantin.getCantidad_bandeja()));
            et_observacion.setText(detallePlantin.getObservaciones());

            List<String> mh_plantin = Arrays.asList(getResources().getStringArray(R.array.desplegable_linea_checklist_plantin));
            int idx = mh_plantin.indexOf(detallePlantin.getLinea());
            if (idx >= 0) {
                sp_linea.setSelection(idx);
                contenedor_otro_linea.setVisibility(View.GONE);
            } else {
                int idxotro = mh_plantin.indexOf("OTRO");
                sp_linea.setSelection(idxotro);
                contenedor_otro_linea.setVisibility(View.VISIBLE);
                et_linea_otro.setText(detallePlantin.getLinea());
            }

            List<String> mh_etiq = Arrays.asList(getResources().getStringArray(R.array.desplegable_bandeja_etiquetada_checklist_plantin));
            sp_bandeja_etiquetada.setSelection(mh_etiq.indexOf(detallePlantin.getBandejas_etiquetadas()));

            List<String> mh_acopio = Arrays.asList(getResources().getStringArray(R.array.desplegable_acopio_adecuado_checklist_plantin));
            sp_acopio_adecuado.setSelection(mh_acopio.indexOf(detallePlantin.getLugar_acopio_adecuado()));

            List<String> mh_entrelineas = Arrays.asList(getResources().getStringArray(R.array.desplegable_separacion_lineas_checklist_plantin));
            sp_separacion_entre_lineas.setSelection(mh_entrelineas.indexOf(detallePlantin.getSeparacion_entre_lineas()));


            et_condicion_planta.setText(String.valueOf(detallePlantin.getCondicion_plantas()));

        }

    }


    public void preguntarCancelar() {
        new AlertDialog.Builder(requireContext())
                .setTitle("¿Cancelar?")
                .setMessage("¿Estás seguro que deseas cancelar? Se perderán los cambios no guardados.")
                .setPositiveButton("Sí", (dialog, which) -> onCancel())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
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


            guardarFoto();

        } catch (Exception e) {
            Toasty.error(requireActivity(), "Error al procesar la foto: " + e.getMessage(), Toast.LENGTH_LONG, true).show();
        }
    }


    private void guardarFoto() {

        executorService.execute(() -> {
            CheckListRecepcionPlantineraDetalleFotos fotos = new CheckListRecepcionPlantineraDetalleFotos();
            fotos.setFecha_tx(Utilidades.fechaActualConHora());
            File file = new File(currentPhotoPath);
            fotos.setNombre_foto(file.getName());
            fotos.setRuta_foto(currentPhotoPath);
            fotos.setClave_unica_foto(UUID.randomUUID().toString());
            MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().insertFoto(fotos);

            activity.runOnUiThread(this::listadoImagenesCabecera);
        });
    }

    private void listadoImagenesCabecera() {
        LinearLayoutManager lManagerH = null, lManagerM = null;
        if (activity != null) {
            lManagerH = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        }

        listado_fotos.setHasFixedSize(true);
        listado_fotos.setLayoutManager(lManagerH);
        executorService.execute(() -> {

            String clave = (detallePlantin == null) ? "" : detallePlantin.getClave_unica_rp_detalle();
            List<CheckListRecepcionPlantineraDetalleFotos> myImageLis = MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().contarFotosPorClaveCabeceraOVacias(clave);

            activity.runOnUiThread(() -> {
                btn_foto_detalle.setEnabled((myImageLis.size() <= 3));

                adapter_fotos = new FotosCheckListRecepcionPlantineraFotoDetalleAdapter(myImageLis, activity,
                        fotos -> muestraImagen(fotos.getRuta_foto()),
                        fotos -> alertForDeletePhoto("¿Estas seguro?", "vas a eliminar la foto", fotos));
                listado_fotos.setAdapter(adapter_fotos);
            });
        });


    }


    public void alertForDeletePhoto(String title, String message, CheckListRecepcionPlantineraDetalleFotos foto) {
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

                executorService.execute(() -> {
                    MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().deleteFoto(foto);

                    activity.runOnUiThread(() -> {
                        listadoImagenesCabecera();
                        builder.dismiss();
                    });

                });


            });
            c.setOnClickListener(v -> builder.dismiss());
        });
        builder.setCancelable(false);
        builder.show();
    }


    private void muestraImagen(final String ruta) {
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_big_img, null);

        final androidx.appcompat.app.AlertDialog builder;
        builder = new androidx.appcompat.app.AlertDialog.Builder(requireActivity())
                .setView(viewInfalted)
                .setPositiveButton("cerrar", (dialogInterface, i) -> {
                }).create();

        final ImageView imageView = viewInfalted.findViewById(R.id.img_alert_foto);

        Picasso.get().load("file:///" + ruta).into(imageView);
        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
            b.setOnClickListener(v -> builder.dismiss());

        });
        builder.setCancelable(false);
        builder.show();
    }


    public void onCancel() {
        executorService.execute(() -> {
            activity.runOnUiThread(() -> {
                Dialog dialog = getDialog();
                if (dialog != null) {
                    dialog.dismiss();
                }

            });
        });
    }

}
