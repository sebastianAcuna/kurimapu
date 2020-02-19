package cl.smapdev.curimapu.clases.utilidades;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AlertDialog;
import androidx.exifinterface.media.ExifInterface;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

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
    public static final String FRAGMENT_CREA_FICHA = "fragment_crea_ficha";
    public static final String FRAGMENT_LIST_VISITS = "fragment_list_visits";




    public static final String SHARED_NAME = "preference_app";
    public static final String SHARED_USER = "user_name";
    public static final String SHARED_PREFERENCE = "frg_preference";
    public static final String SELECTED_ANO = "selected_ano";

    /*DIALOGO FILTRO FICHAS*/
    public static final String SHARED_FILTER_FICHAS_REGION = "filter_fichas_region";
    public static final String SHARED_FILTER_FICHAS_COMUNA = "filter_fichas_comuna";
    public static final String SHARED_FILTER_FICHAS_RADIO = "filter_fichas_radio";
    public static final String SHARED_FILTER_FICHAS_YEAR = "filter_fichas_anno";
    public static final String SHARED_FILTER_FICHAS_HA = "filter_fichas_ha";
    public static final String SHARED_FILTER_FICHAS_NOMB_AG = "filter_fichas_nom_ag";
    public static final String SHARED_FILTER_FICHAS_OF_NEG = "filter_fichas_of_neg";


    public static final String SHARED_FILTER_VISITAS_YEAR = "filter_visitas_anno";
    public static final String SHARED_FILTER_VISITAS_ESPECIE = "filter_visitas_especie";
    public static final String SHARED_FILTER_VISITAS_VARIEDAD = "filter_visitas_variedad";


    public static final String SHARED_VISIT_FICHA_ID = "shared_visit_ficha_id";
    public static final String SHARED_VISIT_ANEXO_ID = "shared_visit_anexo_id";




    public static final String DIRECTORIO_IMAGEN = "curimapu_imagenes";
    public static final String VISTA_FOTOS = "vista";




    public static int getPhenoState(int position){

        int res;

        switch (position){
            case 0:
            default:
                res  = 0;
                break;
            case 1:
            case 2:
            case 3:
                res = 2;
                break;
            case 4:
            case 5:
            case 6:
                res = 3;
                break;
            case 7:
            case 8:
                res = 4;
                break;
            case 9:
                res = 5;
                break;

        }


        return res;
    }
    public static String getStateString(int position){

        String estado;

        switch (position){
            case 0:
            default:
                estado = "All";
                break;
            case 1:
                estado = "Resumen";
                break;
            case 2:
                estado = "Sowing";
                break;
            case 3:
                estado = "Flowering";
                break;
            case 4:
                estado = "Harvest";
                break;
            case 5:
                estado = "Unspecified";
            break;
        }

        return estado;
    }
    public static String getStateDetailString(int position){

        String estado;

        switch (position){
            case 0:
            default:
                estado = "none";
                break;
            case 1:
                estado = "Sowing";
                break;
            case 2:
                estado = "Transplant";
                break;
            case 3:
                estado = "Vegetative";
                break;
            case 4:
                estado = "pre-flowering";
                break;
            case 5:
                estado = "flowering";
                break;
            case 6:
                estado = "Fill";
                break;
            case 7:
                estado = "Pre-Harvest";
                break;
            case 8:
                estado = "Post-harvest";
                break;
            case 9:
                estado = "Unspecified";
                break;
        }

        return estado;
    }



    public static int obtenerAnchoPixelesTexto(String texto){
        Paint p = new Paint();
        Rect bounds = new Rect();
        p.setTextSize(50);

        p.getTextBounds(texto, 0, texto.length(), bounds);
        return bounds.width();
    }



    public static String voltearFechaBD(String fecha){
        if (fecha != null && fecha.length() > 0) {
            String[] date = fecha.split("-");
            return date[2] + "-" + date[1] + "-" + date[0];
        }else{
            return "";
        }
    }

    public static String voltearFechaVista(String fecha){
        if (fecha != null && fecha.length() > 0){
            String[] date = fecha.split("-");
            return date[2]+"-"+date[1]+"-"+date[0];
        }else{
            return "";
        }


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


    static public String formatear(String rut){
        int cont=0;
        String format;
        if(rut.length() == 0){
            return "";
        }else{
            rut = rut.replace(".", "");
            rut = rut.replace("-", "");
            format = "-"+rut.substring(rut.length()-1);
            for(int i = rut.length()-2;i>=0;i--){
                format = rut.substring(i, i+1) + format;
                cont++;
                if(cont == 3 && i != 0){
                    format = "."+format;
                    cont = 0;
                }
            }
            return format;
        }
    }
    public static boolean validarRut(String rut) {

        boolean validacion = false;
        try {
            rut =  rut.toUpperCase();
            rut = rut.replace(".", "");
            rut = rut.replace("-", "");
            int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

            char dv = rut.charAt(rut.length() - 1);

            int m = 0, s = 1;
            for (; rutAux != 0; rutAux /= 10) {
                s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
            }
            if (dv == (char) (s != 0 ? s + 47 : 75)) {
                validacion = true;
            }

        } catch (Exception e) {
        }
        return validacion;
    }

    public static Object[] appendValue(Object[] obj, Object newObj) {

        ArrayList<Object> temp = new ArrayList<Object>(Arrays.asList(obj));
        temp.add(newObj);
        return temp.toArray();

    }
}
