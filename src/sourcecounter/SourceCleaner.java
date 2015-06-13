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

/**
 *
 * @author jorge
 */
public class SourceCleaner {
    // lc: lineas comentadas. scl: líneas de código fuente
    int lc,scl;
    String cleaned, comments;
    
    public SourceCleaner(){
        lc=0;
        scl=0;
        cleaned="";
        comments="";
    }
    /**
     * Separate comments lines and source lines
     * @param sror Original Source Code
     * @return 
     */
    
    public boolean separate(String sror){
        boolean resp=false;
        String[] lineas=sror.split("\\n");
  //      System.out.println("Líneas del método: "+lineas.length);
        for (String linea : lineas) {
            String l = linea.trim();
            l=l+"\n";
            if (l.contains("//") || l.startsWith("/*") || l.startsWith("*")){
                comments=comments.concat(l);
                lc++;
                //System.out.println(l);
            } else {
                if (l.length()>0){
                    // delete text within quotes
                    l=l.replaceAll("\"[^\"]+\"","");
                    l=l.replaceAll("'[^\"]+'","");
                    l=l.replaceAll("`[^\"]+`","");
                    // remove comments at the end of line
                    int slashPos=cleaned.lastIndexOf("//");
                    if (slashPos>0){
                        l=l.substring(0,slashPos);
                        lc++;
                    }
                    
                    cleaned=cleaned.concat(l);
                    
                    scl++;
                }
            }
        }
        if (cleaned.length()>0){
            resp=true;
        }
        return resp;
    }
 
   /**
    * resets Cleaner attributes
    */
    public void reset(){
        lc=0;
        cleaned="";
        comments="";
    }
    
    /**
     * return sourcecode without comments
    */
    public String getCleaned(){
        return cleaned;
    }
    /**
     * 
     * @return String with all commented lines
     */
    
    public String getCommentingLines(){
        return comments;
    }
    
    //getter for Commenting Lines Number (CL)
    
    public int getCLNumber(){
        return lc;
    }
    
    //getter for Source Code Number (CL)
    public int getSCL(){
        return scl;
    }
    
}
