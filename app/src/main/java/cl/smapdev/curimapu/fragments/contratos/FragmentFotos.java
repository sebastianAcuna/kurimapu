package cl.smapdev.curimapu.fragments.contratos;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Fotos;
import cl.smapdev.curimapu.clases.adapters.FotosListAdapter;
import cl.smapdev.curimapu.clases.utilidades.CameraUtils;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;

public class FragmentFotos extends Fragment {

    private FloatingActionMenu materialDesignFAM;

    private RecyclerView recyclerView;
    private MainActivity activity;

    private SharedPreferences prefs;

    private static final String FIELDBOOKKEY = "fieldbookkey";
    private static final int COD_FOTO = 005;


    private FotosListAdapter adapterFotos;

    private File fileImagen;

    private int fieldbook, estado_visita = 0;

    static FragmentFotos getInstance(int fieldbook){

        FragmentFotos fragment = new FragmentFotos();
        Bundle bundle = new Bundle();
        bundle.putInt(FIELDBOOKKEY, fieldbook);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        MainActivity a = (MainActivity) getActivity();
        if (a != null) {
            activity = a;

        }


    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle bundle = getArguments();
        if (bundle != null){
            this.fieldbook = bundle.getInt(FIELDBOOKKEY);
        }

        prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
        estado_visita = MainActivity.myAppDB.myDao().getEstadoVisita(prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0));

