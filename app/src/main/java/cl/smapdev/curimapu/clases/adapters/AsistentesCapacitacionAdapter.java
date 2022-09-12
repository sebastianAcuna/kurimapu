package cl.smapdev.curimapu.clases.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.viewHolders.AsistentesCapacitacionVH;
import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembraDetalle;

public class AsistentesCapacitacionAdapter  extends RecyclerView.Adapter<AsistentesCapacitacionVH> {

    private List<CheckListCapacitacionSiembraDetalle> capacitacionDetalle;
    private OnClickListener onClickFirma;
    private OnClickListener onClickDelete;


    public AsistentesCapacitacionAdapter(
            List<CheckListCapacitacionSiembraDetalle> capacitacionDetalle,
            OnClickListener firma,
            OnClickListener delete
            ) {
        this.capacitacionDetalle = capacitacionDetalle;
        this.onClickFirma = firma;
        this.onClickDelete = delete;
    }

    public interface OnClickListener{ void onItemClick(CheckListCapacitacionSiembraDetalle checkList ); };

    @NonNull
    @Override
    public AsistentesCapacitacionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_checklist_cap_siembra, parent, false);
        return new AsistentesCapacitacionVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AsistentesCapacitacionVH holder, int position) {
        holder.bind(capacitacionDetalle.get(position), onClickFirma, onClickDelete);
    }

    @Override
    public int getItemCount() {
        return capacitacionDetalle.size();
    }
}
