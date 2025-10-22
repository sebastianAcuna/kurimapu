package cl.smapdev.curimapu.clases.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.viewHolders.EstacionFloracionListViewHolder;
import cl.smapdev.curimapu.clases.relaciones.EstacionFloracionCompleto;

public class EstacionFloracionAdapter extends RecyclerView.Adapter<EstacionFloracionListViewHolder> {

    private final List<EstacionFloracionCompleto> estacionFloracionCompletoList;
    private final OnItemClickListener itemClickListenerEliminar;
    private final OnItemClickListener itemClickListenerEditar;
    private final Context context;

    public interface OnItemClickListener {
        void onItemClick(View view, EstacionFloracionCompleto estaciones);
    }


    public EstacionFloracionAdapter(List<EstacionFloracionCompleto> estacionFloracionCompletoList,
                                    OnItemClickListener itemClickListenerEliminar,
                                    OnItemClickListener itemClickListenerEditar,
                                    Context context) {
        this.estacionFloracionCompletoList = estacionFloracionCompletoList;
        this.itemClickListenerEliminar = itemClickListenerEliminar;
        this.itemClickListenerEditar = itemClickListenerEditar;
        this.context = context;
    }

    @NonNull
    @Override
    public EstacionFloracionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_estacion_floracion, parent, false);
        return new EstacionFloracionListViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull EstacionFloracionListViewHolder holder, int position) {
        holder.bind(estacionFloracionCompletoList.get(position), itemClickListenerEliminar, itemClickListenerEditar, context);
    }

    @Override
    public int getItemCount() {
        return estacionFloracionCompletoList.size();
    }

}
