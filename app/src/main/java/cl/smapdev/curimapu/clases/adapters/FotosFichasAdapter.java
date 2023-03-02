package cl.smapdev.curimapu.clases.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.tablas.FotosFichas;

public class FotosFichasAdapter extends RecyclerView.Adapter<FotosFichasAdapter.ImageViewFichasHolder> {

    private final List<FotosFichas> images;
    //    private int[] images;
    private final OnItemClickListener itemClickListener;
    private final OnItemLongClickListener itemLongClickListener;
    private final Context context;


    public FotosFichasAdapter(List<FotosFichas> images, Context context, OnItemClickListener itemClickListener , OnItemLongClickListener itemLongClickListener){
        this.images = images;
        this.itemClickListener = itemClickListener;
        this.itemLongClickListener = itemLongClickListener;
        this.context = context;
    }

    public interface OnItemClickListener{ void onItemClick(FotosFichas fotos); }
    public interface OnItemLongClickListener{ void onItemLongClick(FotosFichas fotos);}


    @NonNull
    @Override
    public ImageViewFichasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_fotos,parent,false);
        return new ImageViewFichasHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewFichasHolder holder, int position) {
        holder.bind(images.get(position), itemClickListener, itemLongClickListener, context);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }


    static class ImageViewFichasHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView imageTitle;
        ImageView imageViewStar;

        ImageViewFichasHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.album);
            imageTitle = itemView.findViewById(R.id.album_text);
            imageViewStar = itemView.findViewById(R.id.star);
        }

        void bind(final FotosFichas fotos, final OnItemClickListener itemClickListener, final OnItemLongClickListener itemLongClickListener, Context context){

            if (context != null){

                imageViewStar.setVisibility(View.INVISIBLE);


//
                Picasso.get().load("file:///"+fotos.getRuta_foto_ficha()).resize(300,200).centerCrop().into(imageView);
                imageTitle.setText(fotos.getNombre_foto_ficha());
                imageTitle.setEnabled(false);

                itemView.setOnClickListener(v -> itemClickListener.onItemClick(fotos));


                itemView.setOnLongClickListener(v -> {
                    itemLongClickListener.onItemLongClick(fotos);
                    return true;
                });

            }




        }
    }
}
