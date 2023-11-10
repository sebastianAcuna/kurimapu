package cl.smapdev.curimapu.fragments.estacion_floracion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.EstacionFloracionEstacionesAdapter;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.relaciones.EstacionFloracionCompleto;
import cl.smapdev.curimapu.clases.relaciones.EstacionesCompletas;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracion;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracionDetalle;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracionEstaciones;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.dialogos.DialogEstaciones;
import es.dmoral.toasty.Toasty;

public class FragmentNuevaEstacionFloracion  extends Fragment {


    private EstacionFloracionCompleto estacionFloracionCompleto;
    private MainActivity activity;
    private SharedPreferences prefs;
    private AnexoCompleto anexoCompleto;
    private Config config;
    private Usuario usuario;

    private EditText et_fecha;
    private EditText et_cantidad_machos;

    private Button btn_agregar_muestra;
    private Button btn_guardar_estacion;
    private Button btn_cancelar_estacion;

    private RecyclerView lista_muestra_estaciones;

    //    cabecera
    private TextView tv_numero_anexo;
    private TextView tv_variedad;
    private TextView tv_resumen;

    public void setEstacionFloracionCompleto(EstacionFloracionCompleto estacionFloracionCompleto) {
        this.estacionFloracionCompleto = estacionFloracionCompleto;
    }

    public static FragmentNuevaEstacionFloracion newInstance(EstacionFloracionCompleto estacionFloracionCompleto ){
        FragmentNuevaEstacionFloracion fs = new FragmentNuevaEstacionFloracion();
        fs.setEstacionFloracionCompleto( estacionFloracionCompleto );
        return fs;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity a = (MainActivity) getActivity();
        if(a != null) activity = a;
        prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nueva_estacion_floracion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bind(view);


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

        cargarListaEstaciones();
    }

    public void cargarListaEstaciones(){

        String claveUnica = (estacionFloracionCompleto != null ) ? estacionFloracionCompleto.getEstacionFloracion().clave_unica_floracion : "0";
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<List<EstacionFloracionEstaciones>> futureDetails = executor.submit(() ->
                MainActivity
                        .myAppDB
                        .DaoEstacionFloracion()
                        .getEstacionesByPadre(claveUnica));


        try {
            List<EstacionFloracionEstaciones> details = futureDetails.get();
            et_cantidad_machos.setEnabled((details.size() == 0));

            List<EstacionesCompletas> estacionesCompletas = new ArrayList<>();

            for (EstacionFloracionEstaciones est : details){
                EstacionesCompletas estCompletas = new EstacionesCompletas();
                estCompletas.setEstaciones(est);

                List<EstacionFloracionDetalle> detalles = executor.submit(() ->
                        MainActivity
                                .myAppDB
                                .DaoEstacionFloracion()
                                .getDetalleByClaveEstacion(est.getClave_unica_floracion_estaciones())).get();

                estCompletas.setDetalles(detalles);

                estacionesCompletas.add(estCompletas);
            }
            EstacionFloracionEstacionesAdapter adapter = new EstacionFloracionEstacionesAdapter(
                    estacionesCompletas,
                    this::showAlertForConfirmarEliminar,
                    (v, update) -> {
                        if(et_cantidad_machos.getText().toString().isEmpty()){
                            Toasty.error(requireContext(), "Debes ingresar la cantidad de machos antes de agregar las muestras", Toast.LENGTH_LONG, true).show();
                            return;
                        }
                        int cantidadMachos = 0;
                        try{
                            cantidadMachos = Integer.parseInt(et_cantidad_machos.getText().toString());
                        }catch (NumberFormatException e){
                            cantidadMachos = 0;
                        }

                        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                        Fragment prev = requireActivity()
                                .getSupportFragmentManager()
                                .findFragmentByTag(Utilidades.DIALOG_TAG_ESTACIONES);
                        if (prev != null) {
                            ft.remove(prev);
                        }

                        DialogEstaciones dialogo = DialogEstaciones.newInstance( ((estacionFloracionCompleto != null) ? estacionFloracionCompleto.getEstacionFloracion() : null),update,cantidadMachos,  saved -> cargarListaEstaciones());
                        dialogo.show(ft, Utilidades.DIALOG_TAG_ESTACIONES);
                    },
                    requireActivity(),
                    (estacionFloracionCompleto != null) ? estacionFloracionCompleto.getEstacionFloracion().getEstado_documento() : 0
            );
            lista_muestra_estaciones.setLayoutManager(new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL, false));
            lista_muestra_estaciones.setHasFixedSize(true);
            lista_muestra_estaciones.setAdapter(adapter);

            actualizarResumen();

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
//        executor.shutdown();

    }



