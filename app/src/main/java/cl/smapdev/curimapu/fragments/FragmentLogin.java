package cl.smapdev.curimapu.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.lang.ref.WeakReference;
import java.util.List;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.relaciones.GsonDescargas;
import cl.smapdev.curimapu.clases.retrofit.ApiService;
import cl.smapdev.curimapu.clases.retrofit.RetrofitClient;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.utilidades.Descargas;
import cl.smapdev.curimapu.clases.utilidades.InternetStateClass;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.clases.utilidades.returnValuesFromAsyntask;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentLogin extends Fragment {

    private TextInputEditText user_login, pass_login;
    private SharedPreferences shared;

    private MainActivity activity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (MainActivity) getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user_login = (TextInputEditText) view.findViewById(R.id.user_login);
        pass_login = (TextInputEditText) view.findViewById(R.id.pass_login);
        Button btn_login = (Button) view.findViewById(R.id.btn_login);



        pass_login.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    /* Write your logic here that will be executed when user taps next button */
                    comprobar();
                }
                return false;
            }
        });


        shared = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprobar();
            }
        });



    }


    @Override
    public void onResume() {
        super.onResume();

        if (activity != null){
            final String androidID = Settings.System.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
            Config config = MainActivity.myAppDB.myDao().getConfig();

//            System.out.println(androidID);
            Log.e("IMEI", androidID);

            final int id = (config == null) ? 0 : config.getId();

            InternetStateClass mm = new InternetStateClass(activity, new returnValuesFromAsyntask() {
                @Override
                public void myMethod(boolean result) {
                    if (result) {
                        Descargas.primeraDescarga(activity, androidID, id, Utilidades.APPLICATION_VERSION);
                    } else {
                        Toasty.warning(activity, activity.getResources().getString(R.string.sync_not_internet), Toast.LENGTH_SHORT, true).show();
                    }
                }
            }, 1);
            mm.execute();

        }
    }

    private void comprobar(){
        String user = user_login.getText().toString();
        String pass = pass_login.getText().toString();



        if (TextUtils.isEmpty(user) && TextUtils.isEmpty(pass)){
            Toasty.warning(activity, activity.getResources().getString(R.string.warning_empty_fields), Toast.LENGTH_SHORT, true).show();
        }else{

            String encryptedPass = Utilidades.getMD5(pass);


            Usuario usuario  = MainActivity.myAppDB.myDao().getUsuarioLogin(user, encryptedPass);

            if (usuario != null){

                shared.edit().remove(Utilidades.SHARED_USER).apply();
                shared.edit().putString(Utilidades.SHARED_USER, user).apply();

                Config cnf = MainActivity.myAppDB.myDao().getConfig();
                cnf.setId_usuario(usuario.getId_usuario());
                MainActivity.myAppDB.myDao().updateConfig(cnf);

                MainActivity activity = (MainActivity) getActivity();
                if (activity != null){
                    Utilidades.hideKeyboard(activity);
                    if (usuario.getTipo_usuario() == 5){
                        activity.cambiarFragment(new servidorFragment(), Utilidades.FRAGMENT_SERVIDOR, R.anim.slide_in_left, R.anim.slide_out_left);
                    }else{
                        shared.edit().putString(Utilidades.SHARED_SERVER_ID_USER, String.valueOf(usuario.getId_usuario())).apply();
                        shared.edit().putString(Utilidades.SHARED_SERVER_ID_SERVER, Utilidades.IP_PRODUCCION).apply();

                        Config cn = MainActivity.myAppDB.myDao().getConfig();
                        cn.setServidorSeleccionado(shared.getString(Utilidades.SHARED_SERVER_ID_SERVER, Utilidades.IP_PRODUCCION));
                        cn.setId_usuario_suplandato(usuario.getId_usuario());
                        MainActivity.myAppDB.myDao().updateConfig(cn);

                        activity.cambiarFragment(new FragmentPrincipal(), Utilidades.FRAGMENT_INICIO, R.anim.slide_in_left, R.anim.slide_out_left);
                    }
                }
            }else{
                Toasty.error(activity, activity.getResources().getString(R.string.warning_incorrect_fields), Toast.LENGTH_SHORT, true).show();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        if (activity != null){
            activity.setDrawerEnabled(false);
        }
    }

}
