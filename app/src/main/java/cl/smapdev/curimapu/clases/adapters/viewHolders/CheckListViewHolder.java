package cl.smapdev.curimapu.clases.adapters.viewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cl.smapdev.curimapu.R;

public class CheckListViewHolder extends RecyclerView.ViewHolder {

    public TextView lbl_documento;
    public ImageView btn_exand_rv;
    public RecyclerView rv_lista_detalle;
    public Button btn_nuevo;

    public CheckListViewHolder(@NonNull View itemView) {
        super(itemView);
        lbl_documento = itemView.findViewById(R.id.lbl_documento);
        btn_exand_rv = itemView.findViewById(R.id.btn_exand_rv);
        rv_lista_detalle = itemView.findViewById(R.id.rv_lista_detalle);
        btn_nuevo = itemView.findViewById(R.id.btn_nuevo);
    }


}
