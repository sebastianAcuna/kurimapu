package cl.smapdev.curimapu.fragments.contratos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.FotosListAdapter;

public class FragmentFotosResumen  extends Fragment {

    private MainActivity activity = null ;

    private RecyclerView rwAgronomo, rwCliente;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (MainActivity) getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fotos_resumen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        rwAgronomo = (RecyclerView) view.findViewById(R.id.fotos_agronomos);
        rwCliente = (RecyclerView) view.findViewById(R.id.fotos_clientes);


        agregarImagenToAgronomos();
        agregarImagenToClientes();

    }



    private void agregarImagenToAgronomos(){



        LinearLayoutManager lManager = null;
        if (activity != null){
//            lManager = new GridLayoutManager(activity, 1);
             lManager  = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);

        }

        rwAgronomo.setHasFixedSize(true);
        rwAgronomo.setLayoutManager(lManager);


        int[] myImageList = new int[]{R.drawable.f1, R.drawable.f2 };


        FotosListAdapter adapter = new FotosListAdapter(activity,myImageList, new FotosListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int fotos) {
//                showAlertForUpdate(fotos);
            }
        }, new FotosListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int fotos) {

            }
        });


        rwAgronomo.setAdapter(adapter);
    }


    private void agregarImagenToClientes(){



        LinearLayoutManager lManager = null;
        if (activity != null){
//            lManager = new GridLayoutManager(activity, 1);
            lManager  = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);

        }

        rwCliente.setHasFixedSize(true);
        rwCliente.setLayoutManager(lManager);


        int[] myImageList = new int[]{R.drawable.f1, R.drawable.f2,R.drawable.f3, R.drawable.f4,R.drawable.f5 };


        FotosListAdapter adapter = new FotosListAdapter(activity,myImageList, new FotosListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int fotos) {
//                showAlertForUpdate(fotos);
            }
        }, new FotosListAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int fotos) {

            }
        });


        rwCliente.setAdapter(adapter);
    }







}
