package application;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;

public class ClientGUI {
    
    private static Client client;
    
    public static JFrame MainWindow = new JFrame();
    public static String UserName = "Anonymous";
    private static JButton B_CONNECT = new JButton();
    private static JButton B_DISCONNECT = new JButton();
    private static JButton B_SEND = new JButton();    
   
    public static void main(String[] args) 
    {
        BuildMainWindow();
    }
    
    public static void Connect()
    {
        try
        {
            final int PORT = 444;
            final String HOST = "localhost";
            Socket SOCK = new Socket(HOST, PORT);
            System.out.println("You connected to: "+ HOST);
            
            client = new Client(SOCK);
            
            PrintWriter OUT = new PrintWriter(SOCK.getOutputStream());
            OUT.println("connected");
            OUT.flush();
            
            Thread X = new Thread(client);
            X.start();
            
        }
        catch(Exception e)
        {
            System.out.print(e);
            JOptionPane.showMessageDialog(null, "Server not responding.");
            System.exit(0);
        }
    }
    
    public static void BuildMainWindow()
    {
        MainWindow.setTitle("Hi Box");
        MainWindow.setSize(450, 500);
        MainWindow.setLocation(220, 180);
        MainWindow.setResizable(false);
        ConfigureMainWindow();
        MainWindow_Action();
        MainWindow.setVisible(true);
    }
    
    public static void ConfigureMainWindow()
    {
        MainWindow.setBackground(new java.awt.Color(255, 255, 255));
        MainWindow.setSize(500, 320);
        MainWindow.getContentPane().setLayout(null);
        
        B_SEND.setBackground(new java.awt.Color(0, 0, 255));
        B_SEND.setForeground(new java.awt.Color(255, 255, 255));
        B_SEND.setText("SEND");
        MainWindow.getContentPane().add(B_SEND);
        B_SEND.setBounds(250, 40, 81, 25);
        
        B_DISCONNECT.setBackground(new java.awt.Color(0, 0, 255));
        B_DISCONNECT.setForeground(new java.awt.Color(255, 255, 255));
        B_DISCONNECT.setText("DISCONNECT");
        MainWindow.getContentPane().add(B_DISCONNECT);
        B_DISCONNECT.setBounds(10, 40, 110, 25);
        
        B_CONNECT.setBackground(new java.awt.Color(0, 0, 255));
        B_CONNECT.setForeground(new java.awt.Color(255, 255, 255));
        B_CONNECT.setText("CONNECT");
        B_CONNECT.setToolTipText("");
        MainWindow.getContentPane().add(B_CONNECT);
        B_CONNECT.setBounds(130, 40, 110, 25);
    }
    
    public static void MainWindow_Action()
    {
        B_SEND.addActionListener(
                new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ACTION_B_SEND();
            }
        }
        );
        
        B_DISCONNECT.addActionListener(
                new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ACTION_B_DISCONNECT();
            }
        }
        );
        
        B_CONNECT.addActionListener(
                new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Connect();
            }
        }
        );
    }
    
    public static void ACTION_B_SEND()
    {
        client.SEND("++xyz");
    }
    
    public static void ACTION_B_DISCONNECT()
    {
        try
        {
            client.DISCONNECT();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
