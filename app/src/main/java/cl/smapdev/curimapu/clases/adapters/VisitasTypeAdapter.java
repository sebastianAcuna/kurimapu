package cl.smapdev.curimapu.clases.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.viewHolders.VisitasTypeViewHolder;

public class VisitasTypeAdapter extends RecyclerView.Adapter<VisitasTypeViewHolder> {

    private String[] elements;
    private Context context;
    private OnItemClickListener cLickListener;
    private int idAnexo;

    public VisitasTypeAdapter(String[] elements, Context context, OnItemClickListener cLickListener, int idAnexo) {
        this.elements = elements;
        this.context = context;
        this.cLickListener = cLickListener;
        this.idAnexo = idAnexo;
    }

    public interface OnItemClickListener{ void onItemClick(int position); }



    @NonNull
    @Override
    public VisitasTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_visitas_type,parent, false);
        return new VisitasTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitasTypeViewHolder holder, int position) {
        holder.bind(elements[position], context, cLickListener, idAnexo);
    }

    @Override
    public int getItemCount() {
        return elements.length;
    }
}
