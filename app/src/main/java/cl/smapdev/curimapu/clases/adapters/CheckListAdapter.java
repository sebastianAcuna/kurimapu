package cl.smapdev.curimapu.clases.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.viewHolders.CheckListViewHolder;
import cl.smapdev.curimapu.clases.tablas.CheckList;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListViewHolder> {

    private final List<CheckList> checkLists;
    private final OnClickListener onClick;
    private final OnClickListener onClickEditar;

    public interface OnClickListener{ void onItemClick( CheckList checkList ); };


    public CheckListAdapter(List<CheckList> checkLists, OnClickListener onClick, OnClickListener onClickEditar) {
        this.checkLists = checkLists;
        this.onClick = onClick;
        this.onClickEditar = onClickEditar;
    }

    @NonNull
    @Override
    public CheckListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_check_list, parent, false);
        return new CheckListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckListViewHolder holder, int position) {
        holder.bind(checkLists.get(position), onClick, onClickEditar);
    }

    @Override
    public int getItemCount() {
        return checkLists.size();
    }
}
