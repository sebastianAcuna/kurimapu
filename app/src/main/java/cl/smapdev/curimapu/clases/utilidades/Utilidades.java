package cl.smapdev.curimapu.clases.utilidades;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.exifinterface.media.ExifInterface;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
    public static final String SHARED_VISIT_VISITA_ID = "shared_visit_visita_id";
    public static final String SHARED_VISIT_MATERIAL_ID = "shared_visit_material_id";




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


    public static ArrayList<ArrayList> cargarUI(View view, int idConstraint, Activity activity, int idMaterial, int idEtapa, ArrayList<ArrayList> all){

        ArrayList<Integer> id_generica = new ArrayList<>();
        ArrayList<Integer> id_importante =  new ArrayList<>();
        ArrayList<EditText> editTexts =  new ArrayList<>();
        ArrayList<TextView> textViews =  new ArrayList<>();
        ArrayList<RecyclerView> recyclers =  new ArrayList<>();
        ArrayList<ArrayList> genn = new ArrayList<>();
        ArrayList<FrameLayout> frames = new ArrayList<>();

        LinearLayout ly12 = (LinearLayout) view.findViewById(R.id.container_papu);

        List<pro_cli_mat> list = MainActivity.myAppDB.myDao().getProCliMatByMateriales(idMaterial, idEtapa);
        if (list.size() > 0) {

            ScrollView sv = new ScrollView(activity);


            ConstraintLayout constraintLayout = (ConstraintLayout) view.findViewById(idConstraint);
            ConstraintSet constraintSet = new ConstraintSet();

            TextView prop = new TextView(new ContextThemeWrapper(activity, R.style.sub_titles_forms), null, 0);
            prop.setId(View.generateViewId());

            if (view.findViewWithTag("PROPERTIES_" + idMaterial + "_" + idEtapa) == null) {

                LinearLayout ly = view.findViewById(R.id.container_linear_fotos);

                FrameLayout fm = (FrameLayout) new FrameLayout(activity);
                fm.setId(View.generateViewId());
                ly.setTag("FOTOS_" + idEtapa);
                ly.addView(fm);
                ViewGroup.LayoutParams lll = fm.getLayoutParams();
                lll.height = WRAP_CONTENT;
                lll.width = 0;
                fm.setLayoutParams(lll);


                frames.add(fm);


                prop.setTag("PROPERTIES_" + idMaterial + "_" + idEtapa);
                prop.setText(activity.getResources().getString(R.string.propiedad));
                constraintLayout.addView(prop, 0);
                ViewGroup.LayoutParams propParam = prop.getLayoutParams();
                propParam.height = WRAP_CONTENT;
                propParam.width = 0;
                prop.setLayoutParams(propParam);

                TextView value = new TextView(new ContextThemeWrapper(activity, R.style.sub_titles_forms), null, 0);
                value.setId(View.generateViewId());
                value.setText(activity.getResources().getString(R.string.valor));
                constraintLayout.addView(value, 1);
                ViewGroup.LayoutParams valueParam = value.getLayoutParams();
                valueParam.height = WRAP_CONTENT;
                valueParam.width = 0;
                value.setLayoutParams(valueParam);

                constraintSet.clone(constraintLayout);


                constraintSet.connect(prop.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP, 0);
                constraintSet.connect(prop.getId(), ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START, 0);
                constraintSet.connect(prop.getId(), ConstraintSet.END, value.getId(), ConstraintSet.START, 0);

                constraintSet.connect(value.getId(), ConstraintSet.START, prop.getId(), ConstraintSet.END, 0);
                constraintSet.connect(value.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP, 0);
                constraintSet.connect(value.getId(), ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END, 0);

                constraintSet.applyTo(constraintLayout);

                View tvOld = (TextView) prop;
//                TextView tvOld = prop;

                ArrayList<String> propiedades = new ArrayList<>();
                for (pro_cli_mat fs : list) {
                    if (!propiedades.contains(fs.getNombre_en())) {
                        propiedades.add(fs.getNombre_en());
                    }
                }

                int cont = 2;
                ArrayList<String> titulosUsados = new ArrayList<>();

                boolean eslista  = false;
                for (pro_cli_mat fs : list) {

                    if (!titulosUsados.contains(fs.getNombre_en())) {

                        titulosUsados.add(fs.getNombre_en());

                        TextView tvTitle = new TextView(new ContextThemeWrapper(activity, R.style.titles_forms), null, 0);

                        int id = View.generateViewId();
                        tvTitle.setId(id);
                        id_generica.add(tvTitle.getId());
                        id_importante.add(fs.getId_prop_mat_cli());


                        tvTitle.setText(fs.getNombre_en());
                        constraintLayout.addView(tvTitle, cont);
                        ViewGroup.LayoutParams paramTitle = tvTitle.getLayoutParams();
                        paramTitle.height = WRAP_CONTENT;
                        paramTitle.width = 0;
                        tvTitle.setLayoutParams(paramTitle);

                        constraintSet.clone(constraintLayout);
                        constraintSet.connect(tvTitle.getId(), ConstraintSet.TOP, tvOld.getId(), ConstraintSet.BOTTOM, 0);
                        constraintSet.connect(tvTitle.getId(), ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START, 0);
                        constraintSet.connect(tvTitle.getId(), ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END, 0);

                        constraintSet.applyTo(constraintLayout);

                        cont++;
                        tvOld = (TextView) tvTitle;


                        if(fs.getEs_lista().equals("SI")){
                            eslista = true;
                        }
                    }


                    if (eslista){

                        RecyclerView tv = new RecyclerView(activity);
                        tv.setId(View.generateViewId());
                        tv.setTag("lista_"+fs.getId_prop());

                        id_generica.add(tv.getId());
                        id_importante.add(fs.getId_prop());

                        constraintLayout.addView(tv, cont);

                        LinearLayoutManager lm = new LinearLayoutManager(activity);
                        tv.setLayoutManager(lm);
                        tv.setHasFixedSize(true);

                        ViewGroup.LayoutParams param = tv.getLayoutParams();
                        param.height = WRAP_CONTENT;
                        param.width = 0;
                        tv.setLayoutParams(param);

                        constraintSet.clone(constraintLayout);
                        /* PROP */
                        constraintSet.connect(tv.getId(), ConstraintSet.TOP, tvOld.getId(), ConstraintSet.BOTTOM, 0);
                        constraintSet.connect(tv.getId(), ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START, 0);
                        constraintSet.connect(tv.getId(), ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END, 0);

                        recyclers.add(tv);

                        eslista = false;
                        tvOld = (RecyclerView) tv;

                    }else {

                        if (fs.getEs_lista().equals("NO")) {


                            TextView tv = new TextView(new ContextThemeWrapper(activity, R.style.sub_titles_forms), null, 0);

                            tv.setId(View.generateViewId());
                            tv.setText(fs.getNombre_elemento());
                            constraintLayout.addView(tv, cont);
                            ViewGroup.LayoutParams param = tv.getLayoutParams();
                            param.height = WRAP_CONTENT;
                            param.width = 0;
                            tv.setLayoutParams(param);


                            if (TextUtils.isEmpty(fs.getTipo_cambio())) {
                                TextView et = new TextView(new ContextThemeWrapper(activity, R.style.sub_titles_forms), null, 0);

                                int id = View.generateViewId();
                                et.setId(id);
                                id_generica.add(et.getId());
                                id_importante.add(fs.getId_prop_mat_cli());


                                et.setText(fs.getNombre_elemento());
                                constraintLayout.addView(et, cont);
                                ViewGroup.LayoutParams paramEditText = et.getLayoutParams();
                                param.height = WRAP_CONTENT;
                                param.width = 0;
                                et.setLayoutParams(paramEditText);

                                constraintSet.clone(constraintLayout);
                                /* PROP */
                                constraintSet.connect(tv.getId(), ConstraintSet.TOP, tvOld.getId(), ConstraintSet.BOTTOM, 0);
                                constraintSet.connect(tv.getId(), ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START, 0);
                                constraintSet.connect(tv.getId(), ConstraintSet.END, et.getId(), ConstraintSet.START, 0);
                                /* VAL */
                                constraintSet.connect(et.getId(), ConstraintSet.TOP, tvOld.getId(), ConstraintSet.BOTTOM, 0);
                                constraintSet.connect(et.getId(), ConstraintSet.START, tv.getId(), ConstraintSet.END, 0);
                                constraintSet.connect(et.getId(), ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END, 0);

                                textViews.add(et);

                            } else {

                                EditText et = null;
                                switch (fs.getTipo_cambio()) {
                                    case "TEXT":
                                    case "INT":
                                    case "DECIMAL":
                                    default:
                                        et = new EditText(activity);
                                        break;
                                    case "DATE":
                                        et = new EditText(new ContextThemeWrapper(activity, R.style.input_date_forms));
                                        break;
                                }

                                int id = View.generateViewId();
                                et.setId(id);
                                id_generica.add(et.getId());
                                id_importante.add(fs.getId_prop_mat_cli());


                                constraintLayout.addView(et, cont);

                                ViewGroup.LayoutParams paramEditText = et.getLayoutParams();
                                paramEditText.height = WRAP_CONTENT;
                                paramEditText.width = 0;
                                et.setLayoutParams(paramEditText);


                                switch (fs.getTipo_cambio()) {
                                    case "TEXT":
                                        et.setInputType(InputType.TYPE_CLASS_TEXT);
                                        et.setHint(activity.getResources().getString(R.string.valor));
                                        break;
                                    case "DATE":
                                        et.setInputType(InputType.TYPE_CLASS_DATETIME);
                                        break;
                                    case "INT":
                                        et.setInputType(InputType.TYPE_CLASS_NUMBER);
                                        et.setHint(activity.getResources().getString(R.string.valor));
                                        break;
                                    case "DECIMAL":
                                        et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                        et.setHint(activity.getResources().getString(R.string.valor));
                                        break;
                                }
                                constraintSet.clone(constraintLayout);
                                /* PROP */
                                constraintSet.connect(tv.getId(), ConstraintSet.TOP, tvOld.getId(), ConstraintSet.BOTTOM, 0);
                                constraintSet.connect(tv.getId(), ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START, 0);
                                constraintSet.connect(tv.getId(), ConstraintSet.END, et.getId(), ConstraintSet.START, 0);
                                /* VAL */
                                constraintSet.connect(et.getId(), ConstraintSet.TOP, tvOld.getId(), ConstraintSet.BOTTOM, 0);
                                constraintSet.connect(et.getId(), ConstraintSet.START, tv.getId(), ConstraintSet.END, 0);
                                constraintSet.connect(et.getId(), ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END, 0);


                                editTexts.add(et);
                            }

                            tvOld = (TextView) tv;
                        }
                    }
                    constraintSet.applyTo(constraintLayout);
                    cont++;
                }
            }else{
                if (all.size() > 0){
                    id_generica = (ArrayList<Integer>) all.get(0);
                    id_importante = (ArrayList<Integer>) all.get(1);
                    textViews = (ArrayList<TextView>) all.get(2);
                    editTexts = (ArrayList<EditText>) all.get(3);
                    recyclers = (ArrayList<RecyclerView>) all.get(4);
                    frames = (ArrayList<FrameLayout>) all.get(5);
                }

            }
        }
        genn.add(id_generica);
        genn.add(id_importante);
        genn.add(textViews);
        genn.add(editTexts);
        genn.add(recyclers);
        genn.add(frames);



        return genn;
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

}
