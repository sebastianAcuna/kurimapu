package cl.smapdev.curimapu.clases.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.relaciones.VisitasCompletas;

public class PrimeraPrioridadAdapter extends RecyclerView.Adapter<PrimeraPrioridadViewHolder> {

    private ArrayList<VisitasCompletas> list;
    private Context context;
    public PrimeraPrioridadAdapter(ArrayList<VisitasCompletas> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public PrimeraPrioridadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_primera_prioridad, parent, false);
        return new PrimeraPrioridadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrimeraPrioridadViewHolder holder, int position) {
        holder.bind(list.get(position), context);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}



class PrimeraPrioridadViewHolder extends RecyclerView.ViewHolder{

    TextView anexo_nov, especie_nov, lote_nov;

    ImageView i_grow,i_fito, i_general,i_humidity, i_weed;

    public PrimeraPrioridadViewHolder(@NonNull View itemView) {
        super(itemView);

        anexo_nov = itemView.findViewById(R.id.anexo_nov);
        especie_nov = itemView.findViewById(R.id.especie_nov);
        lote_nov = itemView.findViewById(R.id.lote_nov);
        i_grow = itemView.findViewById(R.id.i_grow);
        i_fito = itemView.findViewById(R.id.i_fito);
        i_general = itemView.findViewById(R.id.i_general);
        i_humidity = itemView.findViewById(R.id.i_humidity);
        i_weed = itemView.findViewById(R.id.i_weed);
    }


    void bind(VisitasCompletas visitasCompletas, Context context){

        anexo_nov.setText(visitasCompletas.getAnexoCompleto().getAnexoContrato().getAnexo_contrato());
        especie_nov.setText(visitasCompletas.getAnexoCompleto().getEspecie().getDesc_especie());
        lote_nov.setText(visitasCompletas.getAnexoCompleto().getAgricultor().getNombre_agricultor());

        if (visitasCompletas.getVisitas().getGrowth_status_visita().equals("REGULAR")){
            i_grow.setColorFilter(context.getResources().getColor(R.color.colorWarning));
        }else if(visitasCompletas.getVisitas().getGrowth_status_visita().equals("MALA") || visitasCompletas.getVisitas().getGrowth_status_visita().equals("BAD") ){
            i_grow.setColorFilter(context.getResources().getColor(R.color.colorError));
        }else{
            i_grow.setColorFilter(context.getResources().getColor(R.color.colorGreen));
        }


        if (visitasCompletas.getVisitas().getPhytosanitary_state_visita().equals("REGULAR")){
            i_fito.setColorFilter(context.getResources().getColor(R.color.colorWarning));
        }else if(visitasCompletas.getVisitas().getPhytosanitary_state_visita().equals("MALA") || visitasCompletas.getVisitas().getPhytosanitary_state_visita().equals("BAD") ){
            i_fito.setColorFilter(context.getResources().getColor(R.color.colorError));
        }else{
            i_fito.setColorFilter(context.getResources().getColor(R.color.colorGreen));
        }


        if (visitasCompletas.getVisitas().getOverall_status_visita().equals("REGULAR")){
            i_general.setColorFilter(context.getResources().getColor(R.color.colorWarning));
        }else if(visitasCompletas.getVisitas().getOverall_status_visita().equals("MALA") || visitasCompletas.getVisitas().getOverall_status_visita().equals("BAD") ){
            i_general.setColorFilter(context.getResources().getColor(R.color.colorError));
        }else{
            i_general.setColorFilter(context.getResources().getColor(R.color.colorGreen));
        }

        if (visitasCompletas.getVisitas().getHumidity_floor_visita().equals("REGULAR")){
            i_humidity.setColorFilter(context.getResources().getColor(R.color.colorWarning));
        }else if(visitasCompletas.getVisitas().getHumidity_floor_visita().equals("MALA") || visitasCompletas.getVisitas().getHumidity_floor_visita().equals("BAD") ){
            i_humidity.setColorFilter(context.getResources().getColor(R.color.colorError));
        }else{
            i_humidity.setColorFilter(context.getResources().getColor(R.color.colorGreen));
        }

        if (visitasCompletas.getVisitas().getWeed_state_visita().equals("REGULAR")){
            i_weed.setColorFilter(context.getResources().getColor(R.color.colorWarning));
        }else if(visitasCompletas.getVisitas().getWeed_state_visita().equals("ALTA") || visitasCompletas.getVisitas().getWeed_state_visita().equals("HIGH") ){
            i_weed.setColorFilter(context.getResources().getColor(R.color.colorError));
        }else{
            i_weed.setColorFilter(context.getResources().getColor(R.color.colorGreen));
        }

    }
}