/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fendy
 */
public class ServerImpl {
    private int port;
    private String default_path;
    
    ServerImpl(String default_path, int port) {
        this.default_path = default_path;
        this.port = port;
    }
    
    public void start() {
        try {
            ServerSocket server = new ServerSocket(this.port);
            System.out.println("Server started listening on " + this.port);
            
            while(true) {
                Socket socket = server.accept();
                ClientThread client = new ClientThread(socket, this.default_path);
                Thread client_thread = new Thread(client);
                client_thread.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
