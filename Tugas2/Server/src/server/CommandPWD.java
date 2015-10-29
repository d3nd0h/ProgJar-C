/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Fendy
 */
public class CommandPWD {
    private String request, cur_dir;
    
    CommandPWD(String request, String cur_dir) {
        this.request = request;
        this.cur_dir = cur_dir;
    }
    
    public void exec(OutputStream os) throws IOException {
        File dir = new File(this.cur_dir);
        String respond = null;
        boolean error = false;
        String[] temp = this.request.split("\\s+");
        
        if (temp.length > 1) {
            error = true;
            respond = "pwd takes no argument\n";
        }
        else {
            respond = this.cur_dir + '\n';
        }
        
        if(respond != null) {
            if(error) {
                respond = "Error: " + respond;
            }
            String len = Integer.toString(respond.length());
            len = len + "\n";
            os.write(("0".getBytes()));
            os.flush();
            os.write(len.getBytes());
            os.flush();
            os.write(respond.getBytes());
            os.flush();
        }
    }
}
