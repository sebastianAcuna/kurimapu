package cl.smapdev.curimapu.clases.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.viewHolders.CamionesLimpiosVH;
import cl.smapdev.curimapu.clases.tablas.ChecklistLimpiezaCamionesDetalle;

public class CamionesLimpiosAdapter extends RecyclerView.Adapter<CamionesLimpiosVH> {

    public interface OnClickListener{ void onItemClick(ChecklistLimpiezaCamionesDetalle checkList ); };

    private List<ChecklistLimpiezaCamionesDetalle> camionesDetalle;
    private OnClickListener onClickFirma;
    private OnClickListener onClickDelete;


    public CamionesLimpiosAdapter(List<ChecklistLimpiezaCamionesDetalle> camionesDetalle, OnClickListener onClickFirma, OnClickListener onClickDelete) {
        this.camionesDetalle = camionesDetalle;
        this.onClickFirma = onClickFirma;
        this.onClickDelete = onClickDelete;
    }

    @NonNull
    @Override
    public CamionesLimpiosVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_camiones_limpios, parent, false);
        return new CamionesLimpiosVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CamionesLimpiosVH holder, int position) {
        holder.bind(camionesDetalle.get(position),  onClickFirma,  onClickDelete);
    }

    @Override
    public int getItemCount() {
        return camionesDetalle.size();
    }
}
