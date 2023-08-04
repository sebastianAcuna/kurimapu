package cl.smapdev.curimapu.clases.adapters.viewHolders;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.EstacionFloracionDetalleAdapter;
import cl.smapdev.curimapu.clases.adapters.EstacionFloracionEstacionesAdapter;
import cl.smapdev.curimapu.clases.relaciones.EstacionesCompletas;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracionDetalle;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracionEstaciones;

public class EstacionFloracionEstacionesListViewHolder extends RecyclerView.ViewHolder {



    private final TextView tv_tipo_dato;
    private final TextView tv_valores_muestras;


    private final Button btn_eliminar_estacion;
    private final Button btn_editar_estacion;


    public EstacionFloracionEstacionesListViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_tipo_dato  = itemView.findViewById(R.id.tv_tipo_dato);
        tv_valores_muestras  = itemView.findViewById(R.id.tv_valores_muestras);
        btn_eliminar_estacion = itemView.findViewById(R.id.btn_eliminar_estacion);
        btn_editar_estacion = itemView.findViewById(R.id.btn_editar_estacion);

    }


    public void bind (final EstacionesCompletas estaciones, int numeroEstacion, EstacionFloracionEstacionesAdapter.OnItemClickListener onClickEliminar, EstacionFloracionEstacionesAdapter.OnItemClickListener onClickEditar, Context context){

        if( context  != null){
            tv_tipo_dato.setText("ESTACION " + numeroEstacion);

            StringBuilder texto = new StringBuilder();
            for (EstacionFloracionDetalle detalle : estaciones.getDetalles()){
                texto.append(detalle.tituloDato).append(":").append(detalle.valor_dato).append(", ");
            }
            tv_valores_muestras.setText(texto);


            btn_eliminar_estacion.setOnClickListener(v -> onClickEliminar.onItemClick(v, estaciones));
            btn_editar_estacion.setOnClickListener(v -> onClickEditar.onItemClick(v, estaciones));
        }
    }


}
