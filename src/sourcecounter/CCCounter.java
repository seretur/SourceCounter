/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sourcecounter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jorge
 */
public class CCCounter {
    int cc;
    public CCCounter(){
        cc=1;
    }
    /**
     * 
     * @param fuente Java Source code to measure
     * @return Cyclomatic Complexity of the fuente
     */
    public int getCC(String fuente){
      cc=1;
      
           Pattern patif;
           
           patif = Pattern.compile("if |else |case |default |catch|/&/& |for |//?");
           Matcher matif=patif.matcher(fuente);
           while (matif.find()){
               cc++;
           }  
      return cc;             
    }
    
}
