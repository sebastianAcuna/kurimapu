package cl.smapdev.curimapu.fragments.anexoFechas;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.TablaAnexosFechas;
import cl.smapdev.curimapu.clases.adapters.SpinnerToolbarAdapter;
import cl.smapdev.curimapu.clases.relaciones.AnexoWithDates;
import cl.smapdev.curimapu.clases.relaciones.Respuesta;
import cl.smapdev.curimapu.clases.relaciones.RespuestaFecha;
import cl.smapdev.curimapu.clases.relaciones.SubirFechasRetro;
import cl.smapdev.curimapu.clases.relaciones.resFecha;
import cl.smapdev.curimapu.clases.retrofit.ApiService;
import cl.smapdev.curimapu.clases.retrofit.RetrofitClient;
import cl.smapdev.curimapu.clases.tablas.AnexoCorreoFechas;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Temporada;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.utilidades.InternetStateClass;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.clases.utilidades.returnValuesFromAsyntask;
import es.dmoral.toasty.Toasty;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAnexoFechas extends Fragment {

    private View view;

    private TablaAnexosFechas tabla;
    private MainActivity activity;
    private SharedPreferences prefs;
    private Spinner spinner_toolbar;
//    private RadioButton rb_fechas, rb_detalle;

    private List<Temporada> temporadaList;
    private final ArrayList<String> id_temporadas = new ArrayList<>();
    private final ArrayList<String> desc_temporadas = new ArrayList<>();

    private String marca_especial_temporada;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (MainActivity) getActivity();
        if (activity != null){
            prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view  =  inflater.inflate(R.layout.fragment_anexo_fecha, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinner_toolbar = view.findViewById(R.id.spinner_toolbar);


//        rb_fechas = view.findViewById(R.id.rb_fechas);
//        rb_detalle = view.findViewById(R.id.rb_detalle);


        temporadaList = MainActivity.myAppDB.myDao().getTemporada();
        if (temporadaList.size() > 0){
            for (Temporada t : temporadaList){
                id_temporadas.add(t.getId_tempo_tempo());
                desc_temporadas.add(t.getNombre_tempo());
                if(t.getEspecial_temporada() > 0){
                    marca_especial_temporada = t.getId_tempo_tempo();
                }
            }
        }

        setHasOptionsMenu(true);

        spinner_toolbar.setAdapter(new SpinnerToolbarAdapter(activity,R.layout.spinner_template_toolbar_view, temporadaList));



        if(marca_especial_temporada != null && !marca_especial_temporada.isEmpty() && spinner_toolbar != null){
            spinner_toolbar.setSelection(id_temporadas.indexOf(marca_especial_temporada));
        }

        if (activity != null){
            tabla = new TablaAnexosFechas((TableLayout) view.findViewById(R.id.tabla_anexo_fechas), activity, new TablaAnexosFechas.OnItemClickListener() {
                @Override
                public void onItemClick(AnexoWithDates plz) {
                    showAlertForDeletePieza(plz);
                }
            }, this::showAlertDetalleFechaAnexo);
        }

        spinner_toolbar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (spinner_toolbar.getTag() != null ){
                    if (Integer.parseInt(spinner_toolbar.getTag().toString()) != i){

                        prefs.edit().putString(Utilidades.SELECTED_ANO,id_temporadas.get(spinner_toolbar.getSelectedItemPosition())).apply();
                        prefs.edit().putInt(Utilidades.SHARED_FILTER_VISITAS_YEAR, i).apply();

                       cargarInforme(0);

                    }else{
                        spinner_toolbar.setTag(null);
                    }
                }else{
                    prefs.edit().putString(Utilidades.SELECTED_ANO, id_temporadas.get(spinner_toolbar.getSelectedItemPosition())).apply();
                    prefs.edit().putInt(Utilidades.SHARED_FILTER_VISITAS_YEAR, i).apply();

                    cargarInforme(0);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        if (temporadaList.size() > 0){
            cargarInforme( 0);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_sube_fechas, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_upload_files:
                prepararSubirFechas();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }



    private void prepararSubirFechas(){

        InternetStateClass mm = new InternetStateClass(activity, result -> {
            if (result) {
                ProgressDialog pd = new ProgressDialog(activity);
                pd.setMessage("conectandose a internet, espere por favor");
                pd.show();


                List<AnexoCorreoFechas> fechas = MainActivity.myAppDB.myDao().getAnexoCorreoFechas();
                if(fechas != null && fechas.size() > 0){

                    if(pd.isShowing()){
                        pd.dismiss();
                    }
                    subirFechas(fechas);
                }else{
                    if(pd.isShowing()){
                        pd.dismiss();
                    }
                    Toasty.success(activity, activity.getResources().getString(R.string.sync_all_ok), Toast.LENGTH_SHORT, true).show();
                }

            } else {
                    Toasty.error(activity, activity.getResources().getString(R.string.sync_not_internet), Toast.LENGTH_SHORT, true).show();
            }
        }, 1);
        mm.execute();
    }


    public void  subirFechas(List<AnexoCorreoFechas> fechas){

        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Preparando datos para subir...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Config config = MainActivity.myAppDB.myDao().getConfig();

        SubirFechasRetro sfp = new SubirFechasRetro();
        sfp.setId_dispo(config.getId());
        sfp.setId_usuario(config.getId_usuario());
        sfp.setFechas(fechas);

        ApiService apiService = RetrofitClient.getClient(config.getServidorSeleccionado()).create(ApiService.class);
        Call<resFecha> call = apiService.enviarFechas(sfp);
        call.enqueue(new Callback<resFecha>() {
            @Override
            public void onResponse(Call<resFecha> call, Response<resFecha> response) {

                resFecha res = response.body();

                if (res != null){

                    switch (res.getCodigoRespuesta()){
                        case 0:
                            if (res.getRespuestaFechas() != null && res.getRespuestaFechas().size() > 0){

                                boolean problema = false;
                                StringBuilder msg = new StringBuilder();

                                for (RespuestaFecha fc : res.getRespuestaFechas()){

                                    AnexoCorreoFechas acf = MainActivity.myAppDB.myDao().getAnexoCorreoFechasByAnexo(Integer.parseInt(fc.getAnexo()));
                                    if (acf != null){
                                        acf.setCorreo_termino_labores_post_cosechas(fc.getCorreo_termino_labores());
                                        acf.setCorreo_termino_cosecha(fc.getCorreo_termino_cosecha());
                                        acf.setCorreo_inicio_despano(fc.getCorreo_inicio_despano());
                                        acf.setCorreo_inicio_cosecha(fc.getCorreo_inicio_cosecha());
                                        acf.setCorreo_inicio_corte_seda(fc.getCorreo_inicio_corte_seda());
                                        acf.setCorreo_cinco_porciento_floracion(fc.getCorreo_cinco_porciento());
                                        acf.setDetalle_labores(fc.getDetalle());

                                        try{
                                            MainActivity.myAppDB.myDao().UpdateFechasAnexos(acf);
                                        }catch (Exception e){
                                            problema = true;
                                            msg.append(e.getLocalizedMessage());
                                        }
                                    }
                                }


                                if(problema){
                                    Toasty.error(activity, msg, Toast.LENGTH_LONG, true).show();
                                }else{
                                    Toasty.success(activity, "correos enviados con exito", Toast.LENGTH_LONG, true).show();
                                }

                            }
                            cargarInforme(0);
                            progressDialog.dismiss();
                            break;
                        default:
                            Toasty.error(activity, res.getMensaje(), Toast.LENGTH_SHORT, true).show();
                            System.out.println(res.getMensaje());
                            progressDialog.dismiss();
                            break;
                    }

                }else{
                    Toasty.error(activity, "Resupuesta nula del servidor", Toast.LENGTH_SHORT, true).show();
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<resFecha> call, Throwable t) {
                Toasty.error(activity, (t.getLocalizedMessage() != null) ? t.getLocalizedMessage() : "Problemas con el servicio", Toast.LENGTH_LONG, true).show();
                progressDialog.dismiss();
            }
        });


    }

    //mostar y guardar
    private void showAlertForDeletePieza(final AnexoWithDates plz){
        View viewInfalted = LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_editar_fechas,null);
        final AlertDialog builder = new AlertDialog.Builder(requireActivity())
                .setView(viewInfalted)
                .setTitle("Fechas para anexo " + plz.getAnexoCompleto().getAnexoContrato().getAnexo_contrato())
                .setPositiveButton("Guardar fechas", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).setNegativeButton("cancelar",null).create();


        final EditText et_inicio_despano = viewInfalted.findViewById(R.id.et_inicio_despano);
        final EditText et_cinco_porc_floracion = viewInfalted.findViewById(R.id.et_cinco_porc_floracion);
        final EditText et_inicio_corte_seda = viewInfalted.findViewById(R.id.et_inicio_corte_seda);
        final EditText et_inicio_cosecha = viewInfalted.findViewById(R.id.et_inicio_cosecha);
        final EditText et_termino_cosecha = viewInfalted.findViewById(R.id.et_termino_cosecha);
        final EditText et_termino_labores_post_cosecha = viewInfalted.findViewById(R.id.et_termino_labores_post_cosecha);
        final EditText et_detalle_labores = viewInfalted.findViewById(R.id.et_detalle_labores);


        final ImageView correo_envia_inicio_despano = viewInfalted.findViewById(R.id.correo_envia_inicio_despano);
        final ImageView correo_envia_cinco_porc_floracion = viewInfalted.findViewById(R.id.correo_envia_cinco_porc_floracion);
        final ImageView correo_envia_inicio_corte_seda = viewInfalted.findViewById(R.id.correo_envia_inicio_corte_seda);
        final ImageView correo_envia_inicio_cosecha = viewInfalted.findViewById(R.id.correo_envia_inicio_cosecha);
        final ImageView correo_envia_termino_cosecha = viewInfalted.findViewById(R.id.correo_envia_termino_cosecha);
        final ImageView correo_envia_termino_labores_post_cosecha = viewInfalted.findViewById(R.id.correo_envia_termino_labores_post_cosecha);



        //para mostrar vacios en vez de 0 en fecha
        et_inicio_despano.setText((plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getInicio_despano() != null && !plz.getAnexoCorreoFichas().getInicio_despano().equals("0000-00-00")) ? Utilidades.voltearFechaVista(plz.getAnexoCorreoFichas().getInicio_despano()) : "");
        et_cinco_porc_floracion.setText((plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getCinco_porciento_floracion() != null && !plz.getAnexoCorreoFichas().getCinco_porciento_floracion().equals("0000-00-00")) ? Utilidades.voltearFechaVista(plz.getAnexoCorreoFichas().getCinco_porciento_floracion()) : "");
        et_inicio_corte_seda.setText((plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getInicio_corte_seda() != null && !plz.getAnexoCorreoFichas().getInicio_corte_seda().equals("0000-00-00")) ? Utilidades.voltearFechaVista(plz.getAnexoCorreoFichas().getInicio_corte_seda()) : "");
        et_inicio_cosecha.setText((plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getInicio_cosecha() != null && !plz.getAnexoCorreoFichas().getInicio_cosecha().equals("0000-00-00")) ? Utilidades.voltearFechaVista(plz.getAnexoCorreoFichas().getInicio_cosecha()) : "");
        et_termino_cosecha.setText((plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getTermino_cosecha() != null && !plz.getAnexoCorreoFichas().getTermino_cosecha().equals("0000-00-00")) ? Utilidades.voltearFechaVista(plz.getAnexoCorreoFichas().getTermino_cosecha()) : "");
        et_termino_labores_post_cosecha.setText((plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getTermino_labores_post_cosechas() != null && !plz.getAnexoCorreoFichas().getTermino_labores_post_cosechas().equals("0000-00-00")) ? Utilidades.voltearFechaVista(plz.getAnexoCorreoFichas().getTermino_labores_post_cosechas()) : "");
        et_detalle_labores.setText((plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getDetalle_labores() != null ) ? plz.getAnexoCorreoFichas().getDetalle_labores() : "");

        // para habilitar el boton y no escribir en las que estan listas
        et_inicio_despano.setEnabled((plz.getAnexoCorreoFichas() == null || (plz.getAnexoCorreoFichas() != null && (plz.getAnexoCorreoFichas().getInicio_despano() == null || plz.getAnexoCorreoFichas().getInicio_despano().equals("0000-00-00")))));
        et_cinco_porc_floracion.setEnabled((plz.getAnexoCorreoFichas() == null || (plz.getAnexoCorreoFichas() != null && (plz.getAnexoCorreoFichas().getCinco_porciento_floracion() == null || plz.getAnexoCorreoFichas().getCinco_porciento_floracion().equals("0000-00-00")))));
        et_inicio_corte_seda.setEnabled((plz.getAnexoCorreoFichas() == null || (plz.getAnexoCorreoFichas() != null && (plz.getAnexoCorreoFichas().getInicio_corte_seda() == null || plz.getAnexoCorreoFichas().getInicio_corte_seda().equals("0000-00-00")))));
        et_inicio_cosecha.setEnabled((plz.getAnexoCorreoFichas() == null || (plz.getAnexoCorreoFichas() != null && (plz.getAnexoCorreoFichas().getInicio_cosecha() == null || plz.getAnexoCorreoFichas().getInicio_cosecha().equals("0000-00-00")))));
        et_termino_cosecha.setEnabled((plz.getAnexoCorreoFichas() == null || (plz.getAnexoCorreoFichas() != null && (plz.getAnexoCorreoFichas().getTermino_cosecha() == null || plz.getAnexoCorreoFichas().getTermino_cosecha().equals("0000-00-00")))));
        et_termino_labores_post_cosecha.setEnabled((plz.getAnexoCorreoFichas() == null || (plz.getAnexoCorreoFichas() != null && (plz.getAnexoCorreoFichas().getTermino_labores_post_cosechas() == null || plz.getAnexoCorreoFichas().getTermino_labores_post_cosechas().equals("0000-00-00")))));
        et_detalle_labores.setEnabled((plz.getAnexoCorreoFichas() == null || (plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getDetalle_labores() == null)));

        if(plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getInicio_despano() != null && plz.getAnexoCorreoFichas().getCorreo_inicio_despano() > 0){
            correo_envia_inicio_despano.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_email_24_si));
        }else{
            correo_envia_inicio_despano.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_email_24_no));
        }


        if(plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getCinco_porciento_floracion() != null && plz.getAnexoCorreoFichas().getCorreo_cinco_porciento_floracion() > 0){
            correo_envia_cinco_porc_floracion.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_email_24_si));
        }else{
            correo_envia_cinco_porc_floracion.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_email_24_no));
        }


        if(plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getInicio_corte_seda() != null && plz.getAnexoCorreoFichas().getCorreo_inicio_corte_seda() > 0){
            correo_envia_inicio_corte_seda.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_email_24_si));
        }else{
            correo_envia_inicio_corte_seda.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_email_24_no));
        }


        if(plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getInicio_cosecha() != null && plz.getAnexoCorreoFichas().getCorreo_inicio_cosecha() > 0){
            correo_envia_inicio_cosecha.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_email_24_si));
        }else{
            correo_envia_inicio_cosecha.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_email_24_no));
        }

        if(plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getTermino_cosecha() != null && plz.getAnexoCorreoFichas().getCorreo_termino_cosecha() > 0){
            correo_envia_termino_cosecha.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_email_24_si));
        }else{
            correo_envia_termino_cosecha.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_email_24_no));
        }

        if(plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getTermino_labores_post_cosechas() != null && plz.getAnexoCorreoFichas().getCorreo_termino_labores_post_cosechas() > 0){
            correo_envia_termino_labores_post_cosecha.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_email_24_si));
        }else{
            correo_envia_termino_labores_post_cosecha.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_email_24_no));
        }

        et_inicio_despano.setOnFocusChangeListener((view, b) -> {
            if (b){
                levantarFecha(et_inicio_despano);
            }
        });

        et_cinco_porc_floracion.setOnFocusChangeListener((view, b) -> {
            if (b){
                levantarFecha(et_cinco_porc_floracion);
            }
        });

        et_termino_cosecha.setOnFocusChangeListener((view, b) -> {
            if (b){
                levantarFecha(et_termino_cosecha);
            }
        });

        et_inicio_cosecha.setOnFocusChangeListener((view, b) -> {
            if (b){
                levantarFecha(et_inicio_cosecha);
            }
        });

        et_inicio_corte_seda.setOnFocusChangeListener((view, b) -> {
            if (b){
                levantarFecha(et_inicio_corte_seda);
            }
        });

        et_termino_labores_post_cosecha.setOnFocusChangeListener((view, b) -> {
            if (b){
                levantarFecha(et_termino_labores_post_cosecha);
            }
        });

        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
            final Button n = builder.getButton(AlertDialog.BUTTON_NEGATIVE);

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(!TextUtils.isEmpty(et_termino_labores_post_cosecha.getText()) && TextUtils.isEmpty(et_detalle_labores.getText())){
                        Toasty.error(activity, "Debes ingresar el detalle al ingresar el termino de labores ", Toast.LENGTH_LONG, true).show();
                    }else{

                        AnexoCorreoFechas fhc = new AnexoCorreoFechas();

                        if((plz.getAnexoCorreoFichas() == null || (plz.getAnexoCorreoFichas() != null && TextUtils.isEmpty(plz.getAnexoCorreoFichas().getInicio_despano())) || (plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getInicio_despano().equals("0000-00-00"))) &&  !TextUtils.isEmpty(et_inicio_despano.getText())){
                            fhc.setInicio_despano(Utilidades.voltearFechaBD(et_inicio_despano.getText().toString()));
                        }else if(plz.getAnexoCorreoFichas() != null && !TextUtils.isEmpty(plz.getAnexoCorreoFichas().getInicio_despano())){
                            fhc.setInicio_despano(plz.getAnexoCorreoFichas().getInicio_despano());
                        }

                        if((plz.getAnexoCorreoFichas() == null || (plz.getAnexoCorreoFichas() != null && TextUtils.isEmpty(plz.getAnexoCorreoFichas().getCinco_porciento_floracion())) || (plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getCinco_porciento_floracion().equals("0000-00-00"))) &&  !TextUtils.isEmpty(et_cinco_porc_floracion.getText())){
                            fhc.setCinco_porciento_floracion(Utilidades.voltearFechaBD(et_cinco_porc_floracion.getText().toString()));
                        }else if(plz.getAnexoCorreoFichas() != null && !TextUtils.isEmpty(plz.getAnexoCorreoFichas().getCinco_porciento_floracion())){
                            fhc.setCinco_porciento_floracion(plz.getAnexoCorreoFichas().getCinco_porciento_floracion());
                        }

                        if((plz.getAnexoCorreoFichas() == null || (plz.getAnexoCorreoFichas() != null && TextUtils.isEmpty(plz.getAnexoCorreoFichas().getTermino_cosecha())) || (plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getTermino_cosecha().equals("0000-00-00"))) &&  !TextUtils.isEmpty(et_termino_cosecha.getText())){
                            fhc.setTermino_cosecha(Utilidades.voltearFechaBD(et_termino_cosecha.getText().toString()));
                        }else if(plz.getAnexoCorreoFichas() != null && !TextUtils.isEmpty(plz.getAnexoCorreoFichas().getTermino_cosecha())){
                            fhc.setTermino_cosecha(plz.getAnexoCorreoFichas().getTermino_cosecha());
                        }

                        if((plz.getAnexoCorreoFichas() == null || (plz.getAnexoCorreoFichas() != null && TextUtils.isEmpty(plz.getAnexoCorreoFichas().getInicio_cosecha())) || (plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getInicio_cosecha().equals("0000-00-00"))) &&  !TextUtils.isEmpty(et_inicio_cosecha.getText())){
                            fhc.setInicio_cosecha(Utilidades.voltearFechaBD(et_inicio_cosecha.getText().toString()));
                        }else if(plz.getAnexoCorreoFichas() != null && !TextUtils.isEmpty(plz.getAnexoCorreoFichas().getInicio_cosecha())){
                            fhc.setInicio_cosecha(plz.getAnexoCorreoFichas().getInicio_cosecha());
                        }

                        if((plz.getAnexoCorreoFichas() == null || (plz.getAnexoCorreoFichas() != null && TextUtils.isEmpty(plz.getAnexoCorreoFichas().getInicio_corte_seda())) || (plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getInicio_corte_seda().equals("0000-00-00"))) &&  !TextUtils.isEmpty(et_inicio_corte_seda.getText())){
                            fhc.setInicio_corte_seda(Utilidades.voltearFechaBD(et_inicio_corte_seda.getText().toString()));
                        }else if(plz.getAnexoCorreoFichas() != null && !TextUtils.isEmpty(plz.getAnexoCorreoFichas().getInicio_corte_seda())){
                            fhc.setInicio_corte_seda(plz.getAnexoCorreoFichas().getInicio_corte_seda());
                        }

                        if((plz.getAnexoCorreoFichas() == null || (plz.getAnexoCorreoFichas() != null && TextUtils.isEmpty(plz.getAnexoCorreoFichas().getTermino_labores_post_cosechas())) || (plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getTermino_labores_post_cosechas().equals("0000-00-00")) ) &&  !TextUtils.isEmpty(et_termino_labores_post_cosecha.getText())){
                            fhc.setTermino_labores_post_cosechas(Utilidades.voltearFechaBD(et_termino_labores_post_cosecha.getText().toString()));
                            fhc.setDetalle_labores(et_detalle_labores.getText().toString());
                        }else if(plz.getAnexoCorreoFichas() != null && !TextUtils.isEmpty(plz.getAnexoCorreoFichas().getTermino_labores_post_cosechas())){
                            fhc.setTermino_labores_post_cosechas(plz.getAnexoCorreoFichas().getTermino_labores_post_cosechas());
                        }

                        if(plz.getAnexoCorreoFichas() != null){
                            fhc.setCorreo_cinco_porciento_floracion(plz.getAnexoCorreoFichas()
                                    .getCorreo_cinco_porciento_floracion());
                            fhc.setCorreo_inicio_corte_seda(plz.getAnexoCorreoFichas()
                                    .getCorreo_inicio_corte_seda());
                            fhc.setCorreo_inicio_cosecha(plz.getAnexoCorreoFichas().getCorreo_inicio_cosecha());
                            fhc.setCorreo_inicio_despano(plz.getAnexoCorreoFichas().getCorreo_inicio_despano());
                            fhc.setCorreo_termino_cosecha(plz.getAnexoCorreoFichas().getCorreo_termino_cosecha());
                            fhc.setCorreo_termino_labores_post_cosechas(plz.getAnexoCorreoFichas().getCorreo_termino_labores_post_cosechas());
                            fhc.setId_fieldman(plz.getAnexoCorreoFichas().getId_fieldman());
                            fhc.setId_asistente(plz.getAnexoCorreoFichas().getId_asistente());
                            fhc.setId_ac_corr_fech(plz.getAnexoCorreoFichas().getId_ac_corr_fech());
                            fhc.setId_ac_cor_fech(plz.getAnexoCorreoFichas().getId_ac_cor_fech());


                            int id = MainActivity.myAppDB.myDao().UpdateFechasAnexos(fhc);
                            if(id > 0){
                                Toasty.success(activity, "Fechas guardada con exito", Toast.LENGTH_SHORT, true).show();
                            }else{
                                Toasty.error(activity, "No se pudo guardar la fecha", Toast.LENGTH_SHORT, true).show();
                            }
                        }else{
                            Config config = MainActivity.myAppDB.myDao().getConfig();
                            fhc.setId_fieldman(config.getId_usuario());
                            fhc.setId_fieldman(config.getId_usuario());
                            fhc.setId_ac_corr_fech(Integer.parseInt(plz.getAnexoCompleto().getAnexoContrato().getId_anexo_contrato()));

                            fhc.setCorreo_cinco_porciento_floracion(0);
                            fhc.setCorreo_inicio_corte_seda(0);
                            fhc.setCorreo_inicio_cosecha(0);
                            fhc.setCorreo_inicio_despano(0);
                            fhc.setCorreo_termino_cosecha(0);
                            fhc.setCorreo_termino_labores_post_cosechas(0);

                            long id = MainActivity.myAppDB.myDao().insertFechasAnexos(fhc);
                            if(id > 0){
                                Toasty.success(activity, "Fechas guardada con exito", Toast.LENGTH_SHORT, true).show();
                            }else{
                                Toasty.error(activity, "No se pudo guardar la fecha", Toast.LENGTH_SHORT, true).show();
                            }
                        }

                        cargarInforme(0);


                        builder.dismiss();
                    }
                }
            });
            n.setOnClickListener(v -> builder.dismiss());
        });
        builder.setCancelable(false);
        builder.show();
    }

    //mostrar detalle
    private void showAlertDetalleFechaAnexo(final AnexoWithDates plz){
        View viewInfalted = LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_detalle_fechas,null);
        final AlertDialog builder = new AlertDialog.Builder(requireActivity())
                .setView(viewInfalted)
                .setTitle("Fechas para anexo " + plz.getAnexoCompleto().getAnexoContrato().getAnexo_contrato())
                .setNegativeButton("cancelar",null).create();


        final TextView et_inicio_despano = viewInfalted.findViewById(R.id.et_inicio_despano);
        final TextView et_cinco_porc_floracion = viewInfalted.findViewById(R.id.et_cinco_porc_floracion);
        final TextView et_inicio_corte_seda = viewInfalted.findViewById(R.id.et_inicio_corte_seda);
        final TextView et_inicio_cosecha = viewInfalted.findViewById(R.id.et_inicio_cosecha);
        final TextView et_termino_cosecha = viewInfalted.findViewById(R.id.et_termino_cosecha);
        final TextView et_termino_labores_post_cosecha = viewInfalted.findViewById(R.id.et_termino_labores_post_cosecha);
        final TextView et_detalle_labores = viewInfalted.findViewById(R.id.et_detalle_labores);

        final TextView asistente = viewInfalted.findViewById(R.id.asistente);
        final TextView agricultor = viewInfalted.findViewById(R.id.agricultor);
        final TextView potrero = viewInfalted.findViewById(R.id.potrero);
        final TextView especie = viewInfalted.findViewById(R.id.especie);
        final TextView comuna = viewInfalted.findViewById(R.id.comuna);
