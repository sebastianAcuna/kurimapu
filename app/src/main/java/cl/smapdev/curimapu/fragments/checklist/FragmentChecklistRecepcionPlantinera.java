package cl.smapdev.curimapu.fragments.checklist;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.CheckListDetalleRecepcionPlantineraAdapter;
import cl.smapdev.curimapu.clases.adapters.FotosCheckListRecepcionPlantineraFotoDetalleAdapter;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.tablas.CheckListRecepcionPlantinera;
import cl.smapdev.curimapu.clases.tablas.CheckListRecepcionPlantineraDetalle;
import cl.smapdev.curimapu.clases.tablas.CheckListRecepcionPlantineraDetalleFotos;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.temporales.TempFirmas;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.dialogos.DialogFirma;
import cl.smapdev.curimapu.fragments.dialogos.DialogPlantinDetail;
import es.dmoral.toasty.Toasty;

public class FragmentChecklistRecepcionPlantinera extends Fragment {

    private CheckListRecepcionPlantinera checklist;
    private MainActivity activity;
    private SharedPreferences prefs;

    private AnexoCompleto anexoCompleto;
    private Usuario usuario;
    private Config config;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private CheckListDetalleRecepcionPlantineraAdapter adapter;
    private FotosCheckListRecepcionPlantineraFotoDetalleAdapter adapter_fotos;


    private TextView tv_agricultor, tv_especie, tv_anexo, tv_variedad, tv_agricultor_firma, tv_encargado_firma;

    private Button btn_nuevo_plantin, btn_firma_agricultor, btn_firma_encargado, btn_guardar_plantin, btn_cancelar_plantin;

    private ImageView check_firma_agricultor, check_firma_encargado;

    private RecyclerView rv_plantineras;


    public void setChecklist(CheckListRecepcionPlantinera checklist) {
        this.checklist = checklist;
    }

    public void setAnexoCompleto(AnexoCompleto anexoCompleto) {
        this.anexoCompleto = anexoCompleto;
    }

