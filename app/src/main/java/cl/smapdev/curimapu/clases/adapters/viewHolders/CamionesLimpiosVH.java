package cl.smapdev.curimapu.clases.adapters.viewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.CamionesLimpiosAdapter;
import cl.smapdev.curimapu.clases.tablas.ChecklistLimpiezaCamionesDetalle;

public class CamionesLimpiosVH extends RecyclerView.ViewHolder {

    private TextView patente_camion;
    private TextView patente_carro;
    private TextView nombre_chofer;
    private ImageView btn_exand_rv;
    private Button btn_firma;
    private ImageView iv_trash;


    private ConstraintLayout contenedor_detalle_camiones;
    private TextView estado_general_recepcion;
    private TextView equipos_utilizados_limpieza;
    private TextView limpieza_puertas_laterales;
    private TextView limpieza_puertas_traseras;
    private TextView limpieza_piso_realizada;
    private TextView inspeccion_rejillas_mallas_realizada;
    private TextView pisos_costados_bateas_sin_orificio;
    private TextView carpa_limpia_revisada;
    private TextView sistema_cerrado_puertas;
    private TextView nivel_llenado_carga;
    private TextView carga_anterior;
    private TextView sello_color_indica_condicion;
    private TextView etiqueta_cosecha_adherida_camion;
    private TextView camion_carro_limpio_revisado;
    private TextView sello_verde_curimapu_cierre_camion;

    public CamionesLimpiosVH(@NonNull View itemView) {
        super(itemView);

        patente_camion = itemView.findViewById(R.id.patente_camion);
        patente_carro = itemView.findViewById(R.id.patente_carro);
        nombre_chofer = itemView.findViewById(R.id.nombre_chofer);
        btn_exand_rv = itemView.findViewById(R.id.btn_exand_rv);
        btn_firma = itemView.findViewById(R.id.btn_firma);
        iv_trash = itemView.findViewById(R.id.iv_trash);
        camion_carro_limpio_revisado = itemView.findViewById(R.id.camion_carro_limpio_revisado);

        contenedor_detalle_camiones = itemView.findViewById(R.id.contenedor_detalle_camiones);
        estado_general_recepcion = itemView.findViewById(R.id.estado_general_recepcion);
        equipos_utilizados_limpieza = itemView.findViewById(R.id.equipos_utilizados_limpieza);
        limpieza_puertas_laterales = itemView.findViewById(R.id.limpieza_puertas_laterales);
        limpieza_puertas_traseras = itemView.findViewById(R.id.limpieza_puertas_traseras);
        limpieza_piso_realizada = itemView.findViewById(R.id.limpieza_piso_realizada);
        inspeccion_rejillas_mallas_realizada = itemView.findViewById(R.id.inspeccion_rejillas_mallas_realizada);
        pisos_costados_bateas_sin_orificio = itemView.findViewById(R.id.pisos_costados_bateas_sin_orificio);
        carpa_limpia_revisada = itemView.findViewById(R.id.carpa_limpia_revisada);
        sistema_cerrado_puertas = itemView.findViewById(R.id.sistema_cerrado_puertas);
        nivel_llenado_carga = itemView.findViewById(R.id.nivel_llenado_carga);
        carga_anterior = itemView.findViewById(R.id.carga_anterior);
        sello_color_indica_condicion = itemView.findViewById(R.id.sello_color_indica_condicion);
        etiqueta_cosecha_adherida_camion = itemView.findViewById(R.id.etiqueta_cosecha_adherida_camion);
        sello_verde_curimapu_cierre_camion = itemView.findViewById(R.id.sello_verde_curimapu_cierre_camion);


    }

