package cl.smapdev.curimapu.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.tablas.Fichas;
import cl.smapdev.curimapu.clases.adapters.FichasAdapter;
import cl.smapdev.curimapu.clases.adapters.SpinnerToolbarAdapter;
import cl.smapdev.curimapu.clases.relaciones.FichasCompletas;
import cl.smapdev.curimapu.clases.tablas.Temporada;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.dialogos.DialogFilterFichas;
import cl.smapdev.curimapu.fragments.fichas.FragmentCreaFicha;

public class FragmentFichas extends Fragment {


    private FichasAdapter fichasAdapter;
    private RecyclerView id_lista_fichas;
    private MainActivity activity;
    private FloatingActionButton fb_add_ficha;

    private Spinner spinner_toolbar;

    private SharedPreferences prefs;

    private ArrayList<Integer> id_temporadas = new ArrayList<>();
    private ArrayList<String> desc_temporadas = new ArrayList<>();

    private List<Temporada> temporadaList;



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
        return inflater.inflate(R.layout.fragment_fichas, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        id_lista_fichas = (RecyclerView) view.findViewById(R.id.id_lista_fichas);
        spinner_toolbar = (Spinner) view.findViewById(R.id.spinner_toolbar);
        fb_add_ficha = (FloatingActionButton) view.findViewById(R.id.fb_add_ficha);

        temporadaList = MainActivity.myAppDB.myDao().getTemporada();
        if (temporadaList.size() > 0){
            for (Temporada t : temporadaList){
                id_temporadas.add(t.getId_tempo_tempo());
                desc_temporadas.add(t.getNombre_tempo());
            }
        }

        setHasOptionsMenu(true);


        spinner_toolbar.setAdapter(new SpinnerToolbarAdapter(Objects.requireNonNull(getActivity()),R.layout.spinner_template_toolbar_view, temporadaList));


        //spinner_toolbar.setSelection(getResources().getStringArray(R.array.anos_toolbar).length - 1);
        recargarYear();
        spinner_toolbar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (spinner_toolbar.getTag() != null ){
                    if (Integer.parseInt(spinner_toolbar.getTag().toString()) != i){


                        prefs.edit().putInt(Utilidades.SELECTED_ANO,id_temporadas.get(spinner_toolbar.getSelectedItemPosition())).apply();
                        prefs.edit().putInt(Utilidades.SHARED_FILTER_FICHAS_YEAR, i).apply();

                        cargarLista(MainActivity.myAppDB.myDao().getFichasByYear(id_temporadas.get(spinner_toolbar.getSelectedItemPosition())));
                    }else{
                        spinner_toolbar.setTag(null);
                    }
                }else{

                    prefs.edit().putInt(Utilidades.SELECTED_ANO, id_temporadas.get(spinner_toolbar.getSelectedItemPosition())).apply();
                    prefs.edit().putInt(Utilidades.SHARED_FILTER_FICHAS_YEAR, i).apply();

                    cargarLista(MainActivity.myAppDB.myDao().getFichasByYear(id_temporadas.get(spinner_toolbar.getSelectedItemPosition())));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        fb_add_ficha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avisoCreaNuevaFicha("¿Esta seguro?", "¿ Desea crear una nueva ficha ?");
            }
        });

        if (temporadaList.size() > 0){
            cargarLista(MainActivity.myAppDB.myDao().getFichasByYear(prefs.getInt(Utilidades.SELECTED_ANO, temporadaList.get(temporadaList.size() - 1).getId_tempo_tempo())));
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_vistas, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_vistas_filter:
                DialogFilterFichas dialogo = new DialogFilterFichas();
                dialogo.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "DIALOGO_FICHAS");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        MyReceiver r = new MyReceiver();
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getActivity())).registerReceiver(r, new IntentFilter("TAG_REFRESH"));
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && context != null){
                List<FichasCompletas> trabajo = (List<FichasCompletas>) intent.getSerializableExtra(DialogFilterFichas.LLAVE_ENVIO_OBJECTO);
                if (trabajo != null ){
                    spinner_toolbar.setTag(prefs.getInt(Utilidades.SHARED_FILTER_FICHAS_YEAR, temporadaList.size() - 1));
                    spinner_toolbar.setSelection(prefs.getInt(Utilidades.SHARED_FILTER_FICHAS_YEAR, temporadaList.size() - 1));
                    cargarLista(trabajo);
                }
            }

        }
    }

    private void recargarYear(){
        if (temporadaList.size() > 0){
            spinner_toolbar.setSelection(prefs.getInt(Utilidades.SHARED_FILTER_FICHAS_YEAR, temporadaList.size() - 1));
        }

    }




    private void cargarLista(List<FichasCompletas> fichasCompletas){
//        LinearLayoutManager lManager = null;
//        if (activity != null){
//            lManager  = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
//        }


        RecyclerView.LayoutManager lManager = null;
        if (activity != null){
                lManager = new GridLayoutManager(activity, 2);
        }
        id_lista_fichas.setHasFixedSize(true);
        id_lista_fichas.setLayoutManager(lManager);



        fichasAdapter = new FichasAdapter(fichasCompletas, new FichasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FichasCompletas fichas) {
                activity.cambiarFragment(FragmentCreaFicha.getInstance(fichas), Utilidades.FRAGMENT_CREA_FICHA,  R.anim.slide_in_left, R.anim.slide_out_left);
                //avisoActivaFicha("Seleccione estado de ficha", fichas);
            }
        }, new FichasAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(FichasCompletas fichas) {

            }
        },activity);


        id_lista_fichas.setAdapter(fichasAdapter);

    }



    private void avisoCreaNuevaFicha(String title, String message) {
        View viewInfalted = LayoutInflater.from(getActivity()).inflate(R.layout.alert_empty, null);

        final AlertDialog builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setView(viewInfalted)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("crear", new DialogInterface.OnClickListener(){
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
                        if (activity != null){
                            activity.cambiarFragment(new FragmentCreaFicha(), Utilidades.FRAGMENT_CREA_FICHA, R.anim.slide_in_left, R.anim.slide_out_left);
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



    private void avisoActivaFicha(String title, final FichasCompletas completas) {
        final View viewInfalted = LayoutInflater.from(getActivity()).inflate(R.layout.alert_activa_ficha, null);


        final RadioButton ra = viewInfalted.findViewById(R.id.radio_inactiva);
        final RadioButton rb = viewInfalted.findViewById(R.id.radio_activa);
        final RadioButton rc = viewInfalted.findViewById(R.id.radio_rechazada);





        switch (completas.getFichas().getActiva()){
            case 1:
                ra.setChecked(true);
                break;
            case 2:
                rb.setChecked(true);
                break;
            case 3:
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
                        Fichas fichas = completas.getFichas();
                        if (fichas != null){
                            int estado = (ra.isChecked()) ? 1 : (rb.isChecked()) ? 2 : 3;
                            fichas.setActiva(estado);
                            MainActivity.myAppDB.myDao().updateFicha(fichas);

                            if (fichasAdapter != null){
                                fichasAdapter.notifyDataSetChanged();
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (activity != null){
            activity.updateView(getResources().getString(R.string.app_name), getResources().getString(R.string.subtitles_records));
        }
    }
}