//        final TextView fecha_ultima_visita = viewInfalted.findViewById(R.id.fecha_ultima_visita);


        final ImageView correo_envia_inicio_despano = viewInfalted.findViewById(R.id.correo_envia_inicio_despano);
        final ImageView correo_envia_cinco_porc_floracion = viewInfalted.findViewById(R.id.correo_envia_cinco_porc_floracion);
        final ImageView correo_envia_inicio_corte_seda = viewInfalted.findViewById(R.id.correo_envia_inicio_corte_seda);
        final ImageView correo_envia_inicio_cosecha = viewInfalted.findViewById(R.id.correo_envia_inicio_cosecha);
        final ImageView correo_envia_termino_cosecha = viewInfalted.findViewById(R.id.correo_envia_termino_cosecha);
        final ImageView correo_envia_termino_labores_post_cosecha = viewInfalted.findViewById(R.id.correo_envia_termino_labores_post_cosecha);


        Usuario usuario = null;
        if(plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getId_fieldman() > 0){
            usuario = MainActivity.myAppDB.myDao().getUsuarioById(plz.getAnexoCorreoFichas().getId_fieldman());
        }


        asistente.setText((usuario != null) ? usuario.getNombre() + ' ' + usuario.getApellido_p() : "sin informacion");
        agricultor.setText((plz.getAnexoCompleto().getAgricultor() != null && plz.getAnexoCompleto().getAgricultor().getNombre_agricultor() != null) ? plz.getAnexoCompleto().getAgricultor().getNombre_agricultor() : "sin informacion");
        potrero.setText((plz.getAnexoCompleto().getLotes() != null && plz.getAnexoCompleto().getLotes().getNombre_lote() != null) ? plz.getAnexoCompleto().getLotes().getNombre_lote() : "sin informacion");
        especie.setText((plz.getAnexoCompleto().getEspecie() != null && plz.getAnexoCompleto().getEspecie().getDesc_especie() != null) ? plz.getAnexoCompleto().getEspecie().getDesc_especie() : "sin informacion");
        comuna.setText((plz.getComuna() != null && plz.getComuna().getDesc_comuna() != null) ? plz.getComuna().getDesc_comuna() : "sin informacion");
