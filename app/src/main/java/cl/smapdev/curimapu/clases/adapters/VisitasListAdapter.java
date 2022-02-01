package cl.smapdev.curimapu.clases.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.viewHolders.VisitasListViewHolder;
import cl.smapdev.curimapu.clases.relaciones.VisitasCompletas;
import cl.smapdev.curimapu.clases.tablas.Fotos;

public class VisitasListAdapter extends RecyclerView.Adapter<VisitasListViewHolder> {


    private final List<VisitasCompletas> visitasCompletas;
    private final OnItemClickListener itemClickListenerVer;
    private final OnItemClickListener itemClickListenerEliminar;
    private final Context context;

    public interface OnItemClickListener{ void onItemClick(View view, VisitasCompletas fichas); }



    public VisitasListAdapter(List<VisitasCompletas> visitasCompletas,
                              OnItemClickListener itemClickListenerVer,
                              OnItemClickListener itemClickListenerEliminar,
                              Context context) {
        this.visitasCompletas = visitasCompletas;
        this.itemClickListenerVer = itemClickListenerVer;
        this.itemClickListenerEliminar = itemClickListenerEliminar;
        this.context = context;
    }

    @NonNull
    @Override
    public VisitasListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_visitas,parent,false);
        return new VisitasListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitasListViewHolder holder, int position) {
        holder.bind(visitasCompletas.get(position), itemClickListenerVer, itemClickListenerEliminar,context);
    }

    @Override
    public int getItemCount() {
        return visitasCompletas.size();
    }
}
