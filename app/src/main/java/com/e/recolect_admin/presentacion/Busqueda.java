package com.e.recolect_admin.presentacion;

import com.e.recolect_admin.modelo.IncidenciaPojo;
import com.e.recolect_admin.modelo.UsuarioPojo;

import java.util.ArrayList;

public interface Busqueda {
    interface Usuario {
        void cantidadRegistros();

        void cantidadUsos();

        void cantidadLogins();

        void cantidadRegistrados();

        void cantIncPorUsuario(ArrayList<UsuarioPojo> listaUsuarioPojos);
    }

    interface Incidencia {
        void cantidadMes(ArrayList<IncidenciaPojo> listaIncidenciaPojos);

        void cantidadTipo(ArrayList<IncidenciaPojo> listaIncidenciaPojos);

        void cantidadPorEstado(ArrayList<IncidenciaPojo> listaIncidenciaPojos);

        void cantidadTotal(int count);
    }
}
