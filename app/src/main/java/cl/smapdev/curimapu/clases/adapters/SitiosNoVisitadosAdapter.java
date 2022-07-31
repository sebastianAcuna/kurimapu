package cl.smapdev.curimapu.clases.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.relaciones.VisitasCompletas;

public class SitiosNoVisitadosAdapter extends RecyclerView.Adapter<SitiosNoVisitadosViewHolder> {

    ArrayList<VisitasCompletas> list;

    public SitiosNoVisitadosAdapter(ArrayList<VisitasCompletas> list){

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

class SitiosNoVisitadosViewHolder extends RecyclerView.ViewHolder{

    TextView anexo_nov, especie_nov, lote_nov,dias_nov;

    public void bind(VisitasCompletas vis){

        anexo_nov.setText(vis.getAnexoCompleto().getAnexoContrato().getAnexo_contrato());
        especie_nov.setText(vis.getAnexoCompleto().getEspecie().getDesc_especie());
        lote_nov.setText(vis.getAnexoCompleto().getLotes().getNombre_lote());
        dias_nov.setText(vis.getVisitas().getFecha_visita() + " Dias." );
    }

    public SitiosNoVisitadosViewHolder(@NonNull View itemView) {
        super(itemView);

        anexo_nov = (TextView) itemView.findViewById(R.id.anexo_nov);
        especie_nov = (TextView) itemView.findViewById(R.id.especie_nov);
        lote_nov = (TextView) itemView.findViewById(R.id.lote_nov);
        dias_nov = (TextView) itemView.findViewById(R.id.dias_nov);


    }
}


