/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uroflowmetrygui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import uroflowmetrygui.GUI.MainWindow;
import uroflowmetrygui.GUI.PatientDataForm;
import uroflowmetrygui.IO.AppSettings;
import uroflowmetrygui.IO.DatasetReader;
import uroflowmetrygui.IO.DatasetWriter;
import uroflowmetrygui.IO.SaveInvestigation;

/**
 *
 * @author mikej
 */
public class UroflowmetryGUI {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        /*
        DatasetWriter dw = new DatasetWriter("test_dir", "ayyo_1");
        
        HashMap<String, String> patTrialData = new HashMap<String, String>();
        
        patTrialData.put("firstName", "M");
        patTrialData.put("lastName", "J");
        patTrialData.put("DOB", "24/08/1995");
        patTrialData.put("Date", "01/01/2000");

        
        dw.createTrialMetadataFile(patTrialData);
        
        
        XYSeries volumeSeries = new XYSeries("Volume");
        XYSeries flowrateSeries = new XYSeries("Flowrate");
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(volumeSeries);
        dataset.addSeries(flowrateSeries);
        
        
        volumeSeries.add(0, 10);
        volumeSeries.add(1, 30);
        volumeSeries.add(2, 40);
        volumeSeries.add(3, 20);
        volumeSeries.add(4, 25);
        volumeSeries.add(77, 35);
        
        flowrateSeries.add(0, 30);
        flowrateSeries.add(1, 60);
        flowrateSeries.add(2, 10);
        flowrateSeries.add(3, 60);
        flowrateSeries.add(4, 35);
        flowrateSeries.add(5, 65);
        
        dw.createTrialCSVFile(volumeSeries, flowrateSeries);
        */
        /*
        
        String trialDir = "C:\\Development_projects\\test_dir";
            
        DatasetReader dr = new DatasetReader(trialDir);
        
        HashMap<String, String> tr = dr.getTrialMetadata();
        
        */
        
        MainWindow window = new MainWindow();
            
        window.setVisible(true);
        
        //AppSettings settings = new AppSettings();

    }
    
}
