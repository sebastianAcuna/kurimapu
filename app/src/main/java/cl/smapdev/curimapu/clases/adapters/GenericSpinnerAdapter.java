package cl.smapdev.curimapu.clases.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class GenericSpinnerAdapter<T extends SpinnerItem> extends ArrayAdapter<T> {

    private final Context context;
    private final List<T> items;

    public GenericSpinnerAdapter(Context context, List<T> items) {
        super(context, android.R.layout.simple_spinner_item, items);
        this.context = context;
        this.items = items;
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setText(items.get(position).getDescripcion());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setText(items.get(position).getDescripcion());
        return label;
    }

    public T getItemById(int id) {
        for (T item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }
}