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
public class CommandCD {
    private String request, cur_dir;
    
    CommandCD(String request, String cur_dir) {
        this.request = request;
        this.cur_dir = cur_dir;
    }
    
    public String exec(OutputStream os) throws IOException {
        File dir = new File(this.cur_dir);
        String respond = null;
        boolean error = false;
        String[] temp = this.request.split("\\s+");
        
        if (temp.length > 2) {
            error = true;
            respond = "too many arguments\n";
        }
        else {
            String args = temp[1].trim();
            if (args.equals("..") || temp.length == 1) {
                int idx = this.cur_dir.lastIndexOf('/',this.cur_dir.lastIndexOf('/')-1);
                if(idx != -1) {
                    this.cur_dir = this.cur_dir.substring(0,idx+1);
                    dir = new File(this.cur_dir);
                }
            } else if (!args.equals(".")) {
                if(!args.contains(":/"))
                    args = this.cur_dir + args + '/';
                File target_dir = new File(args);
                if (!target_dir.isDirectory()) {
                    error = true;
                    respond = "Folder doesn\'t exists!\n";
                }
                else {
                    this.cur_dir = args;
                }
            }
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
        
        return this.cur_dir;
    }
}
