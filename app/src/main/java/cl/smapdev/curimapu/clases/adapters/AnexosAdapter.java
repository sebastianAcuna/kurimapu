package cl.smapdev.curimapu.clases.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.viewHolders.AnexosListViewHolder;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;

public class AnexosAdapter extends RecyclerView.Adapter<AnexosListViewHolder> {

    private final List<AnexoCompleto> anexoCompletosList;
    private final OnItemClickListener itemClickListenerNuevo;
    private final OnItemClickListener itemClickListenerMenu;
    private final Context context;

    public interface OnItemClickListener{ void onItemClick(View view, AnexoCompleto anexos); }


    public AnexosAdapter(List<AnexoCompleto> anexoCompletosList,
                              OnItemClickListener itemClickListenerNuevo,
                              OnItemClickListener itemClickListenerMenu,
                              Context context) {
        this.anexoCompletosList = anexoCompletosList;
        this.itemClickListenerNuevo = itemClickListenerNuevo;
        this.itemClickListenerMenu = itemClickListenerMenu;
        this.context = context;
    }

    @NonNull
    @Override
    public AnexosListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_visitas,parent,false);
        return new AnexosListViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull AnexosListViewHolder holder, int position) {
        holder.bind(anexoCompletosList.get(position), itemClickListenerNuevo, itemClickListenerMenu, context);
    }

    @Override
    public int getItemCount() {
        return anexoCompletosList.size();
    }
}
