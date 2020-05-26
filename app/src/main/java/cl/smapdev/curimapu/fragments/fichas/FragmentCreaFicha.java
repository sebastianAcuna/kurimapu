package cl.smapdev.curimapu.fragments.fichas;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.SpinnerAdapter;
import cl.smapdev.curimapu.clases.tablas.Agricultor;
import cl.smapdev.curimapu.clases.tablas.Comuna;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Fichas;
import cl.smapdev.curimapu.clases.tablas.Provincia;
import cl.smapdev.curimapu.clases.tablas.Region;
import cl.smapdev.curimapu.clases.adapters.SpinnerToolbarAdapter;
import cl.smapdev.curimapu.clases.relaciones.AgricultorCompleto;
import cl.smapdev.curimapu.clases.relaciones.FichasCompletas;
import cl.smapdev.curimapu.clases.tablas.Temporada;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.FragmentFichas;


public class FragmentCreaFicha extends Fragment {

    private String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private MainActivity activity;
    private Spinner sp_year/*, sp_asoc_agr*/,sp_region_agricultor,sp_comuna_agricultor, sp_provincia_agricultor;
    private SearchableSpinner  sp_agric;

    private EditText et_rut_agricultor, et_nombre_agricultor,et_telef_agricultor,et_admin_agricultor,
            et_tel_admin_agricultor,et_oferta_neg_agricultor,et_localidad_agricultor,et_has_disp_agricultor,et_obs_agricultor,
            et_northing_agricultor,et_easting_agricultor;

//    private LinearLayout older_agr,asoc_agro;

    private Button btn_save_agricultor;


    private List<Region> regionList;
    private List<Comuna> comunaList;
    private List<Provincia> provinciaList;
    private List<Temporada> years;



    private String idComuna  = "", idRegion  = "", idAnno  = "", idProvincia = "";


    private FusedLocationProviderClient client;


    private static final String VIENE_A_EDITAR = "viene_a_editar";

    private ArrayList<String> rutAgricultores = new ArrayList<>();
    private ArrayList<String> idRegiones = new ArrayList<>();
    private ArrayList<String> idComunas = new ArrayList<>();
    private ArrayList<String> idProvincias = new ArrayList<>();
    private ArrayList<String> idTemporadas = new ArrayList<>();

//    private AutoCompleteTextView actv;

    private FichasCompletas fichasCompletas;


    public static FragmentCreaFicha getInstance(FichasCompletas fichasCompletas){

        FragmentCreaFicha fragment = new FragmentCreaFicha();
        Bundle bundle = new Bundle();
        bundle.putSerializable(VIENE_A_EDITAR, fichasCompletas);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (fichasCompletas != null){
            outState.putSerializable(VIENE_A_EDITAR, this.fichasCompletas);
        }

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null && savedInstanceState.getSerializable(VIENE_A_EDITAR) != null) {
            fichasCompletas = (FichasCompletas) savedInstanceState.getSerializable(VIENE_A_EDITAR);
        }
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (MainActivity)  getActivity();

        regionList = MainActivity.myAppDB.myDao().getRegiones();
        comunaList = MainActivity.myAppDB.myDao().getComunas();
        provinciaList = MainActivity.myAppDB.myDao().getProvincias();
        years = MainActivity.myAppDB.myDao().getTemporada();

        if (savedInstanceState != null && savedInstanceState.getSerializable(VIENE_A_EDITAR) != null){
            fichasCompletas = (FichasCompletas) savedInstanceState.getSerializable(VIENE_A_EDITAR);
        }




    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_crea_ficha, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (savedInstanceState != null && savedInstanceState.getSerializable(VIENE_A_EDITAR) != null){
            fichasCompletas = (FichasCompletas) savedInstanceState.getSerializable(VIENE_A_EDITAR);
        }

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (!checkPermission()) {
                requestPermission();
            }
        }


        if (activity != null){
            client = LocationServices.getFusedLocationProviderClient(activity);

        }


