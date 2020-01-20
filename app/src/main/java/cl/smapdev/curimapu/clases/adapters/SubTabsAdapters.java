package cl.smapdev.curimapu.clases.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.fragments.FragmentFichas;
import cl.smapdev.curimapu.fragments.FragmentPrincipal;
import cl.smapdev.curimapu.fragments.contratos.FragmentFlowering;
import cl.smapdev.curimapu.fragments.contratos.FragmentHarvest;
import cl.smapdev.curimapu.fragments.contratos.FragmentResumen;
import cl.smapdev.curimapu.fragments.contratos.FragmentSowing;

public class SubTabsAdapters extends FragmentStatePagerAdapter {

    private String[] tabs;

    public SubTabsAdapters(FragmentManager fm, Context context) {
        super(fm);
        tabs = context.getResources().getStringArray(R.array.sub_tabs);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new FragmentResumen();
            case 1:
                return new FragmentSowing();
            case 2:
                return new FragmentFlowering();
            case 3:
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
