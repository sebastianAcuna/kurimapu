package cl.smapdev.curimapu.clases.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.viewHolders.CropRotationViewHolder;
import cl.smapdev.curimapu.clases.tablas.CropRotation;

public class CropRotationAdapter  extends RecyclerView.Adapter<CropRotationViewHolder> {

    private final List<CropRotation> elements;
    private final Context context;


    public CropRotationAdapter(List<CropRotation> elements, Context context) {
        this.elements = elements;
        this.context = context;
    }

    @NonNull
    @Override
    public CropRotationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_crop_rotation, parent, false);
        return new CropRotationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CropRotationViewHolder holder, int position) {
        holder.bind(elements.get(position), context);
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }
}
