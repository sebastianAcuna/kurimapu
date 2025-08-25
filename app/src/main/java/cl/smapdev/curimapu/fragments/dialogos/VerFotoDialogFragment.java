package cl.smapdev.curimapu.fragments.dialogos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.File;

import cl.smapdev.curimapu.R;

public class VerFotoDialogFragment extends DialogFragment {

    private static final String ARG_PATH = "arg_path";

    private File archivoFoto;
    private int posicion;
    private OnEliminarFotoListener eliminarListener;

    public interface OnEliminarFotoListener {
        void onEliminar(int posicion);
    }

    public static VerFotoDialogFragment newInstance(File archivo, int posicion) {
        VerFotoDialogFragment fragment = new VerFotoDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PATH, archivo.getAbsolutePath());
        args.putInt("posicion", posicion);
        fragment.setArguments(args);
        return fragment;
    }

    public void setOnEliminarFotoListener(OnEliminarFotoListener listener) {
        this.eliminarListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_ver_foto, container, false);

        ImageView imgFotoGrande = view.findViewById(R.id.img_foto_grande);
        Button btnEliminar = view.findViewById(R.id.btn_eliminar);

        if (getArguments() != null) {
            String path = getArguments().getString(ARG_PATH);
            posicion = getArguments().getInt("posicion");
            archivoFoto = new File(path);

            if (archivoFoto.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(archivoFoto.getAbsolutePath());
                imgFotoGrande.setImageBitmap(bitmap);
            }
        }

        btnEliminar.setOnClickListener(v -> {
            if (eliminarListener != null) {
                eliminarListener.onEliminar(posicion);
            }
            dismiss();
        });

        return view;
    }
}