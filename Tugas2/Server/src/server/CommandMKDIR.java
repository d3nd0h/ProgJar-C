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
public class CommandMKDIR {
    private String request, cur_dir;
    
    CommandMKDIR(String request, String cur_dir) {
        this.request = request;
        this.cur_dir = cur_dir;
    }
    
    public void exec(OutputStream os) throws IOException {
        File dir = new File(this.cur_dir);
        String respond = null;
        boolean error = false;
        String[] temp = this.request.split("\\s+");
        
        if (temp.length == 1) {
            error = true;
            respond = "mkdir needs an argument(folder_name)\n";
        }
        else if (temp.length > 2) {
            error = true;
            respond = "too many arguments\n";
        }
        else {
            String args = temp[1].trim();
            File target_dir;
            if (args.contains("/")) {
                target_dir = new File(args);
            }
            else {
                target_dir = new File(this.cur_dir + '/' + args);
            }

            boolean flag = true;
            if (!target_dir.exists()) {
                flag = target_dir.mkdir();
            }
            else {
                error = true;
                respond = "Folder exists!\n";
            }

            if (!flag) {
                respond = "Failed to create directory, please try again.\n";
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
    }
}
