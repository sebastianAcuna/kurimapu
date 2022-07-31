package cl.smapdev.curimapu.clases;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.FotosListAdapter;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.relaciones.AnexoWithDates;
import cl.smapdev.curimapu.clases.tablas.Fotos;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.FragmentContratos;
import cl.smapdev.curimapu.fragments.contratos.FragmentListVisits;

import static cl.smapdev.curimapu.clases.utilidades.Utilidades.obtenerAnchoPixelesTexto;

public class TablaAnexosFechas {

    private TableLayout tabla; // Layout donde se pintará la tabla
    private ArrayList<TableRow> filas; // Array de las filas de la tabla
    private Activity actividad;
    private Resources rs;
    private int FILAS, COLUMNAS; // Filas y columnas de nuestra tabla
    private SharedPreferences prefs;

    private float anchoColumnas = 25.0f;


    private OnItemClickListener itemClickListener;
    private OnItemClickListenerVer itemClickListenerver;

    boolean _listo = false;

    public interface OnItemClickListener{ void onItemClick(AnexoWithDates plz); }
    public interface OnItemClickListenerVer{ void onItemClick(AnexoWithDates plz); }


    public boolean getListo(){
        return _listo;
    }

    public TablaAnexosFechas(TableLayout tabla, Activity actividad, OnItemClickListener itemClickListener, OnItemClickListenerVer itemClickListenerver ) {
        this.actividad = actividad;
        this.tabla = tabla;
        rs = this.actividad.getResources();
        FILAS = COLUMNAS = 0;
        filas = new ArrayList<TableRow>();
        this.itemClickListener = itemClickListener;
        this.itemClickListenerver = itemClickListenerver;

        prefs = this.actividad.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
    }


    public void agregarCabecera(int recursocabecera)
    {
        TableRow.LayoutParams layoutCelda;
        TableRow fila = new TableRow(actividad);
        TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        fila.setLayoutParams(layoutFila);

        String[] arraycabecera = rs.getStringArray(recursocabecera);
        COLUMNAS = arraycabecera.length;

        for (String s : arraycabecera) {
            TextView texto = new TextView(actividad);
            layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(s, anchoColumnas), TableRow.LayoutParams.WRAP_CONTENT);
            texto.setText(s);
            texto.setGravity(Gravity.CENTER_HORIZONTAL);
            texto.setTextAppearance(actividad, android.R.style.TextAppearance_Small);


            switch (s){
                case "INICIO DESPANO":
                        texto.setBackgroundResource(R.drawable.margen_columna_inicio_despano);
                        texto.setTextColor(actividad.getResources().getColor(android.R.color.black));
                    break;
                case "5% FLORACION":
                    texto.setBackgroundResource(R.drawable.margen_columna_cinco_floracion);
                    texto.setTextColor(actividad.getResources().getColor(android.R.color.black));
                    break;
                case "INICIO CORTE DE SEDA":
                    texto.setBackgroundResource(R.drawable.margen_columna_inicio_corte_seda);
                    texto.setTextColor(actividad.getResources().getColor(android.R.color.black));
                    break;
                case "INICIO COSECHA":
                    texto.setBackgroundResource(R.drawable.margen_columna_inicio_cosecha);
                    texto.setTextColor(actividad.getResources().getColor(android.R.color.black));
                    break;
                case "TERMINO COSECHA":
                    texto.setBackgroundResource(R.drawable.margen_columna_termino_cosecha);
                    texto.setTextColor(actividad.getResources().getColor(android.R.color.black));
                    break;
                case "TERMINO LABORES POST COSECHA":
                case "DETALLE LABORES":
                    texto.setBackgroundResource(R.drawable.margen_columna_termino_labores);
                    texto.setTextColor(actividad.getResources().getColor(android.R.color.black));
                    break;
                default:
                    texto.setBackgroundResource(R.drawable.margenes_cabecera);
                    texto.setTextColor(actividad.getResources().getColor(android.R.color.white));
                    break;
            }


            texto.setLayoutParams(layoutCelda);

            fila.addView(texto);
        }

        tabla.addView(fila);
        filas.add(fila);

