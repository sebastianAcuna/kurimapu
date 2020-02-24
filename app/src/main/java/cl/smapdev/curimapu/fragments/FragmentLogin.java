package cl.smapdev.curimapu.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;


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


        shared = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprobar();
            }
        });


    }



    private void comprobar(){
        String user = user_login.getText().toString();
        String pass = pass_login.getText().toString();

        if (TextUtils.isEmpty(user) && TextUtils.isEmpty(pass)){
            Toast.makeText(getActivity(), "Debe ingresar usuario y contraseña", Toast.LENGTH_SHORT).show();
        }else if(user.equals("juliette") && pass.equals("1163")){

            shared.edit().remove(Utilidades.SHARED_USER).apply();
            shared.edit().putString(Utilidades.SHARED_USER, user).apply();

            MainActivity activity = (MainActivity) getActivity();
            if (activity != null){
                activity.cambiarFragment(new FragmentPrincipal(), Utilidades.FRAGMENT_INICIO, R.anim.slide_in_left, R.anim.slide_out_left);
            }


        }else{
            Toast.makeText(getActivity(), "Usuario y/o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
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
