/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uroflowmetrygui.IO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jfree.data.xy.XYSeries;
import org.json.simple.JSONObject;

/**
 *
 * @author mikej
 */
public class DatasetWriter {
    
    private String TrialReference;
    
    private Path trialDirPath;
    private Path csvPath;
    private Path metaPath;
    
    private boolean trialDirMade;
    private boolean trialCSVMade;
    private boolean trialCSVTagMade;
    
    private List<String[]> dataLines;
    
    
    
    public DatasetWriter(String DataDirectory, String TrialReference) throws IOException{
        
        this.TrialReference = TrialReference;
        trialDirPath = Paths.get(DataDirectory);
        csvPath = Paths.get(trialDirPath.toString(), (TrialReference+"_UROFLOW.csv"));
        metaPath = Paths.get(trialDirPath.toString(), (TrialReference+"_UROFLOW_META.json"));
        
        trialDirMade = checkTrialExists(DataDirectory); // Create trial file directory, named { }_new if one with same exists
    }
    
    private boolean checkTrialExists(String DataDirectory) throws IOException{
        
        File f = new File(DataDirectory);
        
        
        if (!f.exists()){ 
            Files.createDirectory(trialDirPath);
            return true;
        } else {
            boolean dirExists = true;
            int dirVersion = 1;
            
            while(dirExists){
                trialDirPath = Paths.get(DataDirectory+"_"+dirVersion); 
                csvPath = Paths.get(trialDirPath.toString(), (TrialReference+"_UFLOW.csv"));
                metaPath = Paths.get(trialDirPath.toString(), (TrialReference+"_UFLOW_META.json"));
 
                f = new File(trialDirPath.toString());
                if (!f.exists()){
                    Files.createDirectory(trialDirPath);
                    dirExists = false;
                } else{
                    dirVersion++;
                }
            }
            return true;
        }
    }
    
    public void createTrialMetadataFile(HashMap<String, String> TrialMetadata){
        
        JSONObject obj = new JSONObject();
        
        for ( String key : TrialMetadata.keySet() ) {
            //System.out.println( key );
            String value = TrialMetadata.get(key);
            obj.put(key, value);
        }
        
        try (FileWriter file = new FileWriter(metaPath.toString()))
        {
            file.write(obj.toString());
            file.flush();
            trialCSVTagMade = true;
        }
        catch (IOException e) {
            e.printStackTrace();
            trialCSVTagMade = false;
        }
    }
    
    public void createTrialCSVFile(XYSeries volumeSeries, XYSeries flowrateSeries) throws IOException{
        
        // Get 1d arrays of the variables
        double[] times = volumeSeries.toArray()[0];
        double[] vols = volumeSeries.toArray()[1];
        double[] flows = flowrateSeries.toArray()[1];
        
        dataLines = new ArrayList<>();
        dataLines.add(new String[] {"Time","Volume","Flowrate"});
        
        for(int i=0; i<times.length; i++){
            //System.out.println(times[i]+","+flows[i]+","+vols[i]);
            dataLines.add(new String[] {String.valueOf(times[i]), String.valueOf(vols[i]), String.valueOf(flows[i])});
        }
        
        saveToCSV(csvPath.toString());
        
        trialCSVMade = true;
    }
    
    private void saveToCSV(String csvPath) throws IOException {
        File csvOutputFile = new File(csvPath);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        }
    }
    
    private String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }
    
    private String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
    
    
    
    
    
}
