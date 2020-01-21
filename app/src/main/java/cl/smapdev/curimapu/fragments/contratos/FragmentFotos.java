package cl.smapdev.curimapu.fragments.contratos;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.List;
import java.util.Objects;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.FotosListAdapter;

public class FragmentFotos extends Fragment {

    private FloatingActionMenu materialDesignFAM;
    private FloatingActionButton floatingActionButton1, floatingActionButton2;

    private RecyclerView recyclerView;

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


    public void agregarImagenToList(){

        RecyclerView.LayoutManager lManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lManager);

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
//                    final List<Fotos> packageIn =  InfoPacking.myAppDB.myDao().getFotosById(ID_PAQUETE);


                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            FotosListAdapter adapter = new FotosListAdapter(getResources().getStringArray(R.array.fotos), new FotosListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(String fotos) {

                                }
                            }, new FotosListAdapter.OnItemLongClickListener() {
                                @Override
                                public void onItemLongClick(String fotos) {

                                }
                            });
                            recyclerView.setAdapter(adapter);
                        }
                    });
                }
            });
    }
}
