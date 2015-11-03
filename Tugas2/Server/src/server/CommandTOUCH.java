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
 * @author Inya
 */
public class CommandTOUCH {
    private String request, cur_dir;
    
    CommandTOUCH(String request, String cur_dir) {
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
            respond = "touch needs an argument(file_name)\n";
        }
        else if (temp.length > 2) {
            error = true;
            respond = "too many arguments\n";
        }
        else {
            String args = temp[1].trim();
            File target_file;
            
            target_file = new File(this.cur_dir + args) ;
            System.out.println(this.cur_dir + args);
            System.out.println("Konstruksi file berhasil");
    
    // jika file tidak ditemukan, maka error
    if (target_file == null) {
      throw new RuntimeException();
    }
    // jika file tidak ada, maka membuat file baru
    if (!target_file.exists()) {
      try {
        target_file.createNewFile();
        System.out.println("File tidak ditemukan, membuat file baru di " + this.cur_dir + args);
      } catch (IOException ex) {}
    }
            
           
        
        }
    }
}
