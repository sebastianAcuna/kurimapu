package cl.smapdev.curimapu.fragments.contratos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.SubTabsAdapters;
import cl.smapdev.curimapu.clases.adapters.TabsAdapters;

public class FragmentFieldbook extends Fragment {

    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fieldbook, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager  = (ViewPager) view.findViewById(R.id.view_sub_pager);

        cargarTabs();
    }


    void cargarTabs(){
        viewPager.setAdapter(new SubTabsAdapters(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),
                Objects.requireNonNull(getContext())));

        TabLayout tabLayout = (TabLayout) Objects.requireNonNull(getView()).findViewById(R.id.tab_sub_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);


    }
}
