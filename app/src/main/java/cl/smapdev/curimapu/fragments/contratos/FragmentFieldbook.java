package cl.smapdev.curimapu.fragments.contratos;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.SubTabsAdapters;
import cl.smapdev.curimapu.clases.adapters.TabsAdapters;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.FragmentContratos;
import es.dmoral.toasty.Toasty;

public class FragmentFieldbook extends Fragment {

    private ViewPager viewPager;
    private MainActivity activity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity a = (MainActivity) getActivity();

        if (a!=null) activity = a;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fieldbook, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager  = (ViewPager) view.findViewById(R.id.view_sub_pager);

//        new LazyLoad().execute();
        cargarTabs();
    }


    private class LazyLoad extends AsyncTask<Void, Void, Void> {

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

        try{


                viewPager.setAdapter(new SubTabsAdapters(getChildFragmentManager(),
                        Objects.requireNonNull(getContext())));

                TabLayout tabLayout = (TabLayout) Objects.requireNonNull(getView()).findViewById(R.id.tab_sub_layout);
                tabLayout.setupWithViewPager(viewPager);
                tabLayout.setTabMode(TabLayout.MODE_FIXED);


        }catch(Exception e){

            Toasty.warning(activity, "Error capturado"+e.getLocalizedMessage(), Toast.LENGTH_SHORT, true).show();
            //new LazyLoad().execute();

        }



    }
}
