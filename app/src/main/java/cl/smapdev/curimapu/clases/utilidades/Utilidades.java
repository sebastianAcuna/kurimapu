package cl.smapdev.curimapu.clases.utilidades;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.exifinterface.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.relaciones.EstacionesCompletas;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracionDetalle;

public class Utilidades {

    public static final String APPLICATION_VERSION = "4.4.0005";

    public static final String FRAGMENT_INICIO = "fragmental_inicio";
    public static final String FRAGMENT_FICHAS = "fragment_fichas";
    public static final String FRAGMENT_VISITAS = "fragment_visitas";
    public static final String FRAGMENT_CONTRATOS = "fragment_contratos";
    public static final String FRAGMENT_ESTACION_FLORACION = "FragmentListaEstacionFloracion";
    public static final String FRAGMENT_MUESTRA_HUMEDAD = "FragmentLista_muestra_humedad";
    public static final String FRAGMENT_NUEVA_MUESTRA = "Fragment_nueva_muestra_humedad";
    public static final String FRAGMENT_NUEVA_ESTACION = "Fragment_nueva_estacion_floracion";

    public static final String FRAGMENT_CHECKLIST = "fragment_checklist";
    public static final String FRAGMENT_CHECKLIST_SIEMBRA = "fragment_checklist_siembra";
    public static final String FRAGMENT_CHECKLIST_COSECHA = "fragment_checklist_cosecha";
    public static final String FRAGMENT_CHECKLIST_CAPACITACION_SIEMBRA = "fragment_checklist_capacitacion_siembra";
    public static final String FRAGMENT_CHECKLIST_DEVOLUCION_SEMILLA = "fragment_checklist_devolucion_semilla";
    public static final String FRAGMENT_CHECKLIST_CAPACITACION_COSECHA = "fragment_checklist_capacitacion_cosecha";
    public static final String FRAGMENT_CHECKLIST_LIMPIEZA_CAMIONES = "fragment_checklist_limpieza_camiones";
    public static final String FRAGMENT_LOGIN = "fragment_login";
    public static final String FRAGMENT_CONFIG = "fragment_config";
    public static final String FRAGMENT_FOTOS = "fragment_fotos";
    public static final String FRAGMENT_CREA_FICHA = "fragment_crea_ficha";
    public static final String FRAGMENT_LIST_VISITS = "fragment_list_visits";
    public static final String FRAGMENT_SERVIDOR = "fragment_servidor";
    public static final String FRAGMENT_ANEXO_FICHA = "fragment_anexo_ficha";


    public static final int IDENTIFICADOR_LC_FECHA_COSECHA = 340;
    public static final int IDENTIFICADOR_LC_FECHA_SIEMBRA = 83;
    public static final int IDENTIFICADOR_LC_FECHA_LINEA_HEMBRA = 90;


    public static final String affiliate_id = "vb7jbic553ts";

    public static final int TIPO_DOCUMENTO_CAPACITACION_SIEMBRA = 1;
    public static final int TIPO_DOCUMENTO_CHECKLIST_SIEMBRA = 2;
    public static final int TIPO_DOCUMENTO_CAPACITACION_COSECHA = 3;
    public static final int TIPO_DOCUMENTO_CHECKLIST_COSECHA = 4;
    public static final int TIPO_DOCUMENTO_CHECKLIST_LIMPIEZA_CAMIONES = 5;
    public static final int TIPO_DOCUMENTO_CHECKLIST_DEVOLUCION_SEMILLA = 6;


    public static final String KEY_EXPORT = "9aB4c5D7eF";
    //    public static final String IP_PRODUCCION = "192.168.1.42";
    public static final String IP_PRODUCCION = "curiexport.zcloud.cl";
    //    public static final String IP_PRODUCCION = "curiexport.pruebas-erp.cl";
    public static final String URL_SERVER_API = "https://" + IP_PRODUCCION;
//    public static final String URL_SERVER_API = "http://" + IP_PRODUCCION + "/curimapu";


    public static final String FILTRO_TEMPORADA = "filtro_temporada";

    public static final String IP_DESARROLLO = "www.zcloud16.cl";
    public static final String IP_PRUEBAS = "190.13.170.26";

    public static final String DIRECTORIO_IMAGEN = "curimapu_imagenes";
    public static final String DIRECTORIO_RESPALDO = "curimapu_respaldo_bd";
    public static final String NOMBRE_DATABASE = "curimapu.db";

