/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uroflowmetrygui.GUI;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author mikej
 */
public class MainWindow extends JFrame {
    
    public int screenWidth;
    public int screenHeight;
    private Toolkit tk;
    
    private JPanel menuBar;
    private JComboBox<String> portList;
    private JButton investButton;
    private JButton connectButton;
    private JButton calibrateButton;
   
    
    
    public MainWindow(){
        super();
        setTitle("Urodynamics Without Borders - Uroflowmetry");
        setScreenDimensions();
        setLayout(new BorderLayout());
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Add the top menu bar to the window
        createMenuBar();
        
        
        
       
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

    }
    
    
    
    
}
