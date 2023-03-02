package cl.smapdev.curimapu.clases.adapters.viewHolders;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.AnexosAdapter;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;

public class AnexosListViewHolder extends RecyclerView.ViewHolder {

    private final TextView num_anexo;
    private final TextView especie;
    private final TextView agricultor;
    private final TextView potrero;
    private final TextView variedad;
    private final Button btn_add_visita;
    private final Button btn_menu_visita;


    public AnexosListViewHolder(@NonNull View itemView) {
        super(itemView);
        num_anexo  = itemView.findViewById(R.id.num_anexo);
        especie  = itemView.findViewById(R.id.especie);
        agricultor  = itemView.findViewById(R.id.agricultor);
        potrero  = itemView.findViewById(R.id.potrero);
        variedad  = itemView.findViewById(R.id.variedad);
        btn_add_visita  = itemView.findViewById(R.id.btn_add_visita);
        btn_menu_visita  = itemView.findViewById(R.id.btn_menu_visita);

    }


    public void bind (final AnexoCompleto anexo, AnexosAdapter.OnItemClickListener onClickNuevo, AnexosAdapter.OnItemClickListener onClickMenu, Context context){

        if( context  != null){

            num_anexo.setText(anexo.getAnexoContrato().getAnexo_contrato().toUpperCase());
            especie.setText(anexo.getEspecie().getDesc_especie().toUpperCase());
            agricultor.setText(anexo.getAgricultor().getNombre_agricultor().toUpperCase());
            potrero.setText(anexo.getLotes().getNombre_lote().toUpperCase());
            variedad.setText(anexo.getVariedad().getDesc_variedad().toUpperCase());

            btn_add_visita.setOnClickListener(view -> onClickNuevo.onItemClick(view, anexo));
            btn_menu_visita.setOnClickListener(view -> onClickMenu.onItemClick(view, anexo));

        }
    }


}
