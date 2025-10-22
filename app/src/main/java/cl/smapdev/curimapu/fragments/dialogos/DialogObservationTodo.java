package cl.smapdev.curimapu.fragments.dialogos;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
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
import cl.smapdev.curimapu.clases.adapters.RecomendacionesAdapter;
import cl.smapdev.curimapu.clases.modelo.EvaluacionAnterior;
import cl.smapdev.curimapu.clases.relaciones.VisitasCompletas;
import cl.smapdev.curimapu.clases.tablas.AnexoContrato;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Evaluaciones;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import es.dmoral.toasty.Toasty;

public class DialogObservationTodo extends DialogFragment {

    private AnexoContrato anexoContrato;
    private EvaluacionAnterior evaluacionAnterior = null;
    private VisitasCompletas visitaAnterior = null;
    private VisitasCompletas visitaActual = null;
    private OnSaveRating onSaveRating;

    // evaluaciones
    private Button btn_guardar_evaluacion, btn_posponer_evaluacion;
    private Button btn_evaluacion, btn_recomendaciones;
    private ConstraintLayout cl_evaluacion, cl_recomendacion;

    private TextView tv_fecha_rankear;
    private TextView tv_recom_rankear;
    private RatingBar ratingBar;
    private EditText et_comentario;

    // recomendaciones
    private EditText et_nueva_recom, fecha_plazo;
    private Button btn_cerrar_recomendaciones, btn_add_recom, btn_no_aplica;
    private RecyclerView rv_recom_pendientes, rv_recom_rechazo, rv_recom_aprobadas;
    private TextView txt_titulo_pendientes, txt_titulo_rechazadas, txt_titulo_aprobadas;
    private ImageView ic_collapse, ic_collapse_rechazo, ic_collapse_aprobada;
    private Button btn_buscar_obs;


    public interface OnSaveRating {
        void onFinishSaveRating(EvaluacionAnterior evaluacionAnterior);
    }

    private String estadoFenologico;

    public void setEstadoFenologico(String estadoFenologico) {
        this.estadoFenologico = estadoFenologico;
    }

    public static DialogObservationTodo newInstance(
            AnexoContrato anexoContrato,
            EvaluacionAnterior evaluacionAnterior,
            VisitasCompletas visitaAnterior,
            VisitasCompletas visitaActual,
            String estadoFenologico,
            OnSaveRating onSaveRating) {
        DialogObservationTodo frag = new DialogObservationTodo();


        frag.setEstadoFenologico(estadoFenologico);

        frag.setAnexoContrato(anexoContrato);
        if (evaluacionAnterior != null) {
            frag.setEvaluacionAnterior(evaluacionAnterior);
        }

        if (visitaAnterior != null) {
            frag.setVisitasAnterior(visitaAnterior);
        }
        if (visitaActual != null) {
            frag.setVisitaActual(visitaActual);
        }
        frag.setOnSaveRating(onSaveRating);
        return frag;
    }


    public void setAnexoContrato(AnexoContrato anexoContrato) {
        this.anexoContrato = anexoContrato;
    }

    public void setVisitasAnterior(VisitasCompletas visitaAnterior) {
        this.visitaAnterior = visitaAnterior;
    }

    public void setVisitaActual(VisitasCompletas visitaActual) {
        this.visitaActual = visitaActual;
    }

    public void setEvaluacionAnterior(EvaluacionAnterior evaluacionAnterior) {
        this.evaluacionAnterior = evaluacionAnterior;
    }

    public void setOnSaveRating(OnSaveRating onSaveRating) {
        this.onSaveRating = onSaveRating;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_observation_todo, null);
        builder.setView(view);
        bind(view);


