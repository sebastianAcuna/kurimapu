package cl.smapdev.curimapu.clases.adapters.viewHolders;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.relaciones.DetalleCampo;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class GenericViewHolder extends RecyclerView.ViewHolder {


    public GenericViewHolder(@NonNull View itemView) {
        super(itemView);

    }

    public void bind(String detalleCampo, Context context, ArrayList<TextView> tv){


        String[] columnas  = detalleCampo.split("&&");


        /*View oldView = null;
        int item = 0 ;*/
        for (int i = 0; i < columnas.length; i++){

            String tagTitulo = "titulo_" + i;
            String tagValor = "valor_" + i;
            String[] valoresAndColumnas = columnas[i].split("--");

            TextView ttitulo = (TextView) itemView.findViewWithTag(tagTitulo);
            TextView tvalor = (TextView) itemView.findViewWithTag(tagValor);

            if (ttitulo != null ){

                ttitulo.setVisibility(View.VISIBLE);
                ttitulo.setText(valoresAndColumnas[1]);
            }
            if (tvalor != null ){
                tvalor.setVisibility(View.VISIBLE);
                tvalor.setText(valoresAndColumnas[0]);
            }
        }

    }
}
