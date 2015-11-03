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
                
                switch (cmd) {
                    case "ls":
                        CommandLS ls = new CommandLS(request, this.cur_dir);
                        ls.exec(this.os);
                        break;
                    case "cd":
                        CommandCD cd = new CommandCD(request, this.cur_dir);
                        this.cur_dir = cd.exec(this.os);
                        break;
                    case "mkdir":
                        CommandMKDIR mkdir = new CommandMKDIR(request, this.cur_dir);
                        mkdir.exec(this.os);
                        break;
                    case "pwd":
                        CommandPWD pwd = new CommandPWD(request, this.cur_dir);
                        pwd.exec(this.os);
                        break;
                    case "upload":
                        CommandUPLOAD upload = new CommandUPLOAD(request, this.cur_dir);
                        upload.exec(this.socket,this.os);
                        break;
                    case "download":
                        CommandDOWNLOAD download = new CommandDOWNLOAD(request, this.cur_dir);
                        download.exec(this.os);
                        break;
                    default:
                        String respond = "Error: command not found\n";
                        String len = Integer.toString(respond.length());
                        len += "\n";
                        
                        this.os.write("0".getBytes());
                        this.os.flush();
                        this.os.write(len.getBytes());
                        this.os.flush();
                        this.os.write(respond.getBytes());
                        this.os.flush();
                        break;
                }
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
        } catch (Exception ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
