package cl.smapdev.curimapu.fragments.checklist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.AsistentesCapacitacionAdapter;
import cl.smapdev.curimapu.clases.adapters.CamionesLimpiosAdapter;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembra;
import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembraDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListCosecha;
import cl.smapdev.curimapu.clases.tablas.CheckListLimpiezaCamiones;
import cl.smapdev.curimapu.clases.tablas.ChecklistLimpiezaCamionesDetalle;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.dialogos.DialogAsistentes;
import cl.smapdev.curimapu.fragments.dialogos.DialogFirma;
import cl.smapdev.curimapu.fragments.dialogos.DialogLimpiadorCamion;
import es.dmoral.toasty.Toasty;

public class FragmentChecklistLimpiezaCamiones extends Fragment {

    private MainActivity activity;
    private SharedPreferences prefs;

    private AnexoCompleto anexoCompleto;
    private Usuario usuario;
    private Config config;

    private TextView tv_numero_anexo;
    private TextView tv_variedad;

    private RecyclerView lista_limpieza_camiones;

    private Button btn_guardar_cl_siembra;
    private Button btn_cancelar_cl_siembra;
    private Button btn_agregar_usuario;

    private CheckListLimpiezaCamiones checkListLimpiezaCamiones;


    public void setCheckListLimpiezaCamiones(CheckListLimpiezaCamiones checkListLimpiezaCamiones) {
        this.checkListLimpiezaCamiones = checkListLimpiezaCamiones;
    }

    public static FragmentChecklistLimpiezaCamiones newInstance(CheckListLimpiezaCamiones checkListLimpiezaCamiones ){

        FragmentChecklistLimpiezaCamiones fs = new FragmentChecklistLimpiezaCamiones();

        fs.setCheckListLimpiezaCamiones( checkListLimpiezaCamiones );

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
        return inflater.inflate(R.layout.fragment_checklist_limpieza_camiones, container, false);
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

        cargarListaAsistentes();
    }


    @Override
    public void onStart() {
        super.onStart();
        cargarDatosPrevios();
    }

    private void cargarDatosPrevios(){

        if(anexoCompleto == null){
            Toasty.error(requireActivity(), "No se pudo obtener informacion del anexo", Toast.LENGTH_LONG, true).show();
            return;
        }

        tv_numero_anexo.setText(anexoCompleto.getAnexoContrato().getAnexo_contrato());
        tv_variedad.setText(anexoCompleto.getVariedad().getDesc_variedad());


    }

    public void cargarListaAsistentes(){

        String claveUnica = (this.checkListLimpiezaCamiones != null ) ? this.checkListLimpiezaCamiones.getClave_unica() : "0";

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<List<ChecklistLimpiezaCamionesDetalle>> futureDetails = executor.submit(() ->
                MainActivity
                        .myAppDB
                        .DaoCheckListLimpiezaCamiones()
                        .getLimpiezaCamionesDetallesByPadre(claveUnica));


        try {
            List<ChecklistLimpiezaCamionesDetalle> details = futureDetails.get();


            if(details.size() >= 12){
                btn_agregar_usuario.setEnabled(false);
            }
            // eliminar asistente
            CamionesLimpiosAdapter adapter = new CamionesLimpiosAdapter(details,
                    checkList -> {
                        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                        Fragment prev = requireActivity()
                                .getSupportFragmentManager()
                                .findFragmentByTag(Utilidades.DIALOG_TAG_LIMPIEZA_CAMIONES);
                        if (prev != null) {
                            ft.remove(prev);
                        }
                        //levantar firma
                        String etRA = UUID.randomUUID().toString() +".png";

                        DialogFirma dialogo = DialogFirma.newInstance(
                                Utilidades.TIPO_DOCUMENTO_CHECKLIST_LIMPIEZA_CAMIONES,
                                etRA,
                                Utilidades.DIALOG_TAG_FIRMA_LIMPIEZA_CAMIONES,
                                (isSaved, savePath) -> {
                                    if(isSaved){
                                        checkList.setFirma_cl_limpieza_camiones_detalle(savePath);
                                        checkList.setEstado_sincronizacion_detalle(0);
                                        ExecutorService executor2 = Executors.newSingleThreadExecutor();
                                        try {
                                            executor2.submit(() -> MainActivity
                                                    .myAppDB.DaoCheckListLimpiezaCamiones()
                                                    .updateDetalle(checkList)).get();
                                            executor2.shutdown();
                                        } catch (ExecutionException | InterruptedException e) {
                                            executor2.shutdown();
                                            e.printStackTrace();
                                        }

                                    }
                                }
                        );

                        dialogo.show(ft, Utilidades.DIALOG_TAG_FIRMA_CAPACITACION_SIEMBRA);

                    }, this::showAlertForConfirmarEliminar
            );

            lista_limpieza_camiones.setLayoutManager(new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL, false));
            lista_limpieza_camiones.setHasFixedSize(true);
            lista_limpieza_camiones.setAdapter(adapter);


        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();

    }


