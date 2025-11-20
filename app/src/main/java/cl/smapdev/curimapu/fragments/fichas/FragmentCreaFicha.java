package cl.smapdev.curimapu.fragments.fichas;

import static android.content.Context.LOCATION_SERVICE;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cl.smapdev.curimapu.BuildConfig;
import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.FotosFichasAdapter;
import cl.smapdev.curimapu.clases.adapters.SpinnerAdapter;
import cl.smapdev.curimapu.clases.relaciones.AgricultorCompleto;
import cl.smapdev.curimapu.clases.relaciones.FichasCompletas;
import cl.smapdev.curimapu.clases.tablas.Agricultor;
import cl.smapdev.curimapu.clases.tablas.Comuna;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.CropRotation;
import cl.smapdev.curimapu.clases.tablas.Especie;
import cl.smapdev.curimapu.clases.tablas.Fichas;
import cl.smapdev.curimapu.clases.tablas.FotosFichas;
import cl.smapdev.curimapu.clases.tablas.Provincia;
import cl.smapdev.curimapu.clases.tablas.Region;
import cl.smapdev.curimapu.clases.tablas.Temporada;
import cl.smapdev.curimapu.clases.tablas.TipoRiego;
import cl.smapdev.curimapu.clases.tablas.TipoSuelo;
import cl.smapdev.curimapu.clases.utilidades.CameraUtils;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.FragmentFichas;
import es.dmoral.toasty.Toasty;


public class FragmentCreaFicha extends Fragment {


    private MainActivity activity;
    private Spinner sp_year/*, sp_asoc_agr*/, sp_region_agricultor, sp_comuna_agricultor, sp_provincia_agricultor,
            sp_especie, sp_tipo_suelo, sp_tipo_riego, sp_experiencia, sp_tenencia, sp_maquinaria;
    private SearchableSpinner sp_agric;

    private EditText et_rut_agricultor, et_nombre_agricultor, et_telef_agricultor, et_admin_agricultor,
            et_tel_admin_agricultor, et_oferta_neg_agricultor, et_localidad_agricultor, et_has_disp_agricultor,
            et_obs_agricultor, ti_easting, ti_norting, ti_easting_manual, ti_norting_manual,
            et_predio, et_potrero, et_rotacion_1, et_rotacion_2, et_rotacion_3, et_rotacion_4, et_rotacion_5, et_carga_maleza,
            et_estado_general, et_fecha_limite_siembra, et_obs_negocio;

    private RecyclerView lista_fotos_ficha;
    private FloatingActionButton floating_picture_fichas;

//    private LinearLayout older_agr,asoc_agro;

    private Button btn_save_agricultor, btn_captura_gps;

    private File fileImagen;

    private List<Region> regionList;
    private List<Comuna> comunaList;
    private List<Provincia> provinciaList;
    private List<Temporada> years;

    private FotosFichasAdapter adapterFotosFichas;


    protected LocationManager locationManager;


    private String marca_especial_temporada;


    private String idComuna = "", idRegion = "", idAnno = "", idProvincia = "", idEspecie = "", idTipoRiego = "", idTipoSuelo = "", idExperiencia = "", idTenencia = "", idMaquinaria = "";


    private static final String VIENE_A_EDITAR = "viene_a_editar";

    private final ArrayList<String> rutAgricultores = new ArrayList<>();
    private final ArrayList<String> idRegiones = new ArrayList<>();
    private final ArrayList<String> idComunas = new ArrayList<>();
    private final ArrayList<String> idProvincias = new ArrayList<>();
    private final ArrayList<String> idEspecies = new ArrayList<>();
    private ArrayList<String> idTipoRiegos = new ArrayList<>();
    private final ArrayList<String> idTipoSuelos = new ArrayList<>();
    private final ArrayList<String> idExperiencias = new ArrayList<>();
    private final ArrayList<String> idTenencias = new ArrayList<>();
    private final ArrayList<String> idMaquinarias = new ArrayList<>();
    private final ArrayList<String> idTemporadas = new ArrayList<>();

    private final String[] forbiddenWords = new String[]{"á", "é", "í", "ó", "ú", "Á", "É", "Í", "Ó", "Ú"};
    private final String[] forbiddenReplacement = new String[]{"a", "e", "i", "o", "u", "A", "E", "I", "O", "U"};


    private String currentPhotoPath;
    private ActivityResultLauncher<Uri> cameraLauncher;

//    private AutoCompleteTextView actv;

    private FichasCompletas fichasCompletas;

    private ExecutorService executors;
    private final Handler handler = new Handler(Looper.getMainLooper());


    public static FragmentCreaFicha getInstance(FichasCompletas fichasCompletas) {

        FragmentCreaFicha fragment = new FragmentCreaFicha();
        Bundle bundle = new Bundle();
        bundle.putSerializable(VIENE_A_EDITAR, fichasCompletas);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (fichasCompletas != null) {
            outState.putSerializable(VIENE_A_EDITAR, this.fichasCompletas);
        }

        if (sp_provincia_agricultor.getSelectedItemPosition() != AdapterView.INVALID_POSITION) {
            outState.putString("id_provincia", idProvincias.get(sp_provincia_agricultor.getSelectedItemPosition()));
        }

        if (sp_region_agricultor.getSelectedItemPosition() != AdapterView.INVALID_POSITION) {
            outState.putString("id_region", idRegiones.get(sp_region_agricultor.getSelectedItemPosition()));
        }

        if (sp_comuna_agricultor.getSelectedItemPosition() != AdapterView.INVALID_POSITION) {
            outState.putString("id_comuna", idRegiones.get(sp_comuna_agricultor.getSelectedItemPosition()));
        }

        if (fileImagen != null) {
            outState.putParcelable("file", Uri.fromFile(fileImagen));
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getSerializable(VIENE_A_EDITAR) != null) {
            fichasCompletas = (FichasCompletas) savedInstanceState.getSerializable(VIENE_A_EDITAR);
        }

        if (savedInstanceState != null) {

            idRegion = savedInstanceState.getString("id_region");
            idProvincia = savedInstanceState.getString("id_provincia");
            idComuna = savedInstanceState.getString("id_comuna");

            Uri ui = savedInstanceState.getParcelable("file_uri");
            if (ui != null && ui.getPath() != null) {
                fileImagen = new File(ui.getPath());
            }
        }
    }


    @Override
    public void onDestroy() {
        if (executors != null && !executors.isShutdown()) {
            executors.shutdown();
        }
        super.onDestroy();
    }

