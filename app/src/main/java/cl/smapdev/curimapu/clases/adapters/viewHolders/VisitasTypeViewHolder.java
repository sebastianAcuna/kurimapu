package cl.smapdev.curimapu.clases.adapters.viewHolders;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.Etapas;
import cl.smapdev.curimapu.clases.adapters.VisitasTypeAdapter;
import cl.smapdev.curimapu.clases.relaciones.CantidadVisitas;
import cl.smapdev.curimapu.clases.tablas.Temporada;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class VisitasTypeViewHolder extends RecyclerView.ViewHolder {

    private TextView cantidad_type, nombre_field;
    private SharedPreferences preferences;


    public VisitasTypeViewHolder(@NonNull View itemView) {
        super(itemView);
        cantidad_type = itemView.findViewById(R.id.cantidad_type);
        nombre_field = itemView.findViewById(R.id.nombre_field);
    }


    public void bind(final Etapas el, Context context, final VisitasTypeAdapter.OnItemClickListener cLickListener, String idAnexo){

        if (context != null){
            nombre_field.setText(el.getDescEtapa());

            preferences = context.getSharedPreferences(Utilidades.SHARED_NAME, 0);

//            String[] annos = context.getResources().getStringArray(R.array.anos_toolbar);
            List<Temporada> annos = MainActivity.myAppDB.myDao().getTemporada();

            String annoSelected = preferences.getString(Utilidades.SELECTED_ANO, annos.get(annos.size() - 1 ).getId_tempo_tempo());
            List<CantidadVisitas> integers = MainActivity.myAppDB.myDao().getCantidadVisitas(idAnexo, annoSelected);

            int cantidadTotal = 0;
            if (integers.size() > 0){
                for (CantidadVisitas c : integers){
                    cantidadTotal+=c.getTodos();
                }
            }



            if (el.isEtapaSelected()){
                cantidad_type.setBackground(context.getDrawable(R.drawable.rounded_dark));
            }


            if (integers.size() > 0) {
                if (el.getNumeroEtapa() == 0) {
                    cantidad_type.setText(String.valueOf(cantidadTotal));
                }


                for (CantidadVisitas cantidadVisitas : integers) {
                    if (cantidadVisitas.getEtapa_visitas() == el.getNumeroEtapa()) {
                        cantidad_type.setText(String.valueOf(cantidadVisitas.getTodos()));
                    }
                }


            }


//            final int finalEtapa = etapa;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cLickListener.onItemClick(el.getNumeroEtapa());
                }
            });






        }
    }
}
