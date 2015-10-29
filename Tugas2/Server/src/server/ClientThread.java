/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fendy
 */
public class ClientThread implements Runnable{
    private String cur_dir;
    private Socket socket;
    private InputStream is;
    private OutputStream os;
    
    ClientThread (Socket socket, String default_path) throws IOException {
        this.socket = socket;
        this.is = this.socket.getInputStream();
        this.os = this.socket.getOutputStream();
        this.cur_dir = default_path;
    }
    
    private String getRequest() throws IOException {
        byte[] buf = new byte[1];
        this.is.read(buf);
        
        String request = new String();
        while(!(new String(buf).equals("\n"))) {
            request += new String(buf);
            this.is.read(buf);
        }
        
        int len = Integer.parseInt(request);
        request = new String();
        for(int i=0;i<len;i++){
            this.is.read(buf);
            request += new String(buf);
        }
        
        return request;
    }
    
    @Override
    public void run() {
        File dir = new File(this.cur_dir);
        
        try {
            while (true) {
                String request = getRequest();
                String[] temp = request.split("\\s+");
                String cmd = temp[0].trim();
                
                boolean error = false;
                String respond = "";
                switch (cmd) {
                    case "ls":
                        if (temp.length > 2) {
                            error = true;
                            respond = "too many arguments";
                        }
                        else {
                            //return list of files
                            respond = "";
                            String[] files = null;
                            if (temp.length == 1)
                                files = dir.list();
                            else {
                                String args = temp[1].trim();
                                File custom_dir = new File(args);
                                if (!custom_dir.isDirectory()) {
                                    error = true;
                                    respond = "Folder doesn\'t exists!\n";
                                }
                                else {
                                    files = custom_dir.list();
                                }
                            }
                            
                            if (!error) {
                                for (String file: files) {
                                    respond += file + ('\n');
                                }
                            }
                        }
                        break;
                    case "cd":
                        if (temp.length > 2) {
                            error = true;
                            respond = "too many arguments\n";
                        }
                        else {
                            if (temp.length == 1) {
                                int idx = this.cur_dir.lastIndexOf('/');
                                this.cur_dir = this.cur_dir.substring(0,idx);
                                dir = new File(this.cur_dir);
                                System.out.println(this.cur_dir);
                            }
                            else {
                                String args = temp[1].trim();
                                if (args.equals("..")) {
                                    int idx = this.cur_dir.lastIndexOf('/');
                                    this.cur_dir = this.cur_dir.substring(0,idx);
                                    dir = new File(this.cur_dir);
                                    System.out.println(this.cur_dir);
                                } else if (!args.equals(".")) {
                                    args = this.cur_dir + '/' + args;
                                    System.out.println("cd " + args);
                                    File target_dir = new File(args);
                                    if (!target_dir.isDirectory()) {
                                        error = true;
                                        respond = "Folder doesn\'t exists!\n";
                                    }
                                    else {
                                        this.cur_dir = args;
                                        dir = new File(this.cur_dir);
                                    }
                                }
                            }
                        }
                        break;
                    case "mkdir":
                        if (temp.length == 1) {
                            error = true;
                            respond = "mkdir need an argument(folder_name)\n";
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
                                respond = "Folder exists!";
                            }
                            
                            if (!flag) {
                                respond = "Failed to create directory, please try again.\n";
                            }
                        }
                        break;
                    case "pwd":
                        if (temp.length > 1) {
                            error = true;
                        respond = "pwd takes no argument\n";
                        }
                        else {
                            respond = this.cur_dir + '\n';
                        }
                        break;
                    default:
                        error = true;
                        respond = "command not found\n";
                        break;
                }
                
                if(error) {
                    respond = "Error: " + respond; 
                }
                this.os.write(respond.getBytes());
                this.os.flush();
                
            }
        } catch (IOException ex) {
            try {
                this.os.close();
                this.is.close();
                this.socket.close();
                System.out.println("Client closed");
            } catch (IOException ex1) {
                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
}
