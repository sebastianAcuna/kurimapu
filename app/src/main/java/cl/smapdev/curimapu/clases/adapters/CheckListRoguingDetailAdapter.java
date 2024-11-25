package cl.smapdev.curimapu.clases.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingDetalle;

public class CheckListRoguingDetailAdapter extends RecyclerView.Adapter<CheckListRoguingDetailAdapter.ImageViewHolder> {

    private final List<CheckListRoguingDetalle> detalles;
    private final Context context;
    private final OnItemClickListener itemClickListener;
    private final OnItemLongClickListener itemLongClickListener;

    public CheckListRoguingDetailAdapter(List<CheckListRoguingDetalle> detalles, Context context, OnItemClickListener itemClickListener, OnItemLongClickListener itemLongClickListener) {
        this.detalles = detalles;
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.itemLongClickListener = itemLongClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(CheckListRoguingDetalle fotos);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(CheckListRoguingDetalle fotos);
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_fuera_tipo, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.bind(detalles.get(position), context, itemClickListener, itemLongClickListener);
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

        ImageView iv_gender;

        TextView tv_offtype, tv_cantidad;

        ImageViewHolder(View itemView) {
            super(itemView);
            iv_gender = itemView.findViewById(R.id.iv_gender);
            tv_offtype = itemView.findViewById(R.id.tv_offtype);
            tv_cantidad = itemView.findViewById(R.id.tv_cantidad);
        }

        void bind(final CheckListRoguingDetalle d, Context context, OnItemClickListener itemClickListener, OnItemLongClickListener itemLongClickListener) {

            if (context != null) {

                if (d.getGenero().equals("Hembra")) {
                    iv_gender.setImageResource(R.drawable.baseline_female_24);
                } else {
                    iv_gender.setImageResource(R.drawable.mars_solid);
                }

                tv_cantidad.setText(String.valueOf(d.getCantidad()));
                tv_offtype.setText(d.getDescripcion_fuera_tipo());

                itemView.setOnClickListener(v -> itemClickListener.onItemClick(d));
                itemView.setOnLongClickListener(v -> {
                    itemLongClickListener.onItemLongClick(d);
                    return true;
                });
            }
        }
    }
}
