/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uroflowmetrygui.GUI;

import javax.swing.JPanel;

/**
 *
 * @author mikej
 */
public class CalibrateTrace extends JPanel{
    
    private int chartWidth;
    private int chartHeight;
    
    public CalibrateTrace(int screenWidth, int screenHeight){
        int padding = 100;
        chartWidth = (screenWidth - padding);
        chartHeight = (screenHeight - padding);
        
        initialiseDatasets();
        
        createCalibrationPane();
        
    }
    
    private void initialiseDatasets(){
        
    }
    
    private void createCalibrationPane(){
        
    }
    
    
    
}
