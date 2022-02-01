package cl.smapdev.curimapu.clases.adapters.viewHolders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.tablas.CardViewsResumen;
import cl.smapdev.curimapu.clases.tablas.CropRotation;

public class CardViewHolder extends RecyclerView.ViewHolder {

    private final TextView titulo;
    private final TextView dato;
    private final ImageView icono;


    public CardViewHolder(@NonNull View itemView) {
        super(itemView);

        titulo = itemView.findViewById(R.id.txt_title);
        dato = itemView.findViewById(R.id.txt_dato);
        icono = itemView.findViewById(R.id.img_icon);

    }

    public void bind(CardViewsResumen crop){

        if (crop != null){

            switch (crop.getNombre()){

                case "Agricultores":
                    icono.setImageResource(R.drawable.ic_hat_cowboy_solid);
                    break;
                case "Contratos":
                    icono.setImageResource(R.drawable.ic_handshake_regular);
                    break;
                case "Quotations":
                    icono.setImageResource(R.drawable.ic_quora_brands);
                    break;
                case "Especies":
                    icono.setImageResource(R.drawable.ic_spa_solid);
                    break;
                case "Hectareas":
                    icono.setImageResource(R.drawable.ic_map_regular);
                    break;
                case "Visitas":
                    icono.setImageResource(R.drawable.ic_search_solid);
                    break;
            }
            titulo.setText(crop.getNombre());
            dato.setText(crop.getTotal());
        }
    }

}
