package cl.smapdev.curimapu.clases.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;

public class PrimeraPrioridadAdapter extends RecyclerView.Adapter<PrimeraPrioridadViewHolder> {

    private final List<AnexoCompleto> list;
    private final Context context;

    public PrimeraPrioridadAdapter(List<AnexoCompleto> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public PrimeraPrioridadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_primera_prioridad, parent, false);
        return new PrimeraPrioridadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrimeraPrioridadViewHolder holder, int position) {
        holder.bind(list.get(position), context);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}


class PrimeraPrioridadViewHolder extends RecyclerView.ViewHolder {

    TextView anexo_nov, especie_nov, lote_nov, fecha_ultima_visita;


    LinearLayout circle_crecimiento, circle_fito, circle_general, circle_ndvi, circle_mi, circle_maleza, circle_cosecha;

    TextView valor_crecimiento, valor_fito, valor_general, valor_ndvi, valor_mi, valor_maleza, valor_cosecha;


    private final Map<String, Integer> circlesBackground = new HashMap<String, Integer>() {{
        put("orange", R.color.colorWarning);
        put("red", R.color.colorError);
        put("green", R.color.colorGreen);
        put("transparent", R.color.backgroundCircle);

    }};

    private final Map<String, Integer> circlesTextColor = new HashMap<String, Integer>() {{
        put("orange", R.color.colorSurface);
        put("red", R.color.colorSurface);
        put("green", R.color.colorSurface);
        put("transparent", R.color.colorOnBackground);
    }};


    public PrimeraPrioridadViewHolder(@NonNull View itemView) {
        super(itemView);

        anexo_nov = itemView.findViewById(R.id.anexo_nov);
        especie_nov = itemView.findViewById(R.id.especie_nov);
        lote_nov = itemView.findViewById(R.id.lote_nov);
        fecha_ultima_visita = itemView.findViewById(R.id.fecha_ultima_visita);

        circle_crecimiento = itemView.findViewById(R.id.circle_crecimiento);
        circle_fito = itemView.findViewById(R.id.circle_fito);
        circle_general = itemView.findViewById(R.id.circle_general);
        circle_ndvi = itemView.findViewById(R.id.circle_ndvi);
        circle_mi = itemView.findViewById(R.id.circle_mi);
        circle_maleza = itemView.findViewById(R.id.circle_maleza);
        circle_cosecha = itemView.findViewById(R.id.circle_cosecha);

        valor_crecimiento = itemView.findViewById(R.id.valor_crecimiento);
        valor_fito = itemView.findViewById(R.id.valor_fito);
        valor_general = itemView.findViewById(R.id.valor_general);
        valor_ndvi = itemView.findViewById(R.id.valor_ndvi);
        valor_mi = itemView.findViewById(R.id.valor_mi);
        valor_maleza = itemView.findViewById(R.id.valor_maleza);
        valor_cosecha = itemView.findViewById(R.id.valor_cosecha);

    }


