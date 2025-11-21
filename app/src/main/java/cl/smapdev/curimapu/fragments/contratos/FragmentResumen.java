package cl.smapdev.curimapu.fragments.contratos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.tablas.pro_cli_mat;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.clases.utilidades.cargarUI;
import cl.smapdev.curimapu.coroutines.ApplicationExecutors;

public class FragmentResumen extends Fragment {

    private MainActivity activity = null;

    private TextView fecha_resumen, estado_fenologico, estado_general, estado_crecimiento, estado_maleza, estado_fito, humedad_suelo, cosecha, observation, recomendaciones,
            anexo, orden_culplica, cliente, especie, variedad, ready_batch, raw_batch, grower, predio, potrero, siembra_sag, sag_register_number, irrigation_system, soil_type,
            production_location, has_contrato, has_customer, fieldman, rch, ptos_ampros;

    private SharedPreferences prefs;
    private ProgressDialog progressBar;

    private View Globalview;

    private ArrayList<ArrayList> global = null;
    private ArrayList<Integer> id_importante = null;
    private ArrayList<Integer> id_generica = null;
    private ArrayList<TextView> textViews = null;
    private final ArrayList<EditText> editTexts = null;
    private final ArrayList<RecyclerView> recyclerViews = null;
    private final ArrayList<ImageView> imageViews = null;
    private final ArrayList<Spinner> spinners = null;


    private final ExecutorService executors = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();


        progressBar = new ProgressDialog(activity);
        progressBar.setTitle(getResources().getString(R.string.espere));
        progressBar.show();

        if (activity != null)
            prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void onDestroy() {
        if (executors != null && !executors.isShutdown()) {
            executors.shutdown();
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Globalview = inflater.inflate(R.layout.fragment_resumen, container, false);
        return Globalview;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind(view);
        setHasOptionsMenu(true);

        if (progressBar.isShowing()) progressBar.dismiss();
        cargarInterfaz();


//        requireActivity().addMenuProvider(new MenuProvider() {
//            @Override
//            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
//                menu.clear();
//                menuInflater.inflate(R.menu.menu_visitas, menu);
//            }
//
//            @Override
//            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
//
//                if (menuItem.getItemId() == R.id.menu_visitas_recom) {
//
//
//                    executors.execute(() -> {
//
//                        VisitasCompletas visitasCompletas;
//
//                        TempVisitas temp_visitas = MainActivity.myAppDB.myDao().getTempFichas();
//                        if (temp_visitas != null && temp_visitas.getAction_temp_visita() != 2) {
//                            visitasCompletas = MainActivity.myAppDB.myDao().getUltimaVisitaByAnexo(temp_visitas.getId_anexo_temp_visita());
//                        } else {
//                            visitasCompletas = null;
//                        }
//
//                        AnexoContrato ac = MainActivity.myAppDB.myDao().getAnexos(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
//
//
//                        handler.post(() -> {
//                            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
//                            Fragment prev = requireActivity().getSupportFragmentManager().findFragmentByTag("EVALUACION_RECOMENDACION");
//                            if (prev != null) {
//                                ft.remove(prev);
//                            }
//
//                            DialogObservationTodo dialogo = DialogObservationTodo.newInstance(ac, temp_visitas, visitasCompletas, (TempVisitas tm) -> {
//                            });
//                            dialogo.show(ft, "EVALUACION_RECOMENDACION");
//                        });
//
//                    });
//                    return true;
//                }
//                return false;
//            }
//
//        }, getViewLifecycleOwner(), Lifecycle.State.CREATED);
//
//        Utilidades.setToolbar(activity, view, getResources().getString(R.string.subtitles_records), "visitas");


    }

    public void cargarInterfaz() {
        ApplicationExecutors exec = new ApplicationExecutors();

        ProgressDialog progressBar = new ProgressDialog(activity);
        progressBar.setTitle("cargando interfaz...");
        progressBar.show();


        exec.getBackground().execute(() -> {
            int idClienteFinal = MainActivity.myAppDB.myDao().getIdClienteByAnexo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));

            exec.getMainThread().execute(() -> {
                if (Globalview != null) {
                    global = cargarUI.cargarUI(Globalview, R.id.relative_constraint_resumen, activity, prefs.getString(Utilidades.SHARED_VISIT_MATERIAL_ID, ""), 1, idClienteFinal, global, prefs.getString(Utilidades.SHARED_VISIT_TEMPORADA, "1"));
                    setearOnFocus();
                    if (progressBar.isShowing()) {
                        progressBar.dismiss();
                    }
                }
            });
        });

