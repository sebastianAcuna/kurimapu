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
import cl.smapdev.curimapu.clases.relaciones.VisitasCompletas;
import cl.smapdev.curimapu.clases.tablas.Flowering;
import cl.smapdev.curimapu.clases.tablas.Fotos;
import cl.smapdev.curimapu.clases.tablas.Harvest;
import cl.smapdev.curimapu.clases.tablas.Sowing;
import cl.smapdev.curimapu.clases.tablas.Visitas;
import cl.smapdev.curimapu.clases.temporales.TempFlowering;
import cl.smapdev.curimapu.clases.temporales.TempHarvest;
import cl.smapdev.curimapu.clases.temporales.TempSowing;
import cl.smapdev.curimapu.clases.temporales.TempVisitas;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.FragmentContratos;

public class FragmentListVisits extends Fragment {

    private SharedPreferences prefs;
    private MainActivity activity;
    private RecyclerView lista_visitas, lista_visitas_type;

    private String[] etapas = new String[]{"All","Sowing","Flowering","Harvest", "Unspecified"};

    private ArrayList<Etapas> etapasArrayList = new ArrayList<>();






    private VisitasTypeAdapter visitasTypeAdapter;
    private VisitasListAdapter visitasListAdapter;


    private List<VisitasCompletas> visitasCompletas;


    private TextView txt_titulo_selected;

    private Spinner spinner_toolbar;

    private String[] annos;
    private int annoSelected, etapaSelected=0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (MainActivity) getActivity();

        if (activity != null){
            prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
        }

        annos = getResources().getStringArray(R.array.anos_toolbar);

        etapasArrayList.add(new Etapas(0, "All", false));
        etapasArrayList.add(new Etapas(2, "Sowing", false));
        etapasArrayList.add(new Etapas(3, "Flowering", false));
        etapasArrayList.add(new Etapas(4, "Harvest", false));
        etapasArrayList.add(new Etapas(5, "Unspecified", false));


        visitasCompletas = MainActivity.myAppDB.myDao().getVisitasCompletas(prefs.getInt(Utilidades.SHARED_VISIT_ANEXO_ID, 0),Integer.parseInt(annos[annos.length -1]));

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
        spinner_toolbar = (Spinner) view.findViewById(R.id.spinner_toolbar);



        spinner_toolbar.setAdapter(new SpinnerToolbarAdapter(Objects.requireNonNull(getActivity()),R.layout.spinner_template_toolbar_view, annos));



        annoSelected = Integer.parseInt(annos[annos.length -1]);


        txt_titulo_selected.setText(Utilidades.getStateString(0));


        spinner_toolbar.setSelection(annos.length -1);
        spinner_toolbar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

//                if (spinner_toolbar.getTag() != null ){
//                    if (Integer.parseInt(spinner_toolbar.getTag().toString()) != i){


                    annoSelected = Integer.parseInt(annos[i]);

                    prefs.edit().putInt(Utilidades.SHARED_FILTER_VISITAS_YEAR, i).apply();

