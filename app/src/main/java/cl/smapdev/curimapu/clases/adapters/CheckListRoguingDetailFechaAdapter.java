package cl.smapdev.curimapu.clases.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.relaciones.CheckListRoguingFechaCompleto;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingDetalleFechas;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class CheckListRoguingDetailFechaAdapter extends RecyclerView.Adapter<CheckListRoguingDetailFechaAdapter.FechasViewHolder> {

    private final List<CheckListRoguingFechaCompleto> lista;
    private final Context context;
    private final OnItemClickListener itemClickListener;
    private final OnItemLongClickListener itemLongClickListener;

    public CheckListRoguingDetailFechaAdapter(List<CheckListRoguingFechaCompleto> lista, Context context, OnItemClickListener itemClickListener, OnItemLongClickListener itemLongClickListener) {
        this.lista = lista;
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.itemLongClickListener = itemLongClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(CheckListRoguingDetalleFechas d);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(CheckListRoguingDetalleFechas d);
    }

    @NonNull
    @Override
    public FechasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_fechas_roguing, parent, false);
        return new FechasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FechasViewHolder holder, int position) {


        CheckListRoguingFechaCompleto fechas = lista.get(position);

        holder.tv_fecha.setText(Utilidades.voltearFechaVista(fechas.getFecha().getFecha()));
        holder.tv_est_feno.setText(fechas.getFecha().getEstado_fenologico());


        CheckListRoguingDetailAdapter nested = new CheckListRoguingDetailAdapter(
                fechas.getDetalles(),
                context,
                checkListDetails -> {

                },
                checkListDetails -> {
                }
        );
        LinearLayoutManager lm = new LinearLayoutManager(holder.itemView.getContext());
        holder.rv_listado_detalle.setLayoutManager(lm);
        holder.rv_listado_detalle.setAdapter(nested);

        holder.contenedor_adapter.setOnClickListener(v -> itemClickListener.onItemClick(fechas.getFecha()));
        holder.itemView.setOnLongClickListener(v -> {
            itemLongClickListener.onItemLongClick(fechas.getFecha());
            return true;
        });
    }


    @Override
    public int getItemCount() {
        if (lista != null) {
            return lista.size();
        } else {
            return 0;
        }
    }

    static class FechasViewHolder extends RecyclerView.ViewHolder {

        TextView tv_fecha, tv_est_feno;

        RecyclerView rv_listado_detalle;
        ConstraintLayout contenedor_adapter;

        FechasViewHolder(View itemView) {
            super(itemView);
            contenedor_adapter = itemView.findViewById(R.id.contenedor_adapter);
            tv_fecha = itemView.findViewById(R.id.tv_fecha);
            tv_est_feno = itemView.findViewById(R.id.tv_est_feno);
            rv_listado_detalle = itemView.findViewById(R.id.rv_listado_detalle);
        }
    }
}
