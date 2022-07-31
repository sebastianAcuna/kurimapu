package cl.smapdev.curimapu.clases.adapters.viewHolders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.FichasAdapter;
import cl.smapdev.curimapu.clases.relaciones.FichasCompletas;

public class FichasViewHolder extends RecyclerView.ViewHolder {

    private TextView tv_region_fichas,tv_comuna_fichas,tv_agricultor_fichas,tv_negocio_fichas;
    private ImageView img_activated;

    public FichasViewHolder(@NonNull View itemView) {
        super(itemView);

        tv_region_fichas = (TextView) itemView.findViewById(R.id.tv_region_fichas);
        tv_comuna_fichas = (TextView) itemView.findViewById(R.id.tv_comuna_fichas);
        tv_agricultor_fichas = (TextView) itemView.findViewById(R.id.tv_agricultor_fichas);
        tv_negocio_fichas = (TextView) itemView.findViewById(R.id.tv_negocio_fichas);
        img_activated = (ImageView) itemView.findViewById(R.id.img_activated);

    }


    public void bind(final FichasCompletas fichas, final FichasAdapter.OnItemClickListener itemClickListener, final FichasAdapter.OnItemLongClickListener itemLongClickListener, Context context){

        if (context != null){

            tv_region_fichas.setText(fichas.getRegion().getDesc_region());
            tv_agricultor_fichas.setText(fichas.getAgricultor().getNombre_agricultor());
            tv_comuna_fichas.setText(fichas.getComuna().getDesc_comuna());
            tv_negocio_fichas.setText(fichas.getFichas().getOferta_negocio());


            switch (fichas.getFichas().getActiva()){
                case 1:
                    img_activated.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_radio_button_unchecked_black_24dp));
                    break;
                case 2:
                case 3:
                    img_activated.setImageDrawable(itemView.getResources().getDrawable(R.drawable.ic_radio_button_checked_black_24dp));
                    break;

            }



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(fichas);
                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemLongClickListener.onItemLongClick(fichas);
                    return true;
                }
            });

        }




    }
}
