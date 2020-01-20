package cl.smapdev.curimapu.clases.utilidades;

import android.graphics.Paint;
import android.graphics.Rect;

public class Utilidades {



    public static final String FRAGMENT_INICIO = "fragment_inicio";
    public static final String FRAGMENT_FICHAS = "fragment_fichas";
    public static final String FRAGMENT_VISITAS = "fragment_visitas";
    public static final String FRAGMENT_CONTRATOS = "fragment_contratos";





    public static int obtenerAnchoPixelesTexto(String texto)
    {
        Paint p = new Paint();
        Rect bounds = new Rect();
        p.setTextSize(50);

        p.getTextBounds(texto, 0, texto.length(), bounds);
        return bounds.width();
    }

}