    public static final String SHARED_NAME = "preference_app";
    public static final String SHARED_USER = "user_name";
    public static final String SELECTED_ANO = "selected_ano";

    public static final String SHARED_SERVER_ID_USER = "server_user_id";
    public static final String SHARED_SERVER_ID_SERVER = "server_server_id";

    /*DIALOGO FILTRO FICHAS*/
    public static final String SHARED_FILTER_FICHAS_COMUNA = "filter_fichas_comuna";
    public static final String SHARED_FILTER_FICHAS_RADIO = "filter_fichas_radio";

    public static final String SHARED_FILTER_VISITAS_YEAR = "filter_visitas_anno";
    public static final String SHARED_FILTER_VISITAS_ESPECIE = "filter_visitas_especie";
    public static final String SHARED_FILTER_VISITAS_VARIEDAD = "filter_visitas_variedad";

    public static final String SHARED_ETAPA_SELECTED = "shared_etapas_selected";

    public static final String SHARED_VISIT_FICHA_ID = "shared_visit_ficha_id";
    public static final String SHARED_VISIT_ANEXO_ID = "shared_visit_anexo_id";
    public static final String SHARED_VISIT_VISITA_ID = "shared_visit_visita_id";
    public static final String SHARED_VISIT_MATERIAL_ID = "shared_visit_material_id";
    public static final String SHARED_VISIT_TEMPORADA = "shared_visit_temporada";


    public static final String DIALOG_TAG_MUESTRA_ESTCION = "DIALOG_TAG_MUESTRA_ESTCION";
    public static final String DIALOG_TAG_ESTACIONES = "DIALOG_TAG_ESTACIONES";


    public static final String DIALOG_TAG_CAPACITACION_SIEMBRA = "CAPACITACION_SIEMBRA";
    public static final String DIALOG_TAG_CAPACITACION_COSECHA = "CAPACITACION_COSECHA";
    public static final String DIALOG_TAG_LIMPIEZA_CAMIONES = "LIPIEZA_CAMIONES";
    public static final String DIALOG_TAG_FIRMA_CAPACITACION_SIEMBRA = "CAPACITACION_FIRMA__SIEMBRA";
    public static final String DIALOG_TAG_FIRMA_LIMPIEZA_CAMIONES = "DIALOG_TAG_FIRMA_LIMPIEZA_CAMIONES";
    public static final String DIALOG_TAG_FIRMA_CAPACITACION_COSECHA = "CAPACITACION_FIRMA__COSECHA";
    public static final String DIALOG_TAG_RESPONSABLE_ASEO_INGRESO = "RESPONSABLE_ASEO_INGRESO";
    public static final String DIALOG_TAG_REVISOR_LIMPIEZA_INGRESO = "REVISOR_LIMPIEZA_INGRESO";
    public static final String DIALOG_TAG_RESPONSABLE_ASEO_SALIDA = "RESPONSABLE_ASEO_SALIDA";
    public static final String DIALOG_TAG_REVISOR_LIMPIEZA_SALIDA = "REVISOR_LIMPIEZA_SALIDA";
    public static final String DIALOG_TAG_RESPONSABLE_CAMPO_INGRESO = "RESPONSABLE_CAMPO_INGRESO";
    public static final String DIALOG_TAG_RESPONSABLE_OPERARIO_INGRESO = "RESPONSABLE_OPERARIO_INGRESO";
    public static final String DIALOG_TAG_RESPONSABLE_OPERARIO_TERMINO = "RESPONSABLE_OPERARIO_TERMINO";
    public static final String DIALOG_TAG_RESPONSABLE_CAMPO_TERMINO = "RESPONSABLE_CAMPO_TERMINO";

    public static final String DIALOG_TAG_RESPONSABLE_DEVOLUCION_SIEMBRA = "RESPONSABLE_DEVOLUCION_SIEMBRA";
    public static final String DIALOG_TAG_REVISOR_DEVOLUCION_SIEMBRA = "REVISOR_DEVOLUCION_SIEMBRA";


    public static final String VISTA_FOTOS = "vista";


