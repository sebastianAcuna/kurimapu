package cl.smapdev.curimapu.fragments.almacigos;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cl.smapdev.curimapu.BuildConfig;
import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.FotosAlmacigosAdapter;
import cl.smapdev.curimapu.clases.modelo.FotoVisitaModel;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.FotosAlmacigos;
import cl.smapdev.curimapu.clases.tablas.OpAlmacigos;
import cl.smapdev.curimapu.clases.tablas.VisitasAlmacigos;
import cl.smapdev.curimapu.clases.utilidades.CameraUtils;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import es.dmoral.toasty.Toasty;


public class FragmentVisitasAlmacigos extends Fragment {


    private static final int REQUEST_PERMISO_CAMARA = 100;
    private MainActivity activity;
    private OpAlmacigos almacigo;
    private VisitasAlmacigos visitasAlmacigos;


    private Spinner sp_crecimiento;
    private EditText et_obs_growth;
    private Spinner sp_malezas;
    private EditText et_obs_maleza;
    private Spinner sp_fito;
    private EditText et_obs_fito;
    private Spinner sp_humedad_suelo;
    private EditText et_obs_humedad;
    private EditText et_n_hojas;
    private EditText et_altura;
    private EditText et_uniformidad;
    private Spinner sp_estado_general;
    private EditText et_obs_estado_general;
    private EditText et_obs_general;

    private EditText et_cobertura_raices;
    private Spinner sp_dureza;
    private EditText et_obs_dureza;
    private EditText et_emergencia_preliminar;
    private Button btn_tomar_foto;


    private EditText et_diametro;


    private RecyclerView rv_listado_fotos;
    private Button btn_guardar;

    final List<FotoVisitaModel> fotosTomadas = new ArrayList<>();

    private static final int REQUEST_TOMAR_FOTO = 101;
    private Uri uriFotoTemporal;
    private File archivoFotoTemporal;

    private FotosAlmacigosAdapter fotosAdapter;

    final private ExecutorService ioExecutor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private String currentPhotoPath;
    private int tipoFoto;
    private Uri currentPhotoUri;
    private ActivityResultLauncher<Uri> cameraLauncher;

    private ProgressDialog progressDialog;

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

    public void setAlmacigo(OpAlmacigos almacigo) {
        this.almacigo = almacigo;
    }

    public void setVisitasAlmacigos(VisitasAlmacigos visitasAlmacigos) {
        this.visitasAlmacigos = visitasAlmacigos;
    }

    public static FragmentVisitasAlmacigos newInstance(MainActivity activity, OpAlmacigos almacigo, VisitasAlmacigos visitasAlmacigos) {
        FragmentVisitasAlmacigos fragment = new FragmentVisitasAlmacigos();
        fragment.setActivity(activity);
        fragment.setAlmacigo(almacigo);
        fragment.setVisitasAlmacigos(visitasAlmacigos);
        Bundle args = new Bundle();
        if (almacigo != null) {
            args.putInt("arg_id_op_almacigo", almacigo.getId_v_post_siembra());
        }
        if (visitasAlmacigos != null) {
            args.putString("arg_id_visita_almacigo", visitasAlmacigos.getUid_visita());
        }
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            activity = (MainActivity) context;
        } else {
            throw new RuntimeException(context + " must be MainActivity");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ioExecutor != null && !ioExecutor.isShutdown()) {
            ioExecutor.shutdown();
        }

        if (!fotosTomadas.isEmpty()) {
            fotosTomadas.clear();
        }

        uriFotoTemporal = null;
        archivoFotoTemporal = null;

    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            int idOpAlmacigo = args.getInt("arg_id_op_almacigo", 0);
            String UidVisitaAlmacigo = args.getString("arg_id_visita_almacigo", "");

            OpAlmacigos opAL = MainActivity.myAppDB.DaoOPAlmacigos().getOpAlmacigoById(idOpAlmacigo);
            this.setAlmacigo(opAL);

