package cl.smapdev.curimapu.clases.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cl.smapdev.curimapu.R;

public class ObsRecAdapter extends RecyclerView.Adapter<ObsRecAdapter.ObsRecViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(String seleccion);
    }

    private List<String> contenido;
    private List<String> contenidoFiltrado;
    private OnItemClickListener itemClickListener;

    public ObsRecAdapter(List<String> contenido, OnItemClickListener itemClickListener) {
        this.contenido = new ArrayList<>(contenido);
        this.contenidoFiltrado = new ArrayList<>(contenido);
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ObsRecViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_observaciones, parent, false);
        return new ObsRecViewHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull ObsRecViewHolder holder, int position) {
        holder.bind(contenidoFiltrado.get(position), itemClickListener);
    }

    @Override
    public int getItemCount() {
        return contenidoFiltrado.size();
    }

    public void filter(String query) {
        List<String> oldList = new ArrayList<>(contenidoFiltrado); // copia de la lista actual
        List<String> newList = new ArrayList<>();

        if (query == null || query.trim().isEmpty()) {
            newList.addAll(contenido);
        } else {
            String lowerQuery = query.toLowerCase();
            for (String item : contenido) {
                if (item.toLowerCase().contains(lowerQuery)) {
                    newList.add(item);
                }
            }
        }

        // Calcula diferencias
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldList.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
            }
        });

        // Reemplaza la lista filtrada y notifica los cambios
        contenidoFiltrado.clear();
        contenidoFiltrado.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    static class ObsRecViewHolder extends RecyclerView.ViewHolder {

        private TextView lbl_obs;
        private Button btn_seleccion;

        ObsRecViewHolder(View itemView) {
            super(itemView);
            lbl_obs = itemView.findViewById(R.id.lbl_obs);
            btn_seleccion = itemView.findViewById(R.id.btn_seleccion);
        }

        void bind(String contenido, OnItemClickListener itemClickListener) {

            lbl_obs.setText(contenido);
            btn_seleccion.setOnClickListener(v -> itemClickListener.onItemClick(contenido));
        }
    }
}
