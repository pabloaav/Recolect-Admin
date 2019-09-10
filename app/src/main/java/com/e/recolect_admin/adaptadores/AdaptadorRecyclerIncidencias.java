package com.e.recolect_admin.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.e.recolect_admin.R;
import com.e.recolect_admin.modelo.IncidenciaPojo;
import com.e.recolect_admin.modelo.UsuarioPojo;

import java.util.ArrayList;
import java.util.Map;

/**
 * La clase AdaptadorRecyclerIncidencias se encarga de sincronizar, vincular o asociar un arraylist (lista de objetos), con los elementos de cada Cardview, mostrandolos a medida que se solicitan los datos
 * <p>
 * Recibe una colecccion de objetos de tipo ViewHolder, que es la clase Inne declarada para manipular los objetos Incidencia
 */
public class AdaptadorRecyclerIncidencias extends RecyclerView.Adapter<AdaptadorRecyclerIncidencias.IncidenciasViewHolder> implements View.OnClickListener {

    //region ATRIBUTOS de AdaptadorRecyclerIncidencias

    //El arreglo de objetos Incidencia
    ArrayList<IncidenciaPojo> listaIncidencias;

    //El layout del cardview donde se define que mostrar en pantalla y como
    int cardViewLayout;

    //El Context, que es la Activity desde donde se llama a mostrar los datos
    Context mContext;

    //Un escuchador para el evento click
    private View.OnClickListener listener;

    //endregion

    //Constructor
    public AdaptadorRecyclerIncidencias(ArrayList<IncidenciaPojo> listaIncidencias, int cardViewLayout, Context mContext) {
        this.listaIncidencias = listaIncidencias;
        this.cardViewLayout = cardViewLayout;
        this.mContext = mContext;
    }

    //Este metodo toma los campos del cardview incidencias, y le asigna un valor determinado, que corresponde a cada objeto del arreglo de incidencias que viene dado com parametro de la clase AdaptadorRecyclerIncidencias
    @Override
    public void onBindViewHolder(final IncidenciasViewHolder incidenciasViewHolder, int position) {
        //En cada posicion del array hay un objeto incidencias.
        IncidenciaPojo incidencia = listaIncidencias.get(position);

        //De cada incidencia del modelo de datos, que se alimenta de la base de datos, tomamos os valores de los atributos de cada objeto, y se los pasamos a la vista, para que en cada atributo del ViewHolder muestre su correspondiente valor
        incidenciasViewHolder.tipo.setText(incidencia.getTipo());
        //Quitamos los segundos a la fecha y hora de la incidencia
        String fechaSinSegundos = quitarSegundos(incidencia.getFecha());
        incidenciasViewHolder.fecha.setText(fechaSinSegundos);
        //Agregamos la palabra "Descripción: " a la descripcion en tiempo de ejecucion
        String descripcion = "Descripción:  " + incidencia.getDescripcion();
        incidenciasViewHolder.descripcion.setText(descripcion);
//        incidenciasViewHolder.direccion.setText(incidencia.getDireccion());
        //Quitamos el state (Estado=Provincia) de la cadena de ubicacion
        String cadenaSinProvincia = quitarProvincia(incidencia.getCadenaUbicacion());
        incidenciasViewHolder.ubicacion.setText(cadenaSinProvincia);
        //Tomamos el estado de la base de datos
        incidenciasViewHolder.estado.setText(leerEstadoIncidencia(incidencia.getEstado()));
        incidenciasViewHolder.usuario.setText(incidencia.getUsuario());
//        incidenciasViewHolder.imagen.setImageResource(R.drawable.basura);
        String imagen = incidencia.getImagen();
//cargar foto Firebase
        Glide
                .with(mContext)
                .load(imagen)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        incidenciasViewHolder.progressBar.setVisibility(View.GONE);
                        incidenciasViewHolder.imagen.setVisibility(View.VISIBLE);
                        incidenciasViewHolder.imagen.setImageResource(R.drawable.icono_error);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        incidenciasViewHolder.progressBar.setVisibility(View.GONE);
                        incidenciasViewHolder.imagen.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .placeholder(R.drawable.icono_sin_foto)
                .error(R.drawable.icono_sin_foto)
                .into(incidenciasViewHolder.imagen);

    }//Fin de OnBind...