            VisitasAlmacigos visAl = MainActivity.myAppDB.VisitasFotosAlmacigos().getVisitaByUid(UidVisitaAlmacigo);
            this.setVisitasAlmacigos(visAl);
        }
        if (savedInstanceState != null) {

            ArrayList<String> paths = savedInstanceState.getStringArrayList("foto_paths");
            ArrayList<Integer> favs = savedInstanceState.getIntegerArrayList("foto_favs");
            if (paths != null) {
                fotosTomadas.clear();
                for (int i = 0; i < paths.size(); i++) {
                    String p = paths.get(i);
                    File f = new File(p);
                    boolean fav = (favs != null && i < favs.size() && favs.get(i) == 1);
                    fotosTomadas.add(new FotoVisitaModel(f, fav));
                }
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
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<String> paths = new ArrayList<>();
        ArrayList<Integer> favs = new ArrayList<>();
        for (FotoVisitaModel fvm : fotosTomadas) {
            paths.add(fvm.getArchivo().getAbsolutePath());
            favs.add(fvm.isFavorita() ? 1 : 0);
        }
        outState.putStringArrayList("foto_paths", paths);
        outState.putIntegerArrayList("foto_favs", favs);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_visita_almacigo, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bind(view);

        fotosAdapter = new FotosAlmacigosAdapter(activity, fotosTomadas);
        fotosAdapter.setOnFavoritaClicked(pos -> {
            FotoVisitaModel foto = fotosTomadas.get(pos);
            if (!foto.isFavorita()) {
                long favoritas = 0;
                for (FotoVisitaModel f : fotosTomadas) {
                    if (f.isFavorita()) favoritas++;
                }
                if (favoritas >= 4) return false;
            }

            foto.setFavorita(!foto.isFavorita());
            return true;
        });
        rv_listado_fotos.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        rv_listado_fotos.setAdapter(fotosAdapter);


        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        String subt = (visitasAlmacigos == null) ? "Nueva visita" : "Ver visita";
        Utilidades.setToolbar(activity, view, "Almacigos", subt);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void tomarFoto() {

        File photoFile = null;
        String uid = UUID.randomUUID().toString();
        String nombre = uid + "_foto_almacigo_";

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
        Bitmap originalBtm = BitmapFactory.decodeFile(currentPhotoPath);
        try {
            btn_tomar_foto.setEnabled(false);

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

            long favoritas = 0;
            for (FotoVisitaModel foto : fotosTomadas) {
                if (foto.isFavorita()) {
                    favoritas++;
                }
            }
            boolean esFavorita = (favoritas < 4); // Marca como favorita si hay menos de 4 favoritas
            fotosTomadas.add(new FotoVisitaModel(file, esFavorita));

            fotosAdapter.notifyItemInserted(fotosTomadas.size() - 1);

        } catch (Exception e) {
            Toasty.error(requireActivity(), "Error al procesar la foto: " + e.getMessage(), Toast.LENGTH_LONG, true).show();
        } catch (OutOfMemoryError e) {
            Toast.makeText(activity, "Memoria insuficiente al procesar la foto", Toast.LENGTH_LONG).show();
        } finally {
            btn_tomar_foto.setEnabled(true);
            if (originalBtm != null && !originalBtm.isRecycled()) {
                originalBtm.recycle();
                originalBtm = null;
            }

            System.gc();
            if (archivoFotoTemporal != null && archivoFotoTemporal.exists()) {
                archivoFotoTemporal.delete();
            }

        }
    }


    private void bind(View view) {
        //    general
        TextView nombre_parental = view.findViewById(R.id.nombre_parental);
        TextView cliente = view.findViewById(R.id.cliente);
        TextView especie = view.findViewById(R.id.especie);
        TextView variedad = view.findViewById(R.id.variedad);
        TextView fecha_siembra = view.findViewById(R.id.fecha_siembra);
        TextView fecha_estimada_despacho = view.findViewById(R.id.fecha_estimada_despacho);
        //    CUBIERTA
        TextView lbl_op = view.findViewById(R.id.lbl_op);
        TextView op = view.findViewById(R.id.op);
        TextView tipo_lc = view.findViewById(R.id.tipo_lc);
        //    DESNUDA
        TextView dias_cultivo = view.findViewById(R.id.dias_cultivo);
        sp_crecimiento = view.findViewById(R.id.sp_crecimiento);
        et_obs_growth = view.findViewById(R.id.et_obs_growth);
        sp_malezas = view.findViewById(R.id.sp_malezas);
        et_obs_maleza = view.findViewById(R.id.et_obs_maleza);
        sp_fito = view.findViewById(R.id.sp_fito);
        et_obs_fito = view.findViewById(R.id.et_obs_fito);
        sp_humedad_suelo = view.findViewById(R.id.sp_humedad_suelo);
        et_obs_humedad = view.findViewById(R.id.et_obs_humedad);
        et_n_hojas = view.findViewById(R.id.et_n_hojas);
        et_altura = view.findViewById(R.id.et_altura);
        et_uniformidad = view.findViewById(R.id.et_uniformidad);
        sp_estado_general = view.findViewById(R.id.sp_estado_general);
        et_obs_estado_general = view.findViewById(R.id.et_obs_estado_general);
        sp_dureza = view.findViewById(R.id.sp_dureza);
        TextInputLayout obs_dureza = view.findViewById(R.id.obs_dureza);
        et_obs_dureza = view.findViewById(R.id.et_obs_dureza);
        TextView lbl_emergencia_preliminar = view.findViewById(R.id.lbl_emergencia_preliminar);
        et_emergencia_preliminar = view.findViewById(R.id.et_emergencia_preliminar);

        et_obs_general = view.findViewById(R.id.et_obs_general);

        TextView lbl_cobertura_raices = view.findViewById(R.id.lbl_cobertura_raices);
        et_cobertura_raices = view.findViewById(R.id.et_cobertura_raices);
        TextView lbl_dureza = view.findViewById(R.id.lbl_dureza);

        //    DESNUDA
        TextView lbl_nom_fantasia = view.findViewById(R.id.lbl_nom_fantasia);
        TextView nom_fantasia = view.findViewById(R.id.nom_fantasia);
        TextView lbl_diametro = view.findViewById(R.id.lbl_diametro);
        et_diametro = view.findViewById(R.id.et_diametro);


        rv_listado_fotos = view.findViewById(R.id.rv_listado_fotos);
        btn_guardar = view.findViewById(R.id.btn_guardar);
        Button btn_volver = view.findViewById(R.id.btn_volver);
        btn_tomar_foto = view.findViewById(R.id.btn_tomar_foto);


        btn_tomar_foto.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISO_CAMARA);
            } else {
                tomarFoto();
            }

        });
        btn_guardar.setOnClickListener(v -> prepararGuardar());
        btn_volver.setOnClickListener(v -> activity.onBackPressed());


        if (almacigo != null) {
            nombre_parental.setText(almacigo.getNombre_parental());
            cliente.setText(almacigo.getNombre_cliente());
            especie.setText(almacigo.getNombre_especie());
            variedad.setText(almacigo.getNombre_variedad());
            String fechaSiembra = (almacigo.getFecha_siembra() != null && !almacigo.getFecha_siembra().isEmpty()) ? Utilidades.voltearFechaVista(almacigo.getFecha_siembra()) : "sin fecha";
            String fechaDespacho = (almacigo.getFecha_estimada_despacho() != null && !almacigo.getFecha_estimada_despacho().isEmpty()) ? Utilidades.voltearFechaVista(almacigo.getFecha_estimada_despacho()) : "sin fecha";

            fecha_siembra.setText(fechaSiembra);
            fecha_estimada_despacho.setText(fechaDespacho);
            tipo_lc.setText(almacigo.getDescripcion_tipo_raiz());
            if (almacigo.getFecha_siembra() != null && !almacigo.getFecha_siembra().isEmpty()) {
                long dias = Utilidades.diferenciaDiasEntreFechas(almacigo.getFecha_siembra(), LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                dias_cultivo.setText(String.valueOf(Math.abs(dias)));
            }

            if (almacigo.getId_tipo_lc() == Utilidades.RAIZ_CUBIERTA) {
                lbl_nom_fantasia.setVisibility(View.GONE);
                nom_fantasia.setVisibility(View.GONE);
                lbl_diametro.setVisibility(View.GONE);
                et_diametro.setVisibility(View.GONE);

                op.setText(almacigo.getOp());

            } else {
                //desnuda
                lbl_op.setVisibility(View.GONE);
                op.setVisibility(View.GONE);
                lbl_cobertura_raices.setVisibility(View.GONE);
                et_cobertura_raices.setVisibility(View.GONE);
                lbl_dureza.setVisibility(View.GONE);
                sp_dureza.setVisibility(View.GONE);
                obs_dureza.setVisibility(View.GONE);
                et_obs_dureza.setVisibility(View.GONE);
                lbl_emergencia_preliminar.setVisibility(View.GONE);
                et_emergencia_preliminar.setVisibility(View.GONE);
                nom_fantasia.setText(almacigo.getNombre_fantasia());
            }

        }


        if (visitasAlmacigos != null) {

            List<String> creciArray = Arrays.asList(getResources().getStringArray(R.array.crecimiento));

            int crecimiento = creciArray.indexOf(visitasAlmacigos.getEstado_crecimiento());
            int maleza = Arrays.asList(getResources().getStringArray(R.array.maleza)).indexOf(visitasAlmacigos.getEstado_maleza());
            int fito = creciArray.indexOf(visitasAlmacigos.getEstado_fito());
            int hum = creciArray.indexOf(visitasAlmacigos.getHumedad_suelo());
            int dureza = Arrays.asList(getResources().getStringArray(R.array.dureza)).indexOf(visitasAlmacigos.getDureza());
            int est_gen = creciArray.indexOf(visitasAlmacigos.getEstado_general());
            sp_crecimiento.setSelection(crecimiento > -1 ? crecimiento : 0);
            sp_malezas.setSelection(maleza > -1 ? maleza : 0);
            sp_fito.setSelection(fito > -1 ? fito : 0);
            sp_humedad_suelo.setSelection(hum > -1 ? hum : 0);
            sp_dureza.setSelection(dureza > -1 ? dureza : 0);
            sp_estado_general.setSelection(est_gen > -1 ? est_gen : 0);


            dias_cultivo.setText(String.valueOf(visitasAlmacigos.getDias_cultivo_a_visita()));

            et_obs_growth.setText(visitasAlmacigos.getObs_crecimiento());
            et_obs_maleza.setText(visitasAlmacigos.getObs_maleza());
            et_obs_fito.setText(visitasAlmacigos.getObs_fito());
            et_obs_humedad.setText(visitasAlmacigos.getObs_humedad());
            et_obs_dureza.setText(visitasAlmacigos.getObs_dureza());
            et_obs_estado_general.setText(visitasAlmacigos.getObs_estado_general());

            et_n_hojas.setText(String.valueOf(visitasAlmacigos.getN_hoja()));
            et_altura.setText(String.valueOf(visitasAlmacigos.getAltura()));
            et_cobertura_raices.setText(String.valueOf(visitasAlmacigos.getCobertura_raices()));
            et_diametro.setText(String.valueOf(visitasAlmacigos.getDiametro()));
            et_uniformidad.setText(String.valueOf(visitasAlmacigos.getUniformidad()));
            et_emergencia_preliminar.setText(String.valueOf(visitasAlmacigos.getEmergencia_preliminar()));
            et_obs_general.setText(String.valueOf(visitasAlmacigos.getComentario()));


            et_n_hojas.setEnabled(false);
            et_altura.setEnabled(false);
            et_cobertura_raices.setEnabled(false);
            et_diametro.setEnabled(false);
            et_uniformidad.setEnabled(false);
            et_emergencia_preliminar.setEnabled(false);
            et_obs_general.setEnabled(false);

            et_obs_growth.setEnabled(false);
            et_obs_maleza.setEnabled(false);
            et_obs_fito.setEnabled(false);
            et_obs_humedad.setEnabled(false);
            et_obs_dureza.setEnabled(false);
            et_obs_estado_general.setEnabled(false);

            sp_crecimiento.setEnabled(false);
            sp_malezas.setEnabled(false);
            sp_fito.setEnabled(false);
            sp_humedad_suelo.setEnabled(false);
            sp_dureza.setEnabled(false);
            sp_estado_general.setEnabled(false);
            btn_tomar_foto.setEnabled(false);
            btn_guardar.setEnabled(false);
        }

    }


    public void prepararGuardar() {
        // Validación de null para evitar crash
        if (sp_crecimiento == null || sp_malezas == null || sp_fito == null || sp_humedad_suelo == null || sp_estado_general == null || sp_dureza == null) {
            Utilidades.avisoListo(activity, "Error", "Hay campos obligatorios que no están inicializados. Por favor, reinicie la pantalla.", "OK");
            return;
        }

        if (sp_crecimiento.getSelectedItemPosition() <= 0 ||
                sp_malezas.getSelectedItemPosition() <= 0 ||
                sp_fito.getSelectedItemPosition() <= 0 ||
                sp_humedad_suelo.getSelectedItemPosition() <= 0 ||
                sp_estado_general.getSelectedItemPosition() <= 0) {
            Utilidades.avisoListo(activity, "Hey", "no puede dejar elementos en '--seleccione--'.", "entiendo");
            return;
        }


        if (almacigo.getId_tipo_lc() == Utilidades.RAIZ_CUBIERTA && sp_dureza.getSelectedItemPosition() <= 0) {
            Utilidades.avisoListo(activity, "Hey", "no puede dejar elementos en '--seleccione--'.", "entiendo");
            return;
        }


        if (fotosTomadas == null || fotosTomadas.size() < 2) {
            Utilidades.avisoListo(activity, "Atención", "Debe tomar al menos 2 fotos", "entiendo");
            return;
        }

        long favoritas = 0;
        for (FotoVisitaModel foto : fotosTomadas) {
            if (foto.isFavorita()) {
                favoritas++;
            }
        }

        if (favoritas < 2) {
            Utilidades.avisoListo(activity, "Atención", "Debe marcar al menos 2 fotos como favoritas.", "OK");
            return;
        }

        btn_guardar.setEnabled(false);
        mostrarDialogoGuardando();
        guardar();
    }

    public void guardar() {

        ioExecutor.execute(() -> {
            try {
                Config config = MainActivity.myAppDB.myDao().getConfig();

                VisitasAlmacigos visita = new VisitasAlmacigos();

                String uidVisita = UUID.randomUUID().toString();
                visita.setUid_visita(uidVisita);
                visita.setId_valor_post_siembra(almacigo.getId_v_post_siembra());

                if (almacigo.getFecha_siembra() != null && !almacigo.getFecha_siembra().isEmpty()) {
                    long dias = Utilidades.diferenciaDiasEntreFechas(almacigo.getFecha_siembra(), LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    visita.setDias_cultivo_a_visita((int) Math.abs(dias));
                }

                visita.setEstado_crecimiento(sp_crecimiento.getSelectedItem().toString());
                visita.setEstado_maleza(sp_malezas.getSelectedItem().toString());
                visita.setEstado_fito(sp_fito.getSelectedItem().toString());
                visita.setHumedad_suelo(sp_humedad_suelo.getSelectedItem().toString());
                if (!et_n_hojas.getText().toString().isEmpty()) {
                    visita.setN_hoja(Double.parseDouble(et_n_hojas.getText().toString()));
                }
                if (!et_altura.getText().toString().isEmpty()) {
                    visita.setAltura(Double.parseDouble(et_altura.getText().toString()));
                }
                if (!et_uniformidad.getText().toString().isEmpty()) {
                    visita.setUniformidad(Double.parseDouble(et_uniformidad.getText().toString()));
                }
                visita.setEstado_general(sp_estado_general.getSelectedItem().toString());
                visita.setComentario(et_obs_general.getText().toString());
                visita.setId_user_crea(config.getId_usuario());
                visita.setFecha_hora_crea(Utilidades.fechaActualConHora());
                visita.setEstado_sincronizacion(0);

                visita.setObs_crecimiento(et_obs_growth.getText().toString());
                visita.setObs_dureza(et_obs_dureza.getText().toString());
                visita.setObs_fito(et_obs_fito.getText().toString());
                visita.setObs_humedad(et_obs_humedad.getText().toString());
                visita.setObs_maleza(et_obs_maleza.getText().toString());
                visita.setObs_estado_general(et_obs_estado_general.getText().toString());


//            CUBIERTA
                if (almacigo.getId_tipo_lc() == Utilidades.RAIZ_CUBIERTA) {
                    if (!et_cobertura_raices.getText().toString().isEmpty()) {
                        visita.setCobertura_raices(Double.parseDouble(et_cobertura_raices.getText().toString()));
                    }
                    visita.setDureza(sp_dureza.getSelectedItem().toString());
                    visita.setEmergencia_preliminar(et_emergencia_preliminar.getText().toString());
                }


//            DESNUDA
                if (almacigo.getId_tipo_lc() == Utilidades.RAIZ_DESNUDA) {
                    if (!et_diametro.getText().toString().isEmpty()) {
                        visita.setDiametro(Double.parseDouble(et_diametro.getText().toString()));
                    }
                }


                MainActivity.myAppDB.VisitasFotosAlmacigos().insertarVisitasAlmacigos(visita);

                for (FotoVisitaModel fotoVisitaModel : fotosTomadas) {


                    File archivoTemporal = fotoVisitaModel.getArchivo();
                    String nombreArchivo = archivoTemporal.getName();  // o genera uno nuevo con timestamp
                    File archivoPermanente = guardarFotoDeFormaPermanente(activity, archivoTemporal, nombreArchivo);

                    if (archivoPermanente != null) {
                        FotosAlmacigos fotosAlmacigos = new FotosAlmacigos();
                        fotosAlmacigos.setUid_visita(uidVisita);
                        fotosAlmacigos.setUid_foto(UUID.randomUUID().toString());
                        fotosAlmacigos.setFecha(Utilidades.fechaActualSinHora());
                        fotosAlmacigos.setHora(Utilidades.hora());
                        fotosAlmacigos.setFecha_hora(Utilidades.fechaActualConHora());
                        fotosAlmacigos.setFavorita(fotoVisitaModel.isFavorita() ? 1 : 0);
                        fotosAlmacigos.setNombre_foto(fotoVisitaModel.getArchivo().getName());
                        fotosAlmacigos.setId_user_tx(String.valueOf(config.getId_usuario()));
                        fotosAlmacigos.setRuta_foto(archivoPermanente.getAbsolutePath());
                        fotosAlmacigos.setEstado_sincronizacion("0");
                        MainActivity.myAppDB.VisitasFotosAlmacigos().insertarFotoAlmacigos(fotosAlmacigos);
                    }
                }

                activity.runOnUiThread(() -> {
                    ocultarDialogoGuardando();
                    Toasty.success(activity, "Visita guardada con éxito", Toast.LENGTH_LONG, true).show();
                    activity.cambiarFragment(FragmentListadoVisitasAlmacigos.newInstance(activity), Utilidades.FRAGMENT_LISTA_ALMACIGOS, R.anim.slide_in_left, R.anim.slide_out_left);
                });
            } catch (Exception e) {
                e.printStackTrace();
                activity.runOnUiThread(() -> {
                    ocultarDialogoGuardando();
                    btn_guardar.setEnabled(true);
                    Toasty.error(activity, "Error al guardar la visita: " + e.getMessage(), Toast.LENGTH_LONG, true).show();
                });
            }
        });


    }

    private File guardarFotoDeFormaPermanente(Context context, File archivoTemporal, String nombreArchivo) {
        File directorio = new File(context.getFilesDir(), "fotos_visita_almacigos");

        if (!directorio.exists()) {
            directorio.mkdirs();  // crea la carpeta si no existe
        }

        File archivoDestino = new File(directorio, nombreArchivo);

        try {
            FileInputStream in = new FileInputStream(archivoTemporal);
            FileOutputStream out = new FileOutputStream(archivoDestino);

            byte[] buffer = new byte[1024];
            int len;

            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }

            in.close();
            out.close();

            return archivoDestino;  // Ruta definitiva
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void mostrarDialogoGuardando() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Espere por favor, guardando visita...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    private void ocultarDialogoGuardando() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
