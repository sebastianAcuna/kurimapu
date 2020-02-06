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
import cl.smapdev.curimapu.Variedad;
import cl.smapdev.curimapu.clases.Especie;
import cl.smapdev.curimapu.clases.adapters.SpinnerToolbarAdapter;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

import static cl.smapdev.curimapu.fragments.dialogos.DialogFilterFichas.LLAVE_ENVIO_OBJECTO;

public class DialogFilterTables extends DialogFragment {
    public static final String LLAVE_FILTER_TABLAS = "llave_filter_tablas";


    private Button buttonCancel, btn_aplica_filtro;
    private Spinner sp_dialog_variedad,sp_dialog_especie,sp_dialog_year;
    private EditText et_dialog_anexo, et_dialog_agricultor,et_dialog_potero;

    private String [] years;

    private ArrayList<Integer> idEspecies = new ArrayList<>();
    private ArrayList<Integer> idVariedades = new ArrayList<>();

    private List<Especie> especieList =  MainActivity.myAppDB.myDao().getEspecies();
    private List<Variedad> variedadList =  MainActivity.myAppDB.myDao().getVariedades();

    private int idEspecie,idVariedad,idAnno;

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

        builder.setTitle("Filtros para contrato");

        bind(view);

        years = getResources().getStringArray(R.array.anos_toolbar);

        activity = (MainActivity) getActivity();
        if (activity != null){
            prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
        }


        if (especieList != null && especieList.size() > 0){
            String[] rg = new String[especieList.size() + 1];
            int contador = 0;
            rg[contador] = getResources().getString(R.string.select);
            idEspecies.add(contador,0);
            contador++;

            for (Especie re : especieList){
                rg[contador] = re.getDesc_especie();
                idEspecies.add(contador, re.getId_especie());
                contador++;
            }

            sp_dialog_especie.setAdapter(new SpinnerToolbarAdapter(Objects.requireNonNull(getActivity()),R.layout.spinner_template_toolbar_view, rg));
            sp_dialog_especie.setSelection(prefs.getInt(Utilidades.SHARED_FILTER_VISITAS_ESPECIE, 0));

        }

        sp_dialog_year.setAdapter(new SpinnerToolbarAdapter(Objects.requireNonNull(getActivity()),R.layout.spinner_template_toolbar_view, getResources().getStringArray(R.array.anos_toolbar)));
        sp_dialog_year.setSelection(prefs.getInt(Utilidades.SHARED_FILTER_VISITAS_YEAR, activity.getResources().getStringArray(R.array.anos_toolbar).length - 1));

        cargarVariedad();
        onset();




        return builder.create();
    }


    private void cargarVariedad(){
        if (variedadList != null && variedadList.size() > 0){
            String[] rg = new String[variedadList.size() + 1];
            int contador = 0;
            rg[contador] = getResources().getString(R.string.select);
            idVariedades.add(contador,0);
            contador++;
            int selectable = 0;
            for (Variedad re : variedadList){
                rg[contador] = re.getDesc_variedad();
                idVariedades.add(contador, re.getId_variedad());

                if (idVariedad == re.getId_variedad()){
                    selectable = contador;
                }

                contador++;


            }
            sp_dialog_variedad.setAdapter(new SpinnerToolbarAdapter(Objects.requireNonNull(getActivity()),R.layout.spinner_template_toolbar_view, rg));
            sp_dialog_variedad.setSelection(prefs.getInt(Utilidades.SHARED_FILTER_VISITAS_VARIEDAD, selectable));
        }

    }

    private void onset(){

        sp_dialog_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idAnno = Integer.parseInt(years[i]);
                prefs.edit().putInt(Utilidades.SELECTED_ANO, idAnno).apply();
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

                    prefs.edit().putInt(Utilidades.SHARED_FILTER_VISITAS_ESPECIE, idEspecie).apply();
                }else{
                    idEspecie = 0;
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

                    prefs.edit().putInt(Utilidades.SHARED_FILTER_VISITAS_VARIEDAD, idVariedad).apply();
                }else{
                    idVariedad = 0;
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
                "anexo_contrato.rut_agricultor_anexo, " +
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
                "INNER JOIN agricultor ON (agricultor.rut_agricultor = anexo_contrato.rut_agricultor_anexo) " +
                "INNER JOIN especie ON (especie.id_especie = anexo_contrato.id_especie_anexo) " +
                "INNER JOIN variedad ON (variedad.id_variedad = anexo_contrato.id_variedad_anexo) " +
                "INNER JOIN fichas ON (fichas.id_ficha= anexo_contrato.id_ficha_contrato) " +
                "WHERE 1 ";

//        anno = :year

        consulta+= " AND fichas.anno =  ?";

        ob = Utilidades.appendValue(ob,prefs.getInt(Utilidades.SELECTED_ANO, 2020));

        String dialog_anexo = et_dialog_anexo.getText().toString();
        String dialog_agricultor = et_dialog_agricultor.getText().toString();
        String dialog_potrero = et_dialog_potero.getText().toString();



        if (idEspecie > 0){
            ob = Utilidades.appendValue(ob, idEspecie);
            consulta+= " AND anexo_contrato.id_especie_anexo = ? ";
        }

        if (idVariedad > 0){
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
