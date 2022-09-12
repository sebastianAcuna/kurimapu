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
import cl.smapdev.curimapu.clases.tablas.CheckListDetails;
import cl.smapdev.curimapu.clases.tablas.CheckLists;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListViewHolder> {

    private final List<CheckLists> checkLists;
    private final OnClickListener onClickNuevo;
    private final OnClickListenerCallback onClickEditar;
    private final OnClickListenerCallback onClickPDF;
    private final OnClickListenerCallback onClickSubir;

    public interface OnClickListener{ void onItemClick( CheckLists checkList ); };
    public interface OnClickListenerCallback{ void onItemClick(CheckLists checkList, CheckListDetails details); };


    public CheckListAdapter(
            List<CheckLists> checkLists,
            OnClickListener onClick,
            OnClickListenerCallback onClickEditar,
            OnClickListenerCallback onClickPDF,
            OnClickListenerCallback onClickSubir) {
        this.checkLists = checkLists;
        this.onClickNuevo = onClick;
        this.onClickEditar = onClickEditar;
        this.onClickPDF = onClickPDF;
        this.onClickSubir = onClickSubir;
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


        NestedCheckListAdapter nested = new NestedCheckListAdapter(
                checkList.getDetails(),
                checkListDetails -> onClickPDF.onItemClick(checkList, checkListDetails),
                checkListDetails -> onClickEditar.onItemClick(checkList, checkListDetails),
                checkListDetails -> onClickSubir.onItemClick(checkList, checkListDetails)
                );
        LinearLayoutManager lm = new LinearLayoutManager(holder.itemView.getContext());

        holder.rv_lista_detalle.setLayoutManager(lm);
        holder.rv_lista_detalle.setAdapter(nested);

        holder.btn_nuevo.setOnClickListener(view -> onClickNuevo.onItemClick(checkList));

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


