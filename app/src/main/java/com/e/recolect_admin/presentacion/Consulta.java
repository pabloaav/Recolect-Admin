package com.e.recolect_admin.presentacion;

public interface Consulta {

    interface Incidencia {
        void cantidadMes();

        void cantidadTipo();

        void cantidadEnProceso();

        void cantidadTerminado();

        void cantidadTotal();
    }

    interface Usuario {
        void cantidadRegistrados();

        void cantidadRegistros();

        void cantIncPorUsuario();

        void cantidadUsos();

        void cantidadLogins();
    }
}
