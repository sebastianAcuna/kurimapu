package cl.smapdev.curimapu.clases;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;



import java.util.ArrayList;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.FragmentContratos;

import static cl.smapdev.curimapu.clases.utilidades.Utilidades.obtenerAnchoPixelesTexto;


public class Tabla {

    private TableLayout tabla; // Layout donde se pintará la tabla
    private ArrayList<TableRow> filas; // Array de las filas de la tabla
    private Activity actividad;
    private Resources rs;
    private int FILAS, COLUMNAS; // Filas y columnas de nuestra tabla


    public Tabla(TableLayout tabla, Activity actividad) {
        this.actividad = actividad;
        this.tabla = tabla;
        rs = this.actividad.getResources();
        FILAS = COLUMNAS = 0;
        filas = new ArrayList<TableRow>();
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
    public void agregarFilaTabla(AnexoCompleto ls)
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
                    activity.cambiarFragment(new FragmentContratos(), Utilidades.FRAGMENT_CONTRATOS, R.anim.slide_in_left,R.anim.slide_out_left);
                }

            }
        });

//        botonForm.setBackgroundColor(botonForm.getContext().getResources().getColor(R.color.colorPrimaryDark));




        botonVer.setText(actividad.getResources().getText(R.string.form_ver));

        Drawable imgVer = botonVer.getContext().getResources().getDrawable( R.drawable.ic_remove_red_eye );
        botonVer.setCompoundDrawablesWithIntrinsicBounds( imgVer, null, null, null);

        botonVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