    void bind(AnexoCompleto pp, Context context) {

        anexo_nov.setText(pp.getAnexoContrato().getAnexo_contrato());
        especie_nov.setText(pp.getEspecie().getDesc_especie());
        lote_nov.setText(pp.getLotes().getNombre_lote());
        fecha_ultima_visita.setText((pp.getAnexoContrato() != null && pp.getAnexoContrato().getFechaUltimaVisitaPP() != null) ?
                Utilidades.voltearFechaVista(pp.getAnexoContrato().getFechaUltimaVisitaPP()) :
                "Sin fecha.");


        if (pp.getAnexoContrato() != null && pp.getAnexoContrato().getColorFitosanitarioPP() != null) {
            Drawable fitoDrawer = ResourcesCompat.getDrawable(circle_fito.getResources(), R.drawable.circle_bg, context.getTheme());
            if (fitoDrawer != null && circlesBackground.get(pp.getAnexoContrato().getColorFitosanitarioPP()) != null) {
                fitoDrawer.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(circlesBackground.get(pp.getAnexoContrato().getColorFitosanitarioPP()), context.getTheme()), PorterDuff.Mode.MULTIPLY));
                circle_fito.setBackground(fitoDrawer);
                valor_fito.setTextColor(context.getResources().getColor(circlesTextColor.get(pp.getAnexoContrato().getColorFitosanitarioPP())));
            }
        }

        if (pp.getAnexoContrato() != null && pp.getAnexoContrato().getColorCrecimientoPP() != null) {
            Drawable crecDrawer = ResourcesCompat.getDrawable(circle_crecimiento.getResources(), R.drawable.circle_bg, context.getTheme());
            if (crecDrawer != null && circlesBackground.get(pp.getAnexoContrato().getColorCrecimientoPP()) != null) {
                crecDrawer.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(circlesBackground.get(pp.getAnexoContrato().getColorCrecimientoPP()), context.getTheme()), PorterDuff.Mode.MULTIPLY));
                circle_crecimiento.setBackground(crecDrawer);
                valor_crecimiento.setTextColor(context.getResources().getColor(circlesTextColor.get(pp.getAnexoContrato().getColorCrecimientoPP())));
            }
        }

        if (pp.getAnexoContrato() != null && pp.getAnexoContrato().getColorGeneralPP() != null) {
            Drawable genDrawer = ResourcesCompat.getDrawable(circle_general.getResources(), R.drawable.circle_bg, context.getTheme());
            if (genDrawer != null && circlesBackground.get(pp.getAnexoContrato().getColorGeneralPP()) != null) {
                genDrawer.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(circlesBackground.get(pp.getAnexoContrato().getColorGeneralPP()), context.getTheme()), PorterDuff.Mode.MULTIPLY));
                circle_general.setBackground(genDrawer);

                valor_general.setTextColor(context.getResources().getColor(circlesTextColor.get(pp.getAnexoContrato().getColorGeneralPP())));
            }
        }
        if (pp.getAnexoContrato() != null && pp.getAnexoContrato().getColorNdviPP() != null) {
            Drawable ndviDrawer = ResourcesCompat.getDrawable(circle_ndvi.getResources(), R.drawable.circle_bg, context.getTheme());
            if (ndviDrawer != null && circlesBackground.get(pp.getAnexoContrato().getColorNdviPP()) != null) {
                ndviDrawer.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(circlesBackground.get(pp.getAnexoContrato().getColorNdviPP()), context.getTheme()), PorterDuff.Mode.MULTIPLY));
                circle_ndvi.setBackground(ndviDrawer);

                valor_ndvi.setTextColor(context.getResources().getColor(circlesTextColor.get(pp.getAnexoContrato().getColorNdviPP())));
            }
        }

        if (pp.getAnexoContrato() != null && pp.getAnexoContrato().getColorMiPP() != null) {
            Drawable miDrawer = ResourcesCompat.getDrawable(circle_mi.getResources(), R.drawable.circle_bg, context.getTheme());
            if (miDrawer != null && circlesBackground.get(pp.getAnexoContrato().getColorMiPP()) != null) {
                miDrawer.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(circlesBackground.get(pp.getAnexoContrato().getColorMiPP()), context.getTheme()), PorterDuff.Mode.MULTIPLY));
                circle_mi.setBackground(miDrawer);

                valor_mi.setTextColor(context.getResources().getColor(circlesTextColor.get(pp.getAnexoContrato().getColorMiPP())));
            }
        }

        if (pp.getAnexoContrato() != null && pp.getAnexoContrato().getColormalezaPP() != null) {
            Drawable malezaDrawer = ResourcesCompat.getDrawable(circle_maleza.getResources(), R.drawable.circle_bg, context.getTheme());
            if (malezaDrawer != null && circlesBackground.get(pp.getAnexoContrato().getColormalezaPP()) != null) {
                malezaDrawer.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(circlesBackground.get(pp.getAnexoContrato().getColormalezaPP()), context.getTheme()), PorterDuff.Mode.MULTIPLY));
                circle_maleza.setBackground(malezaDrawer);
                valor_maleza.setTextColor(context.getResources().getColor(circlesTextColor.get(pp.getAnexoContrato().getColormalezaPP())));

            }
        }

        if (pp.getAnexoContrato() != null && pp.getAnexoContrato().getColorCosechaPP() != null) {
            Drawable cosechaDrawer = ResourcesCompat.getDrawable(circle_cosecha.getResources(), R.drawable.circle_bg, context.getTheme());
            if (cosechaDrawer != null && circlesBackground.get(pp.getAnexoContrato().getColorCosechaPP()) != null) {
                cosechaDrawer.setColorFilter(new PorterDuffColorFilter(context.getResources().getColor(circlesBackground.get(pp.getAnexoContrato().getColorCosechaPP()), context.getTheme()), PorterDuff.Mode.MULTIPLY));
                circle_cosecha.setBackground(cosechaDrawer);
                valor_cosecha.setTextColor(context.getResources().getColor(circlesTextColor.get(pp.getAnexoContrato().getColorCosechaPP())));
            }
        }


        valor_crecimiento.setText(Utilidades.transformarValorPP(pp.getAnexoContrato().getValorCrecimientoPP()));
        valor_fito.setText(Utilidades.transformarValorPP(pp.getAnexoContrato().getValorFitosanitarioPP()));
        valor_general.setText(Utilidades.transformarValorPP(pp.getAnexoContrato().getValorGeneralPP()));
        valor_ndvi.setText(pp.getAnexoContrato().getValorNdviPP());
        valor_mi.setText(Utilidades.transformarValorPP(pp.getAnexoContrato().getValorMiPP()));
        valor_maleza.setText(Utilidades.transformarValorPP(pp.getAnexoContrato().getValormalezaPP()));
        valor_cosecha.setText(Utilidades.transformarValorPP(pp.getAnexoContrato().getValorCosechaPP()));


    }


}