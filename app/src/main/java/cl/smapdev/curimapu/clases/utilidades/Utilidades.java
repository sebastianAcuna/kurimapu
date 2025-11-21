package cl.smapdev.curimapu.clases.utilidades;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;

public class Utilidades {

    public static final String APPLICATION_VERSION = "4.4.0611";

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
    public static final String FRAGMENT_ANEXO_FICHA = "fragment_anexo_ficha";
    public static final String FRAGMENT_LISTA_ALMACIGOS = "fragment_lista_almacigos";
    public static final String FRAGMENT_VISITA_ALMACIGOS = "fragment_visita_almacigos";


    public static final String FRAGMENT_CHECKLIST = "fragment_checklist";
    public static final String FRAGMENT_CHECKLIST_SIEMBRA = "fragment_checklist_siembra";
    public static final String FRAGMENT_CHECKLIST_CAPACITACION_SIEMBRA = "fragment_checklist_capacitacion_siembra";
    public static final String FRAGMENT_CHECKLIST_APLICACION_HORMONAS = "fragment_checklist_aplicacion_hormonas";
    public static final String FRAGMENT_CHECKLIST_ROGUING = "fragment_checklist_roguing";
    public static final String FRAGMENT_CHECKLIST_REVISION_FRUTOS = "fragment_checklist_revision_frutos";
    public static final String FRAGMENT_CHECKLIST_GUIA_INTERNA = "fragment_checklist_guia_interna";
    public static final String FRAGMENT_CHECKLIST_RECEPCION_PLANTINERA = "fragment_checklist_recepcion_plantinera";

    public static final String affiliate_id = "vb7jbic553ts";

    public static final int TIPO_DOCUMENTO_CHECKLIST_SIEMBRA = 1;
    public static final int TIPO_DOCUMENTO_CHECKLIST_APLICACION_HORMONAS = 2;
    public static final int TIPO_DOCUMENTO_CHECKLIST_REVISION_FRUTOS = 3;
    public static final int TIPO_DOCUMENTO_CHECKLIST_ROGUING = 4;
    public static final int TIPO_DOCUMENTO_CHECKLIST_GUIA_INTERNA = 5;
    public static final int TIPO_DOCUMENTO_CHECKLIST_RECEPCION_PLANTINERA = 6;


    public static final int RAIZ_CUBIERTA = 1;
    public static final int RAIZ_DESNUDA = 2;


    public static final String IP_PRODUCCION = "192.168.1.42";
    //        public static final String IP_PRODUCCION = "curivegetables.zcloud.cl";
//    public static final String IP_PRODUCCION = "curivegetables.zpruebas.cl";
//    public static final String URL_SERVER_API = "https://" + IP_PRODUCCION;
    public static final String URL_SERVER_API = "http://" + IP_PRODUCCION + "/curimapu_vegetables";

    public static final String IP_DESARROLLO = "www.zcloud16.cl";
    public static final String IP_PRUEBAS = "190.13.170.26";

    public static final String DIRECTORIO_IMAGEN = "curimapu_vegetable_imagenes";
    public static final String DIRECTORIO_RESPALDO = "curimapu_vegetable_respaldo_bd";
    public static final String NOMBRE_DATABASE = "curimapu_vegetable.db";

    public static final String SHARED_NAME = "preference_app";
    public static final String SHARED_USER = "user_name";
    public static final String SHARED_PREFERENCE = "frg_preference";
    public static final String SELECTED_ANO = "selected_ano";

    public static final String SHARED_SERVER_ID_USER = "server_user_id";
    public static final String SHARED_SERVER_ID_SERVER = "server_server_id";

    /*DIALOGO FILTRO FICHAS*/
    public static final String SHARED_FILTER_FICHAS_COMUNA = "filter_fichas_comuna";
    public static final String SHARED_FILTER_FICHAS_RADIO = "filter_fichas_radio";
    public static final String SHARED_FILTER_FICHAS_YEAR = "filter_fichas_anno";

