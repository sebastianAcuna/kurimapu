package cl.smapdev.curimapu.clases.adapters.viewHolders;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.EstacionFloracionAdapter;
import cl.smapdev.curimapu.clases.adapters.MuestraHumedadAdapter;
import cl.smapdev.curimapu.clases.relaciones.EstacionFloracionCompleto;
import cl.smapdev.curimapu.clases.tablas.MuestraHumedad;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class MuestraHumedadViewHolder extends RecyclerView.ViewHolder {



    private final TextView tv_fecha_estacion;
    private final TextView tv_estado_sinc;
    private final Button btn_eliminar_estacion;
    private final Button btn_editar_estacion;

    private final TextView tv_resumen_promedio;
    private final TextView tv_estado_documento;


    public MuestraHumedadViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_fecha_estacion  = itemView.findViewById(R.id.tv_fecha_estacion);
        tv_estado_sinc  = itemView.findViewById(R.id.tv_estado_sinc);
        btn_eliminar_estacion  = itemView.findViewById(R.id.btn_eliminar_estacion);
        btn_editar_estacion  = itemView.findViewById(R.id.btn_editar_estacion);
        tv_resumen_promedio  = itemView.findViewById(R.id.tv_resumen_promedio);
        tv_estado_documento  = itemView.findViewById(R.id.tv_estado_documento);

    }


    public void bind (final MuestraHumedad muestra, MuestraHumedadAdapter.OnItemClickListener onClickEliminar, MuestraHumedadAdapter.OnItemClickListener onClickEditar, Context context){

        if( context  != null){
            tv_estado_sinc.setText(muestra.getEstado_sincronizacion_muestrao() == 1 ? "SINCRONIZADO" : "NO SINCRONIZADO" );
            tv_fecha_estacion.setText(Utilidades.voltearFechaVista(muestra.getFecha_muestra()));
            btn_eliminar_estacion.setOnClickListener(view -> onClickEliminar.onItemClick(view, muestra));
            btn_editar_estacion.setOnClickListener(view -> onClickEditar.onItemClick(view, muestra));

            tv_estado_documento.setText(muestra.getEstado_documento_muestra() == 0  ? "Guardado" : "Finalizado");

            tv_resumen_promedio.setText(muestra.muestra + "%");
            if(muestra.getEstado_documento_muestra() == 1){
                btn_editar_estacion.setVisibility(View.GONE);
            }


        }
    }


}
