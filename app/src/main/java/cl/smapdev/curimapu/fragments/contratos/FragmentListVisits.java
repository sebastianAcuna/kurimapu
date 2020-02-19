package cl.smapdev.curimapu.fragments.contratos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.SpinnerToolbarAdapter;
import cl.smapdev.curimapu.clases.adapters.VisitasListAdapter;
import cl.smapdev.curimapu.clases.adapters.VisitasTypeAdapter;
import cl.smapdev.curimapu.clases.relaciones.VisitasCompletas;
import cl.smapdev.curimapu.clases.tablas.Fotos;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class FragmentListVisits extends Fragment {

    private SharedPreferences prefs;
    private MainActivity activity;
    private RecyclerView lista_visitas, lista_visitas_type;

    private String[] etapas = new String[]{"All","Sowing","Flowering","Harvest", "Unspecified"};

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

                    if (etapaSelected > 0){
                        visitasCompletas = MainActivity.myAppDB.myDao().getVisitasCompletas(prefs.getInt(Utilidades.SHARED_VISIT_ANEXO_ID, 0),etapaSelected,annoSelected);
                    }else{
                        visitasCompletas = MainActivity.myAppDB.myDao().getVisitasCompletas(prefs.getInt(Utilidades.SHARED_VISIT_ANEXO_ID, 0),annoSelected);
                    }


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


        visitasTypeAdapter = new VisitasTypeAdapter(etapas, activity, new VisitasTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                etapaSelected = position;
                if (position > 0){
                    visitasCompletas = MainActivity.myAppDB.myDao().getVisitasCompletas(prefs.getInt(Utilidades.SHARED_VISIT_ANEXO_ID, 0), position, annoSelected);
                }else{
                    visitasCompletas = MainActivity.myAppDB.myDao().getVisitasCompletas(prefs.getInt(Utilidades.SHARED_VISIT_ANEXO_ID, 0),annoSelected);
                }

                txt_titulo_selected.setText(Utilidades.getStateString(position));
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
                }
            }
        }, new VisitasListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, VisitasCompletas fichas, Fotos fotos) {

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
        String medidaAMostrar = "Nombre prueba";
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
