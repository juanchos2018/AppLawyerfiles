package com.document.lawyerfiles.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.airbnb.lottie.L;
import com.document.lawyerfiles.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

public class Grafico1Activity extends AppCompatActivity {


    private PieChart pieChart;
    private BarChart barChart;

    private String month[]= new String[]{"Enero","Febreo","Marzo","Abril","Mayo" };
    private int[] sale =new int[]{24,20,38,10,15};
    private int[] color = new int[]{Color.BLACK,Color.GRAY,Color.GREEN, Color.RED , Color.CYAN};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico1);


        barChart=(BarChart)findViewById(R.id.graficobarra);
        pieChart=(PieChart)findViewById(R.id.graficopie);

        createChart();


    }

    private Chart getSameChart(Chart chart,String descrpcion,int texcolor,int backgrodcolor,int animatey ){
        chart.getDescription().setText(descrpcion);
        chart.getDescription().setTextSize(15);
        chart.setBackgroundColor(backgrodcolor);
        chart.animateY(animatey);
        legent(chart);
        return  chart;

    }

     private  void  legent(Chart chart){
         Legend legend=chart.getLegend();
         legend.setForm(Legend.LegendForm.CIRCLE);
         legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);

         ArrayList<LegendEntry> entries  =new ArrayList<>();
         for (int i =0;i<month.length;i++){
             LegendEntry entry=new LegendEntry();
             entry.formColor=color[i];
             entry.label=month[i];
             entries.add(entry);

         }

         legend.setCustom(entries);

     }

     private  ArrayList<BarEntry> getBarEntryes(){
         ArrayList<BarEntry> entries  =new ArrayList<>();
         for (int i =0;i<sale.length;i++)
            entries.add(new BarEntry(i,sale[i]));

         return  entries;


    }
    private  ArrayList<PieEntry> getPieEntryes(){
        ArrayList<PieEntry> entries  =new ArrayList<>();
        for (int i =0;i<sale.length;i++)
            entries.add(new PieEntry(sale[i]));

        return  entries;
    }

    private  void  EjeX(XAxis axis){
        axis.setGranularityEnabled(true);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setValueFormatter(new IndexAxisValueFormatter(month));
    }

    private  void asixLeft(YAxis axis){
        axis.setSpaceTop(30);
        axis.setAxisMinimum(0);
    }
    private  void asixRight(YAxis axis){
        axis.setEnabled(true);
   //     axis.setAxisMinimum(0);
    }

    public  void  createChart(){
        barChart=(BarChart)getSameChart(barChart,"series",Color.RED,Color.CYAN,3000);
         barChart.setDrawGridBackground(true);
         barChart.setDrawBarShadow(true);

         barChart.setData(getbarData());
         barChart.invalidate();
        EjeX(barChart.getXAxis());
        asixLeft(barChart.getAxisLeft());
        asixRight(barChart.getAxisRight());

        pieChart=(PieChart)getSameChart(pieChart,"ventas",Color.GRAY,Color.MAGENTA,3000);
        pieChart.setHoleRadius(10);
        pieChart.setTransparentCircleRadius(12);
        pieChart.setData(getpieData());
        pieChart.invalidate();

        //pieChart.setDrawHoleEnabled(false);


    }

    private DataSet getData(DataSet dataSet){

        dataSet.setColors(color);
        dataSet.setValueTextSize(Color.WHITE);
        dataSet.setValueTextSize(10);
        return dataSet;
    }
    private BarData getbarData(){
        BarDataSet barDataset=(BarDataSet)getData(new BarDataSet(getBarEntryes(),""));
        barDataset.setBarShadowColor(Color.GRAY);
        BarData barData= new BarData(barDataset);
        barData.setBarWidth(0.45f);
        return  barData;

    }

    private PieData getpieData(){
        PieDataSet pieDataset=(PieDataSet)getData(new PieDataSet(getPieEntryes(),""));

        pieDataset.setSliceSpace(2);
        pieDataset.setValueFormatter(new PercentFormatter());
        return  new PieData(pieDataset);

    }


}
