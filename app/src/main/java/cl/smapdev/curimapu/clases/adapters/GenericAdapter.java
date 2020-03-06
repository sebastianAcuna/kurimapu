package cl.smapdev.curimapu.clases.adapters;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.enums.Layout;

import java.util.ArrayList;
import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.viewHolders.GenericViewHolder;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class GenericAdapter extends RecyclerView.Adapter<GenericViewHolder> {

//    private String[] titulos;
    private ArrayList<String> valores;
    private Context context;

    private ArrayList<TextView> tv = new  ArrayList<>();


    public GenericAdapter(/*String[] titulos,*/ ArrayList<String> valores, Context context) {
//        this.titulos = titulos;
        this.valores = valores;
        this.context = context;
    }

    @NonNull
    @Override
    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_generica, parent, false);
        return new GenericViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        holder.bind(valores.get(position), context, tv);
    }

    @Override
    public int getItemCount() {
        return valores.size();
    }
}
