package cl.smapdev.curimapu.clases.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.viewHolders.FichasViewHolder;
import cl.smapdev.curimapu.clases.relaciones.FichasCompletas;

public class FichasAdapter extends RecyclerView.Adapter<FichasViewHolder> {

    private final List<FichasCompletas> elements;
    private final List<FichasCompletas> mfilter;
    private final OnItemClickListener itemClickListener;
    private final OnItemLongClickListener itemLongClickListener;
    private final Context context;


    public FichasAdapter(List<FichasCompletas> elements,OnItemClickListener itemClickListener, OnItemLongClickListener itemLongClickListener, Context context) {
        this.elements = elements;
        this.mfilter = elements;
        this.itemClickListener = itemClickListener;
        this.itemLongClickListener = itemLongClickListener;
        this.context = context;
    }

    public interface OnItemClickListener{ void onItemClick(FichasCompletas fichas); }
    public interface OnItemLongClickListener{ void onItemLongClick(FichasCompletas fichas);}



    @NonNull
    @Override
    public FichasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_fichas,parent,false);
        return new FichasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FichasViewHolder holder, int position) {
        holder.bind(elements.get(position), itemClickListener, itemLongClickListener,context);
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }
}
