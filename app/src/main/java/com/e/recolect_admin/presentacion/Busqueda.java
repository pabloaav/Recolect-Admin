package com.e.recolect_admin.presentacion;

import com.e.recolect_admin.modelo.IncidenciaPojo;

import java.util.ArrayList;

public interface Busqueda {
    interface Usuario {
        void cantidadRegistros();

        void cantidadUsos();

        void cantidadLogins();

        void cantidadRegistrados();

        void cantIncPorUsuario();
    }

    interface Incidencia {
        void cantidadMes(ArrayList<IncidenciaPojo> listaIncidenciaPojos);

        void cantidadTipo();

        void cantidadEnProceso();

        void cantidadTerminado();
    }
}
