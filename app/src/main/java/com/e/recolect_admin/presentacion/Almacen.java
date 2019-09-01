package com.e.recolect_admin.presentacion;

import com.e.recolect_admin.modelo.UsuarioPojo;

import java.util.ArrayList;

public interface Almacen {
    interface Usuarios{
        void guardarRegistros();

        void guardarUsos();

        void guardarLogins();

        void guardarRegistrados();

        void guardarIncPorUsuario(ArrayList<UsuarioPojo> listaUsuarioPojos);
    }
    interface Incidencias{
        void guardarMes(int[] cantidadIncMes);

        void guardarTipo(int[] cantidadIncTipo);

        void guardarEstado(int[] cantidadIncEstado);

        void guardarCantidadTotal(int cantidad);
    }
}
