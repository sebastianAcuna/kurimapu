package cl.smapdev.curimapu.clases.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import cl.smapdev.curimapu.R;

public class SpinnerAdapter extends ArrayAdapter<String> {

    private ArrayList<String> objects;
    private Context context;

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);

        this.context = context;
        this.objects = objects;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent, R.layout.spinner_template_view);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent, R.layout.spinner_template_drop_view);
    }


    private View getCustomView(int position, View convertView, ViewGroup parent, int resourceId) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(resourceId, parent, false);
        TextView titulo = row.findViewById(R.id.titulo);
        titulo.setText(objects.get(position));
        return row;
    }
}
