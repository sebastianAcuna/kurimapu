package cl.smapdev.curimapu.clases.adapters.viewHolders;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.NestedCheckListViewHolder;

public class NestedCheckListAdapter extends RecyclerView.Adapter<NestedCheckListViewHolder> {

    List<String> nested;

    public NestedCheckListAdapter(List<String> nested) {
        this.nested = nested;
    }

    @NonNull
    @Override
    public NestedCheckListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_nested_checklist, parent, false);
        return new NestedCheckListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NestedCheckListViewHolder holder, int position) {
        holder.bind(nested.get(position));
    }

    @Override
    public int getItemCount() {
        return nested.size();
    }
}
