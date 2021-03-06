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
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
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
    
    private JPanel menuBar;
    private JComboBox<String> portList;
    private static SerialPort chosenPort;
    private JButton investButton;
    private JButton connectButton;
    private JButton calibrateButton;
    
    public boolean recordData;
   
    private UroflowTrace uroflowTrace;
    private CalibrateTrace calibrateTrace;
    
    private SaveInvestigation dataWriter;
    
    
    public MainWindow(){
        super();
        setTitle("Urodynamics Without Borders - Uroflowmetry");
        setScreenDimensions();
        setLayout(new BorderLayout());
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
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
    
    private void createMenuBar(){
        
        // Create menubar objects
        menuBar = new JPanel();
        
        investButton = new JButton("Investigation");
        calibrateButton = new JButton("Calibrate");
        portList = new JComboBox<String>();
	connectButton = new JButton("Connect");
        
        menuBar.add(investButton);
        menuBar.add(calibrateButton);
	menuBar.add(portList);
	menuBar.add(connectButton);
        add(menuBar, BorderLayout.NORTH);
        
        
        // Format the menubar objects
        connectButton.setEnabled(false);
        setInvestBtnFunction();
        setCalibrateButtonFunction();
        setConnectButtonFunction();
        
        // Get available serial ports and populate dropdown
        SerialPort[] portNames = SerialPort.getCommPorts();
	for (int i = 0; i < portNames.length; i++)
            portList.addItem(portNames[i].getSystemPortName());
        

    }
    
    private void setInvestBtnFunction(){
        
        // Create the data writer for save button
        //dataWriter = new SaveInvestigation();
        
        investButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                if (investButton.getText().equals("Investigation")){
                    
                    System.out.println("Connect to uroflowmetry device");
                    investButton.setText("Connect to Device");
                    investButton.setEnabled(false);
                    connectButton.setEnabled(true);
                    calibrateButton.setEnabled(false);
                }
                else if (investButton.getText().equals("Start")){
                    System.out.println("Investigation started");
                    connectButton.setEnabled(false);
                    recordData=true;
                    investButton.setText("Stop");
                }
                else if (investButton.getText().equals("Stop")){
                    System.out.println("Investigation stoppped");
                    connectButton.setEnabled(true);
                    recordData=false;
                    investButton.setText("Save data");
                    
                    dataWriter.loadInvestigationData(uroflowTrace.getDataset());
                }
                else if (investButton.getText().equals("Save data")){
                    System.out.println("Investigation data saved");
                    investButton.setText("Investigation");
                    calibrateButton.setEnabled(true);

                    dataWriter.saveInvestigation("file-to-save-to.csv");
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
            }
        }
        );
    }
    
    private void setConnectButtonFunction(){
        connectButton.addActionListener(new ActionListener(){
            @Override public void actionPerformed(ActionEvent arg0) {
		if(connectButton.getText().equals("Connect")) {
                    investButton.setEnabled(true);
                    investButton.setText("Start");
                    
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