    public void bind(ChecklistLimpiezaCamionesDetalle checklistLimpiezaCamionesDetalle, CamionesLimpiosAdapter.OnClickListener onClickFirma, CamionesLimpiosAdapter.OnClickListener onClickDelete) {

        btn_exand_rv.setOnClickListener(view -> contenedor_detalle_camiones.setVisibility((contenedor_detalle_camiones.getVisibility() == View.VISIBLE )? View.GONE : View.VISIBLE));


        patente_camion.setText(checklistLimpiezaCamionesDetalle.getPatente_camion_limpieza_camiones());
        patente_carro.setText(checklistLimpiezaCamionesDetalle.getPatente_carro_limpieza_camiones());
        nombre_chofer.setText(checklistLimpiezaCamionesDetalle.getNombre_chofer_limpieza_camiones());


        String estadoPuertaLateral = (checklistLimpiezaCamionesDetalle.getLimpieza_puertas_laterales_limpieza_camiones() <= 0)
                ? "SIN SELECCION"
                : (checklistLimpiezaCamionesDetalle.getLimpieza_puertas_laterales_limpieza_camiones() == 1)
                ? "SI"
                : "NO";

        String estadoPuertaTrasera = (checklistLimpiezaCamionesDetalle.getLimpieza_puertas_traseras_limpieza_camiones() <= 0)
                ? "SIN SELECCION"
                : (checklistLimpiezaCamionesDetalle.getLimpieza_puertas_traseras_limpieza_camiones() == 1)
                ? "SI"
                : "NO";

        String estadoLimpiezaPiso = (checklistLimpiezaCamionesDetalle.getLimpieza_piso_limpieza_camiones() <= 0)
                ? "SIN SELECCION"
                : (checklistLimpiezaCamionesDetalle.getLimpieza_piso_limpieza_camiones() == 1)
                ? "SI"
                : "NO";

        String estadoInspeccionRejillas = (checklistLimpiezaCamionesDetalle.getInspeccion_rejillas_mallas_limpieza_camiones() <= 0)
                ? "SIN SELECCION"
                : (checklistLimpiezaCamionesDetalle.getInspeccion_rejillas_mallas_limpieza_camiones() == 1)
                ? "SI"
                : "NO";

        String estadoPisoCostados = (checklistLimpiezaCamionesDetalle.getPisos_costados_batea_sin_orificios_limpieza_camiones() <= 0)
                ? "SIN SELECCION"
                : (checklistLimpiezaCamionesDetalle.getPisos_costados_batea_sin_orificios_limpieza_camiones() == 1)
                ? "SI"
                : "NO";

        String estadoCarpaLimpia = (checklistLimpiezaCamionesDetalle.getCarpa_limpia_limpieza_camiones() <= 0)
                ? "SIN SELECCION"
                : (checklistLimpiezaCamionesDetalle.getCarpa_limpia_limpieza_camiones() == 1)
                ? "SI"
                : "NO";

        String estadoSistemaCerrado = (checklistLimpiezaCamionesDetalle.getSistema_cerrado_puertas_limpieza_camiones() <= 0)
                ? "SIN SELECCION"
                : (checklistLimpiezaCamionesDetalle.getSistema_cerrado_puertas_limpieza_camiones() == 1)
                ? "SI"
                : "NO";

        String estadoSelloColor = (checklistLimpiezaCamionesDetalle.getSello_color_indica_condicion_limpieza_camiones() <= 0)
                ? "SIN SELECCION"
                : (checklistLimpiezaCamionesDetalle.getSello_color_indica_condicion_limpieza_camiones() == 1)
                ? "SI"
                : "NO";

        String estadoEtiquetaCosecha = (checklistLimpiezaCamionesDetalle.getEtiqueta_cosecha_adherida_camion_jumbo_limpieza_camiones() <= 0)
                ? "SIN SELECCION"
                : (checklistLimpiezaCamionesDetalle.getEtiqueta_cosecha_adherida_camion_jumbo_limpieza_camiones() == 1)
                ? "SI"
                : "NO";

        String estadoCamionCarroLimpio = (checklistLimpiezaCamionesDetalle.getCamion_carro_limpio_limpieza_camiones() <= 0)
                ? "SIN SELECCION"
                : (checklistLimpiezaCamionesDetalle.getCamion_carro_limpio_limpieza_camiones() == 1)
                ? "SI"
                : "NO";

        estado_general_recepcion.setText(checklistLimpiezaCamionesDetalle.getEstado_general_recepcion_camion_campo_limpieza_camiones());
        equipos_utilizados_limpieza.setText(checklistLimpiezaCamionesDetalle.getEquipo_utilizado_limpieza_camiones());
        limpieza_puertas_laterales.setText(estadoPuertaLateral);
        limpieza_puertas_traseras.setText(estadoPuertaTrasera);
        limpieza_piso_realizada.setText(estadoLimpiezaPiso);
        inspeccion_rejillas_mallas_realizada.setText(estadoInspeccionRejillas);
        pisos_costados_bateas_sin_orificio.setText(estadoPisoCostados);
        carpa_limpia_revisada.setText(estadoCarpaLimpia);
        sistema_cerrado_puertas.setText(estadoSistemaCerrado);
        nivel_llenado_carga.setText(checklistLimpiezaCamionesDetalle.getNivel_llenado_carga_limpieza_camiones());
        carga_anterior.setText(checklistLimpiezaCamionesDetalle.getLimpieza_anterior_limpieza_camiones());
        sello_color_indica_condicion.setText(estadoSelloColor);
        etiqueta_cosecha_adherida_camion.setText(estadoEtiquetaCosecha);
        sello_verde_curimapu_cierre_camion.setText(checklistLimpiezaCamionesDetalle.getSello_verde_curimapu_cierre_camion_limpieza_camiones());


        camion_carro_limpio_revisado.setText(estadoCamionCarroLimpio);


        btn_firma.setOnClickListener(view -> onClickFirma.onItemClick(checklistLimpiezaCamionesDetalle));
        iv_trash.setOnClickListener(view -> onClickDelete.onItemClick(checklistLimpiezaCamionesDetalle));


    }
}
