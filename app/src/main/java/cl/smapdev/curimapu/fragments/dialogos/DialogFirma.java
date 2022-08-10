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

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.AreaDibujo;
import cl.smapdev.curimapu.clases.temporales.TempFirmas;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import es.dmoral.toasty.Toasty;

public class DialogFirma  extends DialogFragment {

    private Button btn_reiniciar_firma, btn_guardar;
    private ImageView btn_close_dialog;
    private AreaDibujo areaDibujo;


    public interface IOnSave {
        void onSave(boolean isSaved);
    }


    private IOnSave iOnSave;
    private int tipoDocumento;
    private String nombreFirma;
    private String lugar;

    public static DialogFirma newInstance(
            int tipoDocumento,
            String nombreFirma,
            String lugar,
            IOnSave iOnSave
    ){

        DialogFirma df = new DialogFirma();

        df.setNombreFirma( nombreFirma );
        df.setTipoDocumento( tipoDocumento );
        df.setIOnSave( iOnSave );
        df.setLugar( lugar );

        return df;

    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public void setIOnSave(IOnSave iOnSave){
        this.iOnSave = iOnSave;
    }

    public void setTipoDocumento(int tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public void setNombreFirma(String nombreFirma) {
        this.nombreFirma = nombreFirma;
    }

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

            Dialog di = getDialog();
            String savePath = areaDibujo.saveAsImage(this.nombreFirma);

            if(savePath.isEmpty()){
                Toasty.success( requireActivity(),
                                "No pudimos guardar tu firma, vuelva a intentarlo",
                                Toast.LENGTH_SHORT, true)
                        .show();
                iOnSave.onSave(false);

                if (di != null){ di.dismiss(); }
                return;

            }

            ExecutorService exe = Executors.newSingleThreadExecutor();

            TempFirmas tempFirmas = new TempFirmas();
            tempFirmas.setTipo_documento( this.tipoDocumento );
            tempFirmas.setPath( savePath );
            tempFirmas.setLugar_firma( this.lugar );
            exe.submit(() -> MainActivity.myAppDB.DaoFirmas().insertFirma(tempFirmas));

            iOnSave.onSave(true);

            if (di != null){
                di.dismiss();
            }
        });

    }
}
