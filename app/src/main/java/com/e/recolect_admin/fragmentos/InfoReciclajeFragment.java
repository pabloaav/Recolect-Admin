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
 * {@link InfoReciclajeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InfoReciclajeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoReciclajeFragment extends Fragment {
    //region Atributos por defecto
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
    private TextInputEditText contenido, url, nombreWeb;
    private TextInputLayout til_contenido, til_url, til_nombreWeb;
    DatabaseReference dbRoot;
    private Button btn_cargarInfoUtil;
    private ProgressDialog dialogCargandoInfo;
    private FirebaseDatabase firebaseDatabase;
    //endregion

    //region Metodos
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogCargandoInfo = new ProgressDialog(getActivity());
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbRoot = firebaseDatabase.getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_info_reciclaje, container, false);
        //Link de recursos
        til_contenido = vista.findViewById(R.id.til_contenido);
        til_url = vista.findViewById(R.id.til_url);
        til_nombreWeb = vista.findViewById(R.id.til_nombre_web);
        contenido = vista.findViewById(R.id.txt_contenido);
        url = vista.findViewById(R.id.txt_url);
        nombreWeb = vista.findViewById(R.id.txt_nombre_web);
        btn_cargarInfoUtil = vista.findViewById(R.id.bt_subir_web);
        //Escuchar click del boton
        btn_cargarInfoUtil.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                preCargaInfoUtil();
            }
        });
        return vista;
    }

    private void preCargaInfoUtil() {
        // Obtenemos el string de cada campo
        String nombreDeLaWeb = nombreWeb.getText().toString().trim();
        String contenidoDeLaWeb = contenido.getText().toString().trim();
        String urlDeLaWeb = url.getText().toString().trim();
//        HardCode para para pruebas rapidas
        /*String nombreDeLaWeb = "inforeciclaje";
        String contenidoDeLaWeb = "reciclaje con información y propuestas para reciclar todo tipo de objetos y materiales, facilitando puntos de reciclaje o ecoparques, mostrando los procesos de los diferentes tipos de reciclaje";
        String urlDeLaWeb = "http://www.inforeciclaje.com/reciclaje-vidrio.php";*/

        if (nombreDeLaWeb.isEmpty()) {
            til_nombreWeb.setError("Por Favor, ingrese el nombre de la Web");
        } else if (contenidoDeLaWeb.isEmpty()) {
            til_contenido.setError("Por Favor, ingrese el contenido de la Web");
        } else if (urlDeLaWeb.isEmpty()) {
            til_url.setError("Por Favor, ingrese la URL de la Web ");
        } else {
            til_nombreWeb.setError(null);
            til_contenido.setError(null);
            til_url.setError(null);
            //Subimos la informacion
            dialogCargandoInfo.setMessage("Guardando la Web de Reciclaje, un momento por favor...");
            //Crear un objeto HashMap de Eco Punto
            Map<String, Object> webReciclaje = new HashMap<>();
            //En el mismo orden de Firebase
            webReciclaje.put("nombre", nombreDeLaWeb);
            webReciclaje.put("descripcion", contenidoDeLaWeb);
            webReciclaje.put("enlace", urlDeLaWeb);

            cargarInfoUtil(dialogCargandoInfo, webReciclaje);
        }
    }

    private void cargarInfoUtil(final ProgressDialog dialogo, Map<String, Object> webReciclaje) {
        dialogo.show();
        DatabaseReference dataRef = dbRoot.child("Reciclaje");
        dataRef.push().setValue(webReciclaje).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                exitoInfoUtil();
                dialogo.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error al cargar la información de reciclaje" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void exitoInfoUtil() {
        final CharSequence[] opciones = {"Aceptar"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(getContext());

        alertOpciones.setTitle("Se guardó la información correctamente");
        alertOpciones.setIcon(R.drawable.ic_info_reciclaje_subido);
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

    //region Metodos por defecto
    public InfoReciclajeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoReciclajeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoReciclajeFragment newInstance(String param1, String param2) {
        InfoReciclajeFragment fragment = new InfoReciclajeFragment();
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
