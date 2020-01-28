package cl.smapdev.curimapu.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import androidx.annotation.Nullable;

import java.util.Locale;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;

public class FragmentConfigs extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private MainActivity activity = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.configuraciones);

        MainActivity a = (MainActivity) getActivity();
        if (a != null){
            activity = a;
        }
    }

/*    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        // calculate margins
        int horizontalMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        int verticalMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        int topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50 + 30, getResources().getDisplayMetrics());

        view.setPadding(horizontalMargin, topMargin, horizontalMargin, verticalMargin);
        return view;
    }*/


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        if (activity != null){
            if (s.equals("lang")){
                String id = sharedPreferences.getString(s,"eng");
                switch (id){
                    case "eng":
                        Locale localeEN = new Locale("en_US");
                        activity.setLocale(localeEN);
                        break;
                    case "esp":
                        Locale localeES = new Locale("es", "ES");
                        activity.setLocale(localeES);
                        break;
                }
            }else if(s.equals("tema")){

                activity.restart();

            }
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }




}
