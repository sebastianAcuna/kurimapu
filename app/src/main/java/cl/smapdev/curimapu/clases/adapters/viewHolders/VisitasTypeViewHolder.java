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
import cl.smapdev.curimapu.clases.adapters.VisitasTypeAdapter;
import cl.smapdev.curimapu.clases.relaciones.CantidadVisitas;

public class VisitasTypeViewHolder extends RecyclerView.ViewHolder {

    private TextView cantidad_type, nombre_field;
    private SharedPreferences preferences;


    public VisitasTypeViewHolder(@NonNull View itemView) {
        super(itemView);
        cantidad_type = itemView.findViewById(R.id.cantidad_type);
        nombre_field = itemView.findViewById(R.id.nombre_field);
    }


    public void bind(final String el, Context context, final VisitasTypeAdapter.OnItemClickListener cLickListener, int idAnexo){

        if (context != null){
            nombre_field.setText(el);


            List<CantidadVisitas> integers = MainActivity.myAppDB.myDao().getCantidadVisitas(idAnexo);

            int etapa = 0;
            if (integers.size() > 0) {
                switch (el) {
                    case "All":
                        cantidad_type.setText(String.valueOf(integers.size()));
                        break;
                    case "Sowing":
                        etapa = 2;
                        break;
                    case "Flowering":
                        etapa = 3;
                        break;
                    case "Harvest":
                        etapa = 4;
                        break;
                    case "Unspecified":
                        etapa = 5;
                        break;
                }


                for (CantidadVisitas cantidadVisitas : integers) {
                    if (cantidadVisitas.getEtapa_visitas() == etapa) {
                        cantidad_type.setText(String.valueOf(cantidadVisitas.getTodos()));
                    }
                }


            }


            final int finalEtapa = etapa;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cLickListener.onItemClick(finalEtapa);
                }
            });






        }
    }
}