        agregarImagenToList();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(FIELDBOOKKEY, this.fieldbook);
        if(fileImagen != null){
            outState.putParcelable("file_uri", Uri.fromFile(fileImagen));
        }
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null && savedInstanceState.getParcelable("file_uri") != null){
            Uri ui = savedInstanceState.getParcelable("file_uri");
            if (ui != null && ui.getPath() != null){
                fileImagen= new File(ui.getPath());
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fotos, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (!checkPermission()) {
                requestPermission();
            }
        }

        materialDesignFAM = view.findViewById(R.id.material_design_android_floating_action_menu);
        FloatingActionButton material_private = view.findViewById(R.id.material_private);
        FloatingActionButton material_public = view.findViewById(R.id.material_public);
        recyclerView = view.findViewById(R.id.lista_fotos);


        material_private.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(fieldbook <= 0 ){
                    Utilidades.avisoListo(activity, "!Hey", "No puedes ingresar una foto sin seleccionar un estado fenologico, seleccionalo y luego saca la foto.", "entendi");
                }else{
                    if (estado_visita == 2){
                        Utilidades.avisoListo(activity,getResources().getString(R.string.title_dialog_agron),getResources().getString(R.string.visitas_terminadas),getResources().getString(R.string.entiendo));
                    }else{
                        Utilidades.hideKeyboard(activity);
                        abrirCamara(2);
//                    activity.cambiarFragment(new FragmentTakePicture(), "hola", R.anim.slide_in_left,R.anim.slide_out_left);
//                    activity.cambiarFragmentFoto(FragmentTakePicture.getInstance(fieldbook, 2), Utilidades.FRAGMENT_TAKE_PHOTO, R.anim.slide_in_left,R.anim.slide_out_left);
                    }
                }

            }
        });
        material_public.setOnClickListener(v -> {

            if(fieldbook <= 0 ){
                Utilidades.avisoListo(activity, "!Hey", "No puedes ingresar una foto sin seleccionar un estado fenologico, seleccionalo y luego saca la foto.", "entendi");
            }else {
                if (estado_visita == 2) {
                    Utilidades.avisoListo(activity, getResources().getString(R.string.title_dialog_agron), getResources().getString(R.string.visitas_terminadas), getResources().getString(R.string.entiendo));
                } else {
                    Utilidades.hideKeyboard(activity);
                    abrirCamara(0);
                }
            }
        });


    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED ;
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toasty.normal(requireActivity(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 100);
            }
        }
    }

    private void abrirCamara(int vista){

        prefs.edit().remove(Utilidades.VISTA_FOTOS).apply();

        File miFile = new File(Environment.getExternalStoragePublicDirectory("DCIM"), Utilidades.DIRECTORIO_IMAGEN);
        boolean isCreada = miFile.exists();

        if (!isCreada){
            isCreada=miFile.mkdirs();
        }

        if(isCreada){

            long consecutivo = System.currentTimeMillis()/1000;
            String nombre = consecutivo+"_"+vista+"_"+fieldbook+".jpg";
            String path = Environment.getExternalStoragePublicDirectory("DCIM") + File.separator + Utilidades.DIRECTORIO_IMAGEN + File.separator + nombre;

            fileImagen=new File(path);
            prefs.edit().putInt(Utilidades.VISTA_FOTOS, vista).apply();

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            startActivityForResult(intent, COD_FOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == COD_FOTO && resultCode == RESULT_OK) {

            if (fileImagen != null) {

                Bitmap bm = BitmapFactory.decodeFile(fileImagen.getAbsolutePath());
                Integer[] inte = Utilidades.neededRotation(Uri.fromFile(fileImagen));

                int rotation = inte[1];
                int rotationInDegrees = inte[0];

                Matrix m = new Matrix();
                if (rotation != 0) {
                    m.preRotate(rotationInDegrees);
                }

                Bitmap src = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
                ByteArrayOutputStream  bos = null;
                try {
                    bos = new ByteArrayOutputStream();
                    CameraUtils.escribirFechaImg(src, activity).compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    byte[] bitmapdata = bos.toByteArray();

                    FileOutputStream fos = new FileOutputStream(fileImagen.getAbsoluteFile());
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                guardarBD(fileImagen);
            }

        }
    }

    private void guardarBD(File path){

        Fotos fotos = new Fotos();
        fotos.setFecha(Utilidades.fechaActualConHora());
        fotos.setFieldbook(fieldbook);
        fotos.setHora(Utilidades.hora());
        fotos.setNombre_foto(path.getName());
        fotos.setFavorita(false);
        fotos.setPlano(0);
        fotos.setId_ficha_fotos(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
        fotos.setVista(prefs.getInt(Utilidades.VISTA_FOTOS, 0));
        fotos.setRuta(path.getAbsolutePath());
        fotos.setId_visita_foto(prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0));

        Config config = MainActivity.myAppDB.myDao().getConfig();
        if (config != null){
            fotos.setId_dispo_foto(config.getId());
        }

        MainActivity.myAppDB.myDao().insertFotos(fotos);

        if (adapterFotos != null){
            adapterFotos.notifyDataSetChanged();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
    }


    private void agregarImagenToList(){

        RecyclerView.LayoutManager lManager = null;
        if (activity != null){
//            if(activity.getRotation(activity).equals("v")){
//                lManager = new GridLayoutManager(activity, 3);
//            }else{
                lManager = new GridLayoutManager(activity, 2);
//            }
        }


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lManager);

        final String oreg =prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, "");
        final int idVisi =prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0);



        List<Fotos> myImageList = MainActivity.myAppDB.myDao().getFotosByFieldAndViewNom(fieldbook, "",oreg, idVisi );

        adapterFotos = new FotosListAdapter(myImageList,activity, new FotosListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Fotos fotos) {
                showAlertForUpdate(fotos);
            }
        }, new FotosListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Fotos fotos) {

                if (estado_visita == 2){
                    Utilidades.avisoListo(activity,getResources().getString(R.string.title_dialog_agron),getResources().getString(R.string.visitas_terminadas),getResources().getString(R.string.entiendo));
                }else{
                    if (!fotos.isFavorita()){
                        int favoritas = MainActivity.myAppDB.myDao().getCantFavoritasByFieldbookAndFicha(fieldbook,oreg, idVisi);

                        if (favoritas < 3){
                            cambiarFavorita(fotos);
                        }else{
                            Utilidades.avisoListo(activity,getResources().getString(R.string.title_dialog_fav),getResources().getString(R.string.message_dialog_fav),getResources().getString(R.string.message_dialog_btn_ok));
                        }
                    }else{
                        cambiarFavorita(fotos);
                    }
                }




            }
        });

        recyclerView.setAdapter(adapterFotos);
    }

    private void cambiarFavorita(Fotos fotos){

        if (fotos.isFavorita()){
            Toasty.info(activity, getResources().getString(R.string.message_fav_remove), Toast.LENGTH_SHORT, true).show();
            fotos.setFavorita(false);
        }else{
            Toasty.info(activity, getResources().getString(R.string.message_fav), Toast.LENGTH_SHORT, true).show();
            fotos.setFavorita(true);
        }


        MainActivity.myAppDB.myDao().updateFavorita(fotos);
        if (adapterFotos != null){
            adapterFotos.notifyDataSetChanged();
        }

    }




    private void showAlertForUpdate(Fotos foto){
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_big_img,null);
//        String titulo = "Editando " + fotos.getNombreFoto() + " de PAQUETE " + fotos.getEtiquetaPaquete();
        final AlertDialog builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setView(viewInfalted)
                .setPositiveButton("cerrar", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })/*.setNegativeButton("cancelar",null)*/.create();

//        final TextView txt = viewInfalted.findViewById(R.id.et_cambia_nombre_foto);
        final ImageView imageView = viewInfalted.findViewById(R.id.img_alert_foto);

//        txt.setText(medidaAMostrar);
        Picasso.get().load("file:///"+foto.getRuta()).into(imageView);
        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
}
