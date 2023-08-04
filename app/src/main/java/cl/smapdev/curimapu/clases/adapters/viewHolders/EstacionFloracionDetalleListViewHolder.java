package cl.smapdev.curimapu.clases.adapters.viewHolders;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.EstacionFloracionDetalleAdapter;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracionDetalle;

public class EstacionFloracionDetalleListViewHolder extends RecyclerView.ViewHolder {



    private final TextView tv_tipo_dato;
    private final TextView tv_valor_dato;


    private final Button btn_editar_estacion;


    public EstacionFloracionDetalleListViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_tipo_dato  = itemView.findViewById(R.id.tv_tipo_dato);
        tv_valor_dato  = itemView.findViewById(R.id.tv_valor_dato);

        btn_editar_estacion = itemView.findViewById(R.id.btn_editar_estacion);

    }


    public void bind (final EstacionFloracionDetalle estaciones, EstacionFloracionDetalleAdapter.OnItemClickListener onClickEliminar, EstacionFloracionDetalleAdapter.OnItemClickListener onClickEditar, Context context){

        if( context  != null){
            tv_tipo_dato.setText(estaciones.getTituloDato());
            tv_valor_dato.setText(estaciones.getValor_dato());
            btn_editar_estacion.setOnClickListener((v) -> onClickEditar.onItemClick(v, estaciones));


        }
    }


}