        exec.shutDownBackground();
    }


    private void setearOnFocus() {
        if (global != null && global.size() > 0) {

            try {

                id_generica = (ArrayList<Integer>) global.get(0);
                id_importante = (ArrayList<Integer>) global.get(1);
                textViews = (ArrayList<TextView>) global.get(2);

                int idClienteFinal = MainActivity.myAppDB.myDao().getIdClienteByAnexo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));

                if (textViews != null && textViews.size() > 0) {
                    for (int i = 0; i < textViews.size(); i++) {
                        if (id_generica.contains(textViews.get(i).getId())) {
                            int index = id_generica.indexOf(textViews.get(i).getId());
                            int idImportante = id_importante.get(index);

                            pro_cli_mat fs = MainActivity.myAppDB.myDao().getProCliMatByIdProp(idImportante, idClienteFinal, prefs.getString(Utilidades.SHARED_VISIT_TEMPORADA, "1"));
                            if (fs != null) {
                                if (fs.getForaneo().equals("SI")) {

                                    Object[] ob = new Object[]{};
                                    String nombreCampoTableta = "";
                                    String nombreTabla = "";
                                    if (!fs.getTabla().equals("")) {
                                        switch (fs.getTabla()) {

                                            case "condicion":
                                                nombreCampoTableta = "condicion";
                                                nombreTabla = "anexo_contrato";
                                                break;

                                            case "cliente":
                                                nombreCampoTableta = (fs.getCampo().equals("razon_social")) ? "razon_social_clientes" : fs.getCampo();
                                                nombreTabla = fs.getTabla();
                                                break;

                                            case "comuna":
                                                nombreCampoTableta = (fs.getCampo().equals("nombre")) ? "desc_comuna" : fs.getCampo();
                                                nombreTabla = fs.getTabla();
                                                break;
                                            case "especie":
                                                nombreCampoTableta = (fs.getCampo().equals("nombre")) ? "desc_especie" : fs.getCampo();
                                                nombreTabla = fs.getTabla();
                                                break;
                                            case "materiales":
                                                nombreCampoTableta = (fs.getCampo().equals("nom_hibrido")) ? "desc_hibrido_variedad" : fs.getCampo();
                                                nombreTabla = fs.getTabla();
                                                break;
                                            case "libro_campo":
                                                nombreCampoTableta = " valor_detalle ";
                                                nombreTabla = "detalle_visita_prop";
                                                break;
                                            case "lote":
                                                nombreCampoTableta = (fs.getCampo().equals("nombre")) ? "nombre_lote" : fs.getCampo();
                                                nombreTabla = fs.getTabla();
                                                break;
                                            case "visita":
                                                switch (fs.getCampo()) {
                                                    case "fecha_r":
                                                        nombreCampoTableta = "fecha_visita";
                                                        break;
                                                    case "estado_fen":
                                                        nombreCampoTableta = "phenological_state_visita";
                                                        break;
                                                    case "estado_gen_culti":
                                                        nombreCampoTableta = "overall_status_visita";
                                                        break;
                                                    case "estado_crec":
                                                        nombreCampoTableta = "growth_status_visita";
                                                        break;
                                                    case "estado_male":
                                                        nombreCampoTableta = "weed_state_visita";
                                                        break;
                                                    case "estado_fito":
                                                        nombreCampoTableta = "phytosanitary_state_visita";
                                                        break;
                                                    case "hum_del_suelo":
                                                        nombreCampoTableta = "humidity_floor_visita";
                                                        break;
                                                    case "cosecha":
                                                        nombreCampoTableta = "harvest_visita";
                                                        break;
                                                    default:
                                                        nombreCampoTableta = fs.getCampo();
                                                        break;

                                                }
                                                nombreTabla = fs.getTabla();
                                                break;
                                            case "detalle_visita_prop":
                                                nombreCampoTableta = (fs.getCampo().equals("valor")) ? "valor_detalle" : fs.getCampo();
                                                nombreTabla = fs.getTabla();
                                                break;

                                            case "usuarios":
                                                nombreCampoTableta = (fs.getCampo().equals("nombre")) ? "nombre || ' ' || apellido_p AS nombre " : fs.getCampo();
                                                nombreTabla = fs.getTabla();
                                                break;
                                            case "anexo_correo_fechas":
                                                nombreCampoTableta = fs.getCampo();
                                                nombreTabla = fs.getTabla();
                                                break;
                                            case "detalle_quotation":
                                                nombreCampoTableta = fs.getCampo();
                                                nombreTabla = "quotation";
                                                break;
                                            case "ficha":
                                                nombreCampoTableta = fs.getCampo() + "_new";
                                                nombreTabla = "ficha_new";
                                                break;
                                            case "fieldman_asis":
                                                nombreCampoTableta = " nombre || ' ' || apellido_p AS nombre ";
                                                nombreTabla = " usuarios ";
                                                break;
                                            default:
                                                nombreTabla = fs.getTabla();
                                                nombreCampoTableta = fs.getCampo();
                                        }

                                        String consulta = "SELECT " + nombreCampoTableta + " " +
                                                " FROM " + nombreTabla + " ";


                                        switch (fs.getTabla()) {
                                            case "anexo_contrato":
                                            case "condicion":
                                                consulta += " WHERE id_anexo_contrato = ? ";
                                                ob = Utilidades.appendValue(ob, prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
                                                break;
                                            case "anexo_checklist_siembra":
                                                consulta += " WHERE id_ac_cl_siembra = ? AND estado_documento = 1 ORDER BY id_cl_siembra DESC LIMIT 1 ";
                                                ob = Utilidades.appendValue(ob, prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
                                                break;

                                            case "cliente":

                                                int idCliente = MainActivity.myAppDB.myDao().getIdClienteByAnexo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
                                                consulta += " WHERE id_clientes_tabla = ? ";
                                                ob = Utilidades.appendValue(ob, idCliente);
                                                break;
                                            case "libro_campo":
                                                consulta += " INNER JOIN pro_cli_mat ON (detalle_visita_prop.id_prop_mat_cli_detalle = pro_cli_mat.id_prop_mat_cli)  ";
                                                consulta += " LEFT JOIN visita ON (detalle_visita_prop.id_visita_detalle = visita.id_visita)  ";
                                                consulta += " WHERE visita.id_anexo_visita = ? AND pro_cli_mat.campo = '" + nombreCampoTableta + "' ";
                                                consulta += " ORDER BY id_det_vis_prop_detalle DESC LIMIT 1 ";
                                                ob = Utilidades.appendValue(ob, prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
                                                break;
                                            case "comuna":
                                                consulta += " INNER JOIN ficha_new FN ON FN.id_comuna_new = comuna.id_comuna ";
                                                consulta += " INNER JOIN anexo_contrato AC ON AC.id_ficha_contrato = FN.id_ficha_new WHERE AC.id_anexo_contrato = ? ";
                                                ob = Utilidades.appendValue(ob, prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
                                                break;
                                            case "anexo_correo_fechas":
                                                consulta += "WHERE id_ac_corr_fech = ?";
                                                ob = Utilidades.appendValue(ob, prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
                                                break;
                                            case "especie":
                                                consulta += " WHERE id_especie = ? ";
                                                ob = Utilidades.appendValue(ob, prefs.getString(Utilidades.SHARED_VISIT_MATERIAL_ID, ""));
                                                break;

                                            case "materiales":
                                                String idVariedad = MainActivity.myAppDB.myDao().getIdMaterialByAnexo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
                                                consulta += " WHERE id_variedad = ? ";
                                                ob = Utilidades.appendValue(ob, idVariedad);
                                                break;
                                            case "agricultor":
                                                consulta += " INNER JOIN anexo_contrato AC ON AC.id_agricultor_anexo = agricultor.id_agricultor  WHERE id_anexo_contrato = ? ";
                                                ob = Utilidades.appendValue(ob, prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
                                                break;

//
                                            case "predio":
                                                int idPredio = MainActivity.myAppDB.myDao().getIdPredioByAnexo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
                                                consulta += " WHERE id_pred = ? ";
                                                ob = Utilidades.appendValue(ob, idPredio);
                                                break;
                                            case "lote":
                                                int idLote = MainActivity.myAppDB.myDao().getIdLoteByAnexo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
                                                consulta += " WHERE lote = ? ";
                                                ob = Utilidades.appendValue(ob, idLote);
                                                break;
                                            case "visita":
                                                int idVisita = MainActivity.myAppDB.myDao().getIdVisitaByAnexo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
                                                consulta += " WHERE id_visita = ? ";
                                                ob = Utilidades.appendValue(ob, idVisita);
                                                break;
                                            case "detalle_visita_prop":
                                                int idVisitas = MainActivity.myAppDB.myDao().getIdVisitaByAnexo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
                                                consulta += " WHERE id_det_vis_prop_detalle = ? AND id_visita_detalle = ?  ORDER BY id_det_vis_prop_detalle DESC LIMIT 1 ";
                                                ob = Utilidades.appendValue(ob, fs.getId_prop_mat_cli());
                                                ob = Utilidades.appendValue(ob, idVisitas);
                                                break;
                                            case "tipo_riego":
                                                int idTipoRiego = MainActivity.myAppDB.myDao().getIdTipoRiegoByAnexo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
                                                consulta += " WHERE id_tipo_riego = ? ";
                                                ob = Utilidades.appendValue(ob, idTipoRiego);
                                                break;
                                            case "tipo_suelo":
                                                int idTipoSuelo = MainActivity.myAppDB.myDao().getIdTipoSueloByAnexo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
                                                consulta += " WHERE id_tipo_suelo = ? ";
                                                ob = Utilidades.appendValue(ob, idTipoSuelo);
                                                break;
                                            case "ficha":
                                                consulta += " WHERE id_ficha_new = ? ";
                                                ob = Utilidades.appendValue(ob, prefs.getInt(Utilidades.SHARED_VISIT_FICHA_ID, 0));
                                                break;
                                            case "usuarios":
                                                consulta += " LEFT JOIN ficha_new FN ON FN.id_usuario_new = usuarios.id_usuario ";
                                                consulta += " INNER JOIN anexo_contrato AC ON AC.id_ficha_contrato = FN.id_ficha_new  WHERE AC.id_anexo_contrato = ? ";
                                                ob = Utilidades.appendValue(ob, prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
                                                break;
                                            case "fieldman_asis":
                                                consulta += " LEFT JOIN ficha_new FN ON FN.rut_fieldman_ass = usuarios.rut_usuario ";
                                                consulta += " INNER JOIN anexo_contrato AC ON AC.id_ficha_contrato = FN.id_ficha_new  WHERE AC.id_anexo_contrato = ? ";
                                                ob = Utilidades.appendValue(ob, prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
                                                break;
                                        }
                                        String value = MainActivity.myAppDB.myDao().getValueResume(new SimpleSQLiteQuery(consulta, ob));

                                        textViews.get(i).setText(value);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("ERROR ARRAY RESUMEN", e.getMessage());
            }


        }

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (activity != null) {
                cargarInterfaz();

            }
        }
    }


    private void bind(View view) {


    }

}
