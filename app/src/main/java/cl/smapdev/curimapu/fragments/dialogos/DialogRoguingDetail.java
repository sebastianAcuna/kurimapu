package cl.smapdev.curimapu.fragments.dialogos;


import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cl.smapdev.curimapu.BuildConfig;
import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.FotosCheckListRoguingFotoDetalleAdapter;
import cl.smapdev.curimapu.clases.adapters.SpinnerItem;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingFotoDetalle;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.utilidades.CameraUtils;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.coroutines.ApplicationExecutors;
import es.dmoral.toasty.Toasty;

public class DialogRoguingDetail extends DialogFragment {

    private EditText et_off_type, et_cantidad;
    private Spinner sp_genero;
    private Button btn_nuevo_roguing, btn_guardar_anexo_fecha, btn_posponer_anexo_fecha;
    private RecyclerView listado_fotos;
    private SpinnerItem categoria, subcategoria;

    private String descripcionFueraTipo;


    private MainActivity activity;
    private CheckListRoguingDetalle clDetalle;

    private FotosCheckListRoguingFotoDetalleAdapter adapter_fotos;

    private IOnSave IOnSave;

    private Usuario usuario;

    private String currentPhotoPath;
    private static final int COD_FOTO = 005;
    private File fileImagen;

    public interface IOnSave {
        void onSave(boolean saved);
    }

    public void setDescripcionFueraTipo(String descripcionFueraTipo) {
        this.descripcionFueraTipo = descripcionFueraTipo;
    }

    public void setSubcategoria(SpinnerItem subcategoria) {
        this.subcategoria = subcategoria;
    }

    public void setCategoria(SpinnerItem categoria) {
        this.categoria = categoria;
    }

    public void setClDetalle(CheckListRoguingDetalle clDetalle) {
        this.clDetalle = clDetalle;
    }


    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setIOnSave(IOnSave onSave) {
        this.IOnSave = onSave;
    }


    public static DialogRoguingDetail newInstance(IOnSave onSave, CheckListRoguingDetalle clDetalle, SpinnerItem categoria, SpinnerItem subcategoria, String descripcionFueraTipo, Usuario usuario) {
        DialogRoguingDetail dg = new DialogRoguingDetail();
        dg.setIOnSave(onSave);
        dg.setClDetalle(clDetalle);
        dg.setCategoria(categoria);
        dg.setSubcategoria(subcategoria);
        dg.setUsuario(usuario);
        dg.setDescripcionFueraTipo(descripcionFueraTipo);
        return dg;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_roguing_detail, null);
        builder.setView(view);

        builder.setTitle("Nuevo fuera de tipo");

        MainActivity a = (MainActivity) getActivity();
        if (a != null) activity = a;


        bind(view);

        listadoImagenesCabecera();
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        listadoImagenesCabecera();
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
        startActivityForResult(intent, COD_FOTO);
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