    public static boolean isNumeric(String texto) {
        try {
            Integer.parseInt(texto);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String formatearCoordenada(String coordenada) throws Exception {

        if (coordenada.isEmpty()) return "";
        String nuevaCoordenada = coordenada;
        nuevaCoordenada = nuevaCoordenada.replaceAll("\\.", "").replaceAll("-", "");

        if (nuevaCoordenada.length() < 8) {
            throw new Exception("El largo de la coordenada debe ser de 10 caracteres.");
        }

        if (nuevaCoordenada.length() > 8) {
            nuevaCoordenada = nuevaCoordenada.substring(0, 8);
        }


        nuevaCoordenada = nuevaCoordenada.substring(0, 2) + "." + nuevaCoordenada.substring(2);

        return "-" + nuevaCoordenada;
    }

    public static long compararFechas(String inputString2) {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String inputString1 = fechaActualSinHora();

        try {
            Date date1 = myFormat.parse(inputString1);
            Date date2 = myFormat.parse(inputString2);
            long diff = date1.getTime() - date2.getTime();
            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean isTablet(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        int screenWidthDp = configuration.screenWidthDp;
        int screenHeightDp = configuration.screenHeightDp;

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(metrics);
        int screenSize = Math.min(metrics.widthPixels, metrics.heightPixels);

        if (screenSize >= 600 && configuration.smallestScreenWidthDp >= 600) {
            // Es probable que sea una tableta
            return true;
        }

        return false;
    }

    public static boolean exportDatabse(String databaseName, String packageName) {

        boolean respuesta = true;
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();


            /* CREAR CARPETA curimapu_respaldo_bd */
            File miFile = new File(Environment.getExternalStorageDirectory(), Utilidades.DIRECTORIO_RESPALDO);
            boolean isCreada = miFile.exists();

            if (!isCreada) {
                isCreada = miFile.mkdirs();
            }

            if (isCreada) {
                if (sd.canWrite()) {
                    String currentDBPath = "//data//" + packageName + "//databases//" + databaseName + "";
                    String backupDBPath = Utilidades.DIRECTORIO_RESPALDO + "/" + Utilidades.fechaActualSinHoraNombre() + "_backup_curimapu.db";
                    File currentDB = new File(data, currentDBPath);
                    File backupDB = new File(sd, backupDBPath);

                    if (currentDB.exists()) {
                        FileChannel src = new FileInputStream(currentDB).getChannel();
                        FileChannel dst = new FileOutputStream(backupDB).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                    }
                }
            } else {
                respuesta = false;
            }
            return respuesta;
        } catch (Exception e) {
            return false;
        }
    }

    public static File createImageFile(Activity activity) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    public static int getPhenoState(int position) {

        int res;

        switch (position) {
            case 0:
            default:
                res = 0;
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
            case 9:
                res = 4;
                break;
            case 10:
                res = 6;
                break;
            case 11:
                res = 5;
                break;

        }


        return res;
    }

    public static String getAnno() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy", Locale.getDefault());
        return df.format(new Date());
    }

    public static String getStateString(int position) {

        String estado;

        switch (position) {
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

    public static int obtenerAnchoPixelesTexto(String texto, float textSize) {
        Paint p = new Paint();
        Rect bounds = new Rect();
        p.setTextSize(textSize);

        p.getTextBounds(texto, 0, texto.length(), bounds);
        return bounds.width();
    }

    public static String voltearFechaBD(String fecha) {
        try {
            if (fecha != null && fecha.length() > 0) {
                String[] date = fecha.split("-");
                return date[2] + "-" + date[1] + "-" + date[0];
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static String voltearFechaVista(String fecha) {
        try {
            if (fecha != null && fecha.length() > 0) {
                String[] date = fecha.split("-");
                return date[2] + "-" + date[1] + "-" + date[0];
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static void levantarHora(final EditText edit, Context context) {
        String hora = Utilidades.hora();

        String[] horaRota;
        try {
            if (!TextUtils.isEmpty(edit.getText())) {
                horaRota = edit.getText().toString().split(":");

                if (horaRota.length <= 1) {
                    horaRota = hora.split(":");
                }
            } else {
                horaRota = hora.split(":");
            }
        } catch (Exception e) {
            horaRota = hora.split(":");
        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(context, (timePicker, i, i1) -> {
            String horaShow = (i < 10) ? "0" + i : i + "";
            String minutosShow = (i1 < 10) ? "0" + i1 : i1 + "";

            String horaFinal = horaShow + ":" + minutosShow + ":00";
            edit.setText(horaFinal);
        },
                Integer.parseInt(horaRota[0]),
                Integer.parseInt(horaRota[1]),
                true
        );
        timePickerDialog.show();
    }

    public static void levantarFecha(final EditText edit, Context context) {

        String fecha = Utilidades.fechaActualSinHora();
        String[] fechaRota;

        if (!TextUtils.isEmpty(edit.getText())) {
            try {
                fechaRota = Utilidades.voltearFechaBD(edit.getText().toString()).split("-");
            } catch (Exception e) {
                fechaRota = fecha.split("-");
            }
        } else {
            fechaRota = fecha.split("-");
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (datePicker, year, month, dayOfMonth) -> {

            month = month + 1;
            String mes = "", dia;

            if (month < 10) {
                mes = "0" + month;
            } else {
                mes = String.valueOf(month);
            }

            if (dayOfMonth < 10) dia = "0" + dayOfMonth;
            else dia = String.valueOf(dayOfMonth);

            String finalDate = dia + "-" + mes + "-" + year;
            edit.setText(finalDate);
        }, Integer.parseInt(fechaRota[0]), (Integer.parseInt(fechaRota[1]) - 1), Integer.parseInt(fechaRota[2]));
        datePickerDialog.show();

    }


    public static String fechaActualConHora() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return df.format(new Date());
    }


    public static String hora() {
        return new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
    }


    public static String fechaActualSinHoraNombre() {

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        return df.format(new Date());
    }


    public static String fechaActualSinHora() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return df.format(new Date());
    }

    public static String fechaActualInvSinHora() {
        return new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }

    public static Integer[] neededRotation(Uri ff) {
        Integer[] inte = new Integer[2];
        if (ff != null && ff.getPath() != null) {

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
        } else {
            inte[0] = 0;
            inte[1] = 0;
        }

        return inte;
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public static void avisoListo(Activity activity, String title, String message, String textButton) {
        View viewInfalted = LayoutInflater.from(activity).inflate(R.layout.alert_empty, null);

        final AlertDialog builder = new AlertDialog.Builder(activity)
                .setView(viewInfalted)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(textButton, (dialogInterface, i) -> {
                })
                .create();

        builder.setOnShowListener(dialog -> {
            Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
            b.setOnClickListener(v -> builder.dismiss());
        });
        builder.setCancelable(false);
        builder.show();
    }

    public static Object[] appendValue(Object[] obj, Object newObj) {

        ArrayList<Object> temp = new ArrayList<Object>(Arrays.asList(obj));
        temp.add(newObj);
        return temp.toArray();

    }

    public static boolean isLandscape(Activity context) {
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
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2) {
                    h = "0" + h;
                }
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e("MD5", "md5() NoSuchAlgorithmException: " + e.getMessage());
        }
        return "";
    }

    public static String imageToString(String ruta) {

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeFile(ruta, null);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
            byte[] imgByte = byteArrayOutputStream.toByteArray();

            return Base64.encodeToString(imgByte, Base64.DEFAULT);
        } catch (Exception e) {
            return "";
        }

    }

    public static String calculaPromediosEstacionFloracion(List<EstacionesCompletas> estacionesCompletasList, int cantidadMachos, String separador) {
        StringBuilder promedioMString = new StringBuilder();
        String promedioHString = "";

        for (int i = 1; i <= cantidadMachos; i++) {
            double promedio = 0;
            double promedioH = 0;
            for (EstacionesCompletas completas : estacionesCompletasList) {

                for (EstacionFloracionDetalle detalle : completas.getDetalles()) {

                    if (detalle.getTituloDato().equals("M" + i)) {
                        promedio += Double.parseDouble(detalle.getValor_dato()) / estacionesCompletasList.size();
                    }

                    if (detalle.getTipo_dato().equals("H")) {
                        promedioH += Double.parseDouble(detalle.getValor_dato()) / estacionesCompletasList.size();
                    }
                }
            }

            promedioMString.append("M").append(i).append(" = ").append(promedio).append("% ").append(separador);
            promedioHString = "H = " + promedioH + "% ";

        }

        return promedioMString.append(promedioHString).toString();
    }

    public static String transformarValorPP(String valor) {
        if (valor == null || valor.isEmpty()) return "";


        String[] splitted = valor.split(" ");
        if (splitted.length > 1) {
            return splitted[0].charAt(0) + splitted[1].substring(0, 1);
        }

        return valor.substring(0, 2);
    }


}


