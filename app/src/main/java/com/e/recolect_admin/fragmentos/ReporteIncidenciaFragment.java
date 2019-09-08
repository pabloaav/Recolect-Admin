package com.e.recolect_admin.fragmentos;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.e.recolect_admin.ColoresBarras;
import com.e.recolect_admin.MyValueFormatter;
import com.e.recolect_admin.R;
import com.e.recolect_admin.presentacion.MesTipoPojo;
import com.e.recolect_admin.presentacion.TiposPojo;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.StackedValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReporteIncidenciaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReporteIncidenciaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReporteIncidenciaFragment extends Fragment implements OnChartValueSelectedListener {

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

    private BarChart chart;
    protected final String[] tipos = new String[]{"Vidrio", "Industrial", "Chatarra", "Domiciliario"};
    //Eje X
    private String[] meses = new String[]{"ene", "feb", "mar", "abr", "may", "jun", "jul", "ago", "sep", "oct", "nov", "dic"};
    private String[] julioDiciembre = new String[]{"jul", "ago", "sep", "oct", "nov", "dic"};
    private String[] eneroJunio = new String[]{"ene", "feb", "mar", "abr", "may", "jun"};

    //Colors
    private int[] colores = new int[]{Color.GREEN, Color.GRAY, Color.RED, Color.BLUE};
    private FirebaseDatabase dbRecolectar;
    private DatabaseReference dbRecolectarRoot;
    private ToggleButton boton_semestre;

    //endregion

    //region Metodos por default
    public ReporteIncidenciaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReporteIncidenciaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReporteIncidenciaFragment newInstance(String param1, String param2) {
        ReporteIncidenciaFragment fragment = new ReporteIncidenciaFragment();
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

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

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

    //region METODOS
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbRecolectar = FirebaseDatabase.getInstance();
        dbRecolectarRoot = dbRecolectar.getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_reporte_incidencia, container, false);
        //Link del recurso barChart de Incidencias
        chart = vista.findViewById(R.id.inc_barras);
        boton_semestre = vista.findViewById(R.id.btn_semestre);
        getActivity().setTitle("Reporte de Incidencias");

        chart.setOnChartValueSelectedListener(this);

        chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(40);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);

        chart.setDrawValueAboveBar(true);
        chart.setHighlightFullBarEnabled(true);

        // change the position of the y-labels
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setValueFormatter(new MyValueFormatter("I"));
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        chart.getAxisRight().setEnabled(false);

        try {
            crearLeyenda();
        } catch (Exception e) {
            e.printStackTrace();
        }

        boton_semestre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    setSegundoSemestre();
                } else {
                    // The toggle is disabled
                    setPrimerSemestre();
                }
            }
        });

        if (boton_semestre.isChecked()) {
            setSegundoSemestre();
        } else {
            setPrimerSemestre();
        }

        return vista;
    }

    private void setPrimerSemestre() {
        DatabaseReference ref = dbRecolectarRoot.child("Estadisticas/Incidencias/mes_tipo");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    MesTipoPojo mesTipo = dataSnapshot.getValue(MesTipoPojo.class);
                    setData(mesTipo, 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setSegundoSemestre() {
        DatabaseReference ref = dbRecolectarRoot.child("Estadisticas/Incidencias/mes_tipo");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    MesTipoPojo mesTipo = dataSnapshot.getValue(MesTipoPojo.class);
                    setData(mesTipo, 2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void crearLeyenda() {
        Legend l = chart.getLegend();
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(6f);
        l.setFormToTextSpace(2f);
        l.setXEntrySpace(2f);
        l.setTextSize(16f);

        ArrayList<LegendEntry> entries = new ArrayList<>();
        for (int i = 0; i < tipos.length; i++) {
            LegendEntry entry = new LegendEntry();
            entry.label = tipos[i];
            //Como los tipos de incidencias y la cantidad de colores son las mismas...:
            entry.formColor = colores[i];
            entries.add(entry);
        }
        l.setCustom(entries);
    }

    private void setData(MesTipoPojo mesTipo, int semestre) {

        //region Atributos
        ArrayList<BarEntry> values = new ArrayList<>();
        BarDataSet set1;
        ArrayList<TiposPojo> primerSemestre = mesTipo.getPrimerSemestre();
        ArrayList<TiposPojo> segundoSemestre = mesTipo.getSegundoSemestre();
        Random aleatorio = new Random(System.currentTimeMillis());
        //endregion

        XAxis xLabels = chart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        xLabels.setGranularityEnabled(true);

        if (semestre == 1) {
            int limiteSuperior = 20;
            //creamos un lote de datos para los meses pasados al incio del proyecto Recolect-Ar
            for (TiposPojo cantidadPorTipo : primerSemestre) {

                float val1 = (float) aleatorio.nextInt(limiteSuperior);
                float val2 = (float) aleatorio.nextInt(limiteSuperior);
                float val3 = (float) aleatorio.nextInt(limiteSuperior);
                float val4 = (float) aleatorio.nextInt(limiteSuperior);
                values.add(new BarEntry(
                        primerSemestre.indexOf(cantidadPorTipo),
                        new float[]{val1, val2, val3, val4}));
            }
            xLabels.setValueFormatter(new IndexAxisValueFormatter(eneroJunio));
        } else {
            for (TiposPojo cantidadPorTipo : segundoSemestre) {
                float val1 = (float) cantidadPorTipo.getVidrio();
                float val2 = (float) cantidadPorTipo.getIndustrial();
                float val3 = (float) cantidadPorTipo.getChatarra();
                float val4 = (float) cantidadPorTipo.getDomiciliario();
                values.add(new BarEntry(
                        segundoSemestre.indexOf(cantidadPorTipo),
                        new float[]{val1, val2, val3, val4}));
            }
            xLabels.setValueFormatter(new IndexAxisValueFormatter(julioDiciembre));
        }
//        for (int i = 0; i < 12; i++) {
//
//            float val1 = (float) (Math.random() * 10) + 10 / 3;
//            float val2 = (float) (Math.random() * 10) + 10 / 3;
//            float val3 = (float) (Math.random() * 10) + 10 / 3;
//            float val4 = (float) (Math.random() * 10) + 10 / 3;
//            values.add(new BarEntry(
//                    i,
//                    new float[]{val1, val2, val3, val4}));
//        }

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "Cantidad de Incidencias por tipo y mes");
            set1.setDrawIcons(false);
            set1.setColors(getColors());
            set1.setStackLabels(new String[]{"Vidrio", "Industrial", "Chatarra", "Domiciliario"});
            set1.setBarBorderWidth(0.3f);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueFormatter(new StackedValueFormatter(true, "", 0));
            data.setValueTextColor(Color.BLACK);
            data.setValueTextSize(14f);
            chart.setData(data);
        }

        chart.setFitBars(true);
        chart.invalidate();
        chart.animateY(1400, Easing.EaseInOutQuad);

    }

    private int[] getColors() {
        // have as many colors as stack-values per entry
        int[] colors = new int[4];

        System.arraycopy(ColoresBarras.MATERIAL_COLORS, 0, colors, 0, 4);

        return colors;
    }

    //endregion
}
