package cl.smapdev.curimapu.fragments;

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

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.TabsAdapters;

public class FragmentContratos extends Fragment {

    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return  inflater.inflate(R.layout.fragment_contrato, container, false);


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        viewPager  = (ViewPager) view.findViewById(R.id.view_pager);

        cargarTabs();

    }

    void cargarTabs(){
        viewPager.setAdapter(new TabsAdapters(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),
                Objects.requireNonNull(getContext())));

        TabLayout tabLayout = (TabLayout) Objects.requireNonNull(getView()).findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        if (activity != null){
            activity.updateView(getResources().getString(R.string.app_name), "Contratos");
        }
    }
}
