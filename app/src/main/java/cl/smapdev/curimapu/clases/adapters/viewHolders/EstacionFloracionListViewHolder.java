package cl.smapdev.curimapu.clases.adapters.viewHolders;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.AnexosAdapter;
import cl.smapdev.curimapu.clases.adapters.EstacionFloracionAdapter;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.relaciones.EstacionFloracionCompleto;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class EstacionFloracionListViewHolder extends RecyclerView.ViewHolder {



    private final TextView tv_fecha_estacion;
    private final TextView tv_estado_sinc;
    private final Button btn_eliminar_estacion;
    private final Button btn_editar_estacion;

    private final TextView tv_resumen_promedio;


    public EstacionFloracionListViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_fecha_estacion  = itemView.findViewById(R.id.tv_fecha_estacion);
        tv_estado_sinc  = itemView.findViewById(R.id.tv_estado_sinc);
        btn_eliminar_estacion  = itemView.findViewById(R.id.btn_eliminar_estacion);
        btn_editar_estacion  = itemView.findViewById(R.id.btn_editar_estacion);
        tv_resumen_promedio  = itemView.findViewById(R.id.tv_resumen_promedio);

    }


    public void bind (final EstacionFloracionCompleto estaciones, EstacionFloracionAdapter.OnItemClickListener onClickEliminar, EstacionFloracionAdapter.OnItemClickListener onClickEditar, Context context){

        if( context  != null){
            tv_estado_sinc.setText(estaciones.getEstacionFloracion().getEstado_sincronizacion() == 1 ? "SINCRONIZADO" : "NO SINCRONIZADO" );
            tv_fecha_estacion.setText(Utilidades.voltearFechaVista(estaciones.getEstacionFloracion().getFecha()));
            btn_eliminar_estacion.setOnClickListener(view -> onClickEliminar.onItemClick(view, estaciones));
            btn_editar_estacion.setOnClickListener(view -> onClickEditar.onItemClick(view, estaciones));


            String texto = Utilidades.calculaPromediosEstacionFloracion(estaciones.getEstaciones(), estaciones.getEstacionFloracion().getCantidad_machos(), "; ");

            tv_resumen_promedio.setText(texto);
        }
    }


}
