package cl.smapdev.curimapu.fragments.dialogos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.ArrayList;
import java.util.List;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.CropRotationAdapter;
import cl.smapdev.curimapu.clases.adapters.GenericAdapter;
import cl.smapdev.curimapu.clases.tablas.CropRotation;
import cl.smapdev.curimapu.clases.tablas.detalle_visita_prop;
import cl.smapdev.curimapu.clases.tablas.pro_cli_mat;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.clases.utilidades.cargarUI;
import cl.smapdev.curimapu.infraestructure.utils.coroutines.ApplicationExecutors;
import es.dmoral.toasty.Toasty;

public class DialogLibroCampo extends DialogFragment {


    private View Globalview;
    private Activity activity;

    private SharedPreferences prefs;
    private int etapa = 0;

    private String temporada;
    private String variedad;

    private ArrayList<ArrayList> globalResumen = null;
    private ArrayList<Integer> id_importante = null;
    private ArrayList<Integer> id_generica = null;
    private ArrayList<TextView> textViews = null;
    private ArrayList<EditText> editTexts = null;
    private ArrayList<RecyclerView> recyclerViews = null;
    private ArrayList<ImageView> imageViews = null;
    private ArrayList<Spinner> spinners = null;
    private ArrayList<CheckBox> check = null;


    private IOnSave IOnSave;

    public interface IOnSave {
        void onSave(boolean saved);
    }

    public void setTemporada(String temporada) {
        this.temporada = temporada;
    }

    public void setVariedad(String variedad) {
        this.variedad = variedad;
    }

    public void setEtapa(int etapa) {
        this.etapa = etapa;
    }

    public static DialogLibroCampo newInstance(String temporada, String variedad, int etapa, IOnSave onSave) {
        DialogLibroCampo dg = new DialogLibroCampo();
        dg.setIOnSave(onSave);
        dg.setEtapa(etapa);
        dg.setTemporada(temporada);
        dg.setVariedad(variedad);
        return dg;
    }

    public void setIOnSave(IOnSave onSave) {
        this.IOnSave = onSave;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Log.e("onCreateDialog", "onCreateDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        activity = getActivity();

        Globalview = inflater.inflate(R.layout.fragment_sowing, null);


        builder.setView(Globalview);
        builder.setTitle(titulo());
        builder.setNegativeButton("CANCELAR", (dialog, which) -> cerrar());
        if (etapa > 1) {
            builder.setPositiveButton("GUARDAR", (dialog, which) -> onSave());
        }
        return builder.create();
    }


    private void showKeyboard(EditText editText) {
        // Obtén el InputMethodManager y muestra el teclado
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private String titulo() {
        switch (etapa) {
            default:
            case 1:
                return "Summary";
            case 2:
                return "Sowing";
            case 3:
                return "Flowering";
            case 4:
                return "Harvest";
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setTitle(titulo());

        }
    }


    public void cerrar() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarInterfaz();
        prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
        Log.e("onResume", "onResume");
    }

    public void onSave() {

        for (int i = 0; i < editTexts.size(); i++) {
            if (!id_generica.contains(editTexts.get(i).getId())) continue;


            EditText tempText = editTexts.get(i);


            int index1 = id_generica.indexOf(editTexts.get(i).getId());
            int idImportante = id_importante.get(index1);
            if (!tempText.isEnabled()) continue;
            detalle_visita_prop temp = new detalle_visita_prop();
            temp.setEstado_detalle(0);
            temp.setId_visita_detalle(0);
            temp.setId_prop_mat_cli_detalle(idImportante);

            int idAEditar = MainActivity.myAppDB.myDao().getIdDatoDetalle(idImportante, prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0));
            String datoDetalle = MainActivity.myAppDB.myDao().getDatoDetalle(idImportante, prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));

            if ((datoDetalle != null && datoDetalle.isEmpty()) && tempText.getText().toString().isEmpty()) {
                continue;
            }

            switch (tempText.getInputType()) {
                case InputType.TYPE_CLASS_TEXT:
                case InputType.TYPE_CLASS_NUMBER:
                case InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED:
                    String text = tempText.getText().toString().toUpperCase();
                    temp.setValor_detalle(text);
                    break;
                case InputType.TYPE_CLASS_DATETIME:
                    String prevValue = tempText.getText().toString();
                    String fe = Utilidades.voltearFechaBD(prevValue);
                    fe = (fe.isEmpty()) ? prevValue : fe;
                    temp.setValor_detalle(fe);
                    break;
            }


            if (idAEditar > 0) {
                temp.setId_det_vis_prop_detalle(idAEditar);
                MainActivity.myAppDB.myDao().updateDatoDetalle(temp);
            } else {
                MainActivity.myAppDB.myDao().insertDatoDetalle(temp);
            }

        }

