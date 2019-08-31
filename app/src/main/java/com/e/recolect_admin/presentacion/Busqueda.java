package com.e.recolect_admin.presentacion;

public interface Busqueda {
    interface Usuario {
        void cantidadRegistros();

        void cantidadUsos();

        void cantidadLogins();

        void cantidadRegistrados();

        void cantIncPorUsuario();
    }

    interface Incidencia {
        int cantidadMes();

        void cantidadTipo();

        void cantidadEnProceso();

        void cantidadTerminado();
    }
}
