package cl.smapdev.curimapu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

import android.accounts.Account;
import android.accounts.AccountManager;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import cl.smapdev.curimapu.clases.bd.MyAppBD;
import cl.smapdev.curimapu.clases.relaciones.CantidadVisitas;
import cl.smapdev.curimapu.clases.sincronizacion.ServiceSync;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.FragmentConfigs;
import cl.smapdev.curimapu.fragments.FragmentFichas;
import cl.smapdev.curimapu.fragments.FragmentLogin;
import cl.smapdev.curimapu.fragments.FragmentPrincipal;
import cl.smapdev.curimapu.fragments.FragmentVisitas;
import cl.smapdev.curimapu.fragments.servidorFragment;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle toogle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SharedPreferences shared;

    public static MyAppBD myAppDB;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Create the dummy account
//        .addMigrations(Migrations.MIGRATION_1_TO_2,Migrations.MIGRATION_2_TO_3).
        myAppDB = Room.databaseBuilder(getApplicationContext(),MyAppBD.class,"curimapu.db").allowMainThreadQueries().build();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String ss = prefs.getString("lang", "eng");
        String languageToLoad;
        String languageToLoad2 = "";
        if (ss.equals("eng")){
            languageToLoad = "en_US";
        }else{
            languageToLoad  = "es";
            languageToLoad2  = "ES";
        }

        //prefs.edit().putString("lang","eng").apply();

        Locale locale = new Locale(languageToLoad,languageToLoad2);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,getResources().getDisplayMetrics());



        boolean aa = prefs.getBoolean("tema", false);


//        AppCompatDelegate.setDefaultNightMode((!aa) ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getDelegate().setLocalNightMode((!aa) ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.MyTheme_DayNight);
        }

        Config config1 = MainActivity.myAppDB.myDao().getConfig();
        if (config1 != null){
            if (config1.getServidorSeleccionado().length() <= 0){
                config1.setServidorSeleccionado(Utilidades.IP_PRODUCCION);
                MainActivity.myAppDB.myDao().updateConfig(config1);
            }

        }else{
            Config config2 = new Config();
            config2.setServidorSeleccionado(Utilidades.IP_PRODUCCION);
            MainActivity.myAppDB.myDao().setConfig(config2);
        }

        super.onCreate(savedInstanceState);




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
               if (shared.getString(Utilidades.SHARED_SERVER_ID_USER, "").equals("")){
                   cambiarFragment(new servidorFragment(), Utilidades.FRAGMENT_SERVIDOR, R.anim.slide_in_left, R.anim.slide_out_left);
               }else{
                   cambiarFragment(new FragmentPrincipal(), Utilidades.FRAGMENT_INICIO, R.anim.slide_in_left, R.anim.slide_out_left);
                   navigationView.setCheckedItem(R.id.nv_inicio);
               }

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

    public void cambiarFragmentFoto(Fragment fragment, String tag, int animIn, int animOut){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(animIn, animOut)
                .addToBackStack(null)
                .replace(R.id.container, fragment,tag)
                .commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.nv_inicio:
                cambiarFragment(new FragmentPrincipal(), Utilidades.FRAGMENT_INICIO, R.anim.slide_in_left, R.anim.slide_out_left);
                break;

            case R.id.nv_fichas:
                cambiarFragment(new FragmentFichas(), Utilidades.FRAGMENT_FICHAS, R.anim.slide_in_left, R.anim.slide_out_left);
                break;
            case R.id.nv_visitas:
                cambiarFragment(new FragmentVisitas(), Utilidades.FRAGMENT_VISITAS, R.anim.slide_in_left, R.anim.slide_out_left);
                break;

            case R.id.nv_configs:
                cambiarFragment(new FragmentConfigs(), Utilidades.FRAGMENT_CONFIG, R.anim.slide_in_left, R.anim.slide_out_left);
                break;

            case R.id.nv_salir:
                if (shared != null){
                    shared.edit().remove(Utilidades.SHARED_USER).apply();
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
                return "h";
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
            default:
                return "v";

        }
    }

    public void restart(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            recreate();
        }else{
            Intent refresh = new Intent(MainActivity.this, MainActivity.class);
            startActivity(refresh);
            finish();
        }

    }
    public void setLocale(Locale locale) {

        Resources resources = getResources();
        Configuration conf = resources.getConfiguration();
        conf.locale = locale;
        resources.updateConfiguration(conf, resources.getDisplayMetrics());


        if (!locale.equals(Locale.getDefault())) {
            restart();
        }
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            Fragment fragment = getVisibleFragment();
            if (fragment != null && fragment.getTag() != null) {
                switch (fragment.getTag()) {
                    case Utilidades.FRAGMENT_INICIO:
                    case Utilidades.FRAGMENT_LOGIN:
                        salirApp();
                        break;
                    case Utilidades.FRAGMENT_CONTRATOS:
                    case Utilidades.FRAGMENT_LIST_VISITS:
                        cambiarFragment(new FragmentVisitas(), Utilidades.FRAGMENT_VISITAS, R.anim.slide_in_right, R.anim.slide_out_right);
                        cambiarNavigation(R.id.nv_visitas);
                        break;

                    case Utilidades.FRAGMENT_CREA_FICHA:
                        cambiarFragment(new FragmentFichas(), Utilidades.FRAGMENT_FICHAS, R.anim.slide_in_right, R.anim.slide_out_right);
                        cambiarNavigation(R.id.nv_fichas);
                        break;
                    case Utilidades.FRAGMENT_CONFIG:
                    case Utilidades.FRAGMENT_FICHAS:
                    case Utilidades.FRAGMENT_VISITAS:
                        cambiarFragment(new FragmentPrincipal(), Utilidades.FRAGMENT_INICIO, R.anim.slide_in_right, R.anim.slide_out_right);
                        cambiarNavigation(R.id.nv_inicio);
                    break;
                   /* case Utilidades.FRAGMENT_TAKE_PHOTO:

                        break;*/
                    default:
                        super.onBackPressed();
                    break;

                }
            }
        }
    }

    void salirApp(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    public void cambiarNavigation(int id){
        if (navigationView != null){
            navigationView.setCheckedItem(id);
        }
    }
}
