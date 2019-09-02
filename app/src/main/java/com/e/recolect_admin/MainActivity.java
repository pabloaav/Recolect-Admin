package com.e.recolect_admin;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.e.recolect_admin.fragmentos.GestionarEcopuntoFragment;
import com.e.recolect_admin.fragmentos.GestionarIncidenciaFragment;
import com.e.recolect_admin.fragmentos.ReporteIncidenciaFragment;
import com.e.recolect_admin.fragmentos.ReporteUsuarioFragment;
import com.e.recolect_admin.presentacion.Estadisticas;
import com.e.recolect_admin.presentacion.Reportes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ReporteUsuarioFragment.OnFragmentInteractionListener, GestionarIncidenciaFragment.OnFragmentInteractionListener, GestionarEcopuntoFragment.OnFragmentInteractionListener, ReporteIncidenciaFragment.OnFragmentInteractionListener {

    //region Atributos
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase dbRecolectar;
    private DatabaseReference dbRecolectarRoot;
    Estadisticas estadisticas;
    //endregion

    //region Metodos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //FloatingActionButton fab = findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                .setAction("Action", null).show();
        //    }
        //});
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        inicializarFirebase();

        doLoginWhitEmailPassword("administrador@gmail.com", "admin2019");

        actualizarEstadisticas();
    }

    private void actualizarEstadisticas() {
        estadisticas.doEstadisticasIncidencias();
        estadisticas.doEstadisticasUsuarios();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Se toma el di del item que presiona el usuario
        int id = item.getItemId();
        // Variable tipo Fragment para hacer visible el fragmento que elige el usuario
        Fragment miFragment = null;
        boolean fragmentSeleccionado = false;

        if (id == R.id.gestionar_incidencias) {
            //Se reemplaza contenido principal por fragmento gestionar incidencias
            miFragment = new GestionarIncidenciaFragment();
            fragmentSeleccionado = true;
        } else if (id == R.id.gestionar_ecopuntos) {
            //Se reemplaza contenido principal por fragmento gestionar ecopuntos
            miFragment = new GestionarEcopuntoFragment();
            fragmentSeleccionado = true;
        } else if (id == R.id.reporte_incidencias) {
            //Se reemplaza contenido principal por fragmento reporte incidencias
            miFragment = new ReporteIncidenciaFragment();
            fragmentSeleccionado = true;
        } else if (id == R.id.reporte_usuarios) {
            //Se reemplaza contenido principal por fragmento reporte incidencias
            miFragment = new ReporteUsuarioFragment();
            fragmentSeleccionado = true;
        }

        //Se hace un cambio (replace) del contenido principal por un fragmento seleccionado
        if (fragmentSeleccionado) {
            getSupportFragmentManager().beginTransaction().replace(R.id.contenido_principal, miFragment).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if user is signed in (non-null) and update UI accordingly.
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    View vista = findViewById(R.id.drawer_layout);
                    Snackbar.make(vista, "Bienvenido Administrador: " + currentUser.getEmail(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Toast.makeText(MainActivity.this, "No hay usuario", Toast.LENGTH_SHORT).show();
                }
            }

        }, 2000);
    }//Fin de onStart()

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        //inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
        dbRecolectar = FirebaseDatabase.getInstance();
        dbRecolectarRoot = dbRecolectar.getReference();
        estadisticas = new Estadisticas(dbRecolectarRoot);
    }

    public void doLoginWhitEmailPassword(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                        } else {
                            Toast.makeText(MainActivity.this, "Falló la autenticación. Verifique los datos",
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Reportes reportes = new Reportes(dbRecolectarRoot, this);
        reportes.cantidadTerminado();
    }

    //endregion

}
