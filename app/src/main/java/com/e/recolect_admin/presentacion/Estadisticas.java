package com.e.recolect_admin.presentacion;

public class Estadisticas implements Busqueda.Usuario, Busqueda.Incidencia {

    //region Constructor
    public Estadisticas() {
    }
    //endregion

    //region Busqueda Usuarios

    /**
     * Buscar la cantidad de cuentas de usuarios que se crean
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
    @Override
    public int cantidadMes() {
        int cantidad = 0;

        return cantidad;
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
    //endregion
}
