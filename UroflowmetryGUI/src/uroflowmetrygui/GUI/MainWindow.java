/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uroflowmetrygui.GUI;

import com.fazecast.jSerialComm.SerialPort;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import uroflowmetrygui.IO.AppSettings;
import uroflowmetrygui.IO.DatasetReader;
import uroflowmetrygui.IO.DatasetWriter;
import uroflowmetrygui.IO.SaveInvestigation;

/**
 *
 * @author mikej
 */
public class MainWindow extends JFrame {
    
    public int screenWidth;
    public int screenHeight;
    private Toolkit tk;
    private JPanel mainContent;
    private String settingsFilePath;
    private String dataDirectoryPath;
   
    
    private JPanel menuBar;
    private JComboBox<String> portList;
    private static SerialPort chosenPort;
    private JButton investButton;
    private JButton connectButton;
    private JButton calibrateButton;
    private JButton importDataButton;
    
    public boolean recordData;
    
    private HashMap<String, String> patientInformation;
   
    private UroflowTrace uroflowTrace;
    private CalibrateTrace calibrateTrace;
    
    //private SaveInvestigation dataWriter;
    
    
    public MainWindow(){
        super();
        setTitle("Urodynamics Without Borders - Uroflowmetry");
        setScreenDimensions();
        setLayout(new BorderLayout());
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        loadAppSettings();
        
        // Add the top menu bar to the window
        createMenuBar();
        
        // Uroflow plot added to window
        createUroflowTrace();        
        
    }
    
    private void setScreenDimensions(){
        tk = Toolkit.getDefaultToolkit();
        screenWidth = (int) ((int) tk.getScreenSize().getWidth()*0.9);
        screenHeight = (int) ((int) tk.getScreenSize().getHeight()*0.9);
        setSize(screenWidth,screenHeight);
    }
    
    private void loadAppSettings(){
        AppSettings settings = new AppSettings();
        
        this.settingsFilePath = settings.settingsFilePath;
        this.dataDirectoryPath = settings.dataDirectoryPath;
    }
    
    private void createMenuBar(){
        
        // Create menubar objects
        menuBar = new JPanel();
        
        // Menu buttons
        investButton = new JButton("Investigation");
        importDataButton = new JButton("Import Data");
        calibrateButton = new JButton("Calibrate");
        portList = new JComboBox<String>();
	connectButton = new JButton("Connect");
       
        menuBar.add(investButton);
        menuBar.add(importDataButton);
        menuBar.add(calibrateButton);
	menuBar.add(portList);
	menuBar.add(connectButton);
        
        add(menuBar, BorderLayout.NORTH);
        
        
        // butttons enabled at start
        investButton.setEnabled(true);
        importDataButton.setEnabled(true);
        calibrateButton.setEnabled(true);
        connectButton.setEnabled(false);
        
        // Format the menubar object functions
        setInvestBtnFunction();
        setCalibrateButtonFunction();
        setConnectButtonFunction();
        setImportDataButtonFunction();
        
        
        // Get available serial ports and populate dropdown
        SerialPort[] portNames = SerialPort.getCommPorts();
	for (int i = 0; i < portNames.length; i++)
            portList.addItem(portNames[i].getSystemPortName());
        
    }
    