        FILAS++;
    }


    public void removeViews(){
        this.tabla.removeAllViews();
    }



    public void agregarFilaTabla(final AnexoWithDates ls, int posicion)
    {

//        TableRow.LayoutParams layoutCelda;
        TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow fila = new TableRow(actividad);
        fila.setLayoutParams(layoutFila);




        if(posicion == 0){

            TextView termino_labores_post_cosecha  = new TextView(actividad);
            TextView detalle_labores  = new TextView(actividad);
            TextView anexo = new TextView(actividad);
            TextView comuna = new TextView(actividad);
            TextView inicio_despano = new TextView(actividad);
            TextView five_floracion = new TextView(actividad);
            TextView inicio_corte_seda = new TextView(actividad);
            TextView inicio_cosecha = new TextView(actividad);
            TextView termino_cosecha = new TextView(actividad);


            anexo.setGravity(Gravity.CENTER_HORIZONTAL);
            comuna.setGravity(Gravity.CENTER_HORIZONTAL);
            inicio_despano.setGravity(Gravity.CENTER_HORIZONTAL);
            five_floracion.setGravity(Gravity.CENTER_HORIZONTAL);
            inicio_corte_seda.setGravity(Gravity.CENTER_HORIZONTAL);
            inicio_cosecha.setGravity(Gravity.CENTER_HORIZONTAL);
            termino_cosecha.setGravity(Gravity.CENTER_HORIZONTAL);
            termino_labores_post_cosecha.setGravity(Gravity.CENTER_HORIZONTAL);
            detalle_labores.setGravity(Gravity.CENTER_HORIZONTAL);


//            LinearLayout ly = new LinearLayout(actividad);

            Button botonEditar = new Button(actividad);
            botonEditar.setText("Editar");
            botonEditar.setHeight(30);


            Button botonVer = new Button(actividad);
            botonVer.setText("Ver");
            botonVer.setHeight(30);

//            ly.addView(botonEditar);
//            ly.addView(botonVer);
//            ly.setOrientation();



            anexo.setText((ls.getAnexoCompleto().getAnexoContrato().getAnexo_contrato() != null) ? String.valueOf(ls.getAnexoCompleto().getAnexoContrato().getAnexo_contrato()) : "Sin registros");
            comuna.setText((ls.getComuna() !=null && ls.getComuna().getDesc_comuna() != null) ?  ls.getComuna().getDesc_comuna() : "Sin Registros");
            inicio_despano.setText((ls.getAnexoCorreoFichas() != null && ls.getAnexoCorreoFichas().getInicio_despano() != null) ? Utilidades.voltearFechaVista(ls.getAnexoCorreoFichas().getInicio_despano()) : "Sin Registros");
            five_floracion.setText((ls.getAnexoCorreoFichas() != null && ls.getAnexoCorreoFichas().getCinco_porciento_floracion() != null) ?Utilidades.voltearFechaVista(ls.getAnexoCorreoFichas().getCinco_porciento_floracion()): "Sin Registros");
            inicio_corte_seda.setText((ls.getAnexoCorreoFichas() != null && ls.getAnexoCorreoFichas().getInicio_corte_seda() != null) ?Utilidades.voltearFechaVista(ls.getAnexoCorreoFichas().getInicio_corte_seda()): "Sin Registros");
            inicio_cosecha.setText((ls.getAnexoCorreoFichas() != null && ls.getAnexoCorreoFichas().getInicio_cosecha() != null) ? Utilidades.voltearFechaVista(ls.getAnexoCorreoFichas().getInicio_cosecha()): "Sin Registros");
            termino_cosecha.setText((ls.getAnexoCorreoFichas() != null && ls.getAnexoCorreoFichas().getTermino_cosecha() != null) ?Utilidades.voltearFechaVista(ls.getAnexoCorreoFichas().getTermino_cosecha()): "Sin Registros");
            termino_labores_post_cosecha.setText((ls.getAnexoCorreoFichas() != null && ls.getAnexoCorreoFichas().getTermino_labores_post_cosechas() != null) ? Utilidades.voltearFechaVista(ls.getAnexoCorreoFichas().getTermino_labores_post_cosechas()): "Sin Registros");
            detalle_labores.setText((ls.getAnexoCorreoFichas() != null &&  ls.getAnexoCorreoFichas().getDetalle_labores() != null) ? ls.getAnexoCorreoFichas().getDetalle_labores() : "Sin Registros");


            botonEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(ls);
                }
            });

            botonVer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListenerver.onItemClick(ls);
                }
            });


//            layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(anexo.getText().toString(), 50.0f),
//                    TableRow.LayoutParams.WRAP_CONTENT);
//
//            anexo.setLayoutParams(layoutCelda);
//
//            layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(potrero.getText().toString(), 20.0f),
//                    TableRow.LayoutParams.WRAP_CONTENT);
//
//            potrero.setLayoutParams(layoutCelda);


