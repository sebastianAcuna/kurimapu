package cl.smapdev.curimapu.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.Agricultor;
import cl.smapdev.curimapu.clases.Comuna;
import cl.smapdev.curimapu.clases.Fichas;
import cl.smapdev.curimapu.clases.Region;

public class FragmentPrincipal extends Fragment {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



//        MainActivity.myAppDB.myDao().insertRegion(new Region("Región del Maule"));
//        MainActivity.myAppDB.myDao().insertRegion(new Region("Región del Biobío"));
//        MainActivity.myAppDB.myDao().insertRegion(new Region("Región del la Araucanía"));
//        MainActivity.myAppDB.myDao().insertRegion(new Region("Región del los Rios"));
//
//        MainActivity.myAppDB.myDao().insertComuna(new Comuna("Talca", 1));
//        MainActivity.myAppDB.myDao().insertComuna(new Comuna("Constitución", 1));
//        MainActivity.myAppDB.myDao().insertComuna(new Comuna("Concepcion", 2));
//        MainActivity.myAppDB.myDao().insertComuna(new Comuna("Los Ángeles", 2));
//        MainActivity.myAppDB.myDao().insertComuna(new Comuna("Mulchén", 2));
//        MainActivity.myAppDB.myDao().insertComuna(new Comuna("Temuco", 3));
//        MainActivity.myAppDB.myDao().insertComuna(new Comuna("Angól", 3));
//        MainActivity.myAppDB.myDao().insertComuna(new Comuna("Villa Rica", 3));
//        MainActivity.myAppDB.myDao().insertComuna(new Comuna("Valdivia", 4));
//        MainActivity.myAppDB.myDao().insertComuna(new Comuna("Rio Bueno", 4));
//        MainActivity.myAppDB.myDao().insertComuna(new Comuna("Los Lagos", 4));
//
//
//        MainActivity.myAppDB.myDao().insertAgricultor(new Agricultor("96.611.780-k","COMERCIAL LT LTDA.","966764273","NN","NN",2,4,false));
//
//        MainActivity.myAppDB.myDao().insertFicha(new Fichas(2019, "96.611.780-k","Poroto","Los Tilos",30.0,"Pivote 30ha suelo trumao categoría A",-36.872088173419165, -72.28010347444638,1,false));
//

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        if (activity != null){
            activity.setDrawerEnabled(true);
            activity.updateView(getResources().getString(R.string.app_name), getResources().getString(R.string.subtitles_start));
        }
    }
}
