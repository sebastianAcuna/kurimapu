package cl.smapdev.curimapu.fragments.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.AreaDibujo;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import es.dmoral.toasty.Toasty;

public class DialogFirma  extends DialogFragment {

    private Button btn_reiniciar_firma, btn_guardar;
    private ImageView btn_close_dialog;
    private AreaDibujo areaDibujo;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.firma_digital, null);
        builder.setView(view);

        btn_reiniciar_firma = view.findViewById(R.id.btn_reiniciar_firma);
        btn_guardar = view.findViewById(R.id.btn_guardar);
        areaDibujo = (AreaDibujo) view.findViewById(R.id.vista_firma);
        btn_close_dialog = view.findViewById(R.id.btn_close_dialog);

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        btn_reiniciar_firma.setOnClickListener(view -> {
            areaDibujo.reset();
        });


        btn_close_dialog.setOnClickListener(view -> {
            Dialog di = getDialog();
            if (di != null){
                di.dismiss();
            }
        });

        btn_guardar.setOnClickListener(view -> {
            String name  = "TEST_"+Utilidades.fechaActualConHora()
                    .replaceAll(" " ,"")
                    .replaceAll(":", "_")
                    +"__.png";

            boolean guardo = areaDibujo.saveAsImage(name);

            if(!guardo){
                Toasty.success( requireActivity(),
                                "No pudimos guardar tu firma, vuelva a intentarlo",
                                Toast.LENGTH_SHORT, true)
                        .show();
            }


            Dialog di = getDialog();
            if (di != null){
                di.dismiss();
            }
        });

    }
}
