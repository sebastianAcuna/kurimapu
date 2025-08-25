package cl.smapdev.curimapu.clases.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.viewHolders.OPAlmacigoViewHolder;
import cl.smapdev.curimapu.clases.tablas.OpAlmacigos;

public class OPAlmacigoAdapter extends RecyclerView.Adapter<OPAlmacigoViewHolder> {

    private List<OpAlmacigos> filteredList;
    private final List<OpAlmacigos> OpAlmacigosList;
    private final OnItemClickListener itemClickListenerNuevo;
    private final OnItemClickListener itemClickListenerMenu;
    private final Context context;

    public interface OnItemClickListener {
        void onItemClick(View view, OpAlmacigos almacigo);
    }


    public OPAlmacigoAdapter(List<OpAlmacigos> OpAlmacigosList,
                             OnItemClickListener itemClickListenerNuevo,
                             OnItemClickListener itemClickListenerMenu,
                             Context context) {
        this.OpAlmacigosList = new ArrayList<>(OpAlmacigosList);
        this.filteredList = new ArrayList<>(OpAlmacigosList);
        this.itemClickListenerNuevo = itemClickListenerNuevo;
        this.itemClickListenerMenu = itemClickListenerMenu;
        this.context = context;
    }

    @NonNull
    @Override
    public OPAlmacigoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_op, parent, false);
        return new OPAlmacigoViewHolder(view);
    }


    public void filter(String query) {
        if (query == null || query.trim().isEmpty()) {
            filteredList = new ArrayList<>(OpAlmacigosList);

        } else {
            String lowerQuery = query.toLowerCase();
            filteredList = new ArrayList<>();
            for (OpAlmacigos item : OpAlmacigosList) {
                if ((item.getNombre_parental() != null && item.getNombre_parental().toLowerCase().contains(lowerQuery)) ||
                        (item.getOp() != null && item.getOp().toLowerCase().contains(lowerQuery))) {
                    filteredList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull OPAlmacigoViewHolder holder, int position) {
        holder.bind(filteredList.get(position), itemClickListenerNuevo, itemClickListenerMenu, context);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }
}
