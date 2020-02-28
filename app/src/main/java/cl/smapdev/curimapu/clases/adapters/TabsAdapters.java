package cl.smapdev.curimapu.clases.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.fragments.contratos.FragmentFieldbook;
import cl.smapdev.curimapu.fragments.contratos.FragmentFormVisitas;


public class TabsAdapters extends FragmentStatePagerAdapter {

    private String[] tabs;

    public TabsAdapters(FragmentManager manager, Context context) {
        super(manager);
        tabs = context.getResources().getStringArray(R.array.tabs);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
            default:
                return new FragmentFormVisitas();
            case 1:
                return new FragmentFieldbook();
        }

    }

    @Override
    public int getCount() {
        return tabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
