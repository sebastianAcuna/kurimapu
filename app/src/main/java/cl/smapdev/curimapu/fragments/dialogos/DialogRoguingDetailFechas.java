package cl.smapdev.curimapu.fragments.dialogos;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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
import cl.smapdev.curimapu.clases.adapters.GenericSpinnerAdapter;
import cl.smapdev.curimapu.clases.adapters.SpinnerItem;
import cl.smapdev.curimapu.clases.adapters.SpinnerItemImp;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguing;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingDetalleFechas;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingFotoDetalle;
import cl.smapdev.curimapu.clases.tablas.FueraTipoCategoria;
import cl.smapdev.curimapu.clases.tablas.FueraTipoSubCategoria;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import es.dmoral.toasty.Toasty;


public class DialogRoguingDetailFechas extends DialogFragment {


    private MainActivity activity;

    private ArrayList<SpinnerItemImp> categorias = new ArrayList<>();
    private ArrayList<SpinnerItemImp> subCategorias = new ArrayList<>();
    private EditText et_fecha;
    private Spinner sp_estado_fenologico, sp_categoria, sp_sub_categoria;
    private Button btn_nuevo_roguing, btn_guardar_anexo_fecha, btn_posponer_anexo_fecha;
    private RecyclerView listado_rouging;

    private RadioButton radio_confeccion, radio_terminado;

    private AnexoCompleto anexoCompleto;
    private Usuario usuario;

    private CheckListRoguingDetailAdapter detail_adapter;
    private CheckListRoguingDetalleFechas chk;
    private CheckListRoguing checklist;


    private IOnSave IOnSave;

    public interface IOnSave {
        void onSave(boolean saved);
    }

