/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fendy
 */
public class ServerImpl {
    public void start(){
        try {
            
            int port = 8888;
            try (ServerSocket serversock = new ServerSocket(port)) {
                System.out.println("Server started listening on " + port);
                while (true) {
                    Socket socket = serversock.accept();
                    //System.out.println("Client connected ...");
                    InputStream is = socket.getInputStream();
                    OutputStream os = socket.getOutputStream();
                    
                    byte[] buf = new byte[1024];
                    int len = is.read(buf);
                    //System.out.println("Client said: " + new String(buf));
                    String request = new String(buf);
                    String[] temp = request.split("\\s+");
                    /*
                    for (int i=0;i<temp.length;i++) {
                        System.out.println(temp[i]);
                    }
                    */
                    String cmd = temp[0].trim();
                    
                    boolean error = false;
                    String respond;
                    if (cmd.equals("ls")) {
                        if (temp.length == 1) {
                            error = true;
                            respond = "ls need an argument(path)";
                        }
                        else if (temp.length > 2) {
                            error = true;
                            respond = "too many arguments";
                        }
                        else {
                            //return list of files
                            respond = "ls ok";
                        }
                    }
                    else if (cmd.equals("cd")) {
                        if (temp.length > 2) {
                            error = true;
                            respond = "too many arguments";
                        }
                        else {
                            //do cd;
                            respond = "cd ok";
                        }
                    }
                    else if (cmd.equals("mkdir")) {
                        if (temp.length == 1) {
                            error = true;
                            respond = "mkdir need an argument(folder_name)";
                        }
                        else if (temp.length > 2) {
                            error = true;
                            respond = "too many arguments";
                        }
                        else {
                            //do cd;
                            respond = "mkdir ok";
                        }
                    }
                    else if (cmd.equals("pwd")) {
                        if (temp.length > 1) {
                            error = true;
                            respond = "pwd takes no argument";
                        }
                        else {
                            respond = "pwd ok";
                        }
                    }
                    else {
                        error = true;
                        respond = "command not found";
                    }
                    
                    respond += ("\n");
                    
                    os.write(respond.getBytes());
                    os.flush();
                    
                    os.close();
                    is.close();
                    socket.close();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
