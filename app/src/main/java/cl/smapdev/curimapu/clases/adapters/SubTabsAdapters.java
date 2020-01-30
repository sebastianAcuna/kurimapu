package cl.smapdev.curimapu.clases.adapters;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.contratos.FragmentFlowering;
import cl.smapdev.curimapu.fragments.contratos.FragmentHarvest;
import cl.smapdev.curimapu.fragments.contratos.FragmentResumen;
import cl.smapdev.curimapu.fragments.contratos.FragmentSowing;

public class SubTabsAdapters extends FragmentStatePagerAdapter {

    private String[] tabs;
    private SharedPreferences prefs;

    public SubTabsAdapters(FragmentManager fm, Context context) {
        super(fm);
        tabs = context.getResources().getStringArray(R.array.sub_tabs);

        prefs = context.getSharedPreferences(Utilidades.SHARED_PREFERENCE, Context.MODE_PRIVATE);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                prefs.edit().putInt("tab", position -1).apply();
                return new FragmentResumen();
            case 1:
                prefs.edit().putInt("tab", position -1).apply();
                return new FragmentSowing();
            case 2:
                prefs.edit().putInt("tab", position -1).apply();
                return new FragmentFlowering();
            case 3:
                prefs.edit().putInt("tab", position -1).apply();
                return new FragmentHarvest();
//                return new FragmentNVProducto();
            default:
                return null;
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