                    if (etapaSelected > 0){
                        visitasCompletas = MainActivity.myAppDB.myDao().getVisitasCompletas(prefs.getInt(Utilidades.SHARED_VISIT_ANEXO_ID, 0),etapaSelected,annoSelected);
                    }else{
                        visitasCompletas = MainActivity.myAppDB.myDao().getVisitasCompletas(prefs.getInt(Utilidades.SHARED_VISIT_ANEXO_ID, 0),annoSelected);
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


                etapaSelected = position;
                if (position > 0){

                    visitasCompletas = MainActivity.myAppDB.myDao().getVisitasCompletas(prefs.getInt(Utilidades.SHARED_VISIT_ANEXO_ID, 0), position, annoSelected);
                    for (Etapas et : etapasArrayList){
                        if (et.getNumeroEtapa() == po){

                            etapasArrayList.get(po).setEtapaSelected(true);
                        }else{
                            etapasArrayList.get(po).setEtapaSelected(false);
                        }
                    }


                }else{
                    visitasCompletas = MainActivity.myAppDB.myDao().getVisitasCompletas(prefs.getInt(Utilidades.SHARED_VISIT_ANEXO_ID, 0),annoSelected);
                }


                txt_titulo_selected.setText(Utilidades.getStateString(position));
                cargarListaChica();
                cargarListaGrande();

            }
        },prefs.getInt(Utilidades.SHARED_VISIT_ANEXO_ID, 0));

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
            public void onItemClick(View view, VisitasCompletas fichas, Fotos fotos) {
                switch (view.getId()){
                    case R.id.imagen_referencial:
                        if (fotos != null){
                            showAlertForUpdate(fotos);
                        }
                        break;
                    case R.id.cardview_visitas:
                        showAlertForEdit(fichas);
                        break;
                }
            }
        }, new VisitasListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, VisitasCompletas fichas, Fotos fotos) {
                avisoActivaFicha(" Estado de visita ",fichas);
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


        final AlertDialog builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setView(viewInfalted)
                .setTitle("Atencion")
                .setMessage("¿Estas seguro que deseas ver esta visita?\nDependiendo del estado esta se podra editar o no.")
                .setPositiveButton("aceptar", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
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

                                    MainActivity.myAppDB.myDao().deleteTempHarvest();
//                    MainActivity.myAppDB.myDao().resetTempHarvest();

                                    MainActivity.myAppDB.myDao().deleteTempFlowering();
//                    MainActivity.myAppDB.myDao().resetTempFlowering();

                                    MainActivity.myAppDB.myDao().deleteTempSowing();
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

                                    MainActivity.myAppDB.myDao().setTempVisitas(tempVisitas);


                                    Sowing sowing = MainActivity.myAppDB.myDao().getSowing(visitasCompletas.getVisitas().getId_visita(), visitasCompletas.getVisitas().getId_anexo_visita());
                                    if (sowing != null) {
                                        TempSowing tempSowing = new TempSowing();
                                        tempSowing.setAmount_applied_temp_sowing(sowing.getAmount_applied_sowing());
                                        tempSowing.setBasta_splat_4_ha_temp_sowing(sowing.getBasta_splat_4_ha_sowing());
                                        tempSowing.setBasta_spray_1_temp_sowing(sowing.getBasta_spray_1_sowing());
                                        tempSowing.setBasta_spray_2_temp_sowing(sowing.getBasta_spray_2_sowing());
                                        tempSowing.setBasta_spray_3_temp_sowing(sowing.getBasta_spray_3_sowing());
                                        tempSowing.setBasta_spray_4_temp_sowing(sowing.getBasta_spray_4_sowing());
                                        tempSowing.setDate_1_herb_temp_sowing(sowing.getDate_1_herb_sowing());
                                        tempSowing.setDate_1_temp_sowing(sowing.getDate_1_sowing());
                                        tempSowing.setDate_2_herb_temp_sowing(sowing.getDate_2_herb_sowing());
                                        tempSowing.setDate_2_temp_sowing(sowing.getDate_2_sowing());
                                        tempSowing.setDate_3_herb_temp_sowing(sowing.getDate_3_herb_sowing());
                                        tempSowing.setDate_foliar_temp_sowing(sowing.getDate_foliar_sowing());
                                        tempSowing.setDate_nombre_largo_temp_sowing(sowing.getDate_nombre_largo_sowing());
                                        tempSowing.setDate_pre_emergence_temp_sowing(sowing.getDate_pre_emergence_sowing());
                                        tempSowing.setDelivered_temp_sowing(sowing.getDelivered_sowing());
                                        tempSowing.setDose_1_temp_sowing(sowing.getDose_1_sowing());
                                        tempSowing.setDose_2_temp_sowing(sowing.getDose_2_sowing());
                                        tempSowing.setDose_foliar_temp_sowing(sowing.getDose_foliar_sowing());
                                        tempSowing.setDose_nombre_largo_temp_sowing(sowing.getDose_nombre_largo_sowing());
                                        tempSowing.setDose_pre_emergence_temp_sowing(sowing.getDose_pre_emergence_sowing());
                                        tempSowing.setEast_temp_sowing(sowing.getEast_sowing());
                                        tempSowing.setFemale_lines_temp_sowing(sowing.getFemale_lines_sowing());
                                        tempSowing.setFemale_sowing_date_end_temp_sowing(sowing.getFemale_sowing_date_end_sowing());
                                        tempSowing.setFemale_sowing_date_start_temp_sowing(sowing.getFemale_sowing_date_start_sowing());
                                        tempSowing.setFemale_sowing_lot_temp_sowing(sowing.getFemale_sowing_lot_sowing());
                                        tempSowing.setFoliar_temp_sowing(sowing.getFoliar_sowing());
                                        tempSowing.setId_anexo_temp_sowing(sowing.getId_anexo_sowing());
                                        tempSowing.setMeters_isoliation_temp_sowing(sowing.getMeters_isoliation_sowing());
                                        tempSowing.setName_1_herb_temp_sowing(sowing.getName_1_herb_sowing());
                                        tempSowing.setName_2_herb_temp_sowing(sowing.getName_2_herb_sowing());
                                        tempSowing.setName_3_herb_temp_sowing(sowing.getName_3_herb_sowing());
                                        tempSowing.setNorth_temp_sowing(sowing.getNorth_sowing());
                                        tempSowing.setPlant_m_temp_sowing(sowing.getPlant_m_sowing());
                                        tempSowing.setPopulation_plants_ha_temp_sowing(sowing.getPopulation_plants_ha_sowing());
                                        tempSowing.setProduct_nombre_largo_temp_sowing(sowing.getProduct_nombre_largo_sowing());
                                        tempSowing.setReal_sowing_female_temp_sowing(sowing.getReal_sowing_female_sowing());
                                        tempSowing.setRow_distance_temp_sowing(sowing.getRow_distance_sowing());
                                        tempSowing.setSag_planting_temp_sowing(tempSowing.getSag_planting_temp_sowing());
                                        tempSowing.setSouth_temp_sowing(sowing.getSouth_sowing());
                                        tempSowing.setSowing_seed_meter_temp_sowing(sowing.getSowing_seed_meter_sowing());
                                        tempSowing.setType_of_mixture_applied_temp_sowing(sowing.getType_of_mixture_applied_sowing());
                                        tempSowing.setWater_pre_emergence_temp_sowing(sowing.getWater_pre_emergence_sowing());
                                        tempSowing.setWest_temp_sowing(sowing.getWest_sowing());
                                        tempSowing.setAction_temp_sowing(visitasCompletas.getVisitas().getEstado_visita());
                                        tempSowing.setId_temp_sowing(sowing.getId_sowing());


                                        MainActivity.myAppDB.myDao().setTempSowing(tempSowing);
                                    }

                                    Flowering flowering = MainActivity.myAppDB.myDao().getFlowering(visitasCompletas.getVisitas().getId_visita(), visitasCompletas.getVisitas().getId_anexo_visita());
                                    if (flowering != null) {
                                        TempFlowering tempFlowering = new TempFlowering();

                                        tempFlowering.setCheck_temp_flowering(flowering.getCheck_flowering());
                                        tempFlowering.setDate_beginning_depuration_temp_flowering(flowering.getDate_beginning_depuration_flowering());
                                        tempFlowering.setDate_funficide_temp_flowering(flowering.getDate_funficide_flowering());
                                        tempFlowering.setDate_insecticide_temp_flowering(flowering.getDate_insecticide_flowering());
                                        tempFlowering.setDate_inspection_temp_flowering(flowering.getDate_inspection_flowering());
                                        tempFlowering.setDate_notice_sag_temp_flowering(flowering.getDate_notice_sag_flowering());
                                        tempFlowering.setDate_off_type_temp_flowering(flowering.getDate_off_type_flowering());
                                        tempFlowering.setDose_fungicide_temp_flowering(flowering.getDose_fungicide_flowering());

                                        tempFlowering.setFertility_1_temp_flowering(flowering.getFertility_1_flowering());
                                        tempFlowering.setFertility_2_temp_flowering(flowering.getFertility_2_flowering());
                                        tempFlowering.setFlowering_end_temp_flowering(flowering.getFlowering_end_flowering());
                                        tempFlowering.setFlowering_estimation_temp_flowering(flowering.getFlowering_estimation_flowering());
                                        tempFlowering.setFlowering_start_temp_flowering(flowering.getFlowering_start_flowering());
                                        tempFlowering.setFungicide_name_temp_flowering(flowering.getFungicide_name_flowering());
                                        tempFlowering.setId_anexo_temp_flowering(flowering.getId_anexo_flowering());

                                        tempFlowering.setMain_characteristic_temp_flowering(flowering.getMain_characteristic_flowering());
                                        tempFlowering.setPlant_number_checked_temp_flowering(flowering.getPlant_number_checked_flowering());
                                        tempFlowering.setAction_temp_flowering(visitasCompletas.getVisitas().getEstado_visita());
                                        tempFlowering.setId_temp_flowering(flowering.getId_flowering());

                                        MainActivity.myAppDB.myDao().setTempFlowering(tempFlowering);
                                    }

                                    Harvest harvest = MainActivity.myAppDB.myDao().getharvest(visitasCompletas.getVisitas().getId_visita(), visitasCompletas.getVisitas().getId_anexo_visita());
                                    if (harvest != null) {

                                        TempHarvest tempHarvest = new TempHarvest();

                                        tempHarvest.setBeginning_date_temp_harvest(harvest.getBeginning_date_temp_harvest());
                                        tempHarvest.setDate_harvest_estimation_temp_harvest(harvest.getDate_harvest_estimation_temp_harvest());
                                        tempHarvest.setEnd_date_temp_harvest(harvest.getEnd_date_temp_harvest());

                                        tempHarvest.setEstimated_date_temp_harvest(harvest.getEstimated_date_temp_harvest());
                                        tempHarvest.setId_anexo_temp_harvest(harvest.getId_anexo_temp_harvest());

                                        tempHarvest.setKg_ha_yield_temp_harvest(harvest.getKg_ha_yield_temp_harvest());
                                        tempHarvest.setModel_machine_temp_harvest(harvest.getModel_machine_temp_harvest());
                                        tempHarvest.setObservation_dessicant_temp_harvest(harvest.getObservation_dessicant_temp_harvest());
                                        tempHarvest.setObservation_yield_temp_harvest(harvest.getObservation_yield_temp_harvest());
                                        tempHarvest.setOwner_machine_temp_harvest(harvest.getOwner_machine_temp_harvest());
                                        tempHarvest.setPorcent_temp_harvest(harvest.getPorcent_temp_harvest());
                                        tempHarvest.setReal_date_temp_harvest(harvest.getReal_date_temp_harvest());
                                        tempHarvest.setSwathing_date_temp_harvest(harvest.getSwathing_date_temp_harvest());

                                        tempHarvest.setAction_temp_harvest(visitasCompletas.getVisitas().getEstado_visita());
                                        tempHarvest.setId_temp_harvest(harvest.getId_temp_harvest());

                                        MainActivity.myAppDB.myDao().setTempHarvest(tempHarvest);
                                    }


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
                                                prefs.edit().putInt(Utilidades.SHARED_VISIT_ANEXO_ID, visitasCompletas.getVisitas().getId_anexo_visita()).apply();
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


    private void avisoActivaFicha(String title, final VisitasCompletas completas) {
        final View viewInfalted = LayoutInflater.from(getActivity()).inflate(R.layout.alert_activa_ficha, null);


        final RadioButton ra = viewInfalted.findViewById(R.id.radio_inactiva);
        final RadioButton rb = viewInfalted.findViewById(R.id.radio_activa);
        final RadioButton rc = viewInfalted.findViewById(R.id.radio_rechazada);


        ra.setText("abierta");
        rb.setText("editable");
        rc.setText("cerrada");


        switch (completas.getVisitas().getEstado_visita()){
            case 0:
            default:
                ra.setChecked(true);
                break;
            case 1:
                rb.setChecked(true);
                break;
            case 2:
                rc.setChecked(true);
                break;
        }


        final AlertDialog builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setView(viewInfalted)
                .setTitle(title)
                .setPositiveButton("Modificar", new DialogInterface.OnClickListener(){
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
                        Visitas fichas = completas.getVisitas();
                        if (fichas != null){
                            int estado = (ra.isChecked()) ? 0 : (rb.isChecked()) ? 1 : 2;
                            fichas.setEstado_visita(estado);
                            MainActivity.myAppDB.myDao().updateVisita(fichas);

                            if (visitasListAdapter != null){
                                visitasListAdapter.notifyDataSetChanged();
                            }

                        }
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
        final AlertDialog builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setView(viewInfalted)
                .setPositiveButton("cerrar", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })/*.setNegativeButton("cancelar",null)*/.create();

        final TextView txt = viewInfalted.findViewById(R.id.et_cambia_nombre_foto);
        final ImageView imageView = viewInfalted.findViewById(R.id.img_alert_foto);
        String medidaAMostrar = foto.getNombre_foto();
        txt.setText(medidaAMostrar);
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
