package cl.smapdev.curimapu.fragments.contratos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class FragmentResumen extends Fragment {

    private MainActivity activity = null ;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity a =(MainActivity) getActivity();
        if(a != null) activity = a;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_resumen, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (activity != null){
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fotos_resumenes, FragmentFotos.getInstance(1), Utilidades.FRAGMENT_FOTOS).commit();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (activity != null){
//                Toast.makeText(activity, "Visible resumen", Toast.LENGTH_SHORT).show();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fotos_resumenes, FragmentFotos.getInstance(1), Utilidades.FRAGMENT_FOTOS).commit();
            }
        }
    }

}