//        fecha_ultima_visita.setText((plz.getVisitas() != null && plz.getVisitas().getFecha_visita() != null)  ? plz.getVisitas().getFecha_visita() : "Sin informacion");


        et_inicio_despano.setText((plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getInicio_despano() != null) ? Utilidades.voltearFechaVista(plz.getAnexoCorreoFichas().getInicio_despano()) : "");
        et_cinco_porc_floracion.setText((plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getCinco_porciento_floracion() != null) ? Utilidades.voltearFechaVista(plz.getAnexoCorreoFichas().getCinco_porciento_floracion()) : "");
        et_inicio_corte_seda.setText((plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getInicio_corte_seda() != null) ? Utilidades.voltearFechaVista(plz.getAnexoCorreoFichas().getInicio_corte_seda()) : "");
        et_inicio_cosecha.setText((plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getInicio_cosecha() != null) ? Utilidades.voltearFechaVista(plz.getAnexoCorreoFichas().getInicio_cosecha()) : "");
        et_termino_cosecha.setText((plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getTermino_cosecha() != null) ? Utilidades.voltearFechaVista(plz.getAnexoCorreoFichas().getTermino_cosecha()) : "");
        et_termino_labores_post_cosecha.setText((plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getTermino_labores_post_cosechas() != null) ? Utilidades.voltearFechaVista(plz.getAnexoCorreoFichas().getTermino_labores_post_cosechas()) : "");
        et_detalle_labores.setText((plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getDetalle_labores() != null) ? plz.getAnexoCorreoFichas().getDetalle_labores() : "");



        if(plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getInicio_despano() != null && plz.getAnexoCorreoFichas().getCorreo_inicio_despano() > 0){
            correo_envia_inicio_despano.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_email_24_si));
        }else{
            correo_envia_inicio_despano.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_email_24_no));
        }


        if(plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getCinco_porciento_floracion() != null && plz.getAnexoCorreoFichas().getCorreo_cinco_porciento_floracion() > 0){
            correo_envia_cinco_porc_floracion.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_email_24_si));
        }else{
            correo_envia_cinco_porc_floracion.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_email_24_no));
        }


        if(plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getInicio_corte_seda() != null && plz.getAnexoCorreoFichas().getCorreo_inicio_corte_seda() > 0){
            correo_envia_inicio_corte_seda.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_email_24_si));
        }else{
            correo_envia_inicio_corte_seda.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_email_24_no));
        }


        if(plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getInicio_cosecha() != null && plz.getAnexoCorreoFichas().getCorreo_inicio_cosecha() > 0){
            correo_envia_inicio_cosecha.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_email_24_si));
        }else{
            correo_envia_inicio_cosecha.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_email_24_no));
        }

        if(plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getTermino_cosecha() != null && plz.getAnexoCorreoFichas().getCorreo_termino_cosecha() > 0){
            correo_envia_termino_cosecha.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_email_24_si));
        }else{
            correo_envia_termino_cosecha.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_email_24_no));
        }

        if(plz.getAnexoCorreoFichas() != null && plz.getAnexoCorreoFichas().getTermino_labores_post_cosechas() != null && plz.getAnexoCorreoFichas().getCorreo_termino_labores_post_cosechas() > 0){
            correo_envia_termino_labores_post_cosecha.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_email_24_si));
        }else{
            correo_envia_termino_labores_post_cosecha.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_email_24_no));
        }


        builder.setOnShowListener(dialog -> {
            final Button n = builder.getButton(AlertDialog.BUTTON_NEGATIVE);
            n.setOnClickListener(v -> builder.dismiss());
        });
        builder.setCancelable(false);
        builder.show();
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, year, month, dayOfMonth) -> {

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
            edit.setText(finalDate);
        }, Integer.parseInt(fechaRota[0]), (Integer.parseInt(fechaRota[1]) - 1), Integer.parseInt(fechaRota[2]));
        datePickerDialog.show();

    }

//    aqui es donde se hace la consulta al parecer.
    private void cargarInforme(final int seleccionado){

        if (tabla != null){
            tabla.removeViews();
            tabla.agregarCabecera(R.array.cabecera_anexo_fechas_vista_uno);

            final List<AnexoWithDates> lista = MainActivity.myAppDB.myDao().getFechasSag(id_temporadas.get(spinner_toolbar.getSelectedItemPosition()));

            if (lista != null && lista.size() > 0){
                final ProgressDialog pp = new ProgressDialog(activity);
                                pp.setMessage("Espere por favor");
                                pp.show();
                AsyncTask.execute(() -> activity.runOnUiThread(() -> {
//
                    for (int i = 0; i < lista.size(); i++){
                        tabla.agregarFilaTabla(lista.get(i), 0);
                        if(lista.size() == i + 1){
                            pp.dismiss();
                        }
                    }
                }));
            }
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null){
            activity.updateView(getResources().getString(R.string.app_name), "Anexos Fechas");
        }
    }
}
