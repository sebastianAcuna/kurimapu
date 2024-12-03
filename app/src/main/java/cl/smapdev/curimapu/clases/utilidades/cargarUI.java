package cl.smapdev.curimapu.clases.utilidades;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.tablas.pro_cli_mat;
import es.dmoral.toasty.Toasty;

public class cargarUI {

    static final String COORDENADA = "COORDENADA";

    public static ArrayList<ArrayList> cargarUI(View view, int idConstraint, Activity activity, String idMaterial, int idEtapa, int cliente, ArrayList<ArrayList> all, String temporada) {

        ArrayList<Integer> id_generica = new ArrayList<>();
        ArrayList<Integer> id_importante = new ArrayList<>();
        ArrayList<EditText> editTexts = new ArrayList<>();
        ArrayList<TextView> textViews = new ArrayList<>();
        ArrayList<RecyclerView> recyclers = new ArrayList<>();
        ArrayList<ImageView> images = new ArrayList<>();
        ArrayList<ArrayList> genn = new ArrayList<>();
        ArrayList<Spinner> spinners = new ArrayList<>();
        ArrayList<CheckBox> check = new ArrayList<>();


        List<pro_cli_mat> list = MainActivity.myAppDB.myDao().getProCliMatByMateriales(idMaterial, idEtapa, cliente, temporada);

        if (list.isEmpty()) {

            genn.add(id_generica);
            genn.add(id_importante);
            genn.add(textViews);
            genn.add(editTexts);
            genn.add(recyclers);
            genn.add(images);
            genn.add(spinners);
            genn.add(check);

            return genn;
        }


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String ss = prefs.getString("lang", "eng");

        ConstraintLayout constraintLayout = view.findViewById(idConstraint);
        ConstraintSet constraintSet = new ConstraintSet();

        TextView prop = new TextView(new ContextThemeWrapper(activity, R.style.sub_titles_forms), null, 0);
        prop.setId(View.generateViewId());

        if (view.findViewWithTag("PROPERTIES_" + idMaterial + "_" + idEtapa) != null && !all.isEmpty()) {

            id_generica = (ArrayList<Integer>) all.get(0);
            id_importante = (ArrayList<Integer>) all.get(1);
            textViews = (ArrayList<TextView>) all.get(2);
            editTexts = (ArrayList<EditText>) all.get(3);
            recyclers = (ArrayList<RecyclerView>) all.get(4);
            images = (ArrayList<ImageView>) all.get(5);
            spinners = (ArrayList<Spinner>) all.get(6);
            check = (ArrayList<CheckBox>) all.get(7);

            genn.add(id_generica);
            genn.add(id_importante);
            genn.add(textViews);
            genn.add(editTexts);
            genn.add(recyclers);
            genn.add(images);
            genn.add(spinners);
            genn.add(check);

            return genn;
        }

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

        View tvOld = prop;

        ArrayList<String> propiedades = new ArrayList<>();
        for (pro_cli_mat fs : list) {
            String nombre = (ss.equals("eng")) ? fs.getNombre_en() : fs.getNombre_es();
            if (!propiedades.contains(nombre)) {
                propiedades.add(nombre);
            }
        }

        int cont = 2;
        ArrayList<String> titulosUsados = new ArrayList<>();
        ArrayList<String> titulosUsadosRecyclers = new ArrayList<>();


        for (pro_cli_mat fs : list) {
            boolean eslista = false;
            String nombre = fs.getNombre_es() + "\n(" + fs.getNombre_en() + ")";
            String nombreElemento = fs.getNombre_elemento_es() + "\n(" + fs.getNombre_elemento_en() + ")";

            if (!titulosUsados.contains(nombre)) {

                titulosUsados.add(nombre);

                TextView tvTitle = new TextView(new ContextThemeWrapper(activity, R.style.titles_forms), null, 0);

                int id = View.generateViewId();
                tvTitle.setId(id);
                id_generica.add(tvTitle.getId());
                id_importante.add(fs.getId_prop_mat_cli());

                tvTitle.setText(nombre);
                constraintLayout.addView(tvTitle, cont);
                cont++;
                ViewGroup.LayoutParams paramTitle = tvTitle.getLayoutParams();
                paramTitle.height = WRAP_CONTENT;
                paramTitle.width = WRAP_CONTENT;
                tvTitle.setLayoutParams(paramTitle);

                ImageView img = null;


                if (fs.getEs_lista() != null && fs.getEs_lista().equals("SI") && !fs.getTipo_cambio().equals("RECYCLER_UNO_TEXTVIEW")) {

                    img = new ImageView(activity);
                    img.setId(View.generateViewId());
                    id_generica.add(img.getId());
                    id_importante.add(fs.getId_prop());
                    img.setFocusable(true);
                    img.setClickable(true);
                    img.setFocusableInTouchMode(true);


                    TypedValue outValue = new TypedValue();
                    activity.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
                    img.setBackgroundResource(outValue.resourceId);

                    img.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_playlist_add_black_24dp));
                    img.setColorFilter(activity.getResources().getColor(R.color.colorSecondary));
                    img.setPadding(20, 0, 20, 0);

                    constraintLayout.addView(img, cont);
                    cont++;
                    ViewGroup.LayoutParams paramTitleImg = img.getLayoutParams();
                    paramTitleImg.height = 100;
                    paramTitleImg.width = 0;
                    img.setLayoutParams(paramTitleImg);

                }

                constraintSet.clone(constraintLayout);
                constraintSet.connect(tvTitle.getId(), ConstraintSet.TOP, tvOld.getId(), ConstraintSet.BOTTOM, 0);
                constraintSet.connect(tvTitle.getId(), ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START, 0);


                if (fs.getEs_lista() != null && fs.getEs_lista().equals("SI")) {

                    if (!fs.getTipo_cambio().equals("RECYCLER_UNO_TEXTVIEW")) {
                        constraintSet.connect(img.getId(), ConstraintSet.START, tvTitle.getId(), ConstraintSet.END, 0);
                        constraintSet.connect(img.getId(), ConstraintSet.TOP, tvTitle.getId(), ConstraintSet.TOP, 0);
                    }
                    eslista = true;
                }

                constraintSet.applyTo(constraintLayout);
                if (fs.getEs_lista() != null && fs.getEs_lista().equals("SI") && !fs.getTipo_cambio().equals("RECYCLER_UNO_TEXTVIEW")) {
                    images.add(img);
                }

                tvOld = tvTitle;

            } else {
                if (fs.getEs_lista() != null && fs.getEs_lista().equals("SI") && !fs.getTipo_cambio().equals("RECYCLER_UNO_TEXTVIEW")) {
                    eslista = true;
                }
            }


