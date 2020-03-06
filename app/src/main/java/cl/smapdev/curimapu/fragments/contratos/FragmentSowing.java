package cl.smapdev.curimapu.fragments.contratos;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
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

        global = Utilidades.cargarUI(Globalview,R.id.relative_constraint_sowing, getActivity(), prefs.getInt(Utilidades.SHARED_VISIT_MATERIAL_ID,0), fieldbook, global);
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
                global = Utilidades.cargarUI(Globalview,R.id.relative_constraint_sowing, getActivity(), prefs.getInt(Utilidades.SHARED_VISIT_MATERIAL_ID,0), fieldbook,global);
                setearOnFocus();
            }
        }
    }

    private void setearOnFocus(){

        if (global != null && global.size() > 0){
            try{
                id_generica = (ArrayList<Integer>) global.get(0);
                id_importante = (ArrayList<Integer>) global.get(1);

                editTexts = (ArrayList<EditText>) global.get(3);
                recyclerViews = (ArrayList<RecyclerView>) global.get(4);
//                ArrayList<FrameLayout> fm = (ArrayList<FrameLayout>) global.get(5);


                LinearLayout ly = Globalview.findViewById(R.id.container_linear_fotos);

                FrameLayout fm = (FrameLayout) new FrameLayout(activity);
                fm.setId(View.generateViewId());
                ly.setTag("FOTOS_" + fieldbook);

                ly.addView(fm);

                activity.getSupportFragmentManager().beginTransaction().replace(fm.getId(), FragmentFotos.getInstance(fieldbook), Utilidades.FRAGMENT_FOTOS).commit();
//                if (fm != null && fm.size() > 0){
//                    for (int i= 0; i< fm.size(); i++){
//                        String ll = "FOTOS_"+fieldbook;
//                        if (ll.equals(fm.get(i).getTag())){

//                        }
//
//                    }
//                }

                if (editTexts != null && editTexts.size() > 0){
                    for (int i = 0; i < editTexts.size(); i++){
//                    for (final EditText et : editTexts){
                        if (id_generica.contains(editTexts.get(i).getId())) {
                            int index = id_generica.indexOf(editTexts.get(i).getId());

                            switch (editTexts.get(i).getInputType()) {
                                case InputType.TYPE_CLASS_TEXT:
                                case InputType.TYPE_CLASS_NUMBER:
                                case InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL:
                                    editTexts.get(i).setText(MainActivity.myAppDB.myDao().getDatoDetalle(id_importante.get(index), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0)));
                                    break;
                                case InputType.TYPE_CLASS_DATETIME:
                                    editTexts.get(i).setText(Utilidades.voltearFechaVista(MainActivity.myAppDB.myDao().getDatoDetalle(id_importante.get(index), prefs.getInt(Utilidades.SHARED_VISIT_VISITA_ID, 0))));
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
                                                    temp.setValor_detalle(Utilidades.voltearFechaBD(editTexts.get(finalI).getText().toString()));
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
                                                ArrayList<String> det = (ArrayList<String>) MainActivity.myAppDB.myDao().getDetalleCampo(prefs.getInt(Utilidades.SHARED_VISIT_ANEXO_ID, 0), id_importante.get(index));

                                                //int corte  = MainActivity.myAppDB.myDao().getCorteCabecera(id_importante.get(index));
                                                if (det.size() > 0){
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


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(FIELDGENERIC, this.fieldbook);
    }

}
