package cl.smapdev.curimapu.clases.adapters.viewHolders;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.AsistentesCapacitacionAdapter;
import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembraDetalle;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class AsistentesCapacitacionVH extends RecyclerView.ViewHolder {


    private final TextView tv_fecha_asistente;
    private final TextView tv_rut_asistente;
    private final TextView tv_area_asistente;
    private final TextView tv_nombre_asistente;
    private final Button btn_firma;
    private final ImageView iv_trash;



    public AsistentesCapacitacionVH(@NonNull View itemView) {
        super(itemView);

        tv_fecha_asistente = itemView.findViewById(R.id.tv_fecha_asistente);
        tv_rut_asistente = itemView.findViewById(R.id.tv_rut_asistente);
        tv_area_asistente = itemView.findViewById(R.id.tv_area_asistente);
        tv_nombre_asistente = itemView.findViewById(R.id.tv_nombre_asistente);
        btn_firma = itemView.findViewById(R.id.btn_firma);
        iv_trash = itemView.findViewById(R.id.iv_trash);

    }

    public void bind(CheckListCapacitacionSiembraDetalle capacitacionDetalle,
                     AsistentesCapacitacionAdapter.OnClickListener onClickFirma,
                     AsistentesCapacitacionAdapter.OnClickListener onClickDelete){


        tv_fecha_asistente.setText(Utilidades.voltearFechaVista(capacitacionDetalle
                .getFecha_cl_cap_siembra_detalle()));

        tv_rut_asistente.setText(capacitacionDetalle.getRut_cl_cap_siembra_detalle());
        tv_area_asistente.setText(capacitacionDetalle.getArea_cl_cap_siembra_detalle());
        tv_nombre_asistente.setText(capacitacionDetalle.getNombre_cl_cap_siembra_detalle());


        if(capacitacionDetalle.getFirma_cl_cap_siembra_detalle() != null && !capacitacionDetalle.getFirma_cl_cap_siembra_detalle().isEmpty()){
            btn_firma.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_white_48dp, 0, 0, 0);
//            btn_firma.setTextColor(itemView.getContext().getColor(R.color.colorGreen));
            btn_firma.setCompoundDrawableTintList(itemView.getContext().getColorStateList(R.color.colorGreen));
        }else{
            btn_firma.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_sign, 0, 0, 0);
        }

        if(capacitacionDetalle.getEstado_sincronizacion_detalle() == 1){
            btn_firma.setEnabled(false);
            iv_trash.setEnabled(false);
            iv_trash.setImageTintList(itemView.getContext().getColorStateList(R.color.colorGrey));
            iv_trash.setBackgroundTintList(itemView.getContext().getColorStateList(R.color.colorGrey));
            iv_trash.setForegroundTintList(itemView.getContext().getColorStateList(R.color.colorGrey));

        }else{
            btn_firma.setOnClickListener(view -> onClickFirma.onItemClick(capacitacionDetalle));
            iv_trash.setOnClickListener(view -> onClickDelete.onItemClick(capacitacionDetalle));
        }




    }
}
