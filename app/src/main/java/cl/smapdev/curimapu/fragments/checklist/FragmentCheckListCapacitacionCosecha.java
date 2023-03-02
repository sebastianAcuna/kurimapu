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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.AsistentesCapacitacionAdapter;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembra;
import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembraDetalle;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.dialogos.DialogAsistentes;
import cl.smapdev.curimapu.fragments.dialogos.DialogFirma;
import es.dmoral.toasty.Toasty;


public class FragmentCheckListCapacitacionCosecha extends Fragment {


    private CheckListCapacitacionSiembra capSiembra;
    private MainActivity activity;
    private SharedPreferences prefs;
    private AnexoCompleto anexoCompleto;
    private Config config;
    private Usuario usuario;


//    cabecera
    private TextView tv_numero_anexo;
    private TextView tv_variedad;

    private LinearLayout contenedor_charla;

    private ConstraintLayout contenedor_vista;
    private TextView tv_agricultor, tv_potrero, tv_rch, tv_sag_ogm, tv_sag_idase;
    private TextView tv_condicion_semilla,tv_supervisor_curimapu;

    private Button btn_charla_cinco;
    private Button btn_guardar_cl_siembra;
    private Button btn_cancelar_cl_siembra;

    private ImageView btn_oculta_cabecera;

    private TextView et_impartidor;

    private Button btn_agregar_usuario;
    private RecyclerView lista_capacitacion_siembra;


    public void setCapSiembra(CheckListCapacitacionSiembra capSiembra) {
        this.capSiembra = capSiembra;
    }

