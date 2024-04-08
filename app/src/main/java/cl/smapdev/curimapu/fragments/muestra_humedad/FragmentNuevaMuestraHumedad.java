package cl.smapdev.curimapu.fragments.muestra_humedad;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import cl.smapdev.curimapu.clases.tablas.MuestraHumedad;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.dialogos.DialogEstaciones;
import cl.smapdev.curimapu.fragments.estacion_floracion.FragmentListaEstacionFloracion;
import es.dmoral.toasty.Toasty;
import kotlin.text.Regex;

public class FragmentNuevaMuestraHumedad extends Fragment {


    private MuestraHumedad muestraHumedad;
    private MainActivity activity;
    private SharedPreferences prefs;
    private AnexoCompleto anexoCompleto;
    private Config config;
    private Usuario usuario;

    private EditText et_fecha;
    private EditText et_muestra_humedad;

    private Button btn_guardar_muestra;
    private Button btn_cancelar_muestra;

    //    cabecera
    private TextView tv_numero_anexo;
    private TextView tv_variedad;
    private Pattern pt = Pattern.compile("^\\d*\\.?\\d{0,2}$", Pattern.CASE_INSENSITIVE);

    public void setMuestraHumedad(MuestraHumedad muestraHumedad) {
        this.muestraHumedad = muestraHumedad;
    }

    public static FragmentNuevaMuestraHumedad newInstance(MuestraHumedad muestraHumedad ){
        FragmentNuevaMuestraHumedad fs = new FragmentNuevaMuestraHumedad();
        fs.setMuestraHumedad( muestraHumedad );
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
        return inflater.inflate(R.layout.fragment_nueva_muestra_humedad, container, false);
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

        et_fecha.setText(Utilidades.fechaActualInvSinHora());

        if(muestraHumedad != null){
            et_fecha.setText(Utilidades.voltearFechaVista(muestraHumedad.getFecha_muestra()));
            et_muestra_humedad.setText(muestraHumedad.getMuestra());

            if(muestraHumedad.getEstado_documento_muestra() == 1){
                et_fecha.setEnabled(false);
                et_muestra_humedad.setEnabled(false);
                btn_guardar_muestra.setEnabled(false);
            }

        }
    }

    private void bind(View view) {
    et_fecha = view.findViewById(R.id.et_fecha);
    et_muestra_humedad= view.findViewById(R.id.et_cantidad_machos);
    tv_numero_anexo= view.findViewById(R.id.tv_numero_anexo);
    tv_variedad= view.findViewById(R.id.tv_variedad);
    btn_guardar_muestra= view.findViewById(R.id.btn_guardar_estacion);
    btn_cancelar_muestra= view.findViewById(R.id.btn_cancelar_estacion);


    et_fecha.setOnFocusChangeListener((v, hasFocus) -> { if(hasFocus) Utilidades.levantarFecha(et_fecha, requireActivity()); });
    et_fecha.setOnClickListener(v -> Utilidades.levantarFecha(et_fecha, requireActivity()));

    btn_guardar_muestra.setOnClickListener(view1 -> showAlertForConfirmarGuardar());
    btn_cancelar_muestra.setOnClickListener(view1 -> activity.onBackPressed());



        et_muestra_humedad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                Matcher match = pt.matcher(s.toString());

                if(!match.matches()){
                    et_muestra_humedad.setError("Debe ingresar un numero valido con maximo 2 decimales");
                }
            }
        });
    }

    private void onSave(int estado) {

        if(et_fecha.getText().toString().isEmpty() || et_muestra_humedad.getText().toString().isEmpty()){
            Toasty.error(requireActivity(), "Debe completar todos los campos antes de guardar", Toast.LENGTH_LONG, true).show();
            return;
        }

        Matcher match = pt.matcher(et_muestra_humedad.getText().toString());

        if(!match.matches()){
            Toasty.error(requireActivity(), "Debe ingresar un numero valido con maximo 2 decimales", Toast.LENGTH_LONG, true).show();
            return;
        }

        if(Double.parseDouble(et_muestra_humedad.getText().toString())>999999999.99){
            Toasty.error(requireActivity(), "No puede ser mayor al numero 999999999.99", Toast.LENGTH_LONG, true).show();
            return;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {

            MuestraHumedad muestraHumedadInsert  = new MuestraHumedad();
            muestraHumedadInsert.setMuestra(et_muestra_humedad.getText().toString());
            muestraHumedadInsert.setFecha_muestra(Utilidades.voltearFechaBD(et_fecha.getText().toString()));
            muestraHumedadInsert.setEstado_sincronizacion_muestrao(0);
            muestraHumedadInsert.setEstado_documento_muestra(estado);

            if(muestraHumedad != null){
                muestraHumedadInsert.setFecha_crea(muestraHumedad.getFecha_crea());
                muestraHumedadInsert.setId_user_crea(muestraHumedad.getId_user_crea());
                muestraHumedadInsert.setId_muestra_humedad(muestraHumedad.getId_muestra_humedad());
                muestraHumedadInsert.setFecha_mod(Utilidades.fechaActualConHora());
                muestraHumedadInsert.setId_user_mod(usuario.getId_usuario());
                muestraHumedadInsert.setId_ac_muestra_humedad(muestraHumedad.getId_ac_muestra_humedad());
                muestraHumedadInsert.setClave_unica_muestra(muestraHumedad.getClave_unica_muestra());

                executor.submit(() ->
                        MainActivity
                                .myAppDB
                                .DaoMuestraHumedad()
                                .updateMuestraHumedad(muestraHumedadInsert)).get();

            }else{
                muestraHumedadInsert.setFecha_crea(Utilidades.fechaActualConHora());
                muestraHumedadInsert.setId_user_crea(usuario.getId_usuario());
                muestraHumedadInsert.setId_ac_muestra_humedad(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));
                muestraHumedadInsert.setClave_unica_muestra(UUID.randomUUID().toString());

                executor.submit(() ->
                        MainActivity
                                .myAppDB
                                .DaoMuestraHumedad()
                                .insertMuestraHumedad(muestraHumedadInsert)).get();

            }

            Toasty.success(requireActivity(), "Muestreo guardado con exito", Toast.LENGTH_LONG, true).show();

            executor.shutdown();

            activity.cambiarFragment(
                    new FragmentListaMuestraHumedad(),
                    Utilidades.FRAGMENT_MUESTRA_HUMEDAD,
                    R.anim.slide_in_left,R.anim.slide_out_left
            );



        } catch (ExecutionException | InterruptedException e) {
            Toasty.error(requireActivity(), "No se pudo guardar " + e.getMessage(), Toast.LENGTH_LONG, true).show();
            e.printStackTrace();
            executor.shutdown();
        }

    }


    private void showAlertForConfirmarGuardar(){
        View viewInfalted = LayoutInflater.from(requireActivity()).inflate(R.layout.alert_guardar_estacion,null);

        RadioGroup grupo_radios_estado = viewInfalted.findViewById(R.id.grupo_radios_estado);
        RadioButton rbtn_guardar = viewInfalted.findViewById(R.id.rbtn_guardar);
        RadioButton rbtn_finalizar = viewInfalted.findViewById(R.id.rbtn_finalizar);


        if(muestraHumedad != null){
            rbtn_guardar.setChecked(muestraHumedad.getEstado_documento_muestra() == 0);
            rbtn_finalizar.setChecked(muestraHumedad.getEstado_documento_muestra() == 1);
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
            activity.updateView(getResources().getString(R.string.app_name), "Muestra Humedad");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
