package cl.smapdev.curimapu.fragments.dialogos;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.CheckListRoguingDetailAdapter;
import cl.smapdev.curimapu.clases.adapters.SpinnerAdapter;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguing;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingDetalleFechas;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import es.dmoral.toasty.Toasty;

public class DialogRoguingDetailFechas extends DialogFragment {


    private MainActivity activity;

    private ArrayList<String> offTypes = new ArrayList<>();
    private EditText et_fecha;
    private Spinner sp_estado_fenologico, sp_fuera_tipo;
    private Button btn_nuevo_roguing, btn_guardar_anexo_fecha, btn_posponer_anexo_fecha;
    private RecyclerView listado_rouging;


    private Usuario usuario;

    private CheckListRoguingDetailAdapter detail_adapter;
    private CheckListRoguingDetalleFechas chk;
    private CheckListRoguing checklist;


    private IOnSave IOnSave;

    public interface IOnSave {
        void onSave(boolean saved);
    }

    public void setChk(CheckListRoguingDetalleFechas chk) {
        this.chk = chk;
    }

    public CheckListRoguing getChecklist() {
        return checklist;
    }

    public void setChecklist(CheckListRoguing checklist) {
        this.checklist = checklist;
    }

    public void setIOnSave(IOnSave onSave) {
        this.IOnSave = onSave;
    }


    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public static DialogRoguingDetailFechas newInstance(IOnSave onSave, CheckListRoguingDetalleFechas chk, Usuario usuario, CheckListRoguing checklist) {
        DialogRoguingDetailFechas dg = new DialogRoguingDetailFechas();
        dg.setIOnSave(onSave);
        dg.setUsuario(usuario);
        dg.setChk(chk);
        dg.setChecklist(checklist);
        return dg;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_roguing_detail_fechas, null);
        builder.setView(view);

        builder.setTitle("Nuevo Roguing");

        MainActivity a = (MainActivity) getActivity();
        if (a != null) activity = a;
        bind(view);

        obtenerListadoFueraTipo();