    public void setAnexoCompleto(AnexoCompleto anexoCompleto) {
        this.anexoCompleto = anexoCompleto;
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

    public static DialogRoguingDetailFechas newInstance(IOnSave onSave, CheckListRoguingDetalleFechas chk, Usuario usuario, CheckListRoguing checklist, AnexoCompleto anexoCompleto) {
        DialogRoguingDetailFechas dg = new DialogRoguingDetailFechas();
        dg.setIOnSave(onSave);
        dg.setUsuario(usuario);
        dg.setChk(chk);
        dg.setChecklist(checklist);
        dg.setAnexoCompleto(anexoCompleto);
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

        categorias.clear();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {

            List<FueraTipoCategoria> asd = executorService.submit(() -> MainActivity.myAppDB.DaoFueraTipo().obtenerCategorias()).get();
            if (!asd.isEmpty()) {
                for (FueraTipoCategoria d : asd) {
                    categorias.add(new SpinnerItemImp(d.getId_fuera_tipo_cat(), d.getDescripcion()));
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        sp_categoria.setAdapter(new GenericSpinnerAdapter<SpinnerItemImp>(requireActivity(), categorias));

    }

    private void obtenerListadoSubCategoria(int id) {
        subCategorias.clear();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {

            List<FueraTipoSubCategoria> asd = executorService.submit(() -> MainActivity.myAppDB.DaoFueraTipo().obtenerSubCategoriaPorIdCat(id)).get();
            if (!asd.isEmpty()) {
                for (FueraTipoSubCategoria d : asd) {
                    subCategorias.add(new SpinnerItemImp(d.getId_fuera_tipo_sub_cat(), d.getDescripcion_sub_cat()));
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        subCategorias.add(new SpinnerItemImp(0, "OTRO"));
        sp_sub_categoria.setAdapter(new GenericSpinnerAdapter<SpinnerItemImp>(requireActivity(), subCategorias));
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
                    if (chk != null && chk.getEstadoFecha() == 1) {
                        return;
                    }
                    FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                    Fragment prev = requireActivity()
                            .getSupportFragmentManager()
                            .findFragmentByTag(Utilidades.DIALOG_TAG_ROGUING_DETALLE);
                    if (prev != null) {
                        ft.remove(prev);
                    }

                    SpinnerItem categoria;
                    SpinnerItem subcategoria;

                    FueraTipoCategoria ftCat = MainActivity.myAppDB.DaoFueraTipo().obtenerCategoria(fotos.getId_fuera_tipo_categoria());
                    FueraTipoSubCategoria ftSubCat = MainActivity.myAppDB.DaoFueraTipo().obtenerSubCategoriaPorId(fotos.getId_fuera_tipo_sub_categoria());

                    if (ftCat != null) {
                        categoria = new SpinnerItemImp(ftCat.getId_fuera_tipo_cat(), ftCat.getDescripcion());
                    } else {
                        categoria = new SpinnerItemImp(0, "OTRO");
                    }

                    String descripcionFueraTipo = "";
                    if (fotos.getId_fuera_tipo_sub_categoria() == 0 || ftSubCat == null) {
                        subcategoria = new SpinnerItemImp(0, "OTRO");
                        descripcionFueraTipo = fotos.getDescripcion_fuera_tipo();
                    } else {
                        subcategoria = new SpinnerItemImp(ftSubCat.getId_fuera_tipo_sub_cat(), ftSubCat.getDescripcion_sub_cat());
                        descripcionFueraTipo = subcategoria.getDescripcion();
                    }


                    DialogRoguingDetail dialogo = DialogRoguingDetail.newInstance(saved -> {
                        listadoDetalles();
                        obtenerListadoFueraTipo();
                    }, fotos, categoria, subcategoria, descripcionFueraTipo, usuario);
                    dialogo.show(ft, Utilidades.DIALOG_TAG_ROGUING_DETALLE);
                },
                fotos -> {
//                    alertForDeletePhoto("Eliminar Fuera de tipo", "Esta seguro que desea eliminarlo?", fotos)
                });
        listado_rouging.setAdapter(detail_adapter);
    }

    private void prepararOnSave() {

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
            boolean alertaPorFecha = false;
            boolean alertaPorEstado = false;
            boolean alertaEstadoFechaTerminada = false;
            CheckListRoguingDetalleFechas registroDuplicado = null;
            if (checklist != null) {
                List<CheckListRoguingDetalleFechas> f = executorService.submit(() -> MainActivity.myAppDB.DaoCLRoguing().obtenerDetalleFechaRoguingPorClaveUnicaPadre(checklist.getClave_unica())).get();

                String claveUnica = (chk != null) ? chk.getClave_unica_detalle_fecha() : null;

                for (CheckListRoguingDetalleFechas a : f) {

                    boolean mismaFecha = a.getFecha().equals(Utilidades.voltearFechaBD(et_fecha.getText().toString()));
                    boolean mismoEstado = a.getEstado_fenologico().equals(sp_estado_fenologico.getSelectedItem());
                    boolean mismaClaveUnica = a.getClave_unica_detalle_fecha().equals(claveUnica);
                    boolean fechaTerminada = a.getEstadoFecha() == 1;


                    if (mismaFecha && !mismaClaveUnica && !mismoEstado) {
                        alertaPorFecha = true;
                        break;
                    } else if (mismaFecha && !mismaClaveUnica && fechaTerminada) {
                        alertaEstadoFechaTerminada = true;
                        break;
                    } else if (mismaFecha && !mismaClaveUnica) {
                        alertaPorEstado = true;
                        registroDuplicado = a;
                        break;
                    }
                }

                if (chk != null && (alertaPorFecha || alertaEstadoFechaTerminada || alertaPorEstado)) {
                    Utilidades.avisoListo(
                            requireActivity(),
                            "Registro existente",
                            "La fecha de roguing que deseas modificar ya existe con anterioridad, no puedes realizar la accion. ",
                            "Entiendo");

                    return;
                }
                if (alertaPorFecha) {
                    Utilidades.avisoListo(
                            requireActivity(),
                            "Ya existe un registro con la misma fecha",
                            "No puedes crear la misma fecha con un estado fenológico diferente o ",
                            "Entiendo");

                    return;
                }
                if (alertaEstadoFechaTerminada) {
                    Utilidades.avisoListo(
                            requireActivity(),
                            "Ya existe un registro con la misma fecha",
                            "Ya existe una fecha con el mismo estado fenológico en estado terminado, por ende, no se pueden juntar.",
                            "Entiendo");

                    return;
                }
                if (alertaPorEstado) {
                    avisoJuntaFechas(
                            "Ya existe un registro con la misma fecha",
                            "Ya existe una fecha con el mismo estado fenológico, al guardar se juntarán los fuera de tipo ingresados.",
                            "Entiendo, juntar",
                            registroDuplicado);

                    return;
                }

            }

            onSave(null);
        } catch (InterruptedException | ExecutionException e) {
            Toasty.error(requireContext(),
                    "Error tratando de guardar" + e.getMessage(),
                    Toast.LENGTH_LONG, true).show();
        } finally {
            executorService.shutdown();
        }
    }


    public void avisoJuntaFechas(String title, String message, String textButton, CheckListRoguingDetalleFechas registroFecha) {
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_empty, null);

        final androidx.appcompat.app.AlertDialog builder = new androidx.appcompat.app.AlertDialog.Builder(activity)
                .setView(viewInfalted)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(textButton, (dialogInterface, i) -> {
                })
                .setNegativeButton("cancelar", (dialogInterface, i) -> {
                })
                .create();

        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
            Button c = builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE);

            b.setOnClickListener(v -> {
                onSave(registroFecha);
                builder.dismiss();
            });
            c.setOnClickListener(v -> builder.dismiss());

        });
        builder.setCancelable(false);
        builder.show();
    }

