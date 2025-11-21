package cl.smapdev.curimapu.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;
import cl.smapdev.curimapu.clases.adapters.TabsAdapters;
import cl.smapdev.curimapu.clases.relaciones.AnexoCompleto;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import es.dmoral.toasty.Toasty;

public class FragmentContratos extends Fragment {

    private ViewPager viewPager;
    private MainActivity activity;
    private SharedPreferences prefs;

    private final ExecutorService singleDbExecutor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            activity = (MainActivity) context;
            prefs = activity.getSharedPreferences(Utilidades.SHARED_NAME, Context.MODE_PRIVATE);
        } else {
            throw new RuntimeException(context + " must be MainActivity");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_contrato, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (singleDbExecutor != null && !singleDbExecutor.isShutdown()) {
            singleDbExecutor.shutdown();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        cargarTabs();

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.menu_visitas, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.CREATED);


        singleDbExecutor.execute(() -> {
            AnexoCompleto anexoCompleto = MainActivity.myAppDB.myDao().getAnexoCompletoById(prefs.getString(Utilidades.SHARED_VISIT_ANEXO_ID, ""));

            if (anexoCompleto != null) {
                handler.post(() -> {
                    Utilidades.setToolbar(activity, view, getResources().getString(R.string.app_name), "Anexo " + anexoCompleto.getAnexoContrato().getAnexo_contrato());
                });
            }
        });

    }


    private void cargarTabs() {

        try {
            viewPager.setAdapter(new TabsAdapters(getChildFragmentManager(),
                    Objects.requireNonNull(getContext())));


            TabLayout tabLayout = (TabLayout) activity.findViewById(R.id.tab_layout);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setTabMode(TabLayout.MODE_FIXED);

        } catch (Exception e) {
            Toasty.warning(activity, "Error capturado" + e.getLocalizedMessage(), Toast.LENGTH_SHORT, true).show();
            //new LazyLoad().execute();
        }

    }


}