        if (visitaActual != null && evaluacionAnterior != null) {

//            cl_evaluacion.setVisibility(View.GONE);
//            cl_recomendacion.setVisibility(View.VISIBLE);
//            btn_evaluacion.setEnabled(false);
            ratingBar.setRating(evaluacionAnterior.evaluacion);
            et_comentario.setText(evaluacionAnterior.comentario);
            et_comentario.setEnabled(false);
            ratingBar.setEnabled(false);
            btn_posponer_evaluacion.setEnabled(false);
            btn_guardar_evaluacion.setEnabled(false);
            btn_evaluacion.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            btn_recomendaciones.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else if (this.visitaAnterior == null) {
            btn_evaluacion.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            btn_recomendaciones.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            ratingBar.setEnabled(false);
            et_comentario.setEnabled(false);
            btn_guardar_evaluacion.setEnabled(false);
            cl_evaluacion.setVisibility(View.GONE);
            cl_recomendacion.setVisibility(View.VISIBLE);
            tv_fecha_rankear.setText("SIN VISITAS PREVIAS REGISTRADAS");
        } else {
            btn_evaluacion.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            btn_recomendaciones.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            cl_evaluacion.setVisibility(View.VISIBLE);
            cl_recomendacion.setVisibility(View.GONE);
            String fecha_last = "Fecha: " + this.visitaAnterior.getVisitas().getFecha_visita() + " " + this.visitaAnterior.getVisitas().getHora_visita();
            tv_fecha_rankear.setText(fecha_last);
            tv_recom_rankear.setText(this.visitaAnterior.getVisitas().getRecomendation_visita());
            ratingBar.setRating(evaluacionAnterior.evaluacion);
            et_comentario.setText(evaluacionAnterior.comentario);
        }


        btn_recomendaciones.setOnClickListener(view1 -> {
            btn_evaluacion.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            btn_recomendaciones.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            cl_evaluacion.setVisibility(View.GONE);
            cl_recomendacion.setVisibility(View.VISIBLE);
        });

        btn_evaluacion.setOnClickListener(view1 -> {
            btn_evaluacion.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            btn_recomendaciones.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            cl_evaluacion.setVisibility(View.VISIBLE);
            cl_recomendacion.setVisibility(View.GONE);
        });


        fecha_plazo.setOnFocusChangeListener((view1, b) -> {
            if (b) levantarFecha(fecha_plazo);
        });
        bringRecoms();

        btn_add_recom.setOnClickListener(view1 -> addPendiente());

        btn_no_aplica.setOnClickListener(view1 -> {
            if (fecha_plazo.getText().toString().equals("NO APLICA")) {
                fecha_plazo.setText("");
                fecha_plazo.setEnabled(true);
                btn_no_aplica.setText("N/A");
            } else {
                fecha_plazo.setText("NO APLICA");
                fecha_plazo.setEnabled(false);
                btn_no_aplica.setText("APLICA");
            }
        });
        btn_guardar_evaluacion.setOnClickListener(view1 -> guardarEvaluacion(false));


        ocultarListas();

        return builder.create();
    }

    private void guardarEvaluacion(boolean posponse) {

        float rating = ratingBar.getRating();
        String comentario = et_comentario.getText().toString().trim().toLowerCase(Locale.ROOT);

        if (!posponse) {
            if (rating <= 0.0 || comentario.isEmpty()) {
                Toasty.error(requireActivity(), "Debe evaluar y comentar para guardar", Toast.LENGTH_LONG, true).show();
                return;
            }
        }

        evaluacionAnterior.evaluacion = rating;
        evaluacionAnterior.comentario = comentario;

        if (!posponse) {
            Toasty.success(requireActivity(), "Evaluacion guardada temporalmente, no olvide guardar la visita para confirmar los cambios.", Toast.LENGTH_LONG, true).show();
        }
        this.onSaveRating.onFinishSaveRating(evaluacionAnterior);
        Dialog dialog = getDialog();
        if (dialog != null) {
            getDialog().dismiss();
        }
    }


