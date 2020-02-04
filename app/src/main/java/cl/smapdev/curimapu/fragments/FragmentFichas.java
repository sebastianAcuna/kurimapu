package cl.smapdev.curimapu.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.List;
import java.util.Objects;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.FichasAdapter;
import cl.smapdev.curimapu.clases.adapters.SpinnerToolbarAdapter;
import cl.smapdev.curimapu.clases.relaciones.FichasCompletas;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.dialogos.DialogFilterTables;
import cl.smapdev.curimapu.fragments.fichas.FragmentCreaFicha;

public class FragmentFichas extends Fragment {


    private FichasAdapter fichasAdapter;
    private RecyclerView id_lista_fichas;
    private MainActivity activity;
    private FloatingActionButton fb_add_ficha;

    private Spinner spinner_toolbar;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        activity = (MainActivity) getActivity();
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



        setHasOptionsMenu(true);


        spinner_toolbar.setAdapter(new SpinnerToolbarAdapter(Objects.requireNonNull(getActivity()),R.layout.spinner_template_toolbar_view, getResources().getStringArray(R.array.anos_toolbar)));


        spinner_toolbar.setSelection(getResources().getStringArray(R.array.anos_toolbar).length - 1);
        spinner_toolbar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cargarLista(MainActivity.myAppDB.myDao().getFichasByYear(Integer.parseInt(spinner_toolbar.getSelectedItem().toString())));
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

        cargarLista(MainActivity.myAppDB.myDao().getFichasByYear(2019));
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

                DialogFilterTables dialogo = new DialogFilterTables();
                dialogo.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "DIALOGO");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void cargarLista(List<FichasCompletas> fichasCompletas){
//        LinearLayoutManager lManager = null;
//        if (activity != null){
//            lManager  = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
//        }


        RecyclerView.LayoutManager lManager = null;
        if (activity != null){
                lManager = new GridLayoutManager(activity, 3);
        }
        id_lista_fichas.setHasFixedSize(true);
        id_lista_fichas.setLayoutManager(lManager);



        fichasAdapter = new FichasAdapter(fichasCompletas, new FichasAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FichasCompletas fichas) {

            }
        }, new FichasAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(FichasCompletas fichas) {

                activity.cambiarFragment(FragmentCreaFicha.getInstance(fichas), Utilidades.FRAGMENT_CREA_FICHA,  R.anim.slide_in_left, R.anim.slide_out_left);

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


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (activity != null){
            activity.updateView(getResources().getString(R.string.app_name), getResources().getString(R.string.subtitles_records));
        }
    }
}