//            layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(anexo.getText().toString(), anchoColumnas), TableRow.LayoutParams.WRAP_CONTENT);
//            anexo.setLayoutParams(layoutCelda);


            fila.addView(botonEditar);
            fila.addView(botonVer);
            fila.addView(anexo);
            fila.addView(comuna);
            fila.addView(inicio_despano);
            fila.addView(five_floracion);
            fila.addView(inicio_corte_seda);
            fila.addView(inicio_cosecha);
            fila.addView(termino_cosecha);
            fila.addView(termino_labores_post_cosecha);
            fila.addView(detalle_labores);




        }else{

            TextView anexo = new TextView(actividad);
            TextView comuna = new TextView(actividad);
            TextView inicio_despano = new TextView(actividad);
            TextView five_floracion = new TextView(actividad);
            TextView inicio_corte_seda = new TextView(actividad);
            TextView inicio_cosecha = new TextView(actividad);
            TextView termino_cosecha = new TextView(actividad);


            anexo.setGravity(Gravity.CENTER_HORIZONTAL);
            inicio_cosecha.setGravity(Gravity.CENTER_HORIZONTAL);
            inicio_despano.setGravity(Gravity.CENTER_HORIZONTAL);
            five_floracion.setGravity(Gravity.CENTER_HORIZONTAL);
            inicio_corte_seda.setGravity(Gravity.CENTER_HORIZONTAL);
            comuna.setGravity(Gravity.CENTER_HORIZONTAL);
            termino_cosecha.setGravity(Gravity.CENTER_HORIZONTAL);

//            Button botonForm = new Button(actividad);
            Button botonVer = new Button(actividad);
            botonVer.setText("ver");

            String nombreAgricultor = "";
            if(ls.getAnexoCompleto().getAgricultor() != null && ls.getAnexoCompleto().getAgricultor().getNombre_agricultor() != null){
                nombreAgricultor = (ls.getAnexoCompleto().getAgricultor().getNombre_agricultor().length() > 30)
                    ?  ls.getAnexoCompleto().getAgricultor().getNombre_agricultor().substring(0, 29) : ls.getAnexoCompleto().getAgricultor().getNombre_agricultor();
            }else{
                nombreAgricultor = "sin registros";
            }

            anexo.setText((ls.getUsuario() !=null && ls.getUsuario().getUser() != null) ? ls.getUsuario().getUser() : "Sin registros");
            inicio_cosecha.setText((ls.getAnexoCompleto().getAnexoContrato().getAnexo_contrato() != null) ?  ls.getAnexoCompleto().getAnexoContrato().getAnexo_contrato() : "Sin Registros");
            inicio_despano.setText(nombreAgricultor);
            five_floracion.setText((ls.getAnexoCompleto().getLotes() != null && ls.getAnexoCompleto().getLotes().getNombre_lote() != null) ? ls.getAnexoCompleto().getLotes().getNombre_lote() : "Sin Registros");
            inicio_corte_seda.setText((ls.getAnexoCompleto().getEspecie() != null && ls.getAnexoCompleto().getEspecie().getDesc_especie() != null) ? ls.getAnexoCompleto().getEspecie().getDesc_especie()  : "Sin Registros");
            comuna.setText((ls.getComuna() != null && ls.getComuna().getDesc_comuna() != null) ? ls.getComuna().getDesc_comuna() : "Sin Registros");


            termino_cosecha.setText((ls.getVisitas() != null && ls.getVisitas().getFecha_visita() != null) ? Utilidades.voltearFechaVista(ls.getVisitas().getFecha_visita()) : "Sin registros");


            botonVer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListenerver.onItemClick(ls);
                }
            });
//

//            layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(inicio_cosecha.getText().toString(), 50.0f),
//                    TableRow.LayoutParams.WRAP_CONTENT);
//
//            inicio_cosecha.setLayoutParams(layoutCelda);
//            layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(potrero.getText().toString(), 20.0f),
//                    TableRow.LayoutParams.WRAP_CONTENT);
//
//            potrero.setLayoutParams(layoutCelda);


//            layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(anexo.getText().toString(), anchoColumnas), TableRow.LayoutParams.WRAP_CONTENT);
//            anexo.setLayoutParams(layoutCelda);

//            fila.addView();
            fila.addView(botonVer);
            fila.addView(anexo);
            fila.addView(inicio_cosecha);
            fila.addView(inicio_despano);
            fila.addView(five_floracion);
            fila.addView(inicio_corte_seda);
            fila.addView(comuna);
            fila.addView(termino_cosecha);
        }

        FILAS++;
        tabla.addView(fila);
        filas.add(fila);





