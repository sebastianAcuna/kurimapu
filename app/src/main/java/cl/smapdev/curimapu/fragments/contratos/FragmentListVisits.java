package cl.smapdev.curimapu.fragments.contratos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.Etapas;
import cl.smapdev.curimapu.clases.adapters.SpinnerToolbarAdapter;
import cl.smapdev.curimapu.clases.adapters.VisitasListAdapter;
import cl.smapdev.curimapu.clases.adapters.VisitasTypeAdapter;
import cl.smapdev.curimapu.clases.bd.MyDao;
import cl.smapdev.curimapu.clases.relaciones.VisitasCompletas;
import cl.smapdev.curimapu.clases.tablas.Fotos;
import cl.smapdev.curimapu.clases.tablas.Temporada;
import cl.smapdev.curimapu.clases.tablas.Visitas;
import cl.smapdev.curimapu.clases.temporales.TempVisitas;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.FragmentContratos;

public class FragmentListVisits extends Fragment {

    private SharedPreferences prefs;
    private MainActivity activity;
    private RecyclerView lista_visitas, lista_visitas_type;

    private final String[] etapas = new String[]{"All","Sowing","Flowering","Harvest", "Unspecified"};

    private final ArrayList<Etapas> etapasArrayList = new ArrayList<>();






    private VisitasTypeAdapter visitasTypeAdapter;
    private VisitasListAdapter visitasListAdapter;


    private List<VisitasCompletas> visitasCompletas;


    private TextView txt_titulo_selected;

    private Spinner spinner_toolbar;

    private List<Temporada> annos;
    private final ArrayList<String> id_temporadas = new ArrayList<>();
    private final ArrayList<String> desc_temporadas = new ArrayList<>();


    private String annoSelected;
    private int etapaSelected=0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (MainActivity) getActivity();

        if (activity != null){
            prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
        }
        annos = MainActivity.myAppDB.myDao().getTemporada();
        if (annos.size() > 0){
            for (Temporada t : annos){
                id_temporadas.add(t.getId_tempo_tempo());
                desc_temporadas.add(t.getNombre_tempo());
            }
        }


        //annos = getResources().getStringArray(R.array.anos_toolbar);

        etapasArrayList.add(new Etapas(0, "All", false));
        etapasArrayList.add(new Etapas(2, "Sowing", false));
        etapasArrayList.add(new Etapas(3, "Flowering", false));
        etapasArrayList.add(new Etapas(4, "Harvest", false));
        etapasArrayList.add(new Etapas(5, "Unspecified", false));

        //String years = annos.get(annos.size() -1).getId_tempo_tempo();
        visitasCompletas = MainActivity.myAppDB.myDao().getVisitasCompletasWithFotos(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""),
                id_temporadas.get(prefs.getInt(Utilidades.SHARED_FILTER_VISITAS_YEAR,annos.size() - 1)));

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista_visitas, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lista_visitas = view.findViewById(R.id.lista_visitas);
        lista_visitas_type = view.findViewById(R.id.lista_visitas_type);
        txt_titulo_selected = view.findViewById(R.id.txt_titulo_selected);
        spinner_toolbar = view.findViewById(R.id.spinner_toolbar);



        spinner_toolbar.setAdapter(new SpinnerToolbarAdapter(activity,R.layout.spinner_template_toolbar_view, annos));


       /* visitasCompletas = MainActivity.myAppDB.myDao().getVisitasCompletas(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""),
                );*/
        annoSelected = id_temporadas.get(prefs.getInt(Utilidades.SHARED_FILTER_VISITAS_YEAR,annos.size() - 1));


        txt_titulo_selected.setText(Utilidades.getStateString(0));


        spinner_toolbar.setSelection(id_temporadas.indexOf(annoSelected));
        spinner_toolbar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

//                if (spinner_toolbar.getTag() != null ){
//                    if (Integer.parseInt(spinner_toolbar.getTag().toString()) != i){

                    annoSelected = annos.get(i).getId_tempo_tempo();

                    prefs.edit().putInt(Utilidades.SHARED_FILTER_VISITAS_YEAR, i).apply();
                    prefs.edit().putString(Utilidades.SELECTED_ANO, annoSelected).apply();

                    //String years = annos.get(annos.size() -1).getId_tempo_tempo();

                    if (etapaSelected > 0){
                        visitasCompletas = MainActivity.myAppDB.myDao().getVisitasCompletasWithFotos(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""),annoSelected);
                    }else{
                        visitasCompletas = MainActivity.myAppDB.myDao().getVisitasCompletasWithFotos(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""),annoSelected);
                    }

                    cargarListaChica();
                    cargarListaGrande();
