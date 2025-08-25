package cl.smapdev.curimapu.clases.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.modelo.FotoVisitaModel;
import cl.smapdev.curimapu.fragments.dialogos.VerFotoDialogFragment;


public class FotosAlmacigosAdapter extends RecyclerView.Adapter<FotosAlmacigosAdapter.FotoViewHolder> {
    public interface OnFavoritaClicked {
        boolean onFavoritaToggle(int position);
    }

    private OnFavoritaClicked favoritaClicked;

    public void setOnFavoritaClicked(OnFavoritaClicked listener) {
        this.favoritaClicked = listener;
    }

    private final List<FotoVisitaModel> fotos;
    private final Context context;


    public FotosAlmacigosAdapter(Context context, List<FotoVisitaModel> fotos) {
        this.context = context;
        this.fotos = fotos;
    }

    @NonNull
    @Override
    public FotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_foto, parent, false);
        return new FotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FotoViewHolder holder, int position) {
        FotoVisitaModel fotoVisita = fotos.get(position);

        // Cargar imagen del archivo
        Bitmap bitmap = BitmapFactory.decodeFile(fotoVisita.getArchivo().getAbsolutePath());
        holder.imgFoto.setImageBitmap(bitmap);

        // Establecer ícono favorito
        holder.btnFavorito.setImageResource(
                fotoVisita.isFavorita() ? R.drawable.ic_start_filled : R.drawable.ic_start_border
        );

        holder.imgFoto.setOnClickListener(v -> {
            VerFotoDialogFragment dialog = VerFotoDialogFragment.newInstance(fotoVisita.getArchivo(), position);

            dialog.setOnEliminarFotoListener(pos -> {
                fotos.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, fotos.size());
            });

            FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
            dialog.show(fm, "ver_foto_dialog");
        });


        holder.btnFavorito.setOnClickListener(v -> {
            if (favoritaClicked != null) {
                boolean sePudo = favoritaClicked.onFavoritaToggle(position);
                if (sePudo) notifyItemChanged(position);
                else
                    Toast.makeText(v.getContext(), "Máximo 4 favoritas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return fotos.size();
    }

    static class FotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFoto;
        ImageButton btnFavorito;

        public FotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFoto = itemView.findViewById(R.id.img_foto);
            btnFavorito = itemView.findViewById(R.id.btn_favorito);
        }
    }
}
