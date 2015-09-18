package server.client.test;

import java.io.*;
import java.net.*;
 
public class SOK_1_CLIENT
{
 
        public static void main(String[] args) throws Exception
        {
            SOK_1_CLIENT CLIENT = new SOK_1_CLIENT();
            CLIENT.run();
        }
       
        public void run() throws Exception
        {
            Socket SOCK = new Socket("localhost",444);
            PrintStream PS = new PrintStream(SOCK.getOutputStream());
            PS.println("Hello to server from client");
           
            InputStreamReader IR = new InputStreamReader(SOCK.getInputStream());
            BufferedReader BR = new BufferedReader(IR);
           
            String MESSAGE = BR.readLine();
            System.out.println(MESSAGE);
        }
    }