    public static FragmentChecklistRecepcionPlantinera newInstance(AnexoCompleto anexo, CheckListRecepcionPlantinera clist) {
        FragmentChecklistRecepcionPlantinera fragment = new FragmentChecklistRecepcionPlantinera();
        fragment.setChecklist(clist);
        fragment.setAnexoCompleto(anexo);
        return fragment;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            activity = (MainActivity) context;
            prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
        } else {
            throw new RuntimeException(context + " must be MainActivity");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_checklist_recepcion_plantinera, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    100);
        }

        executorService.execute(() -> {
            config = MainActivity.myAppDB.myDao().getConfig();
            usuario = MainActivity.myAppDB.myDao().getUsuarioById(config.getId_usuario());

            handler.post(() -> {
                bind(view);
            });
        });

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
        Utilidades.setToolbar(activity, view, getResources().getString(R.string.app_name), "CHECKLIST REC. PLANTINERA");
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public void bind(View view) {
        tv_agricultor = view.findViewById(R.id.tv_agricultor);
        tv_especie = view.findViewById(R.id.tv_especie);
        tv_anexo = view.findViewById(R.id.tv_anexo);
        tv_variedad = view.findViewById(R.id.tv_variedad);
        tv_encargado_firma = view.findViewById(R.id.tv_encargado_firma);
        tv_agricultor_firma = view.findViewById(R.id.tv_agricultor_firma);
        btn_nuevo_plantin = view.findViewById(R.id.btn_nuevo_plantin);
        btn_firma_agricultor = view.findViewById(R.id.btn_firma_agricultor);
        btn_firma_encargado = view.findViewById(R.id.btn_firma_encargado);
        check_firma_agricultor = view.findViewById(R.id.check_firma_agricultor);
        check_firma_encargado = view.findViewById(R.id.check_firma_encargado);
        rv_plantineras = view.findViewById(R.id.rv_plantineras);
        btn_guardar_plantin = view.findViewById(R.id.btn_guardar_plantin);
        btn_cancelar_plantin = view.findViewById(R.id.btn_cancelar_plantin);


        btn_guardar_plantin.setOnClickListener((v) -> onSave());
        btn_cancelar_plantin.setOnClickListener((v) -> preguntarCancelar());


        btn_nuevo_plantin.setOnClickListener((v) -> {
            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentByTag(Utilidades.DIALOG_TAG_RECEPCION_PLANTIN);
            if (prev != null) {
                ft.remove(prev);
            }

            DialogPlantinDetail dialogo = DialogPlantinDetail.newInstance(saved -> {
                listadoDetalle();
            }, checklist, null, usuario);
            dialogo.show(ft, Utilidades.DIALOG_TAG_RECEPCION_PLANTIN);
        });

        btn_firma_encargado.setOnClickListener(view1 -> {

            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentByTag(Utilidades.DIALOG_TAG_FIRMA_ENCARGADO_PLANTIN);
            if (prev != null) {
                ft.remove(prev);
            }

            String etRA = UUID.randomUUID().toString() + ".png";

            DialogFirma dialogo = DialogFirma.newInstance(
                    Utilidades.TIPO_DOCUMENTO_CHECKLIST_RECEPCION_PLANTINERA,
                    etRA,
                    Utilidades.DIALOG_TAG_FIRMA_ENCARGADO_PLANTIN,
                    (isSaved, path) -> {
                        if (isSaved) {
                            check_firma_encargado.setVisibility(View.VISIBLE);
                        }
                    }
            );

            dialogo.show(ft, Utilidades.DIALOG_TAG_FIRMA_ENCARGADO_PLANTIN);
        });

        btn_firma_agricultor.setOnClickListener(view1 -> {

            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentByTag(Utilidades.DIALOG_TAG_FIRMA_AGRICULTOR_PLANTIN);
            if (prev != null) {
                ft.remove(prev);
            }

            String etRA = UUID.randomUUID().toString() + ".png";

            DialogFirma dialogo = DialogFirma.newInstance(
                    Utilidades.TIPO_DOCUMENTO_CHECKLIST_RECEPCION_PLANTINERA,
                    etRA,
                    Utilidades.DIALOG_TAG_FIRMA_AGRICULTOR_PLANTIN,
                    (isSaved, path) -> {
                        if (isSaved) {
                            check_firma_agricultor.setVisibility(View.VISIBLE);
                        }
                    }
            );

            dialogo.show(ft, Utilidades.DIALOG_TAG_FIRMA_AGRICULTOR_PLANTIN);
        });


        tv_agricultor.setText(anexoCompleto.getAgricultor().getNombre_agricultor());
        tv_especie.setText(anexoCompleto.getEspecie().getDesc_especie());
        tv_anexo.setText(anexoCompleto.getAnexoContrato().getAnexo_contrato());
        tv_variedad.setText(anexoCompleto.getVariedad().getDesc_variedad());

        tv_agricultor_firma.setText(anexoCompleto.getAgricultor().getNombre_agricultor());
        tv_encargado_firma.setText(usuario.getNombre());

        listadoDetalle();
    }


    public void preguntarCancelar() {
        new AlertDialog.Builder(requireContext())
                .setTitle("¿Cancelar?")
                .setMessage("¿Estás seguro que deseas cancelar? Se perderán los cambios no guardados.")
                .setPositiveButton("Sí", (dialog, which) -> onCancel())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }


    public void onCancel() {
        executorService.execute(() -> {
            MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().eliminarFotosSinClaveUnica();
            MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().eliminarDetalleSinClaveUnica();

            activity.runOnUiThread(() -> {
                activity.onBackPressed();
            });
        });
    }


    private void onSave() {

        executorService.execute(() -> {

            String clave = (checklist == null) ? UUID.randomUUID().toString() : checklist.getClave_unica();
            List<CheckListRecepcionPlantineraDetalle> detalles = MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().obtenerRPDetallePorClaveCabeceraONula(clave);

            if (detalles.isEmpty()) {
                activity.runOnUiThread(() -> {
                    Toasty.error(activity, "Debe ingresar al menos un detalle de plantinera.", Toast.LENGTH_LONG).show();
                });
                return;
            }

            try {
                CheckListRecepcionPlantinera chk = new CheckListRecepcionPlantinera();

                chk.setApellido_checklist(anexoCompleto.getAgricultor().getNombre_agricultor());
                chk.setClave_unica(clave);
                chk.setEstado_sincronizacion(0);
                chk.setId_ac_recepcion_plantinera(Integer.parseInt(anexoCompleto.getAnexoContrato().getId_anexo_contrato()));

                chk.setEstado_documento(1);

                List<TempFirmas> firmasF = MainActivity.myAppDB.DaoFirmas()
                        .getFirmasByDocum(Utilidades.TIPO_DOCUMENTO_CHECKLIST_RECEPCION_PLANTINERA);

                for (TempFirmas ff : firmasF) {

                    switch (ff.getLugar_firma()) {
                        case Utilidades.DIALOG_TAG_FIRMA_AGRICULTOR_PLANTIN:
                            chk.setRuta_firma_agricultor(ff.getPath());
                            break;
                        case Utilidades.DIALOG_TAG_FIRMA_ENCARGADO_PLANTIN:
                            chk.setRuta_firma_responsable(ff.getPath());
                            break;

                    }
                }

                if (checklist == null) {
                    chk.setFecha_hora_tx(Utilidades.fechaActualConHora());
                    chk.setId_usuario(config.getId_usuario());
                    MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().insertclRP(chk);
                } else {
                    chk.setFecha_hora_tx(checklist.getFecha_hora_tx());
                    chk.setId_usuario(checklist.getId_usuario());
                    chk.setId_usuario_mod(config.getId_usuario());
                    chk.setFecha_hora_mod(Utilidades.fechaActualConHora());
                    chk.setId_cl_recepcion_plantinera(checklist.getId_cl_recepcion_plantinera());
                    MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().updateclRP(chk);
                }

                MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().actualizarClaveForaneaDetalle(clave);
                MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().actualizarClaveForaneaFotos(clave);

                activity.runOnUiThread(() -> {
                    Toasty.success(activity, "Checklist guardado correctamente.", Toast.LENGTH_LONG).show();
                    activity.onBackPressed();
                });

            } catch (Exception e) {
                activity.runOnUiThread(() -> {
                    Toasty.error(activity, "Error al guardar el checklist: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
                return;
            }


        });

    }

    private void listadoDetalle() {


        LinearLayoutManager lManagerH = null;
        if (activity != null) {
            lManagerH = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        }

        rv_plantineras.setHasFixedSize(true);
        rv_plantineras.setLayoutManager(lManagerH);

        String clave = (checklist == null) ? "" : checklist.getClave_unica();

        executorService.execute(() -> {

            List<CheckListRecepcionPlantineraDetalle> recepcionPlantineraDetalles = MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().obtenerRPDetallePorClaveCabeceraONula(clave);

            activity.runOnUiThread(() -> {
                adapter = new CheckListDetalleRecepcionPlantineraAdapter(recepcionPlantineraDetalles, activity,
                        this::levantarImagenes,
                        this::preguntaEliminar,
                        (editar) -> {
                            if (checklist != null && checklist.getEstado_sincronizacion() == 1) {
                                Toasty.error(activity, "No se puede editar un plantin sincronizado.", Toast.LENGTH_LONG).show();
                                return;
                            }
                            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                            Fragment prev = requireActivity()
                                    .getSupportFragmentManager()
                                    .findFragmentByTag(Utilidades.DIALOG_TAG_RECEPCION_PLANTIN);
                            if (prev != null) {
                                ft.remove(prev);
                            }

                            DialogPlantinDetail dialogo = DialogPlantinDetail.newInstance(saved -> {

                            }, checklist, editar, usuario);
                            dialogo.show(ft, Utilidades.DIALOG_TAG_RECEPCION_PLANTIN);

                        }
                );
                rv_plantineras.setAdapter(adapter);

            });

        });

    }


    public void levantarImagenes(CheckListRecepcionPlantineraDetalle detalle) {
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_list_img, null);

        final androidx.appcompat.app.AlertDialog builder;
        builder = new androidx.appcompat.app.AlertDialog.Builder(requireActivity())
                .setTitle("Fotos plantinera")
                .setView(viewInfalted)
                .setPositiveButton("cerrar", (dialogInterface, i) -> {
                }).create();

        final RecyclerView lista_imagenes_modal = viewInfalted.findViewById(R.id.lista_imagenes_modal);


        LinearLayoutManager lManagerH = null, lManagerM = null;
        if (activity != null) {
            lManagerH = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        }

        lista_imagenes_modal.setHasFixedSize(true);
        lista_imagenes_modal.setLayoutManager(lManagerH);
        executorService.execute(() -> {
            List<CheckListRecepcionPlantineraDetalleFotos> myImageLis = MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().contarFotosPorClaveCabeceraOVacias(detalle.getClave_unica_rp_detalle());

            activity.runOnUiThread(() -> {
                adapter_fotos = new FotosCheckListRecepcionPlantineraFotoDetalleAdapter(myImageLis, activity,
                        fotos -> {
                        },
                        fotos -> {
                        });
                lista_imagenes_modal.setAdapter(adapter_fotos);
            });
        });


        builder.setCancelable(false);
        builder.show();
    }


    public void preguntaEliminar(CheckListRecepcionPlantineraDetalle detalle) {
        if (detalle.getEstado_sincronizacion() == 1) {
            Toasty.error(activity, "No se puede eliminar un detalle sincronizado.", Toast.LENGTH_LONG).show();
            return;
        }
        new AlertDialog.Builder(requireContext())
                .setTitle("¿Eliminar?")
                .setMessage("¿Estás seguro que deseas eliminar?.")
                .setPositiveButton("Sí", (dialog, which) -> {
                    executorService.execute(() -> {
                        MainActivity.myAppDB.DaoCheckListRecepcionPlantineras().eliminarDetalle(detalle);
                        activity.runOnUiThread(this::listadoDetalle);
                    });
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }


}
