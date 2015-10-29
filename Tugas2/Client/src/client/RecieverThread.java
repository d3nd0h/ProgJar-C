/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fendy
 */
public class RecieverThread implements Runnable{
    private InputStream is;
    
    RecieverThread(InputStream is) {
        this.is = is;
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                byte[] buf = new byte[10];
                int len = this.is.read(buf);
                if(len == -1){
                    break;
                }
                System.out.print(new String(buf));
            }
        } catch (IOException ex) {
            Logger.getLogger(RecieverThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
