package cl.smapdev.curimapu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.FragmentFichas;
import cl.smapdev.curimapu.fragments.FragmentPrincipal;
import cl.smapdev.curimapu.fragments.FragmentVisitas;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle toogle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null)
            navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        if (savedInstanceState == null){
          /*  if (Objects.equals(preferences.getString("rut_user", ""), "")){
                cambiarFragment(new FragmentLogin(), Helper.FRG_LOGIN);
            }else{*/
                cambiarFragment(new FragmentPrincipal(), Utilidades.FRAGMENT_INICIO);
                navigationView.setCheckedItem(R.id.nv_inicio);
//            }
        }



    }


    public void cambiarFragment(Fragment fragment, String tag){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment,tag)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        Fragment  fragment = null;

        switch (id){
            case R.id.nv_inicio:
                cambiarFragment(new FragmentPrincipal(), Utilidades.FRAGMENT_INICIO);
                break;

            case R.id.nv_fichas:
                cambiarFragment(new FragmentFichas(), Utilidades.FRAGMENT_FICHAS);
                break;
            case R.id.nv_visitas:
                cambiarFragment(new FragmentVisitas(), Utilidades.FRAGMENT_VISITAS);
                break;
            case R.id.nv_salir:
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
