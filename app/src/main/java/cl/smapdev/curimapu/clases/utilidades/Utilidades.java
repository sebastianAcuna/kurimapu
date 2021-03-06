package cl.smapdev.curimapu.clases.utilidades;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.tablas.pro_cli_mat;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

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
    public static final String FRAGMENT_TAKE_PHOTO = "fragment_take_photo";
    public static final String FRAGMENT_SERVIDOR = "fragment_servidor";



    public static final String IP_PRODUCCION = "www.zcloud02.cl";
//    public static final String IP_DESARROLLO = "www.zcloud16.cl";
    public static final String IP_DESARROLLO = "www.zcloud16.cl";
    public static final String IP_PRUEBAS = "190.13.170.26";




    public static final String SHARED_NAME = "preference_app";
    public static final String SHARED_USER = "user_name";
    public static final String SHARED_PREFERENCE = "frg_preference";
    public static final String SELECTED_ANO = "selected_ano";


    public static final String SHARED_SERVER_ID_USER = "server_user_id";
    public static final String SHARED_SERVER_ID_SERVER = "server_server_id";

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
    public static final String SHARED_VISIT_VISITA_ID = "shared_visit_visita_id";
    public static final String SHARED_VISIT_MATERIAL_ID = "shared_visit_material_id";
    public static final String SHARED_VISIT_ESPECIE_ID = "shared_visit_especie_id";




    public static final String DIRECTORIO_IMAGEN = "curimapu_imagenes";
    public static final String VISTA_FOTOS = "vista";



    public static Date SumaRestarFecha(int sumaresta){

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, sumaresta);
        return c.getTime();

    }


    public static class MyXAxisValueFormatter extends ValueFormatter {

        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value) {
            return mValues[(int) value];
        }
    }


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


    public static String[] getAnoCompleto(int anno){
        String[] str = new String[2];
        str[0] = anno+"-01-01";
        str[1] = anno+"-12-31";


        return str;
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
        try {
            if (fecha != null && fecha.length() > 0) {
                String[] date = fecha.split("-");
                return date[2] + "-" + date[1] + "-" + date[0];
            } else {
                return "";
            }
        }catch (Exception e){
            return "";
        }
    }

    public static String voltearFechaVista(String fecha){
        try{
            if (fecha != null && fecha.length() > 0){
                String[] date = fecha.split("-");
                return date[2]+"-"+date[1]+"-"+date[0];
            }else{
                return "";
            }
        }catch (Exception e){
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

    public static String horaMin(){
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
    }

    public static String fechaFromDate(Date fecha){
        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(fecha);
    }


    public static Date horaFormat(String hora) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return  sdf.parse(hora);
    }


    public static String fechaActualSinHora(){

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return df.format(new Date());
    }

    public static String fechaActualInvSinHora(){
        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }



    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
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

    public static boolean isLandscape(Activity context){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;

        return width >= height;
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    public static String getMD5(final String s) {
        try{
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for(int i = 0; i < messageDigest.length; i++){
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while(h.length() < 2){
                    h = "0" + h;
                }
                hexString.append(h);
            }
            return hexString.toString();
        }catch (NoSuchAlgorithmException e){
            Log.e("MD5", "md5() NoSuchAlgorithmException: " + e.getMessage());
        }
        return "";
    }



    public static String imageToString(String ruta){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap bitmap = BitmapFactory.decodeFile(ruta,null);

        bitmap.compress(Bitmap.CompressFormat.JPEG,40,byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }


}