        Dialog dialog = getDialog();
        IOnSave.onSave(true);
        if (dialog != null) {
            dialog.dismiss();
        }
    }


    public void cargarInterfaz() {
        ApplicationExecutors exec = new ApplicationExecutors();

        ProgressDialog progressBarResumen = new ProgressDialog(activity);
        progressBarResumen.setTitle("cargando interfaz...");
        if (!progressBarResumen.isShowing()) {
            progressBarResumen.show();
        }
        SharedPreferences pref = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);

        exec.getBackground().execute(() -> {
            int idClienteFinal = MainActivity.myAppDB.myDao().getIdClienteByAnexo(pref.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));

            exec.getMainThread().execute(() -> {
                if (Globalview != null) {
                    globalResumen = cargarUI.cargarUI(Globalview, R.id.relative_constraint_sowing, activity, variedad, etapa, idClienteFinal, globalResumen, temporada);
                    if (progressBarResumen.isShowing() && setearOnFocus()) {
                        progressBarResumen.dismiss();
                    }
                }
            });
        });

        exec.shutDownBackground();
    }


    private void setearTextViews(int idClienteFinal) {

        if (textViews == null || textViews.isEmpty()) return;

        for (int i = 0; i < textViews.size(); i++) {
            if (!id_generica.contains(textViews.get(i).getId())) continue;

            int index = id_generica.indexOf(textViews.get(i).getId());
            int idImportante = id_importante.get(index);

            pro_cli_mat fs = MainActivity.myAppDB.myDao().getProCliMatByIdProp(idImportante, idClienteFinal, prefs.getString(Utilidades.SHARED_VISIT_TEMPORADA, "1"));
            if (fs == null || !fs.getForaneo().equals("SI")) continue;


            Object[] ob = new Object[]{};
            String nombreCampoTableta = "";
            String nombreTabla = "";
            if (fs.getTabla().isEmpty()) continue;
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
                case "anexo_correo_fechas":
                    nombreCampoTableta = fs.getCampo();
                    nombreTabla = fs.getTabla();
                    break;

                case "usuarios":
                    nombreCampoTableta = (fs.getCampo().equals("nombre")) ? "nombre || ' ' || apellido_p AS nombre " : fs.getCampo();
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
                case "anexo_correo_fechas":
                    consulta += "WHERE id_ac_corr_fech = ?";
                    ob = Utilidades.appendValue(ob, prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
                    break;
                case "cliente":

                    int idCliente = MainActivity.myAppDB.myDao().getIdClienteByAnexo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
                    consulta += " WHERE id_clientes_tabla = ? ";
                    ob = Utilidades.appendValue(ob, idCliente);
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
                case "comuna":
                    consulta += " INNER JOIN ficha_new FN ON FN.id_comuna_new = comuna.id_comuna ";
                    consulta += " INNER JOIN anexo_contrato AC ON AC.id_ficha_contrato = FN.id_ficha_new WHERE AC.id_anexo_contrato = ? ";
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

    private void setearImageViews() {
        if (imageViews == null || imageViews.isEmpty()) return;


        for (int i = 0; i < imageViews.size(); i++) {
            if (id_generica.contains(imageViews.get(i).getId())) {

                final int index = id_generica.indexOf(imageViews.get(i).getId());
                final int finalI = i;

                if (prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0) <= 0) {
                    imageViews.get(i).setOnClickListener(v -> {
                        imageViews.get(finalI).requestFocus();
//                        avisoActivaFicha(id_importante.get(index));
                    });

                    imageViews.get(i).setOnFocusChangeListener((v, hasFocus) -> {
                        if (hasFocus) {
//                            avisoActivaFicha(id_importante.get(index));
                        }
                    });
                }
            }
        }
    }

    private void setearChecks() {
        if (check == null || check.isEmpty()) return;

        for (int i = 0; i < check.size(); i++) {

            if (!id_generica.contains(check.get(i).getId())) continue;

            final int index = id_generica.indexOf(check.get(i).getId());

            check.get(i).setEnabled((prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0) <= 0 && etapa > 0));
            String datoDetalle = MainActivity.myAppDB.myDao().getDatoDetalle(id_importante.get(index), prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));

            if (datoDetalle != null && !datoDetalle.isEmpty()) {
                check.get(i).setChecked((datoDetalle.equals("1")));
                check.get(i).setEnabled(false);
            }


            check.get(i).setOnCheckedChangeListener((buttonView, isChecked) -> {

                detalle_visita_prop temp = new detalle_visita_prop();
                if (isChecked) {
                    temp.setValor_detalle(String.valueOf(1));
                    temp.setEstado_detalle(0);
                    temp.setId_visita_detalle(0);
                    temp.setId_prop_mat_cli_detalle(id_importante.get(index));
                    MainActivity.myAppDB.myDao().insertDatoDetalle(temp);
                } else {
                    int idAEditar = MainActivity.myAppDB.myDao().getIdDatoDetalle(id_importante.get(index), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0));
                    temp.setId_det_vis_prop_detalle(idAEditar);
                    temp.setValor_detalle(String.valueOf(0));
                    temp.setEstado_detalle(0);
                    temp.setId_visita_detalle(0);
                    temp.setId_prop_mat_cli_detalle(id_importante.get(index));
                    MainActivity.myAppDB.myDao().updateDatoDetalle(temp);
                }
            });
        }
    }

    private void setearEditTexts(int idClienteFinal) {
        if (editTexts == null || editTexts.isEmpty()) return;

        for (int i = 0; i < editTexts.size(); i++) {
            if (!id_generica.contains(editTexts.get(i).getId())) continue;

            int index = id_generica.indexOf(editTexts.get(i).getId());

            int idImportante = id_importante.get(index);

            pro_cli_mat fs = MainActivity.myAppDB.myDao().getProCliMatByIdProp(idImportante, idClienteFinal, temporada);
            editTexts.get(i).setEnabled((prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0) <= 0 && etapa > 0));

            switch (editTexts.get(i).getInputType()) {
                case InputType.TYPE_CLASS_TEXT:
                case InputType.TYPE_CLASS_NUMBER:
                case InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED:
                    String datoDetalle = "";
                    datoDetalle = MainActivity.myAppDB.myDao().getDatoDetalle(id_importante.get(index), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0));
                    if ((datoDetalle == null) || (TextUtils.isEmpty(datoDetalle) && datoDetalle.isEmpty())) {
                        datoDetalle = MainActivity.myAppDB.myDao().getDatoDetalle(id_importante.get(index), prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
                        if (!TextUtils.isEmpty(datoDetalle)) {
                            editTexts.get(i).setEnabled(false);
                        }
                    }
                    editTexts.get(i).setText(datoDetalle);


                    break;
                case InputType.TYPE_CLASS_DATETIME:

                    String datoDetalleFecha = "";
                    datoDetalleFecha = MainActivity.myAppDB.myDao().getDatoDetalle(id_importante.get(index), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0));
                    if ((datoDetalleFecha == null) || (TextUtils.isEmpty(datoDetalleFecha) && datoDetalleFecha.isEmpty())) {
                        datoDetalleFecha = MainActivity.myAppDB.myDao().getDatoDetalle(id_importante.get(index), prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
                        if (!TextUtils.isEmpty(datoDetalleFecha)) {
                            editTexts.get(i).setEnabled(false);
                        }
                    }
                    String nuevaFecha = Utilidades.voltearFechaVista(datoDetalleFecha);
                    nuevaFecha = (nuevaFecha.isEmpty()) ? datoDetalleFecha : nuevaFecha;
                    editTexts.get(i).setText(nuevaFecha);
                    final int finalI1 = i;
                    if (editTexts.get(i).isEnabled()) {
                        editTexts.get(i).setOnClickListener(v -> Utilidades.levantarFecha(editTexts.get(finalI1), activity));
                    }

                    break;
            }

            final int finalI = i;

            EditText tempText = editTexts.get(finalI);


            tempText.setOnFocusChangeListener((view, b) -> {

                int index1 = id_generica.indexOf(editTexts.get(finalI).getId());


                switch (tempText.getInputType()) {
                    case InputType.TYPE_CLASS_TEXT:
                    case InputType.TYPE_CLASS_NUMBER:
                    case InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED:
                        if (!b) {
                            if (TextUtils.isEmpty(tempText.getText().toString())) {
                                break;
                            }
                            String text = tempText.getText().toString().toUpperCase();

                            if (fs.getIdentificador().equals("50") || fs.getIdentificador().equals("51")) {
                                try {
                                    text = Utilidades.formatearCoordenada(text);
                                    tempText.setText(text);
                                } catch (Exception e) {
                                    Toasty.error(requireActivity(), (e.getMessage() != null) ? e.getMessage() : "coordenada invalida", Toast.LENGTH_LONG, true).show();

                                    break;
                                }
                            }
                            detalle_visita_prop temp = new detalle_visita_prop();
                            temp.setValor_detalle(text);
                            temp.setEstado_detalle(0);
                            temp.setId_visita_detalle(0);
                            temp.setId_prop_mat_cli_detalle(id_importante.get(index1));

                            int idAEditar = MainActivity.myAppDB.myDao().getIdDatoDetalle(id_importante.get(index1), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0));
                            if (idAEditar > 0) {
                                temp.setId_det_vis_prop_detalle(idAEditar);
                                MainActivity.myAppDB.myDao().updateDatoDetalle(temp);
                            } else {
                                MainActivity.myAppDB.myDao().insertDatoDetalle(temp);
                            }
                        } else {
                            showKeyboard(tempText);
                        }
                        break;
                    case InputType.TYPE_CLASS_DATETIME:
                        if (b) {
                            Utilidades.levantarFecha(editTexts.get(finalI), activity);
                        } else {
                            if (!TextUtils.isEmpty(editTexts.get(finalI).getText().toString())) {
                                detalle_visita_prop temp = new detalle_visita_prop();

                                String prevValue = editTexts.get(finalI).getText().toString();
                                String fe = Utilidades.voltearFechaBD(prevValue);

                                fe = (fe.isEmpty()) ? prevValue : fe;

                                temp.setValor_detalle(fe);
                                temp.setEstado_detalle(0);
                                temp.setId_visita_detalle(0);
                                temp.setId_prop_mat_cli_detalle(id_importante.get(index1));

                                int idAEditar = MainActivity.myAppDB.myDao().getIdDatoDetalle(id_importante.get(index1), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0));
                                if (idAEditar > 0) {
                                    temp.setId_det_vis_prop_detalle(idAEditar);
                                    MainActivity.myAppDB.myDao().updateDatoDetalle(temp);
                                } else {
                                    MainActivity.myAppDB.myDao().insertDatoDetalle(temp);
                                }
                            }
                        }
                        break;
                }
            });
        }

    }


    private void setearSpinners() {
        if (spinners == null || spinners.isEmpty()) return;

//        for (int i = 0; i < spinners.size(); i++) {
//            if (!id_generica.contains(spinners.get(i).getId())) continue;
//
//            int index = id_generica.indexOf(spinners.get(i).getId());
//            spinners.get(i).setEnabled((prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0) <= 0 && etapa > 0));
//            spinners.get(i).setAdapter(new SpinnerAdapter(activity, R.layout.spinner_template_view, um));
//
//        }

    }

    private void setearRecyclerViews(int idClienteFinal) {
        if (recyclerViews == null || recyclerViews.isEmpty()) return;


        for (int i = 0; i < recyclerViews.size(); i++) {
            if (!id_generica.contains(recyclerViews.get(i).getId())) continue;

            int index = id_generica.indexOf(recyclerViews.get(i).getId());
            List<pro_cli_mat> lista = MainActivity.myAppDB.myDao().getProCliMatByIdProp(id_importante.get(index), prefs.getString(Utilidades.SHARED_VISIT_MATERIAL_ID, ""), idClienteFinal, prefs.getString(Utilidades.SHARED_VISIT_TEMPORADA, "1"));
            if (lista.isEmpty()) continue;

            int idOld = 0;
            for (pro_cli_mat ls : lista) {
                if (ls.getEs_lista().equals("NO")) continue;


                String[] tipos = ls.getTipo_cambio().split("_");
                if (tipos[1].equals("UNO")) {
                    List<CropRotation> l = MainActivity.myAppDB.myDao().getCropRotation(prefs.getInt(Utilidades.SHARED_VISIT_FICHA_ID, 0));
                    if (l.size() > 0) {
                        CropRotationAdapter cropRotationAdapter = new CropRotationAdapter(l, activity);
                        recyclerViews.get(i).setAdapter(cropRotationAdapter);
                    }
                } else if (tipos[1].equals("GENERICO")) {

                    String idTag = "lista_" + id_importante.get(index);
                    if (idOld != id_importante.get(index) && recyclerViews.get(i).getTag().equals(idTag)) {
                        ArrayList<String> det = (ArrayList<String>) MainActivity.myAppDB.myDao().getDetalleCampo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""), id_importante.get(index));

                        //int corte  = MainActivity.myAppDB.myDao().getCorteCabecera(id_importante.get(index));
                        if (det.size() > 0) {

                            for (int o = 0; o < textViews.size(); o++) {
                                if (textViews.get(o).getTag() != null && textViews.get(o).getTag().equals("VISTAS_" + ls.getId_prop())) {
                                    textViews.get(o).setVisibility(View.VISIBLE);
                                }
                            }
                            List<pro_cli_mat> Popr = MainActivity.myAppDB.myDao().getProCliMatByIdProp(ls.getId_prop(), prefs.getString(Utilidades.SHARED_VISIT_MATERIAL_ID, ""), idClienteFinal, prefs.getString(Utilidades.SHARED_VISIT_TEMPORADA, "1"));
                            ArrayList<String> strings = new ArrayList<>();
                            for (String el : det) {
                                try {
                                    String[] columnas = el.split("&&");
                                    if (columnas.length > 0) {
                                        String valoresAndColumnas = "";
                                        int contadorColumnas = 1;
                                        for (int e = 0; e < columnas.length; e++) {

                                            String[] valtemp = columnas[e].split("--");
                                            valoresAndColumnas += valtemp[0] + "--" + valtemp[1] + " &&";

                                            if (contadorColumnas == Popr.size()) {
                                                strings.add(valoresAndColumnas);
                                                valoresAndColumnas = "";
                                                contadorColumnas = 1;
                                            } else {
                                                contadorColumnas++;
                                            }
                                        }
                                    }

                                } catch (Exception e) {
                                    Log.e("ERROR SPLIT", e.getMessage());
                                }
                            }

                            GenericAdapter genericAdapter = new GenericAdapter(strings, activity);
                            recyclerViews.get(i).setAdapter(genericAdapter);
                        }
                    }
                    idOld = id_importante.get(index);
                }

            }
        }

    }

    private boolean setearOnFocus() {
        ProgressDialog progressBarResumen = new ProgressDialog(activity);
        progressBarResumen.setTitle("preparando data...");
        if (!progressBarResumen.isShowing()) {
            progressBarResumen.show();
        }

        int idClienteFinal = MainActivity.myAppDB.myDao().getIdClienteByAnexo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
        if (globalResumen == null || globalResumen.isEmpty()) return true;
        try {
            id_generica = (ArrayList<Integer>) globalResumen.get(0);
            id_importante = (ArrayList<Integer>) globalResumen.get(1);
            textViews = (ArrayList<TextView>) globalResumen.get(2);
            editTexts = (ArrayList<EditText>) globalResumen.get(3);
            recyclerViews = (ArrayList<RecyclerView>) globalResumen.get(4);
            imageViews = (ArrayList<ImageView>) globalResumen.get(5);
            spinners = (ArrayList<Spinner>) globalResumen.get(6);
            check = (ArrayList<CheckBox>) globalResumen.get(7);

            setearTextViews(idClienteFinal);
            setearImageViews();
            setearChecks();
            setearEditTexts(idClienteFinal);
            setearSpinners();
            setearRecyclerViews(idClienteFinal);

        } catch (Exception e) {
            Log.e("ERROR ARRAY EDIT TEXT", e.getLocalizedMessage());
        }

        progressBarResumen.dismiss();

        return true;

    }

}
