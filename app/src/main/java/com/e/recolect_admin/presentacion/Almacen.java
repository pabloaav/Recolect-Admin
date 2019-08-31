package com.e.recolect_admin.presentacion;

public interface Almacen {
    interface Usuarios{
        void guardarRegistros();

        void guardarUsos();

        void guardarLogins();

        void guardarRegistrados();

        void guardarIncPorUsuario();
    }
    interface Incidencias{
        void guardarMes();

        void guardarTipo();

        void guardarEnProceso();

        void guardarTerminado();
    }
}
