package cl.smapdev.curimapu.clases.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.Fichas;
import cl.smapdev.curimapu.clases.adapters.viewHolders.FichasViewHolder;

public class FichasAdapter extends RecyclerView.Adapter<FichasViewHolder> {

    private List<Fichas> elements;
    private List<Fichas> mfilter;
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongClickListener;
    private Context context;


    public FichasAdapter(List<Fichas> elements,OnItemClickListener itemClickListener, OnItemLongClickListener itemLongClickListener, Context context) {
        this.elements = elements;
        this.mfilter = elements;
        this.itemClickListener = itemClickListener;
        this.itemLongClickListener = itemLongClickListener;
        this.context = context;
    }

    public interface OnItemClickListener{ void onItemClick(Fichas fichas); }
    public interface OnItemLongClickListener{ void onItemLongClick(Fichas fichas);}



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
