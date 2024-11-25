package cl.smapdev.curimapu.clases.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.tablas.CheckListRoguingFotoCabecera;

public class FotosCheckListRoguingCabAdapter extends RecyclerView.Adapter<FotosCheckListRoguingCabAdapter.ImageViewHolder> {

    private final List<CheckListRoguingFotoCabecera> images;
    private final Context context;
    private final OnItemClickListener itemClickListener;
    private final OnItemLongClickListener itemLongClickListener;

    public FotosCheckListRoguingCabAdapter(List<CheckListRoguingFotoCabecera> images, Context context, OnItemClickListener itemClickListener, OnItemLongClickListener itemLongClickListener) {
        this.images = images;
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.itemLongClickListener = itemLongClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(CheckListRoguingFotoCabecera fotos);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(CheckListRoguingFotoCabecera fotos);
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fotos_cabecera_roguing, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.bind(images.get(position), context, itemClickListener, itemLongClickListener);
    }


    @Override
    public int getItemCount() {
        if (images != null) {
            return images.size();
        } else {
            return 0;
        }
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.album);
        }

        void bind(final CheckListRoguingFotoCabecera fotos, Context context, OnItemClickListener itemClickListener, OnItemLongClickListener itemLongClickListener) {

            if (context != null) {
                Picasso.get().load("file:///" + fotos.getRuta()).resize(300, 200).centerCrop().into(imageView);

                imageView.setOnClickListener(v -> itemClickListener.onItemClick(fotos));
                imageView.setOnLongClickListener(v -> {
                    itemLongClickListener.onItemLongClick(fotos);
                    return true;
                });
            }
        }
    }
}
