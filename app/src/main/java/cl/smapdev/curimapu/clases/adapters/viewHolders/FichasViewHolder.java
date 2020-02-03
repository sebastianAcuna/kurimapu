package cl.smapdev.curimapu.clases.adapters.viewHolders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.Fichas;
import cl.smapdev.curimapu.clases.adapters.FichasAdapter;

public class FichasViewHolder extends RecyclerView.ViewHolder {

    private TextView tv_region_fichas,tv_comuna_fichas,tv_agricultor_fichas,tv_negocio_fichas;

    public FichasViewHolder(@NonNull View itemView) {
        super(itemView);

        tv_region_fichas = (TextView) itemView.findViewById(R.id.tv_region_fichas);
        tv_comuna_fichas = (TextView) itemView.findViewById(R.id.tv_comuna_fichas);
        tv_agricultor_fichas = (TextView) itemView.findViewById(R.id.tv_agricultor_fichas);
        tv_negocio_fichas = (TextView) itemView.findViewById(R.id.tv_negocio_fichas);

    }


    public void bind(final Fichas fichas, final FichasAdapter.OnItemClickListener itemClickListener, final FichasAdapter.OnItemLongClickListener itemLongClickListener, Context context){

        if (context != null){



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
