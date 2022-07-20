package cl.smapdev.curimapu.clases.adapters.viewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.tablas.CheckListDetails;

public class NestedCheckListViewHolder extends RecyclerView.ViewHolder {

    private TextView lbl_descripcion;
    public NestedCheckListViewHolder(@NonNull View itemView) {
        super(itemView);

        lbl_descripcion = itemView.findViewById(R.id.lbl_descripcion);
    }


    public void bind( CheckListDetails nestedChecklist ) {

        lbl_descripcion.setText(nestedChecklist.getDescription());
    }
}
