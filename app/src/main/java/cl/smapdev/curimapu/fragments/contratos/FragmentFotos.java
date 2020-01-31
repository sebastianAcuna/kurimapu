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
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
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
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.FotosListAdapter;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

import static android.app.Activity.RESULT_OK;

public class FragmentFotos extends Fragment {

    private FloatingActionMenu materialDesignFAM;
    private FloatingActionButton material_private, material_public;

    private RecyclerView recyclerView;
    private MainActivity activity;

    private SharedPreferences prefs;

    private static final String FIELDBOOKKEY = "fieldbookkey";
    private static final int COD_FOTO = 005;


    private File fileImagen;

    private int fieldbook;


//    private boolean isLoaded =false,isVisibleToUser;


    public static FragmentFotos getInstance(int fieldbook){

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

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(FIELDBOOKKEY, this.fieldbook);
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
//                Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Permiso consedido", Toast.LENGTH_LONG).show();
            }
        }


        /*if(isVisibleToUser && (!isLoaded)){

            isLoaded=true;
        }*/

        materialDesignFAM = (FloatingActionMenu) view.findViewById(R.id.material_design_android_floating_action_menu);
        material_private = (FloatingActionButton) view.findViewById(R.id.material_private);
        material_public = (FloatingActionButton) view.findViewById(R.id.material_public);
        recyclerView = (RecyclerView) view.findViewById(R.id.lista_fotos);


        material_private.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                abrirCamara(1);
                //TODO something when floating action menu first item clicked

            }
        });
        material_public.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                abrirCamara(0);
                //TODO something when floating action menu second item clicked

            }
        });


        agregarImagenToList();

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
        File miFile = new File(Environment.getExternalStorageDirectory(), Utilidades.DIRECTORIO_IMAGEN);
        boolean isCreada = miFile.exists();

        if (!isCreada){
            isCreada=miFile.mkdirs();
        }

        if(isCreada){



            long consecutivo = System.currentTimeMillis()/1000;
            String nombre = consecutivo+"_"+vista+"_"+fieldbook+".jpg";
            String path = Environment.getExternalStorageDirectory() + File.separator + Utilidades.DIRECTORIO_IMAGEN + File.separator + nombre;

            fileImagen=new File(path);




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

            String nombre = fileImagen.getName();



            Bitmap src = BitmapFactory.decodeFile(fileImagen.getAbsolutePath());




//                Bitmap cameraBmp = MediaStore.Images.Media.getBitmap( activity.getContentResolver(), Uri.fromFile( fileImagen.getAbsoluteFile() )  );

                Matrix m = new Matrix();
                m.postRotate( Utilidades.neededRotation(fileImagen.getAbsoluteFile()) );

                src = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, true);

                Bitmap dest = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);

                String fecha  = Utilidades.fechaActualSinHora();

                float widthI = src.getWidth();
                float heightI= src.getHeight();

                Canvas cs = new Canvas(dest);
                Paint tPaint = new Paint();



                tPaint.setTextSize(35);

                tPaint.setColor(Color.BLUE);
                tPaint.setStyle(Paint.Style.FILL);

                cs.drawBitmap(src, 0f, 0f, null);
                float height = tPaint.measureText("yY");
                float width = tPaint.measureText("xX");


                Log.e("MEDIDAS", "height = " + height + " heightI = " + heightI + " || width = " + width + " widthI = " + widthI);

                cs.drawText(fecha, widthI - widthI/6, heightI - heightI/11, tPaint);

                tPaint.setColor(getResources().getColor(R.color.transparentBlack));
                tPaint.setStrokeWidth(0);

                cs.drawRect(widthI - widthI/5,heightI - heightI/9, widthI - 15f, heightI - 15f, tPaint);


        }
    }







    @Override
    public void onResume() {
        super.onResume();

        if (activity != null){
            Toast.makeText(activity, "ON RESUME "+this.fieldbook, Toast.LENGTH_SHORT).show();
        }

    }




    private void agregarImagenToList(){

        RecyclerView.LayoutManager lManager = null;
        if (activity != null){
            if(activity.getRotation(activity).equals("v")){
                lManager = new GridLayoutManager(activity, 3);
            }else{
                lManager = new GridLayoutManager(activity, 2);
            }
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lManager);


        int[] myImageList = new int[]{R.drawable.f1, R.drawable.f2,R.drawable.f3, R.drawable.f4,R.drawable.f5 };


        FotosListAdapter adapter = new FotosListAdapter(activity,myImageList, new FotosListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int fotos) {
                showAlertForUpdate(fotos);
            }
        }, new FotosListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int fotos) {

            }
        });


        recyclerView.setAdapter(adapter);
    }



    private void showAlertForUpdate(int idFoto){
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
        Picasso.get().load(idFoto).resize(720,800).centerInside().into(imageView);
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
