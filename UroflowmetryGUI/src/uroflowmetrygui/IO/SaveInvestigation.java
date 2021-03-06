/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uroflowmetrygui.IO;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author mikej
 */
public class SaveInvestigation {
    
    private XYSeriesCollection investigationData;
    private boolean dataAdded;
    List<String[]> dataLines;
    
    public SaveInvestigation() throws IOException{
        dataAdded = false;
        
        
        dataLines = new ArrayList<>();
        dataLines.add(new String[] { "John", "Doe", "38", "Comment Data\nAnother line of comment data" });
        dataLines.add(new String[] { "Jane", "Doe, Jr.", "19", "She said \"I'm being quoted\"" });
        
        System.out.println("ex data");
        
        saveToCSV("C:\\Development_tools\\patient.csv");
    }
    
    public void loadInvestigationData(XYSeriesCollection dataset){
        investigationData = dataset;
        dataAdded = true;
    }
    
    public void saveInvestigation(String path){
        if (dataAdded){
            System.out.print("need to save here: "+path);

        } else {
            System.out.print("Load invetiagtion data");
        }
    }
    
    public String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
}
    
    private void saveToCSV(String csvPath) throws IOException {
        File csvOutputFile = new File(csvPath);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        }
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
