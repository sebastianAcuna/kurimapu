package cl.smapdev.curimapu.clases.adapters.viewHolders;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.OPAlmacigoAdapter;
import cl.smapdev.curimapu.clases.tablas.OpAlmacigos;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class OPAlmacigoViewHolder extends RecyclerView.ViewHolder {

    private final TextView nombre_parental;
    private final TextView especie;
    private final TextView variedad;
    private final TextView fecha_siembra;
    private final TextView fecha_estimada_despacho;
    private final TextView op;
    private final TextView cliente;
    private final TextView tipo_lc;
    private final TextView lbl_nom_fantasia;
    private final TextView nom_fantasia;
    private final Button btn_add_visita;
    private final Button btn_menu_visita;


    public OPAlmacigoViewHolder(@NonNull View itemView) {
        super(itemView);
        especie = itemView.findViewById(R.id.especie);
        nombre_parental = itemView.findViewById(R.id.nombre_parental);
        variedad = itemView.findViewById(R.id.variedad);
        fecha_siembra = itemView.findViewById(R.id.fecha_siembra);
        fecha_estimada_despacho = itemView.findViewById(R.id.fecha_estimada_despacho);

        cliente = itemView.findViewById(R.id.cliente);
        tipo_lc = itemView.findViewById(R.id.tipo_lc);
        lbl_nom_fantasia = itemView.findViewById(R.id.lbl_nom_fantasia);
        nom_fantasia = itemView.findViewById(R.id.nom_fantasia);

        op = itemView.findViewById(R.id.op);

        btn_add_visita = itemView.findViewById(R.id.btn_add_visita);
        btn_menu_visita = itemView.findViewById(R.id.btn_menu_visita);

    }


    public void bind(final OpAlmacigos opData, OPAlmacigoAdapter.OnItemClickListener onClickNuevo, OPAlmacigoAdapter.OnItemClickListener onClickMenu, Context context) {

        if (context != null) {

            String fechaSiembra = (opData.getFecha_siembra() == null || opData.getFecha_siembra().isEmpty()) ? "Sin fecha" : Utilidades.voltearFechaVista(opData.getFecha_siembra());
            String fechaEstimadaDespacho = (opData.getFecha_estimada_despacho() == null || opData.getFecha_estimada_despacho().isEmpty()) ? "Sin fecha" : Utilidades.voltearFechaVista(opData.getFecha_estimada_despacho());

            especie.setText(opData.getNombre_especie());
            nombre_parental.setText(opData.getNombre_parental());
            variedad.setText(opData.getNombre_variedad());
            fecha_siembra.setText(fechaSiembra);
            fecha_estimada_despacho.setText(fechaEstimadaDespacho);
            op.setText(opData.getOp());

            cliente.setText(opData.getNombre_cliente());
            tipo_lc.setText(opData.getDescripcion_tipo_raiz());

            nom_fantasia.setText(opData.getNombre_fantasia());
            if (opData.getId_tipo_lc() == 2) {
                lbl_nom_fantasia.setVisibility(View.VISIBLE);
                nom_fantasia.setVisibility(View.VISIBLE);
            } else {
                lbl_nom_fantasia.setVisibility(View.INVISIBLE);
                nom_fantasia.setVisibility(View.INVISIBLE);
            }


            btn_add_visita.setOnClickListener(view -> onClickNuevo.onItemClick(view, opData));
            btn_menu_visita.setOnClickListener(view -> onClickMenu.onItemClick(view, opData));

        }
    }


}
