package cl.smapdev.curimapu.fragments.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.SpinnerAdapter;
import cl.smapdev.curimapu.clases.tablas.Comuna;
import cl.smapdev.curimapu.clases.tablas.Provincia;
import cl.smapdev.curimapu.clases.tablas.Region;
import cl.smapdev.curimapu.clases.adapters.SpinnerToolbarAdapter;
import cl.smapdev.curimapu.clases.relaciones.FichasCompletas;
import cl.smapdev.curimapu.clases.tablas.Temporada;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import es.dmoral.toasty.Toasty;

public class DialogFilterFichas extends DialogFragment {

    public static final String LLAVE_ENVIO_OBJECTO = "llave_prodd";

    private Button buttonCancel, btn_aplica_filtro;
    private Spinner sp_dialog_region,sp_dialog_comuna,sp_dialog_year,sp_dialog_provincia;
    private EditText et_dialog_nombre_ag,et_dialog_of_neg, et_dialog_ha_disp;
    private RadioButton radio_todos,radio_inactiva,radio_activa,radio_rechazada;



    private final ArrayList<String> idRegiones = new ArrayList<>();
    private final ArrayList<String> idComunas = new ArrayList<>();
    private final ArrayList<String> idProvincias = new ArrayList<>();
    private final ArrayList<String> idTemporadas = new ArrayList<>();


    private List<Comuna> comunaList =  MainActivity.myAppDB.myDao().getComunas();
    private final List<Region> regionList =  MainActivity.myAppDB.myDao().getRegiones();
    private List<Provincia> provinciaList =  MainActivity.myAppDB.myDao().getProvincias();
    private final List<Temporada> years = MainActivity.myAppDB.myDao().getTemporada();


    private String idComuna,idRegion,idAnno, idProvincia;

    private SharedPreferences prefs;
    private MainActivity activity;

    private Object[] ob = new Object[]{};



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();

        View view = inflater.inflate(R.layout.dialogo_filtros_fichas, null);
        builder.setView(view);
        builder.setTitle(getResources().getString(R.string.filtros_fichas));
        bind(view);

//        years = getResources().getStringArray(R.array.anos_toolbar);



        activity = (MainActivity) getActivity();
        if (activity != null){
            prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
        }


        builder.setCancelable(false);
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();



        if (regionList != null && regionList.size() > 0){
            ArrayList<String> rg = new ArrayList<>();
            int contador = 0;
            rg.add(contador,getResources().getString(R.string.select));
            idRegiones.add(contador,"");
            contador++;

            for (Region re : regionList){
                rg.add(contador,re.getDesc_region());
                idRegiones.add(contador, re.getId_region());
                contador++;
            }

            sp_dialog_region.setAdapter(new SpinnerAdapter(Objects.requireNonNull(getActivity()),R.layout.spinner_template_view, rg));
            //sp_dialog_region.setSelection(prefs.getInt(Utilidades.SHARED_FILTER_FICHAS_REGION, 0));

        }


        if (years != null && years.size() > 0){
            ArrayList<String> rg = new ArrayList<>();
            int contador = 0;
            for (Temporada re : years){
                rg.add(contador,re.getNombre_tempo());
                idTemporadas.add(contador, re.getId_tempo_tempo());
                contador++;
            }

            sp_dialog_year.setAdapter(new SpinnerAdapter(Objects.requireNonNull(getActivity()),R.layout.spinner_template_toolbar_view, rg));
            sp_dialog_year.setSelection(prefs.getInt(Utilidades.SHARED_FILTER_FICHAS_YEAR, years.size() - 1));

        }

        int state  = prefs.getInt(Utilidades.SHARED_FILTER_FICHAS_RADIO, -1);

        switch (state){
            case -1:
            default:
            case 1:
                radio_inactiva.setChecked(true);
                break;
            case 2:
                radio_activa.setChecked(true);
                break;
            case 3:
                radio_rechazada.setChecked(true);
                break;
        }

//        sp_dialog_year.setAdapter(new SpinnerToolbarAdapter(Objects.requireNonNull(getActivity()),R.layout.spinner_template_toolbar_view, getResources().getStringArray(R.array.anos_toolbar)));


