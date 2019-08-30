package com.e.recolect_admin.fragmentos;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.e.recolect_admin.MainActivity;
import com.e.recolect_admin.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;

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
    private PieChart pieIncidencias;
    protected final String[] parties = new String[]{"Vidrio", "Domiciliario", "Industrial", "Chatarra"};
    private int[] sale = new int[]{25, 20, 38, 10, 15};
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_reporte_incidencia, container, false);

        //Link del recurso pieChart de Incidencias
        pieIncidencias = vista.findViewById(R.id.graf_pie_incidencias);
        pieIncidencias.setUsePercentValues(true);
        pieIncidencias.getDescription().setEnabled(false);
        pieIncidencias.setExtraOffsets(5, 10, 5, 5);
        pieIncidencias.setDragDecelerationFrictionCoef(0.95f);

        pieIncidencias.setCenterText(generateCenterSpannableText());

        pieIncidencias.setDrawHoleEnabled(true);
        pieIncidencias.setHoleColor(Color.WHITE);

        pieIncidencias.setTransparentCircleColor(Color.WHITE);
        pieIncidencias.setTransparentCircleAlpha(110);

        pieIncidencias.setHoleRadius(58f);
        pieIncidencias.setTransparentCircleRadius(61f);

        pieIncidencias.setDrawCenterText(true);

        pieIncidencias.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieIncidencias.setRotationEnabled(true);
        pieIncidencias.setHighlightPerTapEnabled(true);
        pieIncidencias.animateY(1400, Easing.EaseInOutQuad);

        Legend l = pieIncidencias.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        pieIncidencias.setEntryLabelColor(Color.WHITE);
        pieIncidencias.setEntryLabelTextSize(12f);
        pieIncidencias.animateY(1400, Easing.EaseInOutQuad);
        setData();
        return vista;
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Recolect-Admin\nReporte de Incidencias");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    private void setData() {


        PieDataSet dataSet = new PieDataSet(getPieEntries(), "Tipos de Incidencia");

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

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieIncidencias));
        data.setValueTextSize(18f);
        data.setValueTextColor(Color.WHITE);
        pieIncidencias.setData(data);

        // undo all highlights
        pieIncidencias.highlightValues(null);

        pieIncidencias.invalidate();
    }

    private ArrayList<PieEntry> getPieEntries() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < sale.length; i++)
            entries.add(new PieEntry(sale[i]));
        return entries;
    }
    //endregion
}