    @Override
    public void onStart() {
        super.onStart();
        cargarDatosPrevios();
        actualizarResumen();
    }

    private void cargarDatosPrevios(){

        if(anexoCompleto == null){
            Toasty.error(requireActivity(), "No se pudo obtener informacion del anexo", Toast.LENGTH_LONG, true).show();
            return;
        }

        tv_numero_anexo.setText(anexoCompleto.getAnexoContrato().getAnexo_contrato());
        tv_variedad.setText(anexoCompleto.getVariedad().getDesc_variedad());

        et_fecha.setText(Utilidades.fechaActualInvSinHora());

        if(estacionFloracionCompleto != null){
            et_fecha.setText(Utilidades.voltearFechaVista(estacionFloracionCompleto.getEstacionFloracion().getFecha()));
            et_cantidad_machos.setText(String.valueOf(estacionFloracionCompleto.getEstacionFloracion().getCantidad_machos()));

            if(estacionFloracionCompleto.getEstacionFloracion().getEstado_documento() == 1){
                et_fecha.setEnabled(false);
                et_cantidad_machos.setEnabled(false);
                btn_agregar_muestra.setEnabled(false);
                btn_guardar_estacion.setEnabled(false);
            }

        }


    }

    private void bind(View view) {

    lista_muestra_estaciones = view.findViewById(R.id.lista_muestra_estaciones);
    et_fecha = view.findViewById(R.id.et_fecha);
    et_cantidad_machos= view.findViewById(R.id.et_cantidad_machos);
    tv_numero_anexo= view.findViewById(R.id.tv_numero_anexo);
    tv_variedad= view.findViewById(R.id.tv_variedad);
        tv_resumen= view.findViewById(R.id.tv_resumen);
    btn_agregar_muestra= view.findViewById(R.id.btn_agregar_muestra);
    btn_guardar_estacion= view.findViewById(R.id.btn_guardar_estacion);
    btn_cancelar_estacion= view.findViewById(R.id.btn_cancelar_estacion);


    et_fecha.setOnFocusChangeListener((v, hasFocus) -> { if(hasFocus) Utilidades.levantarFecha(et_fecha, requireActivity()); });
    et_fecha.setOnClickListener(v -> Utilidades.levantarFecha(et_fecha, requireActivity()));

    btn_agregar_muestra.setOnClickListener(view1 -> {

        if(et_cantidad_machos.getText().toString().isEmpty()){
            Toasty.error(requireContext(), "Debes ingresar la cantidad de machos antes de agregar las muestras", Toast.LENGTH_LONG, true).show();
            return;
        }

        int cantidadMachos = getCantidadMachos();

        if(cantidadMachos == 0){
            Toasty.error(requireContext(), "La cantidad de machos no puede ser 0", Toast.LENGTH_LONG, true).show();
            return;
        }

        MainActivity.myAppDB.DaoEstacionFloracion().deleteDetalleSuelto();

        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        Fragment prev = requireActivity()
                .getSupportFragmentManager()
                .findFragmentByTag(Utilidades.DIALOG_TAG_ESTACIONES);
        if (prev != null) {
            ft.remove(prev);
        }

        DialogEstaciones dialogo = DialogEstaciones.newInstance( ((estacionFloracionCompleto != null) ? estacionFloracionCompleto.getEstacionFloracion() : null),null,cantidadMachos,  saved -> cargarListaEstaciones());
        dialogo.show(ft, Utilidades.DIALOG_TAG_ESTACIONES);
    });


    btn_guardar_estacion.setOnClickListener(view1 -> showAlertForConfirmarGuardar());
    btn_cancelar_estacion.setOnClickListener(view1 -> activity.onBackPressed());
    }

