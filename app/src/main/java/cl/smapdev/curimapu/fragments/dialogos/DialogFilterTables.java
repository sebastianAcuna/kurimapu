package cl.smapdev.curimapu.fragments.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import cl.smapdev.curimapu.R;

public class DialogFilterTables extends DialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();

        View view = inflater.inflate(R.layout.dialogo_filtros_tabla, null);

        builder.setView(view);



        return builder.create();
    }



}