            if (requestCode == COD_FOTO) guardarFoto();

        } catch (Exception e) {
            Log.e("FOTOS", e.getLocalizedMessage());
            System.out.println(e);
        }

    }

    private void guardarFoto() {
        ApplicationExecutors exec = new ApplicationExecutors();
        exec.getBackground().execute(() -> {
            CheckListRoguingFotoDetalle fotos = new CheckListRoguingFotoDetalle();
            fotos.setFecha_tx(Utilidades.fechaActualConHora());
            File file = new File(currentPhotoPath);
            fotos.setNombre_foto(file.getName());
            fotos.setRuta(currentPhotoPath);
            fotos.setClave_unica(UUID.randomUUID().toString());

            MainActivity.myAppDB.DaoCLRoguing().insertRoguingFotoDetalle(fotos);
            exec.getMainThread().execute(this::listadoImagenesCabecera);

        });

        exec.shutDownBackground();

    }

    private void listadoImagenesCabecera() {
        LinearLayoutManager lManagerH = null, lManagerM = null;
        if (activity != null) {
            lManagerH = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        }

        listado_fotos.setHasFixedSize(true);
        listado_fotos.setLayoutManager(lManagerH);

        String clave = (clDetalle == null) ? "" : clDetalle.getClave_unica_detalle();

        List<CheckListRoguingFotoDetalle> myImageLis = MainActivity.myAppDB.DaoCLRoguing().obtenerFotosDetalleSinPadreoPadre(clave);

        btn_nuevo_roguing.setEnabled((myImageLis.size() < 3));

        adapter_fotos = new FotosCheckListRoguingFotoDetalleAdapter(myImageLis, activity,
                fotos -> muestraImagen(fotos.getRuta()),
                fotos -> alertForDeletePhoto("¿Estas seguro?", "vas a eliminar la foto", fotos));
        listado_fotos.setAdapter(adapter_fotos);
    }


    public void alertForDeletePhoto(String title, String message, CheckListRoguingFotoDetalle foto) {
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
                    executor.submit(() -> MainActivity.myAppDB.DaoCLRoguing().deleteFotoRoguingDetalle(foto)).get();
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


    private void onSave() {

        if (sp_genero.getSelectedItem().toString().isEmpty() || et_cantidad.getText().toString().isEmpty() || et_off_type.getText().toString().isEmpty()) {
            Toasty.error(requireContext(),
                    "Debes completar campos todos los campos",
                    Toast.LENGTH_LONG, true).show();
            return;
        }


        try {
            Utilidades.validarNumero(et_cantidad.getText().toString(), "Cantidad ", false);
        } catch (Error e) {
            Toasty.error(requireActivity(), e.getMessage(), Toast.LENGTH_LONG, true).show();
            return;
        }

        try {

            CheckListRoguingDetalle cl = new CheckListRoguingDetalle();

            String claveUnicaDetalle = (clDetalle != null) ? clDetalle.getClave_unica_detalle() : UUID.randomUUID().toString();

            cl.setCantidad(Integer.parseInt(et_cantidad.getText().toString()));
            cl.setGenero(sp_genero.getSelectedItem().toString());
            cl.setDescripcion_fuera_tipo(et_off_type.getText().toString().toUpperCase());
            cl.setId_fuera_tipo_categoria(categoria.getId());
            cl.setId_fuera_tipo_sub_categoria(subcategoria.getId());
            cl.setEstado_sincronizacion(0);

            if (clDetalle != null) {
                cl.setClave_unica_detalle(claveUnicaDetalle);
                cl.setClave_unica_detalle_fecha(clDetalle.getClave_unica_detalle_fecha());
                cl.setClave_unica_roguing(clDetalle.getClave_unica_roguing());
                cl.setId_cl_roguing_detalle(clDetalle.getId_cl_roguing_detalle());
                cl.setFecha_tx(clDetalle.getFecha_tx());
                MainActivity.myAppDB.DaoCLRoguing().updateDetalleRoguing(cl);
            } else {
                cl.setFecha_tx(Utilidades.fechaActualConHora());
                cl.setId_user_tx(usuario.getId_usuario());
                cl.setClave_unica_detalle(claveUnicaDetalle);
                MainActivity.myAppDB.DaoCLRoguing().insertDetallesRoguing(cl);
            }


            List<CheckListRoguingFotoDetalle> myImageLis = MainActivity.myAppDB.DaoCLRoguing().obtenerFotosDetalleSinPadreoPadre(claveUnicaDetalle);

            for (CheckListRoguingFotoDetalle f : myImageLis) {
                f.setClave_unica_detalle(claveUnicaDetalle);
                f.setUser_tx(usuario.getId_usuario());
                MainActivity.myAppDB.DaoCLRoguing().updateRoguingFotoDetalle(f);
            }

            Dialog dialog = getDialog();
            IOnSave.onSave(true);
            if (dialog != null) {
                dialog.dismiss();
            }

        } catch (Exception e) {
            Toasty.error(requireContext(),
                    "No se pudo guardar el detalle " + e.getMessage(),
                    Toast.LENGTH_LONG, true).show();

        }
    }


    private void bind(View view) {

        et_off_type = view.findViewById(R.id.et_off_type);
        et_cantidad = view.findViewById(R.id.et_cantidad);
        sp_genero = view.findViewById(R.id.sp_genero);
        btn_nuevo_roguing = view.findViewById(R.id.btn_nuevo_roguing);
        btn_guardar_anexo_fecha = view.findViewById(R.id.btn_guardar_anexo_fecha);
        btn_posponer_anexo_fecha = view.findViewById(R.id.btn_posponer_anexo_fecha);
        listado_fotos = view.findViewById(R.id.listado_fotos);


        if (subcategoria != null && subcategoria.getId() != 0) {
            et_off_type.setText(subcategoria.getDescripcion());
            et_off_type.setEnabled(false);
        } else {
            et_off_type.setText(descripcionFueraTipo);
        }

        List<String> mh_option = Arrays.asList(getResources().getStringArray(R.array.desplegable_checklist_m_h));
        if (clDetalle != null) {
            et_cantidad.setText(String.valueOf(clDetalle.getCantidad()));
            sp_genero.setSelection(mh_option.indexOf(clDetalle.getGenero()));
        }


        btn_nuevo_roguing.setOnClickListener(v -> abrirCamara());
        btn_guardar_anexo_fecha.setOnClickListener(v -> onSave());
        btn_posponer_anexo_fecha.setOnClickListener(v -> onCancel());

    }

    public void onCancel() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            executorService.submit(() -> MainActivity.myAppDB.DaoCLRoguing().deleteFotosRoguingDetalleSinPadres()).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        executorService.shutdown();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            int with = ViewGroup.LayoutParams.WRAP_CONTENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(with, height);
        }
    }
}