    private void showAlertForConfirmarEliminar(ChecklistLimpiezaCamionesDetalle detalle){
        View viewInfalted = LayoutInflater.from(requireActivity()).inflate(R.layout.alert_empty,null);
        final androidx.appcompat.app.AlertDialog builder = new androidx.appcompat.app.AlertDialog.Builder(requireActivity())
                .setView(viewInfalted)
                .setTitle("Esta seguro?")
                .setMessage("Esta a punto de eliminar  a "+detalle.getPatente_camion_limpieza_camiones()+ "de la lista de limpieza")
                .setPositiveButton(getResources().getString(R.string.eliminar), (dialogInterface, i) -> { })
                .setNegativeButton(getResources().getString(R.string.nav_cancel), (dialogInterface, i) -> { })
                .create();

        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
            Button c = builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE);
            b.setOnClickListener(view -> {

                ExecutorService executorsDelete = Executors.newSingleThreadExecutor();

                try {
                    executorsDelete.submit(() -> MainActivity.myAppDB.DaoCheckListLimpiezaCamiones()
                            .deleteDetalle(detalle)).get();
                    executorsDelete.shutdown();
                    Toasty.success(requireActivity(), "Eliminado con exito",
                            Toast.LENGTH_LONG, true).show();
                    cargarListaAsistentes();
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

    public void bind(View view){
        tv_numero_anexo = view.findViewById(R.id.tv_numero_anexo);
        tv_variedad = view.findViewById(R.id.tv_variedad);
        btn_agregar_usuario = view.findViewById(R.id.btn_agregar_usuario);
        lista_limpieza_camiones = view.findViewById(R.id.lista_limpieza_camiones);
        btn_guardar_cl_siembra = view.findViewById(R.id.btn_guardar_cl_siembra);
        btn_cancelar_cl_siembra = view.findViewById(R.id.btn_cancelar_cl_siembra);

        btn_agregar_usuario.setOnClickListener(view1 -> {

            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentByTag(Utilidades.DIALOG_TAG_LIMPIEZA_CAMIONES);
            if (prev != null) {
                ft.remove(prev);
            }
//
//
            DialogLimpiadorCamion dialogo = DialogLimpiadorCamion.newInstance(saved -> cargarListaAsistentes(), Utilidades.TIPO_DOCUMENTO_CHECKLIST_LIMPIEZA_CAMIONES);
            dialogo.show(ft, Utilidades.DIALOG_TAG_LIMPIEZA_CAMIONES);
        });


        btn_guardar_cl_siembra.setOnClickListener(view1 -> {
            String claveUnica = (this.checkListLimpiezaCamiones != null ) ? this.checkListLimpiezaCamiones.getClave_unica() : "0";

            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<List<ChecklistLimpiezaCamionesDetalle>> futureDetails = executor.submit(() ->
                    MainActivity
                            .myAppDB
                            .DaoCheckListLimpiezaCamiones()
                            .getLimpiezaCamionesDetallesByPadre(claveUnica));
            List<ChecklistLimpiezaCamionesDetalle> details = null;
            try {
                details = futureDetails.get();
                if(details.size() <= 0){
                    executor.shutdown();
                    Toasty.warning(requireActivity(), "Debes agregar a lo menos un detalle",
                            Toast.LENGTH_LONG, true).show();
                    return;
                }

                showAlertForConfirmarGuardar();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                executor.shutdown();
            }

        });
        btn_cancelar_cl_siembra.setOnClickListener(view1 -> activity.onBackPressed());
    }


    private void showAlertForConfirmarGuardar(){
        View viewInfalted = LayoutInflater.from(requireActivity()).inflate(R.layout.alert_guardar_checklist,null);

        RadioGroup grupo_radios_estado = viewInfalted.findViewById(R.id.grupo_radios_estado);
        RadioButton rbtn_activo = viewInfalted.findViewById(R.id.rbtn_activo);
        RadioButton rbtn_pendiente = viewInfalted.findViewById(R.id.rbtn_pendiente);
        EditText et_apellido = viewInfalted.findViewById(R.id.et_apellido);


        if(checkListLimpiezaCamiones != null){

            et_apellido.setText(checkListLimpiezaCamiones.getApellido_checklist());

            if(checkListLimpiezaCamiones.getEstado_documento() > 0){
                rbtn_activo.setChecked(checkListLimpiezaCamiones.getEstado_documento() == 1);
                rbtn_pendiente.setChecked(checkListLimpiezaCamiones.getEstado_documento() == 2);
            }

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

                if((!rbtn_activo.isChecked() && !rbtn_pendiente.isChecked()) || et_apellido.getText().toString().isEmpty() ){
                    Toasty.error(requireActivity(),
                            "Debes seleccionar un estado e ingresar una descripcion",
                            Toast.LENGTH_LONG, true).show();
                    return;
                }
                int state = (rbtn_activo.isChecked()) ? 1 : 2;
                boolean isSaved = onSave(state, et_apellido.getText().toString());
                if(isSaved) {
                    builder.dismiss();
                    activity.onBackPressed();
                }


            });
            c.setOnClickListener(view -> builder.dismiss());
        });
        builder.setCancelable(false);
        builder.show();
    }


