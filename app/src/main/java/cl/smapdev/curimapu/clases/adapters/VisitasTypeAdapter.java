package cl.smapdev.curimapu.clases.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.Etapas;
import cl.smapdev.curimapu.clases.adapters.viewHolders.VisitasTypeViewHolder;

public class VisitasTypeAdapter extends RecyclerView.Adapter<VisitasTypeViewHolder> {

    private ArrayList<Etapas> elements;
    private Context context;
    private OnItemClickListener cLickListener;
    private String idAnexo;

    public VisitasTypeAdapter(ArrayList<Etapas> elements, Context context, OnItemClickListener cLickListener, String idAnexo) {
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
        holder.bind(elements.get(position), context, cLickListener, idAnexo);
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }
}
