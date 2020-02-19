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
import cl.smapdev.curimapu.clases.tablas.Fotos;

public class FotosListAdapter extends RecyclerView.Adapter<FotosListAdapter.ImageViewHolder> {

    private List<Fotos> images;
//    private int[] images;
    private OnItemClickListener itemClickListener;
    private OnItemLongClickListener itemLongClickListener;
    private Context context;


    public FotosListAdapter(List<Fotos> images, Context context, OnItemClickListener itemClickListener , OnItemLongClickListener itemLongClickListener){
        this.images = images;
        this.itemClickListener = itemClickListener;
        this.itemLongClickListener = itemLongClickListener;
        this.context = context;
    }


    public interface OnItemClickListener{ void onItemClick(Fotos fotos); }
    public interface OnItemLongClickListener{ void onItemLongClick(Fotos fotos);}

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_fotos,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.bind(images.get(position), itemClickListener, itemLongClickListener,context);
        }


    @Override
    public int getItemCount() {
        if (images != null){
            return images.size();
        }else{
            return 0;
        }
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView imageTitle;
        ImageView imageViewStar;

        ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.album);
            imageTitle = itemView.findViewById(R.id.album_text);
            imageViewStar = itemView.findViewById(R.id.star);
        }

        void bind(final Fotos fotos, final OnItemClickListener itemClickListener, final OnItemLongClickListener itemLongClickListener, Context context){

            if (context != null){


                if (fotos.isFavorita()){
                    imageViewStar.setVisibility(View.VISIBLE);
                }else{
                    imageViewStar.setVisibility(View.INVISIBLE);
                }

//
                Picasso.get().load("file:///"+fotos.getRuta()).resize(800,600).centerCrop().into(imageView);
                imageTitle.setText(fotos.getNombre_foto());
                imageTitle.setEnabled(false);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onItemClick(fotos);
                    }
                });


                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        itemLongClickListener.onItemLongClick(fotos);
                        return true;
                    }
                });

            }




        }
    }
}
