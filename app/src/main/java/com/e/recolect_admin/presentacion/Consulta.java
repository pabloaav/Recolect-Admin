package com.e.recolect_admin.presentacion;

import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

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

        void estadisticasUsuario(ArrayList<PieEntry> entradasPie, int[] cantCategorias, String[] nombreCategorias);
    }
}
