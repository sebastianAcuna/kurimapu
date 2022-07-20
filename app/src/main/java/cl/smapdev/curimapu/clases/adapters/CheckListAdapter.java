package cl.smapdev.curimapu.clases.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.viewHolders.CheckListViewHolder;
import cl.smapdev.curimapu.clases.tablas.CheckLists;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListViewHolder> {

    private final List<CheckLists> checkLists;
    private final OnClickListener onClick;
    private final OnClickListener onClickEditar;

    public interface OnClickListener{ void onItemClick( CheckLists checkList ); };


    public CheckListAdapter(List<CheckLists> checkLists, OnClickListener onClick, OnClickListener onClickEditar) {
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
    public void onBindViewHolder(@NonNull CheckListViewHolder holder, int position){

        CheckLists checkList = checkLists.get( position );
        holder.lbl_documento.setText( checkList.getDescCheckList() );


        NestedCheckListAdapter nested = new NestedCheckListAdapter(checkList.getDetails());
        LinearLayoutManager lm = new LinearLayoutManager(holder.itemView.getContext());

        holder.rv_lista_detalle.setLayoutManager(lm);
        holder.rv_lista_detalle.setAdapter(nested);


        holder.btn_exand_rv.setOnClickListener(view -> {
            checkList.setExpanded(!checkList.isExpanded());
            holder.rv_lista_detalle.setVisibility(checkList.isExpanded()
                    ? View.VISIBLE
                    : View.GONE);


            holder.btn_exand_rv.setImageDrawable((checkList.isExpanded())
                    ? holder.itemView.getContext().getDrawable(R.drawable.ic_expand_up)
                    : holder.itemView.getContext().getDrawable(R.drawable.ic_expand_down)
            );
        });


    }

    @Override
    public int getItemCount() {
        return checkLists.size();
    }
}


