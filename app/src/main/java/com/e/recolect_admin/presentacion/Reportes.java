package com.e.recolect_admin.presentacion;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Reportes implements Consulta.Incidencia, Consulta.Usuario {

    private DatabaseReference refNodoEstadisticas;
    private Context contexto;

    public Reportes(DatabaseReference dbRoot, Context contexto) {
        this.refNodoEstadisticas = dbRoot.child("Estadisticas");
        this.contexto = contexto;
    }


    //region Consultas sobre Incidencias

    /**
     * Traigo todos los meses en un objeto MesesPojo con un get() por cada mes que devuelve la cantidad de incidencias en ese mes
     */
    @Override
    public void cantidadMes() {
        DatabaseReference ref = refNodoEstadisticas.child("Incidencias/mes");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snap) {
                MesesPojo meses = snap.getValue(MesesPojo.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void cantidadTipo() {
        DatabaseReference ref = refNodoEstadisticas.child("Incidencias/tipos");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snap) {
                TiposPojo tipos = snap.getValue(TiposPojo.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void cantidadEnProceso() {
        DatabaseReference ref = refNodoEstadisticas.child("Incidencias/enProceso");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snap) {
                Integer enProceso = snap.getValue(Integer.class);
//                Toast.makeText(contexto, "Total en proceso es: " + enProceso, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void cantidadTerminado() {
        DatabaseReference ref = refNodoEstadisticas.child("Incidencias/terminado");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snap) {
                Integer terminado = snap.getValue(Integer.class);
                Toast.makeText(contexto, "Total es: " + terminado, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void cantidadTotal() {
        DatabaseReference ref = refNodoEstadisticas.child("Incidencias/cantidad");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snap) {
                Integer total = snap.getValue(Integer.class);
//                Toast.makeText(contexto, "Total es: " + total, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void cantidadMesTipoPrimerSemestre() {

    }

    @Override
    public void cantidadMesTipoSegundoSemestre() {

    }
    //endregion

    //region Consultas sobre Usuarios
    @Override
    public void cantidadRegistrados() {

    }

    @Override
    public void cantidadRegistros() {

    }

    @Override
    public void cantIncPorUsuario() {

    }

    @Override
    public void cantidadUsos() {

    }

    @Override
    public void cantidadLogins() {

    }

    @Override
    public void estadisticasUsuario(final ArrayList<PieEntry> entradasPie, final int[] cantCategorias, final String[] nombreCategorias) {
        DatabaseReference ref = refNodoEstadisticas.child("Usuarios");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snap) {
                if (snap.exists()) {
                    EstadisticasUsuarioPojo estadisticasUsuarioPojo = snap.getValue(EstadisticasUsuarioPojo.class);
                    cantCategorias[0] = estadisticasUsuarioPojo.getLogins();
                    cantCategorias[1] = estadisticasUsuarioPojo.getRegistrados();
                    cantCategorias[2] = estadisticasUsuarioPojo.getRegistros();
                    cantCategorias[3] = estadisticasUsuarioPojo.getUsos();
                    for (int i = 0; i < cantCategorias.length; i++) {
                        entradasPie.add(new PieEntry(cantCategorias[i], nombreCategorias[i]));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //endregion
}
