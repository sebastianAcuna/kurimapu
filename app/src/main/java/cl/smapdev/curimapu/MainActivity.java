package cl.smapdev.curimapu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.Surface;
import android.view.WindowManager;

import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.FragmentConfigs;
import cl.smapdev.curimapu.fragments.FragmentFichas;
import cl.smapdev.curimapu.fragments.FragmentLogin;
import cl.smapdev.curimapu.fragments.FragmentPrincipal;
import cl.smapdev.curimapu.fragments.FragmentVisitas;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle toogle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SharedPreferences shared;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String ss = prefs.getString("lang", "eng");
        String languageToLoad = "";
        String languageToLoad2 = "";
        if (ss.equals("eng")){
            languageToLoad = "en_US";
        }else{
            languageToLoad  = "es";
            languageToLoad2  = "ES";
        }

        Locale locale = new Locale(languageToLoad,languageToLoad2);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());



        boolean aa = prefs.getBoolean("tema", false);

        if(!aa){
            setTheme(R.style.AppTheme);

        }else{
            setTheme(R.style.ThemeSecundary);
        }

        setContentView(R.layout.activity_main);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null)
            navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);





        shared = getSharedPreferences(Utilidades.SHARED_NAME, MODE_PRIVATE);





        if (savedInstanceState == null){
           if (Objects.equals(shared.getString(Utilidades.SHARED_USER, ""), "")){
                cambiarFragment(new FragmentLogin(), Utilidades.FRAGMENT_LOGIN, R.anim.slide_in_left, R.anim.slide_out_left);
            }else{
                cambiarFragment(new FragmentPrincipal(), Utilidades.FRAGMENT_INICIO, R.anim.slide_in_left, R.anim.slide_out_left);
                navigationView.setCheckedItem(R.id.nv_inicio);
            }
        }


    }



    public void setDrawerEnabled(boolean enabled) {
        int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED :
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        if (drawerLayout!=null) drawerLayout.setDrawerLockMode(lockMode);
        if (toogle != null) toogle.setDrawerIndicatorEnabled(enabled);
    }


    public Fragment getVisibleFragment(){

        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for(Fragment fragment : fragments){
            if(fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public android.app.Fragment getVisibleFragmentPreference(){

        android.app.FragmentManager fragmentManager = getFragmentManager();

        List<android.app.Fragment> fragments = fragmentManager.getFragments();
        for(android.app.Fragment fragment : fragments){
            if(fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    public void cambiarFragment(Fragment fragment, String tag, int animIn, int animOut){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(animIn, animOut)
                .replace(R.id.container, fragment,tag)
                .commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        Fragment  fragment = null;

        switch (id){
            case R.id.nv_inicio:
                if (getVisibleFragmentPreference() != null && getVisibleFragmentPreference().getTag().equals(Utilidades.FRAGMENT_CONFIG)){
                    getFragmentManager().beginTransaction().remove(getVisibleFragmentPreference()).commit();
                }
                cambiarFragment(new FragmentPrincipal(), Utilidades.FRAGMENT_INICIO, R.anim.slide_in_left, R.anim.slide_out_left);
                break;

            case R.id.nv_fichas:
                if (getVisibleFragmentPreference() != null && getVisibleFragmentPreference().getTag().equals(Utilidades.FRAGMENT_CONFIG)){
                    getFragmentManager().beginTransaction().remove(getVisibleFragmentPreference()).commit();
                }
                cambiarFragment(new FragmentFichas(), Utilidades.FRAGMENT_FICHAS, R.anim.slide_in_left, R.anim.slide_out_left);
                break;
            case R.id.nv_visitas:


                if (getVisibleFragmentPreference() != null && getVisibleFragmentPreference().getTag().equals(Utilidades.FRAGMENT_CONFIG)){
                    getFragmentManager().beginTransaction().remove(getVisibleFragmentPreference()).commit();
                }
                cambiarFragment(new FragmentVisitas(), Utilidades.FRAGMENT_VISITAS, R.anim.slide_in_left, R.anim.slide_out_left);
                break;

            case R.id.nv_configs:

                if (!getVisibleFragment().getTag().equals(Utilidades.FRAGMENT_CONFIG)){
                    getSupportFragmentManager().beginTransaction().remove(getVisibleFragment()).commit();
                }

                getFragmentManager().beginTransaction().replace(R.id.container, new FragmentConfigs(),Utilidades.FRAGMENT_CONFIG).commit();
                break;
            case R.id.nv_salir:
                if (shared != null){
                    shared.edit().remove(Utilidades.SHARED_USER).apply();
                    if (getVisibleFragmentPreference() != null && getVisibleFragmentPreference().getTag().equals(Utilidades.FRAGMENT_CONFIG)){
                        getFragmentManager().beginTransaction().remove(getVisibleFragmentPreference()).commit();
                    }
                    cambiarFragment(new FragmentLogin(), Utilidades.FRAGMENT_LOGIN, R.anim.slide_in_right, R.anim.slide_out_right);
                }

                break;
        }

        navigationView.setCheckedItem(id);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    public void updateView(String title, String subtitle) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null){
            toolbar.setTitle(title);
            toolbar.setSubtitle(subtitle);
        }
        setSupportActionBar(toolbar);

        toogle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
    }


    public String getRotation(Context context){
        final int rotation = ((WindowManager) Objects.requireNonNull(context.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                return "v";
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
            default:
                return "h";
        }
    }

    public void restart(){
        Intent refresh = new Intent(MainActivity.this, MainActivity.class);
        startActivity(refresh);
        finish();
    }
    public void setLocale(Locale locale) {

//        Locale.setDefault(locale);

        Resources resources = getResources();
        Configuration conf = resources.getConfiguration();
        conf.locale = locale;
        resources.updateConfiguration(conf, resources.getDisplayMetrics());


//        Resources res = getResources();
//        Configuration config = new Configuration(res.getConfiguration());
//        config.setLocale(locale);
//        createConfigurationContext(config);

        if (!locale.equals(Locale.getDefault())) {

            Intent refresh = new Intent(MainActivity.this, MainActivity.class);
            startActivity(refresh);
            finish();
        }
    }
}
