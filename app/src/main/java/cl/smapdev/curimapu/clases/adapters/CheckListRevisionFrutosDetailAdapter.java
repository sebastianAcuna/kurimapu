package cl.smapdev.curimapu.clases.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.tablas.CheckListRevisionFrutosDetalle;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class CheckListRevisionFrutosDetailAdapter extends RecyclerView.Adapter<CheckListRevisionFrutosDetailAdapter.ImageViewHolder> {

    private final List<CheckListRevisionFrutosDetalle> detalles;
    private final Context context;
    private final OnItemClickListener itemClickListener;
    private final OnItemLongClickListener itemLongClickListener;

    public CheckListRevisionFrutosDetailAdapter(List<CheckListRevisionFrutosDetalle> detalles, Context context, OnItemClickListener itemClickListener, OnItemLongClickListener itemLongClickListener) {
        this.detalles = detalles;
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.itemLongClickListener = itemLongClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(CheckListRevisionFrutosDetalle d, int conteo);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(CheckListRevisionFrutosDetalle d);
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_detalle_revision_frutos, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        holder.bind(detalles.get(position), position + 1, context, itemClickListener, itemLongClickListener);
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


        TextView tv_conteo, tv_comentario, tv_frutos_aptos, tv_frutos_no_aptos;

        ImageViewHolder(View itemView) {
            super(itemView);
            tv_conteo = itemView.findViewById(R.id.tv_conteo);
            tv_comentario = itemView.findViewById(R.id.tv_comentario);
            tv_frutos_aptos = itemView.findViewById(R.id.tv_frutos_aptos);
            tv_frutos_no_aptos = itemView.findViewById(R.id.tv_frutos_no_aptos);
        }

        void bind(final CheckListRevisionFrutosDetalle d, int contador, Context context, OnItemClickListener itemClickListener, OnItemLongClickListener itemLongClickListener) {

            if (context != null) {

                tv_conteo.setText(String.valueOf(contador));
                tv_comentario.setText(d.getComentario());

                tv_frutos_aptos.setText(Utilidades.myNumberFormat(d.getFrutos_aptos()));
                tv_frutos_no_aptos.setText(Utilidades.myNumberFormat(d.getFrutos_no_aptos()));


                itemView.setOnClickListener(v -> {
                    Log.d("CLICK", "ITEMVIEW");
                    itemClickListener.onItemClick(d, contador);
                });
                itemView.setOnLongClickListener(v -> {
                    Log.d("LONGCLICK", "ITEMVIEW");
                    itemLongClickListener.onItemLongClick(d);
                    return true;
                });
            }
        }
    }
}
