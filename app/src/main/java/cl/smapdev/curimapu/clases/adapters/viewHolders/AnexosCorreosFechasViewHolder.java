package cl.smapdev.curimapu.clases.adapters.viewHolders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.AnexosAdapter;
import cl.smapdev.curimapu.clases.adapters.AnexosFechasAdapter;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.relaciones.AnexoWithDates;
import cl.smapdev.curimapu.clases.tablas.AnexoCorreoFechas;
import cl.smapdev.curimapu.clases.tablas.Comuna;

public class AnexosCorreosFechasViewHolder extends RecyclerView.ViewHolder {

    private final TextView num_anexo;
    private final TextView especie;
    private final TextView agricultor;
    private final TextView potrero;
    private final TextView comuna;

    private final ImageView corr_inicio_despano;
    private final ImageView corr_5_floracion;
    private final ImageView corr_inicio_corte_seda;
    private final ImageView corr_inicio_cosecha;
    private final ImageView corr_termino_cosecha;
    private final ImageView corr_termino_labores;

    private final ConstraintLayout contenedor_resumen_correo;




    private final TextView condicion;

    private final TextView tv_inicio_despano;
    private final ImageView iv_inicio_despano;
    private final TextView tv_5_floracion;
    private final ImageView iv_5_floracion;
    private final TextView tv_inicio_corte_seda;
    private final ImageView iv_inicio_corte_seda;
    private final TextView tv_inicio_cosecha;
    private final ImageView iv_inicio_cosecha;
    private final TextView tv_termino_cosecha;
    private final ImageView iv_termino_cosecha;
    private final TextView tv_termino_labores;
    private final ImageView iv_termino_labores;
    private final TextView tv_detalle_labores;
    private final ConstraintLayout contenedor_detalle_correo;

    private final Button btn_add_visita;
    private final ImageView ver_detalle;

    private View itemView1;


    public AnexosCorreosFechasViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView1 = itemView;
        num_anexo  = itemView.findViewById(R.id.num_anexo);
        especie  = itemView.findViewById(R.id.especie);
        agricultor  = itemView.findViewById(R.id.agricultor);
        potrero  = itemView.findViewById(R.id.potrero);
        comuna  = itemView.findViewById(R.id.comuna);


        btn_add_visita  = itemView.findViewById(R.id.btn_add_visita);
        ver_detalle  = itemView.findViewById(R.id.ver_detalle);

        condicion = itemView.findViewById(R.id.condicion);

