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

    private final OnClickListener onClickEditar;
    private final OnClickListener onClickPDF;
    private final OnClickListener onClickSubir;

    public interface OnClickListener{ void onItemClick( CheckListDetails checkListDetails ); };

    public NestedCheckListAdapter(List<CheckListDetails> nested, OnClickListener onClickEditar, OnClickListener onClickPDF, OnClickListener onClickSubir) {
        this.nestedDetails = nested;
        this.onClickEditar = onClickEditar;
        this.onClickPDF = onClickPDF;
        this.onClickSubir = onClickSubir;
    }

    @NonNull
    @Override
    public NestedCheckListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_detalle_checklist, parent, false);
        return new NestedCheckListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NestedCheckListViewHolder holder, int position) {
        holder.bind(nestedDetails.get(position), onClickPDF, onClickEditar, onClickSubir);
    }

    @Override
    public int getItemCount() {
        return nestedDetails.size();
    }
}
