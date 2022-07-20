package cl.smapdev.curimapu.clases.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.viewHolders.NestedCheckListViewHolder;
import cl.smapdev.curimapu.clases.tablas.CheckListDetails;

public class NestedCheckListAdapter extends RecyclerView.Adapter<NestedCheckListViewHolder> {

    List<CheckListDetails> nestedDetails;

    public NestedCheckListAdapter(List<CheckListDetails> nested) {
        this.nestedDetails = nested;
    }

    @NonNull
    @Override
    public NestedCheckListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_detalle_checklist, parent, false);
        return new NestedCheckListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NestedCheckListViewHolder holder, int position) {
        holder.bind(nestedDetails.get(position));
    }

    @Override
    public int getItemCount() {
        return nestedDetails.size();
    }
}