    //Este metodo inicializa la vista del ViewHolder
    @Override
    public IncidenciasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Decimos que desde el Context (la Activity Menu) inflar la vista de los cardview
        View vista = LayoutInflater.from(mContext).inflate(cardViewLayout, parent, false);

        //Seteamos a la vista el evento onclick para saber cuando el administrador elige un cardview
        vista.setOnClickListener(this);

        //Le pasamos esta vista, es decir, el layout del cardview, a la Clase IncidenciasViewHolder, para que haga la asociacion de datos que le corresponde
        return new IncidenciasViewHolder(vista);
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

    private String quitarSegundos(String fechaCompleta) {
        //Buscamos la ultima ocurrencia de un caracter en un string o cadena
        int dosPuntos = fechaCompleta.lastIndexOf(":");
        //Extraemos un substring de un string, desde el indice 0 hasta el indice obtenido arriba
        return fechaCompleta.substring(0, dosPuntos);
    }

    private String quitarProvincia(String cadenaUbicacionCompleta) {
        //Buscamos la ultima ocurrencia de un caracter en un string o cadena
        int coma = cadenaUbicacionCompleta.lastIndexOf(",");
        //Extraemos un substring de un string, desde el indice 0 hasta el indice obtenido arriba
        return cadenaUbicacionCompleta.substring(0, coma);
    }

    @Override
    public int getItemCount() {
        if (listaIncidencias.size() > 0) {
            return listaIncidencias.size();
        } else {
            return 0;
        }
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    /**
     * Esta es la Clase Inner que se encarga de ligar los elementos del layout Cardview
     * que muestran los datos traidos de la base de datos
     */
    public class IncidenciasViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imagen;
        private TextView tipo;
        private TextView fecha;
        private TextView descripcion;
        //private TextView direccion;
        private TextView ubicacion;
        private ProgressBar progressBar;
        private TextView estado;
        private TextView usuario;

        /**
         * Constructor: cada dato de una incidencia, se asocia con los los elementos de la vista del cardview representados por su nombre de id que figuran en el layout Cardview
         *
         * @param itemView: Es el Cardview donde estan los elementos que vamos a asociar con los datos traidos de Firebase
         */
        public IncidenciasViewHolder(@NonNull final View itemView) {
            super(itemView);
            //Cada elemento del view (los cardview de incidencias) debe ser clicable
            itemView.setOnClickListener(this);
//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//
//                    return false;
//                }
//            });

            //Aqui en el constructor, asociamos los valores de la incidencia tomados del cardview a sus atributos, declarados mas arriba, luego de la firma de la Clase Inner IncidenciasViewHolder
            imagen = itemView.findViewById(R.id.cv_imagen);
            tipo = itemView.findViewById(R.id.cv_tipo);
            fecha = itemView.findViewById(R.id.cv_fecha);
            descripcion = itemView.findViewById(R.id.cv_descripcion);
//            direccion = itemView.findViewById(R.id.cv_direccion);
            ubicacion = itemView.findViewById(R.id.cv_ubicacion);
            estado = itemView.findViewById(R.id.cv_estado);
            usuario =itemView.findViewById(R.id.cv_nombre_usuario);
            progressBar = itemView.findViewById(R.id.progressBar);
        }

        //Al hacer clic en un cardview debe llevarnos a una vista ampliada
        @Override
        public void onClick(View view) {
            //Vista correspondiente al click en el cardview de incidencia
            hacerAlgoEnClick(view);
        }

        private void hacerAlgoEnClick(View v) {
            if (listener != null) {
                listener.onClick(v);
            }
        }
    }
}
