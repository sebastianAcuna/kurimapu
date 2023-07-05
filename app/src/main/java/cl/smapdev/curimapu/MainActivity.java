package cl.smapdev.curimapu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import cl.smapdev.curimapu.clases.bd.Migrations;
import cl.smapdev.curimapu.clases.bd.MyAppBD;
import cl.smapdev.curimapu.clases.tablas.Config;
import cl.smapdev.curimapu.clases.tablas.Usuario;
import cl.smapdev.curimapu.clases.utilidades.Utilidades;
import cl.smapdev.curimapu.fragments.FragmentConfigs;
import cl.smapdev.curimapu.fragments.FragmentFichas;
import cl.smapdev.curimapu.fragments.FragmentLogin;
import cl.smapdev.curimapu.fragments.FragmentPrincipal;
import cl.smapdev.curimapu.fragments.FragmentVisitas;
import cl.smapdev.curimapu.fragments.anexoFechas.FragmentAnexoFechas;
import cl.smapdev.curimapu.fragments.checklist.FragmentCheckList;
import cl.smapdev.curimapu.fragments.contratos.FragmentListVisits;
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
        myAppDB = Room.databaseBuilder(getApplicationContext(),MyAppBD.class,Utilidades.NOMBRE_DATABASE).allowMainThreadQueries()
                .addMigrations(Migrations.MIGRATION_1_TO_2).addMigrations(Migrations.MIGRATION_2_TO_3)
                .addMigrations(Migrations.MIGRATION_3_TO_4)
                .addMigrations(Migrations.MIGRATION_4_TO_5)
                .addMigrations(Migrations.MIGRATION_5_TO_6)
                .addMigrations(Migrations.MIGRATION_6_TO_7)
                .addMigrations(Migrations.MIGRATION_7_TO_8)
                .addMigrations(Migrations.MIGRATION_8_TO_9)
                .addMigrations(Migrations.MIGRATION_9_TO_10)
                .addMigrations(Migrations.MIGRATION_10_TO_11)
                .addMigrations(Migrations.MIGRATION_11_TO_12)
                .addMigrations(Migrations.MIGRATION_12_TO_13)
                .addMigrations(Migrations.MIGRATION_13_TO_14)
                .build();



        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (!checkPermission()) {
                requestPermission();
            }
        }