//
//        variedad.setText(String.valueOf(ls.getVariedad().getDesc_variedad()));
//        especie.setText(String.valueOf(ls.getEspecie().getDesc_especie()));
//        agricultor.setText(String.valueOf(ls.getAgricultor().getNombre_agricultor()));
//        potrero.setText(String.valueOf(ls.getAnexoContrato().getProtero()));





//        botonForm.setText(actividad.getResources().getText(R.string.form_form));
//        Drawable img = botonForm.getContext().getResources().getDrawable( R.drawable.ic_assignment );
//        botonForm.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);


//        botonForm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new LazyLoad().execute(ls);
//            }
//        });


//        botonVer.setText(actividad.getResources().getText(R.string.form_ver));

//        Drawable imgVer = botonVer.getContext().getResources().getDrawable( R.drawable.ic_remove_red_eye );
//        botonVer.setCompoundDrawablesWithIntrinsicBounds( imgVer, null, null, null);

//        botonVer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                MainActivity activity = (MainActivity) actividad;
//
//                if (activity != null) {
//                    if (prefs != null) {
//                        prefs.edit().putInt(Utilidades.SHARED_VISIT_FICHA_ID, ls.getAnexoContrato().getId_ficha_contrato()).apply();
//                        prefs.edit().putString(Utilidades.SHARED_VISIT_ANEXO_ID, ls.getAnexoContrato().getId_anexo_contrato()).apply();
//                        prefs.edit().putString(Utilidades.SHARED_VISIT_MATERIAL_ID, ls.getAnexoContrato().getId_especie_anexo()).apply();
//                        prefs.edit().putString(Utilidades.SHARED_VISIT_TEMPORADA, ls.getAnexoContrato().getTemporada_anexo()).apply();
//                        prefs.edit().putInt(Utilidades.SHARED_VISIT_VISITA_ID, 0).apply();
//                    }
//
//                    activity.cambiarFragment(new FragmentListVisits(), Utilidades.FRAGMENT_LIST_VISITS, R.anim.slide_in_left, R.anim.slide_out_left);
//                }
//
//            }
//        });


//        botonVer.setBackgroundColor(botonVer.getContext().getResources().getColor(R.color.colorAccent));




//        layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(agricultor.getText().toString()),
//                   TableRow.LayoutParams.WRAP_CONTENT);
//
//        agricultor.setLayoutParams(layoutCelda);





/*        layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(variedad.getText().toString()),
                TableRow.LayoutParams.WRAP_CONTENT);

        variedad.setLayoutParams(layoutCelda);*/










    }



    private class LazyLoad extends AsyncTask<AnexoCompleto, Void, Void> {

        MainActivity activity ;
        private ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            activity = (MainActivity) actividad;

            progressDialog = new ProgressDialog(actividad);
            progressDialog.setTitle(actividad.getResources().getString(R.string.espere));
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(AnexoCompleto... ls) {
            if (activity != null){

                /* eliminara detalles de las propiedades (todas)*/
                MainActivity.myAppDB.myDao().deleteTempVisitas();
                MainActivity.myAppDB.myDao().deleteDetalleVacios();


                List<Fotos> fotos = MainActivity.myAppDB.myDao().getFotosByIdVisita(0);
                if (fotos.size() > 0){
                    for (Fotos fts : fotos){
                        try{
                            File file = new File(fts.getRuta());
                            if (file.exists()) {
                                boolean eliminado = file.delete();
                                if (eliminado){
                                    MainActivity.myAppDB.myDao().deleteFotos(fts);
                                }
                            }
                        }catch (Exception e){
                            Log.e("ERROR DELETING", Objects.requireNonNull(e.getMessage()));
                        }
                    }
                }



                if (prefs != null){
                    prefs.edit().putInt(Utilidades.SHARED_VISIT_FICHA_ID, ls[0].getAnexoContrato().getId_ficha_contrato()).apply();
                    prefs.edit().putString(Utilidades.SHARED_VISIT_MATERIAL_ID, ls[0].getAnexoContrato().getId_especie_anexo()).apply();
                    prefs.edit().putString(Utilidades.SHARED_VISIT_ANEXO_ID, ls[0].getAnexoContrato().getId_anexo_contrato()).apply();

                    prefs.edit().putString(Utilidades.SHARED_VISIT_TEMPORADA, ls[0].getAnexoContrato().getTemporada_anexo()).apply();

                    prefs.edit().putInt(Utilidades.SHARED_VISIT_VISITA_ID, 0).apply();
                }


            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            activity.cambiarFragment(new FragmentContratos(), Utilidades.FRAGMENT_CONTRATOS, R.anim.slide_in_left,R.anim.slide_out_left);

            if (progressDialog != null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }
}
