package cl.smapdev.curimapu.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Locale;

import cl.smapdev.curimapu.MainActivity;
import cl.smapdev.curimapu.R;

public class FragmentConfigs extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private MainActivity activity = null;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.configuraciones);
        MainActivity a = (MainActivity) getActivity();
        if (a != null){
            activity = a;
        }
    }
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
