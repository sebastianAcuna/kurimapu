package cl.smapdev.curimapu.clases.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.viewHolders.EstacionFloracionDetalleListViewHolder;
import cl.smapdev.curimapu.clases.adapters.viewHolders.EstacionFloracionEstacionesListViewHolder;
import cl.smapdev.curimapu.clases.relaciones.EstacionesCompletas;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracionDetalle;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracionEstaciones;

public class EstacionFloracionEstacionesAdapter extends RecyclerView.Adapter<EstacionFloracionEstacionesListViewHolder> {

    private final List<EstacionesCompletas> estaciones;
    private final OnItemClickListener itemClickListenerEliminar;
    private final OnItemClickListener itemClickListenerEditar;
    private final Context context;
    private int estado;

    public interface OnItemClickListener{ void onItemClick(View view, EstacionesCompletas estaciones); }


    public EstacionFloracionEstacionesAdapter(List<EstacionesCompletas> estaciones,
                                              OnItemClickListener itemClickListenerEliminar,
                                              OnItemClickListener itemClickListenerEditar,
                                              Context context,
                                              int estado) {
        this.estaciones = estaciones;
        this.itemClickListenerEliminar = itemClickListenerEliminar;
        this.itemClickListenerEditar = itemClickListenerEditar;
        this.context = context;
        this.estado = estado;
    }

    @NonNull
    @Override
    public EstacionFloracionEstacionesListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_estaciones,parent,false);
        return new EstacionFloracionEstacionesListViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull EstacionFloracionEstacionesListViewHolder holder, int position) {
        holder.bind(estaciones.get(position), position + 1,  estado,  itemClickListenerEliminar, itemClickListenerEditar, context);
    }

    @Override
    public int getItemCount() {
        return estaciones.size();
    }
}
