package cl.smapdev.curimapu.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.tablas.Variedad;
import cl.smapdev.curimapu.clases.tablas.Agricultor;
import cl.smapdev.curimapu.clases.tablas.AnexoContrato;
import cl.smapdev.curimapu.clases.tablas.Comuna;
import cl.smapdev.curimapu.clases.tablas.Especie;
import cl.smapdev.curimapu.clases.tablas.Region;

public class FragmentPrincipal extends Fragment {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if(MainActivity.myAppDB.myDao().getComunas().size() <= 0){

            MainActivity.myAppDB.myDao().insertComuna(new Comuna("Talca", 1));
            MainActivity.myAppDB.myDao().insertComuna(new Comuna("Constitución", 1));
            MainActivity.myAppDB.myDao().insertComuna(new Comuna("Concepcion", 2));
            MainActivity.myAppDB.myDao().insertComuna(new Comuna("Los Ángeles", 2));
            MainActivity.myAppDB.myDao().insertComuna(new Comuna("Mulchén", 2));
            MainActivity.myAppDB.myDao().insertComuna(new Comuna("Temuco", 3));
            MainActivity.myAppDB.myDao().insertComuna(new Comuna("Angól", 3));
            MainActivity.myAppDB.myDao().insertComuna(new Comuna("Villa Rica", 3));
            MainActivity.myAppDB.myDao().insertComuna(new Comuna("Valdivia", 4));
            MainActivity.myAppDB.myDao().insertComuna(new Comuna("Rio Bueno", 4));
            MainActivity.myAppDB.myDao().insertComuna(new Comuna("Los Lagos", 4));
        }


        if (MainActivity.myAppDB.myDao().getRegiones().size() <= 0){
                MainActivity.myAppDB.myDao().insertRegion(new Region("Región del Maule"));
                MainActivity.myAppDB.myDao().insertRegion(new Region("Región del Biobío"));
                MainActivity.myAppDB.myDao().insertRegion(new Region("Región del la Araucanía"));
                MainActivity.myAppDB.myDao().insertRegion(new Region("Región del los Rios"));
        }

        if (MainActivity.myAppDB.myDao().getEspecies().size() <= 0 ){
            MainActivity.myAppDB.myDao().insertEspecie(new Especie("Maravilla"));
            MainActivity.myAppDB.myDao().insertEspecie(new Especie("Frejol"));
            MainActivity.myAppDB.myDao().insertEspecie(new Especie("Canola"));
            MainActivity.myAppDB.myDao().insertEspecie(new Especie("Arveja"));
        }

        if (MainActivity.myAppDB.myDao().getAgricultores().size() <= 0){
            MainActivity.myAppDB.myDao().insertAgricultor(new Agricultor("96.611.780-k","agricultor test","948426521","admin test","948426521",1,1,false));
            MainActivity.myAppDB.myDao().insertAgricultor(new Agricultor("18.804.066-7","Sebastian Acuña","948426521","admin test","948426521",2,4,false));
            MainActivity.myAppDB.myDao().insertAgricultor(new Agricultor("18.805.186-3","agricultor 2","948426521","admin test","948426521",3,7,false));
            MainActivity.myAppDB.myDao().insertAgricultor(new Agricultor("19.714.871-3","Josue Parada","948426521","admin test","948426521",4,9,false));
        }


        if (MainActivity.myAppDB.myDao().getVariedades().size() <= 0){
            MainActivity.myAppDB.myDao().insertVariedad(new Variedad(1,"278A (1627A) FEMALE")); /*1*/
            MainActivity.myAppDB.myDao().insertVariedad(new Variedad(1,"FS73020 FEMALE"));
            MainActivity.myAppDB.myDao().insertVariedad(new Variedad(1,"FR84222 MALE"));
            MainActivity.myAppDB.myDao().insertVariedad(new Variedad(1,"54Ax18B FEMALE"));


            MainActivity.myAppDB.myDao().insertVariedad(new Variedad(2,"PV 792")); /*5*/
            MainActivity.myAppDB.myDao().insertVariedad(new Variedad(2,"PV766"));
            MainActivity.myAppDB.myDao().insertVariedad(new Variedad(2,"PV 609"));
            MainActivity.myAppDB.myDao().insertVariedad(new Variedad(2,"PV 653 ENVASADO"));


            MainActivity.myAppDB.myDao().insertVariedad(new Variedad(3,"PR4CN-610 MALE")); /*9*/
            MainActivity.myAppDB.myDao().insertVariedad(new Variedad(3,"PA1CN-129 FEMALE"));
            MainActivity.myAppDB.myDao().insertVariedad(new Variedad(3,"SSC661R MALE ENVASADO"));
            MainActivity.myAppDB.myDao().insertVariedad(new Variedad(3,"WSC556A FEMALE ENVASADO"));

            MainActivity.myAppDB.myDao().insertVariedad(new Variedad(4,"UTRILLO")); /*13*/
            MainActivity.myAppDB.myDao().insertVariedad(new Variedad(4,"RRBF 4575"));
            MainActivity.myAppDB.myDao().insertVariedad(new Variedad(4,"COBLENTZ"));
        }


        if (MainActivity.myAppDB.myDao().getAnexos().size() <= 0){

            MainActivity.myAppDB.myDao().insertAnexo(new AnexoContrato("20-AR0011",1,1,"96.611.780-k","Privote",1));
            MainActivity.myAppDB.myDao().insertAnexo(new AnexoContrato("20-AR0031",2,1,"96.611.780-k","El Roble 1",3));
            MainActivity.myAppDB.myDao().insertAnexo(new AnexoContrato("20-AR0009",3,1,"18.804.066-7","El Ajial",4));
            MainActivity.myAppDB.myDao().insertAnexo(new AnexoContrato("20-AR0079",5,2,"18.804.066-7","Privote B",1));
            MainActivity.myAppDB.myDao().insertAnexo(new AnexoContrato("20-AR0010",6,2,"19.971.871-3","Pivote Chico Sur",3));
            MainActivity.myAppDB.myDao().insertAnexo(new AnexoContrato("20-AR0032",7,2,"19.971.871-3","CENTRO CORDILLERA",4));
            MainActivity.myAppDB.myDao().insertAnexo(new AnexoContrato("20-AR0136",9,3,"96.611.780-k","ARVEJA",1));
            MainActivity.myAppDB.myDao().insertAnexo(new AnexoContrato("20-AR0139",10,3,"96.611.780-k","PAÑO 3",3));
            MainActivity.myAppDB.myDao().insertAnexo(new AnexoContrato("20-AR0138",11,3,"18.804.066-7","PAÑO 2",4));
            MainActivity.myAppDB.myDao().insertAnexo(new AnexoContrato("20-AR0137",13,4,"18.804.066-7","PAÑO 1",1));
            MainActivity.myAppDB.myDao().insertAnexo(new AnexoContrato("20-AR0140",14,4,"19.971.871-3","PAÑO 4",3));
            MainActivity.myAppDB.myDao().insertAnexo(new AnexoContrato("20-AR0300",15,4,"19.971.871-3","CENTRO CORDILLERA 2",4));


        }



//

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
