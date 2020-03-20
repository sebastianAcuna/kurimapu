package cl.smapdev.curimapu.fragments.contratos;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
    private ArrayList<String> um = null;
    private ArrayList<String> idUm = null;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity a = (MainActivity) getActivity();
        if (a != null) {
            activity = a;
        }

        prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);

        id_importante = new ArrayList<>();
        global = new ArrayList<>();
        id_generica = new ArrayList<>();
        textViews = new ArrayList<>();
        editTexts = new ArrayList<>();
        recyclerViews = new ArrayList<>();
        imageViews = new ArrayList<>();

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

        global = cargarUI.cargarUI(Globalview,R.id.relative_constraint_sowing, getActivity(), prefs.getString(Utilidades.SHARED_VISIT_MATERIAL_ID,""), fieldbook, global);
        setearOnFocus();
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
//                AsyncTask.execute(new Runnable() {
//                    @Override
//                    public void run() {
                        global = cargarUI.cargarUI(Globalview,R.id.relative_constraint_sowing, getActivity(), prefs.getString(Utilidades.SHARED_VISIT_MATERIAL_ID,""), fieldbook,global);
                        setearOnFocus();
//                    }
//                });

            }
        }
    }

    private void setearOnFocus(){

        if (global != null && global.size() > 0){
            try{
                id_generica = (ArrayList<Integer>) global.get(0);
                id_importante = (ArrayList<Integer>) global.get(1);
                textViews = (ArrayList<TextView>) global.get(2);
                editTexts = (ArrayList<EditText>) global.get(3);
                recyclerViews = (ArrayList<RecyclerView>) global.get(4);
                imageViews = (ArrayList<ImageView>) global.get(5);

                if (Globalview.findViewWithTag("FOTOS_"+fieldbook) == null){
                    LinearLayout ly = Globalview.findViewById(R.id.container_linear_fotos);
                    FrameLayout fm = (FrameLayout) new FrameLayout(activity);
                    fm.setId(View.generateViewId());
                    fm.setTag("FOTOS_" + fieldbook);

                    ly.addView(fm);

                    activity.getSupportFragmentManager().beginTransaction().replace(fm.getId(), FragmentFotos.getInstance(fieldbook), Utilidades.FRAGMENT_FOTOS).commit();
                }

                if (imageViews != null && imageViews.size() > 0){
                    for (int i = 0; i < imageViews.size(); i++){
                        if (id_generica.contains(imageViews.get(i).getId())) {

                            final int index = id_generica.indexOf(imageViews.get(i).getId());

                            imageViews.get(i).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    avisoActivaFicha(id_importante.get(index));
                                    //Toast.makeText(activity, "SE ABRIRA POPUP CON ID IMPORTANTE " + id_importante.get(index) + " E ID GENERICA " + v.getId(), Toast.LENGTH_SHORT).show();
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

                            editTexts.get(i).setEnabled((prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0) <= 0));

                            switch (editTexts.get(i).getInputType()) {
                                case InputType.TYPE_CLASS_TEXT:
                                case InputType.TYPE_CLASS_NUMBER:
                                case InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL:
                                    String datoDetalle="";
                                    datoDetalle = MainActivity.myAppDB.myDao().getDatoDetalle(id_importante.get(index), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0));
                                    if ((datoDetalle == null) || (TextUtils.isEmpty(datoDetalle) && datoDetalle.equals(""))){
                                        datoDetalle = MainActivity.myAppDB.myDao().getDatoDetalle(id_importante.get(index), prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));
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
                                    }
                                    editTexts.get(i).setText(Utilidades.voltearFechaVista(datoDetalleFecha));
                                    final int finalI1 = i;
                                    editTexts.get(i).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            levantarFecha(editTexts.get(finalI1));
                                        }
                                    });

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
                                        case InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL:
                                            if (!b) {
                                                if (!TextUtils.isEmpty(editTexts.get(finalI).getText().toString())){
                                                    detalle_visita_prop temp = new detalle_visita_prop();
                                                    temp.setValor_detalle(editTexts.get(finalI).getText().toString());
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

                if (recyclerViews != null && recyclerViews.size() > 0){
                    for (int i = 0; i < recyclerViews.size(); i++){
                        if (id_generica.contains(recyclerViews.get(i).getId())){
                            int index  = id_generica.indexOf(recyclerViews.get(i).getId());
                            List<pro_cli_mat> lista = MainActivity.myAppDB.myDao().getProCliMatByIdProp(id_importante.get(index));
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
                                                    GenericAdapter genericAdapter = new GenericAdapter(det, activity);
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
        String[] fechaRota = fecha.split("-");
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
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }

    private void avisoActivaFicha(final int id_importante) {
        final View viewInfalted = LayoutInflater.from(getActivity()).inflate(R.layout.alert_empty, null);

        final ArrayList<Integer> id_g = new ArrayList<>();
        final ArrayList<Integer> id_i = new ArrayList<>();
        final ArrayList<EditText> editTextsView = new ArrayList<>();
        final ArrayList<Spinner> spinners = new ArrayList<>();


        final List<pro_cli_mat> list = MainActivity.myAppDB.myDao().getProCliMatByIdProp(id_importante);
        ConstraintLayout constraintLayout = (ConstraintLayout) viewInfalted.findViewById(R.id.container_alert_empty);
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
                        eh = (EditText) new EditText(new ContextThemeWrapper(activity, R.style.sub_titles_forms));
                        eh.setInputType(InputType.TYPE_CLASS_TEXT);
                        eh.setHint(activity.getResources().getString(R.string.valor));
                        break;
                    case "DECIMAL":
                        eh = (EditText) new EditText(new ContextThemeWrapper(activity, R.style.sub_titles_forms));
                        eh.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        eh.setHint(activity.getResources().getString(R.string.valor));
                        break;
                    case "DATE":
                        eh = (EditText) new EditText(new ContextThemeWrapper(activity, R.style.input_date_forms));
                        eh.setInputType(InputType.TYPE_CLASS_DATETIME);
                        break;
                    case "SPINNER":
                        etS = (Spinner) new Spinner(activity);
                        break;
                }

                View et = null;
                et = (eh != null) ? (EditText) eh : (Spinner) etS;

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

                editTextsView.get(i).setEnabled((prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0) <= 0));

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
                    spinners.get(i).setEnabled((prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0) <= 0));
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
                        if (editTextsView.size() > 0) {
                            for (int i = 0; i < editTextsView.size(); i++) {
                                if (TextUtils.isEmpty(editTextsView.get(i).getText().toString())){
                                    vacio = true;
                                }
                            }
                        }

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

                                    int idAEditar = MainActivity.myAppDB.myDao().getIdDatoDetalle(id_i.get(index), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0));
                                    if (idAEditar > 0) {
                                        temp.setId_det_vis_prop_detalle(idAEditar);
                                        MainActivity.myAppDB.myDao().updateDatoDetalle(temp);
                                    } else {
                                        MainActivity.myAppDB.myDao().insertDatoDetalle(temp);
                                    }
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
                                    int idAEditar = MainActivity.myAppDB.myDao().getIdDatoDetalle(id_i.get(index), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0));
                                    if (idAEditar > 0) {
                                        temp.setId_det_vis_prop_detalle(idAEditar);
                                        MainActivity.myAppDB.myDao().updateDatoDetalle(temp);
                                    } else {
                                        MainActivity.myAppDB.myDao().insertDatoDetalle(temp);
                                    }
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



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(FIELDGENERIC, this.fieldbook);
    }

}
