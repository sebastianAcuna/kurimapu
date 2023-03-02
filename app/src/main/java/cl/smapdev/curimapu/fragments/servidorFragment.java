package cl.smapdev.curimapu.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.UsuariosAdapter;
import cl.smapdev.curimapu.clases.adapters.VisitasListAdapter;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class servidorFragment extends Fragment {

    private RecyclerView rv;
    private Button button;
    private RadioButton rbDesarrollo, rbProduccion, rbPruebas;
    private SharedPreferences shared;
    private MainActivity activity;
    private MaterialCardView card_admin;
    private UsuariosAdapter adapter;
    private TextView titulo_servidor;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (MainActivity) getActivity();



    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.servidor_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = view.findViewById(R.id.rv_usuarios);
        rbDesarrollo = view.findViewById(R.id.rbDesarrollo);
        rbProduccion = view.findViewById(R.id.rbProduccion);
        rbPruebas = view.findViewById(R.id.rbPruebas);
        button = view.findViewById(R.id.btnContinuar);
        card_admin = view.findViewById(R.id.card_admin);
        titulo_servidor = view.findViewById(R.id.titulo_servidor);

        shared = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
        shared.edit().remove(Utilidades.SHARED_SERVER_ID_USER).apply();

        List<Usuario> listUser = MainActivity.myAppDB.myDao().getUsuarioById();

        cargarAdapter(listUser);

        Config cnf = MainActivity.myAppDB.myDao().getConfig();
        Usuario us = MainActivity.myAppDB.myDao().getUsuarioById(cnf.getId_usuario());
//        if (us.getRut_usuario().equals("18.804.066-7") || us.getRut_usuario().equals("9.411.789-5") || us.getRut_usuario().equals("15.953.693-9") ){
//            rbDesarrollo.setVisibility(View.VISIBLE);
//            rbProduccion.setVisibility(View.VISIBLE);
//            titulo_servidor.setVisibility(View.VISIBLE);
//            rbPruebas.setVisibility(View.VISIBLE);
//        }else{
            rbDesarrollo.setVisibility(View.GONE);
            rbProduccion.setVisibility(View.GONE);
            titulo_servidor.setVisibility(View.GONE);
            rbPruebas.setVisibility(View.GONE);
//        }

        if (String.valueOf(cnf.getId_usuario()).equals(shared.getString(Utilidades.SHARED_SERVER_ID_USER, ""))){
            card_admin.setBackgroundColor(activity.getColor(R.color.colorSelected));
        }

        card_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config cnf = MainActivity.myAppDB.myDao().getConfig();
                shared.edit().remove(Utilidades.SHARED_SERVER_ID_USER).apply();
                shared.edit().putString(Utilidades.SHARED_SERVER_ID_USER,String.valueOf(cnf.getId_usuario())).apply();
                card_admin.setBackgroundColor(activity.getColor(R.color.colorSelected));
                adapter.notifyDataSetChanged();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(shared.getString(Utilidades.SHARED_SERVER_ID_SERVER, ""))){
                    shared.edit().putString(Utilidades.SHARED_SERVER_ID_SERVER, Utilidades.URL_SERVER_API).apply();
                }

                if (!TextUtils.isEmpty(shared.getString(Utilidades.SHARED_SERVER_ID_USER, "")) &&
                    !TextUtils.isEmpty(shared.getString(Utilidades.SHARED_SERVER_ID_SERVER, ""))){
                    if (activity != null){

                        Config cnf = MainActivity.myAppDB.myDao().getConfig();

                        cnf.setId_usuario_suplandato(Integer.parseInt(shared.getString(Utilidades.SHARED_SERVER_ID_USER, String.valueOf(cnf.getId_usuario()))));
                        cnf.setServidorSeleccionado(shared.getString(Utilidades.SHARED_SERVER_ID_SERVER, Utilidades.URL_SERVER_API));

                        MainActivity.myAppDB.myDao().updateConfig(cnf);



                        activity.cambiarFragment(new FragmentPrincipal(), Utilidades.FRAGMENT_INICIO, R.anim.slide_in_left, R.anim.slide_out_left);
                    }
                }else{
                    Utilidades.avisoListo(activity,"Atencion", "Debes completar las selecciones antes de seguir.","Entiendo");
                }
            }
        });

        if (cnf.getServidorSeleccionado().equals(Utilidades.URL_SERVER_API)){
            rbProduccion.setChecked(true);
        }else if (cnf.getServidorSeleccionado().equals(Utilidades.IP_DESARROLLO)){
            rbDesarrollo.setChecked(true);
        }else if(cnf.getServidorSeleccionado().equals(Utilidades.IP_PRUEBAS)){
            rbPruebas.setChecked(true);
        }

        rbDesarrollo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shared.edit().remove(Utilidades.SHARED_SERVER_ID_SERVER).apply();
                shared.edit().putString(Utilidades.SHARED_SERVER_ID_SERVER, Utilidades.IP_DESARROLLO).apply();
            }
        });

        rbPruebas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shared.edit().remove(Utilidades.SHARED_SERVER_ID_SERVER).apply();
                shared.edit().putString(Utilidades.SHARED_SERVER_ID_SERVER, Utilidades.IP_PRUEBAS).apply();
            }
        });

        rbProduccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shared.edit().remove(Utilidades.SHARED_SERVER_ID_SERVER).apply();
                shared.edit().putString(Utilidades.SHARED_SERVER_ID_SERVER, Utilidades.URL_SERVER_API).apply();
            }
        });

    }


    private void cargarAdapter(List<Usuario> listUser){
        adapter = new UsuariosAdapter(listUser, new UsuariosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Usuario user) {
                shared.edit().remove(Utilidades.SHARED_SERVER_ID_USER).apply();
                shared.edit().putString(Utilidades.SHARED_SERVER_ID_USER,String.valueOf(user.getId_usuario())).apply();

                card_admin.setBackgroundColor(activity.getColor(R.color.colorSurface));
                adapter.notifyDataSetChanged();
            }
        }, activity);

        rv.setAdapter(adapter);
    }

}