        listadoDetalles();
        return builder.create();
    }

    private void obtenerListadoFueraTipo() {

        offTypes.clear();
        offTypes.add("NUEVO");

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {

            String clave = (checklist == null) ? "" : checklist.getClave_unica();
            List<CheckListRoguingDetalle> asd = executorService.submit(() -> MainActivity.myAppDB.DaoCLRoguing().obtenerDetalleRoguingPorClaveUnicaPadreFechas(clave)).get();


            if (!asd.isEmpty()) {
                for (CheckListRoguingDetalle d : asd) {
                    if (!offTypes.contains(d.getDescripcion_fuera_tipo())) {
                        offTypes.add(d.getDescripcion_fuera_tipo());
                    }
                }
            }


        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        executorService.shutdown();


        sp_fuera_tipo.setAdapter(new SpinnerAdapter(requireActivity(), android.R.layout.simple_spinner_item, offTypes));


    }


    private void listadoDetalles() {
        LinearLayoutManager lManagerH = null;
        if (activity != null) {
            lManagerH = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        }

        listado_rouging.setHasFixedSize(true);
        listado_rouging.setLayoutManager(lManagerH);

        List<CheckListRoguingDetalle> myImageLis;

        if (chk != null) {
            myImageLis = MainActivity.myAppDB.DaoCLRoguing().obtenerDetalleSinPadreFechaoPadre(chk.getClave_unica_detalle_fecha());
        } else {
            myImageLis = MainActivity.myAppDB.DaoCLRoguing().obtenerDetalleSinPadreFecha();
        }
        detail_adapter = new CheckListRoguingDetailAdapter(myImageLis, activity,
                fotos -> {
                    FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                    Fragment prev = requireActivity()
                            .getSupportFragmentManager()
                            .findFragmentByTag(Utilidades.DIALOG_TAG_ROGUING_DETALLE);
                    if (prev != null) {
                        ft.remove(prev);
                    }

                    DialogRoguingDetail dialogo = DialogRoguingDetail.newInstance(saved -> {
                        listadoDetalles();
                        obtenerListadoFueraTipo();
                    }, fotos, fotos.getDescripcion_fuera_tipo(), usuario);
                    dialogo.show(ft, Utilidades.DIALOG_TAG_ROGUING_DETALLE);
                },
                fotos -> alertForDeletePhoto("Eliminar Fuera de tipo", "Esta seguro que desea eliminarlo?", fotos));
        listado_rouging.setAdapter(detail_adapter);
    }


    private void onSave() {

        if (et_fecha.getText().toString().isEmpty() || sp_estado_fenologico.getSelectedItem().toString().equals("--Seleccione--")) {
            Toasty.error(requireContext(),
                    "Debes completar fecha y estado fenologico",
                    Toast.LENGTH_LONG, true).show();
            return;
        }


        try {
            Utilidades.validarFecha(Utilidades.voltearFechaBD(et_fecha.getText().toString()), "fecha");
        } catch (Error e) {
            Toasty.error(requireActivity(), e.getMessage(), Toast.LENGTH_LONG, true).show();
            return;
        }


        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {

            String clave = (chk == null) ? "" : chk.getClave_unica_detalle_fecha();

            List<CheckListRoguingDetalle> det = executorService.submit(() -> MainActivity.myAppDB.DaoCLRoguing().obtenerDetalleSinPadreFechaoPadre(clave)).get();

            if (det.isEmpty()) {
                Toasty.error(requireContext(),
                        "Debes agregar al menos un fuera de tipo",
                        Toast.LENGTH_LONG, true).show();
                return;
            }

            if (checklist != null) {
                List<CheckListRoguingDetalleFechas> f = executorService.submit(() -> MainActivity.myAppDB.DaoCLRoguing().obtenerDetalleFechaRoguingPorClaveUnicaPadre(checklist.getClave_unica())).get();

                String claveUnica = (chk != null) ? chk.getClave_unica_detalle_fecha() : null;

                for (CheckListRoguingDetalleFechas a : f) {
                    if (a.getFecha().equals(Utilidades.voltearFechaBD(et_fecha.getText().toString())) && !a.getClave_unica_detalle_fecha().equals(claveUnica)) {
                        Toasty.error(requireContext(),
                                "ya existe esta fecha agregada al roguing",
                                Toast.LENGTH_LONG, true).show();
                        return;
                    }
                }
            }


            String claveUnica = (chk == null) ? UUID.randomUUID().toString() : chk.getClave_unica_detalle_fecha();

            CheckListRoguingDetalleFechas fechas = new CheckListRoguingDetalleFechas();

            fechas.setFecha(Utilidades.voltearFechaBD(et_fecha.getText().toString()));
            fechas.setEstado_fenologico(sp_estado_fenologico.getSelectedItem().toString());
            fechas.setClave_unica_detalle_fecha(claveUnica);

            if (chk != null) {
                fechas.setId_cl_roguing_detalle(chk.getId_cl_roguing_detalle());
                executorService.submit(() -> MainActivity.myAppDB.DaoCLRoguing().updateRoguingDetalleFecha(fechas)).get();
            } else {
                executorService.submit(() -> MainActivity.myAppDB.DaoCLRoguing().insertRoguingDetalleFecha(fechas)).get();
            }

            for (CheckListRoguingDetalle d : det) {
                d.setClave_unica_detalle_fecha(claveUnica);
                executorService.submit(() -> MainActivity.myAppDB.DaoCLRoguing().updateDetalleRoguing(d)).get();
            }


            Dialog dialog = getDialog();
            IOnSave.onSave(true);
            if (dialog != null) {
                dialog.dismiss();
            }

        } catch (InterruptedException | ExecutionException e) {
            Toasty.error(requireContext(),
                    "Error tratando de guardar" + e.getMessage(),
                    Toast.LENGTH_LONG, true).show();

        } finally {
            executorService.shutdown();
        }


    }


    public void alertForDeletePhoto(String title, String message, CheckListRoguingDetalle foto) {
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_empty, null);

        final androidx.appcompat.app.AlertDialog builder = new androidx.appcompat.app.AlertDialog.Builder(activity)
                .setView(viewInfalted)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("eliminar", (dialogInterface, i) -> {
                })
                .setNegativeButton("cancelar", (dialog, which) -> {
                })
                .create();

        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
            Button c = builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE);
            b.setOnClickListener(v -> {

                ExecutorService executor = Executors.newSingleThreadExecutor();
                try {
                    executor.submit(() -> MainActivity.myAppDB.DaoCLRoguing().deleteRoguingDetalle(foto)).get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                executor.shutdown();
                listadoDetalles();
                obtenerListadoFueraTipo();
                builder.dismiss();
            });
            c.setOnClickListener(v -> builder.dismiss());
        });
        builder.setCancelable(false);
        builder.show();
    }


    private void bind(View view) {

        et_fecha = view.findViewById(R.id.et_fecha);
        sp_estado_fenologico = view.findViewById(R.id.sp_estado_fenologico);
        sp_fuera_tipo = view.findViewById(R.id.sp_fuera_tipo);
        btn_nuevo_roguing = view.findViewById(R.id.btn_nuevo_roguing);
        btn_guardar_anexo_fecha = view.findViewById(R.id.btn_guardar_anexo_fecha);
        btn_posponer_anexo_fecha = view.findViewById(R.id.btn_posponer_anexo_fecha);
        listado_rouging = view.findViewById(R.id.listado_rouging);


        et_fecha.setKeyListener(null);
        et_fecha.setInputType(InputType.TYPE_NULL);
        et_fecha.setOnClickListener(view1 -> Utilidades.levantarFecha(et_fecha, requireContext()));
        et_fecha.setOnFocusChangeListener((view1, b) -> {
            Utilidades.hideKeyboard(activity);
            if (b) Utilidades.levantarFecha(et_fecha, requireContext());
        });

        btn_posponer_anexo_fecha.setOnClickListener(v -> onCancel());

        btn_guardar_anexo_fecha.setOnClickListener(v -> onSave());

        btn_nuevo_roguing.setOnClickListener(v -> {
            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentByTag(Utilidades.DIALOG_TAG_ROGUING_DETALLE);
            if (prev != null) {
                ft.remove(prev);
            }

            String fueraTipo = (sp_fuera_tipo.getSelectedItem() != null) ? sp_fuera_tipo.getSelectedItem().toString() : "";

            DialogRoguingDetail dialogo = DialogRoguingDetail.newInstance(saved -> {
                listadoDetalles();
                obtenerListadoFueraTipo();
            }, null, fueraTipo, usuario);
            dialogo.show(ft, Utilidades.DIALOG_TAG_ROGUING_DETALLE);
        });


        if (chk != null) {

            List<String> feno_option = Arrays.asList(getResources().getStringArray(R.array.fenologico));

            et_fecha.setText(Utilidades.voltearFechaVista(chk.getFecha()));
            sp_estado_fenologico.setSelection(feno_option.indexOf(chk.getEstado_fenologico()));


        }

    }

    public void onCancel() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            executorService.submit(() -> MainActivity.myAppDB.DaoCLRoguing().deleteFotosRoguingDetalleSinPadres()).get();
            executorService.submit(() -> MainActivity.myAppDB.DaoCLRoguing().deleteRoguingDetalleSinPadres()).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        executorService.shutdown();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            int with = ViewGroup.LayoutParams.WRAP_CONTENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(with, height);
        }
    }
}
