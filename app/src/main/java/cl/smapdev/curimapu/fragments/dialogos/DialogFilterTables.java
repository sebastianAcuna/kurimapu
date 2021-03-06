package cl.smapdev.curimapu.fragments.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
import cl.smapdev.curimapu.clases.tablas.Temporada;
import cl.smapdev.curimapu.clases.tablas.Variedad;
import cl.smapdev.curimapu.clases.tablas.Especie;
import cl.smapdev.curimapu.clases.adapters.SpinnerToolbarAdapter;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class DialogFilterTables extends DialogFragment {
    public static final String LLAVE_FILTER_TABLAS = "llave_filter_tablas";


    private Button buttonCancel, btn_aplica_filtro;
    private Spinner sp_dialog_variedad,sp_dialog_especie,sp_dialog_year;
    private EditText et_dialog_anexo, et_dialog_agricultor,et_dialog_potero;

    private List<Temporada> years = MainActivity.myAppDB.myDao().getTemporada();
    private ArrayList<String> idTemporadas = new ArrayList<>();
    private ArrayList<String> idEspecies = new ArrayList<>();
    private ArrayList<String> idVariedades = new ArrayList<>();

    private List<Especie> especieList =  MainActivity.myAppDB.myDao().getEspecies();
    private List<Variedad> variedadList =  MainActivity.myAppDB.myDao().getVariedades();

    private String idEspecie,idVariedad,idAnno;

    private SharedPreferences prefs;

    private MainActivity activity;

    private Object[] ob = new Object[]{};


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();

        View view = inflater.inflate(R.layout.dialogo_filtros_tabla, null);

        builder.setView(view);

        builder.setTitle(getResources().getString(R.string.filtros_contrato));

        bind(view);

//        years = getResources().getStringArray(R.array.anos_toolbar);

        activity = (MainActivity) getActivity();
        if (activity != null){
            prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
        }


        if (especieList != null && especieList.size() > 0){
            ArrayList<String> rg = new ArrayList<String>();
            int contador = 0;
            rg.add(contador, getResources().getString(R.string.select));
            idEspecies.add(contador,"");
            contador++;

            for (Especie re : especieList){
                rg.add(contador, re.getDesc_especie());
                idEspecies.add(contador, re.getId_especie());
                contador++;
            }

            sp_dialog_especie.setAdapter(new SpinnerAdapter(activity,R.layout.spinner_template_toolbar_view, rg));
            //sp_dialog_especie.setSelection(prefs.getInt(Utilidades.SHARED_FILTER_VISITAS_ESPECIE, 0));

        }

//        sp_dialog_year.setAdapter(new SpinnerToolbarAdapter(Objects.requireNonNull(getActivity()),R.layout.spinner_template_toolbar_view, getResources().getStringArray(R.array.anos_toolbar)));
//        sp_dialog_year.setSelection(prefs.getInt(Utilidades.SHARED_FILTER_VISITAS_YEAR, activity.getResources().getStringArray(R.array.anos_toolbar).length - 1));

        if (years != null && years.size() > 0){
            ArrayList<String> rg = new ArrayList<>();
            int contador = 0;
            for (Temporada re : years){
                rg.add(contador,re.getNombre_tempo());
                idTemporadas.add(contador, re.getId_tempo_tempo());
                contador++;
            }

            sp_dialog_year.setAdapter(new SpinnerAdapter(Objects.requireNonNull(getActivity()),R.layout.spinner_template_toolbar_view, rg));
            //sp_dialog_year.setSelection(prefs.getInt(Utilidades.SHARED_FILTER_VISITAS_YEAR, years.size() - 1));

        }


        cargarVariedad();
        onset();




        return builder.create();
    }


    private void cargarVariedad(){
        if (variedadList != null && variedadList.size() > 0){
            ArrayList<String> rg = new ArrayList<String>();
            int contador = 0;
            rg.add(contador, getResources().getString(R.string.select));
            idVariedades.add(contador,"");
            contador++;
            for (Variedad re : variedadList){
                rg.add(contador,re.getDesc_variedad());
                idVariedades.add(contador, re.getId_variedad());
                contador++;
            }
            sp_dialog_variedad.setAdapter(new SpinnerAdapter(activity,R.layout.spinner_template_toolbar_view, rg));
          //  sp_dialog_variedad.setSelection(prefs.getInt(Utilidades.SHARED_FILTER_VISITAS_VARIEDAD, selectable));
        }

    }

    private void onset(){

        sp_dialog_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idAnno = idTemporadas.get(i);
                prefs.edit().putString(Utilidades.SELECTED_ANO, idAnno).apply();
                prefs.edit().putInt(Utilidades.SHARED_FILTER_VISITAS_YEAR, i).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        sp_dialog_especie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0){
                    idEspecie = idEspecies.get(i);
                    variedadList = MainActivity.myAppDB.myDao().getVariedadesByEspecie(idEspecie);
                    cargarVariedad();

                    prefs.edit().putInt(Utilidades.SHARED_FILTER_VISITAS_ESPECIE, i).apply();
                }else{
                    idEspecie = "";
                    prefs.edit().remove(Utilidades.SHARED_FILTER_VISITAS_ESPECIE).apply();
                    variedadList = MainActivity.myAppDB.myDao().getVariedades();
                    cargarVariedad();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        sp_dialog_variedad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0){
                    idVariedad = idVariedades.get(i);

                    prefs.edit().putInt(Utilidades.SHARED_FILTER_VISITAS_VARIEDAD, i).apply();
                }else{
                    idVariedad = "";
                    prefs.edit().remove(Utilidades.SHARED_FILTER_VISITAS_VARIEDAD).apply();
                    
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btn_aplica_filtro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtrarFichas();
            }
        });


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }



    private void filtrarFichas(){


        String consulta = "SELECT " +
                "anexo_contrato.id_anexo_contrato, " +
                "anexo_contrato.anexo_contrato, " +
                "anexo_contrato.id_especie_anexo, " +
                "anexo_contrato.id_variedad_anexo, " +
                "anexo_contrato.id_ficha_contrato, " +
                "anexo_contrato.protero," +
                "anexo_contrato.id_agricultor_anexo, " +
                "agricultor.nombre_agricultor," +
                "agricultor.telefono_agricultor," +
                "agricultor.administrador_agricultor," +
                "agricultor.telefono_admin_agricultor," +
                "agricultor.region_agricultor," +
                "agricultor.comuna_agricultor," +
                "agricultor.agricultor_subido," +
                "agricultor.rut_agricultor, " +
                "especie.desc_especie, " +
                "variedad.desc_variedad, " +
                "fichas.anno " +
                "FROM anexo_contrato " +
                "INNER JOIN agricultor ON (agricultor.id_agricultor = anexo_contrato.id_agricultor_anexo) " +
                "INNER JOIN especie ON (especie.id_especie = anexo_contrato.id_especie_anexo) " +
                "INNER JOIN variedad ON (variedad.id_variedad = anexo_contrato.id_variedad_anexo) " +
                "INNER JOIN fichas ON (fichas.id_ficha= anexo_contrato.id_ficha_contrato) " +
                "INNER JOIN predios ON (predios.id_pred = fichas.id_pred) " +
                "INNER JOIN lote ON (lote.lote = fichas.id_lote) " +
                "WHERE 1 ";

//        anno = :year

        consulta+= " AND fichas.anno =  ?";

        ob = Utilidades.appendValue(ob,prefs.getString(Utilidades.SELECTED_ANO, years.get(years.size() - 1).getId_tempo_tempo()));

        String dialog_anexo = et_dialog_anexo.getText().toString();
        String dialog_agricultor = et_dialog_agricultor.getText().toString();
        String dialog_potrero = et_dialog_potero.getText().toString();



        if (!TextUtils.isEmpty(idEspecie)){
            ob = Utilidades.appendValue(ob, idEspecie);
            consulta+= " AND anexo_contrato.id_especie_anexo = ? ";
        }

        if (!TextUtils.isEmpty(idVariedad)){
            ob = Utilidades.appendValue(ob, idVariedad);
            consulta+= " AND anexo_contrato.id_variedad_anexo = ? ";
        }

        if (!TextUtils.isEmpty(dialog_anexo)){
            ob = Utilidades.appendValue(ob, "%"+dialog_anexo+"%");
            consulta+= " AND anexo_contrato.anexo_contrato LIKE ? ";

        }

        if (!TextUtils.isEmpty(dialog_agricultor)){
            ob = Utilidades.appendValue(ob,"%"+dialog_agricultor+"%");
            consulta+= " AND agricultor.nombre_agricultor LIKE ? ";

        }

        if (!TextUtils.isEmpty(dialog_potrero)){
            ob = Utilidades.appendValue(ob,"%"+dialog_potrero+"%");
            consulta+= " AND anexo_contrato.protero LIKE ? ";

        }


        List<AnexoCompleto> anexoCompletos = MainActivity.myAppDB.myDao().getAnexosFilter(new SimpleSQLiteQuery(consulta, ob));
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(activity);
        Intent i = new Intent("TAG_REFRESH");
        i.putExtra(LLAVE_FILTER_TABLAS, (Serializable) anexoCompletos);
        lbm.sendBroadcast(i);
        dismiss();

    }
    private void bind(View view){
        buttonCancel = (Button) view.findViewById(R.id.btn_cancela_filtro);
        btn_aplica_filtro = (Button) view.findViewById(R.id.btn_aplica_filtro);

        sp_dialog_variedad = (Spinner) view.findViewById(R.id.sp_dialog_variedad);
        sp_dialog_especie = (Spinner) view.findViewById(R.id.sp_dialog_especie);
        sp_dialog_year = (Spinner) view.findViewById(R.id.sp_dialog_year);

        et_dialog_anexo = (EditText) view.findViewById(R.id.et_dialog_anexo);
        et_dialog_agricultor = (EditText) view.findViewById(R.id.et_dialog_agricultor);
        et_dialog_potero = (EditText) view.findViewById(R.id.et_dialog_potero);


    }


}
