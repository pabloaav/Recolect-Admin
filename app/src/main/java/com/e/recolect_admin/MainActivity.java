package com.e.recolect_admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.e.recolect_admin.fragmentos.GestionarEcopuntoFragment;
import com.e.recolect_admin.fragmentos.GestionarIncidenciaFragment;
import com.e.recolect_admin.fragmentos.InfoReciclajeFragment;
import com.e.recolect_admin.fragmentos.ReporteIncidenciaFragment;
import com.e.recolect_admin.fragmentos.ReporteUsuarioFragment;
import com.e.recolect_admin.presentacion.Estadisticas;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ReporteUsuarioFragment.OnFragmentInteractionListener, GestionarIncidenciaFragment.OnFragmentInteractionListener, GestionarEcopuntoFragment.OnFragmentInteractionListener, ReporteIncidenciaFragment.OnFragmentInteractionListener, InfoReciclajeFragment.OnFragmentInteractionListener, SearchView.OnQueryTextListener {

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

        //Referencias a la base de datos Firebase
        dbRecolectar = FirebaseDatabase.getInstance();
        dbRecolectarRoot = dbRecolectar.getReference();

        //Creamos objeto Estadisticas
        estadisticas = new Estadisticas(dbRecolectarRoot);

        //inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
        //Mostramos el usuario en el SnackBAr
        if (firebaseAuth != null) {
            Snackbar.make(drawer, "Bienvenido: " + firebaseAuth.getCurrentUser().getEmail(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

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
        MenuItem itemBuscar = menu.findItem(R.id.buscador);
        SearchView searchView = (SearchView) itemBuscar.getActionView();
        searchView.setOnQueryTextListener(this);
        itemBuscar.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Fragment fragmento = getSupportFragmentManager().findFragmentByTag("GestionarIncidenciaFragment");
                if (fragmento != null && fragmento.isVisible()) {
                    GestionarIncidenciaFragment gestionar = (GestionarIncidenciaFragment) fragmento;
                    gestionar.llenarConIncidencias();
                }
                return true;
            }
        });

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
        String tag = "";

        if (id == R.id.gestionar_incidencias) {
            //Se reemplaza contenido principal por fragmento gestionar incidencias
            miFragment = new GestionarIncidenciaFragment();
            fragmentSeleccionado = true;
            tag = "GestionarIncidenciaFragment";
        } else if (id == R.id.gestionar_ecopuntos) {
            //Se reemplaza contenido principal por fragmento gestionar ecopuntos
            miFragment = new GestionarEcopuntoFragment();
            fragmentSeleccionado = true;
            tag = "GestionarEcopuntoFragment";
        } else if (id == R.id.reporte_incidencias) {
            //Se reemplaza contenido principal por fragmento reporte incidencias
            miFragment = new ReporteIncidenciaFragment();
            fragmentSeleccionado = true;
            tag = "ReporteIncidenciaFragment";
        } else if (id == R.id.reporte_usuarios) {
            //Se reemplaza contenido principal por fragmento reporte usuarios
            miFragment = new ReporteUsuarioFragment();
            fragmentSeleccionado = true;
            tag = "ReporteUsuarioFragment";
        } else if (id == R.id.cerrar_sesion) {
            //cerrar sesion
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("   Cerrar Sesión");
            dialogo1.setMessage("Estás seguro de que quieres cerrar la sesión ?");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    aceptarCerrarSesion();
                }
            });
            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    dialogo1.dismiss();
                }
            });
            dialogo1.show();

        } else {//Se reemplaza contenido principal por fragmento info reciclaje
            miFragment = new InfoReciclajeFragment();
            fragmentSeleccionado = true;
            tag = "InfoReciclajeFragment";
        }

        //Se hace un cambio (replace) del contenido principal por un fragmento seleccionado
        if (fragmentSeleccionado) {
            getSupportFragmentManager().beginTransaction().replace(R.id.contenido_principal, miFragment, tag).commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void aceptarCerrarSesion() {
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    //Do anything here which needs to be done after signout is complete
                    Intent login = new Intent(MainActivity.this, LoginAdmin.class);
                    login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(login);
                    finish();
                } else {
                }
            }
        };

        //Init and attach
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(authStateListener);

        //Call signOut()
        firebaseAuth.signOut();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Fragment fragmento = getSupportFragmentManager().findFragmentByTag("GestionarIncidenciaFragment");
        if (fragmento != null && fragmento.isVisible()) {
            GestionarIncidenciaFragment gestionar = (GestionarIncidenciaFragment) fragmento;
            gestionar.llenarConTextoBuscar(newText);
        }
        return false;
    }

    //endregion

}