    private void ocultarListas() {
        txt_titulo_aprobadas.setOnClickListener(view1 -> {
            ic_collapse_aprobada.setImageDrawable((rv_recom_aprobadas.getVisibility() == View.VISIBLE)
                    ? getResources().getDrawable(R.drawable.ic_expand_down, requireActivity().getTheme())
                    : getResources().getDrawable(R.drawable.ic_expand_up, requireActivity().getTheme())
            );
            rv_recom_aprobadas.setVisibility((rv_recom_aprobadas.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
        });

        txt_titulo_pendientes.setOnClickListener(view1 -> {
            ic_collapse.setImageDrawable((rv_recom_pendientes.getVisibility() == View.VISIBLE)
                    ? getResources().getDrawable(R.drawable.ic_expand_down, requireActivity().getTheme())
                    : getResources().getDrawable(R.drawable.ic_expand_up, requireActivity().getTheme())
            );
            rv_recom_pendientes.setVisibility((rv_recom_pendientes.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
        });

        txt_titulo_rechazadas.setOnClickListener(view1 -> {
            ic_collapse_rechazo.setImageDrawable((rv_recom_rechazo.getVisibility() == View.VISIBLE)
                    ? getResources().getDrawable(R.drawable.ic_expand_down, requireActivity().getTheme())
                    : getResources().getDrawable(R.drawable.ic_expand_up, requireActivity().getTheme())
            );
            rv_recom_rechazo.setVisibility((rv_recom_rechazo.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
        });


        ic_collapse_aprobada.setOnClickListener(view1 -> {
            ic_collapse_aprobada.setImageDrawable((rv_recom_aprobadas.getVisibility() == View.VISIBLE)
                    ? getResources().getDrawable(R.drawable.ic_expand_down, requireActivity().getTheme())
                    : getResources().getDrawable(R.drawable.ic_expand_up, requireActivity().getTheme())
            );
            rv_recom_aprobadas.setVisibility((rv_recom_aprobadas.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
        });

        ic_collapse.setOnClickListener(view1 -> {
            ic_collapse.setImageDrawable((rv_recom_pendientes.getVisibility() == View.VISIBLE)
                    ? getResources().getDrawable(R.drawable.ic_expand_down, requireActivity().getTheme())
                    : getResources().getDrawable(R.drawable.ic_expand_up, requireActivity().getTheme())
            );
            rv_recom_pendientes.setVisibility((rv_recom_pendientes.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
        });

        ic_collapse_rechazo.setOnClickListener(view1 -> {
            ic_collapse_rechazo.setImageDrawable((rv_recom_rechazo.getVisibility() == View.VISIBLE)
                    ? getResources().getDrawable(R.drawable.ic_expand_down, requireActivity().getTheme())
                    : getResources().getDrawable(R.drawable.ic_expand_up, requireActivity().getTheme())
            );
            rv_recom_rechazo.setVisibility((rv_recom_rechazo.getVisibility() == View.VISIBLE) ? View.GONE : View.VISIBLE);
        });
    }

    private void showAlertForAddObsInEvaluacion(Evaluaciones evaluacion, String estado) {
        View viewInfalted = LayoutInflater.from(requireActivity()).inflate(R.layout.alert_guardar_obs, null);

        EditText txt_obs_recom = viewInfalted.findViewById(R.id.txt_obs_recom);

        final androidx.appcompat.app.AlertDialog builder = new androidx.appcompat.app.AlertDialog.Builder(requireActivity())
                .setView(viewInfalted)
                .setTitle(getResources().getString(R.string.atencion))
                .setPositiveButton(getResources().getString(R.string.guardar), (dialogInterface, i) -> {
                })
                .setNegativeButton(getResources().getString(R.string.nav_cancel), (dialogInterface, i) -> {
                })
                .create();


        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
            Button c = builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE);
            b.setOnClickListener(view -> {
                if (txt_obs_recom.getText().toString().trim().isEmpty()) {
                    Toasty.error(requireActivity(), "Debes ingresar una observacion", Toast.LENGTH_LONG, true).show();
                    return;
                }
                evaluacion.setObservacion_recom(txt_obs_recom.getText().toString().trim().toLowerCase(Locale.ROOT));
                cambiarEstadoEvaluacion(evaluacion, estado);
                builder.dismiss();
            });
            c.setOnClickListener(view -> builder.dismiss());
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void bringRecoms() {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<Evaluaciones>> futureEvaluacionesP = executor.submit(() ->
                MainActivity.myAppDB.DaoEvaluaciones().getEvaluacionesByEstado(Integer.parseInt(anexoContrato.getId_anexo_contrato()), "P"));

        Future<List<Evaluaciones>> futureEvaluacionesR = executor.submit(() ->
                MainActivity.myAppDB.DaoEvaluaciones().getEvaluacionesByEstado(Integer.parseInt(anexoContrato.getId_anexo_contrato()), "R"));

        Future<List<Evaluaciones>> futureEvaluacionesN = executor.submit(() ->
                MainActivity.myAppDB.DaoEvaluaciones().getEvaluacionesByEstado(Integer.parseInt(anexoContrato.getId_anexo_contrato()), "N"));


        try {
            crearAdaptador(futureEvaluacionesP.get(), "P");
            crearAdaptador(futureEvaluacionesR.get(), "R");
            crearAdaptador(futureEvaluacionesN.get(), "N");
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdown();

    }


    private void addPendiente() {
        if (et_nueva_recom.getText().toString().trim().isEmpty()) {
            Toasty.error(requireActivity(), "Debes ingresar alguna recomendacion", Toast.LENGTH_LONG, true).show();
            return;
        }

        if (fecha_plazo.getText().toString().trim().isEmpty()) {
            Toasty.error(requireActivity(), "Debes ingresar una fecha de plazo o un  'NO APLICA' ", Toast.LENGTH_LONG, true).show();
            return;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Config> configFuture = executor.submit(() -> MainActivity.myAppDB.myDao().getConfig());

        try {
            String claveUnica = UUID.randomUUID().toString();
            Config config = configFuture.get();
            Usuario usuario = executor.submit(() -> MainActivity.myAppDB.myDao().getUsuarioById(config.getId_usuario())).get();


            Evaluaciones eva = new Evaluaciones();
            eva.setObliga_visita((this.visitaActual != null) ? 0 : 1);

            if (visitaActual != null) {
                eva.setClave_unica_visita(visitaActual.getVisitas().getClave_unica_visita());
            }

            eva.setClave_unica_recomendacion(claveUnica);
            eva.setEstado("P");
            eva.setDescripcion_recom(et_nueva_recom.getText().toString().toLowerCase(Locale.ROOT).trim());
            eva.setFecha_hora_tx(Utilidades.fechaActualConHora());
            eva.setUser_tx((usuario != null) ? usuario.getRut_usuario() : "18.804.066-7");
            eva.setNombre_crea((usuario != null) ? usuario.getNombre() + " " + usuario.getApellido_p() : "");
            eva.setFecha_plazo(fecha_plazo.getText().toString());
            eva.setId_ac(Integer.parseInt(anexoContrato.getId_anexo_contrato()));

            ExecutorService execInsert = Executors.newSingleThreadExecutor();

            execInsert.submit(() -> MainActivity.myAppDB.DaoEvaluaciones().insertEvaluaciones(eva)).get();


            fecha_plazo.setEnabled(true);
            btn_no_aplica.setText("N/A");
            et_nueva_recom.setText("");
            fecha_plazo.setText("");
            bringRecoms();
            Toasty.success(requireActivity(), "Recomendacion agregada correctamente", Toast.LENGTH_LONG, true).show();


        } catch (ExecutionException | InterruptedException e) {
            Toasty.error(requireActivity(), "No se pudo guardar la recomendacion " + e.getMessage(), Toast.LENGTH_LONG, true).show();
        } finally {
            executor.shutdown();
        }
    }


    public void crearAdaptador(List<Evaluaciones> evaluaciones, String estado) {

        if (estado.equals("R")) {
            LinearLayoutManager lManager = null;
            lManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);

            rv_recom_aprobadas.setHasFixedSize(true);
            rv_recom_aprobadas.setLayoutManager(lManager);
            RecomendacionesAdapter recomendacionesAdapterR = new RecomendacionesAdapter(evaluaciones,
                    (view1, evaluacion) -> {
                    },
                    (view1, evaluacion) -> {
                    },
                    getContext()
            );
            rv_recom_aprobadas.setAdapter(recomendacionesAdapterR);

        }
        if (estado.equals("N")) {
            LinearLayoutManager lManager = null;
            lManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);

            rv_recom_rechazo.setHasFixedSize(true);
            rv_recom_rechazo.setLayoutManager(lManager);
            RecomendacionesAdapter recomendacionesAdapterN = new RecomendacionesAdapter(evaluaciones,
                    (view1, evaluacion) -> {
                    },
                    (view1, evaluacion) -> {
                    },
                    getContext()
            );
            rv_recom_rechazo.setAdapter(recomendacionesAdapterN);
        }
        if (estado.equals("P")) {
            LinearLayoutManager lManager = null;
            lManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);

            rv_recom_pendientes.setHasFixedSize(true);
            rv_recom_pendientes.setLayoutManager(lManager);
            RecomendacionesAdapter recomendacionesAdapterP = new RecomendacionesAdapter(evaluaciones,
                    (view1, evaluacion) -> cambiarEstadoEvaluacion(evaluacion, "R"),
                    (view1, evaluacion) -> showAlertForAddObsInEvaluacion(evaluacion, "N"),
                    getContext()
            );
            rv_recom_pendientes.setAdapter(recomendacionesAdapterP);
        }
    }

    private void cambiarEstadoEvaluacion(Evaluaciones evaluacion, String nuevoEstado) {

        ExecutorService executorUser = Executors.newSingleThreadExecutor();
        try {
            String claveUnica = (evaluacion.getClave_unica_recomendacion() == null) ? UUID.randomUUID().toString() : evaluacion.getClave_unica_recomendacion();
            Config config = executorUser.submit(() -> MainActivity.myAppDB.myDao().getConfig()).get();
            Usuario usuario = executorUser.submit(() -> MainActivity.myAppDB.myDao().getUsuarioById(config.getId_usuario())).get();

            evaluacion.setUser_mod(usuario.getRut_usuario());
            evaluacion.setClave_unica_recomendacion(claveUnica);
            evaluacion.setEstado(nuevoEstado);
            evaluacion.setEstado_server(0);
            evaluacion.setFecha_hora_mod(Utilidades.fechaActualConHora());
            evaluacion.setNombre_mod(usuario.getNombre() + " " + usuario.getApellido_p());


            executorUser.submit(() -> MainActivity.myAppDB.DaoEvaluaciones().updateEvaluaciones(evaluacion)).get();
            bringRecoms();

        } catch (ExecutionException | InterruptedException e) {
            Toasty.error(requireActivity(), "No se pudo modificar la recomendacion " + e.getMessage(), Toast.LENGTH_LONG, true).show();
        } finally {
            executorUser.shutdown();

        }


    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);

            btn_cerrar_recomendaciones.setOnClickListener(view1 -> guardarEvaluacion(true));
            btn_posponer_evaluacion.setOnClickListener(view -> guardarEvaluacion(true));
        }

    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        guardarEvaluacion(true);
    }

    private void levantarFecha(final EditText edit) {


        String fecha = Utilidades.fechaActualSinHora();
        String[] fechaRota;

        if (!TextUtils.isEmpty(edit.getText())) {
            try {
                fechaRota = Utilidades.voltearFechaBD(edit.getText().toString()).split("-");
            } catch (Exception e) {
                fechaRota = fecha.split("-");
            }
        } else {
            fechaRota = fecha.split("-");
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (datePicker, year, month, dayOfMonth) -> {

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
                    edit.setText(finalDate);
                },
                Integer.parseInt(fechaRota[0]),
                (Integer.parseInt(fechaRota[1]) - 1),
                Integer.parseInt(fechaRota[2])
        );
        datePickerDialog.show();

    }


    private void bind(View view) {
        btn_guardar_evaluacion = view.findViewById(R.id.btn_guardar_evaluacion);
        btn_posponer_evaluacion = view.findViewById(R.id.btn_posponer_evaluacion);
        btn_evaluacion = view.findViewById(R.id.btn_evaluacion);
        btn_recomendaciones = view.findViewById(R.id.btn_recomendaciones);
        cl_evaluacion = view.findViewById(R.id.cl_evaluacion);

        tv_fecha_rankear = view.findViewById(R.id.tv_fecha_rankear);
        tv_recom_rankear = view.findViewById(R.id.tv_recom_rankear);
        ratingBar = view.findViewById(R.id.ratingBar);
        et_comentario = view.findViewById(R.id.et_comentario);

        et_nueva_recom = view.findViewById(R.id.et_nueva_recom);
        btn_add_recom = view.findViewById(R.id.btn_add_recom);
        btn_no_aplica = view.findViewById(R.id.btn_no_aplica);
        btn_cerrar_recomendaciones = view.findViewById(R.id.btn_cerrar_recomendaciones);
        rv_recom_pendientes = view.findViewById(R.id.rv_recom_pendientes);
        rv_recom_rechazo = view.findViewById(R.id.rv_recom_rechazo);
        rv_recom_aprobadas = view.findViewById(R.id.rv_recom_aprobadas);
        txt_titulo_pendientes = view.findViewById(R.id.txt_titulo_pendientes);
        txt_titulo_rechazadas = view.findViewById(R.id.txt_titulo_rechazadas);
        txt_titulo_aprobadas = view.findViewById(R.id.txt_titulo_aprobadas);
        ic_collapse = view.findViewById(R.id.ic_collapse);
        ic_collapse_rechazo = view.findViewById(R.id.ic_collapse_rechazo);
        ic_collapse_aprobada = view.findViewById(R.id.ic_collapse_aprobada);
        cl_recomendacion = view.findViewById(R.id.cl_recomendacion);
        fecha_plazo = view.findViewById(R.id.fecha_plazo);

        btn_buscar_obs = view.findViewById(R.id.btn_buscar_obs);


        btn_buscar_obs.setOnClickListener(v -> {
            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();

            Fragment obsDialog = requireActivity().getSupportFragmentManager().findFragmentByTag("DIALOG_OBS_RECOM");
            if (obsDialog != null) {
                ft.remove(obsDialog);
            }
            DialogBuscaObsRecAnterior dialogHarvest = DialogBuscaObsRecAnterior.newInstance(estadoFenologico, "RECOMENDACION", (boolean saved, String contenido) -> {
                if (saved) {
                    et_nueva_recom.setText(contenido);
                }
            });
            dialogHarvest.show(ft, "DIALOG_OBS_RECOM");
        });
    }


}