        corr_inicio_despano = itemView.findViewById(R.id.corr_inicio_despano);
        corr_5_floracion = itemView.findViewById(R.id.corr_5_floracion);
        corr_inicio_corte_seda = itemView.findViewById(R.id.corr_inicio_corte_seda);
        corr_inicio_cosecha = itemView.findViewById(R.id.corr_inicio_cosecha);
        corr_termino_cosecha = itemView.findViewById(R.id.corr_termino_cosecha);
        corr_termino_labores = itemView.findViewById(R.id.corr_termino_labores);
        contenedor_resumen_correo = itemView.findViewById(R.id.contenedor_resumen_correo);
        tv_inicio_despano = itemView.findViewById(R.id.tv_inicio_despano);
        iv_inicio_despano = itemView.findViewById(R.id.iv_inicio_despano);
        tv_5_floracion = itemView.findViewById(R.id.tv_5_floracion);
        iv_5_floracion = itemView.findViewById(R.id.iv_5_floracion);
        tv_inicio_corte_seda = itemView.findViewById(R.id.tv_inicio_corte_seda);
        iv_inicio_corte_seda = itemView.findViewById(R.id.iv_inicio_corte_seda);
        tv_inicio_cosecha = itemView.findViewById(R.id.tv_inicio_cosecha);
        iv_inicio_cosecha = itemView.findViewById(R.id.iv_inicio_cosecha);
        tv_termino_cosecha = itemView.findViewById(R.id.tv_termino_cosecha);
        iv_termino_cosecha = itemView.findViewById(R.id.iv_termino_cosecha);
        tv_termino_labores = itemView.findViewById(R.id.tv_termino_labores);
        iv_termino_labores = itemView.findViewById(R.id.iv_termino_labores);
        tv_detalle_labores = itemView.findViewById(R.id.tv_detalle_labores);
        contenedor_detalle_correo = itemView.findViewById(R.id.contenedor_detalle_correo);

    }


    public void bind (final AnexoWithDates anexo, AnexosFechasAdapter.OnItemClickListener onClickCambiar, Context context){




            AnexoCompleto anexos = anexo.getAnexoCompleto();
            AnexoCorreoFechas anexoCorreos = anexo.getAnexoCorreoFichas();
            Comuna comunas = anexo.getComuna();



            num_anexo.setText(anexos.getAnexoContrato().getAnexo_contrato().toUpperCase());
            especie.setText(anexos.getEspecie().getDesc_especie().toUpperCase());
            agricultor.setText(anexos.getAgricultor().getNombre_agricultor().toUpperCase());
            potrero.setText(anexos.getLotes().getNombre_lote().toUpperCase());
            comuna.setText(comunas.getDesc_comuna().toUpperCase());

            condicion.setText(anexos.getAnexoContrato().getCondicion());


            Drawable correoSi = itemView1.getContext().getDrawable(R.drawable.ic_baseline_email_24_si);
            correoSi.setTint(itemView1.getContext().getColor(R.color.colorGreen));
            Drawable correoNo = itemView1.getContext().getDrawable(R.drawable.ic_baseline_email_24_no);



            if(anexoCorreos != null){

                String inicioDespano = (anexoCorreos.getInicio_despano().equals("0000-00-00") ? "" : anexoCorreos.getInicio_despano());
                String cincoPorciento = (anexoCorreos.getCinco_porciento_floracion().equals("0000-00-00") ? "" : anexoCorreos.getCinco_porciento_floracion());
                String inicioCorteSeda = (anexoCorreos.getInicio_corte_seda().equals("0000-00-00") ? "" : anexoCorreos.getInicio_corte_seda());
                String inicioCosecha = (anexoCorreos.getInicio_cosecha().equals("0000-00-00") ? "" : anexoCorreos.getInicio_cosecha());
                String terminoCosecha = (anexoCorreos.getTermino_cosecha().equals("0000-00-00") ? "" : anexoCorreos.getTermino_cosecha());
                String terminoLabores = (anexoCorreos.getTermino_labores_post_cosechas().equals("0000-00-00") ? "" : anexoCorreos.getTermino_labores_post_cosechas());
                String detalleTerminoLabores = anexoCorreos.getDetalle_labores();

                String horaInicioCosecha = (anexoCorreos.getHora_inicio_cosecha() == null) ? "" : anexoCorreos.getHora_inicio_cosecha();

                corr_inicio_despano.setImageDrawable((anexoCorreos.getCorreo_inicio_despano() > 0) ? correoSi : correoNo);
                corr_5_floracion.setImageDrawable((anexoCorreos.getCorreo_cinco_porciento_floracion() > 0) ? correoSi: correoNo );
                corr_inicio_corte_seda.setImageDrawable((anexoCorreos.getCorreo_inicio_corte_seda() > 0) ? correoSi : correoNo );
                corr_inicio_cosecha.setImageDrawable((anexoCorreos.getCorreo_inicio_cosecha() > 0) ? correoSi: correoNo );
                corr_termino_cosecha.setImageDrawable((anexoCorreos.getCorreo_termino_cosecha() > 0) ? correoSi : correoNo );
                corr_termino_labores.setImageDrawable((anexoCorreos.getCorreo_termino_labores_post_cosechas() > 0) ? correoSi : correoNo );


                iv_inicio_despano.setImageDrawable((anexoCorreos.getCorreo_inicio_despano() > 0) ? correoSi : correoNo);
                iv_5_floracion.setImageDrawable((anexoCorreos.getCorreo_cinco_porciento_floracion() > 0) ? correoSi: correoNo );
                iv_inicio_corte_seda.setImageDrawable((anexoCorreos.getCorreo_inicio_corte_seda() > 0) ? correoSi : correoNo );
                iv_inicio_cosecha.setImageDrawable((anexoCorreos.getCorreo_inicio_cosecha() > 0) ? correoSi: correoNo );
                iv_termino_cosecha.setImageDrawable((anexoCorreos.getCorreo_termino_cosecha() > 0) ? correoSi : correoNo );
                iv_termino_labores.setImageDrawable((anexoCorreos.getCorreo_termino_labores_post_cosechas() > 0) ? correoSi : correoNo );



                tv_inicio_despano.setText(inicioDespano);
                tv_5_floracion.setText(cincoPorciento);
                tv_inicio_corte_seda.setText(inicioCorteSeda);
                tv_inicio_cosecha.setText(inicioCosecha+" "+horaInicioCosecha);
                tv_termino_cosecha.setText(terminoCosecha);
                tv_termino_labores.setText(terminoLabores);
                tv_detalle_labores.setText(detalleTerminoLabores);

            }


            btn_add_visita.setOnClickListener(view -> onClickCambiar.onItemClick(view, anexo));


            ver_detalle.setOnClickListener(view -> {
                contenedor_detalle_correo.setVisibility(contenedor_detalle_correo.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                contenedor_resumen_correo.setVisibility(contenedor_detalle_correo.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);

                ver_detalle.setImageDrawable(contenedor_detalle_correo.getVisibility() == View.GONE
                        ? itemView1.getContext().getDrawable(R.drawable.ic_expand_down)
                        : itemView1.getContext().getDrawable(R.drawable.ic_expand_up) );
            });

    }


}
