package cl.smapdev.curimapu.clases.utilidades;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


import androidx.appcompat.app.AlertDialog;
import androidx.exifinterface.media.ExifInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import cl.smapdev.curimapu.R;

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
    public static final String VISTA_FOTOS = "vista";



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


    public static String hora(){
        return new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
    }


    public static String fechaActualSinHora(){

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return df.format(new Date());
    }

    public static String fechaActualInvSinHora(){
        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }



    public static Integer[] neededRotation(Uri ff)
    {
        Integer[] inte = new Integer[2];
        if (ff !=null && ff.getPath() != null) {

            try {
                ExifInterface exif = new ExifInterface(ff.getPath());
                int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                inte[0] = exifToDegrees(rotation);
                inte[1] = rotation;

            } catch (IOException e) {
                inte[0] = 0;
                inte[1] = 0;
                e.printStackTrace();
            }
        }else{
            inte[0] = 0;
            inte[1] = 0;
        }

        return inte;
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }



    public static void avisoListo(Activity activity, String title, String message, String textButton) {
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_empty, null);

        final AlertDialog builder = new AlertDialog.Builder(activity)
                .setView(viewInfalted)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(textButton, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create();

        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
}
