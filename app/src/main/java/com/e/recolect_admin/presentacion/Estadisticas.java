package com.e.recolect_admin.presentacion;

import androidx.annotation.NonNull;

import com.e.recolect_admin.modelo.IncidenciaPojo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Estadisticas implements Busqueda.Usuario, Busqueda.Incidencia, Almacen.Incidencias {

    //region Atributos
    private DatabaseReference dbRoot, refNodoIncidencias;
    //el array donde guardar las cantidades por mes: indice 0 = enero (ene), etc...
    final int[] cantidadIncMes = new int[12];
    //Crear la lista de incidencias
    ArrayList<IncidenciaPojo> listaIncidenciaPojos = new ArrayList<>();

    //endregion

    //region Constructor
    public Estadisticas(DatabaseReference dbRoot) {
        this.refNodoIncidencias = dbRoot.child("Incidencias");
        this.dbRoot = dbRoot;
    }
    //endregion

    //region Sets y Gets

    public int[] getCantidadIncMes() {
        return cantidadIncMes;
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
    public void cantidadMes(ArrayList<IncidenciaPojo> lista) {
        for (IncidenciaPojo inc : lista) {
            int indiceMes = numeroDeMes(inc.getFecha());
            cantidadIncMes[indiceMes]++;
        }
        this.guardarMes(cantidadIncMes);
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

    public void armarVectoresEstadisticas() {

        refNodoIncidencias.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (!listaIncidenciaPojos.isEmpty()) {
                        listaIncidenciaPojos.clear();
                    }
                    for (DataSnapshot unUsuario : dataSnapshot.getChildren()) {
                        for (DataSnapshot unaIncidencia : unUsuario.getChildren()) {
                            IncidenciaPojo incPojo = unaIncidencia.getValue(IncidenciaPojo.class);
                            listaIncidenciaPojos.add(incPojo);
                        }
                    }
                    //Se arman vectores de estadisticas con datos de las incidencias
                    cantidadMes(listaIncidenciaPojos);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    //endregion

    //region Almacen Incidencias
    @Override
    public void guardarMes(int[] cantidadIncMes) {
        DatabaseReference refMeses = dbRoot.child("Estadisticas/Incidencias/mes");

        Map<String, Integer> mes = new HashMap<>();
        mes.put("ene", cantidadIncMes[0]);
        mes.put("feb", cantidadIncMes[1]);
        mes.put("mar", cantidadIncMes[2]);
        mes.put("abr", cantidadIncMes[3]);
        mes.put("may", cantidadIncMes[4]);
        mes.put("jun", cantidadIncMes[5]);
        mes.put("jul", cantidadIncMes[6]);
        mes.put("ago", cantidadIncMes[7]);
        mes.put("sep", cantidadIncMes[8]);
        mes.put("oct", cantidadIncMes[9]);
        mes.put("nov", cantidadIncMes[10]);
        mes.put("dic", cantidadIncMes[11]);

        refMeses.setValue(mes);
    }

    @Override
    public void guardarTipo() {

    }

    @Override
    public void guardarEnProceso() {

    }

    @Override
    public void guardarTerminado() {

    }
    //endregion
}
