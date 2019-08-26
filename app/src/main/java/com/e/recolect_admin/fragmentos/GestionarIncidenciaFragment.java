package com.e.recolect_admin.fragmentos;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.recolect_admin.R;
import com.e.recolect_admin.adaptadores.AdaptadorRecyclerIncidencias;
import com.e.recolect_admin.modelo.IncidenciaPojo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GestionarIncidenciaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GestionarIncidenciaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GestionarIncidenciaFragment extends Fragment {

    //region Atributos por default
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    //endregion

    //region ATRIBUTOS
    DatabaseReference mDataBase;
    FirebaseAuth mAuth;
    ArrayList<IncidenciaPojo> listaIncidenciaPojos;
    RecyclerView rvIncidencias;
    //endregion

    //region METODOS
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        // Objetos de Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mDataBase = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_gestionar_incidencia, container, false);
        //Tomar el recycler view del layout por medio de su id
        rvIncidencias = vista.findViewById(R.id.rv_gestion_incidencias);
        //Crear la lista de incidencias delcarada en Atributos
        listaIncidenciaPojos = new ArrayList<>();
        //Construir layout manager para el recycle
        construirRecycler(vista);
        //Llenar el recycler con informacion de firebase
        llenarConIncidencias();
        //Retornamos la vista que ha sido inflada
        return vista;
    }

    private void construirRecycler(View vista) {
        //Se necesita un objeto Linear Layaout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        //Se ordenan de mas recientes a menos recientes
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        //Seteamos el recycler con el Layout Manager
        rvIncidencias.setLayoutManager(linearLayoutManager);

    }

    private void llenarConIncidencias() {

        DatabaseReference ref = mDataBase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("incidencias");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if(!listaIncidenciaPojos.isEmpty()){
                        listaIncidenciaPojos.clear();
                    }
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        IncidenciaPojo value = snapshot.getValue(IncidenciaPojo.class);
                        value.setKey(snapshot.getKey());
                        value.setTipo(value.getTipo());
                        value.setFecha(value.getFecha());
                        value.setDescripcion(value.getDescripcion());
                        value.setDireccion(value.getDireccion());
                        value.setImagen(value.getImagen());
                        Map<String, Object> ubicacion = value.getUbicacion();
                        value.setCadenaUbicacion(value.getCadenaUbicacion());
                        value.setEstado(value.getEstado());
                        listaIncidenciaPojos.add(value);
                    }
                }
                //Creamos el adaptador de Incidencias
                AdaptadorRecyclerIncidencias adaptador = new AdaptadorRecyclerIncidencias(listaIncidenciaPojos, R.layout.cv_admin_incidencia, getActivity());

                //Le decimos al adaptador que escuche y haga algo cuando se hace click
                adaptador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            IncidenciaPojo incidenciaPojo = listaIncidenciaPojos.get(rvIncidencias.getChildAdapterPosition(view));
                            mostrarDialogoCambio(incidenciaPojo);
                        }
                    }
                });

                //Seteamos el adaptador al recycler
                rvIncidencias.setAdapter(adaptador);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void mostrarDialogoCambio(final IncidenciaPojo incidencia) {
        //Atributos locales
        String tipo = incidencia.getTipo();
        String ubicacion = incidencia.getCadenaUbicacion();
        String fecha = incidencia.getFecha();
        CharSequence estado = leerEstadoIncidencia(incidencia.getEstado());
        String mensaje = "Estado: " + estado + "\n" + "Tipo: " + tipo +
                "\n" + "Ubicacion: " + ubicacion + "\n" + "Fecha: " + fecha;

        // 1. Instantiate an AlertDialog.Builder with its constructor
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(mensaje)
                .setTitle("Cambiar el estado de la Incidencia");

        // Add the buttons
        builder.setPositiveButton("CAMBIAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                cambiarEstado(incidencia);
            }
        });
        builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                Toast.makeText(getContext(), "La incidencia NO cambió de estado", Toast.LENGTH_LONG).show();
            }
        });

        builder.show();
    }

    private void cambiarEstado(IncidenciaPojo inc) {

        String claveIncidencia = inc.getKey();
        DatabaseReference ref = mDataBase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("incidencias").child(claveIncidencia).child("estado");
        ref.setValue(hashMapCambairEstado())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Se cambio el estado correctamente", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error desde base de dats", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private CharSequence leerEstadoIncidencia(Map<String, Boolean> p_estado) {
        boolean enProceso = p_estado.get("En Proceso");
        boolean terminado = p_estado.get("Terminado");
        CharSequence resultado = "resultado";
        if (enProceso && terminado) {
            resultado = "Terminado";
        } else {
            resultado = "En Proceso";
        }
        return resultado;
    }

    private Map<String, Boolean> hashMapCambairEstado() {
        boolean enProceso = true;
        boolean terminado = true;

        //Ahora el objeto hashMap para subir a la base de datos
        Map<String, Boolean> nodoEstado = new HashMap<>();
        //En el mismo orden de Firebase
        nodoEstado.put("En Proceso", enProceso);
        nodoEstado.put("Terminado", terminado);

        return nodoEstado;
    }

    //endregion

    //region Metodos por default
    public GestionarIncidenciaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GestionarIncidenciaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GestionarIncidenciaFragment newInstance(String param1, String param2) {
        GestionarIncidenciaFragment fragment = new GestionarIncidenciaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    //endregion
}
