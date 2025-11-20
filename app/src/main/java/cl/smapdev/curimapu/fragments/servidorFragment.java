package cl.smapdev.curimapu.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.UsuariosAdapter;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class servidorFragment extends Fragment {

    private RecyclerView rv;
    private SharedPreferences shared;
    private MainActivity activity;
    private MaterialCardView card_admin;
    private UsuariosAdapter adapter;
    RadioButton rbDesarrollo;
    RadioButton rbProduccion;
    RadioButton rbPruebas;

    private ExecutorService executor;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private Config cnf;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity) {
            activity = (MainActivity) context;
            shared = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
        } else {
            throw new RuntimeException(context.toString() + " must be MainActivity");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        executor = Executors.newSingleThreadExecutor();

        ejecutarSeguro(() -> {
            List<Usuario> listUser = MainActivity.myAppDB.myDao().getUsuarioById();
            Config configuracion = MainActivity.myAppDB.myDao().getConfig();

            handler.post(() -> {
                cnf = configuracion;
                cargarAdapter(listUser);

                if (String.valueOf(cnf.getId_usuario()).equals(shared.getString(Utilidades.SHARED_SERVER_ID_USER, ""))) {
                    card_admin.setBackgroundColor(activity.getColor(R.color.colorSelected));
                }
                switch (cnf.getServidorSeleccionado()) {
                    case Utilidades.URL_SERVER_API:
                        rbProduccion.setChecked(true);
                        break;
                    case Utilidades.IP_DESARROLLO:
                        rbDesarrollo.setChecked(true);
                        break;
                    case Utilidades.IP_PRUEBAS:
                        rbPruebas.setChecked(true);
                        break;
                }

            });


        });

    }

    private void ejecutarSeguro(Runnable r) {
        if (executor == null || executor.isShutdown() || executor.isTerminated()) {
            executor = Executors.newSingleThreadExecutor();
        }
        executor.execute(r);
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
        Button button = view.findViewById(R.id.btnContinuar);
        card_admin = view.findViewById(R.id.card_admin);
        TextView titulo_servidor = view.findViewById(R.id.titulo_servidor);

        shared.edit().remove(Utilidades.SHARED_SERVER_ID_USER).apply();

        rbDesarrollo.setVisibility(View.GONE);
        rbProduccion.setVisibility(View.GONE);
        titulo_servidor.setVisibility(View.GONE);
        rbPruebas.setVisibility(View.GONE);

        card_admin.setOnClickListener(v -> {
            shared.edit().remove(Utilidades.SHARED_SERVER_ID_USER).apply();
            shared.edit().putString(Utilidades.SHARED_SERVER_ID_USER, String.valueOf(cnf.getId_usuario())).apply();
            card_admin.setBackgroundColor(activity.getColor(R.color.colorSelected));
            adapter.notifyDataSetChanged();
        });


        button.setOnClickListener(v -> {
            if (TextUtils.isEmpty(shared.getString(Utilidades.SHARED_SERVER_ID_SERVER, ""))) {
                shared.edit().putString(Utilidades.SHARED_SERVER_ID_SERVER, Utilidades.URL_SERVER_API).apply();
            }

            if (!TextUtils.isEmpty(shared.getString(Utilidades.SHARED_SERVER_ID_USER, "")) &&
                    !TextUtils.isEmpty(shared.getString(Utilidades.SHARED_SERVER_ID_SERVER, ""))) {
                if (activity != null) {
                    cnf.setId_usuario_suplandato(Integer.parseInt(shared.getString(Utilidades.SHARED_SERVER_ID_USER, String.valueOf(cnf.getId_usuario()))));
                    cnf.setServidorSeleccionado(shared.getString(Utilidades.SHARED_SERVER_ID_SERVER, Utilidades.URL_SERVER_API));
                    ejecutarSeguro(() -> {
                        MainActivity.myAppDB.myDao().updateConfig(cnf);
                        handler.post(() -> {
                            activity.cambiarFragment(new FragmentPrincipal(), Utilidades.FRAGMENT_INICIO, R.anim.slide_in_left, R.anim.slide_out_left);
                        });
                    });
                }
            } else {
                Utilidades.avisoListo(activity, "Atencion", "Debes completar las selecciones antes de seguir.", "Entiendo");
            }
        });


        rbDesarrollo.setOnClickListener(v -> {
            shared.edit().remove(Utilidades.SHARED_SERVER_ID_SERVER).apply();
            shared.edit().putString(Utilidades.SHARED_SERVER_ID_SERVER, Utilidades.IP_DESARROLLO).apply();
        });

        rbPruebas.setOnClickListener(v -> {
            shared.edit().remove(Utilidades.SHARED_SERVER_ID_SERVER).apply();
            shared.edit().putString(Utilidades.SHARED_SERVER_ID_SERVER, Utilidades.IP_PRUEBAS).apply();
        });

        rbProduccion.setOnClickListener(v -> {
            shared.edit().remove(Utilidades.SHARED_SERVER_ID_SERVER).apply();
            shared.edit().putString(Utilidades.SHARED_SERVER_ID_SERVER, Utilidades.URL_SERVER_API).apply();
        });

    }

    private void cargarAdapter(List<Usuario> listUser) {
        adapter = new UsuariosAdapter(listUser, user -> {
            shared.edit().remove(Utilidades.SHARED_SERVER_ID_USER).apply();
            shared.edit().putString(Utilidades.SHARED_SERVER_ID_USER, String.valueOf(user.getId_usuario())).apply();

            card_admin.setBackgroundColor(activity.getColor(R.color.colorSurface));
            adapter.notifyDataSetChanged();
        }, activity);

        rv.setAdapter(adapter);
    }

}
