/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uroflowmetrygui.IO;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author mikej
 */
public class ConversionFactors {
    
    private float gradient;
    private float yIntercept;
    private float[] factors;
    
    private String jsonPath;
    
    
    public ConversionFactors(){
        jsonPath = "UroflowConversionFactors.json";
        factors = new float[2];
        
        initialiseFactors();
    }
    
    public void updateConversionFactorsFilepath(String path){jsonPath=path;}
    
    private void initialiseFactors(){
        gradient = 1;
        yIntercept =0;
        factors[0] = gradient;
        factors[1] = yIntercept;
    }
    
    public void updateFactors(float gradient, float yIntercept){
        this.gradient = gradient;
        this.yIntercept = yIntercept;
        factors[0] = gradient;
        factors[1] = yIntercept;
    }
    
    public float[] getConversionFactors(){return factors;}
    
    public void saveConversionFactor(){
        JSONObject obj = new JSONObject();
        obj.put("loadcellGradient", gradient);
        obj.put("loadcellYIntercept", yIntercept);
        
        try (FileWriter file = new FileWriter(jsonPath))
        {
            file.write(obj.toString());
            file.flush();
        }
        catch (IOException e) {e.printStackTrace();}
        
        System.out.println(obj);
    }
    
    public void loadConversionFactors(){
        JSONParser parser = new JSONParser();
        try{
            Object obj = parser.parse(new FileReader(jsonPath));
            JSONObject jsonObject = (JSONObject) obj;
            Double g = (Double) jsonObject.get("loadcellGradient");
            Double y = (Double) jsonObject.get("loadcellYIntercept");
            
            gradient = g.floatValue();
            yIntercept = y.floatValue();     
        }
        catch(FileNotFoundException e) {e.printStackTrace();}
        catch(IOException e) {e.printStackTrace();}
        //catch(ParseException e) {e.printStackTrace();}
        catch(Exception e) {e.printStackTrace();}
    }
    
}