        cargarProvincia();
        cargarComuna();
        onset();


//        sp_dialog_year.setSelection(prefs.getInt(Utilidades.SHARED_FILTER_FICHAS_YEAR, getResources().getStringArray(R.array.anos_toolbar).length - 1));

    }

    private void onset(){

        sp_dialog_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idAnno = idTemporadas.get(i);
                prefs.edit().putString(Utilidades.SELECTED_ANO, idAnno).apply();
                prefs.edit().putInt(Utilidades.SHARED_FILTER_FICHAS_YEAR, i).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        radio_todos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    prefs.edit().putInt(Utilidades.SHARED_FILTER_FICHAS_RADIO, -1).apply();
                }else{
                    prefs.edit().remove(Utilidades.SHARED_FILTER_FICHAS_RADIO).apply();
                }
            }
        });


        radio_activa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    prefs.edit().putInt(Utilidades.SHARED_FILTER_FICHAS_RADIO, 1).apply();
                }else{
                    prefs.edit().remove(Utilidades.SHARED_FILTER_FICHAS_RADIO).apply();
                }
            }
        });

        radio_inactiva.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    prefs.edit().putInt(Utilidades.SHARED_FILTER_FICHAS_RADIO, 0).apply();
                }else{
                    prefs.edit().remove(Utilidades.SHARED_FILTER_FICHAS_RADIO).apply();
                }
            }
        });

        radio_rechazada.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    prefs.edit().putInt(Utilidades.SHARED_FILTER_FICHAS_RADIO, 0).apply();
                }else{
                    prefs.edit().remove(Utilidades.SHARED_FILTER_FICHAS_RADIO).apply();
                }
            }
        });


        sp_dialog_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0){
                    idRegion = idRegiones.get(i);

                    provinciaList = MainActivity.myAppDB.myDao().getProvinciaByRegion(idRegion);
                    prefs.edit().remove(Utilidades.SHARED_FILTER_FICHAS_COMUNA).apply();
                    //prefs.edit().putString(Utilidades.SHARED_FILTER_FICHAS_REGION, idRegion).apply();


                    cargarProvincia();
                }else{
                    idRegion = "";
                    //prefs.edit().remove(Utilidades.SHARED_FILTER_FICHAS_REGION).apply();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                idRegion = "";
                //prefs.edit().remove(Utilidades.SHARED_FILTER_FICHAS_REGION).apply();
            }
        });

        sp_dialog_provincia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    idProvincia = idProvincias.get(i);
                    comunaList = MainActivity.myAppDB.myDao().getComunaByProvincia(idProvincia);
                    prefs.edit().remove(Utilidades.SHARED_FILTER_FICHAS_COMUNA).apply();

                    cargarComuna();
                }else{
                    idProvincia = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                idProvincia = "";

            }
        });



        sp_dialog_comuna.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0){
                    idComuna = idComunas.get(i);
                    prefs.edit().putString(Utilidades.SHARED_FILTER_FICHAS_COMUNA, idComuna).apply();
                }else{
                    idComuna = "";
                    prefs.edit().remove(Utilidades.SHARED_FILTER_FICHAS_COMUNA).apply();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                idComuna = "";
                prefs.edit().remove(Utilidades.SHARED_FILTER_FICHAS_COMUNA).apply();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        btn_aplica_filtro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtrarFichas();
            }
        });
    }



    private void filtrarFichas(){


        try{
            String consulta = "SELECT * " +
                    " FROM ficha F " +
                    "INNER JOIN agricultor A ON (A.id_agricultor = F.id_agricultor_ficha) " +
                    "INNER JOIN region ON (region.id_region = F.id_region_ficha)" +
                    "INNER JOIN comuna ON (comuna.id_comuna = F.id_comuna_ficha)" +
                    "INNER JOIN provincia ON (provincia.id_provincia = comuna.id_provincia_comuna)" +
                    "WHERE 1 ";


            //        anno = :year

            consulta+= "AND anno =  ?";
            ob = Utilidades.appendValue(ob,prefs.getString(Utilidades.SELECTED_ANO, years.get(years.size() - 1).getId_tempo_tempo()));

            String nombre_Ag = et_dialog_nombre_ag.getText().toString();
            String of_neg = et_dialog_of_neg.getText().toString();
            String ha_disp = et_dialog_ha_disp.getText().toString();

//            int estado = (radio_todos.isChecked()) ? -1 : (radio_inactiva.isChecked()) ? 1 : (radio_activa.isChecked()) ? 2 : 3;


//            if (estado >= 0){
//                consulta += "AND F.activa != ?";
//                ob = Utilidades.appendValue(ob, 2);
//            }

            if (!TextUtils.isEmpty(ha_disp)){
                ob = Utilidades.appendValue(ob, ha_disp);
                consulta+= " AND F.has_disponible = ? ";

            }

            if (!TextUtils.isEmpty(nombre_Ag)){
                ob = Utilidades.appendValue(ob,"%"+nombre_Ag+"%");
                consulta+= " AND A.razon_social LIKE ? ";

            }

            if (!TextUtils.isEmpty(of_neg)){
                ob = Utilidades.appendValue(ob,"%"+of_neg+"%");
                consulta+= " AND F.oferta_negocio LIKE ? ";
            }

            if (!TextUtils.isEmpty(idRegion)){
                ob = Utilidades.appendValue(ob, idRegion);
                consulta+= " AND F.id_region_ficha = ? ";
            }

            if (!TextUtils.isEmpty(idComuna)){
                ob = Utilidades.appendValue(ob, idComuna);
                consulta+= " AND F.id_comuna_ficha = ? ";
            }

            consulta+= " ORDER BY F.activa ASC, F.id_ficha DESC ";

            List<FichasCompletas> fichasCompletas = MainActivity.myAppDB.myDao().getFichasFilter(new SimpleSQLiteQuery(consulta, ob));
            LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(activity);
            Intent i = new Intent("TAG_REFRESH");
            i.putExtra(LLAVE_ENVIO_OBJECTO, (Serializable) fichasCompletas);
            lbm.sendBroadcast(i);
            dismiss();

        }catch (Exception e){
            Log.e("ERROR",e.getMessage());
            Toasty.error(activity, "No pudimos encontrar lo que buscaste", Toast.LENGTH_SHORT, true).show();

            dismiss();
        }


    }




    private void cargarProvincia(){

        if (provinciaList != null && provinciaList.size() > 0){
            ArrayList<String> rg = new ArrayList<>();
            int contador = 0;
            rg.add(contador,getResources().getString(R.string.select));
            idProvincias.add(contador,"");
            contador++;
            int selectable = 0;
            for (Provincia re : provinciaList){
                rg.add(contador,re.getNombre_provincia());
                idProvincias.add(contador, re.getId_provincia());

                if (!TextUtils.isEmpty(idProvincia) && idProvincia.equals(re.getId_provincia())){
                    selectable = contador;
                }

                contador++;


            }
            sp_dialog_provincia.setAdapter(new SpinnerAdapter(Objects.requireNonNull(getActivity()),R.layout.spinner_template_toolbar_view, rg));
            //sp_dialog_provincia.setSelection(prefs.getInt(Utilidades.SHARED_FILTER_FICHAS_COMUNA, selectable));
        }
    }
    private void cargarComuna(){

        if (comunaList != null && comunaList.size() > 0){
            ArrayList<String> rg = new ArrayList<>();
            int contador = 0;
            rg.add(contador,getResources().getString(R.string.select));
            idComunas.add(contador,"");
            contador++;
            int selectable = 0;
            for (Comuna re : comunaList){
                rg.add(contador,re.getDesc_comuna());
                idComunas.add(contador, re.getId_comuna());

                if (idComuna == re.getId_comuna()){
                    selectable = contador;
                }

                contador++;


            }
            sp_dialog_comuna.setAdapter(new SpinnerAdapter(Objects.requireNonNull(getActivity()),R.layout.spinner_template_toolbar_view, rg));
            //sp_dialog_comuna.setSelection(prefs.getInt(Utilidades.SHARED_FILTER_FICHAS_COMUNA, selectable));
        }
    }


    private void bind(View view){
        buttonCancel = view.findViewById(R.id.btn_cancela_filtro);
        btn_aplica_filtro = view.findViewById(R.id.btn_aplica_filtro);

        sp_dialog_region = view.findViewById(R.id.sp_dialog_region);
        sp_dialog_comuna = view.findViewById(R.id.sp_dialog_comuna);
        sp_dialog_year = view.findViewById(R.id.sp_dialog_year);
        sp_dialog_provincia = view.findViewById(R.id.sp_dialog_provincia);



        et_dialog_nombre_ag = view.findViewById(R.id.et_dialog_nombre_ag);
        et_dialog_of_neg = view.findViewById(R.id.et_dialog_of_neg);
        et_dialog_ha_disp = view.findViewById(R.id.et_dialog_ha_disp);


        radio_todos = view.findViewById(R.id.radio_todos);
        radio_inactiva = view.findViewById(R.id.radio_inactiva);
        radio_activa = view.findViewById(R.id.radio_activa);
        radio_rechazada = view.findViewById(R.id.radio_rechazada);
    }
}