    private void setImportDataButtonFunction(){
        
        importDataButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                if (importDataButton.getText().equals("Import Data")){
                    
                    // Manage Buttons
                    investButton.setEnabled(false);
                    importDataButton.setEnabled(true);
                    calibrateButton.setEnabled(false);
                    connectButton.setEnabled(false);
                    importDataButton.setText("Exit Trial");
                    
                    
                    JFileChooser f = new JFileChooser(new File(dataDirectoryPath));
                    f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
                    f.showSaveDialog(null);
                    
                    String trialDir = f.getSelectedFile().toString();
                    
                    try {
                        DatasetReader dr = new DatasetReader(trialDir);
                        uroflowTrace.loadDatasets(dr.getVolumeData(), dr.getFlowrateData());
    
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    
                } else if (importDataButton.getText().equals("Exit Trial")){
                    
                    // Manage Buttons
                    investButton.setEnabled(true);
                    importDataButton.setEnabled(true);
                    calibrateButton.setEnabled(true);
                    connectButton.setEnabled(false);
                    importDataButton.setText("Import Data");
                }
                
            }
        }
        );
    }
    
    private void setInvestBtnFunction(){
        
        // Create the data writer for save button
        //dataWriter = new SaveInvestigation();
        
        investButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                if (investButton.getText().equals("Investigation")){
                    
                    // Manage Buttons
                    investButton.setEnabled(false);
                    importDataButton.setEnabled(false);
                    calibrateButton.setEnabled(false);
                    //connectButton.setEnabled(true);
                    investButton.setText("Start");
                    
                    // Logic for adding pat deets
                    PatientDataForm pd = new PatientDataForm();
        
                    pd.submitBTN.addActionListener(new ActionListener(){
                        @Override public void actionPerformed(ActionEvent arg0) {
                            if (pd.submitBTN.getText().equals("Add Information to trial")){

                                patientInformation = pd.getPatientInfo();

                                System.out.println(patientInformation.get("FirstName"));

                                pd.submitBTN.setText("Close Window");
                                System.out.println("add method to send pat info");
                                System.out.println("Closing window");
                                pd.infoSent = true;
                                
                                connectButton.setEnabled(true);
                                
                                uroflowTrace.updateChartTitle(patientInformation.get("FirstName"),
                                        patientInformation.get("LastName"),
                                        patientInformation.get("InvestigationDate"));
                            } else {}
                        }
                    });
                    
                    // logic
                    System.out.println("Connect to uroflowmetry device");
                    
                    
                    
                    
                }
                else if (investButton.getText().equals("Start")){
                    // Manage Buttons
                    //investButton.setEnabled(true);
                    importDataButton.setEnabled(false);
                    calibrateButton.setEnabled(false);
                    //connectButton.setEnabled(true);
                    
                    
                    // Check if device connected
                    if (connectButton.getText().equals("Disconnect")){
                        recordData=true;
                        System.out.println("Investigation started");
                        connectButton.setEnabled(false);
                        investButton.setText("Stop");
                        
                    } else if (connectButton.getText().equals("Connect")) {
                        System.out.println("Connect to device!");
                    }
                      
                }
                else if (investButton.getText().equals("Stop")){
                    recordData=false;
                    
                    // Manage Buttons
                    //investButton.setEnabled(true);
                    importDataButton.setEnabled(false);
                    calibrateButton.setEnabled(false);
                    connectButton.setEnabled(true);
                    
                    System.out.println("Investigation stoppped");
                    
                    
                    investButton.setText("Save data");
                    
                    //dataWriter.loadInvestigationData(uroflowTrace.getDataset());
                }
                else if (investButton.getText().equals("Save data")){
                    
                    // butttons enabled at start
                    investButton.setEnabled(true);
                    importDataButton.setEnabled(true);
                    calibrateButton.setEnabled(true);
                    connectButton.setEnabled(false);
                    investButton.setText("Investigation");
                    
    
                    System.out.println("Investigation data saved");
                    
                    try {
                        DatasetWriter dw = new DatasetWriter((dataDirectoryPath+"\\"+patientInformation.get("TrialRef")), patientInformation.get("TrialRef"));
                        dw.createTrialMetadataFile(patientInformation);
                        dw.createTrialCSVFile(uroflowTrace.volumeSeries, uroflowTrace.flowrateSeries);
                        System.out.println("Trial dataset saved to: "+Paths.get("Data",patientInformation.get("TrialRef").toString()));
                    } catch (IOException ex) {
                        Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }     
            }
        }
        );
    }
    
    private void setCalibrateButtonFunction(){
        
        calibrateButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Begin calibration process");
                
                createCalibrateTrace(); // Change plot
                
                connectButton.setEnabled(true);
                investButton.setEnabled(false);
                calibrateButton.setEnabled(false);
                
                importDataButton.setEnabled(false);
            }
        }
        );
    }
    
    private void setConnectButtonFunction(){
        connectButton.addActionListener(new ActionListener(){
            @Override public void actionPerformed(ActionEvent arg0) {
		if(connectButton.getText().equals("Connect")) {
                    investButton.setEnabled(true);
                 
                    
                    importDataButton.setEnabled(false);
                    
                    
                    // attempt to connect to the serial port
                    chosenPort = SerialPort.getCommPort(portList.getSelectedItem().toString());
                    chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
                    if(chosenPort.openPort()) {
			connectButton.setText("Disconnect");
			portList.setEnabled(false);
                    }
					
                    // create a new thread that listens for incoming text and populates the graph
                    Thread thread = new Thread(){
                        @Override public void run() {
                            Scanner scanner = new Scanner(chosenPort.getInputStream());
                            while(scanner.hasNextLine()) {
				try {
                                    if (recordData){
                                        String line = scanner.nextLine();
                                        String[] datapoint = line.split(",");
                                        //int number = Integer.parseInt(line);
                                        float x = Float.parseFloat(datapoint[0]);
                                        float y = Float.parseFloat(datapoint[1]);
                                    
                                        uroflowTrace.addData(x, y);
                                        //uroflowTrace.addData(x, convFactors.convertRawData(y));
                                    } else {
                                    } 
				} catch(Exception e) {}
                            }
                            scanner.close();
			}
                    };
                    thread.start();
		} else {
                    // disconnect from the serial port
                    chosenPort.closePort();
                    portList.setEnabled(true);
                    connectButton.setText("Connect");
                    
                    importDataButton.setEnabled(true);
                    
		}
            }
	});
    }
           
    private void createUroflowTrace(){
        uroflowTrace = new UroflowTrace(screenWidth, screenHeight);
        add(uroflowTrace, BorderLayout.CENTER);
    }
    
    private void createCalibrateTrace(){
        calibrateTrace = new CalibrateTrace(screenWidth, screenHeight);
        add(calibrateTrace, BorderLayout.CENTER);
    }

     
}
