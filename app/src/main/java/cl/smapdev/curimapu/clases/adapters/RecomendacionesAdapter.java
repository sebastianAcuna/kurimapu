package cl.smapdev.curimapu.clases.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.viewHolders.RecomendacionesViewHolder;
import cl.smapdev.curimapu.clases.tablas.Evaluaciones;

public class RecomendacionesAdapter extends RecyclerView.Adapter<RecomendacionesViewHolder> {

    private final List<Evaluaciones> evaluacionesList;
    private final OnItemClickListener itemClickListenerApruebo;
    private final OnItemClickListener itemClickListenerRechazo;
    private final Context context;

    public interface OnItemClickListener{ void onItemClick(View view, Evaluaciones evaluaciones); }


    public RecomendacionesAdapter(List<Evaluaciones> evaluacionesList,
                                  OnItemClickListener itemClickListenerApruebo,
                                  OnItemClickListener itemClickListenerRechazo,
                                  Context context) {
        this.evaluacionesList = evaluacionesList;
        this.itemClickListenerApruebo = itemClickListenerApruebo;
        this.itemClickListenerRechazo = itemClickListenerRechazo;
        this.context = context;
    }

    @NonNull
    @Override
    public RecomendacionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_recomendaciones,parent,false);
        return new RecomendacionesViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecomendacionesViewHolder holder, int position) {
        holder.bind(evaluacionesList.get(position), itemClickListenerApruebo, itemClickListenerRechazo, context);
    }

    @Override
    public int getItemCount() {
        return evaluacionesList.size();
    }
}
