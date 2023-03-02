package cl.smapdev.curimapu.clases.adapters.viewHolders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.VisitasListAdapter;
import cl.smapdev.curimapu.clases.relaciones.VisitasCompletas;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Fotos;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class VisitasListViewHolder extends RecyclerView.ViewHolder {

    private final TextView nombre_agricultor;
    private final TextView nombre_etapa;
    private final TextView nombre_detalle_etapa;
    private final TextView fecha_lista_visitas;
    private final TextView hora_lista_visitas;
    private final Button btn_ver;
    private final Button btn_eliminar;
    private final ConstraintLayout cardview_visitas;

    public VisitasListViewHolder(@NonNull View itemView) {
        super(itemView);

        nombre_agricultor  = itemView.findViewById(R.id.nombre_agricultor);
        nombre_etapa  = itemView.findViewById(R.id.nombre_etapa);
        nombre_detalle_etapa  = itemView.findViewById(R.id.nombre_detalle_etapa);
        fecha_lista_visitas  = itemView.findViewById(R.id.fecha_lista_visitas);
        cardview_visitas  = itemView.findViewById(R.id.cardview_visitas);
        hora_lista_visitas  = itemView.findViewById(R.id.hora_lista_visitas);

        btn_ver  = itemView.findViewById(R.id.btn_Ver);
        btn_eliminar  = itemView.findViewById(R.id.btn_eliminar);

    }


    public void bind(final VisitasCompletas elem, final VisitasListAdapter.OnItemClickListener clickListenerVer, final VisitasListAdapter.OnItemClickListener clickListenerEliminar, Context context){

        if (context != null){

            int configid = 0;
            Config config = MainActivity.myAppDB.myDao().getConfig();
            if (config != null){
                configid = config.getId();
            }

            btn_ver.setOnClickListener(view -> clickListenerVer.onItemClick(view, elem));

            if(elem.getVisitas().getEstado_server_visitas() > 0){
                btn_eliminar.setEnabled(false);
            }else{
                btn_eliminar.setEnabled(true);
                btn_eliminar.setOnClickListener(view -> clickListenerEliminar.onItemClick(view, elem));
            }


            hora_lista_visitas.setText(elem.getVisitas().getHora_visita());
            fecha_lista_visitas.setText(Utilidades.voltearFechaVista(elem.getVisitas().getFecha_visita()));
            nombre_agricultor.setText(elem.getAnexoCompleto().getAgricultor().getNombre_agricultor());
            nombre_etapa.setText(Utilidades.getStateString(elem.getVisitas().getEtapa_visitas()));
            nombre_detalle_etapa.setText(elem.getVisitas().getPhenological_state_visita());

        }

    }


}