    private void onSave(CheckListRoguingDetalleFechas registroFecha) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        try {
            String clave = (chk == null) ? "" : chk.getClave_unica_detalle_fecha();
            List<CheckListRoguingDetalle> det = executorService.submit(() -> MainActivity.myAppDB.DaoCLRoguing().obtenerDetalleSinPadreFechaoPadre(clave)).get();
            String claveUnica = (chk == null) ? UUID.randomUUID().toString() : chk.getClave_unica_detalle_fecha();

            CheckListRoguingDetalleFechas fechas = new CheckListRoguingDetalleFechas();

            fechas.setFecha(Utilidades.voltearFechaBD(et_fecha.getText().toString()));
            fechas.setEstado_fenologico(sp_estado_fenologico.getSelectedItem().toString());
            fechas.setClave_unica_detalle_fecha(claveUnica);

            int estadoFecha = radio_confeccion.isChecked() ? 0 : 1;
            fechas.setEstadoFecha(estadoFecha);


            if (registroFecha == null) {
                if (chk != null) {
                    fechas.setId_cl_roguing_detalle(chk.getId_cl_roguing_detalle());
                    executorService.submit(() -> MainActivity.myAppDB.DaoCLRoguing().updateRoguingDetalleFecha(fechas)).get();
                } else {
                    executorService.submit(() -> MainActivity.myAppDB.DaoCLRoguing().insertRoguingDetalleFecha(fechas)).get();
                }
            } else {
                fechas.setClave_unica_roguing(registroFecha.getClave_unica_roguing());
                fechas.setClave_unica_detalle_fecha(registroFecha.getClave_unica_detalle_fecha());
                fechas.setId_cl_roguing_detalle(registroFecha.getId_cl_roguing_detalle());
                fechas.setEstado_sincronizacion(0);
                executorService.submit(() -> MainActivity.myAppDB.DaoCLRoguing().updateRoguingDetalleFecha(fechas)).get();
            }

            for (CheckListRoguingDetalle d : det) {
                d.setClave_unica_detalle_fecha(claveUnica);
                if (registroFecha != null) {
                    d.setClave_unica_roguing(registroFecha.getClave_unica_roguing());
                    d.setClave_unica_detalle_fecha(registroFecha.getClave_unica_detalle_fecha());

                    List<CheckListRoguingFotoDetalle> fotDet = executorService.submit(() -> MainActivity.myAppDB.DaoCLRoguing().obtenerDetalleRoguingFotoDetPorDetalle(clave)).get();

                    for (CheckListRoguingFotoDetalle f : fotDet) {
                        f.setClave_unica_roguing(registroFecha.getClave_unica_roguing());
                        executorService.submit(() -> MainActivity.myAppDB.DaoCLRoguing().updateRoguingFotoDetalle(f)).get();
                    }
                }
                d.setEstado_sincronizacion(0);
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
        sp_categoria = view.findViewById(R.id.sp_categoria);
        sp_sub_categoria = view.findViewById(R.id.sp_sub_categoria);
        btn_nuevo_roguing = view.findViewById(R.id.btn_nuevo_roguing);
        btn_guardar_anexo_fecha = view.findViewById(R.id.btn_guardar_anexo_fecha);
        btn_posponer_anexo_fecha = view.findViewById(R.id.btn_posponer_anexo_fecha);
        listado_rouging = view.findViewById(R.id.listado_rouging);

        radio_confeccion = view.findViewById(R.id.radio_confeccion);
        radio_terminado = view.findViewById(R.id.radio_terminado);


        et_fecha.setKeyListener(null);
        et_fecha.setInputType(InputType.TYPE_NULL);
        et_fecha.setOnClickListener(view1 -> Utilidades.levantarFecha(et_fecha, requireContext()));
        et_fecha.setOnFocusChangeListener((view1, b) -> {
            Utilidades.hideKeyboard(activity);
            if (b) Utilidades.levantarFecha(et_fecha, requireContext());
        });


        btn_posponer_anexo_fecha.setOnClickListener(v -> onCancel());

        btn_guardar_anexo_fecha.setOnClickListener(v -> prepararOnSave());

        sp_categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerItem seleccionado = (SpinnerItem) parent.getItemAtPosition(position);
                int idSeleccionado = seleccionado.getId();

                obtenerListadoSubCategoria(idSeleccionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_nuevo_roguing.setOnClickListener(v -> {
            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentByTag(Utilidades.DIALOG_TAG_ROGUING_DETALLE);
            if (prev != null) {
                ft.remove(prev);
            }

            SpinnerItem categoria = (SpinnerItem) sp_categoria.getSelectedItem();
            SpinnerItem subcategoria = (SpinnerItem) sp_sub_categoria.getSelectedItem();

            String descripcionFueraTipo = (subcategoria.getId() == 0) ? "" : subcategoria.getDescripcion();

            DialogRoguingDetail dialogo = DialogRoguingDetail.newInstance(saved -> {
                listadoDetalles();
                obtenerListadoFueraTipo();
            }, null, categoria, subcategoria, descripcionFueraTipo, usuario);
            dialogo.show(ft, Utilidades.DIALOG_TAG_ROGUING_DETALLE);
        });


        if (chk != null) {

            if (chk.getEstadoFecha() == 1) {
                btn_nuevo_roguing.setEnabled(false);
                et_fecha.setEnabled(false);
                sp_estado_fenologico.setEnabled(false);
                sp_categoria.setEnabled(false);
                sp_sub_categoria.setEnabled(false);
            }

            List<String> feno_option = Arrays.asList(getResources().getStringArray(R.array.fenologico));

            radio_confeccion.setChecked((chk.getEstadoFecha() == 0));
            radio_terminado.setChecked((chk.getEstadoFecha() == 1));
            et_fecha.setText(Utilidades.voltearFechaVista(chk.getFecha()));
            sp_estado_fenologico.setSelection(feno_option.indexOf(chk.getEstado_fenologico()));
        } else {
            radio_confeccion.setChecked(true);
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