//        actv = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);


        bind(view);
        setAdapters();







        sp_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idAnno = idTemporadas.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



/*        sp_asoc_agr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (sp_asoc_agr.getSelectedItem().toString().equals(getResources().getString(R.string.asoc_older_farmer))){
                    older_agr.setVisibility(View.VISIBLE);
                }else{
                    older_agr.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        sp_region_agricultor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0){
                    idRegion = idRegiones.get(i);
                    provinciaList = MainActivity.myAppDB.myDao().getProvinciaByRegion(idRegion);
                    cargarProvincia();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_agric.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if(i > 0){
                    llenarAgricultor(rutAgricultores.get(i));
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_provincia_agricultor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idProvincia = idProvincias.get(i);
                comunaList = MainActivity.myAppDB.myDao().getComunaByProvincia(idProvincia);
                cargarComuna();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_comuna_agricultor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0){
                    idComuna = idComunas.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        btn_save_agricultor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fichasCompletas != null){

                    avisoConfirmaCrear("Atencion", "¿Desea modificar esta ficha ? ", "Editar",1);

                }else{
                    avisoConfirmaCrear("Atencion", "¿Desea guardar esta ficha ? ", "Crear",0);
                }



            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();


        Bundle bundle = getArguments();
        if (bundle != null && bundle.getSerializable(VIENE_A_EDITAR) != null){
            fichasCompletas = (FichasCompletas) bundle.getSerializable(VIENE_A_EDITAR);
        }

        if (fichasCompletas != null){
            btn_save_agricultor.setText(getResources().getString(R.string.editar));

//            asoc_agro.setVisibility(View.INVISIBLE);


//            sp_region_agricultor.setEnabled(false);
            sp_region_agricultor.setSelection(idRegiones.indexOf(fichasCompletas.getRegion().getId_region()));




//            sp_comuna_agricultor.setEnabled(false);
            idComuna = fichasCompletas.getComuna().getId_comuna();

            idProvincia = fichasCompletas.getComuna().getId_provincia_comuna();
            sp_provincia_agricultor.setSelection(idProvincias.indexOf(idProvincia));

            cargarComuna();
            sp_comuna_agricultor.setSelection(idComunas.indexOf(idComuna));

            et_rut_agricultor.setEnabled(false);
            et_rut_agricultor.setText(fichasCompletas.getAgricultor().getRut_agricultor());
            et_nombre_agricultor.setEnabled(false);
            et_nombre_agricultor.setText(fichasCompletas.getAgricultor().getNombre_agricultor());
            et_telef_agricultor.setEnabled(false);
            et_telef_agricultor.setText(fichasCompletas.getAgricultor().getTelefono_agricultor());
            et_admin_agricultor.setEnabled(false);
            et_admin_agricultor.setText(fichasCompletas.getAgricultor().getAdministrador_agricultor());
            et_tel_admin_agricultor.setEnabled(false);
            et_tel_admin_agricultor.setText(fichasCompletas.getAgricultor().getTelefono_admin_agricultor());

            try{
                if(fichasCompletas.getFichas().getCoo_utm_ampros_ficha().length() > 0){
                    String coor = fichasCompletas.getFichas().getCoo_utm_ampros_ficha();
                    String[] coordenadas = coor.split(" ");
                    et_northing_agricultor.setText((coordenadas.length > 0) ? coordenadas[0] : "");
                    et_easting_agricultor.setText((coordenadas.length > 0) ? coordenadas[1] : "");
                }
            }catch (Exception e){
                Toast.makeText(activity, "No hay coordenadas", Toast.LENGTH_SHORT).show();
            }





            et_oferta_neg_agricultor.setText(fichasCompletas.getFichas().getOferta_negocio());
            et_localidad_agricultor.setText(fichasCompletas.getFichas().getLocalidad());
            et_has_disp_agricultor.setText(String.valueOf(fichasCompletas.getFichas().getHas_disponible()));
            et_obs_agricultor.setText(fichasCompletas.getFichas().getObservaciones());


            if (fichasCompletas.getFichas().getActiva() == 2){
                sp_region_agricultor.setEnabled(false);
                btn_save_agricultor.setEnabled(false);
                sp_provincia_agricultor.setEnabled(false);
                sp_comuna_agricultor.setEnabled(false);
                et_rut_agricultor.setEnabled(false);
                et_nombre_agricultor.setEnabled(false);
                et_telef_agricultor.setEnabled(false);
                et_admin_agricultor.setEnabled(false);
                et_tel_admin_agricultor.setEnabled(false);
                et_oferta_neg_agricultor.setEnabled(false);
                et_localidad_agricultor.setEnabled(false);
                et_has_disp_agricultor.setEnabled(false);
                et_obs_agricultor.setEnabled(false);
                sp_agric.setEnabled(false);
                sp_year.setEnabled(false);
            }



        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_COARSE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION);
        int result2 = ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.INTERNET);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED ;
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(getActivity(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, 100);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        client.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null && TextUtils.isEmpty(et_easting_agricultor.getText()) && TextUtils.isEmpty(et_northing_agricultor.getText())){
                    et_easting_agricultor.setText(String.valueOf(location.getLatitude()));
                    et_northing_agricultor.setText(String.valueOf(location.getLongitude()));
                }
            }
        });
    }



    private void comprobarCampos(){

        String rutAgro = et_rut_agricultor.getText().toString();
        String nombreAgro = et_nombre_agricultor.getText().toString();
        String fonoAgro  = et_telef_agricultor.getText().toString();
        String admin = et_admin_agricultor.getText().toString();
        String fonoAdmin = et_tel_admin_agricultor.getText().toString();
        String has = et_has_disp_agricultor.getText().toString();


        String oferta = et_oferta_neg_agricultor.getText().toString();
        String localidad = et_localidad_agricultor.getText().toString();
        String observacion = et_obs_agricultor.getText().toString();

        String easting = et_easting_agricultor.getText().toString();
        String norting = et_northing_agricultor.getText().toString();



//&& !TextUtils.isEmpty(admin) && !TextUtils.isEmpty(fonoAdmin)&&  !TextUtils.isEmpty(fonoAgro)
        if (!TextUtils.isEmpty(rutAgro) && !TextUtils.isEmpty(nombreAgro)
        && !TextUtils.isEmpty(oferta) && !TextUtils.isEmpty(localidad) &&  !TextUtils.isEmpty(observacion) && !TextUtils.isEmpty(has)
        && !TextUtils.isEmpty(idAnno) && !TextUtils.isEmpty(idRegion) && !TextUtils.isEmpty(idComuna) ){


                Config config = MainActivity.myAppDB.myDao().getConfig();

                Fichas fichas = new Fichas();
                fichas.setActiva(1);
                fichas.setAnno(idAnno);
                fichas.setHas_disponible(Double.parseDouble(has));
                fichas.setLocalidad(localidad);
                fichas.setObservaciones(observacion);
                fichas.setSubida(false);
                fichas.setOferta_negocio(oferta);
                fichas.setId_region_ficha(idRegion);
                fichas.setId_comuna_ficha(idComuna);
                fichas.setId_usuario(config.getId_usuario());


                if (!TextUtils.isEmpty(easting) && !TextUtils.isEmpty(norting)){
                    fichas.setEasting(Double.parseDouble(easting));
                    fichas.setNorting(Double.parseDouble(norting));
                }

                fichas.setId_agricultor_ficha(MainActivity.myAppDB.myDao().getIdAgricutorByRut(rutAgro));

                if(MainActivity.myAppDB.myDao().insertFicha(fichas) > 0){
                    Snackbar.make(Objects.requireNonNull(getView()), "Se creo la ficha de manera correcta", Snackbar.LENGTH_SHORT).show();

                    if(activity != null){
                        activity.cambiarFragment(new FragmentFichas(), Utilidades.FRAGMENT_FICHAS, R.anim.slide_in_right, R.anim.slide_out_right);
                    }
                }

//            }else{
//                //todo muestra dialogo
//            }

        }else{
            Utilidades.avisoListo(activity, "Falto algo", "Debe completar todos los campos", "OK");
            //todo muestra dialogo
        }






    }


    private void actualizar(){
        String rutAgro = et_rut_agricultor.getText().toString();
        String nombreAgro = et_nombre_agricultor.getText().toString();
        String fonoAgro  = et_telef_agricultor.getText().toString();
        String admin = et_admin_agricultor.getText().toString();
        String fonoAdmin = et_tel_admin_agricultor.getText().toString();
        String has = et_has_disp_agricultor.getText().toString();


        String oferta = et_oferta_neg_agricultor.getText().toString();
        String localidad = et_localidad_agricultor.getText().toString();
        String observacion = et_obs_agricultor.getText().toString();

        String easting = et_easting_agricultor.getText().toString();
        String norting = et_northing_agricultor.getText().toString();

        if (!TextUtils.isEmpty(rutAgro) && !TextUtils.isEmpty(nombreAgro) && !TextUtils.isEmpty(oferta) && !TextUtils.isEmpty(localidad) &&  !TextUtils.isEmpty(observacion)
                && !TextUtils.isEmpty(has) && !TextUtils.isEmpty(idAnno) && !TextUtils.isEmpty(idRegion) && !TextUtils.isEmpty(idComuna)){

//            !TextUtils.isEmpty(easting) && !TextUtils.isEmpty(norting) &&

            Fichas fichas = fichasCompletas.getFichas();

            fichas.setAnno(idAnno);
            fichas.setHas_disponible(Double.parseDouble(has));
            fichas.setLocalidad(localidad);
            fichas.setObservaciones(observacion);
            fichas.setOferta_negocio(oferta);
            fichas.setId_comuna_ficha(idComuna);
            fichas.setId_region_ficha(idRegion);

            if (!TextUtils.isEmpty(easting) && !TextUtils.isEmpty(norting)){
                fichas.setEasting(Double.parseDouble(easting));
                fichas.setNorting(Double.parseDouble(norting));
            }



            if(MainActivity.myAppDB.myDao().updateFicha(fichas) > 0){
                Snackbar.make(Objects.requireNonNull(getView()), "Se modifico la ficha de manera correcta", Snackbar.LENGTH_SHORT).show();

                if(activity != null){
                    activity.cambiarFragment(new FragmentFichas(), Utilidades.FRAGMENT_FICHAS, R.anim.slide_in_right, R.anim.slide_out_right);
                }
            }

        }else{
            Utilidades.avisoListo(activity, "Falto algo", "Debe completar todos los campos", "OK");
            //todo muestra dialogo
        }


    }

    private void avisoConfirmaCrear(String title, String message, String buton, final int accion) {
        View viewInfalted = LayoutInflater.from(getActivity()).inflate(R.layout.alert_empty, null);

        final AlertDialog builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setView(viewInfalted)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(buton, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setCancelable(false)
                .create();

        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                Button c = builder.getButton(AlertDialog.BUTTON_NEGATIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (accion > 0){
                            actualizar();
                        }else{
                            comprobarCampos();
                        }

                        builder.dismiss();
                    }
                });
                c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder.dismiss();
                    }
                });
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    private void llenarAgricultor(String rut){
        if (rut.length() > 0){
            AgricultorCompleto agricultor = MainActivity.myAppDB.myDao().getAgricultorByRut(rut);
            if (agricultor != null){

                sp_agric.setSelection(rutAgricultores.indexOf(agricultor.getAgricultor().getRut_agricultor()));

                et_rut_agricultor.setText(agricultor.getAgricultor().getRut_agricultor());
                et_rut_agricultor.setEnabled(false);
                et_nombre_agricultor.setText(agricultor.getAgricultor().getNombre_agricultor());
                et_nombre_agricultor.setEnabled(false);
                et_telef_agricultor.setText(agricultor.getAgricultor().getTelefono_agricultor());
                et_telef_agricultor.setEnabled(false);
                et_admin_agricultor.setText(agricultor.getAgricultor().getAdministrador_agricultor());
                et_admin_agricultor.setEnabled(false);
                et_tel_admin_agricultor.setText(agricultor.getAgricultor().getTelefono_admin_agricultor());
                et_tel_admin_agricultor.setEnabled(false);


                if (fichasCompletas != null){
                    idRegion = fichasCompletas.getRegion().getId_region();
                    sp_region_agricultor.setSelection(idRegiones.indexOf(idRegion));


                    idProvincia  = fichasCompletas.getComuna().getId_provincia_comuna();
                    sp_provincia_agricultor.setSelection(idProvincias.indexOf(idProvincia));



                    comunaList = MainActivity.myAppDB.myDao().getComunaByProvincia(fichasCompletas.getComuna().getId_provincia_comuna());
                    idComuna = fichasCompletas.getComuna().getId_comuna();

                    cargarComuna();
                    sp_comuna_agricultor.setSelection(idComunas.indexOf(idComuna));
                }




            }
        }
    }



    private void setAdapters(){

        if (years.size() > 0){
            ArrayList<String> str = new ArrayList<>();
            int contador = 0;
            for (Temporada t : years){
                str.add(t.getNombre_tempo());
                idTemporadas.add(contador, t.getId_tempo_tempo());
                contador++;
            }

            sp_year.setAdapter(new SpinnerAdapter(Objects.requireNonNull(getActivity()),R.layout.spinner_template_view, str));
            sp_year.setSelection(years.size() - 1);
        }



/*        sp_asoc_agr.setAdapter(new SpinnerAdapter(Objects.requireNonNull(getActivity()),R.layout.spinner_template_view, getResources().getStringArray(R.array.asociacion_agricultor)));
        sp_asoc_agr.setSelection(1);*/




        List<Agricultor> agricultorList = MainActivity.myAppDB.myDao().getAgricultores();
        if (agricultorList.size() > 0){
            ArrayList<String> str = new ArrayList<>();
            int contador = 0;
//            str.add(getResources().getString(R.string.select));
//            rutAgricultores.add(contador,"");
//            contador++;
            for (Agricultor fs : agricultorList){
                str.add(fs.getNombre_agricultor());
                rutAgricultores.add(contador,fs.getRut_agricultor());
                contador++;
            }
            sp_agric.setTitle(getResources().getString(R.string.seleccione_item));
            sp_agric.setPositiveButton(getResources().getString(R.string.ok));
            sp_agric.setAdapter(new SpinnerAdapter(Objects.requireNonNull(getActivity()),R.layout.spinner_template_view, str));

           /* ArrayAdapter<ArrayList<String>> adapter = new ArrayAdapter<ArrayList<String>>(activity,android.R.layout.select_dialog_item, Collections.singletonList(str));
//            ArrayAdapter<ArrayList<String>> adapter = new ArrayAdapter<ArrayList<String>> (activity, android.R.layout.select_dialog_item, str);
            //Getting the instance of AutoCompleteTextView

            actv.setThreshold(1);//will start working from first character
            actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
            actv.setTextColor(Color.RED);*/

        }


        if (regionList != null && regionList.size() > 0){
            ArrayList<String> rg = new ArrayList<>();
            int contador = 0;
            rg.add(getResources().getString(R.string.select));
            idRegiones.add(contador,"");
            contador++;

            for (Region re : regionList){
                rg.add(re.getDesc_region());
                idRegiones.add(contador, re.getId_region());
                contador++;
            }

            sp_region_agricultor.setAdapter(new SpinnerAdapter(Objects.requireNonNull(getActivity()),R.layout.spinner_template_view, rg));
            sp_region_agricultor.setSelection(0);

        }
        cargarProvincia();
        cargarComuna();

    }


    private void cargarComuna(){
        if (comunaList != null && comunaList.size() > 0){
            ArrayList<String> rg = new ArrayList<>();
            int contador = 0;
            rg.add(getResources().getString(R.string.select));
            idComunas.add(contador,"");
            contador++;
            int selectable = 0;
            for (Comuna re : comunaList){
                rg.add(re.getDesc_comuna());
                idComunas.add(contador, re.getId_comuna());


                if (idComuna.equals(re.getId_comuna())){
                    selectable = contador;
                }

                contador++;


            }

            sp_comuna_agricultor.setAdapter(new SpinnerAdapter(Objects.requireNonNull(getActivity()),R.layout.spinner_template_view, rg));
            sp_comuna_agricultor.setSelection(selectable);
        }
    }

    private void cargarProvincia(){
        if (provinciaList != null && provinciaList.size() > 0){
            ArrayList<String> rg = new ArrayList<>();
            int contador = 0;
            rg.add(getResources().getString(R.string.select));
            idProvincias.add(contador,"");
            contador++;
            int selectable = 0;
            for (Provincia re : provinciaList){
                rg.add(re.getNombre_provincia());
                idProvincias.add(contador, re.getId_provincia());

                if (idProvincia.equals(re.getId_provincia())){
                    selectable = contador;
                }

                contador++;


            }

            sp_provincia_agricultor.setAdapter(new SpinnerAdapter(Objects.requireNonNull(getActivity()),R.layout.spinner_template_view, rg));
            sp_provincia_agricultor.setSelection(selectable);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (activity != null){
            activity.updateView(getResources().getString(R.string.subtitles_records), "New Record");
        }
    }

    private void bind(View view){


        sp_year = (Spinner) view.findViewById(R.id.sp_year);
//        sp_asoc_agr = (Spinner) view.findViewById(R.id.sp_asoc_agr);
        sp_agric = (SearchableSpinner) view.findViewById(R.id.sp_agric);
        sp_region_agricultor = (Spinner) view.findViewById(R.id.sp_region_agricultor);
        sp_comuna_agricultor = (Spinner) view.findViewById(R.id.sp_comuna_agricultor);
        sp_provincia_agricultor = (Spinner) view.findViewById(R.id.sp_provincia_agricultor);


//        older_agr = (LinearLayout) view.findViewById(R.id.older_agr);
//        asoc_agro = (LinearLayout) view.findViewById(R.id.asoc_agro);

        et_rut_agricultor = (EditText) view.findViewById(R.id.et_rut_agricultor);
        et_nombre_agricultor = (EditText) view.findViewById(R.id.et_nombre_agricultor);
        et_telef_agricultor = (EditText) view.findViewById(R.id.et_telef_agricultor);
        et_admin_agricultor = (EditText) view.findViewById(R.id.et_admin_agricultor);
        et_tel_admin_agricultor = (EditText) view.findViewById(R.id.et_tel_admin_agricultor);
        et_oferta_neg_agricultor = (EditText) view.findViewById(R.id.et_oferta_neg_agricultor);
        et_localidad_agricultor = (EditText) view.findViewById(R.id.et_localidad_agricultor);
        et_has_disp_agricultor = (EditText) view.findViewById(R.id.et_has_disp_agricultor);
        et_obs_agricultor = (EditText) view.findViewById(R.id.et_obs_agricultor);
        et_northing_agricultor = (EditText) view.findViewById(R.id.et_northing_agricultor);
        et_easting_agricultor = (EditText) view.findViewById(R.id.et_easting_agricultor);


        btn_save_agricultor = (Button) view.findViewById(R.id.btn_save_agricultor);




    }
}
