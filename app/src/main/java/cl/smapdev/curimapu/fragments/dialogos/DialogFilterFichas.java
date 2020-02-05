package cl.smapdev.curimapu.fragments.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.Comuna;
import cl.smapdev.curimapu.clases.Region;
import cl.smapdev.curimapu.clases.adapters.SpinnerToolbarAdapter;
import cl.smapdev.curimapu.clases.relaciones.FichasCompletas;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class DialogFilterFichas extends DialogFragment {

    public static final String LLAVE_ENVIO_OBJECTO = "llave_prodd";

    private Button buttonCancel, btn_aplica_filtro;

    private Spinner sp_dialog_region,sp_dialog_comuna;

    private EditText et_dialog_nombre_ag,et_dialog_of_neg, et_dialog_ha_disp;


    private ArrayList<Integer> idRegiones = new ArrayList<>();
    private ArrayList<Integer> idComunas = new ArrayList<>();


    private List<Comuna> comunaList =  MainActivity.myAppDB.myDao().getComunas();
    private List<Region> regionList =  MainActivity.myAppDB.myDao().getRegiones();


    private int idComuna,idRegion;

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


        buttonCancel = (Button) view.findViewById(R.id.btn_cancela_filtro);
        btn_aplica_filtro = (Button) view.findViewById(R.id.btn_aplica_filtro);

        sp_dialog_region = (Spinner) view.findViewById(R.id.sp_dialog_region);
        sp_dialog_comuna = (Spinner) view.findViewById(R.id.sp_dialog_comuna);


        et_dialog_nombre_ag = (EditText) view.findViewById(R.id.et_dialog_nombre_ag);
        et_dialog_of_neg = (EditText) view.findViewById(R.id.et_dialog_of_neg);
        et_dialog_ha_disp = (EditText) view.findViewById(R.id.et_dialog_ha_disp);




        if (regionList != null && regionList.size() > 0){
            String[] rg = new String[regionList.size() + 1];
            int contador = 0;
            rg[contador] = getResources().getString(R.string.select);
            idRegiones.add(contador,0);
            contador++;

            for (Region re : regionList){
                rg[contador] = re.getDesc_region();
                idRegiones.add(contador, re.getId_region());
                contador++;
            }

            sp_dialog_region.setAdapter(new SpinnerToolbarAdapter(Objects.requireNonNull(getActivity()),R.layout.spinner_template_toolbar_view, rg));
            sp_dialog_region.setSelection(0);

        }


        activity = (MainActivity) getActivity();
        if (activity != null){
            prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
        }

        cargarComuna();



        sp_dialog_region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0){
                    idRegion = idRegiones.get(i);
                    comunaList = MainActivity.myAppDB.myDao().getComunaByRegion(idRegion);
                    cargarComuna();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        sp_dialog_comuna.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0){
                    idComuna = idComunas.get(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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



        builder.setCancelable(false);
        return builder.create();
    }


    private void filtrarFichas(){


        String consulta = "SELECT " +
                "fichas.id_ficha, " +
                "fichas.anno, " +
                "fichas.rut_agricultor_fichas, " +
                "fichas.oferta_negocio, " +
                "fichas.localidad," +
                "fichas.has_disponible," +
                "fichas.observaciones," +
                "fichas.norting," +
                "fichas.easting," +
                "fichas.activa," +
                "fichas.subida," +
                "agricultor.nombre_agricultor," +
                "agricultor.telefono_agricultor," +
                "agricultor.administrador_agricultor," +
                "agricultor.telefono_admin_agricultor," +
                "agricultor.region_agricultor," +
                "agricultor.comuna_agricultor," +
                "agricultor.agricultor_subido," +
                "agricultor.rut_agricultor," +
                "region.id_region," +
                "region.desc_region," +
                "comuna.id_comuna," +
                "comuna.desc_comuna," +
                "comuna.id_region_comuna FROM fichas " +
                "INNER JOIN agricultor ON (agricultor.rut_agricultor = fichas.rut_agricultor_fichas) " +
                "INNER JOIN region ON (region.id_region = agricultor.region_agricultor)" +
                "INNER JOIN comuna ON (comuna.id_comuna = agricultor.comuna_agricultor)" +
                "WHERE 1 ";

//        anno = :year

        consulta+= "AND anno =  ?";
        ob = appendValue(ob,prefs.getInt(Utilidades.SELECTED_ANO, 2020));

        String nombre_Ag = et_dialog_nombre_ag.getText().toString();
        String of_neg = et_dialog_of_neg.getText().toString();
        String ha_disp = et_dialog_ha_disp.getText().toString();



        if (!TextUtils.isEmpty(ha_disp)){
            ob = appendValue(ob, ha_disp);
            consulta+= " AND fichas.has_disponible = ? ";

        }

        if (!TextUtils.isEmpty(nombre_Ag)){
            ob = appendValue(ob,"%"+nombre_Ag+"%");
            consulta+= " AND agricultor.nombre_agricultor LIKE ? ";

        }

        if (!TextUtils.isEmpty(of_neg)){
            ob = appendValue(ob,"%"+of_neg+"%");
            consulta+= " AND fichas.oferta_negocio LIKE ? ";

        }




        List<FichasCompletas> fichasCompletas = MainActivity.myAppDB.myDao().getFichasFilter(new SimpleSQLiteQuery(consulta, ob));
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(activity);
        Intent i = new Intent("TAG_REFRESH");
        i.putExtra(LLAVE_ENVIO_OBJECTO, (Serializable) fichasCompletas);
        lbm.sendBroadcast(i);
        dismiss();

    }

    private Object[] appendValue(Object[] obj, Object newObj) {

        ArrayList<Object> temp = new ArrayList<Object>(Arrays.asList(obj));
        temp.add(newObj);
        return temp.toArray();

    }


    private void cargarComuna(){

        if (comunaList != null && comunaList.size() > 0){
            String[] rg = new String[comunaList.size() + 1];
            int contador = 0;
            rg[contador] = getResources().getString(R.string.select);
            idComunas.add(contador,0);
            contador++;
            int selectable = 0;
            for (Comuna re : comunaList){
                rg[contador] = re.getDesc_comuna();
                idComunas.add(contador, re.getId_comuna());

                if (idComuna == re.getId_comuna()){
                    selectable = contador;
                }

                contador++;


            }
            sp_dialog_comuna.setAdapter(new SpinnerToolbarAdapter(Objects.requireNonNull(getActivity()),R.layout.spinner_template_toolbar_view, rg));
            sp_dialog_comuna.setSelection(selectable);
        }
    }
}
