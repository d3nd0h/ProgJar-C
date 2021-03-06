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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fendy
 */
public class ServerImpl {
    private String cur_dir;
    private int port;
    
    ServerImpl (String base_path, int port) {
        this.cur_dir = base_path;
        this.port = port;
    }
    
    public void start() {
        File dir = new File(this.cur_dir);
        
        try {
            try (ServerSocket serversock = new ServerSocket(this.port)) {
                System.out.println("Server started listening on " + this.port);
                while (true) {
                    Socket socket = serversock.accept();
                    //System.out.println("Client connected ...");
                    InputStream is = socket.getInputStream();
                    OutputStream os = socket.getOutputStream();
                    
                    byte[] buf = new byte[1024];
                    int len = is.read(buf);
                    String request = new String(buf);
                    String[] temp = request.split("\\s+");
                    String cmd = temp[0].trim();
                    
                    boolean error = false;
                    String respond = "";
                    if (cmd.equals("ls")) {
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
                    }
                    else if (cmd.equals("cd")) {
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
                    }
                    else if (cmd.equals("mkdir")) {
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
                    }
                    else if (cmd.equals("pwd")) {
                        if (temp.length > 1) {
                            error = true;
                            respond = "pwd takes no argument\n";
                        }
                        else {
                            respond = this.cur_dir + '\n';
                        }
                    }
                    else {
                        error = true;
                        respond = "command not found\n";
                    }
                    
                    os.write(respond.getBytes());
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
