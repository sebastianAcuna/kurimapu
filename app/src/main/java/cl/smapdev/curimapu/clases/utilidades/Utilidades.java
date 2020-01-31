package cl.smapdev.curimapu.clases.utilidades;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.exifinterface.media.ExifInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utilidades {



    public static final String FRAGMENT_INICIO = "fragment_inicio";
    public static final String FRAGMENT_FICHAS = "fragment_fichas";
    public static final String FRAGMENT_VISITAS = "fragment_visitas";
    public static final String FRAGMENT_CONTRATOS = "fragment_contratos";
    public static final String FRAGMENT_LOGIN = "fragment_login";
    public static final String FRAGMENT_CONFIG = "fragment_config";
    public static final String FRAGMENT_FOTOS = "fragment_fotos";
    public static final String FRAGMENT_FOTOS_RESUMEN = "fragment_fotos_resumen";




    public static final String SHARED_NAME = "preference_app";
    public static final String SHARED_USER = "user_name";
    public static final String SHARED_PREFERENCE = "frg_preference";




    public static final String DIRECTORIO_IMAGEN = "curimapu_imagenes";



    public static int obtenerAnchoPixelesTexto(String texto)
    {
        Paint p = new Paint();
        Rect bounds = new Rect();
        p.setTextSize(50);

        p.getTextBounds(texto, 0, texto.length(), bounds);
        return bounds.width();
    }


    public static String fechaActualConHora(){

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return df.format(new Date());
    }


    public static String fechaActualSinHora(){

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return df.format(new Date());
    }

    public static String fechaActualInvSinHora(){
        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }


    public static int neededRotation(File ff)
    {
        try
        {

            ExifInterface exif = new ExifInterface(ff.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
            { return 270; }
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
            { return 180; }
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
            { return 90; }
            return 0;

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

}
