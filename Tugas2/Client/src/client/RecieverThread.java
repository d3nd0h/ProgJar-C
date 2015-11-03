/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
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
                System.out.println(len);
                response = new String();
                for(int i=0;i<len;i++){
                    this.is.read(buf);
                    response += new String(buf);
                }
                
                if(flag == 1) {
                    String filename = response;
                    response = new String();
                    this.is.read(buf);
                    while(!(new String(buf).equals("\n"))) {
                        response += new String(buf);
                        this.is.read(buf);
                    }
                    
                    buf = new byte[1024];
                    long file_size = Long.parseLong(response);
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filename));
                    long size_received = 0;
                    while(size_received < file_size) {
                        size_received += is.read(buf,0,buf.length);
                        bos.write(buf);
                    }
                    bos.close();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(RecieverThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
