package cl.smapdev.curimapu.clases.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.viewHolders.CardViewHolder;
import cl.smapdev.curimapu.clases.tablas.CardViewsResumen;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewHolder> {

    private final List<CardViewsResumen> elem;

    public CardViewAdapter(List<CardViewsResumen> elem) {
        this.elem = elem;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_cards, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.bind(elem.get(position));
    }

    @Override
    public int getItemCount() {
        return elem.size();
    }
}
