package cl.smapdev.curimapu.clases.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.tablas.CheckListRecepcionPlantineraDetalle;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class CheckListDetalleRecepcionPlantineraAdapter extends RecyclerView.Adapter<CheckListDetalleRecepcionPlantineraAdapter.ImageViewHolder> {

    private final List<CheckListRecepcionPlantineraDetalle> detalles;
    private final Context context;
    private final OnItemClickListener itemClickFotos;
    private final OnItemClickListener itemClickBorrar;
    private final OnItemClickListener itemClickEditar;

    public CheckListDetalleRecepcionPlantineraAdapter(
            List<CheckListRecepcionPlantineraDetalle> detalles,
            Context context,
            OnItemClickListener itemClickFotos,
            OnItemClickListener itemClickBorrar,
            OnItemClickListener itemClickEditar) {
        this.detalles = detalles;
        this.context = context;
        this.itemClickFotos = itemClickFotos;
        this.itemClickBorrar = itemClickBorrar;
        this.itemClickEditar = itemClickEditar;
    }

    public interface OnItemClickListener {
        void onItemClick(CheckListRecepcionPlantineraDetalle detalle);
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_detalle_recepcion_plantinera, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.bind(detalles.get(position), context, itemClickFotos, itemClickBorrar, itemClickEditar);
    }


    @Override
    public int getItemCount() {
        if (detalles != null) {
            return detalles.size();
        } else {
            return 0;
        }
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_trash;

        TextView tv_fecha_asistente, tv_area_asistente, tv_op, tv_linea;
        Button btn_firma, btn_editar;


        ImageViewHolder(View itemView) {
            super(itemView);
            iv_trash = itemView.findViewById(R.id.iv_trash);
            tv_fecha_asistente = itemView.findViewById(R.id.tv_fecha_asistente);
            tv_area_asistente = itemView.findViewById(R.id.tv_area_asistente);
            tv_op = itemView.findViewById(R.id.tv_op);
            tv_linea = itemView.findViewById(R.id.tv_linea);
            btn_firma = itemView.findViewById(R.id.btn_firma);
            btn_editar = itemView.findViewById(R.id.btn_editar);
        }

        void bind(final CheckListRecepcionPlantineraDetalle detalle,
                  Context context,
                  OnItemClickListener itemClickFotos, OnItemClickListener itemClickBorrar, OnItemClickListener itemClickEditar) {

            if (context != null) {

                btn_editar.setOnClickListener(v -> itemClickEditar.onItemClick(detalle));
                btn_firma.setOnClickListener(v -> itemClickFotos.onItemClick(detalle));
                iv_trash.setOnClickListener(v -> itemClickBorrar.onItemClick(detalle));

                tv_fecha_asistente.setText(Utilidades.voltearFechaVista(detalle.getFecha_recepcion()));
                tv_area_asistente.setText(detalle.getVariedad());
                tv_op.setText(String.valueOf(detalle.getN_orden()));
                tv_linea.setText(detalle.getLinea());
            }
        }
    }
}
