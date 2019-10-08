package com.e.recolect_admin.fragmentos;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.e.recolect_admin.MyValueFormatter;
import com.e.recolect_admin.R;
import com.e.recolect_admin.presentacion.EstadisticasUsuarioPojo;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.StackedValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReporteUsuarioFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReporteUsuarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReporteUsuarioFragment extends Fragment implements OnChartValueSelectedListener {
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
    BarChart chart;
    private String[] nombreCategorias = new String[]{"Accesos", "Cuentas Creadas", "Usuarios Activos", "Consultas"};
    private FirebaseDatabase dbRecolectar;
    private DatabaseReference dbRecolectarRoot;

    //endregion

    //region Metodos por default
    public ReporteUsuarioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReporteUsuarioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReporteUsuarioFragment newInstance(String param1, String param2) {
        ReporteUsuarioFragment fragment = new ReporteUsuarioFragment();
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

    //region Metodos

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
        View vista = inflater.inflate(R.layout.fragment_reporte_usuario, container, false);
        //Link del recurso barChart de Usuarios
        chart = vista.findViewById(R.id.usuarios_barchart);

        getActivity().setTitle("Reporte de Usuarios");
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.setOnChartValueSelectedListener(this);
        chart.getDescription().setEnabled(false);
        chart.setMaxVisibleValueCount(60);
        chart.setDragDecelerationFrictionCoef(0.95f);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);

        // change the position of the y-labels
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        chart.getAxisRight().setEnabled(false);

        chart.getAxisLeft().setDrawGridLines(false);

        // add a nice and smooth animation
        chart.animateY(1500, Easing.EaseInOutQuad);

        setEstadisticasUsuarios();

        return vista;
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    private void setEstadisticasUsuarios() {
        final int[] cantidadPorCategoria = new int[4];
        DatabaseReference ref = dbRecolectarRoot.child("Estadisticas/Usuarios");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snap) {
                if (snap.exists()) {
                    EstadisticasUsuarioPojo estadisticasUsuarioPojo = snap.getValue(EstadisticasUsuarioPojo.class);
                    cantidadPorCategoria[0] = estadisticasUsuarioPojo.getLogins();
                    cantidadPorCategoria[1] = estadisticasUsuarioPojo.getRegistrados();
                    cantidadPorCategoria[2] = estadisticasUsuarioPojo.getRegistros();
                    cantidadPorCategoria[3] = estadisticasUsuarioPojo.getUsos();
                    setData(cantidadPorCategoria);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setData(int[] cantCategorias) {

        XAxis xLabels = chart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        xLabels.setGranularityEnabled(true);
        xLabels.setValueFormatter(new IndexAxisValueFormatter());
        ArrayList<BarEntry> entradasBar = new ArrayList<>();
        for (int i = 0; i < cantCategorias.length; i++) {
            entradasBar.add(new BarEntry(i, cantCategorias[i]));
        }
//        Legend l = chart.getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//        l.setOrientation(Legend.LegendOrientation.VERTICAL);
//        l.setDrawInside(false);
//        l.setXEntrySpace(7f);
//        l.setYEntrySpace(0f);
//        l.setYOffset(0f);
//        l.setTextSize(14f);
        Legend legend = chart.getLegend();

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
//
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);
        ArrayList<LegendEntry> entries = new ArrayList<>();
        for (int i = 0; i < nombreCategorias.length; i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = colors.get(i);
            entry.label = nombreCategorias[i];
            entries.add(entry);
        }
        legend.setCustom(entries);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        legend.setXEntrySpace(10f);
        legend.setTextSize(10F);
        legend.setFormSize(6f);
        legend.setFormToTextSpace(2f);
        BarDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(entradasBar);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(entradasBar, "Data Set");
            set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
            set1.setDrawValues(true);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            chart.setData(data);
            data.setValueFormatter(new DefaultValueFormatter(0));
            data.setValueTextColor(Color.BLACK);
            data.setValueTextSize(14f);

        }
        chart.setFitBars(true);
        chart.invalidate();

    }

    //endregion

}
