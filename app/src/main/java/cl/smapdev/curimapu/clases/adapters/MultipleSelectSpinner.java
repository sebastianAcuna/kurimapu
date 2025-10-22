package cl.smapdev.curimapu.clases.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

import androidx.appcompat.widget.AppCompatSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cl.smapdev.curimapu.clases.relaciones.SpinnerItem;

public class MultipleSelectSpinner extends AppCompatSpinner implements
        DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnDismissListener {

    private List<SpinnerItem> items;         // todos los items
    private boolean[] selected;         // seleccionados
    private SpinnerItem defaultText = new SpinnerItem(0, "Todas");
    private ArrayAdapter<SpinnerItem> proxyAdapter;

    public MultipleSelectSpinner(Context context) {
        super(context);
        init();
    }

    public MultipleSelectSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MultipleSelectSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        items = new ArrayList<>();
        proxyAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new ArrayList<SpinnerItem>());
        super.setAdapter(proxyAdapter);
    }

    /**
     * Asigna los items visibles en el diálogo.
     */
    public void setItems(List<SpinnerItem> items) {
        this.items = new ArrayList<>(items);
        selected = new boolean[items.size()];
        // inicializa texto del "spinner" (la vista)
        updateSpinnerText();
    }

    /**
     * Selecciona índices por defecto.
     */
    public void setSelection(int[] indices) {
        if (selected == null) selected = new boolean[items.size()];
        Arrays.fill(selected, false);
        for (int index : indices) {
            if (index >= 0 && index < selected.length) selected[index] = true;
        }
        updateSpinnerText();
    }

    /**
     * Retorna los índices seleccionados.
     */
    public List<Integer> getSelectedIndices() {
        List<Integer> res = new ArrayList<>();
        if (selected == null) return res;
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) res.add(i);
        }
        return res;
    }

    public List<Integer> getSelectedIds() {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                ids.add(items.get(i).getId());
            }
        }
        return ids;
    }


    public List<SpinnerItem> getSelectedItems() {
        List<SpinnerItem> res = new ArrayList<>();
        for (int i : getSelectedIndices()) {
            res.add(items.get(i));
        }
        return res;
    }

    public List<String> getSelectedNames() {
        List<String> res = new ArrayList<>();
        for (int i : getSelectedIndices()) {
            res.add(items.get(i).getNombre());
        }
        return res;
    }

    /**
     * Texto por defecto mostrado cuando no hay selección.
     */
    public void setDefaultText(SpinnerItem text) {
        this.defaultText = text;
        updateSpinnerText();
    }

    @Override
    public boolean performClick() {
        if (items == null || items.isEmpty()) return true;

        String[] itemNames = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            itemNames[i] = items.get(i).getNombre();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(itemNames, selected, this);
        builder.setPositiveButton("OK", (dialog, which) -> {
            updateSpinnerText();
            dialog.dismiss();
        });
        builder.setNegativeButton("CANCELAR", null);
        builder.setOnDismissListener(this);
        builder.show();
        return true;
    }

    // OnMultiChoiceClickListener
    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (selected != null && which >= 0 && which < selected.length) {
            selected[which] = isChecked;
        }
    }

    // OnDismissListener
    @Override
    public void onDismiss(DialogInterface dialog) {
        // actualiza el texto visible del "spinner" cuando se cierra el diálogo
        updateSpinnerText();
    }


    private void updateSpinnerText() {
        StringBuilder sb = new StringBuilder();
        if (selected != null) {
            int count = 0;
            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    if (count > 0) sb.append(", ");
                    sb.append(items.get(i).getNombre()); // ✅
                    count++;
                }
            }
            if (sb.length() == 0) sb.append(defaultText.getNombre());
        } else {
            sb.append(defaultText.getNombre());
        }

        proxyAdapter.clear();
        proxyAdapter.add(new SpinnerItem(0, sb.toString())); // ✅ para mantener el tipo consistente
        proxyAdapter.notifyDataSetChanged();
    }
}