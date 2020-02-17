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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import cl.smapdev.curimapu.clases.adapters.FotosListAdapter;
import cl.smapdev.curimapu.clases.adapters.SpinnerAdapter;
import cl.smapdev.curimapu.clases.tablas.Fotos;
import cl.smapdev.curimapu.clases.tablas.Visitas;
import cl.smapdev.curimapu.clases.temporales.TempVisitas;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.FragmentVisitas;

import static android.app.Activity.RESULT_OK;

public class FragmentFormVisitas extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, View.OnFocusChangeListener {

    private Spinner sp_fenologico,sp_cosecha,sp_crecimiento,sp_fito,sp_general_cultivo,sp_humedad,sp_malezas;

    private Button btn_guardar, btn_volver;

    private MainActivity activity;
    private SharedPreferences prefs;

    private TempVisitas temp_visitas;

    private String[] fenologico, cosecha, crecimiento, maleza;

    private EditText et_obs, et_recomendacion;



    /* IMAGENES */
    private RecyclerView rwAgronomo, rwCliente;

//    private static final String FIELDBOOKKEY = "fieldbookkey";
    private static final int COD_FOTO = 005;

    private File fileImagen;

    private int fieldbook;

    private FloatingActionButton material_private, material_public;

    private FotosListAdapter adapterAgronomo;
    private FotosListAdapter adapterCliente;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity a = (MainActivity) getActivity();
        if ( a != null) activity = a;

        prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);


        fenologico = getResources().getStringArray(R.array.fenologico);
        cosecha = getResources().getStringArray(R.array.cosecha);
        crecimiento = getResources().getStringArray(R.array.crecimiento);
        maleza = getResources().getStringArray(R.array.maleza);




    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form_visitas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind(view);


   /*     if (prefs != null && prefs.getInt(Utilidades.SHARED_VISIT_FICHA_ID, 0) > 0){
            temp_visitas = MainActivity.myAppDB.myDao().getTempFichas();
        }*/

        if (temp_visitas == null){
            temp_visitas = new TempVisitas();
            if (prefs != null){
                temp_visitas.setId_anexo_temp_visita(prefs.getInt(Utilidades.SHARED_VISIT_ANEXO_ID, 0));
            }
            MainActivity.myAppDB.myDao().setTempVisitas(temp_visitas);
            temp_visitas = MainActivity.myAppDB.myDao().getTempFichas();
        }



        cargarSpinners();



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





        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (!checkPermission()) {
                requestPermission();
            }
        }


    }

    /*
    * GENERA LOS ONITEMSELECTEDLISTENER DE LOS SPINNERS
    * */
    private void accionSpinners(){
        if (temp_visitas != null){

            sp_fenologico.setSelection(temp_visitas.getPhenological_state_temp_visita());
            sp_cosecha.setSelection(temp_visitas.getHarvest_temp_visita());
            sp_crecimiento.setSelection(temp_visitas.getGrowth_status_temp_visita());
            sp_fito.setSelection(temp_visitas.getPhytosanitary_state_temp_visita());
            sp_general_cultivo.setSelection(temp_visitas.getOverall_status_temp_visita());
            sp_humedad.setSelection(temp_visitas.getHumidity_floor_temp_visita());
            sp_malezas.setSelection(temp_visitas.getWeed_state_temp_visita());

            et_obs.setText(temp_visitas.getObservation_temp_visita());
            et_recomendacion.setText(temp_visitas.getRecomendation_temp_visita());

        }
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






    @Override
    public void onResume() {
        super.onResume();
        if (activity != null){
            //activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fotos_resumen, FragmentFotosResumen.getInstance(0), Utilidades.FRAGMENT_FOTOS_RESUMEN).commit();

            agregarImagenToAgronomos();
            agregarImagenToClientes();
        }
    }

    private void cargarSpinners(){

        sp_fenologico.setAdapter(new SpinnerAdapter(activity,R.layout.spinner_template_view, fenologico));
        sp_cosecha.setAdapter(new SpinnerAdapter(activity,R.layout.spinner_template_view, cosecha));
        sp_crecimiento.setAdapter(new SpinnerAdapter(activity,R.layout.spinner_template_view, crecimiento));
        sp_fito.setAdapter(new SpinnerAdapter(activity,R.layout.spinner_template_view, crecimiento));
        sp_general_cultivo.setAdapter(new SpinnerAdapter(activity,R.layout.spinner_template_view, crecimiento));
        sp_humedad.setAdapter(new SpinnerAdapter(activity,R.layout.spinner_template_view, crecimiento));
        sp_malezas.setAdapter(new SpinnerAdapter(activity,R.layout.spinner_template_view, maleza));

        accionSpinners();


    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (activity != null){
                //activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fotos_resumen, FragmentFotosResumen.getInstance(0), Utilidades.FRAGMENT_FOTOS_RESUMEN).commit();
                temp_visitas  = (temp_visitas != null) ? temp_visitas : MainActivity.myAppDB.myDao().getTempFichas();
                accionSpinners();
                agregarImagenToAgronomos();
                agregarImagenToClientes();

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_guardar:
                break;

            case R.id.btn_volver:
                MainActivity activity = (MainActivity) getActivity();
                if (activity != null){
                    activity.cambiarFragment(new FragmentVisitas(), Utilidades.FRAGMENT_VISITAS, R.anim.slide_in_right, R.anim.slide_out_right);
                }

                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void bind(View view){
        sp_fenologico = (Spinner) view.findViewById(R.id.sp_feno);
        sp_cosecha = (Spinner) view.findViewById(R.id.sp_cosecha);
        sp_crecimiento = (Spinner) view.findViewById(R.id.sp_crecimiento);
        sp_fito = (Spinner) view.findViewById(R.id.sp_fito);
        sp_general_cultivo = (Spinner) view.findViewById(R.id.sp_general_cultivo);
        sp_humedad = (Spinner) view.findViewById(R.id.sp_humedad);
        sp_malezas = (Spinner) view.findViewById(R.id.sp_malezas);
        btn_guardar = (Button) view.findViewById(R.id.btn_guardar);


        et_obs = (EditText) view.findViewById(R.id.et_obs);
        et_recomendacion = (EditText) view.findViewById(R.id.et_recomendacion);



        btn_volver = (Button) view.findViewById(R.id.btn_volver);



        sp_fenologico.setOnItemSelectedListener(this);
        sp_malezas.setOnItemSelectedListener(this);
        sp_humedad.setOnItemSelectedListener(this);
        sp_general_cultivo.setOnItemSelectedListener(this);
        sp_fito.setOnItemSelectedListener(this);
        sp_crecimiento.setOnItemSelectedListener(this);
        sp_cosecha.setOnItemSelectedListener(this);


        et_recomendacion.setOnFocusChangeListener(this);
        et_obs.setOnFocusChangeListener(this);


        btn_volver.setOnClickListener(this);
        btn_guardar.setOnClickListener(this);



        rwAgronomo = (RecyclerView) view.findViewById(R.id.fotos_agronomos);
        rwCliente = (RecyclerView) view.findViewById(R.id.fotos_clientes);

        material_private = (FloatingActionButton) view.findViewById(R.id.material_private);
        material_public = (FloatingActionButton) view.findViewById(R.id.material_public);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i > 0) {

            switch (adapterView.getId()) {
                case R.id.sp_feno:
                    temp_visitas.setPhenological_state_temp_visita(i);
                    break;
                case R.id.sp_cosecha:
                    temp_visitas.setHarvest_temp_visita(i);
                    break;
                case R.id.sp_crecimiento:
                    temp_visitas.setGrowth_status_temp_visita(i);
                    break;
                case R.id.sp_fito:
                    temp_visitas.setPhytosanitary_state_temp_visita(i);
                    break;
                case R.id.sp_general_cultivo:
                    temp_visitas.setOverall_status_temp_visita(i);
                    break;
                case R.id.sp_humedad:
                    temp_visitas.setHumidity_floor_temp_visita(i);
                    break;
                case R.id.sp_malezas:
                    temp_visitas.setWeed_state_temp_visita(i);
                    break;
            }
            MainActivity.myAppDB.myDao().updateTempVisitas(temp_visitas);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onFocusChange(View view, boolean b) {

        if (!b){
            switch (view.getId()){
                case R.id.et_obs:
                    temp_visitas.setObservation_temp_visita(et_obs.getText().toString());
                    break;
                case R.id.et_recomendacion:
                    temp_visitas.setRecomendation_temp_visita(et_recomendacion.getText().toString());
                    break;
            }

            MainActivity.myAppDB.myDao().updateTempVisitas(temp_visitas);
        }
    }





    /* IMAGENES */

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


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

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

}
