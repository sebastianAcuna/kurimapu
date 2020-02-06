package cl.smapdev.curimapu.fragments.contratos;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import cl.smapdev.curimapu.clases.Fotos;
import cl.smapdev.curimapu.clases.adapters.FotosListAdapter;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

import static android.app.Activity.RESULT_OK;

public class FragmentFotosResumen  extends Fragment {

    private MainActivity activity = null ;

    private RecyclerView rwAgronomo, rwCliente;

    private static final String FIELDBOOKKEY = "fieldbookkey";
    private static final int COD_FOTO = 005;

    private SharedPreferences prefs;

    private File fileImagen;

    private int fieldbook;

    private FloatingActionMenu materialDesignFAM;
    private FloatingActionButton material_private, material_public;

    private FotosListAdapter adapterAgronomo;
    private FotosListAdapter adapterCliente;

//todo pasar a form visitas todo este puto fragment

    public static FragmentFotosResumen getInstance(int fieldbook){

        FragmentFotosResumen fragment = new FragmentFotosResumen();
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
            prefs = activity.getSharedPreferences(Utilidades.SHARED_PREFERENCE, Context.MODE_PRIVATE);
        }

    }


    @Override
    public void onStart() {
        super.onStart();

        Bundle bundle = getArguments();
        if (bundle != null){
            this.fieldbook = bundle.getInt(FIELDBOOKKEY);
        }


        agregarImagenToAgronomos();
        agregarImagenToClientes();
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

        // get the file url
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
        return inflater.inflate(R.layout.fragment_fotos_resumen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        rwAgronomo = (RecyclerView) view.findViewById(R.id.fotos_agronomos);
        rwCliente = (RecyclerView) view.findViewById(R.id.fotos_clientes);

        materialDesignFAM = (FloatingActionMenu) view.findViewById(R.id.material_design_android_floating_action_menu);
        material_private = (FloatingActionButton) view.findViewById(R.id.material_private);
        material_public = (FloatingActionButton) view.findViewById(R.id.material_public);


        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (!checkPermission()) {
                requestPermission();
            }
        }


        material_private.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int cantidaAgr = MainActivity.myAppDB.myDao().getCantAgroByFieldViewAndFicha(0, 2, 0);
                if (cantidaAgr > 2){
                    Utilidades.avisoListo(activity,getResources().getString(R.string.title_dialog_agron),getResources().getString(R.string.message_dialog_agron),getResources().getString(R.string.message_dialog_btn_ok));
                }else{
                    abrirCamara(2);
                }

                //TODO something when floating action menu first item clicked

            }
        });
        material_public.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                abrirCamara(0);
                //TODO something when floating action menu second item clicked

            }
        });

    }



    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED ;
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getActivity(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 100);
            }
        }
    }


    private void abrirCamara(int vista){

        prefs.edit().remove(Utilidades.VISTA_FOTOS).apply();

        File miFile = new File(Environment.getExternalStorageDirectory(), Utilidades.DIRECTORIO_IMAGEN);
        boolean isCreada = miFile.exists();

        if (!isCreada){
            isCreada=miFile.mkdirs();
        }

        if(isCreada){

            long consecutivo = System.currentTimeMillis()/1000;
            String nombre = consecutivo+"_"+vista+"_0.jpg";
            String path = Environment.getExternalStorageDirectory() + File.separator + Utilidades.DIRECTORIO_IMAGEN + File.separator + nombre;

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



                ByteArrayOutputStream bos = null;
                try {
                    bos = new ByteArrayOutputStream();
                    escribirFechaImg(src).compress(Bitmap.CompressFormat.JPEG, 100, bos);
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
        fotos.setFieldbook(0);
        fotos.setHora(Utilidades.hora());
        fotos.setNombre_foto(path.getName());
        fotos.setFavorita(false);
        fotos.setPlano(0);
        fotos.setId_ficha(0);
        fotos.setVista(prefs.getInt(Utilidades.VISTA_FOTOS, 0));
        fotos.setRuta(path.getAbsolutePath());

        MainActivity.myAppDB.myDao().insertFotos(fotos);

        if (adapterAgronomo != null){
            adapterAgronomo.notifyDataSetChanged();
        }

        if (adapterCliente != null){
            adapterCliente.notifyDataSetChanged();
        }

    }



    //    todo agregar imagen de sello de agua ;)
    private Bitmap escribirFechaImg(Bitmap bm){

        Bitmap dest = Bitmap.createBitmap(bm,0, 0,bm.getWidth(), bm.getHeight()).copy(Bitmap.Config.ARGB_8888, true);

        String fecha = Utilidades.fechaActualInvSinHora();

        Canvas cs = new Canvas(dest);


        Paint myPaint = new Paint();
        myPaint.setColor(getResources().getColor(R.color.transparentBlack));
        myPaint.setStrokeWidth(10);
        cs.drawRect(0, dest.getHeight() - 90, 700, dest.getHeight() - 10, myPaint);


        Paint tPaint = new Paint();
        tPaint.setStyle(Paint.Style.FILL);
        tPaint.setColor(getResources().getColor(android.R.color.white));
        tPaint.setTextSize(80);
        tPaint.setStrokeWidth(10);



        // text shadow
        tPaint.setShadowLayer(1f, 0f, 1f, getResources().getColor(android.R.color.black));

        Rect bounds = new Rect();
        tPaint.getTextBounds(fecha, 0, fecha.length(), bounds);

        cs.drawText(fecha, 90, dest.getHeight() - 25 , tPaint);





        return dest;
    }



    private void agregarImagenToAgronomos(){



        LinearLayoutManager lManager = null;
        if (activity != null){
//            lManager = new GridLayoutManager(activity, 1);
             lManager  = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);

        }

        rwAgronomo.setHasFixedSize(true);
        rwAgronomo.setLayoutManager(lManager);


//        int[] myImageList = new int[]{R.drawable.f1, R.drawable.f2 };
        List<Fotos> myImageList = MainActivity.myAppDB.myDao().getFotosByFieldAndView(0, 2);

        adapterAgronomo = new FotosListAdapter(myImageList,activity, new FotosListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Fotos fotos) {
                showAlertForUpdate(fotos);
            }
        }, new FotosListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Fotos fotos) {

                if (!fotos.isFavorita()){
                    int favoritas = MainActivity.myAppDB.myDao().getCantFavoritasByFieldbookFichaAndVista(0, 0,2);

                    if (favoritas < 3){
                        cambiarFavorita(fotos);
                    }else{
                        Utilidades.avisoListo(activity,getResources().getString(R.string.title_dialog_fav),getResources().getString(R.string.message_dialog_fav),getResources().getString(R.string.message_dialog_btn_ok));
                    }
                }else{
                    cambiarFavorita(fotos);
                }

            }
        });


        rwAgronomo.setAdapter(adapterAgronomo);
    }


    private void cambiarFavorita(Fotos fotos){

        if (fotos.isFavorita()){
            Toast.makeText(activity, getResources().getString(R.string.message_fav_remove), Toast.LENGTH_SHORT).show();
            fotos.setFavorita(false);
        }else{
            Toast.makeText(activity, getResources().getString(R.string.message_fav), Toast.LENGTH_SHORT).show();
            fotos.setFavorita(true);
        }


        MainActivity.myAppDB.myDao().updateFavorita(fotos);
        if (adapterAgronomo != null){
            adapterAgronomo.notifyDataSetChanged();
        }

        if (adapterCliente != null){
            adapterCliente.notifyDataSetChanged();
        }

    }




    private void agregarImagenToClientes(){



        LinearLayoutManager lManager = null;
        if (activity != null){
//            lManager = new GridLayoutManager(activity, 1);
            lManager  = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);

        }

        rwCliente.setHasFixedSize(true);
        rwCliente.setLayoutManager(lManager);


        List<Fotos> myImageList = MainActivity.myAppDB.myDao().getFotosByFieldAndView(0, 0);
//        int[] myImageList = new int[]{R.drawable.f1, R.drawable.f2,R.drawable.f3, R.drawable.f4,R.drawable.f5 };


        adapterCliente = new FotosListAdapter(myImageList, activity,new FotosListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Fotos fotos) {
                showAlertForUpdate(fotos);
            }
        }, new FotosListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Fotos fotos) {

                if (!fotos.isFavorita()){
                    int favoritas = MainActivity.myAppDB.myDao().getCantFavoritasByFieldbookFichaAndVista(0, 0, 0);


                    if (favoritas < 3){
                        cambiarFavorita(fotos);
                    }else{
                        Utilidades.avisoListo(activity,getResources().getString(R.string.title_dialog_fav),getResources().getString(R.string.message_dialog_fav),getResources().getString(R.string.message_dialog_btn_ok));
                    }
                }else{
                    cambiarFavorita(fotos);
                }

            }
        });


        rwCliente.setAdapter(adapterCliente);
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

        final TextView txt = viewInfalted.findViewById(R.id.et_cambia_nombre_foto);
        final ImageView imageView = viewInfalted.findViewById(R.id.img_alert_foto);
        String medidaAMostrar = "Nombre prueba";
        txt.setText(medidaAMostrar);
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
