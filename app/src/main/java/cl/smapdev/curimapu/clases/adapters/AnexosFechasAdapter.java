package cl.smapdev.curimapu.clases.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.viewHolders.AnexosCorreosFechasViewHolder;
import cl.smapdev.curimapu.clases.relaciones.AnexoWithDates;

public class AnexosFechasAdapter extends RecyclerView.Adapter<AnexosCorreosFechasViewHolder> {

    private final List<AnexoWithDates> anexoCorreosFechasList;
    private final OnItemClickListener itemClickListenerCambiar;
    private final Context context;

    public interface OnItemClickListener{ void onItemClick(View view, AnexoWithDates anexos); }


    public AnexosFechasAdapter(List<AnexoWithDates> anexoCorreosFechasList,
                               OnItemClickListener itemClickListenerCambiar,
                               Context context) {
        this.anexoCorreosFechasList = anexoCorreosFechasList;
        this.itemClickListenerCambiar = itemClickListenerCambiar;
        this.context = context;
    }

    @NonNull
    @Override
    public AnexosCorreosFechasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_anexos_fechas,parent,false);
        return new AnexosCorreosFechasViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull AnexosCorreosFechasViewHolder holder, int position) {
        holder.bind(anexoCorreosFechasList.get(position), itemClickListenerCambiar, context);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return anexoCorreosFechasList.size();
    }
}
