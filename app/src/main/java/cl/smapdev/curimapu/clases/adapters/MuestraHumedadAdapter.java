package cl.smapdev.curimapu.clases.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.viewHolders.MuestraHumedadViewHolder;
import cl.smapdev.curimapu.clases.tablas.MuestraHumedad;

public class MuestraHumedadAdapter extends RecyclerView.Adapter<MuestraHumedadViewHolder> {

    private final List<MuestraHumedad> muestraHumedadCompletoList;
    private final OnItemClickListener itemClickListenerEliminar;
    private final OnItemClickListener itemClickListenerEditar;
    private final Context context;

    public interface OnItemClickListener{ void onItemClick(View view, MuestraHumedad estaciones); }


    public MuestraHumedadAdapter(List<MuestraHumedad> muestraHumedadCompletoList,
                                 OnItemClickListener itemClickListenerEliminar,
                                 OnItemClickListener itemClickListenerEditar,
                                 Context context) {
        this.muestraHumedadCompletoList = muestraHumedadCompletoList;
        this.itemClickListenerEliminar = itemClickListenerEliminar;
        this.itemClickListenerEditar = itemClickListenerEditar;
        this.context = context;
    }

    @NonNull
    @Override
    public MuestraHumedadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_muestra_humedad,parent,false);
        return new MuestraHumedadViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MuestraHumedadViewHolder holder, int position) {
        holder.bind(muestraHumedadCompletoList.get(position), itemClickListenerEliminar, itemClickListenerEditar, context);
    }

    @Override
    public int getItemCount() {
        return muestraHumedadCompletoList.size();
    }
}
