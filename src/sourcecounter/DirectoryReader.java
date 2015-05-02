/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sourcecounter;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

/**
 *
 * @author jorge
 */
public class DirectoryReader {
    ArrayList<String> listArchivos, listDirectorios;
    File actual;
    boolean ready;
    public DirectoryReader(String s){
        listArchivos=new ArrayList<String>();
        listDirectorios=new ArrayList<String>();
        actual=new File(s);
        ready=actual.exists();
        
    }
    
    public ArrayList<String> getDirs(){
        File[] lar;
        // muestra directorios en base a un filtro SoloDirs
        lar=actual.listFiles(new SoloDirs());
        for (int i=0; i<lar.length; i++){
            listArchivos.add(lar[i].getName());
        }
        return listArchivos;
    }
    
    
    
}

class SoloDirs implements FileFilter{
    public boolean accept(File archi){
        boolean ok=archi.isDirectory() && !archi.isHidden();
        return ok;
    }
}