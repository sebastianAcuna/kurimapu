package cl.smapdev.curimapu.clases.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.tablas.SitiosNoVisitados;

public class SitiosNoVisitadosAdapter extends RecyclerView.Adapter<SitiosNoVisitadosViewHolder> {

    List<SitiosNoVisitados> list;

    public SitiosNoVisitadosAdapter(List<SitiosNoVisitados> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public SitiosNoVisitadosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_sitios_no_visitados, parent, false);
        return new SitiosNoVisitadosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SitiosNoVisitadosViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class SitiosNoVisitadosViewHolder extends RecyclerView.ViewHolder {

    TextView anexo_nov, especie_nov, lote_nov, dias_nov, agricultor_nov, fecha_ultima_visita, usuario_nov;


    public void bind(SitiosNoVisitados vis) {

        anexo_nov.setText((vis.getNumAnexo() != null) ? vis.getNumAnexo() : "--");
        especie_nov.setText((vis.getNombreEspecie() != null) ? vis.getNombreEspecie() : "--");
        lote_nov.setText((vis.getNombreLote() != null) ? vis.getNombreLote() : "--");
        dias_nov.setText((vis.getDias() != null) ? vis.getDias() : "--");
        agricultor_nov.setText((vis.getNombreAgricultor() != null) ? vis.getNombreAgricultor() : "--");
        fecha_ultima_visita.setText((vis.getFechaUltimaVisita() != null) ? vis.getFechaUltimaVisita() : "Sin fecha.");
        usuario_nov.setText((vis.getNombreUsuario() != null) ? vis.getNombreUsuario() : "--");

    }

    public SitiosNoVisitadosViewHolder(@NonNull View itemView) {
        super(itemView);

        anexo_nov = itemView.findViewById(R.id.anexo_nov);
        agricultor_nov = itemView.findViewById(R.id.agricultor_nov);
        fecha_ultima_visita = itemView.findViewById(R.id.fecha_ultima_visita);
        usuario_nov = itemView.findViewById(R.id.usuario_nov);
        especie_nov = itemView.findViewById(R.id.especie_nov);
        lote_nov = itemView.findViewById(R.id.lote_nov);
        dias_nov = itemView.findViewById(R.id.dias_nov);
    }
}


