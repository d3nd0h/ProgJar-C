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
                    System.out.println("Client connected ...");
                    InputStream is = socket.getInputStream();
                    OutputStream os = socket.getOutputStream();
                    
                    byte[] buf = new byte[1024];
                    int len = is.read(buf);
                    System.out.println("Client said: " + new String(buf));
                    
                    os.write(buf);
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
