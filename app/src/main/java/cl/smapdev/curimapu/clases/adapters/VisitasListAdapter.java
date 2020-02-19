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


    private List<VisitasCompletas> visitasCompletas;
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongClickListener;
    private Context context;

    public interface OnItemClickListener{ void onItemClick(View view, VisitasCompletas fichas, Fotos fotos); }
    public interface OnItemLongClickListener{ void onItemLongClick(View view, VisitasCompletas fichas, Fotos fotos);}


    public VisitasListAdapter(List<VisitasCompletas> visitasCompletas, OnItemClickListener itemClickListener, OnItemLongClickListener itemLongClickListener, Context context) {
        this.visitasCompletas = visitasCompletas;
        this.itemClickListener = itemClickListener;
        this.itemLongClickListener = itemLongClickListener;
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
        holder.bind(visitasCompletas.get(position), itemClickListener, itemLongClickListener,context);
    }

    @Override
    public int getItemCount() {
        return visitasCompletas.size();
    }
}
