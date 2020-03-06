package cl.smapdev.curimapu.clases.adapters.viewHolders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.VisitasListAdapter;
import cl.smapdev.curimapu.clases.relaciones.VisitasCompletas;
import cl.smapdev.curimapu.clases.tablas.Fotos;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class VisitasListViewHolder extends RecyclerView.ViewHolder {

    private TextView nombre_agricultor, nombre_etapa, nombre_detalle_etapa,fecha_lista_visitas,hora_lista_visitas;
    private ImageView imagen_referencial;
    private CardView cardview_visitas;

    public VisitasListViewHolder(@NonNull View itemView) {
        super(itemView);

        imagen_referencial  = itemView.findViewById(R.id.imagen_referencial);
        nombre_agricultor  = itemView.findViewById(R.id.nombre_agricultor);
        nombre_etapa  = itemView.findViewById(R.id.nombre_etapa);
        nombre_detalle_etapa  = itemView.findViewById(R.id.nombre_detalle_etapa);
        fecha_lista_visitas  = itemView.findViewById(R.id.fecha_lista_visitas);
        cardview_visitas  = itemView.findViewById(R.id.cardview_visitas);
        hora_lista_visitas  = itemView.findViewById(R.id.hora_lista_visitas);

    }


    public void bind(final VisitasCompletas elem, final VisitasListAdapter.OnItemClickListener clickListener, final VisitasListAdapter.OnItemLongClickListener longClickListener, Context context){

        if (context != null){


            final Fotos fotos = MainActivity.myAppDB.myDao().getFoto(elem.getVisitas().getId_anexo_visita(), elem.getVisitas().getId_visita());

            if (fotos != null){
                Picasso.get().load("file:///"+fotos.getRuta()).resize(800,600).centerCrop().into(imagen_referencial);

                imagen_referencial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickListener.onItemClick(view, elem, fotos);
                    }
                });


            }

            cardview_visitas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view, elem, null);
                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longClickListener.onItemLongClick(view, elem, null);
                    return false;
                }
            });


            hora_lista_visitas.setText(elem.getVisitas().getHora_visita());
            fecha_lista_visitas.setText(Utilidades.voltearFechaVista(elem.getVisitas().getFecha_visita()));
            nombre_agricultor.setText(elem.getAnexoCompleto().getAgricultor().getNombre_agricultor());
            nombre_etapa.setText(Utilidades.getStateString(elem.getVisitas().getEtapa_visitas()));
            nombre_detalle_etapa.setText(elem.getVisitas().getPhenological_state_visita());

        }

    }


}