    public static final String SHARED_FILTER_VISITAS_YEAR = "filter_visitas_anno";
    public static final String SHARED_FILTER_VISITAS_ESPECIE = "filter_visitas_especie";
    public static final String SHARED_FILTER_VISITAS_VARIEDAD = "filter_visitas_variedad";

    public static final String SHARED_ETAPA_SELECTED = "shared_etapas_selected";

    public static final String SHARED_VISIT_FICHA_ID = "shared_visit_ficha_id";
    public static final String SHARED_VISIT_ANEXO_ID = "shared_visit_anexo_id";
    public static final String SHARED_VISIT_VISITA_ID = "shared_visit_visita_id";
    public static final String SHARED_VISIT_MATERIAL_ID = "shared_visit_material_id";
    public static final String SHARED_VISIT_TEMPORADA = "shared_visit_temporada";


    public static final String DIALOG_TAG_CAPACITACION_SIEMBRA = "CAPACITACION_SIEMBRA";
    public static final String DIALOG_TAG_FOTO_CHECKLIST_SIEMBRA_ENVASE = "DIALOG_TAG_FOTO_CHECKLIST_SIEMBRA_ENVASE";
    public static final String DIALOG_TAG_FOTO_CHECKLIST_SIEMBRA_SEMILLA = "DIALOG_TAG_FOTO_CHECKLIST_SIEMBRA_SEMILLA";
    public static final String DIALOG_TAG_FIRMA_RESPONSABLE_AP_HORMONA = "CAPACITACION_FIRMA_RESPONSABLE_AP_HORMONA";
    public static final String DIALOG_TAG_FIRMA_PRESTADOR_SERVICIO_AP_HORMONA = "CAPACITACION_FIRMA_PRESTADOR_SERVICIO_AP_HORMONA";
    public static final String DIALOG_TAG_FIRMA_AGRICULTOR_PLANTIN = "DIALOG_TAG_FIRMA_AGRICULTOR_PLANTIN";
    public static final String DIALOG_TAG_FIRMA_ENCARGADO_PLANTIN = "DIALOG_TAG_FIRMA_ENCARGADO_PLANTIN";
    public static final String DIALOG_TAG_FIRMA_AGRICULTOR_REVISION_FRUTOS = "DIALOG_TAG_FIRMA_AGRICULTOR_REVISION_FRUTOS";
    public static final String DIALOG_TAG_ROGUING_DETALLE = "DIALOG_TAG_ROGUING_DETALLE";
    public static final String DIALOG_TAG_ROGUING_DETALLE_FECHA = "DIALOG_TAG_ROGUING_DETALLE_FECHA";
    public static final String DIALOG_TAG_GUIA_INTERNA_TRANSPORTISTA = "DIALOG_TAG_GUIA_INTERNA_TRANSPORTISTA";
    public static final String DIALOG_TAG_GUIA_INTERNA_SUPERVISOR = "DIALOG_TAG_GUIA_INTERNA_SUPERVISOR";
    public static final String DIALOG_TAG_REVISION_FRUTO_AGRICULTOR = "DIALOG_TAG_REVISION_FRUTO_AGRICULTOR";
    public static final String DIALOG_TAG_REVISION_FRUTO_DETALLE = "DIALOG_TAG_REVISION_FRUTO_DETALLE";
    public static final String DIALOG_TAG_REVISOR_LIMPIEZA_INGRESO = "REVISOR_LIMPIEZA_INGRESO";
    public static final String DIALOG_TAG_RESPONSABLE_ASEO_INGRESO = "RESPONSABLE_ASEO_INGRESO";
    public static final String DIALOG_TAG_RESPONSABLE_ASEO_SALIDA = "RESPONSABLE_ASEO_SALIDA";
    public static final String DIALOG_TAG_REVISOR_LIMPIEZA_SALIDA = "REVISOR_LIMPIEZA_SALIDA";
    public static final String DIALOG_TAG_RESPONSABLE_CAMPO_INGRESO = "RESPONSABLE_CAMPO_INGRESO";
    public static final String DIALOG_TAG_RESPONSABLE_OPERARIO_INGRESO = "RESPONSABLE_OPERARIO_INGRESO";
    public static final String DIALOG_TAG_RESPONSABLE_OPERARIO_TERMINO = "RESPONSABLE_OPERARIO_TERMINO";
    public static final String DIALOG_TAG_RESPONSABLE_CAMPO_TERMINO = "RESPONSABLE_CAMPO_TERMINO";
    public static final String DIALOG_TAG_RECEPCION_PLANTIN = "DIALOG_TAG_RECEPCION_PLANTIN";