    private boolean onSave(int state, String apellido) {

        String claveUnica = (this.checkListLimpiezaCamiones != null ) ? this.checkListLimpiezaCamiones.getClave_unica() : "0";

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<List<ChecklistLimpiezaCamionesDetalle>> futureDetails = executor.submit(() ->
                MainActivity
                        .myAppDB
                        .DaoCheckListLimpiezaCamiones()
                        .getLimpiezaCamionesDetallesByPadre(claveUnica));


        try {
            List<ChecklistLimpiezaCamionesDetalle> details = futureDetails.get();


            if(details.size() <= 0){
                executor.shutdown();
                Toasty.warning(requireActivity(), "Debes agregar a lo menos un detalle",
                        Toast.LENGTH_LONG, true).show();
                return false;
            }


            CheckListLimpiezaCamiones cabecera = new CheckListLimpiezaCamiones();

            cabecera.setApellido_checklist(apellido);
            cabecera.setEstado_documento(state);
            cabecera.setEstado_sincronizacion(0);
            cabecera.setId_ac_cl_limpieza_camiones(Integer.parseInt(
                    anexoCompleto.getAnexoContrato().getId_anexo_contrato()));
            cabecera.setFecha_hora_tx(Utilidades.fechaActualConHora());
            cabecera.setId_usuario(usuario.getId_usuario());


            if( checkListLimpiezaCamiones == null){
                String claveUnicaI = UUID.randomUUID().toString();

                cabecera.setClave_unica( claveUnicaI );
                cabecera.setId_usuario(usuario.getId_usuario());


                executor.submit(() ->
                        MainActivity.myAppDB.DaoCheckListLimpiezaCamiones()
                                .insertLimpiezaCamiones(cabecera)
                ).get();

            }else{
                cabecera.setClave_unica( checkListLimpiezaCamiones.getClave_unica() );
                cabecera.setFecha_hora_mod(Utilidades.fechaActualConHora());

                cabecera.setId_usuario_mod(usuario.getId_usuario());
                cabecera.setId_cl_limpieza_camiones(checkListLimpiezaCamiones.getId_cl_limpieza_camiones());

                executor.submit(() ->
                        MainActivity.myAppDB.DaoCheckListLimpiezaCamiones()
                                .updateLimpiezaCamiones(cabecera)).get();
            }


            executor.submit(() -> MainActivity.myAppDB.DaoCheckListLimpiezaCamiones()
                            .updateLimpiezaCamionesDetalleConCero(cabecera.getClave_unica()))
                    .get();


            Toasty.success(requireActivity(),
                    "CheckList guardado con exito",
                    Toast.LENGTH_LONG, true).show();
            executor.shutdown();
            return true;

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            executor.shutdown();
            return false;
        }



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (activity != null){
            activity.updateView(getResources().getString(R.string.app_name), "CHECKLIST LIMPIEZA CAMIONES");
        }
    }
}
