package cl.smapdev.curimapu.clases.adapters.viewHolders;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.VisitasAlmacigoAdapter;
import cl.smapdev.curimapu.clases.relaciones.VisitaAlmacigoCompleto;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class VisitasAlmacigoViewHolder extends RecyclerView.ViewHolder {

    private final TextView nombre_parental;
    private final TextView especie;
    private final TextView variedad;
    private final TextView fecha_siembra;
    private final TextView fecha_estimada_despacho;
    private final TextView cliente;
    private final TextView fecha_visita;
    private final Button btn_menu_visita;

    private final TextView est_crecimiento;
    private final TextView est_maleza;
    private final TextView est_fito;
    private final TextView hum_suelo;
    private final TextView est_dureza;
    private final TextView est_general;
    private final TextView dias_cultivo;


    public VisitasAlmacigoViewHolder(@NonNull View itemView) {
        super(itemView);
        especie = itemView.findViewById(R.id.especie);
        nombre_parental = itemView.findViewById(R.id.nombre_parental);
        variedad = itemView.findViewById(R.id.variedad);
        fecha_siembra = itemView.findViewById(R.id.fecha_siembra);
        fecha_estimada_despacho = itemView.findViewById(R.id.fecha_estimada_despacho);

        est_crecimiento = itemView.findViewById(R.id.est_crecimiento);
        est_maleza = itemView.findViewById(R.id.est_maleza);
        est_fito = itemView.findViewById(R.id.est_fito);
        hum_suelo = itemView.findViewById(R.id.hum_suelo);
        est_dureza = itemView.findViewById(R.id.est_dureza);
        est_general = itemView.findViewById(R.id.est_general);
        dias_cultivo = itemView.findViewById(R.id.dias_cultivo);


        cliente = itemView.findViewById(R.id.cliente);
        fecha_visita = itemView.findViewById(R.id.fecha_visita);


        btn_menu_visita = itemView.findViewById(R.id.btn_menu_visita);

    }


    public void bind(final VisitaAlmacigoCompleto opData, VisitasAlmacigoAdapter.OnItemClickListener onClickMenu, Context context) {

        if (context != null) {

            String fechaSiembra = (opData.getOpAlmacigos().getFecha_siembra().isEmpty()) ? "Sin fecha" : Utilidades.voltearFechaVista(opData.getOpAlmacigos().getFecha_siembra());
            String fechaEstimadaDespacho = (opData.getOpAlmacigos().getFecha_estimada_despacho().isEmpty()) ? "Sin fecha" : Utilidades.voltearFechaVista(opData.getOpAlmacigos().getFecha_estimada_despacho());

            especie.setText(opData.getOpAlmacigos().getNombre_especie());
            nombre_parental.setText(opData.getOpAlmacigos().getNombre_parental());
            variedad.setText(opData.getOpAlmacigos().getNombre_variedad());
            fecha_siembra.setText(fechaSiembra);
            fecha_estimada_despacho.setText(fechaEstimadaDespacho);

            est_crecimiento.setText(opData.getVisitasAlmacigos().getEstado_crecimiento());
            est_maleza.setText(opData.getVisitasAlmacigos().getEstado_maleza());
            est_fito.setText(opData.getVisitasAlmacigos().getEstado_fito());
            hum_suelo.setText(opData.getVisitasAlmacigos().getHumedad_suelo());
            est_dureza.setText(opData.getVisitasAlmacigos().getDureza());
            est_general.setText(opData.getVisitasAlmacigos().getEstado_general());

            dias_cultivo.setText(String.valueOf(opData.getVisitasAlmacigos().getDias_cultivo_a_visita()));

            cliente.setText(opData.getOpAlmacigos().getNombre_cliente());


            String fecha = opData.getVisitasAlmacigos().getFecha_hora_crea().split(" ")[0];
            fecha_visita.setText(Utilidades.voltearFechaVista(fecha));
            btn_menu_visita.setOnClickListener(view -> onClickMenu.onItemClick(view, opData));

        }
    }


}
