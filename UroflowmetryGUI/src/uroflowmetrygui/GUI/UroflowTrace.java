/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uroflowmetrygui.GUI;

import java.awt.Dimension;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author mikej
 */
public class UroflowTrace extends JPanel {
    
    // Chart Variables
    private int chartWidth;
    private int chartHeight;
    private XYSeries volumeSeries;
    private XYSeries flowrateSeries;
    private XYSeriesCollection dataset;
    JFreeChart chart;
    
    // Flowrate calculation variables
    private float flowrate;
    private int seriesLength;
    private int frCalcSamples;
    
    
    public UroflowTrace(int screenWidth, int screenHeight){
        int padding = 100;
        chartWidth = (screenWidth - padding);
        chartHeight = (screenHeight - padding);
        
        initialiseDatasets();
        
        createUroflowGraphArea();
        
    }
    
    private void initialiseDatasets(){
        frCalcSamples = 3; // Number of samples difference between volumes/times used to calculate current flowrate
        flowrate =0; // Start flowrate at zero ml/sec 
        
        volumeSeries = new XYSeries("Volume"); // legend label
        volumeSeries.setMaximumItemCount(1000);
        
        flowrateSeries = new XYSeries("Flowrate"); // legend label
        flowrateSeries.setMaximumItemCount(1000);
       
	dataset = new XYSeriesCollection();
        dataset.addSeries(volumeSeries);
        dataset.addSeries(flowrateSeries);
    }
            
    private void createUroflowGraphArea(){
        
        chart = ChartFactory.createXYLineChart("Uroflowmetry", "Time (seconds)", "Volume (ml)", dataset);
        
        XYPlot xyPlot = (XYPlot) chart.getPlot();
   
        ValueAxis domainAxis = xyPlot.getDomainAxis();
        ValueAxis rangeAxis = xyPlot.getRangeAxis();
       
        //domainAxis.setRange(0.0, 100.0); //X-axis
        //domainAxis.setTickUnit(new NumberTickUnit(100));
        rangeAxis.setRange(0.0, 1000.0); //Y-axis
        //rangeAxis.setTickUnit(new NumberTickUnit(0.05));

        ChartPanel chartPanel = new ChartPanel(chart);
        // Use full screen dimensions to work out plot size
        chartPanel.setPreferredSize(new Dimension(chartWidth, chartHeight));
               
        add(chartPanel);
    }
    
    
    

    
    private void calculateFlowrate(float x1, float x2, float y1, float y2){
        flowrate = (y2-y1)/(x2-x1);
    }
    
    private void updateFlowrateDataset(float time, float volume){
        seriesLength = volumeSeries.getItemCount();
        
        if (time > frCalcSamples){
            calculateFlowrate(
                    volumeSeries.getX(seriesLength-frCalcSamples).floatValue(),
                    time,
                    volumeSeries.getY(seriesLength-frCalcSamples).floatValue(),
                    volume
            );
        } else {}
        
        flowrateSeries.add(time, flowrate);
    }
    
    public void addData(float time, float volume){
        volumeSeries.add(time, volume);
        updateFlowrateDataset(time, volume);
        
        repaint(); // Refresh the plot window
    }
    
 
}
   
