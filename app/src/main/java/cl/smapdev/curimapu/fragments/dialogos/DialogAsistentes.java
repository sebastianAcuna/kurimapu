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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.tablas.CheckListCapacitacionSiembraDetalle;
import cl.smapdev.curimapu.clases.temporales.TempFirmas;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import es.dmoral.toasty.Toasty;

public class DialogAsistentes  extends DialogFragment {

    private EditText et_fecha;
    private EditText et_area;
    private EditText et_nombre;
    private EditText et_rut;


    private Button btn_firma;
    private Button btn_posponer_anexo_fecha;
    private Button btn_guardar_anexo_fecha;

    private ImageView iv_firma;
    private IOnSave IOnSave;

    public interface IOnSave {
        void onSave( boolean saved );
    }


    public static DialogAsistentes newInstance( IOnSave onSave ){
        DialogAsistentes dg = new DialogAsistentes();
        dg.setIOnSave( onSave );
        return dg;
    }

    public void setIOnSave(IOnSave onSave){ this.IOnSave = onSave; }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_agregar_asistente, null);
        builder.setView(view);

        bind(view);

        builder.setTitle("Nuevo Asistente");


        btn_guardar_anexo_fecha.setOnClickListener(view1 -> onSave());
        btn_posponer_anexo_fecha.setOnClickListener(view1 -> cerrar());

        btn_firma.setOnClickListener(view1 -> {

            if(et_rut.getText().toString().isEmpty() || et_area.getText().toString().isEmpty() ||
                    et_nombre.getText().toString().isEmpty() || et_fecha.getText().toString().isEmpty()){
                Toasty.error(requireContext(),
                        "Debe ingresar fecha, area, nombre y rut antes de firmar" ,
                        Toast.LENGTH_LONG, true).show();
                return;
            }


            FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
            Fragment prev = requireActivity()
                    .getSupportFragmentManager()
                    .findFragmentByTag(Utilidades.DIALOG_TAG_FIRMA_CAPACITACION_SIEMBRA);
            if (prev != null) {
                ft.remove(prev);
            }


            String etRA = et_rut.getText().toString()
                    .trim()
                    .toLowerCase(Locale.ROOT)
                    .replaceAll(" ", "_")
                    .replaceAll("ñ", "n")
                    .replaceAll("á", "a")
                    .replaceAll("é", "e")
                    .replaceAll("í", "i")
                    .replaceAll("ó", "o")
                    .replaceAll("ú", "u")
                    +"_"+
                    Utilidades.fechaActualConHora()
                            .replaceAll(" " ,"")
                            .replaceAll("-" ,"")
                            .replaceAll(":", "_")+".png";

            DialogFirma dialogo = DialogFirma.newInstance(
                    Utilidades.TIPO_DOCUMENTO_CAPACITACION_SIEMBRA,
                    etRA,
                    Utilidades.DIALOG_TAG_FIRMA_CAPACITACION_SIEMBRA,
                    (isSaved, path) -> {
                        if(isSaved){
                            iv_firma.setVisibility(View.VISIBLE);
                        }
                    }
            );

            dialogo.show(ft, Utilidades.DIALOG_TAG_FIRMA_CAPACITACION_SIEMBRA);
        });

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


        if(et_rut.getText().toString().isEmpty() || et_area.getText().toString().isEmpty() ||
                et_nombre.getText().toString().isEmpty() || et_fecha.getText().toString().isEmpty() ||
            iv_firma.getVisibility() == View.GONE){
            Toasty.error(requireContext(),
                    "Debes completar todos los campos antes de guardar." ,
                    Toast.LENGTH_LONG, true).show();
            return;
        }

        CheckListCapacitacionSiembraDetalle detalle = new CheckListCapacitacionSiembraDetalle();

        detalle.setArea_cl_cap_siembra_detalle(et_area.getText().toString());
        detalle.setClave_unica_cl_cap_siembra("0");
        detalle.setFecha_cl_cap_siembra_detalle(Utilidades
                .voltearFechaBD(et_fecha.getText().toString()));
        detalle.setNombre_cl_cap_siembra_detalle(et_nombre.getText().toString());
        detalle.setRut_cl_cap_siembra_detalle(et_rut.getText().toString());
        detalle.setEstado_sincronizacion_detalle(0);

        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {
            List<TempFirmas> firmasFuture = executor.submit(() -> MainActivity.myAppDB.DaoFirmas()
                    .getFirmasByDocum(Utilidades.TIPO_DOCUMENTO_CAPACITACION_SIEMBRA)).get();


            for (TempFirmas ff : firmasFuture){
                detalle.setFirma_cl_cap_siembra_detalle(ff.getPath());
            }

            executor.submit(() -> MainActivity.myAppDB.DaoFirmas()
                    .deleteFirmasByDoc(Utilidades.TIPO_DOCUMENTO_CAPACITACION_SIEMBRA));

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdown();

        String claveUnicaI =
                Math.random()+""+Utilidades.fechaActualConHora()
                .replaceAll(" ", "")
                .replaceAll("-", "")
                .replaceAll(":", "");

        detalle.setClave_unica_cl_cap_siembra_detalle(claveUnicaI);

        ExecutorService executor2 = Executors.newSingleThreadExecutor();
        try {
            executor2.submit(() -> MainActivity.myAppDB.DaoCheckListCapSiembra()
                    .insertCapacitacionSiembraDetalle(detalle)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        Dialog dialog = getDialog();
        IOnSave.onSave( true);
        if( dialog != null ){ dialog.dismiss();}
    }


    public void bind(View view){

        et_fecha = view.findViewById(R.id.et_fecha);
        et_area = view.findViewById(R.id.et_area);
        et_nombre = view.findViewById(R.id.et_nombre);
        et_rut = view.findViewById(R.id.et_rut);
        btn_firma = view.findViewById(R.id.btn_firma);
        btn_posponer_anexo_fecha = view.findViewById(R.id.btn_posponer_anexo_fecha);
        btn_guardar_anexo_fecha = view.findViewById(R.id.btn_guardar_anexo_fecha);
        iv_firma = view.findViewById(R.id.iv_firma);


        et_fecha.setText(Utilidades.voltearFechaVista(Utilidades.fechaActualSinHora()));

        et_fecha.setOnFocusChangeListener((view1, b) -> {
            if(b){
                Utilidades.levantarFecha(et_fecha, requireActivity());
            }
        });

    }

}
