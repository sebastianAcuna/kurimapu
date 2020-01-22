package cl.smapdev.curimapu.clases.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import cl.smapdev.curimapu.R;

public class SpinnerToolbarAdapter extends ArrayAdapter<String> {

    private String[] objects;
    private Context context;


    public SpinnerToolbarAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
        super(context, resource,  objects);


        this.context = context;
        this.objects = objects;
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
        TextView titulo = (TextView) row.findViewById(R.id.titulo);
        titulo.setText(objects[position]);
        return row;
    }
}
