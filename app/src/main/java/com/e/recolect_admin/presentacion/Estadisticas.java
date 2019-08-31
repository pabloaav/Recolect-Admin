package com.e.recolect_admin.presentacion;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.e.recolect_admin.MainActivity;
import com.e.recolect_admin.modelo.IncidenciaPojo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Estadisticas implements Busqueda.Usuario, Busqueda.Incidencia {

    //region Atributos
    private FirebaseDatabase dbRecolectar;
    private DatabaseReference dbRecolectarRoot, refNodoIncidencias;
    //el array donde guardar las cantidades por mes: indice 0 = enero (ene), etc...
    final int[] cantidadIncMes = new int[12];

    //endregion

    //region Constructor
    public Estadisticas() {
        this.dbRecolectar = FirebaseDatabase.getInstance();
        this.dbRecolectarRoot = dbRecolectar.getReference();
        this.refNodoIncidencias = dbRecolectarRoot.child("Incidencias");
        setcantidadIncMes();

    }
    //endregion

    //region Sets y Gets

    public int[] getCantidadIncMes() {
        return cantidadIncMes;
    }

    public void setcantidadIncMes() {
        cantidadMes();
    }

    //endregion

    //region Busqueda Usuarios

    /**
     * Buscar la cantidadIncMes de cuentas de usuarios que se crean
     */
    @Override
    public void cantidadRegistros() {

    }

    @Override
    public void cantidadUsos() {

    }

    @Override
    public void cantidadLogins() {

    }

    @Override
    public void cantidadRegistrados() {

    }

    @Override
    public void cantIncPorUsuario() {

    }

    //endregion

    //region Busqueda Incidencias

    /**
     * Busca en Nodo Incidencias cuantas incidencias hay por cada mes
     */
    @Override
    public void cantidadMes() {

    }

    @Override
    public void cantidadTipo() {

    }

    @Override
    public void cantidadEnProceso() {

    }

    @Override
    public void cantidadTerminado() {

    }

    private int numeroDeMes(String fecha) {
        int mes = 0;
        if (fecha.contains("ene")) {
            mes = 0;
        } else if (fecha.contains("feb")) {
            mes = 1;
        } else if (fecha.contains("mar")) {
            mes = 2;
        } else if (fecha.contains("abr")) {
            mes = 3;
        } else if (fecha.contains("may")) {
            mes = 4;
        } else if (fecha.contains("jun")) {
            mes = 5;
        } else if (fecha.contains("jul")) {
            mes = 6;
        } else if (fecha.contains("ago")) {
            mes = 7;
        } else if (fecha.contains("sep")) {
            mes = 8;
        } else if (fecha.contains("oct")) {
            mes = 9;
        } else if (fecha.contains("nov")) {
            mes = 10;
        } else {
            mes = 11;
        }

        return mes;
    }

    //endregion
}
