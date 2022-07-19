package cl.smapdev.curimapu.clases.adapters.viewHolders;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.CheckListAdapter;
import cl.smapdev.curimapu.clases.tablas.CheckList;

public class CheckListViewHolder extends RecyclerView.ViewHolder {

    private final TextView lbl_estad_doc;
    private final TextView lbl_documento;
    private final TextView lbl_estado_sincro;
//    private final Button btn_nuevo_checklist;
//    private final Button btn_editar_checklist;
    private final ImageView btn_subir_check;
    private final ImageView btn_exand_rv;
    private final RecyclerView rv_lista_detalle;

    private Context context;
    public CheckListViewHolder(@NonNull View itemView) {
        super(itemView);
        lbl_estad_doc = itemView.findViewById(R.id.lbl_estad_doc);
        lbl_documento = itemView.findViewById(R.id.lbl_documento);
        lbl_estado_sincro = itemView.findViewById(R.id.lbl_estado_sincro);
//        btn_nuevo_checklist = itemView.findViewById(R.id.btn_nuevo_checklist);
//        btn_editar_checklist = itemView.findViewById(R.id.btn_editar_checklist);
        btn_subir_check = itemView.findViewById(R.id.btn_subir_check);
        btn_exand_rv = itemView.findViewById(R.id.btn_exand_rv);
        rv_lista_detalle = itemView.findViewById(R.id.rv_lista_detalle);

        this.context = itemView.getContext();
    }

    public void bind(CheckList checkList, CheckListAdapter.OnClickListener onclick, CheckListAdapter.OnClickListener onclickEdit){


        lbl_estad_doc.setText( (checkList.getEstadoDocumento() == 1) ? "COMPLETA" : (checkList.getIdDocumentoPendiente() <= 0) ? "PENDIENTE" : "EN TRABAJO" );
        lbl_estado_sincro.setText( (checkList.getEstadoSincronizazion() == 1) ? "SINCRONIZADO" : "NO SINCRONIZADO" );
        lbl_documento.setText( checkList.getDescTipoDocumento() );

        btn_exand_rv.setOnClickListener(view -> checkList.setExpanded(!checkList.isExpanded()));

        rv_lista_detalle.setVisibility((checkList.isExpanded()) ? View.VISIBLE : View.GONE);
        btn_subir_check.setVisibility( (checkList.getEstadoSincronizazion() == 1) ? View.GONE : View.VISIBLE);

        LinearLayoutManager lManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        rv_lista_detalle.setHasFixedSize(true);
        rv_lista_detalle.setLayoutManager(lManager);


        NestedCheckListAdapter adapter = new NestedCheckListAdapter(  checkList.getNestedChecks()  );
        rv_lista_detalle.setAdapter(adapter);


//        btn_nuevo_checklist.setEnabled(!(checkList.getEstadoDocumento() == 1 && checkList.getEstadoSincronizazion() == 0));
//
//        btn_editar_checklist.setVisibility(
//                !(!(checkList.getEstadoDocumento() == 1 && checkList.getEstadoSincronizazion() == 1)
//                && !(checkList.getIdDocumentoPendiente() <= 0)) ? View.GONE : View.VISIBLE);



//        btn_nuevo_checklist.setOnClickListener(view -> onclick.onItemClick(checkList));
//        btn_editar_checklist.setOnClickListener(view -> onclickEdit.onItemClick(checkList));


    }
}