//                    }else{
//                        spinner_toolbar.setTag(null);
//                    }
                /*}else{


                }*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cargarListaChica();
        cargarListaGrande();

    }


    private void cargarListaChica(){
        LinearLayoutManager lManager = null;
        if (activity != null){
            lManager  = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        }
        lista_visitas_type.setHasFixedSize(true);
        lista_visitas_type.setLayoutManager(lManager);


        visitasTypeAdapter = new VisitasTypeAdapter(etapasArrayList, activity, new VisitasTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                int po = 0;
                switch (position){
                    case 0:
                    case 1:
                    default:
                        po = 0;
                        break;
                    case 2: po = 1;break;
                    case 3: po = 2;break;
                    case 4: po = 3;break;
                    case 5: po = 4;break;
                }

                String years = annoSelected;
                etapaSelected = position;
                if (position > 0){

                    visitasCompletas = MainActivity.myAppDB.myDao().getVisitasCompletasWithFotos(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""), position, years);
                    for (Etapas et : etapasArrayList){
                        etapasArrayList.get(po).setEtapaSelected(et.getNumeroEtapa() == po);
                    }
                }else{
                    visitasCompletas = MainActivity.myAppDB.myDao().getVisitasCompletasWithFotos(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""),years);
                }


                txt_titulo_selected.setText(Utilidades.getStateString(position));
                cargarListaChica();
                cargarListaGrande();

            }
        },prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));

        lista_visitas_type.setAdapter(visitasTypeAdapter);
    }


    private void cargarListaGrande(){
        LinearLayoutManager lManagerVisitas = null;
        if (activity != null){
            lManagerVisitas  = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);

        }
        lista_visitas.setHasFixedSize(true);
        lista_visitas.setLayoutManager(lManagerVisitas);


        visitasListAdapter = new VisitasListAdapter(visitasCompletas, new VisitasListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, VisitasCompletas fichas) {
                showAlertForEdit(fichas);
            }
        }, new VisitasListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, VisitasCompletas fichas) {
                avisoActivaFicha("Esta a punto de eliminar esta visita para el anexo "+fichas.getAnexoCompleto().getAnexoContrato().getAnexo_contrato(), "esta visita realizada el dia "+fichas.getVisitas().getFecha_visita()+" se eliminara completamente de la tableta, no se subira a servidor tampoco",fichas);
            }
        }, activity);

        lista_visitas.setAdapter(visitasListAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (activity != null){
            activity.updateView(getResources().getString(R.string.app_name), getResources().getString(R.string.subtitles_visit));
        }



    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    private void showAlertForEdit(final VisitasCompletas visitasCompletas){
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_empty,null);


        final AlertDialog builder = new AlertDialog.Builder(requireActivity())
                .setView(viewInfalted)
                .setTitle(getResources().getString(R.string.atencion))
                .setMessage(getResources().getString(R.string.mensaje_alerta_editar_visita))
                .setPositiveButton(getResources().getString(R.string.entiendo), new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).setNegativeButton(getResources().getString(R.string.nav_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();


        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                Button c = builder.getButton(AlertDialog.BUTTON_NEGATIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    MainActivity.myAppDB.myDao().deleteTempVisitas();
//                    MainActivity.myAppDB.myDao().resetTempVisitas();

//                                    MainActivity.myAppDB.myDao().deleteTempHarvest();
//                    MainActivity.myAppDB.myDao().resetTempHarvest();

                                    MainActivity.myAppDB.myDao().deleteDetalleVacios();
//                    MainActivity.myAppDB.myDao().resetTempSowing();

                                    TempVisitas tempVisitas = new TempVisitas();
                                    tempVisitas.setEtapa_temp_visitas(visitasCompletas.getVisitas().getEtapa_visitas());
                                    tempVisitas.setRecomendation_temp_visita(visitasCompletas.getVisitas().getRecomendation_visita());
                                    tempVisitas.setObservation_temp_visita(visitasCompletas.getVisitas().getObservation_visita());
                                    tempVisitas.setId_anexo_temp_visita(visitasCompletas.getVisitas().getId_anexo_visita());
                                    tempVisitas.setWeed_state_temp_visita(visitasCompletas.getVisitas().getWeed_state_visita());
                                    tempVisitas.setHumidity_floor_temp_visita(visitasCompletas.getVisitas().getHumidity_floor_visita());
                                    tempVisitas.setOverall_status_temp_visita(visitasCompletas.getVisitas().getOverall_status_visita());
                                    tempVisitas.setPhytosanitary_state_temp_visita(visitasCompletas.getVisitas().getPhytosanitary_state_visita());
                                    tempVisitas.setGrowth_status_temp_visita(visitasCompletas.getVisitas().getGrowth_status_visita());
                                    tempVisitas.setHarvest_temp_visita(visitasCompletas.getVisitas().getHarvest_visita());
                                    tempVisitas.setPhenological_state_temp_visita(visitasCompletas.getVisitas().getPhenological_state_visita());
                                    tempVisitas.setId_temp_visita(visitasCompletas.getVisitas().getId_visita());
                                    tempVisitas.setAction_temp_visita(visitasCompletas.getVisitas().getEstado_visita());

                                    tempVisitas.setPercent_humedad(visitasCompletas.getVisitas().getPercent_humedad());

                                    tempVisitas.setObs_cosecha(visitasCompletas.getVisitas().getObs_cosecha());
                                    tempVisitas.setObs_creci(visitasCompletas.getVisitas().getObs_creci());
                                    tempVisitas.setObs_fito(visitasCompletas.getVisitas().getObs_fito());
                                    tempVisitas.setObs_humedad(visitasCompletas.getVisitas().getObs_humedad());
                                    tempVisitas.setObs_maleza(visitasCompletas.getVisitas().getObs_maleza());
                                    tempVisitas.setObs_overall(visitasCompletas.getVisitas().getObs_overall());

                                    tempVisitas.setId_visita_local(visitasCompletas.getVisitas().getId_visita_local());
                                    tempVisitas.setId_dispo(visitasCompletas.getVisitas().getId_dispo());

                                    MainActivity.myAppDB.myDao().setTempVisitas(tempVisitas);


                                    List<Fotos> fotos = MainActivity.myAppDB.myDao().getFotosByIdVisita(0);
                                    if (fotos.size() > 0){
                                        for (Fotos fts : fotos){
                                            try{
                                                File file = new File(fts.getRuta());
                                                if (file.exists()) {
                                                    boolean eliminado = file.delete();
                                                    if (eliminado){
                                                        MainActivity.myAppDB.myDao().deleteFotos(fts);
                                                    }
                                                }
                                            }catch (Exception e){
                                                Log.e("ERROR DELETING", Objects.requireNonNull(e.getMessage()));
                                            }
                                        }
                                    }

                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            if (prefs != null){
                                                prefs.edit().putInt(Utilidades.SHARED_VISIT_FICHA_ID, visitasCompletas.getAnexoCompleto().getAnexoContrato().getId_ficha_contrato()).apply();
                                                prefs.edit().putString(Utilidades.SHARED_VISIT_ANEXO_ID, visitasCompletas.getVisitas().getId_anexo_visita()).apply();
                                                prefs.edit().putInt(Utilidades.SHARED_VISIT_VISITA_ID, visitasCompletas.getVisitas().getId_visita()).apply();
                                            }

                                            activity.cambiarFragment(new FragmentContratos(), Utilidades.FRAGMENT_CONTRATOS, R.anim.slide_in_left,R.anim.slide_out_left);
                                            builder.dismiss();
                                        }
                                    });

                                }catch (SQLiteException e){
                                    Log.e("BD PROBLEM", e.getMessage());

                                }
                            }
                        });



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


    private void avisoActivaFicha(String title, String message, final VisitasCompletas completas) {
        final View viewInfalted = LayoutInflater.from(getActivity()).inflate(R.layout.alert_empty, null);


        final AlertDialog builder = new AlertDialog.Builder(requireActivity())
                .setView(viewInfalted)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Eliminar", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setNegativeButton(getResources().getString(R.string.nav_cancel), new DialogInterface.OnClickListener() {
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

                        MainActivity.myAppDB.myDao().deleteDetallesByVisita(completas.getVisitas().getId_visita());
                        MainActivity.myAppDB.myDao().deleteFotosByVisita(completas.getVisitas().getId_visita());
                        MainActivity.myAppDB.myDao().deleteVisita(completas.getVisitas().getId_visita());

                        visitasCompletas = MainActivity.myAppDB.myDao().getVisitasCompletasWithFotos(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""),annoSelected);

                        cargarListaChica();
                        cargarListaGrande();
                        builder.dismiss();
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


    private void showAlertForUpdate(Fotos foto){
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_big_img,null);


//        String titulo = "Editando " + fotos.getNombreFoto() + " de PAQUETE " + fotos.getEtiquetaPaquete();
        final AlertDialog builder = new AlertDialog.Builder(requireActivity())
                .setView(viewInfalted)
                .setPositiveButton("cerrar", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })/*.setNegativeButton("cancelar",null)*/.create();

//        final TextView txt = viewInfalted.findViewById(R.id.et_cambia_nombre_foto);
        final ImageView imageView = viewInfalted.findViewById(R.id.img_alert_foto);
//        String medidaAMostrar = foto.getNombre_foto();
//        txt.setText(medidaAMostrar);
        Picasso.get().load("file:///"+foto.getRuta()).into(imageView);
        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();

                    }
                });
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
}
