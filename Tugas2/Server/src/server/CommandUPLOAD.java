/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author PRANAWA
 */
class CommandUPLOAD {
    private String request, cur_dir;
    
    CommandUPLOAD(String request, String cur_dir){
        this.request=request;
        this.cur_dir=cur_dir;
    }

    void exec(Socket socket, OutputStream os) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        FileOutputStream fos = null;
        byte [] buffer = new byte[1];
        String respond=null;
        boolean error=false;
        String[] temp = this.request.split("\\s+");
        
        if (temp.length == 1) {
            error = true;
            respond = "upload needs an argument(folder_name)\n";
        }
        else if (temp.length > 2) {
            error = true;
            respond = "too many arguments\n";
        }
        else {
            // 1. Read file name.
            Object o = ois.readObject();

            if (o instanceof String) {
                fos = new FileOutputStream(new File(this.cur_dir, o.toString()));
            } else {
                //throwException("Something is wrong");
                respond="Something is wrong\n";
            }

            // 2. Read file to the end.
            Integer bytesRead = 0;

            do {
                o = ois.readObject();

                if (!(o instanceof Integer)) {
                    //throwException("Something is wrong");
                    respond="Something is wrong\n";
                }

                bytesRead = (Integer)o;

                o = ois.readObject();

                if (!(o instanceof byte[])) {
                    //throwException("Something is wrong");
                    respond="Something is wrong\n";
                }

                buffer = (byte[])o;

                // 3. Write data to output file.
                fos.write(buffer, 0, bytesRead);

            } while (bytesRead == 1);

            System.out.println("File transfer success");

            fos.close();

            ois.close();
            oos.close();
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

    public static void throwException(String message) throws Exception {
        throw new Exception(message);
    }
}