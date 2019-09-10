package com.e.recolect_admin.fragmentos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.e.recolect_admin.MainActivity;
import com.e.recolect_admin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GestionarEcopuntoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GestionarEcopuntoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GestionarEcopuntoFragment extends Fragment {
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

    //region Atributos

    private View vista;
    private TextInputEditText nombre, descripcion, ubicacion;
    private TextInputLayout til_campoNombre, til_campoDescripcion, til_campoUbicacion;
    private ProgressDialog dialogCargandoEcoPunto;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbRoot;
    private Button btn_cargarEcoPunto;
    //endregion

    //region Metodos

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Dialogo de aviso de carga
        dialogCargandoEcoPunto = new ProgressDialog(getActivity());
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbRoot = firebaseDatabase.getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_gestionar_ecopunto, container, false);
        //Links de recursos visuales
        nombre = vista.findViewById(R.id.txt_nombre_ecop);
        descripcion = vista.findViewById(R.id.txt_descripcion_ecop);
        ubicacion = vista.findViewById(R.id.txt_ubicacion_ecop);
        til_campoNombre = vista.findViewById(R.id.til_ecop);
        til_campoDescripcion = vista.findViewById(R.id.til_descrip_ecop);
        til_campoUbicacion = vista.findViewById(R.id.til_ubic_ecop);
        btn_cargarEcoPunto = vista.findViewById(R.id.bt_subir_ecop);
        //Escuchar click del boton
        btn_cargarEcoPunto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cargarEcoPunto();
            }
        });
        return vista;
    }


    public void cargarEcoPunto() {
//        Obtenemos el string de cada campo
        String campoNombre = nombre.getText().toString().trim();
        String campoDescripcion = descripcion.getText().toString().trim();
        String campoUbicacion = ubicacion.getText().toString().trim();
//        String campoNombre = "Plaza Libertad";
//        String campoDescripcion = "Se pueden reciclar botellas de vidrio, plástico, pilas,carton y papel";
//        String campoUbicacion = "Hipólito Yrigoyen 2330";

        if (campoNombre.isEmpty()) {
            til_campoNombre.setError("Por Favor, ingrese el nombre del Eco Punto");
        } else if (campoDescripcion.isEmpty()) {
            til_campoDescripcion.setError("Por Favor, ingrese una descripcion del Eco Punto");
        } else if (campoUbicacion.isEmpty()) {
            til_campoUbicacion.setError("Por Favor, ingrese la direccion: calle y numero ");
        } else {
            til_campoNombre.setError(null);
            til_campoDescripcion.setError(null);
            til_campoUbicacion.setError(null);
            //Subimos la informacion
            dialogCargandoEcoPunto.setMessage("Guardando su Eco Punto, un momento por favor...");
            //Crear un objeto HashMap de Eco Punto
            Map<String, Object> ecopunto = new HashMap<>();
            //En el mismo orden de Firebase
            ecopunto.put("nombre", campoNombre);
            ecopunto.put("informacion", campoDescripcion);
            ecopunto.put("ubicacion", campoUbicacion);

            cargarEcoPunto(dialogCargandoEcoPunto, ecopunto);
        }

    }

    public void cargarEcoPunto(final ProgressDialog cargandoEcoPunto, Map<String, Object> ecopunto) {
        cargandoEcoPunto.show();
        DatabaseReference dataRef = dbRoot.child("Ecopuntos");
        dataRef.push().setValue(ecopunto).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                exitoEcoPunto();
                cargandoEcoPunto.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al cargar el Eco Punto" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void exitoEcoPunto() {
        final CharSequence[] opciones = {"Aceptar"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(getContext());

        alertOpciones.setTitle("Su Eco Punto se cargó correctamente");
        alertOpciones.setIcon(R.drawable.ic_ecopunto_subido);
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Aceptar")) {
                    cerrarFragment();
                    dialogInterface.dismiss();
                } else {
                    cerrarFragment();
                    dialogInterface.dismiss();
                }
            }
        });

        alertOpciones.show();
    }

    private void cerrarFragment() {
        getFragmentManager().beginTransaction().remove(this).commit();
    }

    //endregion

    //region Metodos por default
    public GestionarEcopuntoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GestionarEcopuntoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GestionarEcopuntoFragment newInstance(String param1, String param2) {
        GestionarEcopuntoFragment fragment = new GestionarEcopuntoFragment();
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
