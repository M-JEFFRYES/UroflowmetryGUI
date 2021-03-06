/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uroflowmetrygui.IO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author mikej
 */
public class DatasetReader {
    
    private String trialDirPath;
    private String csvPath;
    private String metaPath;
    
    private boolean csvFileFound;
    private boolean jsonFileFound;
    
    private HashMap<String, String> trialMetadata;
    private XYSeries volumeSeries;
    private XYSeries flowrateSeries;
    
    
    
    public DatasetReader(String trialDirPath) throws FileNotFoundException {
        
        this.trialDirPath = trialDirPath;
        File f = new File(trialDirPath);
        
        if (f.exists() && f.isDirectory()){
            String[] csvFile = f.list(new MyCSVFilter());
            String[] jsonFile = f.list(new MyJSONFilter());
            
            if (csvFile.length > 0){
                csvPath = Paths.get(trialDirPath,csvFile[0]).toString();
                csvFileFound = true;
            } else {
                csvFileFound = false;
                System.out.println("Trial data file not found in: "+ trialDirPath +" !");
            }
            
            if (jsonFile.length > 0){
                metaPath = Paths.get(trialDirPath,jsonFile[0]).toString();
                jsonFileFound = true;
            } else {
                jsonFileFound = false;
                System.out.println("Trial Metadata file not found in: "+ trialDirPath +" !");
            }
            
        } else {
            System.out.println("Directory: "+ trialDirPath +" -- Not found!");
        }
        
        if (csvFileFound){
            loadTrialCSVFile();
        } else {}
        
        if (jsonFileFound){
            loadTrialJSONFile();
        } else {}
       
    }
    
    private void loadTrialJSONFile(){
        JSONParser parser = new JSONParser();
        
        trialMetadata = new HashMap<String, String>();
        
        try{
            Object obj = parser.parse(new FileReader(metaPath));
            JSONObject jsonObject = (JSONObject) obj;
            
            Set<String> trialData = jsonObject.keySet();
            
            for (String key : trialData){
                trialMetadata.put(key, (String) jsonObject.get(key));
            }
    
        }
        catch(FileNotFoundException e) {e.printStackTrace();}
        catch(IOException e) {e.printStackTrace();}
        //catch(ParseException e) {e.printStackTrace();}
        catch(Exception e) {e.printStackTrace();}
    }
    
    private void loadTrialCSVFile() throws FileNotFoundException{
        
        volumeSeries = new XYSeries("Volume");
        flowrateSeries = new XYSeries("Flowrate");
        
        File csvFile = new File(csvPath);
        
        int lineNo = 0;
        Scanner scanner = new Scanner(csvFile);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] points = line.split(",");
            
            if (lineNo>1){
                volumeSeries.add(Double.parseDouble(points[0]), Double.parseDouble(points[1]));
                flowrateSeries.add(Double.parseDouble(points[0]), Double.parseDouble(points[2]));
            } else {}
            
            lineNo++;
        }
    }
    
    
    public HashMap<String, String> getTrialMetadata(){return trialMetadata;}
    
    public XYSeries getVolumeData(){return volumeSeries;}
    
    public XYSeries getFlowrateData(){return volumeSeries;}
    
    public XYSeriesCollection getDataset(){
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(volumeSeries);
        dataset.addSeries(flowrateSeries);
        return dataset;
    }  
    
}



class MyCSVFilter implements FilenameFilter {
        @Override
        //return true if find a file named "a",change this name according to your file name
        public boolean accept(final File dir, final String name) {
            return (name.endsWith("_UROFLOW.csv"));
        }
}

class MyJSONFilter implements FilenameFilter {
        @Override
        //return true if find a file named "a",change this name according to your file name
        public boolean accept(final File dir, final String name) {
            return (name.endsWith("_UROFLOW_META.json"));
        }
}