    public static FragmentCheckListCapacitacionCosecha newInstance(CheckListCapacitacionSiembra capSiembra ){
        FragmentCheckListCapacitacionCosecha fs = new FragmentCheckListCapacitacionCosecha();
        fs.setCapSiembra( capSiembra );
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
        return inflater.inflate(R.layout.fragment_checklist_cap_cosecha, container, false);
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


    public void cargarListaAsistentes(){

        String claveUnica = (this.capSiembra != null ) ? this.capSiembra.getClave_unica() : "0";

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<List<CheckListCapacitacionSiembraDetalle>> futureDetails = executor.submit(() ->
                MainActivity
                        .myAppDB
                        .DaoCheckListCapSiembra()
                        .getCapSiembraDetallesByPadre(claveUnica, Utilidades.TIPO_DOCUMENTO_CAPACITACION_COSECHA));


        try {
            List<CheckListCapacitacionSiembraDetalle> details = futureDetails.get();

            // eliminar asistente
            AsistentesCapacitacionAdapter adapter = new AsistentesCapacitacionAdapter(details,
                    checkList -> {
                        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                        Fragment prev = requireActivity()
                                .getSupportFragmentManager()
                                .findFragmentByTag(Utilidades.DIALOG_TAG_FIRMA_CAPACITACION_COSECHA);
                        if (prev != null) {
                            ft.remove(prev);
                        }
                        //levantar firma
                        String etRA = checkList.getRut_cl_cap_siembra_detalle()
                                .trim()
                                .toLowerCase(Locale.ROOT)
                                .replaceAll(" ", "_")
                                .replaceAll("ñ", "n")
                                .replaceAll("á", "a")
                                .replaceAll("é", "e")
                                .replaceAll("í", "i")
                                .replaceAll("ó", "o")
                                .replaceAll("ú", "u")
                                +"_"+
                                Utilidades.fechaActualConHora()
                                        .replaceAll(" " ,"")
                                        .replaceAll("-" ,"")
                                        .replaceAll(":", "_")+".png";

                        DialogFirma dialogo = DialogFirma.newInstance(
                                Utilidades.TIPO_DOCUMENTO_CAPACITACION_COSECHA,
                                etRA,
                                Utilidades.DIALOG_TAG_FIRMA_CAPACITACION_COSECHA,
                                (isSaved, savePath) -> {
                                    if(isSaved){

                                        checkList.setFirma_cl_cap_siembra_detalle(savePath);
                                        checkList.setEstado_sincronizacion_detalle(0);
                                        ExecutorService executor2 = Executors.newSingleThreadExecutor();
                                        try {
                                            executor2.submit(() -> MainActivity
                                                    .myAppDB.DaoCheckListCapSiembra()
                                                    .updateDetalle(checkList)).get();
                                            executor2.shutdown();
                                        } catch (ExecutionException | InterruptedException e) {
                                            executor2.shutdown();
                                            e.printStackTrace();
                                        }

                                    }
                                }
                        );

                        dialogo.show(ft, Utilidades.DIALOG_TAG_FIRMA_CAPACITACION_COSECHA);

                    }, this::showAlertForConfirmarEliminar
            );

            lista_capacitacion_siembra.setLayoutManager(new LinearLayoutManager(getContext(),
                    LinearLayoutManager.VERTICAL, false));
            lista_capacitacion_siembra.setHasFixedSize(true);
            lista_capacitacion_siembra.setAdapter(adapter);


        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();

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
        tv_agricultor.setText(anexoCompleto.getAgricultor().getNombre_agricultor());

        tv_potrero.setText(anexoCompleto.getLotes().getNombre_lote());
        tv_rch.setText(anexoCompleto.getAnexoContrato().getRch());

        tv_sag_ogm.setText(anexoCompleto.getAnexoContrato().getSag_register_number());
        tv_sag_idase.setText(anexoCompleto.getAnexoContrato().getSag_register_idase());
        tv_condicion_semilla.setText(anexoCompleto.getAnexoContrato().getCondicion());
        tv_supervisor_curimapu.setText(usuario.getNombre()+ " " +usuario.getApellido_p());


        if(capSiembra != null){
            et_impartidor.setText(capSiembra.getImpartidor());
        }


    }

    private void bind(View view) {

        tv_numero_anexo = view.findViewById(R.id.tv_numero_anexo);
        tv_variedad = view.findViewById(R.id.tv_variedad);
        contenedor_vista = view.findViewById(R.id.contenedor_vista);
        tv_agricultor = view.findViewById(R.id.tv_agricultor);
        tv_potrero = view.findViewById(R.id.tv_potrero);
        tv_rch = view.findViewById(R.id.tv_rch);
        tv_sag_ogm = view.findViewById(R.id.tv_sag_ogm);
        tv_sag_idase = view.findViewById(R.id.tv_sag_idase);
        tv_condicion_semilla = view.findViewById(R.id.tv_condicion_semilla);
        tv_supervisor_curimapu = view.findViewById(R.id.tv_supervisor_curimapu);
        btn_charla_cinco = view.findViewById(R.id.btn_charla_cinco);
        btn_oculta_cabecera = view.findViewById(R.id.btn_oculta_cabecera);
        et_impartidor = view.findViewById(R.id.et_impartidor);
        btn_agregar_usuario = view.findViewById(R.id.btn_agregar_usuario);
        lista_capacitacion_siembra = view.findViewById(R.id.lista_capacitacion_siembra);
        btn_guardar_cl_siembra = view.findViewById(R.id.btn_guardar_cl_siembra);
        btn_cancelar_cl_siembra = view.findViewById(R.id.btn_cancelar_cl_siembra);
        contenedor_charla = view.findViewById(R.id.contenedor_charla);





        btn_charla_cinco.setOnClickListener(view1 -> {
            contenedor_charla.setVisibility(contenedor_charla.getVisibility()==View.VISIBLE ? View.GONE : View.VISIBLE);
            btn_charla_cinco.setText(contenedor_charla.getVisibility()==View.VISIBLE ? "OCULTAR CHARLA 5 MIN." : "LEER CHARLA 5 MIN.");
        });

        btn_oculta_cabecera.setOnClickListener(view1 -> {
            contenedor_vista.setVisibility((contenedor_vista.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
            btn_oculta_cabecera.setImageDrawable((contenedor_vista.getVisibility() == View.VISIBLE) ? getResources().getDrawable(R.drawable.ic_expand_up) : getResources().getDrawable(R.drawable.ic_expand_down));
        });


        btn_agregar_usuario.setOnClickListener(view1 -> {

            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentByTag(Utilidades.DIALOG_TAG_CAPACITACION_COSECHA);
            if (prev != null) {
                ft.remove(prev);
            }


            DialogAsistentes dialogo = DialogAsistentes.newInstance(saved -> cargarListaAsistentes(), Utilidades.TIPO_DOCUMENTO_CAPACITACION_COSECHA);
            dialogo.show(ft, Utilidades.DIALOG_TAG_CAPACITACION_COSECHA);
        });


        btn_guardar_cl_siembra.setOnClickListener(view1 -> {
            String claveUnica = (this.capSiembra != null ) ? this.capSiembra.getClave_unica() : "0";

            ExecutorService executor = Executors.newSingleThreadExecutor();

            Future<List<CheckListCapacitacionSiembraDetalle>> futureDetails = executor.submit(() ->
                    MainActivity
                            .myAppDB
                            .DaoCheckListCapSiembra()
                            .getCapSiembraDetallesByPadre(claveUnica, Utilidades.TIPO_DOCUMENTO_CAPACITACION_COSECHA));


            List<CheckListCapacitacionSiembraDetalle> details = null;
            try {
                details = futureDetails.get();

                if(details.size() <= 0){
                    executor.shutdown();
                    Toasty.warning(requireActivity(), "Debes agregar a lo menos un asistente",
                            Toast.LENGTH_LONG, true).show();
                    return;
                }

                if(et_impartidor.getText().toString().isEmpty()){
                    executor.shutdown();
                    Toasty.warning(requireActivity(), "Debes agregar quien " +
                                    "impartio la capacitacion",
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

    private boolean onSave(int state, String apellido) {

        String claveUnica = (this.capSiembra != null ) ? this.capSiembra.getClave_unica() : "0";

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<List<CheckListCapacitacionSiembraDetalle>> futureDetails = executor.submit(() ->
                MainActivity
                        .myAppDB
                        .DaoCheckListCapSiembra()
                        .getCapSiembraDetallesByPadre(claveUnica, Utilidades.TIPO_DOCUMENTO_CAPACITACION_COSECHA));


        try {
            List<CheckListCapacitacionSiembraDetalle> details = futureDetails.get();


            if(details.size() <= 0){
                executor.shutdown();
                Toasty.warning(requireActivity(), "Debes agregar a lo menos un asistente",
                        Toast.LENGTH_LONG, true).show();
                return false;
            }

            if(et_impartidor.getText().toString().isEmpty()){
                executor.shutdown();
                Toasty.warning(requireActivity(), "Debes agregar quien " +
                                "impartio la capacitacion",
                        Toast.LENGTH_LONG, true).show();
                return false;
            }


            CheckListCapacitacionSiembra cabecera = new CheckListCapacitacionSiembra();

            cabecera.setApellido_checklist(apellido);
            cabecera.setEstado_documento(state);
            cabecera.setEstado_sincronizacion(0);
            cabecera.setId_ac_cl_cap_siembra(Integer.parseInt(
                    anexoCompleto.getAnexoContrato().getId_anexo_contrato()));
            cabecera.setFecha_hora_tx(Utilidades.fechaActualConHora());
            cabecera.setId_usuario(usuario.getId_usuario());
            cabecera.setTipo_capacitacion(Utilidades.TIPO_DOCUMENTO_CAPACITACION_COSECHA);

            cabecera.setImpartidor(et_impartidor.getText().toString());

            if( capSiembra == null){
                String claveUnicaI = config.getId()
                        +""+config.getId_usuario()
                        +""+Utilidades.fechaActualConHora()
                        .replaceAll(" ", "")
                        .replaceAll("-", "")
                        .replaceAll(":", "");

                cabecera.setClave_unica( claveUnicaI );
                cabecera.setId_usuario(usuario.getId_usuario());


                executor.submit(() ->
                        MainActivity.myAppDB.DaoCheckListCapSiembra()
                                .insertCapacitacionSiembra(cabecera)
                ).get();

            }else{
                cabecera.setClave_unica( capSiembra.getClave_unica() );
                cabecera.setFecha_hora_mod(Utilidades.fechaActualConHora());

                cabecera.setId_usuario_mod(usuario.getId_usuario());
                cabecera.setId_cl_cap_siembra(capSiembra.getId_cl_cap_siembra());

                executor.submit(() ->
                        MainActivity.myAppDB.DaoCheckListCapSiembra()
                                .updateCapacitacionSiembra(cabecera)).get();
            }


            executor.submit(() -> MainActivity.myAppDB.DaoCheckListCapSiembra()
                    .updateCapacitacionSiembraDetalleConCero(cabecera.getClave_unica(), Utilidades.TIPO_DOCUMENTO_CAPACITACION_COSECHA))
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


    private void showAlertForConfirmarEliminar(CheckListCapacitacionSiembraDetalle detalle){
        View viewInfalted = LayoutInflater.from(requireActivity()).inflate(R.layout.alert_empty,null);
        final androidx.appcompat.app.AlertDialog builder = new androidx.appcompat.app.AlertDialog.Builder(requireActivity())
                .setView(viewInfalted)
                .setTitle("Esta seguro?")
                .setMessage("Esta a punto de eliminar  a "+detalle.getNombre_cl_cap_siembra_detalle()+ "de la lista de asistentes")
                .setPositiveButton(getResources().getString(R.string.eliminar), (dialogInterface, i) -> { })
                .setNegativeButton(getResources().getString(R.string.nav_cancel), (dialogInterface, i) -> { })
                .create();

        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
            Button c = builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE);
            b.setOnClickListener(view -> {

                    ExecutorService executorsDelete = Executors.newSingleThreadExecutor();

                try {
                    executorsDelete.submit(() -> MainActivity.myAppDB.DaoCheckListCapSiembra()
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


    private void showAlertForConfirmarGuardar(){
        View viewInfalted = LayoutInflater.from(requireActivity()).inflate(R.layout.alert_guardar_checklist,null);

        RadioGroup grupo_radios_estado = viewInfalted.findViewById(R.id.grupo_radios_estado);
        RadioButton rbtn_activo = viewInfalted.findViewById(R.id.rbtn_activo);
        RadioButton rbtn_pendiente = viewInfalted.findViewById(R.id.rbtn_pendiente);
        EditText et_apellido = viewInfalted.findViewById(R.id.et_apellido);


        if(capSiembra != null){

            et_apellido.setText(capSiembra.getApellido_checklist());

            if(capSiembra.getEstado_documento() > 0){
                rbtn_activo.setChecked(capSiembra.getEstado_documento() == 1);
                rbtn_pendiente.setChecked(capSiembra.getEstado_documento() == 2);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (activity != null){
            activity.updateView(getResources().getString(R.string.app_name), "CHECKLIST CAPACITACION COSECHA");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
