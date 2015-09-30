/*
 * Copyright (C) 2015 jorge Ramirez (seretur)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
           String paths="if|for|while|case|catch|\\|\\|\\?|&&";
           
           patif = Pattern.compile(paths);
           Matcher matif=patif.matcher(fuente);
           int fd=0;
           while (matif.find()){
              // System.out.println(matif.group(fd));
               cc++;
               fd++;
           }  
      return cc;             
    }
    
}
