package cl.smapdev.curimapu.fragments.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracion;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracionDetalle;
import cl.smapdev.curimapu.clases.tablas.EstacionFloracionEstaciones;
import es.dmoral.toasty.Toasty;

public class DialogMuestraEstacion extends DialogFragment {


    private TextView lbl_tipo_muestra;
    private EditText et_valor_muestra;
    private Button   btn_guardar_muestra;
    private Button   btn_cancelar_muestra;

    private String tituloInput;

    private IOnSave IOnSave;

    public interface IOnSave {
        void onSave( boolean saved );
    }

    private EstacionFloracionEstaciones estacionFloracionEstaciones;
    private EstacionFloracionDetalle detalle;

    public void setEstacionFloracionEstaciones(EstacionFloracionEstaciones estacionFloracionEstaciones) {
        this.estacionFloracionEstaciones = estacionFloracionEstaciones;
    }

    public void setDetalle(EstacionFloracionDetalle detalle) {
        this.detalle = detalle;
    }

    public void setTituloInput(String tituloInput) {
        this.tituloInput = tituloInput;
    }

    public String tipoDato;

    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }

    public static DialogMuestraEstacion newInstance(EstacionFloracionEstaciones estacionFloracionEstaciones, String tituloInput, String tipoDato, EstacionFloracionDetalle detalle, IOnSave onSave){
        DialogMuestraEstacion dg = new DialogMuestraEstacion();
        dg.setIOnSave( onSave );
        dg.setDetalle(detalle);
        dg.setEstacionFloracionEstaciones(estacionFloracionEstaciones);
        dg.setTituloInput(tituloInput);
        dg.setTipoDato(tipoDato);
        return dg;
    }

    public void setIOnSave(IOnSave onSave){ this.IOnSave = onSave; }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_agregar_muestra_estacion, null);
        builder.setView(view);

        bind(view);

        builder.setTitle("Nueva Muestra");


        lbl_tipo_muestra.setText(tituloInput);

        if(detalle != null){
            et_valor_muestra.setText(detalle.getValor_dato());
        }

        btn_guardar_muestra.setOnClickListener(view1 -> onSave());
        btn_cancelar_muestra.setOnClickListener(view1 -> cerrar());

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if( dialog != null ){
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            int width = ViewGroup.LayoutParams.WRAP_CONTENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);

        }
    }

    public void cerrar(){
        Dialog dialog = getDialog();
        if( dialog != null ){
            dialog.dismiss();
        }
    }

    public void onSave(){

        if(et_valor_muestra.getText().toString().isEmpty()){
            Toasty.error(requireContext(),
                    "Debes completar todos los campos antes de guardar." ,
                    Toast.LENGTH_LONG, true).show();
            return;
        }

        EstacionFloracionDetalle floracionDetalle = new EstacionFloracionDetalle();

        floracionDetalle.setTipo_dato(tipoDato);
        floracionDetalle.setTituloDato(tituloInput);
        floracionDetalle.setValor_dato(et_valor_muestra.getText().toString());

        floracionDetalle.setClave_unica_floracion_estacion((estacionFloracionEstaciones != null) ? estacionFloracionEstaciones.getClave_unica_floracion_estaciones() : "0");

        ExecutorService executor2 = Executors.newSingleThreadExecutor();
        try {
        if(detalle != null){
            floracionDetalle.setId_estacion_floracion_detalle(detalle.id_estacion_floracion_detalle);
            floracionDetalle.setClave_unica_floracion_detalle(detalle.clave_unica_floracion_detalle);

            executor2.submit(() -> MainActivity.myAppDB.DaoEstacionFloracion()
                    .updateEstacionDetalle(floracionDetalle)).get();
        }else{
            floracionDetalle.setClave_unica_floracion_detalle(UUID.randomUUID().toString());

            executor2.submit(() -> MainActivity.myAppDB.DaoEstacionFloracion()
                    .insertEstacionDetalle(floracionDetalle)).get();
        }
            if(estacionFloracionEstaciones != null && estacionFloracionEstaciones.getClave_unica_floracion_cabecera() != null){
                executor2.submit(() -> MainActivity.myAppDB.DaoEstacionFloracion()
                        .updateCabeceraSinc(estacionFloracionEstaciones.clave_unica_floracion_cabecera)).get();
            }


        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        Dialog dialog = getDialog();
        IOnSave.onSave( true);
        if( dialog != null ){ dialog.dismiss();}
    }


    public void bind(View view){

        lbl_tipo_muestra = view.findViewById(R.id.lbl_tipo_muestra);
        et_valor_muestra = view.findViewById(R.id.et_valor_muestra);
        btn_guardar_muestra = view.findViewById(R.id.btn_guardar_muestra);
        btn_cancelar_muestra = view.findViewById(R.id.btn_cancelar_muestra);

    }

}
