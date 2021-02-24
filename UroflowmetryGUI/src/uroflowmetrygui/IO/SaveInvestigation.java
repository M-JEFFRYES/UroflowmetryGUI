/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uroflowmetrygui.IO;

import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author mikej
 */
public class SaveInvestigation {
    
    private XYSeriesCollection investigationData;
    private boolean dataAdded;
    
    public SaveInvestigation(){
        dataAdded = false;
        
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
    
    
    
}
