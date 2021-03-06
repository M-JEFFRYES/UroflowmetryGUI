/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uroflowmetrygui.GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author mikej
 */
public class PatientDataForm extends JFrame {
    
    private JPanel form;
    private GridBagConstraints c;
    
    public boolean patInfoAdded;
    public boolean infoSent;
    private HashMap<String, String> patInformation;
    
    ArrayList<JLabel> labels;
    String[] fieldLabels;
    ArrayList<JTextField> textFields;
   
    private JLabel invDateLAB;
    private JLabel invDate;
    
    private JLabel patNameLAB;
    private JLabel patSurnameLAB;
    private JLabel patDOBLAB;
    private JLabel patHospitalNoLAB;
    private JLabel clinicianNameLAB;
    
    private JTextField patNameEntry;
    private JTextField patSurnameEntry;
    private JTextField patDOBEntry;
    private JTextField patHospitalNoEntry;
    private JTextField clinicianNameEntry;
    
    public JButton submitBTN;
    
    
    
    
    
    public PatientDataForm(){
        super();
        setTitle("Enter Patient Data");
        setSize(500, 400);
        initLayout();
       
        initFields();
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    
    private void initLayout(){
        // frame layout
        setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx =10;
        c.ipady = 10;
        
    }
    
    private void initFields(){
        
        patInfoAdded = false;
        infoSent = false;
        
        createAppData();
        
        // Create arrays to loop 
        fieldLabels = new String[]{"FirstName","LastName","DOB","HospitalNo","AttendingClinician"};
        labels = new ArrayList<JLabel>();
        textFields = new ArrayList<JTextField>();
       
        patNameLAB = new JLabel("Patient First Name");
        patNameEntry =  new JTextField();
        labels.add(patNameLAB);
        textFields.add(patNameEntry);
        
        patSurnameLAB = new JLabel("Patient Last Name");
        patSurnameEntry =  new JTextField();
        labels.add(patSurnameLAB);
        textFields.add(patSurnameEntry);
        
        patDOBLAB = new JLabel("Patient DOB");
        patDOBEntry =  new JTextField();
        labels.add(patDOBLAB);
        textFields.add(patDOBEntry);
        
        patHospitalNoLAB = new JLabel("Patient Hospital Number");
        patHospitalNoEntry =  new JTextField();
        labels.add(patHospitalNoLAB);
        textFields.add(patHospitalNoEntry);
        
        clinicianNameLAB = new JLabel("Clinician Performing Investigation");
        clinicianNameEntry =  new JTextField();
        labels.add(clinicianNameLAB);
        textFields.add(clinicianNameEntry);
        
        for (int i=0; i<(textFields.size());i++){
            System.out.println(labels.get(i).getText());
            addFormField(labels.get(i), textFields.get(i), (i+1), 0);
        }
        
        createSubmitButton();
    }
    
    private void createAppData(){
        invDateLAB = new JLabel("Investigation Date");
        
        LocalDate date = LocalDate.now(); 
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String dateToday = date.format(formatter);

        invDate = new JLabel(dateToday);
        c.gridx = 0;
        c.gridy = 0;
        add(invDateLAB, c);
        c.gridx = (1);
        add(invDate, c);
    }
    
    private void createSubmitButton(){
        submitBTN = new JButton("Record Patient Information");
        c.gridx=0;
        c.gridy=(labels.size()+2);
        add(submitBTN, c);
        
        setSubmitBtnFunction();
    }
    
    private void addFormField(JLabel label, JTextField textField, int row, int col){
        c.gridx = col;
        c.gridy = row;
        add(label, c);
        c.gridx = (col+1);
        textField.setPreferredSize(new Dimension(200, 30)); // width, height
        add(textField, c);
    }
    
    private void setSubmitBtnFunction(){
        submitBTN.addActionListener(new ActionListener(){
            @Override public void actionPerformed(ActionEvent arg0) {
		
                if (submitBTN.getText().equals("Record Patient Information")){
                    
                    // methods to check each field

                    loadFormData();

                    patNameEntry.setEnabled(false);
                    patSurnameEntry.setEnabled(false);
                    patDOBEntry.setEnabled(false);
                    patHospitalNoEntry.setEnabled(false);
                    clinicianNameEntry.setEnabled(false);

                    patInfoAdded = true;
                    submitBTN.setText("Add Information to trial");
                    
                } /*else if (submitBTN.getText().equals("Add Information to trial")){
                    submitBTN.setText("Close Window");
                    System.out.println("add method to send pat info");
                    
                } */
                else if (submitBTN.getText().equals("Close Window"))
                    if (infoSent){
                        dispose();
                    } else {
                        System.out.println("Check patient info has been sent");   
                    }
            }
        });
    }
            
    
    private void loadFormData(){
        patInformation = new HashMap<String, String>();
        
        patInformation.put("InvestigationDate", invDate.getText());
        
        for (int i=0; i<(textFields.size());i++){
            patInformation.put(fieldLabels[i], textFields.get(i).getText());
        }
    }
    
    public HashMap<String, String> getPatientInfo(){
        return patInformation;
    } 
           
}
