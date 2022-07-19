package cl.smapdev.curimapu.clases.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cl.smapdev.curimapu.R;

public class NestedCheckListViewHolder extends RecyclerView.ViewHolder {

    private TextView lbl_nested;
    public NestedCheckListViewHolder(@NonNull View itemView) {
        super(itemView);

        lbl_nested = itemView.findViewById(R.id.lbl_nested);
    }


    public void bind( String nestedChecklist ) {

        lbl_nested.setText(nestedChecklist);
    }
}
