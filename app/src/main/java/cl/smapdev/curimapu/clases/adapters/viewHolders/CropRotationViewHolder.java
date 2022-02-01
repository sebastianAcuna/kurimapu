package cl.smapdev.curimapu.clases.adapters.viewHolders;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.tablas.CropRotation;

public class CropRotationViewHolder extends RecyclerView.ViewHolder {

    TextView ano, cultivo;

    public CropRotationViewHolder(@NonNull View itemView) {
        super(itemView);

        ano = itemView.findViewById(R.id.crop_year);
        cultivo = itemView.findViewById(R.id.crop_cultivo);
    }


    public void bind(CropRotation crop, Context context){

        if (context != null && crop != null){
            ano.setText(String.valueOf(crop.getTemporada_crop_rotation()));
            cultivo.setText(crop.getCultivo_crop_rotation());
        }
    }
}
