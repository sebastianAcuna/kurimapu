package cl.smapdev.curimapu.fragments.contratos;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.FotosListAdapter;

public class FragmentFotos extends Fragment {

    private FloatingActionMenu materialDesignFAM;
    private FloatingActionButton floatingActionButton1, floatingActionButton2;

    private RecyclerView recyclerView;
    private MainActivity activity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity a = (MainActivity) getActivity();
        if (a != null){
            activity  =  a;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fotos, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        materialDesignFAM = (FloatingActionMenu) view.findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) view.findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (FloatingActionButton) view.findViewById(R.id.material_design_floating_action_menu_item2);
        recyclerView = (RecyclerView) view.findViewById(R.id.lista_fotos);



        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked

            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu second item clicked

            }
        });

        agregarImagenToList();

    }


    private void agregarImagenToList(){

        RecyclerView.LayoutManager lManager = null;
        if (activity != null){
            if(activity.getRotation(activity).equals("v")){
                lManager = new GridLayoutManager(activity, 3);
            }else{
                lManager = new GridLayoutManager(activity, 2);
            }
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lManager);

//            AsyncTask.execute(new Runnable() {
//                @Override
//                public void run() {
//                    final List<Fotos> packageIn =  InfoPacking.myAppDB.myDao().getFotosById(ID_PAQUETE);


//                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {

                            int[] myImageList = new int[]{R.drawable.f1, R.drawable.f2,R.drawable.f3, R.drawable.f4,R.drawable.f5 };


                            FotosListAdapter adapter = new FotosListAdapter(activity,myImageList, new FotosListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int fotos) {
                                    showAlertForUpdate(fotos);
                                }
                            }, new FotosListAdapter.OnItemLongClickListener() {
                                @Override
                                public void onItemLongClick(int fotos) {

                                }
                            });


                            recyclerView.setAdapter(adapter);
                        }
//                    });
//                }
//            });
//    }



    private void showAlertForUpdate(int idFoto){
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
        Picasso.get().load(idFoto).resize(720,800).centerInside().into(imageView);
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
