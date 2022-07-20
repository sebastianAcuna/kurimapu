package cl.smapdev.curimapu.clases.adapters.viewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.CheckListAdapter;
import cl.smapdev.curimapu.clases.adapters.NestedCheckListAdapter;
import cl.smapdev.curimapu.clases.tablas.CheckListDetails;

public class NestedCheckListViewHolder extends RecyclerView.ViewHolder {

    private final TextView lbl_descripcion;
    private final Button btn_editar;
    private final ImageView btn_ver_pdf, btn_subir_check;

    public NestedCheckListViewHolder(@NonNull View itemView) {
        super(itemView);

        lbl_descripcion = itemView.findViewById(R.id.lbl_descripcion);
        btn_editar = itemView.findViewById(R.id.btn_editar);
        btn_ver_pdf = itemView.findViewById(R.id.btn_ver_pdf);
        btn_subir_check = itemView.findViewById(R.id.btn_subir_check);

    }


    public void bind(CheckListDetails nestedChecklist,
                     NestedCheckListAdapter.OnClickListener onclickPDF,
                     NestedCheckListAdapter.OnClickListener onclickEditar,
                     NestedCheckListAdapter.OnClickListener onclickSubir ) {




        btn_editar.setOnClickListener(view -> onclickEditar.onItemClick(nestedChecklist));
        btn_ver_pdf.setOnClickListener(view -> onclickPDF.onItemClick(nestedChecklist));
        btn_subir_check.setOnClickListener(view -> onclickSubir.onItemClick(nestedChecklist));


        lbl_descripcion.setText(nestedChecklist.getDescription());
    }
}
