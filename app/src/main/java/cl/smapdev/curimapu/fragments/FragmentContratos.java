package cl.smapdev.curimapu.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
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
    private  MainActivity activity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity a = (MainActivity) getActivity();
        if(a != null) activity = a;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return  inflater.inflate(R.layout.fragment_contrato, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        viewPager  = (ViewPager) view.findViewById(R.id.view_pager);

        new LazyLoad().execute();

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (activity != null){
               // activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fotos_resumen, FragmentFotosResumen.getInstance(0), Utilidades.FRAGMENT_FOTOS_RESUMEN).commit();
            }
        }
    }


    private class LazyLoad extends AsyncTask<Void, Void, Void>{

        private ProgressDialog progressBar;

        @Override
        protected void onPreExecute() {
            progressBar = new ProgressDialog(activity);
            progressBar.setTitle(getResources().getString(R.string.espere));
            progressBar.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (progressBar != null && progressBar.isShowing()){
                progressBar.dismiss();
            }
            cargarTabs();
        }
    }

    private void cargarTabs(){
        viewPager.setAdapter(new TabsAdapters(getChildFragmentManager(),
                Objects.requireNonNull(getContext())));

        TabLayout tabLayout = (TabLayout) activity.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (activity != null){
            activity.updateView(getResources().getString(R.string.app_name), getResources().getString(R.string.subtitles_visit));
        }
    }







}
