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
import cl.smapdev.curimapu.clases.tablas.EstacionFloracionDetalle;

public class EstacionFloracionDetalleAdapter extends RecyclerView.Adapter<EstacionFloracionDetalleListViewHolder> {

    private final List<EstacionFloracionDetalle> estacionFloracionDetalle;
    private final OnItemClickListener itemClickListenerEliminar;
    private final OnItemClickListener itemClickListenerEditar;
    private final Context context;

    public interface OnItemClickListener{ void onItemClick(View view, EstacionFloracionDetalle estaciones); }


    public EstacionFloracionDetalleAdapter(List<EstacionFloracionDetalle> estacionFloracionDetalle,
                                           OnItemClickListener itemClickListenerEliminar,
                                           OnItemClickListener itemClickListenerEditar,
                                           Context context) {
        this.estacionFloracionDetalle = estacionFloracionDetalle;
        this.itemClickListenerEliminar = itemClickListenerEliminar;
        this.itemClickListenerEditar = itemClickListenerEditar;
        this.context = context;
    }

    @NonNull
    @Override
    public EstacionFloracionDetalleListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_muestra_floracion,parent,false);
        return new EstacionFloracionDetalleListViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull EstacionFloracionDetalleListViewHolder holder, int position) {
        holder.bind(estacionFloracionDetalle.get(position), itemClickListenerEliminar, itemClickListenerEditar, context);
    }

    @Override
    public int getItemCount() {
        return estacionFloracionDetalle.size();
    }
}
