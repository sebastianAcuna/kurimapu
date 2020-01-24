package cl.smapdev.curimapu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Objects;

import cl.smapdev.curimapu.clases.utilidades.Utilidades;
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


    public void cambiarFragment(Fragment fragment, String tag, int animIn, int animOut){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(animIn, animOut)
                .replace(R.id.container, fragment,tag)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        Fragment  fragment = null;

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
}
