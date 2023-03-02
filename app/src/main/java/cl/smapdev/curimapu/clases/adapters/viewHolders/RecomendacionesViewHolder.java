package cl.smapdev.curimapu.clases.adapters.viewHolders;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.RecomendacionesAdapter;
import cl.smapdev.curimapu.clases.tablas.Evaluaciones;

public class RecomendacionesViewHolder extends RecyclerView.ViewHolder {

    private final TextView fecha_hora_mod;
    private final TextView txt_recomendacion;
    private final TextView fecha_plazo;
    private final TextView realizador;
    private final ImageButton btn_aprobar;
    private final ImageButton btn_rechazar;

    public RecomendacionesViewHolder(@NonNull View itemView) {
        super(itemView);

        fecha_hora_mod = itemView.findViewById(R.id.fecha_hora_mod);
        txt_recomendacion = itemView.findViewById(R.id.txt_recomendacion);
        realizador = itemView.findViewById(R.id.realizador);
        fecha_plazo = itemView.findViewById(R.id.fecha_plazo);
        btn_aprobar = itemView.findViewById(R.id.btn_aprobar);
        btn_rechazar = itemView.findViewById(R.id.btn_rechazar);

    }



    public void bind (Evaluaciones evaluaciones,
                      RecomendacionesAdapter.OnItemClickListener onClickAprobar,
                      RecomendacionesAdapter.OnItemClickListener onCLickRechazar,
                      Context context){

        if( context != null ){

            String fechaHoraTx = evaluaciones.getFecha_hora_tx() != null ? evaluaciones.getFecha_hora_tx() : "";
            String fechaHoraMod = evaluaciones.getFecha_hora_mod() != null ? evaluaciones.getFecha_hora_mod() : "";
            String descripcionRecom = evaluaciones.getDescripcion_recom() != null ? evaluaciones.getDescripcion_recom() : "";
            String fechaPlazo = evaluaciones.getFecha_plazo() != null ? evaluaciones.getFecha_plazo() : " Sin fecha plazo";

            String nombreTx =  evaluaciones.getNombre_crea() != null ? evaluaciones.getNombre_crea() : "";
            String nombreMod =  evaluaciones.getNombre_mod() != null ? evaluaciones.getNombre_mod() : "";

            fecha_hora_mod.setText((evaluaciones.getEstado().equals("P")) ?  fechaHoraTx : fechaHoraMod);
            txt_recomendacion.setText(descripcionRecom);
            fecha_plazo.setText(fechaPlazo);
            realizador.setText((evaluaciones.getEstado().equals("P") ? nombreTx : nombreMod));

            if( !evaluaciones.getEstado().equals("P") ){
                btn_aprobar.setVisibility(View.INVISIBLE);
                btn_rechazar.setVisibility(View.INVISIBLE);
            }

            btn_aprobar.setOnClickListener(view -> onClickAprobar.onItemClick(view, evaluaciones));
            btn_rechazar.setOnClickListener(view -> onCLickRechazar.onItemClick(view, evaluaciones));

        }


    }
}
