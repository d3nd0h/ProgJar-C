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
            while(true) {
                byte[] buf = new byte[1];
                this.is.read(buf);
                int flag = Integer.parseInt(new String(buf));
                this.is.read(buf);

                String response = new String();
                while(!(new String(buf).equals("\n"))) {
                    response += new String(buf);
                    this.is.read(buf);
                }

                int len = Integer.parseInt(response);
                response = new String();
                for(int i=0;i<len;i++){
                    this.is.read(buf);
                    response += new String(buf);
                }
                System.out.print(response);
            }
        } catch (IOException ex) {
            Logger.getLogger(RecieverThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
