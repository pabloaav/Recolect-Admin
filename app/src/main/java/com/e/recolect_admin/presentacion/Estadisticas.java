package com.e.recolect_admin.presentacion;

import androidx.annotation.NonNull;

import com.e.recolect_admin.modelo.IncidenciaPojo;
import com.e.recolect_admin.modelo.UsuarioPojo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Estadisticas implements Busqueda.Usuario, Busqueda.Incidencia, Almacen.Incidencias, Almacen.Usuarios {

    //region Atributos
    private DatabaseReference dbRoot, refNodoIncidencias, refNodoUsuarios;
    //el array donde guardar las cantidades por mes: indice 0 = enero (ene), etc...
    final int[] cantidadIncMes = new int[12];
    //el array donde guardar las cantidades por tipo: indice 0 = vidrio, etc...
    final int[] cantidadIncTipo = new int[4];
    //el array donde guardar las cantidades por tipo: indice 0 = vidrio, etc...
    final int[] cantidadIncEstado = new int[2];
    //Crear la lista de incidencias
    ArrayList<IncidenciaPojo> listaIncidenciaPojos = new ArrayList<>();
    //Crear la lista de usuarios
    ArrayList<UsuarioPojo> listaUsuarioPojos = new ArrayList<>();
    //endregion

    //region Constructor
    public Estadisticas(DatabaseReference dbRoot) {
        this.refNodoIncidencias = dbRoot.child("Incidencias");
        this.refNodoUsuarios = dbRoot.child("Usuarios");
        this.dbRoot = dbRoot;
    }
    //endregion

    //region Sets y Gets

    public int[] getCantidadIncMes() {
        return cantidadIncMes;
    }

    //endregion

    public void doEstadisticasIncidencias() {

        refNodoIncidencias.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    if (!listaIncidenciaPojos.isEmpty()) {
                        listaIncidenciaPojos.clear();
                    }
                    //Contador de cantidad total de incidencias
                    int count = 0;
                    for (DataSnapshot unUsuario : dataSnapshot.getChildren()) {
                        for (DataSnapshot unaIncidencia : unUsuario.getChildren()) {
                            count++;
                            IncidenciaPojo incPojo = unaIncidencia.getValue(IncidenciaPojo.class);
                            listaIncidenciaPojos.add(incPojo);
                        }
                    }
                    //Se arman vectores de estadisticas con datos de las incidencias
                    cantidadMes(listaIncidenciaPojos);
                    cantidadTipo(listaIncidenciaPojos);
                    cantidadPorEstado(listaIncidenciaPojos);
                    cantidadTotal(count);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void doEstadisticasUsuarios() {
        refNodoUsuarios.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    if (!listaUsuarioPojos.isEmpty()) {
                        listaUsuarioPojos.clear();
                    }
                    int cuentaIncPorUsuario = 0;
                    //Leyendo del nodo Usuarios...
                    for (DataSnapshot unUsuario : dataSnapshot.getChildren()) {
                        //Hago un Pojo Usuario
                        UsuarioPojo user = unUsuario.getValue(UsuarioPojo.class);

                        //Si existe el subnodo "incidencias" dentro del usuario
                        if (unUsuario.child("incidencias").exists()) {
                            for (DataSnapshot unaIncidencia : unUsuario.child("incidencias").getChildren()) {
                                cuentaIncPorUsuario++;
                            }
                        }
                        user.setCantidad(cuentaIncPorUsuario);
                        listaUsuarioPojos.add(user);
                    }
                    //Se arman vectores de estadisticas con datos de usuarios
                    cantIncPorUsuario(listaUsuarioPojos);
                    cantidadRegistrados(listaUsuarioPojos);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //region Busqueda Usuarios

    @Override
    public void cantidadRegistrados(ArrayList<UsuarioPojo> lista) {
        guardarRegistrados(lista.size());
    }

    @Override
    public void cantIncPorUsuario(ArrayList<UsuarioPojo> lista) {
        this.guardarIncPorUsuario(lista);
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
    public void cantidadTipo(ArrayList<IncidenciaPojo> lista) {
        for (IncidenciaPojo inc : lista) {
            switch (inc.getTipo()) {
                case "Vidrio":
                    cantidadIncTipo[0]++;
                    break;
                case "Industrial":
                    cantidadIncTipo[1]++;
                    break;
                case "Chatarra":
                    cantidadIncTipo[2]++;
                    break;
                case "Domiciliario":
                    cantidadIncTipo[3]++;
                    break;
            }
        }
        this.guardarTipo(cantidadIncTipo);
    }

    @Override
    public void cantidadPorEstado(ArrayList<IncidenciaPojo> lista) {

        for (IncidenciaPojo inc : lista) {
            String estado = leerEstadoIncidencia(inc.getEstado());
            if (estado.contains("Proceso")) {
                cantidadIncEstado[0]++;
            } else {
                cantidadIncEstado[1]++;
            }
        }
        this.guardarEstado(cantidadIncEstado);
    }

    @Override
    public void cantidadTotal(int cuenta) {
        this.guardarCantidadTotal(cuenta);
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

    private String leerEstadoIncidencia(Map<String, Boolean> p_estado) {
        boolean enProceso = p_estado.get("En Proceso");
        boolean terminado = p_estado.get("Terminado");
        String resultado;
        if (enProceso && terminado) {
            resultado = "Terminado";
        } else {
            resultado = "En Proceso";
        }
        return resultado;
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
    public void guardarTipo(int[] cantidadIncTipo) {
        DatabaseReference refTipos = dbRoot.child("Estadisticas/Incidencias/tipos");

        Map<String, Integer> tipos = new HashMap<>();
        tipos.put("Vidrio", cantidadIncTipo[0]);
        tipos.put("Industrial", cantidadIncTipo[1]);
        tipos.put("Chatarra", cantidadIncTipo[2]);
        tipos.put("Domiciliario", cantidadIncTipo[3]);

        refTipos.setValue(tipos);
    }

    @Override
    public void guardarEstado(int[] cantidadIncEstado) {
        DatabaseReference refEnProceso = dbRoot.child("Estadisticas/Incidencias/enProceso");
        DatabaseReference refTerminado = dbRoot.child("Estadisticas/Incidencias/terminado");
        refEnProceso.setValue(cantidadIncEstado[0]);
        refTerminado.setValue(cantidadIncEstado[1]);
    }

    @Override
    public void guardarCantidadTotal(int cantidad) {
        DatabaseReference refCantidad = dbRoot.child("Estadisticas/Incidencias/cantidad");
        refCantidad.setValue(cantidad);
    }

    //endregion

    //region Almacen Usuarios
    @Override
    public void guardarRegistrados(int cantidadRegistrados) {
        DatabaseReference refUsuarios = dbRoot.child("Estadisticas/Usuarios/registrados");
        refUsuarios.setValue(cantidadRegistrados);
    }

    @Override
    public void guardarIncPorUsuario(ArrayList<UsuarioPojo> lista) {
        DatabaseReference refUsuarios = dbRoot.child("Estadisticas/Usuarios").child("usuarios");
        for (UsuarioPojo user : lista) {
            //Obtenemos la clave de usuario o userId
            String userId = user.getIdUsuario();
            Map<String,Object> cantidadPorUsuario = new HashMap<>();
            cantidadPorUsuario.put("nomape", user.toString());
            cantidadPorUsuario.put("email", user.getEmail());
            cantidadPorUsuario.put("cantInc", user.getCantidad());
            refUsuarios.child(userId).setValue(cantidadPorUsuario);
        }
    }
    //endregion

}
