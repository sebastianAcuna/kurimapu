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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.tablas.CheckListRevisionFrutosDetalle;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import es.dmoral.toasty.Toasty;

public class DialogRevisionFrutoDetail extends DialogFragment {

    private MainActivity activity;

    public interface IOnSave {
        void onSave(boolean saved);
    }

    private int conteo = 0;
    private IOnSave IOnSave;
    private CheckListRevisionFrutosDetalle clDetalle;

    public void setIOnSave(DialogRevisionFrutoDetail.IOnSave IOnSave) {
        this.IOnSave = IOnSave;
    }

    public void setClDetalle(CheckListRevisionFrutosDetalle clDetalle) {
        this.clDetalle = clDetalle;
    }

    public void setConteo(int conteo) {
        this.conteo = conteo;
    }

    private TextView tv_conteo;
    private EditText et_frutos_aptos, et_frutos_no_aptos, et_observacion;
    private Button btn_guardar_anexo_fecha, btn_posponer_anexo_fecha;


    public static DialogRevisionFrutoDetail newInstance(IOnSave onSave, CheckListRevisionFrutosDetalle clDetalle, int conteo) {
        DialogRevisionFrutoDetail dg = new DialogRevisionFrutoDetail();
        dg.setIOnSave(onSave);
        dg.setClDetalle(clDetalle);
        dg.setConteo(conteo);
        return dg;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_revision_fruto_detail, null);
        builder.setView(view);

        builder.setTitle("Nuevo fuera de tipo");

        MainActivity a = (MainActivity) getActivity();
        if (a != null) activity = a;
        bind(view);
        return builder.create();
    }


    private void bind(View view) {

        tv_conteo = view.findViewById(R.id.tv_conteo);
        et_frutos_aptos = view.findViewById(R.id.et_frutos_aptos);
        et_frutos_no_aptos = view.findViewById(R.id.et_frutos_no_aptos);
        et_observacion = view.findViewById(R.id.et_observacion);
        btn_guardar_anexo_fecha = view.findViewById(R.id.btn_guardar_anexo_fecha);
        btn_posponer_anexo_fecha = view.findViewById(R.id.btn_posponer_anexo_fecha);

        tv_conteo.setText(String.valueOf(conteo));

        btn_guardar_anexo_fecha.setOnClickListener(v -> onSave());
        btn_posponer_anexo_fecha.setOnClickListener(v -> onCancel());


        if (clDetalle != null) {

            et_frutos_aptos.setText(String.valueOf(clDetalle.getFrutos_aptos()));
            et_frutos_no_aptos.setText(String.valueOf(clDetalle.getFrutos_no_aptos()));
            et_observacion.setText(clDetalle.getComentario());

        }

    }

    public void onSave() {

        if (et_frutos_aptos.getText().toString().isEmpty() || et_frutos_no_aptos.getText().toString().isEmpty()) {
            Toasty.error(requireContext(),
                    "Debes completar N° frutos aptos y no aptos",
                    Toast.LENGTH_LONG, true).show();
            return;
        }

        String alfaNumerico = getResources().getString(R.string.alfanumericos_con_signos);
        et_observacion.setText(Utilidades.sanitizarString(et_observacion.getText().toString(), alfaNumerico));


        ExecutorService executor = Executors.newSingleThreadExecutor();

        try {

            CheckListRevisionFrutosDetalle cl = new CheckListRevisionFrutosDetalle();

            String claveUnicaDetalle = (clDetalle != null) ? clDetalle.getClave_unica_detalle() : UUID.randomUUID().toString();

            cl.setFrutos_no_aptos(Integer.parseInt(et_frutos_no_aptos.getText().toString()));
            cl.setFrutos_aptos(Integer.parseInt(et_frutos_aptos.getText().toString()));
            cl.setComentario(et_observacion.getText().toString());
            cl.setEstado_sincronizacion(0);
            cl.setClave_unica_detalle(claveUnicaDetalle);

            if (clDetalle != null) {
                cl.setId_cl_revision_frutos_detalle(clDetalle.getId_cl_revision_frutos_detalle());
                cl.setClave_unica_revision_frutos(clDetalle.getClave_unica_revision_frutos());

                executor.submit(() -> MainActivity.myAppDB.DaoCheckListRevisionFrutos().updateclrevisionFrutosDetalle(cl)).get();
            } else {
                executor.submit(() -> MainActivity.myAppDB.DaoCheckListRevisionFrutos().insertDetallesRevFrutos(cl)).get();
            }

            Dialog dialog = getDialog();
            IOnSave.onSave(true);
            if (dialog != null) {
                dialog.dismiss();
            }

        } catch (Exception e) {
            Toasty.error(requireContext(),
                    "No se pudo guardar el detalle " + e.getMessage(),
                    Toast.LENGTH_LONG, true).show();

        }

    }


    public void onCancel() {

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            int with = ViewGroup.LayoutParams.WRAP_CONTENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(with, height);
        }
    }


}