//        importDB();
//        myAppDB.myDao().updateServidor(Utilidades.IP_PRODUCCION);
//        myAppDB.myDao().updateVisitaTest();
//
//        List<Visitas> visitas =  myAppDB.myDao().getVisitasPorSubir();
//        for (Visitas v : visitas){
//            myAppDB.myDao().updateFotoTest();
//        }

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

        Locale locale = new Locale(languageToLoad,languageToLoad2);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config,getResources().getDisplayMetrics());

        boolean aa = prefs.getBoolean("tema", false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getDelegate().setLocalNightMode((!aa) ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.MyTheme_DayNight);
        }

        Config config1 = MainActivity.myAppDB.myDao().getConfig();
        if (config1 != null){
            if (config1.getServidorSeleccionado().length() <= 0){
                config1.setServidorSeleccionado(Utilidades.URL_SERVER_API);
                MainActivity.myAppDB.myDao().updateConfig(config1);
            }
        }else{
            Config config2 = new Config();
            config2.setServidorSeleccionado(Utilidades.URL_SERVER_API);
            MainActivity.myAppDB.myDao().setConfig(config2);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = findViewById(R.id.nav_view);
        if (navigationView != null)
            navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = findViewById(R.id.drawer_layout);

        Config config2 = MainActivity.myAppDB.myDao().getConfig();
        if (config2 != null){
            cambiarNombreUser(config2.getId_usuario());
        }

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

    private void importDB() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data  = Environment.getDataDirectory();

//            if (sd.canWrite()) {
                String  currentDBPath= "//data//" + getPackageName()
                        + "//databases//" + Utilidades.NOMBRE_DATABASE;
                String backupDBPath  = "/20210122112939_backup_curimapu.db";
                File  backupDB= new File(data, currentDBPath);
                File currentDB  = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
//                dst.close();
                Toast.makeText(getBaseContext(), backupDB.toString(),
                        Toast.LENGTH_LONG).show();

            Log.e("DATABASE", backupDBPath);

        } catch (Exception e) {
            Log.e("DATABASE", e.getMessage());
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG)
                    .show();

        }
    }

    public void cambiarNombreUser(int usuario) {
        TextView textView = navigationView.getHeaderView(0).findViewById(R.id.id_usuarios);
        if (textView != null){

            Usuario usuarios = MainActivity.myAppDB.myDao().getUsuarioById(usuario);
            if(usuarios != null){
                textView.setText(usuarios.getUser());
            }


            TextView version = findViewById(R.id.version_app);
            if (version != null){

                Config config = MainActivity.myAppDB.myDao().getConfig();
                String data = "ID: "+config.getId() + " VERSION: " + Utilidades.APPLICATION_VERSION;
                version.setText(data);

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




    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission( this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int result3 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED  && result2 == PackageManager.PERMISSION_GRANTED  && result3 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            }
        }
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

            case R.id.nv_anexo_fecha:
                cambiarFragment(new FragmentAnexoFechas(), Utilidades.FRAGMENT_ANEXO_FICHA, R.anim.slide_in_left, R.anim.slide_out_left);
                break;

            case R.id.nv_curiweb:
                Config conf = MainActivity.myAppDB.myDao().getConfig();
                Uri uri = Uri.parse("https://curiweb.zproduccion.cl/#/acceso-app?apiKey="+Utilidades.KEY_EXPORT+"&userId="+conf.getId_usuario() );
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;

            case R.id.nv_configs:
                cambiarFragment(new FragmentConfigs(), Utilidades.FRAGMENT_WB, R.anim.slide_in_left, R.anim.slide_out_left);
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
        Toolbar toolbar = findViewById(R.id.toolbar);
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

                        preguntarSiQuiereVolver("ATENCION", "SI VUELVES NO GUARDARA LOS CAMBIOS, ESTAS SEGURO QUE DESEAS VOLVER ?");

                        break;

                    case Utilidades.FRAGMENT_CREA_FICHA:
                        cambiarFragment(new FragmentFichas(), Utilidades.FRAGMENT_FICHAS, R.anim.slide_in_right, R.anim.slide_out_right);
                        cambiarNavigation(R.id.nv_fichas);
                        break;
                    case Utilidades.FRAGMENT_CONFIG:
                    case Utilidades.FRAGMENT_FICHAS:
                    case Utilidades.FRAGMENT_VISITAS:
                    case Utilidades.FRAGMENT_ANEXO_FICHA:
                        cambiarFragment(new FragmentPrincipal(), Utilidades.FRAGMENT_INICIO, R.anim.slide_in_right, R.anim.slide_out_right);
                        cambiarNavigation(R.id.nv_inicio);
                    break;

                    case Utilidades.FRAGMENT_CHECKLIST:
                        cambiarFragment(new FragmentListVisits(), Utilidades.FRAGMENT_LIST_VISITS, R.anim.slide_in_right, R.anim.slide_out_right);
                        break;
                    case Utilidades.FRAGMENT_CHECKLIST_SIEMBRA:
                    case Utilidades.FRAGMENT_CHECKLIST_CAPACITACION_SIEMBRA:
                    case Utilidades.FRAGMENT_CHECKLIST_CAPACITACION_COSECHA:
                    case Utilidades.FRAGMENT_CHECKLIST_LIMPIEZA_CAMIONES:
                    case Utilidades.FRAGMENT_CHECKLIST_COSECHA:
                    case Utilidades.FRAGMENT_CHECKLIST_DEVOLUCION_SEMILLA:
                        cambiarFragment(new FragmentCheckList(), Utilidades.FRAGMENT_CHECKLIST, R.anim.slide_in_right, R.anim.slide_out_right);
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

    public void  preguntarSiQuiereVolver ( String title, String message) {
        View viewInfalted = LayoutInflater.from(this).inflate(R.layout.alert_empty, null);

        final AlertDialog builder = new AlertDialog.Builder(this)
                .setView(viewInfalted)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("aceptar", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();

        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                Button c = builder.getButton(AlertDialog.BUTTON_NEGATIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cambiarFragment(new FragmentVisitas(), Utilidades.FRAGMENT_VISITAS, R.anim.slide_in_right, R.anim.slide_out_right);
                        cambiarNavigation(R.id.nv_visitas);
                        builder.dismiss();
                    }
                });
                c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
            }
        });
        builder.setCancelable(false);
        builder.show();
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