    private void ejecutarSeguro(Runnable r) {
        if (executors == null || executors.isShutdown() || executors.isTerminated()) {
            executors = Executors.newSingleThreadExecutor();
        }
        executors.execute(r);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        executors = Executors.newSingleThreadExecutor();

        activity = (MainActivity) getActivity();

        if (activity != null) {
            if (Utilidades.checkPermission(requireContext())) {
                locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        200,
                        1, locationListenerGPS);
                isLocationEnabled();
            }
        }


        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(),
                (Boolean success) -> {
                    if (success) {
                        procesarFotoTomada();
                    } else {
                        Toasty.info(activity, "Captura de foto cancelada", Toast.LENGTH_LONG, true).show();
                    }
                });


        if (regionList == null) regionList = MainActivity.myAppDB.myDao().getRegiones();
        if (comunaList == null) comunaList = MainActivity.myAppDB.myDao().getComunas();
        if (provinciaList == null) provinciaList = MainActivity.myAppDB.myDao().getProvincias();
        if (years == null) years = MainActivity.myAppDB.myDao().getTemporada();


        if (savedInstanceState != null && savedInstanceState.getSerializable(VIENE_A_EDITAR) != null) {
            fichasCompletas = (FichasCompletas) savedInstanceState.getSerializable(VIENE_A_EDITAR);
        }

        if (savedInstanceState != null) {
            Uri ui = savedInstanceState.getParcelable("file_uri");
            if (ui != null && ui.getPath() != null) {
                fileImagen = new File(ui.getPath());
            }

            comunaList = MainActivity.myAppDB.myDao().getComunaByProvincia(savedInstanceState.getString("id_provincia"));
            provinciaList = MainActivity.myAppDB.myDao().getProvinciaByRegion(savedInstanceState.getString("id_region"));

        }
    }

    private final LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            ti_norting_manual.setText(String.valueOf(latitude));
            ti_easting_manual.setText(String.valueOf(longitude));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_crea_ficha, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (savedInstanceState != null && savedInstanceState.getSerializable(VIENE_A_EDITAR) != null) {
            fichasCompletas = (FichasCompletas) savedInstanceState.getSerializable(VIENE_A_EDITAR);
        }


        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (!Utilidades.checkPermission(requireContext())) {
                Utilidades.requestPermission(requireActivity());
            }
        }


        bind(view);
        setAdapters();


        floating_picture_fichas.setOnClickListener(v -> abrirCamara());


        sp_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idAnno = idTemporadas.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_region_agricultor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    idRegion = idRegiones.get(i);
                    provinciaList = MainActivity.myAppDB.myDao().getProvinciaByRegion(idRegion);
                    cargarProvincia();
                } else {
                    provinciaList = null;
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

                if (i > 0) {
                    idProvincia = idProvincias.get(i);
                    comunaList = MainActivity.myAppDB.myDao().getComunaByProvincia(idProvincia);
                    cargarComuna();
                } else {
                    comunaList = null;
                    cargarComuna();
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_comuna_agricultor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    idComuna = idComunas.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_especie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    idEspecie = idEspecies.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_tipo_suelo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    idTipoSuelo = idTipoSuelos.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_tipo_riego.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    idTipoRiego = idTipoRiegos.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        sp_experiencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    idExperiencia = idExperiencias.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_tenencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    idTenencia = idTenencias.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_maquinaria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    idMaquinaria = idMaquinarias.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btn_save_agricultor.setOnClickListener(view1 -> {

            if (fichasCompletas != null) {

                avisoConfirmaCrear("Atencion", "¿Desea modificar este prospecto ? ", "Editar", 1);

            } else {
                avisoConfirmaCrear("Atencion", "¿Desea guardar este prospecto ? ", "Crear", 0);
            }
        });

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                Utilidades.setToolbar(activity, view, "Nuevo registro", "");
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.CREATED);

    }

    @Override
    public void onStart() {
        super.onStart();


        locationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.getSerializable(VIENE_A_EDITAR) != null) {
            fichasCompletas = (FichasCompletas) bundle.getSerializable(VIENE_A_EDITAR);
        }

        Config config = MainActivity.myAppDB.myDao().getConfig();


        if (fichasCompletas != null) {


            btn_save_agricultor.setText(getResources().getString(R.string.editar));
            sp_region_agricultor.setSelection(idRegiones.indexOf(fichasCompletas.getRegion().getId_region()));

            sp_year.setSelection(idTemporadas.indexOf(fichasCompletas.getFichas().getAnno()));

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


            et_predio.setText(fichasCompletas.getFichas().getPredio_ficha());
            et_potrero.setText(fichasCompletas.getFichas().getPotrero_ficha());
            et_obs_negocio.setText(fichasCompletas.getFichas().getObservacion_negocio_ficha());
            et_carga_maleza.setText(fichasCompletas.getFichas().getMaleza());
            et_estado_general.setText(fichasCompletas.getFichas().getEstado_general_ficha());


            if (!TextUtils.isEmpty(fichasCompletas.getFichas().getFecha_limite_siembra_ficha())) {
                et_fecha_limite_siembra.setText(Utilidades.voltearFechaVista(fichasCompletas.getFichas().getFecha_limite_siembra_ficha()));
            }


            int positionRegion = idRegiones.indexOf(fichasCompletas.getRegion().getId_region());
            sp_region_agricultor.setSelection(positionRegion);

            int positionComuna = idComunas.indexOf(fichasCompletas.getComuna().getId_comuna());
            sp_comuna_agricultor.setSelection(positionComuna);

            int positionProvincia = idProvincias.indexOf(fichasCompletas.getComuna().getId_provincia_comuna());
            sp_provincia_agricultor.setSelection(positionProvincia);

//            cargarEspecie();
            int positionEspecie = idEspecies.indexOf(fichasCompletas.getFichas().getEspecie_ficha());
            sp_especie.setSelection(positionEspecie);

//            cargarSpinnerIndependientes();

            int positionTipoSuelo = idTipoSuelos.indexOf(fichasCompletas.getFichas().getId_tipo_suelo());
            sp_tipo_suelo.setSelection(positionTipoSuelo);

            TipoRiego tipoRiego = MainActivity.myAppDB.myDao().getTipoRiegoById(fichasCompletas.getFichas().getId_tipo_riego());
            if (tipoRiego.getVigencia().equals("NO")) {
                List<TipoRiego> tipoRiegoList = MainActivity.myAppDB.myDao().getTipoRiego();
                cargarSpinnerRiego(tipoRiegoList);
            }


            int positionTipoRiego = idTipoRiegos.indexOf(fichasCompletas.getFichas().getId_tipo_riego());
            sp_tipo_riego.setSelection(positionTipoRiego);

            int positionExperiencia = idExperiencias.indexOf(fichasCompletas.getFichas().getExperiencia());
            sp_experiencia.setSelection(positionExperiencia);

            int positionTenencia = idTenencias.indexOf(fichasCompletas.getFichas().getId_tipo_tenencia_terreno());
            sp_tenencia.setSelection(positionTenencia);

            int positionMaquinaria = idMaquinarias.indexOf(fichasCompletas.getFichas().getId_tipo_tenencia_maquinaria());
            sp_maquinaria.setSelection(positionMaquinaria);


            sp_agric.setSelection(rutAgricultores.indexOf(fichasCompletas.getAgricultor().getRut_agricultor()));


            List<CropRotation> cropRotations;

            if (fichasCompletas.getFichas().isSubida()) {
                cropRotations = MainActivity.myAppDB.myDao().getCropRotationRemotos(fichasCompletas.getFichas().getId_ficha());
            } else {
                cropRotations = MainActivity.myAppDB.myDao().getCropRotationLocal(fichasCompletas.getFichas().getId_ficha_local_ficha());
            }


            if (cropRotations.size() > 0) {

                et_rotacion_1.setText(cropRotations.get(0).getCultivo_crop_rotation());
                et_rotacion_1.setTag("1");

                et_rotacion_2.setText(cropRotations.size() > 1 ? cropRotations.get(1).getCultivo_crop_rotation() : "");
                et_rotacion_2.setTag("2");

                et_rotacion_3.setText(cropRotations.size() > 2 ? cropRotations.get(2).getCultivo_crop_rotation() : "");
                et_rotacion_3.setTag("3");

                et_rotacion_4.setText(cropRotations.size() > 3 ? cropRotations.get(3).getCultivo_crop_rotation() : "");
                et_rotacion_4.setTag("4");

                et_rotacion_5.setText(cropRotations.size() > 4 ? cropRotations.get(4).getCultivo_crop_rotation() : "");
                et_rotacion_5.setTag("5");


            }

            double norting = 0.0;
            double easting = 0.0;

            norting = fichasCompletas.getFichas().getNorting();
            easting = fichasCompletas.getFichas().getEasting();


            ti_norting.setText(String.valueOf(norting));
            ti_easting.setText(String.valueOf(easting));


            et_oferta_neg_agricultor.setText(fichasCompletas.getFichas().getOferta_negocio());
            et_localidad_agricultor.setText(fichasCompletas.getFichas().getLocalidad());
            et_has_disp_agricultor.setText(String.valueOf(fichasCompletas.getFichas().getHas_disponible()));
            et_obs_agricultor.setText(fichasCompletas.getFichas().getObservaciones());


            if (fichasCompletas.getFichas().getActiva() == 2 || fichasCompletas.getFichas().getActiva() == 3) {
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

                ti_easting.setEnabled(false);
                ti_norting.setEnabled(false);


                et_predio.setEnabled(false);
                et_potrero.setEnabled(false);
                et_obs_negocio.setEnabled(false);
                et_carga_maleza.setEnabled(false);
                et_estado_general.setEnabled(false);
                et_fecha_limite_siembra.setEnabled(false);
                sp_especie.setEnabled(false);
                sp_tipo_suelo.setEnabled(false);
                sp_tipo_riego.setEnabled(false);
                sp_experiencia.setEnabled(false);
                sp_tenencia.setEnabled(false);
                sp_maquinaria.setEnabled(false);


                et_rotacion_1.setEnabled(false);
                et_rotacion_2.setEnabled(false);
                et_rotacion_3.setEnabled(false);
                et_rotacion_4.setEnabled(false);
                et_rotacion_5.setEnabled(false);


                btn_captura_gps.setEnabled(false);
                floating_picture_fichas.setEnabled(false);
            }

            if (fichasCompletas.getFichas().isSubida() && (fichasCompletas.getFichas().getActiva() == 2 || fichasCompletas.getFichas().getActiva() == 3)) {
                sp_region_agricultor.setEnabled(false);
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

                ti_easting.setEnabled(false);
                ti_norting.setEnabled(false);


                et_predio.setEnabled(false);
                et_potrero.setEnabled(false);
                et_obs_negocio.setEnabled(false);
                et_carga_maleza.setEnabled(false);
                et_estado_general.setEnabled(false);
                et_fecha_limite_siembra.setEnabled(false);
                sp_especie.setEnabled(false);
                sp_tipo_suelo.setEnabled(false);
                sp_tipo_riego.setEnabled(false);
                sp_experiencia.setEnabled(false);
                sp_tenencia.setEnabled(false);
                sp_maquinaria.setEnabled(false);

                et_rotacion_1.setEnabled(false);
                et_rotacion_2.setEnabled(false);
                et_rotacion_3.setEnabled(false);
                et_rotacion_4.setEnabled(false);
                et_rotacion_5.setEnabled(false);

                btn_save_agricultor.setEnabled(true);
                floating_picture_fichas.setEnabled(true);
                btn_captura_gps.setEnabled(false);
            }
        } else {
            double norting = (!TextUtils.isEmpty(ti_norting.getText())) ? Double.parseDouble(ti_norting.getText().toString()) : 0.0;
            double easting = (!TextUtils.isEmpty(ti_easting.getText())) ? Double.parseDouble(ti_easting.getText().toString()) : 0.0;

            ti_norting.setText(String.valueOf(norting));
            ti_easting.setText(String.valueOf(easting));
        }

        agregarImagenToFichas();


    }


    @Override
    public void onResume() {
        super.onResume();

        isLocationEnabled();
    }

    private void isLocationEnabled() {

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
            alertDialog.setTitle("Habilita la ubicacion");
            alertDialog.setMessage("La ubicacion en su telefono esta deshabilitada, favor hablitar.");
            alertDialog.setPositiveButton("Configuracion de ubicacion", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = alertDialog.create();
            alert.show();
        }
//        else{
//            AlertDialog.Builder alertDialog=new AlertDialog.Builder(activity);
//            alertDialog.setTitle("Confirm Location");
//            alertDialog.setMessage("Your Location is enabled, please enjoy");
//            alertDialog.setNegativeButton("Back to interface",new DialogInterface.OnClickListener(){
//                public void onClick(DialogInterface dialog, int which){
//                    dialog.cancel();
//                }
//            });
//            AlertDialog alert=alertDialog.create();
//            alert.show();
//        }
    }

    /*GUARDAR*/
    private void comprobarCampos() {

        String rutAgro = et_rut_agricultor.getText().toString();
        String nombreAgro = et_nombre_agricultor.getText().toString();
        String fonoAgro = et_telef_agricultor.getText().toString();
        String admin = et_admin_agricultor.getText().toString();
        String fonoAdmin = et_tel_admin_agricultor.getText().toString();


        String has = et_has_disp_agricultor.getText().toString();
        String oferta = et_oferta_neg_agricultor.getText().toString();
        String localidad = et_localidad_agricultor.getText().toString();
        String observacion = et_obs_agricultor.getText().toString();

        String rotacion1 = et_rotacion_1.getText().toString().toUpperCase();
        String rotacion2 = et_rotacion_2.getText().toString().toUpperCase();
        String rotacion3 = et_rotacion_3.getText().toString().toUpperCase();
        String rotacion4 = et_rotacion_4.getText().toString().toUpperCase();
        String rotacion5 = et_rotacion_5.getText().toString().toUpperCase();

        String predio = et_predio.getText().toString().toUpperCase();
        String potrero = et_potrero.getText().toString().toUpperCase();
        String cargaMaleza = et_carga_maleza.getText().toString().toUpperCase();
        String estadoGeneral = et_estado_general.getText().toString().toUpperCase();
        String observacionNegocio = et_obs_negocio.getText().toString().toUpperCase();

        String fechaLimite = et_fecha_limite_siembra.getText().toString();


        String norting = ti_norting.getText().toString();
        String easting = ti_easting.getText().toString();

        String coordNorting;
        String coordEasting;
        try {
            coordNorting = Utilidades.formatearCoordenada(norting);
            coordEasting = Utilidades.formatearCoordenada(easting);
        } catch (Exception e) {
            Toasty.error(requireActivity(), (e.getMessage() != null) ? e.getMessage() : "No se pudo formatear la coordenada, debe contener 10 caracteres", Toast.LENGTH_LONG, true).show();
            return;
        }

        if (TextUtils.isEmpty(rutAgro) || TextUtils.isEmpty(nombreAgro)
                || TextUtils.isEmpty(oferta) || TextUtils.isEmpty(localidad) || TextUtils.isEmpty(observacion) || TextUtils.isEmpty(has)
                || TextUtils.isEmpty(idAnno) || TextUtils.isEmpty(idRegion) || TextUtils.isEmpty(idComuna) || TextUtils.isEmpty(norting) || TextUtils.isEmpty(easting)
                || sp_comuna_agricultor.getSelectedItemPosition() <= 0
                || sp_provincia_agricultor.getSelectedItemPosition() <= 0
                || sp_region_agricultor.getSelectedItemPosition() <= 0) {
            Utilidades.avisoListo(activity, "Falto algo", "Debe completar todos los campos con la marca (*) ", "OK");
            return;
        }


        Config config = MainActivity.myAppDB.myDao().getConfig();


        for (int i = 0; i < forbiddenWords.length; i++) {
            localidad = localidad.replace(forbiddenWords[i], forbiddenReplacement[i]);
            observacion = observacion.replace(forbiddenWords[i], forbiddenReplacement[i]);
            oferta = oferta.replace(forbiddenWords[i], forbiddenReplacement[i]);

            rotacion1 = rotacion1.replace(forbiddenWords[i], forbiddenReplacement[i]);
            rotacion2 = rotacion2.replace(forbiddenWords[i], forbiddenReplacement[i]);
            rotacion3 = rotacion3.replace(forbiddenWords[i], forbiddenReplacement[i]);
            rotacion4 = rotacion4.replace(forbiddenWords[i], forbiddenReplacement[i]);
            rotacion5 = rotacion5.replace(forbiddenWords[i], forbiddenReplacement[i]);

            predio = predio.replace(forbiddenWords[i], forbiddenReplacement[i]);
            potrero = potrero.replace(forbiddenWords[i], forbiddenReplacement[i]);
            cargaMaleza = cargaMaleza.replace(forbiddenWords[i], forbiddenReplacement[i]);
            estadoGeneral = estadoGeneral.replace(forbiddenWords[i], forbiddenReplacement[i]);
            observacionNegocio = observacionNegocio.replace(forbiddenWords[i], forbiddenReplacement[i]);

        }


        Fichas fichas = new Fichas();
        fichas.setActiva(1);
        fichas.setAnno(idTemporadas.get(sp_year.getSelectedItemPosition()));
        fichas.setHas_disponible(Double.parseDouble(has));
        fichas.setLocalidad(localidad.toUpperCase());
        fichas.setObservaciones(observacion.toUpperCase());
        fichas.setSubida(false);
        fichas.setOferta_negocio(oferta.toUpperCase());
        fichas.setId_region_ficha(idRegion);
        fichas.setId_comuna_ficha(idComuna);
        fichas.setId_usuario(config.getId_usuario());

        fichas.setId_tipo_riego(idTipoRiegos.get(sp_tipo_riego.getSelectedItemPosition()));
        fichas.setId_tipo_suelo(idTipoSuelos.get(sp_tipo_suelo.getSelectedItemPosition()));
        fichas.setId_tipo_tenencia_maquinaria(sp_maquinaria.getSelectedItem().toString());
        fichas.setId_tipo_tenencia_terreno(sp_tenencia.getSelectedItem().toString());
        fichas.setMaleza(cargaMaleza);
        fichas.setExperiencia(sp_experiencia.getSelectedItem().toString());

        fichas.setEstado_general_ficha(estadoGeneral);
        fichas.setObservacion_negocio_ficha(observacionNegocio);
        fichas.setPredio_ficha(predio);
        fichas.setPotrero_ficha(potrero);

        fichas.setEspecie_ficha(idEspecies.get(sp_especie.getSelectedItemPosition()));


        try {
            fichas.setFecha_limite_siembra_ficha(Utilidades.voltearFechaBD(fechaLimite));
        } catch (Exception e) {
            Toasty.error(activity, "No se pudo guardar fecha limite de siembra", Toast.LENGTH_SHORT, true).show();
        }


        fichas.setId_provincia_ficha(idProvincias.get(sp_provincia_agricultor.getSelectedItemPosition()));

        try {


            fichas.setNorting(Double.parseDouble(coordNorting));
            fichas.setEasting(Double.parseDouble(coordEasting));
        } catch (ParseException e) {
            fichas.setNorting(0);
            fichas.setEasting(0);
            Toasty.info(activity, "No se pudo guardar norting y easting , se setearan en 0", Toast.LENGTH_SHORT, true).show();
        }


        fichas.setId_agricultor_ficha(MainActivity.myAppDB.myDao().getIdAgricutorByRut(rutAgro));

        /* INSERT DE FICHA */
        long idFichaNueva = MainActivity.myAppDB.myDao().insertFicha(fichas);
        if (idFichaNueva > 0) {

            Fichas fichas2 = new Fichas();
            fichas2.setActiva(1);
            fichas2.setAnno(fichas.getAnno());
            fichas2.setHas_disponible(Double.parseDouble(has));
            fichas2.setLocalidad(localidad.toUpperCase());
            fichas2.setObservaciones(observacion.toUpperCase());
            fichas2.setSubida(false);
            fichas2.setOferta_negocio(oferta.toUpperCase());
            fichas2.setId_region_ficha(idRegion);
            fichas2.setId_comuna_ficha(idComuna);
            fichas2.setId_usuario(config.getId_usuario());
            fichas2.setId_ficha((int) idFichaNueva);
            fichas2.setId_ficha_local_ficha((int) idFichaNueva);
            fichas2.setId_provincia_ficha(fichas.getId_provincia_ficha());


            fichas2.setId_tipo_riego(fichas.getId_tipo_riego());
            fichas2.setId_tipo_suelo(fichas.getId_tipo_suelo());
            fichas2.setId_tipo_tenencia_maquinaria(fichas.getId_tipo_tenencia_maquinaria());
            fichas2.setId_tipo_tenencia_terreno(fichas.getId_tipo_tenencia_terreno());
            fichas2.setMaleza(fichas.getMaleza());
            fichas2.setExperiencia(fichas.getExperiencia());

            fichas2.setEstado_general_ficha(fichas.getEstado_general_ficha());
            fichas2.setObservacion_negocio_ficha(fichas.getObservacion_negocio_ficha());
            fichas2.setPredio_ficha(fichas.getPredio_ficha());
            fichas2.setPotrero_ficha(fichas.getPotrero_ficha());

            fichas2.setFecha_limite_siembra_ficha(fichas.getFecha_limite_siembra_ficha());
            fichas2.setEspecie_ficha(fichas.getEspecie_ficha());


            ArrayList<CropRotation> cropRotations = new ArrayList<>();
            CropRotation crp5 = new CropRotation();

            crp5.setCultivo_crop_rotation(rotacion5);
            crp5.setTemporada_crop_rotation("5");
            crp5.setId_ficha_crop_rotation((int) idFichaNueva);
            crp5.setId_ficha_local_cp((int) idFichaNueva);
            crp5.setTipo_crop("P");
            cropRotations.add(crp5);

            CropRotation crp1 = new CropRotation();
            crp1.setCultivo_crop_rotation(rotacion4);
            crp1.setTemporada_crop_rotation("4");
            crp1.setId_ficha_crop_rotation((int) idFichaNueva);
            crp1.setId_ficha_local_cp((int) idFichaNueva);
            crp1.setTipo_crop("P");
            cropRotations.add(crp1);

            CropRotation crp2 = new CropRotation();
            crp2.setCultivo_crop_rotation(rotacion3);
            crp2.setTemporada_crop_rotation("3");
            crp2.setId_ficha_crop_rotation((int) idFichaNueva);
            crp2.setId_ficha_local_cp((int) idFichaNueva);
            crp2.setTipo_crop("P");
            cropRotations.add(crp2);

            CropRotation crp3 = new CropRotation();
            crp3.setCultivo_crop_rotation(rotacion2);
            crp3.setTemporada_crop_rotation("2");
            crp3.setId_ficha_crop_rotation((int) idFichaNueva);
            crp3.setId_ficha_local_cp((int) idFichaNueva);
            crp3.setTipo_crop("P");
            cropRotations.add(crp3);

            CropRotation crp4 = new CropRotation();
            crp4.setCultivo_crop_rotation(rotacion1);
            crp4.setTemporada_crop_rotation("1");
            crp4.setId_ficha_crop_rotation((int) idFichaNueva);
            crp4.setId_ficha_local_cp((int) idFichaNueva);
            crp4.setTipo_crop("P");
            cropRotations.add(crp4);

            MainActivity.myAppDB.myDao().insertCrop(cropRotations);


            fichas2.setNorting(fichas.getNorting());
            fichas2.setEasting(fichas.getEasting());


            fichas2.setId_agricultor_ficha(MainActivity.myAppDB.myDao().getIdAgricutorByRut(rutAgro));

            MainActivity.myAppDB.myDao().updateFicha(fichas2);
            MainActivity.myAppDB.myDao().updateFotosFichas((int) idFichaNueva);


            if (activity != null) {
                Snackbar.make(requireView(), "Se creo el prospecto de manera correcta", Snackbar.LENGTH_SHORT).show();
                activity.cambiarFragment(new FragmentFichas(), Utilidades.FRAGMENT_FICHAS, R.anim.slide_in_right, R.anim.slide_out_right);
            }
        }


    }


    private void actualizar() {
        String rutAgro = et_rut_agricultor.getText().toString();
        String nombreAgro = et_nombre_agricultor.getText().toString();
        String fonoAgro = et_telef_agricultor.getText().toString();
        String admin = et_admin_agricultor.getText().toString();
        String fonoAdmin = et_tel_admin_agricultor.getText().toString();
        String has = et_has_disp_agricultor.getText().toString();


        String oferta = et_oferta_neg_agricultor.getText().toString();
        String localidad = et_localidad_agricultor.getText().toString();
        String observacion = et_obs_agricultor.getText().toString();


        String rotacion1 = et_rotacion_1.getText().toString().toUpperCase();
        String rotacion2 = et_rotacion_2.getText().toString().toUpperCase();
        String rotacion3 = et_rotacion_3.getText().toString().toUpperCase();
        String rotacion4 = et_rotacion_4.getText().toString().toUpperCase();
        String rotacion5 = et_rotacion_5.getText().toString().toUpperCase();

        String predio = et_predio.getText().toString().toUpperCase();
        String potrero = et_potrero.getText().toString().toUpperCase();
        String cargaMaleza = et_carga_maleza.getText().toString().toUpperCase();
        String estadoGeneral = et_estado_general.getText().toString().toUpperCase();
        String observacionNegocio = et_obs_negocio.getText().toString().toUpperCase();

        String fechaLimite = et_fecha_limite_siembra.getText().toString();

        String norting = ti_norting.getText().toString();
        String easting = ti_easting.getText().toString();

        String coordNorting;
        String coordEasting;
        try {
            coordNorting = Utilidades.formatearCoordenada(norting);
            coordEasting = Utilidades.formatearCoordenada(easting);


        } catch (Exception e) {
            Toasty.error(requireActivity(), (e.getMessage() != null) ? e.getMessage() : "No se pudo formatear la coordenada, debe contener 10 caracteres", Toast.LENGTH_LONG, true).show();
            return;
        }


        if (!TextUtils.isEmpty(rutAgro) && !TextUtils.isEmpty(nombreAgro) && !TextUtils.isEmpty(oferta) && !TextUtils.isEmpty(localidad) && !TextUtils.isEmpty(observacion)
                && !TextUtils.isEmpty(has) && !TextUtils.isEmpty(idAnno) && !TextUtils.isEmpty(idRegion) && !TextUtils.isEmpty(idComuna)
                && !TextUtils.isEmpty(norting) && !TextUtils.isEmpty(easting) && sp_comuna_agricultor.getSelectedItemPosition() > 0
                && sp_provincia_agricultor.getSelectedItemPosition() > 0
                && sp_region_agricultor.getSelectedItemPosition() > 0) {

//            !TextUtils.isEmpty(easting) && !TextUtils.isEmpty(norting) &&

            Fichas fichas = fichasCompletas.getFichas();

            for (int i = 0; i < forbiddenWords.length; i++) {
                localidad = localidad.replace(forbiddenWords[i], forbiddenReplacement[i]);
                observacion = observacion.replace(forbiddenWords[i], forbiddenReplacement[i]);
                oferta = oferta.replace(forbiddenWords[i], forbiddenReplacement[i]);

                rotacion1 = rotacion1.replace(forbiddenWords[i], forbiddenReplacement[i]);
                rotacion2 = rotacion2.replace(forbiddenWords[i], forbiddenReplacement[i]);
                rotacion3 = rotacion3.replace(forbiddenWords[i], forbiddenReplacement[i]);
                rotacion4 = rotacion4.replace(forbiddenWords[i], forbiddenReplacement[i]);
                rotacion5 = rotacion5.replace(forbiddenWords[i], forbiddenReplacement[i]);

                predio = predio.replace(forbiddenWords[i], forbiddenReplacement[i]);
                potrero = potrero.replace(forbiddenWords[i], forbiddenReplacement[i]);
                cargaMaleza = cargaMaleza.replace(forbiddenWords[i], forbiddenReplacement[i]);
                estadoGeneral = estadoGeneral.replace(forbiddenWords[i], forbiddenReplacement[i]);
                observacionNegocio = observacionNegocio.replace(forbiddenWords[i], forbiddenReplacement[i]);
            }
//            oferta = oferta.replace()

            fichas.setAnno(idAnno);
            fichas.setHas_disponible(Double.parseDouble(has));
            fichas.setLocalidad(localidad.toUpperCase());
            fichas.setObservaciones(observacion.toUpperCase());
            fichas.setOferta_negocio(oferta.toUpperCase());

            fichas.setId_tipo_riego(idTipoRiegos.get(sp_tipo_riego.getSelectedItemPosition()));
            fichas.setId_tipo_suelo(idTipoSuelos.get(sp_tipo_suelo.getSelectedItemPosition()));
            fichas.setId_tipo_tenencia_maquinaria(sp_maquinaria.getSelectedItem().toString());
            fichas.setId_tipo_tenencia_terreno(sp_tenencia.getSelectedItem().toString());
            fichas.setMaleza(cargaMaleza);
            fichas.setExperiencia(sp_experiencia.getSelectedItem().toString());

            fichas.setEstado_general_ficha(estadoGeneral);
            fichas.setObservacion_negocio_ficha(observacionNegocio);
            fichas.setPredio_ficha(predio);
            fichas.setPotrero_ficha(potrero);

            fichas.setEspecie_ficha(idEspecies.get(sp_especie.getSelectedItemPosition()));

            List<CropRotation> cropRotations;
            if (fichasCompletas.getFichas().isSubida()) {
                cropRotations = MainActivity.myAppDB.myDao().getCropRotationRemotos(fichasCompletas.getFichas().getId_ficha());
            } else {
                cropRotations = MainActivity.myAppDB.myDao().getCropRotationLocal(fichasCompletas.getFichas().getId_ficha_local_ficha());
            }

//            List<CropRotation> cropRotations = MainActivity.myAppDB.myDao().getCropRotationLocal(fichas.getId_ficha_local_ficha());
            if (cropRotations.size() > 0) {


                for (CropRotation crp : cropRotations) {
                    CropRotation crp1 = new CropRotation();

                    crp1.setId_ficha_local_cp(crp.getId_ficha_local_cp());
                    crp1.setId_ficha_crop_rotation(crp.getId_ficha_crop_rotation());
                    crp1.setTemporada_crop_rotation(crp.getTemporada_crop_rotation());
                    crp1.setEstado_subida_crop_rotation(0);
                    crp1.setId_anexo_crop_rotation(crp.getId_anexo_crop_rotation());
                    crp1.setId_crop_rotation(crp.getId_crop_rotation());
                    crp1.setTipo_crop("P");

                    if (et_rotacion_1.getTag().toString().equals(crp.getTemporada_crop_rotation())) {
                        crp1.setCultivo_crop_rotation(et_rotacion_1.getText().toString().toUpperCase());
                    }

                    if (et_rotacion_2.getTag().toString().equals(crp.getTemporada_crop_rotation())) {
                        crp1.setCultivo_crop_rotation(et_rotacion_2.getText().toString().toUpperCase());
                    }
                    if (et_rotacion_3.getTag().toString().equals(crp.getTemporada_crop_rotation())) {
                        crp1.setCultivo_crop_rotation(et_rotacion_3.getText().toString().toUpperCase());
                    }
                    if (et_rotacion_4.getTag().toString().equals(crp.getTemporada_crop_rotation())) {
                        crp1.setCultivo_crop_rotation(et_rotacion_4.getText().toString().toUpperCase());
                    }
                    if (et_rotacion_5.getTag().toString().equals(crp.getTemporada_crop_rotation())) {
                        crp1.setCultivo_crop_rotation(et_rotacion_5.getText().toString().toUpperCase());
                    }


                    MainActivity.myAppDB.myDao().updateCrop(crp1);
                }

            } else {


                ArrayList<CropRotation> cropRotationss = new ArrayList<>();
                CropRotation crp5 = new CropRotation();

                CropRotation crp4 = new CropRotation();
                crp4.setCultivo_crop_rotation(rotacion1);
                crp4.setTemporada_crop_rotation("1");
                crp4.setId_ficha_crop_rotation((int) fichas.getId_ficha());
                crp4.setId_ficha_local_cp((int) fichas.getId_ficha());
                crp4.setTipo_crop("P");

                cropRotationss.add(crp4);

                CropRotation crp3 = new CropRotation();
                crp3.setCultivo_crop_rotation(rotacion2);
                crp3.setTemporada_crop_rotation("2");
                crp3.setId_ficha_crop_rotation((int) fichas.getId_ficha());
                crp3.setId_ficha_local_cp((int) fichas.getId_ficha());
                crp3.setTipo_crop("P");
                cropRotationss.add(crp3);

                CropRotation crp2 = new CropRotation();
                crp2.setCultivo_crop_rotation(rotacion3);
                crp2.setTemporada_crop_rotation("3");
                crp2.setId_ficha_crop_rotation((int) fichas.getId_ficha());
                crp2.setId_ficha_local_cp((int) fichas.getId_ficha());
                crp2.setTipo_crop("P");
                cropRotationss.add(crp2);

                CropRotation crp1 = new CropRotation();
                crp1.setCultivo_crop_rotation(rotacion4);
                crp1.setTemporada_crop_rotation("4");
                crp1.setId_ficha_crop_rotation((int) fichas.getId_ficha());
                crp1.setId_ficha_local_cp((int) fichas.getId_ficha());
                crp1.setTipo_crop("P");
                cropRotationss.add(crp1);

                crp5.setCultivo_crop_rotation(rotacion5);
                crp5.setTemporada_crop_rotation("5");
                crp5.setId_ficha_crop_rotation((int) fichas.getId_ficha());
                crp5.setId_ficha_local_cp((int) fichas.getId_ficha());
                crp5.setTipo_crop("P");
                cropRotationss.add(crp5);


                MainActivity.myAppDB.myDao().insertCrop(cropRotationss);
            }


            try {
                fichas.setFecha_limite_siembra_ficha(Utilidades.voltearFechaBD(fechaLimite));
            } catch (Exception e) {
                Toasty.error(activity, "No se pudo guardar fecha limite de siembra", Toast.LENGTH_SHORT, true).show();
            }

//            fichas.setId_provincia_ficha(idProvincias.get(sp_provincia_agricultor.getSelectedItemPosition()));

            fichas.setId_comuna_ficha(idComunas.get(sp_comuna_agricultor.getSelectedItemPosition()));
            fichas.setId_region_ficha(idRegiones.get(sp_region_agricultor.getSelectedItemPosition()));
            fichas.setId_provincia_ficha(idProvincias.get(sp_provincia_agricultor.getSelectedItemPosition()));
            fichas.setSubida(false);


            try {
                fichas.setNorting(Double.parseDouble(coordNorting));
                fichas.setEasting(Double.parseDouble(coordEasting));
            } catch (ParseException e) {
                fichas.setNorting(0);
                fichas.setEasting(0);
                Toasty.info(activity, "No se pudo guardar norting y easting , se setearan en 0", Toast.LENGTH_SHORT, true).show();
            }


            if (MainActivity.myAppDB.myDao().updateFicha(fichas) > 0) {

                Snackbar.make(requireView(), "Se modifico la ficha de manera correcta", Snackbar.LENGTH_SHORT).show();
                if (activity != null) {
                    activity.cambiarFragment(new FragmentFichas(), Utilidades.FRAGMENT_FICHAS, R.anim.slide_in_right, R.anim.slide_out_right);
                }
            }
        } else {
            Utilidades.avisoListo(activity, "Falto algo", "Debe completar todos los campos", "OK");
            //todo muestra dialogo
        }


    }

    private void avisoConfirmaCrear(String title, String message, String buton, final int accion) {
        View viewInfalted = LayoutInflater.from(getActivity()).inflate(R.layout.alert_empty, null);

        final AlertDialog builder = new AlertDialog.Builder(requireActivity())
                .setView(viewInfalted)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(buton, new DialogInterface.OnClickListener() {
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
                        if (accion > 0) {
                            actualizar();
                        } else {
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

    private void llenarAgricultor(String rut) {
        if (rut.length() > 0) {
            AgricultorCompleto agricultor = MainActivity.myAppDB.myDao().getAgricultorByRut(rut);
            if (agricultor != null) {

                sp_agric.setSelection(rutAgricultores.indexOf(agricultor.getAgricultor().getRut_agricultor()));

                et_rut_agricultor.setText((agricultor.getAgricultor().getRut_agricultor() != null) ? agricultor.getAgricultor().getRut_agricultor() : "");
                et_rut_agricultor.setEnabled(false);
                et_nombre_agricultor.setText((agricultor.getAgricultor().getNombre_agricultor() != null) ? agricultor.getAgricultor().getNombre_agricultor() : "");
                et_nombre_agricultor.setEnabled(false);
                et_telef_agricultor.setText((agricultor.getAgricultor().getTelefono_agricultor() != null) ? agricultor.getAgricultor().getTelefono_agricultor() : "");
                et_telef_agricultor.setEnabled(false);
                et_admin_agricultor.setText((agricultor.getAgricultor().getAdministrador_agricultor() != null) ? agricultor.getAgricultor().getAdministrador_agricultor() : "");
                et_admin_agricultor.setEnabled(false);
                et_tel_admin_agricultor.setText((agricultor.getAgricultor().getTelefono_admin_agricultor() != null) ? agricultor.getAgricultor().getTelefono_admin_agricultor() : "");
                et_tel_admin_agricultor.setEnabled(false);


//                if (fichasCompletas != null){
//                    idRegion = fichasCompletas.getRegion().getId_region();
//                    sp_region_agricultor.setSelection(idRegiones.indexOf(idRegion));
//
//                    idProvincia  = fichasCompletas.getComuna().getId_provincia_comuna();
//                    sp_provincia_agricultor.setSelection(idProvincias.indexOf(idProvincia));
//                    comunaList = MainActivity.myAppDB.myDao().getComunaByProvincia(fichasCompletas.getComuna().getId_provincia_comuna());
//                    idComuna = fichasCompletas.getComuna().getId_comuna();
//
//                    cargarComuna();
//                    sp_comuna_agricultor.setSelection(idComunas.indexOf(idComuna));
//                }

            }
        }
    }


    private void setAdapters() {

        if (years.size() > 0) {
            ArrayList<String> str = new ArrayList<>();
            int contador = 0;
            for (Temporada t : years) {
                str.add(t.getNombre_tempo());
                idTemporadas.add(contador, t.getId_tempo_tempo());
                if (t.getEspecial_temporada() > 0) {
                    marca_especial_temporada = t.getId_tempo_tempo();
                }
                contador++;
            }
            sp_year.setAdapter(new SpinnerAdapter(requireActivity(), R.layout.spinner_template_view, str));
            if (!marca_especial_temporada.isEmpty()) {
                sp_year.setSelection(idTemporadas.indexOf(marca_especial_temporada));

            }
        }



/*        sp_asoc_agr.setAdapter(new SpinnerAdapter(Objects.requireNonNull(getActivity()),R.layout.spinner_template_view, getResources().getStringArray(R.array.asociacion_agricultor)));
        sp_asoc_agr.setSelection(1);*/

        List<Agricultor> agricultorList = MainActivity.myAppDB.myDao().getAgricultores();
        if (agricultorList.size() > 0) {
            ArrayList<String> str = new ArrayList<>();
            int contador = 0;
//            str.add(getResources().getString(R.string.select));
//            rutAgricultores.add(contador,"");
//            contador++;
            for (Agricultor fs : agricultorList) {
                str.add(fs.getRut_agricultor() + " " + fs.getNombre_agricultor());
                rutAgricultores.add(contador, fs.getRut_agricultor());
                contador++;
            }
            sp_agric.setTitle(getResources().getString(R.string.seleccione_item));
            sp_agric.setPositiveButton(getResources().getString(R.string.ok));
            sp_agric.setAdapter(new SpinnerAdapter(requireActivity(), R.layout.spinner_template_view, str));

        }


        if (regionList != null && regionList.size() > 0) {
            ArrayList<String> rg = new ArrayList<>();
            int contador = 0;
            rg.add(getResources().getString(R.string.select));
            idRegiones.add(contador, "");
            contador++;

            for (Region re : regionList) {
                rg.add(re.getDesc_region());
                idRegiones.add(contador, re.getId_region());
                contador++;
            }

            sp_region_agricultor.setAdapter(new SpinnerAdapter(requireActivity(), R.layout.spinner_template_view, rg));
//            sp_region_agricultor.setSelection(0);

            cargarProvincia();
            cargarComuna();

            cargarEspecie();
            cargarSpinnerIndependientes();
        }
    }


    private void cargarEspecie() {

        List<Especie> especieList = MainActivity.myAppDB.myDao().getEspecies();

        if (especieList != null && especieList.size() > 0) {
            ArrayList<String> rg = new ArrayList<>();
            int contador = 0;
            rg.add(getResources().getString(R.string.select));
            idEspecies.add(contador, "");
            contador++;
            int selectable = 0;
            for (Especie esp : especieList) {
                rg.add(esp.getDesc_especie());
                idEspecies.add(contador, esp.getId_especie());

                if (idEspecie.equals(esp.getId_especie())) {
                    selectable = contador;
                }
                contador++;
            }
            sp_especie.setAdapter(new SpinnerAdapter(requireActivity(), R.layout.spinner_template_view, rg));
            sp_especie.setSelection(selectable);

        } else {
            ArrayList<String> rg = new ArrayList<>();
            int contador = 0;
            rg.add(getResources().getString(R.string.select));
            idEspecies.add(contador, "");
            sp_especie.setAdapter(new SpinnerAdapter(requireActivity(), R.layout.spinner_template_view, rg));
        }

    }

    private void cargarSpinnerRiego(List<TipoRiego> tipoRiegoList) {
        idTipoRiegos = new ArrayList<>();

        if (tipoRiegoList != null && tipoRiegoList.size() > 0) {
            ArrayList<String> rg = new ArrayList<>();
            int contador = 0;
            rg.add(getResources().getString(R.string.select));
            idTipoRiegos.add(contador, "");
            contador++;
            int selectable = 0;
            for (TipoRiego esp : tipoRiegoList) {
                rg.add(esp.getDescripcion());
                idTipoRiegos.add(contador, esp.getId_tipo_riego());

                if (idTipoRiego.equals(esp.getId_tipo_riego())) {
                    selectable = contador;
                }
                contador++;
            }
            sp_tipo_riego.setAdapter(new SpinnerAdapter(requireActivity(), R.layout.spinner_template_view, rg));
            sp_tipo_riego.setSelection(selectable);

        } else {
            ArrayList<String> rg = new ArrayList<>();
            int contador = 0;
            rg.add(getResources().getString(R.string.select));
            idTipoRiegos.add(contador, "");
            sp_tipo_riego.setAdapter(new SpinnerAdapter(requireActivity(), R.layout.spinner_template_view, rg));
        }
    }

    private void cargarSpinnerIndependientes() {

        List<TipoRiego> tipoRiegoList = MainActivity.myAppDB.myDao().getTipoRiegoVigente();
        cargarSpinnerRiego(tipoRiegoList);


        List<TipoSuelo> tipoSueloList = MainActivity.myAppDB.myDao().getTipoSuelo();

        if (tipoSueloList != null && tipoSueloList.size() > 0) {
            ArrayList<String> rg = new ArrayList<>();
            int contador = 0;
            rg.add(getResources().getString(R.string.select));
            idTipoSuelos.add(contador, "");
            contador++;
            int selectable = 0;
            for (TipoSuelo esp : tipoSueloList) {
                rg.add(esp.getDescripcion());
                idTipoSuelos.add(contador, esp.getId_tipo_suelo());

                if (idTipoSuelo.equals(esp.getId_tipo_suelo())) {
                    selectable = contador;
                }
                contador++;
            }
            sp_tipo_suelo.setAdapter(new SpinnerAdapter(requireActivity(), R.layout.spinner_template_view, rg));
            sp_tipo_suelo.setSelection(selectable);

        } else {
            ArrayList<String> rg = new ArrayList<>();
            int contador = 0;
            rg.add(getResources().getString(R.string.select));
            idTipoSuelos.add(contador, "");
            sp_tipo_suelo.setAdapter(new SpinnerAdapter(requireActivity(), R.layout.spinner_template_view, rg));
        }


        ArrayList<String> rgExp = new ArrayList<>();
        int contadorExp = 0;
        rgExp.add(getResources().getString(R.string.select));
        idExperiencias.add(contadorExp, "");
        contadorExp++;
        rgExp.add("SI");
        idExperiencias.add(contadorExp, "SI");
        contadorExp++;
        rgExp.add("NO");
        idExperiencias.add(contadorExp, "NO");
        sp_experiencia.setAdapter(new SpinnerAdapter(requireActivity(), R.layout.spinner_template_view, rgExp));

        sp_experiencia.setSelection((idExperiencia.equals("SI")) ? 1 : (idExperiencia.equals("NO") ? 2 : 0));


        ArrayList<String> rgTen = new ArrayList<>();
        int contadorTen = 0;
        rgTen.add(getResources().getString(R.string.select));
        idTenencias.add(contadorTen, "");
        contadorTen++;
        rgTen.add("PROPIA");
        idTenencias.add(contadorTen, "PROPIA");
        contadorTen++;
        rgTen.add("ARRENDADA");
        idTenencias.add(contadorTen, "ARRENDADA");
        sp_tenencia.setAdapter(new SpinnerAdapter(requireActivity(), R.layout.spinner_template_view, rgTen));
        sp_tenencia.setSelection((idTenencia.equals("PROPIA")) ? 1 : (idTenencia.equals("ARRENDADA") ? 2 : 0));


        ArrayList<String> rgMaq = new ArrayList<>();
        int contadorMaq = 0;
        rgMaq.add(getResources().getString(R.string.select));
        idMaquinarias.add(contadorMaq, "");
        contadorMaq++;
        rgMaq.add("PROPIA");
        idMaquinarias.add(contadorMaq, "PROPIA");
        contadorMaq++;
        rgMaq.add("ARRENDADA");
        idMaquinarias.add(contadorMaq, "ARRENDADA");
        sp_maquinaria.setAdapter(new SpinnerAdapter(requireActivity(), R.layout.spinner_template_view, rgMaq));
        sp_maquinaria.setSelection((idMaquinaria.equals("PROPIA")) ? 1 : (idMaquinaria.equals("ARRENDADA") ? 2 : 0));

    }

    private void cargarProvincia() {
        if (provinciaList != null && provinciaList.size() > 0) {
            ArrayList<String> rg = new ArrayList<>();
            int contador = 0;
            rg.add(getResources().getString(R.string.select));
            idProvincias.add(contador, "");
            contador++;
            int selectable = 0;
            for (Provincia re : provinciaList) {
                rg.add(re.getNombre_provincia());
                idProvincias.add(contador, re.getId_provincia());

                if (idProvincia.equals(re.getId_provincia())) {
                    selectable = contador;
                }
                contador++;
            }
            sp_provincia_agricultor.setAdapter(new SpinnerAdapter(requireActivity(), R.layout.spinner_template_view, rg));
            sp_provincia_agricultor.setSelection(selectable);

            cargarComuna();
        } else {
            ArrayList<String> rg = new ArrayList<>();
            int contador = 0;
            rg.add(getResources().getString(R.string.select));
            idProvincias.add(contador, "");
            sp_provincia_agricultor.setAdapter(new SpinnerAdapter(requireActivity(), R.layout.spinner_template_view, rg));
//            sp_provincia_agricultor.setSelection(selectable);
            cargarComuna();
        }
    }

    private void cargarComuna() {
        if (comunaList != null && comunaList.size() > 0) {
            ArrayList<String> rg = new ArrayList<>();
            int contador = 0;
            rg.add(getResources().getString(R.string.select));
            idComunas.add(contador, "");
            contador++;
            int selectable = 0;

            for (Comuna re : comunaList) {
                rg.add(re.getDesc_comuna());
                idComunas.add(contador, re.getId_comuna());
                if (idComuna.equals(re.getId_comuna())) {
                    selectable = contador;
                }
                contador++;
            }

            sp_comuna_agricultor.setAdapter(new SpinnerAdapter(requireActivity(), R.layout.spinner_template_view, rg));
            sp_comuna_agricultor.setSelection(selectable);
        } else {
            ArrayList<String> rg = new ArrayList<>();
            int contador = 0;
            rg.add(getResources().getString(R.string.select));
            idComunas.add(contador, "");
            sp_comuna_agricultor.setAdapter(new SpinnerAdapter(requireActivity(), R.layout.spinner_template_view, rg));
        }
    }

    /* IMAGENES */


    private void abrirCamara() {

        File photoFile = null;
        try {
            photoFile = Utilidades.createImageFile(requireActivity());
            currentPhotoPath = photoFile.getAbsolutePath();
        } catch (IOException e) {
            Toasty.error(requireActivity(), "No se pudo crear la imagen " + e.getMessage(), Toast.LENGTH_LONG, true).show();
        }

        if (photoFile == null) {
            Toasty.error(requireActivity(), "No se pudo crear la imagen (nula)", Toast.LENGTH_LONG, true).show();
            return;
        }

        Uri currentPhotoUri = FileProvider.getUriForFile(requireActivity(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
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

            guardarBD();

        } catch (Exception e) {
            Toasty.error(requireActivity(), "Error al procesar la foto: " + e.getMessage(), Toast.LENGTH_LONG, true).show();
        }
    }


    private void guardarBD() {

        ejecutarSeguro(() -> {
            try {
                File file = new File(currentPhotoPath);
                FotosFichas fotos = new FotosFichas();
                fotos.setFecha_hora_captura(Utilidades.fechaActualConHora());


                fotos.setNombre_foto_ficha(file.getName());
                fotos.setRuta_foto_ficha(file.getAbsolutePath());

                if (fichasCompletas != null) {
                    if (fichasCompletas.getFichas() != null) {
                        fotos.setId_ficha_fotos_local(fichasCompletas.getFichas().getId_ficha());
                        fotos.setId_ficha_fotos_servidor(fichasCompletas.getFichas().getId_ficha());
                    }
                }


                Config config = MainActivity.myAppDB.myDao().getConfig();
                if (config != null) {
                    fotos.setId_dispo_captura(config.getId());
                    fotos.setId_usuario_captura(config.getId_usuario());
                }
                MainActivity.myAppDB.myDao().insertFotosFichas(fotos);
                handler.post(this::agregarImagenToFichas);
            } catch (Exception e) {
                handler.post(() -> Toasty.error(activity, "Error guardando foto" + e.getMessage(), Toast.LENGTH_LONG, true).show());
            }
        });
    }


    private void agregarImagenToFichas() {

        final int idFichaLocal = (fichasCompletas != null && fichasCompletas.getFichas() != null) ? fichasCompletas.getFichas().getId_ficha_local_ficha() : 0;
        final int idFicha = (fichasCompletas != null && fichasCompletas.getFichas() != null) ? fichasCompletas.getFichas().getId_ficha() : 0;
        ejecutarSeguro(() -> {

            Config config = MainActivity.myAppDB.myDao().getConfig();
            List<FotosFichas> myImageList = MainActivity.myAppDB.myDao().getFotosFichasByIdes(idFichaLocal, idFicha, config.getId());

            handler.post(() -> {
                lista_fotos_ficha.setHasFixedSize(true);

                adapterFotosFichas = new FotosFichasAdapter(myImageList, activity, fotos -> {
                }, fotos -> {
                });
                lista_fotos_ficha.setAdapter(adapterFotosFichas);
            });
        });
    }


    private void levantarFecha() {


        String fecha = Utilidades.fechaActualSinHora();
        String[] fechaRota;

        if (!TextUtils.isEmpty(et_fecha_limite_siembra.getText())) {
            try {
                fechaRota = Utilidades.voltearFechaBD(et_fecha_limite_siembra.getText().toString()).split("-");
            } catch (Exception e) {
                fechaRota = fecha.split("-");
            }
        } else {
            fechaRota = fecha.split("-");
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                month = month + 1;

                String mes = "", dia;

                if (month < 10) {
                    mes = "0" + month;
                } else {
                    mes = String.valueOf(month);
                }
                if (dayOfMonth < 10) dia = "0" + dayOfMonth;
                else dia = String.valueOf(dayOfMonth);

                String finalDate = dia + "-" + mes + "-" + year;
//                edit.setHint("");
                et_fecha_limite_siembra.setText(finalDate);
            }
        }, Integer.parseInt(fechaRota[0]), (Integer.parseInt(fechaRota[1]) - 1), Integer.parseInt(fechaRota[2]));
//        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }


    private void bind(View view) {


        lista_fotos_ficha = (RecyclerView) view.findViewById(R.id.lista_fotos_ficha);

        floating_picture_fichas = (FloatingActionButton) view.findViewById(R.id.floating_picture_fichas);


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

//        et_oferta_neg_agricultor.setFilters(new InputFilter[] { inputfilter });
//        et_localidad_agricultor.setFilters(new InputFilter[] { inputfilter });
//        et_has_disp_agricultor.setFilters(new InputFilter[] { inputfilter });
//        et_obs_agricultor.setFilters(new InputFilter[] { inputfilter });


        btn_captura_gps = (Button) view.findViewById(R.id.btn_captura_gps);


        ti_easting = (EditText) view.findViewById(R.id.ti_easting);
        ti_norting = (EditText) view.findViewById(R.id.ti_norting);

        ti_norting_manual = (EditText) view.findViewById(R.id.ti_norting_manual);
        ti_easting_manual = (EditText) view.findViewById(R.id.ti_easting_manual);


        ti_norting_manual.setEnabled(false);
        ti_easting_manual.setEnabled(false);


        sp_especie = (Spinner) view.findViewById(R.id.sp_especie);
        sp_tipo_suelo = (Spinner) view.findViewById(R.id.sp_tipo_suelo);
        sp_tipo_riego = (Spinner) view.findViewById(R.id.sp_tipo_riego);
        sp_experiencia = (Spinner) view.findViewById(R.id.sp_experiencia);
        sp_tenencia = (Spinner) view.findViewById(R.id.sp_tenencia);
        sp_maquinaria = (Spinner) view.findViewById(R.id.sp_maquinaria);
        sp_maquinaria = (Spinner) view.findViewById(R.id.sp_maquinaria);


        et_predio = (EditText) view.findViewById(R.id.et_predio);
        et_potrero = (EditText) view.findViewById(R.id.et_potrero);
        et_rotacion_1 = (EditText) view.findViewById(R.id.et_rotacion_1);
        et_rotacion_2 = (EditText) view.findViewById(R.id.et_rotacion_2);
        et_rotacion_3 = (EditText) view.findViewById(R.id.et_rotacion_3);
        et_rotacion_4 = (EditText) view.findViewById(R.id.et_rotacion_4);
        et_rotacion_5 = (EditText) view.findViewById(R.id.et_rotacion_5);
        et_carga_maleza = (EditText) view.findViewById(R.id.et_carga_maleza);
        et_estado_general = (EditText) view.findViewById(R.id.et_estado_general);
        et_fecha_limite_siembra = (EditText) view.findViewById(R.id.et_fecha_limite_siembra);

        et_fecha_limite_siembra.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) levantarFecha();
            }
        });

        et_obs_negocio = (EditText) view.findViewById(R.id.et_obs_negocio);

        TextInputLayout cont_rotacion_3 = (TextInputLayout) view.findViewById(R.id.cont_rotacion_3);
        TextInputLayout cont_rotacion_2 = (TextInputLayout) view.findViewById(R.id.cont_rotacion_2);
        TextInputLayout cont_rotacion_1 = (TextInputLayout) view.findViewById(R.id.cont_rotacion_1);
        TextInputLayout cont_rotacion_4 = (TextInputLayout) view.findViewById(R.id.cont_rotacion_4);
        TextInputLayout cont_rotacion_5 = (TextInputLayout) view.findViewById(R.id.cont_rotacion_5);


        ti_easting.setSelectAllOnFocus(true);
        ti_norting.setSelectAllOnFocus(true);

        et_rotacion_1.setSelectAllOnFocus(true);
        et_rotacion_2.setSelectAllOnFocus(true);
        et_rotacion_3.setSelectAllOnFocus(true);
        et_rotacion_4.setSelectAllOnFocus(true);
        et_rotacion_5.setSelectAllOnFocus(true);


        btn_save_agricultor = (Button) view.findViewById(R.id.btn_save_agricultor);


        btn_captura_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(ti_easting_manual.getText()) && !TextUtils.isEmpty(ti_norting_manual.getText())) {
                    ti_norting.setText(ti_norting_manual.getText());
                    ti_easting.setText(ti_easting_manual.getText());
                } else {
                    Utilidades.avisoListo(activity, "Atencion", "No hay contenido para capturar, espere a que el gps tome las coordenadas.", "entiendo");
                }
            }
        });


    }
}