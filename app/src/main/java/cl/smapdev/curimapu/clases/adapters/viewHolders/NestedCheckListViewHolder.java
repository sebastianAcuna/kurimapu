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
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class NestedCheckListViewHolder extends RecyclerView.ViewHolder {

    private final TextView lbl_descripcion, lbl_estado_documento;
    private final Button btn_editar;
    private final ImageView btn_ver_pdf, btn_subir_check;
    private final View view;

    public NestedCheckListViewHolder(@NonNull View itemView) {
        super(itemView);

        lbl_descripcion = itemView.findViewById(R.id.lbl_descripcion);
        btn_editar = itemView.findViewById(R.id.btn_editar);
        btn_ver_pdf = itemView.findViewById(R.id.btn_ver_pdf);
        btn_subir_check = itemView.findViewById(R.id.btn_subir_check);
        lbl_estado_documento = itemView.findViewById(R.id.lbl_estado_documento);
        view = itemView;
    }


    public void bind(CheckListDetails nestedChecklist,
                     NestedCheckListAdapter.OnClickListener onclickPDF,
                     NestedCheckListAdapter.OnClickListener onclickEditar,
                     NestedCheckListAdapter.OnClickListener onclickSubir ) {


        if(nestedChecklist.isUploaded()){
            btn_subir_check.setColorFilter(view.getContext().getColor(R.color.colorGreen));
        }else{
            btn_ver_pdf.setEnabled(false);
            btn_ver_pdf.setColorFilter(view.getContext().getColor(R.color.colorGrey));
        }

        btn_editar.setOnClickListener(view -> onclickEditar.onItemClick(nestedChecklist));
        btn_ver_pdf.setOnClickListener(view -> onclickPDF.onItemClick(nestedChecklist));
        btn_subir_check.setOnClickListener(view -> onclickSubir.onItemClick(nestedChecklist));


        lbl_descripcion.setText(nestedChecklist.getDescription());
        lbl_estado_documento.setText(nestedChecklist.getDescEstado());
    }
}
