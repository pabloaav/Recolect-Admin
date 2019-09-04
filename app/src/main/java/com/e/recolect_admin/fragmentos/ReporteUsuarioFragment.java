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

import com.e.recolect_admin.R;
import com.e.recolect_admin.presentacion.EstadisticasUsuarioPojo;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
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
    PieChart chart, chart2;
    private Typeface tfRegular;
    private Typeface tfLight;
    private String[] nombreCategorias = new String[]{"Logins", "Registrados", "Registros", "Usos"};
    private FirebaseDatabase dbRecolectar;
    private DatabaseReference dbRecolectarRoot;
    TextView cargando;
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

        tfRegular = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        tfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_reporte_usuario, container, false);
        //Link del recurso barChart de Incidencias
        chart = vista.findViewById(R.id.usuarios_piechart);
        chart2 = vista.findViewById(R.id.usuarios_piechart2);
        cargando = vista.findViewById(R.id.tv_cargando_datos);
        getActivity().setTitle("Reporte de Usuarios");

        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setCenterTextTypeface(tfLight);
        chart.setCenterText(generarCirucloBlanco());

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.GREEN);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        chart.setOnChartValueSelectedListener(this);

        // entry label styling
        chart.setEntryLabelColor(Color.BLACK);
        chart.setEntryLabelTypeface(tfRegular);
        chart.setEntryLabelTextSize(13f);
        chart.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        setEstadisticasUsuarios();

        return vista;
    }

    private SpannableString generarCirucloBlanco() {

        SpannableString s = new SpannableString("Recolect-Admin\nEstadisticas de Usuarios");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 0, s.length(), 0);
        return s;
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

        ArrayList<PieEntry> entradasPie = new ArrayList<>();
        for (int i = 0; i < cantCategorias.length; i++) {
            entradasPie.add(new PieEntry(cantCategorias[i], nombreCategorias[i]));
        }
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        l.setTextSize(14f);

        PieDataSet dataSet = new PieDataSet(entradasPie, "");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        //dataSet.setSelectionShift(0f);
        cargando.setVisibility(View.INVISIBLE);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTextSize(14f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(tfLight);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }

    //endregion

}
