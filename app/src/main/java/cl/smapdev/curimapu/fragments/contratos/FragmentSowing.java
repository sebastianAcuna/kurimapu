package cl.smapdev.curimapu.fragments.contratos;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.CropRotationAdapter;
import cl.smapdev.curimapu.clases.adapters.GenericAdapter;
import cl.smapdev.curimapu.clases.adapters.SpinnerAdapter;
import cl.smapdev.curimapu.clases.tablas.CropRotation;
import cl.smapdev.curimapu.clases.tablas.UnidadMedida;
import cl.smapdev.curimapu.clases.tablas.detalle_visita_prop;
import cl.smapdev.curimapu.clases.tablas.pro_cli_mat;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.clases.utilidades.cargarUI;
import es.dmoral.toasty.Toasty;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class FragmentSowing extends Fragment {


    private MainActivity activity = null;
    private static final String FIELDGENERIC = "fieldBGeneric";

    public static FragmentSowing getInstance(int fieldbook){

        FragmentSowing fragment = new FragmentSowing();
        Bundle bundle = new Bundle();
        bundle.putInt(FIELDGENERIC, fieldbook);
        fragment.setArguments(bundle);
        return fragment;
    }

    private SharedPreferences prefs;

    private int fieldbook;


    private View Globalview;

    private ArrayList<ArrayList> global = null;
    private ArrayList<Integer> id_importante = null;
    private ArrayList<Integer> id_generica = null;
    private ArrayList<TextView> textViews = null;
    private ArrayList<EditText> editTexts  = null;
    private ArrayList<RecyclerView> recyclerViews  = null;
    private ArrayList<ImageView> imageViews  = null;
    private ArrayList<Spinner> spinners  = null;
    private ArrayList<CheckBox> check  = null;


    private ArrayList<String> um = null;
    private ArrayList<String> idUm = null;

    private final String[] forbiddenWords = new String[]{"á", "é", "í", "ó", "ú", "Á", "É", "Í", "Ó", "Ú"};
    private final String[] forbiddenReplacement = new String[]{"a", "e", "i", "o", "u", "A", "E", "I", "O", "U"};



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        activity = (MainActivity) getActivity();




        prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);

        id_importante = new ArrayList<>();
        global = new ArrayList<>();
        id_generica = new ArrayList<>();
        textViews = new ArrayList<>();
        editTexts = new ArrayList<>();
        recyclerViews = new ArrayList<>();
        imageViews = new ArrayList<>();
        spinners = new ArrayList<>();
        check = new ArrayList<>();


        List<UnidadMedida> umli = MainActivity.myAppDB.myDao().getUM();
        um = new ArrayList<>();
        idUm = new ArrayList<>();
        if (umli.size()  > 0){
            for (UnidadMedida m : umli){
                um.add(m.getNombre_um());
                idUm.add(m.getId_um());
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Globalview = inflater.inflate(R.layout.fragment_sowing, container, false);
        return Globalview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        Bundle bundle = getArguments();
        if (bundle != null){
            this.fieldbook = bundle.getInt(FIELDGENERIC);
        }

        if(getUserVisibleHint()) {
            new LazyLoad(true).execute();
        }
      /*  global = cargarUI.cargarUI(Globalview,R.id.relative_constraint_sowing, getActivity(), prefs.getString(Utilidades.SHARED_VISIT_MATERIAL_ID,""), fieldbook, global);
        setearOnFocus();*/
    }


    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();
        if (bundle != null){
            this.fieldbook = bundle.getInt(FIELDGENERIC);

        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (activity != null){
//                if(fieldbook <= 0){
//                    Utilidades.avisoListo(activity, "Hey!", "Antes de completar el libro de campo debes ingresar un estado fenologico", "ENTIENDO");
//                }
//                AsyncTask.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (progressBar != null && progressBar.isShowing()){
                            new LazyLoad(false).execute();
//                        }
//                    }
//                });

                if (Globalview.findViewWithTag("FOTOS_"+fieldbook) == null){
                    if (Globalview.findViewById(R.id.container_linear_fotos) != null){
                        LinearLayout ly = Globalview.findViewById(R.id.container_linear_fotos);
                        if (ly != null){
                            if(activity != null){
                                try{
                                    FrameLayout fm = new FrameLayout(activity);
//                            if (fm != null){
                                    fm.setId(View.generateViewId());
                                    fm.setTag("FOTOS_" + fieldbook);
                                    ly.addView(fm);

                                    if(ly.findViewWithTag("FOTOS_" + fieldbook) != null){
                                        activity.getSupportFragmentManager().beginTransaction().replace(fm.getId(), FragmentFotos.getInstance(fieldbook), Utilidades.FRAGMENT_FOTOS).commit();
                                    }
//                                }
                                }catch (NullPointerException  e){
                                    Toasty.warning(activity, "No se pudo cargar las fotos", Toast.LENGTH_SHORT, true).show();
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    private void setearOnFocus(){
        int idClienteFinal  = MainActivity.myAppDB.myDao().getIdClienteByAnexo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID,""));
        if (global != null && global.size() > 0){
            try{
                id_generica = (ArrayList<Integer>) global.get(0);
                id_importante = (ArrayList<Integer>) global.get(1);
                textViews = (ArrayList<TextView>) global.get(2);
                editTexts = (ArrayList<EditText>) global.get(3);
                recyclerViews = (ArrayList<RecyclerView>) global.get(4);
                imageViews = (ArrayList<ImageView>) global.get(5);
                spinners = (ArrayList<Spinner>) global.get(6);
                check = (ArrayList<CheckBox>) global.get(7);

                if (textViews != null && textViews.size() > 0){
                    for (int i = 0; i < textViews.size(); i++){
                        if (id_generica.contains(textViews.get(i).getId())) {
                            int index = id_generica.indexOf(textViews.get(i).getId());
                            int idImportante = id_importante.get(index);

                            pro_cli_mat fs = MainActivity.myAppDB.myDao().getProCliMatByIdProp(idImportante, idClienteFinal, prefs.getString(Utilidades.SHARED_VISIT_TEMPORADA, "1"));
                            if (fs != null){
                                if (fs.getForaneo().equals("SI")){

                                    Object[] ob = new Object[]{};
                                    String nombreCampoTableta = "";
                                    String nombreTabla = "";
                                    if (!fs.getTabla().equals("")){
                                        switch (fs.getTabla()){
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
                                                switch (fs.getCampo()){
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
                                                nombreCampoTableta =  fs.getCampo();
                                                nombreTabla = "quotation";
                                                break;
                                            case "ficha":
                                                nombreCampoTableta =  fs.getCampo()+"_new";
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

                                        String  consulta = "SELECT " + nombreCampoTableta + " " +
                                                    " FROM " + nombreTabla + " ";



                                        switch (fs.getTabla()){
                                            case "anexo_contrato" :
                                                consulta += " WHERE id_anexo_contrato = ? ";
                                                ob = Utilidades.appendValue(ob,prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID,""));
                                                break;
                                            case "anexo_correo_fechas":
                                                consulta += "WHERE id_ac_corr_fech = ?";
                                                ob = Utilidades.appendValue(ob, prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID,""));
                                                break;
                                            case "cliente":

                                                int idCliente  = MainActivity.myAppDB.myDao().getIdClienteByAnexo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID,""));
                                                consulta += " WHERE id_clientes_tabla = ? ";
                                                ob = Utilidades.appendValue(ob,idCliente);
                                                break;

                                            case "especie":
                                                consulta += " WHERE id_especie = ? ";
                                                ob = Utilidades.appendValue(ob,prefs.getString(Utilidades.SHARED_VISIT_MATERIAL_ID,""));
                                                break;

                                            case "materiales":
                                                String idVariedad  = MainActivity.myAppDB.myDao().getIdMaterialByAnexo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID,""));
                                                consulta += " WHERE id_variedad = ? ";
                                                ob = Utilidades.appendValue(ob,idVariedad);
                                                break;
                                            case "agricultor":
                                                consulta += " INNER JOIN anexo_contrato AC ON AC.id_agricultor_anexo = agricultor.id_agricultor  WHERE id_anexo_contrato = ? ";
                                                ob = Utilidades.appendValue(ob,prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID,""));
                                                break;
                                            case "comuna":
                                                consulta += " INNER JOIN ficha_new FN ON FN.id_comuna_new = comuna.id_comuna ";
                                                consulta += " INNER JOIN anexo_contrato AC ON AC.id_ficha_contrato = FN.id_ficha_new WHERE AC.id_anexo_contrato = ? ";
                                                ob = Utilidades.appendValue(ob,prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID,""));
                                                break;
//
                                            case "predio":
                                                int idPredio  = MainActivity.myAppDB.myDao().getIdPredioByAnexo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID,""));
                                                consulta += " WHERE id_pred = ? ";
                                                ob = Utilidades.appendValue(ob,idPredio);
                                                break;
                                            case "lote":
                                                int idLote  = MainActivity.myAppDB.myDao().getIdLoteByAnexo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID,""));
                                                consulta += " WHERE lote = ? ";
                                                ob = Utilidades.appendValue(ob,idLote);
                                                break;
                                            case "visita":
                                                int idVisita  = MainActivity.myAppDB.myDao().getIdVisitaByAnexo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID,""));
                                                consulta += " WHERE id_visita = ? ";
                                                ob = Utilidades.appendValue(ob,idVisita);
                                                break;
                                            case "detalle_visita_prop":
                                                int idVisitas  = MainActivity.myAppDB.myDao().getIdVisitaByAnexo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID,""));
                                                consulta += " WHERE id_det_vis_prop_detalle = ? AND id_visita_detalle = ?  ORDER BY id_det_vis_prop_detalle DESC LIMIT 1 ";
                                                ob = Utilidades.appendValue(ob,fs.getId_prop_mat_cli());
                                                ob = Utilidades.appendValue(ob,idVisitas);
                                                break;
                                            case "tipo_riego":
                                                int idTipoRiego  = MainActivity.myAppDB.myDao().getIdTipoRiegoByAnexo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID,""));
                                                consulta += " WHERE id_tipo_riego = ? ";
                                                ob = Utilidades.appendValue(ob,idTipoRiego);
                                                break;
                                            case "tipo_suelo":
                                                int idTipoSuelo  = MainActivity.myAppDB.myDao().getIdTipoSueloByAnexo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID,""));
                                                consulta += " WHERE id_tipo_suelo = ? ";
                                                ob = Utilidades.appendValue(ob,idTipoSuelo);
                                                break;
                                            case "ficha":
                                                consulta += " WHERE id_ficha_new = ? ";
                                                ob = Utilidades.appendValue(ob,prefs.getInt(Utilidades.SHARED_VISIT_FICHA_ID,0));
                                                break;
                                            case "usuarios":
                                                consulta += " LEFT JOIN ficha_new FN ON FN.id_usuario_new = usuarios.id_usuario ";
                                                consulta += " INNER JOIN anexo_contrato AC ON AC.id_ficha_contrato = FN.id_ficha_new  WHERE AC.id_anexo_contrato = ? ";
                                                ob = Utilidades.appendValue(ob,prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID,""));
                                                break;
                                            case "fieldman_asis":
                                                consulta += " LEFT JOIN ficha_new FN ON FN.rut_fieldman_ass = usuarios.rut_usuario ";
                                                consulta += " INNER JOIN anexo_contrato AC ON AC.id_ficha_contrato = FN.id_ficha_new  WHERE AC.id_anexo_contrato = ? ";
                                                ob = Utilidades.appendValue(ob,prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID,""));
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

                if (imageViews != null && imageViews.size() > 0){
                    for (int i = 0; i < imageViews.size(); i++){
                        if (id_generica.contains(imageViews.get(i).getId())) {

                            final int index = id_generica.indexOf(imageViews.get(i).getId());
                            final int finalI = i;

                            if(prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0) <= 0){
                                imageViews.get(i).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        imageViews.get(finalI).requestFocus();
                                        avisoActivaFicha(id_importante.get(index));
                                    }
                                });

                                imageViews.get(i).setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                    @Override
                                    public void onFocusChange(View v, boolean hasFocus) {
                                        if (hasFocus){
                                            avisoActivaFicha(id_importante.get(index));
                                        }
                                    }
                                });
                            }
                        }
                    }
                }

                if (check != null && check.size() > 0){
                    for (int i = 0; i < check.size(); i++){
                        if (id_generica.contains(check.get(i).getId())) {

                            final int index = id_generica.indexOf(check.get(i).getId());

                            check.get(i).setEnabled((prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0) <= 0  && fieldbook > 0));

                            String datoDetalle = MainActivity.myAppDB.myDao().getDatoDetalle(id_importante.get(index), prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));

                            if (datoDetalle != null && datoDetalle.length() > 0){
                                check.get(i).setChecked((datoDetalle.equals("1")));
                                check.get(i).setEnabled(false);
                            }


                            check.get(i).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                    detalle_visita_prop temp = new detalle_visita_prop();
                                    if (isChecked){
                                        temp.setValor_detalle(String.valueOf(1));
                                        temp.setEstado_detalle(0);
                                        temp.setId_visita_detalle(0);
                                        temp.setId_prop_mat_cli_detalle(id_importante.get(index));
                                        MainActivity.myAppDB.myDao().insertDatoDetalle(temp);
                                    }else{
                                        int idAEditar = MainActivity.myAppDB.myDao().getIdDatoDetalle(id_importante.get(index), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0));
                                        temp.setId_det_vis_prop_detalle(idAEditar);
                                        temp.setValor_detalle(String.valueOf(0));
                                        temp.setEstado_detalle(0);
                                        temp.setId_visita_detalle(0);
                                        temp.setId_prop_mat_cli_detalle(id_importante.get(index));
                                        MainActivity.myAppDB.myDao().updateDatoDetalle(temp);
                                    }
                                }
                            });
                        }
                    }
                }

                if (editTexts != null && editTexts.size() > 0){
                    for (int i = 0; i < editTexts.size(); i++){
//                    for (final EditText et : editTexts){
                        if (id_generica.contains(editTexts.get(i).getId())) {
                            int index = id_generica.indexOf(editTexts.get(i).getId());

                            editTexts.get(i).setEnabled((prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0) <= 0  && fieldbook > 0));

                            switch (editTexts.get(i).getInputType()) {
                                case InputType.TYPE_CLASS_TEXT:
                                case InputType.TYPE_CLASS_NUMBER:
                                case InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED:
                                    String datoDetalle="";
                                    datoDetalle = MainActivity.myAppDB.myDao().getDatoDetalle(id_importante.get(index), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0));
                                    if ((datoDetalle == null) || (TextUtils.isEmpty(datoDetalle) && datoDetalle.equals(""))){
                                        datoDetalle = MainActivity.myAppDB.myDao().getDatoDetalle(id_importante.get(index), prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
                                        if(!TextUtils.isEmpty(datoDetalle)){
                                            editTexts.get(i).setEnabled(false);
                                        }
                                    }
                                    editTexts.get(i).setSelectAllOnFocus(true);
                                    editTexts.get(i).setText(datoDetalle);


                                    //editTexts.get(i).setEnabled((prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0) <= 0));

                                    break;
                                case InputType.TYPE_CLASS_DATETIME:

                                    String datoDetalleFecha="";
                                    datoDetalleFecha = MainActivity.myAppDB.myDao().getDatoDetalle(id_importante.get(index), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0));
                                    if ((datoDetalleFecha == null) || (TextUtils.isEmpty(datoDetalleFecha) && datoDetalleFecha.equals(""))){
                                        datoDetalleFecha = MainActivity.myAppDB.myDao().getDatoDetalle(id_importante.get(index), prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
                                        if(!TextUtils.isEmpty(datoDetalleFecha)){
                                            editTexts.get(i).setEnabled(false);
                                        }
                                    }
                                    editTexts.get(i).setText(Utilidades.voltearFechaVista(datoDetalleFecha));
                                    final int finalI1 = i;
                                    if (editTexts.get(i).isEnabled()){
                                        editTexts.get(i).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                levantarFecha(editTexts.get(finalI1));
                                            }
                                        });
                                    }

                                    break;
                            }

                            final int finalI = i;
                            editTexts.get(i).setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View view, boolean b) {

                                    int index = id_generica.indexOf(editTexts.get(finalI).getId());

                                    switch (editTexts.get(finalI).getInputType()) {
                                        case InputType.TYPE_CLASS_TEXT:
                                        case InputType.TYPE_CLASS_NUMBER:
                                        case InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED:
                                            if (!b) {
                                                if (!TextUtils.isEmpty(editTexts.get(finalI).getText().toString())){

                                                    String text = editTexts.get(finalI).getText().toString().toUpperCase();
                                                    for (int i = 0; i < forbiddenWords.length; i++){
                                                        text = text.replace(forbiddenWords[i],forbiddenReplacement[i]);
                                                    }


                                                    detalle_visita_prop temp = new detalle_visita_prop();
                                                    temp.setValor_detalle(text);
                                                    temp.setEstado_detalle(0);
                                                    temp.setId_visita_detalle(0);
                                                    temp.setId_prop_mat_cli_detalle(id_importante.get(index));

                                                    int idAEditar = MainActivity.myAppDB.myDao().getIdDatoDetalle(id_importante.get(index), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0));
                                                    if (idAEditar > 0){
                                                        temp.setId_det_vis_prop_detalle(idAEditar);
                                                        MainActivity.myAppDB.myDao().updateDatoDetalle(temp);
                                                    }else{
                                                        MainActivity.myAppDB.myDao().insertDatoDetalle(temp);
                                                    }
                                                }
                                            }
                                            break;
                                        case InputType.TYPE_CLASS_DATETIME:
                                            if (b) {
                                                levantarFecha(editTexts.get(finalI));
                                            } else {
                                                if (!TextUtils.isEmpty(editTexts.get(finalI).getText().toString())) {
                                                    detalle_visita_prop temp = new detalle_visita_prop();

                                                    String fe = Utilidades.voltearFechaBD(editTexts.get(finalI).getText().toString());

                                                    temp.setValor_detalle(fe);
                                                    temp.setEstado_detalle(0);
                                                    temp.setId_visita_detalle(0);
                                                    temp.setId_prop_mat_cli_detalle(id_importante.get(index));

                                                    int idAEditar = MainActivity.myAppDB.myDao().getIdDatoDetalle(id_importante.get(index), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0));
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
                                }
                            });
                        }
                    }
                }

                if (spinners != null && spinners.size() > 0){
                    for (int i = 0 ; i < spinners.size(); i ++){
                        if (id_generica.contains(spinners.get(i).getId())){
                            int index  = id_generica.indexOf(spinners.get(i).getId());

                            spinners.get(i).setEnabled((prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0) <= 0  && fieldbook > 0));
                            spinners.get(i).setAdapter(new SpinnerAdapter(activity, R.layout.spinner_template_view, um));

                        }
                    }
                }

                if (recyclerViews != null && recyclerViews.size() > 0){
                    for (int i = 0; i < recyclerViews.size(); i++){
                        if (id_generica.contains(recyclerViews.get(i).getId())){
                            int index  = id_generica.indexOf(recyclerViews.get(i).getId());
                            List<pro_cli_mat> lista = MainActivity.myAppDB.myDao().getProCliMatByIdProp(id_importante.get(index),prefs.getString(Utilidades.SHARED_VISIT_MATERIAL_ID,""),idClienteFinal, prefs.getString(Utilidades.SHARED_VISIT_TEMPORADA, "1"));
                            if (lista.size() > 0){
                                int idOld = 0;
                                for (pro_cli_mat ls : lista){
                                    if (ls.getEs_lista().equals("NO")){
                                        break;
                                    }else{
                                        String[] tipos = ls.getTipo_cambio().split("_");
                                        if(tipos[1].equals("UNO")){
                                            List<CropRotation> l = MainActivity.myAppDB.myDao().getCropRotation(prefs.getInt(Utilidades.SHARED_VISIT_FICHA_ID, 0));
                                            if (l.size() > 0){
                                                CropRotationAdapter cropRotationAdapter = new CropRotationAdapter(l,activity);
                                                recyclerViews.get(i).setAdapter(cropRotationAdapter);
                                            }
                                        }else if(tipos[1].equals("GENERICO")){

                                            String idTag = "lista_"+id_importante.get(index);
                                            if (idOld != id_importante.get(index) && recyclerViews.get(i).getTag().equals(idTag)){
                                                ArrayList<String> det = (ArrayList<String>) MainActivity.myAppDB.myDao().getDetalleCampo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""), id_importante.get(index));

                                                //int corte  = MainActivity.myAppDB.myDao().getCorteCabecera(id_importante.get(index));
                                                if (det.size() > 0){

                                                    for (int o = 0; o < textViews.size() ; o++){
                                                        if(textViews.get(o).getTag() != null && textViews.get(o).getTag().equals("VISTAS_"+ls.getId_prop())){
                                                            textViews.get(o).setVisibility(View.VISIBLE);
                                                        }
                                                    }
                                                    List<pro_cli_mat> Popr = MainActivity.myAppDB.myDao().getProCliMatByIdProp(ls.getId_prop(), prefs.getString(Utilidades.SHARED_VISIT_MATERIAL_ID,""),idClienteFinal,prefs.getString(Utilidades.SHARED_VISIT_TEMPORADA, "1"));
                                                    ArrayList<String> strings = new ArrayList<>();
                                                    for (String el : det){
                                                        try{
                                                            String[] columnas  = el.split("&&");
                                                            if (columnas.length > 0){
                                                                String valoresAndColumnas = "";
                                                                int contadorColumnas = 1;
                                                                for (int e = 0; e < columnas.length; e++){


                                                                    String[] valtemp = columnas[e].split("--");
                                                                    valoresAndColumnas += valtemp[0] + "--" + valtemp[1] + " &&";

                                                                    if(contadorColumnas  == Popr.size()){
                                                                        strings.add(valoresAndColumnas);
                                                                        valoresAndColumnas = "";
                                                                        contadorColumnas = 1;
                                                                    }else{
                                                                        contadorColumnas++;
                                                                    }
                                                                }
                                                            }

                                                        }catch(Exception e){
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
                        }
                    }
                }
            }catch (Exception e){
                Log.e("ERROR ARRAY EDIT TEXT", e.getLocalizedMessage());
            }

        }
    }

    private void levantarFecha(final EditText edit){


        String fecha = Utilidades.fechaActualSinHora();
        String[] fechaRota;

        if (!TextUtils.isEmpty(edit.getText())){
            try{
               fechaRota = Utilidades.voltearFechaBD(edit.getText().toString()).split("-");
            }catch (Exception e){
                fechaRota = fecha.split("-");
            }
        }else{
            fechaRota = fecha.split("-");
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

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
//                edit.setHint("");
                edit.setText(finalDate);
            }
        }, Integer.parseInt(fechaRota[0]), (Integer.parseInt(fechaRota[1]) - 1), Integer.parseInt(fechaRota[2]));
//        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }

    private void avisoActivaFicha(final int id_importante) {
        int idClienteFinal  = MainActivity.myAppDB.myDao().getIdClienteByAnexo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID,""));
        final View viewInfalted = LayoutInflater.from(getActivity()).inflate(R.layout.alert_empty, null);

        final ArrayList<Integer> id_g = new ArrayList<>();
        final ArrayList<Integer> id_i = new ArrayList<>();
        final ArrayList<EditText> editTextsView = new ArrayList<>();
        final ArrayList<Spinner> spinners = new ArrayList<>();


        final List<pro_cli_mat> list = MainActivity.myAppDB.myDao().getProCliMatByIdProp(id_importante,prefs.getString(Utilidades.SHARED_VISIT_MATERIAL_ID,""),idClienteFinal,prefs.getString(Utilidades.SHARED_VISIT_TEMPORADA, "1"));
        ConstraintLayout constraintLayout = viewInfalted.findViewById(R.id.container_alert_empty);
        ConstraintSet constraintSet = new ConstraintSet();

        View old = null;

        if (list.size() > 0) {
            int cont = 0;
            for (pro_cli_mat ls : list) {

                TextView tv = new TextView(new ContextThemeWrapper(activity, R.style.sub_titles_forms), null, 0);
                tv.setId(View.generateViewId());
                tv.setText(ls.getNombre_elemento_en());

                constraintLayout.addView(tv, cont);
                cont++;

                ViewGroup.LayoutParams propParam = tv.getLayoutParams();
                propParam.height = WRAP_CONTENT;
                propParam.width = 0;
                tv.setLayoutParams(propParam);

                String[] tipoDato = ls.getTipo_cambio().split("_");

                EditText eh = null;
                Spinner etS = null;

                switch (tipoDato[2]) {
                    case "STRING":
                    default:
                        eh = new EditText(new ContextThemeWrapper(activity, R.style.sub_titles_forms));
                        eh.setInputType(InputType.TYPE_CLASS_TEXT);
                        eh.setHint(activity.getResources().getString(R.string.valor));
                        break;
                    case "DECIMAL":
                        eh = new EditText(new ContextThemeWrapper(activity, R.style.sub_titles_forms));
                        eh.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                        eh.setHint(activity.getResources().getString(R.string.valor));
                        break;
                    case "DATE":
                        eh = new EditText(new ContextThemeWrapper(activity, R.style.input_date_forms));
                        eh.setInputType(InputType.TYPE_CLASS_DATETIME);
                        break;
                    case "SPINNER":
                        etS = new Spinner(activity);
                        break;
                }

                View et = null;
                et = (eh != null) ? eh : etS;

                et.setId(View.generateViewId());

                id_g.add(et.getId());
                id_i.add(ls.getId_prop_mat_cli());

                constraintLayout.addView(et, cont);
                cont++;

                ViewGroup.LayoutParams propParamEt = et.getLayoutParams();
                propParamEt.height = WRAP_CONTENT;
                propParamEt.width = 0;
                et.setLayoutParams(propParamEt);


                constraintSet.clone(constraintLayout);

                if (old == null) {
                    constraintSet.connect(tv.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP, 20);
                } else {
                    constraintSet.connect(tv.getId(), ConstraintSet.TOP, old.getId(), ConstraintSet.BOTTOM, 20);
                }

                constraintSet.connect(tv.getId(), ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START, 20);
                constraintSet.connect(tv.getId(), ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END, 20);


                constraintSet.connect(et.getId(), ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START, 20);
                constraintSet.connect(et.getId(), ConstraintSet.TOP, tv.getId(), ConstraintSet.BOTTOM, 20);
                constraintSet.connect(et.getId(), ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END, 20);

                constraintSet.applyTo(constraintLayout);

                old = et;

                if (tipoDato[2].equals("SPINNER")) {
                    spinners.add(etS);
                } else {
                    editTextsView.add(eh);
                }
            }
        }

        if (editTextsView.size() > 0) {
            for (int i = 0; i < editTextsView.size(); i++) {

                editTextsView.get(i).setEnabled((prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0) <= 0  && fieldbook > 0));

                if (editTextsView.get(i).getInputType() == InputType.TYPE_CLASS_DATETIME) {
                    final int finalI = i;
                    editTextsView.get(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            levantarFecha(editTextsView.get(finalI));
                        }
                    });

                    editTextsView.get(i).setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus){
                                levantarFecha(editTextsView.get(finalI));
                            }
                        }
                    });
                }
            }
        }

        if (um.size() > 0) {
            if (spinners.size() > 0) {

                for (int i = 0; i < spinners.size(); i++) {
                    spinners.get(i).setEnabled((prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0) <= 0  && fieldbook > 0));
                    spinners.get(i).setAdapter(new SpinnerAdapter(activity, R.layout.spinner_template_view, um));
                }
            }
        }


        final AlertDialog builder = new AlertDialog.Builder(activity)
                .setView(viewInfalted)
                .setTitle("Nuevo elemento")
                .setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create();


        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                Button c = builder.getButton(AlertDialog.BUTTON_NEGATIVE);

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        boolean vacio = false;
//                        if (editTextsView.size() > 0) {
//                            for (int i = 0; i < editTextsView.size(); i++) {
//                                if (TextUtils.isEmpty(editTextsView.get(i).getText().toString())){
//                                    vacio = true;
//                                }
//                            }
//                        }

                        if (!vacio){
                            if (editTextsView.size() > 0) {
                                for (int i = 0; i < editTextsView.size(); i++) {

                                    int index = id_g.indexOf(editTextsView.get(i).getId());
                                    detalle_visita_prop temp = new detalle_visita_prop();
                                    if (editTextsView.get(i).getInputType() == InputType.TYPE_CLASS_DATETIME) {
                                        String fe = Utilidades.voltearFechaBD(editTextsView.get(i).getText().toString());
                                        temp.setValor_detalle(fe);
                                    } else {
                                        temp.setValor_detalle(editTextsView.get(i).getText().toString());
                                    }

                                    temp.setEstado_detalle(0);
                                    temp.setId_visita_detalle(0);
                                    temp.setId_prop_mat_cli_detalle(id_i.get(index));

//                                    int idAEditar = MainActivity.myAppDB.myDao().getIdDatoDetalle(id_i.get(index), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0));
//                                    if (idAEditar > 0) {
//                                        temp.setId_det_vis_prop_detalle(idAEditar);
//                                        MainActivity.myAppDB.myDao().insertDatoDetalle(temp);
//                                    } else {
                                        MainActivity.myAppDB.myDao().insertDatoDetalle(temp);
//                                    }
                                }
                            }

                            if (spinners.size() > 0) {
                                for (int i = 0; i < spinners.size(); i++) {
                                    int index = id_g.indexOf(spinners.get(i).getId());
                                    detalle_visita_prop temp = new detalle_visita_prop();
                                    temp.setValor_detalle(spinners.get(i).getSelectedItem().toString());
                                    temp.setEstado_detalle(0);
                                    temp.setId_visita_detalle(0);
                                    temp.setId_prop_mat_cli_detalle(id_i.get(index));
//                                    int idAEditar = MainActivity.myAppDB.myDao().getIdDatoDetalle(id_i.get(index), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0));
//                                    if (idAEditar > 0) {
//                                        temp.setId_det_vis_prop_detalle(idAEditar);
//                                        MainActivity.myAppDB.myDao().updateDatoDetalle(temp);
//                                    } else {
                                        MainActivity.myAppDB.myDao().insertDatoDetalle(temp);
//                                    }
                                }
                            }


                            setearOnFocus();
                            builder.dismiss();
                        }else{
                            Snackbar.make(viewInfalted.getRootView(),"Debe completar todos los campos", Snackbar.LENGTH_LONG).show();
                           // Utilidades.avisoListo(activity,"Falta algo","Debe completar todos los campos antes de guardar","entiendo");
                        }
                    }
                });
                c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder.dismiss();
                    }
                });
            }
        });

        builder.setCancelable(false);
        builder.show();
    }


    private class LazyLoad extends AsyncTask<Void, Void, ArrayList<ArrayList>>{

        private ProgressDialog progressBar;
        private final boolean show;

        public LazyLoad(boolean show) {
           this.show = show;
        }

        @Override
        protected void onPreExecute() {
            if (show){
                progressBar = new ProgressDialog(activity);
                progressBar.setTitle(getResources().getString(R.string.espere));
                progressBar.setMessage("cargando interfaz...");
                progressBar.setCancelable(false);
                progressBar.show();
            }

            super.onPreExecute();
        }

        @Override
        protected ArrayList<ArrayList> doInBackground(Void... voids) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList> aVoid) {
            super.onPostExecute(aVoid);
            int idClienteFinal  = MainActivity.myAppDB.myDao().getIdClienteByAnexo(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID,""));


            if (Globalview != null){
                global = cargarUI.cargarUI(Globalview,R.id.relative_constraint_sowing, activity, prefs.getString(Utilidades.SHARED_VISIT_MATERIAL_ID,""), fieldbook,idClienteFinal,global, prefs.getString(Utilidades.SHARED_VISIT_TEMPORADA, "1"));
                setearOnFocus();
            }
            if (show && progressBar != null && progressBar.isShowing()){
                progressBar.dismiss();
            }

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getUserVisibleHint()){
            if (Globalview.findViewWithTag("FOTOS_"+fieldbook) == null){
                if (Globalview.findViewById(R.id.container_linear_fotos) != null){
                    LinearLayout ly = Globalview.findViewById(R.id.container_linear_fotos);
                    if (ly != null){
                        if(activity != null){
                            try{
                                FrameLayout fm = new FrameLayout(activity);
//                            if (fm != null){
                                fm.setId(View.generateViewId());
                                fm.setTag("FOTOS_" + fieldbook);
                                ly.addView(fm);


                                if(ly.findViewWithTag("FOTOS_" + fieldbook) != null){
                                    activity.getSupportFragmentManager().beginTransaction().replace(fm.getId(), FragmentFotos.getInstance(fieldbook), Utilidades.FRAGMENT_FOTOS).commit();
                                }
//                                }
                            }catch (NullPointerException  e){
                                Toasty.warning(activity, "No se pudo cargar las fotos", Toast.LENGTH_SHORT, true).show();
                            }
                        }
                    }
                }
            }
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(FIELDGENERIC, this.fieldbook);
    }

}
