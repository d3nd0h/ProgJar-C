/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fendy
 */
public class ClientImpl {
    
    public void start() {
        try {
            
            Scanner scanner = new Scanner(System.in);
            String test = scanner.nextLine();
            
            while (!test.contentEquals("exit")) {
                Socket socket = new Socket("localhost", 8888);
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();
                os.write(test.getBytes());
                os.flush();
                
                while (true) {
                    byte[] buf = new byte[10];
                    int len = is.read(buf);
                    if(len == -1){
                        break;
                    }
                    System.out.print(new String(buf));
                }
                
                test = scanner.nextLine();
                os.close();
                is.close();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