            if (eslista) {

                if (!titulosUsadosRecyclers.contains(nombre)) {
                    titulosUsadosRecyclers.add(nombre);

                    List<pro_cli_mat> Popr = MainActivity.myAppDB.myDao().getProCliMatByIdProp(fs.getId_prop(), idMaterial, cliente, temporada);
                    if (!Popr.isEmpty()) {
                        View tvO = null;
                        int el = 1;
                        for (pro_cli_mat p : Popr) {


                            TextView textView = new TextView(activity/*new ContextThemeWrapper(activity, R.style.titles_forms), null, 0*/);
                            textView.setId(View.generateViewId());
                            textView.setText((ss.equals("eng")) ? p.getNombre_elemento_en() : p.getNombre_elemento_es());
                            textView.setGravity(Gravity.CENTER);
                            textView.setVisibility(View.INVISIBLE);
                            textView.setTextColor(activity.getResources().getColor(R.color.colorOnSurface));


                            textView.setTag("VISTAS_" + p.getId_prop());
                            constraintLayout.addView(textView, cont);
                            cont++;


                            ViewGroup.LayoutParams param = textView.getLayoutParams();
                            param.height = WRAP_CONTENT;
                            param.width = 0;
                            textView.setLayoutParams(param);


                            constraintSet.clone(constraintLayout);
                            /* PROP */
                            constraintSet.connect(textView.getId(), ConstraintSet.TOP, tvOld.getId(), ConstraintSet.BOTTOM, 0);


                            if (tvO != null) {
                                constraintSet.connect(tvO.getId(), ConstraintSet.END, textView.getId(), ConstraintSet.START, 0);
                                constraintSet.connect(textView.getId(), ConstraintSet.START, tvO.getId(), ConstraintSet.END, 0);

                            } else {
                                constraintSet.connect(textView.getId(), ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START, 0);
                            }

                            if (el == Popr.size()) {
                                constraintSet.connect(textView.getId(), ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END, 0);
                                tvOld = textView;
                            }

                            constraintSet.applyTo(constraintLayout);

                            el++;
                            tvO = textView;
                            textViews.add(textView);
                        }
                    }

                    RecyclerView tv = new RecyclerView(activity);
                    tv.setId(View.generateViewId());
                    tv.setTag("lista_" + fs.getId_prop());


                    id_generica.add(tv.getId());
                    id_importante.add(fs.getId_prop());

                    constraintLayout.addView(tv, cont);
                    cont++;

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
                    tvOld = tv;
                }
            } else {

                if (fs.getEs_lista() != null && fs.getEs_lista().equals("NO")) {

                    TextView tv = new TextView(new ContextThemeWrapper(activity, R.style.sub_titles_forms), null, 0);

                    tv.setId(View.generateViewId());
                    tv.setText(nombreElemento);
                    constraintLayout.addView(tv, cont);
                    cont++;
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


                        et.setText(nombreElemento);
                        constraintLayout.addView(et, cont);
                        cont++;
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

                        if (fs.getTipo_cambio().equals("SPINNER")) {

                            Spinner sp = new Spinner(activity);

                            int id = View.generateViewId();
                            sp.setId(id);
                            id_generica.add(sp.getId());
                            id_importante.add(fs.getId_prop_mat_cli());


                            constraintLayout.addView(sp, cont);
                            cont++;

                            ViewGroup.LayoutParams paramEditText = sp.getLayoutParams();
                            paramEditText.height = WRAP_CONTENT;
                            paramEditText.width = 0;
                            sp.setLayoutParams(paramEditText);

                            constraintSet.clone(constraintLayout);
                            /* PROP */
                            constraintSet.connect(tv.getId(), ConstraintSet.TOP, tvOld.getId(), ConstraintSet.BOTTOM, 0);
                            constraintSet.connect(tv.getId(), ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START, 0);
                            constraintSet.connect(tv.getId(), ConstraintSet.END, sp.getId(), ConstraintSet.START, 0);
                            /* VAL */
                            constraintSet.connect(sp.getId(), ConstraintSet.TOP, tvOld.getId(), ConstraintSet.BOTTOM, 0);
                            constraintSet.connect(sp.getId(), ConstraintSet.START, tv.getId(), ConstraintSet.END, 0);
                            constraintSet.connect(sp.getId(), ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END, 0);


                            spinners.add(sp);


                        } else if (fs.getTipo_cambio().equals("TEXTVIEW")) {
                            TextView et = new TextView(activity);
                            int id = View.generateViewId();
                            et.setId(id);
                            id_generica.add(et.getId());
                            id_importante.add(fs.getId_prop_mat_cli());
                            constraintLayout.addView(et, cont);
                            cont++;

                            ViewGroup.LayoutParams paramEditText = et.getLayoutParams();
                            paramEditText.height = WRAP_CONTENT;
                            paramEditText.width = 0;
                            et.setLayoutParams(paramEditText);

                            constraintSet.clone(constraintLayout);
                            /* PROP */
                            constraintSet.connect(tv.getId(), ConstraintSet.TOP, tvOld.getId(), ConstraintSet.BOTTOM, 0);
                            constraintSet.connect(tv.getId(), ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START, 0);
                            constraintSet.connect(tv.getId(), ConstraintSet.END, et.getId(), ConstraintSet.START, 0);
                            /* VAL */
                            constraintSet.connect(et.getId(), ConstraintSet.TOP, tv.getId(), ConstraintSet.TOP, 0);
                            constraintSet.connect(et.getId(), ConstraintSet.START, tv.getId(), ConstraintSet.END, 0);
                            constraintSet.connect(et.getId(), ConstraintSet.BOTTOM, tv.getId(), ConstraintSet.BOTTOM, 0);
                            constraintSet.connect(et.getId(), ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END, 0);

                            textViews.add(et);


                        } else if (fs.getTipo_cambio().equals("CHECK")) {
                            CheckBox et = new CheckBox(activity);
                            int id = View.generateViewId();
                            et.setId(id);
                            id_generica.add(et.getId());
                            id_importante.add(fs.getId_prop_mat_cli());
                            constraintLayout.addView(et, cont);
                            cont++;

                            ViewGroup.LayoutParams paramEditText = et.getLayoutParams();
                            paramEditText.height = WRAP_CONTENT;
                            paramEditText.width = 0;
                            et.setLayoutParams(paramEditText);

                            constraintSet.clone(constraintLayout);
                            /* PROP */
                            constraintSet.connect(tv.getId(), ConstraintSet.TOP, tvOld.getId(), ConstraintSet.BOTTOM, 0);
                            constraintSet.connect(tv.getId(), ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START, 0);
                            constraintSet.connect(tv.getId(), ConstraintSet.END, et.getId(), ConstraintSet.START, 0);
                            /* VAL */
                            constraintSet.connect(et.getId(), ConstraintSet.TOP, tv.getId(), ConstraintSet.TOP, 0);
                            constraintSet.connect(et.getId(), ConstraintSet.START, tv.getId(), ConstraintSet.END, 0);
                            constraintSet.connect(et.getId(), ConstraintSet.BOTTOM, tv.getId(), ConstraintSet.BOTTOM, 0);
                            constraintSet.connect(et.getId(), ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END, 0);

                            check.add(et);
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
                            cont++;

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
                                    et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                                    et.setHint(activity.getResources().getString(R.string.valor));
                                    break;
                            }


                            EditText finalEt1 = et;
                            if (fs.getValidacion() != null && fs.getValidacion().equals(COORDENADA)) {
                                finalEt1.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                    }

                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                    }

                                    @Override
                                    public void afterTextChanged(Editable editable) {

                                        if (editable.toString().isEmpty()) return;
                                        if (editable.toString().equals("NO APLICA")) return;
                                        if (finalEt1.getInputType() != (InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED))
                                            return;


                                        String textoIngresado = finalEt1.getText().toString();

                                        if (textoIngresado.charAt(0) != '-' && !Utilidades.isNumeric(String.valueOf(textoIngresado.charAt(0)))) {
                                            Toasty.error(view.getContext(), "Debes ingresar signo - o un numero como primer caracter", Toast.LENGTH_LONG, true).show();
                                            finalEt1.setText("");
                                            return;
                                        }

                                        if (textoIngresado.indexOf(',') > 0) {
                                            finalEt1.setText(textoIngresado.replaceAll(",", "."));
                                            return;
                                        }

                                        if (textoIngresado.indexOf('.') > 0) {
                                            String[] textoAntesDePunto = textoIngresado.split("\\.");
                                            if (textoAntesDePunto[0].length() > 3) {

                                                Toasty.error(view.getContext(), "solo puede ingresar 3 caracteres antes del punto " + textoAntesDePunto[0], Toast.LENGTH_LONG, true).show();
                                                finalEt1.setText(textoIngresado.substring(0, 3));
                                                return;
                                            }

                                            if (textoAntesDePunto.length > 1 && textoAntesDePunto[1].length() > 6) {
                                                Toasty.error(view.getContext(), "solo puede ingresar 6 caracteres despues del punto", Toast.LENGTH_LONG, true).show();
                                                String nuevoTexto = textoAntesDePunto[0] + "." + textoAntesDePunto[1].substring(0, 6);
                                                finalEt1.setText(nuevoTexto);
                                            }
                                        }
                                    }
                                });
                            }


                            Button button = new Button(activity);
                            int idButton = View.generateViewId();
                            button.setId(idButton);
                            button.setText("N/A");
                            EditText finalEt = et;

                            button.setOnClickListener(view1 -> {
                                finalEt.setText("NO APLICA");
                                finalEt.requestFocus();
                            });

                            constraintLayout.addView(button, cont);

                            constraintSet.clone(constraintLayout);
                            int pxValue = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, activity.getResources().getDisplayMetrics());

                            /* PROP */
                            constraintSet.connect(tv.getId(), ConstraintSet.TOP, tvOld.getId(), ConstraintSet.BOTTOM, pxValue);
                            constraintSet.connect(tv.getId(), ConstraintSet.START, constraintLayout.getId(), ConstraintSet.START, 0);
                            constraintSet.connect(tv.getId(), ConstraintSet.END, finalEt.getId(), ConstraintSet.START, 0);
                            /* VAL */
                            constraintSet.connect(finalEt.getId(), ConstraintSet.TOP, tvOld.getId(), ConstraintSet.BOTTOM, pxValue);
                            constraintSet.connect(finalEt.getId(), ConstraintSet.START, tv.getId(), ConstraintSet.END, 0);
                            constraintSet.connect(finalEt.getId(), ConstraintSet.END, button.getId(), ConstraintSet.START, 0);
                            /* BUTTON */
//                            constraintSet.setMargin(idButton, ConstraintSet.TOP, pxValue);
                            constraintSet.setMargin(idButton, ConstraintSet.BOTTOM, pxValue);
                            constraintSet.connect(button.getId(), ConstraintSet.TOP, tvOld.getId(), ConstraintSet.BOTTOM, 0);
                            constraintSet.connect(button.getId(), ConstraintSet.START, finalEt.getId(), ConstraintSet.END, 1);
                            constraintSet.connect(button.getId(), ConstraintSet.END, constraintLayout.getId(), ConstraintSet.END, 1);

                            editTexts.add(finalEt);
                        }
                    }

                    tvOld = tv;
                }
            }
            constraintSet.applyTo(constraintLayout);

        }


        genn.add(id_generica);
        genn.add(id_importante);
        genn.add(textViews);
        genn.add(editTexts);
        genn.add(recyclers);
        genn.add(images);
        genn.add(spinners);
        genn.add(check);

        return genn;
    }
}
