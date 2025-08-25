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
import cl.smapdev.curimapu.clases.adapters.viewHolders.VisitasAlmacigoViewHolder;
import cl.smapdev.curimapu.clases.relaciones.VisitaAlmacigoCompleto;

public class VisitasAlmacigoAdapter extends RecyclerView.Adapter<VisitasAlmacigoViewHolder> {

    private List<VisitaAlmacigoCompleto> filteredList;
    private final List<VisitaAlmacigoCompleto> OpAlmacigosList;
    private final OnItemClickListener itemClickListenerMenu;
    private final Context context;

    public interface OnItemClickListener {
        void onItemClick(View view, VisitaAlmacigoCompleto almacigo);
    }


    public VisitasAlmacigoAdapter(List<VisitaAlmacigoCompleto> OpAlmacigosList,
                                  OnItemClickListener itemClickListenerMenu,
                                  Context context) {
        this.OpAlmacigosList = new ArrayList<>(OpAlmacigosList);
        this.filteredList = new ArrayList<>(OpAlmacigosList);
        this.itemClickListenerMenu = itemClickListenerMenu;
        this.context = context;
    }

    @NonNull
    @Override
    public VisitasAlmacigoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_visita_almacigo, parent, false);
        return new VisitasAlmacigoViewHolder(view);
    }


    public void filter(String query) {
//        if (query == null || query.trim().isEmpty()) {
//            filteredList = new ArrayList<>(OpAlmacigosList);
//
//        } else {
////            String lowerQuery = query.toLowerCase();
////            filteredList = new ArrayList<>();
////            for (VisitaAlmacigoCompleto item : OpAlmacigosList) {
////                if ((item.getNombre_parental() != null && item.getNombre_parental().toLowerCase().contains(lowerQuery)) ||
////                        (item.getOp() != null && item.getOp().toLowerCase().contains(lowerQuery))) {
////                    filteredList.add(item);
////                }
////            }
//        }
//        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull VisitasAlmacigoViewHolder holder, int position) {
        holder.bind(filteredList.get(position), itemClickListenerMenu, context);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }
}
