package cl.smapdev.curimapu.clases;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.tablas.Fotos;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.FragmentContratos;
import cl.smapdev.curimapu.fragments.contratos.FragmentListVisits;

import static cl.smapdev.curimapu.clases.utilidades.Utilidades.obtenerAnchoPixelesTexto;


public class Tabla {

    private TableLayout tabla; // Layout donde se pintará la tabla
    private ArrayList<TableRow> filas; // Array de las filas de la tabla
    private Activity actividad;
    private Resources rs;
    private int FILAS, COLUMNAS; // Filas y columnas de nuestra tabla
    private SharedPreferences prefs;


    public Tabla(TableLayout tabla, Activity actividad) {
        this.actividad = actividad;
        this.tabla = tabla;
        rs = this.actividad.getResources();
        FILAS = COLUMNAS = 0;
        filas = new ArrayList<TableRow>();

        prefs = this.actividad.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
    }


    /**
     * Añade la cabecera a la tabla
     * @param recursocabecera Recurso (array) donde se encuentra la cabecera de la tabla
     */
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
            layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(s), TableRow.LayoutParams.WRAP_CONTENT);
            texto.setText(s);
            texto.setGravity(Gravity.CENTER_HORIZONTAL);
            texto.setTextAppearance(actividad, android.R.style.TextAppearance_Small);
            texto.setBackgroundResource(R.drawable.margenes_cabecera);
            texto.setTextColor(actividad.getResources().getColor(android.R.color.white));
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

    /**
     * Agrega una fila a la tabla
     * @param ls Elementos de la fila
     **/
    public void agregarFilaTabla(final AnexoCompleto ls)
    {

        TableRow.LayoutParams layoutCelda;
        TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow fila = new TableRow(actividad);
        fila.setLayoutParams(layoutFila);



            TextView anexo = new TextView(actividad);
            TextView variedad = new TextView(actividad);
            TextView especie= new TextView(actividad);
            TextView agricultor = new TextView(actividad);
            TextView potrero = new TextView(actividad);

        Button botonForm = new Button(actividad);
        Button botonVer = new Button(actividad);




            anexo.setText(String.valueOf(ls.getAnexoContrato().getAnexo_contrato()));
            variedad.setText(String.valueOf(ls.getVariedad().getDesc_variedad()));
            especie.setText(String.valueOf(ls.getEspecie().getDesc_especie()));
            agricultor.setText(String.valueOf(ls.getAgricultor().getNombre_agricultor()));
            potrero.setText(String.valueOf(ls.getAnexoContrato().getProtero()));



        anexo.setGravity(Gravity.CENTER_HORIZONTAL);
        potrero.setGravity(Gravity.CENTER_HORIZONTAL);
        variedad.setGravity(Gravity.CENTER_HORIZONTAL);

        botonForm.setText(actividad.getResources().getText(R.string.form_form));
        Drawable img = botonForm.getContext().getResources().getDrawable( R.drawable.ic_assignment );
        botonForm.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);


        botonForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) actividad;
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
                        prefs.edit().putInt(Utilidades.SHARED_VISIT_FICHA_ID, ls.getAnexoContrato().getId_ficha_contrato()).apply();
                        prefs.edit().putString(Utilidades.SHARED_VISIT_MATERIAL_ID, ls.getAnexoContrato().getId_especie_anexo()).apply();
                        prefs.edit().putString(Utilidades.SHARED_VISIT_ANEXO_ID, ls.getAnexoContrato().getId_anexo_contrato()).apply();
                        prefs.edit().putInt(Utilidades.SHARED_VISIT_VISITA_ID, 0).apply();
                    }

                    activity.cambiarFragment(new FragmentContratos(), Utilidades.FRAGMENT_CONTRATOS, R.anim.slide_in_left,R.anim.slide_out_left);
                }

            }
        });


        botonVer.setText(actividad.getResources().getText(R.string.form_ver));

        Drawable imgVer = botonVer.getContext().getResources().getDrawable( R.drawable.ic_remove_red_eye );
        botonVer.setCompoundDrawablesWithIntrinsicBounds( imgVer, null, null, null);

        botonVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity activity = (MainActivity) actividad;

                if (activity != null) {
                    if (prefs != null) {
                        prefs.edit().putInt(Utilidades.SHARED_VISIT_FICHA_ID, ls.getAnexoContrato().getId_ficha_contrato()).apply();
                        prefs.edit().putString(Utilidades.SHARED_VISIT_ANEXO_ID, ls.getAnexoContrato().getId_anexo_contrato()).apply();
                    }

                    activity.cambiarFragment(new FragmentListVisits(), Utilidades.FRAGMENT_LIST_VISITS, R.anim.slide_in_left, R.anim.slide_out_left);
                }

            }
        });


//        botonVer.setBackgroundColor(botonVer.getContext().getResources().getColor(R.color.colorAccent));

       layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(anexo.getText().toString()),
                   TableRow.LayoutParams.WRAP_CONTENT);

        anexo.setLayoutParams(layoutCelda);


//        layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(agricultor.getText().toString()),
//                   TableRow.LayoutParams.WRAP_CONTENT);
//
//        agricultor.setLayoutParams(layoutCelda);


        layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(potrero.getText().toString()),
                TableRow.LayoutParams.WRAP_CONTENT);

        potrero.setLayoutParams(layoutCelda);


/*        layoutCelda = new TableRow.LayoutParams(obtenerAnchoPixelesTexto(variedad.getText().toString()),
                TableRow.LayoutParams.WRAP_CONTENT);

        variedad.setLayoutParams(layoutCelda);*/


            fila.addView(anexo);
            fila.addView(variedad);
            fila.addView(especie);
            fila.addView(agricultor);
            fila.addView(potrero);
            fila.addView(botonForm);
            fila.addView(botonVer);


            FILAS++;

        tabla.addView(fila);
        filas.add(fila);


    }
}