    public static boolean isNumeric(String texto) {
        try {
            Integer.parseInt(texto);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static final String VISTA_FOTOS = "vista";

    public static boolean exportDatabse(String databaseName, String packageName) {
        return true;
    }


    public static File createImageFile(Activity activity, String nombre) throws IOException {

        String name = (nombre != null) ? nombre : "";

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + name + "_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
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

    public static String[] getAnoCompleto(int anno) {
        String[] str = new String[2];
        str[0] = anno + "-01-01";
        str[1] = anno + "-12-31";


        return str;
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
            if (fecha != null && !fecha.isEmpty()) {
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


    public static String sanitizarString(String texto, String caracteresDisponibles) {

        StringBuilder filteredText = new StringBuilder();
        for (int i = 0; i < texto.length(); i++) {
            char currentChar = texto.charAt(i);
            if (caracteresDisponibles.contains(String.valueOf(currentChar)) || String.valueOf(currentChar).equals("\n")) {
                filteredText.append(currentChar);
            }
        }
        return filteredText.toString();
    }

    public static void validarDecimal(String number, String titulo) {
        if (number.isEmpty()) return;
        try {
            Double.parseDouble(number);
        } catch (NumberFormatException e) {
            throw new Error(titulo + " invalido(a) ");
        }
    }

    public static void validarDecimal(String number, String titulo, boolean allowNegatives) {
        if (number.isEmpty()) return;
        try {
            double n = Double.parseDouble(number);
            if (!allowNegatives && n < 0)
                throw new Error(titulo + " invalido(a), no puede ser negativo ");
        } catch (NumberFormatException e) {
            throw new Error(titulo + " invalido(a) ");
        }
    }

    public static void validarNumero(String number, String titulo, boolean allowNegatives) {
        if (number.isEmpty()) return;
        try {
            int n = Integer.parseInt(number);
            if (!allowNegatives && n < 0)
                throw new Error(titulo + " invalido(a), no puede ser negativo ");
        } catch (NumberFormatException e) {
            throw new Error(titulo + " invalido(a) ");
        }
    }

    public static void validarNumero(String number, String titulo) {
        if (number.isEmpty()) return;
        try {
            Integer.parseInt(number);
        } catch (NumberFormatException e) {
            throw new Error(titulo + " invalido(a) ");
        }
    }

    public static void validarHora(String hora, String titulo) {
        if (hora.isEmpty()) return;
        String fecha = Utilidades.fechaActualSinHora() + " " + hora;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            dtf.parse(fecha);
        } catch (Exception e) {
            throw new Error(titulo + " invalido(a) ");
        }

    }

    public static void validarFecha(String d, String titulo) {
        if (d.isEmpty()) return;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            dtf.parse(d);
        } catch (Exception e) {
            throw new Error(titulo + " invalido(a) ");
        }
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


    public static long diferenciaDiasEntreFechas(String fechaDesde, String fechaHasta) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaObjetivo = LocalDate.parse(fechaDesde, formatter);
        LocalDate fechaActual = LocalDate.parse(fechaHasta, formatter);
        return ChronoUnit.DAYS.between(fechaActual, fechaObjetivo);
    }


    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
        // Primero lee solo dimensiones
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calcula el factor de escala
        options.inSampleSize = Utilidades.calculateInSampleSize(options, reqWidth, reqHeight);

        // Decodifica con el factor
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
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
            byte messageDigest[] = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String h = Integer.toHexString(0xFF & b);
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

    public static String convertirAStringBase64(String rutaArchivo) {
        try {
            File file = new File(rutaArchivo); // construye el File a partir de la ruta
            byte[] bytes = Files.readAllBytes(file.toPath());
            return Base64.encodeToString(bytes, Base64.NO_WRAP);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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

    public static String transformarValorPP(String valor) {
        if (valor == null || valor.isEmpty()) return "";


        String[] splitted = valor.split(" ");
        if (splitted.length > 1) {
            return splitted[0].charAt(0) + splitted[1].substring(0, 1);
        }

        return valor.substring(0, 2);
    }


    public static String myDecimalFormat(Object obj) {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator(',');
        simbolos.setGroupingSeparator('.');
        DecimalFormat formatoDecimal = new DecimalFormat("#,##0.00", simbolos);
        return formatoDecimal.format(obj);
    }


    public static String myNumberFormat(Object obj) {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator(',');
        simbolos.setGroupingSeparator('.');
        NumberFormat formatoEntero = new DecimalFormat("#,##0", simbolos);
        return formatoEntero.format(obj);
    }


    public static boolean checkPermission(Context context) {
        // Permisos comunes para todas las versiones
        int cameraPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        int fineLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);

        boolean commonPermissionsGranted = cameraPermission == PackageManager.PERMISSION_GRANTED &&
                fineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                coarseLocationPermission == PackageManager.PERMISSION_GRANTED;

        // A partir de Android 13 (TIRAMISU), se usan los permisos de medios granulares
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            int readImagesPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES);
            return commonPermissionsGranted && readImagesPermission == PackageManager.PERMISSION_GRANTED;
        }
        // Para versiones anteriores a Android 10, WRITE_EXTERNAL_STORAGE sigue siendo relevante
        else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            int storagePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return commonPermissionsGranted && storagePermission == PackageManager.PERMISSION_GRANTED;
        }

        // Entre Android 10 y 12, las apps no necesitan permisos para escribir en su propio directorio,
        // por lo que solo verificamos los permisos comunes.
        return commonPermissionsGranted;
    }

    public static void requestPermission(Activity activity) {
        List<String> permissionsToRequest = new ArrayList<>();

        // Permisos comunes que siempre se solicitan
        permissionsToRequest.add(Manifest.permission.CAMERA);
        permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        // Lógica de permisos de almacenamiento basada en la versión de Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Para Android 13 y superior, solicitar READ_MEDIA_IMAGES si es necesario
            permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES);
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            // Para versiones anteriores a Android 10, solicitar el permiso de escritura antiguo
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        // No se añade ningún permiso de almacenamiento para Android 10, 11 y 12,
        // ya que el Scoped Storage permite a la app escribir en su directorio específico sin permisos.

        ActivityCompat.requestPermissions(activity, permissionsToRequest.toArray(new String[0]), 100);
    }


    public static void setToolbar(MainActivity activity, View view, String title, String subTitle) {

        androidx.appcompat.widget.Toolbar toolbar = view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            // 4. Muestra el botón de "hacia atrás" o el ícono de hamburguesa.
            //    Esto le indica al usuario que puede navegar hacia arriba/atrás.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);

            // 5. Establece el título que desees para esta pantalla.
            actionBar.setTitle(title);
            if (!subTitle.isEmpty()) {
                actionBar.setSubtitle(subTitle);
            }

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    activity,
                    activity.drawerLayout, // Accede al drawerLayout de MainActivity
                    toolbar,
                    R.string.open_drawer,
                    R.string.close_drawer
            );

            activity.drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        }

    }

}
