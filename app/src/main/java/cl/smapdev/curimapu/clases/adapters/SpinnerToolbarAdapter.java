package cl.smapdev.curimapu.clases.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.tablas.Temporada;

public class SpinnerToolbarAdapter extends ArrayAdapter<Temporada> {

    private final List<Temporada> objects;
    private final Context context;

    public SpinnerToolbarAdapter(@NonNull Context context, int resource, @NonNull List<Temporada> objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position,convertView,parent, R.layout.spinner_template_toolbar_view);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position,convertView,parent, R.layout.spinner_template_toolbar_drop_view);
    }


    private View getCustomView(int position, View convertView, ViewGroup parent, int resourceId){

        LayoutInflater inflater=(LayoutInflater) context.getSystemService(  Context.LAYOUT_INFLATER_SERVICE );
        View row=inflater.inflate(resourceId, parent, false);
        TextView titulo = row.findViewById(R.id.titulo);
        titulo.setText(objects.get(position).getNombre_tempo());
        return row;
    }
}