    private void onSave(int estado) {

        if(et_fecha.getText().toString().isEmpty() || et_cantidad_machos.getText().toString().isEmpty()){
            Toasty.error(requireActivity(), "Debe completar todos los campos antes de guardar", Toast.LENGTH_LONG, true).show();
            return;
        }


        int cantidadMachos = getCantidadMachos();

        if(cantidadMachos == 0) {
            Toasty.error(requireActivity(), "La cantidad de machos no puede ser 0", Toast.LENGTH_LONG, true).show();
            return;
        }

        String claveUnica = (estacionFloracionCompleto != null ) ? estacionFloracionCompleto.getEstacionFloracion().clave_unica_floracion : "0";
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<List<EstacionFloracionEstaciones>> futureDetails = executor.submit(() ->
                MainActivity
                        .myAppDB
                        .DaoEstacionFloracion()
                        .getEstacionesByPadre(claveUnica));

        try {
            List<EstacionFloracionEstaciones> details = futureDetails.get();

            if(details.size() == 0){
                Toasty.error(requireActivity(), "Debe tener al menos una estacion antes de guardar", Toast.LENGTH_LONG, true).show();
                return;
            }




            EstacionFloracion estacionFloracionInsert  = new EstacionFloracion();
            estacionFloracionInsert.setCantidad_machos(cantidadMachos);
            estacionFloracionInsert.setFecha(Utilidades.voltearFechaBD(et_fecha.getText().toString()));
            estacionFloracionInsert.setEstado_sincronizacion(0);

            estacionFloracionInsert.setEstado_documento(estado);


            if(estacionFloracionCompleto != null){
                estacionFloracionInsert.setFecha_crea(estacionFloracionCompleto.getEstacionFloracion().getFecha_crea());
                estacionFloracionInsert.setId_user_crea(estacionFloracionCompleto.getEstacionFloracion().getId_user_crea());

                estacionFloracionInsert.setFecha_mod(Utilidades.fechaActualConHora());
                estacionFloracionInsert.setId_user_mod(usuario.getId_usuario());
                estacionFloracionInsert.setId_estacion_floracion(estacionFloracionCompleto.getEstacionFloracion().getId_estacion_floracion());
                estacionFloracionInsert.setId_ac_floracion(estacionFloracionCompleto.getEstacionFloracion().getId_ac_floracion());
                estacionFloracionInsert.setClave_unica_floracion(estacionFloracionCompleto.getEstacionFloracion().clave_unica_floracion);

                executor.submit(() ->
                        MainActivity
                                .myAppDB
                                .DaoEstacionFloracion()
                                .updateEstacionCabecera(estacionFloracionInsert)).get();



            }else{
                estacionFloracionInsert.setFecha_crea(Utilidades.fechaActualConHora());
                estacionFloracionInsert.setId_user_crea(usuario.getId_usuario());
                estacionFloracionInsert.setId_ac_floracion(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));
                estacionFloracionInsert.setClave_unica_floracion(UUID.randomUUID().toString());

                executor.submit(() ->
                        MainActivity
                                .myAppDB
                                .DaoEstacionFloracion()
                                .insertEstacionCabecera(estacionFloracionInsert)).get();

            }



            executor.submit(() ->
                    MainActivity
                            .myAppDB
                            .DaoEstacionFloracion()
                            .updateClaveCabeceraEstaciones(estacionFloracionInsert.getClave_unica_floracion())).get();

            executor.submit(() ->
                    MainActivity
                            .myAppDB
                            .DaoEstacionFloracion()
                            .updateClaveCabeceraDetalle(estacionFloracionInsert.getClave_unica_floracion())).get();

            Toasty.success(requireActivity(), "CheckList guardado con exito", Toast.LENGTH_LONG, true).show();

            executor.shutdown();

            activity.cambiarFragment(
                    new FragmentListaEstacionFloracion(),
                    Utilidades.FRAGMENT_ESTACION_FLORACION,
                    R.anim.slide_in_left,R.anim.slide_out_left
            );



        } catch (ExecutionException | InterruptedException e) {
            Toasty.error(requireActivity(), "No se pudo guardar " + e.getMessage(), Toast.LENGTH_LONG, true).show();
            e.printStackTrace();
            executor.shutdown();
        }

    }


    private void showAlertForConfirmarEliminar(View views,  EstacionesCompletas detalle){
        View viewInfalted = LayoutInflater.from(requireActivity()).inflate(R.layout.alert_empty,null);
        final androidx.appcompat.app.AlertDialog builder = new androidx.appcompat.app.AlertDialog.Builder(requireActivity())
                .setView(viewInfalted)
                .setTitle("Esta seguro?")
                .setMessage("Esta a punto de eliminar  esta estacion.")
                .setPositiveButton(getResources().getString(R.string.eliminar), (dialogInterface, i) -> { })
                .setNegativeButton(getResources().getString(R.string.nav_cancel), (dialogInterface, i) -> { })
                .create();

        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
            Button c = builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE);
            b.setOnClickListener(view -> {

                ExecutorService executorsDelete = Executors.newSingleThreadExecutor();

                try {
                    executorsDelete.submit(() -> MainActivity.myAppDB.DaoEstacionFloracion().deleteDetalles(detalle.getDetalles())).get();
                    executorsDelete.submit(() -> MainActivity.myAppDB.DaoEstacionFloracion().deleteEstacion(detalle.getEstaciones())).get();
                    executorsDelete.shutdown();
                    Toasty.success(requireActivity(), "Eliminado con exito",
                            Toast.LENGTH_LONG, true).show();
                    cargarListaEstaciones();
                    builder.dismiss();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                    executorsDelete.shutdown();
                    Toasty.warning(requireActivity(), "Error al eliminar",
                            Toast.LENGTH_LONG, true).show();
                }



            });
            c.setOnClickListener(view -> builder.dismiss());
        });
        builder.setCancelable(false);
        builder.show();
    }


    private void actualizarResumen(){

        if(et_cantidad_machos.getText().toString().isEmpty()) return;

        String claveUnica = (estacionFloracionCompleto != null ) ? estacionFloracionCompleto.getEstacionFloracion().clave_unica_floracion : "0";
        ExecutorService executor = Executors.newSingleThreadExecutor();

        List<EstacionesCompletas> estacionesCompletasList = new ArrayList<>();

        Future<List<EstacionFloracionEstaciones>> futureDetails = executor.submit(() ->
                MainActivity
                        .myAppDB
                        .DaoEstacionFloracion()
                        .getEstacionesByPadre(claveUnica));

        try {
            List<EstacionFloracionEstaciones> details = futureDetails.get();

            for (EstacionFloracionEstaciones estaciones : details){

                EstacionesCompletas estacionesCompleta = new EstacionesCompletas();


                List<EstacionFloracionDetalle> detalles = executor.submit(() ->
                        MainActivity
                                .myAppDB
                                .DaoEstacionFloracion()
                                .getDetalleByClaveEstacion(estaciones.getClave_unica_floracion_estaciones())).get();

                estacionesCompleta.setEstaciones(estaciones);
                estacionesCompleta.setDetalles(detalles);

                estacionesCompletasList.add(estacionesCompleta);
            }

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }


        int cantidadMachosInput = getCantidadMachos();

        String resultado = Utilidades.calculaPromediosEstacionFloracion(estacionesCompletasList, cantidadMachosInput, "\n");
        tv_resumen.setText(resultado);

    }

    private int getCantidadMachos(){

        int cantidadMachosInput = 0;
        try {
            cantidadMachosInput = Integer.parseInt(et_cantidad_machos.getText().toString());
        }catch (NumberFormatException e){
            cantidadMachosInput = 0;
        }

        return cantidadMachosInput;

    }


    private void showAlertForConfirmarGuardar(){
        View viewInfalted = LayoutInflater.from(requireActivity()).inflate(R.layout.alert_guardar_estacion,null);

        RadioGroup grupo_radios_estado = viewInfalted.findViewById(R.id.grupo_radios_estado);
        RadioButton rbtn_guardar = viewInfalted.findViewById(R.id.rbtn_guardar);
        RadioButton rbtn_finalizar = viewInfalted.findViewById(R.id.rbtn_finalizar);


        if(estacionFloracionCompleto != null){
            rbtn_guardar.setChecked(estacionFloracionCompleto.getEstacionFloracion().getEstado_documento() == 0);
            rbtn_finalizar.setChecked(estacionFloracionCompleto.getEstacionFloracion().getEstado_documento() == 1);
        }

        final androidx.appcompat.app.AlertDialog builder = new androidx.appcompat.app.AlertDialog.Builder(requireActivity())
                .setView(viewInfalted)
                .setPositiveButton(getResources().getString(R.string.guardar), (dialogInterface, i) -> { })
                .setNegativeButton(getResources().getString(R.string.nav_cancel), (dialogInterface, i) -> { })
                .create();

        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
            Button c = builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE);
            b.setOnClickListener(view -> {

                if(!rbtn_guardar.isChecked() && !rbtn_finalizar.isChecked()){
                    Toasty.error(requireActivity(),
                            "Debes seleccionar un estado antes de guardar.",
                            Toast.LENGTH_LONG, true).show();
                    return;
                }
                int state = (rbtn_guardar.isChecked()) ? 0 : 1;
                onSave(state);
                builder.dismiss();
            });
            c.setOnClickListener(view -> builder.dismiss());
        });
        builder.setCancelable(false);
        builder.show();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (activity != null){
            activity.updateView(getResources().getString(R.string.app_name), "Estacion Floracion");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
