package cl.smapdev.curimapu.clases.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.viewHolders.UserViewHolder;
import cl.smapdev.curimapu.clases.tablas.Usuario;

public class UsuariosAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private final List<Usuario> items;
    private final OnItemClickListener listener;
    private final Context context;

    public UsuariosAdapter(List<Usuario> items, OnItemClickListener listener,Context context) {
        this.items = items;
        this.listener = listener;
        this.context = context;
    }

    public interface OnItemClickListener{ void onItemClick(Usuario user);}


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_usuarios,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(items.get(position), listener, context);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
